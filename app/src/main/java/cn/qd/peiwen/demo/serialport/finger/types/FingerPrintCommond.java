package cn.qd.peiwen.demo.serialport.finger.types;

public enum FingerPrintCommond {
    FINGER_COMMAND_NONE(0x00),
    FINGER_COMMAND_CLEAR(0x05),
    FINGER_COMMAND_BREAK(0xFE),
    FINGER_COMMAND_DELETE(0x04),
    FINGER_COMMAND_UPLOAD(0x41),
    FINGER_COMMAND_COMPARE(0x0C),
    FINGER_COMMAND_DOWNLOAD(0x31),
    FINGER_COMMAND_REGIST_FIRST(0x01),
    FINGER_COMMAND_REGIST_THIRD(0x03),
    FINGER_COMMAND_REGIST_SECOND(0x02),
    FINGER_COMMAND_SEARCH_FINGER(0x2B),
    FINGER_COMMAND_REGIST_REFUSE_REPEAT(0x2D),
    FINGER_COMMAND_REGIST_HAND_DETECTION(0x3F);


    public final int value;

    FingerPrintCommond(int value) {
        this.value = value;
    }

    public static FingerPrintCommond get(int value) throws IllegalArgumentException {
        switch (value) {
            case 0x05:
                return FINGER_COMMAND_CLEAR;
            case 0xFE:
                return FINGER_COMMAND_BREAK;
            case 0x04:
                return FINGER_COMMAND_DELETE;
            case 0x41:
                return FINGER_COMMAND_UPLOAD;
            case 0x0C:
                return FINGER_COMMAND_COMPARE;
            case 0x31:
                return FINGER_COMMAND_DOWNLOAD;
            case 0x01:
                return FINGER_COMMAND_REGIST_FIRST;
            case 0x03:
                return FINGER_COMMAND_REGIST_THIRD;
            case 0x02:
                return FINGER_COMMAND_REGIST_SECOND;
            case 0x2B:
                return FINGER_COMMAND_SEARCH_FINGER;
            case 0x2D:
                return FINGER_COMMAND_REGIST_REFUSE_REPEAT;
            case 0x3F:
                return FINGER_COMMAND_REGIST_HAND_DETECTION;
            default:
                return FINGER_COMMAND_NONE;
        }
    }
}
