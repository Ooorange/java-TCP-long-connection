package procotol;

import java.io.UnsupportedEncodingException;

import socketserver.ProtocolException;
import socketserver.SocketUtil;

/**
 * Created by orange on 16/6/13.
 */
public class ChatMsgProtocol extends BasicProtocol {
	public static final String CHATMEGCOMMEND="0001";
    public static final int SLEFUUID_LEN=32;
    public static final int MSGTARGETUUID_LEN=32;
    public static final int CLIENTVERION_LEN=4;
    
	
    private String message;
    private String selfUUid;
    private String msgTargetUUID;
    private int clientVersion;

    public String getMsgTargetUUID() {
        return msgTargetUUID;
    }

    public void setMsgTargetUUID(String msgTargetUUID) {
        this.msgTargetUUID = msgTargetUUID;
    }


    public String getMessage() {
        return message;
    }

    public int getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(int clientVersion) {
        this.clientVersion = clientVersion;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSelfUUid() {
        return selfUUid;
    }

    public void setSelfUUid(String selfUUid) {
        this.selfUUid = selfUUid;
    }
    @Override
    public String toString() {
    	return "信息:"+message+",自身UUID:"+selfUUid+",消息目的UUID:"+msgTargetUUID+",版本号:"+clientVersion;
    }

	@Override
	public String getCommend() {
		return CHATMEGCOMMEND;
	}
	
	@Override
	public byte[] getContentData() {
		return super.getContentData();
	}
	
	@Override
	public int parseBinary(byte[] data) throws ProtocolException {
		int pos=super.parseBinary(data);
		selfUUid=new String(data, pos, SLEFUUID_LEN);
		pos+=SLEFUUID_LEN;
		msgTargetUUID=new String(data, pos, SLEFUUID_LEN);
		pos+=SLEFUUID_LEN;
		clientVersion=SocketUtil.bytesToInt(data, pos);
		pos+=CLIENTVERION_LEN;
		try {
			message=new String(data, pos, data.length-pos,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return pos;
	}
}
