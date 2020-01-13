package cn.haier.bio.medical.serialport.rsms.entity.send;

import cn.haier.bio.medical.serialport.rsms.tools.RSMSTools;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class RSMSQueryStatusEntity implements IRSMSSendEntity {
    private String code;
    private byte[] mac;
    private byte[] mcu;

    public RSMSQueryStatusEntity() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public byte[] getMac() {
        return mac;
    }

    public void setMac(byte[] mac) {
        this.mac = mac;
    }

    public byte[] getMcu() {
        return mcu;
    }

    public void setMcu(byte[] mcu) {
        this.mcu = mcu;
    }

    @Override
    public byte[] packageSendMessage() {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeBytes(this.mac);
        buffer.writeBytes(this.mcu);
        buffer.writeBytes(RSMSTools.packageString(this.code));
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data, 0, buffer.readableBytes());
        buffer.release();
        return data;
    }
}
