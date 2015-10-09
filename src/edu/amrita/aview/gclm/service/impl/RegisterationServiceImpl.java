/*
 * 
 */
package edu.amrita.aview.gclm.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import edu.amrita.aview.audit.helpers.AuditUserLoginHelper;
import edu.amrita.aview.audit.helpers.UserActionHelper;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.gclm.entities.ClassRegistration;
import edu.amrita.aview.gclm.entities.Course;
import edu.amrita.aview.gclm.entities.Institute;
import edu.amrita.aview.gclm.entities.Lecture;
import edu.amrita.aview.gclm.entities.User;
import edu.amrita.aview.gclm.helpers.ClassHelper;
import edu.amrita.aview.gclm.helpers.ClassRegistrationHelper;
import edu.amrita.aview.gclm.helpers.CourseHelper;
import edu.amrita.aview.gclm.helpers.InstituteHelper;
import edu.amrita.aview.gclm.helpers.LectureHelper;
import edu.amrita.aview.gclm.helpers.NodeTypeHelper;
import edu.amrita.aview.gclm.helpers.UserHelper;
import edu.amrita.aview.gclm.service.RegistrationService;


/**
 * The Class RegisterationServiceImpl.
 *
 * @author Krishnakumar.R
 */
@Service("registrationService")
public class RegisterationServiceImpl implements RegistrationService {

	/** The logger. */
	static Logger logger = Logger.getLogger(RegisterationServiceImpl.class);
	
	/** The course name. */
	final String COURSE_NAME = "National AVIEW Workshop";
	
	/** The institite name. */
	final String INSTITITE_NAME = "Amrita E-Learning Research Lab";

	/* (non-Javadoc)
	 * @see edu.amrita.aview.gclm.service.RegistrationService#registerClass(java.lang.Long, java.lang.Long)
	 */
	@Override
	public String registerClass(Long userId, Long classId) {
		String statusMessage = new String();
		try {
			User user = UserHelper.getUser(userId);
			edu.amrita.aview.gclm.entities.Class workShop = ClassHelper
					.getClass(classId);
			if (user == null || workShop == null) 
			{
				// return from this function at this point
				statusMessage = "invalid";
			}
			else
			{
				List<ClassRegistration> classRegistrationList = ClassRegistrationHelper
						.searchForClassRegisterForUser(userId, classId, null, null,
								null);
				if (!classRegistrationList.isEmpty())				
				{
					// return from this function at this point
					statusMessage = "exist";
				}
				else
				{
					ClassRegistration clr = new ClassRegistration();
					clr.setIsModerator("N");
					clr.setAviewClass(workShop);
					clr.setUser(user);
					clr.setEnable2DSharing("Y");
					clr.setEnable3DSharing("Y");
					clr.setEnableAudioVideo("Y");
					clr.setEnableDesktopSharing("Y");
					clr.setEnableDocumentSharing("Y");
					clr.setEnableVideoSharing("Y");
					clr.setNodeTypeId(NodeTypeHelper.getClassroomNodeType());
					ClassRegistrationHelper.createClassRegistration(clr,
							user.getUserId(), StatusHelper.getActiveStatusId());
					if (clr.getClassRegisterId() != 0) 
					{
						statusMessage = clr.getClassRegisterId().toString();
						//EmailHelper.sendEmailForWorkshopRegistration(user, workShop);
					} 
					else 
					{
						statusMessage = "failed";
					}
				}
			}
		}
		catch (AViewException aviewException)
		{
			logger.error(aviewException.getMessage());
			statusMessage = "failed";
		}
		return statusMessage;
	}

	/* (non-Javadoc)
	 * @see edu.amrita.aview.gclm.service.RegistrationService#listWorkshops(boolean)
	 */
	@Override
	public LinkedHashMap<edu.amrita.aview.gclm.entities.Class, Integer> listWorkshops(boolean ascending) {
		LinkedHashMap<edu.amrita.aview.gclm.entities.Class, Integer> aviewClassesWithClassRegCount = new LinkedHashMap<edu.amrita.aview.gclm.entities.Class, Integer>();
		List<edu.amrita.aview.gclm.entities.Class> aviewClasses = null;
		int userAttendedCount = 0;
		try {
			Institute institute = InstituteHelper
					.getInstituteByName(INSTITITE_NAME);
			Course course = null;
			if (institute == null) {
				logger.debug("No Institute is found.");
				return null;
			}
			List<Course> courses = CourseHelper.searchCourse(COURSE_NAME, null,
					institute.getInstituteId());
			if (courses == null || courses.size() <= 0) {
				logger.debug("No courses found.");
				return null;
			}
			for (Course tempCourse : courses) {
				if (tempCourse.getCourseName().equals(COURSE_NAME)) {
					course = tempCourse;
					break;
				}
			}
			List<Integer> statuses = new ArrayList<Integer>();
			statuses.add(StatusHelper.getActiveStatusId());
			statuses.add(StatusHelper.getClosedStatusId());
			aviewClasses = ClassHelper.searchClass(institute.getInstituteId(),
					course.getCourseId(), null, null, statuses);
			if (aviewClasses != null && aviewClasses.size() > 0) {
				if(ascending)
				{
					Collections.sort(aviewClasses,
							new Comparator<edu.amrita.aview.gclm.entities.Class>() {
								public int compare(
										edu.amrita.aview.gclm.entities.Class first,
										edu.amrita.aview.gclm.entities.Class second) {
									return first.getEndDate().compareTo(
											second.getEndDate());
								}
							});
				}
				else
				{
					Collections.sort(aviewClasses,
							new Comparator<edu.amrita.aview.gclm.entities.Class>() {
								public int compare(
										edu.amrita.aview.gclm.entities.Class first,
										edu.amrita.aview.gclm.entities.Class second) {
									return second.getEndDate().compareTo(
											first.getEndDate());
								}
							});
				}
			}
			
			for(edu.amrita.aview.gclm.entities.Class workshopClass : aviewClasses)
			{
				userAttendedCount = 0;
				//classRegistrationCount = ClassRegistrationHelper.getClassRegistrationCount(workshopClass.getClassId());
				List<Lecture> lectures = LectureHelper.getLecturesForClass(workshopClass.getClassId());
				// Get the lecture details in descending order so that the end date is the actual workshop date.
				if (lectures != null && lectures.size() > 0) {
					Collections.sort(lectures,
							new Comparator<edu.amrita.aview.gclm.entities.Lecture>() {
								public int compare(
										edu.amrita.aview.gclm.entities.Lecture first,
										edu.amrita.aview.gclm.entities.Lecture second) {
									return second.getStartDate().compareTo(
											first.getStartDate());
								}
							});
				}
				if (lectures != null && lectures.size() > 0)
				{
					userAttendedCount = AuditUserLoginHelper.getUserLoginCountForLecture(lectures.get(0).getLectureId());
				}
				aviewClassesWithClassRegCount.put(workshopClass, userAttendedCount);				
			}
				
		}
		catch (AViewException aviewException) 
		{
			logger.error(aviewException.getMessage());
			return null;
		}		
		return aviewClassesWithClassRegCount;

	}
	
	/* (non-Javadoc)
	 * @see edu.amrita.aview.gclm.service.RegistrationService#userLogin(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public User userLogin(String userName, String password, String ipAddress, String additionalInfo) throws AViewException
	{
		User user = null;
		try 
		{
			user = UserHelper.getUserByUserNamePassword(userName, password);
		}
		catch (AViewException aviewException) 
		{
			if(aviewException.getMessage().contains("Please check your user name/password"))
			{
				User existingUser = UserHelper.getUserByUserName(userName);
				try
				{
					if(existingUser == null)
					{
						UserActionHelper.createUserActionForFailedLoginAttemptWrongUserName(userName, ipAddress, "aview.in", additionalInfo);
					}
					else
					{
						UserActionHelper.createUserActionForFailedLoginAttempt(ipAddress, "aview.in", additionalInfo, existingUser.getUserId());					
					}
				}
				catch(Exception e)
				{
					logger.debug("Some other issue while writing to audit table: " + e.getMessage());
				}
			}
		}
		catch(Exception e)
		{
			logger.debug("Some other issue: " + e.getMessage());
		}
		return user;
	}
}
