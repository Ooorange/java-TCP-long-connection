package socketserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

/**
 * Created by orange on 16/6/8.
 */
public class SocketUtil {

    /**
     * 注意,并没有关闭输入输出流
     * @param inputStream
     * @return
     */
    public static String readFromStream(InputStream inputStream) {
        StringBuilder result = new StringBuilder("");
        BufferedInputStream bufferedReader = null;
        try {
            bufferedReader = new BufferedInputStream(inputStream);
            byte[] header=new byte[4];
            byte[] contentData=new byte[1024];
            int length=bufferedReader.read(header, 0, header.length);
            int readedLen=0;
            while(readedLen<length){
            	int temp=bufferedReader.read(contentData,4,contentData.length-4);
            	readedLen=temp+readedLen;
            	String readChar=new String(contentData, 0, contentData.length);
            	result.append(readChar);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
        }

        return result.toString();
    }
    /**
     * 向客户端写数据
     * @param data
     * @param socket
     * @throws UnsupportedEncodingException 
     */
    public static void write2Stream(String data,Socket socket) throws UnsupportedEncodingException{
    	if(socket.isClosed()){
    		System.out.println("客户端连接断开___");
    		return;
    	}
        BufferedOutputStream outputStream=null;
        
        	
		try {
			outputStream =new BufferedOutputStream(socket.getOutputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
        byte[] buffData= data.getBytes("utf-8");//28length
        byte[] header=int2ByteArrays(buffData.length);//headerLen:4 
        try {
        	outputStream.write(header);
        	outputStream.flush();
        	outputStream.write(buffData,0,buffData.length);
        	outputStream.flush();
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
