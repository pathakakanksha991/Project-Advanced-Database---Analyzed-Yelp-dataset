package yelp.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * 
 * @author CJ
 *
 */
@Component
public class GaleraConnect{

	@Autowired
	JSONUtil json;
	
	private static String driverClass = "org.mariadb.jdbc.Driver";
	private Connection con = null;
	
	public GaleraConnect(){
		try{
			Class.forName(driverClass);  
			String user = "XXX";
			String pass = "XXX";
			String url = "jdbc:mariadb:failover://IP1,IP2,IP3/<dbname>?autoReconnect=true";
			
			System.setProperty("javax.net.ssl.keyStore","xxx.xxx"); 
		//	setCon(DriverManager.getConnection(url,user,pass)); // this is commented to avoid app from connecting to Galera, the EC2 instance for IPs mentioned above is terminated
			
		}catch(Exception e){
			System.out.println(e);
		}
	}  
	
	
	public void close() {
		try {
			getCon().close();
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	public Connection getCon() {
		return con;
	}

	public void setCon(Connection con) {
		this.con = con;
	}
	
}
