package cn.qd.peiwen.demo.refriger;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.lang.ref.WeakReference;

import cn.qd.peiwen.demo.refriger.tools.LTFTools;
import cn.qd.peiwen.pwlogger.PWLogger;
import cn.qd.peiwen.pwtools.ByteUtils;
import cn.qd.peiwen.pwtools.EmptyUtils;
import cn.qd.peiwen.serialport.PWSerialPort;
import cn.qd.peiwen.serialport.PWSerialPortHelper;
import cn.qd.peiwen.serialport.PWSerialPortListener;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class LTFManager implements PWSerialPortListener {
    private ByteBuf buffer;
    private HandlerThread thread;
    private MainBoardHandler handler;
    private PWSerialPortHelper helper;

    private byte system = 0x00;
    private boolean enabled = false;
    private WeakReference<ILTFListener> listener;

    private static LTFManager manager;

    public static LTFManager getInstance() {
        if (manager == null) {
            synchronized (LTFManager.class) {
                if (manager == null)
                    manager = new LTFManager();
            }
        }
        return manager;
    }

    private LTFManager() {

    }

    public void init(ILTFListener listener) {
        this.createHandler();
        this.createHelper();
        this.createBuffer();
        this.listener = new WeakReference<>(listener);
    }

    public void enable() {
        if (this.isInitialized() && !this.enabled) {
            this.enabled = true;
            this.helper.open();
        }
    }

    public void disable() {
        if (this.isInitialized() && this.enabled) {
            this.enabled = false;
            this.helper.close();
        }
    }

    public void release() {
        this.destoryHandler();
        this.destoryHelper();
        this.destoryBuffer();
    }

    private boolean isInitialized() {
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
            this.helper = new PWSerialPortHelper("LTFManager");
            this.helper.setTimeout(2);
            if ("magton".equals(Build.MODEL)) {
                this.helper.setPath("/dev/ttyS2");
            } else {
                this.helper.setPath("/dev/ttyS4");
            }
            this.helper.setBaudrate(9600);
            this.helper.init(this);
        }
    }

    private void destoryHelper() {
        if (EmptyUtils.isNotEmpty(this.helper)) {
            this.helper.release();
            this.helper = null;
        }
    }

    private void createHandler() {
        if (EmptyUtils.isEmpty(this.thread) && EmptyUtils.isEmpty(this.handler)) {
            this.thread = new HandlerThread("LTFManager");
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
        if (!"magton".equals(Build.MODEL)) {
            PWSerialPort.writeFile("/sys/class/gpio/gpio24/value", "1");
        } else {
            PWSerialPort.writeFile("/sys/class/misc/sunxi-acc/acc/sochip_acc", "0");
        }
    }

    private void switchWriteModel() {
        if (!"magton".equals(Build.MODEL)) {
            PWSerialPort.writeFile("/sys/class/gpio/gpio24/value", "0");
        } else {
            PWSerialPort.writeFile("/sys/class/misc/sunxi-acc/acc/sochip_acc", "1");
        }
    }

    private void sendCommand(byte[] data) {
        if (this.isInitialized()) {
            this.helper.write(data);
            PWLogger.d("指令发送:" + ByteUtils.bytes2HexString(data, true, ", "));
        }
    }

    private boolean checkSystemType(byte system) {
        if (system == 0x01 || system == 0x02 || system == 0x04 || system == 0x05) {
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

    private boolean ignorePackage() {
        if (this.system == 0x00) {
            byte[] bytes = new byte[]{0x01, 0x10, 0x40, 0x1F};
            int index = indexOf(this.buffer, bytes);
            if (index != -1) {
                byte[] data = new byte[index];
                this.buffer.readBytes(data, 0, data.length);
                this.buffer.discardReadBytes();
                PWLogger.d("指令丢弃:" + ByteUtils.bytes2HexString(data, true, ", "));
                return true;
            }
            bytes = new byte[]{0x02, 0x10, 0x40, 0x1F};
            index = indexOf(this.buffer, bytes);
            if (index != -1) {
                byte[] data = new byte[index];
                this.buffer.readBytes(data, 0, data.length);
                this.buffer.discardReadBytes();
                PWLogger.d("指令丢弃:" + ByteUtils.bytes2HexString(data, true, ", "));
                return true;
            }
            bytes = new byte[]{0x04, 0x10, 0x40, 0x1F};
            index = indexOf(this.buffer, bytes);
            if (index != -1) {
                byte[] data = new byte[index];
                this.buffer.readBytes(data, 0, data.length);
                this.buffer.discardReadBytes();
                PWLogger.d("指令丢弃:" + ByteUtils.bytes2HexString(data, true, ", "));
                return true;
            }
            bytes = new byte[]{0x05, 0x10, 0x40, 0x1F};
            index = indexOf(this.buffer, bytes);
            if (index != -1) {
                byte[] data = new byte[index];
                this.buffer.readBytes(data, 0, data.length);
                this.buffer.discardReadBytes();
                PWLogger.d("指令丢弃:" + ByteUtils.bytes2HexString(data, true, ", "));
                return true;
            }
        } else {
            byte[] bytes = new byte[]{this.system, 0x10};
            int index = indexOf(this.buffer, bytes);
            if (index != -1) {
                byte[] data = new byte[index];
                this.buffer.readBytes(data, 0, data.length);
                this.buffer.discardReadBytes();
                PWLogger.d("指令丢弃:" + ByteUtils.bytes2HexString(data, true, ", "));
                return true;
            }
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

    private void stateDataReceived(byte[] data){
        this.sendCommand(LTFTools.makeStateResponse(data));
        this.switchReadModel();
        if(EmptyUtils.isNotEmpty(this.listener)){
            this.listener.get().onStateDataReceived(data);
        }
    }

    private void requestCommandReceived(int model){
        byte[] reply = null;
        if(EmptyUtils.isNotEmpty(this.listener)) {
            reply = this.listener.get().requestCommandResponse(model);
        }
        if (EmptyUtils.isNotEmpty(reply)) {
            sendCommand(reply);
        }
        this.switchReadModel();
    }

    @Override
    public void onConnected(PWSerialPortHelper helper) {
        if(!this.isInitialized() || !helper.equals(this.helper))   {
            return;
        }
        this.buffer.clear();
        this.system = 0x00;
        this.switchReadModel();
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onMainBoardReady();
        }
    }

    @Override
    public void onException(PWSerialPortHelper helper) {
        if(!this.isInitialized() || !helper.equals(this.helper))   {
            return;
        }
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onMainBoardException();
        }
    }

    @Override
    public void onByteReceived(PWSerialPortHelper helper, byte[] buffer) throws IOException {
        if(!this.isInitialized() || !helper.equals(this.helper))   {
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
            if (!LTFTools.checkFrame(data)) {
                this.buffer.resetReaderIndex();
                this.buffer.skipBytes(2);
                this.buffer.discardReadBytes();
                continue;
            }
            this.buffer.discardReadBytes();
            if (this.system == 0x00) {
                this.system = system;
                if (EmptyUtils.isNotEmpty(this.listener)) {
                    this.listener.get().onSystemTypeChanged(this.system);
                }
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
            this.handler.sendMessageDelayed(msg, 5);
        }
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
                    stateDataReceived((byte[]) msg.obj);
                    break;
                }
                case 0x03: {
                    requestCommandReceived(msg.arg1);
                    break;
                }
                case 0x04:{
                    switchReadModel();
                    break;
                }
                default:
                    break;
            }
        }
    }
}

