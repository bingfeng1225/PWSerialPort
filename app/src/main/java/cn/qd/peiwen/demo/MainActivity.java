package cn.qd.peiwen.demo;


import android.os.Bundle;
import android.system.OsConstants;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import cn.qd.peiwen.serialport.SerialPort;
import cn.qd.peiwen.serialport.SerialPortBuilder;


public class MainActivity extends AppCompatActivity {

    private SerialPort serialPort;

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
                writeFile();
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
        this.serialPort = new SerialPortBuilder()
                .baudrate(115200)
                .path("dev/ttyUSB0")
                .build();
        new ReadThread().start();
    }


    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (serialPort != null) {
                byte[] data = new byte[256];
                try {
                    int len = serialPort.inputStream().read(data);
                    Log.e("SerialPort", "读取长度:" + len);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    private void sendMessage() {
        try {
            if (null != this.serialPort) {
                byte[] bytes = {(byte) 0xF5, (byte) 0x09, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x09, (byte) 0xF5};
                this.serialPort.outputStream().write(bytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeFile() {
        long start = System.currentTimeMillis();
        SerialPort.writeFile("/sdcard/pw.log","0");
        Log.e("SerialPort","写文件耗时:" + (System.currentTimeMillis() - start));
    }

    private void closeSerialPort() {
        if (null != this.serialPort) {
            this.serialPort.close();
            this.serialPort = null;
        }
    }
}
