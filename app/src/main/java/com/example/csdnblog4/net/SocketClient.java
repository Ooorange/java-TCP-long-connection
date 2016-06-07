package com.example.csdnblog4.net;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.net.SocketFactory;

/**
 * 实现客户端的热加载以及修复
 * 通过向classLoader插入一个dex文件(DexClassLoader会优先加载先找到的类,
 * 所以通过此特性可以实现宿主AP看的热修复功能),
 * serverSocket的client端的实现
 * Created by orange on 16/6/1.
 */
public class SocketClient {
    public String reciverData;
    private SuccessCallBack mSuccessCallBack;
    private Handler handler;
    public SocketClient(final SuccessCallBack successCallBack){
        this.mSuccessCallBack = successCallBack;
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (mSuccessCallBack !=null){
                    reciverData=msg.getData().getString("data");
                    mSuccessCallBack.reciveDataSuccess(reciverData);
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                getConnect();
            }
        }).start();

    }

    public void getConnect(){
        try {
            //建立链接
            //此处IP地址需要用本机的实际地址,不能用getHostAddress()获得的地址(获取到的是本地服务器127.0.01),
            // 10.0.2.2模拟器使用的特定地址,是你电脑的别名
            Socket socket= SocketFactory.getDefault().createSocket("172.16.101.148",9000);
            //获取输出流
            OutputStream outputStream=socket.getOutputStream();
            PrintWriter printWriter=new PrintWriter(outputStream);
            printWriter.write("今天是个好日子,做什么都顺心");
            printWriter.flush();
            socket.shutdownOutput();
            reciverData=getReciverData(socket);
            if (mSuccessCallBack !=null){
                Message message=Message.obtain();
                Bundle bundle=new Bundle();
                bundle.putString("data",reciverData);
                message.setData(bundle);
                handler.sendMessage(message);
            }
            printWriter.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //从服务器拿到一个java文件
    public String getReciverData(Socket socket){
        try {
            InputStream inputStream=socket.getInputStream();
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream,"utf-8");
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            String data=null;
            StringBuilder stringBuilder=new StringBuilder();
            while ((data=bufferedReader.readLine())!=null){
                stringBuilder.append(data);
            }
//            socket.shutdownInput();
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            Log.d("orangeReciverData",stringBuilder.toString());
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "happened a exception";
        }
    }

    //将dex文件写入android包下
    public void writeFile2Project(){
    }

    public interface SuccessCallBack{
        void reciveDataSuccess(String message);
    }
}
