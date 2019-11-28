package cn.qd.peiwen.demo.serialport.mainboard.listener;

public interface MainBoardListener {
    void onMainBoardReady();
    void onMainBoardException();
    void onSystemTypeChanged(int type);
    void onStateDataReceived(byte[] data);
    byte[] requestStateReply(byte[] data);
    byte[] requestCommandReply(int type);
}
