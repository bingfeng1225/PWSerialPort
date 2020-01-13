package cn.haier.bio.medical.demo;


import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import java.net.NetworkInterface;
import java.util.Enumeration;

import androidx.appcompat.app.AppCompatActivity;
import cn.haier.bio.medical.serialport.finger.ITZFPListener;
import cn.haier.bio.medical.serialport.refriger.ILTB760AFGListener;
import cn.haier.bio.medical.serialport.refriger.entity.LTB760AFGEntity;
import cn.haier.bio.medical.serialport.rfid.IRFIDZLG600AListener;
import cn.haier.bio.medical.serialport.rsms.IRSMSListener;
import cn.haier.bio.medical.serialport.rsms.RSMSManager;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSConfigModelResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSModulesEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSNetworkEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSStatusEntity;
import cn.haier.bio.medical.serialport.rsms.entity.send.RSMSConfigEntity;
import cn.qd.peiwen.pwlogger.PWLogger;


public class MainActivity extends AppCompatActivity implements ILTB760AFGListener, IRFIDZLG600AListener, ITZFPListener, IRSMSListener {
    private TextView textView;
    private LTB760AFGEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.textView = findViewById(R.id.text);
        this.textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        RSMSManager.getInstance().init(null, this.getMachineHardwareAddress(), this);
        RSMSManager.getInstance().enable();
    }

    private void refreshTextView(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(text);
                int offset = textView.getLineCount() * textView.getLineHeight();
                if (offset > textView.getHeight()) {
                    textView.scrollTo(0, offset - textView.getHeight());
                } else {
                    textView.scrollTo(0, 0);
                }
            }
        });
    }

    private byte[] getMachineHardwareAddress() {
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
        RSMSManager.getInstance().release();
    }

    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.query_status:
                RSMSManager.getInstance().queryStatus();
                break;
            case R.id.query_network:
                RSMSManager.getInstance().queryNetwork();
                break;
            case R.id.query_modules:
                RSMSManager.getInstance().queryModules();
                break;
            case R.id.quit_config:
                RSMSManager.getInstance().quitConfigModel();
                break;
            case R.id.clear_cache:
                RSMSManager.getInstance().clearCache();
                break;
            case R.id.recovery:
                RSMSManager.getInstance().recovery();
                break;
            case R.id.enter_dce_config:
                RSMSManager.getInstance().enterDCEConfigModel();
                break;
            case R.id.enter_pda_config:
                RSMSManager.getInstance().enterPDAConfigModel();
                break;
            case R.id.clear:
                this.refreshTextView("");
                break;
            case R.id.config_network:
                RSMSConfigEntity entity = new RSMSConfigEntity();
                entity.setModel((byte) 0x02);
                entity.setAddress("ucool.haierbiomeidcal.com");
                entity.setPort("20000");

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
    public void onLTB760AFGReady() {
        this.entity = null;
        PWLogger.e("Main board ready!");
    }

    @Override
    public void onLTB760AFGConnected() {
        PWLogger.e("Main board connected!");
    }

    @Override
    public void onLTB760AFGException() {
        PWLogger.e("Main board exception!");
    }

    @Override
    public byte[] packageResponse(int type) {
        return new byte[0];
    }

    @Override
    public void onLTB760AFGSystemChanged(int type) {
        PWLogger.e("System type changed " + type);
    }

    @Override
    public void onLTB760AFGStateChanged(LTB760AFGEntity entity) {

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
    public void onMessageSended(String data) {
        final StringBuffer buffer = new StringBuffer(textView.getText().toString());
        buffer.append("发送数据:\n");
        buffer.append(data + "\n");
        this.refreshTextView(buffer.toString());
    }

    @Override
    public void onMessageRecved(String data) {
        final StringBuffer buffer = new StringBuffer(textView.getText().toString());
        buffer.append("接收数据:\n");
        buffer.append(data + "\n");
        this.refreshTextView(buffer.toString());
    }

    @Override
    public void onRSMSStatusReceived(RSMSStatusEntity status) {
        final StringBuffer buffer = new StringBuffer(textView.getText().toString());
        buffer.append(status.toString());
        this.refreshTextView(buffer.toString());
    }

    @Override
    public void onRSMSNetworReceived(RSMSNetworkEntity network) {
        final StringBuffer buffer = new StringBuffer(textView.getText().toString());
        buffer.append(network.toString());
        this.refreshTextView(buffer.toString());
    }

    @Override
    public void onRSMSModulesReceived(RSMSModulesEntity modules) {
        final StringBuffer buffer = new StringBuffer(textView.getText().toString());
        buffer.append(modules.toString());
        this.refreshTextView(buffer.toString());
    }

    @Override
    public void onRSMSUnknownReceived() {
        final StringBuffer buffer = new StringBuffer(textView.getText().toString());
        buffer.append("接收到未知类型的信息\n");
    }

    @Override
    public void onRSMSDataCollectionReceived() {
        final StringBuffer buffer = new StringBuffer(textView.getText().toString());
        buffer.append("数据采集成功\n");
        this.refreshTextView(buffer.toString());
    }

    @Override
    public void onRSMSRecoveryReceived(RSMSResponseEntity response) {
        StringBuffer buffer = new StringBuffer(textView.getText().toString());
        if (0x01 == response.getResponse()) {
            buffer.append("恢复出厂设置成功\n");
        } else {
            buffer.append("恢复出厂设置失败\n");
        }
        this.refreshTextView(buffer.toString());
    }

    @Override
    public void onRSMSClearCacheReceived(RSMSResponseEntity response) {
        StringBuffer buffer = new StringBuffer(textView.getText().toString());
        if (0x01 == response.getResponse()) {
            buffer.append("清空本地缓存成功\n");
        } else {
            buffer.append("清空本地缓存失败\n");
        }
        this.refreshTextView(buffer.toString());
    }

    @Override
    public void onRSMSQuitConfigReceived(RSMSResponseEntity response) {
        StringBuffer buffer = new StringBuffer(textView.getText().toString());
        if (0x01 == response.getResponse()) {
            buffer.append("退出配置模式成功\n");
        } else {
            buffer.append("退出配置模式失败\n");
        }
        this.refreshTextView(buffer.toString());
    }

    @Override
    public void onRSMSConfigNetworkReceived(RSMSResponseEntity response) {
        StringBuffer buffer = new StringBuffer(textView.getText().toString());
        if (0x01 == response.getResponse()) {
            buffer.append("配置网络参数成功\n");
        } else {
            buffer.append("配置网络参数失败\n");
        }
        this.refreshTextView(buffer.toString());
    }

    @Override
    public void onRSMSEnterConfigReceived(RSMSConfigModelResponseEntity response) {
        StringBuffer buffer = new StringBuffer(textView.getText().toString());
        buffer.append("DTE识别码：" + new String(response.getMcu()));
        if (0x01 == response.getResponse()) {
            buffer.append("进入" + (0xB0 == response.getConfigModel() ? "串口" : "PDA") + "配置模式成功\n");
        } else {
            buffer.append("进入" + (0xB0 == response.getConfigModel() ? "串口" : "PDA") + "配置模式失败\n");
        }
        this.refreshTextView(buffer.toString());
    }
}
