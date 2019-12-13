package cn.haier.bio.medical.demo;


import android.os.Bundle;
import android.view.View;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import cn.haier.bio.medical.serialport.finger.ITZFPListener;
import cn.haier.bio.medical.serialport.finger.TZFPManager;
import cn.haier.bio.medical.serialport.refriger.ILTB760AGListener;
import cn.haier.bio.medical.serialport.refriger.LTB760AGManager;
import cn.haier.bio.medical.serialport.refriger.entity.LTB760AGAlarmEntity;
import cn.haier.bio.medical.serialport.refriger.entity.LTB760AGBasicsEntity;
import cn.haier.bio.medical.serialport.refriger.entity.LTB760AGEntity;
import cn.haier.bio.medical.serialport.refriger.entity.LTB760AGStatusEntity;
import cn.haier.bio.medical.serialport.rfid.IRFIDZLG600AListener;
import cn.haier.bio.medical.serialport.rfid.RFIDZLG600AManager;
import cn.haier.bio.medical.serialport.rsms.IRSMSListener;
import cn.haier.bio.medical.serialport.rsms.RSMSManager;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSModulesEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSNetworkEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSStatusEntity;
import cn.haier.bio.medical.serialport.rsms.entity.send.RSMSConfigEntity;
import cn.qd.peiwen.pwlogger.PWLogger;
import cn.qd.peiwen.pwtools.EmptyUtils;


public class MainActivity extends AppCompatActivity implements ILTB760AGListener, IRFIDZLG600AListener, ITZFPListener, IRSMSListener {
    private LTB760AGEntity entity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LTB760AGEntity entity = new LTB760AGEntity();
        LTB760AGEntity e1 = entity.clone();
        RFIDZLG600AManager.getInstance().init(this);
        TZFPManager.getInstance().init(this);
        LTB760AGManager.getInstance().init(this);
        RSMSManager.getInstance().init("BE0HF000U00QGK1N0001", this.getMachineHardwareAddress(), this);
    }

    public byte[] getMachineHardwareAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface element = interfaces.nextElement();
                if ("wlan0".equals(element.getName())) {
                    return element.getHardwareAddress();
                }
            }
            return null;
        } catch (Exception e) {
            PWLogger.e(e);
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LTB760AGManager.getInstance().release();
        RFIDZLG600AManager.getInstance().release();
        TZFPManager.getInstance().release();
        RSMSManager.getInstance().release();
    }

    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.regist:
                if (!TZFPManager.getInstance().isBusy()) {
                    TZFPManager.getInstance().regist();
                }
                break;
            case R.id.download:
                if (!TZFPManager.getInstance().isBusy()) {
                    TZFPManager.getInstance().download("/sdcard");
                }
                break;
            case R.id.upload:
                if (!TZFPManager.getInstance().isBusy()) {
                    List<String> files = new ArrayList<>();
                    files.add("/sdcard/finger.1");
                    files.add("/sdcard/finger.2");
                    files.add("/sdcard/finger.3");
                    TZFPManager.getInstance().uplaod(files);
                }
                break;
            case R.id.open_finger:
                TZFPManager.getInstance().enable();
                break;
            case R.id.close_finger:
                TZFPManager.getInstance().disable();
                break;
            case R.id.open_rfid:
                RFIDZLG600AManager.getInstance().enable();
                break;
            case R.id.close_rfid:
                RFIDZLG600AManager.getInstance().disable();
                break;
            case R.id.open_main:
                LTB760AGManager.getInstance().enable();
                break;
            case R.id.close_main:
                LTB760AGManager.getInstance().disable();
                break;
            case R.id.open_rsms:
                RSMSManager.getInstance().enable();
                break;
            case R.id.close_rsms:
                RSMSManager.getInstance().disable();
                break;
            case R.id.query_status:
                RSMSManager.getInstance().queryStatus();
                break;
            case R.id.query_network:
                RSMSManager.getInstance().queryNetwork();
                break;
            case R.id.query_modules:
                RSMSManager.getInstance().queryModules();
                break;
            case R.id.config_quit:
                RSMSManager.getInstance().quitConfigModel();
                break;
            case R.id.config_clear:
                RSMSManager.getInstance().clearCache();
                break;
            case R.id.config_recovery:
                RSMSManager.getInstance().recovery();
                break;
            case R.id.config_enter:
                RSMSManager.getInstance().enterConfigModel();
                break;
            case R.id.config_network:
                RSMSConfigEntity entity = new RSMSConfigEntity();
                entity.setModel((byte)0x02);
                entity.setDomain("ucool.haierbiomeidcal.com");
                entity.setAddress("192.178.1.168");
                entity.setPort("20000");

                entity.setWifiDomain("ucool.haierbiomeidcal.com");
                entity.setWifiAddress("192.178.1.168");
                entity.setWifiPort("20000");

                entity.setWifiName("Haier-Guest");
                entity.setWifiPassword("1234567890");

                entity.setApn("cmnet");
                entity.setApnName("haierbiomedical");
                entity.setApnPassword("password");

                RSMSManager.getInstance().configNetwork(entity);
                break;
        }
    }

    @Override
    public void onLTB760AGReady() {
        this.entity = null;
        PWLogger.e("Main board ready!");
    }

    @Override
    public void onLTB760AGConnected() {
        PWLogger.e("Main board connected!");
    }

    @Override
    public void onLTB760AGException() {
        PWLogger.e("Main board exception!");
    }

    @Override
    public byte[] packageResponse(int type) {
        return new byte[0];
    }

    @Override
    public void onLTB760AGSystemChanged(int type) {
        PWLogger.e("System type changed " + type);
    }

    @Override
    public void onLTB760AGStateChanged(LTB760AGEntity entity) {
        if(EmptyUtils.isEmpty(this.entity)){
            outputLTB760AGAlarm(entity.getAlarm());
            outputLTB760AGBasics(entity.getBasics());
            outputLTB760AGStatus(entity.getStatus());
        }else{
            if(!entity.getAlarm().equals(this.entity.getAlarm())){
                outputLTB760AGAlarm(entity.getAlarm());
            }
            if(!entity.getBasics().equals(this.entity.getBasics())){
                outputLTB760AGBasics(entity.getBasics());
            }
            if(!entity.getStatus().equals(this.entity.getStatus())){
                outputLTB760AGStatus(entity.getStatus());
            }
        }
        this.entity = entity;
    }

    private void outputLTB760AGAlarm(LTB760AGAlarmEntity entity){
        PWLogger.e("报警数据：");
        PWLogger.d("报警状态信息1:" + entity.getAlarmStatus1());
        PWLogger.d("报警状态信息2:" + entity.getAlarmStatus2());
    }

    private void outputLTB760AGBasics(LTB760AGBasicsEntity entity){
        PWLogger.e("基础数据：");
        PWLogger.d("箱内温度:" + entity.getTemperature());
        PWLogger.d("环境温度:" + entity.getAmbientTemperature());
        PWLogger.d("冷凝器温度:" + entity.getCondenserTemperature());
        PWLogger.d("冷凝器2温度:" + entity.getCondenser2Temperature());
        PWLogger.d("热交换器温度:" + entity.getHeatExchangerTemperature());
        PWLogger.d("电源电压:" + entity.getSupplyVoltage());
        PWLogger.d("后备系统检测箱内温度:" + entity.getBackupTemperature());
        PWLogger.d("热电偶温度1(预留):" + entity.getThermocoupleTemperature1());
        PWLogger.d("热电偶温度2(预留):" + entity.getThermocoupleTemperature2());
        PWLogger.d("热电偶温度3(预留):" + entity.getThermocoupleTemperature3());
        PWLogger.d("热电偶温度4(预留):" + entity.getThermocoupleTemperature4());
        PWLogger.d("热电偶温度5(预留):" + entity.getThermocoupleTemperature5());
        PWLogger.d("热电偶温度6(预留):" + entity.getThermocoupleTemperature6());
        PWLogger.d("热电偶温度7(预留):" + entity.getThermocoupleTemperature7());
        PWLogger.d("热电偶温度8(预留):" + entity.getThermocoupleTemperature8());
        PWLogger.d("热电偶温度9(预留):" + entity.getThermocoupleTemperature9());
        PWLogger.d("热电偶温度10(预留):" + entity.getThermocoupleTemperature10());
    }


    private void outputLTB760AGStatus(LTB760AGStatusEntity entity) {
        PWLogger.e("状态数据：");
        PWLogger.d("后备板电池状态:" + entity.getBackupBatteryStatus());
        PWLogger.d("门开关输入状态1:" + entity.getDoorInputStatus1());
        PWLogger.d("门开关输入状态2:" + entity.getDoorInputStatus2());
        PWLogger.d("电池充电状态:" + entity.getBatteryChargingStatus());
        PWLogger.d("远程报警输出状态:" + entity.getRemoteAlarmOutputStatus());
        PWLogger.d("高温压机状态:" + entity.getHighTemperatureCompressorStatus());
        PWLogger.d("低温压机状态:" + entity.getLowTemperatureCompressorStatus());
        PWLogger.d("冷凝风机1输出状态:" + entity.getCondensateBlowerOutputStatus1());
        PWLogger.d("冷凝风机2输出状态:" + entity.getCondensateBlowerOutputStatus2());
        PWLogger.d("升压输出状态:" + entity.getRisePressureOutputStatus());
        PWLogger.d("降压输出状态:" + entity.getDropPressureOutputStatus());
        PWLogger.d("电磁锁输出状态:" + entity.getElectromagneticLockOutputStatus());
        PWLogger.d("蜂鸣器输出状态:" + entity.getBuzzerOutputStatus());
        PWLogger.d("交流毛细管加热丝输出状态:" + entity.getAccapillaryHeatingWireOutputStatus());
        PWLogger.d("柜口加热丝输出状态:" + entity.getCabinetHeatingWireOutputStatus());
        PWLogger.d("门体加热丝输出状态:" + entity.getDoorHeatingWireOutputStatus());
        PWLogger.d("平衡口加热丝输出状态:" + entity.getBalanceHeatingWireOutputStatus());
        PWLogger.d("预留加热丝输出状态:" + entity.getReservedHeatingWireStatus());
        PWLogger.d("后备系统各种状态:" + entity.getBackupStatus());
        PWLogger.d("后备系统连接状态:" + entity.getBackupConnectionStatus());
        PWLogger.d("设定温度值:" + entity.getSettingTemperatureValue());
        PWLogger.d("高温报警值:" + entity.getHighTemperatureAlarmValue());
        PWLogger.d("低温报警值:" + entity.getLowTemperatureAlarmValue());
    }

    @Override
    public void onRFIDZLG600AReady() {
        PWLogger.e("RFID ready!");
    }

    @Override
    public void onRFIDZLG600AConnected() {
        PWLogger.e("RFID connected!");
    }

    @Override
    public void onRFIDZLG600AException() {
        PWLogger.e("RFID exception!");
    }

    @Override
    public void onRFIDZLG600ARecognized(long id, String card) {
        PWLogger.e("RFID recognized " + card + "(" + id + ")");
    }


    @Override
    public void onTZFPReady() {
        PWLogger.e("FingerPrint ready");
    }

    @Override
    public void onTZFPConnected() {
        PWLogger.e("FingerPrint connected");
    }

    @Override
    public void onTZFPException() {
        PWLogger.e("FingerPrint exception");
    }

    @Override
    public void onRegistStated() {
        PWLogger.e("FingerPrint regist stated ");
    }

    @Override
    public void onRegistTimeout() {
        PWLogger.e("FingerPrint regist timeout ");
    }

    @Override
    public void onRegistFailured() {
        PWLogger.e("FingerPrint regist failured ");
    }

    @Override
    public void onFingerAlreadyExists() {
        PWLogger.e("FingerPrint already exists");
    }

    @Override
    public void onRegistStepChanged(int step) {
        PWLogger.e("FingerPrint regist step changed " + step);
    }

    @Override
    public void onRegistSuccessed(int finger) {
        PWLogger.e("FingerPrint regist successed");
    }

    @Override
    public void onUploadStated() {
        PWLogger.e("FingerPrint upload stated");
    }

    @Override
    public void onUploadFailured() {
        PWLogger.e("FingerPrint upload failured");
    }

    @Override
    public void onUploadSuccessed() {
        PWLogger.e("FingerPrint upload successed");
    }

    @Override
    public void onNoFingerExist() {
        PWLogger.e("FingerPrint no finger exist");
    }

    @Override
    public void onDownloadStated() {
        PWLogger.e("FingerPrint download stated");
    }

    @Override
    public void onDownloadFailured() {
        PWLogger.e("FingerPrint download failured");
    }

    @Override
    public void onDownloadSuccessed() {
        PWLogger.e("FingerPrint download successed");
    }

    @Override
    public boolean isFingerValid(int finger) {
        return false;
    }

    @Override
    public void onFingerUNRegistered() {
        PWLogger.e("FingerPrint Finger unregistered");
    }

    @Override
    public void onFingerRecognized(int finger) {
        PWLogger.e("FingerPrint finger recognized " + finger);
    }


    @Override
    public void onRSMSReady() {
        PWLogger.e("RSMS ready");
    }

    @Override
    public void onRSMSConnected() {
        PWLogger.e("RSMS connected");
    }

    @Override
    public void onRSMSException() {
        PWLogger.e("RSMS exception");
    }

    @Override
    public void onRSMSStatusChanged(RSMSStatusEntity status) {
        PWLogger.e("RSMS status changed");
    }

    @Override
    public void onRSMSNetworChanged(RSMSNetworkEntity network) {
        PWLogger.e("RSMS network changed");
    }

    @Override
    public void onRSMSModulesChanged(RSMSModulesEntity modules) {
        PWLogger.e("RSMS modules changed");
    }

    @Override
    public void onRSMSResponseChanged(int type, RSMSResponseEntity response) {
        PWLogger.e("RSMS response changed:" + type);
    }
}
