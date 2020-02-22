package cn.haier.bio.medical.serialport.refriger;

import cn.qd.peiwen.pwtools.EmptyUtils;

/***
 * 超低温变频、T系列、双系统主控板通讯
 *
 */
public class LTB760AFGManager {
    private LTB760AFGSerialPort serialPort;
    private static LTB760AFGManager manager;

    public static LTB760AFGManager getInstance() {
        if (manager == null) {
            synchronized (LTB760AFGManager.class) {
                if (manager == null)
                    manager = new LTB760AFGManager();
            }
        }
        return manager;
    }

    private LTB760AFGManager() {

    }

    public void init(ILTB760AFGListener listener) {
        if(EmptyUtils.isEmpty(this.serialPort)){
            this.serialPort = new LTB760AFGSerialPort();
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
            this.serialPort.disable();
            this.serialPort = null;
        }
    }
}

