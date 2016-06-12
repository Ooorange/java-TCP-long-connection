package socketserver;

public class Test {

	
	public static void main(String []args){
		byte[] header=SocketUtil.int2ByteArrays(567666);
		System.out.println(SocketUtil.byteArrayToInt(header));
	}
}
