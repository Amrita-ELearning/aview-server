package edu.amrita.aview.gclm.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.helpers.EmailHelper;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.AppenderUtils;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.common.utils.JSONParserUtils;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.common.utils.ValidationUtils;
import edu.amrita.aview.gclm.daos.ClassDAO;
import edu.amrita.aview.gclm.entities.Class;
import edu.amrita.aview.gclm.entities.ClassRegistration;
import edu.amrita.aview.gclm.entities.ClassServer;
import edu.amrita.aview.gclm.entities.Course;
import edu.amrita.aview.gclm.entities.Institute;
import edu.amrita.aview.gclm.entities.InstituteServer;
import edu.amrita.aview.gclm.entities.Lecture;
import edu.amrita.aview.gclm.entities.Server;
import edu.amrita.aview.gclm.entities.ServerType;
import edu.amrita.aview.gclm.entities.User;

import edu.amrita.aview.meeting.helpers.MeetingManagerHelper;

import com.sun.jersey.api.client.ClientResponse.Status;

import flex.messaging.MessageBroker;
import flex.messaging.messages.AsyncMessage;
import flex.messaging.util.UUIDUtils;

@Controller
/**
 * The Class ClassHelper.
 */
public class ClassHelper {	

	/** The logger. */
	private static Logger logger = Logger.getLogger(ClassHelper.class);
	//Cache code
	/** The active classes map. */
	private static Map<Long,Class> activeClassesMap = Collections.synchronizedMap(new HashMap<Long,Class>());
	
	/** The Constant CACHE_CODE. */
	private static final String CACHE_CODE = "ClassHelper";

	private static final String COLLABORATION_SERVER = "FMS_DATA";
	public static final String CONTENT_SERVER = "CONTENT_SERVER";
	private static final String PRESENTER_VIDEO_SERVER = "FMS_VIDEO_PRESENTER";
	private static final String VIEWER_VIDEO_SERVER = "FMS_VIDEO_VIEWER";
	private static final String DESKTOP_SHARING_SERVER = "FMS_DESKTOP_SHARING";
	
	/**
	 * Populate cache.
	 *
	 * @param classes the classes
	 */
	private static synchronized void populateCache(List<Class> classes)
	{
		activeClassesMap.clear();
		for(Class cls:classes)
		{
			activeClassesMap.put(cls.getClassId(), cls);
		}
		CacheHelper.setCache(CACHE_CODE);
	}

	/**
	 * Gets the class count.
	 *
	 * @param courseId the course id
	 * @return the class count
	 * @throws AViewException
	 */
	public static Integer getClassCount(Long courseId) throws AViewException
	{
		List<Integer> statuses = new ArrayList<Integer>();
		statuses.add(StatusHelper.getActiveStatusId());
		statuses.add(StatusHelper.getClosedStatusId());
		return ClassDAO.getClassCount(courseId, statuses);
	}
	
	/**
	 * Adds the item to cache.
	 *
	 * @param aviewClass the aview class
	 * @throws AViewException
	 */
	private static synchronized void addItemToCache(Class aviewClass) throws AViewException
	{
		populateNames(aviewClass);
		activeClassesMap.put(aviewClass.getClassId(), aviewClass);
	}

	/**
	 * Removes the item from cache.
	 *
	 * @param aviewClass the aview class
	 * @throws AViewException
	 */
	private static synchronized void removeItemFromCache(Class aviewClass) throws AViewException
	{
		activeClassesMap.remove(aviewClass.getClassId());
	}

	/**
	 * Removes the class based on course.
	 *
	 * @param courseId the course id
	 * @throws AViewException
	 */
	public static void removeClassBasedOnCourse(Long courseId) throws AViewException
	{
		logger.info("Deleting classes based on course id:"+courseId);
		List<Class> aviewClasses = new ArrayList<Class>(); //active classs for active crs
		aviewClasses.addAll(getActiveCalssesIdMap().values());
		for(Class cls:aviewClasses)
		{
			if(cls.getCourseId().equals(courseId))
			{
				logger.info("Deleting class '"+cls.getClassName()+"' based on course id:"+courseId);
				removeItemFromCache(cls);
			}
		}
	}
	
	/**
	 * Update institute name in cache.
	 *
	 * @param institute the institute
	 * @param courseId the course id
	 * @throws AViewException
	 */
	public static void updateInstituteNameInCache(Institute institute,Long courseId) throws AViewException
	{
		logger.info("updateInstituteNameInCache course id:"+courseId+":InstituteName:"+institute.getInstituteName());
		List<Class> aviewClasses = new ArrayList<Class>(); //active classs for active crs
		aviewClasses.addAll(getActiveCalssesIdMap().values());
		for(Class cls:aviewClasses)
		{
			if(cls.getCourseId().equals(courseId))
			{
				logger.info("Updating class's InstituteName '"+cls.getClassName()+"' based on course id:"+courseId);
				cls.setInstituteName(institute.getInstituteName());
			}
		}
	}

	
	/**
	 * Update course name in cache.
	 *
	 * @param course the course
	 * @throws AViewException
	 */
	public static void updateCourseNameInCache(Course course) throws AViewException
	{
		Long courseId = course.getCourseId();
		logger.info("updateCourseNameInCache course id:"+courseId);
		List<Class> aviewClasses = new ArrayList<Class>(); //active classs for active crs
		aviewClasses.addAll(getActiveCalssesIdMap().values());
		for(Class cls:aviewClasses)
		{
			if(cls.getCourseId().equals(courseId))
			{
				logger.info("Updating class's Course Name '"+cls.getClassName()+"' based on course id:"+courseId);
				cls.setCourseName(course.getCourseName());
			}
		}
	}

	/**
	 * Gets the active calsses id map.
	 *
	 * @return the active calsses id map
	 * @throws AViewException
	 */
	public static synchronized Map<Long,Class> getActiveCalssesIdMap() throws AViewException
	{
		//If cache is expired or invalidated
		if(!CacheHelper.isCacheValid(CACHE_CODE))
		{
			int activeSId = StatusHelper.getActiveStatusId();
			List<Integer> statuses = new ArrayList<Integer>();
			statuses.add(activeSId);
			statuses.add(StatusHelper.getClosedStatusId());
			List<Class> aviewClasses = ClassDAO.getActiveClasses(activeSId, statuses); //active classs for active crs				
			populateNames(aviewClasses);
			populateCache(aviewClasses);
		}
		return activeClassesMap;
	}

	private static List<Class> getActiveMeetingsByInstitute(Long instituteId) throws AViewException
	{
		int activeSId = StatusHelper.getActiveStatusId();
		List<Integer> statuses = new ArrayList<Integer>();
		statuses.add(activeSId);
		statuses.add(StatusHelper.getClosedStatusId());
		List<Class> meetings = ClassDAO.getActiveMeetingsByInstitute(instituteId, activeSId, statuses); //active meetings for the given institute				
		logger.debug("getActiveMeetingsByInstitute: Number of active meeting rooms for the Institute are :"+meetings.size()+", for the institute id:"+instituteId);
		return meetings;
	}
	
	public static void notifyInstituteMeetingServerChange(Long instituteId) throws AViewException
	{
		List<Class> meetings = getActiveMeetingsByInstitute(instituteId);
		for(Class meeting:meetings)
		{
			logger.debug("notifyInstituteMeetingServerChange: for the meeting room :"+meeting.getClassName()+", for the institute id:"+instituteId);
			MeetingManagerHelper.assignMeetingServers(meeting, instituteId);
			populateNames(meeting);
			logger.debug("Assigned the meeting servers: for the meeting room :"+meeting.getClassName()+", for the institute id:"+instituteId);
			sendServerChangeMessageToClients(meeting);
		}
	}

	/**
	 * Gets the active classes by admin.
	 *
	 * @param adminId the admin id
	 * @return the active classes by admin
	 * @throws AViewException
	 */
	public static List<Class> getActiveClassesByAdmin(Long adminId) throws AViewException{		
		int activeSId = StatusHelper.getActiveStatusId();
		List<Integer> statuses = new ArrayList<Integer>();
		statuses.add(activeSId);
		statuses.add(StatusHelper.getClosedStatusId());
		List<Class> aviewClasses = ClassDAO.getActiveClassesByAdmin(adminId,activeSId, statuses); //active classs for active crs				
		populateNames(aviewClasses);
		return aviewClasses;
	}

	/**
	 * Gets the active classes.
	 *
	 * @return the active classes
	 * @throws AViewException
	 */
	public static List<Class> getActiveClasses() throws AViewException{
		List<Class> aviewClasses = new ArrayList<Class>(); //active classs for active crs
		aviewClasses.addAll(getActiveCalssesIdMap().values());
		return aviewClasses;
	}

	/**
	 * Removes the date times.
	 *
	 * @param aviewClass the aview class
	 */
	public static void removeDateTimes(Class aviewClass)
	{
		if(aviewClass.getStartDate() != null)
		{
			aviewClass.setStartDate(TimestampUtils.removeTime(aviewClass.getStartDate()));
		}
		if(aviewClass.getEndDate() != null)
		{
			aviewClass.setEndDate(TimestampUtils.removeTime(aviewClass.getEndDate()));
		}
		if(aviewClass.getStartTime() != null)
		{
			aviewClass.setStartTime(TimestampUtils.removeDateAndMillis(aviewClass.getStartTime()));
		}
		if(aviewClass.getEndTime() != null)
		{
			aviewClass.setEndTime(TimestampUtils.removeDateAndMillis(aviewClass.getEndTime()));
		}
	}

	/**
	 * Creates the class.
	 *
	 * @param aviewClass the aview class
	 * @param creatorId the creator id
	 * @return the long
	 * @throws AViewException
	 */
	public static Long createClass(Class aviewClass,Long creatorId) throws AViewException{

		//Code change for setting the default value of class registration as Approval
		if(aviewClass.getRegistrationType() == null)
		{
			aviewClass.setRegistrationType(Constant.APPROVAL_CLASS_REGISTRATION);
		}
		//Code ends here for setting the registration type as Approval
		removeDateTimes(aviewClass);
		logger.debug("Class to be created:"+aviewClass);
		aviewClass.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());

		Session session = null;	
		try 
		{
			session = HibernateUtils.getCurrentHibernateConnection();
			session.beginTransaction();
			ClassDAO.createClass(aviewClass);
			if(aviewClass.getScheduleType().equals(Constant.SCHEDULED_CLASS_TYPE))
			{
				List<Lecture> lectures = LectureHelper.getLecturesForClassCreate(aviewClass) ;
				if((lectures != null) && (lectures.size() > 0))
				{
					LectureHelper.createLectures(lectures,creatorId);
				}
				logger.debug("ClassiD is:"+ aviewClass.getClassId());
				//Add to cache
			}
			addItemToCache(aviewClass);
			session.getTransaction().commit();
		}
		catch (HibernateException he) 
		{
			processException(he);	
			session.getTransaction().rollback();
		}
		catch(Exception e)
		{
			String exceptionMessage = null ;
			exceptionMessage = e.getMessage();
			if(e.getCause() != null && e.getCause().getMessage() != null)
			{
				exceptionMessage = e.getCause().getMessage();
			}
			else
			{
				exceptionMessage = e.getMessage();
			}
			logger.error(exceptionMessage,e);
			throw (new AViewException(exceptionMessage));
		}
		finally 
		{
			HibernateUtils.closeConnection(session);
		}

		//		ClassDAO.createClass(aviewClass);	
		//		List<Lecture> lectures = LectureHelper.getLecturesForClassCreate(aviewClass) ;
		//		LectureHelper.createLectures(lectures,creatorId);			
		return aviewClass.getClassId();
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
	 * Gets the class by class name.
	 *
	 * @param className the class name
	 * @param courseId the course id
	 * @return the class by class name
	 * @throws AViewException
	 */
	public static Class getClassByClassName(String className ,Long courseId) throws AViewException
	{
		Class aviewClass = ClassDAO.getClassByClassName(className,courseId);
		populateNames(aviewClass);
		return aviewClass ;
	}

	/**
	 * Gets the class by user id.
	 *
	 * @param userId the user id
	 * @return the class by user id
	 * @throws AViewException
	 */
	public static List<Class> getClassByUserId(Long userId) throws AViewException
	{
		int activeSId = StatusHelper.getActiveStatusId();
		List<Integer> statuses = new ArrayList<Integer>();
		statuses.add(activeSId);
		statuses.add(StatusHelper.getClosedStatusId());
		List<Class> aviewClasses = ClassDAO.getClassByUserId(userId,activeSId,statuses);
		populateNames(aviewClasses);
		return aviewClasses;
	}

	//Fix for Bug id 2450 start
	//Get the list of classes for which the given user is a moderator
	/**
	 * Gets the class by moderator.
	 *
	 * @param userId the user id
	 * @return the class by moderator
	 * @throws AViewException
	 */
	public static List<Class> getClassByModerator(Long userId) throws AViewException
	{
		int activeSId = StatusHelper.getActiveStatusId();
		List<Integer> statuses = new ArrayList<Integer>();
		statuses.add(activeSId);
		statuses.add(StatusHelper.getClosedStatusId());
		List<Class> aviewClasses = ClassDAO.getClassByModerator(userId, activeSId, statuses);
		populateNames(aviewClasses);
		return aviewClasses ;	
	}
	//Fix for Bug id 2450 end

	/**
	 * Did class schedule change.
	 *
	 * @param oldClass the old class
	 * @param newClass the new class
	 * @return true, if successful
	 */
	private static boolean didClassScheduleChange(Class oldClass,Class newClass)
	{
		boolean scheduleChanged = false;

		if(oldClass != null && newClass != null)
		{
			if( (oldClass.getScheduleType().equals(Constant.ADHOC_CLASS_TYPE)) && 
				(newClass.getScheduleType().equals(Constant.ADHOC_CLASS_TYPE)))
			{
				scheduleChanged = false;
			}
			/*
			else if((oldClass.getScheduleType().equals(Constant.SCHEDULED_CLASS_TYPE)) && 
					(newClass.getScheduleType().equals(Constant.ADHOC_CLASS_TYPE)))
			{
				scheduleChanged = true;
			}
			else if((oldClass.getScheduleType().equals(Constant.ADHOC_CLASS_TYPE)) && 
					(newClass.getScheduleType().equals(Constant.SCHEDULED_CLASS_TYPE)))
			{
				scheduleChanged = true;
			}*/
			else if( (oldClass.getScheduleType().equals(Constant.SCHEDULED_CLASS_TYPE)) && 
				(newClass.getScheduleType().equals(Constant.SCHEDULED_CLASS_TYPE)))
			{
				if(!oldClass.getStartTime().equals(newClass.getStartTime()))
				{
					scheduleChanged = true;
				}
				else if(!oldClass.getEndTime().equals(newClass.getEndTime()))
				{
					scheduleChanged = true;
				}
				else if(!oldClass.getWeekDays().equals(newClass.getWeekDays()))
				{
					scheduleChanged = true;
				}
				if(!oldClass.getStartDate().equals(newClass.getStartDate()))
				{
					scheduleChanged = true;
				}
				else if(!oldClass.getEndDate().equals(newClass.getEndDate()))
				{
					scheduleChanged = true;
				}
			}			
			else
			{
				scheduleChanged = true;
			}
		}
		else
		{
			scheduleChanged = true;
		}
		return scheduleChanged;

	}


	/**
	 * Did class servers change.
	 *
	 * @param oldClass the old class
	 * @param newClass the new class
	 * @return true, if successful
	 */
	private static boolean didClassServersChange(Class oldClass,Class newClass)
	{
		boolean serversChanged = false;

		if(oldClass != null && newClass != null)
		{
			Set<ClassServer> oldCSs = oldClass.getClassServers();
			//Fix for Bug #8192 start
			Set<ClassServer> newCSs = newClass.getClassServers();
			//Fix for Bug #8192 end
			
			if(oldCSs.size() != newCSs.size())
			{
				serversChanged = true;
			}
			else 
			{
				for(ClassServer oldCS:oldCSs)
				{
					for(ClassServer newCS:newCSs)
					{
						//Fix for Bug #8192 start
						//Use equals instead of == 
						if( (oldCS.getServerTypeId().equals(newCS.getServerTypeId())) && (oldCS.getPresenterPublishingBandwidthKbps().equals(newCS.getPresenterPublishingBandwidthKbps())))
						{
							//Use equals instead of == 
							if(!(oldCS.getServer().getServerId().equals(newCS.getServer().getServerId())))
							{
								//Fix for Bug #8192 end
								serversChanged = true;
								//Fix for Bug #8192 start.
								//Come out of the for loop after the first detection
								break;
								//Fix for Bug #8192 end
							}
						}
					}
					//Fix for Bug #8192 start
					if(serversChanged)
					{
						//Come out of the for loop after the first detection
						break;
					}
					//Fix for Bug #8192 end
				}
			}
		}
		else
		{
			serversChanged = true;
		}
		return serversChanged;

	}
	
	/**
	 * Update class.
	 *
	 * @param aviewClass the aview class
	 * @param updaterId the updater id
	 * @throws AViewException
	 */
	public static void updateClass(Class aviewClass,Long updaterId)  throws AViewException
	{

		//Code change for setting the default value of registration type as Approval
		if(aviewClass.getRegistrationType() == null)
		{
			aviewClass.setRegistrationType(Constant.APPROVAL_CLASS_REGISTRATION);
		}
		
		boolean classScheduleChanged = false;
		
		//Code ends here for setting the registration type as Approval
		logger.debug("New Class Before:"+aviewClass.toString());
		removeDateTimes(aviewClass);		
		Class oldClass = getClass(aviewClass.getClassId());

		if(oldClass == null)
		{
			throw new AViewException("Class with id :"+aviewClass.getClassId()+": is not found");
		}
		logger.debug("Old Class Before:"+oldClass.toString());
		removeDateTimes(oldClass);		

		logger.debug("New Class:"+aviewClass.toString());
		logger.debug("Old Class:"+oldClass.toString());

		Session session = null;	
		try 
		{
			session = HibernateUtils.getCurrentHibernateConnection();
			session.beginTransaction();				

			classScheduleChanged = didClassScheduleChange(oldClass,aviewClass);
			boolean isClassServersChanged = didClassServersChange(oldClass, aviewClass);
			
			if(isClassServersChanged)
			{
				logger.debug("Class Servers are Changed. \nOld Servers:"+oldClass.toStringClassServers()+"\n New Servers:"+aviewClass.toStringClassServers());
			}
			//Delete lectures if the new class is schedule -> schedule with date chanage
			//or schedule to adhoc
			if(classScheduleChanged)
			{
				logger.debug("Class schedule changed");
				Long classId = aviewClass.getClassId();
				Long courseId = aviewClass.getCourseId() ;

				//Delete all future lectures from the old settings
				LectureHelper.deleteLectureByClassId(classId,courseId);

				//Create new lectures from the current date.
				List<Lecture> lectures = LectureHelper.getLecturesForClassUpdate(aviewClass);
				LectureHelper.createLectures(lectures,updaterId);
			}
			
			oldClass.updateFrom(aviewClass);
			oldClass.setModifiedAuditData(updaterId, TimestampUtils.getCurrentTimestamp());
			ClassDAO.updateClass(oldClass);

			//Add to cache
			addItemToCache(oldClass);

			session.getTransaction().commit();
			
			if(isClassServersChanged)
			{
				sendServerChangeMessageToClients(oldClass);
			}
			
			//Code change for NIC start
			//Send email if there is any change in the class schedule change
			if(classScheduleChanged)
			{
				sendEmailToClassRegistrantsOnClassScheduleChange(aviewClass);
			}
			//Code change for NIC end		
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
	}
	
	/**
	 * Send server change message to clients.
	 *
	 * @param updatedClass the updated class
	 */
	private static void sendServerChangeMessageToClients(Class updatedClass)
	{
		MessageBroker msgBroker = MessageBroker.getMessageBroker(null);
		String clientID = UUIDUtils.createUUID();
		AsyncMessage msg = new AsyncMessage();
		msg.setDestination("classServerChange");
		msg.setClientId(clientID);
		msg.setMessageId(UUIDUtils.createUUID());
		msg.setTimestamp(System.currentTimeMillis());
		msg.setHeader("classId", updatedClass.getClassId()); 
		msg.setBody(updatedClass);
		msgBroker.routeMessageToService(msg, null);
	}

	/**
	 * Update lecture by class id.
	 *
	 * @param classId the class id
	 * @param userId the user id
	 * @param statusId the status id
	 * @throws AViewException
	 */
	public static void updateLectureByClassId(Long classId,Long userId , Integer statusId) throws AViewException
	{
		int deletedSId = StatusHelper.getDeletedStatusId() ;
		List<Lecture> lectures = LectureHelper.getLecturesForClass(classId) ;
		if(lectures.size() > 0)
		{
			for(Lecture l:lectures)
			{
				l.setStatusId(deletedSId) ;
				l.setModifiedByUserId(userId);
				l.setModifiedDate(TimestampUtils.getCurrentTimestamp());
				l.setLectureName(l.getLectureName() + AppenderUtils.DeleteAppender()) ;
			}
			LectureHelper.updateLectures(lectures,userId) ;
		}
		else
		{
			return ;
		}
	}
	
	/**
	 * Delete class.
	 *
	 * @param classId the class id
	 * @param userId the user id
	 * @throws AViewException
	 */
	public static void deleteClass(Long classId, Long userId) throws AViewException
	{				
		int deletedSId = StatusHelper.getDeletedStatusId();
		Class aviewClass = getClass(classId);
		if(aviewClass != null)
		{
			Session session = null;	
			try {
				session = HibernateUtils.getCurrentHibernateConnection();
				session.beginTransaction();
				aviewClass.setStatusId(deletedSId);
				//bug fix for changing the class name for the deleted class, so that the new class can have 
				//the same name
				aviewClass.setClassName(aviewClass.getClassName() + AppenderUtils.DeleteAppender());
				aviewClass.setModifiedAuditData(userId, TimestampUtils.getCurrentTimestamp());
				ClassDAO.updateClass(aviewClass);
				removeItemFromCache(aviewClass);
				session.getTransaction().commit();
			} catch (HibernateException he) {
				processException(he);	
				session.getTransaction().rollback();
			}
			finally {
				HibernateUtils.closeConnection(session);
			}
		}
		else
		{
			throw new AViewException("Class with id :"+classId+": is not found");
		}
	}

	/**
	 * Gets the class.
	 *
	 * @param classId the class id
	 * @return the class
	 * @throws AViewException
	 */
	public static Class getClass(Long classId) throws AViewException
	{
		Class aviewClass = null;
		aviewClass = ClassDAO.getClass(classId);
		if(aviewClass != null)
		{
			populateNames(aviewClass);
		}
		return aviewClass;
	}

	/**
	 * Populate names.
	 *
	 * @param classes the classes
	 * @throws AViewException
	 */
	private static void populateNames(List<Class> classes) throws AViewException
	{
		for(Class cls:classes)
		{
			populateNames(cls);
		}
	}

	/**
	 * Populate names.
	 *
	 * @param cls the cls
	 * @throws AViewException
	 */
	public static void populateNames(Class cls) throws AViewException
	{
		Course course = CourseHelper.getActiveCoursesIdMap().get(cls.getCourseId()); //Cached, so do not worry :-)
		if(course != null)
		{
			cls.setCourseName(course.getCourseName());
			cls.setInstituteName(course.getInstituteName());
			//Populate Server Type Names if servers are not null
			//Fix for Bug #13328 start
			//Check for null before populating the server type
			if(cls.getClassServers() != null)
			{
				for(ClassServer classServer:cls.getClassServers())
				{
					classServer.setServerTypeName(ServerTypeHelper.getServerTypeName(classServer.getServerTypeId()));
				}
			}
			//Fix for Bug #13328 end
		}
		else
		{
			logger.debug("Exit from populate class when course is not valid");
			throw new AViewException("Given course is not valid");
		}
	}

	/**
	 * Search class.
	 *
	 * @param instituteId the institute id
	 * @param courseId the course id
	 * @param className the class name
	 * @return the list
	 * @throws AViewException
	 */
	public static List <Class> searchClass(Long instituteId,Long courseId,String className) throws AViewException
	{
		return searchClass(instituteId,courseId,className,null) ;
	}

	/**
	 * Search class.
	 *
	 * @param instituteId the institute id
	 * @param courseId the course id
	 * @param className the class name
	 * @param adminId the admin id
	 * @return the list
	 * @throws AViewException
	 */
	public static List <Class> searchClass(Long instituteId,Long courseId,String className,Long adminId) throws AViewException
	{
		int activeSId = StatusHelper.getActiveStatusId();	
		List<Integer> statuses = new ArrayList<Integer>();
		statuses.add(activeSId);
		return searchClass(instituteId,courseId,className,adminId,statuses) ;
	}
	
	/**
	 * Search class.
	 *
	 * @param instituteId the institute id
	 * @param courseId the course id
	 * @param className the class name
	 * @param adminId the admin id
	 * @param statuses the statuses
	 * @return the list
	 * @throws AViewException
	 */
	public static List <Class> searchClass(Long instituteId,Long courseId,String className,Long adminId,List<Integer> statuses) throws AViewException
	{
		List <Class> classes = ClassDAO.searchClass(instituteId,courseId, className,adminId, statuses);
		List<Class> meetingClassesToRemove = new ArrayList<Class>();
		for(Class aviewClass : classes)
		{
			if( (aviewClass.getClassType()!= null) && (aviewClass.getClassType().equals(Constant.MEETING_CLASS_TYPE)))
			{
				meetingClassesToRemove.add(aviewClass);
			}
		}
		classes.removeAll(meetingClassesToRemove);
		if(classes!=null)
		{
			populateNames(classes);
		}
		return classes;
	}
	
	/**
	 * Close class for registration.
	 *
	 * @param classId the class id
	 * @param adminId the admin id
	 * @throws AViewException
	 */
	public static void closeClassForRegistration(Long classId, Long adminId) throws AViewException
	{
		int closeId = StatusHelper.getClosedStatusId();
		Class aviewClass = getClass(classId);
		if(aviewClass != null)
		{
			Session session = null;	
			try 
			{
				session = HibernateUtils.getCurrentHibernateConnection();
				session.beginTransaction();
				aviewClass.setStatusId(closeId);
				aviewClass.setModifiedAuditData(adminId, TimestampUtils.getCurrentTimestamp());
				ClassDAO.updateClass(aviewClass);
				session.getTransaction().commit();
				CacheHelper.invalidateCache(CACHE_CODE);
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
		}
		else
		{
			throw new AViewException("Class with id :"+classId+": is not found");
		}
	}
	
	/**
	 * Activate class for registration.
	 *
	 * @param classId the class id
	 * @param adminId the admin id
	 * @throws AViewException
	 */
	public static void activateClassForRegistration(Long classId, Long adminId) throws AViewException
	{
		int activeId = StatusHelper.getActiveStatusId();
		Class aviewClass = getClass(classId);
		if(aviewClass != null)
		{
			Session session = null;	
			try 
			{
				session = HibernateUtils.getCurrentHibernateConnection();
				session.beginTransaction();
				aviewClass.setStatusId(activeId);
				aviewClass.setModifiedAuditData(adminId, TimestampUtils.getCurrentTimestamp());
				ClassDAO.updateClass(aviewClass);
				session.getTransaction().commit();
				addItemToCache(aviewClass);
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
		}
		else
		{
			throw new AViewException("Class with id :"+classId+": is not found");
		}
	}
	
	//Code change for NIC start
	//Send email to all the registered users for this class about the change in the class schedule
	/**
	 * Send email to class registrants on class schedule change.
	 *
	 * @param aviewClass the aview class
	 * @throws AViewException
	 */
	public static void sendEmailToClassRegistrantsOnClassScheduleChange(Class aviewClass) throws AViewException
	{
		List<ClassRegistration> classRegistrations = ClassRegistrationHelper.searchForClassRegister(null, null, null, aviewClass.getClassId(), null, aviewClass.getCourseId(), null);
		List<String> emailIdOfRegisteredUsers = new ArrayList<String>();
		for(ClassRegistration classRegistration : classRegistrations)
		{
			emailIdOfRegisteredUsers.add(classRegistration.getUser().getEmail());
		}
		EmailHelper.sendEmailForClassScheduleChange(emailIdOfRegisteredUsers, aviewClass);
	}
	//Code change for NIC end
	
	 /**
	 * 
	 * @param courseId
	 * @return Class
	 */
	public static List <Class> searchMeetingClassType(List<Long> courseIds) throws AViewException
	{
		//This function will not remove the meeting classes
		int activeSId = StatusHelper.getActiveStatusId();	
		List<Integer> statuses = new ArrayList<Integer>();
		statuses.add(activeSId);
		List<Class> classes = ClassDAO.searchMeetingClassType(courseIds, statuses);
		if(classes!=null)
		{
			populateNames(classes);
		}
		return classes;
	}
	
	
	/**
	 * Clear cache.
	 */
	public static void clearCache()
	{
		logger.debug("Entered ClassHelper.clearCache()");
		activeClassesMap = null;
		logger.debug("Exit ClassHelper.clearCache()");
	}

	/**
	 * Function to validate all 5 Servers are allocated for class for API
	 * @param jArray
	 * @return string
	 * @throws AViewException
	 */
	private static String isServerAllocated(ArrayList classServerList) throws AViewException
	{
		String message = null ;
		if(classServerList == null)
		{
			message = "No Servers available";
		}
		else
		{
			Boolean  hasCollaborationServer = false;
			Boolean  hasContentServer = false;
			Boolean  hasPresenterVideoServer = false;
			Boolean  hasViewerVideoServer = false;
			Boolean  hasDesktopSharingServer = false;
			
			String collaborationServerId = null;
			String contentServerId = null;
			String presenterVideoServerId = null;
			String viewerVideoServerId = null;
			String desktopSharingServerId = null;
			
			int collaborationServerCount = 0;
			int contentServerCount = 0;
			int presenterVideoServerCount = 0;
			int viewerVideoServerCount = 0;
			int desktopSharingServerCount = 0;
			
			List serverTypes = ServerTypeHelper.getAllServerTypes();
			for(int j = 0 ; j < serverTypes.size(); j++)
			{
				ServerType serverTypeObj = (ServerType)serverTypes.get(j) ;
				if (serverTypeObj.getServerType().equals(COLLABORATION_SERVER))
				{
					collaborationServerId = "serverTypeId:"+serverTypeObj.getServerTypeId();
				}
				else if (serverTypeObj.getServerType().equals(CONTENT_SERVER))
				{
					contentServerId = "serverTypeId:"+serverTypeObj.getServerTypeId();
				}
				else if (serverTypeObj.getServerType().equals(PRESENTER_VIDEO_SERVER))
				{
					presenterVideoServerId = "serverTypeId:"+serverTypeObj.getServerTypeId();
				}
				else if (serverTypeObj.getServerType().equals(VIEWER_VIDEO_SERVER))
				{
					viewerVideoServerId = "serverTypeId:"+serverTypeObj.getServerTypeId();
				}
				else if (serverTypeObj.getServerType().equals(DESKTOP_SHARING_SERVER))
				{
					desktopSharingServerId = "serverTypeId:"+serverTypeObj.getServerTypeId();
				}
			}
			Object servetTypeObject=new Object();
			ArrayList servertypeArray=new ArrayList();
			for (int i = 0; i < classServerList.size(); i++) 
			{
				ClassServer classServer = (ClassServer)classServerList.get(i);
				String serverType = null;
				try
				{
					serverType = ServerTypeHelper.getServerTypeName(classServer.getServerTypeId());
				}
				catch(AViewException ae)
				{
					
				}
			    if(serverType == null)
		        {
		        	servetTypeObject=classServer.getServerTypeId();
		        	servertypeArray.add("serverTypeId:"+servetTypeObject);
		        	message=servertypeArray+"are not existing serverType. ";
		        } 
		        else if (serverType.equals(COLLABORATION_SERVER))
				{
					hasCollaborationServer = true;
					collaborationServerCount++;
					if(collaborationServerCount > 1)
					{
						message = collaborationServerId+" has inserted more than once";
					}
				}
				else if (serverType.equals(CONTENT_SERVER))
				{
					hasContentServer = true;
					contentServerCount++;
					if(contentServerCount > 1)
					{
						message = contentServerId+" has inserted more than once";
					}
				}
				else if (serverType.equals(PRESENTER_VIDEO_SERVER))
				{
					hasPresenterVideoServer = true;
					presenterVideoServerCount++;
					if(presenterVideoServerCount > 1)
					{
						message = presenterVideoServerId+" has inserted more than once";
					}
				}
				else if (serverType.equals(VIEWER_VIDEO_SERVER))
				{
					hasViewerVideoServer = true;
					viewerVideoServerCount++;
					if(viewerVideoServerCount > 1)
					{
						message = viewerVideoServerId+" has inserted more than once";
					}
				}
				else if (serverType.equals(DESKTOP_SHARING_SERVER))
				{
					hasDesktopSharingServer = true;
					desktopSharingServerCount++;
					if(desktopSharingServerCount > 1)
					{
						message = desktopSharingServerId+" has inserted more than once";
					}
				}
			}
	
			String serverName = null;
			if(hasCollaborationServer != true)
			{
				serverName = collaborationServerId;
				if(message == null)
				{
					message = serverName;
				}
				else
				{
					message += ","+serverName;
				}
				
			}
			if(hasContentServer != true )
			{
				serverName =  contentServerId;
				if(message == null)
				{
					message = serverName;
				}
				else
				{
					message += ","+serverName;
				}
			}
			if(hasPresenterVideoServer != true )
			{
				serverName =  presenterVideoServerId;
				if(message == null)
				{
					message = serverName;
				}
				else
				{
					message += ","+serverName;
				}
			}
			if(hasViewerVideoServer != true )
			{
				serverName =  viewerVideoServerId;
				if(message == null)
				{
					message = serverName;
				}
				else
				{
					message += ","+serverName;
				}
			}
			if(hasDesktopSharingServer != true)
			{
				serverName = desktopSharingServerId;
				if(message == null)
				{
					message = serverName;
				}
				else
				{
					message += ","+serverName;
				}
			}
		}
		return message;
	}

	/***
	 * Function to check validation for class in API
	 * @param aviewClass
	 * @return response
	 * @throws AViewException
	 */
	public static Object validationCheckForClass(Class aviewClass,User admin) throws AViewException
	{
		if (aviewClass.getCourseId() == null) 
		{
			return "Entered courseId is not valid or courseId doesn't exist.";
		} 
		else 
		{
			String intError = null;
			intError = ValidationUtils.integerOnly(aviewClass.getCourseId().toString());
			if (intError != null) 
			{
				return intError +" in courseId.";
			}
		}
	
		/*if (aviewClass.getStartDate() == null) 
		{
			return Response.status(Status.BAD_REQUEST).entity("Entered startDate is not valid or StartDate doesn't exist.").build();
		}
		if (aviewClass.getEndDate() == null) 
		{
			return Response.status(Status.BAD_REQUEST).entity("Entered endDate is not valid or EndDate doesn't exist.").build();
		}
		Date startDate = TimestampUtils.removeTime(aviewClass.getStartDate());
		Date endDate = TimestampUtils.removeTime(aviewClass.getEndDate());
		Date currentDate = TimestampUtils.removeTime(TimestampUtils.getCurrentTimestamp());
		if(startDate.after(currentDate))
		{
			logger.debug("Enter start date is greater than current date.");	
        }
		else if(startDate.equals(currentDate))
		{
    	   logger.debug("Enter current date and start date is same.");	
		}
		else if(startDate.before(currentDate))
		{
    		return Response.status(Status.BAD_REQUEST).entity("Start date is less than current date.").build();
		}*/
		Object validationMessageForAdmin = ClassHelper.adminValidation(aviewClass, admin);
		if(validationMessageForAdmin != null)
		{
			return validationMessageForAdmin.toString();
		}
		if(aviewClass.getMaxStudents() == 0)
		{
			aviewClass.setMaxStudents(500);
		}
		else if (aviewClass.getMaxStudents() < 0 || aviewClass.getMaxStudents() >= 999999999) 
		{
			return "MaxStudent count out of bound.";
		}
		if(aviewClass.getMinPublishingBandwidthKbps() == 0)
		{
			aviewClass.setMinPublishingBandwidthKbps(28);
		}
		else
		{
			int[] ar = { 28, 56, 128, 256, 512, 768, 1024, 2520, 5670, 8505 };
			int i = 0;
			while (i < ar.length) 
			{
				if (aviewClass.getMinPublishingBandwidthKbps() == (ar[i]))
				{
					break;
				}
				i++;
			}
			if (!(i < ar.length)) 
			{
				return "MinPublishingBandwidth should be 28, 56, 128, 256, 512, 768, 1024, 2520, 5670 ,8505,others not valid.";
			}
		}
		if (aviewClass.getMaxPublishingBandwidthKbps() == 0) 
		{
			aviewClass.setMaxPublishingBandwidthKbps(1024);
			if (aviewClass.getMaxPublishingBandwidthKbps() < aviewClass.getMinPublishingBandwidthKbps())
			{
				return "Max bandwidth missing, the default value for max bandwidth is 1024. Hence, the min bandwidth should be less than (or) equal to the max bandwidth";
			}
		} 
		else 
		{
			int[] ar = { 28, 56, 128, 256, 512, 768, 1024, 2520, 5670, 8505 };
			int i = 0;
			while (i < ar.length) 
			{
				if (aviewClass.getMaxPublishingBandwidthKbps() == (ar[i]))
				{
				/*	if (ar[i] < aviewClass.getMinPublishingBandwidthKbps())
					{
						return Response.status(Status.BAD_REQUEST)
								.entity("Minimum bandwidth to publish should be less than the Maximum bandwidth to publish.")
								.build();
					}*/
					break;
				} 
				i++;
			}
			if (!(i < ar.length))
			{
				return "MaxPublishingBandwidth should be 28, 56, 128, 256, 512, 768, 1024, 2520, 5670 ,8505,others not valid.";
			}
		}
		if (aviewClass.getMaxPublishingBandwidthKbps() < aviewClass.getMinPublishingBandwidthKbps())
		{
			return "Minimum bandwidth to publish should be less than the Maximum bandwidth to publish.";
		}
		if(aviewClass.getMaxViewerInteraction() == 0)
		{
			aviewClass.setMaxViewerInteraction(1);
		}
		else 
		{
			int[] ar = { 1, 2, 3, 4, 5, 6, 7, 8 };
			int i = 0;
			while (i < ar.length) 
			{
				if (aviewClass.getMaxViewerInteraction() == (ar[i])) 
				{
					break;
				}
				i++;
			}
			if (!(i < ar.length)) 
			{
				return "MaxViewerInteraction should be 1,2,3,4,5,6,7,8,Others not valid.";
			}
		}
		if(aviewClass.getPresenterPublishingBwsKbps() == null || aviewClass.getPresenterPublishingBwsKbps() == "")
		{
			aviewClass.setPresenterPublishingBwsKbps("256");
		}
		else 
		{
			String[] ar = { "28","56","128","256","512","768","1024","2520","5670","8505" };
			int i = 0;
			while (i < ar.length)
			{
				if (aviewClass.getPresenterPublishingBwsKbps().equals(ar[i])) 
				{
					break;
				}
				i++;
			}
			if (!(i < ar.length)) 
			{
				return "PresenterPublishing Bandwidth should be 28, 56, 128, 256, 512, 768, 1024, 2520, 5670 ,8505,Others not valid.";
			}
		}
		if (aviewClass.getScheduleType() == null || aviewClass.getScheduleType() == "") 
		{
			aviewClass.setScheduleType("Adhoc");
		} 
		else 
		{
			String st = aviewClass.getScheduleType().toUpperCase();
			String[] ar = { "ADHOC", "SCHEDULED" };
			int i = 0;
			while (i < ar.length)
			{
				if (st.equals(ar[i]))
				{
					if (ar[i].equals("ADHOC"))
					{
						aviewClass.setScheduleType("Adhoc");
					} 
					else if(ar[i].equals("SCHEDULED"))
					{
						aviewClass.setScheduleType("Scheduled");
						return "Scheduled Type can't be set for this particular class.";
					}
					break;
				}
				i++;
			}
			if (!(i < ar.length)) 
			{
				return "ScheduleType should be Adhoc or Scheduled,others not valid.";
			}
		}
		if(aviewClass.getClassType() == null || aviewClass.getClassType() == "")
		{
			aviewClass.setClassType("Classroom");
		}
		if (aviewClass.getVideoCodec() == null	|| aviewClass.getVideoCodec() == "")
		{
			aviewClass.setVideoCodec("H.264");
		} 
		else
		{
			String vc = aviewClass.getVideoCodec().toUpperCase();
			String[] ar = { "VP6", "SORENSON", "H.264" };
			int i = 0;
			while (i < ar.length) 
			{
				if (vc.equals(ar[i]))
				{
					if (ar[i].equals("VP6"))
					{
						aviewClass.setVideoCodec("VP6");
					}
					else if (ar[i].equals("SORENSON"))
					{
						aviewClass.setVideoCodec("Sorenson");
					}
					else
					{
						aviewClass.setVideoCodec("H.264");
					}
					break;
				}
				i++;
			}
			if (!(i < ar.length))
			{
				return "VideoCodec should be VP6,Sorenson,H.264 ,Others not valid.";
			}
		}
		if (aviewClass.getVideoStreamingProtocol() == null || aviewClass.getVideoStreamingProtocol() == "") 
		{
			aviewClass.setVideoStreamingProtocol("RTMP");
		}
		else 
		{
			String vsp=aviewClass.getVideoStreamingProtocol().toUpperCase();
			String videoStreamingProtocol="RTMP";
			if(vsp.equals(videoStreamingProtocol))
			{
				aviewClass.setVideoStreamingProtocol(videoStreamingProtocol);
			}
			else
			{
				return "VideoStreamingProtocol should be RTMP ,Others not valid.";
			}
		}
		if (aviewClass.getAuditLevel() == null || aviewClass.getAuditLevel() == "")
		{
			aviewClass.setAuditLevel("Action");
		} 
		else
		{
			String al=aviewClass.getAuditLevel().toUpperCase();
			String[] ar = { "ACTION", "LECTURE" };
			
			int i=0;
			while(i<ar.length)
			{
				if (al.equals(ar[i])) 
				{
					if(ar[i].equals("ACTION"))
					{
					aviewClass.setAuditLevel("Action");
					}
					else
					{
						aviewClass.setAuditLevel("Lecture");
					}
					break;
				}
				i++;
			}
			if(!(i<ar.length))
			{
				return "AuditLevel should be Action or Lecture ,Others not valid.";
			}
		}
		if (aviewClass.getRegistrationType() == null || aviewClass.getRegistrationType() == "")
		{
			aviewClass.setRegistrationType("Approval");
		} 
		else
		{
			String rt=aviewClass.getRegistrationType().toUpperCase();
			String[] ar = { "APPROVAL", "NOAPPROVAL", "OPENWITHLOGIN","OPEN" };
			int i = 0;
			while(i<ar.length)
			{
				if (rt.equals(ar[i]))
				{
					if(ar[i].equals("APPROVAL"))
					{
						aviewClass.setRegistrationType("Approval");
					}
					else if(ar[i].equals("NOAPPROVAL"))
						aviewClass.setRegistrationType("NoApproval");
					else if(ar[i].equals("OPENWITHLOGIN"))
						aviewClass.setRegistrationType("OpenWithLogin");
					else
						aviewClass.setRegistrationType("Open");
						
					break;
				}
				i++;
			}
			if(!(i<ar.length))
			{
				return "RegistrationType should be Approval,NoApproval,OpenWithLogin,Open,others not valid.";
			}
		}
		
		if (aviewClass.getAllowDynamicSwitching() == null || aviewClass.getAllowDynamicSwitching() == "") 
		{
			aviewClass.setAllowDynamicSwitching("N");
		}
		else 
		{
			String[] ar = { "Y", "N", "y", "n" };
			int i = 0;
			while (i < ar.length) 
			{
				if (aviewClass.getAllowDynamicSwitching().equals(ar[i])) 
				{
					break;
				}
				i++;
			}
			if (!(i < ar.length)) 
			{
				return "DynamicSwitching should be Y,N,y,n ,others not valid.";
			}
		}
		if (aviewClass.getClassDescription() == null || aviewClass.getClassDescription() == "")
		{
			aviewClass.setClassDescription(null);
		} 
		else 
		{
			String ClassDescError = null;
			ClassDescError = ValidationUtils.AllowedCharForClassDescrption(aviewClass.getClassDescription());
			if (ClassDescError != null) 
			{
				return ClassDescError+" in ClassDescription.";
			}
		}
		if (aviewClass.getClassName() == null || aviewClass.getClassName() == "" || aviewClass.getClassName().length() > 99) 
		{
			return "Entered classname is not valid or classname is not given.";
		}
		else 
		{
			String ClassError = null;
			ClassError = ValidationUtils.AllowedCharForclass(aviewClass.getClassName());
			if (ClassError != null)
			{
				return ClassError+" in Classname.";
			}
		}
		if(aviewClass.getStartTime() == null)
		{
			//setting the class start time 
		/*	String startTime ="1899-12-31T00:00:00.000+0530";
			logger.debug("Enter end date and start date is same");	
			try
	        {
				aviewClass.setStartTime(formatter.parse(startTime));
	        }
	        catch (ParseException ex)
	        {
	            System.out.println("Exception "+ex);
	        }*/
			aviewClass.setStartTime(null);
		}
		if(aviewClass.getEndTime() == null)
		{
			//setting the class end time 
			/*String endTime ="1899-12-31T23:59:59.000+0530";
			logger.debug("Enter end date and start date is same");	
			try
	        {
				aviewClass.setEndTime(formatter.parse(endTime));
			}
	        catch (ParseException ex)
	        {
	            System.out.println("Exception "+ex);
	        }*/
			aviewClass.setEndTime(null);
		}
		if(aviewClass.getWeekDays() == null || aviewClass.getWeekDays() == "")
		{
			aviewClass.setWeekDays("NNNNNNN");
		}
		if(aviewClass.getIsMultiBitrate() == null || aviewClass.getIsMultiBitrate() == "")
		{
			aviewClass.setIsMultiBitrate("N");
		}
		else
		{
			String[] ar = { "Y", "N", "y", "n" };
			String name = new String();
			for (int i = 0; i < ar.length; i++) {
				if (aviewClass.getIsMultiBitrate().equals(ar[i])) {
					name = ar[i];
					break;
				}
			}
			if (!aviewClass.getIsMultiBitrate().equals(name)) {
				return "IsMultiBitrate should be Y,N,y,n ,others not valid.";
			}
		}
		if(aviewClass.getAudioVideoInteractionMode() == null || aviewClass.getAudioVideoInteractionMode() == "")
		{
			aviewClass.setAudioVideoInteractionMode(Constant.DEFAULT_AUDIO_VIDEO_INTERACTION_MODE);
		}
		if(aviewClass.getCanMonitor() == null || aviewClass.getCanMonitor() == "")
		{
			aviewClass.setCanMonitor(Constant.DEFAULT_CAN_MONITOR);
		}
		/*	if(startDate.after(endDate))
		{
			return Response.status(Status.BAD_REQUEST).entity("Start date is greater than end date.").build();
		}
		else if(startDate.equals(endDate))
		{
			logger.debug("Enter end date and start date is same.");	*/
			//**************Checking the time which have to be implemented later*************//
			/*Date startTime = TimestampUtils.removeDate(aviewClass.getStartTime());
		      Date endTime = aviewClass.getEndTime();
			  Date currentTime =TimestampUtils.removeDate(TimestampUtils.getCurrentTimestamp());
			if(startTime != null && endTime != null)
			{ 
				if(startTime.after(currentTime) && startTime.before(endTime))
				{
					if(startTime.after(endTime))
					{
						errorMessage ="Enter start time is great than end time";	
						return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
					}
					else if(startTime.before(endTime))
					{
						logger.debug("Enter start time is less than end time");	
						
					}
					else if(startTime.equals(endTime))
					{
						errorMessage ="Start time and end time can't be same";
						return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
						
					}
				}
				else
				{
					errorMessage ="Start time is less than current time/end time is less than start time";
 					return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
				}
			}*/
	/*	}
		else if(startDate.before(endDate))
		{
		logger.debug("Enter start date is less than end date.");	
		} */
		return null;
	}
	
	/**
	 * Function to validate the class date with current date
	 * @param dateType
	 * @param date
	 * @return Response
	 * @throws AViewException
	 */
	public static Response validationCheckComparisonwithCurrentDate(String dateType,Date date) throws AViewException
	{
		Date currentDate = TimestampUtils.removeTime(TimestampUtils.getCurrentTimestamp());
		if(date.after(currentDate))
		{
			logger.debug("Enter "+ dateType +" date is greater than current date.");	
        }
		else if(date.equals(currentDate))
		{
			//dateResponse="Class has been started already.";
    	   logger.debug("Enter current date and "+ dateType +" date is same.");	
		}
		else if(date.before(currentDate))
		{
			return  Response.status(Status.BAD_REQUEST).entity( dateType +" date is less than current date.").build();
    		
		}
		return  null;
	}
	
	/**
	 * Function to validate the class start date and end date
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws AViewException
	 */
	public static Response validationCheckForEndDate(Date startDate,Date endDate) throws AViewException
	{
		if(endDate.before(startDate))
		{
			return  Response.status(Status.BAD_REQUEST).entity("End date is less than Start date.").build();
		}
		else if(startDate.equals(endDate))
		{
			logger.debug("Enter end date and start date is same.");	
		}
		else if(endDate.after(startDate))
		{
			logger.debug("Enter end date is after start date.");	
		}
		return  null;
	}
	
//		Date currentDate = TimestampUtils.removeTime(TimestampUtils.getCurrentTimestamp());
		
//		if(startDate.after(endDate))
//		{
//			
//		}
//			
			//**************Checking the time which have to be implemented later*************//
			/*Date startTime = TimestampUtils.removeDate(aviewClass.getStartTime());
		      Date endTime = aviewClass.getEndTime();
			  Date currentTime =TimestampUtils.removeDate(TimestampUtils.getCurrentTimestamp());
			if(startTime != null && endTime != null)
			{ 
				if(startTime.after(currentTime) && startTime.before(endTime))
				{
					if(startTime.after(endTime))
					{
						errorMessage ="Enter start time is great than end time";	
						return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
					}
					else if(startTime.before(endTime))
					{
						logger.debug("Enter start time is less than end time");	
						
					}
					else if(startTime.equals(endTime))
					{
						errorMessage ="Start time and end time can't be same";
						return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
						
					}
				}
				else
				{
					errorMessage ="Start time is less than current time/end time is less than start time";
					return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
				}
			}*/
//		}
//		else if(startDate.before(endDate))
//		{
//			logger.debug("Enter start date is less than end date.");	
//		}
//		return null;
//		
//	}
	/**
	 * Function to validate class servers
	 * @param classServerList
	 * @param aviewClass
	 * @return response
	 * @throws AViewException
	 */
	public static Response validationCheckForClassServer(ArrayList classServerList,Class aviewClass) throws AViewException
	{
		List<InstituteServer> collabrationServer = new ArrayList<InstituteServer>();
		List<InstituteServer> presenterVideoServer= new ArrayList<InstituteServer>();
		List<InstituteServer> viewerVideoServer= new ArrayList<InstituteServer>();
		List<InstituteServer> contentServer= new ArrayList<InstituteServer>();
		List<InstituteServer> desktopSharingServer= new ArrayList<InstituteServer>();
		List instituteServer = new ArrayList();
		
		boolean hasCollaborationServer = false;
		boolean hasPresenterVideoServer = false;
		boolean hasViewerVideoServer = false;
		boolean hasContentServer = false;
		boolean hasDesktopSharingServer = false;
		
		String presenterVideoServerId = null;
		String presenterVideoServerTypeId = null;
		
		String contentServerId = null;
		String contentServerTypeId = null;
		
		String viewerVideoServerId = null;
		String viewerVideoServerTypeId = null;
		
		String collaborationServerId = null;
		String collaborationServerTypeId = null;
		
		String desktopSharingServerId = null;
		String desktopSharingServerTypeId = null;
		
		Course course = CourseHelper.getCourse(aviewClass.getCourseId());
		Long instituteId = course.getInstituteId();
		//instituteServer = InstituteServerHelper.getAllInstituteServers(instituteId);
		instituteServer = InstituteServerHelper.getInstituteServers(instituteId);
		String serverAllocated = isServerAllocated(classServerList);
    	if(serverAllocated != null)
    	{
   			if(serverAllocated.endsWith("has inserted more than once"))
    		{
    			return Response.status(Status.BAD_REQUEST).entity(serverAllocated).build();
    		}
			else
			{
				return Response.status(Status.BAD_REQUEST).entity(serverAllocated +" is/are missing").build();
			}
     	}
    	for(int i = 0;i < classServerList.size(); i++)
		{
			for(int j = 0;j < instituteServer.size(); j++)
			{
				InstituteServer instServer = (InstituteServer)instituteServer.get(j) ;
				ClassServer classServer = (ClassServer)classServerList.get(i);
				String serverTypeName = ServerTypeHelper.getServerTypeName(classServer.getServerTypeId());
				if(serverTypeName.equals(COLLABORATION_SERVER))
				{
					collabrationServer.add(instServer);
				}
	        	if(serverTypeName.equals(PRESENTER_VIDEO_SERVER))
				{
	        		presenterVideoServer.add(instServer);
				}
	        	if(serverTypeName.equals(VIEWER_VIDEO_SERVER))
				{
					viewerVideoServer.add(instServer);
				}
	        	if(serverTypeName.equals(CONTENT_SERVER))
				{
					contentServer.add(instServer);
				}
	        	if(serverTypeName.equals(DESKTOP_SHARING_SERVER))
				{
					desktopSharingServer.add(instServer);
				}
				if(COLLABORATION_SERVER.equals(serverTypeName))
				{
					collaborationServerId = "serverId:"+classServer.getServer().getServerId();
					collaborationServerTypeId = "serverType:"+classServer.getServerTypeId();
					for(int index = 0; index < collabrationServer.size() ; index++)
					{
						InstituteServer tempCollabrationServer = (InstituteServer)collabrationServer.get(index) ;
						if(tempCollabrationServer.getServer().getServerId() == classServer.getServer().getServerId())
						{
							hasCollaborationServer = true;
						}
					}
				}
				if(CONTENT_SERVER.equals(serverTypeName))
				{
					contentServerId = "serverId:"+classServer.getServer().getServerId();
					contentServerTypeId = "serverType:"+classServer.getServerTypeId();
					for(int index = 0; index < contentServer.size() ; index++)
					{
						InstituteServer tempContentServer = (InstituteServer)contentServer.get(index) ;
						if(tempContentServer.getServer().getServerId() == classServer.getServer().getServerId())
						{
							hasContentServer = true;
						}
					}
				}
				if(VIEWER_VIDEO_SERVER.equals(serverTypeName))
				{
					viewerVideoServerId = "serverId:"+classServer.getServer().getServerId();
					viewerVideoServerTypeId = "serverType:"+classServer.getServerTypeId();
					for(int index = 0; index < viewerVideoServer.size() ; index++)
					{
						InstituteServer tempViewerVideoServer = (InstituteServer)viewerVideoServer.get(index) ;
						if(tempViewerVideoServer.getServer().getServerId() == classServer.getServer().getServerId())
						{
							hasViewerVideoServer = true;
						}
					}
				}
				if(PRESENTER_VIDEO_SERVER.equals(serverTypeName))
				{
					presenterVideoServerId = "serverId:"+classServer.getServer().getServerId();
					presenterVideoServerTypeId = "serverType:"+classServer.getServerTypeId();
					for(int index = 0; index < presenterVideoServer.size() ; index++)
					{
						InstituteServer tempPresenterVideoServer = (InstituteServer)presenterVideoServer.get(index) ;
						if(tempPresenterVideoServer.getServer().getServerId() == classServer.getServer().getServerId())
						{
							hasPresenterVideoServer = true;
						}
					}
				}
				if(DESKTOP_SHARING_SERVER.equals(serverTypeName))
				{
					desktopSharingServerId = "serverId:"+classServer.getServer().getServerId();
					desktopSharingServerTypeId = "serverType:"+classServer.getServerTypeId();
					for(int index = 0; index < desktopSharingServer.size() ; index++)
					{
						InstituteServer tempDesktopSharingServer = (InstituteServer)desktopSharingServer.get(index) ;
						if(tempDesktopSharingServer.getServer().getServerId() == classServer.getServer().getServerId())
						{
							hasDesktopSharingServer = true;
						}
					}
				}
				if(classServer.getServerPort() == 0)
				{
					return Response.status(Status.BAD_REQUEST).entity("serverPort doesn't exist.").build();
				}
				else
				{
					String AllowedInteger = null;
		        	AllowedInteger = ValidationUtils.integerOnly(String.valueOf(classServer.getServerPort()));
					if (AllowedInteger != null) 
					{
						return Response.status(Status.BAD_REQUEST).entity("Entered value should be Integer in serverPort.").build();
					}
				}
				if(classServer.getPresenterPublishingBandwidthKbps() == 0)
				{
					return Response.status(Status.BAD_REQUEST).entity("presenterPublishingBandwidthKbps doesn't exist.").build();
				}
				if(classServer.getServerTypeId() == 0)
		        {
					return Response.status(Status.BAD_REQUEST).entity("Given server type ID doesn't exist.").build();
		        	
		        }
				Server server = null;
				if (classServer.getServer().getServerId() != null ) 
				{
					server = ServerHelper.getServer(classServer.getServer().getServerId());
					if(server != null && server.getStatusId() != StatusHelper.getActiveStatusId())
		    		{
						return Response.status(Status.BAD_REQUEST).entity("Given server ID doesn't exist.").build();
		    		}
		    		
				}
			}
		}
		
		String serverName = null;
		String message = null;
		if(hasCollaborationServer != true)
		{
			serverName = collaborationServerId+" is not "+collaborationServerTypeId;
			if(message == null)
			{
				message = serverName;
			}
			else
			{
				message += ","+serverName;
			}
		}
		if(hasContentServer != true )
		{
			serverName =  contentServerId+" is not "+contentServerTypeId;
			if(message == null)
			{
				message = serverName;
			}
			else
			{
				message += ","+serverName;
			}
		}
		if(hasPresenterVideoServer != true )
		{
			serverName =  presenterVideoServerId+" is not "+presenterVideoServerTypeId;
			if(message == null)
			{
				message = serverName;
			}
			else
			{
				message += ","+serverName;
			}
		}
		if(hasViewerVideoServer != true )
		{
			serverName =  viewerVideoServerId+" is not "+viewerVideoServerTypeId;
			if(message == null)
			{
				message = serverName;
			}
			else
			{
				message += ","+serverName;
			}
		}
		if(hasDesktopSharingServer != true)
		{
			serverName = desktopSharingServerId+" is not "+desktopSharingServerTypeId;
			if(message == null)
			{
				message = serverName;
			}
			else
			{
				message += ","+serverName;
			}
		}
		if(message != null)
		{
			return Response.status(Status.BAD_REQUEST).entity(message).build();
		}
		return null;
	}
	
	/**
	 * Function the admin(user) is of the corresponding class
	 * @param aviewClass
	 * @param admin
	 * @return object
	 * @throws AViewException
	 */
	public static Object adminValidation(Class aviewClass,User admin) throws AViewException
	{
		boolean isAdmin = false;
		
	    Course course = CourseHelper.getCourse(aviewClass.getCourseId());
        if(course != null && course.getStatusId() == StatusHelper.getActiveStatusId())
		{
			logger.debug("Enter course id is a active course id::class creation.");
			Institute instituteDetails = InstituteHelper.getInstituteById(course.getInstituteId());
			if(instituteDetails != null && instituteDetails.getStatusId() == StatusHelper.getActiveStatusId())
			{
				List<Institute> institutes = InstituteHelper.getAllInstitutesForAdmin(admin.getUserId());
				if(institutes != null)
				{
					 isAdmin = institutes.contains(instituteDetails);
				}
			}
			else
			{
				logger.debug("Given institute id doesn't exist");
			}
			if (admin.getRole().equals(Constant.MASTER_ADMIN_ROLE) || (isAdmin == true && admin.getRole().equals(Constant.ADMIN_ROLE)) ) 
			{
			}
			else
			{
				return "Admin Id is not authorized to perform this operation";
			}
		}
		else
		{
			return "Class in which course contains, is not valid or doesn't exist.";
		}
	       
        return null;
	}
		
	/**
	 * Function to get class has object	
	 * @param classDetails
	 * @param classServerDetails
	 * @return object
	 * @throws AViewException
	 */
	public static Object classCreationObject(String classDetails,String classServerDetails) throws AViewException
	{
		Response dateValidationResponse ;
		Response startDateResponse;
		Response endDateResponse;
		String dateValidationErrorMessage = null;
		String validationErrorMessage = null;
		
		try
		{
			JSONObject obj = new JSONObject(classDetails);
/*			String startTimeString = obj.getString("startTime");
			timeValidationResponse = TimestampUtils.timeValidation(startTimeString);
			if(timeValidationResponse != null)
			{
				timeValidationErrorMessage = timeValidationResponse.getEntity().toString();
				if(timeValidationErrorMessage != null)
				{
					return Response.status(Status.BAD_REQUEST).entity("Start time "+timeValidationErrorMessage).build();
				}				
			}
		    String endTimeString = obj.getString("endTime");
			timeValidationResponse = TimestampUtils.timeValidation(endTimeString);
			if(timeValidationResponse != null)
			{
				timeValidationErrorMessage = timeValidationResponse.getEntity().toString();
				if(timeValidationErrorMessage != null)
				{
					return Response.status(Status.BAD_REQUEST).entity("End time"+timeValidationErrorMessage).build();
				}
				
			}*/
			String startDateString = obj.getString("startDate");;
			dateValidationResponse = dateValidation(startDateString);
			if(dateValidationResponse != null)
			{
				dateValidationErrorMessage = dateValidationResponse.getEntity().toString();
				if(dateValidationErrorMessage != null)
				{
					return dateValidationErrorMessage +" in start date.";
				}
				
			}
			String endDateString = obj.getString("endDate");
			dateValidationResponse = dateValidation(endDateString);
			if(dateValidationResponse != null)
			{
				dateValidationErrorMessage = dateValidationResponse.getEntity().toString();
				if(dateValidationErrorMessage != null)
				{
					return dateValidationErrorMessage +" in end date.";
				}
				
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		String result = new String();
		//String errorMessage = null;
		logger.debug("Enter class creation::class creation ");
		Class aviewClass = null;
		
		
		Object resultObject = JSONParserUtils.readJSONAsObject(classDetails,Class.class);
		if(Class.class.isInstance(resultObject))
			aviewClass = (Class)resultObject;
		else
		{
			return resultObject.toString();
		}
		if(aviewClass != null)
		{
			
			if (aviewClass.getStartDate() == null) 
			{
				return "Entered startDate is not valid or StartDate doesn't exist.";
			}
			else
			{
				startDateResponse = validationCheckComparisonwithCurrentDate("Start",aviewClass.getStartDate());
				if(startDateResponse != null)
				{
					 String dateErrorMessage = startDateResponse.getEntity().toString();
					return dateErrorMessage;
				}
			}
			if (aviewClass.getEndDate() == null) 
			{
				return "Entered endDate is not valid or EndDate doesn't exist.";
			}
			else
			{
				endDateResponse = validationCheckForEndDate(aviewClass.getStartDate(),aviewClass.getEndDate());
				if(endDateResponse != null)
				{
					 String dateErrorMessage = endDateResponse.getEntity().toString();
					return dateErrorMessage;
				}
			}
		}
		try
		{
				if(classServerDetails.length() == 0 )
				{
					return "Please enter the class server details";
				}
				ArrayList classServer =  new ArrayList();
				Long resultId = 0l;
				JSONObject obj = new JSONObject(classServerDetails);
				String serverdetails = obj.getString("serverDetail");
				String server=serverdetails.replaceAll("\\[", "").replaceAll("\\]","");
				String[] strarray=server.split("},");
				for(Object clsserver:strarray)
				{
					String string=""+clsserver+"}]";
					Object serverObj =JSONParserUtils.readJSONAsObject(string,ClassServer.class);
					classServer.add(serverObj);
					System.out.print(classServer.size());
				}
				Response classServerValidationResponse = validationCheckForClassServer(classServer,aviewClass);
				if(classServerValidationResponse != null)
				{
					String classServerValidationErrorMessage = classServerValidationResponse.getEntity().toString();
					if(classServerValidationErrorMessage != null)
					{
						return classServerValidationErrorMessage;
					}
				}
				for(int i = 0;i < classServer.size(); i++)
				{
	        		ClassServer aviewclassServer = (ClassServer)classServer.get(i);
	        		aviewClass.addClassServer(aviewclassServer);
				}
				return aviewClass;
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Error while entering class server details";
		}
	}
	
	/**
	 * API to create class
	 * @param adminId
	 * @param classDetails as JSON
	 * @return Response
	 * @throws AViewException
	 */
	@RequestMapping(value = "/createclass.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response createClass(@RequestParam("adminId") Long adminId,
			@RequestParam("classDetails") String classDetails,
			@RequestParam("classServerDetails") String classServerDetails) throws AViewException {
		String validationErrorMessage = null;
		String result = new String();
		logger.debug("Enter class creation::class creation ");
		Class aviewClass = null;
		
		String errorMessage = null;
		User admin = null;
		Object resultObjectAdmin =  UserHelper.userValidCheck(Constant.ADMIN_ID,adminId);
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
				
				Object resultObject = classCreationObject(classDetails, classServerDetails);
				if(Class.class.isInstance(resultObject))
				{
					aviewClass = (Class)resultObject;
					if(aviewClass != null && aviewClass.getStatusId() == StatusHelper.getClosedStatusId())
					{
						errorMessage ="Registration for this class has been closed.";
						return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
					}
				}
				else
				{
					String classErrorMessage = resultObject.toString();
					return Response.status(Status.BAD_REQUEST).entity(classErrorMessage).build();
				}
				Long resultId = 0l;
				Object validationMessageForClass = validationCheckForClass(aviewClass,admin);
				if(validationMessageForClass != null)
				{
					return Response.status(Status.BAD_REQUEST).entity(validationMessageForClass.toString()).build();
				}
				
				ClassHelper.createClass(aviewClass, adminId);
				resultId = aviewClass.getClassId();
				logger.debug("Exit class creation on success::class creation.");
				return Response.status(Status.OK).entity(resultId).build(); 
			}
			catch(NumberFormatException nfe) 
			{
				result = "invalid";
				logger.debug("Exit class creation on invalid request::class creation.");
				return Response.status(Status.BAD_REQUEST).entity(result).build();    
			}
			catch(AViewException ae)
			{
				if(ae.getMessage().equals("Duplicate entry '"+ aviewClass.getClassName()+"-"+aviewClass.getCourseId() +"' for key 'class_name_course'"))
				{
					result = "The given class name already exists. Please try a different class name.";
				}
				else if(ae.getMessage().equals("Duplicate entry '"+aviewClass.getClassId()+"' for key 'PRIMARY'"))
				{
					result = "The given class id already exists. Please try a different class id.";
				}
				else
				{
					result = "Error during log. Possible reason(s) : 1. Unexpected data, 2. Unknown.";
				}
				logger.debug("Exit class creation on error durning log::class creation");
				return Response.status(Status.BAD_REQUEST).entity(result).build();
			} 
	}
	/**
	 * API to search class.
	 * @param adminId
	 * @param className 
	 * @param courseId
	 * @param instituteId
	 * @return Response
	 * @throws AViewException
	 */
	@RequestMapping(value = "/searchclass.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response searchClass(@RequestParam("adminId") Long adminId,@RequestParam("className") String className,
			@RequestParam("courseId") Long courseId,@RequestParam("instituteId") Long instituteId ) throws AViewException
	{
		logger.debug("Enter class search::class search ");
		String errorMessage = null;
		Institute institute = null;
		ArrayList classDetailsArray = new ArrayList();
		User admin = null;
		Course course = null;
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
		String trimClassName = className.trim();
		if(courseId != null)
		{
			Course resultObjectCourse = CourseHelper.getCourse(courseId);
			if(resultObjectCourse != null && resultObjectCourse.getStatusId() == StatusHelper.getActiveStatusId())
			{
				if(Course.class.isInstance(resultObjectCourse))
				{
					course = (Course)resultObjectCourse;
				}
				else
				{
					errorMessage = resultObjectCourse.toString();
					errorMessage=(course == null)? null:errorMessage;
					logger.debug(errorMessage);
				}
			}
			else
			{
				return Response.status(Status.BAD_REQUEST).entity("courseId is not valid or doesn't exist").build();
			}
		}
		if(instituteId != null )
		{
			Institute resultObjectInstitute = InstituteHelper.getInstituteById(instituteId);
			if(resultObjectInstitute != null && resultObjectInstitute.getStatusId() == StatusHelper.getActiveStatusId())
			{
				if(Institute.class.isInstance(resultObjectInstitute))
				{
					institute = (Institute)resultObjectInstitute;
				}
				else
				{
					errorMessage = resultObjectInstitute.toString();
					errorMessage = (institute == null) ? null : errorMessage;
					return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
				}
			}
			else
			{
				return Response.status(Status.BAD_REQUEST).entity("instituteId is not valid or doesn't exist").build();
			}
		}
		if(courseId == null && (className == null || className == "") && (instituteId == null ))
		{
			return Response.status(Status.BAD_REQUEST).entity("Please provide any of the search criteria").build();
		}
		List<Class> classDetailList;
		Long courseIdValue =  (course == null) ? null : course.getCourseId();
		Long instituteIdValue = (institute == null) ? null : institute.getInstituteId();
		if(admin.getRole().equals(Constant.MASTER_ADMIN_ROLE))
		{
			adminId = null;
			classDetailList = searchClass(instituteIdValue, courseIdValue, trimClassName, adminId);
		}
		else
		{
			classDetailList = searchClass(instituteIdValue, courseIdValue, trimClassName, adminId);
		}
		if(classDetailList == null || classDetailList.size() == 0) 
		{
			return Response.status(Status.BAD_REQUEST).entity("No active class details returned for the given search criteria").build();
		} 
		else 
		{
			ArrayList classArray = new ArrayList();
			for(Class classDetails:classDetailList)
			{
				classArray = new ArrayList();
				classArray.add("class: " + classDetails.getClassName());
				classArray.add("classId: " + classDetails.getClassId());
				classArray.add("institute: " + classDetails.getInstituteName());
				classArray.add("course: " + classDetails.getCourseName());
				//we are added substring for trimming the date to dateformat yyyy-mm-dd 
				// for eg:classStartDate = 2011-06-14 00:00:00.0 in db by trimming classStartDate becomes 2011-06-14
				String classStartDate = classDetails.getStartDate().toString().substring(0,10);
				classArray.add("startDate: " + classStartDate);
				//we are added substring for trimming the date to dateformat yyyy-mm-dd 
				// for eg:classEndDate = 2011-06-14 00:00:00.0 in db by trimming classEndDate becomes 2011-06-14
				String classEndDate = classDetails.getEndDate().toString().substring(0,10);
				classArray.add("endDate: " + classEndDate);
				classDetailsArray.add(classArray);
				
			}
		}
		logger.debug("Exit class search on success:class search");
		return Response.status(Status.OK).entity(classDetailsArray).build();
	}
	/**
	 * API to delete class.
	 * @param adminId
	 * @param classId 
	 * @return Response
	 * @throws AViewException
	 */
	@RequestMapping(value = "/deleteclass.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response classdelete(@RequestParam("adminId") Long adminId,@RequestParam("classId") Long classId) throws AViewException 
	{
		logger.debug("Enter class delete::class delete ");
		Class  aviewClass= null;
		String errorMessage = null;
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
			Object resultObjectClass = classValidCheck(classId);
			if(Class.class.isInstance(resultObjectClass))
			{
				aviewClass = (Class)resultObjectClass;
			}
			else
			{
				errorMessage = resultObjectClass.toString();
				return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
			}
			if (aviewClass!= null && aviewClass.getStatusId() == StatusHelper.getActiveStatusId())
			{
				Object validationMessageForAdmin = adminValidation(aviewClass, admin);
				if(validationMessageForAdmin != null)
				{
					return Response.status(Status.BAD_REQUEST).entity(validationMessageForAdmin.toString()).build();
				}
				deleteClass(classId, adminId);
				logger.debug("Exit class delete on success:class delete");
				return Response.status(Status.OK).entity("Deleted "+aviewClass.getClassName()+"(ID: " +classId + ") successfully").build();
			}
			else
			{
				return Response.status(Status.BAD_REQUEST).entity("Class "+classId + " doesn't exist").build();
			}
		}
		catch (NumberFormatException nfe) 
		{
			result = "invalid";
			logger.debug("Exit class delete on invalid request::class.");
			return Response.status(Status.BAD_REQUEST).entity(result).build();    
		}
		catch(AViewException ae)
		{
			if(ae.getMessage().equals("Given course is not valid"))
			{
				result = "Please enter a valid class id";
				logger.debug("Exit class delete on error durning log::class."); 
				return Response.status(Status.BAD_REQUEST).entity(result).build();    
			}
		}
		return null;
	}

	@RequestMapping(value = "/updateclass.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response updateClass(@RequestParam("adminId") Long adminId,
			@RequestParam("classDetails")String classDetails,
			@RequestParam("classServerDetails") String classServerDetails) throws AViewException, JSONException 
	{
		Response dateValidationResponse ;
		String result = new String();
		String dateValidationErrorMessage = null;
		try
		{
			JSONObject obj = new JSONObject(classDetails);
/*			String startTimeString = obj.getString("startTime");
			timeValidationResponse = TimestampUtils.timeValidation(startTimeString);
			if(timeValidationResponse != null)
			{
				timeValidationErrorMessage = timeValidationResponse.getEntity().toString();
				if(timeValidationErrorMessage != null)
				{
					return Response.status(Status.BAD_REQUEST).entity("Start time "+timeValidationErrorMessage).build();
				}
				
			}
			String endTimeString = obj.getString("endTime");
			timeValidationResponse = TimestampUtils.timeValidation(endTimeString);
			if(timeValidationResponse != null)
			{
				timeValidationErrorMessage = timeValidationResponse.getEntity().toString();
				if(timeValidationErrorMessage != null)
				{
					return Response.status(Status.BAD_REQUEST).entity("End time"+timeValidationErrorMessage).build();
				}
				
			}*/
			String startDateString = obj.getString("startDate");;
			dateValidationResponse = dateValidation(startDateString);
			if(dateValidationResponse != null)
			{
				dateValidationErrorMessage = dateValidationResponse.getEntity().toString();
				if(dateValidationErrorMessage != null)
				{
					return Response.status(Status.BAD_REQUEST).entity(dateValidationErrorMessage +" in start date.").build();
				}
				
			}
			String endDateString = obj.getString("endDate");
			dateValidationResponse = dateValidation(endDateString);
			if(dateValidationResponse != null)
			{
				dateValidationErrorMessage = dateValidationResponse.getEntity().toString();
				if(dateValidationErrorMessage != null)
				{
					return Response.status(Status.BAD_REQUEST).entity(dateValidationErrorMessage +" in end date.").build();
				}
				
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		User admin = null;
		String errorMessage = null;
		Class tempAviewClass = null;
		Class oldClass = null;
		Response dateResponse = null;
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
			if(classServerDetails.length() == 0 && classDetails.length() == 0)
			{
				return Response.status(Status.BAD_REQUEST).entity("Please provide corresponding class server details to be updated").build();
			}
			if(classDetails != null && classDetails.length()!= 0)
			{
				Object resultObject = JSONParserUtils.readJSONAsObject(classDetails,Class.class);
				if(Class.class.isInstance(resultObject))
				{
					tempAviewClass = (Class)resultObject;
				}
				else
				{
					errorMessage = resultObject.toString();
					logger.error(errorMessage);
					return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
				}
				Object resultObjectClass = classValidCheck(tempAviewClass.getClassId());
				if(Class.class.isInstance(resultObjectClass))
				{
					Class aviewClass = (Class)resultObjectClass;
				}
				else
				{
					errorMessage = resultObjectClass.toString();
					return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
				}
				oldClass = getClass(tempAviewClass.getClassId());
				if(tempAviewClass != null )
				{
					if(tempAviewClass.getClassName()!=null)
					{
						oldClass.setClassName(tempAviewClass.getClassName());
					}
					if(tempAviewClass.getCourseId()!=0)
					{
						oldClass.setCourseId(tempAviewClass.getCourseId());		
					}
					if(tempAviewClass.getClassDescription()!=null)
					{
						oldClass.setClassDescription(tempAviewClass.getClassDescription());
					}
					if(tempAviewClass.getMaxStudents()!=0)
					{
						oldClass.setMaxStudents(tempAviewClass.getMaxStudents());
					}
					if(tempAviewClass.getMinPublishingBandwidthKbps()!=0)
					{
						oldClass.setMinPublishingBandwidthKbps(tempAviewClass.getMinPublishingBandwidthKbps());
					}
					if(tempAviewClass.getMaxPublishingBandwidthKbps()!=0)
					{
						oldClass.setMaxPublishingBandwidthKbps(tempAviewClass.getMaxPublishingBandwidthKbps());
					}
					if(tempAviewClass.getPresenterPublishingBwsKbps()!=null)
					{
						oldClass.setPresenterPublishingBwsKbps(tempAviewClass.getPresenterPublishingBwsKbps());
					}
					if(tempAviewClass.getMaxViewerInteraction()!=1)
					{
						oldClass.setMaxViewerInteraction(tempAviewClass.getMaxViewerInteraction());
					}
					if(tempAviewClass.getVideoCodec()!= null)
					{
						oldClass.setVideoCodec(tempAviewClass.getVideoCodec());
					}
	
					if(tempAviewClass.getStartDate()!=null)
					{
						/*if(oldClass.getStartDate().before(TimestampUtils.getCurrentDate()))
						{
							return Response.status(Status.BAD_REQUEST).entity("Class is started already,so start date can't be change").build();
						}
						if(oldClass.getStartDate().equals(TimestampUtils.getCurrentDate()))
						{
							return Response.status(Status.BAD_REQUEST).entity("Class is started already,so start date can't be change").build();
						}*/
						dateResponse = validationCheckComparisonwithCurrentDate("Start",tempAviewClass.getStartDate());
						if(dateResponse != null)
						{
							  String dateErrorMessage = dateResponse.getEntity().toString();
							 if(dateErrorMessage != null)
							 {
								 return Response.status(Status.BAD_REQUEST).entity(dateErrorMessage).build();
							 }
						}
						oldClass.setStartDate(tempAviewClass.getStartDate());
					}
					if(tempAviewClass.getEndDate()!=null)
					{
						Response currentDateResponse = validationCheckComparisonwithCurrentDate("End",tempAviewClass.getEndDate());
						if(currentDateResponse != null)
						{
							 String dateErrorMessage = currentDateResponse.getEntity().toString();
							 if(dateErrorMessage != null)
							 {
								 return Response.status(Status.BAD_REQUEST).entity(dateErrorMessage).build();
							 }
						}
						dateResponse=validationCheckForEndDate(oldClass.getStartDate(),tempAviewClass.getEndDate());
						if(dateResponse!=null)
						{
							 String dateErrorMessage = dateResponse.getEntity().toString();
							 if(dateErrorMessage != null)
							 {
								 return Response.status(Status.BAD_REQUEST).entity(dateErrorMessage).build();
							 }
						}
						oldClass.setEndDate(tempAviewClass.getEndDate());
					}
				}
			}
			else
			{
				return Response.status(Status.BAD_REQUEST).entity("Please provide the corresponding class details to be updated").build();
			}
			
			if(oldClass != null)
			{
				Object validationMessageForClass = validationCheckForClass(oldClass,admin);
				if(validationMessageForClass != null)
				{
					return Response.status(Status.BAD_REQUEST).entity(validationMessageForClass.toString()).build();
				}
				if(classServerDetails != null && classServerDetails.length() != 0)
				{
					Object serverObj = new Object();
					ArrayList classServer =  new ArrayList();
					JSONObject obj = new JSONObject(classServerDetails);
					String serverdetails = obj.getString("serverDetail");
					String server=serverdetails.replaceAll("\\[", "").replaceAll("\\]","");
					String[] strarray=server.split("},");
					for(Object clsserver:strarray)
					{
						String string = ""+clsserver+"}";
						
						serverObj =JSONParserUtils.readJSONAsObject(string,ClassServer.class);
						classServer.add(serverObj);
					}
					for(ClassServer oldClassServer:oldClass.getClassServers())
					{
						for(int i = 0;i < classServer.size();i++)
						{
							ClassServer classServerClass = (ClassServer)classServer.get(i);
							if(oldClassServer.getServerTypeId().equals(classServerClass.getServerTypeId()))
							{
								oldClassServer.setServerPort(classServerClass.getServerPort());
								oldClassServer.setPresenterPublishingBandwidthKbps(classServerClass.getPresenterPublishingBandwidthKbps());
								oldClassServer.getServer().setServerId(classServerClass.getServer().getServerId());
								break;
							}
						}
					}
					Response classServerValidationResponse = validationCheckForClassServer(classServer,oldClass);
					if(classServerValidationResponse != null)
					{
						String classServerValidationErrorMessage = classServerValidationResponse.getEntity().toString();
						if(classServerValidationErrorMessage != null)
						{
							return Response.status(Status.BAD_REQUEST).entity(classServerValidationErrorMessage).build();
						}
					}
				}
				else
				{
					oldClass.setClassServers(oldClass.getClassServers());
				}
			}
		
			Long resultId = 0l;
			ClassHelper.updateClass(oldClass, adminId);
			resultId = oldClass.getClassId();
			logger.debug("Exit class updation on success::class updation.");
			return Response.status(Status.OK).entity("Updated class (ID: "+ resultId +") successfully").build(); 
		}
		catch(NumberFormatException nfe) 
		{
			result = "invalid";
			logger.debug("Exit class updation on invalid request::class updation.");
			return Response.status(Status.BAD_REQUEST).entity(result).build();    
		}
		catch(AViewException ae)
		{
			if(ae.getMessage().equals("Given course is not valid"))
			{
					result = "Please enter a valid class id";
					logger.debug("Exit lecture updation on error durning log::class updation."); 
					return Response.status(Status.BAD_REQUEST).entity(result).build();    
			}
			else if(ae.getMessage().equals("Duplicate entry '"+ oldClass.getClassName()+"-"+oldClass.getCourseId() +"' for key 'class_name_course'"))
			{
				result = "The given class name already exists. Please try a different class name.";
			}
			else
			{
				result = "Error during log. Possible reason(s) : 1. Unexpected data, 2. Unknown.";
			}
			logger.debug("Exit class updation on error durning log::class updation");
			return Response.status(Status.BAD_REQUEST).entity(result).build();
		}
	}

	/**
	 * Function to check whether the class id given is valid or not
	 * @param classId
	 * @return object
	 * @throws AViewException
	 */
	public  static Object classValidCheck(Long classId) throws AViewException
	{
		Object convertedObject = new Object();
		Class aviewClass = null;
		if(classId == null)
		{
			classId = 0l;
		}
		else
		{
			aviewClass = ClassHelper.getClass(classId);
		}
		if(aviewClass == null)
		{
			convertedObject = "Entered class id is not valid or doesn't exist.";
		}
		else
		{
			if(aviewClass.getStatusId() == StatusHelper.getActiveStatusId() || aviewClass.getStatusId() == StatusHelper.getClosedStatusId() )
			{ 
				logger.debug("Given class is valid.");
				convertedObject = aviewClass;
			}
			else if(aviewClass.getStatusId() ==  StatusHelper.getDeletedStatusId())
			{ 
				convertedObject = "Given class id is already deleted";
			}
			else
			{
				convertedObject = "Given class id is not valid.";
			}
		}
		return convertedObject;
	}
	
	/**
	 * Function to validate date for API
	 * @param dateString
	 * @return response
	 * @throws AViewException
	 */
	public static Response dateValidation(String dateString) throws AViewException
	{
		String dateError = null;
		if(!dateString.equals(null))
		{
			dateError = ValidationUtils.DateValidation(dateString);
			if(dateError != null)
			{
				return Response.status(Status.BAD_REQUEST).entity(dateError).build();
			}
			else
			{
				String month = dateString.substring(5,7);
				String year = dateString.substring(0,4);
				String day = dateString.substring(8,10);
				if(day.equals("31") && (month.equals("04") || month.equals("06")	|| month.equals("09") || month.equals("11")) )
				{
					return Response.status(Status.BAD_REQUEST).entity("Month is not valid").build();
				}
				else if(month.equals("02"))
				{
					int yearCheckStartDate = Integer.parseInt(year);
					if(yearCheckStartDate%4 == 0)
					{
						if(day.equals("30") || day.equals("31"))
						{
							return Response.status(Status.BAD_REQUEST).entity("Day is not valid" ).build();
						}
					}
					else
					{
						if(day.equals("29") || day.equals("30") || day.equals("31"))
						{
							return Response.status(Status.BAD_REQUEST).entity("Day is not valid" ).build();
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Function to validate time for API
	 * @param timeString
	 * @return response
	 * @throws AViewException
	 */
	public static Response timeValidation(String timeString) throws AViewException
	{
		String timeError = null;
		if(!timeString.equals(null))
		{
			//String startValue = obj.getString("startTime");
			String mid = timeString.substring(11, 16);
			timeError = ValidationUtils.Timevalidator(mid);
			if (timeError != null) 
			{
				return Response.status(Status.BAD_REQUEST).entity(timeError).build();
			} 
			else 
			{
				if (mid != null) 
				{
					String tim = "1899-12-31T" + mid + ":00.000+0530";
					//classDetails = obj.put("startTime", tim).toString();
				}
			}
		}
		return null;
	}
}
