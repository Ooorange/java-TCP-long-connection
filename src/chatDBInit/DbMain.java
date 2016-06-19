package chatDBInit;

import java.util.Date;

public class DbMain {

	public static void main(String[] args) {
		DBConnect db=DBConnect.instance;
		
		db.insertMessage(1, "我是小米", "4c37987f8e13461dbf0c133af338c039", "04fb00752bc94d84b718d9a41e024c7a",new Date().toString());
		db.insertMessage(2, "我是小米", "4c37987f8e13461dbf0c133af338c039", "04fb00752bc94d84b718d9a41e024c7a",new Date().toString());
	}
}
