package edu.amrita.aview.common.utils;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JSONParserUtils
{
	private static ObjectMapper objectMapper = new ObjectMapper();
	public String error= null;

	static
	{
		objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
	}
	
	public static Object readJSONAsObject(String strToRead, Class<?> classToConvert)
	{
		 Object convertedObject = new Object();
		try 
		{
			 convertedObject = objectMapper.readValue(strToRead, classToConvert);
		}
		catch (JsonParseException e)
		{
			// modify the alert give proper alert message
			convertedObject ="Invalid exception at JSON value. Possible reason(s): 1. Integer value is specified as string without quots,vice versa, 2 .Value may be not specified for the attributes 3. Unknown.";
			e.printStackTrace();
		}
		catch (JsonMappingException e) 
		{ 
			
			convertedObject ="Invalid exception at JSON mapping. Possible reason(s): 1. Provided data-type of the field(s) is/are unacceptable, 2. Invalid format in time/date 3. Unknown.";
			e.printStackTrace();
			
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			convertedObject = "Invalid exception at input output or No content to map to Object due to end of input";
			e.printStackTrace();
		}		
		return convertedObject;
	}
	
	public static String writeObjectAsJSON(Object genericObject)
	{
		String objAsString = null;
		try
		{
			objAsString = objectMapper.writeValueAsString(genericObject);
		}
		catch (JsonGenerationException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return objAsString;
	}
}
