package cn.haier.bio.medical.serialport.rsms.listener;

import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSNetworkResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSQueryModulesResponseEntity;

public interface IRSMSDTEListener {
    void onPDAConfigQuited();
    void onDTEConfigQuited();
    void onDTEConfigEntered();
    void onPDAConfigEntered();
    void onDETMacChanged(String mac);
    void onDeviceCodeChanged(String dceMac);
    void onNetworkReceived(RSMSNetworkResponseEntity network);
    void onModulesReceived(RSMSQueryModulesResponseEntity modules);
}
