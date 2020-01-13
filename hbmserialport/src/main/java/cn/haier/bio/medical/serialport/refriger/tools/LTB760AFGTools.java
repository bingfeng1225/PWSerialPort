package cn.haier.bio.medical.serialport.refriger.tools;

import android.os.Build;

import java.util.Arrays;

import cn.haier.bio.medical.serialport.refriger.entity.LTB760AFGEntity;
import cn.qd.peiwen.pwtools.ByteUtils;
import cn.qd.peiwen.serialport.PWSerialPort;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class LTB760AFGTools {

    public static final byte[] SYSTEM_TYPES = {0x01,0x02,0x04,0x08};
    public static final byte[] COMMAND_TYPES = {0x10,0x03};

    public static boolean checkFrame(byte[] data) {
        byte[] crc = new byte[]{data[data.length - 2], data[data.length - 1]};
        byte[] check = ByteUtils.computeCRCCode(data, 0, data.length - 2);
        return Arrays.equals(crc, check);
    }

    public static boolean checkSystemType(byte system) {
        for (byte item:SYSTEM_TYPES) {
            if(item == system){
                return true;
            }
        }
        return false;
    }

    public static boolean checkCommandType(byte command) {
        for (byte item:COMMAND_TYPES) {
            if(item == command){
                return true;
            }
        }
        return false;
    }

    public static void switchReadModel() {
        if (!"magton".equals(Build.MODEL)) {
            PWSerialPort.writeFile("/sys/class/gpio/gpio24/value", "1");
        } else {
            PWSerialPort.writeFile("/sys/class/misc/sunxi-acc/acc/sochip_acc", "0");
        }
    }

    public static void switchWriteModel() {
        if (!"magton".equals(Build.MODEL)) {
            PWSerialPort.writeFile("/sys/class/gpio/gpio24/value", "0");
        } else {
            PWSerialPort.writeFile("/sys/class/misc/sunxi-acc/acc/sochip_acc", "1");
        }
    }

    public static byte[] packageStateResponse(byte[] data) {
        byte[] buffer = new byte[8];
        System.arraycopy(data, 0, buffer, 0, buffer.length - 2);
        byte[] crc = ByteUtils.computeCRCCode(data, 0, buffer.length - 2);
        buffer[buffer.length - 2] = crc[0];
        buffer[buffer.length - 1] = crc[1];
        return buffer;
    }

    public static LTB760AFGEntity parseLTB760AGEntity(byte[] data) {
        ByteBuf buffer = Unpooled.copiedBuffer(data);
        LTB760AFGEntity entity = new LTB760AFGEntity();
        entity.setSystem(buffer.getByte(0));
        entity.setTemperature(buffer.getShortLE(7));
        entity.setAmbientTemperature(buffer.getShortLE(9));
        if(entity.getSystem() == 0x02){
            entity.setCondenser1Temperature(buffer.getShortLE(11));
            entity.setCondenser2Temperature(buffer.getShortLE(13));
        }else {
            entity.setCondenserTemperature(buffer.getShortLE(11));
            entity.setHeatExchangerTemperature(buffer.getShortLE(13));
        }
        entity.setSupplyVoltage(buffer.getShortLE(15));
        entity.setMainBatteryVoltage(buffer.getByte(17));
        entity.setDoorInputStatus1(buffer.getShortLE(19));
        entity.setDoorInputStatus2(buffer.getShortLE(21));
        entity.setBatteryChargingStatus(buffer.getShortLE(23));
        entity.setRemoteAlarmOutputStatus(buffer.getShortLE(25));
        entity.setHighTemperatureCompressorStatus(buffer.getShortLE(27));
        entity.setLowTemperatureCompressorStatus(buffer.getShortLE(29));
        entity.setHighTemperatureBlowerStatus(buffer.getShortLE(31));
        entity.setLowTemperatureBlowerStatus(buffer.getShortLE(33));
        entity.setRisePressureOutputStatus(buffer.getShortLE(35));
        entity.setDropPressureOutputStatus(buffer.getShortLE(37));
        entity.setAccapillaryHeatingWireOutputStatus(buffer.getShortLE(39));
        entity.setCabinetHeatingWireOutputStatus(buffer.getShortLE(41));
        entity.setDoorHeatingWireOutputStatus(buffer.getShortLE(43));
        entity.setBalanceHeatingWireOutputStatus(buffer.getShortLE(45));
        entity.setReservedHeatingWireStatus(buffer.getShortLE(47));
        entity.setElectromagneticLockOutputStatus(buffer.getShortLE(49));
        entity.setBuzzerOutputStatus(buffer.getShortLE(51));
        entity.setAlarmStatus1(buffer.getShortLE(53));
        entity.setAlarmStatus2(buffer.getShortLE(55));
        entity.setBackupTemperature(buffer.getShortLE(57));
        entity.setBackupStatus(buffer.getShortLE(59));
        entity.setThermocoupleTemperature1(buffer.getShortLE(61));
        entity.setThermocoupleTemperature2(buffer.getShortLE(63));
        entity.setThermocoupleTemperature3(buffer.getShortLE(65));
        entity.setThermocoupleTemperature4(buffer.getShortLE(67));
        entity.setThermocoupleTemperature5(buffer.getShortLE(69));
        entity.setThermocoupleTemperature6(buffer.getShortLE(71));
        entity.setThermocoupleTemperature7(buffer.getShortLE(73));
        entity.setThermocoupleTemperature8(buffer.getShortLE(75));
        entity.setThermocoupleTemperature9(buffer.getShortLE(77));
        entity.setThermocoupleTemperature10(buffer.getShortLE(79));
        entity.setBackupConnectionStatus(buffer.getShortLE(81));
        entity.setSettingTemperatureValue(buffer.getShortLE(83));
        entity.setHighTemperatureAlarmValue(buffer.getShortLE(85));
        entity.setLowTemperatureAlarmValue(buffer.getShortLE(87));
        buffer.release();
        return entity;
    }
}
