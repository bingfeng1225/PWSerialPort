package cn.haier.bio.medical.serialport.rsms;

import cn.haier.bio.medical.serialport.rsms.entity.send.IRSMSSendEntity;

public class TestSendEntity implements IRSMSSendEntity {
    private byte[] buffer;

    public TestSendEntity(byte[] buffer) {
        this.buffer = buffer;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    @Override
    public byte[] packageSendMessage() {
        return this.buffer;
    }
}
