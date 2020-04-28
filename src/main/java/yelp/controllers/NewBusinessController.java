package yelp.controllers;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
public class NewBusinessController {

	@Autowired
	JSONUtil json; //Object to assist with JSON operations

	/*
	 * Commenting as using NoSQL
	 * 
		@Autowired
		SQLData sql; //Object to get SQL data from Galera
	 */
	
	@Autowired
	NoSQLData noSql; //Object to get data from MongoDB

	@PostMapping("/q1")
	public String returnData(@RequestBody String category){
		System.out.println("#############----->"+ category);
		HashMap<String,String> map = new HashMap<String, String>();

		//map = sql.getQ1Result(category);
		map = noSql.getQ1Result(category);
		String out = json.objToString(map);
		return out;
	}
}
