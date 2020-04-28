package yelp.controllers;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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
public class ReviewController {

	/*
	 * Commenting as using NoSQL
	 * 
		@Autowired
		SQLData sql; //Object to get SQL data from Galera
	 */
	
	@Autowired
	JSONUtil json; //Object to assist with JSON operations
	
	@Autowired
	NoSQLData noSql; //Object to get data from MongoDB

	@RequestMapping("/q2")
	public String returnData(@RequestBody String input) throws IOException {
		System.out.println("#############----->"+ input);
		HashMap<String,String> map = new HashMap<String, String>();

		//map = sql.getQ2Result(input);
		map = noSql.getQ2Result(input);
		String out = json.objToString(map);
		return out;
	}
}
