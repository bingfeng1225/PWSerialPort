package cn.qd.peiwen.demo;


import android.os.Bundle;
import android.util.Log;
import android.view.View;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import androidx.appcompat.app.AppCompatActivity;
import cn.qd.peiwen.serialport.PWSerialPort;


public class MainActivity extends AppCompatActivity {

    private PWSerialPort serialPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.open:
                openSerialPort();
                break;
            case R.id.send:
                sendMessage();
                break;
            case R.id.write:
                writeFile("1");
                break;
            case R.id.read:
                writeFile("0");
                break;
            case R.id.close:
                closeSerialPort();
                break;
        }
    }

    private void openSerialPort() {
        if (this.serialPort != null) {
            return;
        }
        try {
            this.serialPort = new PWSerialPort.Builder()
                    .path("/dev/ttyUSB0")
                    .baudrate(115200)
                    .build();
            ReadThread thread = new ReadThread();
            Log.e("SerialPort", "创建读取线程-----------------" + this);
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ReadThread extends Thread {
        public ReadThread() {
        }

        @Override
        public void run() {
            super.run();
            byte[] data = new byte[256];
            while (serialPort != null) {
                try {
                    int len = serialPort.readBuffer(data, 256);
                    if (len > 0) {
                        Log.e("SerialPort", toHexString(data, 0, len));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
            Log.e("SerialPort", "读取线程释放-----------------" + this);
        }
    }

    public String toHexString(byte[] data, int offset, int len) {
        if (data == null) {
            return null;
        }
        if (offset < 0 || offset > data.length - 1) {
            return null;
        }
        if (len < 0 || offset + len > data.length) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = offset; i < offset + len; i++) {
            buffer.append(String.format("0x%02X ", data[i]));
        }
        return buffer.toString();
    }

    private void sendMessage() {
        try {
            if (null != this.serialPort) {
                byte[] bytes = {(byte) 0xF5, (byte) 0x09, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x09, (byte) 0xF5};
                this.serialPort.writeBuffer(bytes,bytes.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeFile(String content) {
        long start = System.currentTimeMillis();
        PWSerialPort.writeFile("/sys/class/gpio/gpio24/value", content);
        Log.e("SerialPort", "写文件耗时:" + (System.currentTimeMillis() - start));
    }

    private void closeSerialPort() {
        if (null != this.serialPort) {
            this.serialPort.release();
            this.serialPort = null;
        }
    }
}
