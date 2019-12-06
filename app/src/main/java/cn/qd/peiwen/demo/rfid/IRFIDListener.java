package cn.qd.peiwen.demo.rfid;

public interface IRFIDListener {
    void onRFIDReaderReady();
    void onRFIDReaderException();
    void onRFIDReaderRecognized(long id, String card);
}
