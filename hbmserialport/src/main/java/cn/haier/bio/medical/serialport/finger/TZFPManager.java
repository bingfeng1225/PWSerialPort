package cn.haier.bio.medical.serialport.finger;

import java.util.List;

import cn.qd.peiwen.pwtools.EmptyUtils;

/***
 * 上海图正指纹模块
 *
 */
public class TZFPManager {
    private TZFPSerialPort serialPort;
    private static TZFPManager manager;

    public static TZFPManager getInstance() {
        if (manager == null) {
            synchronized (TZFPManager.class) {
                if (manager == null)
                    manager = new TZFPManager();
            }
        }
        return manager;
    }

    private TZFPManager() {

    }

    public void init(ITZFPListener listener) {
        if(EmptyUtils.isEmpty(this.serialPort)){
            this.serialPort = new TZFPSerialPort();
            this.serialPort.init(listener);
        }
    }

    public void enable() {
        if(EmptyUtils.isNotEmpty(this.serialPort)){
            this.serialPort.enable();
        }
    }

    public void disable() {
        if(EmptyUtils.isNotEmpty(this.serialPort)){
            this.serialPort.disable();
        }
    }

    public void regist() {
        if(EmptyUtils.isNotEmpty(this.serialPort)){
            this.serialPort.regist();
        }
    }

    public void uplaod(List<String> fileList) {
        if(EmptyUtils.isNotEmpty(this.serialPort)){
            this.serialPort.uplaod(fileList);
        }
    }

    public void download(String filePath) {
        if(EmptyUtils.isNotEmpty(this.serialPort)){
            this.serialPort.download(filePath);
        }
    }

    public boolean isBusy() {
        if(EmptyUtils.isNotEmpty(this.serialPort)){
            this.serialPort.isBusy();
        }
        return false;
    }


    public void release() {
        if(EmptyUtils.isNotEmpty(this.serialPort)){
            this.serialPort.enable();
            this.serialPort = null;
        }
    }
}

