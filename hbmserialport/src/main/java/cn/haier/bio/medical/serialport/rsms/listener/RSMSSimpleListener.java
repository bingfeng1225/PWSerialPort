package cn.haier.bio.medical.serialport.rsms.listener;

import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSCommontResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSControlCommandEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSEnterConfigResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSNetworkResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSQueryModulesResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSQueryPDAModulesResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSQueryStatusResponseEntity;

public class RSMSSimpleListener implements IRSMSListener {
    @Override
    public void onRSMSConnected() {

    }

    @Override
    public void onRSMSException() {

    }

    @Override
    public String findDeviceCode() {
        return null;
    }

    @Override
    public void onMessageSended(String data) {

    }

    @Override
    public void onMessageRecved(String data) {

    }

    @Override
    public void onRSMSStatusReceived(RSMSQueryStatusResponseEntity status) {

    }

    @Override
    public void onRSMSNetworkReceived(RSMSNetworkResponseEntity network) {

    }

    @Override
    public void onRSMSModulesReceived(RSMSQueryModulesResponseEntity modules) {

    }

    @Override
    public void onRSMSPDAModulesReceived(RSMSQueryPDAModulesResponseEntity modules) {

    }

    @Override
    public void onRSMSUnknownReceived() {

    }

    @Override
    public void onRSMSDataCollectionReceived() {

    }

    @Override
    public void onRSMSControlReceived(RSMSControlCommandEntity entity) {

    }

    @Override
    public void onRSMSRecoveryReceived(RSMSCommontResponseEntity response) {

    }

    @Override
    public void onRSMSClearCacheReceived(RSMSCommontResponseEntity response) {

    }

    @Override
    public void onRSMSQuitConfigReceived(RSMSCommontResponseEntity response) {

    }

    @Override
    public void onRSMSAModelConfigReceived(RSMSCommontResponseEntity response) {

    }

    @Override
    public void onRSMSBModelConfigReceived(RSMSCommontResponseEntity response) {

    }

    @Override
    public void onRSMSDTEModelConfigReceived(RSMSCommontResponseEntity response) {

    }

    @Override
    public void onRSMSEnterConfigReceived(RSMSEnterConfigResponseEntity response) {

    }
}
