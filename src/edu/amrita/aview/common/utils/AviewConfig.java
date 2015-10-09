/*
 * 
 */
package edu.amrita.aview.common.utils;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import edu.amrita.aview.common.entities.Auditable;
import edu.amrita.aview.licensing.LicenseValidator;

/**
 * The Class AviewConfig.
 */
public class AviewConfig implements ServletContextListener 
{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(AviewConfig.class);
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent event) 
	{
		// TODO Auto-generated method stub
		Auditable.cleanup();
		//logger.debug("*********AVIEW App stopped********");
	}
		
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) 
	{
		// TODO Auto-generated method stub
		logger.debug("*********AVIEW App started**************");
		
		TimerTask timerTask = new LicenseValidator();
		Timer timer = new Timer();
		timer.schedule(timerTask, 1000, 60000);
				
		//Get all the initial data for the application to run
		//and store them in cache
		
		//Bulk DocumentConversion is not being used. So commented. 
		/*		 
		try
		{
			TimerTask timerDocConversionTask =  new DocumentConversionServiceHelper();	
		
			Timer timerDOC = new Timer();
			timerDOC.schedule(timerDocConversionTask, 1000, 30000);		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		*/
		Auditable.getAllInitialData();
		
	}

}
