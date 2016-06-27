package chatDBInit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bean.User;

/**
 * 数据库创建思路：
 * 	为每个用户创建自己的表，包含一个唯一的序列号serise_id(未来拓展使用),用户自己的user_uuid，用户的IP地址user_ip(每次用户登录时创建或者更新表)
 * 	
 * 	创建一个好友表（只有一张好友表）,将所有人的好友存储在此。（后台自己操作）
 * 
 * 	创建离线消息表
 * 	当对方用户不在线的时候创建或者更新
 * @author orange
 *
 */
public class DBConnect {

	String DBName="chatServer.db";
	java.sql.Statement statement; 
	Connection connection;
	public static final DBConnect instance=new DBConnect();
	private DBConnect(){
		try {
			Class.forName("org.sqlite.JDBC");
			connection=DriverManager.getConnection("jdbc:sqlite:"+DBName);
			statement=connection.createStatement();
			
			//创建离线消息表
			statement.executeUpdate("create table  if not exists message(message_type int ,message varchar(200),sender_uuid varchar(32), reviver_uuid varchar(32), send_time varchar(25),insert_time varchar(25));" );//创建一个表，两列
		} catch (ClassNotFoundException e) {
			System.out.println("sqlite not found exception ");
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 创建用户表
	 * @param userUUID
	 * @param statement
	 * @return
	 */
	public boolean crateateUserTable(String userUUID){
		boolean createReust=false;
		try {
			statement.executeUpdate("create table  if not exists "+"user_"+userUUID+"(serise_id integer  auto_increment,user_uuid varchar(32) unique not null ,user_ip varchar(20),primary key(serise_id));");
			createReust=true;
		} catch (SQLException e) {
			createReust=false;
			e.printStackTrace();
		}
		return createReust;
	}
	
	/**
	 * 插入一个新的用户到用户表
	 * @param user
	 * @return
	 */
	public boolean insertUser(User user){
		//创建用户好友表
		try {
			statement.executeUpdate("insert into user_"+user.getSelfUUID()+"(user_uuid,user_ip) values ('"+user.getSelfUUID()+"','"+user.getFriendIP()+"');");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 插入一个离线的信息
	 * @param type
	 * @param message
	 * @param senderUUID
	 * @param reciverUUID
	 * @param sendDate
	 * @return
	 */
	public boolean insertMessage(int type,String message,String senderUUID,String reciverUUID,String sendDate){
		boolean insertResult=false;
		try {
			statement.executeUpdate("insert into message values("+type+",'"+message+"','"+senderUUID+"','"+reciverUUID+"','"+sendDate+"','"+new Date().toString()+"');");
			insertResult=true;
		} catch (SQLException e) {
			insertResult=false;
			e.printStackTrace();
		}
		return insertResult;
	}
	
	
	/**
	 * 创建用户好友表
	 * @return
	 */
	public boolean createUserFriendTable(){
		try {
			statement.executeUpdate("create table if not exists user_friend(user_friend_seriseid integer auto_increment,self_uuid varchar(32) not null,friend_uuid varchar(32)  not null,friend_ip varchar(20) ,friend_nick_name varchar(10) not null,primary key(user_friend_seriseid));");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 插入一个好友
	 * @param friend 插入好友的信息
	 * @param selfUUID 自己的信息
	 * @return
	 */
	public boolean inserUserFriend(User friend,String selfUUID){
		boolean insertResult=false;
		try {
			statement.executeUpdate("insert into user_friend(self_uuid,friend_uuid,friend_ip,friend_nick_name) values('"+selfUUID+"','"+friend.getSelfUUID()+"','"+friend.getFriendIP()+"'"+friend.getFriendNickName()+"');");
		} catch (SQLException e) {
			insertResult=false;
			e.printStackTrace();
		}
		return insertResult;
	}
	/**
	 * 获取好友列表
	 * @param selfUUId
	 * @return
	 */
	public List<User> getFriends(String selfUUId){
		List<User> users=new ArrayList<User>();
		try {
			ResultSet resultSet=statement.executeQuery("select * from user_friend where self_uuid= '"+selfUUId+"';");
			
			while(resultSet.next()){
				User user=new User();
				user.setSelfUUID(selfUUId);
				user.setFriendNickName(resultSet.getString("friend_nick_name"));
				user.setFriendIP(resultSet.getString("friend_ip"));
				user.setFriendUUID(resultSet.getString("friend_uuid"));
				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return users;
	}

	

	
	
	
	
}
