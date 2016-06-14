package com.example.csdnblog4.net;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.csdnblog4.common.ProjectApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.SocketFactory;

/**
 * 发送数据,并且启动心跳,连续发送3次数据
 * Created by orange on 16/6/8.
 */
public class RequestTask implements Runnable {
    private static final String ADDRESS = "172.16.101.148";
    private static final int PORT = 9012;
    private static final int SUCCESS=100;
    private static final int FAILED=-1;
    private boolean isLongConnection = true;
    private TCPRequestCallBack tcpRequestCallBack;
    private Handler handler;
    private SendTask sendTask;
    private ReciverTask reciverTask;
    HeartBeatTask heartBeatTask = null;
    private String uuid;
    private ExecutorService executorService;
    Socket socket = null;
    protected volatile ConcurrentLinkedQueue<String> requsetData = new ConcurrentLinkedQueue<String>();
    protected ConcurrentLinkedQueue<String> reciverDatas = new ConcurrentLinkedQueue<String>();

    public RequestTask(TCPRequestCallBack tcpRequestCallBacks) {
        this.tcpRequestCallBack=tcpRequestCallBacks;
        handler=new MyHandler(tcpRequestCallBack);
    }

    @Override
    public void run() {
        uuid= ProjectApplication.getUUID();
        try {
            failedMessage(0,"服务器连接中");
            socket = SocketFactory.getDefault().createSocket("172.16.101.148", 9012);
            sendTask=new SendTask();

            sendTask.outputStreamSend = socket.getOutputStream();
            sendTask.start();

            reciverTask=new ReciverTask();
            reciverTask.inputStreamReciver=socket.getInputStream();
            reciverTask.start();

            if (isLongConnection) {
                heartBeatTask = new HeartBeatTask(sendTask.outputStreamSend,uuid);
                executorService=Executors.newCachedThreadPool();
                executorService.execute(heartBeatTask);
            }
        } catch (IOException e) {
            failedMessage(-1,"IOException");
            e.printStackTrace();
        }
    }

    private void failedMessage(int code,String msg){
        Message message=handler.obtainMessage(FAILED);
        message.what=FAILED;
        message.arg1=code;
        message.obj=msg;
        handler.sendMessage(message);
    }

    private void successMessage(String data){
        Message message=handler.obtainMessage(SUCCESS);
        message.what=SUCCESS;
        message.obj=data;
        handler.sendMessage(message);
    }
    public void addRequest(String data){
        requsetData.add(data);
    }

    public void stop() {
        if (sendTask!=null){
            sendTask.interrupt();
            sendTask.isCancle=true;
            if(sendTask.outputStreamSend!=null){
                synchronized (sendTask.outputStreamSend){
                    sendTask.outputStreamSend=null;
                }
            }
            sendTask=null;
            toNotifyAll(requsetData);
        }

        if (reciverTask!=null){
            reciverTask.interrupt();
            reciverTask.isCancle=true;
            if (reciverTask.inputStreamReciver!=null){
                SocketUtil.closeStream(reciverTask.inputStreamReciver);
                reciverTask.inputStreamReciver=null;

            }
            reciverTask=null;
            toNotifyAll(reciverDatas);
        }

        if (executorService!=null){
            executorService.shutdown();
        }
        if ( heartBeatTask!=null){
            heartBeatTask.setKeepAlive(false);
            heartBeatTask=null;
        }
        if (socket!=null){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        clearConnection();
    }

    private void clearConnection(){
        requsetData.clear();
        reciverDatas.clear();
        isLongConnection=false;
    }

    public class ReciverTask extends Thread{

        InputStream inputStreamReciver;
        private boolean isCancle=false;
        @Override
        public void run() {
            super.run();
            while (!isCancle){
                if (socket.isClosed()||!socket.isConnected()){
                    isCancle=true;
                    if (heartBeatTask!=null)
                    heartBeatTask.setKeepAlive(false);
                    RequestTask.this.stop();
                    return;
                }
                String reciverData = SocketUtil.readFromStream(inputStreamReciver);
                if (reciverData!=null) {
                    successMessage(reciverData);
                    reciverDatas.offer(reciverData);
                    toNotifyAll(reciverDatas);
                    toNotifyAll(requsetData);
                }
            }
            SocketUtil.closeStream(inputStreamReciver);
        }
    }


    public class SendTask extends Thread{
        private  boolean isCancle=false;
        private OutputStream outputStreamSend;
        @Override
        public void run() {
            super.run();
            while (!isCancle){
                try {
                    String dataContent=requsetData.poll();
                    if (dataContent==null){
                        toWait(requsetData);
                    }else {
                        if (socket.isClosed()){
                            isCancle=true;
                            if (heartBeatTask!=null)
                            heartBeatTask.setKeepAlive(false);
                            RequestTask.this.stop();
                            break;
                        }
                        SocketUtil.write2Stream(dataContent,uuid, outputStreamSend);
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            SocketUtil.closeStream(outputStreamSend);
        }
    }

    private void toWait(Object o){
        synchronized (o){
            try {
                o.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void toNotifyAll(Object o) {
        synchronized (o) {
            o.notifyAll();
        }
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
