/*
 * 
 */
package edu.amrita.aview.audit.daos;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.audit.entities.Action;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.utils.HibernateUtils;



/**
 * The Class ActionDAO.
 */
public class ActionDAO extends SuperDAO{

	/** The logger. */
	private static Logger logger = Logger.getLogger(ActionDAO.class);
	
	/**
	 * Gets the action id.
	 *
	 * @param actionName the action name
	 * @return the action id
	 * @throws AViewException
	 */
	public static int getActionId(String actionName) throws AViewException{
		Session session = null;
		int actionId = 0;
		try {
			session = HibernateUtils.getHibernateConnection();
			Query q = session.createQuery("select actionId from Action action where actionName=:actionName");
			q.setString("actionName", actionName);
			if(q.list().size() == 1)
			{
				actionId = Integer.parseInt((q.list().get(0).toString()));
				logger.debug("action id :: "+ actionId);
			}
			else if(q.list().size() == 0)
			{
				logger.warn("Warning :: No status id returned for a given status name ");				
			}
			else if(q.list().size() > 1)
			{
				logger.warn("Warning :: More than one status id returned for a given status name ");				
			}

		} catch (HibernateException he) {
			processException(he);	
		} finally {
			HibernateUtils.closeConnection(session);
		}

		return actionId;
	}
	
	/**
	 * Gets the actions.
	 *
	 * @return the actions
	 * @throws AViewException
	 */
	public static List<Action> getActions() throws AViewException{
		Session session = null;
		List<Action> action = null;

		try {
			session = HibernateUtils.getHibernateConnection();
			Query q = session.createQuery("select c from Action c");
			if(q.list().size() >= 1)
			{
				action =(q.list());

			}
			else if(q.list().size() == 0)
			{
				logger.warn("Warning :: no action returned ");				
			}
			else if(q.list().size() > 1)
			{
				logger.info("Returning actions. Size:"+q.list().size());				
			}

		} catch (HibernateException he) {
			processException(he);	
		} finally {
			HibernateUtils.closeConnection(session);
		}

		return action;
	}
	
}
