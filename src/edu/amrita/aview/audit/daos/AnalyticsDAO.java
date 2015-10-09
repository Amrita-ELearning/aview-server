package edu.amrita.aview.audit.daos;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.utils.HibernateUtils;

public class AnalyticsDAO extends SuperDAO {
	private static Logger logger = Logger.getLogger(AnalyticsDAO.class);
	
	public static List getUserLoginLogoutbyLectureId(Long lectureId,Long userId,Integer statusId) throws AViewException
	{		
		Session session = null;
		List logDetails = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "Select u.userId,u.userName,MIN(aul.loginTime),MIN(al.createdDate)," +
									"MAX(al.lastActionDate),MAX(aul.logOutTime) " +
									"FROM AuditLecture al,AuditUserLogin aul," +
									"User u,Institute i,Lecture l  WHERE " +
									"u.instituteId = i.instituteId AND u.userId = aul.userId " +
									"AND aul.auditUserLoginId = al.auditUserLoginid AND " +
									"al.lectureId = l.lectureId " +
									"AND u.statusId=:statusId AND i.statusId=:statusId " +
									"AND l.statusId=:statusId AND l.lectureId =:lectureId " ;
			if(userId != 0l)
			{
				hqlQueryString += "AND u.userId= :userId " ;
			}
			hqlQueryString += "GROUP BY l.lectureId,u.userId";
			/*String hqlQueryString = "Select u,aul,al FROM AuditLecture al,AuditUserLogin aul," +
									"User u,Institute i,Lecture l  WHERE " +
									"u.instituteId = i.instituteId AND u.userId = aul.userId " +
									"AND aul.auditUserLoginId = al.auditUserLoginid AND " +
									"al.lectureId = l.lectureId " +
									"AND u.statusId=:statusId AND i.statusId=:statusId " +
									"AND l.statusId=:statusId AND l.lectureId =:lectureId " +
									"GROUP BY l.lectureId,u.userId";*/
			Query hqlQuery = session.createQuery(hqlQueryString);		
			hqlQuery.setLong("lectureId", lectureId);
			if(userId != 0l)
			{
				hqlQuery.setLong("userId", userId);
			}
			hqlQuery.setInteger("statusId", statusId);
			logDetails = hqlQuery.list();			
		} catch (HibernateException he) {
			processException(he);	
		}
		finally {
			HibernateUtils.closeConnection(session);
		}
		return logDetails;		
	}
	
	public static List getUserInteractionDetailsbyLectureId(Long lectureId,Long userId,Integer statusId) throws AViewException
	{		
		List userInteractionDetails = null;
		Session session = null;
		try
		{
			session = HibernateUtils.getAuditBatchHibernateConnection(); //Audit table in the audit database.
			session.beginTransaction();
			HibernateUtils.setDatabase(session);
			String queryStr = "SELECT interactUser.user_id,interactUser.user_name,a.action_name, " + 
					 		  " ua.created_date,i.institute_name " +
							  " FROM aview.user userModerator,aview.institute i,aview.lecture l,aview.action a, " +
							  " aview_audit.user_action ua,aview.audit_user_login aul, " +
							  " aview.audit_lecture al, " +
							  " aview.user interactUser WHERE " + 
							  " ua.audit_user_login_id = aul.audit_user_login_id" +
							  " AND al.audit_user_login_id = aul.audit_user_login_id " +
							  " AND al.lecture_id = l.lecture_id  " +
							  " AND aul.user_id = userModerator.user_id " +
							  " AND interactUser.institute_id = i.institute_id " + 
							  " AND ua.action_id = a.action_id  " +
							  " AND a.action_name IN ('Interacting','InteractionEnded') " + 
							  " AND interactUser.user_name=ua.attr1_value " +
							  " AND userModerator.status_id = "+ statusId +" AND i.status_id = "+ statusId +
							  " AND l.status_id = "+ statusId +" AND a.status_id = "+ statusId +
							  " AND ua.status_id ="+ statusId +" AND aul.status_id = "+ statusId +
							  " AND al.status_id = "+ statusId +" AND l.lecture_id = "+ lectureId +
							  " AND interactUser.status_id = "+ statusId ;
			if(userId != 0l)
			{
				queryStr += " AND interactUser.user_id = " + userId  ;
			}
			queryStr += " ORDER BY interactUser.user_id";				  
			
			/*String queryStr = "SELECT interactUser.user_id,interactUser.user_name, " + 
							  " userModerator.user_id, userModerator.user_name," +
							  " ua.created_date,i.institute_name " +
							  " FROM aview.user userModerator,aview.institute i,aview.lecture l,aview.action a, " +
							  " aview_audit.user_action ua,aview.audit_user_login aul, " +
							  " aview.audit_lecture al, " +
							  " aview.user interactUser WHERE " + 
							  " ua.audit_user_login_id = aul.audit_user_login_id " + 
							  " AND al.audit_user_login_id = aul.audit_user_login_id " +
							  " AND al.lecture_id = l.lecture_id  " +
							  " AND aul.user_id = userModerator.user_id " + 
							  " AND interactUser.institute_id = i.institute_id " + 
							  " AND ua.action_id = a.action_id  " +
							  " AND a.action_name IN ('Interacting') " + 
							  " AND interactUser.user_name=ua.attr1_value " +
							  " AND userModerator.status_id = "+ statusId +" AND i.status_id = "+ statusId +
							  " AND l.status_id = "+ statusId +" AND a.status_id = "+ statusId +
							  " AND ua.status_id ="+ statusId +" AND aul.status_id = "+ statusId +
							  " AND al.status_id = "+ statusId +" AND l.lecture_id = "+ lectureId +
							  " AND interactUser.status_id = "+ statusId +
							  " ORDER BY interactUser.user_id";*/
			Query query = session.createSQLQuery(queryStr);
			userInteractionDetails = query.list();	
		} catch (HibernateException he) {
			processException(he);
			session.getTransaction().rollback();
		}
		finally {
			HibernateUtils.closeConnection(session);
		}
		return userInteractionDetails;
	}
	
	public static List getQuestionDetailsbyLectureId(Long lectureId,Integer statusId) throws AViewException
	{		
		List questionDetails = null;
		Session session = null;
		try
		{
			session = HibernateUtils.getAuditBatchHibernateConnection(); //Audit table in the audit database.
			session.beginTransaction();
			HibernateUtils.setDatabase(session);
			String queryStr = 	"SELECT SUBSTRING(ua.attr1_value,POSITION(':' IN ua.attr1_value)+2)," +
								"u.user_name,a.action_name,ua.created_date,ua.attr2_value FROM " +
								"aview.action a,aview.audit_lecture al,aview.audit_user_login aul," +
								"aview.user u,aview.lecture l,aview_audit.user_action ua " +
								"WHERE l.lecture_id = al.lecture_id " +
								"AND aul.audit_user_login_id = al.audit_user_login_id " +
								"AND aul.user_id = u.user_id " +
								"AND ua.audit_lecture_id = al.audit_lecture_id " +
								"AND a.action_id = ua.action_id " +
								"AND a.action_name IN ('QuestionAsk', 'QuestionAnswer', 'QuestionVote') " +
								"AND l.lecture_id ="+ lectureId +" AND u.status_id="+ statusId +"  " +
								"AND l.status_id="+ statusId +" AND a.status_id = "+ statusId +" " +
								"AND ua.status_id ="+ statusId+" AND aul.status_id = "+ statusId +" " +
								"AND al.status_id = "+ statusId + ";";
			Query query = session.createSQLQuery(queryStr);
			questionDetails = query.list();	

		} catch (HibernateException he) {
			processException(he);
			session.getTransaction().rollback();
		}
		finally {
			HibernateUtils.closeConnection(session);
		}
		return questionDetails;
	}
	
	public static List getConnectionDetailsbyLectureId(Long lectureId,Integer statusId) throws AViewException
	{		
		List reconnectionDetails = null;
		Session session = null;
		try
		{
			session = HibernateUtils.getAuditBatchHibernateConnection(); //Audit table in the audit database.
			session.beginTransaction();
			HibernateUtils.setDatabase(session);
			String queryStr ="SELECT u.user_name,a.action_name,ua.created_date FROM " +
							 "aview.user u,aview.institute i,aview.lecture l,aview.action a," +
							 "aview_audit.user_action ua,aview.audit_user_login aul,aview.audit_lecture al " +
							 "WHERE ua.audit_user_login_id = aul.audit_user_login_id " +
							 "AND al.audit_user_login_id=aul.audit_user_login_id " +
							 "AND al.lecture_id=l.lecture_id AND aul.user_id = u.user_id " +
							 "AND u.institute_id = i.institute_id AND ua.action_id =a.action_id	" +
							 "AND a.action_name IN('ConnectionReject','ConnectionFail','ConnectionSuccess') " +
							 "AND l.lecture_id = "+ lectureId +" AND u.status_id = "+ statusId +
							 " AND l.status_id = "+ statusId +" AND a.status_id = "+ statusId +
							 " AND ua.status_id = "+ statusId +" AND aul.status_id = "+ statusId +
							 " AND al.status_id = "+ statusId +" GROUP BY u.user_id,ua.created_date";
			Query query = session.createSQLQuery(queryStr);
			reconnectionDetails = query.list();	

		} catch (HibernateException he) {
			processException(he);
			session.getTransaction().rollback();
		}
		finally {
			HibernateUtils.closeConnection(session);
		}
		return reconnectionDetails;
	}
	
	public static List getLiveSessionsByAdmin(Long adminId,Integer statusId) throws AViewException
	{		
		List liveLectures = null;
		Session session = null;
		try
		{
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "SELECT l FROM Lecture l,Class cl,Course c,Institute inst," +
								    "InstituteAdminUser instAdmin,User u WHERE " +
								    "l.classId = cl.classId AND cl.courseId = c.courseId " +
								    "AND c.instituteId = inst.instituteId " +
								    "AND u.userId = instAdmin.user.userId " +
								    "AND TIME(NOW()) BETWEEN l.startTime AND l.endTime AND l.startDate = DATE(NOW()) " +
								    "AND l.statusId =:statusId AND cl.statusId =:statusId " +
								    "AND c.statusId =:statusId AND inst.statusId =:statusId " +
								    "AND instAdmin.statusId =:statusId AND u.statusId =:statusId ";
			if((adminId != null) && (adminId != 0l))
			{
				hqlQueryString += "AND u.userId = :adminId " ;
			}
			Query hqlQuery = session.createQuery(hqlQueryString);		

			if(adminId != 0l)
			{
				hqlQuery.setLong("adminId", adminId);
			}	
			hqlQuery.setInteger("statusId", statusId);
			liveLectures = hqlQuery.list();	

		} catch (HibernateException he) {
			processException(he);
		}
		finally {
			HibernateUtils.closeConnection(session);
		}
		return liveLectures;
	}

	public static List getAttendingUsersbyLectureId(Long lectureId,Integer statusId) throws AViewException
	{		
		List attendingUserDetails = null;
		Session session = null;
		try
		{
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "SELECT u FROM Lecture l,User u,AuditLecture al,AuditUserLogin aul " +
									"WHERE al.lectureId = l.lectureId " +
									"AND al.auditUserLoginid = aul.auditUserLoginId	" +
									"AND aul.userId = u.userId AND l.statusId = :statusId " +
									"AND aul.statusId = :statusId AND al.statusId = :statusId " +
									"AND u.statusId = :statusId AND l.lectureId = :lectureId " +
									"GROUP BY u.userId" ;
			Query hqlQuery = session.createQuery(hqlQueryString);		
			hqlQuery.setLong("lectureId", lectureId);
			hqlQuery.setInteger("statusId", statusId);
			attendingUserDetails = hqlQuery.list();	

		} catch (HibernateException he) {
			processException(he);
		}
		finally {
			HibernateUtils.closeConnection(session);
		}		
		return attendingUserDetails;
	}
	
	public static List getUserChatDetailsbyLectureId(Long lectureId,Integer statusId) throws AViewException
	{		
		List userChatDetails = null;
		Session session = null;
		try
		{
			session = HibernateUtils.getAuditBatchHibernateConnection(); //Audit table in the audit database.
			session.beginTransaction();
			HibernateUtils.setDatabase(session);
			String queryStr = "SELECT u.user_id,u.user_name,ua.attr1_value," +
							  "ua.created_date FROM aview.lecture l,aview.action a," +
							  "aview_audit.user_action ua,aview.audit_user_login aul," +
							  "aview.audit_lecture al,aview.user u  WHERE " +
							  "ua.audit_lecture_id = al.audit_lecture_id " +
							  "AND ua.audit_user_login_id = aul.audit_user_login_id " +
							  "AND ua.lecture_id = l.lecture_id " +
							  "AND al.audit_user_login_id = aul.audit_user_login_id " +
							  "AND al.lecture_id = l.lecture_id " +
							  "AND aul.user_id = u.user_id " +
							  "AND ua.created_by_user_id = u.user_id " +
							  "AND ua.action_id = a.action_id " +
							  "AND a.action_name IN ('ChatMessage') " +
							  "AND l.status_id = "+ statusId +" AND a.status_id = "+ statusId +" " +
							  "AND ua.status_id = "+ statusId +" AND aul.status_id = "+ statusId +" " +
							  "AND u.status_id = "+ statusId +" AND al.status_id = "+ statusId +" " +
							  "AND l.lecture_id ="+ lectureId ;
			Query query = session.createSQLQuery(queryStr);
			userChatDetails = query.list();	

		} catch (HibernateException he) {
			processException(he);
			session.getTransaction().rollback();
		}
		finally {
			HibernateUtils.closeConnection(session);
		}
		return userChatDetails;
	}
	
	public static List getUserAudioVideoPublishingDetailsbyLectureId(Long lectureId,Integer statusId) throws AViewException
	{		
		List userAudioVideoPublishingDetails = null;
		Session session = null;
		try
		{
			session = HibernateUtils.getAuditBatchHibernateConnection(); //Audit table in the audit database.
			session.beginTransaction();
			HibernateUtils.setDatabase(session);
			String queryStr = "SELECT u.user_id,u.user_name,ua.attr1_value FROM " +
							  "aview.lecture l,aview.action a,aview_audit.user_action ua," +
							  "aview.audit_user_login aul,aview.audit_lecture al,aview.user u " +
							  "WHERE ua.audit_user_login_id = aul.audit_user_login_id " +
							  "AND ua.audit_lecture_id = al.audit_lecture_id " +
							  "AND ua.lecture_id = l.lecture_id " +
							  "AND al.audit_user_login_id = aul.audit_user_login_id " +
							  "AND al.lecture_id = l.lecture_id AND aul.user_id = u.user_id " +
							  "AND ua.action_id = a.action_id AND a.action_name IN ('VideoPublishStart') " +
							  "AND l.status_id = "+ statusId +" AND a.status_id = "+ statusId +" " +
							  "AND ua.status_id = "+ statusId +" AND aul.status_id = "+ statusId +" " +
							  "AND u.status_id = "+ statusId +" AND al.status_id = "+ statusId +" " +
							  "AND l.lecture_id ="+ lectureId +" ORDER BY ua.created_date";
			Query query = session.createSQLQuery(queryStr);
			userAudioVideoPublishingDetails = query.list();	

		} catch (HibernateException he) {
			processException(he);
			session.getTransaction().rollback();
		}
		finally {
			HibernateUtils.closeConnection(session);
		}
		return userAudioVideoPublishingDetails;
	}
}
