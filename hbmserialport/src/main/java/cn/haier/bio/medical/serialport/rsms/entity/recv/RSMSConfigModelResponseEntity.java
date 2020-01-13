package cn.haier.bio.medical.serialport.rsms.entity.recv;

public class RSMSConfigModelResponseEntity {
    private byte[] mcu;
    private byte response;
    private byte configModel;

    public RSMSConfigModelResponseEntity() {

    }

    public byte[] getMcu() {
        return mcu;
    }

    public void setMcu(byte[] mcu) {
        this.mcu = mcu;
    }

    public byte getResponse() {
        return response;
    }

    public void setResponse(byte response) {
        this.response = response;
    }

    public byte getConfigModel() {
        return configModel;
    }

    public void setConfigModel(byte configModel) {
        this.configModel = configModel;
    }


}
