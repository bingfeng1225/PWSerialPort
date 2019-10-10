package cn.qd.peiwen.demo;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import cn.qd.peiwen.serialport.PWSerialPort;

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

    private final String name;
    private PWSerialPort serialPort;

    //发送线程
    private SHandler shandler;
    private HandlerThread sthread;
    //处理线程
    private PHandler phandler;
    private HandlerThread pthread;
    //读取线程
    private ReadThread readThread;

    public PWSerialPortHelper(String name) {
        this.name = name;
    }

    public void init() {
        this.createProcessHandler();
    }

    public void open() {

    }

    public void close() {

    }

    public void release() {
        this.closeSerialPort();
        this.destoryProcessHandler();
    }

    private void openSerialPort(){

    }

    private void closeSerialPort(){
        if(readThread != null){
            readThread.finish();
            readThread = null;
        }
    }

    private void createSendHandler() {
        if (null == sthread && null == shandler) {
            sthread = new HandlerThread("Send-Thread-" + this.name);
            sthread.start();
            shandler = new SHandler(sthread.getLooper());
        }
    }

    private void destorySendHandler() {
        if (sthread != null) {
            sthread.quitSafely();
            sthread = null;
            shandler = null;
        }
    }

    private void createProcessHandler() {
        if (null == pthread && null == phandler) {
            pthread = new HandlerThread("Process-Thread-" + this.name);
            pthread.start();
            phandler = new PHandler(pthread.getLooper());
        }
    }

    private void destoryProcessHandler() {
        if (pthread != null) {
            pthread.quitSafely();
            pthread = null;
            phandler = null;
        }
    }

    private class SHandler extends Handler {
        public SHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    private class PHandler extends Handler {
        public PHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    private class ReadThread extends Thread {
        private boolean finished = false;

        public ReadThread() {

        }

        public void finish() {
            this.finished = true;
        }

        public boolean isFinished() {
            return finished;
        }

        @Override
        public void run() {
            super.run();
            try {
                byte[] buffer = new byte[128];
                while (!finished) {
                    int len = serialPort.readBuffer(buffer, 128);
                    if (len == 0) {
                        continue;
                    }
                    byte[] data = new byte[len];
                    System.arraycopy(buffer, 0, data, 0, len);
                    if(phandler != null){
                        //TODO 通知
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
