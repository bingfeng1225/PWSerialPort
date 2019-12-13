package cn.haier.bio.medical.serialport.finger;

public interface ITZFPListener {
    void onTZFPReady();
    void onTZFPConnected();
    void onTZFPException();

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
}
