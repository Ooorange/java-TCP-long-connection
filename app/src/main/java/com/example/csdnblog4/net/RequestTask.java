package com.example.csdnblog4.net;

import android.util.Log;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;

import javax.net.SocketFactory;

/**
 * 发送数据,并且启动心跳,连续发送3次数据
 * Created by orange on 16/6/8.
 */
public class RequestTask implements Runnable {
    private static final String ADDRESS="192.168.56.1";
    private static final int PORT =9003;

    private boolean isLongConnection=true;
    public RequestTask(){

    }

    @Override
    public void run() {
        Socket socket=null;
        HeartBeatTask heartBeatTask=null;
        try {
            socket= SocketFactory.getDefault().createSocket("192.168.1.10",9010);


            SocketUtil.write2Stream("i am client data", socket.getOutputStream());

            if (isLongConnection){
                heartBeatTask=new HeartBeatTask(socket);
                Executors.newCachedThreadPool().execute(heartBeatTask);
            }

            for (int i=0;i<3;i++){
                SocketUtil.write2Stream("this is client,counter to:" + i, socket.getOutputStream());

                Log.d("orange-","等待服务端数据--------");
                String reciverData=SocketUtil.readFromStream(socket.getInputStream());
                Log.d("orange-","收到服务端数据:"+""+reciverData);

                if (i==1){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }

            if (isLongConnection)
            {
                heartBeatTask.setKeepAlive(false);
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
