/*
 * 
 */
package edu.amrita.aview.audit.helpers;


import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import edu.amrita.aview.audit.daos.ActionDAO;
import edu.amrita.aview.audit.entities.Action;
import edu.amrita.aview.common.AViewException;


/**
 * The Class ActionHelper.
 */
public class ActionHelper {

	/** The logger. */
	private static Logger logger = Logger.getLogger(ActionHelper.class);
	
	/** The wait time in milli seconds. */
	private static String waitTimeInMilliSeconds = "";
	
	static 
	{
		Properties properties = new Properties();
		try 
		{
			properties.load(ActionHelper.class.getClassLoader().getResourceAsStream("mysql.properties"));			
			waitTimeInMilliSeconds = properties.getProperty("action.waitTime");
		}
		catch(IOException ioe)
		{
			
		}
	}
			
	
	/**
	 * Gets the action id.
	 *
	 * @param actionName the action name
	 * @return the action id
	 * @throws AViewException
	 */
	public static Integer getActionId(String actionName) throws AViewException
	{
		return ActionDAO.getActionId(actionName);
	}
	
	/**
	 * Gets the actions.
	 *
	 * @return the actions
	 * @throws AViewException
	 */
	public static List<Action> getActions() throws AViewException
	{
		try
		{
			Thread.currentThread();
			Thread.sleep(Long.parseLong(waitTimeInMilliSeconds));
		}
		catch(Exception e)
		{
			
		}
		return ActionDAO.getActions();
	}

}
