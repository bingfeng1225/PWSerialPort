package cn.qd.peiwen.serialport;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SerialPort {
    private long descriptor;
    private boolean released = false;

    public SerialPort(File device, int baudrate, int stopbits, int databits, int parity, int flowControl) throws IOException {
        this.descriptor = this.open(device.getAbsolutePath(), baudrate, stopbits, databits, parity, flowControl);
        if (this.descriptor == 0) {
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
        int length = this.read(this.descriptor, data, len);
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
        int length = this.write(this.descriptor, data, len);
        if(length == -1){
            throw new IOException("Serial port write error");
        }
        return length;
    }

    public void release() {
        this.released = true;
        if (this.descriptor != 0) {
            this.close(this.descriptor);
            this.descriptor = 0;
        }
    }

    public native static void writeFile(String path, String content);

    private native long open(String path, int baudrate, int stopbits, int databits, int parity, int flowControl); //打开串口

    private native int read(long descriptor, byte[] buffer, int len);

    private native int write(long descriptor, byte[] buffer, int len);

    private native void close(long descriptor); //关闭串口

    static {
        System.loadLibrary("serial_port"); // 载入底层C文件 so库链接文件
    }
}
