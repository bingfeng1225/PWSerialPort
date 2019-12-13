package cn.haier.bio.medical.serialport.rsms.entity.recv;

public class RSMSNetworkEntity {
    private byte[] mcu;//MCU识别码
    private byte model;//联网模式

    private String port;//4G端口
    private String domain;//4G服务器域名
    private String address;//4G服务器IP地址

    private String wifiPort;//WIFI端口
    private String wifiDomain;//WIFI服务器域名
    private String wifiAddress;//WIFI服务器IP地址

    private String wifiName;//WIFI名称
    private String wifiPassword;//WIFI密码

    private String apn;//APN
    private String apnName;//APN用户名
    private String apnPassword;//APN密码

    private short nomalFrequency;//普通数据上传频率（单位：S）
    private short alarmFrequency;//报警数据上传频率（单位：S）

    public RSMSNetworkEntity() {

    }

    public byte[] getMcu() {
        return mcu;
    }

    public void setMcu(byte[] mcu) {
        this.mcu = mcu;
    }

    public byte getModel() {
        return model;
    }

    public void setModel(byte model) {
        this.model = model;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWifiPort() {
        return wifiPort;
    }

    public void setWifiPort(String wifiPort) {
        this.wifiPort = wifiPort;
    }

    public String getWifiDomain() {
        return wifiDomain;
    }

    public void setWifiDomain(String wifiDomain) {
        this.wifiDomain = wifiDomain;
    }

    public String getWifiAddress() {
        return wifiAddress;
    }

    public void setWifiAddress(String wifiAddress) {
        this.wifiAddress = wifiAddress;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public String getWifiPassword() {
        return wifiPassword;
    }

    public void setWifiPassword(String wifiPassword) {
        this.wifiPassword = wifiPassword;
    }

    public String getApn() {
        return apn;
    }

    public void setApn(String apn) {
        this.apn = apn;
    }

    public String getApnName() {
        return apnName;
    }

    public void setApnName(String apnName) {
        this.apnName = apnName;
    }

    public String getApnPassword() {
        return apnPassword;
    }

    public void setApnPassword(String apnPassword) {
        this.apnPassword = apnPassword;
    }

    public short getNomalFrequency() {
        return nomalFrequency;
    }

    public void setNomalFrequency(short nomalFrequency) {
        this.nomalFrequency = nomalFrequency;
    }

    public short getAlarmFrequency() {
        return alarmFrequency;
    }

    public void setAlarmFrequency(short alarmFrequency) {
        this.alarmFrequency = alarmFrequency;
    }
}
