package cn.haier.bio.medical.serialport.rsms.entity.recv;

public class RSMSControlCommandEntity {
    private byte year; //年
    private byte month;//月
    private byte day;//日
    private byte hour;//时
    private byte minute;//分
    private byte second;//秒
    private short command;
    private byte[] control;


    public RSMSControlCommandEntity() {

    }

    public byte getYear() {
        return year;
    }

    public void setYear(byte year) {
        this.year = year;
    }

    public byte getMonth() {
        return month;
    }

    public void setMonth(byte month) {
        this.month = month;
    }

    public byte getDay() {
        return day;
    }

    public void setDay(byte day) {
        this.day = day;
    }

    public byte getHour() {
        return hour;
    }

    public void setHour(byte hour) {
        this.hour = hour;
    }

    public byte getMinute() {
        return minute;
    }

    public void setMinute(byte minute) {
        this.minute = minute;
    }

    public byte getSecond() {
        return second;
    }

    public void setSecond(byte second) {
        this.second = second;
    }

    public short getCommand() {
        return command;
    }

    public void setCommand(short command) {
        this.command = command;
    }

    public byte[] getControl() {
        return control;
    }

    public void setControl(byte[] control) {
        this.control = control;
    }
}
