package cn.qd.peiwen.demo.rfid;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteOrder;

import cn.qd.peiwen.demo.rfid.tools.RFIDTools;
import cn.qd.peiwen.pwlogger.PWLogger;
import cn.qd.peiwen.pwtools.ByteUtils;
import cn.qd.peiwen.pwtools.EmptyUtils;
import cn.qd.peiwen.serialport.PWSerialPortHelper;
import cn.qd.peiwen.serialport.PWSerialPortListener;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class RFIDManager implements PWSerialPortListener {
    private ByteBuf buffer;
    private RFIDHandler handler;
    private HandlerThread thread;
    private PWSerialPortHelper helper;

    private int times = 0;
    private boolean ready = false;
    private boolean enabled = false;
    private WeakReference<IRFIDListener> listener;

    private static RFIDManager manager;

    public static RFIDManager getInstance() {
        if (manager == null) {
            synchronized (RFIDManager.class) {
                if (manager == null)
                    manager = new RFIDManager();
            }
        }
        return manager;
    }

    private RFIDManager() {

    }

    public void init(IRFIDListener listener) {
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
            this.helper = new PWSerialPortHelper("RFIDManager");
            this.helper.setTimeout(2);
            if ("magton".equals(Build.MODEL)) {
                this.helper.setPath("/dev/ttyS5");
            } else {
                this.helper.setPath("/dev/ttyS1");
            }
            this.helper.setBaudrate(115200);
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
            this.thread = new HandlerThread("RFIDManager");
            this.thread.start();
            this.handler = new RFIDHandler(this.thread.getLooper());
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

    private void sendCommand(int type) {
        if (this.isInitialized()) {
            byte[] data = RFIDTools.createFingerCommand(type);
            PWLogger.d("指令发送：" + ByteUtils.bytes2HexString(data));
            this.helper.write(data);
        }
    }

    @Override
    public void onConnected(PWSerialPortHelper helper) {
        if (!this.isInitialized() || !helper.equals(this.helper)) {
            return;
        }
        this.times = 0;
        this.ready = false;
        this.buffer.clear();
        this.handler.sendEmptyMessage(0);
    }

    @Override
    public void onException(PWSerialPortHelper helper) {
        if (!this.isInitialized() || !helper.equals(this.helper)) {
            return;
        }
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onRFIDReaderException();
        }
        if (this.enabled && !"magton".equals(Build.MODEL)) {
            RFIDTools.resetRFIDReader();
        }
    }

    @Override
    public void onByteReceived(PWSerialPortHelper helper, byte[] buffer, int length) throws IOException {
        if (!this.isInitialized() || !helper.equals(this.helper)) {
            return;
        }
        this.buffer.writeBytes(buffer, 0, length);
        if (!this.ready) {
            this.buffer.markReaderIndex();
            byte mark = this.buffer.readByte();
            if (mark == 0x06) {
                this.ready = true;
                if (EmptyUtils.isNotEmpty(this.listener)) {
                    this.listener.get().onRFIDReaderReady();
                }
                if (this.buffer.readableBytes() == 0) {
                    this.buffer.discardReadBytes();
                } else {
                    this.buffer.resetReaderIndex();
                }
            }
        }
        if (this.buffer.readableBytes() > 0) {
            this.buffer.markReaderIndex();
            int temp = this.buffer.readByte();
            this.buffer.resetReaderIndex();
            if (this.buffer.readableBytes() < temp) {
                return;
            }
            this.buffer.markReaderIndex();
            byte[] data = new byte[temp];
            this.buffer.readBytes(data, 0, temp);
            this.buffer.resetReaderIndex();
            if (!RFIDTools.checkFrame(data)) {
                return;
            }
            PWLogger.d("指令回复:" + ByteUtils.bytes2HexString(data));
            this.parseRFIDPackage();
            this.handler.sendEmptyMessageDelayed(1, 1000);
        }
    }

    private void parseRFIDPackage() {
        int total = this.buffer.readByte();
        byte type = this.buffer.readByte();
        if (type != 0x06) {
            this.buffer.skipBytes(total - 2);
            this.buffer.discardReadBytes();
            return;
        }
        byte command = this.buffer.readByte();
        if (command != 0x00) {
            this.buffer.skipBytes(total - 3);
            this.buffer.discardReadBytes();
            return;
        }
        int len = this.buffer.readByte();
        if (len + 6 != total) {
            this.buffer.skipBytes(total - 4);
            this.buffer.discardReadBytes();
            return;
        }
        this.buffer.skipBytes(3);
        byte length = this.buffer.readByte();
        if (length + 4 != len) {
            this.buffer.skipBytes(total - 8);
            this.buffer.discardReadBytes();
            PWLogger.e("reader：" + this.buffer.readerIndex());
            PWLogger.e("write：" + this.buffer.writerIndex());
            return;
        }

        byte[] data = new byte[8];
        this.buffer.readBytes(data, 0, length > 8 ? 8 : length);

        long id = ByteUtils.bytes2Long(data, ByteOrder.LITTLE_ENDIAN);
        String card = ByteUtils.bytes2HexString(data, 0, length);
        this.buffer.skipBytes(2);
        this.buffer.discardReadBytes();
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onRFIDReaderRecognized(id, card);
        }
    }

    private class RFIDHandler extends Handler {
        public RFIDHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    times++;
                    if (times < 2) {
                        sendEmptyMessageDelayed(0, 1);
                    } else {
                        sendEmptyMessageDelayed(1, 1000);
                    }
                    sendCommand(RFIDTools.RFID_COMMAND_UART);
                    break;
                case 1:
                    sendCommand(RFIDTools.RFID_COMMAND_READ);
                    break;
            }
        }
    }
}

