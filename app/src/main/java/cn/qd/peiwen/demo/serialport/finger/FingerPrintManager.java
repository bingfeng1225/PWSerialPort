package cn.qd.peiwen.demo.serialport.finger;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.qd.peiwen.demo.serialport.finger.listener.FingerPrintListener;
import cn.qd.peiwen.demo.serialport.finger.tools.FingerPrintTools;
import cn.qd.peiwen.demo.serialport.finger.types.FingerPrintCommond;
import cn.qd.peiwen.demo.serialport.finger.types.FingerPrintState;
import cn.qd.peiwen.pwtools.ByteUtils;
import cn.qd.peiwen.pwtools.EmptyUtils;
import cn.qd.peiwen.pwtools.FileUtils;
import cn.qd.peiwen.pwtools.ThreadUtils;
import cn.qd.peiwen.pwtools.logger.PWLogger;
import cn.qd.peiwen.serialport.PWSerialPortHelper;
import cn.qd.peiwen.serialport.PWSerialPortListener;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class FingerPrintManager implements PWSerialPortListener {
    private ByteBuf buffer;
    private HandlerThread thread;
    private FingerHandler handler;
    private PWSerialPortHelper helper;

    private boolean ready = false;
    private FingerPrintState state;
    private boolean enabled = false;
    private WeakReference<FingerPrintListener> listener;

    private int currentIndex = 0;
    private String filePath = null;
    private List<String> fileList = null;
    private List<Integer> fingerList = null;

    private static FingerPrintManager manager;

    public static FingerPrintManager getInstance() {
        if (manager == null) {
            synchronized (FingerPrintManager.class) {
                if (manager == null)
                    manager = new FingerPrintManager();
            }
        }
        return manager;
    }

    private FingerPrintManager() {

    }

    public void init(FingerPrintListener listener) {
        this.createHandler();
        this.createHelper();
        this.createBuffer();
        this.listener = new WeakReference<>(listener);
        this.state = FingerPrintState.FINGER_STATE_DISABLED;
    }

    public void enable() {
        if (this.isReady() && !this.enabled) {
            this.enabled = true;
            this.helper.open();
            this.state = FingerPrintState.FINGER_STATE_REGIST_MODEL;
        }
    }

    public void disable() {
        if (this.enabled && this.isReady()) {
            this.state = FingerPrintState.FINGER_STATE_DISABLED;
            this.enabled = false;
            this.helper.close();
        }
    }

    public void regist() {
        this.fireRegistStated();
        this.changeFingerPrintState(FingerPrintState.FINGER_STATE_REGIST);
    }

    public void uplaod(List<String> fileList) {
        this.currentIndex = -1;
        this.fileList = fileList;
        this.fireUploadStated();
        this.changeFingerPrintState(FingerPrintState.FINGER_STATE_UPLOAD);
    }

    public void download(String filePath) {
        this.filePath = filePath;
        this.fireDownloadStated();
        this.changeFingerPrintState(FingerPrintState.FINGER_STATE_DOWNLOAD);
    }

    public boolean isBusy() {
        return (this.state != FingerPrintState.FINGER_STATE_COMPARE);
    }

    public void release() {
        this.state = FingerPrintState.FINGER_STATE_DISABLED;
        this.destoryHandler();
        this.destoryHelper();
        this.destoryBuffer();
    }

    private boolean isReady() {
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

    private void createHelper() {
        if (EmptyUtils.isEmpty(this.helper)) {
            this.helper = new PWSerialPortHelper("FingerPrint");
            this.helper.setTimeout(10);
            this.helper.setPath("/dev/ttyUSB0");
            this.helper.setBaudrate(115200);
            this.helper.init(this);
        }
    }

    private boolean checkHelper(PWSerialPortHelper helper) {
        return (this.isReady() && this.helper.equals(helper));
    }

    private void destoryHelper() {
        if (EmptyUtils.isNotEmpty(this.helper)) {
            this.helper.release();
            this.helper = null;
        }
    }

    private void createHandler() {
        if (EmptyUtils.isEmpty(this.thread) && EmptyUtils.isEmpty(this.handler)) {
            this.thread = new HandlerThread("FingerPrint");
            this.thread.start();
            this.handler = new FingerHandler(this.thread.getLooper());
        }
    }

    private void destoryHandler() {
        if (EmptyUtils.isNotEmpty(this.thread)) {
            this.thread.quitSafely();
            this.thread = null;
            this.handler = null;
        }
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

    private void sendCommand(byte[] data) {
        if (this.isReady()) {
            this.helper.write(data);
        }
    }

    private void sendCommand(FingerPrintCommond type) {
        this.sendCommand(type, 0);
    }

    private void sendCommand(FingerPrintCommond type, int param) {
        byte[] data = FingerPrintTools.createFingerCommand(type, param);
        PWLogger.d("指令发送：" + ByteUtils.bytes2HexString(data));
        this.sendCommand(data);
    }

    private void changeFingerPrintState(FingerPrintState state) {
        this.state = state;
        PWLogger.d("中断模组当前操作");
        this.sendCommand(FingerPrintCommond.FINGER_COMMAND_BREAK);
    }

    private void operationInterrupted() {
        switch (this.state) {
            case FINGER_STATE_REGIST:
                PWLogger.d("设置抬手检测");
                this.sendCommand(FingerPrintCommond.FINGER_COMMAND_REGIST_HAND_DETECTION);
                break;
            case FINGER_STATE_UPLOAD:
                PWLogger.d("清空所有已注册指纹");
                this.sendCommand(FingerPrintCommond.FINGER_COMMAND_CLEAR);
                break;
            case FINGER_STATE_DOWNLOAD:
                PWLogger.d("查询所有已注册指纹");
                this.sendCommand(FingerPrintCommond.FINGER_COMMAND_SEARCH_FINGER);
                break;
            case FINGER_STATE_REGIST_MODEL:
                PWLogger.d("设置拒绝重复注册");
                this.sendCommand(FingerPrintCommond.FINGER_COMMAND_REGIST_REFUSE_REPEAT);
                break;
            default: {
                PWLogger.d("开始指纹识别");
                this.handler.sendEmptyMessageDelayed(FingerPrintCommond.FINGER_COMMAND_COMPARE.value, 1000);
                break;
            }
        }
    }

    private void fireRegistStated() {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onRegistStated();
        }
    }

    private void fireRegistTimeout() {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onRegistTimeout();
        }
    }

    private void fireRegistFailured() {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onRegistFailured();
        }
    }

    private void fireFingerAlreadyExists() {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onFingerAlreadyExists();
        }
    }

    private void fireRegistStepChanged(int step) {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onRegistStepChanged(step);
        }
    }


    private void fireRegistSuccessed(int finger) {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onRegistSuccessed(finger);
        }
    }

    private void fireUploadStated() {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onUploadStated();
        }
    }

    private void fireUploadFailured() {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onUploadFailured();
        }
    }

    private void fireUploadSuccessed() {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onUploadSuccessed();
        }
    }

    private void fireNoFingerExist() {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onNoFingerExist();
        }
    }

    private void fireDownloadStated() {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onDownloadStated();
        }
    }

    private void fireDownloadFailured() {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onDownloadFailured();
        }
    }

    private void fireDownloadSuccessed() {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onDownloadSuccessed();
        }
    }

    private boolean isFingerValid(int finger) {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            return this.listener.get().isFingerValid(finger);
        }
        return false;
    }

    private void fireFingerUNRegistered() {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onFingerUNRegistered();
        }
    }

    private void fireFingerRecognized(int finger) {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onFingerRecognized(finger);
        }
    }

    private void fireFingerPrintReady() {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onFingerPrintReady();
        }
    }

    private void fireFingerPrintException() {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onFingerPrintException();
        }
    }

    @Override
    public void onConnected(PWSerialPortHelper helper) {
        if (!checkHelper(helper)) {
            return;
        }
        this.ready = false;
        this.buffer.clear();
        this.changeFingerPrintState(FingerPrintState.FINGER_STATE_REGIST_MODEL);
    }

    @Override
    public void onException(PWSerialPortHelper helper) {
        if (!checkHelper(helper)) {
            return;
        }
        this.fireFingerPrintException();
        if (this.enabled) {
            FingerPrintTools.resetFingerPrint();
            if (this.state == FingerPrintState.FINGER_STATE_REGIST) {
                PWLogger.d("指纹注册失败");
                this.fireRegistFailured();
            } else if (this.state == FingerPrintState.FINGER_STATE_UPLOAD) {
                PWLogger.d("指纹上传失败");
                this.fireUploadFailured();
            } else if (this.state == FingerPrintState.FINGER_STATE_DOWNLOAD) {
                PWLogger.d("指纹下载失败");
                this.fireDownloadFailured();
            }
        }
    }

    @Override
    public void onByteReceived(PWSerialPortHelper helper, byte[] buffer) throws IOException {
        if (!checkHelper(helper)) {
            return;
        }
        if (!this.ready) {
            this.ready = true;
            this.fireFingerPrintReady();
        }
        this.buffer.writeBytes(buffer, 0, buffer.length);
        if (this.buffer.readableBytes() < 8) {
            return;
        }
        this.buffer.markReaderIndex();
        byte[] data = new byte[8];
        this.buffer.readBytes(data, 0, 8);
        this.buffer.resetReaderIndex();
        if (!FingerPrintTools.checkFrame(data)) {
            return;
        }
        PWLogger.d("指令回复：" + ByteUtils.bytes2HexString(data));
        FingerPrintCommond command = FingerPrintCommond.get(data[1] & 0xff);
        switch (command) {
            case FINGER_COMMAND_CLEAR://删除所有已注册指纹
                this.processClearCommand();
                break;
            case FINGER_COMMAND_BREAK://中断当前操作
                this.processBreakCommand();
                break;
            case FINGER_COMMAND_UPLOAD://中断当前操作
                this.processUploadCommand();
                break;
            case FINGER_COMMAND_DELETE:
                this.processDeleteCommand();
                break;
            case FINGER_COMMAND_COMPARE:
                this.processCompareCommand();
                break;
            case FINGER_COMMAND_DOWNLOAD:
                this.processDownloadCommand();
                break;
            case FINGER_COMMAND_REGIST_FIRST://指纹注册第一步结果
                this.processRegistFirstCommand();
                break;
            case FINGER_COMMAND_REGIST_SECOND://指纹注册第二步结果
                this.processRegistSecondCommand();
                break;
            case FINGER_COMMAND_REGIST_THIRD: //指纹注册第三步结果
                this.processRegistThirdCommand();
                break;
            case FINGER_COMMAND_SEARCH_FINGER://查询所有已经注册的指纹
                this.processSearchFingerCommand();
                break;
            case FINGER_COMMAND_REGIST_REFUSE_REPEAT://设置拒绝重复注册
                this.processRefuseRepeatCommand();
                break;
            case FINGER_COMMAND_REGIST_HAND_DETECTION://设置抬手检测
                this.processHandDetectionCommand();
                break;
            default:
                this.processUnknownCommand();
                break;
        }
    }

    private void uplaodFinger() throws IOException {
        String filePath = this.fileList.get(this.currentIndex);
        PWLogger.d("指纹上传开始:" + filePath);
        this.sendCommand(FingerPrintCommond.FINGER_COMMAND_UPLOAD, 8195);
        InputStream stream = new FileInputStream(filePath);
        while (stream.available() > 0) {
            byte[] data = null;
            int readable = stream.available();
            if (readable >= 128) {
                data = new byte[128];
                stream.read(data, 0, 128);
            } else {
                data = new byte[readable];
                stream.read(data, 0, readable);
            }
            this.sendCommand(data);
            ThreadUtils.sleep(30);//200
        }
        PWLogger.d("指纹上传完成:" + filePath);
        stream.close();
    }

    private void processUnknownCommand() {
        this.buffer.skipBytes(8);
        this.buffer.discardReadBytes();
        PWLogger.d("指令无法识别");
    }

    private void processFileList() throws IOException {
        this.currentIndex++;
        if (this.currentIndex < this.fileList.size()) {
            this.uplaodFinger();
        } else {
            PWLogger.d("指纹上传完毕");
            this.fireUploadSuccessed();
            this.changeFingerPrintState(FingerPrintState.FINGER_STATE_COMPARE);
        }
    }

    private void processClearCommand() throws IOException {
        this.buffer.skipBytes(8);
        this.buffer.discardReadBytes();
        PWLogger.d("所有指纹已删除");
        this.currentIndex = -1;
        this.processFileList();
    }

    private void processBreakCommand() {
        this.buffer.skipBytes(8);
        this.buffer.discardReadBytes();
        PWLogger.d("指纹操作中断");
        this.operationInterrupted();
    }

    private void processUploadCommand() throws IOException {
        this.buffer.skipBytes(4);
        int status = this.buffer.readByte();
        this.buffer.skipBytes(3);
        this.buffer.discardReadBytes();
        if (status == 0x00) {
            PWLogger.d("指纹上传成功:" + this.fileList.get(this.currentIndex));
            this.processFileList();
        } else {
            PWLogger.d("指纹上传失败");
            this.fireUploadFailured();
            this.changeFingerPrintState(FingerPrintState.FINGER_STATE_COMPARE);
        }
    }

    private void processDeleteCommand() {
        this.buffer.skipBytes(8);
        this.buffer.discardReadBytes();
        this.processFingerList();
    }

    private void processCompareCommand() {
        this.buffer.skipBytes(2);
        int finger = this.buffer.readShort();
        int status = this.buffer.readByte();
        this.buffer.skipBytes(3);
        this.buffer.discardReadBytes();
        if (status == 0x08) {
            PWLogger.d("指纹比对超时");
            this.changeFingerPrintState(FingerPrintState.FINGER_STATE_COMPARE);
        } else if (status == 0x18) {
            PWLogger.d("指纹比对中断");
            this.operationInterrupted();
        } else {
            if (finger == 0) {
                PWLogger.d("指纹未注册");
                this.fireFingerUNRegistered();
            } else {
                PWLogger.d("指纹比对成功");
                this.fireFingerRecognized(finger);
            }
            this.changeFingerPrintState(FingerPrintState.FINGER_STATE_COMPARE);
        }
    }

    private void processDownloadCommand() throws IOException {
        this.buffer.markReaderIndex();
        this.buffer.skipBytes(2);
        int length = this.buffer.readShort();
        int state = this.buffer.readByte();
        this.buffer.skipBytes(3);
        if (state == 0x00) {
            if (this.buffer.readableBytes() < length + 3) {
                this.buffer.resetReaderIndex();
            } else {
                byte[] data = new byte[length + 3];
                this.buffer.readBytes(data, 0, length + 3);
                this.buffer.discardReadBytes();
                if (!FingerPrintTools.checkFrame(data)) {
                    throw new IOException("Finger data format error");
                }
                int finger = this.fingerList.get(this.currentIndex);
                if (!FileUtils.writeFile(this.filePath + File.separator + finger + ".finger", data, data.length)) {
                    throw new IOException("Write finger file error");
                } else {
                    PWLogger.d("指纹下载完成：" + finger);
                    this.processFingerList();
                }
            }
        } else {
            PWLogger.d("指纹下载失败");
            this.fireDownloadFailured();
            this.changeFingerPrintState(FingerPrintState.FINGER_STATE_COMPARE);
        }
    }

    private void processRegistFirstCommand() {
        this.buffer.skipBytes(8);
        this.buffer.discardReadBytes();
        PWLogger.d("指纹注册第二步开始");
        this.fireRegistStepChanged(2);
        this.handler.sendEmptyMessageDelayed(FingerPrintCommond.FINGER_COMMAND_REGIST_SECOND.value, 1000);
    }

    private void processRegistSecondCommand() {
        this.buffer.skipBytes(8);
        this.buffer.discardReadBytes();
        this.fireRegistStepChanged(3);
        PWLogger.d("指纹注册第三步开始");
        this.handler.sendEmptyMessageDelayed(FingerPrintCommond.FINGER_COMMAND_REGIST_THIRD.value, 1000);
    }

    private void processRegistThirdCommand() {
        this.buffer.skipBytes(2);
        int finger = this.buffer.readShort();
        int status = this.buffer.readByte();
        this.buffer.skipBytes(3);
        this.buffer.discardReadBytes();
        this.changeFingerPrintState(FingerPrintState.FINGER_STATE_COMPARE);
        if (status == 0x00) {//注册成功
            PWLogger.d("指纹注册成功");
            this.fireRegistSuccessed(finger);
        } else if (status == 0x08) {//超时
            PWLogger.d("指纹注册超时");
            this.fireRegistTimeout();
        } else if (status == 0x07) {//重复注册
            PWLogger.d("指纹注册重复");
            this.fireFingerAlreadyExists();
        } else if (status == 0x18) {//打断
            PWLogger.d("指纹注册被打断");
            this.fireFingerAlreadyExists();
        } else {//注册失败
            PWLogger.d("指纹注册失败");
            this.fireRegistFailured();
        }
    }

    private List<Integer> parseFingerList() {
        this.buffer.skipBytes(1);
        int length = this.buffer.readShort();
        int number = length / 3;
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            int finger = this.buffer.readShort();
            int role = this.buffer.readByte();
            PWLogger.d("发现已注册指纹：" + finger + "，分组：" + role);
            list.add(finger);
        }
        this.buffer.skipBytes(2);
        return list;
    }

    private void processFingerList() {
        this.currentIndex++;
        if (this.currentIndex < this.fingerList.size()) {
            int finger = this.fingerList.get(this.currentIndex);
            if (this.state == FingerPrintState.FINGER_STATE_DOWNLOAD) {
                PWLogger.d("指纹下载开始：" + finger);
                this.sendCommand(FingerPrintCommond.FINGER_COMMAND_DOWNLOAD, finger);
            } else {
                if (this.isFingerValid(finger)) {
                    PWLogger.d("发现已绑定用户指纹：" + finger);
                    this.processFingerList();
                } else {
                    PWLogger.d("删除未绑定用户指纹：" + finger);
                    this.sendCommand(FingerPrintCommond.FINGER_COMMAND_DELETE, finger);
                }
            }
        } else {
            if (this.state == FingerPrintState.FINGER_STATE_REGIST) {
                PWLogger.d("指纹注册第一步开始");
                this.fireRegistStepChanged(1);
                this.sendCommand(FingerPrintCommond.FINGER_COMMAND_REGIST_FIRST);
            } else {
                PWLogger.d("指纹下载完毕");
                this.fireDownloadSuccessed();
                this.changeFingerPrintState(FingerPrintState.FINGER_STATE_COMPARE);
            }
        }
    }

    private void processSearchFingerCommand() {
        this.buffer.markReaderIndex();
        this.buffer.skipBytes(2);
        int length = this.buffer.readShort();
        int state = this.buffer.readByte();
        this.buffer.skipBytes(3);
        if (state == 0x00) {
            if (this.buffer.readableBytes() < length + 3) {
                this.buffer.resetReaderIndex();
            } else {
                this.fingerList = this.parseFingerList();
                this.buffer.discardReadBytes();
                this.currentIndex = -1;
                this.processFingerList();
            }
        } else {
            PWLogger.d("无已注册指纹");
            this.buffer.discardReadBytes();
            if (this.state == FingerPrintState.FINGER_STATE_REGIST) {
                PWLogger.d("指纹注册第一步开始");
                this.fireRegistStepChanged(1);
                this.sendCommand(FingerPrintCommond.FINGER_COMMAND_REGIST_FIRST);
            } else if (this.state == FingerPrintState.FINGER_STATE_DOWNLOAD) {
                PWLogger.d("无可下载的指纹特征");
                this.fireNoFingerExist();
                this.changeFingerPrintState(FingerPrintState.FINGER_STATE_COMPARE);
            }
        }
    }

    private void processRefuseRepeatCommand() {
        this.buffer.skipBytes(8);
        this.buffer.discardReadBytes();
        this.changeFingerPrintState(FingerPrintState.FINGER_STATE_COMPARE);
    }

    private void processHandDetectionCommand() {
        this.buffer.skipBytes(8);
        this.buffer.discardReadBytes();
        PWLogger.d("查询所有已注册指纹");
        this.sendCommand(FingerPrintCommond.FINGER_COMMAND_SEARCH_FINGER);
    }


    private class FingerHandler extends Handler {
        public FingerHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x0c:
                    if (state == FingerPrintState.FINGER_STATE_COMPARE) {
                        sendCommand(FingerPrintCommond.FINGER_COMMAND_COMPARE);
                    }
                    break;
                case 0x02:
                    if (state == FingerPrintState.FINGER_STATE_REGIST) {
                        sendCommand(FingerPrintCommond.FINGER_COMMAND_REGIST_SECOND);
                    }
                    break;
                case 0x03:
                    if (state == FingerPrintState.FINGER_STATE_REGIST) {
                        sendCommand(FingerPrintCommond.FINGER_COMMAND_REGIST_THIRD);
                    }
                    break;
            }
        }
    }
}

