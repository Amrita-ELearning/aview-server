/**
 * 
 */
package edu.amrita.aview.meeting.helpers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.Constant;
import edu.amrita.aview.gclm.daos.ClassDAO;
import edu.amrita.aview.gclm.daos.LectureDAO;
import edu.amrita.aview.gclm.entities.Class;
import edu.amrita.aview.gclm.entities.ClassRegistration;
import edu.amrita.aview.gclm.entities.ClassServer;
import edu.amrita.aview.gclm.entities.Course;
import edu.amrita.aview.gclm.entities.Institute;
import edu.amrita.aview.gclm.entities.InstituteServer;
import edu.amrita.aview.gclm.entities.Lecture;
import edu.amrita.aview.gclm.entities.ServerType;
import edu.amrita.aview.gclm.entities.User;

import edu.amrita.aview.gclm.helpers.ClassHelper;
import edu.amrita.aview.gclm.helpers.ClassRegistrationHelper;
import edu.amrita.aview.gclm.helpers.CourseHelper;
import edu.amrita.aview.gclm.helpers.InstituteHelper;
import edu.amrita.aview.gclm.helpers.LectureHelper;
import edu.amrita.aview.gclm.helpers.NodeTypeHelper;
import edu.amrita.aview.gclm.helpers.ServerTypeHelper;
import edu.amrita.aview.gclm.helpers.UserHelper;


import edu.amrita.aview.common.utils.HashCodeUtils;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.common.helpers.EmailHelper;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.meeting.vo.MeetingRoomVO;

/**
 * @author(s) Ramesh G, Sethu Subramanian N, Soumya M D
 *
 */
public class MeetingManagerHelper 
{
	//The default course name is <userName>_Meeting.
	//Check if the course is avaliable else create one
	
	//All bandwidth values are in Kbps
	private static final int MAXIMUM_STUDENTS_TO_REGISTER = 1000;
	private static final int MAXIMUM_PUBLISHING_BANDWIDTH = 8505;
	//Fix for Bug #13330 start
	//Changed the minimum publishing bandwidth to 56 kbps
	private static final int MINIMUM_PUBLISHING_BANDWIDTH = 28;
	//Fix for Bug #13330 end
	private static final int MAXIMUM_VIEWER_INTERACTION = 8;
	
	private static final String VIDEO_CODEC = "H.264";
	private static final String VIDEO_STREAMING_PROTOCOL = "RTMP";
	private static final String IS_MULTI_BITRATE = "N";
	private static final String ALLOW_DYNAMIC_SWITCHING = "N";
	//Fix for Bug #13330 start
	//Changed the presenter publishing bandwidth to 56 Kbps
	private static final String PRESENTER_PUBLISHING_BANDWIDTH = "56";
	//Fix for Bug #13330 end
	private static final String AUDIT_LEVEL = "Action";
	private static final String REGISTRATION_TYPE = "Approval";
	
	private static final String DEFAULT_INSTITUTE_NAME = "__DUMMY__INSTITUTE__";
	
	//To set the monitoring attribute to No by default
	private static final String DEFAULT_CAN_MONITOR_VALUE = "No";
	
	private static Long defaultInstituteId = null;
	private static Integer meetingPresenterServerTypeId = null;
	private static Integer meetingDataServerTypeId = null;
	private static Integer meetingViewerServerTypeId = null;
	private static Integer meetingContentServerTypeId = null;
	private static Integer meetingDesktopSharingServerTypeId = null;

	private static Logger logger = Logger.getLogger(MeetingManagerHelper.class);

	static
	{
		try
		{
			List<ServerType> serverTypes = ServerTypeHelper.getAllServerTypes();
			for(ServerType serverType : serverTypes)
			{
				if(serverType.getServerType().equals("MEETING_FMS_VIDEO_PRESENTER"))
				{
					meetingPresenterServerTypeId = serverType.getServerTypeId();
				}
				else if(serverType.getServerType().equals("MEETING_FMS_DATA")) 
				{
					meetingDataServerTypeId = serverType.getServerTypeId();
				}
				else if(serverType.getServerType().equals("MEETING_FMS_VIDEO_VIEWER")) 
				{
					meetingViewerServerTypeId = serverType.getServerTypeId();
				}
				else if(serverType.getServerType().equals("MEETING_CONTENT_SERVER")) 
				{
					meetingContentServerTypeId = serverType.getServerTypeId();
				}
				else if(serverType.getServerType().equals("MEETING_FMS_DESKTOP_SHARING")) 
				{
					meetingDesktopSharingServerTypeId = serverType.getServerTypeId();
				}
			}
		}
		catch(AViewException ae)
		{
			
		}
	}
	 
	private static Long getDefaultInstitute() throws AViewException
	{
		if(defaultInstituteId == null)
		{
			defaultInstituteId = InstituteHelper.getInstituteByName(DEFAULT_INSTITUTE_NAME).getInstituteId();
		}
		return defaultInstituteId;
	}
	
	private static Course createCourse(String courseName, User creator) throws AViewException
	{
		Course course = new Course();
		//Course name and course code is same
		course.setCourseName(courseName);
		course.setCourseCode(courseName);
		//Institute id is same as that of the moderator (or) creator id
		course.setInstituteId(creator.getInstituteId());
		CourseHelper.createCourse(course, creator.getUserId());
		return course;
	}
	
	private static Course checkForCourseExistenceAndCreate(String courseName, User creator) throws AViewException
	{
		Course actualCourse = null;
		List<Course> defaultMeetingCourse = CourseHelper.searchCourse(courseName, courseName, creator.getInstituteId());
		if((defaultMeetingCourse == null) || (defaultMeetingCourse.size() == 0))
		{
			//Need to create a course
			actualCourse = createCourse(courseName, creator);
		}
		else if(defaultMeetingCourse.size() == 1)
		{
			actualCourse = defaultMeetingCourse.get(0);
		}
		else
		{
			throw new AViewException("Course name duplication while creating new meeting course");
		}
		return actualCourse;
	}
	
	private static User checkForUserExistenceByEmailId(String emailId) throws AViewException
	{
		User user = null;
		// Fix for Bug #13227 start
		List<User> users = UserHelper.searchActiveUsers(null, null, null, null, null, null, null, emailId, null,null,null);
		// Fix for Bug #13227 end
		if((users != null) && (users.size() > 0))
		{
			for(User tmpUser : users)
			{
				if(tmpUser.getEmail().equals(emailId))
				{
					user = tmpUser;
					break;
				}
			}
		}
		return user;
	}
	
	private static User createGuestUser(String userName, String emailId, Long creatorId)throws AViewException 
	{
		User user = new User();
		user.setUserName(userName);
		user.setPassword(HashCodeUtils.SHA1(userName));
		user.setRole(Constant.STUDENT_ROLE);
		user.setFname(userName);
		user.setLname(userName);
		user.setEmail(emailId);
		user.setInstituteId(getDefaultInstitute());
		user.setCreatedFrom(Constant.CREATED_FROM_MEETING) ;
		try
		{
			UserHelper.createUser(user, creatorId, StatusHelper.getActiveStatusId());
		}
		catch(AViewException ae)
		{
			logger.error("Error in creating user");
		}
		return user;
	}
	
	private static List<User> createGuestUsers(List<String> guestMembersEmail, Long creatorId) throws AViewException
	{
		List<User> guestUsersForClassRegistrations = new ArrayList<User>();
		//Fix for Bug #13501 start
		//Array list to know the list of existing users whose email is given by the moderator
		List<String> existingUsersEmailIds = new ArrayList<String>();
		//Fix for Bug #13501 end
		User user = null;
		for(String emailId : guestMembersEmail)
		{
			//Check if the user with same email id exists
			user = checkForUserExistenceByEmailId(emailId);
			if(user == null)
			{
				user = createGuestUser(emailId, emailId, creatorId);
			}
			else
			{
				//Fix for Bug #13501 start
				existingUsersEmailIds.add(user.getEmail());
				//Fix for Bug #13501 end
				
				// For existing user change the role to student, if it is a guest role
				String userRole = user.getRole();
				if(userRole.equals(Constant.GUEST_ROLE))
				{
					user.setRole(Constant.STUDENT_ROLE);
					UserHelper.updateUser(user, creatorId);
				}
				//Check if user is neither a teacher nor a student
				//If so, they are either admin/master admin or executive admin
				//to make them attend the meeting, we create a dummy user name for them
				//with the last five digits of the current time stamp
				else if(!((userRole.equals(Constant.TEACHER_ROLE)) || (userRole.equals(Constant.STUDENT_ROLE))))
				{
					//If the user is neither a student nor a teacher but a part of masteradmin/admin/executive admin,
					//create another user name for the same user, which could be used for meeting 
					//purposes. Before creating, check if that user name exists.
					//For easiness, the newly created user name would be <userName>_meeting where userName
					//is the existing user name of the masteradmin/admin/executive admin
					String userName = user.getUserName() + "_meeting";
					user = UserHelper.getUserByUserName(userName);
					if((user != null) && (user.getEmail().equals(emailId)))
					{
						//meeting username is already there for the admin
						//so do nothing
					}
					else if((user != null) && (!user.getEmail().equals(emailId)))
					{
						//user id existing but a different email
						//this is possible when normal user creates an user name like that
						
						//TODO identify what should be done here
					}
					else if(user == null)
					{
						//create a guest user for admin
						user = createGuestUser(userName, emailId, creatorId);
					}
				}
			}
			if(user != null)
			{
				guestUsersForClassRegistrations.add(user);
			}
		}
		//Fix for Bug #13501 start
		//Remove the email id of the existing user from the list
		if(existingUsersEmailIds != null)
		{
			guestMembersEmail.removeAll(existingUsersEmailIds);
		}
		//Fix for Bug #13501 end
		return guestUsersForClassRegistrations;
	}
	
	private static void  createClassRegistrationForModerator(Class aviewClass, Long creatorId) throws AViewException
	{
		ClassRegistration classRegistration = new ClassRegistration();
		classRegistration.setAviewClass(aviewClass);
		classRegistration.setUser(UserHelper.getUser(creatorId));
		classRegistration.setIsModerator("Y");
		classRegistration.setEnable2DSharing("Y");
		classRegistration.setEnable3DSharing("Y");
		classRegistration.setEnableAudioVideo("Y");
		classRegistration.setEnableDesktopSharing("Y");
		classRegistration.setEnableDocumentSharing("Y");
		classRegistration.setEnableVideoSharing("Y");
		classRegistration.setNodeTypeId(NodeTypeHelper.getPersonalComputerNodeType());
		ClassRegistrationHelper.createClassRegistration(classRegistration, creatorId, StatusHelper.getActiveStatusId());
	}
	
	private static void createClassRegistrationForUsers(Class aviewClass, Long creatorId, List<User> meetingMembers) throws AViewException
	{
		List<ClassRegistration> classRegistrations = new ArrayList<ClassRegistration>();
		ClassRegistration classRegistration = null;
		for(User user : meetingMembers)
		{
			classRegistration = new ClassRegistration();
			classRegistration.setAviewClass(aviewClass);
			classRegistration.setUser(user);
			classRegistration.setIsModerator("N");
			classRegistration.setEnable2DSharing("Y");
			classRegistration.setEnable3DSharing("Y");
			classRegistration.setEnableAudioVideo("Y");
			classRegistration.setEnableDesktopSharing("Y");
			classRegistration.setEnableDocumentSharing("Y");
			classRegistration.setEnableVideoSharing("Y");
			classRegistration.setNodeTypeId(NodeTypeHelper.getPersonalComputerNodeType());
			classRegistrations.add(classRegistration);
		}		
		ClassRegistrationHelper.createBulkClassRegistrationsForNonModerators(classRegistrations, creatorId, StatusHelper.getActiveStatusId());
	}
	
	public static Boolean isMeetingServer(InstituteServer instituteServer)
	{
		if( (instituteServer.getServerTypeId().equals(meetingPresenterServerTypeId)) ||
			    (instituteServer.getServerTypeId().equals(meetingViewerServerTypeId)) ||
			    (instituteServer.getServerTypeId().equals(meetingDesktopSharingServerTypeId)) ||
			    (instituteServer.getServerTypeId().equals(meetingContentServerTypeId)) ||
			    (instituteServer.getServerTypeId().equals(meetingDataServerTypeId)))
			{
				return true;
			}
		return false;
	}


	public static void assignMeetingServers(Class aviewClass, Long instituteId) throws AViewException
	{
		//Remove all the existing server from this class
		aviewClass.getClassServers().clear();
		
		Institute institute = InstituteHelper.getInstituteById(instituteId);
		Set<InstituteServer> instituteServers = institute.getInstituteServers();
		ClassServer classServer = null;
		for(InstituteServer instituteServer : instituteServers)
		{
			classServer = new ClassServer();
			classServer.setServer(instituteServer.getServer());
			classServer.setPresenterPublishingBandwidthKbps(128);
			classServer.setServerTypeId(instituteServer.getServerTypeId());
			if( (instituteServer.getServerTypeId().equals(meetingPresenterServerTypeId)) ||
			    (instituteServer.getServerTypeId().equals(meetingViewerServerTypeId)) ||
			    (instituteServer.getServerTypeId().equals(meetingDesktopSharingServerTypeId)) ||
			    (instituteServer.getServerTypeId().equals(meetingDataServerTypeId)))
			{
				classServer.setServerPort(1935);
				aviewClass.addClassServer(classServer);
			}
			else if(instituteServer.getServerTypeId().equals(meetingContentServerTypeId))
			{
				classServer.setServerPort(80);
				aviewClass.addClassServer(classServer);
			}
		}
	}
	
	public static void createMeetingRoom(Class aviewClass, List<Long> meetingMembers, List<String> guestMembers, Long creatorId) throws AViewException
	{
		User creator = UserHelper.getUser(creatorId);
		//Check if it is a create (or) update
		if(aviewClass.getClassId().equals(new Long(0)))
		{
			//create
			Course course = checkForCourseExistenceAndCreate(aviewClass.getCourseName(), creator);
			aviewClass.setCourseId(course.getCourseId());
			aviewClass.setMaxStudents(MAXIMUM_STUDENTS_TO_REGISTER);
			aviewClass.setMinPublishingBandwidthKbps(MINIMUM_PUBLISHING_BANDWIDTH);
			aviewClass.setMaxPublishingBandwidthKbps(MAXIMUM_PUBLISHING_BANDWIDTH);
			aviewClass.setVideoCodec(VIDEO_CODEC);
			aviewClass.setVideoStreamingProtocol(VIDEO_STREAMING_PROTOCOL);
			aviewClass.setIsMultiBitrate(IS_MULTI_BITRATE);
			aviewClass.setAllowDynamicSwitching(ALLOW_DYNAMIC_SWITCHING);
			aviewClass.setPresenterPublishingBwsKbps(PRESENTER_PUBLISHING_BANDWIDTH);
			aviewClass.setAuditLevel(AUDIT_LEVEL);
			aviewClass.setMaxViewerInteraction(MAXIMUM_VIEWER_INTERACTION);
			aviewClass.setRegistrationType(REGISTRATION_TYPE);
			//Set the class schedule type to Adhoc irrespective of what is being set in the client
			//for all meetings
			aviewClass.setScheduleType(Constant.ADHOC_CLASS_TYPE);
			aviewClass.setClassType(Constant.MEETING_CLASS_TYPE);
			//Making the monitoring default as No 
			aviewClass.setCanMonitor(DEFAULT_CAN_MONITOR_VALUE);
//			assignMeetingServers(aviewClass, creatorId);
			ClassHelper.removeDateTimes(aviewClass);
			ClassHelper.createClass(aviewClass, creatorId);
			createClassRegistrationForModerator(aviewClass, creatorId);
			registerUsersForMeeting(aviewClass, meetingMembers, guestMembers, creatorId);
		}
		else
		{
			throw new AViewException("Meeting is already created");
		}
	}
	
	private static void checkForClassRegistrationAndRemove(Class aviewClass, List<User>registeredUsers) throws AViewException	
	{
		List<Long> userIds = new ArrayList<Long>();
		if((registeredUsers != null) && (registeredUsers.size() > 0))
		{
			for(User user : registeredUsers)
			{
				userIds.add(user.getUserId());
			}
			
			List<ClassRegistration> classRegistrations = ClassRegistrationHelper.getClassRegistersForClass(aviewClass.getClassId());
			
			if((classRegistrations != null) && (classRegistrations.size() > 0))
			{
				for(ClassRegistration clr : classRegistrations)
				{
					if(registeredUsers.contains(clr.getUser()))
					{
						registeredUsers.remove(clr.getUser());
					}
				}
			}
		}
	}
	
	private static void registerUsersForMeeting(Class aviewClass, List<Long> meetingMembers, List<String> guestMembers, Long creatorId) throws AViewException
	{
		List<User> usersForMeetingRegistrations = new ArrayList<User>();
		if((guestMembers != null) && (guestMembers.size() > 0))
		{
			usersForMeetingRegistrations.addAll(createGuestUsers(guestMembers, creatorId));
		}
		if( (meetingMembers != null) && (meetingMembers.size() > 0) )
		{
			usersForMeetingRegistrations.addAll(UserHelper.getUsers(meetingMembers));
		}
		checkForClassRegistrationAndRemove(aviewClass, usersForMeetingRegistrations);
		createClassRegistrationForUsers(aviewClass, creatorId, usersForMeetingRegistrations);
	}
	
	private static void prepareMeetingInvitationAndSend(String meetingName, Lecture lecture, Class aviewClass, List<String> guestMembers, List<User> existingUsers, Long moderatorId, boolean isScheduled) throws AViewException
	{
		//Fix for Bug #13670 start
		//Check if ~ is there as part of lecture Name. If so remove.
		int tildaIndex = meetingName.indexOf("~");
		if(tildaIndex > 0)
		{
			meetingName = meetingName.substring(0, tildaIndex);
		}
		
		//Fix for Bug #13670 end
		List<User> guestUsersToSendEmail = new ArrayList<User>();
		List<User> existingUsersToSendEmail = new ArrayList<User>();
		List<ClassRegistration> classRegistrations = getMeetingRoomMembers(aviewClass.getClassId(), moderatorId);  
		User user = null;
		if((classRegistrations != null) && (classRegistrations.size() > 0))
		{
			for(ClassRegistration clr : classRegistrations)
			{
				user = clr.getUser();
				//The email ids of those users who are already registered is removed
				//during createGuestUsers
				if((guestMembers != null) && (guestMembers.contains(user.getEmail())))
				{
					guestUsersToSendEmail.add(user);
				}
				else
				{
					existingUsersToSendEmail.add(user);
				}
			}
		}
		//In case of sending email during update schedule, send the meeting invitation
		//only to those users who are newly added. Already registerd users will 
		//get email on change schedule notification
		if((existingUsers != null) && (existingUsers.size() > 0))
		{
			existingUsersToSendEmail.removeAll(existingUsers);
		}
		//Email invitation for guest users
		if(guestUsersToSendEmail.size() > 0)
		{
			if(lecture == null)
			{
				EmailHelper.sendMeetingInvitation(meetingName, aviewClass, guestUsersToSendEmail, UserHelper.getUser(moderatorId), true, isScheduled);
			}
			else
			{
				EmailHelper.sendMeetingInvitation(lecture, guestUsersToSendEmail, UserHelper.getUser(moderatorId), true);
			}
		}
		if(existingUsersToSendEmail.size() > 0)
		{
			if(lecture == null)
			{
				EmailHelper.sendMeetingInvitation(meetingName, aviewClass, existingUsersToSendEmail, UserHelper.getUser(moderatorId), false, isScheduled);
			}
			else
			{
				EmailHelper.sendMeetingInvitation(lecture, existingUsersToSendEmail, UserHelper.getUser(moderatorId), false);
			}
		}
	}
	
	public static Lecture createAdhocMeeting(Class aviewClass,
			List<String> guestMembers, String lectureName,
			Date scheduleDate, Date startTime,
			Date endTime, Long moderatorId)throws AViewException
			{
				
			   Class classvo=ClassHelper.getClass(aviewClass.getClassId());
			   boolean hasCreatedMeetingSchedule = false;
			   Lecture meeting=null;
		       Session session=null;
		       session=HibernateUtils.getCurrentHibernateConnection();
		       try
		       {
		    		session.beginTransaction();
					if(aviewClass.getEndDate().compareTo(scheduleDate)<0)
					{
						aviewClass.setEndDate(scheduleDate);
						classvo.updateFrom(aviewClass);
						classvo.setModifiedAuditData(moderatorId, TimestampUtils.getCurrentTimestamp());
						ClassDAO.updateClass(classvo);
					}
					List<Lecture> lectures=getLecturesForClassCreate(aviewClass, "NNNNNNN", scheduleDate, scheduleDate, startTime, endTime, lectureName);
					//meeting = LectureHelper.createLecture(lectures.get(0), creatorId);
					LectureHelper.createLectures(lectures, moderatorId);
					meeting = lectures.get(0);
					//Fix for Bug #13437 start
					session.getTransaction().commit();
					//Fix for Bug #13437 end
					hasCreatedMeetingSchedule = true;
		       }
		       catch(HibernateException he)
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
		       
		       if(hasCreatedMeetingSchedule)
		       {
		    	   if(guestMembers != null)
		    	   {
			    	   registerUsersForMeeting(aviewClass, null, guestMembers, moderatorId);
		    	   }
		    	   prepareMeetingInvitationAndSend(lectureName, null, aviewClass, guestMembers, null, moderatorId, false);
		       }
		       return meeting;
			}
			
	public static void createMeetingSchedule(Class newAviewClass, 
			List<String> guestMembers,
			String lectureName,
			String weekDays,
			Date scheduleStartDate,
			Date scheduleEndDate,
			Date startTime,
			Date endTime,
			Long moderatorId)throws AViewException
	{	
		Class oldAviewClass = ClassHelper.getClass(newAviewClass.getClassId());
		//Fix for Bug #13704 start
		List<Lecture> lectures = null;
		//Fix for Bug #13704 end
		if((newAviewClass.getWeekDays() == null) || (!newAviewClass.getWeekDays().equals(weekDays)))
		{
			newAviewClass.setWeekDays(weekDays);
		}
		Session session = null;
		boolean hasCreatedMeetingSchedule = false;
		try
		{
			session = HibernateUtils.getCurrentHibernateConnection();
			session.beginTransaction();
			newAviewClass.setStartTime(startTime);
			newAviewClass.setEndTime(endTime);
			if(newAviewClass.getEndDate().compareTo(scheduleEndDate)<0)
			{
				newAviewClass.setEndDate(scheduleEndDate);
			}
			ClassHelper.removeDateTimes(newAviewClass);
			oldAviewClass.updateFrom(newAviewClass);
			oldAviewClass.setModifiedAuditData(moderatorId, TimestampUtils.getCurrentTimestamp());
			ClassDAO.updateClass(oldAviewClass);
			lectures=getLecturesForClassCreate(newAviewClass, weekDays, scheduleStartDate, scheduleEndDate, startTime, endTime, lectureName);
			LectureHelper.createLectures(lectures,moderatorId);
			session.getTransaction().commit();
			hasCreatedMeetingSchedule = true;
		}
		catch(HibernateException he)
		{
			processException(he);
			session.getTransaction().rollback();
		}
		finally 
		{
			HibernateUtils.closeConnection(session);
		}
		//Check if there are guest users and the meeting schedule is created successfully
		//If so, register the new guest users for the meeting
		if(hasCreatedMeetingSchedule)
		{
			if(guestMembers != null)
			{
				registerUsersForMeeting(newAviewClass, null, guestMembers, moderatorId);
			}
			//Fix for Bug #13704 start
			//In case of single meeting send only the lecture details
			if(lectures != null)
			{
				if(lectures.size() == 1)
				{
					prepareMeetingInvitationAndSend(lectureName, lectures.get(0), newAviewClass, guestMembers, null, moderatorId, true);
				}
				else
				{
					//Fix for Bug #13788 start
					//Before sending the email set the start and end date that corresponds to
					//this meeting schedule
					newAviewClass.setStartDate(scheduleStartDate);
					newAviewClass.setEndDate(scheduleEndDate);
					//Fix for Bug #13788 end
					prepareMeetingInvitationAndSend(lectureName, null, newAviewClass, guestMembers, null, moderatorId, true);	
				}
			}
			//Fix for Bug #13704 end
		}
	}
	
	private static List<Lecture> getLecturesForClassCreate(Class aviewClass,String weekDays,
			Date scheduleStartDate,
			Date scheduleEndDate,
			Date startTime,
			Date endTime,
			String lectureName)
	{
		List<Lecture> aviewLectures =new ArrayList<Lecture>() ;
		String week = weekDays ;
	 	GregorianCalendar classEndCal = new GregorianCalendar();
	 	classEndCal.setTime(scheduleEndDate);
	 	GregorianCalendar classStartCal  = new GregorianCalendar();
	 	classStartCal.setTime(scheduleStartDate) ;
	 	GregorianCalendar lectureStartTime  = new GregorianCalendar();
	 	lectureStartTime.setTime(startTime);
	 	GregorianCalendar lectureStartCal  = null;
	 	// then set the start date of lecture to  start date of class
	 	lectureStartCal = classStartCal;
	 	logger.debug(" lecture start cal ::"+lectureStartCal.getTime());
	 	classEndCal.add(Calendar.DATE, 1) ;
	 	Lecture lecture = null;
	 	if(scheduleStartDate.compareTo(scheduleEndDate)==0)
		{
	 		lecture = new Lecture() ;
				lecture.setClassId(aviewClass.getClassId());
				lecture.setEndTime(endTime);
				lecture.setStartTime(startTime);
				lecture.setKeywords("");
				lecture.setLectureNumber(0);
				lecture.setStartDate(lectureStartCal.getTime());
			lecture.setLectureName(lectureName);
			appendDateTimeToLecture(lecture);
				aviewLectures.add(lecture);
		}
	 	else
	 	{
	 		while(lectureStartCal.before(classEndCal)) // checking the start date of lecture , upto the end date of class 
	 		{
	 			// to create lectures from start date of class to end date of class
	 	 		// check the weekday if there is a lecture on that particular weekday
	 			if((isTheLectureDay(week,lectureStartCal.get(Calendar.DAY_OF_WEEK))))
	 			{
	 				lecture = new Lecture();
	 				lecture.setClassId(aviewClass.getClassId());
	 				lecture.setEndTime(endTime);
	 				lecture.setStartTime(startTime);
	 				lecture.setKeywords("");
	 				lecture.setLectureNumber(0);
	 				lecture.setStartDate(lectureStartCal.getTime());
	 				lecture.setLectureName(lectureName);
	 				appendDateTimeToLecture(lecture);
	 				aviewLectures.add(lecture);
	 			}
	 			lectureStartCal.add(Calendar.DATE,1) ;
	 		}
	 	}
	 	return aviewLectures;
	}
	
	private static void appendDateTimeToLecture(Lecture lecture)
	{
		GregorianCalendar gcStartDate = new GregorianCalendar();
		gcStartDate.setTime(lecture.getStartDate());
		GregorianCalendar gcStartTime = new GregorianCalendar();
		gcStartTime.setTime(lecture.getStartTime());
		
		lecture.setLectureName(lecture.getLectureName() + "~" +
							  gcStartDate.get(Calendar.YEAR) + "_" +
							  (gcStartDate.get(Calendar.MONTH)+1)+ "_" + 
							  gcStartDate.get(Calendar.DATE)+"_" + 
							  gcStartTime.get(Calendar.HOUR_OF_DAY) + "_" + 
							  gcStartTime.get(Calendar.MINUTE) +"_" + 
							  gcStartTime.get(Calendar.SECOND));
	}
		
	private static boolean isTheLectureDay(String weekDays,int day)
	{
		boolean flag = false ;
			day = day-1;
			if(weekDays.charAt(day) == 'Y')
			{
				flag = true ;
			}		
		return flag ;
	}
	
	//Fix for Bug # 13796
	//Added the lecture object in case of editing from inside meeting room
	public static void updateMeetingRoom(Class newAviewClass,Lecture onGoingMeeting, List<Long> meetingMembers, List<String> guestMembers,String isInsideMeetingRoom,Long moderatorId) throws AViewException
	{
		Session session = null;
		Class oldAviewClass = ClassHelper.getClass(newAviewClass.getClassId());
		boolean hasChangeInCourseName = false;
		boolean hasChangeInClassName = false;
		//Fix for Bug #13291 
		//Check if the class name is same as the old class name. If not, change
		//the course name
		//Fix for Bug #13693 start
		if(!(oldAviewClass.getCourseName().equals(newAviewClass.getCourseName())))
		{
			hasChangeInCourseName = true;
		}
		if(!(oldAviewClass.getClassName().equals(newAviewClass.getClassName())))
		{
			hasChangeInClassName = true;
		}
		//Fix for Bug #13693 end
		boolean hasUpdatedMeetingRoom = true;
		if(hasChangeInClassName)
		{
			ClassHelper.removeDateTimes(newAviewClass);
			session = HibernateUtils.getCurrentHibernateConnection();
			try
			{
				session.beginTransaction();	
				oldAviewClass.updateFrom(newAviewClass);
				oldAviewClass.setModifiedAuditData(moderatorId, TimestampUtils.getCurrentTimestamp());
				ClassDAO.updateClass(oldAviewClass);
				session.getTransaction().commit();
			}
			catch(HibernateException he)
			{
				hasUpdatedMeetingRoom = false;
				processException(he);
				session.getTransaction().rollback();
			}
			finally
			{
				HibernateUtils.closeConnection(session);
			}
		}
		//Fix for Bug #13291 start
		//If there is a change in the class name, then change the course name as well
		if(hasChangeInCourseName)
		{
			//Fix for Bug #13693 start
			Course course = CourseHelper.getCourse(newAviewClass.getCourseId());
			course.setCourseName(newAviewClass.getCourseName());
			course.setCourseCode(newAviewClass.getCourseName());
			//Fix for Bug #13693 end
			CourseHelper.updateCourse(course, moderatorId);
		}
		List<ClassRegistration> toRemoveClassRegistrations = new ArrayList<ClassRegistration>();
		List<User> existingUsers = getMeetingRoomUsers(newAviewClass.getClassId());
		//Fix for Bug #13291 start end
		List<Long> newUsersToRegister = new ArrayList<Long>();
		if( (guestMembers!=null || meetingMembers!=null) && (hasUpdatedMeetingRoom))
		{
			hasChangeInMeetingAttendees(newAviewClass, meetingMembers, toRemoveClassRegistrations, newUsersToRegister); 
			registerUsersForMeeting(newAviewClass, newUsersToRegister, guestMembers, moderatorId);
		}
		if(isInsideMeetingRoom.equals("Y"))
		{
			//Fix for Bug # 13796 start
			prepareMeetingInvitationAndSend(onGoingMeeting.getDisplayName(), null, newAviewClass, guestMembers, existingUsers, moderatorId, false);
			//Fix for Bug # 13796 end
		}
		else if(isInsideMeetingRoom.equals("N"))
		{
			removeMeetingAttendees(toRemoveClassRegistrations);
		}
	}
	
	public static void updateMeetingSchedule(Lecture meeting,List<String>guestMembers,Long moderatorId)throws AViewException
	{
		//Session session = null;	
		Lecture tmpOldMeeting = null;
		Lecture oldMeeting=LectureHelper.getLecture(meeting.getLectureId());
		tmpOldMeeting = LectureHelper.getLecture(meeting.getLectureId());
		User moderator = ClassRegistrationHelper.getModeratorByClass(meeting.getClassId());
		Class aviewClass = null;
		List<User> existingUsers = new ArrayList<User>();
		boolean hasLectureScheduleChanged = false;
		boolean hasLectureNameChanged = false;
		hasLectureScheduleChanged = LectureHelper.didLectureScheduleChanged(oldMeeting, meeting);
		hasLectureNameChanged = oldMeeting.getDisplayName().equals(meeting.getLectureName());
		try
		{	
			//Update the lecture table only when there is a change in the lecture details
			//like name, start time (or) end time
			if(!hasLectureNameChanged || hasLectureScheduleChanged)
			{
				oldMeeting.updateFrom(meeting);
				appendDateTimeToLecture(oldMeeting);
				oldMeeting.setModifiedAuditData(moderatorId, TimestampUtils.getCurrentTimestamp());
				LectureDAO.updateLecture(oldMeeting);
			}
			/*
			List<ClassRegistration> tmpClassRegistrations = getMeetingRoomMembers(meeting.getClassId(), moderator.getUserId());
			if((tmpClassRegistrations != null) && (tmpClassRegistrations.size() > 0))
			{
				for(ClassRegistration clr : tmpClassRegistrations)
				{
					existingUsers.add(clr.getUser());
				}
			}
			*/
			existingUsers = getMeetingRoomUsers(meeting.getClassId());
			if(hasLectureScheduleChanged)
			{
				EmailHelper.notifyMembersOnMeetingScheduleChange(existingUsers, tmpOldMeeting, oldMeeting, moderator);
			}
			if((guestMembers!=null) && (guestMembers.size() > 0))
			{
				aviewClass = ClassHelper.getClass(meeting.getClassId());
				registerUsersForMeeting(aviewClass, null, guestMembers, moderatorId);
				//sendAdhocMeetingInvitationForGuest(meeting.getLectureName(), aviewClass, guestMembers, updaterId);
				//prepareAdhocMeetingInvitation(guestMembers, existingUserIds, meeting.getLectureName(), aviewClass, updaterId);
				prepareMeetingInvitationAndSend(meeting.getLectureName(), meeting, aviewClass, guestMembers, existingUsers, moderatorId, true);
			}
		
		}
		catch(HibernateException he)
		{
			processException(he);
		}
		finally 
		{
			
		}
	}
	
	public static void removeMeetingAttendees(List<ClassRegistration> toDeleteClassRegistrations) throws AViewException
	{
		ClassRegistrationHelper.deleteClassRegisters(toDeleteClassRegistrations);
	}

	private static void hasChangeInMeetingAttendees(Class aviewClass, List<Long> newMeetingMembersId, List <ClassRegistration> toRemoveClassRegistrations,
											List<Long> newUsersToRegister) throws AViewException
	{
		List<ClassRegistration> classRegistrations = ClassRegistrationHelper.getMeetingRegistrations(aviewClass.getClassId());
		List<User> newMeetingMembers = new ArrayList<User>();
		List<User> existingMembers =  new ArrayList<User>();
		//Fix for Bug #13615 start
		if((newMeetingMembersId != null) && (newMeetingMembersId.size() > 0))
		{
			newMeetingMembers.addAll(UserHelper.getUsers(newMeetingMembersId));
		}
		User tmpUser = null;
		//Check if the existing user is available in the new list
		if((newMeetingMembers != null) && (newMeetingMembers.size() > 0) && 
			(classRegistrations != null) && (classRegistrations.size() > 0))
		{
			for(ClassRegistration classRegistration : classRegistrations)
			{
				tmpUser = classRegistration.getUser();
				//To use for getting the new users added during edit
				existingMembers.add(tmpUser);
				if(!newMeetingMembers.contains(tmpUser))
				{
					toRemoveClassRegistrations.add(classRegistration);
				}
			}
		}
		if( (existingMembers != null) && (newMeetingMembers != null) && (newMeetingMembers.size() > 0))
		{
			for(User user : newMeetingMembers)
			{
				if(!existingMembers.contains(user))
				{
					newUsersToRegister.add(user.getUserId());
				}
			}
		}
	}
	public static List<Class> getMeetingRoomsForModerator(Long userId) throws AViewException
	{
		List<Class> classes=new ArrayList<Class>();
		List<ClassRegistration> registeredMeetings = ClassRegistrationHelper.searchScheduledMeetingsForModerator(userId);
		for(ClassRegistration registeredMeeting : registeredMeetings)
		{
			Class aviewClass=registeredMeeting.getAviewClass();
			classes.add(aviewClass);
		}
		return classes;
	}
	public static List<MeetingRoomVO> getMeetingsForModerator(Long userId) throws AViewException
	{
		List<MeetingRoomVO> meetingRoomVOs = new ArrayList<MeetingRoomVO>();
		List<Class> meetingRooms=getMeetingRoomsForModerator(userId);
		for(Class meetingRoom :meetingRooms)
		{
			List<Lecture> lectures=new ArrayList<Lecture>();
			lectures.addAll(LectureHelper.getLecturesForClass(meetingRoom.getClassId()));
			//add meeting members here
			List<ClassRegistration>meetingMembers=getMeetingRoomMembers(meetingRoom.getClassId(), userId);
			MeetingRoomVO meetingRoomVO = new MeetingRoomVO(lectures, meetingRoom,meetingMembers);
			meetingRoomVOs.add(meetingRoomVO);
		}
		return meetingRoomVOs;
	}
	
	public static List<ClassRegistration> getMeetingRoomMembers(Long classId,Long moderatorId) throws AViewException
	{
		List<ClassRegistration> classesRegisters=ClassRegistrationHelper.getClassRegistersForClass(classId);
		ClassRegistration tempregister=null;
		if(moderatorId != null)
		{
			for(ClassRegistration registeredMeeting : classesRegisters)
			{
				if(registeredMeeting.getUser().getUserId().equals(moderatorId))
				{
					tempregister=registeredMeeting;
				}
			}
		}
		if(tempregister!=null)
		{
			classesRegisters.remove(tempregister);
		}
		return classesRegisters;
	}
	
	//Need to refactor this code. Its just a temporary fix
	public static List<User> getMeetingRoomUsers(Long classId) throws AViewException
	{
		User moderator = ClassRegistrationHelper.getModeratorByClass(classId);
		Long moderatorId = 0l;
		if(moderator != null)
		{
			moderatorId = moderator.getUserId();
		}
		List<ClassRegistration> classesRegisters = ClassRegistrationHelper.getClassRegistersForClass(classId);
		List<User> meetingRoomUsers = new ArrayList<User>();
		if(moderatorId != null)
		{
			for(ClassRegistration registeredMeeting : classesRegisters)
			{
				if(!registeredMeeting.getUser().getUserId().equals(moderatorId))
				{
					meetingRoomUsers.add(registeredMeeting.getUser());
				}
			}
		}
		return meetingRoomUsers;
	}
	
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
	
	public static void deleteMeetings(List<Lecture> meetings) throws AViewException
	{
		User moderator = null;
		Long moderatorId = null;
		List<ClassRegistration> classRegistrations = new ArrayList<ClassRegistration>();
		List<User> usersForClassRegistration = new ArrayList<User>();
		for(Lecture meeting:meetings)
		{
			//Fix for Bug #13626 start
			usersForClassRegistration.clear();
			//Fix for Bug #13626 end
			LectureHelper.deleteLecture(meeting.getLectureId());
			moderator = ClassRegistrationHelper.getModeratorByClass(meeting.getClassId());
			if(moderator != null)
			{
				moderatorId = moderator.getUserId();
			}
			classRegistrations = getMeetingRoomMembers(meeting.getClassId(), moderatorId);
			if((classRegistrations != null) && (classRegistrations.size() > 0))
			{
				for(ClassRegistration classRegistration : classRegistrations)
				{
					usersForClassRegistration.add(classRegistration.getUser());
				}
			}
			if(usersForClassRegistration.size() > 0)
			{
				EmailHelper.notifyMembersOnMeetingCancellation(usersForClassRegistration, meeting, moderator);
			}
		}
	}
	//Fix for Bug #13291 start
	public static void deleteMeetingRoom(Long aviewClassId, Long updaterId) throws AViewException
	{
		//Firstly delete the class (meeting room) and the course
		//Delete all the meetings (lectures) for that class (meeting room)
		//Then send email to all the registered users about the cancellation of 
		//the meetings
		Class aviewClass = ClassHelper.getClass(aviewClassId);
		List<Lecture> futureMeetings = LectureHelper.getFutureLecturesForClass(aviewClassId);
		User moderator = ClassRegistrationHelper.getModeratorByClass(aviewClassId);
		List<User> meetingRoomUsers = getMeetingRoomUsers(aviewClassId);
		ClassHelper.deleteClass(aviewClass.getClassId(), updaterId);
		CourseHelper.deleteCourse(aviewClass.getCourseId(), updaterId);
		//Fix for Bug #13720 start
		if((futureMeetings != null) && (futureMeetings.size() > 0))
		{
			//Fix for Bug # 16251 start
			//LectureHelper.deleteLectures(futureMeetings);
			//Fix for Bug # 16251 end
			Set<String> uniqueMeetingName = new HashSet<String>();
			for(Lecture meeting : futureMeetings)
			{
				uniqueMeetingName.add(meeting.getDisplayName());
			}
			if((meetingRoomUsers != null) && (meetingRoomUsers.size() > 0) &&
			   (uniqueMeetingName.size() > 0))
			{
				for(String meetingName : uniqueMeetingName)
				{
					EmailHelper.notifyMembersOnMeetingRoomCancellation(meetingRoomUsers, meetingName, moderator);
				}
			}
		}
		//Fix for Bug #13720 end
	}
	//Fix for Bug #13291 end
	
	//Fix for Bug #13457 start
	public static void endMeeting(Lecture meeting, Long updaterId) throws AViewException 
	{
		Lecture oldMeeting = LectureHelper.getLecture(meeting.getLectureId());
		Date endTime = TimestampUtils.removeDateAndMillis(new Date());
		meeting.setEndTime(endTime);
		oldMeeting.updateFrom(meeting);
		oldMeeting.setModifiedAuditData(updaterId, TimestampUtils.getCurrentTimestamp());
		LectureDAO.updateLecture(oldMeeting);
	}
	//Fix for Bug #13457 end
}