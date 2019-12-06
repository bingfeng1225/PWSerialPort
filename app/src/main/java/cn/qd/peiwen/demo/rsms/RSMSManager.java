package cn.qd.peiwen.demo.rsms;

import android.os.Build;

import java.io.IOException;
import java.lang.ref.WeakReference;

import cn.qd.peiwen.pwtools.EmptyUtils;
import cn.qd.peiwen.serialport.PWSerialPortHelper;
import cn.qd.peiwen.serialport.PWSerialPortListener;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class RSMSManager implements PWSerialPortListener {
    private ByteBuf buffer;
    private PWSerialPortHelper helper;

    private boolean ready = false;
    private boolean enabled = false;
    private static RSMSManager manager;
    private WeakReference<IRSMSListener> listener;

    public static RSMSManager getInstance() {
        if (manager == null) {
            synchronized (RSMSManager.class) {
                if (manager == null)
                    manager = new RSMSManager();
            }
        }
        return manager;
    }

    public void init(IRSMSListener listener){
        createBuffer();
        createHelper();
        this.listener = new WeakReference<>(listener);
    }

    public void enable() {
        if (this.isInitialized() && !this.enabled) {
            this.enabled = true;
            this.helper.open();
        }
    }

    public void disable() {
        if (this.isInitialized() && this.enabled) {
            this.enabled = false;
            this.helper.close();
        }
    }

    public void release(){
        this.destoryHelper();
        this.destoryBuffer();
    }

    private boolean isInitialized() {
        if (EmptyUtils.isEmpty(this.helper)) {
            return false;
        }
        if (EmptyUtils.isEmpty(this.buffer)) {
            return false;
        }
        return true;
    }

    private void createBuffer() {
        if (EmptyUtils.isEmpty(this.buffer)) {
            this.buffer = Unpooled.buffer(4);
        }
    }

    private void destoryBuffer() {
        if (EmptyUtils.isNotEmpty(this.buffer)) {
            this.buffer.release();
            this.buffer = null;
        }
    }

    private void createHelper() {
        if (EmptyUtils.isEmpty(this.helper)) {
            this.helper = new PWSerialPortHelper("RFIDManager");
            this.helper.setTimeout(2);
            if ("magton".equals(Build.MODEL)) {
                this.helper.setPath("/dev/ttyS5");
            } else {
                this.helper.setPath("/dev/ttyS1");
            }
            this.helper.setBaudrate(115200);
            this.helper.init(this);
        }
    }

    private void destoryHelper() {
        if (EmptyUtils.isNotEmpty(this.helper)) {
            this.helper.release();
            this.helper = null;
        }
    }

    @Override
    public void onConnected(PWSerialPortHelper helper) {
        if(!this.isInitialized() || !helper.equals(this.helper))   {
            return;
        }
    }

    @Override
    public void onException(PWSerialPortHelper helper) {
        if(!this.isInitialized() || !helper.equals(this.helper))   {
            return;
        }
    }

    @Override
    public void onByteReceived(PWSerialPortHelper helper, byte[] buffer) throws IOException {
        if(!this.isInitialized() || !helper.equals(this.helper))   {
            return;
        }
    }
}
