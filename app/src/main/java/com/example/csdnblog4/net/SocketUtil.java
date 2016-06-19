package com.example.csdnblog4.net;

import android.util.Log;

import com.example.csdnblog4.common.ProjectApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Created by orange on 16/6/8.
 */
public class SocketUtil {

    public static final int CLIENT_UUID=32;
    public static final int MSGTARGETCLIENT_UUID_LEN=32;
    public static final int CLIENT_VERSION_LEN=4;


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
    public static Protocol readFromStream(InputStream inputStream) throws SocketExceptions{
        Protocol protocol=new Protocol();
        BufferedInputStream bis;
        int pos=CLIENT_VERSION_LEN;
        byte[] header=new byte[CLIENT_VERSION_LEN];
        try {

            bis=new BufferedInputStream(inputStream);

            int len=0;
            int temp=0;

            while(len<header.length) {
                if((temp = bis.read(header, len, header.length-len))>0){
                    len+=temp;
                }else {
                    throw new SocketExceptions("serverClose");
                }
            }

            int length=byteArrayToInt(header);
            len=0;
            temp=0;

            byte[] content=new byte[length];
            while (len<length){
                if((temp=bis.read(content,len,content.length-len))>0){
                    len+=temp;
                }else{
                    throw new SocketExceptions("serverClose");
                }
            }

            protocol.setClientVersion(bytes2Int(content,0));
            protocol.setMsgTargetUUID(new String(content, pos, MSGTARGETCLIENT_UUID_LEN));
            pos+=MSGTARGETCLIENT_UUID_LEN;
            protocol.setMessage(new String(content,pos,content.length-pos));

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return protocol;
    }

    public static  void writeContent2Stream(Protocol protocol, OutputStream outputStream)  {
        /**---------------先写死------------------*/
        protocol.setMsgTargetUUID("9b0a60c7df57485ba2be6b81dac00d5d");//小米9b0a60c7df57485ba2be6b81dac00d5d
        protocol.setClientVersion(ProjectApplication.versionID);
        protocol.setSelfUUid(ProjectApplication.getUUID());
        /**--------------------------------------*/

        BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(outputStream);

        byte[] buffData= getContentData(protocol);

        byte[] header= int2ByteArrays(buffData.length);
        try {
            bufferedOutputStream.write(header);
            bufferedOutputStream.write(buffData);
            bufferedOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("orangeWr",protocol.toString());
    }

    //协议包体
    public static byte[] getContentData(Protocol protocol){
        ByteArrayOutputStream baos=new ByteArrayOutputStream(CLIENT_UUID+MSGTARGETCLIENT_UUID_LEN+CLIENT_VERSION_LEN);

        baos.write(int2ByteArrays(protocol.getClientVersion()), 0, CLIENT_VERSION_LEN);
        baos.write(protocol.getSelfUUid().getBytes(), 0, CLIENT_UUID);
        baos.write(protocol.getMsgTargetUUID().getBytes(), 0, MSGTARGETCLIENT_UUID_LEN);
        byte[] msg=protocol.getMessage().getBytes();
        baos.write(msg, 0, msg.length);

        return baos.toByteArray();
    }

    public static void closeStream(InputStream is){
        try {
            if (is!=null)
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeStream(OutputStream os){
        try {
            if (os!=null)
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

    public static int byteArrayToInt(byte[] b) {
        int intValue=0;
        for(int i=0;i<b.length;i++){
            intValue +=(b[i] & 0xFF)<<(8*(3-i));
        }
        return intValue;
    }

    public static int bytes2Int(byte[]b ,int offset){
        ByteBuffer byteBuffer=ByteBuffer.allocate(Integer.SIZE/Byte.SIZE);
        byteBuffer.put(b,offset,4);
        byteBuffer.flip();
        return byteBuffer.getInt();
    }
}
