package cn.qd.peiwen.demo.rfid.listener;

public interface RFIDReaderListener {
    void onCardRecognized(long id, String card);
}
