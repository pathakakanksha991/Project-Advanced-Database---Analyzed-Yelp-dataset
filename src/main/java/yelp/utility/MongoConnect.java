package yelp.utility;

import org.springframework.stereotype.Component;

import com.mongodb.DB;
import com.mongodb.MongoClient;

@Component
public class MongoConnect {
	
	DB database=null;
	
	static MongoClient mongoClient = null;
	@SuppressWarnings("deprecation")
	public void getNewDatabase() {
		try{
			mongoClient = new MongoClient("localhost");
			database = mongoClient.getDB("yelp");
		}catch(Exception e){
			System.out.println("Exception"+ e);
		}
	}
	public DB getDatabase() {
		if(database==null) {
			getNewDatabase(); 
		}
		return database;
	}
	
	public static void close() {
		mongoClient.close();
	}
	
}
