package socketserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import socketserver.TCPLongConnectServerHandlerData.TCPResultCallBack;

/**
 * Created by orange on 16/6/14.
 */
public class ServerResponseTask implements Runnable {
    private ServerReadTask serverReadTask;
    private ServerWriteTask serverWriteTask;
    private Socket socket;//新加入的客户端
    private TCPResultCallBack tBack;
    private volatile ConcurrentLinkedQueue<Protocol> reciverData= new ConcurrentLinkedQueue<Protocol>();
    
    public ServerResponseTask(Socket socket,TCPResultCallBack tBack){
        this.socket=socket;
        this.tBack=tBack;
    }


    @Override
    public void run() {
        try {
            serverReadTask = new ServerReadTask();
            serverWriteTask = new ServerWriteTask();


            serverWriteTask.outputStream = new DataOutputStream(socket.getOutputStream());
            serverReadTask.inputStream = new DataInputStream(socket.getInputStream());


            serverReadTask.start();
            serverWriteTask.start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class ServerWriteTask extends Thread {

        DataOutputStream outputStream;
        boolean isCancle;
        @Override
        public void run() {
            while (!isCancle){
            	Protocol procotol=reciverData.poll();
            	if(procotol==null){
            		toWaitAll(reciverData);
				} else {	
					SocketUtil.write2Stream(procotol, outputStream);// TODO
				}
            }
            SocketUtil.closeStream(outputStream);
        }
    }

    public class ServerReadTask extends Thread {

        DataInputStream inputStream;
        boolean isCancle;
        @Override
        public void run() {
            while (!isCancle){
            	Protocol clientData=SocketUtil.readFromStream(inputStream);
                if(clientData!=null){
                	reciverData.offer(clientData);
                    toNotifyAll(reciverData);
                    if(tBack!=null&&clientData!=null){
            			tBack.connectSuccess(clientData);
            		}
                }
            }
            SocketUtil.closeStream(inputStream);
        }
    }

    public void toWaitAll(Object o){
    	synchronized (o) {
    		try {
				o.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }
    
    public synchronized void addWriteTask(Protocol procotol,Socket targetClient ){
    	reciverData.add(procotol);
    	try {
			serverWriteTask.outputStream=new DataOutputStream(targetClient.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void toNotifyAll(Object obj){
    	synchronized (obj) {
    		obj.notifyAll();
		}
    }
    
    public void stop(){
        if (serverReadTask!=null){
            serverReadTask.isCancle=true;
            serverReadTask.interrupt();
            serverReadTask=null;
        }

        if (serverWriteTask!=null) {
            serverWriteTask.isCancle = true;
            serverWriteTask.interrupt();
            serverWriteTask=null;
        }
    }

}
