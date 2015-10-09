/*
 * 
 */
package edu.amrita.aview.audit.daos;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.audit.entities.AuditLecture;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.utils.HibernateUtils;




/**
 * The Class AuditLectureDAO.
 */
public class AuditLectureDAO extends SuperDAO{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(AuditLectureDAO.class);
	
	/**
	 * Creates the lecture setting.
	 *
	 * @param auditLecture the audit lecture
	 * @throws AViewException
	 */
	public static void createLectureSetting(AuditLecture auditLecture) throws AViewException
	{		
		Session session = null;	
		try {
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.save(auditLecture);
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
	 * Update lecture setting.
	 *
	 * @param auditLecture the audit lecture
	 * @throws AViewException
	 */
	public static void updateLectureSetting(AuditLecture auditLecture) throws AViewException
	{		
		Session session = null;	
		try {
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.update(auditLecture);
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
	 * Gets the all currently logged in guest users.
	 *
	 * @param lectureId the lecture id
	 * @return the all currently logged in guest users
	 * @throws AViewException
	 */
	public static List<Long> getAllCurrentlyLoggedInGuestUsers(long lectureId) throws AViewException
	{
		Session session = null;
		List<Long> userIds = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			Query hqlQuery = session.createQuery(
					"select u.userId from " +
					"AuditLecture al,AuditUserLogin aul,User u " +
					"where al.auditUserLoginid = aul.auditUserLoginId and aul.userId = u.userId " +
					"and u.role = 'GUEST' and al.createdDate = al.modifiedDate and al.lectureId=:lectureId");
			hqlQuery.setLong("lectureId", lectureId);

			userIds = hqlQuery.list();
			if(userIds.size() == 0)
			{
				logger.warn("Warning :: No AuditLectures returned for the given auditLectureId");
			}

		} catch (HibernateException he) {
			processException(he);	
		}
		catch(Throwable e)
		{
			logger.error("Exception while getAllCurrentlyLoggedInGuestUsers for lectureId:"+lectureId , e);
		}
		finally {
			HibernateUtils.closeConnection(session);
		}
		return userIds;
	}

	/**
	 * Gets the audit lecture by id.
	 *
	 * @param auditLectureId the audit lecture id
	 * @return the audit lecture by id
	 * @throws AViewException
	 */
	public static AuditLecture getAuditLectureById(long auditLectureId) throws AViewException{
		Session session = null;
		AuditLecture auditLecture = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			auditLecture = (AuditLecture)session.get(AuditLecture.class, auditLectureId);
//			Query hqlQuery = session.createQuery("select auditLecture from AuditLecture auditLecture where auditLecture.auditLectureId=:auditLectureId");
//			hqlQuery.setLong("auditLectureId", auditLectureId);
//			
//			List <AuditLecture> alLst = hqlQuery.list();
//			if(alLst.size() == 1)
//			{
//				auditLecture = (AuditLecture)(alLst.get(0));
//			}
//			else if(alLst.size() == 0)
//			{
//				logger.warn("Warning :: No AuditLectures returned for the given auditLectureId");
//			}
//			else if(alLst.size() > 1)
//			{
//				logger.warn("Warning :: More than one Audit Lectures returned for the given auditLectureId");
//			}

		} catch (HibernateException he) {
			processException(he);	
		}	
		finally {
			HibernateUtils.closeConnection(session);
		}
		return auditLecture;
	}
	/**
	 * @param lectureId
	 * @return
	 * @throws AViewException
	 */
	//Fix for Bug#20297,20288
	public static boolean getAuditLectureByLectureId(long lectureId) throws AViewException{
		Session session = null;
		boolean result = false;
		List<AuditLecture> auditLectures = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			Query hqlQuery = session.createQuery("select al from AuditLecture al where al.lectureId=:lectureId and al.isModerator='Y'");
			hqlQuery.setLong("lectureId", lectureId);

			auditLectures = hqlQuery.list();
			if(auditLectures.size() == 0)
			{
				logger.warn("Warning :: No AuditLectures returned for the given lectureId");
			}
			else if(auditLectures.size() > 0)
			{
				result = true;
			}
			

		} catch (HibernateException he) {
			processException(he);	
		}
		catch(Throwable e)
		{
			logger.error("Exception while getAuditLecture for lectureId:"+lectureId , e);
		}
		finally {
			HibernateUtils.closeConnection(session);
		}
		return result;
	}

}
