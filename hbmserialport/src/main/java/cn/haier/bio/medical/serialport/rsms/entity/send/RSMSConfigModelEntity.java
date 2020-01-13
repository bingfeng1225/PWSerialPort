package cn.haier.bio.medical.serialport.rsms.entity.send;

import cn.haier.bio.medical.serialport.rsms.tools.RSMSTools;

public class RSMSConfigModelEntity implements IRSMSSendEntity {
    private boolean pda;

    public RSMSConfigModelEntity(boolean pda) {
        this.pda = pda;
    }

    @Override
    public byte[] packageSendMessage() {
        if(!this.pda) {
            return new byte[]{RSMSTools.CONFIG};
        }else{
            return new byte[]{RSMSTools.PDA_CONFIG};
        }
    }
}
