package com.orange.blog.net;

import com.orange.blog.net.protocol.BasicProtocol;
import com.orange.blog.net.protocol.ChatMsgProcotol;
import com.orange.blog.net.protocol.HeartBeatProtocol;
import com.orange.blog.net.protocol.RegisterProcotol;
import com.orange.blog.net.protocol.ResponseProcotol;
import com.orange.blog.net.protocol.UserFriendReuqestProcotol;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * java.net.SocketException: Connection reset
 引起这个异常的原因有两个：
 一、客户端和服务器端如果一端的Socket被关闭，另一端仍发送数据，发送的第一个数据包引发该异常；
 二、客户端和服务器端一端退出，但退出时并未关闭该连接，另一端如果在从连接中读数据则抛出该异常。
 简单来说就是在连接断开后的读和写操作引起的。
 * 注意,并没有关闭输入输出流
 *
 *Created by orange on 16/6/8.
 */
public class SocketUtil {

    public static final int CLIENT_UUID=32;
    public static final int MSGTARGETCLIENT_UUID_LEN=32;
    public static final int CLIENT_VERSION_LEN=4;

    private static Map<String,String> msgImp=new HashMap<String,String>();
    static {
        msgImp.put(HeartBeatProtocol.HEART_COMMEND,"com.orange.blog.net.protocol.HeartBeatProtocol");//0000
        msgImp.put(ChatMsgProcotol.CHATMEGCOMMEND,"com.orange.blog.net.protocol.ChatMsgProcotol");//0001
        msgImp.put(ResponseProcotol.RESPONSECOMMEND,"com.orange.blog.net.protocol.ResponseProcotol");//0002
        msgImp.put(UserFriendReuqestProcotol.USERFRIENDREQUESTCOMMEND,"com.orange.blog.net.protocol.UserFriendReuqestProcotol");//0003
        msgImp.put(RegisterProcotol.REGISTERCOMMEND,"com.orange.blog.net.protocol.RegisterProcotol");//0004
    }

    public static BasicProtocol readFromStream(InputStream inputStream) throws SocketExceptions{
        BasicProtocol protocol;
        BufferedInputStream bis;
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

            int length=byteArrayToInt(header);//包体长度

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
            protocol=parseContentMsg(content);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return protocol;
    }


    public static BasicProtocol parseContentMsg(byte[] data){
        String commendType=BasicProtocol.paraseCommend(data);

        String className=msgImp.get(commendType);
        BasicProtocol basicProtocol= null;
        try {
            basicProtocol = (BasicProtocol) Class.forName(className).newInstance();
            basicProtocol.parseBinary(data);
        } catch (Exception e) {
            basicProtocol=null;
            e.printStackTrace();
        }
        return basicProtocol;
    }


    public static  void writeContent2Stream(BasicProtocol protocol, OutputStream outputStream)  {
        /**-先写死----6.0  9b0a60c7df57485ba2be6b81dac00d5d--------------5.0:4d8e938015b34049a21fad31a3e29820*/

        BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(outputStream);

        byte[] buffData= protocol.getContentData();

        byte[] header= int2ByteArrays(buffData.length);
        try {
            bufferedOutputStream.write(header);
            bufferedOutputStream.write(buffData);
            bufferedOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //协议包体
    public static byte[] getContentData(ChatMsgProcotol protocol){
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
