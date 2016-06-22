package procotol;

import java.io.ByteArrayOutputStream;

import socketserver.ProtocolException;

public class RegisterProtocol extends BasicProtocol{

	public static final String REGISTERCOMMEND="0004";
	 public String selfUUID="";
	 
	@Override
	public String getCommend() {
		return REGISTERCOMMEND;
	}

	@Override
	public byte[] getContentData() {
		byte[] pre=super.getContentData();
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		baos.write(pre,0,pre.length);
		baos.write(selfUUID.getBytes(),0,ChatMsgProtocol.SLEFUUID_LEN);
		
		return baos.toByteArray();
	}
	
	@Override
	public int parseBinary(byte[] data) throws ProtocolException {
		int pos=super.parseBinary(data);
		selfUUID=new String(data, pos, ChatMsgProtocol.SLEFUUID_LEN);
		return pos;
	}

	public String getSelfUUID() {
		return selfUUID;
	}

	public void setSelfUUID(String selfUUID) {
		this.selfUUID = selfUUID;
	}
	
}
