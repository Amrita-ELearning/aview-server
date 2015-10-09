/*
 * 
 */
package edu.amrita.aview.common.utils;

import java.sql.Timestamp;


/**
 * The Class AppenderUtils.
 */
public class AppenderUtils {
	
	/**
	 * Delete appender.
	 *
	 * @return the string
	 */
	public static String DeleteAppender()
	{
		//this variable cannot be declared as final as the time stamp needs to change every time
		return ("_DELETED_<" + new Timestamp(System.currentTimeMillis()) + ">");
	}
}
