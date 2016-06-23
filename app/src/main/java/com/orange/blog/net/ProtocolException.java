package com.orange.blog.net;

/**
 * Exception for binary protocol parse error
 * Created by iceqi on 12/16/15.
 */
public class ProtocolException extends Exception
{
    public ProtocolException(String detailMessage) {
        super(detailMessage);
    }

    public ProtocolException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ProtocolException(Throwable throwable) {
        super(throwable);
    }
}
