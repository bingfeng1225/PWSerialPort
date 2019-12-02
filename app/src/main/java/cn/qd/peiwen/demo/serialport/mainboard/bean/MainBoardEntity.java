package cn.qd.peiwen.demo.serialport.mainboard.bean;

import java.util.Set;

import androidx.annotation.Nullable;

public class MainBoardEntity {
    private short temperature; //箱内温度*10 2
    private short ambientTemperature; //环境温度 2
    private short condenserTemperature;//冷凝器温度 2
    private short heatExchangerTemperature;//热交换器温度 2

    private short supplyVoltage;//电源电压 2
    private short batteryVoltage;//电池电压*10 2

    private short doorInputStatus1;//门开关输入状态1 2
    private short doorInputStatus2;//门开关输入状态2 2

    private short batteryChargingStatus;//电池充电状态 2

    private short remoteAlarmOutputStatus;//远程报警输出状态 2

    private short highTemperatureCompressorStatus;//高温压机状态 2
    private short lowTemperatureCompressorStatus;//低温压机状态 2

    private short condensateBlowerOutputStatus1;//冷凝风机1输出状态 2
    private short condensateBlowerOutputStatus2;//冷凝风机2输出状态 2

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

    private short thermocoupleTemperature1;//热电偶温度1(预留)    2
    private short thermocoupleTemperature2;//热电偶温度2(预留)    2
    private short thermocoupleTemperature3;//热电偶温度3(预留)    2
    private short thermocoupleTemperature4;//热电偶温度4(预留)    2
    private short thermocoupleTemperature5;//热电偶温度5(预留)    2
    private short thermocoupleTemperature6;//热电偶温度6(预留)    2
    private short thermocoupleTemperature7;//热电偶温度7(预留)    2
    private short thermocoupleTemperature8;//热电偶温度8(预留)    2
    private short thermocoupleTemperature9;//热电偶温度9(预留)    2
    private short thermocoupleTemperature10;//热电偶温度10(预留)  2

    private short backupConnectionStatus;//后备系统连接状态 2

    private short settingTemperatureValue;//设定温度*10 2
    private short highTemperatureAlarmValue;//高温报警值*10 2
    private short lowTemperatureAlarmValue;//低温报警值*10 2

    private byte evaporationAStatus;//A蒸发风机输出状态(预留) 1
    private byte evaporationBStatus;//B蒸发风机输出状态(预留) 1

    private byte electromagneticAOutputStatus;//A电磁阀输出状态(预留) 1
    private byte electromagneticBOutputStatus;//B电磁阀输出状态(预留) 1

    private byte systemDefrostingStatus;//系统化霜状态(预留) 1
    private short systemADefrostingJudgmentValue;//A系统化霜判断值(预留) 2
    private short systemBDefrostingJudgmentValue;//b系统化霜判断值(预留) 2

    private short systemXValueGenerated;//系统X值已生成(预留) 2


    public MainBoardEntity() {
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

    public short getSupplyVoltage() {
        return supplyVoltage;
    }

    public void setSupplyVoltage(short supplyVoltage) {
        this.supplyVoltage = supplyVoltage;
    }

    public short getBatteryVoltage() {
        return batteryVoltage;
    }

    public void setBatteryVoltage(short batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
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

    public short getCondensateBlowerOutputStatus1() {
        return condensateBlowerOutputStatus1;
    }

    public void setCondensateBlowerOutputStatus1(short condensateBlowerOutputStatus1) {
        this.condensateBlowerOutputStatus1 = condensateBlowerOutputStatus1;
    }

    public short getCondensateBlowerOutputStatus2() {
        return condensateBlowerOutputStatus2;
    }

    public void setCondensateBlowerOutputStatus2(short condensateBlowerOutputStatus2) {
        this.condensateBlowerOutputStatus2 = condensateBlowerOutputStatus2;
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

    public byte getEvaporationAStatus() {
        return evaporationAStatus;
    }

    public void setEvaporationAStatus(byte evaporationAStatus) {
        this.evaporationAStatus = evaporationAStatus;
    }

    public byte getEvaporationBStatus() {
        return evaporationBStatus;
    }

    public void setEvaporationBStatus(byte evaporationBStatus) {
        this.evaporationBStatus = evaporationBStatus;
    }

    public byte getElectromagneticAOutputStatus() {
        return electromagneticAOutputStatus;
    }

    public void setElectromagneticAOutputStatus(byte electromagneticAOutputStatus) {
        this.electromagneticAOutputStatus = electromagneticAOutputStatus;
    }

    public byte getElectromagneticBOutputStatus() {
        return electromagneticBOutputStatus;
    }

    public void setElectromagneticBOutputStatus(byte electromagneticBOutputStatus) {
        this.electromagneticBOutputStatus = electromagneticBOutputStatus;
    }

    public byte getSystemDefrostingStatus() {
        return systemDefrostingStatus;
    }

    public void setSystemDefrostingStatus(byte systemDefrostingStatus) {
        this.systemDefrostingStatus = systemDefrostingStatus;
    }

    public short getSystemADefrostingJudgmentValue() {
        return systemADefrostingJudgmentValue;
    }

    public void setSystemADefrostingJudgmentValue(short systemADefrostingJudgmentValue) {
        this.systemADefrostingJudgmentValue = systemADefrostingJudgmentValue;
    }

    public short getSystemBDefrostingJudgmentValue() {
        return systemBDefrostingJudgmentValue;
    }

    public void setSystemBDefrostingJudgmentValue(short systemBDefrostingJudgmentValue) {
        this.systemBDefrostingJudgmentValue = systemBDefrostingJudgmentValue;
    }

    public short getSystemXValueGenerated() {
        return systemXValueGenerated;
    }

    public void setSystemXValueGenerated(short systemXValueGenerated) {
        this.systemXValueGenerated = systemXValueGenerated;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(this == obj){
            return true;//地址相等
        }
        if(obj == null){
            return false;//非空性：对于任意非空引用x，x.equals(null)应该返回false。
        }
        if(!(obj instanceof MainBoardEntity)){
            return false;
        }
        MainBoardEntity other = (MainBoardEntity) obj;
        if(this.temperature != other.temperature) {
            return false;
        }
        if(this.ambientTemperature != other.ambientTemperature) {
            return false;
        }
        if(this.condenserTemperature != other.condenserTemperature) {
            return false;
        }
        if(this.heatExchangerTemperature != other.heatExchangerTemperature) {
            return false;
        }
        if(this.supplyVoltage != other.supplyVoltage) {
            return false;
        }
        if(this.batteryVoltage != other.batteryVoltage) {
            return false;
        }
        if(this.doorInputStatus1 != other.doorInputStatus1) {
            return false;
        }
        if(this.doorInputStatus2 != other.doorInputStatus2) {
            return false;
        }
        if(this.batteryChargingStatus != other.batteryChargingStatus) {
            return false;
        }
        if(this.remoteAlarmOutputStatus != other.remoteAlarmOutputStatus) {
            return false;
        }
        if(this.highTemperatureCompressorStatus != other.highTemperatureCompressorStatus) {
            return false;
        }
        if(this.lowTemperatureCompressorStatus != other.lowTemperatureCompressorStatus) {
            return false;
        }
        if(this.condensateBlowerOutputStatus1 != other.condensateBlowerOutputStatus1) {
            return false;
        }
        if(this.condensateBlowerOutputStatus2 != other.condensateBlowerOutputStatus2) {
            return false;
        }
        if(this.risePressureOutputStatus != other.risePressureOutputStatus) {
            return false;
        }
        if(this.dropPressureOutputStatus != other.dropPressureOutputStatus) {
            return false;
        }
        if(this.accapillaryHeatingWireOutputStatus != other.accapillaryHeatingWireOutputStatus) {
            return false;
        }
        if(this.cabinetHeatingWireOutputStatus != other.cabinetHeatingWireOutputStatus) {
            return false;
        }
        if(this.doorHeatingWireOutputStatus != other.doorHeatingWireOutputStatus) {
            return false;
        }
        if(this.balanceHeatingWireOutputStatus != other.balanceHeatingWireOutputStatus) {
            return false;
        }
        if(this.reservedHeatingWireStatus != other.reservedHeatingWireStatus) {
            return false;
        }
        if(this.electromagneticLockOutputStatus != other.electromagneticLockOutputStatus) {
            return false;
        }
        if(this.buzzerOutputStatus != other.buzzerOutputStatus) {
            return false;
        }
        if(this.alarmStatus1 != other.alarmStatus1) {
            return false;
        }
        if(this.alarmStatus2 != other.alarmStatus2) {
            return false;
        }
        if(this.backupTemperature != other.backupTemperature) {
            return false;
        }
        if(this.backupStatus != other.backupStatus) {
            return false;
        }
        if(this.thermocoupleTemperature1 != other.thermocoupleTemperature1) {
            return false;
        }
        if(this.thermocoupleTemperature2 != other.thermocoupleTemperature2) {
            return false;
        }
        if(this.thermocoupleTemperature3 != other.thermocoupleTemperature3) {
            return false;
        }
        if(this.thermocoupleTemperature4 != other.thermocoupleTemperature4) {
            return false;
        }
        if(this.thermocoupleTemperature5 != other.thermocoupleTemperature5) {
            return false;
        }
        if(this.thermocoupleTemperature6 != other.thermocoupleTemperature6) {
            return false;
        }
        if(this.thermocoupleTemperature7 != other.thermocoupleTemperature7) {
            return false;
        }
        if(this.thermocoupleTemperature8 != other.thermocoupleTemperature8) {
            return false;
        }
        if(this.thermocoupleTemperature9 != other.thermocoupleTemperature9) {
            return false;
        }
        if(this.thermocoupleTemperature10 != other.thermocoupleTemperature10) {
            return false;
        }
        if(this.backupConnectionStatus != other.backupConnectionStatus) {
            return false;
        }
        if(this.settingTemperatureValue != other.settingTemperatureValue) {
            return false;
        }
        if(this.highTemperatureAlarmValue != other.highTemperatureAlarmValue) {
            return false;
        }
        if(this.lowTemperatureAlarmValue != other.lowTemperatureAlarmValue) {
            return false;
        }
        if(this.evaporationAStatus != other.evaporationAStatus) {
            return false;
        }
        if(this.evaporationBStatus != other.evaporationBStatus) {
            return false;
        }
        if(this.electromagneticAOutputStatus != other.electromagneticAOutputStatus) {
            return false;
        }
        if(this.electromagneticBOutputStatus != other.electromagneticBOutputStatus) {
            return false;
        }
        if(this.systemDefrostingStatus != other.systemDefrostingStatus) {
            return false;
        }
        if(this.systemADefrostingJudgmentValue != other.systemADefrostingJudgmentValue) {
            return false;
        }
        if(this.systemBDefrostingJudgmentValue != other.systemBDefrostingJudgmentValue) {
            return false;
        }
        if(this.systemXValueGenerated != other.systemXValueGenerated) {
            return false;
        }
        return true;
    }
}
