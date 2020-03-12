package cn.haier.bio.medical.serialport.rsms.listener;


import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSEnterConfigResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSControlCommandEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSQueryModulesResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSNetworkResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSQueryPDAModulesResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSCommontResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSQueryStatusResponseEntity;

public interface IRSMSListener {
    void onRSMSConnected();
    void onRSMSException();
    String findDeviceCode();
    void onMessageSended(String data);
    void onMessageRecved(String data);
    void onRSMSStatusReceived(RSMSQueryStatusResponseEntity status);
    void onRSMSNetworkReceived(RSMSNetworkResponseEntity network);
    void onRSMSModulesReceived(RSMSQueryModulesResponseEntity modules);
    void onRSMSPDAModulesReceived(RSMSQueryPDAModulesResponseEntity modules);

    void onRSMSUnknownReceived();
    void onRSMSDataCollectionReceived();
    void onRSMSControlReceived(RSMSControlCommandEntity entity);
    void onRSMSRecoveryReceived(RSMSCommontResponseEntity response);
    void onRSMSClearCacheReceived(RSMSCommontResponseEntity response);
    void onRSMSQuitConfigReceived(RSMSCommontResponseEntity response);
    void onRSMSAModelConfigReceived(RSMSCommontResponseEntity response);
    void onRSMSBModelConfigReceived(RSMSCommontResponseEntity response);
    void onRSMSDTEModelConfigReceived(RSMSCommontResponseEntity response);
    void onRSMSEnterConfigReceived(RSMSEnterConfigResponseEntity response);

}
