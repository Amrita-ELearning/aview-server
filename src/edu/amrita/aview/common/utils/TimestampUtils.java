/*
 * 
 */
package edu.amrita.aview.common.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.ws.rs.core.Response;

import com.sun.jersey.api.client.ClientResponse.Status;

import edu.amrita.aview.common.AViewException;

/**
 * The Class TimestampUtils.
 */
public class TimestampUtils {
	
	/**
	 * Gets the current timestamp.
	 *
	 * @return the current timestamp
	 */
	public static Timestamp getCurrentTimestamp()
	{
		return (new Timestamp(System.currentTimeMillis()));
	}

	/**
	 * Gets the current timestamp.
	 *
	 * @param offset the offset
	 * @return the current timestamp
	 */
	public static Timestamp getCurrentTimestamp(long offset)
	{
		return (new Timestamp(System.currentTimeMillis()+offset));
	}

	/**
	 * Gets the current date.
	 *
	 * @return the current date
	 */
	public static Date getCurrentDate()
	{
		return new Date(System.currentTimeMillis());
	}

	/**
	 * Removes the time.
	 *
	 * @param dateTime the date time
	 * @return the date
	 */
	public static java.util.Date removeTime(java.util.Date dateTime)
	{
		return removeTimeCalendar(dateTime).getTime();
	}

	public static GregorianCalendar removeTimeCalendar(java.util.Date dateTime)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(dateTime);		
		gc.set(GregorianCalendar.MINUTE, 0);
		gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
		gc.set(GregorianCalendar.SECOND, 0);
		gc.set(GregorianCalendar.MILLISECOND, 0);		
		return gc;		
	}
	
	/**
	 * Removes the date and milliseconds
	 *
	 * @param dateTime the date time
	 * @return the date
	 */
	public static java.util.Date removeDateAndMillis(java.util.Date dateTime)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(dateTime);
		
		gc.set(GregorianCalendar.DAY_OF_MONTH, 0);
		gc.set(GregorianCalendar.YEAR, 0);
		gc.set(GregorianCalendar.MONTH, 0);
		gc.set(GregorianCalendar.MILLISECOND, 0);
		return gc.getTime();
	}

	
	/**
	 * Adds the time to date.
	 *
	 * @param date the date
	 * @param time the time
	 * @param adjustedMinutes the adjusted minutes
	 * @return the date
	 */
	public static java.util.Date addTimeToDate(java.util.Date date,java.util.Date time,int adjustedMinutes)
	{
		GregorianCalendar gcDate = new GregorianCalendar();
		gcDate.setTime(date);
		
		GregorianCalendar gcTime = new GregorianCalendar();
		gcTime.setTime(time);
		
		gcDate.set(GregorianCalendar.MINUTE, gcTime.get(GregorianCalendar.MINUTE));
		gcDate.set(GregorianCalendar.HOUR_OF_DAY, gcTime.get(GregorianCalendar.HOUR_OF_DAY));
		gcDate.set(GregorianCalendar.SECOND, gcTime.get(GregorianCalendar.SECOND));
		
		gcDate.add(GregorianCalendar.MINUTE, adjustedMinutes);
		
		return gcDate.getTime();
	}
	
	/**
	 * Str to date.
	 *
	 * @param strDate the str date
	 * @param delimiter the delimiter
	 * @param dateIndex the date index
	 * @param monthIndex the month index
	 * @param yearIndex the year index
	 * @return the date
	 */
	public static java.util.Date strToDate(String strDate, String delimiter, int dateIndex, int monthIndex, int yearIndex)
	{
		GregorianCalendar gcDate = new GregorianCalendar();		
		String[] splittedDate = strDate.split(delimiter);
		gcDate.set(Calendar.DATE, Integer.parseInt(splittedDate[dateIndex]));
		// do a minus one as January is 0...December is 11 for GC
		gcDate.set(Calendar.MONTH, Integer.parseInt(splittedDate[monthIndex]) - 1);
		gcDate.set(Calendar.YEAR, Integer.parseInt(splittedDate[yearIndex]));		
		return gcDate.getTime();
	}
	
}
