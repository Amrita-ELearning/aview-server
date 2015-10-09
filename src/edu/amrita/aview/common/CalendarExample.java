/*
 * 
 */
package edu.amrita.aview.common;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * The Class CalendarExample.
 */
public class CalendarExample {

	/**
	 * The main method.
	 *
	 * @param args the args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
						
		 	GregorianCalendar startCal = new GregorianCalendar(2010,11,22);
		 	GregorianCalendar endCal = new GregorianCalendar(2010,11,30);
		 	GregorianCalendar currCal  = startCal;
		 	Timestamp startDate = new Timestamp(startCal.getTime().getTime());
		 	Date df = startDate;
		 	GregorianCalendar a = new GregorianCalendar() ;
		 	a.setTime(startDate);
		 	//System.out.println(a.getTime());
		 	//Timestamp currDate = startDate ;
		 	while(currCal.before(endCal))
		 	{
		 		//System.out.println(currCal.getTime());
		 		currCal.roll(Calendar.DATE, true) ;		 		
		 	//	currCal.s
		 	}
		    //calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		    //int day = calendar.get(Calendar.DAY_OF_WEEK);
		    
		 	String week = "YYNNNNN" ;
		 	char[] arr = week.toCharArray();
		 	for(int i=1;i<=arr.length ;i++)
		 	{
		 		System.out.println(" week :: i"+i+arr[i]);
		 	}
		 	
	}

}
