package yelp.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import yelp.utility.GaleraConnect;
import yelp.utility.JSONUtil;


/**
 * 
 * @author CJ
 *
 */
@Component
public class SQLData {
	@Autowired
	GaleraConnect gc; //Gets the object for Galera DB connection

	@Autowired
	JSONUtil json; //Object to assist with JSON operations

	public HashMap<String, String> getQ1Result(String category){
		HashMap<String,String> map = new HashMap<String, String>();
		Connection con = gc.getCon();
		String sql = "select a.name,c.city from yelp_business a, yelp_reviews b, yelp_location c where"
				+ " a.business_id=b.business_id and a.category=?"
				+ " and a.business_id=c.business_id and a.is_open=1 and a.stars>3.5 order by b.date desc limit 10";
		System.out.println("Running: "+sql);
		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, category.replace("\"", ""));
			ResultSet rs = ps.executeQuery();
			System.out.println("done!");

			while(rs.next()) {
				map.put(rs.getString("name"),rs.getString("city"));
			}
		}catch(Exception e) {
			System.out.println("Exception:"+e);
		}
		return map;
	}


	public HashMap<String, String> getQ2Result(String input) throws IOException{
		HashMap<String,String> map = new HashMap<String, String>();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode actualObj = mapper.readTree(input);
		String state = actualObj.get("state").toString().replace("\"", "");
		int year = Integer.parseInt(actualObj.get("year").toString().replace("\"", ""));

		Connection con = gc.getCon();
		String sql ="select user_id, count(review_id) as cnt from yelp_reviews where year=? and business_id "
				+ "in(select business_id from yelp_location where state=?) group by user_id order by cnt desc limit 10";


		System.out.println("Running: "+sql);
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, year);
			ps.setString(2, state);
			ResultSet rs = ps.executeQuery();
			System.out.println("done!");

			while(rs.next()) {
				map.put(rs.getString("user_id"),rs.getString("cnt"));
			}
		}catch(Exception e) {
			System.out.println("Exception:"+e);
		}
		return map;
	}

	public HashMap<String,String> getQ3Result(String city) {
		HashMap<String,String> map = new HashMap<String, String>();
		Connection con = gc.getCon();
		String sql = "select count(*) AS count, category from yelp_business a, yelp_location b where "
				+ "a.business_id=b.business_id and city=? group by category order by count desc, category asc";
		System.out.println("Running: "+sql);
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, city.replace("\"", ""));
			ResultSet rs = ps.executeQuery();
			System.out.println("done!");

			while(rs.next()) {
				map.put(rs.getString("category"),rs.getString("count"));
			}
		}catch(Exception e) {
			System.out.println("Exception:"+e);
		}
		return map;
	}


	/**
	 * getting the categories values from DB
	 * @return
	 */
	public String getCategory() {
		List<String> list = new ArrayList<String>();
		Connection con = gc.getCon();
		String sql = "select distinct category from yelp_business";


		try{
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				list.add(rs.getString("category"));
			}
		}catch(SQLException e) {
			System.out.println("Exception:"+e);
		}
		String out = json.objToString(list);
		return out;

	}

	/**
	 * getting the city values from DB
	 * @return
	 */
	public String getCity() {
		List<String> list = new ArrayList<String>();
		Connection con = gc.getCon();
		String sql = "select distinct city from yelp_location";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				list.add(rs.getString("city"));
			}
		}catch(SQLException e) {
			System.out.println("Exception:"+e);
		}
		String out = json.objToString(list);
		return out;

	}

	/**
	 * getting the state values from DB
	 * @return
	 */
	public String getState() {
		List<String> list = new ArrayList<String>();
		Connection con = gc.getCon();
		String sql = "select distinct state from yelp_location";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				list.add(rs.getString("state"));
			}
		}catch(SQLException e) {
			System.out.println("Exception:"+e);
		}
		String out = json.objToString(list);
		return out;

	}
}
