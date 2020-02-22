package cn.haier.bio.medical.demo.mcu;

import cn.haier.bio.medical.serialport.refriger.entity.LTB760AFGEntity;
import cn.haier.bio.medical.serialport.rsms.entity.send.IRSMSSendEntity;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class LTB760AFGCollectionEntity implements IRSMSSendEntity {
    private LTB760AFGEntity entity;
    public LTB760AFGCollectionEntity() {

    }

    public LTB760AFGEntity getEntity() {
        return entity;
    }

    public void setEntity(LTB760AFGEntity entity) {
        this.entity = entity;
    }

    @Override
    public byte[] packageSendMessage() {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeByte(0x85); //数据类型
        buffer.writeByte(0xFE); //设备类型
        buffer.writeShortLE(this.entity.getTemperature()); //箱内温度
        buffer.writeShortLE(this.entity.getAmbientTemperature());//环境温度
        buffer.writeShortLE(this.entity.getCondenserTemperature());//冷凝器温度
        buffer.writeShortLE(this.entity.getHeatExchangerTemperature());//热交换器温度
        buffer.writeShortLE(this.entity.getCondenser1Temperature());//冷凝器1温度
        buffer.writeShortLE(this.entity.getCondenser2Temperature());//冷凝器2温度
        buffer.writeShortLE(this.entity.getSupplyVoltage());//电源电压
        buffer.writeShortLE(this.entity.getMainBatteryVoltage());//电池电压
        buffer.writeShortLE(this.entity.getBackupTemperature());//后备系统监测箱内温度
        buffer.writeShortLE(this.entity.getThermocoupleTemperature1());//热电偶温度1
        buffer.writeShortLE(this.entity.getThermocoupleTemperature2());//热电偶温度2
        buffer.writeShortLE(this.entity.getThermocoupleTemperature3());//热电偶温度3
        buffer.writeShortLE(this.entity.getThermocoupleTemperature4());//热电偶温度4
        buffer.writeShortLE(this.entity.getThermocoupleTemperature5());//热电偶温度5
        buffer.writeShortLE(this.entity.getThermocoupleTemperature6());//热电偶温度6
        buffer.writeShortLE(this.entity.getThermocoupleTemperature7());//热电偶温度7
        buffer.writeShortLE(this.entity.getThermocoupleTemperature8());//热电偶温度8
        buffer.writeShortLE(this.entity.getThermocoupleTemperature9());//热电偶温度9
        buffer.writeShortLE(this.entity.getThermocoupleTemperature10());//热电偶温度10

        buffer.writeShortLE(this.entity.getSettingTemperatureValue());//设定温度
        buffer.writeShortLE(this.entity.getHighTemperatureAlarmValue());//高温报警值
        buffer.writeShortLE(this.entity.getLowTemperatureAlarmValue());//低温报警值

        buffer.writeByte(this.entity.getDoorInputStatus1());//门开关输入状态1L
        buffer.writeByte(this.entity.getDoorInputStatus2());//门开关输入状态2L
        buffer.writeByte(this.entity.getBatteryChargingStatus());//电池充电状态L
        buffer.writeByte(this.entity.getRemoteAlarmOutputStatus());//远程报警输出状态L
        buffer.writeByte(this.entity.getHighTemperatureCompressorStatus());//高温压缩机输出状态L
        buffer.writeByte(this.entity.getLowTemperatureCompressorStatus());//低温压缩机输出状态L
        buffer.writeByte(this.entity.getHighTemperatureBlowerStatus());//高温风机输出状态L
        buffer.writeByte(this.entity.getLowTemperatureBlowerStatus());//低温风机输出状态L
        buffer.writeByte(this.entity.getRisePressureOutputStatus());//升压输出状态L
        buffer.writeByte(this.entity.getDropPressureOutputStatus());//降压输出状态L

        buffer.writeByte(this.entity.getAccapillaryHeatingWireOutputStatus());//交流毛细管加热丝输出状态
        buffer.writeByte(this.entity.getCabinetHeatingWireOutputStatus());//柜口加热丝输出状态
        buffer.writeByte(this.entity.getDoorHeatingWireOutputStatus());//门体加热丝输出状态
        buffer.writeByte(this.entity.getBalanceHeatingWireOutputStatus());//平衡口加热丝输出状态
        buffer.writeByte(this.entity.getReservedHeatingWireStatus());//预留加热丝输出状态

        buffer.writeByte(this.entity.getElectromagneticLockOutputStatus());//电磁锁输出状态
        buffer.writeByte(this.entity.getBuzzerOutputStatus());//蜂鸣器输出状态

        buffer.writeByte(this.entity.getBackupSparyStatus());//后备系统喷射状态
        buffer.writeByte(this.entity.getBackupConnectionStatus());//后备系统连接状态

        buffer.writeByte(this.entity.isHighTemperatureAlarm()?0x01:0x00);//高温报警
        buffer.writeByte(this.entity.isLowTemperatureAlarm()?0x01:0x00);//低温报警
        buffer.writeByte(this.entity.isPowerOffAlarm()?0x01:0x00);//断电报警
        buffer.writeByte(this.entity.isPowerBoardFaultAlarm()?0x01:0x00);//电源板故障
        buffer.writeByte(this.entity.isBatteryReversedAlarm()?0x01:0x00);//电池插反
        buffer.writeByte(this.entity.isCondenserDirtyAlarm()?0x01:0x00);//冷凝器脏
        buffer.writeByte(this.entity.isDoorOpeningAlarm()?0x01:0x00);//开门报警
        buffer.writeByte(this.entity.isAmbientTemperatureSensorFaultAlarm()?0x01:0x00);//环温传感器故障
        buffer.writeByte(this.entity.isMainSensorFaultAlarm()?0x01:0x00);//主传感器故障
        buffer.writeByte(this.entity.isHeatExchangerSensorFaultAlarm()?0x01:0x00);//热交换器传感器故障
        buffer.writeByte(this.entity.isCondenserSensorFaultAlarm()?0x01:0x00);//冷凝器传感器故障
        buffer.writeByte(this.entity.isCondenser1SensorFaultAlarm()?0x01:0x00);//冷凝器1传感器故障
        buffer.writeByte(this.entity.isCondenser2SensorFaultAlarm()?0x01:0x00);//冷凝器2传感器故障
        buffer.writeByte(this.entity.isBackupSystemFaultAlarm()?0x01:0x00);//后备系统故障
        buffer.writeByte(this.entity.isHighAmbientTemperatureAlarm()?0x01:0x00);//环温过高
        buffer.writeByte(this.entity.isBatteryNotConnectedAlarm()?0x01:0x00);//电池未连接
        buffer.writeByte(this.entity.isLowBatteryAlarm()?0x01:0x00);//电池电量低
        buffer.writeByte(this.entity.isVoltageOutofLimitAlarm()?0x01:0x00);//电压超标
        buffer.writeByte(this.entity.isBackupSystemRefrigerantAlarm()?0x01:0x00);//后备系统冷媒不足
        buffer.writeByte(this.entity.isBackupSystemSensorFaultAlarm()?0x01:0x00);//后备系统传感器故障

        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data,0, data.length);
        buffer.release();
        return data;
    }
}
