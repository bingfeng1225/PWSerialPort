package cn.qd.peiwen.demo.finger.listener;

public interface FingerPrintListener {
    void onRegistTimeout();
    void onRegistFailured();
    void onFingerAlreadyExists();
    void onRegistSuccessed(int finger);

    void onUploadFailured();
    void onUploadSuccessed();

    void onNoFingerExist();
    void onDownloadFailured();
    void onDownloadSuccessed();

    boolean isFingerValid(int finger);

    void onFingerUNRegistered();
    void onFingerRecognized(int finger);
}
