package cn.haier.bio.medical.serialport.rfid;

public interface IRFIDZLG600AListener {
    void onRFIDZLG600AReady();
    void onRFIDZLG600AConnected();
    void onRFIDZLG600AException();
    void onRFIDZLG600ARecognized(long id, String card);
}
