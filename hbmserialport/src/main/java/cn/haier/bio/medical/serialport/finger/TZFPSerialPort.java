package cn.haier.bio.medical.serialport.finger;

import android.os.Build;
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

import cn.haier.bio.medical.serialport.finger.hub.HUBController;
import cn.haier.bio.medical.serialport.finger.tools.TZFPTools;
import cn.qd.peiwen.pwlogger.PWLogger;
import cn.qd.peiwen.pwtools.ByteUtils;
import cn.qd.peiwen.pwtools.EmptyUtils;
import cn.qd.peiwen.pwtools.FileUtils;
import cn.qd.peiwen.pwtools.ThreadUtils;
import cn.qd.peiwen.serialport.PWSerialPortHelper;
import cn.qd.peiwen.serialport.PWSerialPortListener;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class TZFPSerialPort implements PWSerialPortListener {
    private ByteBuf buffer;
    private HandlerThread thread;
    private TZFPHandler handler;
    private HUBController controller;
    private PWSerialPortHelper helper;

    private int state;
    private boolean ready = false;
    private boolean enabled = false;
    private WeakReference<ITZFPListener> listener;

    private int currentIndex = 0;
    private String filePath = null;
    private List<String> fileList = null;
    private List<Integer> fingerList = null;


    public TZFPSerialPort() {
    }

    public void init(ITZFPListener listener) {
        this.createHandler();
        this.createHelper();
        this.createBuffer();
        this.state = TZFPTools.FINGER_STATE_DISABLED;
        this.listener = new WeakReference<>(listener);
    }

    public void enable() {
        if (this.isInitialized() && !this.enabled) {
            this.enabled = true;
            this.helper.open();
            this.state = TZFPTools.FINGER_STATE_REGIST_MODEL;
            if ("magton".equals(Build.MODEL)) {
                createController();
            }
        }
    }

    public void disable() {
        if (this.enabled && this.isInitialized()) {
            this.state = TZFPTools.FINGER_STATE_DISABLED;
            this.enabled = false;
            this.helper.close();
            if ("magton".equals(Build.MODEL)) {
                destoryController();
            }
        }
    }

    public void regist() {
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onRegistStated();
        }
        this.changeFingerPrintState(TZFPTools.FINGER_STATE_REGIST);
    }

    public void uplaod(List<String> fileList) {
        this.currentIndex = -1;
        this.fileList = fileList;
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onUploadStated();
        }
        this.changeFingerPrintState(TZFPTools.FINGER_STATE_UPLOAD);
    }

    public void download(String filePath) {
        this.filePath = filePath;
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onDownloadStated();
        }
        this.changeFingerPrintState(TZFPTools.FINGER_STATE_DOWNLOAD);
    }

    public boolean isBusy() {
        return (this.state != TZFPTools.FINGER_STATE_COMPARE);
    }

    public void release() {
        this.listener = null;
        this.state = TZFPTools.FINGER_STATE_DISABLED;
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

    private void createHelper() {
        if (EmptyUtils.isEmpty(this.helper)) {
            this.helper = new PWSerialPortHelper("TZFPSerialPort");
            this.helper.setTimeout(10);
            this.helper.setPath("/dev/ttyUSB0");
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
            this.thread = new HandlerThread("TZFPSerialPort");
            this.thread.start();
            this.handler = new TZFPHandler(this.thread.getLooper());
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

    private void createController() {
        if (EmptyUtils.isEmpty(this.controller)) {
            this.controller = new HUBController();
            this.controller.init();
        }
    }

    private void destoryController() {
        if (EmptyUtils.isNotEmpty(this.controller)) {
            this.controller.release();
            this.controller = null;
        }
    }

    private void write(byte[] data) {
        PWLogger.d("指令发送:" + ByteUtils.bytes2HexString(data, true, ", "));
        if (this.isInitialized() && this.enabled) {
            this.helper.write(data);
        }
    }

    private void sendCommand(int type) {
        this.sendCommand(type, 0);
    }

    private void sendCommand(int type, int param) {
        byte[] data = TZFPTools.packageCommand(type, param);
        this.write(data);
    }

    private void changeFingerPrintState(int state) {
        this.state = state;
        PWLogger.d("中断模组当前操作");
        this.sendCommand(TZFPTools.FINGER_COMMAND_BREAK);
    }

    private void operationInterrupted() {
        switch (this.state) {
            case TZFPTools.FINGER_STATE_REGIST:
                PWLogger.d("设置抬手检测");
                this.sendCommand(TZFPTools.FINGER_COMMAND_REGIST_HAND_DETECTION);
                break;
            case TZFPTools.FINGER_STATE_UPLOAD:
                PWLogger.d("清空所有已注册指纹");
                this.sendCommand(TZFPTools.FINGER_COMMAND_CLEAR);
                break;
            case TZFPTools.FINGER_STATE_DOWNLOAD:
                PWLogger.d("查询所有已注册指纹");
                this.sendCommand(TZFPTools.FINGER_COMMAND_SEARCH_FINGER);
                break;
            case TZFPTools.FINGER_STATE_REGIST_MODEL:
                PWLogger.d("设置拒绝重复注册");
                this.sendCommand(TZFPTools.FINGER_COMMAND_REGIST_REFUSE_REPEAT);
                break;
            default: {
                PWLogger.d("开始指纹识别");
                this.handler.sendEmptyMessageDelayed(TZFPTools.FINGER_COMMAND_COMPARE, 1000);
                break;
            }
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

    private void uplaodFinger() throws IOException {
        String filePath = this.fileList.get(this.currentIndex);
        PWLogger.d("指纹上传开始:" + filePath);
        this.sendCommand(TZFPTools.FINGER_COMMAND_UPLOAD, 8195);
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
            this.write(data);
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
            if (EmptyUtils.isNotEmpty(this.listener)) {
                this.listener.get().onUploadSuccessed();
            }
            this.changeFingerPrintState(TZFPTools.FINGER_STATE_COMPARE);
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
            if (EmptyUtils.isNotEmpty(this.listener)) {
                this.listener.get().onUploadFailured();
            }
            this.changeFingerPrintState(TZFPTools.FINGER_STATE_COMPARE);
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
            this.changeFingerPrintState(TZFPTools.FINGER_STATE_COMPARE);
        } else if (status == 0x18) {
            PWLogger.d("指纹比对中断");
            this.operationInterrupted();
        } else {
            if (finger == 0) {
                PWLogger.d("指纹未注册");
                if (EmptyUtils.isNotEmpty(this.listener)) {
                    this.listener.get().onFingerUNRegistered();
                }
            } else {
                PWLogger.d("指纹比对成功");
                if (EmptyUtils.isNotEmpty(this.listener)) {
                    this.listener.get().onFingerRecognized(finger);
                }
            }
            this.changeFingerPrintState(TZFPTools.FINGER_STATE_COMPARE);
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
                if (!TZFPTools.checkFrame(data)) {
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
            if (EmptyUtils.isNotEmpty(this.listener)) {
                this.listener.get().onDownloadFailured();
            }
            this.changeFingerPrintState(TZFPTools.FINGER_STATE_COMPARE);
        }
    }

    private void processRegistFirstCommand() {
        this.buffer.skipBytes(8);
        this.buffer.discardReadBytes();
        PWLogger.d("指纹注册第二步开始");
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onRegistStepChanged(2);
        }
        this.handler.sendEmptyMessageDelayed(TZFPTools.FINGER_COMMAND_REGIST_SECOND, 1000);
    }

    private void processRegistSecondCommand() {
        this.buffer.skipBytes(8);
        this.buffer.discardReadBytes();
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onRegistStepChanged(3);
        }
        PWLogger.d("指纹注册第三步开始");
        this.handler.sendEmptyMessageDelayed(TZFPTools.FINGER_COMMAND_REGIST_THIRD, 1000);
    }

    private void processRegistThirdCommand() {
        this.buffer.skipBytes(2);
        int finger = this.buffer.readShort();
        int status = this.buffer.readByte();
        this.buffer.skipBytes(3);
        this.buffer.discardReadBytes();
        this.changeFingerPrintState(TZFPTools.FINGER_STATE_COMPARE);
        if (status == 0x00) {//注册成功
            PWLogger.d("指纹注册成功");
            if (EmptyUtils.isNotEmpty(this.listener)) {
                this.listener.get().onRegistSuccessed(finger);
            }
        } else if (status == 0x08) {//超时
            PWLogger.d("指纹注册超时");
            if (EmptyUtils.isNotEmpty(this.listener)) {
                this.listener.get().onRegistTimeout();
            }
        } else if (status == 0x07) {//重复注册
            PWLogger.d("指纹注册重复");
            if (EmptyUtils.isNotEmpty(this.listener)) {
                this.listener.get().onFingerAlreadyExists();
            }
        } else {//注册失败
            PWLogger.d("指纹注册失败");
            if (EmptyUtils.isNotEmpty(this.listener)) {
                this.listener.get().onRegistFailured();
            }
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
            if (this.state == TZFPTools.FINGER_STATE_DOWNLOAD) {
                PWLogger.d("指纹下载开始：" + finger);
                this.sendCommand(TZFPTools.FINGER_COMMAND_DOWNLOAD, finger);
            } else {
                if (this.isFingerValid(finger)) {
                    PWLogger.d("发现已绑定用户指纹：" + finger);
                    this.processFingerList();
                } else {
                    PWLogger.d("删除未绑定用户指纹：" + finger);
                    this.sendCommand(TZFPTools.FINGER_COMMAND_DELETE, finger);
                }
            }
        } else {
            if (this.state == TZFPTools.FINGER_STATE_REGIST) {
                PWLogger.d("指纹注册第一步开始");
                if (EmptyUtils.isNotEmpty(this.listener)) {
                    this.listener.get().onRegistStepChanged(1);
                }
                this.sendCommand(TZFPTools.FINGER_COMMAND_REGIST_FIRST);
            } else {
                PWLogger.d("指纹下载完毕");
                this.fireDownloadSuccessed();
                this.changeFingerPrintState(TZFPTools.FINGER_STATE_COMPARE);
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
            if (this.state == TZFPTools.FINGER_STATE_REGIST) {
                PWLogger.d("指纹注册第一步开始");
                if (EmptyUtils.isNotEmpty(this.listener)) {
                    this.listener.get().onRegistStepChanged(1);
                }
                this.sendCommand(TZFPTools.FINGER_COMMAND_REGIST_FIRST);
            } else if (this.state == TZFPTools.FINGER_STATE_DOWNLOAD) {
                PWLogger.d("无可下载的指纹特征");
                if (EmptyUtils.isNotEmpty(this.listener)) {
                    this.listener.get().onNoFingerExist();
                }
                this.changeFingerPrintState(TZFPTools.FINGER_STATE_COMPARE);
            }
        }
    }

    private void processRefuseRepeatCommand() {
        this.buffer.skipBytes(8);
        this.buffer.discardReadBytes();
        this.changeFingerPrintState(TZFPTools.FINGER_STATE_COMPARE);
    }

    private void processHandDetectionCommand() {
        this.buffer.skipBytes(8);
        this.buffer.discardReadBytes();
        PWLogger.d("查询所有已注册指纹");
        this.sendCommand(TZFPTools.FINGER_COMMAND_SEARCH_FINGER);
    }


    @Override
    public void onConnected(PWSerialPortHelper helper) {
        if (!this.isInitialized() || !helper.equals(this.helper)) {
            return;
        }
        this.ready = false;
        this.buffer.clear();
        if(EmptyUtils.isNotEmpty(this.listener)){
            this.listener.get().onTZFPConnected();
        }
        this.changeFingerPrintState(TZFPTools.FINGER_STATE_REGIST_MODEL);
    }

    @Override
    public void onException(PWSerialPortHelper helper) {
        if (!this.isInitialized() || !helper.equals(this.helper)) {
            return;
        }
        if (EmptyUtils.isNotEmpty(this.listener)) {
            this.listener.get().onTZFPException();
        }
        if (this.enabled) {
            if ("magton".equals(Build.MODEL)) {
                this.controller.reset();
            } else {
                TZFPTools.resetFingerPrint();
            }
            if (this.state == TZFPTools.FINGER_STATE_REGIST) {
                PWLogger.d("指纹注册失败");
                if (EmptyUtils.isNotEmpty(this.listener)) {
                    this.listener.get().onRegistFailured();
                }
            } else if (this.state == TZFPTools.FINGER_STATE_UPLOAD) {
                PWLogger.d("指纹上传失败");
                if (EmptyUtils.isNotEmpty(this.listener)) {
                    this.listener.get().onUploadFailured();
                }
            } else if (this.state == TZFPTools.FINGER_STATE_DOWNLOAD) {
                PWLogger.d("指纹下载失败");
                if (EmptyUtils.isNotEmpty(this.listener)) {
                    this.listener.get().onDownloadFailured();
                }
            }
        }
    }

    @Override
    public void onByteReceived(PWSerialPortHelper helper, byte[] buffer, int length) throws IOException {
        if (!this.isInitialized() || !helper.equals(this.helper)) {
            return;
        }
        if (!this.ready) {
            this.ready = true;
            if (EmptyUtils.isNotEmpty(this.listener)) {
                this.listener.get().onTZFPReady();
            }
        }
        this.buffer.writeBytes(buffer, 0, length);
        if (this.buffer.readableBytes() < 8) {
            return;
        }
        this.buffer.markReaderIndex();
        byte[] data = new byte[8];
        this.buffer.readBytes(data, 0, 8);
        this.buffer.resetReaderIndex();
        if (!TZFPTools.checkFrame(data)) {
            return;
        }
        PWLogger.d("指令接收:" + ByteUtils.bytes2HexString(data, true, ", "));
        int command = data[1] & 0xff;
        switch (command) {
            case TZFPTools.FINGER_COMMAND_CLEAR://删除所有已注册指纹
                this.processClearCommand();
                break;
            case TZFPTools.FINGER_COMMAND_BREAK://中断当前操作
                this.processBreakCommand();
                break;
            case TZFPTools.FINGER_COMMAND_UPLOAD://中断当前操作
                this.processUploadCommand();
                break;
            case TZFPTools.FINGER_COMMAND_DELETE:
                this.processDeleteCommand();
                break;
            case TZFPTools.FINGER_COMMAND_COMPARE:
                this.processCompareCommand();
                break;
            case TZFPTools.FINGER_COMMAND_DOWNLOAD:
                this.processDownloadCommand();
                break;
            case TZFPTools.FINGER_COMMAND_REGIST_FIRST://指纹注册第一步结果
                this.processRegistFirstCommand();
                break;
            case TZFPTools.FINGER_COMMAND_REGIST_SECOND://指纹注册第二步结果
                this.processRegistSecondCommand();
                break;
            case TZFPTools.FINGER_COMMAND_REGIST_THIRD: //指纹注册第三步结果
                this.processRegistThirdCommand();
                break;
            case TZFPTools.FINGER_COMMAND_SEARCH_FINGER://查询所有已经注册的指纹
                this.processSearchFingerCommand();
                break;
            case TZFPTools.FINGER_COMMAND_REGIST_REFUSE_REPEAT://设置拒绝重复注册
                this.processRefuseRepeatCommand();
                break;
            case TZFPTools.FINGER_COMMAND_REGIST_HAND_DETECTION://设置抬手检测
                this.processHandDetectionCommand();
                break;
            default:
                this.processUnknownCommand();
                break;
        }
    }

    private class TZFPHandler extends Handler {
        public TZFPHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x0c:
                    if (state == TZFPTools.FINGER_STATE_COMPARE) {
                        sendCommand(TZFPTools.FINGER_COMMAND_COMPARE);
                    }
                    break;
                case 0x02:
                    if (state == TZFPTools.FINGER_STATE_REGIST) {
                        sendCommand(TZFPTools.FINGER_COMMAND_REGIST_SECOND);
                    }
                    break;
                case 0x03:
                    if (state == TZFPTools.FINGER_STATE_REGIST) {
                        sendCommand(TZFPTools.FINGER_COMMAND_REGIST_THIRD);
                    }
                    break;
            }
        }
    }
}