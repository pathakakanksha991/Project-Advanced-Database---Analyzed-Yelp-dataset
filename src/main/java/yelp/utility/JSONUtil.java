package yelp.utility;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class to assist on conversion of Objects into JSON string
 * @author CJ
 *
 */
@Component
public class JSONUtil {

	public String objToString(Object in) {
		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			json = mapper.writeValueAsString(in);
		} catch (JsonProcessingException e) {
			System.out.println("Exception"+ e);
		}
		return json;
		
	}
}
