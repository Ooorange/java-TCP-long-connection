package com.example.csdnblog4.net;

import java.io.UnsupportedEncodingException;

/**
 * Created by orange on 16/6/21.
 */
public class ResponseProcotol extends BasicProtocol {

    public static final String RESPONSECOMMEND="0002";
    private String body;


    @Override
    public int parseBinary(byte[] data) throws ProtocolException {
        int pos=super.parseBinary(data);
        try {
            body=new String(data,pos,data.length-pos,"utf-8");
        } catch (UnsupportedEncodingException e) {
           throw new ProtocolException("不支持的编码格式");
        }
        return pos;
    }

    @Override
    public String getCommend() {
        return RESPONSECOMMEND;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
