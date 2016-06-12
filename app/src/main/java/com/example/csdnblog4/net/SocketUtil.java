package com.example.csdnblog4.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by orange on 16/6/8.
 */
public class SocketUtil {

    /**
     * java.net.SocketException: Connection reset
     引起这个异常的原因有两个：
     一、客户端和服务器端如果一端的Socket被关闭，另一端仍发送数据，发送的第一个数据包引发该异常；
     二、客户端和服务器端一端退出，但退出时并未关闭该连接，另一端如果在从连接中读数据则抛出该异常。
     简单来说就是在连接断开后的读和写操作引起的。
     * 注意,并没有关闭输入输出流
     * @param inputStream
     * @return
     */
    public static String readFromStream(InputStream inputStream) {
        StringBuilder result = new StringBuilder("");
        BufferedInputStream bufferedInputStream=null;
        BufferedInputStream contentBufferedInputStream=null;
        byte[] header=new byte[4];

        try {
            int len=0;
            bufferedInputStream=new BufferedInputStream(inputStream);
//            contentBufferedInputStream=new BufferedInputStream(inputStream);

            String piece = "";
            int te=0;

            while(te<header.length&&te!=-1) {
                te += bufferedInputStream.read(header, te, header.length);
            }

            int lenght=byteArrayToInt(header);
//            ByteBuffer buffer = ByteBuffer.allocate(lenght);
            len=0;
            byte[] content=new byte[lenght];
            while (len<lenght){
                len+=bufferedInputStream.read(content,len,content.length);
            }
            piece=new String(content,0,lenght,"utf-8");
            result.append(piece);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        return result.toString();
    }

    public static void write2Stream(String data,OutputStream outputStream) throws UnsupportedEncodingException {
        BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(outputStream);
        byte[] buffData= data.getBytes("utf-8");
        byte[] header= int2ByteArrays(buffData.length);
        try {
            bufferedOutputStream.write(header);
            bufferedOutputStream.flush();
            bufferedOutputStream.write(buffData,0,buffData.length);
            bufferedOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }
    }


    public static void closeStream(InputStream is){
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeStream(OutputStream os){
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] int2ByteArrays(int i) {
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }


    //字节数组转int
    public static int byteArrayToInt(byte[] b) {
        int intValue=0;
        for(int i=0;i<b.length;i++){
            intValue +=(b[i] & 0xFF)<<(8*(3-i));
        }
        return intValue;
    }
}
