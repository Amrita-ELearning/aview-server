/*
 * 
 */
package edu.amrita.aview.audit.daos;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.audit.entities.AuditUserLogin;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.utils.HibernateUtils;



/**
 * The Class AuditUserLoginDAO.
 */
public class AuditUserLoginDAO extends SuperDAO{

	/** The logger. */
	private static Logger logger = Logger.getLogger(AuditUserLoginDAO.class);
	
	/**
	 * Creates the user login.
	 *
	 * @param userLogin the user login
	 * @throws AViewException
	 */
	public static void createUserLogin(AuditUserLogin userLogin) throws AViewException
	{		
		Session session = null;	
		try {
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.save(userLogin);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		}
		finally {
			HibernateUtils.closeConnection(session);
		}
	}
	
	/**
	 * Gets the user login.
	 *
	 * @param loginTime the login time
	 * @param userId the user id
	 * @return the user login
	 * @throws AViewException
	 */
	public static AuditUserLogin getUserLogin(Timestamp loginTime,int userId) throws AViewException{
		Session session = null;
		AuditUserLogin userLogin = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			Query hqlQuery = session.createQuery("select c from AuditUserLogin c where c.userId=:userId and c.loginTime=:loginTime");
			hqlQuery.setInteger("userId", userId);
			hqlQuery.setTimestamp("loginTime", loginTime);
			//Code change to perform the query operation only once and parse the result
			List<AuditUserLogin> aulList = hqlQuery.list();
			if(aulList.size() == 1)
			{
				userLogin = aulList.get(0);
			}
			else if(aulList.size() == 0)
			{
				logger.warn("Warning :: No user returned for the given auditUserLogin id ");
			}
			else if(aulList.size() > 1 )
			{
				logger.warn("Warning :: More than one user returned for the given auditUserLogin id ");
			}

		} catch (HibernateException he) 
		{
			processException(he);	
		}	
		finally 
		{
			HibernateUtils.closeConnection(session);
		}
		return userLogin;
	}

	/**
	 * Gets the user login by id.
	 *
	 * @param auditUserLoginId the audit user login id
	 * @return the user login by id
	 * @throws AViewException
	 */
	public static AuditUserLogin getUserLoginById(long auditUserLoginId) throws AViewException{
		Session session = null;
		AuditUserLogin userLogin = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			userLogin = (AuditUserLogin)session.get(AuditUserLogin.class, auditUserLoginId);

		} catch (HibernateException he) {
			processException(he);	
		}	
		finally {
			HibernateUtils.closeConnection(session);
		}
		return userLogin;
	}

	
	/**
	 * Update user login.
	 *
	 * @param userLogin the user login
	 * @throws AViewException
	 */
	public static void updateUserLogin(AuditUserLogin userLogin) throws AViewException{
		Session session = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.update(userLogin);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		} finally {
			HibernateUtils.closeConnection(session);
		}
	}
	
	/**
	 * Gets the user login count for lecture.
	 *
	 * @param lectureId the lecture id
	 * @return the user login count for lecture
	 * @throws AViewException
	 */
	public static Integer getUserLoginCountForLecture(Long lectureId) throws AViewException
	{
		Session session = null;
		HashSet<AuditUserLogin> userLogins = null;
		HashSet<Long> userIds = new HashSet<Long>();
		int distinctUserLoginCount = 0;
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			Query hqlQuery = session.createQuery("select aul from AuditUserLogin aul, AuditLecture al, Lecture l where " +
												 "al.lectureId = l.lectureId and l.lectureId = :lectureId and " +
												 "aul.auditUserLoginId = al.auditUserLoginid");
			hqlQuery.setLong("lectureId", lectureId);
			userLogins = new HashSet<AuditUserLogin>(hqlQuery.list());
			for(AuditUserLogin aul : userLogins)
			{
				userIds.add(aul.getUserId());
			}
			distinctUserLoginCount = userIds.size();
		} 
		catch (HibernateException he) 
		{
			processException(he);	
		}
		finally {
			HibernateUtils.closeConnection(session);
		}
		return distinctUserLoginCount;
	}		
}
