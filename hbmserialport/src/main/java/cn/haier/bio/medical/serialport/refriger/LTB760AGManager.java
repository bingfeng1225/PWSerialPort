package cn.haier.bio.medical.serialport.refriger;

import cn.qd.peiwen.pwtools.EmptyUtils;

/***
 * 超低温变频、T系列、双系统主控板通讯
 *
 */
public class LTB760AGManager {
    private LTB760AGSerialPort serialPort;
    private static LTB760AGManager manager;

    public static LTB760AGManager getInstance() {
        if (manager == null) {
            synchronized (LTB760AGManager.class) {
                if (manager == null)
                    manager = new LTB760AGManager();
            }
        }
        return manager;
    }

    private LTB760AGManager() {

    }

    public void init(ILTB760AGListener listener) {
        if(EmptyUtils.isEmpty(this.serialPort)){
            this.serialPort = new LTB760AGSerialPort();
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

