package cn.haier.bio.medical.serialport.rsms.entity.send;

import cn.qd.peiwen.pwtools.EmptyUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class RSMSControlResponseEntity implements IRSMSSendEntity {
    private byte result;
    private IRSMSSendEntity entity;

    public RSMSControlResponseEntity() {
    }

    public byte getResult() {
        return result;
    }

    public void setResult(byte result) {
        this.result = result;
    }

    public IRSMSSendEntity getEntity() {
        return entity;
    }

    public void setEntity(IRSMSSendEntity entity) {
        this.entity = entity;
    }

    @Override
    public byte[] packageSendMessage() {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeByte(result);
        byte[] response = null;
        if(EmptyUtils.isNotEmpty(entity)){
            response = entity.packageSendMessage();
        }
        if(EmptyUtils.isNotEmpty(response)){
            buffer.writeBytes(response);
        }
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data, 0, buffer.readableBytes());
        buffer.release();
        return data;
    }
}
