package cn.haier.bio.medical.serialport.rsms;


import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSConfigModelResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSControlEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSModulesEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSNetworkEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSStatusEntity;

public interface IRSMSListener {
    void onRSMSReady();
    void onRSMSConnected();
    void onRSMSException();
    String findDeviceCode();
    void onMessageSended(String data);
    void onMessageRecved(String data);
    void onRSMSStatusReceived(RSMSStatusEntity status);
    void onRSMSNetworkReceived(RSMSNetworkEntity network);
    void onRSMSModulesReceived(RSMSModulesEntity modules);

    void onRSMSUnknownReceived();
    void onRSMSDataCollectionReceived();
    void onRSMSControlReceived(RSMSControlEntity entity);
    void onRSMSRecoveryReceived(RSMSResponseEntity response);
    void onRSMSClearCacheReceived(RSMSResponseEntity response);
    void onRSMSQuitConfigReceived(RSMSResponseEntity response);
    void onRSMSConfigNetworkReceived(RSMSResponseEntity response);
    void onRSMSEnterConfigReceived(RSMSConfigModelResponseEntity response);
}
