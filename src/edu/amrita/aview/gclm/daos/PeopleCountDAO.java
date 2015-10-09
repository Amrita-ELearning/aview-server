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
import edu.amrita.aview.gclm.entities.PeopleCount;


public class PeopleCountDAO  extends SuperDAO
{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(PeopleCountDAO.class);
	

	public static void createPeopleCount(PeopleCount peopleCount) throws AViewException
	{
		Session session = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.save(peopleCount);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		} finally {
			HibernateUtils.closeConnection(session);
		}
	}
	public static List<PeopleCount> getPeopleCount(Long lectureId) throws AViewException{
		Session session = null;
		List peopleCount = null;
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString="select pplcount from PeopleCount pplcount where pplcount.lectureId=:lectureId";
			Query query= session.createQuery(hqlQueryString);
			query.setLong("lectureId",lectureId);
			peopleCount=query.list();
			if(peopleCount.size()>0)
			{
				logger.info("Returned peopleCount ");
			}
			else if(peopleCount.size() == 0)
			{
				logger.warn("Warning :: peopleCount details not available ");
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
		return peopleCount;
	}
}