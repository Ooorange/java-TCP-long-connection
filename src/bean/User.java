package bean;

public class User {

	private String friendIP;
	private String selfUUID;
	private String friendUUID;

	public String getSelfUUID() {
		return selfUUID;
	}

	public void setSelfUUID(String selfUUID) {
		this.selfUUID = selfUUID;
	}

	public String getFriendIP() {
		return friendIP;
	}

	public void setFriendIP(String friendIP) {
		this.friendIP = friendIP;
	}

	public String getFriendUUID() {
		return friendUUID;
	}

	public void setFriendUUID(String friendUUID) {
		this.friendUUID = friendUUID;
	}
	
}
