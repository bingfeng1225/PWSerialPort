package cn.qd.peiwen.demo.rfid.listener;

public interface RFIDReaderListener {
    void onRFIDReaderReady();
    void onRFIDReaderException();
    void onRFIDReaderRecognized(long id, String card);
}
