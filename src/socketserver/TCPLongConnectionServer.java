package socketserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by orange on 16/6/8.
 */
public class TCPLongConnectionServer {
    public TCPLongConnectionServer(){

    }
    public static void main(String[] args){
        ServerSocket serverSocket=null;
        ExecutorService executorService=Executors.newCachedThreadPool();

        try {
            serverSocket=new ServerSocket(9012);
            for (int i=0;i<10;i++){
                Socket socket=serverSocket.accept();
                
                executorService.execute(new TCPLongConnectServerHandlerData(socket,i));
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (serverSocket!=null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
