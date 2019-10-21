package cn.qd.peiwen.demo.serialport.mainboard;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.lang.ref.WeakReference;

import cn.qd.peiwen.demo.serialport.mainboard.listener.MainBoardListener;
import cn.qd.peiwen.demo.serialport.mainboard.tools.MainBoardTools;
import cn.qd.peiwen.logger.PWLogger;
import cn.qd.peiwen.pwtools.ByteUtils;
import cn.qd.peiwen.pwtools.EmptyUtils;
import cn.qd.peiwen.serialport.PWSerialPort;
import cn.qd.peiwen.serialport.PWSerialPortHelper;
import cn.qd.peiwen.serialport.PWSerialPortListener;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class MainBoardManager implements PWSerialPortListener {
    private ByteBuf buffer;
    private HandlerThread thread;
    private MainBoardHandler handler;
    private PWSerialPortHelper helper;

    private boolean system = false;
    private WeakReference<MainBoardListener> listener;

    private static MainBoardManager manager;

    public static MainBoardManager getInstance() {
        if (manager == null) {
            synchronized (MainBoardManager.class) {
                if (manager == null)
                    manager = new MainBoardManager();
            }
        }
        return manager;
    }

    private MainBoardManager() {

    }

    public void init(MainBoardListener listener) {
        this.createHandler();
        this.createHelper();
        this.createBuffer();
        if (this.isReady()) {
            this.helper.open();
        }
        this.listener = new WeakReference<>(listener);
    }

    public void release() {
        this.destoryHandler();
        this.destoryHelper();
        this.destoryBuffer();
    }

    private boolean isReady() {
        if (EmptyUtils.isEmpty(this.handler)) {
            return false;
        }
        if (EmptyUtils.isEmpty(this.helper)) {
            return false;
        }
        if (EmptyUtils.isEmpty(this.buffer)) {
            return false;
        }
        return true;
    }

    private void createHelper() {
        if (EmptyUtils.isEmpty(this.helper)) {
            this.helper = new PWSerialPortHelper("MainBoard");
            this.helper.setTimeout(0);
            this.helper.setPath("/dev/ttyS4");
            this.helper.setBaudrate(9600);
            this.helper.init(this);
        }
    }

    private boolean checkHelper(PWSerialPortHelper helper) {
        return (this.isReady() && this.helper.equals(helper));
    }

    private void destoryHelper() {
        if (EmptyUtils.isNotEmpty(this.helper)) {
            this.helper.release();
            this.helper = null;
        }
    }

    private void createHandler() {
        if (EmptyUtils.isEmpty(this.thread) && EmptyUtils.isEmpty(this.handler)) {
            this.thread = new HandlerThread("FingerPrint");
            this.thread.start();
            this.handler = new MainBoardHandler(this.thread.getLooper());
        }
    }

    private void destoryHandler() {
        if (EmptyUtils.isNotEmpty(this.thread)) {
            this.thread.quitSafely();
            this.thread = null;
            this.handler = null;
        }
    }

    private void createBuffer() {
        if (EmptyUtils.isEmpty(this.buffer)) {
            this.buffer = Unpooled.buffer(4);
        }
    }

    private void destoryBuffer() {
        if (EmptyUtils.isNotEmpty(this.buffer)) {
            this.buffer.release();
            this.buffer = null;
        }
    }

    private void switchReadModel() {
        PWSerialPort.writeFile("/sys/class/gpio/gpio24/value", "1");
    }

    private void switchWriteModel() {
        PWSerialPort.writeFile("/sys/class/gpio/gpio24/value", "0");
    }

    private void sendCommand(byte[] data) {
        if (this.isReady()) {
            this.helper.write(data);
            PWLogger.e("指令发送:" + ByteUtils.bytes2HexString(data,true,", "));
        }
    }

    private boolean checkSystemType(byte system) {
        if (system == 0x01 || system == 0x02 || system == 0x04) {
            return true;
        }
        return false;
    }

    private boolean checkCommandType(byte command) {
        if (command == 0x10 || command == 0x03) {
            return true;
        }
        return false;
    }


    private void fireMainBoardReady() {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onMainBoardReady();
        }
    }

    private void fireMainBoardException() {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onMainBoardException();
        }
    }

    private void fireSystemTypeChanged(int type) {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onSystemTypeChanged(type);
        }
    }

    private void fireStateDataReceived(byte[] data) {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onStateDataReceived(data);
        }
    }

    private byte[] requestStateReply(byte[] data) {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            return this.listener.get().requestStateReply(data);
        }
        return null;
    }

    private byte[] requestCommandReply(int type) {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            return this.listener.get().requestCommandReply(type);
        }
        return null;
    }

    @Override
    public void onConnected(PWSerialPortHelper helper) {
        if (!checkHelper(helper)) {
            return;
        }
        this.buffer.clear();
        this.system = false;
        this.switchReadModel();
        this.fireMainBoardReady();
    }

    @Override
    public void onException(PWSerialPortHelper helper) {
        if (!checkHelper(helper)) {
            return;
        }
        this.fireMainBoardException();
    }

    @Override
    public void onByteReceived(PWSerialPortHelper helper, byte[] buffer) throws IOException {
        if (!checkHelper(helper)) {
            return;
        }
        this.buffer.writeBytes(buffer, 0, buffer.length);
        while (this.buffer.readableBytes() >= 2) {
            byte system = this.buffer.getByte(0);
            byte command = this.buffer.getByte(1);
            if (!this.checkSystemType(system) || !this.checkCommandType(command)) {
                if (this.ignorePackage()) {
                    continue;
                } else {
                    break;
                }
            }
            int lenth = (command == 0x10) ? 109 : 8;
            if (this.buffer.readableBytes() < lenth) {
                break;
            }
            this.buffer.markReaderIndex();
            byte[] data = new byte[lenth];
            byte model = this.buffer.getByte(2);
            this.buffer.readBytes(data, 0, lenth);
            if (!MainBoardTools.checkFrame(data)) {
                this.buffer.resetReaderIndex();
                this.buffer.skipBytes(2);
                this.buffer.discardReadBytes();
                continue;
            }
            this.buffer.discardReadBytes();
            if (!this.system) {
                this.system = true;
                this.fireSystemTypeChanged(system);
            }
            PWLogger.d("指令接收:" + ByteUtils.bytes2HexString(data, true, ", "));
            this.switchWriteModel();
            Message msg = Message.obtain();
            msg.what = command;
            if (command == 0x10) {
                msg.obj = data;
            } else {
                msg.arg1 = model & 0xFF;
            }
            this.handler.sendMessageDelayed(msg,5);
        }
    }

    private boolean ignorePackage() {
        byte[] bytes = new byte[]{0x01, 0x10};
        int index = indexOf(this.buffer, bytes);
        if (index != -1) {
            byte[] data = new byte[index];
            this.buffer.readBytes(data, 0, data.length);
            this.buffer.discardReadBytes();
            PWLogger.d("指令丢弃:" + ByteUtils.bytes2HexString(data, true, ", "));
            return true;
        }
        bytes = new byte[]{0x02, 0x10};
        index = indexOf(this.buffer, bytes);
        if (index != -1) {
            byte[] data = new byte[index];
            this.buffer.readBytes(data, 0, data.length);
            this.buffer.discardReadBytes();
            PWLogger.d("指令丢弃:" + ByteUtils.bytes2HexString(data, true, ", "));
            return true;
        }
        bytes = new byte[]{0x04, 0x10};
        index = indexOf(this.buffer, bytes);
        if (index != -1) {
            byte[] data = new byte[index];
            this.buffer.readBytes(data, 0, data.length);
            this.buffer.discardReadBytes();
            PWLogger.d("指令丢弃:" + ByteUtils.bytes2HexString(data, true, ", "));
            return true;
        }
        return false;
    }


    private int indexOf(ByteBuf haystack, byte[] needle) {
        //遍历haystack的每一个字节
        for (int i = haystack.readerIndex(); i < haystack.writerIndex(); i++) {
            int needleIndex;
            int haystackIndex = i;
            /*haystack是否出现了delimiter，注意delimiter是一个ChannelBuffer（byte[]）
            例如对于haystack="ABC\r\nDEF"，needle="\r\n"
            那么当haystackIndex=3时，找到了“\r”，此时needleIndex=0
            继续执行循环，haystackIndex++，needleIndex++，
            找到了“\n”
            至此，整个needle都匹配到了
            程序然后执行到if (needleIndex == needle.capacity())，返回结果
            */
            for (needleIndex = 0; needleIndex < needle.length; needleIndex++) {
                if (haystack.getByte(haystackIndex) != needle[needleIndex]) {
                    break;
                } else {
                    haystackIndex++;
                    if (haystackIndex == haystack.writerIndex() && needleIndex != needle.length - 1) {
                        return -1;
                    }
                }
            }

            if (needleIndex == needle.length) {
                // Found the needle from the haystack!
                return i - haystack.readerIndex();
            }
        }
        return -1;
    }

    private class MainBoardHandler extends Handler {
        public MainBoardHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x10: {
                    byte[] data = (byte[]) msg.obj;
                    byte[] reply = requestStateReply(data);
                    if (EmptyUtils.isNotEmpty(reply)) {
                        sendCommand(reply);
                        switchReadModel();
                    }
                    fireStateDataReceived(data);
                    break;
                }
                case 0x03: {
                    byte[] reply = requestCommandReply(msg.arg1);
                    if (EmptyUtils.isNotEmpty(reply)) {
                        sendCommand(reply);
                        switchReadModel();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    }
}

