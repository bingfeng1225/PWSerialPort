package cn.qd.peiwen.demo.serialport.rfid;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteOrder;

import cn.qd.peiwen.demo.serialport.rfid.listener.RFIDReaderListener;
import cn.qd.peiwen.demo.serialport.rfid.tools.RFIDReaderTools;
import cn.qd.peiwen.demo.serialport.rfid.types.RFIDReaderCommond;
import cn.qd.peiwen.logger.PWLogger;
import cn.qd.peiwen.pwtools.ByteUtils;
import cn.qd.peiwen.pwtools.EmptyUtils;
import cn.qd.peiwen.serialport.PWSerialPortHelper;
import cn.qd.peiwen.serialport.PWSerialPortListener;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class RFIDReaderManager implements PWSerialPortListener {
    private ByteBuf buffer;
    private RFIDHandler handler;
    private HandlerThread thread;
    private PWSerialPortHelper helper;

    private int times = 0;
    private boolean uart = false;
    private boolean enabled = false;
    private WeakReference<RFIDReaderListener> listener;

    private static RFIDReaderManager manager;

    public static RFIDReaderManager getInstance() {
        if (manager == null) {
            synchronized (RFIDReaderManager.class) {
                if (manager == null)
                    manager = new RFIDReaderManager();
            }
        }
        return manager;
    }

    private RFIDReaderManager() {

    }

    public void init(RFIDReaderListener listener) {
        this.createHandler();
        this.createHelper();
        this.createBuffer();
        this.listener = new WeakReference<>(listener);
    }

    public void enable() {
        if (this.isReady() && !this.enabled) {
            this.enabled = true;
            this.helper.open();
        }
    }

    public void disable() {
        if (this.isReady() && this.enabled) {
            this.enabled = false;
            this.helper.close();
        }
    }

    public void release() {
        this.destoryHandler();
        this.destoryHelper();
        this.destoryBuffer();
    }

    private boolean isReady() {
        if(EmptyUtils.isEmpty(this.handler)){
            return false;
        }
        if(EmptyUtils.isEmpty(this.helper)){
            return false;
        }
        if(EmptyUtils.isEmpty(this.buffer)){
            return false;
        }
        return true;
    }

    private void createHelper() {
        if (EmptyUtils.isEmpty(this.helper)) {
            this.helper = new PWSerialPortHelper("RFIDReader");
            this.helper.setTimeout(3);
            this.helper.setPath("/dev/ttyS1");
            this.helper.setBaudrate(115200);
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
            this.thread = new HandlerThread("RFIDReader");
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

    private void sendCommand(RFIDReaderCommond type) {
        if (this.isReady()) {
            byte[] data = RFIDReaderTools.createFingerCommand(type);
            PWLogger.d("指令发送：" + ByteUtils.bytes2HexString(data));
            this.helper.write(data);
        }
    }

    private void fireRFIDReaderReady(){
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onRFIDReaderReady();
        }
    }

    private void fireRFIDReaderException(){
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onRFIDReaderException();
        }
    }

    private void fireRFIDReaderRecognized(long id, String card){
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onRFIDReaderRecognized(id, card);
        }
    }

    @Override
    public void onConnected(PWSerialPortHelper helper) {
        if (!checkHelper(helper)) {
            return;
        }
        this.times = 0;
        this.uart = false;
        this.buffer.clear();
        this.handler.sendEmptyMessage(0);
    }

    @Override
    public void onException(PWSerialPortHelper helper) {
        if (!checkHelper(helper)) {
            return;
        }
        this.fireRFIDReaderException();
        if (this.enabled) {
            RFIDReaderTools.resetRFIDReader();
        }
    }

    @Override
    public void onByteReceived(PWSerialPortHelper helper, byte[] buffer) throws IOException {
        if (!checkHelper(helper)) {
            return;
        }
        this.buffer.writeBytes(buffer, 0, buffer.length);
        if (!this.uart) {
            this.buffer.markReaderIndex();
            byte mark = this.buffer.readByte();
            if (mark != 0x06) {
                this.buffer.resetReaderIndex();
            } else {
                this.uart = true;
                this.fireRFIDReaderReady();
                this.buffer.discardReadBytes();
                this.handler.sendEmptyMessageDelayed(1, 1000);
            }
        } else {
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
            if (!RFIDReaderTools.checkFrame(data)) {
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
        this.fireRFIDReaderRecognized(id, card);

        this.buffer.skipBytes(2);
        this.buffer.discardReadBytes();
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
                        sendEmptyMessageDelayed(0, 10);
                    }
                    sendCommand(RFIDReaderCommond.RFID_COMMAND_UART);
                    break;
                default:
                    sendCommand(RFIDReaderCommond.RFID_COMMAND_READ);
                    break;
            }
        }
    }
}

