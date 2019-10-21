package cn.qd.peiwen.demo.serialport.mainboard.tools;

import android.util.Log;

import java.sql.Array;
import java.util.Arrays;

import cn.qd.peiwen.pwtools.ByteUtils;


public class MainBoardTools {

    public static boolean checkFrame(byte[] data) {
        byte[] crc = new byte[]{data[data.length - 2], data[data.length - 1]};
        byte[] check = ByteUtils.computeCRCCode(data, 0, data.length - 2);
        return Arrays.equals(crc, check);
    }
}
