package cn.qd.peiwen.demo.serialport.mainboard.tools;

import java.util.Arrays;

import cn.qd.peiwen.pwtools.ByteUtils;

public class MainBoardTools {

    public static boolean checkFrame(byte[] data) {
        byte[] crc = new byte[]{data[data.length - 2], data[data.length - 1]};
        byte[] check = ByteUtils.computeCRCCode(data, 0, data.length - 2);
        return Arrays.equals(crc, check);
    }

    public static byte[] makeStateResponse(byte[] data){
        byte[] buffer = new byte[8];
        System.arraycopy(data, 0, buffer, 0, buffer.length - 2);
        byte[] crc = ByteUtils.computeCRCCode(data, 0, buffer.length - 2);
        buffer[buffer.length - 2] = crc[0];
        buffer[buffer.length - 1] = crc[1];
        return buffer;
    }
}
