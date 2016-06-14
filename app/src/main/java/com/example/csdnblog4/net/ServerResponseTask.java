package com.example.csdnblog4.net;

import java.io.DataInputStream;

/**
 * Created by orange on 16/6/14.
 */
public class ServerResponseTask implements Runnable {
    @Override
    public void run() {

    }




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

    public class ServerWriteTask implements Runnable {

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
}
