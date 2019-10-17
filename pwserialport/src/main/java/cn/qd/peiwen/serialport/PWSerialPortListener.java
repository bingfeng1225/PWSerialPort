package cn.qd.peiwen.serialport;


import java.io.IOException;

public interface PWSerialPortListener {
    void onConnected(PWSerialPortHelper helper);

    void onException(PWSerialPortHelper helper);

    void onByteReceived(PWSerialPortHelper helper, byte[] buffer) throws IOException;
}
