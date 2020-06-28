package cn.qd.peiwen.serialport;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.lang.ref.WeakReference;

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

    private int timeout = 0;
    private final String name;
    private PWSerialPortState state;
    private PWSerialPort serialPort;
    private WeakReference<PWSerialPortListener> listener;

    //处理线程
    private PHandler phandler;
    private HandlerThread pthread;
    //读取线程
    private ReadThread readThread;



    public PWSerialPortHelper(String name) {
        this.name = name;
        this.state = PWSerialPortState.PW_SERIAL_PORT_STATE_RELEASED;
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
            this.changeSerialProtState(PWSerialPortState.PW_SERIAL_PORT_STATE_IDLE);
        }
    }

    public void open() {
        if (isIdle() || isClosed()) {
            this.changeSerialProtState(PWSerialPortState.PW_SERIAL_PORT_STATE_OPENED);
        }
    }

    public synchronized void write(byte[] bytes) {
        try {
            if (isWriteable()) {
                this.serialPort.outputStream().write(bytes);
            }
        } catch (IOException throwable) {
            this.onException(throwable);
        }
    }

    public synchronized void writeAndFlush(byte[] bytes){
        try {
            if (isWriteable()) {
                this.serialPort.outputStream().write(bytes);
                this.serialPort.outputStream().flush();
                this.serialPort.outputStream().getFD().sync();
            }
        } catch (IOException throwable) {
            this.onException(throwable);
        }
    }

    public void close() {
        if (this.isOpened()) {
            this.changeSerialProtState(PWSerialPortState.PW_SERIAL_PORT_STATE_CLOSED);
        }
    }

    public void release() {
        if (!isReleased()) {
            this.changeSerialProtState(PWSerialPortState.PW_SERIAL_PORT_STATE_RELEASED);
        }
    }

    private boolean isIdle() {
        if (this.state == PWSerialPortState.PW_SERIAL_PORT_STATE_IDLE) {
            return true;
        }
        return false;
    }

    private boolean isOpened() {
        if (this.state == PWSerialPortState.PW_SERIAL_PORT_STATE_OPENED) {
            return true;
        }
        return false;
    }

    private boolean isClosed() {
        if (this.state == PWSerialPortState.PW_SERIAL_PORT_STATE_CLOSED) {
            return true;
        }
        return false;
    }

    private boolean isReleased() {
        if (this.state == PWSerialPortState.PW_SERIAL_PORT_STATE_RELEASED) {
            return true;
        }
        return false;
    }

    private boolean isAvailable(){
        if(this.serialPort == null){
            return false;
        }
        if(this.serialPort.inputStream() == null){
            return false;
        }
        if(this.serialPort.outputStream() == null){
            return false;
        }
        return true;
    }

    private boolean isReadable() {
        if (this.isOpened() && isAvailable()) {
            return true;
        }
        return false;
    }

    private boolean isWriteable() {
        if (this.isOpened() && isAvailable()) {
            return true;
        }
        return false;
    }

    private synchronized void onOpen() {
        try {
            this.createSerialPort();
            this.createReadThread();
            if (null != this.listener && null != this.listener.get()) {
                this.listener.get().onConnected(this);
            }
        }catch (Exception throwable){
            this.onException(throwable);
        }
    }

    private synchronized void onException(Throwable throwable) {
        this.destoryReadThread();
        this.destorySerialPort();
        if (!isClosed() && !isReleased()) {
            if (null != this.listener && null != this.listener.get()) {
                this.listener.get().onException(this,throwable);
            }
            this.phandler.sendEmptyMessageDelayed(0, 3000);
        }
    }

    private synchronized void onClose() {
        this.destoryReadThread();
        this.destorySerialPort();
    }

    private synchronized void onRelease() {
        this.destoryReadThread();
        this.destorySerialPort();
        this.destoryProcessHandler();
    }

    private void changeSerialProtState(PWSerialPortState state) {
        if (this.state != state) {
            this.state = state;
            this.phandler.sendEmptyMessage(0);
            if (null != listener && null != listener.get()) {
                listener.get().onStateChanged(this,this.state);
            }
        }
    }

    private void createReadThread() {
        if (this.readThread == null) {
            this.readThread = new ReadThread();
            this.readThread.start();
        }
    }

    private void destoryReadThread() {
        if (null != this.readThread) {
            this.readThread.finish();
            this.readThread = null;
        }
    }

    private void createSerialPort() throws Exception {
        if (this.serialPort == null) {
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
        if (null != this.serialPort) {
            this.serialPort.release();
            this.serialPort = null;
        }
    }

    private void createProcessHandler() {
        if (this.pthread == null && this.phandler == null) {
            pthread = new HandlerThread("Process-Thread-" + this.name);
            pthread.start();
            phandler = new PHandler(pthread.getLooper());
        }
    }

    private void destoryProcessHandler() {
        if (null != this.pthread) {
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
            switch (state) {
                case PW_SERIAL_PORT_STATE_IDLE:
                    break;
                case PW_SERIAL_PORT_STATE_OPENED:
                    if (!isClosed() && !isReleased()) {
                        PWSerialPortHelper.this.onOpen();
                    }
                    break;
                case PW_SERIAL_PORT_STATE_CLOSED:
                    PWSerialPortHelper.this.onClose();
                    break;
                case PW_SERIAL_PORT_STATE_RELEASED:
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
                    if (null != listener && null != listener.get()) {
                        listener.get().onByteReceived(PWSerialPortHelper.this, buffer, length);
                    }
                }
            } catch (Exception throwable) {
                PWSerialPortHelper.this.onException(throwable);
            } finally {
                if (null != listener && null != listener.get()) {
                    listener.get().onReadThreadReleased(PWSerialPortHelper.this);
                }
            }
        }
    }
}
