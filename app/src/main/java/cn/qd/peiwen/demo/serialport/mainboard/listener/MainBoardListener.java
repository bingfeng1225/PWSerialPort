package cn.qd.peiwen.demo.serialport.mainboard.listener;

import io.netty.buffer.ByteBuf;

public interface MainBoardListener {
    void onMainBoardReady();
    void onMainBoardException();
    void onSystemTypeChanged(int type);
    byte[] requestStateReply(byte[] data);
    byte[] requestCommandReply(int type);
    void onStateDataReceived(byte[] data);
}
