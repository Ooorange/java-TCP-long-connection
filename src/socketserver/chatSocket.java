package socketserver;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
			socket.shutdownInput();
			//＊＊＊＊＊＊＊＊＊将一个java文件传给客户端＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊＊
			File file=new File("/work/workspace/ChatServer/src/socketserver/ServerActivity.java");
			FileInputStream fileInputStream=new FileInputStream(file);
			InputStreamReader inputFileStreamReader=new InputStreamReader(fileInputStream,"utf-8");
			BufferedReader bufferedFileReader=new BufferedReader(inputFileStreamReader);
			String fileData=null;
			StringBuilder stringBuilder=new StringBuilder();
			while((fileData=bufferedFileReader.readLine())!=null){
				stringBuilder.append(fileData);
			}
			System.out.println(stringBuilder.toString());
			//*****************end of file read********
			
			//****************file write************
			OutputStream outputStream=socket.getOutputStream();
			BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(outputStream);
			byte[] data=stringBuilder.toString().getBytes();
			bufferedOutputStream.write(data, 0, data.length);
			bufferedOutputStream.flush();
			if (socket!=null) {
				socket.shutdownOutput();
			}
			bufferedOutputStream.close();
			outputStream.close();
			bufferedFileReader.close();
			inputFileStreamReader.close();
			fileInputStream.close();
			//***************end of file write************
			
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
