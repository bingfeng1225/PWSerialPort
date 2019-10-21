package cn.qd.peiwen.demo.serialport.finger.listener;

public interface FingerPrintListener {
    void onRegistStated();
    void onRegistTimeout();
    void onRegistFailured();
    void onFingerAlreadyExists();
    void onRegistStepChanged(int step);
    void onRegistSuccessed(int finger);

    void onUploadStated();
    void onUploadFailured();
    void onUploadSuccessed();

    void onNoFingerExist();
    void onDownloadStated();
    void onDownloadFailured();
    void onDownloadSuccessed();

    boolean isFingerValid(int finger);

    void onFingerUNRegistered();
    void onFingerRecognized(int finger);

    void onFingerPrintReady();
    void onFingerPrintException();
}
