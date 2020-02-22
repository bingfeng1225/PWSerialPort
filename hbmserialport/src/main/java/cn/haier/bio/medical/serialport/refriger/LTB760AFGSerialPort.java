package cn.haier.bio.medical.serialport.refriger;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.lang.ref.WeakReference;

import cn.haier.bio.medical.serialport.refriger.entity.LTB760AFGEntity;
import cn.haier.bio.medical.serialport.refriger.tools.LTB760AFGTools;
import cn.haier.bio.medical.serialport.tools.ByteBufTools;
import cn.qd.peiwen.pwlogger.PWLogger;
import cn.qd.peiwen.pwtools.ByteUtils;
import cn.qd.peiwen.pwtools.EmptyUtils;
import cn.qd.peiwen.serialport.PWSerialPortHelper;
import cn.qd.peiwen.serialport.PWSerialPortListener;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class LTB760AFGSerialPort implements PWSerialPortListener {
    private ByteBuf buffer;
    private HandlerThread thread;
    private LTB760AGHandler handler;
    private PWSerialPortHelper helper;

    private byte system = 0x00;
    private boolean ready = false;
    private boolean enabled = false;
    private WeakReference<ILTB760AFGListener> listener;

    public LTB760AFGSerialPort() {
    }

    public void init(ILTB760AFGListener listener) {
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
        this.listener = null;
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
            this.helper = new PWSerialPortHelper("LTB760AFGSerialPort");
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
            this.thread = new HandlerThread("LTB760AFGSerialPort");
            this.thread.start();
            this.handler = new LTB760AGHandler(this.thread.getLooper());
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

    private void write(byte[] data) {
        PWLogger.d("指令发送:" + ByteUtils.bytes2HexString(data, true, ", "));
        if (this.isInitialized() && this.enabled) {
            this.helper.writeAndFlush(data);
        }
    }

    private boolean ignorePackage() {
        boolean result = false;
        if (this.system == 0x00) {
            for (byte item : LTB760AFGTools.SYSTEM_TYPES) {
                byte[] bytes = new byte[]{item, 0x10, 0x40, 0x1F};
                int index = ByteBufTools.indexOf(this.buffer, bytes);
                if (index != -1) {
                    result = true;
                    byte[] data = new byte[index];
                    this.buffer.readBytes(data, 0, data.length);
                    this.buffer.discardReadBytes();
                    PWLogger.d("指令丢弃:" + ByteUtils.bytes2HexString(data, true, ", "));
                    break;
                }
            }
        } else {
            byte[] bytes = new byte[]{this.system, 0x10, 0x40, 0x1F};
            int index = ByteBufTools.indexOf(this.buffer, bytes);
            if (index != -1) {
                result = true;
                byte[] data = new byte[index];
                this.buffer.readBytes(data, 0, data.length);
                this.buffer.discardReadBytes();
                PWLogger.d("指令丢弃:" + ByteUtils.bytes2HexString(data, true, ", "));
            }
        }
        return result;
    }


    @Override
    public void onConnected(PWSerialPortHelper helper) {
        if (!this.isInitialized() || !helper.equals(this.helper)) {
            return;
        }
        this.ready = false;
        this.buffer.clear();
        this.system = 0x00;
        LTB760AFGTools.switchReadModel();
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onLTB760AFGConnected();
        }
    }

    @Override
    public void onException(PWSerialPortHelper helper) {
        if (!this.isInitialized() || !helper.equals(this.helper)) {
            return;
        }
        this.ready = false;
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onLTB760AFGException();
        }
    }

    @Override
    public void onByteReceived(PWSerialPortHelper helper, byte[] buffer, int length) throws IOException {
        if (!this.isInitialized() || !helper.equals(this.helper)) {
            return;
        }
        this.buffer.writeBytes(buffer, 0, length);
        while (this.buffer.readableBytes() >= 2) {
            byte system = this.buffer.getByte(0);
            byte command = this.buffer.getByte(1);
            if (!LTB760AFGTools.checkSystemType(system) || !LTB760AFGTools.checkCommandType(command)) {
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
            if (!LTB760AFGTools.checkFrame(data)) {
                this.buffer.resetReaderIndex();
                //当前包不合法 丢掉正常的包头以免重复判断
                this.buffer.skipBytes(4);
                this.buffer.discardReadBytes();
                continue;
            }
            this.buffer.discardReadBytes();
            if (!this.ready) {
                this.ready = true;
                if (EmptyUtils.isNotEmpty(this.listener)) {
                    this.listener.get().onLTB760AFGReady();
                }
            }
            if (this.system == 0x00) {
                this.system = system;
                if (EmptyUtils.isNotEmpty(this.listener)) {
                    this.listener.get().onLTB760AFGSystemChanged(this.system);
                }
            }
            PWLogger.d("指令接收:" + ByteUtils.bytes2HexString(data, true, ", "));
            LTB760AFGTools.switchWriteModel();
            Message msg = Message.obtain();
            msg.what = command;
            if (command == 0x10) {
                msg.obj = data;
            } else {
                msg.arg1 = model & 0xFF;
            }
            this.handler.sendMessage(msg);
        }
    }


    private class LTB760AGHandler extends Handler {
        public LTB760AGHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x10: {
                    byte[] data = (byte[]) msg.obj;
                    byte[] response = LTB760AFGTools.packageStateResponse(data);
                    LTB760AFGSerialPort.this.write(response);
                    LTB760AFGTools.switchReadModel();

                    LTB760AFGEntity entity = LTB760AFGTools.parseLTB760AGEntity(data);
                    if (EmptyUtils.isNotEmpty(LTB760AFGSerialPort.this.listener)) {
                        LTB760AFGSerialPort.this.listener.get().onLTB760AFGStateChanged(entity);
                    }
                    break;
                }
                case 0x03: {
                    byte[] response = null;
                    if (EmptyUtils.isNotEmpty(LTB760AFGSerialPort.this.listener)) {
                        response = LTB760AFGSerialPort.this.listener.get().packageResponse(msg.arg1);
                    }
                    if (EmptyUtils.isNotEmpty(response)) {
                        LTB760AFGSerialPort.this.write(response);
                    }
                    LTB760AFGTools.switchReadModel();
                    break;
                }
                default:
                    break;
            }
        }
    }
}
