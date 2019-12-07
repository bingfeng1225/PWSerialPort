package cn.qd.peiwen.demo.finger.hub;

import android.os.HandlerThread;

import java.io.IOException;

import cn.qd.peiwen.pwlogger.PWLogger;
import cn.qd.peiwen.pwtools.EmptyUtils;
import cn.qd.peiwen.serialport.PWSerialPortHelper;
import cn.qd.peiwen.serialport.PWSerialPortListener;
import io.netty.buffer.ByteBuf;

public class HUBController implements PWSerialPortListener {
    private ByteBuf buffer;
    private HandlerThread thread;
    private PWSerialPortHelper helper;

    public HUBController() {

    }

    public void init() {
        if (EmptyUtils.isEmpty(this.helper)) {
            this.helper = new PWSerialPortHelper("HUBController");
            this.helper.setTimeout(0);
            this.helper.setPath("/dev/ttyS7");
            this.helper.setBaudrate(9600);
            this.helper.init(this);
            this.helper.open();
        }
    }

    public void reset() {
        if (EmptyUtils.isNotEmpty(this.helper)) {
            byte[] data = {(byte) 0xAF, (byte) 0xA6, 0x01, 0x01, 0x00, 0x57};
            this.helper.write(data);
        }
    }

    public void release() {
        if (EmptyUtils.isNotEmpty(this.helper)) {
            this.helper.release();
            this.helper = null;
        }
    }

    @Override
    public void onConnected(PWSerialPortHelper helper) {
        PWLogger.d("HUBController connected");
    }

    @Override
    public void onException(PWSerialPortHelper helper) {
        PWLogger.d("HUBController exception");
    }

    @Override
    public void onByteReceived(PWSerialPortHelper helper, byte[] buffer, int length) throws IOException {
        PWLogger.d("HUBController byte received");
    }
}
