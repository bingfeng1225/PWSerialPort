package cn.haier.bio.medical.serialport.refriger;


import cn.haier.bio.medical.serialport.refriger.entity.LTB760AGEntity;

public interface ILTB760AGListener {
    void onLTB760AGReady();
    void onLTB760AGConnected();
    void onLTB760AGException();
    byte[] packageResponse(int type);
    void onLTB760AGSystemChanged(int type);
    void onLTB760AGStateChanged(LTB760AGEntity entity);
}
