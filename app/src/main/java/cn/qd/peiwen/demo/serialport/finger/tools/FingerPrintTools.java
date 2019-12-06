package cn.qd.peiwen.demo.serialport.finger.tools;

import cn.qd.peiwen.pwlogger.PWLogger;
import cn.qd.peiwen.pwtools.ByteUtils;
import cn.qd.peiwen.pwtools.ThreadUtils;
import cn.qd.peiwen.serialport.PWSerialPort;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


public class FingerPrintTools {
    public static final int FINGER_COMMAND_NONE = 0x00;
    public static final int FINGER_COMMAND_CLEAR = 0x05;
    public static final int FINGER_COMMAND_BREAK = 0xFE;
    public static final int FINGER_COMMAND_DELETE = 0x04;
    public static final int FINGER_COMMAND_UPLOAD = 0x41;
    public static final int FINGER_COMMAND_COMPARE = 0x0C;
    public static final int FINGER_COMMAND_DOWNLOAD = 0x31;
    public static final int FINGER_COMMAND_REGIST_FIRST = 0x01;
    public static final int FINGER_COMMAND_REGIST_THIRD = 0x03;
    public static final int FINGER_COMMAND_REGIST_SECOND = 0x02;
    public static final int FINGER_COMMAND_SEARCH_FINGER = 0x2B;
    public static final int FINGER_COMMAND_REGIST_REFUSE_REPEAT= 0x2D;
    public static final int FINGER_COMMAND_REGIST_HAND_DETECTION = 0x3F;

    private static final String FINGER_RESET_ON = "1";
    private static final String FINGER_RESET_OFF = "0";
    private static final String FINGER_RESET_USB = "/sys/kernel/finger_set/usb_value";
    private static final String FINGER_RESET_TARGET = "/sys/kernel/finger_set/finger_value";

    public static boolean checkFrame(byte[] data) {
        if ((data[0] & 0xff) != 0xF5) {
            return false;
        }
        if ((data[data.length - 1] & 0xff) != 0xF5) {
            return false;
        }
        byte check = ByteUtils.computeXORCode(data, 1, data.length - 3);
        return (check == data[data.length - 2]);
    }

    public static void resetFingerPrint() {
        PWLogger.d("FingerPrint OFF");
        PWSerialPort.writeFile(FINGER_RESET_TARGET, FINGER_RESET_OFF);
        ThreadUtils.sleep(50L);
        PWSerialPort.writeFile(FINGER_RESET_USB, FINGER_RESET_OFF);
        ThreadUtils.sleep(50L);
        PWLogger.d("FingerPrint  ON");
        PWSerialPort.writeFile(FINGER_RESET_TARGET, FINGER_RESET_ON);
        ThreadUtils.sleep(50L);
        PWSerialPort.writeFile(FINGER_RESET_USB, FINGER_RESET_ON);
    }

    public static byte[] createFingerCommand(int type, int param) {
        ByteBuf buffer = Unpooled.buffer(8);
        buffer.writeByte(0xF5);
        buffer.writeByte(type);
        switch (type) {
            case FINGER_COMMAND_CLEAR:
            case FINGER_COMMAND_BREAK:
            case FINGER_COMMAND_COMPARE:
            case FINGER_COMMAND_SEARCH_FINGER:
                buffer.writeByte(0x00);
                buffer.writeByte(0x00);
                buffer.writeByte(0x00);
                buffer.writeByte(0x00);
                break;
            case FINGER_COMMAND_DELETE:
            case FINGER_COMMAND_UPLOAD:
            case FINGER_COMMAND_DOWNLOAD:
                buffer.writeShort(param);
                buffer.writeByte(0x00);
                buffer.writeByte(0x00);
                break;
            case FINGER_COMMAND_REGIST_FIRST:
            case FINGER_COMMAND_REGIST_SECOND:
            case FINGER_COMMAND_REGIST_THIRD:
                buffer.writeByte(0x00);
                buffer.writeByte(0x00);
                buffer.writeByte(0x01);
                buffer.writeByte(0x00);
                break;
            case FINGER_COMMAND_REGIST_REFUSE_REPEAT:
                buffer.writeByte(0x00);
                buffer.writeByte(0x01);
                buffer.writeByte(0x00);
                buffer.writeByte(0x00);
                break;
            case FINGER_COMMAND_REGIST_HAND_DETECTION:
                buffer.writeByte(0x00);
                buffer.writeByte(0x02);
                buffer.writeByte(0x01);
                buffer.writeByte(0x00);
                break;
        }
        byte[] data = new byte[8];
        buffer.readBytes(data, 0, 6);
        data[6] = ByteUtils.computeXORCode(data, 1, 5);
        data[7] = (byte) 0xF5;
        buffer.release();
        return data;
    }

}
