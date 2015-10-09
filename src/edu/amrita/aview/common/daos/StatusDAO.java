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
import edu.amrita.aview.common.entities.Status;
import edu.amrita.aview.common.utils.HibernateUtils;


/**
 * The Class StatusDAO.
 */
public class StatusDAO  extends SuperDAO{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(StatusDAO.class);

	/**
	 * Gets the status id.
	 *
	 * @param statusName the status name
	 * @return the status id
	 * @throws AViewException
	 */
	public static Integer getStatusId(String statusName) throws AViewException {
		Session session = null;
		int statusId = 0;
		try {
			session = HibernateUtils.getHibernateConnection();
			Query q = session.createQuery("select statusId from Status status where statusName=:statusName");
			q.setString("statusName", statusName);
			List temp = q.list();
			if(temp.size() == 1)
			{
				statusId = Integer.parseInt((temp.get(0).toString()));
				logger.debug("status id :: "+statusId);
			}
			else if(temp.size() == 0)
			{
				logger.debug("Warning :: No status id returned for a given status name ");				
			}
			else if(temp.size() > 1)
			{
				logger.debug("Warning :: More than one status id returned for a given status name ");				
			}

		} catch (HibernateException he) {
			processException(he);	
		} finally {
			HibernateUtils.closeConnection(session);
		}

		return statusId;
	}
	
	/**
	 * Gets the statuses.
	 *
	 * @return the statuses
	 * @throws AViewException
	 */
	public static List<Status> getStatuses() throws AViewException
	{
		Session session = null;
		List<Status> status = null;

		try 
		{
			session = HibernateUtils.getHibernateConnection();
			Query q = session.createQuery("select s from Status s order by statusName");
			if(q.list().size() >= 1)
			{
				status =(q.list());

			}
			else if(q.list().size() == 0)
			{
				logger.warn("Warning :: no status returned ");				
			}
			else if(q.list().size() > 1)
			{
				logger.info("Returning status. Size:" + q.list().size());				
			}

		} catch (HibernateException he) {
			processException(he);	
		} finally {
			HibernateUtils.closeConnection(session);
		}

		return status;
	}
}
