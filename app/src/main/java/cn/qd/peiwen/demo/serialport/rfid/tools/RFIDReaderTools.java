package cn.qd.peiwen.demo.serialport.rfid.tools;

import cn.qd.peiwen.demo.serialport.rfid.types.RFIDReaderCommond;
import cn.qd.peiwen.pwlogger.PWLogger;
import cn.qd.peiwen.pwtools.ByteUtils;
import cn.qd.peiwen.pwtools.ThreadUtils;
import cn.qd.peiwen.serialport.PWSerialPort;


public class RFIDReaderTools {
    private static final String RFID_RESET_ON = "1";
    private static final String RFID_RESET_OFF = "0";
    private static final String RFID_RESET_PATH = "/sys/kernel/finger_set/nfc_value";

    public static boolean checkFrame(byte[] data) {
        if (data[0] < 0x06) {
            return false;
        }
        if ((data[data.length - 1] & 0xff) != 0x03) {
            return false;
        }
        byte check = ByteUtils.computeXORInverse(data, 0, data.length - 2);
        return (check == data[data.length - 2]);
    }

    public static void resetRFIDReader() {
        PWLogger.d("RFID Reader OFF");
        PWSerialPort.writeFile(RFID_RESET_PATH, RFID_RESET_OFF);
        ThreadUtils.sleep(200);
        PWSerialPort.writeFile(RFID_RESET_PATH, RFID_RESET_ON);
        ThreadUtils.sleep(200);
        PWLogger.d("RFID Reader  ON");
    }

    public static byte[] createFingerCommand(RFIDReaderCommond type) {
        switch (type) {
            case RFID_COMMAND_READ:
                return new byte[]{
                        0x08, 0x06, (byte) 0x4D, 0x02,
                        0x00, 0x52, (byte) 0xEC, 0x03
                };
            case RFID_COMMAND_RESET:
                return new byte[]{
                        0x06, 0x05, 0x45, 0x00, (byte) 0xB9, 0x03
                };
            default:
                return new byte[]{0x20};
        }
    }
}
