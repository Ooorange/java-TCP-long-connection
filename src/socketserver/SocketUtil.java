package socketserver;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.User;
import procotol.BasicProtocol;
import procotol.ChatMsgProtocol;
import procotol.HeartBeatProcotol;
import procotol.RegisterProtocol;
import procotol.UserFriendReuqetProtocol;

/**
 * Created by orange on 16/6/8.
 */
public class SocketUtil {
    public static final int CLIENT_UUID=32;
    public static final int MSGTARGETCLIENT_UUID_LEN=32;
    public static final int CLIENT_VERSION_LEN=4;
    
    private static Map<String,String> msgImp=new HashMap<String,String>();
    
    static {
        msgImp.put(ChatMsgProtocol.CHATMEGCOMMEND,"procotol.ChatMsgProtocol");
        msgImp.put(HeartBeatProcotol.HEART_COMMEND,"procotol.HeartBeatProcotol");
        msgImp.put(UserFriendReuqetProtocol.FRIENDREQUEST,"procotol.UserFriendReuqetProtocol");
        msgImp.put(RegisterProtocol.REGISTERCOMMEND, "procotol.RegisterProtocol");
    }
    
    /**
     * 并没有关闭输入输出流
     *
     * @param inputStream
     * @return
     */
    public static  BasicProtocol readFromStream(InputStream inputStream) {
    	BasicProtocol basicProcotol=null;
        DataInputStream dis = null;
        int pos=CLIENT_VERSION_LEN;

        try {
            dis = new DataInputStream(inputStream);
            byte[] header = new byte[4];

            int length = dis.read(header, 0,pos);
            length=byteArrayToInt(header);

            byte[] contentData = new byte[length];


            if(length==-1){
                return null;
            }
            int readedLen = 0;
            int temp=0;
            while (readedLen < length) {
                if((temp = dis.read(contentData,readedLen, length-readedLen))>0)
                    readedLen = temp + readedLen;
            }
            basicProcotol=parseContentMsg(contentData);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return basicProcotol;
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
    

    /**
     *
     * 服务端向客户端写数据
     *
     * @param protocol
     * @throws UnsupportedEncodingException
     * 
     */
    public static  void write2Stream(BasicProtocol protocol, DataOutputStream outputStream) {
    	
        byte[] buffData = protocol.getContentData();
        // 28length
        byte[] header = int2ByteArrays(buffData.length);// headerLen:4
        try {
            outputStream.write(header);
            outputStream.flush();
            outputStream.write(buffData);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 协议包体,服务端只要向目标客户端写入信息,以及对应的versionCode就行
     * 
     * 谁发送的信息:msgSenderUUID
     * 发送消息人的版本号:clientVersion
     * 发送的消息内容: message
     * @param protocol
     * @return
     */
    public static byte[] getContentData(ChatMsgProtocol protocol){
        ByteArrayOutputStream baos=new ByteArrayOutputStream(CLIENT_UUID+MSGTARGETCLIENT_UUID_LEN+CLIENT_VERSION_LEN);

        baos.write(int2ByteArrays(protocol.getClientVersion()),0,CLIENT_VERSION_LEN);
        baos.write(protocol.getSelfUUid().getBytes(), 0, CLIENT_UUID);
        byte[] msg=protocol.getMessage().getBytes();
        baos.write(msg, 0, msg.length);

        return baos.toByteArray();
    }


    public static void closeStream(InputStream is) {
        try {
            if(is!=null)
                is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeStream(OutputStream os) {
        try {
            if(os!=null)
                os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] int2ByteArrays(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }

    // 字节数组转int
    public static int byteArrayToInt(byte[] b) {
        int intValue = 0;
        for (int i = 0; i < b.length; i++) {
            intValue += (b[i] & 0xFF) << (8 * (3 - i));
        }
        return intValue;
    }

    //indexOutofException 记得当客户端断开连接后关闭其相关管道以及线程就好
    public static int bytesToInt(byte b[], int startPos){
        ByteBuffer buffer = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE);

        buffer.put(b, startPos, 4);
        buffer.flip();//need flip
        return buffer.getInt();
    }
}
