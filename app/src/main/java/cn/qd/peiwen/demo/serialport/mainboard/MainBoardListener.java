package cn.qd.peiwen.demo.serialport.mainboard;

public interface MainBoardListener {
    void onMainBoardReady();
    void onMainBoardException();
    void onSystemTypeChanged(int type);
    void onStateDataReceived(byte[] data);
    byte[] requestCommandResponse(int type);
}
