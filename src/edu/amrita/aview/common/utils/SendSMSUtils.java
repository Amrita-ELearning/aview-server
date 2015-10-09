/*
 * 
 */
package edu.amrita.aview.common.utils;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.javacodegeeks.kannel.api.SMSManager;


/**
 * The Class SendSMSUtils.
 */
public class SendSMSUtils 
{ 
	
	/** The sms server ip. */
	private static String smsServerIP;
	
	/** The sms server port. */
	private static String smsServerPort;
	
	/** The sms server user. */
	private static String smsServerUser;
	
	/** The sms server password. */
	private static String smsServerPassword;
	
	/** The sms sending mob num. */
	private static String smsSendingMobNum;
	
	/** The sms manager. */
	private static SMSManager smsManager;

	static
	{
		try
		{
			Properties prop = new Properties();		
			prop.load(SendSMSUtils.class.getClassLoader().getResourceAsStream("sms.properties"));
			smsServerIP = prop.getProperty("sms.ip");
			smsServerPort = prop.getProperty("sms.port");
			smsServerUser = prop.getProperty("sms.userName");
			smsServerPassword = prop.getProperty("sms.password");
			smsSendingMobNum = prop.getProperty("sms.phoneNumber");
			smsManager = SMSManager.getInstance();
			smsManager.setMessagesPrefetchSize(Integer.valueOf(prop.getProperty("sms.messagesPrefetchSize")));
			smsManager.setMessagesSendRate(Integer.valueOf(prop.getProperty("sms.messagesSendRate")));
		}
		catch (IOException ioe)
		{
			// processException(ioe);
		}
	}
	
	/**
	 * Send sms.
	 *
	 * @param mobile the mobile
	 * @param msg the msg
	 */
	public static void sendSMS (List<String> mobile,String msg) 
	{
		try
		{
			smsManager.sendSMS(smsServerIP, smsServerPort, smsServerUser, smsServerPassword, smsSendingMobNum, mobile, msg);
		}
		catch (Exception e)
		{
			e. printStackTrace();	
		}
		
	}
} 
