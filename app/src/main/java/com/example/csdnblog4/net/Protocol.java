package com.example.csdnblog4.net;

import java.io.Serializable;

/**
 * Created by orange on 16/6/13.
 */
public class Protocol implements Serializable{

    private int header;
    private String message;
    private String selfUUid;
    private String msgTargetUUID;

    public String getMsgTargetUUID() {
        return msgTargetUUID;
    }

    public void setMsgTargetUUID(String msgTargetUUID) {
        this.msgTargetUUID = msgTargetUUID;
    }

    public int getHeader() {
        return header;
    }

    public void setHeader(int header) {
        this.header = header;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSelfUUid() {
        return selfUUid;
    }

    public void setSelfUUid(String selfUUid) {
        this.selfUUid = selfUUid;
    }
}
