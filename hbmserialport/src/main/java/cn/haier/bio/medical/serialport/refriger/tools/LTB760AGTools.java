package cn.haier.bio.medical.serialport.refriger.tools;

import android.os.Build;

import java.util.Arrays;

import cn.haier.bio.medical.serialport.refriger.entity.LTB760AGAlarmEntity;
import cn.haier.bio.medical.serialport.refriger.entity.LTB760AGBasicsEntity;
import cn.haier.bio.medical.serialport.refriger.entity.LTB760AGEntity;
import cn.haier.bio.medical.serialport.refriger.entity.LTB760AGStatusEntity;
import cn.qd.peiwen.pwtools.ByteUtils;
import cn.qd.peiwen.serialport.PWSerialPort;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class LTB760AGTools {

    public static boolean checkFrame(byte[] data) {
        byte[] crc = new byte[]{data[data.length - 2], data[data.length - 1]};
        byte[] check = ByteUtils.computeCRCCode(data, 0, data.length - 2);
        return Arrays.equals(crc, check);
    }

    public static boolean checkSystemType(byte system) {
        if (system == 0x01 || system == 0x02 || system == 0x04 || system == 0x05) {
            return true;
        }
        return false;
    }

    public static boolean checkCommandType(byte command) {
        if (command == 0x10 || command == 0x03) {
            return true;
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

    public static LTB760AGEntity parseLTB760AGEntity(byte[] data) {
        ByteBuf buffer = Unpooled.copiedBuffer(data);
        LTB760AGEntity entity = new LTB760AGEntity();
        LTB760AGBasicsEntity basics = entity.getBasics();
        byte system = buffer.getByte(0);
        basics.setTemperature(buffer.getShortLE(7));
        basics.setAmbientTemperature(buffer.getShortLE(9));
        basics.setCondenserTemperature(buffer.getShortLE(11));
        if (system == 0x02) {
            basics.setCondenser2Temperature(buffer.getShortLE(13));
        } else {
            basics.setHeatExchangerTemperature(buffer.getShortLE(13));
        }
        basics.setSupplyVoltage(buffer.getShortLE(15));
        basics.setMainBatteryVoltage(buffer.getByte(17));
        basics.setBackupTemperature(buffer.getShortLE(57));
        basics.setThermocoupleTemperature1(buffer.getShortLE(61));
        basics.setThermocoupleTemperature2(buffer.getShortLE(63));
        basics.setThermocoupleTemperature3(buffer.getShortLE(65));
        basics.setThermocoupleTemperature4(buffer.getShortLE(67));
        basics.setThermocoupleTemperature5(buffer.getShortLE(69));
        basics.setThermocoupleTemperature6(buffer.getShortLE(71));
        basics.setThermocoupleTemperature7(buffer.getShortLE(73));
        basics.setThermocoupleTemperature8(buffer.getShortLE(75));
        basics.setThermocoupleTemperature9(buffer.getShortLE(77));
        basics.setThermocoupleTemperature10(buffer.getShortLE(79));

        LTB760AGStatusEntity status = entity.getStatus();
        status.setBackupBatteryStatus(buffer.getByte(18));
        status.setDoorInputStatus1(buffer.getShortLE(19));
        status.setDoorInputStatus2(buffer.getShortLE(21));
        status.setBatteryChargingStatus(buffer.getShortLE(23));
        status.setRemoteAlarmOutputStatus(buffer.getShortLE(25));
        status.setHighTemperatureCompressorStatus(buffer.getShortLE(27));
        status.setLowTemperatureCompressorStatus(buffer.getShortLE(29));
        status.setCondensateBlowerOutputStatus1(buffer.getShortLE(31));
        status.setCondensateBlowerOutputStatus2(buffer.getShortLE(33));
        status.setRisePressureOutputStatus(buffer.getShortLE(35));
        status.setDropPressureOutputStatus(buffer.getShortLE(37));
        status.setAccapillaryHeatingWireOutputStatus(buffer.getShortLE(39));
        status.setCabinetHeatingWireOutputStatus(buffer.getShortLE(41));
        status.setDoorHeatingWireOutputStatus(buffer.getShortLE(43));
        status.setBalanceHeatingWireOutputStatus(buffer.getShortLE(45));
        status.setReservedHeatingWireStatus(buffer.getShortLE(47));
        status.setElectromagneticLockOutputStatus(buffer.getShortLE(49));
        status.setBuzzerOutputStatus(buffer.getShortLE(51));
        status.setBackupStatus(buffer.getShortLE(59));
        status.setBackupConnectionStatus(buffer.getShortLE(81));
        status.setSettingTemperatureValue(buffer.getShortLE(83));
        status.setHighTemperatureAlarmValue(buffer.getShortLE(85));
        status.setLowTemperatureAlarmValue(buffer.getShortLE(87));

        LTB760AGAlarmEntity alarm = entity.getAlarm();
        alarm.setAlarmStatus1(buffer.getShortLE(53));
        alarm.setAlarmStatus2(buffer.getShortLE(55));

        buffer.release();
        return entity;
    }
}
