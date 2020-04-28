package yelp.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import yelp.data.NoSQLData;
//import yelp.data.SQLData;
import yelp.utility.JSONUtil;


/**
 * 
 * @author CJ
 *
 */
@RestController
public class YelpController {
	@Autowired
	JSONUtil json; //Object to assist with JSON operations
	
	/*
	 * Commenting as using NoSQL
	 * 
		@Autowired
		SQLData sql; //Object to get SQL Data from Galera
	 */
	
	@Autowired
	NoSQLData noSql; //Object to get Data from MongoDB
	
	/**
	 * Returns static data for city, state, category and year
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping("/staticData")
	public String returnData(){
		/*
		String category = sql.getCategory();
		String city = sql.getCity();
		String state = sql.getState();
		*/
		String category = noSql.getCategory();
		String city = noSql.getCity();
		String state = noSql.getState();
		String year = getYear();
		HashMap<String,String> map = new HashMap<String, String>();
		map.put("category", category);
		map.put("city", city);
		map.put("state", state);
		map.put("year", year);
		
		String out = json.objToString(map);
		return out;
		
	}
	
	/**
	 * years vary from 2004 to 2017 as per data, setting them in a list
	 */
	public String getYear() {
		List<Integer> year = new ArrayList<Integer>();
		for(int i = 2004; i< 2018; i++) {
			year.add(i);
		}
		String out = json.objToString(year);
		return out;
	}
	
}
