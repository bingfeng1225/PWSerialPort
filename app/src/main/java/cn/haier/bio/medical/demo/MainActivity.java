package cn.haier.bio.medical.demo;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import java.net.NetworkInterface;
import java.nio.ByteOrder;
import java.util.Enumeration;

import androidx.appcompat.app.AppCompatActivity;
import cn.haier.bio.medical.demo.control.ControlTools;
import cn.haier.bio.medical.demo.control.recv.TemptureEntity;
import cn.haier.bio.medical.demo.mcu.LTB760AFGCollectionEntity;
import cn.haier.bio.medical.serialport.refriger.ILTB760AFGListener;
import cn.haier.bio.medical.serialport.refriger.entity.LTB760AFGEntity;
import cn.haier.bio.medical.serialport.rsms.IRSMSListener;
import cn.haier.bio.medical.serialport.rsms.RSMSManager;
import cn.haier.bio.medical.serialport.rsms.TestSendEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSConfigModelResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSControlEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSModulesEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSNetworkEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSStatusEntity;
import cn.haier.bio.medical.serialport.rsms.entity.send.RSMSConfigEntity;
import cn.qd.peiwen.pwlogger.PWLogger;
import cn.qd.peiwen.pwtools.ByteUtils;


public class MainActivity extends AppCompatActivity implements ILTB760AFGListener, IRSMSListener {
    private TextView textView;
    private boolean ready;
    private long lastTime = 0;
    private LTB760AFGEntity entity;
    private RSMSStatusEntity status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.textView = findViewById(R.id.text);
        this.textView.setMovementMethod(ScrollingMovementMethod.getInstance());

        byte[] data1 = {(byte) 0xAE, 0x47, 0x10, 0x42};
        byte[] data2 = {(byte) 0xF6, (byte) 0xA8, (byte) 0xF0, 0x42};

        PWLogger.e("BIG_ENDIAN 1:" + ByteUtils.bytes2Flaot(data1));
        PWLogger.e("LITTLE_ENDIAN 1:" + ByteUtils.bytes2Flaot(data1, ByteOrder.LITTLE_ENDIAN));

        PWLogger.e("BIG_ENDIAN 2:" + ByteUtils.bytes2Flaot(data2));
        PWLogger.e("LITTLE_ENDIAN 2:" + ByteUtils.bytes2Flaot(data2, ByteOrder.LITTLE_ENDIAN));

//        LTB760AFGManager.getInstance().init(this);
//        LTB760AFGManager.getInstance().enable();

        RSMSManager.getInstance().init(null, this.getMachineHardwareAddress(), this);
        RSMSManager.getInstance().enable();

        this.handler.sendEmptyMessage(0);
    }

    private void refreshTextView(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int lineCount = textView.getLineCount();
                if (lineCount > 150) {
                    textView.setText("");
                }
                StringBuffer buffer = new StringBuffer(textView.getText());
                buffer.append(text);
                textView.setText(buffer.toString());
                int offset = textView.getLineCount() * textView.getLineHeight();
                if (offset > textView.getHeight()) {
                    textView.scrollTo(0, offset - textView.getHeight());
                } else {
                    textView.scrollTo(0, 0);
                }
            }
        });
    }

    private boolean checkUpload(LTB760AFGEntity entity) {
        if (!this.ready) {
            return false;
        }
//        if (!entity.isStatusEquals(this.entity) || !entity.isAlarmsEquals(this.entity)) {
//            return true;
//        }
        if (System.currentTimeMillis() - this.lastTime >= 1000 * this.status.getUploadFrequency()) {
            return true;
        }
        return false;
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
        this.handler.removeMessages(0);
//        LTB760AFGManager.getInstance().disable();
//        LTB760AFGManager.getInstance().release();
        RSMSManager.getInstance().disable();
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
        if (this.checkUpload(entity)) {
            LTB760AFGCollectionEntity collection = new LTB760AFGCollectionEntity();
            collection.setEntity(entity);
            RSMSManager.getInstance().collectionDeviceData(collection);
        }
        this.entity = entity;
    }

    @Override
    public void onRSMSReady() {
        this.ready = true;
        this.handler.sendEmptyMessage(0);
        this.refreshTextView("模块准备完成\n");
    }

    @Override
    public void onRSMSConnected() {
        this.refreshTextView("模块连接成功\n");
    }

    @Override
    public void onRSMSException() {
        this.ready = false;
        this.refreshTextView("模块连接断开，三秒后重新连接:\n");
    }

    @Override
    public void onMessageSended(String data) {
        final StringBuffer buffer = new StringBuffer();
        buffer.append("发送数据:\n");
        buffer.append(data + "\n");
        this.refreshTextView(buffer.toString());
    }

    @Override
    public void onMessageRecved(String data) {
        final StringBuffer buffer = new StringBuffer();
        buffer.append("接收数据:\n");
        buffer.append(data + "\n");
        this.refreshTextView(buffer.toString());
    }

    @Override
    public void onRSMSStatusReceived(RSMSStatusEntity status) {
        this.status = status;
        this.refreshTextView(status.toString());
    }

    @Override
    public void onRSMSNetworReceived(RSMSNetworkEntity network) {
        this.refreshTextView(network.toString());
    }

    @Override
    public void onRSMSModulesReceived(RSMSModulesEntity modules) {
        this.refreshTextView(modules.toString());
    }

    @Override
    public void onRSMSUnknownReceived() {
        this.refreshTextView("接收到未知类型的信息\n");
    }

    @Override
    public void onRSMSDataCollectionReceived() {
        this.lastTime = System.currentTimeMillis();
        this.refreshTextView("数据采集成功\n");
    }

    @Override
    public void onRSMSControlReceived(RSMSControlEntity entity) {
        switch (entity.getCommand()) {
            case (short) ControlTools.RSMS_CONTROL_TEMPTURE_COMMAND:
                TemptureEntity tempture = ControlTools.parseTemptureEntity(entity.getControl());
                this.refreshTextView("收到控制温度指令，设置温度为："+ tempture.getTempture() +"\n");
                break;
            default:
                break;
        }
    }

    @Override
    public void onRSMSRecoveryReceived(RSMSResponseEntity response) {
        if (0x01 == response.getResponse()) {
            this.refreshTextView("恢复出厂设置成功\n");
        } else {
            this.refreshTextView("恢复出厂设置失败\n");
        }
    }

    @Override
    public void onRSMSClearCacheReceived(RSMSResponseEntity response) {
        if (0x01 == response.getResponse()) {
            this.refreshTextView("清空本地缓存成功\n");
        } else {
            this.refreshTextView("清空本地缓存失败\n");
        }
    }

    @Override
    public void onRSMSQuitConfigReceived(RSMSResponseEntity response) {
        if (0x01 == response.getResponse()) {
            this.refreshTextView("退出配置模式成功\n");
        } else {
            this.refreshTextView("退出配置模式失败\n");
        }
    }

    @Override
    public void onRSMSConfigNetworkReceived(RSMSResponseEntity response) {
        if (0x01 == response.getResponse()) {
            this.refreshTextView("配置网络参数成功\n");
        } else {
            this.refreshTextView("配置网络参数失败\n");
        }
    }

    @Override
    public void onRSMSEnterConfigReceived(RSMSConfigModelResponseEntity response) {
        if (0x01 == response.getResponse()) {
            this.refreshTextView("进入" + (((byte) 0xB0 == response.getConfigModel()) ? "串口" : "PDA") + "配置模式成功\n");
        } else {
            this.refreshTextView("进入" + (((byte) 0xB0 == response.getConfigModel()) ? "串口" : "PDA") + "配置模式失败\n");
        }
    }



    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            byte[] data = {
                    (byte)0x85, (byte)0xFE, (byte)0x31, (byte)0xF8, (byte)0xD8,
                    (byte)0xFF, (byte)0xD8, (byte)0xFF, (byte)0xCE, (byte)0xFF,
                    (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                    (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                    (byte)0xDC, (byte)0xFF, (byte)0xDC, (byte)0xFF, (byte)0xDC,
                    (byte)0xFF, (byte)0xDC, (byte)0xFF, (byte)0xDC, (byte)0xFF,
                    (byte)0xDC, (byte)0xFF, (byte)0xDC, (byte)0xFF, (byte)0xDC,
                    (byte)0xFF, (byte)0xDC, (byte)0xFF, (byte)0xDC, (byte)0xFF,
                    (byte)0x24, (byte)0xFA, (byte)0x88, (byte)0xFA, (byte)0xC0,
                    (byte)0xF9, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00,
                    (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                    (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                    (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01,
                    (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00,
                    (byte)0x00, (byte)0x01, (byte)0x01, (byte)0x01, (byte)0x01,
                    (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
                    (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
            };
            if(checkUpload(null)) {
                RSMSManager.getInstance().collectionDeviceData(new TestSendEntity(data));
            }
            this.sendEmptyMessageDelayed(0,1000);
        }
    };
}
