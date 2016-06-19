package chatDBInit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

	String DBName="chatServer.db";
	
	private DBConnect(){
		try {
			Class.forName("org.sqlite.JDBC");
			Connection connection=DriverManager.getConnection("jdbc:sqlite:"+DBName);
			java.sql.Statement statement=connection.createStatement();
			
		} catch (ClassNotFoundException e) {
			System.out.println("sqlite not found exception ");
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static final DBConnect instance=new DBConnect();
	
	
}
