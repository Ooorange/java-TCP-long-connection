package com.example.csdnblog4.net;

/**
 * Created by orange on 16/6/17.
 */
public class HeartBeatProtocol extends BasicProtocol {

    private static String COMMEND="0000";
    @Override
    public String getCommend() {
        return COMMEND;
    }

    @Override
    public int parseBinary(byte[] data) throws ProtocolException {
        return COMMEND.length();
    }

}
