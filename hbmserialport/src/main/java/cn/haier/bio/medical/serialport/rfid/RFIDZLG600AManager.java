package cn.haier.bio.medical.serialport.rfid;

import cn.qd.peiwen.pwtools.EmptyUtils;

/***
 * ZLG600A
 * 集成电路卡读写器
 */
public class RFIDZLG600AManager {
    private RFIDZLG600ASerialPort serialPort;
    private static RFIDZLG600AManager manager;

    public static RFIDZLG600AManager getInstance() {
        if (manager == null) {
            synchronized (RFIDZLG600AManager.class) {
                if (manager == null)
                    manager = new RFIDZLG600AManager();
            }
        }
        return manager;
    }

    private RFIDZLG600AManager(){

    }

    public void init(IRFIDZLG600AListener listener) {
        if(EmptyUtils.isEmpty(this.serialPort)){
            this.serialPort = new RFIDZLG600ASerialPort();
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

    public void release() {
        if(EmptyUtils.isNotEmpty(this.serialPort)){
            this.serialPort.enable();
            this.serialPort = null;
        }
    }
}

