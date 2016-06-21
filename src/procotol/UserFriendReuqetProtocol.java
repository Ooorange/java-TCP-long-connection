package procotol;

public class UserFriendReuqetProtocol extends BasicProtocol {
	 public static String FRIENDREQUEST="0002";
	  
	public String requestClientUUID;
    private int clientVersion;
	public String getRequestClientUUID() {
		return requestClientUUID;
	}
	public void setRequestClientUUID(String requestClientUUID) {
		this.requestClientUUID = requestClientUUID;
	}
	public int getClientVersion() {
		return clientVersion;
	}
	public void setClientVersion(int clientVersion) {
		this.clientVersion = clientVersion;
	}
	@Override
	public String getCommend() {
		return FRIENDREQUEST;
	}
}
