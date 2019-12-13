package cn.haier.bio.medical.serialport.rsms.entity.send;

import cn.haier.bio.medical.serialport.rsms.tools.RSMSTools;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

//4G模式，仅填充4G参数
//WIFI模式，仅填充WIFI参数
//4G和WIFI模式，填充4G和WIFI参数
//增加国内国外标志，根据国内国外标志选择性填充APN参数

public class RSMSConfigEntity implements IRSMSSendEntity {
    private byte model;

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

    public RSMSConfigEntity() {

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

    @Override
    public byte[] packageSendMessage() {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeByte(model);
        buffer.writeBytes(RSMSTools.packageString(domain));
        buffer.writeBytes(RSMSTools.packageString(address));
        buffer.writeBytes(RSMSTools.packageString(port));
        buffer.writeBytes(RSMSTools.packageString(wifiDomain));
        buffer.writeBytes(RSMSTools.packageString(wifiAddress));
        buffer.writeBytes(RSMSTools.packageString(wifiPort));
        buffer.writeBytes(RSMSTools.packageString(wifiName));
        buffer.writeBytes(RSMSTools.packageString(wifiPassword));
        buffer.writeBytes(RSMSTools.packageString(apn));
        buffer.writeBytes(RSMSTools.packageString(apnName));
        buffer.writeBytes(RSMSTools.packageString(apnPassword));
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data, 0, buffer.readableBytes());
        buffer.release();
        return data;
    }
}
