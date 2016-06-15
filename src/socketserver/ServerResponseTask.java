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
    private Socket targetClient;
    private ConcurrentLinkedQueue<Procotol> reciverData= new ConcurrentLinkedQueue<Procotol>();
    
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
            	Procotol procotol=reciverData.poll();
            	if(procotol==null){
            		toWaitAll(reciverData);
				} else {
					SocketUtil.write2Stream(procotol.getContent(), outputStream);// TODO
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
            	Procotol clientData=SocketUtil.readFromStream(inputStream);
                if(clientData==null){
                	isCancle=true;
                	return;
                }
                reciverData.offer(clientData);
                toNotifyAll(reciverData);
                if(clientData.getContent()!=null&&!clientData.getContent().isEmpty()){
                	if(clientData.getUuid()!=null&&!clientData.getUuid().isEmpty()){
//                		System.out.println("用户ID:"+clientData.getUuid()+"用户端信息:"+clientData.getContent());
                		if(tBack!=null){
                			tBack.connectSuccess(clientData.getUuid(),clientData.getContent());
                		}
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
    
    public void addWriteTask(Procotol procotol,Socket targetClient ){
    	reciverData.offer(procotol);
    	this.targetClient=targetClient;
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
