package com.example.csdnblog4.net;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * 1:定义服务器地址,端口号,以及发送的数据
 * 2:创建数据报,
 * 3:创建DatagramSocket对象
 * 4:发送数据
 *
 * Created by orange on 16/6/3.
 */
public class UDPDataInteractor {
    private DatagramPacket datagramPacket;
    private DatagramSocket datagramSocket;
    private String reciverData;
    private SuccessCallBack successCallBack;
    private InetAddress inetAddress;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (successCallBack!=null){

                successCallBack.reciveUDPDataSuccess(msg.getData().getString("data"));
            }
        }
    };
    public UDPDataInteractor(SuccessCallBack successCallBackk){
        this.successCallBack=successCallBackk;
        initClient();
    }

    public void initClient(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    inetAddress=InetAddress.getByName("172.16.101.148");
                    String sendData="我是客户端发送的数据";
                    byte[] wriBuff=sendData.getBytes();
                    datagramPacket=new DatagramPacket(wriBuff,wriBuff.length,inetAddress,9001);
                    datagramSocket=new DatagramSocket();
                    datagramSocket.send(datagramPacket);
                    //从服务端获得数据

                    byte[] bufferRec=new byte[1024];
                    datagramPacket=new DatagramPacket(bufferRec,bufferRec.length);
                    datagramSocket.receive(datagramPacket);
                    reciverData=new String(bufferRec,0,datagramPacket.getLength());
                    Bundle bundle=new Bundle();
                    Message message=Message.obtain();
                    bundle.putString("data",reciverData);
                    message.setData(bundle);
                    Log.d("orangeDat",reciverData);
                    handler.sendMessage(message);
                    datagramSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface SuccessCallBack{
        void reciveUDPDataSuccess(String message);
    }
}
