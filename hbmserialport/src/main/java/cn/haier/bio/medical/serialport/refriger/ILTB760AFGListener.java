package cn.haier.bio.medical.serialport.refriger;


import cn.haier.bio.medical.serialport.refriger.entity.LTB760AFGEntity;

public interface ILTB760AFGListener {
    void onLTB760AFGReady();
    void onLTB760AFGConnected();
    void onLTB760AFGException();
    byte[] packageResponse(int type);
    void onLTB760AFGSystemChanged(int type);
    void onLTB760AFGStateChanged(LTB760AFGEntity entity);
}
