package cn.haier.bio.medical.serialport.refriger.entity;

public class LTB760AFGEntity implements Cloneable {
    private byte system;//系统类型

    private short temperature; //箱内温度*10 2
    private short ambientTemperature; //环境温度 2
    private short condenserTemperature;//冷凝器温度 2
    private short heatExchangerTemperature;//热交换器温度 2

    private short condenser1Temperature;//冷凝器1温度 2
    private short condenser2Temperature;//冷凝器2温度 2

    private short supplyVoltage;//电源电压 2
    private byte mainBatteryVoltage;//电池电压*10 1

    private short doorInputStatus1;//门开关输入状态1 2
    private short doorInputStatus2;//门开关输入状态2 2

    private short batteryChargingStatus;//电池充电状态 2

    private short remoteAlarmOutputStatus;//远程报警输出状态 2

    private short highTemperatureCompressorStatus;//高温压机状态 2
    private short lowTemperatureCompressorStatus;//低温压机状态 2

    private short highTemperatureBlowerStatus;//高温风机输出状态 2
    private short lowTemperatureBlowerStatus;//低温风机输出状态 2

    private short risePressureOutputStatus;//升压输出状态 2
    private short dropPressureOutputStatus;//降压输出状态 2

    private short accapillaryHeatingWireOutputStatus;//交流毛细管加热丝输出状态 2
    private short cabinetHeatingWireOutputStatus;//柜口加热丝输出状态 2
    private short doorHeatingWireOutputStatus;//门体加热丝输出状态 2
    private short balanceHeatingWireOutputStatus;//平衡口加热丝输出状态 2
    private short reservedHeatingWireStatus;//预留加热丝输出状态 2

    private short electromagneticLockOutputStatus;//电磁锁输出状态 2
    private short buzzerOutputStatus;//蜂鸣器输出状态 2

    private short alarmStatus1;//报警状态信息1  2
    private short alarmStatus2;//报警状态信息2  2

    private short backupTemperature;//后备系统检测箱内温度*10 2
    private short backupStatus;//后备系统各种状态 2

    private short thermocoupleTemperature1;//热电偶温度1    2
    private short thermocoupleTemperature2;//热电偶温度2    2
    private short thermocoupleTemperature3;//热电偶温度3    2
    private short thermocoupleTemperature4;//热电偶温度4    2
    private short thermocoupleTemperature5;//热电偶温度5    2
    private short thermocoupleTemperature6;//热电偶温度6    2
    private short thermocoupleTemperature7;//热电偶温度7    2
    private short thermocoupleTemperature8;//热电偶温度8    2
    private short thermocoupleTemperature9;//热电偶温度9    2
    private short thermocoupleTemperature10;//热电偶温度10  2

    private short backupConnectionStatus;//后备系统连接状态 2

    private short settingTemperatureValue;//设定温度*10 2
    private short highTemperatureAlarmValue;//高温报警值*10 2
    private short lowTemperatureAlarmValue;//低温报警值*10 2


    public LTB760AFGEntity() {

    }

    public byte getSystem() {
        return system;
    }

    public void setSystem(byte system) {
        this.system = system;
    }

    public short getTemperature() {
        return temperature;
    }

    public void setTemperature(short temperature) {
        this.temperature = temperature;
    }

    public short getAmbientTemperature() {
        return ambientTemperature;
    }

    public void setAmbientTemperature(short ambientTemperature) {
        this.ambientTemperature = ambientTemperature;
    }

    public short getCondenserTemperature() {
        return condenserTemperature;
    }

    public void setCondenserTemperature(short condenserTemperature) {
        this.condenserTemperature = condenserTemperature;
    }

    public short getHeatExchangerTemperature() {
        return heatExchangerTemperature;
    }

    public void setHeatExchangerTemperature(short heatExchangerTemperature) {
        this.heatExchangerTemperature = heatExchangerTemperature;
    }

    public short getCondenser1Temperature() {
        return condenser1Temperature;
    }

    public void setCondenser1Temperature(short condenser1Temperature) {
        this.condenser1Temperature = condenser1Temperature;
    }

    public short getCondenser2Temperature() {
        return condenser2Temperature;
    }

    public void setCondenser2Temperature(short condenser2Temperature) {
        this.condenser2Temperature = condenser2Temperature;
    }

    public short getSupplyVoltage() {
        return supplyVoltage;
    }

    public void setSupplyVoltage(short supplyVoltage) {
        this.supplyVoltage = supplyVoltage;
    }

    public byte getMainBatteryVoltage() {
        return mainBatteryVoltage;
    }

    public void setMainBatteryVoltage(byte mainBatteryVoltage) {
        this.mainBatteryVoltage = mainBatteryVoltage;
    }

    public short getDoorInputStatus1() {
        return doorInputStatus1;
    }

    public void setDoorInputStatus1(short doorInputStatus1) {
        this.doorInputStatus1 = doorInputStatus1;
    }

    public short getDoorInputStatus2() {
        return doorInputStatus2;
    }

    public void setDoorInputStatus2(short doorInputStatus2) {
        this.doorInputStatus2 = doorInputStatus2;
    }

    public short getBatteryChargingStatus() {
        return batteryChargingStatus;
    }

    public void setBatteryChargingStatus(short batteryChargingStatus) {
        this.batteryChargingStatus = batteryChargingStatus;
    }

    public short getRemoteAlarmOutputStatus() {
        return remoteAlarmOutputStatus;
    }

    public void setRemoteAlarmOutputStatus(short remoteAlarmOutputStatus) {
        this.remoteAlarmOutputStatus = remoteAlarmOutputStatus;
    }

    public short getHighTemperatureCompressorStatus() {
        return highTemperatureCompressorStatus;
    }

    public void setHighTemperatureCompressorStatus(short highTemperatureCompressorStatus) {
        this.highTemperatureCompressorStatus = highTemperatureCompressorStatus;
    }

    public short getLowTemperatureCompressorStatus() {
        return lowTemperatureCompressorStatus;
    }

    public void setLowTemperatureCompressorStatus(short lowTemperatureCompressorStatus) {
        this.lowTemperatureCompressorStatus = lowTemperatureCompressorStatus;
    }

    public short getHighTemperatureBlowerStatus() {
        return highTemperatureBlowerStatus;
    }

    public void setHighTemperatureBlowerStatus(short highTemperatureBlowerStatus1) {
        this.highTemperatureBlowerStatus = highTemperatureBlowerStatus;
    }

    public short getLowTemperatureBlowerStatus() {
        return lowTemperatureBlowerStatus;
    }

    public void setLowTemperatureBlowerStatus(short lowTemperatureBlowerStatus) {
        this.lowTemperatureBlowerStatus = lowTemperatureBlowerStatus;
    }

    public short getRisePressureOutputStatus() {
        return risePressureOutputStatus;
    }

    public void setRisePressureOutputStatus(short risePressureOutputStatus) {
        this.risePressureOutputStatus = risePressureOutputStatus;
    }

    public short getDropPressureOutputStatus() {
        return dropPressureOutputStatus;
    }

    public void setDropPressureOutputStatus(short dropPressureOutputStatus) {
        this.dropPressureOutputStatus = dropPressureOutputStatus;
    }

    public short getAccapillaryHeatingWireOutputStatus() {
        return accapillaryHeatingWireOutputStatus;
    }

    public void setAccapillaryHeatingWireOutputStatus(short accapillaryHeatingWireOutputStatus) {
        this.accapillaryHeatingWireOutputStatus = accapillaryHeatingWireOutputStatus;
    }

    public short getCabinetHeatingWireOutputStatus() {
        return cabinetHeatingWireOutputStatus;
    }

    public void setCabinetHeatingWireOutputStatus(short cabinetHeatingWireOutputStatus) {
        this.cabinetHeatingWireOutputStatus = cabinetHeatingWireOutputStatus;
    }

    public short getDoorHeatingWireOutputStatus() {
        return doorHeatingWireOutputStatus;
    }

    public void setDoorHeatingWireOutputStatus(short doorHeatingWireOutputStatus) {
        this.doorHeatingWireOutputStatus = doorHeatingWireOutputStatus;
    }

    public short getBalanceHeatingWireOutputStatus() {
        return balanceHeatingWireOutputStatus;
    }

    public void setBalanceHeatingWireOutputStatus(short balanceHeatingWireOutputStatus) {
        this.balanceHeatingWireOutputStatus = balanceHeatingWireOutputStatus;
    }

    public short getReservedHeatingWireStatus() {
        return reservedHeatingWireStatus;
    }

    public void setReservedHeatingWireStatus(short reservedHeatingWireStatus) {
        this.reservedHeatingWireStatus = reservedHeatingWireStatus;
    }

    public short getElectromagneticLockOutputStatus() {
        return electromagneticLockOutputStatus;
    }

    public void setElectromagneticLockOutputStatus(short electromagneticLockOutputStatus) {
        this.electromagneticLockOutputStatus = electromagneticLockOutputStatus;
    }

    public short getBuzzerOutputStatus() {
        return buzzerOutputStatus;
    }

    public void setBuzzerOutputStatus(short buzzerOutputStatus) {
        this.buzzerOutputStatus = buzzerOutputStatus;
    }

    public short getAlarmStatus1() {
        return alarmStatus1;
    }

    public void setAlarmStatus1(short alarmStatus1) {
        this.alarmStatus1 = alarmStatus1;
    }

    public short getAlarmStatus2() {
        return alarmStatus2;
    }

    public void setAlarmStatus2(short alarmStatus2) {
        this.alarmStatus2 = alarmStatus2;
    }

    public short getBackupTemperature() {
        return backupTemperature;
    }

    public void setBackupTemperature(short backupTemperature) {
        this.backupTemperature = backupTemperature;
    }

    public short getBackupStatus() {
        return backupStatus;
    }

    public short getBackupSparyStatus() {
        return (short) (this.backupStatus & 0x0001);
    }

    public void setBackupStatus(short backupStatus) {
        this.backupStatus = backupStatus;
    }

    public short getThermocoupleTemperature1() {
        return thermocoupleTemperature1;
    }

    public void setThermocoupleTemperature1(short thermocoupleTemperature1) {
        this.thermocoupleTemperature1 = thermocoupleTemperature1;
    }

    public short getThermocoupleTemperature2() {
        return thermocoupleTemperature2;
    }

    public void setThermocoupleTemperature2(short thermocoupleTemperature2) {
        this.thermocoupleTemperature2 = thermocoupleTemperature2;
    }

    public short getThermocoupleTemperature3() {
        return thermocoupleTemperature3;
    }

    public void setThermocoupleTemperature3(short thermocoupleTemperature3) {
        this.thermocoupleTemperature3 = thermocoupleTemperature3;
    }

    public short getThermocoupleTemperature4() {
        return thermocoupleTemperature4;
    }

    public void setThermocoupleTemperature4(short thermocoupleTemperature4) {
        this.thermocoupleTemperature4 = thermocoupleTemperature4;
    }

    public short getThermocoupleTemperature5() {
        return thermocoupleTemperature5;
    }

    public void setThermocoupleTemperature5(short thermocoupleTemperature5) {
        this.thermocoupleTemperature5 = thermocoupleTemperature5;
    }

    public short getThermocoupleTemperature6() {
        return thermocoupleTemperature6;
    }

    public void setThermocoupleTemperature6(short thermocoupleTemperature6) {
        this.thermocoupleTemperature6 = thermocoupleTemperature6;
    }

    public short getThermocoupleTemperature7() {
        return thermocoupleTemperature7;
    }

    public void setThermocoupleTemperature7(short thermocoupleTemperature7) {
        this.thermocoupleTemperature7 = thermocoupleTemperature7;
    }

    public short getThermocoupleTemperature8() {
        return thermocoupleTemperature8;
    }

    public void setThermocoupleTemperature8(short thermocoupleTemperature8) {
        this.thermocoupleTemperature8 = thermocoupleTemperature8;
    }

    public short getThermocoupleTemperature9() {
        return thermocoupleTemperature9;
    }

    public void setThermocoupleTemperature9(short thermocoupleTemperature9) {
        this.thermocoupleTemperature9 = thermocoupleTemperature9;
    }

    public short getThermocoupleTemperature10() {
        return thermocoupleTemperature10;
    }

    public void setThermocoupleTemperature10(short thermocoupleTemperature10) {
        this.thermocoupleTemperature10 = thermocoupleTemperature10;
    }

    public short getBackupConnectionStatus() {
        return backupConnectionStatus;
    }

    public void setBackupConnectionStatus(short backupConnectionStatus) {
        this.backupConnectionStatus = backupConnectionStatus;
    }

    public short getSettingTemperatureValue() {
        return settingTemperatureValue;
    }

    public void setSettingTemperatureValue(short settingTemperatureValue) {
        this.settingTemperatureValue = settingTemperatureValue;
    }

    public short getHighTemperatureAlarmValue() {
        return highTemperatureAlarmValue;
    }

    public void setHighTemperatureAlarmValue(short highTemperatureAlarmValue) {
        this.highTemperatureAlarmValue = highTemperatureAlarmValue;
    }

    public short getLowTemperatureAlarmValue() {
        return lowTemperatureAlarmValue;
    }

    public void setLowTemperatureAlarmValue(short lowTemperatureAlarmValue) {
        this.lowTemperatureAlarmValue = lowTemperatureAlarmValue;
    }

    //高温报警  15
    public boolean isHighTemperatureAlarm() {
        return ((this.alarmStatus1 & 0x0001) == 0x0001);
    }

    //低温报警 14
    public boolean isLowTemperatureAlarm() {
        return ((this.alarmStatus1 & 0x0002) == 0x0002);
    }

    //断电报警 13
    public boolean isPowerOffAlarm() {
        return ((this.alarmStatus1 & 0x0004) == 0x0004);
    }

    //开门报警 12
    public boolean isDoorOpeningAlarm() {
        return ((this.alarmStatus1 & 0x0008) == 0x0008);
    }

    //环温传感器故障 11
    public boolean isAmbientTemperatureSensorFaultAlarm() {
        return ((this.alarmStatus1 & 0x0010) == 0x0010);
    }

    //主传感器故障 10
    public boolean isMainSensorFaultAlarm() {
        return ((this.alarmStatus1 & 0x0020) == 0x0020);
    }

    //热交换器传感器故障 9
    public boolean isHeatExchangerSensorFaultAlarm() {
        if (this.system != 0x02) {
            return ((this.alarmStatus1 & 0x0040) == 0x0040);
        }
        return false;
    }

    //冷凝器2传感器故障 9
    public boolean isCondenser2SensorFaultAlarm() {
        if (this.system == 0x02) {
            return ((this.alarmStatus1 & 0x0040) == 0x0040);
        }
        return false;
    }

    //冷凝器传感器故障 8
    public boolean isCondenserSensorFaultAlarm() {
        if (this.system != 0x02) {
            return ((this.alarmStatus1 & 0x0080) == 0x0080);
        }
        return false;
    }

    //冷凝器1传感器故障 8
    public boolean isCondenser1SensorFaultAlarm() {
        if (this.system == 0x02) {
            return ((this.alarmStatus1 & 0x0080) == 0x0080);
        }
        return false;
    }

    //后备系统故障 7
    public boolean isBackupSystemFaultAlarm() {
        return ((this.alarmStatus1 & 0x0100) == 0x0100);
    }

    //冷凝器脏 6
    public boolean isCondenserDirtyAlarm() {
        return ((this.alarmStatus1 & 0x0200) == 0x0200);
    }

    //环温过高 5
    public boolean isHighAmbientTemperatureAlarm() {
        return ((this.alarmStatus1 & 0x0400) == 0x0400);
    }

    //电池插反 4
    public boolean isBatteryReversedAlarm() {
        return ((this.alarmStatus1 & 0x0800) == 0x0800);
    }

    //电池未连接 3
    public boolean isBatteryNotConnectedAlarm() {
        return ((this.alarmStatus1 & 0x1000) == 0x1000);
    }

    //电池电量低 2
    public boolean isLowBatteryAlarm() {
        return ((this.alarmStatus1 & 0x2000) == 0x2000);
    }

    //电源板故障 1
    public boolean isPowerBoardFaultAlarm() {
        return ((this.alarmStatus1 & 0x4000) == 0x4000);
    }

    //电压超标 0
    public boolean isVoltageOutofLimitAlarm() {
        return ((this.alarmStatus1 & 0x8000) == 0x8000);
    }

    //后备系统冷媒不足 14
    public boolean isBackupSystemRefrigerantAlarm() {
        return ((this.backupStatus & 0x0002) == 0x0002);
    }

    //后备系统传感器故障 13
    public boolean isBackupSystemSensorFaultAlarm() {
        return ((this.backupStatus & 0x0004) == 0x0004);
    }

    public boolean hasRedAlarm() {
        return this.isPowerOffAlarm() ||
                this.isDoorOpeningAlarm() ||
                this.isLowTemperatureAlarm() ||
                this.isHighTemperatureAlarm() ||
                this.isPowerBoardFaultAlarm();
    }

    public boolean hasOrangeAlarm() {
        return this.isLowBatteryAlarm() ||
                this.isMainSensorFaultAlarm() ||
                this.isCondenserDirtyAlarm() ||
                this.isBatteryReversedAlarm() ||
                this.isVoltageOutofLimitAlarm() ||
                this.isBackupSystemFaultAlarm() ||
                this.isBatteryNotConnectedAlarm() ||
                this.isCondenserSensorFaultAlarm() ||
                this.isCondenser2SensorFaultAlarm() ||
                this.isCondenser1SensorFaultAlarm() ||
                this.isHighAmbientTemperatureAlarm() ||
                this.isBackupSystemRefrigerantAlarm() ||
                this.isBackupSystemSensorFaultAlarm() ||
                this.isHeatExchangerSensorFaultAlarm() ||
                this.isAmbientTemperatureSensorFaultAlarm();
    }

    public boolean isAlarmsEquals(Object obj) {
        if (this == obj) {
            return true;//地址相等
        }
        if (obj == null) {
            return false;//非空性：对于任意非空引用x，x.equals(null)应该返回false。
        }
        if (!(obj instanceof LTB760AFGEntity)) {
            return false;
        }
        LTB760AFGEntity other = (LTB760AFGEntity) obj;
        if (this.isHighTemperatureAlarm() != other.isHighTemperatureAlarm()) {
            return false;
        }
        if (this.isLowTemperatureAlarm() != other.isLowTemperatureAlarm()) {
            return false;
        }
        if (this.isPowerOffAlarm() != other.isPowerOffAlarm()) {
            return false;
        }
        if (this.isDoorOpeningAlarm() != other.isDoorOpeningAlarm()) {
            return false;
        }
        if (this.isAmbientTemperatureSensorFaultAlarm() != other.isAmbientTemperatureSensorFaultAlarm()) {
            return false;
        }
        if (this.isMainSensorFaultAlarm() != other.isMainSensorFaultAlarm()) {
            return false;
        }
        if (this.isHeatExchangerSensorFaultAlarm() != other.isHeatExchangerSensorFaultAlarm()) {
            return false;
        }
        if (this.isCondenser2SensorFaultAlarm() != other.isCondenser2SensorFaultAlarm()) {
            return false;
        }
        if (this.isCondenserSensorFaultAlarm() != other.isCondenserSensorFaultAlarm()) {
            return false;
        }
        if (this.isCondenser1SensorFaultAlarm() != other.isCondenser1SensorFaultAlarm()) {
            return false;
        }
        if (this.isBackupSystemFaultAlarm() != other.isBackupSystemFaultAlarm()) {
            return false;
        }
        if (this.isCondenserDirtyAlarm() != other.isCondenserDirtyAlarm()) {
            return false;
        }
        if (this.isHighAmbientTemperatureAlarm() != other.isHighAmbientTemperatureAlarm()) {
            return false;
        }
        if (this.isBatteryReversedAlarm() != other.isBatteryReversedAlarm()) {
            return false;
        }
        if (this.isBatteryNotConnectedAlarm() != other.isBatteryNotConnectedAlarm()) {
            return false;
        }
        if (this.isLowBatteryAlarm() != other.isLowBatteryAlarm()) {
            return false;
        }
        if (this.isPowerBoardFaultAlarm() != other.isPowerBoardFaultAlarm()) {
            return false;
        }
        if (this.isVoltageOutofLimitAlarm() != other.isVoltageOutofLimitAlarm()) {
            return false;
        }
        if (this.isBackupSystemRefrigerantAlarm() != other.isBackupSystemRefrigerantAlarm()) {
            return false;
        }
        if (this.isBackupSystemSensorFaultAlarm() != other.isBackupSystemSensorFaultAlarm()) {
            return false;
        }
        return true;
    }

    public boolean isStatusEquals(Object obj) {
        if (this == obj) {
            return true;//地址相等
        }
        if (obj == null) {
            return false;//非空性：对于任意非空引用x，x.equals(null)应该返回false。
        }
        if (!(obj instanceof LTB760AFGEntity)) {
            return false;
        }
        LTB760AFGEntity other = (LTB760AFGEntity) obj;

        if (this.doorInputStatus1 != other.doorInputStatus1) {
            return false;
        }
        if (this.doorInputStatus2 != other.doorInputStatus2) {
            return false;
        }
        if (this.batteryChargingStatus != other.batteryChargingStatus) {
            return false;
        }
        if (this.remoteAlarmOutputStatus != other.remoteAlarmOutputStatus) {
            return false;
        }
        if (this.highTemperatureCompressorStatus != other.highTemperatureCompressorStatus) {
            return false;
        }
        if (this.lowTemperatureCompressorStatus != other.lowTemperatureCompressorStatus) {
            return false;
        }
        if (this.highTemperatureBlowerStatus != other.highTemperatureBlowerStatus) {
            return false;
        }
        if (this.lowTemperatureBlowerStatus != other.lowTemperatureBlowerStatus) {
            return false;
        }
        if (this.risePressureOutputStatus != other.risePressureOutputStatus) {
            return false;
        }
        if (this.dropPressureOutputStatus != other.dropPressureOutputStatus) {
            return false;
        }
        if (this.accapillaryHeatingWireOutputStatus != other.accapillaryHeatingWireOutputStatus) {
            return false;
        }
        if (this.cabinetHeatingWireOutputStatus != other.cabinetHeatingWireOutputStatus) {
            return false;
        }
        if (this.doorHeatingWireOutputStatus != other.doorHeatingWireOutputStatus) {
            return false;
        }
        if (this.balanceHeatingWireOutputStatus != other.balanceHeatingWireOutputStatus) {
            return false;
        }
        if (this.reservedHeatingWireStatus != other.reservedHeatingWireStatus) {
            return false;
        }
        if (this.electromagneticLockOutputStatus != other.electromagneticLockOutputStatus) {
            return false;
        }
        if (this.buzzerOutputStatus != other.buzzerOutputStatus) {
            return false;
        }
        if (this.getBackupSparyStatus() != other.getBackupSparyStatus()) {
            return false;
        }
        if (this.backupConnectionStatus != other.backupConnectionStatus) {
            return false;
        }
        if (this.settingTemperatureValue != other.settingTemperatureValue) {
            return false;
        }
        if (this.highTemperatureAlarmValue != other.highTemperatureAlarmValue) {
            return false;
        }
        if (this.lowTemperatureAlarmValue != other.lowTemperatureAlarmValue) {
            return false;
        }
        return true;
    }
}
