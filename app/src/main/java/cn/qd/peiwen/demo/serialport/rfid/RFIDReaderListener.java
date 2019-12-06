package cn.qd.peiwen.demo.serialport.rfid;

public interface RFIDReaderListener {
    void onRFIDReaderReady();
    void onRFIDReaderException();
    void onRFIDReaderRecognized(long id, String card);
}
