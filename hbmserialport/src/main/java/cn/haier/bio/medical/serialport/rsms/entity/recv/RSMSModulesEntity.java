package cn.haier.bio.medical.serialport.rsms.entity.recv;

public class RSMSModulesEntity {
    private byte[] mcu; //MCU识别码
    private byte[] mac; //WIFI MAC
    private String code;//BE码
    private byte model; //联网模式
    private String imei;//IMEI号
    private String iccid;//ICCID号
    private String phone;//SIM卡号码
    private String operator;//运营商
    private String mcuVersion;//MCU软件版本
    private String wifiVersion;//Wifi软件版本
    private String moduleVersion;//模块软件版本

    public RSMSModulesEntity() {
    }

    public byte[] getMcu() {
        return mcu;
    }

    public void setMcu(byte[] mcu) {
        this.mcu = mcu;
    }

    public byte[] getMac() {
        return mac;
    }

    public void setMac(byte[] mac) {
        this.mac = mac;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public byte getModel() {
        return model;
    }

    public void setModel(byte model) {
        this.model = model;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getMcuVersion() {
        return mcuVersion;
    }

    public void setMcuVersion(String mcuVersion) {
        this.mcuVersion = mcuVersion;
    }

    public String getWifiVersion() {
        return wifiVersion;
    }

    public void setWifiVersion(String wifiVersion) {
        this.wifiVersion = wifiVersion;
    }

    public String getModuleVersion() {
        return moduleVersion;
    }

    public void setModuleVersion(String moduleVersion) {
        this.moduleVersion = moduleVersion;
    }
}
