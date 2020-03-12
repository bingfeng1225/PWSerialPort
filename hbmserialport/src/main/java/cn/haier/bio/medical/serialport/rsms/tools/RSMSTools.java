package cn.haier.bio.medical.serialport.rsms.tools;

import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSEnterConfigResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSControlCommandEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSQueryModulesResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSNetworkResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSQueryPDAModulesResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSCommontResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.recv.RSMSQueryStatusResponseEntity;
import cn.haier.bio.medical.serialport.rsms.entity.send.IRSMSSendEntity;
import cn.haier.bio.medical.serialport.tools.ByteBufTools;
import cn.qd.peiwen.pwtools.EmptyUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class RSMSTools {
    public static final byte DEVICE = (byte) 0xA0;
    public static final byte DTE_CONFIG = (byte) 0xB0;
    public static final byte PDA_CONFIG = (byte) 0xB1;
    public static final byte[] HEADER = {(byte) 0x55, (byte) 0xAA};
    public static final byte[] TAILER = {(byte) 0xEA, (byte) 0xEE};
    public static final byte[] DEFAULT_MAC = {
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF
    };
    public static final byte[] DEFAULT_BE_CODE = {
            (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20,
            (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20,
            (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20,
            (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20, (byte) 0x20
    };

    public static final int RSMS_COMMAND_QUERY_STATUS = 0x1101;
    public static final int RSMS_RESPONSE_QUERY_STATUS = 0x1201;

    public static final int RSMS_COMMAND_QUERY_NETWORK = 0x1102;
    public static final int RSMS_RESPONSE_QUERY_NETWORK = 0x1202;

    public static final int RSMS_COMMAND_QUERY_MODULES = 0x1103;
    public static final int RSMS_RESPONSE_QUERY_MODULES = 0x1203;

    public static final int RSMS_COMMAND_QUERY_PDA_MODULES = 0x1104;
    public static final int RSMS_RESPONSE_QUERY_PDA_MODULES = 0x1204;

    public static final int RSMS_COMMAND_ENTER_CONFIG = 0x1301;
    public static final int RSMS_RESPONSE_ENTER_CONFIG = 0x1401;

    public static final int RSMS_COMMAND_CONFIG_QUIT = 0x1302;
    public static final int RSMS_RESPONSE_CONFIG_QUIT = 0x1402;

    public static final int RSMS_COMMAND_CONFIG_DTE_MODEL = 0x1303;
    public static final int RSMS_RESPONSE_CONFIG_DTE_MODEL = 0x1403;

    public static final int RSMS_COMMAND_CONFIG_A_MODEL = 0x1304;
    public static final int RSMS_RESPONSE_CONFIG_A_MODEL = 0x1404;

    public static final int RSMS_COMMAND_CONFIG_B_MODEL = 0x1305;
    public static final int RSMS_RESPONSE_CONFIG_B_MODEL = 0x1405;

    public static final int RSMS_COMMAND_CONFIG_RECOVERY = 0x1306;
    public static final int RSMS_RESPONSE_CONFIG_RECOVERY = 0x1406;

    public static final int RSMS_COMMAND_CONFIG_CLEAR_CACHE = 0x1307;
    public static final int RSMS_RESPONSE_CONFIG_CLEAR_CACHE = 0x1407;

    public static final int RSMS_COMMAND_COLLECTION_DATA = 0x1501;
    public static final int RSMS_RESPONSE_COLLECTION_DATA = 0x1601;

    public static final int RSMS_CONTROL_COMMAND = 0xC001;
    public static final int RSMS_CONTROL_RESPONSE = 0xC101;

    public static boolean checkFrame(byte[] data) {
        byte check = data[data.length - 3];
        byte l8sum = computeL8SumCode(data, 2, data.length - 5);
        return (check == l8sum);
    }

    public static byte[] generateMac(byte[] mac) {
        if (EmptyUtils.isEmpty(mac) || mac.length != 6) {
            return DEFAULT_MAC;
        }
        return mac;
    }

    public static String generateCode(String code) {
        if (EmptyUtils.isEmpty(code) || !code.startsWith("BE") || code.getBytes().length != 20) {
            return new String(DEFAULT_BE_CODE);
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

    public static byte[] packageCommand(int type, IRSMSSendEntity entity) {
        ByteBuf buffer = Unpooled.buffer(8);
        buffer.writeBytes(HEADER, 0, HEADER.length); //帧头 2位
        byte[] buf = EmptyUtils.isEmpty(entity) ? new byte[0] : entity.packageSendMessage();
        //数据长度 = type(1) + cmd(1) + device(1) + entity(n) + check(1)
        buffer.writeShort(4 + buf.length); //长度 2位
        buffer.writeShort(type);   //2位
        buffer.writeByte(DEVICE);  //1位

        buffer.writeBytes(buf, 0, buf.length); //其他参数 N位

        byte l8sum = computeL8SumCode(buffer.array(), 2, buffer.readableBytes() - 2);
        buffer.writeByte(l8sum);  //校验和  1位
        buffer.writeBytes(TAILER, 0, TAILER.length); //帧尾 2位

        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data, 0, data.length);
        buffer.release();
        return data;
    }

    public static RSMSQueryStatusResponseEntity parseRSMSStatusEntity(byte[] data) {
        ByteBuf buffer = Unpooled.copiedBuffer(data);
        buffer.skipBytes(6);

        RSMSQueryStatusResponseEntity entity = new RSMSQueryStatusResponseEntity();

        entity.setModel(buffer.readByte());

        entity.setStatus(buffer.readByte());
        entity.setEncode(buffer.readByte());

        entity.setLevel(buffer.readByte());
        entity.setWifiLevel(buffer.readByte());

        entity.setUploadFrequency(buffer.readShort());
        entity.setAcquisitionFrequency(buffer.readShort());

        entity.setYear(buffer.readByte());
        entity.setMonth(buffer.readByte());
        entity.setDay(buffer.readByte());
        entity.setHour(buffer.readByte());
        entity.setMinute(buffer.readByte());
        entity.setSecond(buffer.readByte());

        return entity;
    }

    public static RSMSNetworkResponseEntity parseRSMSNetworkEntity(byte[] data) {
        ByteBuf buffer = Unpooled.copiedBuffer(data);
        buffer.skipBytes(6);

        RSMSNetworkResponseEntity entity = new RSMSNetworkResponseEntity();

        entity.setModel(buffer.readByte());

        entity.setAddress(parseString(buffer));
        entity.setPort(parseString(buffer));

        entity.setWifiName(parseString(buffer));
        entity.setWifiPassword(parseString(buffer));

        entity.setApn(parseString(buffer));
        entity.setApnName(parseString(buffer));
        entity.setApnPassword(parseString(buffer));

        return entity;
    }

    public static RSMSQueryModulesResponseEntity parseRSMSModulesEntity(byte[] data) {
        ByteBuf buffer = Unpooled.copiedBuffer(data);
        buffer.skipBytes(6);

        RSMSQueryModulesResponseEntity entity = new RSMSQueryModulesResponseEntity();
        byte[] mcu = new byte[12];
        buffer.readBytes(mcu, 0, mcu.length);
        entity.setMcu(mcu);

        byte[] mac = new byte[6];
        buffer.readBytes(mac, 0, mac.length);
        entity.setMac(mac);

        entity.setCode(parseString(buffer));

        entity.setImei(parseString(buffer));
        entity.setIccid(parseString(buffer));
        entity.setPhone(parseString(buffer));

        entity.setModuleVersion(parseString(buffer));
        entity.setWifiVersion(parseString(buffer));
        entity.setMcuVersion(parseString(buffer));

        entity.setOperator(parseString(buffer));
        return entity;
    }

    public static RSMSQueryPDAModulesResponseEntity parseRSMSPDAModulesEntity(byte[] data) {
        ByteBuf buffer = Unpooled.copiedBuffer(data);
        buffer.skipBytes(6);
        RSMSQueryPDAModulesResponseEntity entity = new RSMSQueryPDAModulesResponseEntity();
        entity.setDeviceType(buffer.readByte());
        entity.setConfigType(buffer.readByte());
        return entity;
    }

    public static RSMSCommontResponseEntity parseRSMSResponseEntity(byte[] data) {
        ByteBuf buffer = Unpooled.copiedBuffer(data);
        buffer.skipBytes(6);
        RSMSCommontResponseEntity entity = new RSMSCommontResponseEntity();
        entity.setResponse(buffer.readByte());
        return entity;
    }

    public static RSMSEnterConfigResponseEntity parseRSMSConfigModelResponseEntity(byte[] data) {
        ByteBuf buffer = Unpooled.copiedBuffer(data);
        buffer.skipBytes(6);

        RSMSEnterConfigResponseEntity entity = new RSMSEnterConfigResponseEntity();
        entity.setConfigModel(buffer.readByte());
        entity.setResponse(buffer.readByte());

        return entity;
    }

    public static RSMSControlCommandEntity parseRSMSControlEntity(byte[] data) {
        ByteBuf buffer = Unpooled.copiedBuffer(data);
        buffer.skipBytes(6);
        RSMSControlCommandEntity entity = new RSMSControlCommandEntity();
        entity.setYear(buffer.readByte());
        entity.setMonth(buffer.readByte());
        entity.setDay(buffer.readByte());
        entity.setHour(buffer.readByte());
        entity.setMinute(buffer.readByte());
        entity.setSecond(buffer.readByte());
        entity.setCommand(buffer.readShort());
        int read = buffer.readerIndex();
        int write = buffer.writerIndex();
        byte[] control = new byte[write - read - 3];
        buffer.readBytes(control, 0, control.length);
        entity.setControl(control);
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
        byte[] data = new byte[index - buffer.readerIndex()];
        buffer.readBytes(data, 0, data.length);
        buffer.skipBytes(1);//跳过尾\"
        return new String(data);
    }


}
