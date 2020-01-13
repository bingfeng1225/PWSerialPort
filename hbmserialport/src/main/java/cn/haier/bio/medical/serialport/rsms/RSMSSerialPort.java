package cn.haier.bio.medical.serialport.rsms;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.lang.ref.WeakReference;

import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSConfigModelResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSModulesEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSNetworkEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSStatusEntity;
import cn.haier.bio.medical.serialport.rsms.entity.send.IRSMSSendEntity;
import cn.haier.bio.medical.serialport.rsms.entity.send.RSMSConfigEntity;
import cn.haier.bio.medical.serialport.rsms.entity.send.RSMSConfigModelEntity;
import cn.haier.bio.medical.serialport.rsms.entity.send.RSMSQueryStatusEntity;
import cn.haier.bio.medical.serialport.rsms.tools.RSMSTools;
import cn.haier.bio.medical.serialport.tools.ByteBufTools;
import cn.qd.peiwen.pwlogger.PWLogger;
import cn.qd.peiwen.pwtools.ByteUtils;
import cn.qd.peiwen.pwtools.EmptyUtils;
import cn.qd.peiwen.serialport.PWSerialPortHelper;
import cn.qd.peiwen.serialport.PWSerialPortListener;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class RSMSSerialPort implements PWSerialPortListener {
    private ByteBuf buffer;
    private RSMSHandler handler;
    private HandlerThread thread;
    private PWSerialPortHelper helper;

    private byte[] mac;
    private String code;
    private boolean ready = false;
    private boolean enabled = false;
    private WeakReference<IRSMSListener> listener;

    public RSMSSerialPort() {

    }

    public void init(String code, byte[] mac, IRSMSListener listener) {
        createHandler();
        createBuffer();
        createHelper();
        this.mac = RSMSTools.generateMac(mac);
        this.code = RSMSTools.generateCode(code);
        this.listener = new WeakReference<>(listener);
    }

    public void enable() {
        if (this.isInitialized() && !this.enabled) {
            this.enabled = true;
            this.helper.open();
        }
    }

    public void disable() {
        if (this.isInitialized() && this.enabled) {
            this.enabled = false;
            this.helper.close();
        }
    }

    public void queryStatus() {
        RSMSQueryStatusEntity entity = new RSMSQueryStatusEntity();
        entity.setMac(this.mac);
        entity.setCode(this.code);
        entity.setMcu(RSMSTools.DEFAULT_MAC);
        this.sendCommand(RSMSTools.RSMS_COMMAND_QUERY_STATUS, entity);
    }

    public void queryNetwork() {
        this.sendCommand(RSMSTools.RSMS_COMMAND_QUERY_NETWORK);
    }

    public void queryModules() {
        this.sendCommand(RSMSTools.RSMS_COMMAND_QUERY_MODULES);
    }

    public void recovery() {
        this.sendCommand(RSMSTools.RSMS_COMMAND_CONFIG_RECOVERY);
    }

    public void clearCache() {
        this.sendCommand(RSMSTools.RSMS_COMMAND_CONFIG_CLEAR_CACHE);
    }

    public void quitConfigModel() {
        this.sendCommand(RSMSTools.RSMS_COMMAND_CONFIG_QUIT);
    }

    public void enterConfigModel(boolean pda) {
        this.sendCommand(RSMSTools.RSMS_COMMAND_CONFIG_ENTER,new RSMSConfigModelEntity(pda));
    }

    public void configNetwork(RSMSConfigEntity entity) {
        this.sendCommand(RSMSTools.RSMS_COMMAND_CONFIG_NETWORK, entity);
    }

    public void collectionDeviceData(IRSMSSendEntity entity) {
        this.sendCommand(RSMSTools.RSMS_COMMAND_COLLECTION_DATA,entity);
    }

    public void release() {
        this.destoryHandler();
        this.destoryHelper();
        this.destoryBuffer();
    }

    private boolean isInitialized() {
        if (EmptyUtils.isEmpty(this.handler)) {
            return false;
        }
        if (EmptyUtils.isEmpty(this.helper)) {
            return false;
        }
        if (EmptyUtils.isEmpty(this.buffer)) {
            return false;
        }
        return true;
    }

    private void createBuffer() {
        if (EmptyUtils.isEmpty(this.buffer)) {
            this.buffer = Unpooled.buffer(4);
        }
    }

    private void destoryBuffer() {
        if (EmptyUtils.isNotEmpty(this.buffer)) {
            this.buffer.release();
            this.buffer = null;
        }
    }

    private void createHelper() {
        if (EmptyUtils.isEmpty(this.helper)) {
            this.helper = new PWSerialPortHelper("RSMSManager");
            this.helper.setTimeout(0);
            this.helper.setPath("/dev/ttyS1");
            this.helper.setBaudrate(115200);
            this.helper.init(this);
        }
    }

    private void destoryHelper() {
        if (EmptyUtils.isNotEmpty(this.helper)) {
            this.helper.release();
            this.helper = null;
        }
    }

    private void createHandler() {
        if (EmptyUtils.isEmpty(this.thread) && EmptyUtils.isEmpty(this.handler)) {
            this.thread = new HandlerThread("RSMSManager");
            this.thread.start();
            this.handler = new RSMSHandler(this.thread.getLooper());
        }
    }

    private void destoryHandler() {
        if (EmptyUtils.isNotEmpty(this.thread)) {
            this.thread.quitSafely();
            this.thread = null;
            this.handler = null;
        }
    }

    private void sendCommand(int type) {
        this.sendCommand(type, null);
    }

    private void sendCommand(int type, IRSMSSendEntity entity) {
        byte[] data = RSMSTools.packageCommand(type, entity);
        this.write(data);
    }

    private void write(byte[] data) {
        String log = ByteUtils.bytes2HexString(data, true, ", ");
        PWLogger.d("指令发送:" + log);
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onMessageSended(log);
        }
        if (this.isInitialized() && this.enabled) {
            this.helper.write(data);
        }
    }

    @Override
    public void onConnected(PWSerialPortHelper helper) {
        if (!this.isInitialized() || !helper.equals(this.helper)) {
            return;
        }
        this.ready = false;
        this.buffer.clear();
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onRSMSConnected();
        }
    }

    @Override
    public void onException(PWSerialPortHelper helper) {
        if (!this.isInitialized() || !helper.equals(this.helper)) {
            return;
        }
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onRSMSException();
        }
    }

    @Override
    public void onByteReceived(PWSerialPortHelper helper, byte[] buffer, int length) throws IOException {
        if (!this.isInitialized() || !helper.equals(this.helper)) {
            return;
        }
        this.buffer.writeBytes(buffer, 0, length);
        while (this.buffer.readableBytes() > 4) {
            //帧头监测
            int headerIndex = ByteBufTools.indexOf(this.buffer, RSMSTools.HEADER);
            if (headerIndex == -1) {
                break;
            }
            if (headerIndex > 0) {
                //抛弃帧头以前的数据
                byte[] data = new byte[headerIndex];
                this.buffer.readBytes(data, 0, headerIndex);
                this.buffer.discardReadBytes();
                PWLogger.d("丢弃帧头前不合法数据：" + ByteUtils.bytes2HexString(data));
                continue;
            }
            //长度监测
            //数据长度 = type(1) + cmd(1) + data(n) + check(1)
            //总长度 = header(2) + len(2) + data(len) + tailer(2)
            short len = this.buffer.getShort(2);
            if (this.buffer.readableBytes() < len + 6) {
                break;
            }
            //帧尾监测
            int tailerIndex = ByteBufTools.indexOf(this.buffer, RSMSTools.TAILER);
            if (tailerIndex != len + 4) {
                //当前包尾位置错误 丢掉正常的包头以免重复判断
                this.buffer.skipBytes(2);
                this.buffer.discardReadBytes();
                PWLogger.d("帧尾位置不匹配，丢弃帧头，查找下一帧数据");
                continue;
            }
            this.buffer.markReaderIndex();
            byte[] data = new byte[len + 6];
            this.buffer.readBytes(data, 0, data.length);
            //校验和检验
            if (!RSMSTools.checkFrame(data)) {
                this.buffer.resetReaderIndex();
                this.buffer.skipBytes(2);
                this.buffer.discardReadBytes();
                PWLogger.d("校验和不匹配，丢弃帧头，查找下一帧数据");
                continue;
            }
            short type = this.buffer.getShort(4);
            this.buffer.discardReadBytes();
            String log = ByteUtils.bytes2HexString(data, true, ", ");
            PWLogger.d("指令接收:" + log);
            if (EmptyUtils.isNotEmpty(this.listener)) {
                this.listener.get().onMessageRecved(log);
            }
            switch (type) {
                case RSMSTools.RSMS_RESPONSE_QUERY_STATUS: {
                    RSMSStatusEntity entity = RSMSTools.parseRSMSStatusEntity(data);
                    if (EmptyUtils.isNotEmpty(this.listener)) {
                        this.listener.get().onRSMSStatusReceived(entity);
                    }
                    break;
                }
                case RSMSTools.RSMS_RESPONSE_QUERY_NETWORK: {
                    RSMSNetworkEntity entity = RSMSTools.parseRSMSNetworkEntity(data);
                    if (EmptyUtils.isNotEmpty(this.listener)) {
                        this.listener.get().onRSMSNetworReceived(entity);
                    }
                    break;
                }
                case RSMSTools.RSMS_RESPONSE_QUERY_MODULES: {
                    RSMSModulesEntity entity = RSMSTools.parseRSMSModulesEntity(data);
                    if (EmptyUtils.isNotEmpty(this.listener)) {
                        this.listener.get().onRSMSModulesReceived(entity);
                    }
                    break;
                }
                case RSMSTools.RSMS_RESPONSE_CONFIG_ENTER:{
                        RSMSConfigModelResponseEntity entity = RSMSTools.parseRSMSConfigModelResponseEntity(data);
                        if (EmptyUtils.isNotEmpty(this.listener)) {
                            this.listener.get().onRSMSEnterConfigReceived(entity);
                        }
                        break;
                }
                case RSMSTools.RSMS_RESPONSE_CONFIG_QUIT:{
                    RSMSResponseEntity entity = RSMSTools.parseRSMSResponseEntity(data);
                    if (EmptyUtils.isNotEmpty(this.listener)) {
                        this.listener.get().onRSMSQuitConfigReceived(entity);
                    }
                    break;
                }
                case RSMSTools.RSMS_RESPONSE_CONFIG_NETWORK:{
                    RSMSResponseEntity entity = RSMSTools.parseRSMSResponseEntity(data);
                    if (EmptyUtils.isNotEmpty(this.listener)) {
                        this.listener.get().onRSMSConfigNetworkReceived(entity);
                    }
                    break;
                }
                case RSMSTools.RSMS_RESPONSE_CONFIG_RECOVERY:{
                    RSMSResponseEntity entity = RSMSTools.parseRSMSResponseEntity(data);
                    if (EmptyUtils.isNotEmpty(this.listener)) {
                        this.listener.get().onRSMSRecoveryReceived(entity);
                    }
                    break;
                }
                case RSMSTools.RSMS_RESPONSE_CONFIG_CLEAR_CACHE: {
                    RSMSResponseEntity entity = RSMSTools.parseRSMSResponseEntity(data);
                    if (EmptyUtils.isNotEmpty(this.listener)) {
                        this.listener.get().onRSMSClearCacheReceived(entity);
                    }
                    break;
                }
                case RSMSTools.RSMS_RESPONSE_COLLECTION_DATA: {
                    if (EmptyUtils.isNotEmpty(this.listener)) {
                        this.listener.get().onRSMSDataCollectionReceived();
                    }
                    break;
                }
                default:
                    byte[] bytes = ByteUtils.short2Bytes(type);
                    if(EmptyUtils.isNotEmpty(this.listener)){
                        this.listener.get().onRSMSUnknownReceived();
                    }
                    PWLogger.d("指令" + ByteUtils.bytes2HexString(bytes, true) + "暂不支持");
                    break;
            }
        }
    }


    private class RSMSHandler extends Handler {
        public RSMSHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                default:
                    break;
            }
        }
    }
}
