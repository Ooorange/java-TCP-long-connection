package com.example.csdnblog4.net;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.csdnblog4.common.ProjectApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.SocketFactory;

/**
 *
 *
 * 因为写消息用的是一个死循环,当没有数据的时候就不需要进行写操作了,直接wait,当有消息来时就notify
 *
 * Created by orange on 16/6/8.
 *
 */
public class RequestTask implements Runnable {
    private static final String ADDRESS = "172.16.101.148";
    private static final int PORT = 9013;
    private static final int SUCCESS = 100;
    private static final int FAILED = -1;
    private boolean isLongConnection = true;
    private TCPRequestCallBack tcpRequestCallBack;
    private Handler handler;
    private SendTask sendTask;
    private ReciverTask reciverTask;
    HeartBeatTask heartBeatTask = null;
    private String uuid;
    private ExecutorService executorService;
    Socket socket = null;
    protected volatile ConcurrentLinkedQueue<Protocol> sendData = new ConcurrentLinkedQueue<Protocol>();
    protected volatile ConcurrentLinkedQueue<Protocol> reciveDatas = new ConcurrentLinkedQueue<Protocol>();

    public RequestTask(TCPRequestCallBack tcpRequestCallBacks) {
        this.tcpRequestCallBack = tcpRequestCallBacks;
        handler = new MyHandler(tcpRequestCallBack);
    }

    @Override
    public void run() {
        uuid = ProjectApplication.getUUID();
        try {
            failedMessage(0,"服务器连接中");
            try {
                socket = SocketFactory.getDefault().createSocket(ADDRESS, PORT);
            }catch (ConnectException e){
                failedMessage(-1,"服务区器连接异常,请检查网络");
                return;
            }
            sendTask=new SendTask();

            sendTask.outputStreamSend = socket.getOutputStream();
            sendTask.start();

            reciverTask = new ReciverTask();
            reciverTask.inputStreamReciver = socket.getInputStream();
            reciverTask.start();

            if (isLongConnection) {
                heartBeatTask = new HeartBeatTask(sendTask.outputStreamSend, uuid);
                executorService = Executors.newCachedThreadPool();
                executorService.execute(heartBeatTask);
            }
        } catch (IOException e) {
            failedMessage(-1, "IOException");
            e.printStackTrace();
        }
    }

    public void stop() {
        toNotifyAll(sendData);

        if (sendTask != null) {
            sendTask.interrupt();
            sendTask.isCancle = true;
            if (sendTask.outputStreamSend != null) {
                synchronized (sendTask.outputStreamSend) {
                    sendTask.outputStreamSend = null;
                }
            }
            sendTask = null;
        }

        if (reciverTask != null) {
            reciverTask.interrupt();
            reciverTask.isCancle = true;
            if (reciverTask.inputStreamReciver != null) {
                SocketUtil.closeStream(reciverTask.inputStreamReciver);
                reciverTask.inputStreamReciver = null;
            }
            reciverTask = null;
        }

        if (executorService != null) {
            executorService.shutdown();
        }
        if (heartBeatTask != null) {
            heartBeatTask.setKeepAlive(false);
            heartBeatTask = null;
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        clearConnection();
    }

    private void clearConnection() {
        sendData.clear();
        reciveDatas.clear();
        isLongConnection = false;
    }


    /**
     * read...
     */
    public class ReciverTask extends Thread {

        InputStream inputStreamReciver;
        private boolean isCancle = false;

        @Override
        public void run() {
            while (!isCancle) {
                if (socket.isClosed() || !socket.isConnected()) {
                    isCancle = true;
                    RequestTask.this.stop();
                    break;
                }
                try {
                    Protocol reciverData = SocketUtil.readFromStream(inputStreamReciver);
                    if (reciverData != null) {
                        successMessage(reciverData);
                        reciveDatas.offer(reciverData);
                    }
                }catch (SocketExceptions e){
                    isCancle=true;
                    heartBeatTask.setKeepAlive(false);
                    RequestTask.this.stop();
                }
            }
            SocketUtil.closeStream(inputStreamReciver);
        }
    }


    //write 当没有发送的数据时让发送线程进行等待
    public class SendTask extends Thread {
        private boolean isCancle = false;
        private OutputStream outputStreamSend;

        @Override
        public void run() {
            while (!isCancle) {
                printMessage();
                Protocol dataContent = sendData.poll();
                if (dataContent == null) {
                    toWait(sendData);
                } else {
                    if (socket.isClosed()) {
                        isCancle = true;
                        RequestTask.this.stop();
                        break;
                    }else {
                        if (outputStreamSend!=null) {
                            synchronized (outputStreamSend) {
                                SocketUtil.writeContent2Stream(dataContent, outputStreamSend);
                            }
                        }
                    }
                }
            }
            SocketUtil.closeStream(outputStreamSend);
        }
    }


    private void toWait(Object o) {
        synchronized (o) {
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
    public void printMessage(){
    }

    private void failedMessage(int code, String msg) {
        Message message = handler.obtainMessage(FAILED);
        message.what = FAILED;
        message.arg1 = code;
        message.obj = msg;
        handler.sendMessage(message);
    }

    private void successMessage(Protocol protocol) {
        Message message = handler.obtainMessage(SUCCESS);
        message.what = SUCCESS;
        message.obj = protocol.getMessage();
        handler.sendMessage(message);
    }

    public void addRequest(Protocol data) {
        sendData.add(data);
        toNotifyAll(sendData);
        printMessage();
    }


    public class MyHandler extends Handler {
        TCPRequestCallBack tcpRequestCallBack;

        MyHandler(TCPRequestCallBack callBack) {
            super(Looper.getMainLooper());
            this.tcpRequestCallBack = callBack;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
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
