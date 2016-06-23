package chatDBInit;


import java.util.List;

import bean.User;
import socketserver.JsonUtil;

public class DbMain {

	public static void main(String[] args) {
		DBConnect db=DBConnect.instance;
		
		
//		createUser(db);
		
//		queryFriends(db,"9b0a60c7df57485ba2be6b81dac00d5d");
		User user1=new User();
		user1.setFriendIP("120.0.0.1");
		user1.setFriendUUID("4d8e938015b34049a21fad31a3e29820");
		user1.setSelfUUID("4c37987f8e13461dbf0c133af338c039");
		
		User user2=new User();
		user2.setFriendIP("128.6.110.1");
		user2.setFriendUUID("04fb00752bc94d84b718d9a41e024c7a");
		user2.setSelfUUID("4c37987f8e13461dbf0c133af338c039");
		
		insertFriends(user1, db, "4c37987f8e13461dbf0c133af338c039");
		insertFriends(user2, db, "4c37987f8e13461dbf0c133af338c039");
	}
	
	public List<User> createUserMock(int leng){
		List<User> users=null;
		for(int i=0;i<leng;i++){
			User user=new User();
//			user.
//			users.add(e)
		}
		return users;
	}
	private static void createUser(DBConnect db) {
		db.createUserFriendTable();
		
		User user=new User();
		user.setSelfUUID("4c37987f8e13461dbf0c133af338c039");
		user.setFriendIP("120.0.0.1");
		db.insertUser(user);
		
		
		User friend1=new User();
		friend1.setSelfUUID("4d8e938015b34049a21fad31a3e29820");
		friend1.setFriendIP("120.0.2.1");
		db.insertUser(friend1);
		
		User user3=new User();
		user3.setSelfUUID("4c37987f8e13461dbf0c133af338c039");
		user3.setFriendIP("148.142.21.101");
		db.insertUser(user3);
		
		User user4=new User();
		user4.setSelfUUID("04fb00752bc94d84b718d9a41e024c7a");
		user4.setFriendIP("241.31.12.53");
		db.insertUser(user4);
	}
	
	public static void queryFriends(DBConnect db,String selfUUID){
		List<User> users=db.getFriends(selfUUID);
		for(int i=0,size=users.size();i<size;i++){
			System.out.println(users.get(i).getFriendUUID());
		}
		System.out.println("json: "+JsonUtil.toJson(users));
	}
	public static void insertFriends(User user,DBConnect db,String selfUUID){
		db.inserUserFriend(user,selfUUID);
	}
}
