package cn.haier.bio.medical.serialport.rsms;


import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSModulesEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSNetworkEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSStatusEntity;

public interface IRSMSListener {
    void onRSMSReady();
    void onRSMSConnected();
    void onRSMSException();
    void onMessageSended(String data);
    void onMessageRecved(String data);
    void onRSMSStatusChanged(RSMSStatusEntity status);
    void onRSMSNetworChanged(RSMSNetworkEntity network);
    void onRSMSModulesChanged(RSMSModulesEntity modules);
    void onRSMSResponseChanged(int type, RSMSResponseEntity response);
}
