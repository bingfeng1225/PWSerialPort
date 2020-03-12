package cn.haier.bio.medical.serialport.rsms;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSCommontResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSEnterConfigResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSNetworkResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSQueryModulesResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSQueryStatusResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.send.IRSMSSendEntity;
import cn.haier.bio.medical.serialport.rsms.entity.send.RSMSDTEModelConfigEntity;
import cn.haier.bio.medical.serialport.rsms.listener.IRSMSDTEListener;
import cn.haier.bio.medical.serialport.rsms.listener.RSMSSimpleListener;
import cn.qd.peiwen.pwlogger.PWLogger;
import cn.qd.peiwen.pwtools.ByteUtils;
import cn.qd.peiwen.pwtools.EmptyUtils;

public class RSMSDTEManager extends RSMSSimpleListener {
    private String code;
    private String dceMac;


    private long lastTime = 0;
    private boolean pda = false;
    private boolean dte = false;
    private boolean collection = false;
    private RSMSQueryStatusResponseEntity status;

    private RSMSHandler handler;
    private HandlerThread thread;
    private RSMSSerialPort serialPort;
    private static RSMSDTEManager manager;
    private WeakReference<IRSMSDTEListener> listener;

    private static final int RSMS_QUERY_STATUS_MSG = 0x01;
    private static final int RSMS_QUERY_MODULES_MSG = 0x02;

    public static RSMSDTEManager getInstance() {
        if (manager == null) {
            synchronized (RSMSCommandManager.class) {
                if (manager == null)
                    manager = new RSMSDTEManager();
            }
        }
        return manager;
    }

    private RSMSDTEManager() {

    }

    public void init(byte[] mac, String code, IRSMSDTEListener listener) {
        this.code = code;
        this.listener = new WeakReference<>(listener);
        if (EmptyUtils.isEmpty(this.serialPort)) {
            this.serialPort = new RSMSSerialPort();
            this.serialPort.init(mac, this);
        }
        if (EmptyUtils.isEmpty(this.thread) && EmptyUtils.isEmpty(this.handler)) {
            this.thread = new HandlerThread("RSMSDTEManager");
            this.thread.start();
            this.handler = new RSMSHandler(this.thread.getLooper());
        }
    }

    public void enable() {
        if (EmptyUtils.isNotEmpty(this.serialPort)) {
            this.serialPort.enable();
        }
    }

    public void disable() {
        if (EmptyUtils.isNotEmpty(this.serialPort)) {
            this.serialPort.disable();
        }
    }

    public void release() {
        if (EmptyUtils.isNotEmpty(this.thread)) {
            this.thread.quitSafely();
            this.thread = null;
            this.handler = null;
        }
        if (EmptyUtils.isNotEmpty(this.serialPort)) {
            this.serialPort.release();
            this.serialPort = null;
        }
    }

    public void recovery() {
        if (EmptyUtils.isNotEmpty(this.serialPort)) {
            this.serialPort.recovery();
        }
    }

    public void clearCache() {
        if (EmptyUtils.isNotEmpty(this.serialPort)) {
            this.serialPort.clearCache();
        }
    }

    public void queryNetwork() {
        if (EmptyUtils.isNotEmpty(this.serialPort)) {
            this.serialPort.queryNetwork();
        }
    }

    public void queryModules() {
        if (EmptyUtils.isNotEmpty(this.serialPort)) {
            this.serialPort.queryModules();
        }
    }

    public void enterDTEConfigModel() {
        if (EmptyUtils.isNotEmpty(this.serialPort)) {
            this.serialPort.enterDTEConfigModel();
        }
    }

    public void configDTEModel(RSMSDTEModelConfigEntity entity) {
        if (EmptyUtils.isNotEmpty(this.serialPort)) {
            this.serialPort.configDTEModel(entity);
        }
    }

    public void collectionDeviceData(IRSMSSendEntity entity) {
        if(!this.collection) {
            this.collection = true;
            this.serialPort.collectionDeviceData(entity);
        }
    }

    public boolean isReady() {
        if (EmptyUtils.isEmpty(this.status)) {
            return false;
        }
        if (EmptyUtils.isEmpty(this.serialPort)) {
            return false;
        }
        if (((this.status.getStatus() & 0x80) != 0x80)) {
            return false;
        }
        return true;
    }

    public boolean isArrivalTime() {
        if (EmptyUtils.isEmpty(this.status)) {
            return false;
        }
        if (EmptyUtils.isEmpty(this.serialPort)) {
            return false;
        }
        if (System.currentTimeMillis() - this.lastTime < 1000 * this.status.getUploadFrequency()) {
            return false;
        }
        return true;
    }

    @Override
    public void onRSMSConnected() {
        this.serialPort.queryModules();
    }

    @Override
    public void onRSMSException() {
        this.pda = false;
        this.status = null;
        this.handler.removeMessages(RSMS_QUERY_STATUS_MSG);
        this.handler.removeMessages(RSMS_QUERY_MODULES_MSG);
    }

    @Override
    public String findDeviceCode() {
        return this.code;
    }

    @Override
    public void onRSMSStatusReceived(RSMSQueryStatusResponseEntity status) {
        PWLogger.e("查询设备状态成功");
        this.status = status;
        this.handler.sendEmptyMessageDelayed(1, 5000);
    }


    @Override
    public void onRSMSNetworkReceived(RSMSNetworkResponseEntity network) {
        PWLogger.e("查询联网参数成功");
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onNetworkReceived(network);
        }
    }

    @Override
    public void onRSMSClearCacheReceived(RSMSCommontResponseEntity response) {
        if (0x01 == response.getResponse()) {
            PWLogger.e("清空本地缓存成功");
        } else {
            PWLogger.e("清空本地缓存失败");
        }
    }

    @Override
    public void onRSMSModulesReceived(RSMSQueryModulesResponseEntity modules) {
        PWLogger.e("查询模块参数成功");
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onModulesReceived(modules);
        }
        if (!this.pda) {
            //未进入配置，判断BE码是否一致
            if (modules.getCode().equals(this.code)) {
                //一致：查询设备状态
                PWLogger.e("本地BE码与模块参数一致，开始查询设备状态");
                this.handler.sendEmptyMessage(RSMS_QUERY_STATUS_MSG);
            } else {
                //不一致：进入PDA配置模式
                this.dceMac = null;
                PWLogger.e("本地BE码与模块参数不一致，进入PDA配置模式");
                this.serialPort.enterPDAConfigModel();
            }
        } else {
            if (EmptyUtils.isEmpty(this.dceMac)) {
                //已进入PDA配置模式，还未获取到有效的MAC地址
                String mac = ByteUtils.bytes2HexString(modules.getMac());
                this.handler.sendEmptyMessageDelayed(RSMS_QUERY_MODULES_MSG, 2000);
                if (!mac.equals("FFFFFFFFFFFF")) {
                    this.dceMac = mac;
                    PWLogger.e("获取到模块的MAC地址：" + this.dceMac);
                    //已进入PDA配置模式，还未获取到有效的MAC地址
                    if (EmptyUtils.isNotEmpty(this.listener)) {
                        this.listener.get().onDETMacChanged(this.dceMac);
                    }
                }
            } else {
                //已进入PDA配置模式，循环判断是否已经配置新的BE码
                String code = modules.getCode();
                if (EmptyUtils.isNotEmpty(code) && !"BEFFFFFFFFFFFFFFFFFF".equals(code)) {
                    this.code = code;
                    PWLogger.e("模块的BE码已经配置完成：" + this.code);
                    if (EmptyUtils.isNotEmpty(this.listener)) {
                        this.listener.get().onDeviceCodeChanged(this.code);
                    }
                    PWLogger.e("退出配置模");
                    this.serialPort.quitConfigModel();
                } else {
                    PWLogger.e("模块的BE码未配置，继续查询模块参数");
                    this.handler.sendEmptyMessageDelayed(RSMS_QUERY_MODULES_MSG, 2000);
                }
            }
        }
    }

    @Override
    public void onRSMSEnterConfigReceived(RSMSEnterConfigResponseEntity response) {
        if (response.getResponse() == 0x01) {
            if (response.getConfigModel() == (byte) 0xB0) {
                PWLogger.e("进入串口配置成功");
                this.dte = true;
                if (EmptyUtils.isNotEmpty(this.listener)) {
                    this.listener.get().onDTEConfigEntered();
                }
            } else {
                PWLogger.e("进入PDA配置成功");
                this.pda = true;
                PWLogger.e("清空模块缓存数据");
                this.serialPort.clearCache();
                if (EmptyUtils.isNotEmpty(this.listener)) {
                    this.listener.get().onPDAConfigEntered();
                }
                PWLogger.e("开始查询模块参数");
                this.handler.sendEmptyMessageDelayed(RSMS_QUERY_MODULES_MSG, 2000);
            }
        }
    }

    @Override
    public void onRSMSQuitConfigReceived(RSMSCommontResponseEntity response) {
        if (response.getResponse() == 0x01) {
            if (this.pda) {
                this.pda = false;
                PWLogger.e("退出PDA配置成功");
                if (EmptyUtils.isNotEmpty(this.listener)) {
                    this.listener.get().onPDAConfigQuited();
                }
            } else if (this.dte) {
                this.dte = false;
                PWLogger.e("退出DTE配置成功");
                if (EmptyUtils.isNotEmpty(this.listener)) {
                    this.listener.get().onDTEConfigQuited();
                }
            }
            this.handler.sendEmptyMessageDelayed(RSMS_QUERY_STATUS_MSG, 2000);
        }
    }

    @Override
    public void onRSMSDataCollectionReceived() {
        PWLogger.e("采集数据成功");
        this.collection = false;
        this.lastTime = System.currentTimeMillis();
    }

    private class RSMSHandler extends Handler {
        public RSMSHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RSMS_QUERY_STATUS_MSG://查询模块参数
                    if (EmptyUtils.isNotEmpty(serialPort)) {
                        serialPort.queryStatus();
                    }
                    break;
                case RSMS_QUERY_MODULES_MSG://查询状态参数
                    if (EmptyUtils.isNotEmpty(serialPort)) {
                        serialPort.queryModules();
                    }
                    break;
            }
        }
    }
}
