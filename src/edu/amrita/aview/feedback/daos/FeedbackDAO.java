/*
 * 
 */
package edu.amrita.aview.feedback.daos;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.feedback.entities.Feedback;


/**
 * The Class FeedbackDAO.
 */
public class FeedbackDAO extends SuperDAO {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(FeedbackDAO.class);
	
	/**
	 * Creates the feedback.
	 *
	 * @param feedback the feedback
	 * @throws AViewException
	 */
	public static void createFeedback(Feedback feedback) throws AViewException
	{		
		Session session = null;	
		try {
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.save(feedback);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		}
		finally {
			HibernateUtils.closeConnection(session);
		}
	}

}
