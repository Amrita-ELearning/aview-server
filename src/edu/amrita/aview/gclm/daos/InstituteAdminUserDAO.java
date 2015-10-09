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
import edu.amrita.aview.gclm.entities.InstituteAdminUser;



/**
 * The Class InstituteAdminUserDAO.
 */
public class InstituteAdminUserDAO  extends SuperDAO{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(InstituteAdminUserDAO.class);	
	
	/**
	 * Creates the institute admin user.
	 *
	 * @param instituteAdminUser the institute admin user
	 * @throws AViewException
	 */
	public static void createInstituteAdminUser(InstituteAdminUser instituteAdminUser)  throws AViewException{
		Session session = null;
		String creationMessage = null ;
		try {
				session = HibernateUtils.getHibernateConnection();
				session.beginTransaction();	
				session.save(instituteAdminUser);
				session.getTransaction().commit();
		}catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		}finally {
				HibernateUtils.closeConnection(session);
		}
		
	}
	
	/**
	 * Update institute admin user.
	 *
	 * @param instituteAdminUser the institute admin user
	 * @throws AViewException
	 */
	public static void updateInstituteAdminUser(InstituteAdminUser instituteAdminUser)  throws AViewException{
		
		Session session = null;
		String creationMessage = null ;
		try {			
				session = HibernateUtils.getHibernateConnection();
				session.beginTransaction();
				session.update(instituteAdminUser);
				session.getTransaction().commit();
		}catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		}finally {
				HibernateUtils.closeConnection(session);
		}
	}
	
	/**
	 * Gets the institute admin users.
	 *
	 * @return the institute admin users
	 * @throws AViewException
	 */
	public static List<InstituteAdminUser> getInstituteAdminUsers() throws AViewException
	{
		Session session = null;
		List<InstituteAdminUser> instituteAdminUsers = null;
		try {
				session = HibernateUtils.getHibernateConnection();
				Query hqlQuery = session.createQuery("SELECT instAdmin FROM InstituteAdminUser instAdmin");
				instituteAdminUsers =  hqlQuery.list();
			
				if(instituteAdminUsers.size()>0)
				{
					logger.info("Returned instituteAdminUsers ");
				}
				else if(instituteAdminUsers.size() == 0)
				{
					logger.warn("Warning :: No instituteAdminUsers available ");
				}
	
		}catch (HibernateException he) {
			processException(he);	
		}finally {
				HibernateUtils.closeConnection(session);
		}
		return instituteAdminUsers;
			
	}
	
	/**
	 * Gets the institute admin user.
	 *
	 * @param instituteAdminId the institute admin id
	 * @return the institute admin user
	 * @throws AViewException
	 */
	public static InstituteAdminUser getInstituteAdminUser(Long instituteAdminId) throws AViewException
	{
		Session session = null;
		InstituteAdminUser instituteAdminUser = null;
		try {
				session = HibernateUtils.getHibernateConnection();
				Query hqlQuery = session.createQuery("SELECT iau FROM InstituteAdminUser iau WHERE iau.instituteAdminUserId=:instituteAdminId");
				hqlQuery.setLong("instituteAdminId",instituteAdminId);
				instituteAdminUser = (InstituteAdminUser)session.get(InstituteAdminUser.class, instituteAdminId);
		}catch (HibernateException he) {
			processException(he);	
		}finally {
			HibernateUtils.closeConnection(session);
		}
		return instituteAdminUser;

	}

}
