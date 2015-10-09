/*
 * 
 */
package edu.amrita.aview.gclm.daos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.common.utils.ListUtils;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.gclm.entities.Class;
import edu.amrita.aview.gclm.entities.ClassRegistration;
import edu.amrita.aview.gclm.entities.Course;
import edu.amrita.aview.gclm.entities.Institute;
import edu.amrita.aview.gclm.entities.Lecture;
import edu.amrita.aview.gclm.entities.User;
import edu.amrita.aview.gclm.helpers.ClassRegistrationHelper;
import edu.amrita.aview.gclm.vo.LectureListVO;


/**
 * The Class LectureDAO.
 */
public class LectureDAO  extends SuperDAO{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(LectureDAO.class);

	/**
	 * Creates the lecture.
	 *
	 * @param lecture the lecture
	 * @throws AViewException
	 */
	public static void createLecture(Lecture lecture) throws AViewException{
		Session session = null;
		String creationMessage = null ;
		try {
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();	
			session.save(lecture);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		} finally {
			HibernateUtils.closeConnection(session);
		}
	}
	
	/**
	* Creates the lectures.
	*
	* @param lectures the lectures
	* @throws HibernateException the hibernate exception
	*/
	public static void createLectures(List<Lecture> lectures) throws HibernateException
	{		
		Session session = HibernateUtils.getCurrentHibernateConnection();
		for(int i=0;i<lectures.size();i++)
		{			
			Lecture l = lectures.get(i);
			
			//System.out.println(l.getClassId()+"\t"+l.getLectureName()+"\t"+l.getStartDate()+"\t"+l.hashCode());
			
			session.save(l);
			//System.out.println("After Save");
			
			//Once the batch is full, then send them to db
			if((i+1) % HibernateUtils.HIBERNATE_BATCH_SIZE == 0)
			{
				//System.out.println("In batch operation");
				session.flush();
				session.clear();
			}
		}
	}
	
	/**
	 * Gets the lecture count.
	 *
	 * @param classId the class id
	 * @param statusId the status id
	 * @return the lecture count
	 * @throws AViewException
	 */
	public static Integer getLectureCount(Long classId,Integer statusId) throws AViewException{
		Session session = null;
		Integer lectureCount = -1;
		try {
			session = HibernateUtils.getHibernateConnection();
			Query hqlQuery = session.createQuery("select count(aviewUser.lectureId) from Lecture aviewUser " +
					"where aviewUser.classId = :classId");
			hqlQuery.setLong("classId",classId);
			lectureCount = Integer.parseInt(hqlQuery.list().get(0).toString());

		} catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}

		return lectureCount;
	}

	/**
	 * Gets the future lectures.
	 *
	 * @param classId the class id
	 * @param statusId the status id
	 * @return the future lectures
	 * @throws AViewException
	 */
	public static List<Lecture> getFutureLectures(Long classId,Integer statusId) throws AViewException{
		Session session = null;
		List<Lecture> lectures = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			Query q = session.createQuery("select aviewLecture from Lecture aviewLecture where classId=:classId and startDate >= curdate() and aviewLecture.statusId = :statusId");
			q.setInteger("statusId", statusId);
			q.setLong("classId", classId);
			lectures = q.list();

		} catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}
		return lectures;
	}

	public static List<Lecture> getFutureLecturesForClass(Long classId,Integer statusId) throws AViewException{
		Session session = HibernateUtils.getCurrentHibernateConnection();
		List<Lecture> lectures = null;
		//Fix for Bug # 16251 start
		//This fix is not being used
		Query q = session.createQuery("select aviewLecture from Lecture aviewLecture where classId=:classId and startDate >= curdate() and startTime > curtime() and aviewLecture.statusId = :statusId");
		//Fix for Bug # 16251 end
		q.setInteger("statusId", statusId);
		q.setLong("classId", classId);
		lectures = q.list();
		return lectures;
	}
	
	public static List<Lecture> getRecordedLecturesForClass(Long classId,Integer statusId) throws HibernateException{
		Session session = HibernateUtils.getCurrentHibernateConnection();
		List<Lecture> lectures = null;
		//Fix for Bug #13321 start
		//Added order by to show the meeting in ascending order
		Query q = session.createQuery("select aviewLecture from Lecture aviewLecture " +
									  "where aviewLecture.classId=:classId and " +
									  "aviewLecture.statusId = :statusId and " +
									  "aviewLecture.recordedContentFilePath is not null " +
									  "order by aviewLecture.startDate desc, aviewLecture.startTime desc");
		//Fix for Bug #13321 end
		q.setInteger("statusId", statusId);
		q.setLong("classId", classId);
		lectures = q.list();
		return lectures;
	}
	
	public static List<Lecture> searchRecordedLecturesForClass(Long classId,Integer statusId,String title,String keywords,Date date) throws HibernateException
	{
		Session session = HibernateUtils.getCurrentHibernateConnection();
		List<Lecture> lectures = null;
		//Fix for Bug #13321 start
		//Added order by to show the meeting in ascending order
		String query = "select aviewLecture from Lecture aviewLecture " +
				  "where aviewLecture.classId=:classId and " +
				  "aviewLecture.statusId = :statusId and " +
				  "aviewLecture.recordedContentFilePath is not null ";
		if(title != null)
		{
			query += " and LOWER(aviewLecture.lectureName) like :title";
		}
		if(keywords != null)
		{
			query += " and LOWER(aviewLecture.keywords) like :keywords";
		}
		if(date != null)
		{
			query += " and aviewLecture.startDate = :date";
		}
		query += " order by aviewLecture.startDate desc, aviewLecture.startTime desc";
		Query q = session.createQuery(query);
		//Fix for Bug #13321 end
		q.setInteger("statusId", statusId);
		q.setLong("classId", classId);
		
		if(title != null)
		{
			q.setString("title", "%"+title.toLowerCase()+"%");
		}
		if(keywords != null)
		{
			q.setString("keywords", "%"+keywords.toLowerCase()+"%");
		}
		if(date != null)
		{
			q.setDate("date", date);
		}
		lectures = q.list();
		return lectures;
	}

	/**
	 * Gets the lectures for class.
	 *
	 * @param classId the class id
	 * @param statusId the status id
	 * @return the lectures for class
	 * @throws HibernateException the hibernate exception
	 */
	public static List<Lecture> getLecturesForClass(Long classId,Integer statusId) throws HibernateException{
		Session session = HibernateUtils.getCurrentHibernateConnection();
		List<Lecture> lectures = null;
		//Fix for Bug #13321 start
		//Added order by to show the meeting in ascending order
		Query q = session.createQuery("select aviewLecture from Lecture aviewLecture " +
									  "where aviewLecture.classId=:classId and " +
									  "aviewLecture.statusId = :statusId " +
									  "order by aviewLecture.startDate, aviewLecture.startTime ");
		//Fix for Bug #13321 end
		q.setInteger("statusId", statusId);
		q.setLong("classId", classId);
		lectures = q.list();
		return lectures;
	}

	/**
	 * Gets the lecture.
	 *
	 * @param lectureId the lecture id
	 * @return the lecture
	 * @throws AViewException
	 */
	public static Lecture getLecture(Long lectureId) throws AViewException{
		Session session = null;
		Lecture lecture = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			lecture = (Lecture)session.get(Lecture.class, lectureId);

		} catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}
		return lecture;
	}
	
	/**
	 * Update lecture.
	 *
	 * @param lecture the lecture
	 * @throws AViewException
	 */
	public static void updateLecture(Lecture lecture) throws AViewException{		
		Session session = null;
		String creationMessage = null ;
		try {
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();			
			session.update(lecture);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		} finally {
			HibernateUtils.closeConnection(session);
		}
	}

	/**
	 * Delete lecture.
	 *
	 * @param lectureId the lecture id
	 * @throws AViewException
	 */
	public static void deleteLecture(Long lectureId) throws AViewException
	{		
		Session session = null;
		Lecture lecture = getLecture(lectureId) ; 
		try {
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();								
			session.delete(lecture);								
			session.getTransaction().commit();			
			
		} catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		} finally {
			HibernateUtils.closeConnection(session);
		}
	}
	
	/**
	 * Delete lectures.
	 *
	 * @param lectures the lectures
	 * @throws AViewException
	 */
	public static void deleteLectures(List<Lecture> lectures) throws AViewException
	{		
		Session session = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();							
			for(int i=0;i<lectures.size();i++)
			{			
				Lecture l = lectures.get(i);			
				session.delete(l);
				//Once the batch is full, then send them to db
				if((i+1) % HibernateUtils.HIBERNATE_BATCH_SIZE == 0)
				{
					session.flush();
					session.clear();								
				}
			}						
			session.getTransaction().commit();			
			
		} catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		} finally {
			HibernateUtils.closeConnection(session);
		}
	}
	
	/**
	 * Delete lecture by class id.
	 *
	 * @param classId the class id
	 * @param courseId the course id
	 * @throws HibernateException the hibernate exception
	 */
	public static void deleteLectureByClassId(Long classId,Long courseId) throws HibernateException
	{
		Session session = HibernateUtils.getCurrentHibernateConnection();
		int row = 0;
		//Fix for bug #6611 start
		String hql = "DELETE FROM lecture WHERE class_id = :classId" +
				" AND date > CURDATE()" +
				" AND lecture_id NOT IN (" +
					"SELECT DISTINCT lecture_id FROM (" +
						"SELECT recorded_lecture_id lecture_id FROM lectures WHERE recorded_lecture_id IS NOT NULL " +
						"UNION " +
						"SELECT al.lecture_id FROM audit_lecture al,lecture l WHERE al.lecture_id = l.lecture_id AND l.class_id = :classId" +
					") X" +
				")" ;
		//Fix for bug #6611 end
		SQLQuery query = session.createSQLQuery(hql);
		query.setLong("classId", classId);
		row = query.executeUpdate();
		if (row == 0)
		{
			logger.debug("Didn't delete any row!");
		}
		else
		{
			logger.debug("Deleted Row: " + row);
		}
						
	}

//	public static void deleteLectureByClassId(Long classId,Long courseId) throws AViewException
//	{
//		Session session = null;
//		int row = 0;
//		try {
//			session = HibernateUtils.getHibernateConnection();
//			session.beginTransaction();							
//			
//			String hql = "DELETE FROM lecture WHERE class_id = :classId" +
//					" AND date >= CURDATE()" +
//					" AND lecture_id NOT IN (SELECT distinct recorded_lecture_id FROM lectures WHERE recorded_lecture_id is not null)" ;
//		      SQLQuery query = session.createSQLQuery(hql);
//		      query.setLong("classId", classId);
//		      row = query.executeUpdate();
//		      if (row == 0)
//		      {
//		        logger.debug("Didn't delete any row!");
//		      }
//		      else
//		      {
//		        logger.debug("Deleted Row: " + row);
//		      }
//						
//				session.getTransaction().commit();
//				
//			
//		} catch (HibernateException he) {
//			processException(he);	
//			session.getTransaction().rollback();
//		} finally {
//			HibernateUtils.closeConnection(session);
//		}
//	}
	
	/**
 * Update lectures.
 *
 * @param lectures the lectures
 * @throws AViewException
 */
public static void updateLectures(List<Lecture> lectures) throws AViewException
	{
		Session session = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();							
			for(int i=0;i<lectures.size();i++)
			{			
				Lecture l = lectures.get(i);			
				session.update(l);
				//Once the batch is full, then send them to db
				if((i+1) % HibernateUtils.HIBERNATE_BATCH_SIZE == 0)
				{
					session.flush();
					session.clear();								
				}
			}						
			session.getTransaction().commit();			
			
		} catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		} finally {
			HibernateUtils.closeConnection(session);
		}
	}
	
	/**
	 * Gets the class room lecture by lecture id.
	 *
	 * @param userId the user id
	 * @param lectureId the lecture id
	 * @param userType the user type
	 * @param classRegistrationType the class registration type
	 * @return the class room lecture by lecture id
	 * @throws AViewException
	 */
	public static LectureListVO getClassRoomLectureByLectureId(Long userId,Long lectureId,String userType,String classRegistrationType,String classType) throws AViewException{
		Session session = null;
		LectureListVO lectureVO = null;
		logger.info("Entered getClassRoomLectureByLectureId userId:"+userId+", lectureId:"+lectureId+", userType:"+userType+", classRegistrationType:"+classRegistrationType);
		try {
			session = HibernateUtils.getHibernateConnection();
			final String userQuery = 
				"select l,clr.aviewClass,co,i,clr " +
				"from Lecture l,ClassRegistration clr,Course co,Institute i " +
				"where l.classId = clr.aviewClass.classId " +
				"and clr.aviewClass.courseId = co.courseId " +
				"and co.instituteId = i.instituteId " +
				"and clr.user.userId = :userId " +
				"and l.lectureId = :lectureId ";
			
			final String unRegisteredQuery = 
					"select l,cl,co,i " +
					"from Lecture l,Class cl,Course co,Institute i " +
					"where l.classId = cl.classId " +
					"and cl.courseId = co.courseId " +
					"and co.instituteId = i.instituteId " +
					"and l.lectureId = :lectureId ";
			
			Query q = null;
			
			/*
			 * Below table explains the possible combinations of the User Types (left most column) and class type & registration combinations (header row)
			 * and the query is used for each combination. If a combination has N/A that means the case is not possible/not allowed in A-VIEW 
			------------------------------------------------------------------------------------------------------------------------
					|RegularClass-Registered	|RegularClass-UnRegistered	|OpenClass-Registered	|OpenClass-UnRegistered
			------------------------------------------------------------------------------------------------------------------------
			Regular	|RegisteredQuery			|N/A						|RegisteredQuery		|UnRegisteredQuery
			Admin	|N/A						|UnRegisteredQuery			|N/A					|UnRegisteredQuery
			Guest	|N/A						|N/A						|N/A					|UnRegisteredQuery
			------------------------------------------------------------------------------------------------------------------------

			 */
			
			//If the user type is Teacher or Student, then first look for a registration
			if(!userType.equals(Constant.MASTER_ADMIN_ROLE) && !userType.equals(Constant.ADMIN_ROLE) && !userType.equals(Constant.GUEST_ROLE))
			{
				q = session.createQuery(userQuery);
				q.setLong("lectureId", lectureId);
				q.setLong("userId", userId);
				
				lectureVO =  getLectureByLectureId(session, q, true);
				
				//Try the Unregistered if the class is open type
				if(lectureVO == null && 
						(classRegistrationType.equals(Constant.OPEN_CLASS_REGISTRATION) 
								|| classRegistrationType.equals(Constant.OPEN_WITH_LOGIN_CLASS_REGISTRATION)))
				{
					logger.info("No registration for this lecture. It's Open class. Trying to get Lecture without regisration.");
					q = session.createQuery(unRegisteredQuery);
					q.setLong("lectureId", lectureId);
					lectureVO =  getLectureByLectureId(session, q, false);
				}
			}
			//added checking for meeting
			else if(userType.equals(Constant.MASTER_ADMIN_ROLE) || userType.equals(Constant.ADMIN_ROLE) 
					|| (userType.equals(Constant.GUEST_ROLE) && classRegistrationType.equals(Constant.OPEN_CLASS_REGISTRATION))
					||(userType.equals(Constant.GUEST_ROLE) && classType.equals(Constant.MEETING_CLASS_TYPE)))
			{
				logger.info("It's Open class or Admin user. Trying to get Lecture without regisration.");
				q = session.createQuery(unRegisteredQuery);
				q.setLong("lectureId", lectureId);
				lectureVO =  getLectureByLectureId(session, q, false);
			}

		} catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}
		
		if(lectureVO != null)
		{
			logger.info("Lecture found for  userId:"+userId+", lectureId:"+lectureId+", userType:"+userType+", classRegistrationType:"+classRegistrationType);
		}
		else
		{
			logger.info("Lecture not found for userId:"+userId+", lectureId:"+lectureId+", userType:"+userType+", classRegistrationType:"+classRegistrationType);
		}
		
		return lectureVO;
	}

	
	/**
	 * Gets the lecture by lecture id.
	 *
	 * @param session the session
	 * @param q the q
	 * @param registered the registered
	 * @return the lecture by lecture id
	 * @throws AViewException
	 */
	private static LectureListVO getLectureByLectureId(Session session,Query q,boolean registered) throws AViewException{
		LectureListVO lectureVO = null;
//		logger.debug("Before the query");
		List<Object[]> temp = q.list();
//		logger.debug("After the query");
		for(Object[] objA:temp)
		{
			lectureVO = new LectureListVO();
			lectureVO.lecture = (Lecture)objA[0];
			lectureVO.aviewClass = (Class)objA[1];
			lectureVO.course = (Course)objA[2];
			lectureVO.institute = (Institute)objA[3];
			if(registered)
			{
				lectureVO.classRegistration = (ClassRegistration)objA[4];
			}
			
			break; //Only one..
		}

		return lectureVO;
	}
	
	/**
	 * Gets the todays lectures.
	 *
	 * @param userId the user id
	 * @param activeStatusId the active status id
	 * @param classStatusIds the class status ids
	 * @param classRegStatusIds the class reg status ids
	 * @param userType the user type
	 * @return the todays lectures
	 * @throws AViewException
	 */
	public static List<LectureListVO> getTodaysLectures(Long userId,Integer activeStatusId, List<Integer> classStatusIds, List<Integer> classRegStatusIds, String userType) throws AViewException{
		Session session = null;
		List<LectureListVO> lecturesList = new ArrayList<LectureListVO>();
		try {
			session = HibernateUtils.getHibernateConnection();
			final String userQuery = 
				"select l,clr.aviewClass,co,i,clr " +
				"from Lecture l,ClassRegistration clr,Course co,Institute i " +
				"where l.classId = clr.aviewClass.classId " +
				"and clr.aviewClass.courseId = co.courseId " +
				"and co.instituteId = i.instituteId " +
				"and clr.user.userId = :userId " +
				"and l.startDate = :today " +
				"and l.statusId = :activeStatusId " +
				"and clr.statusId IN (~) " +
				"and clr.aviewClass.statusId IN (#) " +
				"and co.statusId = :activeStatusId " +
				"and i.statusId = :activeStatusId ";
			
			final String adminQuery = 
				"select l,cl,co,i " +
				"from Lecture l,Class cl,Course co,Institute i " +
				"where l.classId = cl.classId " +
				"and cl.courseId = co.courseId " +
				"and co.instituteId = i.instituteId " +
				"and i.instituteId in " +
					"(select distinct inst.instituteId " +
					"from InstituteAdminUser iau,Institute inst " +
					"where iau.user.userId = :adminId " +
					"and (iau.institute.instituteId = inst.instituteId " +
						"or iau.institute.instituteId = inst.parentInstituteId))" +
				"and l.startDate = :today " +
				"and l.statusId = :activeStatusId " +
				"and cl.statusId IN (#) " +
				"and co.statusId = :activeStatusId " +
				"and i.statusId = :activeStatusId ";

			final String masterAdminQuery = 
				"select l,cl,co,i " +
				"from Lecture l,Class cl,Course co,Institute i " +
				"where l.classId = cl.classId " +
				"and cl.courseId = co.courseId " +
				"and co.instituteId = i.instituteId " +
				"and l.startDate = :today " +
				"and l.statusId = :activeStatusId " +
				"and cl.statusId IN (#) " +
				"and co.statusId = :activeStatusId " +
				"and i.statusId = :activeStatusId ";

			String query = null;
			if(userType.equals(Constant.MASTER_ADMIN_ROLE))
			{
				query = masterAdminQuery;
			}
			else if(userType.equals(Constant.ADMIN_ROLE))
			{
				query = adminQuery;
			}
			else
			{
				query = userQuery;
			}
			query = query.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(classStatusIds));
			query = query.replaceAll("~", ListUtils.getNumericListAsCommaDelimitedString(classRegStatusIds));
			Query q = session.createQuery(query);
			q.setInteger("activeStatusId", activeStatusId);
			q.setDate("today", TimestampUtils.getCurrentDate());
			
			if(userType.equals(Constant.ADMIN_ROLE))
			{
				q.setLong("adminId", userId);
			}
			else if(!userType.equals(Constant.MASTER_ADMIN_ROLE))
			{
				q.setLong("userId", userId);
			}

			List<Object[]> temp = q.list();
			for(Object[] objA:temp)
			{
				LectureListVO lectureVO = new LectureListVO();
				lectureVO.lecture = (Lecture)objA[0];
				lectureVO.aviewClass = (Class)objA[1];
				lectureVO.course = (Course)objA[2];
				lectureVO.institute = (Institute)objA[3];
				lectureVO.currentTime= TimestampUtils.getCurrentDate();
				lectureVO.lecture.setStartTime(TimestampUtils.addTimeToDate(lectureVO.lecture.getStartDate(), lectureVO.lecture.getStartTime(), 0));
				lectureVO.lecture.setEndTime(TimestampUtils.addTimeToDate(lectureVO.lecture.getStartDate(), lectureVO.lecture.getEndTime(), 0));
				//logger.debug("*****" + lectureVO.currentTime + "***************");
				User moderator=ClassRegistrationHelper.getModeratorByClass(lectureVO.aviewClass.getClassId());
				if(moderator!=null)
				lectureVO.moderatorName= moderator.getFname()+" "+moderator.getLname();
				if(!userType.equals(Constant.MASTER_ADMIN_ROLE) && !userType.equals(Constant.ADMIN_ROLE))
				{
					lectureVO.classRegistration = (ClassRegistration)objA[4];
				}
				
				lecturesList.add(lectureVO);
			}

		} catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}
		return lecturesList;
	}
	
	/**
	 * Gets the todays open lectures.
	 *
	 * @param activeStatusId the active status id
	 * @param classStatusIds the class status ids
	 * @param classRegStatusIds the class reg status ids
	 * @return the todays open lectures
	 * @throws AViewException
	 */
	public static List<LectureListVO> getTodaysOpenLectures(Integer activeStatusId, List<Integer> classStatusIds, List<Integer> classRegStatusIds) throws AViewException{
		Session session = null;
		List<LectureListVO> lecturesList = new ArrayList<LectureListVO>();
		try {
			session = HibernateUtils.getHibernateConnection();

			String openClassQuery = 
				"select l,cl,co,i " +
				"from Lecture l,Class cl,Course co,Institute i " +
				"where l.classId = cl.classId " +
				"and cl.courseId = co.courseId " +
				"and co.instituteId = i.instituteId " +
				"and cl.registrationType in ('OpenWithLogin','Open') " +
				"and l.startDate = :today " +
				"and l.statusId = :activeStatusId " +
				"and cl.statusId IN (#) " +
				"and co.statusId = :activeStatusId " +
				"and i.statusId = :activeStatusId ";

			openClassQuery = openClassQuery.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(classStatusIds));
			Query q = session.createQuery(openClassQuery);
			q.setInteger("activeStatusId", activeStatusId);
			q.setDate("today", TimestampUtils.getCurrentDate());

			List<Object[]> temp = q.list();
			for(Object[] objA:temp)
			{
				LectureListVO lectureVO = new LectureListVO();
				lectureVO.lecture = (Lecture)objA[0];
				lectureVO.aviewClass = (Class)objA[1];
				lectureVO.course = (Course)objA[2];
				lectureVO.institute = (Institute)objA[3];
				//lectureVO.currentTime= TimestampUtils.removeDateAndMillis(new Date());
				lectureVO.currentTime= TimestampUtils.getCurrentDate();
				lectureVO.lecture.setStartTime(TimestampUtils.addTimeToDate(lectureVO.lecture.getStartDate(), lectureVO.lecture.getStartTime(), 0));
				lectureVO.lecture.setEndTime(TimestampUtils.addTimeToDate(lectureVO.lecture.getStartDate(), lectureVO.lecture.getEndTime(), 0));
				//logger.debug("*****" + lectureVO.currentTime + "***************");				
				User moderator=ClassRegistrationHelper.getModeratorByClass(lectureVO.aviewClass.getClassId());
				if(moderator!=null)
				lectureVO.moderatorName= moderator.getFname()+" "+moderator.getLname();
			    lecturesList.add(lectureVO);
			}

		} catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}
		return lecturesList;
	}
	
	/**
	 * Gets the todays webinars.
	 *
	 * @param statusId the status id
	 * @return the todays webinars
	 * @throws AViewException
	 */
	public static List<LectureListVO> getTodaysWebinars(Integer statusId) throws AViewException
	{
		Session session = null;
		List<LectureListVO> lecturesList = new ArrayList<LectureListVO>();
		try {
			  session = HibernateUtils.getHibernateConnection();
			  final String queryString = 
				"select l,clr.aviewClass,co,i,clr.nodeTypeId,u.userName " +
				"from Lecture l,ClassRegistration clr,Course co,Institute i, User u " +
				"where l.classId = clr.aviewClass.classId " +
				"and clr.aviewClass.courseId = co.courseId " +				
				"and co.instituteId = i.instituteId " +
				"and l.startDate = :today " +
				"and l.statusId = :statusId " +
				"and clr.statusId = :statusId " +
				"and clr.aviewClass.statusId = :statusId " +
				"and co.statusId = :statusId " +
				"and i.statusId = :statusId " +
				"and clr.aviewClass.classType=:webinarConstant " + 
				"and clr.user.userId = u.userId and clr.isModerator = 'Y' " +
				"and u.statusId = :statusId ";
			  
			    Query q = session.createQuery(queryString);
				q.setInteger("statusId", statusId);			   
				q.setDate("today", TimestampUtils.getCurrentDate());
				q.setString("webinarConstant","Webinar");
				
				List<Object[]> temp = q.list();
				for(Object[] objA:temp)
				{
					LectureListVO lectureVO = new LectureListVO();
					lectureVO.lecture = (Lecture)objA[0];
					lectureVO.aviewClass = (Class)objA[1];
					lectureVO.course = (Course)objA[2];
					lectureVO.institute = (Institute)objA[3];
					lectureVO.userName = (String)objA[5];
					lecturesList.add(lectureVO);
				}

			} catch (HibernateException he) {
				processException(he);	
			}		
			finally {
				HibernateUtils.closeConnection(session);
			}
			return lecturesList;		
	}
	
	/**
	 * Gets the lectures.
	 *
	 * @param userId the user id
	 * @param classIds the class ids
	 * @param courseId the course id
	 * @param instituteId the institute id
	 * @param activeStatusId the active status id
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param classStatusIds the class status ids
	 * @param userType the user type
	 * @return the lectures
	 * @throws AViewException
	 */
	public static List<LectureListVO> getLectures(Long userId,List<Long> classIds,Long courseId,Long instituteId,Integer activeStatusId,java.util.Date startDate,java.util.Date endDate, List<Integer> classStatusIds, String userType) throws AViewException{
		Session session = null;
		List<LectureListVO> lecturesList = new ArrayList<LectureListVO>();
		try {
			session = HibernateUtils.getHibernateConnection();
			final String userQuery = 
				"select l,clr.aviewClass,co,i,clr " +
				"from Lecture l,ClassRegistration clr,Course co,Institute i " +
				"where l.classId = clr.aviewClass.classId " +
				" and clr.aviewClass.classId IN (~) " +
				" and clr.aviewClass.courseId = co.courseId " +
				" and co.instituteId = i.instituteId " +
				" and clr.user.userId = :userId " +
				" and l.startDate BETWEEN  :statrtDate AND :endDate " +
				" and l.statusId = :activeStatusId " +
				" and clr.statusId = :activeStatusId " +
				" and clr.aviewClass.statusId IN (#) " +
				" and co.statusId = :activeStatusId " +
				" and i.statusId = :activeStatusId ";
			
			final String adminQuery = 
					"select l,cl,co,i " +
					"from Lecture l,Class cl,Course co,Institute i " +
					"where l.classId = cl.classId " +
					"and cl.courseId = co.courseId " +
					" and cl.classId IN (~) " +
					" and co.instituteId = i.instituteId " +
					"and i.instituteId in " +
						"(select distinct inst.instituteId " +
						"from InstituteAdminUser iau,Institute inst " +
						"where iau.user.userId = :adminId " +
						"and (iau.institute.instituteId = inst.instituteId " +
							"or iau.institute.instituteId = inst.parentInstituteId))" +
					"and l.startDate BETWEEN  :statrtDate AND :endDate " +
					"and l.statusId = :activeStatusId " +
					"and cl.statusId IN (#) " +
					"and co.statusId = :activeStatusId " +
					"and i.statusId = :activeStatusId ";

				final String masterAdminQuery = 
					"select l,cl,co,i " +
					"from Lecture l,Class cl,Course co,Institute i " +
					"where l.classId = cl.classId " +
					" and cl.classId IN (~) " +
					"and cl.courseId = co.courseId " +
					"and co.instituteId = i.instituteId " +
					"and l.startDate BETWEEN  :statrtDate AND :endDate " +
					"and l.statusId = :activeStatusId " +
					"and cl.statusId IN (#) " +
					"and co.statusId = :activeStatusId " +
					"and i.statusId = :activeStatusId ";
				
				
			String query = null;
			if(userType.equals(Constant.MASTER_ADMIN_ROLE))
			{
				query = masterAdminQuery;
			}
			else if(userType.equals(Constant.ADMIN_ROLE))
			{
				query = adminQuery;
			}
			else
			{
				query = userQuery;
			}
			if(courseId != null && courseId > 0)
			{
				query += " and co.courseId=:courseId" ;
			}
			
			if(instituteId != null && instituteId > 0)
			{
				query += " and i.instituteId=:instituteId" ;
			}
			query = query.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(classStatusIds));
			query = query.replaceAll("~", ListUtils.getNumericListAsCommaDelimitedString(classIds));
			Query q = session.createQuery(query);
			q.setInteger("activeStatusId", activeStatusId);
			q.setDate("statrtDate",startDate);
			q.setDate("endDate", endDate);
			if(userType.equals(Constant.ADMIN_ROLE))
			{
				q.setLong("adminId", userId);
			}
			else if(!userType.equals(Constant.MASTER_ADMIN_ROLE))
			{
				q.setLong("userId", userId);
			}			
			//q.setLong("classId", classIds);			
			
			if(courseId != null && courseId > 0)
			{
				q.setLong("courseId", courseId);
			}
			
			if(instituteId != null && instituteId > 0)
			{
				q.setLong("instituteId", instituteId);
			}
			List<Object[]> temp = q.list();
			for(Object[] objA:temp)
			{
				LectureListVO lectureVO = new LectureListVO();
				lectureVO.lecture = (Lecture)objA[0];
				lectureVO.aviewClass = (Class)objA[1];
				lectureVO.course = (Course)objA[2];
				lectureVO.institute = (Institute)objA[3];
				/*if(!userType.equals(Constant.MASTER_ADMIN_ROLE) && !userType.equals(Constant.ADMIN_ROLE))
				{
					lectureVO.classRegistration = (ClassRegistration)objA[4];
				}*/
				User moderator=ClassRegistrationHelper.getModeratorByClass(lectureVO.aviewClass.getClassId());
				if(moderator!=null)
				{
					lectureVO.moderatorName= moderator.getFname()+" "+moderator.getLname();
				}
				lecturesList.add(lectureVO);
			}

		} catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}
		return lecturesList;
	}
	
	/**
	 * Gets the lectures.
	 *
	 * @param lectureIds the lecture ids
	 * @return the lectures
	 * @throws AViewException
	 */
	public static List<Lecture> getLectures(List<Long> lectureIds) throws AViewException
	{
		Session session = null;
		List<Lecture> lectures = new ArrayList<Lecture>();
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			String hqlQuery = "select aviewLecture from Lecture aviewLecture where lectureId IN (#)";
			hqlQuery = hqlQuery.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(lectureIds));
			Query q = session.createQuery(hqlQuery);
			lectures = q.list();			
		}
		catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}
		return lectures;
	}
	
	/**
	 * Gets the lectures for class.
	 *
	 * @param classId the class id
	 * @param today the today
	 * @param lectureKeyword the lecture keyword
	 * @param lectureTopic the lecture topic
	 * @param statusId the status id
	 * @return the lectures for class
	 * @throws AViewException
	 */
	public static List<Lecture> getLecturesForClass(Long classId, Date today, String lectureKeyword, String lectureTopic, Integer statusId) throws AViewException
	{
		Session session = null;
		List<Lecture> lectures = null;
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			
			String queryString = "select aviewLecture from Lecture aviewLecture where aviewLecture.classId=:classId and " +
							  "aviewLecture.startDate <= :today and aviewLecture.statusId = :statusId ";
			
			if(lectureKeyword != null)
			{
				queryString += " and aviewLecture.keywords like :lectureKeyword ";
			}
			if(lectureTopic != null)
			{
				queryString += " and aviewLecture.lectureName like :lectureTopic ";
			}
			Query hqlQuery = session.createQuery(queryString);
			if(lectureKeyword != null)
			{
				hqlQuery.setString("lectureKeyword", ("%" + lectureKeyword + "%"));
			}
			if(lectureTopic != null)
			{
				hqlQuery.setString("lectureTopic", ("%" + lectureTopic + "%"));
			}
			
			hqlQuery.setInteger("statusId", statusId);
			hqlQuery.setLong("classId", classId);
			hqlQuery.setDate("today", today);
			lectures = hqlQuery.list();
		}
		catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}		
		return lectures;
	}
}
