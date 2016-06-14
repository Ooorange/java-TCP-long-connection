package com.example.csdnblog4.net;

import java.io.DataInputStream;

/**
 * Created by orange on 16/6/14.
 */
public class ServerReadTask implements Runnable {

    DataInputStream inputStream;
    boolean isCancle;
    @Override
    public void run() {
        while (!isCancle){
            SocketUtil.readFromStream(inputStream);
        }

        SocketUtil.closeStream(inputStream);
    }
}
