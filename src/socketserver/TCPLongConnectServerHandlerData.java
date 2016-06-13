package socketserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 在复杂的业务中，比如说车钥匙，会有接收消息队列以及发送的消息队列，作为两个独立的线程去操作，
 * 对于消息的分发还需根据请求时的Seq和接收到的Seq对应，保证数据正确分发(通过分发的线程，对注册监听的接口进行回调)
 * 
 * Created by orange on 16/6/8.
 */
public class TCPLongConnectServerHandlerData implements Runnable {

    private Socket socket;
    private int requestId;
    private static final int TIMEOUT=5000;
    private OutputStream outputStream=null;
    private InputStream inputStream=null;
    public TCPLongConnectServerHandlerData(Socket socket,int requesetId){
        this.socket=socket;
        this.requestId=requesetId;
    }
    @Override
    public void run() {
        try {
            socket.setSoTimeout(TIMEOUT);

            while (true){
                try {
                	if(socket.isClosed()){
                		return;
                	}
                	inputStream=socket.getInputStream();
                	if(inputStream==null){
                		System.out.println("输入流为空－－－－－－－－－－－－－－－－－－－－");
                	}
                    String clientData=SocketUtil.readFromStream(inputStream);
                    if(clientData.isEmpty()){
                    	SocketUtil.closeStream(outputStream);
                    	return;
                    }
                    System.out.println("客户端数据:"+clientData);
                    outputStream=socket.getOutputStream();
                    
                    if(outputStream==null||inputStream==null){
                    	System.out.println("输入出流为空－－－－－－－－－－－－－－－－－－－－");
                    }
                    if (clientData.contains("heartBeat")){
                        SocketUtil.write2Stream("收到你的心跳了...,id:"+requestId,outputStream);
                    }else {
                        SocketUtil.write2Stream("hello my friend"
                        		+ "",outputStream);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                	System.out.println("断开异常");
                    e.printStackTrace();
                }
            }
            
        } catch (Exception e) {
        	System.out.println("time is out: "+requestId);
        	if(outputStream!=null){
        		SocketUtil.closeStream(outputStream);
        	}
        	if(inputStream!=null){
            	System.out.println("closeInputStream");
            	SocketUtil.closeStream(inputStream);
            }
        	
        }
        if(outputStream!=null){
    		SocketUtil.closeStream(outputStream);
    	}
    	if(inputStream!=null){
        	System.out.println("closeInputStream");
        	SocketUtil.closeStream(inputStream);
        }
        
    }
}
