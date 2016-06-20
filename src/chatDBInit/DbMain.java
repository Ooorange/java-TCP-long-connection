package chatDBInit;


import bean.User;

public class DbMain {

	public static void main(String[] args) {
		DBConnect db=DBConnect.instance;
		
		
		db.createUserFriendTable();
		
		User user=new User();
		user.setSelf_uuid("9b0a60c7df57485ba2be6b81dac00d5d");
		user.setFriendIP("120.0.0.1");
		db.insertUser(user);
		
		
		User friend1=new User();
		friend1.setSelf_uuid("4d8e938015b34049a21fad31a3e29820");
		friend1.setFriendIP("120.0.2.1");
		db.insertUser(friend1);
		db.inserUserFriend(friend1,user.getSelf_uuid());
		
		User user3=new User();
		user3.setSelf_uuid("4c37987f8e13461dbf0c133af338c039");
		user3.setFriendIP("148.142.21.101");
		db.insertUser(user3);
		db.inserUserFriend(user3,user.getSelf_uuid());
		
		User user4=new User();
		user4.setSelf_uuid("04fb00752bc94d84b718d9a41e024c7a");
		user4.setFriendIP("241.31.12.53");
		db.insertUser(user4);
		db.inserUserFriend(user4,user.getSelf_uuid());
	}
}
