package com.example.csdnblog4.net.protocol;

import com.example.csdnblog4.net.ProtocolException;

/**
 * Created by orange on 16/6/17.
 */
public class HeartBeatProtocol extends BasicProtocol {

    public static String HEART_COMMEND="0000";
    @Override
    public String getCommend() {
        return HEART_COMMEND;
    }

    @Override
    public int parseBinary(byte[] data) throws ProtocolException {
        return HEART_COMMEND.length();
    }

}
