package procotol;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import socketserver.ProtocolException;

public class UserFriendReuqetProtocol extends BasicProtocol {
	 public static String FRIENDREQUEST="0003";
	  
	public String requestClientUUID;
//    private int clientVersion;
    private String usersJson;
    
    public UserFriendReuqetProtocol() {
    	
	}
    
	public String getRequestClientUUID() {
		return requestClientUUID;
	}
	public void setRequestClientUUID(String requestClientUUID) {
		this.requestClientUUID = requestClientUUID;
	}
	@Override
	public String getCommend() {
		return FRIENDREQUEST;
	}
	public String getUsersJson() {
		return usersJson;
	}
	public void setUsersJson(String usersJson) {
		this.usersJson = usersJson;
	}
	@Override
	public int parseBinary(byte[] data) throws ProtocolException {
		int pos=super.parseBinary(data);
		try {
			requestClientUUID=new String(data, pos, ChatMsgProtocol.SLEFUUID_LEN,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return pos;
	}
	@Override
	public byte[] getContentData() {
		byte[] pre=super.getContentData();
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		baos.write(pre,0,pre.length);
		byte[] userJson=usersJson.getBytes();
		baos.write(userJson,0,usersJson.length());
		return baos.toByteArray();
	}
	
	@Override
	public String toString() {
		return "UserFriendReuqetProtocol:"+usersJson;
	}
}
