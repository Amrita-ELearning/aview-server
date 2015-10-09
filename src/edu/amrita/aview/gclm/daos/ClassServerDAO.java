/*
 * 
 */
package edu.amrita.aview.gclm.daos;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.gclm.entities.ClassServer;


/**
 * The Class ClassServerDAO.
 */
public class ClassServerDAO  extends SuperDAO{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(ClassServerDAO.class);	
	
	/**
	 * Creates the class server.
	 *
	 * @param classserver the classserver
	 * @throws AViewException
	 */
	public static void createClassServer(ClassServer classserver) throws AViewException
	{
		Session session=null;
		String creationMessage = null ;
		try
		{
			session=HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.save(classserver);
			session.getTransaction().commit();
		}catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		}finally {
		HibernateUtils.closeConnection(session);
		}
	}
	
	/**
	 * Update class server.
	 *
	 * @param classserver the classserver
	 * @throws AViewException
	 */
	public static void updateClassServer(ClassServer classserver) throws AViewException
	{
		Session session=null;
		String creationMessage = null ;
		try
		{
			session=HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.update(classserver);
			session.getTransaction().commit();
		}catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		}finally {
			HibernateUtils.closeConnection(session);
		}
	}
}