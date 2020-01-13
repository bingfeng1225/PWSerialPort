package cn.haier.bio.medical.serialport.rsms.entity.recv;

public class RSMSStatusEntity {
    private byte model; //联网模式
    private byte status;//模块状态码
    private byte encode;//编码规则状态码
    private byte level; //4G信号强度
    private byte wifiLevel; //WIFI信号强度
    private short uploadFrequency;//上传频率
    private short acquisitionFrequency;//采集频率
    private byte year; //年
    private byte month;//月
    private byte day;//日
    private byte hour;//时
    private byte minute;//分
    private byte second;//秒

    public RSMSStatusEntity() {

    }

    public byte getModel() {
        return model;
    }

    public void setModel(byte model) {
        this.model = model;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public byte getEncode() {
        return encode;
    }

    public void setEncode(byte encode) {
        this.encode = encode;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public byte getWifiLevel() {
        return wifiLevel;
    }

    public void setWifiLevel(byte wifiLevel) {
        this.wifiLevel = wifiLevel;
    }

    public short getUploadFrequency() {
        return uploadFrequency;
    }

    public void setUploadFrequency(short uploadFrequency) {
        this.uploadFrequency = uploadFrequency;
    }

    public short getAcquisitionFrequency() {
        return acquisitionFrequency;
    }

    public void setAcquisitionFrequency(short acquisitionFrequency) {
        this.acquisitionFrequency = acquisitionFrequency;
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


    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("联网模式：" + this.model + "\n");

        //状态指示
        buffer.append("4G初始化状态0：" + ((status & 0x80) == 0x80) + "\n");
        buffer.append("4G注册联网状态1：" + ((status & 0x40) == 0x40) + "\n");
        buffer.append("SIM监测状态2：" + ((status & 0x20) == 0x20) + "\n");
        buffer.append("WIFI初始化状态3：" + ((status & 0x10) == 0x10) + "\n");
        buffer.append("WIFI联网状态4：" + ((status & 0x08) == 0x08) + "\n");
        buffer.append("WIFI检索状态5：" + ((status & 0x04) == 0x04) + "\n");
        buffer.append("DCE数据存储状态6：" + ((status & 0x02) == 0x02) + "\n");
        buffer.append("DCE准备状态7：" + ((status & 0x01) == 0x01)+ "\n");

        //编码规则指示
        buffer.append("设备识别码状态：" + ((status & 0x80) == 0x80) + "\n");
        buffer.append("BE码状态：" + ((status & 0x40) == 0x40) + "\n");

        buffer.append("4G信号强度：" + this.level + "\n");
        buffer.append("WIFI信号强度：" + this.wifiLevel + "\n");
        buffer.append("上传频率：" + this.uploadFrequency + "\n");
        buffer.append("采集频率：" + this.acquisitionFrequency + "\n");
        buffer.append("时间：" + this.year + "-" + this.month + "-" + this.day + " " + this.hour + ":" + this.minute + ":" + this.second + "\n");

        return buffer.toString();
    }
}
