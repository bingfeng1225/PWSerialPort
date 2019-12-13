package cn.haier.bio.medical.serialport.refriger.entity;

public class LTB760AGAlarmEntity {
    private short alarmStatus1;//报警状态信息1  2
    private short alarmStatus2;//报警状态信息2  2

    public LTB760AGAlarmEntity() {

    }
    public short getAlarmStatus1() {
        return alarmStatus1;
    }

    public void setAlarmStatus1(short alarmStatus1) {
        this.alarmStatus1 = alarmStatus1;
    }

    public short getAlarmStatus2() {
        return alarmStatus2;
    }

    public void setAlarmStatus2(short alarmStatus2) {
        this.alarmStatus2 = alarmStatus2;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;//地址相等
        }
        if(obj == null){
            return false;//非空性：对于任意非空引用x，x.equals(null)应该返回false。
        }
        if(!(obj instanceof LTB760AGAlarmEntity)){
            return false;
        }
        LTB760AGAlarmEntity other = (LTB760AGAlarmEntity) obj;
        if(this.alarmStatus1 != other.alarmStatus1) {
            return false;
        }
        if(this.alarmStatus2 != other.alarmStatus2) {
            return false;
        }
        return true;
    }
}
