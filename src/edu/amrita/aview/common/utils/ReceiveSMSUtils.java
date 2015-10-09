/*
 * 
 */
package edu.amrita.aview.common.utils;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javacodegeeks.kannel.api.SMSPushRequestException;

import edu.amrita.aview.evaluation.helpers.MobileQuizHelper;


/**
 * MQAs.
 */
public class ReceiveSMSUtils extends HttpServlet 
{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The Constant MAXNOSIMSMS. */
	static final int MAXNOSIMSMS= 200;
	
	/** The mobile numbers. */
	String mobileNumbers;
	
	/** The rcvd answers. */
	String rcvdAnswers;
	
	/** The rcvd date time. */
	String rcvdDateTime;	
	
	/**
	 * Parses the result.
	 *
	 * @param result the result
	 * @param index the index
	 */
	public void parseResult(String result, int index)
	{
		try
		{		  
			String tmpMob = result.substring(3, 13);
			String tmpDT = result.substring(14, 31);
			String rcvdAns = result.substring(36);
			if(!(rcvdAns.length() > 4))
			{
				mobileNumbers += tmpMob + "~";
				rcvdDateTime += tmpDT + "~";
				rcvdAnswers += rcvdAns + "~";
			}
		}
		catch(Exception e)
		{
			//System.out.println("Exception in parse String " + e);
		}
	}
	//	End parseResult
	
	//	Doget parseResult
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{				  		 
		//System.out.println("Incoming text is " + request.getParameter("text"));
		//System.out.println("Incoming number is " + request.getParameter("phone"));
		String receivedMessage = request.getParameter("text");
		String senderMobileNumber = request.getParameter("phone");
		try 
		{				
			MobileQuizHelper.getMobileQuizAnswerChoice(receivedMessage, senderMobileNumber);
		}
		catch (SMSPushRequestException e)
		{ 		
			e.printStackTrace();
		} 
		catch (Exception e) 
		{	
			e.printStackTrace();
		}				  		
	}	
} 