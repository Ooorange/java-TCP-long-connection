package com.example.csdnblog4.net;

import java.io.ByteArrayOutputStream;

/**
 * Created by orange on 16/6/17.
 */
public abstract class BasicProtocol {

    public static final int VERSION_LEN=2;//协议的版本
    public static final int COMMEND_LEN=4;//协议的类型: 0000心跳

    public static String VERSION="00";    //目前版本号死的

    public String paraseCommend(byte[] data){
        return new String(data,VERSION_LEN,COMMEND_LEN);
    }

    public abstract String getCommend();

    public byte[] getContentData(){
        ByteArrayOutputStream baos=new ByteArrayOutputStream(VERSION_LEN+COMMEND_LEN);
        baos.write(VERSION.getBytes(),0,VERSION_LEN);
        baos.write(getCommend().getBytes(),0,COMMEND_LEN);
        return baos.toByteArray();
    }

    public int parseBinary(byte[] data) throws ProtocolException{
        String version=new String(data,0,VERSION_LEN);
        if (!version.equals("00")){
            throw new ProtocolException("income version is error"+version);
        }
        return VERSION_LEN+COMMEND_LEN;
    }
}
