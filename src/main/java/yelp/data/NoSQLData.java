package yelp.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import yelp.utility.JSONUtil;
import yelp.utility.MongoConnect;

/**
 * 
 * @author CJ
 *
 */
@SuppressWarnings("deprecation")
@Component
public class NoSQLData {

	@Autowired
	MongoConnect mc; //Gets the object for MongoDB connection
	
	@Autowired
	JSONUtil json; //Object to assist with JSON operations
	
	public HashMap<String, String> getQ1Result(String category){
		HashMap<String,String> map = new HashMap<String, String>();
		DB database = mc.getDatabase();
		
		//db.yelp_reviews_main.find({category:"Education",is_open:1, stars :{$gt:3.5}},{name:1,city:1,_id:0}).sort({date:-1}).limit(10)
		DBCollection collection = database.getCollection("yelp_reviews_main");
		BasicDBObject projection = new BasicDBObject("name",1).append("city", 1);
		BasicDBObject match = new BasicDBObject("stars",new BasicDBObject("$gt",3.5));
		match.append("is_open",1);
		match.append("category",category.replace("\"", ""));
		BasicDBObject sort = new BasicDBObject("date",-1);
	
		DBCursor output = collection.find(match,projection).sort(sort).limit(10);
		while(output.hasNext()) {
			@SuppressWarnings("unchecked")
			LinkedHashMap<String, String> temp = (LinkedHashMap<String, String>) output.next();
			map.put(temp.get("name"), String.valueOf(temp.get("city")));
		}
			
		return map;
	}
	
	public HashMap<String, String> getQ2Result(String input) throws IOException{
		ObjectMapper mapper = new ObjectMapper();
		JsonNode actualObj = mapper.readTree(input);
		String state = actualObj.get("state").toString().replace("\"", "");
		int year = Integer.parseInt(actualObj.get("year").toString().replace("\"", ""));
		
		HashMap<String,String> map = new HashMap<String, String>();
		DB database = mc.getDatabase();
		//db.yelp_reviews_main.aggregate( [  {$match: {state:"AZ",year:2016} }, {$group:{_id: "$user_id",count: { $sum: 1 }}}, {$sort : {count:-1}},{$limit:10}])
		DBCollection collection = database.getCollection("yelp_reviews_main");
		List<DBObject> pipeline = new ArrayList<>();
		
		BasicDBObject match1 = new BasicDBObject("$match",new BasicDBObject("state",state));
		BasicDBObject match2 = new BasicDBObject("$match",new BasicDBObject("year",year));
		BasicDBObject groupCondition = new BasicDBObject("_id","$user_id");
		groupCondition.put("count", new BasicDBObject( "$sum", 1));
		BasicDBObject group = new BasicDBObject("$group", groupCondition);
		BasicDBObject sort = new BasicDBObject("$sort", new BasicDBObject("count",-1));
		BasicDBObject limit = new BasicDBObject("$limit", 10);
		
		pipeline.add(match1);
		pipeline.add(match2);
		pipeline.add(group);
		pipeline.add(sort);
		pipeline.add(limit);
		
		AggregationOutput output = collection.aggregate(pipeline);
		map = aggrToMap(output);
		return map;
	}
	
	public HashMap<String, String> getQ3Result(String city){
		HashMap<String,String> map = new HashMap<String, String>();
		DB database = mc.getDatabase();
		// db.yelp_business_main.aggregate( [  {$match: {city:"Phoenix"} }, {$group: {_id: "$category",  count: { $sum: 1 } }   }])
		DBCollection collection = database.getCollection("yelp_business_main");
		BasicDBObject groupFields = new BasicDBObject( "_id", "$category");
		groupFields.put("count", new BasicDBObject( "$sum", 1));
		BasicDBObject  group = new BasicDBObject("$group", groupFields );
		BasicDBObject match = new BasicDBObject("$match", new BasicDBObject("city", city.replace("\"", "")) );
	  
		AggregationOutput output = collection.aggregate(match,group);
		map = aggrToMap(output);
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public String getCategory() {
		List<String> list = new ArrayList<String>();
		DB database = mc.getDatabase();
		DBCollection collection = database.getCollection("yelp_business");
		list = collection.distinct("category");

		String out = json.objToString(list);
		return out;
	}
	
	@SuppressWarnings("unchecked")
	public String getCity() {
		List<String> list = new ArrayList<String>();
		DB database = mc.getDatabase();
		DBCollection collection = database.getCollection("yelp_location");
		list = collection.distinct("city");

		String out = json.objToString(list);
		return out;
	}
	
	@SuppressWarnings("unchecked")
	public String getState() {
		List<String> list = new ArrayList<String>();
		DB database = mc.getDatabase();
		DBCollection collection = database.getCollection("yelp_location");
		list = collection.distinct("state");

		String out = json.objToString(list);
		return out;
	}
	
	private HashMap<String, String> aggrToMap(AggregationOutput output) {
		HashMap<String,String> map = new HashMap<String, String>();
		
		Iterator<DBObject> it = output.results().iterator();
		while(it.hasNext()) {
			@SuppressWarnings("unchecked")
			LinkedHashMap<String, String> temp = (LinkedHashMap<String, String>) it.next();
			map.put(temp.get("_id"), String.valueOf(temp.get("count")));
		}
		return map;
	}
}
