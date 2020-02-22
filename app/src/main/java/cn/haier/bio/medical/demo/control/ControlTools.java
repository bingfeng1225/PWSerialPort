package cn.haier.bio.medical.demo.control;

import java.nio.ByteOrder;

import cn.haier.bio.medical.demo.control.recv.TemptureEntity;
import cn.qd.peiwen.pwtools.ByteUtils;

public class ControlTools {
    public static final int RSMS_CONTROL_TEMPTURE_COMMAND = 0xC001;
    public static final int RSMS_CONTROL_TEMPTURE_RESPONSE = 0xC101;

    public static TemptureEntity parseTemptureEntity(byte[] data){
        TemptureEntity entity = new TemptureEntity();
        entity.setTempture(ByteUtils.bytes2Short(data, ByteOrder.LITTLE_ENDIAN));
        return entity;
    }
}
