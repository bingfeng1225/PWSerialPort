package cn.haier.bio.medical.serialport.refriger.entity;

public class LTB760AGBasicsEntity {
    private short temperature; //箱内温度*10 2
    private short ambientTemperature; //环境温度 2
    private short condenserTemperature;//冷凝器温度 2
    //双系统：冷凝器2
    private short condenser2Temperature;//热交换器温度 2
    //变频/T系列：热交换器
    private short heatExchangerTemperature;//热交换器温度 2

    private short supplyVoltage;//电源电压 2
    private byte mainBatteryVoltage;//电池电压*10 1

    private short backupTemperature;//后备系统检测箱内温度*10 2

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

    public LTB760AGBasicsEntity() {

    }

    public short getTemperature() {
        return temperature;
    }

    public void setTemperature(short temperature) {
        this.temperature = temperature;
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

    public short getBackupTemperature() {
        return backupTemperature;
    }

    public void setBackupTemperature(short backupTemperature) {
        this.backupTemperature = backupTemperature;
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

    public short getCondenser2Temperature() {
        return condenser2Temperature;
    }

    public void setCondenser2Temperature(short condenser2Temperature) {
        this.condenser2Temperature = condenser2Temperature;
    }

    public short getHeatExchangerTemperature() {
        return heatExchangerTemperature;
    }

    public void setHeatExchangerTemperature(short heatExchangerTemperature) {
        this.heatExchangerTemperature = heatExchangerTemperature;
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

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;//地址相等
        }
        if(obj == null){
            return false;//非空性：对于任意非空引用x，x.equals(null)应该返回false。
        }
        if(!(obj instanceof LTB760AGBasicsEntity)){
            return false;
        }
        LTB760AGBasicsEntity other = (LTB760AGBasicsEntity) obj;
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
        if(this.mainBatteryVoltage != other.mainBatteryVoltage) {
            return false;
        }
        if(this.backupTemperature != other.backupTemperature) {
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
        return true;
    }
}
