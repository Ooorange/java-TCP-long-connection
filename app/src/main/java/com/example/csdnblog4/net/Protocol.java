package com.example.csdnblog4.net;

import java.io.Serializable;

/**
 * Created by orange on 16/6/13.
 */
public class Protocol implements Serializable{

    private int header;
    private String message="";
    private String selfUUid="00000000000000000000000000000000";
    private String msgTargetUUID="00000000000000000000000000000000";
    private int clientVersion=1;


    public Protocol(String msg){
        this.message=msg;
    }

    public Protocol(){

    }
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

    public int getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(int clientVersion) {
        this.clientVersion = clientVersion;
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

    @Override
    public String toString() {
        return "信息:"+message+",自身UUID:"+selfUUid+",消息目的UUID:"+msgTargetUUID+",版本号:"+clientVersion;
    }
}
