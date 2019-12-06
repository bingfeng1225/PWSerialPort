package cn.qd.peiwen.demo;


import android.os.Bundle;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import cn.qd.peiwen.demo.finger.FPManager;
import cn.qd.peiwen.demo.finger.IFPListener;
import cn.qd.peiwen.demo.refriger.LTFManager;
import cn.qd.peiwen.demo.bean.MainBoardEntity;
import cn.qd.peiwen.demo.refriger.ILTFListener;
import cn.qd.peiwen.demo.rfid.RFIDManager;
import cn.qd.peiwen.demo.rfid.IRFIDListener;
import cn.qd.peiwen.pwlogger.PWLogger;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


public class MainActivity extends AppCompatActivity implements ILTFListener, IRFIDListener, IFPListener {
    private MainBoardEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LTFManager.getInstance().init(this);
        RFIDManager.getInstance().init(this);
        FPManager.getInstance().init(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LTFManager.getInstance().release();
        RFIDManager.getInstance().release();
        FPManager.getInstance().release();
    }

    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.regist:
                if (!FPManager.getInstance().isBusy()) {
                    FPManager.getInstance().regist();
                }
                break;
            case R.id.download:
                if (!FPManager.getInstance().isBusy()) {
                    FPManager.getInstance().download("/sdcard");
                }
                break;
            case R.id.upload:
                if (!FPManager.getInstance().isBusy()) {
                    List<String> files = new ArrayList<>();
                    files.add("/sdcard/finger.1");
                    files.add("/sdcard/finger.2");
                    files.add("/sdcard/finger.3");
                    FPManager.getInstance().uplaod(files);
                }
                break;
            case R.id.open_finger:
                FPManager.getInstance().enable();
                break;
            case R.id.close_finger:
                FPManager.getInstance().disable();
                break;
            case R.id.open_rfid:
                RFIDManager.getInstance().enable();
                break;
            case R.id.close_rfid:
                RFIDManager.getInstance().disable();
                break;
            case R.id.open_main:
                LTFManager.getInstance().enable();
                break;
            case R.id.close_main:
                LTFManager.getInstance().disable();
                break;
        }
    }

    @Override
    public void onMainBoardReady() {
        PWLogger.e("Main board ready!");
    }

    @Override
    public void onMainBoardException() {
        PWLogger.e("Main board exception!");
    }

    @Override
    public void onSystemTypeChanged(int type) {
        PWLogger.e("System type changed " + type);
    }


    @Override
    public byte[] requestCommandResponse(int type) {
        return new byte[0];
    }


    @Override
    public void onStateDataReceived(byte[] data) {
        ByteBuf buffer = Unpooled.copiedBuffer(data);
        MainBoardEntity entity = new MainBoardEntity();
        buffer.skipBytes(7);
        entity.setTemperature(buffer.readShortLE());
        entity.setAmbientTemperature(buffer.readShortLE());
        entity.setCondenserTemperature(buffer.readShortLE());
        entity.setHeatExchangerTemperature(buffer.readShortLE());

        entity.setSupplyVoltage(buffer.readShortLE());
        entity.setBatteryVoltage(buffer.readShortLE());

        entity.setDoorInputStatus1(buffer.readShortLE());
        entity.setDoorInputStatus2(buffer.readShortLE());

        entity.setBatteryChargingStatus(buffer.readShortLE());
        entity.setRemoteAlarmOutputStatus(buffer.readShortLE());

        entity.setHighTemperatureCompressorStatus(buffer.readShortLE());
        entity.setLowTemperatureCompressorStatus(buffer.readShortLE());

        entity.setCondensateBlowerOutputStatus1(buffer.readShortLE());
        entity.setCondensateBlowerOutputStatus2(buffer.readShortLE());

        entity.setRisePressureOutputStatus(buffer.readShortLE());
        entity.setDropPressureOutputStatus(buffer.readShortLE());

        entity.setAccapillaryHeatingWireOutputStatus(buffer.readShortLE());
        entity.setCabinetHeatingWireOutputStatus(buffer.readShortLE());
        entity.setDoorHeatingWireOutputStatus(buffer.readShortLE());
        entity.setBalanceHeatingWireOutputStatus(buffer.readShortLE());
        entity.setReservedHeatingWireStatus(buffer.readShortLE());

        entity.setElectromagneticLockOutputStatus(buffer.readShortLE());
        entity.setBuzzerOutputStatus(buffer.readShortLE());

        entity.setAlarmStatus1(buffer.readShortLE());
        entity.setAlarmStatus2(buffer.readShortLE());

        entity.setBackupTemperature(buffer.readShortLE());
        entity.setBackupStatus(buffer.readShortLE());

        entity.setThermocoupleTemperature1(buffer.readShortLE());
        entity.setThermocoupleTemperature2(buffer.readShortLE());
        entity.setThermocoupleTemperature3(buffer.readShortLE());
        entity.setThermocoupleTemperature4(buffer.readShortLE());
        entity.setThermocoupleTemperature5(buffer.readShortLE());
        entity.setThermocoupleTemperature6(buffer.readShortLE());
        entity.setThermocoupleTemperature7(buffer.readShortLE());
        entity.setThermocoupleTemperature8(buffer.readShortLE());
        entity.setThermocoupleTemperature9(buffer.readShortLE());
        entity.setThermocoupleTemperature10(buffer.readShortLE());

        entity.setBackupConnectionStatus(buffer.readShortLE());

        entity.setSettingTemperatureValue(buffer.readShortLE());
        entity.setHighTemperatureAlarmValue(buffer.readShortLE());
        entity.setLowTemperatureAlarmValue(buffer.readShortLE());

        entity.setEvaporationAStatus(buffer.readByte());
        entity.setEvaporationBStatus(buffer.readByte());

        entity.setElectromagneticAOutputStatus(buffer.readByte());
        entity.setElectromagneticBOutputStatus(buffer.readByte());

        entity.setSystemDefrostingStatus(buffer.readByte());

        buffer.skipBytes(1);

        entity.setSystemADefrostingJudgmentValue(buffer.readShortLE());
        entity.setSystemBDefrostingJudgmentValue(buffer.readShortLE());

        entity.setSystemXValueGenerated(buffer.readShortLE());

        buffer.release();

        this.changeMainBoardEntity(entity);
    }

    private void changeMainBoardEntity(MainBoardEntity entity) {
        if (entity.equals(this.entity)) {
            return;
        }
        this.entity = entity;
        this.showMainBoardEntity();
    }

    private void showMainBoardEntity() {
        PWLogger.d("箱内温度:" + entity.getTemperature() + "\n");
        PWLogger.d("环境温度:" + entity.getAmbientTemperature() + "\n");
        PWLogger.d("冷凝器温度:" + entity.getCondenserTemperature() + "\n");
        PWLogger.d("热交换器温度:" + entity.getHeatExchangerTemperature() + "\n");
        PWLogger.d("电源电压:" + entity.getSupplyVoltage() + "\n");
        PWLogger.d("电池电压:" + entity.getBatteryVoltage() + "\n");
        PWLogger.d("门开关输入状态1:" + entity.getDoorInputStatus1() + "\n");
        PWLogger.d("门开关输入状态2:" + entity.getDoorInputStatus2() + "\n");
        PWLogger.d("电池充电状态:" + entity.getBatteryChargingStatus() + "\n");
        PWLogger.d("远程报警输出状态:" + entity.getRemoteAlarmOutputStatus() + "\n");
        PWLogger.d("高温压机状态:" + entity.getHighTemperatureCompressorStatus() + "\n");
        PWLogger.d("低温压机状态:" + entity.getLowTemperatureCompressorStatus() + "\n");
        PWLogger.d("冷凝风机1输出状态:" + entity.getCondensateBlowerOutputStatus1() + "\n");
        PWLogger.d("冷凝风机2输出状态:" + entity.getCondensateBlowerOutputStatus2() + "\n");
        PWLogger.d("升压输出状态:" + entity.getRisePressureOutputStatus() + "\n");
        PWLogger.d("降压输出状态:" + entity.getDropPressureOutputStatus() + "\n");
        PWLogger.d("电磁锁输出状态:" + entity.getElectromagneticLockOutputStatus() + "\n");
        PWLogger.d("蜂鸣器输出状态:" + entity.getBuzzerOutputStatus() + "\n");
        PWLogger.d("报警状态信息1:" + entity.getAlarmStatus1() + "\n");
        PWLogger.d("报警状态信息2:" + entity.getAlarmStatus2());
        PWLogger.d("交流毛细管加热丝输出状态:" + entity.getAccapillaryHeatingWireOutputStatus() + "\n");
        PWLogger.d("柜口加热丝输出状态:" + entity.getCabinetHeatingWireOutputStatus() + "\n");
        PWLogger.d("门体加热丝输出状态:" + entity.getDoorHeatingWireOutputStatus() + "\n");
        PWLogger.d("平衡口加热丝输出状态:" + entity.getBalanceHeatingWireOutputStatus() + "\n");
        PWLogger.d("预留加热丝输出状态:" + entity.getReservedHeatingWireStatus() + "\n");
        PWLogger.d("热电偶温度1(预留):" + entity.getThermocoupleTemperature1() + "\n");
        PWLogger.d("热电偶温度2(预留):" + entity.getThermocoupleTemperature2() + "\n");
        PWLogger.d("热电偶温度3(预留):" + entity.getThermocoupleTemperature3() + "\n");
        PWLogger.d("热电偶温度4(预留):" + entity.getThermocoupleTemperature4() + "\n");
        PWLogger.d("热电偶温度5(预留):" + entity.getThermocoupleTemperature5() + "\n");
        PWLogger.d("热电偶温度6(预留):" + entity.getThermocoupleTemperature6() + "\n");
        PWLogger.d("热电偶温度7(预留):" + entity.getThermocoupleTemperature7() + "\n");
        PWLogger.d("热电偶温度8(预留):" + entity.getThermocoupleTemperature8() + "\n");
        PWLogger.d("热电偶温度9(预留):" + entity.getThermocoupleTemperature9() + "\n");
        PWLogger.d("热电偶温度10(预留):" + entity.getThermocoupleTemperature10());

        PWLogger.d("A蒸发风机输出状态(预留):" + entity.getEvaporationAStatus() + "\n");
        PWLogger.d("B蒸发风机输出状态(预留):" + entity.getEvaporationBStatus() + "\n");
        PWLogger.d("A电磁阀输出状态(预留):" + entity.getElectromagneticAOutputStatus() + "\n");
        PWLogger.d("B电磁阀输出状态(预留):" + entity.getElectromagneticBOutputStatus() + "\n");
        PWLogger.d("系统化霜状态(预留):" + entity.getSystemDefrostingStatus() + "\n");
        PWLogger.d("A系统化霜判断值(预留):" + entity.getSystemADefrostingJudgmentValue() + "\n");
        PWLogger.d("B系统化霜判断值(预留):" + entity.getSystemBDefrostingJudgmentValue() + "\n");
        PWLogger.d("系统X值已生成(预留):" + entity.getSystemXValueGenerated() + "\n");
        PWLogger.d("后备系统检测箱内温度:" + entity.getBackupTemperature() + "\n");
        PWLogger.d("后备系统各种状态:" + entity.getBackupStatus() + "\n");
        PWLogger.d("后备系统连接状态:" + entity.getBackupConnectionStatus() + "\n");
        PWLogger.d("设定温度值:" + entity.getSettingTemperatureValue() + "\n");
        PWLogger.d("高温报警值:" + entity.getHighTemperatureAlarmValue() + "\n");
        PWLogger.d("低温报警值:" + entity.getLowTemperatureAlarmValue() + "\n");
    }

    @Override
    public void onRFIDReaderReady() {
        PWLogger.e("RFID board ready!");

    }

    @Override
    public void onRFIDReaderException() {
        PWLogger.e("RFID reader exception!");
    }

    @Override
    public void onRFIDReaderRecognized(long id, String card) {
        PWLogger.e("RFID reader recognized " + card +"(" + id + ")");
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
    public void onFingerPrintReady() {
        PWLogger.e("FingerPrint ready");
    }

    @Override
    public void onFingerPrintException() {
        PWLogger.e("FingerPrint exception");
    }
}
