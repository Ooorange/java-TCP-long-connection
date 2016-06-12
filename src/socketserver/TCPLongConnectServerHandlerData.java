package socketserver;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by orange on 16/6/8.
 */
public class TCPLongConnectServerHandlerData implements Runnable {

    private Socket socket;
    private int requestId;
    private static final int TIMEOUT=5000;
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
                	if(socket.isClosed()||socket.isInputShutdown()||socket.isOutputShutdown()){
                		return;
                	}
                    String clientData=SocketUtil.readFromStream(socket.getInputStream());
                    System.out.println("客户端数据:"+clientData);

                    if (clientData.contains("heartBeat")){
                        SocketUtil.write2Stream("hello,client,this is server,id:"+requestId,socket);
                    }else {
                        SocketUtil.write2Stream("Server has reciver  has reciver  data"
                        		+ "",socket);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
        	System.out.println("time is out: "+requestId);
            e.printStackTrace();
        }
    }
}
