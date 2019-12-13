package cn.haier.bio.medical.serialport.refriger.entity;

public class LTB760AGStatusEntity {
    private byte backupBatteryStatus;//后备板电池接入状态 1

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

    private short backupStatus;//后备系统各种状态 2

    private short backupConnectionStatus;//后备系统连接状态 2

    private short settingTemperatureValue;//设定温度*10 2
    private short highTemperatureAlarmValue;//高温报警值*10 2
    private short lowTemperatureAlarmValue;//低温报警值*10 2


    public LTB760AGStatusEntity() {
    }

    public byte getBackupBatteryStatus() {
        return backupBatteryStatus;
    }

    public void setBackupBatteryStatus(byte backupBatteryStatus) {
        this.backupBatteryStatus = backupBatteryStatus;
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

    public short getBackupStatus() {
        return backupStatus;
    }

    public void setBackupStatus(short backupStatus) {
        this.backupStatus = backupStatus;
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

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;//地址相等
        }
        if(obj == null){
            return false;//非空性：对于任意非空引用x，x.equals(null)应该返回false。
        }
        if(!(obj instanceof LTB760AGStatusEntity)){
            return false;
        }
        LTB760AGStatusEntity other = (LTB760AGStatusEntity) obj;

        if(this.backupBatteryStatus != other.backupBatteryStatus) {
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
        if(this.backupStatus != other.backupStatus) {
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
        return true;
    }
}
