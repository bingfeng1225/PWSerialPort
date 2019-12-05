package cn.qd.peiwen.demo;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import cn.qd.peiwen.demo.serialport.finger.FingerPrintManager;
import cn.qd.peiwen.demo.serialport.finger.listener.FingerPrintListener;
import cn.qd.peiwen.demo.serialport.mainboard.MainBoardManager;
import cn.qd.peiwen.demo.bean.MainBoardEntity;
import cn.qd.peiwen.demo.serialport.mainboard.listener.MainBoardListener;
import cn.qd.peiwen.demo.serialport.rfid.RFIDReaderManager;
import cn.qd.peiwen.demo.serialport.rfid.listener.RFIDReaderListener;
import cn.qd.peiwen.pwlogger.PWLogger;
import cn.qd.peiwen.pwtools.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


public class MainActivity extends AppCompatActivity implements MainBoardListener, RFIDReaderListener, FingerPrintListener {
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private MainBoardEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.textView1 = findViewById(R.id.text_view_1);
        this.textView2 = findViewById(R.id.text_view_2);
        this.textView3 = findViewById(R.id.text_view_3);
        MainBoardManager.getInstance().init(this);
        MainBoardManager.getInstance().enable();

        RFIDReaderManager.getInstance().init(this);
        RFIDReaderManager.getInstance().enable();

        FingerPrintManager.getInstance().init(this);
        FingerPrintManager.getInstance().enable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainBoardManager.getInstance().disable();
        MainBoardManager.getInstance().release();

        RFIDReaderManager.getInstance().disable();
        RFIDReaderManager.getInstance().release();

        FingerPrintManager.getInstance().disable();
        FingerPrintManager.getInstance().release();
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
    public byte[] requestStateReply(byte[] data) {
        byte[] buffer = new byte[8];
        System.arraycopy(data, 0, buffer, 0, buffer.length - 2);
        byte[] crc = ByteUtils.computeCRCCode(data, 0, buffer.length - 2);
        buffer[buffer.length - 2] = crc[0];
        buffer[buffer.length - 1] = crc[1];
        return buffer;
    }

    @Override
    public byte[] requestCommandReply(int type) {
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

        Message msg = new Message();
        msg.what = 0;
        msg.obj = entity;
        this.handler.sendMessage(msg);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity.this.changeMainBoardEntity((MainBoardEntity) msg.obj);
        }
    };


    private void changeMainBoardEntity(MainBoardEntity entity) {
        if (entity.equals(this.entity)) {
            return;
        }
        this.entity = entity;
        this.showMainBoardEntity();
    }

    private void showMainBoardEntity() {
        StringBuilder builder = new StringBuilder();
        builder.append("箱内温度:" + entity.getTemperature() + "\n");
        builder.append("环境温度:" + entity.getAmbientTemperature() + "\n");
        builder.append("冷凝器温度:" + entity.getCondenserTemperature() + "\n");
        builder.append("热交换器温度:" + entity.getHeatExchangerTemperature() + "\n");
        builder.append("电源电压:" + entity.getSupplyVoltage() + "\n");
        builder.append("电池电压:" + entity.getBatteryVoltage() + "\n");
        builder.append("门开关输入状态1:" + entity.getDoorInputStatus1() + "\n");
        builder.append("门开关输入状态2:" + entity.getDoorInputStatus2() + "\n");
        builder.append("电池充电状态:" + entity.getBatteryChargingStatus() + "\n");
        builder.append("远程报警输出状态:" + entity.getRemoteAlarmOutputStatus() + "\n");
        builder.append("高温压机状态:" + entity.getHighTemperatureCompressorStatus() + "\n");
        builder.append("低温压机状态:" + entity.getLowTemperatureCompressorStatus() + "\n");
        builder.append("冷凝风机1输出状态:" + entity.getCondensateBlowerOutputStatus1() + "\n");
        builder.append("冷凝风机2输出状态:" + entity.getCondensateBlowerOutputStatus2() + "\n");
        builder.append("升压输出状态:" + entity.getRisePressureOutputStatus() + "\n");
        builder.append("降压输出状态:" + entity.getDropPressureOutputStatus() + "\n");
        builder.append("电磁锁输出状态:" + entity.getElectromagneticLockOutputStatus() + "\n");
        builder.append("蜂鸣器输出状态:" + entity.getBuzzerOutputStatus() + "\n");
        builder.append("报警状态信息1:" + entity.getAlarmStatus1() + "\n");
        builder.append("报警状态信息2:" + entity.getAlarmStatus2());
        this.textView1.setText(builder.toString());


        builder = new StringBuilder();
        builder.append("交流毛细管加热丝输出状态:" + entity.getAccapillaryHeatingWireOutputStatus() + "\n");
        builder.append("柜口加热丝输出状态:" + entity.getCabinetHeatingWireOutputStatus() + "\n");
        builder.append("门体加热丝输出状态:" + entity.getDoorHeatingWireOutputStatus() + "\n");
        builder.append("平衡口加热丝输出状态:" + entity.getBalanceHeatingWireOutputStatus() + "\n");
        builder.append("预留加热丝输出状态:" + entity.getReservedHeatingWireStatus() + "\n");
        builder.append("热电偶温度1(预留):" + entity.getThermocoupleTemperature1() + "\n");
        builder.append("热电偶温度2(预留):" + entity.getThermocoupleTemperature2() + "\n");
        builder.append("热电偶温度3(预留):" + entity.getThermocoupleTemperature3() + "\n");
        builder.append("热电偶温度4(预留):" + entity.getThermocoupleTemperature4() + "\n");
        builder.append("热电偶温度5(预留):" + entity.getThermocoupleTemperature5() + "\n");
        builder.append("热电偶温度6(预留):" + entity.getThermocoupleTemperature6() + "\n");
        builder.append("热电偶温度7(预留):" + entity.getThermocoupleTemperature7() + "\n");
        builder.append("热电偶温度8(预留):" + entity.getThermocoupleTemperature8() + "\n");
        builder.append("热电偶温度9(预留):" + entity.getThermocoupleTemperature9() + "\n");
        builder.append("热电偶温度10(预留):" + entity.getThermocoupleTemperature10());
        this.textView2.setText(builder.toString());

        builder = new StringBuilder();
        builder.append("A蒸发风机输出状态(预留):" + entity.getEvaporationAStatus() + "\n");
        builder.append("B蒸发风机输出状态(预留):" + entity.getEvaporationBStatus() + "\n");
        builder.append("A电磁阀输出状态(预留):" + entity.getElectromagneticAOutputStatus() + "\n");
        builder.append("B电磁阀输出状态(预留):" + entity.getElectromagneticBOutputStatus() + "\n");
        builder.append("系统化霜状态(预留):" + entity.getSystemDefrostingStatus() + "\n");
        builder.append("A系统化霜判断值(预留):" + entity.getSystemADefrostingJudgmentValue() + "\n");
        builder.append("B系统化霜判断值(预留):" + entity.getSystemBDefrostingJudgmentValue() + "\n");
        builder.append("系统X值已生成(预留):" + entity.getSystemXValueGenerated() + "\n");
        builder.append("后备系统检测箱内温度:" + entity.getBackupTemperature() + "\n");
        builder.append("后备系统各种状态:" + entity.getBackupStatus() + "\n");
        builder.append("后备系统连接状态:" + entity.getBackupConnectionStatus() + "\n");
        builder.append("设定温度值:" + entity.getSettingTemperatureValue() + "\n");
        builder.append("高温报警值:" + entity.getHighTemperatureAlarmValue() + "\n");
        builder.append("低温报警值:" + entity.getLowTemperatureAlarmValue() + "\n");
        this.textView3.setText(builder.toString());
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
