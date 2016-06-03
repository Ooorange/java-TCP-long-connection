package socketserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

public class UDPServer {
	/**
	 * 1:创建DatagramSocket,并且指定端口号
	 * 2:创建DatagramPacket,
	 * 3:接收数据
	 * 4:读取数据
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DatagramSocket datagramSocket=new DatagramSocket(9001);
			byte[] buff=new byte[1024];//用于接受客户端的数据，指定接收数据包的大小
			DatagramPacket datagramPacket=new DatagramPacket(buff,buff.length);
			System.out.println("-------等待客户端发送数据中-------------");
			datagramSocket.receive(datagramPacket);//在接收到数据包之前会阻塞
			String data=new String(buff,0,datagramPacket.getLength());
			System.out.println("-------收到客户端数据为:"+data+"-------------");
			//向客户端发送数据：定义地址，端口号，数据报信息等
			byte[] buffData="这是一条服务端的数据哦".getBytes();
			InetAddress inetAddress=datagramPacket.getAddress();
			int port=datagramPacket.getPort();
			DatagramPacket sendData=new DatagramPacket(buffData, buffData.length,inetAddress,port);
			datagramSocket.send(sendData);
			System.out.println("-------向客户端发送数据完成-------------");
			datagramSocket.close();
		} catch (SocketException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
