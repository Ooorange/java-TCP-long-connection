package chatDBInit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bean.User;

public class DBConnect {

	String DBName="chatServer.db";
	java.sql.Statement statement; 
	Connection connection;
	private DBConnect(){
		try {
			Class.forName("org.sqlite.JDBC");
			connection=DriverManager.getConnection("jdbc:sqlite:"+DBName);
			statement=connection.createStatement();
			
			//创建离线消息表
			statement.executeUpdate("create table  if not exists message(message_type int ,message varchar(200),sender_uuid varchar(32), reviver_uuid varchar(32), send_time varchar(25),insert_time varchar(25));" );//创建一个表，两列
			//创建用户表
			statement.executeUpdate("create table  if not exists user(serise_id integer  auto_increment,user_uuid varchar(32) unique not null ,user_ip varchar(20),primary key(serise_id));");
			
		} catch (ClassNotFoundException e) {
			System.out.println("sqlite not found exception ");
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static final DBConnect instance=new DBConnect();
	
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
		}finally {
			//				if(statement!=null)
//				statement.close();
			if (connection!=null) {
//					connection.close();
			}
		}
		return insertResult;
	}
	
	/**
	 * 创建用户好友表
	 * @return
	 */
	public boolean createUserFriendTable(){
		try {
			statement.executeUpdate("create table if not exists user_friend(user_friend_seriseid integer auto_increment,self_uuid varchar(32) not null,friend_uuid varchar(32) unique not null,friend_ip varchar(20) unique,primary key(user_friend_seriseid));");
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
			statement.executeUpdate("insert into user_friend(self_uuid,friend_uuid,friend_ip) values('"+selfUUID+"+','"+friend.getSelf_uuid()+"','"+friend.getFriendIP()+"');");
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
			ResultSet resultSet=statement.executeQuery("select * from user_friend where self_uuid="+selfUUId+";");
			while(resultSet.next()){
				User user=new User();
				user.setFriendIP(resultSet.getString("friend_ip"));
				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return users;
	}

	
	/**
	 * 插入一个新的用户
	 * @param user
	 * @return
	 */
	public boolean insertUser(User user){
		//创建用户好友表
		try {
			statement.executeUpdate("insert into user (user_uuid,user_ip) values ('"+user.getSelf_uuid()+"','"+user.getFriendIP()+"');");
			return true;
		} catch (SQLException e) {
			System.out.println("SqlException");
			return false;
		}
	}
	
	
	
	
}
