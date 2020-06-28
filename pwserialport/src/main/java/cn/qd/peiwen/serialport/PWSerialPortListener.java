package cn.qd.peiwen.serialport;


import java.io.IOException;

public interface PWSerialPortListener {
    void onConnected(PWSerialPortHelper helper);

    void onReadThreadReleased(PWSerialPortHelper helper);

    void onException(PWSerialPortHelper helper,Throwable throwable);

    void onStateChanged(PWSerialPortHelper helper,PWSerialPortState state);

    void onByteReceived(PWSerialPortHelper helper, byte[] buffer, int length) throws IOException;
}
