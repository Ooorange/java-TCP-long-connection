package com.orange.blog.net;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * 手上需要申请网络访问权限,手机默认inetAddress.getHostName():localHost
 * InetAddress
 * Created by orange on 16/6/1.
 */
public class HostInetTest {
    public HostInetTest(){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    InetAddress inetAddress= null;
                    try {
                        inetAddress = InetAddress.getLocalHost();
                        Log.d("ooo计算机名字:",""+inetAddress.getHostName());
                        Log.d("oooIP地址:",""+inetAddress.getHostAddress());
                        byte[] ipAddress=inetAddress.getAddress();
                        Log.d("oooIP字节地址:", "" + Arrays.toString(ipAddress));

                        Log.d("oooo---------","-----------");
                        InetAddress inetAddress1=InetAddress.getByName("127.0.0.1");
                        Log.d("ooo计算机名字:",""+inetAddress1.getHostName());
                        Log.d("oooIP地址:",""+inetAddress1.getHostAddress());
                        Log.d("ooo---------","-----------");
                        URLTest urlTest=new URLTest();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

    }

    class URLTest{
        public URLTest(){
            try {
                /**
                 *如果未指定端口号,默认http是80,此时getPort()获取的端口号为-1
                 oooimAuthority:: www.imooc.com
                 oooimFile:: /index.html?username=there_there
                 oooimHost:: www.imooc.com
                 oooimPath:: /index.html
                 oooimProtocol:: http
                 oooimQuery:: username=there_there
                 oooimUserInfo:: null
                 oooimPort:-1
                 */
                URL im=new URL("http://www.imooc.com");
                URL indexUrl=new URL(im,"/index.html?username=there_there#test");

                Log.d("oooimAuthority:", "" + indexUrl.getAuthority());
                Log.d("oooimFile:", "" +indexUrl.getFile());
                Log.d("oooimHost:", "" +indexUrl.getHost());
                Log.d("oooimPath:", "" +indexUrl.getPath());
                Log.d("oooimProtocol:", "" + indexUrl.getProtocol());
                Log.d("oooimQuery:", "" + indexUrl.getQuery());
                Log.d("oooimUserInfo:", "" + indexUrl.getUserInfo());
                Log.d("oooimPort:", "" + indexUrl.getPort());
                Log.d("ooo---------","-----------");
                new CheyaoshiUrlTest();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

    }
    public class CheyaoshiUrlTest{
        public CheyaoshiUrlTest(){
            try {
                URL url=new URL("http://www.cheyaoshi.com");
                //将资源文件转换成字节输入流
                InputStream inputStream=url.openConnection().getInputStream();
                //将字节输入流转换为字符输入流
                InputStreamReader inputStreamReader=new InputStreamReader(inputStream,"utf-8");
                //为字符输入流建立缓冲区
                BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                String data=bufferedReader.readLine();
                while (data!=null){
                    Log.d("data",data);
                    data=bufferedReader.readLine();
                }
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
