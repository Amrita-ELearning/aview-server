package edu.amrita.aview.audit.helpers;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Ignore;
import org.junit.Test;

import edu.amrita.aview.audit.daos.AnalyticsDAO;
import edu.amrita.aview.common.AViewException;

public class AnalyticsHelperTest {

	/**
	 * This method is called to test the method getUserAndLectureDetailsForLecture 
	 * This Method is used to get the lecture details and get registered user count,
	 * using the method getLectureDetails
	 * @param lectureId
	 * @return list
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 **/
	@Test
	public void testGetUserAndLectureDetailsForLecture() throws AViewException, JsonParseException, JsonMappingException, IOException
	{
		List attendedUserDetails = AnalyticsHelper.getUserAndLectureDetailsForLecture(79290l);
		for(int i = 0; i < attendedUserDetails.size(); i++)
		{
			System.out.println("attendedUserDetails :: "+attendedUserDetails.get(i));
		}
	}
	
}
