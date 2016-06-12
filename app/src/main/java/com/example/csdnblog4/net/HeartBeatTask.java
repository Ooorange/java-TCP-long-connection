package com.example.csdnblog4.net;

import android.util.Log;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

/**
 * 实现5秒的定时发送一个心跳
 * Created by orange on 16/6/8.
 */
public class HeartBeatTask implements Runnable {
    private static final int REPEATTIME = 4000;
    private volatile boolean isKeepAlive = true;
    private Socket socket;

    public HeartBeatTask(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (isKeepAlive) {

                SocketUtil.write2Stream("heartBeat", socket.getOutputStream());
                try {
                    Thread.sleep(REPEATTIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            Log.d(new Date().toString() , " : Time is out, request" + " has been closed.");
            e.printStackTrace();
        }finally {
            if (socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setKeepAlive(boolean isKeepAlive) {
        this.isKeepAlive = isKeepAlive;
    }
}
