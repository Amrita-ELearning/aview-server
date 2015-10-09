/**
 * 
 */
package edu.amrita.aview.common.daos;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.entities.State;
import edu.amrita.aview.common.utils.HibernateUtils;


/**
 * The Class StateDAO.
 *
 * @author
 */
public class StateDAO  extends SuperDAO{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(StateDAO.class);

	/**
	 * Gets the states.
	 *
	 * @param statusId the status id
	 * @return the states
	 * @throws AViewException
	 */
	public static List<State> getStates(Integer statusId) throws AViewException
	{
		Session session = null;
		List<State> states = null;
		try {
				session = HibernateUtils.getHibernateConnection();
				Query hqlQuery = session.createQuery("SELECT s FROM State s where s.statusId = :statusId");
				
				hqlQuery.setInteger("statusId", statusId);
				
				states =  hqlQuery.list();			
				if(states.size()>0)
				{
					logger.debug("Returned states ");
				}
				else if(states.size() == 0)
				{
					logger.debug("Warning :: No states ");
				}
		}catch (HibernateException he) {
			processException(he);	
		}finally {
				HibernateUtils.closeConnection(session);
		}
		return states;
	}

}
