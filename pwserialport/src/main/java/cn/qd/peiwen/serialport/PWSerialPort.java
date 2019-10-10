package cn.qd.peiwen.serialport;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class PWSerialPort {
    private long serialPort;
    private boolean released = false;

    private PWSerialPort(File device, int baudrate, int stopbits, int databits, int parity, int flowControl) throws IOException {
        this.serialPort = this.open(device.getAbsolutePath(), baudrate, stopbits, databits, parity, flowControl);
        if (this.serialPort == 0) {
            throw new IOException();
        }
    }

    public int readBuffer(byte[] data, int len) throws IOException {
        if (this.released) {
            throw new IOException("Serial port released");
        }
        if (data == null) {
            throw new IOException("Serial port read buffer can not be null");
        }
        int length = this.read(this.serialPort, data, len);
        if(length == -1){
            throw new IOException("Serial port read error");
        }
        return length;
    }

    public int writeBuffer(byte[] data, int len) throws IOException {
        if (this.released) {
            throw new IOException("Serial port released");
        }
        if (data == null) {
            throw new IOException("Serial port write buffer can not be null");
        }
        int length = this.write(this.serialPort, data, len);
        if(length == -1 || length != len){
            throw new IOException("Serial port write error(" + length + ")");
        }
        return length;
    }

    public void release() {
        this.released = true;
        if (this.serialPort != 0) {
            this.close(this.serialPort);
            this.serialPort = 0;
        }
    }

    public native static void writeFile(String path, String content);

    private native long open(String path, int baudrate, int stopbits, int databits, int parity, int flowControl); //打开串口

    private native int read(long serialPort, byte[] buffer, int len);

    private native int write(long serialPort, byte[] buffer, int len);

    private native void close(long serialPort); //关闭串口

    static {
        System.loadLibrary("pw_serial_port"); // 载入底层C文件 so库链接文件
    }

    public static class Builder {
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

        public Builder() {

        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder parity(int parity) {
            this.parity = parity;
            return this;
        }

        public Builder stopbits(int stopbits) {
            this.stopbits = stopbits;
            return this;
        }

        public Builder databits(int databits) {
            this.databits = databits;
            return this;
        }

        public Builder baudrate(int baudrate) {
            this.baudrate = baudrate;
            return this;
        }

        public Builder flowControl(int flowControl) {
            this.flowControl = flowControl;
            return this;
        }

        public PWSerialPort build() {
            File device = new File(path);
            if (!checkDevice(device)) {
                Log.e("PWSerialPort", "Missing read/write permission " + path);
                return null;
            }
            try {
                return new PWSerialPort(
                        device,
                        this.baudrate,
                        this.stopbits,
                        this.databits,
                        this.parity,
                        this.flowControl
                );
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        private boolean checkDevice(File device) {
            if (device.canRead() && device.canWrite()) {
                return true;
            }
            return chmodDevice(device);
        }

        private boolean chmodDevice(File device) {
            try {
                Process su = Runtime.getRuntime().exec("/system/bin/su");
                String cmd = "chmod 666 " + device.getAbsolutePath() + "\n" + "exit\n";
                su.getOutputStream().write(cmd.getBytes());
                if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
                    return false;
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
