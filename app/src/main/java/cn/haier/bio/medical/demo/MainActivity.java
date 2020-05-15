package cn.haier.bio.medical.demo;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import java.net.NetworkInterface;
import java.util.Enumeration;

import androidx.appcompat.app.AppCompatActivity;
import cn.haier.bio.medical.demo.control.ControlTools;
import cn.haier.bio.medical.demo.control.recv.TemptureEntity;
import cn.haier.bio.medical.demo.mcu.LTB760AFGCollectionEntity;
import cn.haier.bio.medical.demo.mcu.TestSendEntity;
import cn.haier.bio.medical.serialport.refriger.ILTB760AFGListener;
import cn.haier.bio.medical.serialport.refriger.LTB760AFGManager;
import cn.haier.bio.medical.serialport.refriger.entity.LTB760AFGEntity;
import cn.haier.bio.medical.serialport.rsms.RSMSCommandManager;
import cn.haier.bio.medical.serialport.rsms.RSMSDTEManager;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSCommontResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSControlCommandEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSEnterConfigResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSNetworkResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSQueryModulesResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSQueryPDAModulesResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSQueryStatusResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.send.RSMSAModelConfigEntity;
import cn.haier.bio.medical.serialport.rsms.entity.send.RSMSDTEModelConfigEntity;
import cn.haier.bio.medical.serialport.rsms.listener.IRSMSDTEListener;
import cn.haier.bio.medical.serialport.rsms.listener.IRSMSListener;
import cn.haier.bio.medical.serialport.rsms.tools.RSMSTools;
import cn.qd.peiwen.pwlogger.PWLogger;
import cn.qd.peiwen.pwtools.ByteUtils;
import cn.qd.peiwen.pwtools.EmptyUtils;


public class MainActivity extends AppCompatActivity implements ILTB760AFGListener, IRSMSListener, IRSMSDTEListener {
    private TextView textView;
    private LTB760AFGEntity entity;
    private QRCodeDialog qrCodeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.textView = findViewById(R.id.text);
        this.textView.setMovementMethod(ScrollingMovementMethod.getInstance());

//        LTB760AFGManager.getInstance().init(this);
//        LTB760AFGManager.getInstance().enable();

//        RSMSCommandManager.getInstance().init(this.getMachineHardwareAddress(), this);
//        RSMSCommandManager.getInstance().enable();

        RSMSDTEManager.getInstance().init(this.getMachineHardwareAddress(),"",this);
        RSMSDTEManager.getInstance().enable();

//        handler.sendEmptyMessageDelayed(0,2000);




        byte[] data = {
                (byte) 0x85, (byte) 0xFE, (byte) 0x31, (byte) 0xF8, (byte) 0xD8,
                (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xCE, (byte) 0xFF,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0xDC, (byte) 0xFF, (byte) 0xDC, (byte) 0xFF, (byte) 0xDC,
                (byte) 0xFF, (byte) 0xDC, (byte) 0xFF, (byte) 0xDC, (byte) 0xFF,
                (byte) 0xDC, (byte) 0xFF, (byte) 0xDC, (byte) 0xFF, (byte) 0xDC,
                (byte) 0xFF, (byte) 0xDC, (byte) 0xFF, (byte) 0xDC, (byte) 0xFF,
                (byte) 0x24, (byte) 0xFA, (byte) 0x88, (byte) 0xFA, (byte) 0xC0,
                (byte) 0xF9, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01,
                (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01,
                (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
        };

        byte[] buffer = RSMSTools.packageCommand(RSMSTools.RSMS_COMMAND_COLLECTION_DATA,new TestSendEntity(data));
        PWLogger.e("data:" + ByteUtils.bytes2HexString(buffer,false," "));
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
        LTB760AFGManager.getInstance().disable();
        LTB760AFGManager.getInstance().release();
        RSMSCommandManager.getInstance().disable();
        RSMSCommandManager.getInstance().release();
        RSMSDTEManager.getInstance().disable();
        RSMSDTEManager.getInstance().release();
    }

    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.query_status:
                RSMSCommandManager.getInstance().queryStatus();
                break;
            case R.id.query_network:
                RSMSCommandManager.getInstance().queryNetwork();
                break;
            case R.id.query_modules:
                RSMSCommandManager.getInstance().queryModules();
                break;
            case R.id.query_pda_modules:
                RSMSCommandManager.getInstance().queryPDAModules();
                break;
            case R.id.quit_config:
                RSMSCommandManager.getInstance().quitConfigModel();
                break;
            case R.id.clear_cache:
                RSMSCommandManager.getInstance().clearCache();
                break;
            case R.id.recovery:
                RSMSCommandManager.getInstance().recovery();
                break;
            case R.id.enter_dce_config:
                RSMSCommandManager.getInstance().enterDTEConfigModel();
                break;
            case R.id.enter_pda_config:
                RSMSCommandManager.getInstance().enterPDAConfigModel();
                break;
            case R.id.clear:
                textView.setText("");
                break;
            case R.id.config_network1: {
                RSMSDTEModelConfigEntity entity = new RSMSDTEModelConfigEntity();
                entity.setModel((byte) 0x01);
                entity.setAddress("msg.haierbiomedical.com");
                entity.setPort("1777");
//                entity.setWifiName("Bio_Wireless");
//                entity.setWifiPassword("12345678");
//                entity.setApn("spe.inet4gd.gdsp");
//                entity.setApnName("");
//                entity.setApnPassword("");
                RSMSCommandManager.getInstance().configDTEModel(entity);
                break;
            }
            case R.id.config_network2: {
                RSMSDTEModelConfigEntity entity = new RSMSDTEModelConfigEntity();
                entity.setModel((byte) 0x02);
                entity.setAddress("msg.haierbiomedical.com");
                entity.setPort("1777");
                entity.setWifiName("Bio_Wireless");
                entity.setWifiPassword("12345678");
//                entity.setApn("spe.inet4gd.gdsp");
                entity.setApnName("");
                entity.setApnPassword("");
                RSMSCommandManager.getInstance().configDTEModel(entity);
                break;
            }
            case R.id.config_network3: {
                RSMSDTEModelConfigEntity entity = new RSMSDTEModelConfigEntity();
                entity.setModel((byte) 0x03);
                entity.setAddress("msg.haierbiomedical.com");
                entity.setPort("1777");
                entity.setWifiName("Bio_Wireless");
                entity.setWifiPassword("12345678");
//                entity.setApn("spe.inet4gd.gdsp");
                entity.setApnName("");
                entity.setApnPassword("");
                RSMSCommandManager.getInstance().configDTEModel(entity);
                break;
            }
            case R.id.config_becode:{
                RSMSAModelConfigEntity entity = new RSMSAModelConfigEntity();
                entity.setCode("BE0F0P01T00QGJ190004");
                entity.setUsername("haier");
                entity.setPassword("1234");
                RSMSCommandManager.getInstance().configAModel(entity);
                break;
            }
            case R.id.collection:
                byte[] data = {
                        (byte) 0x85, (byte) 0xFE, (byte) 0x31, (byte) 0xF8, (byte) 0xD8,
                        (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xCE, (byte) 0xFF,
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0xDC, (byte) 0xFF, (byte) 0xDC, (byte) 0xFF, (byte) 0xDC,
                        (byte) 0xFF, (byte) 0xDC, (byte) 0xFF, (byte) 0xDC, (byte) 0xFF,
                        (byte) 0xDC, (byte) 0xFF, (byte) 0xDC, (byte) 0xFF, (byte) 0xDC,
                        (byte) 0xFF, (byte) 0xDC, (byte) 0xFF, (byte) 0xDC, (byte) 0xFF,
                        (byte) 0x24, (byte) 0xFA, (byte) 0x88, (byte) 0xFA, (byte) 0xC0,
                        (byte) 0xF9, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01,
                        (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01,
                        (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
                };
                RSMSCommandManager.getInstance().collectionDeviceData(new TestSendEntity(data));
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
        RSMSDTEManager manager = RSMSDTEManager.getInstance();
        boolean ready = manager.isReady();
        boolean overtime = manager.isArrivalTime();
        boolean changed =  (!entity.isStatusEquals(this.entity) || !entity.isAlarmsEquals(this.entity));
        if(ready && (overtime || changed)){
            LTB760AFGCollectionEntity collection = new LTB760AFGCollectionEntity();
            collection.setEntity(entity);
            RSMSDTEManager.getInstance().collectionDeviceData(collection);
        }
        this.entity = entity;
    }

    @Override
    public void onRSMSConnected() {
        this.refreshTextView("模块连接成功\n");
    }

    @Override
    public void onRSMSException() {
        this.refreshTextView("模块连接断开，三秒后重新连接:\n");
    }

    @Override
    public String findDeviceCode() {
        return null;
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
    public void onRSMSStatusReceived(RSMSQueryStatusResponseEntity status) {
        this.refreshTextView(status.toString());
    }

    @Override
    public void onRSMSNetworkReceived(RSMSNetworkResponseEntity network) {
        this.refreshTextView(network.toString());
    }


    @Override
    public void onRSMSModulesReceived(RSMSQueryModulesResponseEntity modules) {
        this.refreshTextView(modules.toString());
    }

    @Override
    public void onRSMSPDAModulesReceived(RSMSQueryPDAModulesResponseEntity modules) {
        this.refreshTextView(modules.toString());
    }

    @Override
    public void onRSMSUnknownReceived() {
        this.refreshTextView("接收到未知类型的信息\n");
    }

    @Override
    public void onRSMSDataCollectionReceived() {
        this.refreshTextView("数据采集成功\n");
    }

    @Override
    public void onRSMSControlReceived(RSMSControlCommandEntity entity) {
        switch (entity.getCommand()) {
            case (short) ControlTools.RSMS_CONTROL_TEMPTURE_COMMAND:
                TemptureEntity tempture = ControlTools.parseTemptureEntity(entity.getControl());
                this.refreshTextView("收到控制温度指令，设置温度为：" + tempture.getTempture() + "\n");
                break;
            default:
                break;
        }
    }

    @Override
    public void onRSMSRecoveryReceived(RSMSCommontResponseEntity response) {
        if (0x01 == response.getResponse()) {
            this.refreshTextView("恢复出厂设置成功\n");
        } else {
            this.refreshTextView("恢复出厂设置失败\n");
        }
    }

    @Override
    public void onRSMSClearCacheReceived(RSMSCommontResponseEntity response) {
        if (0x01 == response.getResponse()) {
            this.refreshTextView("清空本地缓存成功\n");
        } else {
            this.refreshTextView("清空本地缓存失败\n");
        }
    }

    @Override
    public void onRSMSQuitConfigReceived(RSMSCommontResponseEntity response) {
        if (0x01 == response.getResponse()) {
            this.refreshTextView("退出配置模式成功\n");
        } else {
            this.refreshTextView("退出配置模式失败\n");
        }
    }

    @Override
    public void onRSMSAModelConfigReceived(RSMSCommontResponseEntity response) {
        if (0x01 == response.getResponse()) {
            this.refreshTextView("PDA机编配置成功\n");
        } else {
            this.refreshTextView("PDA机编配置失败\n");
        }
    }

    @Override
    public void onRSMSBModelConfigReceived(RSMSCommontResponseEntity response) {
        if (0x01 == response.getResponse()) {
            this.refreshTextView("PDA配置网络参数成功\n");
        } else {
            this.refreshTextView("PDA配置网络参数失败\n");
        }
    }

    @Override
    public void onRSMSDTEModelConfigReceived(RSMSCommontResponseEntity response) {
        if (0x01 == response.getResponse()) {
            this.refreshTextView("DTE配置网络参数成功\n");
        } else {
            this.refreshTextView("DTE配置网络参数失败\n");
        }
    }

    @Override
    public void onRSMSEnterConfigReceived(RSMSEnterConfigResponseEntity response) {
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
            RSMSDTEManager manager = RSMSDTEManager.getInstance();
            boolean ready = manager.isReady();
            boolean overtime = manager.isArrivalTime();
//            if(ready && overtime){
                byte[] data = {
                        (byte) 0x85, (byte) 0xFE, (byte) 0x31, (byte) 0xF8, (byte) 0xD8,
                        (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xCE, (byte) 0xFF,
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0xDC, (byte) 0xFF, (byte) 0xDC, (byte) 0xFF, (byte) 0xDC,
                        (byte) 0xFF, (byte) 0xDC, (byte) 0xFF, (byte) 0xDC, (byte) 0xFF,
                        (byte) 0xDC, (byte) 0xFF, (byte) 0xDC, (byte) 0xFF, (byte) 0xDC,
                        (byte) 0xFF, (byte) 0xDC, (byte) 0xFF, (byte) 0xDC, (byte) 0xFF,
                        (byte) 0x24, (byte) 0xFA, (byte) 0x88, (byte) 0xFA, (byte) 0xC0,
                        (byte) 0xF9, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01,
                        (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01,
                        (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
                };
                manager.collectionDeviceData(new TestSendEntity(data));
//            }
            handler.sendEmptyMessageDelayed(0,2000);
        }
    };

    @Override
    public void onPDAConfigQuited() {
        if(EmptyUtils.isNotEmpty(this.qrCodeDialog)) {
            this.qrCodeDialog.dismiss();
            this.qrCodeDialog = null;
        }
    }

    @Override
    public void onDTEConfigQuited() {

    }

    @Override
    public void onDTEConfigEntered() {

    }

    @Override
    public void onPDAConfigEntered() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showQRCodeDialog();
            }
        });
    }

    @Override
    public void onDETMacChanged(String mac) {
        if(EmptyUtils.isNotEmpty(this.qrCodeDialog)){
            this.qrCodeDialog.changeMac(mac);
        }
    }

    @Override
    public void onDeviceCodeChanged(String dceMac) {
        //TODO 保存设备BE码
    }

    @Override
    public void onNetworkReceived(RSMSNetworkResponseEntity network) {

    }

    @Override
    public void onModulesReceived(RSMSQueryModulesResponseEntity modules) {

    }

    private void showQRCodeDialog(){
        if(EmptyUtils.isEmpty(this.qrCodeDialog)){
            this.qrCodeDialog = new QRCodeDialog(this);
        }
        if(!this.qrCodeDialog.isShowing()) {
            this.qrCodeDialog.show();
        }
    }
}
