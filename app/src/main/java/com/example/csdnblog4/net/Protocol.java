package com.example.csdnblog4.net;

/**
 * Created by orange on 16/6/13.
 */
public class Protocol {

    private int header;
    private String message;
    private String uuid;

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
