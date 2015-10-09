/*
 * 
 */
package edu.amrita.aview.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import edu.amrita.aview.common.AViewException;

/**
 * A simple email sender class.
 */
public class SimpleEmailSenderUtils implements Runnable
{
	/**
	 * Main method to send a message given on the command line.
	 */

	private static Logger logger = Logger.getLogger(SimpleEmailSenderUtils.class);
	
	private static List<Message> emailsInQueue = new ArrayList<Message>();
	private static final long WAIT_MS = 1000;
	
	/** The email from. */
	private static String emailFrom = "";
	private static String emailServer = "";
	private static boolean stopFlag = false;

	static
	{
		Properties getEmailProps = new Properties();
		try
		{
			getEmailProps.load(SimpleEmailSenderUtils.class.getClassLoader().getResourceAsStream("sendemail.properties"));		  
			emailServer = getEmailProps.getProperty("sendemail.server");
			emailFrom = getEmailProps.getProperty("sendemail.from");
			SimpleEmailSenderUtils emailSender = new SimpleEmailSenderUtils();
			Thread insertThread = new Thread(emailSender);
			insertThread.start();
			logger.info("SimpleEmailSenderUtils thread Started");
		}
		catch(Exception e)
		{

		}	    
	}

	/**
	 * addEmailToQueue.
	 *
	 * @param toEmailId the to email id
	 * @param emailBody the email body
	 * @param emailSubject the email subject
	 * @return the string
	 */
	public static synchronized void addEmailToQueue(String toEmailId, String emailBody, String emailSubject)
	{
		try 
		{
			Properties sendEmailProps = new Properties();
			sendEmailProps.put("mail.smtp.host", emailServer);
			Session mailConnection = Session.getInstance(sendEmailProps, null);
			Message msg = new MimeMessage(mailConnection);
			Address From = new InternetAddress(emailFrom);
			Address To = new InternetAddress(toEmailId);
			msg.setContent(emailBody, "text/plain");
			msg.setFrom(From);
			msg.setRecipient(Message.RecipientType.TO, To);
			msg.setSubject(emailSubject);
			emailsInQueue.add(msg);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static void addEmailToQueue(List<String> toEmailIds, String emailBody, String emailSubject)
	{
		for(String toEmailId : toEmailIds)
		{
			addEmailToQueue(toEmailId, emailBody, emailSubject);
		}
	}
	
	private static synchronized List<Message> getEmailsFromQueue() throws AViewException
	{
		List<Message> emailsToSend = new ArrayList<Message>();
		emailsToSend.addAll(emailsInQueue);
		emailsInQueue.clear();
		return emailsToSend;
	}
	
	public void run()
	{
		long lastInserTime = System.currentTimeMillis();
		while(!stopFlag)
		{
			//Insert if the events are waited enough time
			if((System.currentTimeMillis() - lastInserTime) > WAIT_MS && emailsInQueue.size() > 0)
			{
				try 
				{
					List<Message> emailsToSend = getEmailsFromQueue();
					dispatchEmail(emailsToSend);
					emailsToSend.clear();
					lastInserTime = System.currentTimeMillis();
				} 
				catch (AViewException e) 
				{
					logger.error("Error while inserting emails from Queue", e);
				}
			}
			try
			{
				Thread.sleep(1000); //Wait for 1 second before checking again 
			}catch(InterruptedException ignore){};
		}
	}
	
	private static void dispatchEmail(List<Message> emailsToSend)
	{
		try
		{
			for(Message message : emailsToSend)
			{
				logger.debug("Coming inside dispatch thread with email size: " + emailsToSend.size());
				Transport.send(message);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static void stopThread() throws AViewException
	{
		stopFlag = true;
		if(emailsInQueue.size() > 0)
		{
			List<Message> emailsToSend = getEmailsFromQueue();
			dispatchEmail(emailsToSend);
			logger.debug("Sending :"+ emailsToSend.size()+": Emails before undeploying the Aview App********");
			emailsToSend.clear();
			emailsToSend = null;
		}
	}
}