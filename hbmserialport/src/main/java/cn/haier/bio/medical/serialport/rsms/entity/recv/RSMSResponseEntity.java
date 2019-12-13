package cn.haier.bio.medical.serialport.rsms.entity.recv;

public class RSMSResponseEntity {
    private byte[] mcu; //MCU识别码
    private byte response;

    public RSMSResponseEntity() {
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
}
