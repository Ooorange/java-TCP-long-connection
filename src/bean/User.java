package bean;

public class User {

	private String friendIP;
	private String self_uuid;
	private String friendUUID;

	public String getSelf_uuid() {
		return self_uuid;
	}

	public void setSelf_uuid(String self_uuid) {
		this.self_uuid = self_uuid;
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
