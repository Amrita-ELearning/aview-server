/*
 * 
 */
package edu.amrita.aview.common.daos;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.utils.HibernateUtils;


/**
 * The Class StatusDAO.
 */
public class VersionDAO  extends SuperDAO{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(VersionDAO.class);

	/**
	 * Gets the status id.
	 *
	 * @param statusName the status name
	 * @return the status id
	 * @throws AViewException
	 */
	public static boolean isClientServerCompatible(String clientVersion) throws AViewException 
	{
		Session session = null;
		boolean result = false;
		try
		{
			session = HibernateUtils.getHibernateConnection();
			String sqlQuery = "select v.latest from version v where v.client_version = 'A-VIEW_Classroom-" + clientVersion + "' and v.supported = 1";
			Query q = session.createSQLQuery(sqlQuery);
			List queryResult = q.list();
			if(queryResult.size() == 1)
			{
				result = true;
			}
			else if(queryResult.size() == 0)
			{
				logger.debug("Warning :: No client version entry available ");				
			}
			else if(queryResult.size() > 1)
			{
				logger.debug("Warning :: More than one client version entry available");				
			}
		} 
		catch (HibernateException he) 
		{
			processException(he);	
		}
		finally 
		{
			HibernateUtils.closeConnection(session);
		}
		return result;
	}
}
