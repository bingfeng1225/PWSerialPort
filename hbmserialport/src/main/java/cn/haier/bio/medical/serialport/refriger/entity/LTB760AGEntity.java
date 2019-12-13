package cn.haier.bio.medical.serialport.refriger.entity;

import cn.qd.peiwen.pwlogger.PWLogger;

public class LTB760AGEntity implements Cloneable {
    private LTB760AGAlarmEntity alarm;
    private LTB760AGBasicsEntity basics;
    private LTB760AGStatusEntity status;

    public LTB760AGEntity() {
        this.alarm = new LTB760AGAlarmEntity();
        this.basics = new LTB760AGBasicsEntity();
        this.status = new LTB760AGStatusEntity();
    }

    public LTB760AGAlarmEntity getAlarm() {
        return alarm;
    }

    public void setAlarm(LTB760AGAlarmEntity alarm) {
        this.alarm = alarm;
    }

    public LTB760AGBasicsEntity getBasics() {
        return basics;
    }

    public void setBasics(LTB760AGBasicsEntity basics) {
        this.basics = basics;
    }

    public LTB760AGStatusEntity getStatus() {
        return status;
    }

    public void setStatus(LTB760AGStatusEntity status) {
        this.status = status;
    }

    public LTB760AGEntity clone(){
        LTB760AGEntity entity = null;
        try {
            entity = (LTB760AGEntity)super.clone();
        }catch (Exception e){
            PWLogger.e(e);
        }
        return entity;
    }
}
