package com.example.csdnblog4.net.protocol;

import com.example.csdnblog4.common.ProjectApplication;

import java.io.ByteArrayOutputStream;

/**
 * Created by orange on 16/6/22.
 */
public class RegisterProcotol extends BasicProtocol {

    public static final String REGISTERCOMMEND="0004";

    public String selfUUID="00000000000000000000000000000000";

    public RegisterProcotol(){
        selfUUID= ProjectApplication.getUUID();
    }
    @Override
    public String getCommend() {
        return REGISTERCOMMEND;
    }

    @Override
    public byte[] getContentData() {
        byte[] pre=super.getContentData();

        ByteArrayOutputStream baos=new ByteArrayOutputStream(ChatMsgProcotol.SLEFUUID_LEN+BasicProtocol.COMMEND_LEN+BasicProtocol.VERSION_LEN);
        baos.write(pre,0,pre.length);
        baos.write(selfUUID.getBytes(),0,ChatMsgProcotol.SLEFUUID_LEN);
        return baos.toByteArray();
    }
}
