package cn.haier.bio.medical.demo.control.send;

import cn.haier.bio.medical.demo.control.ControlTools;
import cn.haier.bio.medical.serialport.rsms.entity.send.IRSMSSendEntity;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class TemptureResponseEntity implements IRSMSSendEntity {
    private short tempture;

    public TemptureResponseEntity(short tempture) {
        this.tempture = tempture;
    }

    public short getTempture() {
        return tempture;
    }

    public void setTempture(short tempture) {
        this.tempture = tempture;
    }

    @Override
    public byte[] packageSendMessage() {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeShort(ControlTools.RSMS_CONTROL_TEMPTURE_RESPONSE);
        buffer.writeShortLE(this.tempture);
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data, 0, buffer.readableBytes());
        buffer.release();
        return data;
    }
}
