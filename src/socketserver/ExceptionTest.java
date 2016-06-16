package socketserver;

import java.io.IOException;

public class ExceptionTest {
	public static void main(String[] args) {
		try {
			producer(13);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void  producer(int i) throws IOException{
		if(i==3){
			throw new IOException("值错误");
		}else{
			throw new IOException("值正确");
		}
	}
}
