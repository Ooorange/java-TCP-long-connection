package com.orange.blog.net;

import com.orange.blog.net.protocol.BasicProtocol;

public interface TCPRequestCallBack {
    void onSuccess(BasicProtocol msg);

    void onFailed(int errorCode, String msg);


}