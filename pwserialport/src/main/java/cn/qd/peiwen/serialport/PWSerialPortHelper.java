package cn.qd.peiwen.serialport;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.lang.ref.WeakReference;

import cn.qd.peiwen.pwlogger.PWLogger;
import cn.qd.peiwen.pwtools.EmptyUtils;


public class PWSerialPortHelper {
    //串口地址
    private String path;
    //奇偶校验:
    // 0 --> 无校验
    // 1 --> 奇校验
    // 2 --> 偶校验
    // 3 --> Space校验
    private int parity = 0;
    //停止位:
    // 1 --> 1位
    // 2 --> 2位
    private int stopbits = 1;
    //数据位:
    // 5 --> 5位
    // 6 --> 6位
    // 7 --> 7位
    // 8 --> 8位
    private int databits = 8;
    //波特率
    private int baudrate = 0;
    //数据流控:
    // 0 --> 无流控
    // 1 --> 硬件流控
    // 1 --> 软件流控
    private int flowControl = 0;

    private int state;
    private int timeout = 0;
    private final String name;
    private PWSerialPort serialPort;
    private WeakReference<PWSerialPortListener> listener;

    //处理线程
    private PHandler phandler;
    private HandlerThread pthread;
    //读取线程
    private ReadThread readThread;

    public static final int PW_SERIAL_PORT_STATE_IDLE = 1;
    public static final int PW_SERIAL_PORT_STATE_OPENED = 2;
    public static final int PW_SERIAL_PORT_STATE_CLOSED = 3;
    public static final int PW_SERIAL_PORT_STATE_RELEASED = 4;

    public PWSerialPortHelper(String name) {
        this.name = name;
        this.state = PW_SERIAL_PORT_STATE_RELEASED;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getParity() {
        return parity;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }

    public int getStopbits() {
        return stopbits;
    }

    public void setStopbits(int stopbits) {
        this.stopbits = stopbits;
    }

    public int getDatabits() {
        return databits;
    }

    public void setDatabits(int databits) {
        this.databits = databits;
    }

    public int getBaudrate() {
        return baudrate;
    }

    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
    }

    public int getFlowControl() {
        return flowControl;
    }

    public void setFlowControl(int flowControl) {
        this.flowControl = flowControl;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void init(PWSerialPortListener listener) {
        if (isReleased()) {
            this.createProcessHandler();
            this.listener = new WeakReference<>(listener);
            this.changeSerialProtState(PW_SERIAL_PORT_STATE_IDLE);
        }
    }

    public void open() {
        if (isIdle() || isClosed()) {
            this.changeSerialProtState(PW_SERIAL_PORT_STATE_OPENED);
        }
    }

    public void write(byte[] bytes) {
        try {
            if (isWriteable()) {
                this.serialPort.outputStream().write(bytes);
                this.serialPort.outputStream().flush();
                this.serialPort.outputStream().getFD().sync();
            }
        } catch (IOException e) {
            PWLogger.d(e);
            this.onException();
        }
    }

    public void close() {
        if (this.isOpened()) {
            this.changeSerialProtState(PW_SERIAL_PORT_STATE_CLOSED);
        }
    }

    public void release() {
        if (!isReleased()) {
            this.changeSerialProtState(PW_SERIAL_PORT_STATE_RELEASED);
        }
    }

    private boolean isIdle() {
        if (this.state == PW_SERIAL_PORT_STATE_IDLE) {
            return true;
        }
        return false;
    }

    private boolean isOpened() {
        if (this.state == PW_SERIAL_PORT_STATE_OPENED) {
            return true;
        }
        return false;
    }

    private boolean isClosed() {
        if (this.state == PW_SERIAL_PORT_STATE_CLOSED) {
            return true;
        }
        return false;
    }

    private boolean isReleased() {
        if (this.state == PW_SERIAL_PORT_STATE_RELEASED) {
            return true;
        }
        return false;
    }

    private boolean isReadable() {
        if (this.isOpened() && EmptyUtils.isNotEmpty(this.serialPort) && EmptyUtils.isNotEmpty(this.serialPort.inputStream())) {
            return true;
        }
        return false;
    }

    private boolean isWriteable() {
        if (this.isOpened() && EmptyUtils.isNotEmpty(this.serialPort) && EmptyUtils.isNotEmpty(this.serialPort.outputStream())) {
            return true;
        }
        return false;
    }

    private void onOpen() {
        this.createSerialPort();
        if (EmptyUtils.isNotEmpty(this.serialPort)) {
            this.fireConnected();
            this.createReadThread();
        } else {
            this.onException();
        }
    }

    private synchronized void onException() {
        this.destoryReadThread();
        this.destorySerialPort();
        if (!isClosed() && !isReleased()) {
            this.fireException();
            this.phandler.sendEmptyMessageDelayed(PW_SERIAL_PORT_STATE_OPENED, 1000);
        }
    }

    private void onClose() {
        this.destoryReadThread();
        this.destorySerialPort();
    }

    private void onRelease() {
        this.destoryReadThread();
        this.destorySerialPort();
        this.destoryProcessHandler();
    }

    private void fireConnected() {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onConnected(this);
        }
    }

    private void fireException() {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onException(this);
        }
    }

    private void fireByteReceived(byte[] data) throws IOException {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onByteReceived(this, data);
        }
    }

    private void changeSerialProtState(int state) {
        if (this.state != state) {
            this.state = state;
            this.phandler.sendEmptyMessage(this.state);
        }
    }

    private void createReadThread() {
        if (EmptyUtils.isEmpty(this.readThread)) {
            this.readThread = new ReadThread();
            this.readThread.start();
        }
    }

    private void destoryReadThread() {
        if (EmptyUtils.isNotEmpty(this.readThread)) {
            this.readThread.finish();
            this.readThread = null;
        }
    }

    private void createSerialPort() {
        if (EmptyUtils.isEmpty(this.serialPort)) {
            this.serialPort = new PWSerialPort.Builder()
                    .path(this.path)
                    .parity(this.parity)
                    .stopbits(this.stopbits)
                    .databits(this.databits)
                    .baudrate(this.baudrate)
                    .flowControl(this.flowControl)
                    .build();
        }
    }

    private void destorySerialPort() {
        if (EmptyUtils.isNotEmpty(this.serialPort)) {
            this.serialPort.release();
            this.serialPort = null;
        }
    }

    private void createProcessHandler() {
        if (EmptyUtils.isEmpty(this.pthread) && EmptyUtils.isEmpty(this.phandler)) {
            pthread = new HandlerThread("Process-Thread-" + this.name);
            pthread.start();
            phandler = new PHandler(pthread.getLooper());
        }
    }

    private void destoryProcessHandler() {
        if (EmptyUtils.isNotEmpty(this.pthread)) {
            pthread.quitSafely();
            pthread = null;
            phandler = null;
        }
    }

    private class PHandler extends Handler {
        public PHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PW_SERIAL_PORT_STATE_IDLE:
                    PWLogger.d("PW_SERIAL_PORT_STATE_IDLE(" + name + ")");
                    break;
                case PW_SERIAL_PORT_STATE_OPENED:
                    if (!isClosed() && !isReleased()) {
                        PWLogger.d("PW_SERIAL_PORT_STATE_OPENED(" + name + ")");
                        PWSerialPortHelper.this.onOpen();
                    }
                    break;
                case PW_SERIAL_PORT_STATE_CLOSED:
                    PWLogger.d("PW_SERIAL_PORT_STATE_CLOSED(" + name + ")");
                    PWSerialPortHelper.this.onClose();
                    break;
                case PW_SERIAL_PORT_STATE_RELEASED:
                    PWLogger.d("PW_SERIAL_PORT_STATE_RELEASED(" + name + ")");
                    PWSerialPortHelper.this.onRelease();
                    break;
                default:
                    break;
            }
        }
    }

    private class ReadThread extends Thread {
        private int times = 0;
        private boolean finished = false;

        public ReadThread() {

        }

        public void finish() {
            this.finished = true;
        }

        private void checkTimeout() throws IOException {
            this.times++;
            if (timeout == 0) {
                this.times = 0;
                return;
            }
            if (this.times >= timeout) {
                throw new IOException("PWSerialPort(" + name + ") read timeout");
            }
        }

        @Override
        public void run() {
            super.run();
            try {
                byte[] buffer = new byte[128];
                while (!finished && isReadable()) {
                    int ret = serialPort.select();
                    if (ret == 0) {
                        this.checkTimeout();
                        continue;
                    }
                    this.times = 0;
                    if (!isReadable()) {
                        break;
                    }
                    int length = serialPort.inputStream().read(buffer);
                    byte[] data = new byte[length];
                    System.arraycopy(buffer, 0, data, 0, length);
                    PWSerialPortHelper.this.fireByteReceived(data);
                }
            } catch (Exception e) {
                PWLogger.d(e);
                PWSerialPortHelper.this.onException();
            } finally {
                PWLogger.d("PWSerialPort(" + name + ") read thread released");
            }
        }
    }
}
