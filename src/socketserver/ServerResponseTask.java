package socketserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;import javax.crypto.Cipher;

import bean.User;
import chatDBInit.DBConnect;
import procotol.BasicProtocol;
import procotol.ChatMsgProtocol;
import procotol.HeartBeatProcotol;
import procotol.UserFriendReuqetProtocol;
import socketserver.TCPLongConnectServerHandlerData.TCPResultCallBack;

/**
 * Created by orange on 16/6/14.
 */
public class ServerResponseTask implements Runnable {
    private ServerReadTask serverReadTask;
    private ServerWriteTask serverWriteTask;
    private Socket socket;//新加入的客户端
    private TCPResultCallBack tBack;
    private volatile ConcurrentLinkedQueue<BasicProtocol> reciverData= new ConcurrentLinkedQueue<BasicProtocol>();
    private static ConcurrentHashMap<String, Socket> onLineClient=new ConcurrentHashMap<String, Socket>();
    DBConnect db;
    public ServerResponseTask(Socket socket,TCPResultCallBack tBack,DBConnect db){
        this.socket=socket;
        this.tBack=tBack;
        this.db=db;
    }


    @Override
    public void run() {
        try {
            serverReadTask = new ServerReadTask();
            serverWriteTask = new ServerWriteTask();


            serverWriteTask.outputStream = new DataOutputStream(socket.getOutputStream());//默认初始化发给自己 
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
            	BasicProtocol procotol=reciverData.poll();
            	if(procotol==null){
            		toWaitAll(reciverData);
				} else {	
					if(outputStream!=null){
						if(procotol instanceof ChatMsgProtocol){
							SocketUtil.write2Stream((ChatMsgProtocol) procotol, outputStream);
						}else{
							if(procotol instanceof UserFriendReuqetProtocol){
								List<User> users=db.getFriends(((UserFriendReuqetProtocol) procotol).getRequestClientUUID());
								SocketUtil.sendFriendList(users, (UserFriendReuqetProtocol) procotol, outputStream);
							}
						}
					}
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
            	BasicProtocol clientData=SocketUtil.readFromStream(inputStream);
                if(clientData!=null){
                	Socket targetClient =null;
                	
                	if(clientData instanceof ChatMsgProtocol){
                		onLineClient.put(((ChatMsgProtocol)clientData).getSelfUUid(), socket);
                		targetClient= getConnectClient(((ChatMsgProtocol)clientData).getMsgTargetUUID());
                		System.out.println("用户聊天请求"+((ChatMsgProtocol)clientData).toString());
                		if (targetClient!=null) {// 对方用户在线
                    		reciverData.offer(clientData);
                    		tBack.targetIsOnline(((ChatMsgProtocol)clientData).getMsgTargetUUID());
    					} else {				//对方用户不在线
    						if(tBack!=null){
    	            			tBack.connectSuccess((ChatMsgProtocol)clientData);
    	            		}
    					}
                		toNotifyAll(reciverData);
                	}else if(clientData instanceof UserFriendReuqetProtocol){
                		System.out.println("用户好友列表请求");
                		onLineClient.put(((UserFriendReuqetProtocol)clientData).getRequestClientUUID(), socket);
                		targetClient= getConnectClient(((UserFriendReuqetProtocol)clientData).getRequestClientUUID());
                		toNotifyAll(reciverData);
					}else if(clientData instanceof HeartBeatProcotol){
						System.out.println("用户心跳");
					}
                	
                	try {
                		if(targetClient!=null){
                			serverWriteTask.outputStream=new DataOutputStream(targetClient.getOutputStream());
                		}else{
                			serverWriteTask.outputStream=null;
                		}
					} catch (IOException e) {
						e.printStackTrace();
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
    
    public synchronized void addWriteTask(ChatMsgProtocol procotol,Socket targetClient ){
    	reciverData.add(procotol);
    	try {
			serverWriteTask.outputStream=new DataOutputStream(targetClient.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    public  Socket getConnectClient(String clientID){
    	return onLineClient.get(clientID);//与小米手机进行通信
    }
    
    
    /**
     * 打印已经链接的客户端
     */
    public static void printAllClient(){
    	if (onLineClient==null) {
			return ;
		}
    	Iterator<String> inter=onLineClient.keySet().iterator();
    	while(inter.hasNext()){
    		System.out.println("client:"+inter.next());
    	}
    }
    
    /**
     *   发送消息给指定用户
     * @param clientId
     * @return
     */
    public boolean sendToClient(String clientId){
    	if(onLineClient.contains(clientId)){
    		return true;
    	}
    	return false;
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
