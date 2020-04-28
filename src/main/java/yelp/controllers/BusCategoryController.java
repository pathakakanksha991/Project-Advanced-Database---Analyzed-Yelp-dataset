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
 * Class for Question 3
 * Input as city
 * Output is categories and their count for that city
 */
@RestController
public class BusCategoryController {

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

	@PostMapping("/q3")
	public String returnData(@RequestBody String city){
		System.out.println("#############----->"+ city);
		HashMap<String,String> map = new HashMap<String, String>();

		//map = sql.getQ3Result(city);
		map = noSql.getQ3Result(city);
		String out = json.objToString(map);
		return out;
	}
}
