package chatDBInit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

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
			statement.executeUpdate("create table  if not exist user(user_uuid varchar(32),user_ip varchar(20));");
			//创建用户好友表
			statement.executeUpdate("create table if not exits user_friend(self_uuid varchar(32),friend_ip varchar(20);");
			
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
	
	public User getFriend(String selfUUId){
		
		User user=new User();
		try {
			ResultSet resultSet=statement.executeQuery("select * from user_friend where self_uuid="+selfUUId+";");
			while(resultSet.next()){
				user.setFriendIP(resultSet.getString("friend_ip"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return user;
	}
	
	
	
	
	
	
	
	
	
	
}
