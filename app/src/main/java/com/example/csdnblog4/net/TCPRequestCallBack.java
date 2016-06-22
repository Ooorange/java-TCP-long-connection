package com.example.csdnblog4.net;

import com.example.csdnblog4.net.protocol.BasicProtocol;

public interface TCPRequestCallBack {
    void onSuccess(BasicProtocol msg);

    void onFailed(int errorCode, String msg);


}