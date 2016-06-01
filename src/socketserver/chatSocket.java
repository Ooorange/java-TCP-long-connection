package socketserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class chatSocket {
	public static void main(String[] arg){
		try {
			//创建ServerSocket实例，并且监听端口号，服务端处于阻塞状态
			ServerSocket serverSocket=new ServerSocket(9000);
			System.out.println("＊＊＊服务端启动完成，等待客户端链接＊＊＊＊");
			Socket socket=serverSocket.accept();
			InputStream inputStream=socket.getInputStream();
			InputStreamReader inputStreamReader=new InputStreamReader(inputStream,"utf-8");
			BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
			String reciverData=null;
			while((reciverData=bufferedReader.readLine())!=null){
				System.out.println("服务器收到数据:"+reciverData);
			}
			System.out.println("＊＊＊客户端链接成功，数据接受成功，即将关闭链接＊＊＊＊");
			socket.shutdownInput();
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
