package cn.qd.peiwen.serialport;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SerialPort {
    private FileDescriptor descriptor;
    private FileInputStream inputStream;
    private FileOutputStream outputStream;

    public SerialPort(File device, int baudrate, int stopbits, int databits, int parity, int flowControl, int flag) throws IOException {
        this.descriptor = open(device.getAbsolutePath(), baudrate, stopbits, databits, parity, flowControl, flag);
        if (descriptor == null) {
            throw new IOException();
        }
        inputStream = new FileInputStream(descriptor);
        outputStream = new FileOutputStream(descriptor);
    }

    public FileInputStream inputStream() {
        return inputStream;
    }

    public FileOutputStream outputStream() {
        return outputStream;
    }

    public void release() {
        this.close();
        this.descriptor = null;
        this.inputStream = null;
        this.outputStream = null;
    }

    private native static FileDescriptor open(String path, int baudrate, int stopbits, int databits, int parity, int flowControl, int flag); //打开串口

    public native static void writeFile(String path, String content);

    public native void close(); //关闭串口

    static {
        System.loadLibrary("serial_port"); // 载入底层C文件 so库链接文件
    }
}
