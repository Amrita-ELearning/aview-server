/*
 * 
 */
package edu.amrita.aview.gclm.daos;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.gclm.entities.ServerType;


/**
 * The Class ServerTypeDAO.
 */
public class ServerTypeDAO  extends SuperDAO{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(ServerTypeDAO.class);
	
	/**
	 * Gets the all server types.
	 *
	 * @return the all server types
	 * @throws AViewException
	 */
	public static List<ServerType> getAllServerTypes()  throws AViewException{
		Session session = null;
		List<ServerType> serverTypes = null;
		try {
				session = HibernateUtils.getHibernateConnection();
				Query hqlQuery = session.createQuery("SELECT serverType FROM ServerType serverType");
				serverTypes =  hqlQuery.list();
				if(serverTypes.size()>0)
				{
					logger.debug("Returned instituteCategories ");
				}
				else if(serverTypes.size() == 0)
				{
					logger.debug("Warning :: No institute category ");
				}
		
		}catch (HibernateException he) {
			processException(he);	
		}finally {
				HibernateUtils.closeConnection(session);
		}
		return serverTypes;
	}

}