/*
 * 
 */
package edu.amrita.aview.audit.daos;


import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.audit.entities.UserAction;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.utils.HibernateUtils;


/**
 * The Class UserActionDAO.
 */
public class UserActionDAO extends SuperDAO{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(AuditUserSettingDAO.class);
	
	
	public static void createDownloadUserAction(UserAction action) throws AViewException
	{		
		Session session = null;	
		try {
			session = HibernateUtils.getHibernateConnection(); //Audit table in the main database.
			session.beginTransaction();
			session.save(action);
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
	 * Creates the user action.
	 *
	 * @param action the action
	 * @throws AViewException
	 */
	public static void createUserAction(UserAction action) throws AViewException
	{		
		Session session = null;	
		try {
			session = HibernateUtils.getAuditHibernateConnection();
			session.beginTransaction();
			session.save(action);
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
	 * Creates the user actions.
	 *
	 * @param actions the actions
	 * @throws AViewException
	 */
	public static void createUserActions(List<UserAction> actions) throws AViewException
	{		
		Session session = null;	
		try {
			session = HibernateUtils.getAuditHibernateConnection();
			session.beginTransaction();
			int counter = 0;
			for(UserAction action:actions)
			{
				session.save(action);
				counter++;
				if(counter % HibernateUtils.HIBERNATE_BATCH_SIZE == 0)
				{
					session.flush();
					session.clear();
				}
			}
			
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
	 * Gets the future license validation count.
	 *
	 * @param currentTime the current time
	 * @param licValId the lic val id
	 * @return the future license validation count
	 * @throws AViewException
	 */
	public static int getFutureLicenseValidationCount(java.sql.Timestamp currentTime,int licValId) throws AViewException
	{
		Session session = null;
		int validationsCount = 0;
		try
		{
			session = HibernateUtils.getAuditHibernateConnection();
			String queryStr = "select count(*) from UserAction ua where ua.actionId = :actionId and ua.createdDate >= :currTime";
			Query query = session.createQuery(queryStr);
			query.setInteger("actionId", licValId);
			query.setTimestamp("currTime", currentTime);
			List list = query.list();
			validationsCount = Integer.parseInt(list.get(0).toString());
		} catch (HibernateException he)
		{
			processException(he);
		}
		finally {
			HibernateUtils.closeConnection(session);
		}
		return validationsCount;
	}
	
	public static Map<Long,Timestamp> getLastActionTimesForLast3Days() throws AViewException
	{
		Session session = null;
		Map<Long,Timestamp> auditLectureMap = new HashMap<Long,Timestamp>();
		try
		{
			session = HibernateUtils.getAuditHibernateConnection(); //Audit table in the audit database.
			String queryStr = 
					"UPDATE audit_lecture al," +
					"(SELECT ua.audit_lecture_id,MAX(ua.created_date) AS last_action_time " +
					"FROM user_action ua " +
					"WHERE ua.audit_lecture_id IS NOT NULL  AND ua.created_date > DATE(NOW())-3 " +
					"GROUP BY ua.audit_lecture_id " +
						"UNION " +
					"SELECT MAX(al.audit_lecture_id) AS audit_lecture_id,uab.last_action_time  " +
					"FROM audit_lecture al," +
						"(	SELECT ua.lecture_id,MAX(ua.created_date) AS last_action_time 	" +
						"FROM user_action ua 	" +
						"WHERE ua.audit_lecture_id IS NULL AND ua.lecture_id IS NOT NULL 	" +
						"AND ua.created_date > DATE(NOW())-3 	" +
						"GROUP BY ua.lecture_id) uab " +
					"WHERE al.lecture_id = uab.lecture_id " +
					"GROUP BY al.lecture_id) b" +
					" SET al.last_action_date = b.last_action_date WHERE al.audit_lecture_id = b.audit_lecture_id";
			Query query = session.createSQLQuery(queryStr);
			List<Object[]> list = query.list();
			for(Object[] obj:list)
			{
				auditLectureMap.put((Long)obj[0], (Timestamp)obj[1]);
			}
		} catch (HibernateException he)
		{
			processException(he);
		}
		finally {
			HibernateUtils.closeConnection(session);
		}
		return auditLectureMap;
	}
	
	private static void printAvailableAuditLecturesForUpdate(Session session) throws AViewException
	{
		String queryStr = 
				"SELECT ua.audit_lecture_id,MAX(ua.created_date) AS last_action_date " +
				"FROM aview_audit.user_action ua " +
				"WHERE ua.audit_lecture_id IS NOT NULL  " +
				"AND ua.created_date > DATE(NOW())-3 " +
				"GROUP BY ua.audit_lecture_id " +
					"UNION " +
				"SELECT MAX(al.audit_lecture_id) AS audit_lecture_id,uab.last_action_date  " +
				"FROM audit_lecture al," +
					"(	SELECT ua.lecture_id,ua.created_by_user_id,MAX(ua.created_date) AS last_action_date 	" +
					"FROM aview_audit.user_action ua 	" +
					"WHERE ua.audit_lecture_id IS NULL AND ua.lecture_id IS NOT NULL 	" +
					"AND ua.created_date > DATE(NOW())-3 	" +
					"GROUP BY ua.lecture_id) uab " +
				"WHERE al.lecture_id = uab.lecture_id  AND al.created_by_user_id = uab.created_by_user_id " +
				"GROUP BY al.lecture_id";
		Query query = session.createSQLQuery(queryStr);
		List list = query.list();
		System.out.println(list.size());
	}
	
	
	private static void printUpdatedAuditLectureCount(Session session) throws AViewException
	{
		String queryStr = "SELECT COUNT(*) FROM aview.audit_lecture al WHERE al.last_action_date IS NOT NULL";
		Query query = session.createSQLQuery(queryStr);
		List list = query.list();
		System.out.println(list.get(0).toString());
	}

	
	public static void updateLastActionTimesForLast3Days() throws AViewException
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getAuditBatchHibernateConnection(); //Audit table in the audit database.
//			setDatabase(session);
			session.beginTransaction();

//			printAvailableAuditLecturesForUpdate(session);
//			printUpdatedAuditLectureCount(session);
			HibernateUtils.setDatabase(session);
			String queryStr = 
					"UPDATE audit_lecture al," +
					"(" +
						"SELECT ot.audit_lecture_id,MAX(ot.last_action_date) AS last_action_date FROM" +
						"(" +
							"SELECT ua.audit_lecture_id,MAX(ua.created_date) AS last_action_date " +
							"FROM aview_audit.user_action ua " +
							"WHERE ua.audit_lecture_id IS NOT NULL  " +
							"AND ua.created_date > DATE(NOW())-3 " +
							"GROUP BY ua.audit_lecture_id " +
								"UNION " +
							"SELECT MAX(al.audit_lecture_id) AS audit_lecture_id,uab.last_action_date  " +
							"FROM audit_lecture al," +
								"(	SELECT ua.lecture_id,ua.created_by_user_id,MAX(ua.created_date) AS last_action_date 	" +
								"FROM aview_audit.user_action ua 	" +
								"WHERE ua.audit_lecture_id IS NULL AND ua.lecture_id IS NOT NULL 	" +
								"AND ua.created_date > DATE(NOW())-3 	" +
								"GROUP BY ua.lecture_id) uab " +
							"WHERE al.lecture_id = uab.lecture_id  AND al.created_by_user_id = uab.created_by_user_id " +
							"GROUP BY al.lecture_id" +
						") ot " +
						"GROUP BY ot.audit_lecture_id" +
					") b" +
					" SET al.last_action_date = b.last_action_date WHERE al.audit_lecture_id = b.audit_lecture_id";
			Query query = session.createSQLQuery(queryStr);
			query.executeUpdate();
			session.getTransaction().commit();
//			printUpdatedAuditLectureCount(session);
		} catch (HibernateException he) {
			processException(he);
			session.getTransaction().rollback();
		}
		finally {
			HibernateUtils.closeConnection(session);
		}
	}
}
