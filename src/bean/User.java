package bean;

public class User {

	private String friendIP;
	private String selfUUID;
	private String friendUUID;
	private String friendNicName;
	
	
	public String getFriendNickName() {
		return friendNicName;
	}

	public void setFriendNickName(String friend_nick_name) {
		this.friendNicName = friend_nick_name;
	}

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
