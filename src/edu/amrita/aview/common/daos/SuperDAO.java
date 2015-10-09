/*
 * 
 */
package edu.amrita.aview.common.daos;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import edu.amrita.aview.common.AViewException;


/**
 * The Class SuperDAO.
 */
public class SuperDAO {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(SuperDAO.class);
	
	/**
	 * Process exception.
	 *
	 * @param he the he
	 * @throws AViewException
	 */
	protected static void processException(HibernateException he) throws AViewException
	{
		String exceptionMessage = null ;
		exceptionMessage = he.getMessage();
		if(he.getCause() != null && he.getCause().getMessage() != null)
		{
			exceptionMessage = he.getCause().getMessage();
		}
		else
		{
			exceptionMessage = he.getMessage();
		}
		logger.error(exceptionMessage,he);
		throw (new AViewException(exceptionMessage));
	}
	

}
