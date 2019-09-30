package cn.qd.peiwen.serialport;

import android.util.Log;

import java.io.File;

public class SerialPortBuilder {
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

    public SerialPortBuilder() {

    }


    public SerialPortBuilder path(String path) {
        this.path = path;
        return this;
    }

    public SerialPortBuilder parity(int parity) {
        this.parity = parity;
        return this;
    }

    public SerialPortBuilder stopbits(int stopbits) {
        this.stopbits = stopbits;
        return this;
    }

    public SerialPortBuilder databits(int databits) {
        this.databits = databits;
        return this;
    }

    public SerialPortBuilder baudrate(int baudrate) {
        this.baudrate = baudrate;
        return this;
    }

    public SerialPortBuilder flowControl(int flowControl) {
        this.flowControl = flowControl;
        return this;
    }

    public SerialPort build() {
        File device = new File(path);
        if (!checkDevice(device)) {
            Log.e("SerialPort", "Missing read/write permission " + path);
            return null;
        }
        try {
            return new SerialPort(
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
