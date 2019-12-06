package cn.qd.peiwen.demo.refriger;

public interface ILTFListener {
    void onMainBoardReady();
    void onMainBoardException();
    void onSystemTypeChanged(int type);
    void onStateDataReceived(byte[] data);
    byte[] requestCommandResponse(int type);
}
