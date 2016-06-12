package com.example.csdnblog4.net;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Executors;

import javax.net.SocketFactory;

/**
 * 发送数据,并且启动心跳,连续发送3次数据
 * Created by orange on 16/6/8.
 */
public class RequestTask implements Runnable {
    private static final String ADDRESS = "192.168.56.1";
    private static final int PORT = 9003;
    private OutputStream outputStream;
    private static final int SUCCESS=100;
    private static final int FAILED=-1;
    private InputStream inputStream;
    private boolean isLongConnection = true;
    private TCPRequestCallBack tcpRequestCallBack;
    private Handler handler;
    public RequestTask(TCPRequestCallBack tcpRequestCallBack) {
        this.tcpRequestCallBack=tcpRequestCallBack;
        handler=new MyHandler(tcpRequestCallBack);
    }

    @Override
    public void run() {
        Socket socket = null;
        HeartBeatTask heartBeatTask = null;
        try {
            socket = SocketFactory.getDefault().createSocket("192.168.56.1", 9011);
            outputStream = socket.getOutputStream();
            SocketUtil.write2Stream("i am client data", outputStream);
            if (isLongConnection) {
                heartBeatTask = new HeartBeatTask(socket);
                Executors.newCachedThreadPool().execute(heartBeatTask);
            }
            inputStream = socket.getInputStream();
            for (int i = 0; i < 3; i++) {
                SocketUtil.write2Stream("this is client,counter to:" + i, outputStream);
                String reciverData = SocketUtil.readFromStream(inputStream);
                Message message=handler.obtainMessage(SUCCESS);
                message.what=SUCCESS;
                message.obj=reciverData;
                handler.sendMessage(message);

                if (i == 1) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        failedMessage("IOException");
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        failedMessage("IOException");
                        e.printStackTrace();
                    }
                }

            }

            if (isLongConnection) {
                heartBeatTask.setKeepAlive(false);
                SocketUtil.closeStream(outputStream);
                SocketUtil.closeStream(inputStream);
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                failedMessage("IOException");
                e.printStackTrace();
            }

        } catch (IOException e) {
            failedMessage("IOException");
            e.printStackTrace();
        }
    }

    private void failedMessage(String msg){
        Message message=handler.obtainMessage(FAILED);
        message.what=FAILED;
        message.arg1=FAILED;
        message.obj=msg;
        handler.sendMessage(message);
    }
    public class MyHandler extends Handler{
        TCPRequestCallBack tcpRequestCallBack;
        MyHandler(TCPRequestCallBack callBack){
            super(Looper.getMainLooper());
            this.tcpRequestCallBack=callBack;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SUCCESS:
                    tcpRequestCallBack.onSuccess((String) msg.obj);
                    break;
                case FAILED:
                    tcpRequestCallBack.onFailed(msg.arg1, (String) msg.obj);
                    break;
                default:
                    break;
            }
        }
    }
}
