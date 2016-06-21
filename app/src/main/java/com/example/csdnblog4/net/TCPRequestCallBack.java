package com.example.csdnblog4.net;

public interface TCPRequestCallBack {
    void onSuccess(BasicProtocol msg);

    void onFailed(int errorCode, String msg);
}