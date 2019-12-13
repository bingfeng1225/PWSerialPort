package cn.haier.bio.medical.serialport.rsms;

import cn.haier.bio.medical.serialport.rsms.entity.send.RSMSConfigEntity;
import cn.qd.peiwen.pwtools.EmptyUtils;

public class RSMSManager {

    private RSMSSerialPort serialPort;
    private static RSMSManager manager;

    public static RSMSManager getInstance() {
        if (manager == null) {
            synchronized (RSMSManager.class) {
                if (manager == null)
                    manager = new RSMSManager();
            }
        }
        return manager;
    }

    private RSMSManager(){

    }

    public void init(String code, byte[] mac, IRSMSListener listener) {
        if (EmptyUtils.isEmpty(this.serialPort)) {
            this.serialPort = new RSMSSerialPort();
            this.serialPort.init(code, mac, listener);
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

    public void queryStatus() {
        if (EmptyUtils.isNotEmpty(this.serialPort)) {
            this.serialPort.queryStatus();
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

    public void quitConfigModel() {
        if (EmptyUtils.isNotEmpty(this.serialPort)) {
            this.serialPort.quitConfigModel();
        }
    }

    public void enterConfigModel() {
        if (EmptyUtils.isNotEmpty(this.serialPort)) {
            this.serialPort.enterConfigModel();
        }
    }

    public void configNetwork(RSMSConfigEntity entity) {
        if (EmptyUtils.isNotEmpty(this.serialPort)) {
            this.serialPort.configNetwork(entity);
        }
    }

    public void release() {
        if (EmptyUtils.isNotEmpty(this.serialPort)) {
            this.serialPort.release();
            this.serialPort = null;
        }
    }
}
