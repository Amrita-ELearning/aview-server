/*
 * 
 */
package edu.amrita.aview.gclm.helpers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import com.sun.jersey.api.client.ClientResponse.Status;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.helpers.EmailHelper;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.common.utils.JSONParserUtils;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.common.utils.ValidationUtils;
import edu.amrita.aview.common.vo.AViewResponse;
import edu.amrita.aview.gclm.daos.LectureDAO;
import edu.amrita.aview.gclm.daos.LecturesDAO;
import edu.amrita.aview.gclm.entities.Class;
import edu.amrita.aview.gclm.entities.ClassRegistration;
import edu.amrita.aview.gclm.entities.Lecture;
import edu.amrita.aview.gclm.entities.Lectures;
import edu.amrita.aview.gclm.entities.User;
import edu.amrita.aview.gclm.vo.LectureListVO;
import edu.amrita.aview.gclm.vo.RecordedLectureVO;
import edu.amrita.aview.meeting.helpers.MeetingManagerHelper;


/**
 * The Class LectureHelper.
 */
@Controller
public class LectureHelper {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(LectureHelper.class);

	/**
	 * Creates the lecture.
	 *
	 * @param lecture the lecture
	 * @param creatorId the creator id
	 * @return the lecture
	 * @throws AViewException
	 */
	public static Lecture createLecture(Lecture lecture,Long creatorId) throws AViewException{	
		//Fix for Bug#15278
		setLectureNameWithDate(lecture);
		lecture.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());		
		LectureDAO.createLecture(lecture);
		return lecture;
	}

	/**
	 * Creates the lectures.
	 *
	 * @param lectures the lectures
	 * @param creatorId the creator id
	 * @throws AViewException
	 */
	public static void createLectures(List<Lecture> lectures,Long creatorId) throws AViewException
	{
		for(Lecture lecture:lectures)
		{
			lecture.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
		}
		LectureDAO.createLectures(lectures);
	}

	/**
	 * Update lecture.
	 *
	 * @param lecture the lecture
	 * @param updaterId the updater id
	 * @throws AViewException
	 */
	public static void updateLecture(Lecture lecture,Long updaterId) throws AViewException{
		
		Lecture oldLecture = LectureHelper.getLecture(lecture.getLectureId());
		//Fix for Bug#15278
		setLectureNameWithDate(lecture);
		//Check if the date and time of the lecture has been changed
		boolean hasLectureScheduleChanged = didLectureScheduleChanged(oldLecture, lecture);
		
		oldLecture.updateFrom(lecture);
		
		oldLecture.setModifiedAuditData(updaterId, TimestampUtils.getCurrentTimestamp());
		LectureDAO.updateLecture(oldLecture);
		
		if(hasLectureScheduleChanged)
		{
			sendEmailToClassRegistrantsOnLectureScheduleChange(oldLecture);
		}
	}

	/**
	 * Update lectures.
	 *
	 * @param lectures the lectures
	 * @param updaterId the updater id
	 * @throws AViewException
	 */
	public static void updateLectures(List<Lecture> lectures,Long updaterId) throws AViewException{			
		List<Lecture> toBeUpdated = new ArrayList<Lecture>();
		for(Lecture lecture:lectures)
		{
			Lecture oldLecture = LectureHelper.getLecture(lecture.getLectureId());
			oldLecture.updateFrom(lecture);
			oldLecture.setModifiedAuditData(updaterId, TimestampUtils.getCurrentTimestamp());
			toBeUpdated.add(oldLecture);
		}
		LectureDAO.updateLectures(toBeUpdated);	
	}

	
	/**
	 * Delete lecture.
	 *
	 * @param lectureId the lecture id
	 * @throws AViewException
	 */
	public static void deleteLecture(Long lectureId) throws AViewException{			
		LectureDAO.deleteLecture(lectureId) ;
	}
	
	/**
	 * Delete list of lectures.
	 *
	 * @param lectures the lectures
	 * @throws AViewException
	 */
	public static void deleteLectures(List<Lecture> lectures) throws AViewException
	{
		LectureDAO.deleteLectures(lectures) ;
	}
	
	/**
	 * Delete lecture recording.
	 *
	 * @param lectureId the lecture id
	 * @param updaterId the updater id
	 * @return the a view response
	 */
	public static AViewResponse deleteLectureRecording(Long lectureId,Long updaterId)
	{
		AViewResponse response =  new AViewResponse();;
		try
		{
			Lecture lecture = LectureHelper.getLecture(lectureId);
			
			Date cutOff = TimestampUtils.addTimeToDate(lecture.getStartDate(), lecture.getStartTime(), 24*60);
			
			if(cutOff.after(new Date()))
			{
				response.setResponseId(AViewResponse.ERROR_CLIENT);
				//Fix for Bug #11982
			 	response.setResponseMessage("Deletion of Lecture's recording failed. Recording can be deleted only after 1 day from the start time.");
			}
			else
			{
				lecture.setRecordedContentFilePath(null);
				lecture.setRecordedContentUrl(null);
				lecture.setRecordedPresenterVideoUrl(null);
				lecture.setRecordedVideoFilePath(null);
				lecture.setRecordedViewerVideoUrl(null);
				
				lecture.setModifiedAuditData(updaterId, TimestampUtils.getCurrentTimestamp());
				LectureDAO.updateLecture(lecture);
				response.setResponseId(AViewResponse.REQUEST_SUCCESS);
			 	response.setResponseMessage("Successfully deleted Lecture's recording");
			}
		}
		catch (Throwable e)
		{
			String responseMessage = "Error while deleting recording of lecture with id :"+lectureId+", by updaterId :"+updaterId+", Error:"+e.toString();
			logger.error(responseMessage,e);
		 	response.setResponseId(AViewResponse.ERROR_SERVER);
		 	response.setResponseMessage(responseMessage);
		}
		//"Successfully deleted Lecture's recording"
		return response;
	}
	
	/**
	 * Gets the lecture count.
	 *
	 * @param classId the class id
	 * @return the lecture count
	 * @throws AViewException
	 */
	public static Integer getLectureCount(Long classId) throws AViewException
	{
		return LectureDAO.getLectureCount(classId, StatusHelper.getActiveStatusId());
	}
	
	/**
	 * Delete lecture by class id.
	 *
	 * @param classId the class id
	 * @param courseId the course id
	 * @throws AViewException
	 */
	public static void deleteLectureByClassId(Long classId,Long courseId) throws AViewException
	{
		LectureDAO.deleteLectureByClassId(classId,courseId);
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
		List<Lecture> lectures = new ArrayList<Lecture>();
		lectures = LectureDAO.getLectures(lectureIds);
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
		Lecture lecture = LectureDAO.getLecture(lectureId);
		if(lecture != null )
		{
			populateNames(lecture);
		}
		return lecture;
	}
	
	/**
	 * Gets the lectures for class.
	 *
	 * @param classId the class id
	 * @return the lectures for class
	 * @throws AViewException
	 */
	public static List<Lecture> getLecturesForClass(Long classId) throws AViewException{
		Session session = null;	
		List<Lecture> aviewLectures = null;
		try 
		{
			session = HibernateUtils.getCurrentHibernateConnection();
			session.beginTransaction();
			int activeSId = StatusHelper.getActiveStatusId();
			aviewLectures = LectureDAO.getLecturesForClass(classId,activeSId) ;
			populateNames(aviewLectures);
			session.getTransaction().commit();
		}
		catch (HibernateException he) 
		{
			processException(he);	
			session.getTransaction().rollback();
		}
		finally 
		{
			HibernateUtils.closeConnection(session);
		}
		return aviewLectures ;
	}
	
	public static List<Lecture> getFutureLecturesForClass(Long classId) throws AViewException
	{
		Session session = null;	
		List<Lecture> aviewLectures = null;
		try 
		{
			session = HibernateUtils.getCurrentHibernateConnection();
			session.beginTransaction();
			int activeSId = StatusHelper.getActiveStatusId();
			aviewLectures = LectureDAO.getFutureLecturesForClass(classId, activeSId);
			if((aviewLectures != null) && (aviewLectures.size() > 0))
			{
				populateNames(aviewLectures);
			}
			session.getTransaction().commit();
		}
		catch (HibernateException he) 
		{
			processException(he);	
			session.getTransaction().rollback();
		}
		finally
		{
			HibernateUtils.closeConnection(session);
		}
		return aviewLectures;
	}

	
	/**
	 * Populate names.
	 *
	 * @param lectures the lectures
	 * @throws AViewException
	 */
	private static void populateNames(List<Lecture> lectures) throws AViewException
	{
		for(Lecture lecture:lectures)
		{
			populateNames(lecture);
		}
	}
	
	/**
	 * Populate names.
	 *
	 * @param lecture the lecture
	 * @throws AViewException
	 */
	public static void populateNames(Lecture lecture) throws AViewException
	{
		Long classId = lecture.getClassId();

		Class cls = ClassHelper.getActiveCalssesIdMap().get(classId); //Cached, so do not worry :-)
		if(cls != null)
		{
			lecture.setClassName(cls.getClassName());
			lecture.setCourseName(cls.getCourseName());
			lecture.setInstituteName(cls.getInstituteName());
		}
		
	}
				
	/**
	 * Process exception.
	 *
	 * @param he the he
	 * @throws AViewException
	 */
	private static void processException(HibernateException he) throws AViewException
	{
		String exceptionMessage = null ;
		exceptionMessage = he.getMessage();
		if(he.getCause() != null && he.getCause().getMessage() != null)
		{
			exceptionMessage = he.getCause().getMessage();
		}
		else
		{
			exceptionMessage = he.getMessage();
		}
		logger.error(exceptionMessage,he);
		throw (new AViewException(exceptionMessage));
	}
	
	/**
	 * Checks if is the lecture day.
	 *
	 * @param weekDays the week days
	 * @param day the day
	 * @return true, if is the lecture day
	 */
	public static boolean isTheLectureDay(String weekDays,int day)
	{
		
		boolean flag = false ;
		
			if(day == 1 )
			{
				day = 6;
			}
			else
			{
				day = day - 2 ;
			}
					
			if(weekDays.charAt(day) == 'Y')
			{
				flag = true ;
			}		
		return flag ;
	}

	/**
	 * Gets the lectures for class create.
	 *
	 * @param aviewClass the aview class
	 * @return the lectures for class create
	 */
	public static List<Lecture> getLecturesForClassCreate(Class aviewClass)
	{
		List<Lecture> aviewLectures =new ArrayList<Lecture>() ;
		
		String week = aviewClass.getWeekDays() ;
		
	 	GregorianCalendar todayCal = TimestampUtils.removeTimeCalendar(new Date());
	 	
	 	GregorianCalendar classEndCal = new GregorianCalendar();
	 	classEndCal.setTime(aviewClass.getEndDate());

	 	GregorianCalendar classStartCal  = new GregorianCalendar();
	 	classStartCal.setTime(aviewClass.getStartDate()) ;

	 	GregorianCalendar lectureStartCal  = null;
	 	
	 	if(todayCal.before(classStartCal)) // checking the start date of a class , if today's date is before the start date of class
	 	{	 								// then set the start date of lecture to  start date of class 	 		
	 		lectureStartCal = classStartCal;	 	
	 	}
	 	else
	 	{
	 		lectureStartCal = todayCal;
	 	}
	
	 	logger.debug(" lecture start cal ::"+lectureStartCal.getTime());
	 	classEndCal.add(Calendar.DATE, 1) ;

	 	while(lectureStartCal.before(classEndCal)) // checking the start date of lecture , upto the end date of class 
	 	{																					// to create lectures from start date of class to end date of class	 		
	 		if( (isTheLectureDay(week,lectureStartCal.get(Calendar.DAY_OF_WEEK)))	) 		// check the weekday if there is a lecture on that particular weekday
	 		{	 				 		
	 				Lecture lecture = new Lecture() ;
	 				lecture.setClassId(aviewClass.getClassId());
	 				lecture.setEndTime(aviewClass.getEndTime());
	 				lecture.setStartTime(aviewClass.getStartTime());
	 				lecture.setKeywords("");	 				 
	 				lecture.setLectureName(aviewClass.getClassName()+"~"+lectureStartCal.get(Calendar.YEAR)+"-"+(lectureStartCal.get(Calendar.MONTH)+1)+"-"+lectureStartCal.get(Calendar.DATE));
	 				lecture.setLectureNumber(0);
	 				lecture.setStartDate(lectureStartCal.getTime());
	 				aviewLectures.add(lecture);
	 				
	 		}
	 		lectureStartCal.add(Calendar.DATE,1) ;
	 	}
	 	
	 	
	 	return aviewLectures ;
	}

	/**
	 * Removes the date times.
	 *
	 * @param lecture the lecture
	 */
	private static void removeDateTimes(Lecture lecture)
	{
		lecture.setStartDate(TimestampUtils.removeTime(lecture.getStartDate()));
		lecture.setStartTime(TimestampUtils.removeDateAndMillis(lecture.getStartTime()));
		lecture.setEndTime(TimestampUtils.removeDateAndMillis(lecture.getEndTime()));
	}

	/**
	 * Gets the lectures for class update.
	 *
	 * @param aviewClass the aview class
	 * @return the lectures for class update
	 * @throws AViewException
	 */
	public static List<Lecture> getLecturesForClassUpdate(Class aviewClass) throws AViewException
	{
		List<Lecture> currentLectures = LectureDAO.getLecturesForClass(aviewClass.getClassId(),StatusHelper.getActiveStatusId()) ;
		Map<java.util.Date,Lecture> currentLecturesMap = new HashMap<java.util.Date,Lecture>();
		
		for(Lecture currentLecture:currentLectures)
		{
			removeDateTimes(currentLecture);
			currentLecturesMap.put(currentLecture.getStartDate(), currentLecture);
//			logger.debug("Key for the lecture:"+currentLecture.getStartDate()+":"+currentLecture.getStartDate().getTime());
		}
		
		List<Lecture> aviewLectures = getLecturesForClassCreate(aviewClass);
		Iterator<Lecture> iter = aviewLectures.iterator();
		while(iter.hasNext())
		{
			Lecture aviewLecture = iter.next();
			
			if(currentLecturesMap.containsKey(aviewLecture.getStartDate()))
			{
				iter.remove();
//				logger.debug("Removed lecture for startDate from list to be created:"+aviewLecture.getStartDate()+":"+aviewLecture.getStartDate().getTime());
			}
//			else
//			{
//				logger.debug("Did not remove lecture for startDate from list to be created:"+aviewLecture.getStartDate()+":"+aviewLecture.getStartDate().getTime());
//			}
		}
	 	
	 	return aviewLectures ;
	}
	
	/**
	 * Gets the todays lectures.
	 *
	 * @param userId the user id
	 * @return the todays lectures
	 * @throws AViewException
	 */
	public static List<LectureListVO> getTodaysAllLectures(Long userId) throws AViewException
	{
		User user = UserHelper.getUser(userId);
		
		// To get the class registrations for user for both active and testing status
		List<Integer> classRegStatusIds = new ArrayList<Integer>();
		classRegStatusIds.add(StatusHelper.getActiveStatusId());
		classRegStatusIds.add(StatusHelper.getTestingStatusId());
		
		List<Integer> classStatusIds = new ArrayList<Integer>();
		classStatusIds.add(StatusHelper.getActiveStatusId());
		classStatusIds.add(StatusHelper.getClosedStatusId());
		List<LectureListVO> lectureLists = LectureDAO.getTodaysLectures(userId,StatusHelper.getActiveStatusId(), classStatusIds, classRegStatusIds, user.getRole());
		List<LectureListVO> lectureListsOpen = LectureDAO.getTodaysOpenLectures(StatusHelper.getActiveStatusId(), classStatusIds, classRegStatusIds); //Getting Open lectures
		
		lectureListsOpen.removeAll(lectureLists);
		
		lectureLists.addAll(lectureListsOpen);
		
		List<LectureListVO> finalLecturesLists = new ArrayList<LectureListVO>();
		
		for(LectureListVO lectureListVO:lectureLists)
		{
			populateNamesAndRegistration(lectureListVO,user);
			finalLecturesLists.add(lectureListVO);
		}
		
		return finalLecturesLists;
	}
	
	public static List<Lecture> getRecordedLectures(Long classId) throws AViewException
	{
		Session session = null;	
		List<Lecture> aviewLectures = null;
		try 
		{
			session = HibernateUtils.getCurrentHibernateConnection();
			session.beginTransaction();
			int activeSId = StatusHelper.getActiveStatusId();
			aviewLectures = LectureDAO.getRecordedLecturesForClass(classId,activeSId) ;
			populateNames(aviewLectures);
			session.getTransaction().commit();
		}
		catch (HibernateException he) 
		{
			processException(he);	
			session.getTransaction().rollback();
		}
		finally 
		{
			HibernateUtils.closeConnection(session);
		}
		return aviewLectures ;
	}
	
	public static List<Lecture> searchRecordedLectures(Long classId,String title,String keywords,Date date) throws AViewException
	{
		Session session = null;	
		List<Lecture> aviewLectures = null;
		try 
		{
			session = HibernateUtils.getCurrentHibernateConnection();
			session.beginTransaction();
			int activeSId = StatusHelper.getActiveStatusId();
			aviewLectures = LectureDAO.searchRecordedLecturesForClass(classId,activeSId,title,keywords,date) ;
			populateNames(aviewLectures);
			session.getTransaction().commit();
		}
		catch (HibernateException he) 
		{
			processException(he);	
			session.getTransaction().rollback();
		}
		finally 
		{
			HibernateUtils.closeConnection(session);
		}
		return aviewLectures ;
	}
	
	/**
	 * Populate names and registration.
	 *
	 * @param lectureListVO the lecture list vo
	 * @param user the user
	 * @throws AViewException
	 */
	private static void populateNamesAndRegistration(LectureListVO lectureListVO,User user) throws AViewException
	{
		InstituteHelper.populateNames(lectureListVO.institute, InstituteHelper.getParentInstituteName(lectureListVO.institute.getParentInstituteId()));
		CourseHelper.populateNames(lectureListVO.course);
		ClassHelper.populateNames(lectureListVO.aviewClass);
		//Populate a dummy ClassRegistration..
		if(lectureListVO.classRegistration == null)
		{
			lectureListVO.classRegistration = ClassRegistration.prepareDummyClassRegistration(lectureListVO.aviewClass, user);
		}
		
	}
	
	/**
	 * Gets the class room lecture by lecture id.
	 *
	 * @param userId the user id
	 * @param lectureId the lecture id
	 * @return the class room lecture by lecture id
	 * @throws AViewException
	 */
	public static AViewResponse getClassRoomLectureByLectureId(Long userId,Long lectureId) throws AViewException{
		logger.debug("Entered into getClassRoomLectureByLectureId userId:"+userId+", lectureId:"+lectureId);
		
		//Getting the user to check the user type. Getting locally to prevent misuse
		User u = UserHelper.getUser(userId);

		//Getting the lecture to check the Class's registration type type. Getting locally to prevent misuse
		Lecture lecture = LectureHelper.getLecture(lectureId);
		
		AViewResponse response = new AViewResponse();
		/*
		 * Fix for Bug#18800
		 * This check is not needed as this is already done at client side
		 * 
		 * */
//		if(!canEnterLecture(lecture, new Date()))
//		{
//			response.setResponseId(AViewResponse.ERROR_CLIENT);
//			response.setResponseMessage("Entry is permitted into classes only 30 minutes before session begins till the session ends\n" +
//					 "Please contact System Administrator");
//		}
//		else
//		{
			Class cls = ClassHelper.getActiveCalssesIdMap().get(lecture.getClassId());
			if(cls != null)
			{
				LectureListVO lectureListVO = LectureDAO.getClassRoomLectureByLectureId(userId, lectureId, u.getRole(),cls.getRegistrationType(),cls.getClassType());
//				logger.debug("Fetched the raw VO from DAO");
				if(lectureListVO != null)
				{
					
					//If the class is a meeting type, then populate the class with meeting servers from the institute.
					if(lectureListVO.aviewClass.getClassType().equals(Constant.MEETING_CLASS_TYPE))
					{
						long instituteId=ClassRegistrationHelper.getModeratorByClass(lectureListVO.aviewClass.getClassId()).getInstituteId();	
						MeetingManagerHelper.assignMeetingServers(lectureListVO.aviewClass, instituteId);
					}
					populateNamesAndRegistration(lectureListVO,u);
					
//					logger.debug("Populated the Names");
					response.setResponseId(AViewResponse.REQUEST_SUCCESS);
					response.setResponseMessage("Success");
					response.setResult(lectureListVO);
				}
				else
				{
					response.setResponseId(AViewResponse.ERROR_CLIENT);
					response.setResponseMessage("Lecture is not found.\n Either lecture id may be wrong, class registration may not exist for the user, or class may not be an 'Open' class.");
				}
			}
			else
			{
				response.setResponseId(AViewResponse.ERROR_CLIENT);
				response.setResponseMessage("Class is not Active. Please choose another Active Class");
			}
			
//		}
		
		logger.debug("Exiting from getClassRoomLectureByLectureId with the response code:"+response.getResponseId());
		return response;
	}

	
	/**
	 * Can enter lecture.
	 *
	 * @param lecture the lecture
	 * @param currentTime the current time
	 * @return true, if successful
	 */
	private static boolean canEnterLecture(Lecture lecture,Date currentTime)
	{
		//Fix for Bug#18800 : Start
		int TWO_HOURS_IN_MINUTES = 120;
		Date startDateTime = TimestampUtils.addTimeToDate(lecture.getStartDate(), lecture.getStartTime(),-TWO_HOURS_IN_MINUTES); //Adding 120 minutes before class starts
		Date endDateTime ;
		if(ClassHelper.getClass(lecture.getClassId()).getClassType() == Constant.MEETING_CLASS_TYPE)
		{
			endDateTime = TimestampUtils.addTimeToDate(lecture.getStartDate(), lecture.getEndTime(),0);
		}
		else
		{
			endDateTime = TimestampUtils.addTimeToDate(lecture.getStartDate(), lecture.getEndTime(),TWO_HOURS_IN_MINUTES);//Adding 120 minutes after class ends
		}
		//Fix for Bug#18800 : End
		return 
				(currentTime.after(startDateTime))
				&&
				(currentTime.before(endDateTime));
	}
	
	/**
	 * Gets the lectures.
	 *
	 * @param userId the user id
	 * @param classId the class id
	 * @param courseId the course id
	 * @param instituteId the institute id
	 * @param startDate the start date
	 * @param endDate the end date
	 * @return the lectures
	 * @throws AViewException
	 */
	public static List<LectureListVO> getLectures(Long userId,Long classId,Long courseId,Long instituteId,Date startDate,Date endDate) throws AViewException
	{
		User user = UserHelper.getUser(userId);	
		String userType=user.getRole();
		List<ClassRegistration> classRegistrationIds=null;
		if(userType.equals(Constant.MASTER_ADMIN_ROLE))
		{
			classRegistrationIds = ClassRegistrationHelper.searchForClassRegisterForUser(null, null, null, null, null);		
		}
		else if(userType.equals(Constant.ADMIN_ROLE))
		{
			classRegistrationIds = ClassRegistrationHelper.searchForClassRegisterForUser(null, null, null, null, user.getInstituteId());		
		}
		else
		{
			 classRegistrationIds = ClassRegistrationHelper.searchForClassRegisterForUser(userId, null, null, null, null);		
		}
		
		List<Long> classIds =  new ArrayList<Long>();
		if((classId == 0) || (classId == null))
		{
			for(ClassRegistration clr : classRegistrationIds)
			{
				classIds.add(clr.getAviewClass().getClassId());
			}
		}
		else
		{
			classIds.add(classId);
		}
		List<Integer> classStatusIds = new ArrayList<Integer>();
		classStatusIds.add(StatusHelper.getActiveStatusId());
		classStatusIds.add(StatusHelper.getClosedStatusId());
		List<LectureListVO> lectureLists = LectureDAO.getLectures(userId,classIds,courseId,instituteId,StatusHelper.getActiveStatusId(),startDate,endDate,classStatusIds,user.getRole());
		
		for(LectureListVO lectureList:lectureLists)
		{
			InstituteHelper.populateNames(lectureList.institute, InstituteHelper.getParentInstituteName(lectureList.institute.getParentInstituteId()));
			CourseHelper.populateNames(lectureList.course);
			ClassHelper.populateNames(lectureList.aviewClass);			
		}
		return lectureLists;
	}
	
	/**
	 * Gets the lectures for class.
	 *
	 * @param classId the class id
	 * @param today the today
	 * @param lectureKeyword the lecture keyword
	 * @param lectureTopic the lecture topic
	 * @return the lectures for class
	 * @throws AViewException
	 */
	public static List<Lecture> getLecturesForClass(Long classId, Date today, String lectureKeyword, String lectureTopic) throws AViewException
	{
		List<Lecture> aviewLectures = null;
		try 
		{
			int activeSId = StatusHelper.getActiveStatusId();
			aviewLectures = LectureDAO.getLecturesForClass(classId, today, lectureKeyword, lectureTopic, activeSId) ;
			populateNames(aviewLectures);
		}
		catch (HibernateException he)
		{
			processException(he);		
		}
		 return aviewLectures ;
	}

	/**
	 * Gets the recorded lectures.
	 *
	 * @param userId the user id
	 * @param classId the class id
	 * @param courseId the course id
	 * @param instituteId the institute id
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param lectureKeyword the lecture keyword
	 * @param lectureTopic the lecture topic
	 * @return the recorded lectures
	 * @throws AViewException
	 */
	public static List<RecordedLectureVO> getRecordedLectures(Long userId, Long classId, Long courseId, Long instituteId, Date startDate, Date endDate, String lectureKeyword, String lectureTopic) throws AViewException
	{
		User user = UserHelper.getUser(userId);	
		String userType=user.getRole();
		List<ClassRegistration> classRegistrationIds = null;
		List<Long> classIds =  new ArrayList<Long>();

		Map<Long, String> clrModeratorMap = new HashMap<Long, String>();
		
		//Long userId,Long classId,String moderator,Long courseId,Long instituteId
		if(userType.equals(Constant.MASTER_ADMIN_ROLE))
		{
			classRegistrationIds = ClassRegistrationHelper.searchForClassRegisterForUser(null, null, null, courseId, null);
		}
		else if(userType.equals(Constant.ADMIN_ROLE))
		{
			classRegistrationIds = ClassRegistrationHelper.searchForClassRegisterForUser(null, null, null, courseId, user.getInstituteId());		
		}
		else
		{
			 classRegistrationIds = ClassRegistrationHelper.searchForClassRegisterForUser(userId, null, null, courseId, null);		
		}
		
		for(ClassRegistration clr : classRegistrationIds)
		{
			classIds.add(clr.getAviewClass().getClassId());
			clrModeratorMap.put(clr.getAviewClass().getClassId(), clr.getIsModerator());
		}
		
		List<Lecture> newRecordedLectures = new ArrayList<Lecture>();
		
		for(Long registeredClassId : classIds)
		{
			newRecordedLectures.addAll(getLecturesForClass(registeredClassId, TimestampUtils.getCurrentTimestamp(), lectureKeyword, lectureTopic));
		}
		
		List<RecordedLectureVO> recordedLectures = new ArrayList<RecordedLectureVO>();
		RecordedLectureVO recordedLectureVo = new RecordedLectureVO();
		for(Lecture newRecordedLecture : newRecordedLectures)
		{
			if( ((newRecordedLecture.getRecordedContentFilePath() == null) || 
				(newRecordedLecture.getRecordedContentFilePath() == "")) &&
				((newRecordedLecture.getRecordedContentUrl() == null) ||
				(newRecordedLecture.getRecordedContentUrl() == "")) &&
				((newRecordedLecture.getRecordedPresenterVideoUrl() == null) ||
				(newRecordedLecture.getRecordedPresenterVideoUrl() == "")) &&
				((newRecordedLecture.getRecordedVideoFilePath() == null) ||
				(newRecordedLecture.getRecordedVideoFilePath() == "")) &&
				((newRecordedLecture.getRecordedViewerVideoUrl() == null) ||
				(newRecordedLecture.getRecordedViewerVideoUrl() == "")))
			{
				// do nothing if it is not a recorded lecture
			}
			else
			{
				recordedLectureVo = new RecordedLectureVO();
				recordedLectureVo.setIsModerator(clrModeratorMap.get(newRecordedLecture.getClassId()));
				recordedLectureVo.setUseNewPlayer(true);
				recordedLectureVo.setLecture(newRecordedLecture);
				recordedLectures.add(recordedLectureVo);
			}
		}
		List<Lecture> oldRecordedLectures = getOldRecordedVideoLectures(userId, courseId, lectureKeyword, lectureTopic);
		for(Lecture oldRecordedLecture : oldRecordedLectures)
		{
			recordedLectureVo = new RecordedLectureVO();
			// Setting the isModerator field to default NULL since editing is not possible for older recorded video files
			recordedLectureVo.setIsModerator("N");
			recordedLectureVo.setUseNewPlayer(false);
			recordedLectureVo.setLecture(oldRecordedLecture);
			recordedLectures.add(recordedLectureVo);
		}
		return recordedLectures;
		
	}

	/**
	 * Gets the old recorded video lectures.
	 *
	 * @param userId the user id
	 * @param courseId the course id
	 * @param lectureKeyword the lecture keyword
	 * @param lectureTopic the lecture topic
	 * @return the old recorded video lectures
	 * @throws AViewException
	 */
	private static List<Lecture> getOldRecordedVideoLectures(Long userId, Long courseId, String lectureKeyword, String lectureTopic) throws AViewException
	{
		User user = UserHelper.getUser(userId); 
		
		List<Lectures> oldLectures = LecturesDAO.getOldRecordedVideoLectures(courseId, user.getUserId(), user.getRole(), lectureKeyword, lectureTopic);
		logger.debug(" Count of old lectures :: " + oldLectures.size());
		
		List<Lecture> oldRecordedLectures = new ArrayList<Lecture>();
		
		Lecture oldRecordedLecture = null;
				
		for(Lectures oldLecture : oldLectures)
		{
			oldRecordedLecture = new Lecture();			
			oldRecordedLecture.setLectureId(oldLecture.getLectureId());
			oldRecordedLecture.setLectureName(oldLecture.getLectureTopic());
			//oldRecordedLecture.setStartDate(oldLecture.getStartDate());
			oldRecordedLecture.setRecordedPresenterVideoUrl(oldLecture.getVideoFilePath());
			oldRecordedLecture.setRecordedVideoFilePath(oldLecture.getVideoFileName());
			oldRecordedLecture.setRecordedContentFilePath(oldLecture.getXmlFilePath());
			oldRecordedLecture.setKeywords(oldLecture.getKeyWords());
			oldRecordedLectures.add(oldRecordedLecture);
			if(oldLecture.getStartDate() != null)
			{
				oldRecordedLecture.setStartDate(TimestampUtils.strToDate(oldLecture.getStartDate(), "/", 0, 1, 2));
			}
			else
			{
				oldRecordedLecture.setStartDate(TimestampUtils.strToDate("1/1/1900", "/", 0, 1, 2));
			}
		}
		return oldRecordedLectures;
	}

	//Code change for NIC start
	//To check if there is any change in the schedule of the lecture
	/**
	 * Did lecture schedule changed.
	 *
	 * @param oldLecture the old lecture
	 * @param newLecture the new lecture
	 * @return true, if did lecture schedule changed
	 */
	public static boolean didLectureScheduleChanged(Lecture oldLecture, Lecture newLecture)
	{
		boolean scheduleChanged = false;
		removeDateTimes(oldLecture);
		removeDateTimes(newLecture);
		if(!oldLecture.getStartTime().equals(newLecture.getStartTime()))
		{
			scheduleChanged = true;
		}
		else if(!oldLecture.getEndTime().equals(newLecture.getEndTime()))
		{
			scheduleChanged = true;
		}
		if(!oldLecture.getStartDate().equals(newLecture.getStartDate()))
		{
			scheduleChanged = true;
		}
		return scheduleChanged;
	}
	//Code change for NIC end
	
	//Code change for NIC start
	//To send email to all the registered users in case of schedule change
	/**
	 * Send email to class registrants on lecture schedule change.
	 *
	 * @param lecture the lecture
	 * @throws AViewException
	 */
	private static void sendEmailToClassRegistrantsOnLectureScheduleChange(Lecture lecture) throws AViewException
	{
		List<ClassRegistration> classRegistrations = ClassRegistrationHelper.searchForClassRegister(null, null, null, lecture.getClassId(), null, null, null);
		List<String> emailIdOfRegisteredUsers = new ArrayList<String>();
		for(ClassRegistration classRegistration : classRegistrations)
		{
			emailIdOfRegisteredUsers.add(classRegistration.getUser().getEmail());
		}
		EmailHelper.sendEmailForLectureScheduleChange(emailIdOfRegisteredUsers, lecture, ClassHelper.getClass(lecture.getClassId()));
	}
	//Code change for NIC end
	//Fix for Bug#15278 :Start
	private static void setLectureNameWithDate(Lecture lecture) throws AViewException
	{
		if(lecture.getLectureName().indexOf("~") < 0)
		{
			GregorianCalendar todayCal = TimestampUtils.removeTimeCalendar(new Date());
			GregorianCalendar lectureStartCal  = null;
			GregorianCalendar classStartCal  = new GregorianCalendar();
		 	classStartCal.setTime(lecture.getStartDate()) ;
		 	
		 	if(todayCal.before(classStartCal)) // checking the start date of a class , if today's date is before the start date of class
		 	{	 								// then set the start date of lecture to  start date of class 	 		
		 		lectureStartCal = classStartCal;	 	
		 	}
		 	else
		 	{
		 		lectureStartCal = todayCal;
		 	}
		 	lecture.setLectureName(lecture.getLectureName()+"~"+lectureStartCal.get(Calendar.YEAR)+"-"+(lectureStartCal.get(Calendar.MONTH)+1)+"-"+lectureStartCal.get(Calendar.DATE));
		}
	}
	//Fix for Bug#15278 :End

	private static void sendEmailToClassRegistrantsOnLectureCreation(Lecture lecture) throws AViewException
	{
		List<ClassRegistration> classRegistrations = ClassRegistrationHelper.searchForClassRegister(null, null, null, lecture.getClassId(), null, null, null);
		List<String> emailIdOfRegisteredUsers = new ArrayList<String>();
		for(ClassRegistration classRegistration : classRegistrations)
		{
			if(classRegistration.getUser().getEmail() != null)
			{
				emailIdOfRegisteredUsers.add(classRegistration.getUser().getEmail());
			}
		}
		Class cls = ClassHelper.getClass(lecture.getClassId());
		EmailHelper.sendEmailForLectureCreation(emailIdOfRegisteredUsers, lecture, cls, cls.getInstituteName());
	}

	/**
	 * Function to validate lecture 
	 * @param lecture
	 * @return Response
	 * @throws AViewException
	 */
	public static Object validationCheckForLecture(Lecture lecture) throws AViewException
 	{
		if(lecture.getStartDate() == null)
		{
			return "Entered start Date is not valid or start Date is not given.";
		}
		else
		{
			lecture.setStartDate(TimestampUtils.removeTime(lecture.getStartDate()));
		}
		if(lecture.getStartTime() == null)
		{
			return "Entered start time is not valid or start time is not given.";
		}
		else
		{
			lecture.setStartTime(TimestampUtils.removeDateAndMillis(lecture.getStartTime()));
		}
		if(lecture.getEndTime() == null)
		{
			return "Entered end time is not valid or end time is not given.";
		}
		else
		{
			lecture.setEndTime(TimestampUtils.removeDateAndMillis(lecture.getEndTime()));
		}
		if(lecture.getRecordedContentFilePath() == null || lecture.getRecordedContentFilePath() == "")
		{
			lecture.setRecordedContentFilePath(null);
		}
		if(lecture.getRecordedContentUrl() == null || lecture.getRecordedContentUrl() == "")
		{
			lecture.setRecordedContentUrl(null);
		}
		if(lecture.getRecordedPresenterVideoUrl() == null || lecture.getRecordedPresenterVideoUrl() == "")
		{
			lecture.setRecordedPresenterVideoUrl(null);
		}
		if(lecture.getRecordedVideoFilePath() == null || lecture.getRecordedVideoFilePath() == "")
		{
			lecture.setRecordedVideoFilePath(null);
		}
		if(lecture.getRecordedViewerVideoUrl() == null || lecture.getRecordedViewerVideoUrl() == "")
		{
			lecture.setRecordedViewerVideoUrl(null);
		}
		if(lecture.getLectureNumber() == 0)
		{
			lecture.setLectureNumber(0);
		}
		if (lecture.getLectureName() == null || lecture.getLectureName() == "" || lecture.getLectureName().length()>255) 
		{
			return "Entered LectureName is not valid or LectureName is not given.";
		} 
		else 
		{
			String AllowedCharError=null;
			AllowedCharError = ValidationUtils.AllowedCharForname(lecture.getLectureName());
			if (AllowedCharError != null) 
			{
				return AllowedCharError+" in LectureName.";
			}
		}
		if (lecture.getLectureNumber() != 0) 
		{
			return "Entered LectureNumber is not valid or LectureNumber is not given.";
		} 
		if (lecture.getStartDate() == null) 
		{
			return "Entered StartDate is not valid or StartDate is not given.";
		}
		if (lecture.getStartTime() == null) 
		{
			return "Entered StartTime is not valid or StartTime is not given.";
		}
		if (lecture.getEndTime() == null) 
		{
			return "Entered EndTime is not valid or EndTime is not given.";
		}
		if (lecture.getClassId() == null) 
		{
			return "Entered ClassId is not valid or ClassId is not given.";
		} 
		else 
		{
			String intError = null;
			intError = ValidationUtils.integerOnly(lecture.getClassId().toString());
			if (intError != null) 
			{
				return intError+" in ClassId.";
			}
		}
		if (lecture.getKeywords() == null ||lecture.getKeywords().equals("") ) 
		{
			logger.debug("Keyword is not specified in :Lecture creation.");
			//return Response.status(Status.BAD_REQUEST).entity("Keyword,Not valid").build();
		} 
		else 
		{
			String AllowedCharError = null;
			AllowedCharError = ValidationUtils.AllowedCharForname(lecture.getKeywords());
			if (AllowedCharError != null) 
			{
				return AllowedCharError+" in Keywords.";
			}
			if(lecture.getKeywords().length()>255)
			{
				return "Length exceeds in the keywords";
			}
		}
		return null;
	}
	
	public static Object validationDateTimeCheckwithLecture(Date startDateValue,Date lectureStartTimeValue,Date lectureEndTimeValue) throws AViewException
	{
		Date lectureStartTime = lectureStartTimeValue;
		Date lectureEndTime = lectureEndTimeValue;
		Date startDate = TimestampUtils.removeTime(startDateValue);
		Date currentDate = TimestampUtils.removeTime(TimestampUtils.getCurrentDate());
		Date currentTime = TimestampUtils.removeDateAndMillis(TimestampUtils.getCurrentTimestamp());
		if(startDate.equals(currentDate))
		{
			logger.debug("The current date and the start date of lecture are same");
			if(lectureStartTime.after(currentTime))
			{
				logger.debug("Start time of lecture is greater than current time ");
			}
			else if(lectureStartTime.before(currentTime))
			{
				return "Start time of lecture is less than current time";
			}
			else if(lectureStartTime.equals(currentTime))
			{
				return "Start time of lecture and current time can't be same";
			}
			if(lectureStartTime.after(lectureEndTime))
			{
				return "Start time of lecture is greater than lecture end time";
				
			}
			else if(lectureStartTime.before(lectureEndTime))
			{
				logger.debug("Start time of lecture is less than lecture end time ");
			}
			else if(lectureStartTime.equals(lectureEndTime))
			{
				return "Start time of lecture and current time can't be same";
			}	
		}
		else if(startDate.before(currentDate))
		{
			return "The current date greater than the start date of lecture";
		}
		else if(startDate.after(currentDate))
		{
			logger.debug("The current date is after the start date of lecture");
			if(lectureStartTime.after(lectureEndTime))
			{
				return "Start time of lecture is greater than lecture end time";
				
			}
			else if(lectureStartTime.before(lectureEndTime))
			{
				logger.debug("Start time of lecture is less than lecture end time ");
			}
			else if(lectureStartTime.equals(lectureEndTime))
			{
				return "Start time of lecture and current time can't be same";
			}	
		}
		return null;
	}
	
	public static Object validationDateTimeCheckwithClass(Date startDateValue,Date endDateValue,Date lectureStartDate) throws AViewException
	{
		//Date currentDate =TimestampUtils.removeTime(TimestampUtils.getCurrentDate());
		//Date currentTime =TimestampUtils.removeDateAndMillis(TimestampUtils.getCurrentTimestamp());
		Date startDate = TimestampUtils.removeTime(startDateValue);
		Date endDate = TimestampUtils.removeTime(endDateValue);
		if(startDate.after(lectureStartDate))
		{
			return "Lecture start date is less than class start date";
			//logger.debug("The start date is greater than the start date of lecture to be updated");
		}
		else if(startDate.before(lectureStartDate))
		{
			//return Response.status(Status.BAD_REQUEST).entity("Lecture start date is less than class start date").build();
			logger.debug("The lecture start date is greater than the class start date");
		}
		else if(startDate.equals(lectureStartDate))
		{
			logger.debug("The class start date and the lecture start date are equal");
		}
		if(endDate.after(lectureStartDate))
		{
			logger.debug("Lecture start date is less than class end date");
		}
		else if(endDate.before(lectureStartDate))
		{
			return "The lecture start date is greater than the class end date";
			
		}
		else if(endDate.equals(lectureStartDate))
		{
			logger.debug("The class end date and the lecture start date are equal");
		}
		//comparison with lecture start time and class start time
		/*if(lectureStartTime.after(classStartTime) && lectureStartTime.after(currentTime))
		{
			logger.debug("Enter lecture start time is greater than current time  & lecture start time after current time");	
		}
		else
		{
			errorMessage ="lecture start time is less than current time/lecture start time is less than class start time";
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}*/
		
		//comparison with lecture end time and class end time
		/*if(lectureEndTime.before(classEndTime) && lectureEndTime.after(currentTime))
		{
			logger.debug("Enter lecture end time is less than current time  & lecture end time after current time");	
		}
		else
		{
			errorMessage ="Lecture end time is less than current time/lecture end time is less than class start time";
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}*/

		//comparison with lecture start time and lecture end time
		/*if(lectureStartTime != null && lectureEndTime != null)
		{ 
			if(lectureStartTime.equals(lectureEndTime))
			{
				result="Lecture start time and end time can't be same";
				return Response.status(Status.BAD_REQUEST).entity(result).build();
			}
			else if(lectureStartTime.after(currentTime) && lectureStartTime.before(lectureEndTime))
			{
				if(lectureStartTime.after(lectureEndTime ))
				{
					errorMessage ="Enter lecture start time is great than lecture end time";	
					return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
				}
				else if(lectureStartTime.before(lectureEndTime))
				{
					logger.debug("Enter lecture start time is less than lecture end time");	
				}
				else if(lectureStartTime.equals(lectureEndTime))
				{
					errorMessage ="Lecture start time and lecture end time can't be same";
					return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
				}
			}
			else if(lectureStartTime.before(currentTime))
			{
				errorMessage ="Lecture start time is less than current time/end time is less than start time";
				return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
			}
			
		}*/
		return null;
	}
	
	/**
	 * API to create lecture
	 * @param adminId
	 * @param lectureDetails as JSON
	 * @return Response
	 * @throws AViewException
	 */
	@RequestMapping(value = "/createlecture.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response createLecture(@RequestParam("adminId") Long adminId,
			@RequestParam("lectureDetails") String lectureDetails,
			@RequestParam("sendEmail") String sendEmail) throws AViewException {
		
		String result = new String();
		logger.debug("Enter lecture Details::lecture ");
		Lecture lecture = null;
		String errorMessage = null;
		Response dateValidationResponse ;
		Response timeValidationResponse ;
		String timeValidationErrorMessage = null;
		String dateValidationErrorMessage = null;
		User admin = null;
		
		try
		{
			JSONObject obj = new JSONObject(lectureDetails);
			String startTimeString = obj.getString("startTime");
			timeValidationResponse = ClassHelper.timeValidation(startTimeString);
			if(timeValidationResponse != null)
			{
				timeValidationErrorMessage = timeValidationResponse.getEntity().toString();
				if(timeValidationErrorMessage != null)
				{
					return Response.status(Status.BAD_REQUEST).entity("Start time "+timeValidationErrorMessage).build();
				}
				
			}
			String endTimeString = obj.getString("endTime");
			timeValidationResponse = ClassHelper.timeValidation(endTimeString);
			if(timeValidationResponse != null)
			{
				timeValidationErrorMessage = timeValidationResponse.getEntity().toString();
				if(timeValidationErrorMessage != null)
				{
					return Response.status(Status.BAD_REQUEST).entity("End time"+timeValidationErrorMessage).build();
				}
				
			}
			String startDateString = obj.getString("startDate");;
			dateValidationResponse = ClassHelper.dateValidation(startDateString);
			if(dateValidationResponse != null)
			{
				dateValidationErrorMessage = dateValidationResponse.getEntity().toString();
				if(dateValidationErrorMessage != null)
				{
					return Response.status(Status.BAD_REQUEST).entity(dateValidationErrorMessage +" in start date.").build();
				}
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		try
		{
			Object resultObjectAdmin = UserHelper.userValidCheck(Constant.ADMIN_ID,adminId);
			if(User.class.isInstance(resultObjectAdmin))
			{
				admin = (User)resultObjectAdmin;
			}
			else
			{
				errorMessage = resultObjectAdmin.toString();
				return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
			}
			Object resultObject = JSONParserUtils.readJSONAsObject(lectureDetails,Lecture.class);
			if(Lecture.class.isInstance(resultObject))
			{
				lecture = (Lecture)resultObject;
			}
			else
			{
				errorMessage = resultObject.toString();
				logger.error(errorMessage);
			}
			if(lecture != null)
			{
				Object validationMessageForLecture = validationCheckForLecture(lecture);
				if(validationMessageForLecture != null)
				{
					return Response.status(Status.BAD_REQUEST).entity(validationMessageForLecture.toString()).build();
				}
				Class aviewClass = null;
				Long classId = lecture.getClassId();
				Object resultObjectClass = ClassHelper.classValidCheck(classId);
				if(Class.class.isInstance(resultObjectClass))
				{
					aviewClass = (Class)resultObjectClass;
					if(aviewClass != null && aviewClass.getStatusId() == StatusHelper.getActiveStatusId())
					{
						Object validationMessageForAdmin = ClassHelper.adminValidation(aviewClass, admin);
						if(validationMessageForAdmin != null)
						{
							return Response.status(Status.BAD_REQUEST).entity(validationMessageForAdmin.toString()).build();
						}
					}
				}
				else
				{
					String classErrorMessage = resultObjectClass.toString();
					return Response.status(Status.BAD_REQUEST).entity(classErrorMessage).build();
				}
				Date classStartTime = null;
				//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");
				if(aviewClass.getStartTime() != null)
				{
					classStartTime = TimestampUtils.removeDateAndMillis(aviewClass.getStartTime());
				}
				else
				{
					//	to set the class start time for checking with lecture start time
					/*try 
					{
						classStartTime =TimestampUtils.removeDate(formatter.parse("1899-12-31T00:00:00.000+0530"));
					} 
					catch (java.text.ParseException e)
					{
						e.printStackTrace();
					}*/
				}
				
				Date classEndTime = null;
				if(aviewClass.getEndTime() != null)
				{
					classEndTime = TimestampUtils.removeDateAndMillis(aviewClass.getEndTime());
				}
				else
				{
					//	to set the class start time for checking with lecture end time
					/*try {
						classEndTime =TimestampUtils.removeDate(formatter.parse("1899-12-31T23:59:59.000+0530"));
					} catch (java.text.ParseException e) {
						e.printStackTrace();
					}*/
					//classEndTime = formatter.parse();
				}
				
				Date startDate = aviewClass.getStartDate();
				Date endDate = aviewClass.getEndDate();
				Date lectureStartDate = TimestampUtils.removeTime(lecture.getStartDate());
				Date lectureStartTime = lecture.getStartTime();
				Date lectureEndTime = lecture.getEndTime();
				Object validationMessageForDataTimewithClass = validationDateTimeCheckwithClass(startDate, endDate, lectureStartDate);
				if(validationMessageForDataTimewithClass !=null)
				{
					return Response.status(Status.BAD_REQUEST).entity(validationMessageForDataTimewithClass.toString()).build();
				}
				Object validationMessageForDateTimewithLecture = validationDateTimeCheckwithLecture(lectureStartDate, lectureStartTime, lectureEndTime);				if(validationMessageForDateTimewithLecture != null)
				if(validationMessageForDateTimewithLecture !=null)
				{
					return Response.status(Status.BAD_REQUEST).entity(validationMessageForDateTimewithLecture.toString()).build();
				}
			}
			else
			{
				return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
			}
			Long resultId = 0l;
			LectureHelper.createLecture(lecture, adminId);
			if(sendEmail.equals("Y") || sendEmail.equals("y"))
			{
				sendEmailToClassRegistrantsOnLectureCreation(lecture);
			}
			resultId = lecture.getLectureId();
			logger.debug("Exit lecture Details on success::lecture.");
			return Response.ok().entity(resultId).build();
		}
		catch (NumberFormatException nfe) 
		{
			result = "invalid";
			logger.debug("Exit lecture Details on invalid request::lecture.");
			return Response.status(Status.BAD_REQUEST).entity(result).build();    
		}
		catch(AViewException ae)
		{
			SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
			if(ae.getMessage().equals("Duplicate entry '"+ lecture.getLectureName()+"-"+lecture.getClassId()+"' for key 'lecture_name'"))
			{
				result = "The given lecture name already exists. Please try a different lecture name.";
			}
			else if(ae.getMessage().equals("Duplicate entry '"+formatDateTime.format(lecture.getStartDate())+"-"+formatTime.format(lecture.getStartTime())+"-"+formatTime.format(lecture.getEndTime())+"-"+lecture.getClassId() +"' for key 'lecture_date'"))
			{
				result = "The lecture already exists at this time in same class.Please try for different time.";
			}
			else if(ae.getMessage().equals("Duplicate entry '"+lecture.getLectureId()+"' for key 'PRIMARY'"))
			{
				result = "The given lecture id already exists. Please try a different lecture id.";
			}
			else if(ae.getMessage().equals("Given course is not valid"))
			{
				result = "Entered class id is not valid or doesn't exist.";
			}
			else
			{
				result = "Error during log.Possible reason(s) : 1. Unexpected data, 2. Unknown.";
			}
			logger.debug("Exit lecture Details on error durning log::lecture.");
			return Response.status(Status.BAD_REQUEST).entity(result).build();    
		}
	}

	/**
	 * API to search lecture.
	 * @param adminId
	 * @param classId 
	 * @return Response
	 * @throws AViewException
	 */
	@RequestMapping(value = "/searchlecture.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response lectureSearch(@RequestParam("adminId") Long adminId,@RequestParam("classId") Long classId) throws AViewException
	{
		logger.debug("Enter lecture search::lecture search ");
		String errorMessage = null;
		Class aviewClass = null;
		List<Lecture> lectureArrayList = null;
		ArrayList lectureDetailsArray = new ArrayList();
		User admin = null;
		String result = null;
		Object resultObjectAdmin = UserHelper.userValidCheck(Constant.ADMIN_ID,adminId);
		if(User.class.isInstance(resultObjectAdmin))
		{
			admin = (User)resultObjectAdmin;
		}
		else
		{
			errorMessage = resultObjectAdmin.toString();
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		try
		{
			Object resultObjectClass = ClassHelper.classValidCheck(classId);
			if(Class.class.isInstance(resultObjectClass))
			{
				aviewClass = (Class)resultObjectClass;
			}
			else
			{
				errorMessage = resultObjectClass.toString();
				return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
			}
			if(admin.getRole().equals(Constant.ADMIN_ROLE) || admin.getRole().equals(Constant.MASTER_ADMIN_ROLE))
			{
				Object validationMessageForAdmin = ClassHelper.adminValidation(aviewClass, admin);
				if(validationMessageForAdmin != null)
				{
					return Response.status(Status.BAD_REQUEST).entity(validationMessageForAdmin.toString()).build();
				}
				lectureArrayList= (List<Lecture>) getLecturesForClass(aviewClass.getClassId());
			}
			//removed the search lecture for normal users
/*			else 
			{
				List classRegistration = ClassRegistrationHelper.getClassRegisterationForUser(aviewClass.getClassId(), admin.getUserId());
				if(!(classRegistration.isEmpty()))
				{
					lectureArrayList= (List<Lecture>) getLecturesForClass(aviewClass.getClassId());
				}
				else
					
				{
					return Response.status(Status.BAD_REQUEST).entity("No active lecture details returned for the given classId and  userId").build();
				}
			}*/
			if(lectureArrayList == null || lectureArrayList.size() == 0 ) 
			{
				return Response.status(Status.BAD_REQUEST).entity("No active lecture details returned for the given classId and  userId").build();
			} 
			else
			{
				ArrayList lectureArray = new ArrayList();
				for(Lecture lectureDetails:lectureArrayList)
				{
					lectureArray = new ArrayList();
					lectureArray.add("lecture: " + lectureDetails.getDisplayName());
					lectureArray.add("lectureId: " + lectureDetails.getLectureId());
					lectureArray.add("class: " + lectureDetails.getClassName());
					lectureArray.add("course: " + lectureDetails.getCourseName());
					lectureArray.add("institute: " + lectureDetails.getInstituteName());
					//we are added substring for trimming the date to dateformat yyyy-mm-dd 
					// for eg:startdate = 2011-06-14 00:00:00.0 in db by trimming startdate becomes 2011-06-14
					String startdate = lectureDetails.getStartDate().toString().substring(0,10);
					lectureArray.add("date: " + startdate);
					//we are added substring for trimming the time to timeformat hh:mm:ss 
					// for eg:startTime = 2011-06-14 00:00:00.0 in db by trimming startTime becomes 00:00:00
					String startTime = lectureDetails.getStartTime().toString().substring(11,19);
					lectureArray.add("startTime: " + startTime);
					//we are added substring for trimming the time to timeformat hh:mm:ss 
					// for eg:endTime = 2011-06-14 00:00:00.0 in db by trimming endTime becomes 00:00:00
					String endTime = lectureDetails.getEndTime().toString().substring(11,19);
					lectureArray.add("endTime: " + endTime);
					lectureArray.add("keyword: " + lectureDetails.getKeywords());
					lectureDetailsArray.add(lectureArray);
				}
			}
			logger.debug("Exit lecture search on success:lecture search");
			return Response.status(Status.OK).entity(lectureDetailsArray).build();
		}
		catch (NumberFormatException nfe) 
		{
			result = "invalid";
			logger.debug("Exit lecture search on invalid request::lecture.");
			return Response.status(Status.BAD_REQUEST).entity(result).build();    
		}
		catch(AViewException ae)
		{
			if(ae.getMessage().equals("Given course is not valid"))
			{
				result = "Given search criteria provided is not valid or doesn't exist";
			}
			logger.debug("Exit lecture search on error durning log::lecturer."); 
			return Response.status(Status.BAD_REQUEST).entity(result).build();    
		}
	}
	
	/**
	 * API to delete lecture.
	 * @param adminId
	 * @param lectureId 
	 * @return Response
	 * @throws AViewException
	 */
	@RequestMapping(value = "/deletelecture.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response deleteLecture(@RequestParam("adminId") Long adminId,@RequestParam("lectureId") Long lectureId) throws AViewException 
	{
		logger.debug("Enter lecture delete::lecture delete ");
		String errorMessage = null;
		String result = null;
		User admin = null;
		Object resultObjectAdmin = UserHelper.userValidCheck(Constant.ADMIN_ID,adminId);
		if(User.class.isInstance(resultObjectAdmin))
		{
			admin = (User)resultObjectAdmin;
		}
		else
		{
			errorMessage = resultObjectAdmin.toString();
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		if(lectureId == null)
		{
			lectureId = 0l;
		}
		try
		{
			Lecture lecture = getLecture(lectureId);
			if (lecture!=null && lecture.getStatusId() == StatusHelper.getActiveStatusId())
			{
				Class aviewClass=ClassHelper.getClass(lecture.getClassId());
				if(aviewClass != null && aviewClass.getStatusId() == StatusHelper.getActiveStatusId())
				{
					Object validationMessageForAdmin = ClassHelper.adminValidation(aviewClass, admin);
					if(validationMessageForAdmin != null)
					{
						return Response.status(Status.BAD_REQUEST).entity(validationMessageForAdmin.toString()).build();
					}
				}
				Date startDate = TimestampUtils.removeTime(lecture.getStartDate());
				Date currentDate = TimestampUtils.removeTime(TimestampUtils.getCurrentDate());
				if(startDate.equals(currentDate))
				{
					return Response.status(Status.BAD_REQUEST).entity("Can't delete on going lecture").build();
				}
				//Can delete the past lecture
				/*
				else if (startDate.before(currentDate))
				{
					return Response.status(Status.BAD_REQUEST).entity("Can't delete the past lecture").build();
				}
				*/
				deleteLecture(lectureId);
				logger.debug("Exit lecture delete on success:lecture delete");
				return Response.status(Status.OK).entity("Deleted "+lecture.getLectureName() +"(ID: " +lectureId + ") successfully").build();
			}
			else
			{
				return Response.status(Status.BAD_REQUEST).entity("Given lecture id is not valid or doesn't exist").build();
			}
		}
		catch (NumberFormatException nfe) 
		{
			result = "invalid";
			logger.debug("Exit lecture delete on invalid request::lecture.");
			return Response.status(Status.BAD_REQUEST).entity(result).build();    
		}
		catch(AViewException ae)
		{
			if(ae.getMessage().equals("Given course is not valid"))
			{
				result = "Please enter a valid lecture id";
				logger.debug("Exit lecture delete on error durning log::lecture."); 
				return Response.status(Status.BAD_REQUEST).entity(result).build();    
			}
			if(ae.getMessage().equals("Cannot delete or update a parent row: " +
					"a foreign key constraint fails (`aview`.`audit_lecture`, CONSTRAINT `audit_lecture_lecture_id` " +
					"FOREIGN KEY (`lecture_id`) REFERENCES `lecture` (`lecture_id`) " +
					"ON DELETE NO ACTION ON UPDATE NO ACTION)"))
			{
				result = "Can't delete the past lecture,since the lecture has happened";
				logger.debug("Exit lecture delete on error durning log::lecture."); 
				return Response.status(Status.BAD_REQUEST).entity(result).build();  
			}
		}
		return null;
	}
	
	/**
	 * API to update lecture
	 * @param adminId
	 * @param lectureDetails
	 * @return Response
	 * @throws AViewException
	 */
	@RequestMapping(value = "/updatelecture.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response updateLecture(@RequestParam("adminId") Long adminId,
			@RequestParam("lectureDetails") String lectureDetails) throws AViewException
	{
		
		logger.debug("Enter lecture Details::lecture ");
		
		String result = new String();
		Date currentDate =TimestampUtils.removeTime(TimestampUtils.getCurrentDate());
		Date currentTime =TimestampUtils.removeDateAndMillis(TimestampUtils.getCurrentTimestamp());
		
		User admin = null;
		 
		Lecture lecture = null;
		Lecture tempLectureArray = null;
		
		String errorMessage = null;
		String timeValidationErrorMessage = null;
		String dateValidationErrorMessage = null;
		
		Response dateValidationResponse ;
		Response timeValidationResponse ;
				
		Date startDate = null;
		Date endDate = null;

		Date lectureStartDate = null;
		Date lectureStartTime = null;
		Date lectureEndTime = null;
		
		boolean startDateChange = false;
		boolean startTimeChange = false;
		boolean endTimeChange = false;
		
		try
		{
			JSONObject obj = new JSONObject(lectureDetails);
			String startTimeString = obj.getString("startTime");
			timeValidationResponse = ClassHelper.timeValidation(startTimeString);
			if(timeValidationResponse != null)
			{
				timeValidationErrorMessage = timeValidationResponse.getEntity().toString();
				if(timeValidationErrorMessage != null)
				{
					return Response.status(Status.BAD_REQUEST).entity("Start time "+timeValidationErrorMessage).build();
				}
			}
			String endTimeString = obj.getString("endTime");
			timeValidationResponse = ClassHelper.timeValidation(endTimeString);
			if(timeValidationResponse != null)
			{
				timeValidationErrorMessage = timeValidationResponse.getEntity().toString();
				if(timeValidationErrorMessage != null)
				{
					return Response.status(Status.BAD_REQUEST).entity("End time"+timeValidationErrorMessage).build();
				}
			}
			String startDateString = obj.getString("startDate");;
			dateValidationResponse = ClassHelper.dateValidation(startDateString);
			if(dateValidationResponse != null)
			{
				dateValidationErrorMessage = dateValidationResponse.getEntity().toString();
				if(dateValidationErrorMessage != null)
				{
					return Response.status(Status.BAD_REQUEST).entity(dateValidationErrorMessage +" in start date.").build();
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		Object resultObjectAdmin = UserHelper.userValidCheck(Constant.ADMIN_ID,adminId);
		if(User.class.isInstance(resultObjectAdmin))
		{
			admin = (User)resultObjectAdmin;
		}
		else
		{
			errorMessage = resultObjectAdmin.toString();
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		
		Object resultObject = JSONParserUtils.readJSONAsObject(lectureDetails,Lecture.class);
		if(Lecture.class.isInstance(resultObject))
		{
			tempLectureArray = (Lecture)resultObject;
			Object resultObjectUpdater = getLecture(tempLectureArray.getLectureId());
			if(Lecture.class.isInstance(resultObjectUpdater))
			{
				lecture = (Lecture)resultObjectUpdater;
			}
			else
			{
				if(resultObjectUpdater == null)
				{
					return Response.status(Status.BAD_REQUEST).entity("Lecture Id doesn't exist").build();
				}
				errorMessage = resultObjectUpdater.toString();
				return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
			}
			if(lecture != null && lecture.getStatusId() == StatusHelper.getActiveStatusId())
			{
				if(tempLectureArray.getLectureName() != null)
				{
					lecture.setLectureName(tempLectureArray.getLectureName());
				}
				else if(tempLectureArray.getLectureName() == null) 
				{
					lecture.setLectureName(lecture.getDisplayName());
				}
				if(tempLectureArray.getStartDate() != null)
				{
					if(TimestampUtils.removeTime(lecture.getStartDate()).after(currentDate))
					{
						lecture.setStartDate(tempLectureArray.getStartDate());
						startDateChange = true;
					}
					else if(TimestampUtils.removeTime(lecture.getStartDate()).before(currentDate))
					{
						return Response.status(Status.BAD_REQUEST).entity("Lecture can't be modified,since this lecture is already been started").build();
					}
					else if(TimestampUtils.removeTime(lecture.getStartDate()).equals(currentDate))
					{
						logger.debug("lecture start  date and current date is same so no need to set the start date on updation");
						if(TimestampUtils.removeDateAndMillis(lecture.getStartTime()).before((currentTime)))
						{
							return Response.status(Status.BAD_REQUEST).entity("Lecture can't be modified,since this lecture is already been started").build();
						}
						else if(TimestampUtils.removeDateAndMillis(lecture.getStartTime()).after(currentTime))
						{
							lecture.setStartDate(tempLectureArray.getStartDate());
							startDateChange = true;
						}
						else if(TimestampUtils.removeDateAndMillis(lecture.getStartTime()).equals(currentTime))
						{
							return Response.status(Status.BAD_REQUEST).entity("Lecture can't be modified,since this lecture is already been started").build();
						}
					}
				}
				if(tempLectureArray.getStartTime() != null)
				{
					if(TimestampUtils.removeTime(lecture.getStartDate()).equals(currentDate))
					{
						if(TimestampUtils.removeDateAndMillis(lecture.getStartTime()).before(TimestampUtils.removeDateAndMillis(tempLectureArray.getStartTime())))
						{
							lecture.setStartTime(tempLectureArray.getStartTime());
							startTimeChange = true;
						}
						else if(TimestampUtils.removeDateAndMillis(lecture.getStartTime()).after(TimestampUtils.removeDateAndMillis(tempLectureArray.getStartTime())))
						{
							if(TimestampUtils.removeDateAndMillis(lecture.getStartTime()).before((currentTime)))
							{
								return Response.status(Status.BAD_REQUEST).entity("Lecture can't be modified,since this lecture is already been started").build();
							}
							else if(TimestampUtils.removeDateAndMillis(lecture.getStartTime()).after(currentTime))
							{
								lecture.setStartTime(tempLectureArray.getStartTime());
								startTimeChange = true;
							}
							else if(TimestampUtils.removeDateAndMillis(lecture.getStartTime()).equals(currentTime))
							{
								return Response.status(Status.BAD_REQUEST).entity("Lecture can't be modified,since this lecture is already been started").build();
							}
						}
						else if(TimestampUtils.removeDateAndMillis(lecture.getStartTime()).equals(TimestampUtils.removeDateAndMillis(tempLectureArray.getStartTime())))
						{
							return Response.status(Status.BAD_REQUEST).entity("Lecture old start time and new start time is same").build();
						}
					}
					else if(TimestampUtils.removeTime(lecture.getStartDate()).before(currentDate))
					{
						return Response.status(Status.BAD_REQUEST).entity("Lecture can't be modified,since this lecture is already been started").build();
					}
					else if(TimestampUtils.removeTime(lecture.getStartDate()).after(currentDate))
					{
						lecture.setStartTime(tempLectureArray.getStartTime());
						startTimeChange = true;
					}
				}
				if(tempLectureArray.getEndTime() != null)
				{
					if(TimestampUtils.removeTime(lecture.getStartDate()).equals(currentDate))
					{
						//Commented to fix the bug no:14469
						/*if(TimestampUtils.removeDateAndMillis(lecture.getStartTime()).before(currentTime))
						{
							return Response.status(Status.BAD_REQUEST).entity("Lecture can't be modified,since this lecture is already been started").build();
						}
						else if(TimestampUtils.removeDateAndMillis(lecture.getStartTime()).equals(currentTime))
						{
							return Response.status(Status.BAD_REQUEST).entity("Lecture can't be modified,since this lecture is already been started").build();
						}
						else*/
						if(TimestampUtils.removeDateAndMillis(lecture.getEndTime()).before(currentTime))
						{
							return Response.status(Status.BAD_REQUEST).entity("Lecture can't be modified,since this lecture is already been ended").build();
						}
						if(TimestampUtils.removeDateAndMillis(lecture.getStartTime()).after(currentTime))
						{
							lecture.setEndTime(tempLectureArray.getEndTime());
							endTimeChange = true;
						}
						if(TimestampUtils.removeDateAndMillis(tempLectureArray.getEndTime()).before(currentTime))
						{
							return Response.status(Status.BAD_REQUEST).entity("Lecture can't be modified,since this lecture is already been started").build();
						}
						else if(TimestampUtils.removeDateAndMillis(tempLectureArray.getEndTime()).after(currentTime))
						{
							lecture.setEndTime(tempLectureArray.getEndTime());
							//endTimeChange = true;
						}
					}
					else if(TimestampUtils.removeTime(lecture.getStartDate()).before(currentDate))
					{
						return Response.status(Status.BAD_REQUEST).entity("Lecture can't be modified,since this lecture is already been started").build();
					}
					else if(TimestampUtils.removeTime(lecture.getStartDate()).after(currentDate))
					{
						lecture.setEndTime(tempLectureArray.getEndTime());
						endTimeChange = true;
					}
				}
				if(tempLectureArray.getKeywords() != null)
				{
					lecture.setKeywords(tempLectureArray.getKeywords());
				}
				if(tempLectureArray.getClassId() != 0)
				{
					lecture.setClassId(tempLectureArray.getClassId());
				}
			}
			else
			{
				return Response.status(Status.BAD_REQUEST).entity("Lecture Id doesn't exist").build();
			}
		}
		else
		{
			errorMessage = resultObject.toString();
			logger.error(errorMessage);
		}
		if(lecture != null)
		{
			Object validationMessageForLecture = validationCheckForLecture(lecture);
			if(validationMessageForLecture != null)
			{
				return Response.status(Status.BAD_REQUEST).entity(validationMessageForLecture.toString()).build();
			}
			
			Class aviewClass = null;
			Long classId = lecture.getClassId();
			Object resultObjectClass = ClassHelper.classValidCheck(classId);
			if(Class.class.isInstance(resultObjectClass))
			{
				aviewClass = (Class)resultObjectClass;
				if(aviewClass != null && aviewClass.getStatusId() == StatusHelper.getActiveStatusId())
				{
					Object validationMessageForAdmin = ClassHelper.adminValidation(aviewClass, admin);
					if(validationMessageForAdmin != null)
					{
						return Response.status(Status.BAD_REQUEST).entity(validationMessageForAdmin.toString()).build();
					}
				}
			}
			else
			{
				String classErrorMessage = resultObjectClass.toString();
				return Response.status(Status.BAD_REQUEST).entity(classErrorMessage).build();
			}
			
			Date classStartTime = null;
			//SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");
			if(aviewClass.getStartTime() != null)
			{
				classStartTime = TimestampUtils.removeDateAndMillis(aviewClass.getStartTime());
			}
			else
			{
				//	to set the class start time for checking with lecture start time
				/*try 
				{
					classStartTime =TimestampUtils.removeDate(formatter.parse("1899-12-31T00:00:00.000+0530"));
				} 
				catch (java.text.ParseException e)
				{
					e.printStackTrace();
				}*/
			}
			
			Date classEndTime = null;
			if(aviewClass.getEndTime() != null)
			{
				classEndTime = TimestampUtils.removeDateAndMillis(aviewClass.getEndTime());
			}
			else
			{
				//	to set the class start time for checking with lecture end time
				/*try {
					classEndTime =TimestampUtils.removeDate(formatter.parse("1899-12-31T23:59:59.000+0530"));
				} catch (java.text.ParseException e) {
					e.printStackTrace();
				}*/
				//classEndTime = formatter.parse();
			}
			
			startDate = aviewClass.getStartDate();
			endDate = aviewClass.getEndDate();
			lectureStartDate = TimestampUtils.removeTime(lecture.getStartDate());
			lectureStartTime = lecture.getStartTime();
			lectureEndTime = lecture.getEndTime();
			
			Object validationMessageForDataTimewithClass = validationDateTimeCheckwithClass(startDate, endDate, lectureStartDate);
			if(validationMessageForDataTimewithClass !=null)
			{
				return Response.status(Status.BAD_REQUEST).entity(validationMessageForDataTimewithClass.toString()).build();
			}
			if(startDateChange == true || startTimeChange == true || endTimeChange == true)
			{
				Object validationMessageForDateTimewithLecture = validationDateTimeCheckwithLecture(lectureStartDate, lectureStartTime, lectureEndTime);				if(validationMessageForDateTimewithLecture != null)
				if(validationMessageForDateTimewithLecture != null)
				{
					return Response.status(Status.BAD_REQUEST).entity(validationMessageForDateTimewithLecture.toString()).build();
				}
			}
		}
		else
		{
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		try 
		{
			Long resultId = 0l;
			LectureHelper.updateLecture(lecture, adminId);
			resultId = lecture.getLectureId();
			logger.debug("Exit lecture Details on success::lecture.");
			return Response.ok().entity("Updated lecture (ID: "+resultId+") successfully").build();
		}
		catch (NumberFormatException nfe) 
		{
			result = "invalid";
			logger.debug("Exit lecture Details on invalid request::lecture.");
			return Response.status(Status.BAD_REQUEST).entity(result).build();    
		}
		catch(AViewException ae)
		{
			SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
			if(ae.getMessage().equals("Duplicate entry '"+lecture.getLectureName()+"-"+lecture.getClassId()+"' for key 'lecture_name'"))
			{
				result = "The given lecture name already exists. Please try a different lecture name.";
			}
			else if(ae.getMessage().equals("Duplicate entry '"+formatDateTime.format(lecture.getStartDate())+"-"+formatTime.format(lecture.getStartTime())+"-"+formatTime.format(lecture.getEndTime())+"-"+lecture.getClassId() +"' for key 'lecture_date'"))
			{
				result = "The lecture already exists at this time in same class.Please try for different time.";
			}
			else
			{
				result = "Error during log.Possible reason(s) : 1. Unexpected data, 2. Unknown.";
			}
			logger.debug("Exit lecture Details on error durning log::lecture.");
			return Response.status(Status.BAD_REQUEST).entity(result).build();    
		}
	}
	
}
