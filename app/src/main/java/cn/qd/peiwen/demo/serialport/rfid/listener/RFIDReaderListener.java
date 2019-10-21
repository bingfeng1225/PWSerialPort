package cn.qd.peiwen.demo.serialport.rfid.listener;

public interface RFIDReaderListener {
    void onRFIDReaderReady();
    void onRFIDReaderException();
    void onRFIDReaderRecognized(long id, String card);
}
