package cn.haier.bio.medical.serialport.rsms.entity.send;

import cn.haier.bio.medical.serialport.rsms.tools.RSMSTools;

public class RSMSEnterConfigModelEntity implements IRSMSSendEntity {
    private boolean pda;

    public RSMSEnterConfigModelEntity(boolean pda) {
        this.pda = pda;
    }

    @Override
    public byte[] packageSendMessage() {
        if(!this.pda) {
            return new byte[]{RSMSTools.DTE_CONFIG};
        }else{
            return new byte[]{RSMSTools.PDA_CONFIG};
        }
    }
}
