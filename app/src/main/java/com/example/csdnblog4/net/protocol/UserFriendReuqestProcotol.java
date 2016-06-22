package com.example.csdnblog4.net.protocol;

import com.example.csdnblog4.common.ProjectApplication;
import com.example.csdnblog4.net.ProtocolException;

import java.io.ByteArrayOutputStream;

/**
 * Created by orange on 16/6/22.
 */
public class UserFriendReuqestProcotol extends BasicProtocol {

    public static final String USERFRIENDREQUESTCOMMEND="0003";

    private String usersJson;
    private String selfUUID;


    public UserFriendReuqestProcotol(){
        selfUUID= ProjectApplication.getUUID();
    }

    @Override
    public String getCommend() {
        return USERFRIENDREQUESTCOMMEND;
    }

    @Override
    public byte[] getContentData() {
        byte[] pre=super.getContentData();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        baos.write(pre,0,pre.length);
        baos.write(selfUUID.getBytes(),0,ChatMsgProcotol.SLEFUUID_LEN);
        return baos.toByteArray();
    }

    public String getUsersJson() {
        return usersJson;
    }

    @Override
    public int parseBinary(byte[] data) throws ProtocolException {
        int pos=super.parseBinary(data);
        usersJson=new String(data,pos,data.length-pos);
        return pos;
    }

    public void setUsersJson(String usersJson) {
        this.usersJson = usersJson;
    }

}
