package cn.haier.bio.medical.serialport.rsms.entity.recv;

public class RSMSEnterConfigResponseEntity {
    private byte response;
    private byte configModel;

    public RSMSEnterConfigResponseEntity() {

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
