package cn.haier.bio.medical.serialport.rsms.tools;

import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSModulesEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSNetworkEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSStatusEntity;
import cn.haier.bio.medical.serialport.rsms.entity.send.IRSMSSendEntity;
import cn.haier.bio.medical.serialport.tools.ByteBufTools;
import cn.qd.peiwen.pwtools.EmptyUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class RSMSTools {
    public static byte DEVICE = (byte) 0xA0;
    public static byte[] HEADER = {(byte) 0x55, (byte) 0xAA};
    public static byte[] TAILER = {(byte) 0xEA, (byte) 0xEE};
    public static byte[] DEFAULT_CODE = {
            (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20,
            (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20,
            (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20,
            (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20
    };


    public static final int RSMS_COMMAND_QUERY_STATUS = 0x1101;
    public static final int RSMS_COMMAND_QUERY_NETWORK = 0x1102;
    public static final int RSMS_COMMAND_QUERY_MODULES = 0x1103;
    public static final int RSMS_COMMAND_CONFIG_ENTER = 0x1301;
    public static final int RSMS_COMMAND_CONFIG_QUIT = 0x1302;
    public static final int RSMS_COMMAND_CONFIG_NETWORK = 0x1303;
    public static final int RSMS_COMMAND_CONFIG_RECOVERY = 0x1304;
    public static final int RSMS_COMMAND_CONFIG_CLEAR_CACHE = 0x1305;
    public static final int RSMS_COMMAND_COLLECTION_DATA = 0x1501;


    public static final int RSMS_RESPONSE_QUERY_STATUS = 0x1201;
    public static final int RSMS_RESPONSE_QUERY_NETWORK = 0x1202;
    public static final int RSMS_RESPONSE_QUERY_MODULES = 0x1203;
    public static final int RSMS_RESPONSE_CONFIG_ENTER = 0x1401;
    public static final int RSMS_RESPONSE_CONFIG_QUIT = 0x1402;
    public static final int RSMS_RESPONSE_CONFIG_NETWORK = 0x1403;
    public static final int RSMS_RESPONSE_CONFIG_RECOVERY = 0x1404;
    public static final int RSMS_RESPONSE_CONFIG_CLEAR_CACHE = 0x1405;
    public static final int RSMS_RESPONSE_COLLECTION_DATA = 0x1601;

    public static boolean checkFrame(byte[] data) {
        byte check = data[data.length - 3];
        byte l8sum = computeL8SumCode(data, 2, data.length - 5);
        return (check == l8sum);
    }

    public static String generateCode(String code) {
        if (EmptyUtils.isEmpty(code) || !code.startsWith("BE") || code.getBytes().length != 20) {
            return new String(DEFAULT_CODE);
        }
        return code;
    }

    public static byte[] packageString(String src) {
        ByteBuf buffer = Unpooled.buffer(22);
        buffer.writeByte('\"');
        if (EmptyUtils.isNotEmpty(src)) {
            byte[] bytes = src.getBytes();
            buffer.writeBytes(bytes, 0, bytes.length);
        }
        buffer.writeByte('\"');
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data, 0, buffer.readableBytes());
        buffer.release();
        return data;
    }

    public static byte[] packageCommand(int type, String code, byte[] mac, IRSMSSendEntity entity) {
        ByteBuf buffer = Unpooled.buffer(8);

        buffer.writeBytes(HEADER, 0, HEADER.length); //帧头 2位

        byte[] config = EmptyUtils.isEmpty(entity) ? new byte[0] : entity.packageSendMessage();
        //数据长度 = type(1) + cmd(1) + device(1) + data(mac(6) + mcu(6) + BE(22) + entity(n)) + check(1)
        buffer.writeShort(38 + config.length); //长度 2位
        buffer.writeShort(type);   //2位
        buffer.writeByte(DEVICE);  //1位
        buffer.writeBytes(mac, 0, mac.length);//屏DTE-MAC 6位
        buffer.writeBytes(new byte[6], 0, 6); //MCU-DTE-MAC 6位
        buffer.writeBytes(packageString(code));//BE码 22位

        buffer.writeBytes(config, 0, config.length); //其他参数 N位

        byte l8sum = computeL8SumCode(buffer.array(), 2, buffer.readableBytes() - 2);
        buffer.writeByte(l8sum);  //校验和  1位
        buffer.writeBytes(TAILER, 0, TAILER.length); //帧尾 2位
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data, 0, data.length);
        buffer.release();
        return data;
    }

    public static RSMSStatusEntity parseRSMSStatusEntity(byte[] data) {
        ByteBuf buffer = Unpooled.copiedBuffer(data);
        buffer.skipBytes(6);

        RSMSStatusEntity entity = new RSMSStatusEntity();
        byte[] mcu = new byte[6];
        buffer.readBytes(mcu, 0, mcu.length);
        entity.setMcu(mcu);

        entity.setModel(buffer.readByte());

        entity.setImei(parseString(buffer));

        entity.setStatus(buffer.readByte());
        entity.setEncode(buffer.readByte());

        entity.setLevel(buffer.readByte());
        entity.setWifiLevel(buffer.readByte());

        entity.setYear(buffer.readByte());
        entity.setMonth(buffer.readByte());
        entity.setDay(buffer.readByte());
        entity.setHour(buffer.readByte());
        entity.setMinute(buffer.readByte());
        entity.setSecond(buffer.readByte());

        return entity;
    }

    public static RSMSNetworkEntity parseRSMSNetworkEntity(byte[] data) {
        ByteBuf buffer = Unpooled.copiedBuffer(data);
        buffer.skipBytes(6);

        RSMSNetworkEntity entity = new RSMSNetworkEntity();
        byte[] mcu = new byte[6];
        buffer.readBytes(mcu, 0, mcu.length);
        entity.setMcu(mcu);

        entity.setModel(buffer.readByte());

        entity.setDomain(parseString(buffer));
        entity.setAddress(parseString(buffer));
        entity.setPort(parseString(buffer));

        entity.setWifiDomain(parseString(buffer));
        entity.setWifiAddress(parseString(buffer));
        entity.setWifiPort(parseString(buffer));

        entity.setWifiName(parseString(buffer));
        entity.setWifiPassword(parseString(buffer));

        entity.setApn(parseString(buffer));
        entity.setApnName(parseString(buffer));
        entity.setApnPassword(parseString(buffer));

        entity.setNomalFrequency(buffer.readShort());
        entity.setAlarmFrequency(buffer.readShort());

        return entity;
    }

    public static RSMSModulesEntity parseRSMSModulesEntity(byte[] data) {
        ByteBuf buffer = Unpooled.copiedBuffer(data);
        buffer.skipBytes(6);

        RSMSModulesEntity entity = new RSMSModulesEntity();
        byte[] mcu = new byte[6];
        buffer.readBytes(mcu, 0, mcu.length);
        entity.setMcu(mcu);

        entity.setCode(parseString(buffer));
        entity.setModel(buffer.readByte());
        entity.setImei(parseString(buffer));
        entity.setIccid(parseString(buffer));
        entity.setPhone(parseString(buffer));
        entity.setModuleVersion(parseString(buffer));
        entity.setOperator(parseString(buffer));

        byte[] mac = new byte[6];
        buffer.readBytes(mac, 0, mac.length);
        entity.setMac(mac);

        entity.setWifiVersion(parseString(buffer));
        entity.setMcuVersion(parseString(buffer));
        return null;
    }

    public static RSMSResponseEntity parseRSMSResponseEntity(byte[] data){
        ByteBuf buffer = Unpooled.copiedBuffer(data);
        buffer.skipBytes(6);

        RSMSResponseEntity entity = new RSMSResponseEntity();
        byte[] mcu = new byte[6];
        buffer.readBytes(mcu, 0, mcu.length);
        entity.setMcu(mcu);

        entity.setResponse(buffer.readByte());

        return entity;

    }


    //校验和取低8位算法
    public static byte computeL8SumCode(byte[] data) {
        if (EmptyUtils.isEmpty(data)) {
            throw new IllegalArgumentException("The data can not be blank");
        }
        return computeL8SumCode(data, 0, data.length);
    }

    public static byte computeL8SumCode(byte[] data, int offset, int len) {
        if (EmptyUtils.isEmpty(data)) {
            throw new IllegalArgumentException("The data can not be blank");
        }
        if (offset < 0 || offset > data.length - 1) {
            throw new IllegalArgumentException("The offset index out of bounds");
        }
        if (len < 0 || offset + len > data.length) {
            throw new IllegalArgumentException("The len can not be < 0 or (offset + len) index out of bounds");
        }
        int sum = 0;
        for (int pos = offset; pos < offset + len; pos++) {
            sum += data[pos];
        }
        return (byte) sum;
    }

    private static String parseString(ByteBuf buffer) {
        buffer.skipBytes(1); //跳过头\"
        int index = ByteBufTools.indexOf(buffer, (byte) ('\"'));
        byte[] data = new byte[index];
        buffer.readBytes(data, 0, data.length);
        buffer.skipBytes(1);//跳过尾\"
        return new String(data);
    }


}
