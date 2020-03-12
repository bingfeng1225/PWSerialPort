package cn.haier.bio.medical.serialport.rsms;

import cn.haier.bio.medical.serialport.rsms.entity.send.IRSMSSendEntity;
import cn.haier.bio.medical.serialport.rsms.entity.send.RSMSAModelConfigEntity;
import cn.haier.bio.medical.serialport.rsms.entity.send.RSMSBModelConfigEentity;
import cn.haier.bio.medical.serialport.rsms.entity.send.RSMSDTEModelConfigEntity;
import cn.haier.bio.medical.serialport.rsms.listener.IRSMSListener;
import cn.qd.peiwen.pwtools.EmptyUtils;

public class RSMSCommandManager {
    private RSMSSerialPort serialPort;
    private static RSMSCommandManager manager;

    public static RSMSCommandManager getInstance() {
        if (manager == null) {
            synchronized (RSMSCommandManager.class) {
                if (manager == null)
                    manager = new RSMSCommandManager();
            }
        }
        return manager;
    }

    private RSMSCommandManager() {

    }

    public void init(byte[] mac, IRSMSListener listener) {
        if (EmptyUtils.isEmpty(this.serialPort)) {
            this.serialPort = new RSMSSerialPort();
            this.serialPort.init(mac, listener);
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

    public void queryPDAModules() {
        if (EmptyUtils.isNotEmpty(this.serialPort)) {
            this.serialPort.queryPDAModules();
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

    public void enterDTEConfigModel() {
        if (EmptyUtils.isNotEmpty(this.serialPort)) {
            this.serialPort.enterDTEConfigModel();
        }
    }

    public void enterPDAConfigModel() {
        if (EmptyUtils.isNotEmpty(this.serialPort)) {
            this.serialPort.enterPDAConfigModel();
        }
    }

    public void configDTEModel(RSMSDTEModelConfigEntity entity) {
        if (EmptyUtils.isNotEmpty(this.serialPort)) {
            this.serialPort.configDTEModel(entity);
        }
    }

    public void configAModel(RSMSAModelConfigEntity entity) {
        if (EmptyUtils.isNotEmpty(this.serialPort)) {
            this.serialPort.configAModel(entity);
        }
    }

    public void configBModel(RSMSBModelConfigEentity entity) {
        if (EmptyUtils.isNotEmpty(this.serialPort)) {
            this.serialPort.configBModel(entity);
        }
    }

    public void collectionDeviceData(IRSMSSendEntity entity) {
        if (EmptyUtils.isNotEmpty(this.serialPort)) {
            this.serialPort.collectionDeviceData(entity);
        }
    }

    public void release() {
        if (EmptyUtils.isNotEmpty(this.serialPort)) {
            this.serialPort.release();
            this.serialPort = null;
        }
    }
}
