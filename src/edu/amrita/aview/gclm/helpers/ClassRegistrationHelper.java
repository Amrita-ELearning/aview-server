/*
 * 
 */
package edu.amrita.aview.gclm.helpers;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.helpers.EmailHelper;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.ListUtils;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.gclm.daos.ClassRegistrationDAO;
import edu.amrita.aview.gclm.entities.Class;
import edu.amrita.aview.gclm.entities.ClassRegistration;
import edu.amrita.aview.gclm.entities.Course;
import edu.amrita.aview.gclm.entities.Institute;
import edu.amrita.aview.gclm.entities.User;
import edu.amrita.aview.gclm.entities.InstituteAdminUser;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sun.jersey.api.client.ClientResponse.Status;

@Controller
/**
 * The Class ClassRegistrationHelper.
 */
public class ClassRegistrationHelper {

	private static Logger logger = Logger.getLogger(ClassRegistrationHelper.class);
	
	// These two variables are used only for workshop registrations. They get initialized only once during the first invocation. 
	// No need to get the details every time, as the values will not change
	/** The workshop institute. */
	private static Institute workshopInstitute = null;
	
	/** The workshop course. */
	private static Course workshopCourse = null;

	/**
	 * Gets the active classes registers.
	 *
	 * @return the active classes registers
	 * @throws AViewException
	 */
	public static List<ClassRegistration> getActiveClassesRegisters() throws AViewException{
		int activeSId = StatusHelper.getActiveStatusId();
		List<ClassRegistration> classesRegisters = ClassRegistrationDAO.getActiveClassesRegister(activeSId) ;		
		return classesRegisters;
	}
	
	/**
	 * Creates the class registration.
	 *
	 * @param classRegistration the class registration
	 * @param creatorId the creator id
	 * @param statusId the status id
	 * @return the string
	 * @throws AViewException
	 */
	public static String createClassRegistration(ClassRegistration classRegistration,Long creatorId, Integer statusId) throws AViewException{	
		String errorMsg = null;
		if(classRegistration != null && classRegistration.getUser() != null && !classRegistration.getUser().getRole().equals(Constant.GUEST_ROLE))
		{
			Class cls = ClassHelper.getActiveCalssesIdMap().get(classRegistration.getAviewClass().getClassId());
			
			String regType = cls.getRegistrationType(); 
			
			//If there is a non moderator registration for any of the below classes, then make them active automatically, they do not need approval from Administrator
			if((regType.equals(Constant.NO_APPROVAL_CLASS_REGISTRATION) || 
					regType.equals(Constant.OPEN_CLASS_REGISTRATION) ||
					regType.equals(Constant.OPEN_WITH_LOGIN_CLASS_REGISTRATION)) && !classRegistration.getIsModerator().equals("Y"))
			{
				statusId = StatusHelper.getActiveStatusId();
			}
			
		classRegistration.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), statusId);
			errorMsg = ClassRegistrationDAO.createClassRegistration(classRegistration);
			//Fix for Bug #12581
			if((errorMsg == null) && (!cls.getClassType().equals(Constant.MEETING_CLASS_TYPE)))
			{
				sendEmailForStatusChange(classRegistration);
			}
		}
		else if(classRegistration == null)
		{
			errorMsg = "Invalid Registration Object";
		}
		else if(classRegistration.getUser() == null)
		{
			errorMsg = "Invalid User object in the Registration";
		}
		else if(classRegistration.getUser().getRole().equals(Constant.GUEST_ROLE))
		{
			errorMsg = "Guest Users Can't register for Classes. Please visit A-VIEW website to register to get regular User Id";
		}
		return errorMsg ;
	}

	/**
	 * Update class registration.
	 *
	 * @param classRegistration the class registration
	 * @param updaterId the updater id
	 * @throws AViewException
	 */
	public static void updateClassRegistration(ClassRegistration classRegistration,Long updaterId) throws AViewException{
		classRegistration.setModifiedAuditData(updaterId, TimestampUtils.getCurrentTimestamp());
		ClassRegistration oldClassRegistration = getClassRegister(classRegistration.getClassRegisterId());
		ClassRegistrationDAO.updateClassRegistration(classRegistration);
		if(!(oldClassRegistration.getStatusId().equals(classRegistration.getStatusId())))
		{
			//Fix for Bug#17595
			if(!oldClassRegistration.getAviewClass().getClassType().equals(Constant.MEETING_CLASS_TYPE))
			{
				sendEmailForStatusChange(classRegistration);
			}
		}
	}

	/**
	 * Delete class register.
	 *
	 * @param classRegisterId the class register id
	 * @throws AViewException
	 */
	public static void deleteClassRegister(Long classRegisterId) throws AViewException{
									
		ClassRegistrationDAO.deleteClassRegister(classRegisterId);		
	}
	
	/**
	 * Search for class register.
	 *
	 * @param userName the user name
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param classId the class id
	 * @param moderator the moderator
	 * @param courseId the course id
	 * @param instituteId the institute id
	 * @return the list
	 * @throws AViewException
	 */
	public static List<ClassRegistration> searchForClassRegister(
			String userName,String firstName,String lastName,Long classId,String moderator,
			Long courseId,Long instituteId) throws AViewException
	{		
		return searchForClassRegister(userName,firstName,lastName,classId,moderator,courseId,instituteId,null) ;
	}

	/**
	 * Gets the class registration count.
	 *
	 * @param classId the class id
	 * @return the class registration count
	 * @throws AViewException
	 */
	public static Integer getClassRegistrationCount(Long classId) throws AViewException
	{
		return ClassRegistrationDAO.getClassRegistrationCount(classId, StatusHelper.getActiveStatusId());
	}
	
	/**
	 * Gets the user class registration count.
	 *
	 * @param userId the user id
	 * @return the user class registration count
	 * @throws AViewException
	 */
	public static Integer getUserClassRegistrationCount(Long userId) throws AViewException
	{
		return ClassRegistrationDAO.getUserClassRegistrationCount(userId, StatusHelper.getActiveStatusId());
	}
	
	/**
	 * Search for class register.
	 *
	 * @param userName the user name
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param classId the class id
	 * @param moderator the moderator
	 * @param courseId the course id
	 * @param instituteId the institute id
	 * @param adminId the admin id
	 * @return the list
	 * @throws AViewException
	 */
	public static List<ClassRegistration> searchForClassRegister(
			String userName,String firstName,String lastName,Long classId,String moderator,
			Long courseId,Long instituteId,Long adminId) throws AViewException
	{		
		int activeSId = StatusHelper.getActiveStatusId();
		List<ClassRegistration> classesRegistered = 
			ClassRegistrationDAO.searchForClassRegister(userName,firstName,lastName,classId,moderator,courseId,
					instituteId,adminId,activeSId, getClassRegistrationStatusIds(), getClassStatusIds());
		if(classesRegistered.size()> 0)
		{
		populateFKNames(classesRegistered);
		}
		return classesRegistered ;
	}
	
	/**
	 * Gets the class register.
	 *
	 * @param classRegisterId the class register id
	 * @return the class register
	 * @throws AViewException
	 */
	public static ClassRegistration getClassRegister(Long classRegisterId) throws AViewException
	{		
		ClassRegistration classRegistration = null;
		try
		{
			classRegistration = ClassRegistrationDAO.getClassRegister(classRegisterId);
			populateFKNames(classRegistration);
		}
		catch(AViewException e)
		{
			logger.debug(e.getMessage());
			throw new AViewException("Given course is not valid");
		}
		return classRegistration ;
	}
	
	/**
	 * Search for class register for user.
	 *
	 * @param userId the user id
	 * @param classId the class id
	 * @param moderator the moderator
	 * @param courseId the course id
	 * @param instituteId the institute id
	 * @return the list
	 * @throws AViewException
	 */
	public static List<ClassRegistration> searchForClassRegisterForUser(
			Long userId,Long classId,String moderator,Long courseId,Long instituteId) throws AViewException
	{		
		List<ClassRegistration> classesRegistered = 
			searchForClassRegisterForUser(userId,classId,moderator,courseId,instituteId, getClassRegistrationStatusIds());
		//Not required as the above function already fills the required foreign key names
		//populateFKNames(classesRegistered);
		return classesRegistered ;
	}
	
	/**
	 * Search for class register for user.
	 *
	 * @param userId the user id
	 * @param classId the class id
	 * @param moderator the moderator
	 * @param courseId the course id
	 * @param instituteId the institute id
	 * @param clrStatusIds the clr status ids
	 * @return the list
	 * @throws AViewException
	 */
	public static List<ClassRegistration> searchForClassRegisterForUser(
			Long userId,Long classId,String moderator,Long courseId,Long instituteId, List<Integer> clrStatusIds) throws AViewException
	{		
		int activeSId = StatusHelper.getActiveStatusId();
		List<ClassRegistration> classesRegistered = 
		ClassRegistrationDAO.searchForClassRegisterForUser(userId,classId,moderator,null,courseId,instituteId,activeSId, clrStatusIds, getClassStatusIds());
		if(classesRegistered.size() > 0)
		{
			populateFKNames(classesRegistered);
		}
		return classesRegistered ;
	}
	
	/**
	 * Gets the class registers for master admin approval.
	 *
	 * @param instituteId the institute id
	 * @return the class registers for master admin approval
	 * @throws AViewException
	 */
	public static List<ClassRegistration> getClassRegistersForMasterAdminApproval(Long instituteId) throws AViewException
	{
		Integer pendingSId = StatusHelper.getPendingStatusId();
		Integer activeSId = StatusHelper.getActiveStatusId();
		//Class status can be either active or closed for registration. So get the class registrations for both the statuses 
		List<Integer> classStatusesId = getClassStatusIds();
		List<ClassRegistration> classesRegisters = ClassRegistrationDAO.getClassRegistersForMasterAdminApproval(instituteId, pendingSId, activeSId, classStatusesId);
		populateFKNames(classesRegisters);
		return classesRegisters;
	}
	
	/**
	 * Gets the class registers for institute admin approval.
	 *
	 * @param instituteId the institute id
	 * @param instAdminId the inst admin id
	 * @return the class registers for institute admin approval
	 * @throws AViewException
	 */
	public static List<ClassRegistration> getClassRegistersForInstituteAdminApproval(Long instituteId, Long instAdminId) throws AViewException
	{
		Integer pendingSId = StatusHelper.getPendingStatusId();
		Integer activeSId = StatusHelper.getActiveStatusId();
		List<Integer> classStatusesId = getClassStatusIds();
		List<ClassRegistration> classesRegisters = ClassRegistrationDAO.getClassRegistersForInstituteAdminApproval(instituteId, instAdminId,pendingSId,activeSId, classStatusesId);
		populateFKNames(classesRegisters);
		return classesRegisters;
	}
	
	/**
	 * Approve pending class registrations.
	 *
	 * @param classRegistrationIds the class registration ids
	 * @param adminUserId the admin user id
	 * @throws AViewException
	 */
	public static void approvePendingClassRegistrations(List<Long> classRegistrationIds,Long adminUserId) throws AViewException
	{
		List<List> brokenClRLists = ListUtils.breakListInto1000s(classRegistrationIds);
		
		Integer pendingStatus = StatusHelper.getPendingStatusId();
		Integer activeStatus = StatusHelper.getActiveStatusId();
		
		List<ClassRegistration> clrsToBeApproved = new ArrayList<ClassRegistration>();
		
		for(List brokenClRIds:brokenClRLists)
		{
			//Get the batch of Registrations from DB based on Id
			List<ClassRegistration> clrs = ClassRegistrationDAO.getClassRegistrations(brokenClRIds);
			
			
			for(ClassRegistration clr:clrs)
			{
				if(clr.getStatusId() != pendingStatus) //Only approve pending Registrations
				{
					continue;
				}
				clr.setStatusId(activeStatus);
				clr.setModifiedAuditData(adminUserId, TimestampUtils.getCurrentTimestamp());
				clrsToBeApproved.add(clr);
			}
		}
		ClassRegistrationDAO.updateClassRegistrations(clrsToBeApproved);
		// Send email if the approval is for aview workshop registration 
		sendEmailForWorkshopApproval(clrsToBeApproved);
	}
	
	/**
	 * Populate fk names.
	 *
	 * @param classRegistrations the class registrations
	 * @throws AViewException
	 */
	private static void populateFKNames(List<ClassRegistration> classRegistrations) throws AViewException
	{
		for(ClassRegistration clr:classRegistrations)
		{
			populateFKNames(clr);
		}
	}
	
	/**
	 * Populate fk names.
	 *
	 * @param clr the clr
	 * @throws AViewException
	 */
	private static void populateFKNames(ClassRegistration clr) throws AViewException
	{
		try {
			clr.setStatusName(StatusHelper.getStatus(clr.getStatusId())
					.getStatusName());
			UserHelper.populateFKNames(clr.getUser());
			ClassHelper.populateNames(clr.getAviewClass());
		} catch (Throwable e) {
			e.printStackTrace();
			throw new AViewException("Given course is not valid");
		}
	}
	
	/**
	 * Gets the class repository folder structure.
	 *
	 * @param userId the user id
	 * @return the class repository folder structure
	 * @throws AViewException
	 */
	public static String getClassRepositoryFolderStructure(Long userId) throws AViewException
	{
		int activeSId = StatusHelper.getActiveStatusId();	
		List<Integer> statuses = new ArrayList<Integer>();
		statuses.add(activeSId);		
		List<ClassRegistration> clsRegisters = searchForClassRegisterForUser(userId,null,null,null,null,statuses);
		ClassRepositoryFolderStructureHelper folders = new ClassRepositoryFolderStructureHelper();
		for(ClassRegistration classRegister:clsRegisters)
		{
			folders.addClassRegistraiton(classRegister);
		}		
		return folders.getFolderXML();
	}
	
	/**
	 * Gets the class registration status ids.
	 *
	 * @return the class registration status ids
	 * @throws AViewException
	 */
	private static List<Integer> getClassRegistrationStatusIds() throws AViewException
	{
		List<Integer> clrStatusIds = new ArrayList<Integer>();
		clrStatusIds.add(StatusHelper.getActiveStatusId());
		clrStatusIds.add(StatusHelper.getPendingStatusId());
		clrStatusIds.add(StatusHelper.getCommunicatingStatusId());
		clrStatusIds.add(StatusHelper.getTestingStatusId());
		clrStatusIds.add(StatusHelper.getFailedTestingStatusId());
		clrStatusIds.add(StatusHelper.getInActiveStatusId());
		return clrStatusIds;
	}
	
	/**
	 * Gets the class status ids.
	 *
	 * @return the class status ids
	 * @throws AViewException
	 */
	private static List<Integer> getClassStatusIds() throws AViewException
	{
		List<Integer> classStatusIds = new ArrayList<Integer>();
		classStatusIds.add(StatusHelper.getActiveStatusId());
		classStatusIds.add(StatusHelper.getClosedStatusId());
		return classStatusIds;
	}
	
	/**
	 * Send email for workshop approval.
	 *
	 * @param clrs the clrs
	 * @throws AViewException
	 */
	private static void sendEmailForWorkshopApproval(List<ClassRegistration> clrs) throws AViewException
	{
		if(workshopInstitute == null)
		{
			workshopInstitute = InstituteHelper.getInstituteByName(Constant.AVIEW_WORKSHOP_INSTITUTE_NAME);
		}
		if(workshopCourse == null)
		{
			List<Course> courses = CourseHelper.searchCourse(Constant.AVIEW_WORKSHOP_COURSE_NAME, null, workshopInstitute.getInstituteId());
			if (courses == null || courses.size() <= 0) 
			{
				return;
			}
			for (Course course : courses) 
			{
				if (course.getCourseName().equals(Constant.AVIEW_WORKSHOP_COURSE_NAME)) 
				{
					workshopCourse = course;
					break;
				}					
			}			
		}
		
		for(ClassRegistration clr : clrs)
		{
			// Since the class registration does not hold the details of course id, 
			// based on the class id we get its respective course id
			if(ClassHelper.getClass(clr.getAviewClass().getClassId()).getCourseId().equals(workshopCourse.getCourseId()))
			{
				//EmailHelper.sendEmailForWorkshopRegistration(clr.getUser(), clr.getAviewClass());
				EmailHelper.sendEmailForWorkshopRegistration(UserHelper.getUser(clr.getUser().getUserId()), ClassHelper.getClass(clr.getAviewClass().getClassId()));
			}
		}
	}
	
	/**
	 * Send email for status change.
	 *
	 * @param clr the clr
	 * @throws AViewException
	 */
	private static void sendEmailForStatusChange(ClassRegistration clr) throws AViewException
	{
		if(clr.getStatusId().equals(StatusHelper.getCommunicatingStatusId()))
		{
			//System.out.println("Coming inside communicating status");
			EmailHelper.sendEmailForCommunicatingStatus(clr.getUser().getEmail(), clr.getUser().getMobileNumber(),clr.getAviewClass().getClassName());
		}
		else if(clr.getStatusId().equals(StatusHelper.getInActiveStatusId()))
		{
			//System.out.println("Coming inside inactive status");
			EmailHelper.sendEmailForInActiveStatus(clr.getUser().getEmail(),clr.getAviewClass().getClassName());
		}
		//send email on activated users only for workshop registration
		else if(clr.getStatusId().equals(StatusHelper.getActiveStatusId())) 
		{
			List<ClassRegistration> clrs = new ArrayList<ClassRegistration>();
			clrs.add(clr);
			sendEmailForWorkshopApproval(clrs);
		}
	}
	
	//Code change for NIC release start
	/**
	 * Get Class Registration for Approval for the Moderator.
	 *
	 * @param instituteId the Institute Id
	 * @param classModeratorId the Moderator Id
	 * @return the list of ClassRegistration pending for approval
	 * @throws AViewException
	 */
	public static List<ClassRegistration> getClassRegistersForModeratorApproval(Long instituteId, Long classModeratorId) throws AViewException
	{
		Integer pendingSId = StatusHelper.getPendingStatusId();
		Integer activeSId = StatusHelper.getActiveStatusId();
		List<Integer> classStatusesId = getClassStatusIds();
		List<ClassRegistration> classesRegisters = ClassRegistrationDAO.getClassRegistersForModeratorApproval(instituteId, classModeratorId, pendingSId, activeSId, classStatusesId);
		populateFKNames(classesRegisters);
		return classesRegisters;
	}
	public static List<ClassRegistration> getClassRegistersForClass(Long classId) throws AViewException
	{
		Integer activeSId = StatusHelper.getActiveStatusId();
		List<ClassRegistration> classesRegisters=ClassRegistrationDAO.getClassRegistersForClass(classId, activeSId);
		populateFKNames(classesRegisters);
		return classesRegisters;
		
	}
	
	public static User getModeratorByClass(Long classId) throws AViewException
	{
		Integer activeSId = StatusHelper.getActiveStatusId();
		User moderator=ClassRegistrationDAO.getModeratorUserForClass(classId, activeSId);
		//To populate the foreign key details
		UserHelper.populateFKNames(moderator);
		return moderator;
		
	}
	//Code change for NIC release end
	
	
	/**
	 * Create class registrations for meetings
	 *
	 * @param classRegistrations the ClassRegistration
	 * @param creatorId the creator Id
	 * @param statusId the Status Id
	 * @return void
	 * @throws AViewException the a view exception
	 */
	public static void createBulkClassRegistrationsForNonModerators(List<ClassRegistration> classRegistrations,Long creatorId, Integer statusId) throws AViewException{	
		for(ClassRegistration classRegistration : classRegistrations)
		{
			classRegistration.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), statusId);
		}
		ClassRegistrationDAO.createBulkClassRegistrationsForNonModerators(classRegistrations);
	}
	
	/**
	 * Search for meeting
	 *
	 * @param userId the userId
	 * @param classId the classId
	 * @return ClassRegistration
	 * @throws AViewException the a view exception
	 */
	public static List<ClassRegistration> getMeetingRegistrations(Long classId) throws AViewException
	{		
		int activeSId = StatusHelper.getActiveStatusId();
		List<ClassRegistration> classesRegistered = 
			ClassRegistrationDAO.searchForClassRegisterForUser(null,classId,null,Constant.MEETING_CLASS_TYPE,null, null, activeSId, getClassRegistrationStatusIds(), getClassStatusIds());
		populateFKNames(classesRegistered);
		return classesRegistered ;
	}
	
	/**
	 * Search for meeting
	 *
	 * @param userId the userId
	 * @param classId the classId
	 * @return ClassRegistration
	 * @throws AViewException the a view exception
	 */
	public static List<ClassRegistration> searchScheduledMeetingsForModerator(Long userId) throws AViewException
	{		
		int activeSId = StatusHelper.getActiveStatusId();
		List<ClassRegistration> classesRegistered = 
			ClassRegistrationDAO.searchForClassRegisterForUser(userId,null,"Y",Constant.MEETING_CLASS_TYPE,null, null, activeSId, getClassRegistrationStatusIds(), getClassStatusIds());
		populateFKNames(classesRegistered);
		return classesRegistered ;
	}
	
	public static List<ClassRegistration> getAllRegisteredAndOpenClassesForUser(Long userId) throws AViewException
	{
		User user = UserHelper.getUser(userId);
		
		// To get the class registrations for user for both active and testing status
		List<Integer> classRegStatusIds = new ArrayList<Integer>();
		classRegStatusIds.add(StatusHelper.getActiveStatusId());
		classRegStatusIds.add(StatusHelper.getTestingStatusId());
		
		List<Integer> classStatusIds = new ArrayList<Integer>();
		classStatusIds.add(StatusHelper.getActiveStatusId());
		classStatusIds.add(StatusHelper.getClosedStatusId());
		
		List<ClassRegistration> registrations = null;
		List<ClassRegistration> openRegistrations = null;
		if(user.getRole().equals(Constant.MASTER_ADMIN_ROLE) || user.getRole().equals(Constant.ADMIN_ROLE))
		{
			registrations = ClassRegistrationDAO.getAllRegisteredClassesForAdmin(user,StatusHelper.getActiveStatusId(), classStatusIds, classRegStatusIds);
		}
		else
		{
			registrations = ClassRegistrationDAO.getAllRegisteredClassesForUser(user,StatusHelper.getActiveStatusId(), classStatusIds, classRegStatusIds);
		}
		openRegistrations = ClassRegistrationDAO.getAllOpenClassesForUser(user,StatusHelper.getActiveStatusId(), classStatusIds, classRegStatusIds); //Getting Open lectures
		
		openRegistrations.removeAll(registrations);
		
		registrations.addAll(openRegistrations);
		
		populateFKNames(registrations);
		
		return registrations;
		
	}

	/**
	 * Delete the for meeting members from the meeting
	 *
	 * @param classRegistrations the classRegistrations
	 * @return void
	 * @throws AViewException the a view exception
	 */
	public static void deleteClassRegisters(List<ClassRegistration> classRegistrations) throws AViewException
	{
		ClassRegistrationDAO.deleteClassRegisters(classRegistrations);
	}
	
	/**
	 * Function to validate class registration
	 * @param admin
	 * @param user
	 * @param isModerator
	 * @param classId
	 * @return object
	 * @throws AViewException
	 */
	public static Object validationofClassRegistration(User admin,User user,String isModerator,Class aviewClass) throws AViewException
	{
		ClassRegistration classRegistration = new ClassRegistration();
		if(isModerator == null || isModerator == "")
		{
			return "IsModerator should be Y,N,y,n ,others not valid.";
		}
		else
		{
			classRegistration.setIsModerator(isModerator);
			String[] ar = { "Y", "N", "y", "n" };
			String name = new String();
			for (int i = 0; i < ar.length; i++) 
			{
				if (classRegistration.getIsModerator().equals(ar[i])) 
				{
					name = ar[i];
					//classRegistration.setIsModerator(isModerator);
					break;
				}
			}
			if (!classRegistration.getIsModerator().equals(name)) 
			{
				return "IsModerator should be Y,N,y,n ,others not valid.";
			}
		}
		if(((isModerator.equals("Y"))|| (isModerator.equals("y"))) &&  user.getRole().equals(Constant.TEACHER_ROLE))
		{
			classRegistration.setIsModerator(isModerator);
			logger.debug("The given user is of teacher role.");
		}
		else if((isModerator.equals("N")) || (isModerator.equals("n")))
		{
			classRegistration.setIsModerator(isModerator);
			logger.debug("The given user is of student role.");
		}
		else
		{
			return "The given user is not having a teacher role.";
		}
		
		classRegistration.setUser(user);
		classRegistration.setAviewClass(aviewClass);
		//all the below values are hard coded
		classRegistration.setNodeTypeId(NodeTypeHelper.getClassroomNodeType());
		if(classRegistration.getEnableAudioVideo() == null || classRegistration.getEnableAudioVideo() == "")
		{
			classRegistration.setEnableAudioVideo("Y");
		}
		if(classRegistration.getEnableDocumentSharing() == null || classRegistration.getEnableDocumentSharing() == "")
		{
			classRegistration.setEnableDocumentSharing("Y");
		}
		if(classRegistration.getEnableDesktopSharing() == null ||classRegistration.getEnableDesktopSharing() == "")
		{
			classRegistration.setEnableDesktopSharing("Y");
		}
		if(classRegistration.getEnableVideoSharing() == null || classRegistration.getEnableVideoSharing() == "")
		{
			classRegistration.setEnableVideoSharing("Y");
		}
		if(classRegistration.getEnable2DSharing() == null ||classRegistration.getEnable2DSharing() == "")
		{
			classRegistration.setEnable2DSharing("Y");
		}
		if(classRegistration.getEnable3DSharing() == null || classRegistration.getEnable3DSharing() == "")
		{
			classRegistration.setEnable3DSharing("Y");
		}
		return classRegistration;
	}
	
	/**
	 * API to create class registration
	 * @param userId
	 * @param classId
	 * @param isModerator
	 * @param adminId
	 * @return Response
	 * @throws AViewException
	 **/
	@RequestMapping(value ="/createclassregister.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response createClassRegistration(@RequestParam("userId") Long userId,
			@RequestParam("classId") Long classId,
			@RequestParam("isModerator") String isModerator, 
			@RequestParam("adminId") Long adminId,
			@RequestParam("sendEmail") String sendEmail) throws AViewException {
		String result = new String();
		Class aviewClass = null;
		//int statusId = StatusHelper.getActiveStatusId();
		logger.debug("Enter class registration::class register ");
		String errorMessage = new String();
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
		//Get user details if userID is valid
		User user = null;
		Object resultObjectUser = UserHelper.userValidCheck(Constant.USER_ID,userId);
		if(User.class.isInstance(resultObjectUser))
		{
			user = (User)resultObjectUser;
		}
		else
		{
			errorMessage = resultObjectUser.toString();
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		
		ClassRegistration classRegistration = null;
		try
		{
			Object resultObject = ClassHelper.classValidCheck(classId);
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
			Object validationMessageForAdmin = ClassHelper.adminValidation(aviewClass, admin);
			if(validationMessageForAdmin != null)
			{
				return Response.status(Status.BAD_REQUEST).entity(validationMessageForAdmin.toString()).build();
			}
			Object resultObjectClassRegistration = validationofClassRegistration(admin, user, isModerator, aviewClass);
			if(ClassRegistration.class.isInstance(resultObjectClassRegistration))
			{
				classRegistration = (ClassRegistration)resultObjectClassRegistration;
			}
			else
			{
				errorMessage = resultObjectClassRegistration.toString();
				return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
			}
			Long resultId = 0l;
			if(validationMessageForAdmin == null && (admin.getRole().equals(Constant.MASTER_ADMIN_ROLE) || (admin.getRole().equals(Constant.ADMIN_ROLE))))
			{				
				ClassRegistrationHelper.createClassRegistration(classRegistration, adminId, StatusHelper.getActiveStatusId());
				if(sendEmail.equals("Y") || sendEmail.equals("y"))
				{
					String instituteName = null;
					if(classRegistration.getAviewClass() != null)
					{
						instituteName = classRegistration.getAviewClass().getInstituteName();
					}
					EmailHelper.sendEmailForActiveClassRegistration(classRegistration.getUser().getEmail(), classRegistration.getAviewClass().getClassName(), instituteName, null, false);
				}
				resultId =classRegistration.getClassRegisterId();
				logger.debug("Exit class registration on success::class register.");
				return Response.status(Status.OK).entity(resultId).build();
			}
			else if(admin.getRole().equals(Constant.STUDENT_ROLE) ||  admin.getRole().equals(Constant.TEACHER_ROLE))
 			{
				ClassRegistrationHelper.createClassRegistration(classRegistration, adminId, StatusHelper.getPendingStatusId());
				resultId =classRegistration.getClassRegisterId();
				logger.debug("Exit class registration on success::class register.");
				return Response.status(Status.OK).entity(resultId).build();
			}
		}
		catch (NumberFormatException nfe)
		{
			result = "invalid";
			logger.debug("Exit class registration on invalid request::class register.");
			return Response.status(Status.BAD_REQUEST).entity(result).build();    
		}
		catch(AViewException ae)
		{
			if(ae.getMessage().equals("Cannot assign more than one moderator for a class"))
			{
				result = "Already a moderator has been assigned for this class. A class cannot have more than one moderator.";
			}
			else if(ae.getMessage().equals("Duplicate entry '"+classId+"-"+userId+"' for key 'class_user'"))
			{
				result = "User is already registered for this class.";
			}
			else if(ae.getMessage().equals("Given course is not valid"))
			{
				result = "Entered class id is not valid or doesn't exist.";
			}
			else 
			{
				result = "error during log.Possible reason(s) : 1. Unexpected data, 2. Unknown.";

			}
			logger.debug("Exit class registration on error durning log::class register."); 
			return Response.status(Status.BAD_REQUEST).entity(result).build();    
		}
		return null;
	}

	/**
	 * API to search classRegistration
	 * @param adminId
	 * @param classId
	 * @param userId
	 * @return Response
	 * @throws AViewException
	 */
	@RequestMapping(value = "/searchclassregister.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response searchClassRegister(@RequestParam("adminId") Long adminId,
			@RequestParam("classId") Long classId,
			@RequestParam("userId")  Long userId) throws AViewException
	{
		logger.debug("Enter class registration search::class registration search");
		String errorMessage = null;
		Class aviewClass = null;
		User user = null;
		ArrayList classRegisterDetailsArray = new ArrayList();
		User admin = null;
		String result= null;
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
			if(userId == null && classId == null)
			{
				return Response.status(Status.BAD_REQUEST).entity("Please enter any one of the search criteria").build();
			}
			else
			{
				if(classId != null)
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
				}
				if(userId != null )
				{
					Object resultObjectUser = UserHelper.userValidCheck(Constant.USER_ID,userId);
					if(User.class.isInstance(resultObjectUser))
					{
						user = (User)resultObjectUser;
					}
					else
					{
						errorMessage = resultObjectUser.toString();
						return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
					}
				}
			}
			
			List<ClassRegistration> classRegisterArrayList = null;
			if(userId == null && classId == null)
			{
				return Response.status(Status.BAD_REQUEST).entity("Please enter any one of the search criteria").build();
			}
			String userName =  (user == null) ? null : user.getUserName();
			if(admin.getRole().equals(Constant.MASTER_ADMIN_ROLE))
			{
				adminId = null;
				classRegisterArrayList = searchForClassRegister(userName, null, null, classId, null, null, null, adminId);
			}
			else
			{
				classRegisterArrayList = searchForClassRegister(userName, null, null, classId, null, null, null, adminId);
			}
			if(classRegisterArrayList == null || classRegisterArrayList.size() == 0) 
			{
				return Response.status(Status.BAD_REQUEST).entity("No class registration for the given search criteria").build();
			} 
			else
			{
				ArrayList  classRegisterArray = new ArrayList();
				for(ClassRegistration classRegisterationDetails:classRegisterArrayList)
				{
					classRegisterArray = new ArrayList();
					classRegisterArray.add("classRegistrationId: " + classRegisterationDetails.getClassRegisterId());
					classRegisterArray.add("userName: " + classRegisterationDetails.getUser().getUserName());
					classRegisterArray.add("userId: " + classRegisterationDetails.getUser().getUserId());
					classRegisterArray.add("classId: " + classRegisterationDetails.getAviewClass().getClassId());
					classRegisterArray.add("class: " + classRegisterationDetails.getAviewClass().getClassName());
					classRegisterArray.add("isModerator: " + classRegisterationDetails.getIsModerator());
					classRegisterDetailsArray.add(classRegisterArray);
				}
			}
			logger.debug("Exit class registration on search success:class search");
			return Response.status(Status.OK).entity(classRegisterDetailsArray).build();
		}
		catch (NumberFormatException nfe) 
		{
			result = "invalid";
			logger.debug("Exit class registration search on invalid request::class register.");
			return Response.status(Status.BAD_REQUEST).entity(result).build();    
		}
		catch(AViewException ae)
		{
			if(ae.getMessage().equals("Given course is not valid"))
			{
				result = "Given search criteria provided is not valid or doesn't exist";
			}
			logger.debug("Exit class registration search on error durning log::class register."); 
			return Response.status(Status.BAD_REQUEST).entity(result).build();    
		}
	}
	
	/**
	 * API to delete classRegistration
	 * @param adminId
	 * @param classRegisterId
	 * @return Response
	 * @throws AViewException
	 */
	@RequestMapping(value = "/deleteclassregister.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response deleteClassRegister(@RequestParam("adminId") Long adminId,
			@RequestParam("classRegisterId") Long classRegisterId) throws AViewException 
	{
		logger.debug("Enter class register delete::class register delete ");
		String result = null;
		String errorMessage = null;
		boolean isParentAdmin = false;
		InstituteAdminUser instAdminUser = null;
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
		if(classRegisterId == null)
		{
			classRegisterId = 0l;
		}
		try
		{
			ClassRegistration classRegistration = getClassRegister(classRegisterId);
			if(classRegistration != null)
			{
				Course course = CourseHelper.getCourse(classRegistration.getAviewClass().getCourseId());
				Institute instituteDetails = InstituteHelper.getInstituteById(course.getInstituteId());
				if(instituteDetails != null && instituteDetails.getStatusId() == StatusHelper.getActiveStatusId())
				{
					List<Institute> institutes = InstituteHelper.getAllInstitutesForAdmin(adminId);
					if(institutes != null || institutes.size() != 0)
					{
						 isParentAdmin = institutes.contains(instituteDetails);
					}
				}
				else
				{
					logger.debug("Given institute id doesn't exist");
				}
				List<InstituteAdminUser> instituteAdminUser = InstituteAdminUserHelper.getInstituteAdminUsers();
				for (int j = 0; j < instituteAdminUser.size(); j++)
				{
					instAdminUser = (InstituteAdminUser)instituteAdminUser.get(j) ;
					if(adminId.equals(instAdminUser.getUser().getUserId()))
					{
						break;
					}
				}
				if (admin.getRole().equals(Constant.MASTER_ADMIN_ROLE) || (isParentAdmin == true && admin.getRole().equals(Constant.ADMIN_ROLE)) ) 
				{
					deleteClassRegister(classRegisterId);
					logger.debug("Exit class registration on delete success:class register delete");
					return Response.status(Status.OK).entity("Deleted class enrollment(ID: " +classRegisterId + ") successfully").build();
				} 
				else 
				{
					return Response.status(Status.BAD_REQUEST).entity("Master admin or parent institute admin has the permission to delete the ClassRegister").build();
				}
			}
			else
			{
				return Response.status(Status.BAD_REQUEST).entity("Class enrollment Id doesn't exist").build();
			}	
		}
		catch (NumberFormatException nfe) 
		{
			result = "invalid";
			logger.debug("Exit class registration delete on invalid request::class register.");
			return Response.status(Status.BAD_REQUEST).entity(result).build();    
		}
		catch(AViewException ae)
		{
			if(ae.getMessage().equals("Given course is not valid"))
			{
				result = "Please enter a valid classRegisterId.";
			}
			logger.debug("Exit class registration delete on error durning log::class register."); 
			return Response.status(Status.BAD_REQUEST).entity(result).build();    
		}
	}
	
	
	@RequestMapping(value = "/updateclassregister.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response updateClassRegister(@RequestParam("adminId") Long adminId,
			@RequestParam("classRegisterId") Long classRegisterId,
			@RequestParam("isModerator") String isModerator) throws AViewException 
	{
		logger.debug("Enter class register update ::class register update ");
		String errorMessage = null;
		String result = null;
		boolean isAdmin = false;
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
		if(classRegisterId == null)
		{
			classRegisterId = 0l;
		}
		try 
		{
			ClassRegistration classRegistration = getClassRegister(classRegisterId);
			if(classRegistration != null)
			{
				if(isModerator == null || isModerator == "")
				{
					return Response.status(Status.BAD_REQUEST).entity("IsModerator should be Y,N,y,n ,others not valid.").build();
				}
				else
				{
					classRegistration.setIsModerator(isModerator);
					String[] ar = { "Y", "N", "y", "n" };
					String name = new String();
					for (int i = 0; i < ar.length; i++) 
					{
						if (classRegistration.getIsModerator().equals(ar[i])) 
						{
							name = ar[i];
							//classRegistration.setIsModerator(isModerator);
							break;
						}
					}
					if (!classRegistration.getIsModerator().equals(name)) 
					{
						return Response	.status(Status.BAD_REQUEST)
								.entity("IsModerator should be Y,N,y,n ,others not valid.")
								.build();
					}
				}
				if(((isModerator.equals("Y"))|| (isModerator.equals("y"))) &&  classRegistration.getUser().getRole().equals(Constant.TEACHER_ROLE))
				{
					classRegistration.setIsModerator(isModerator);
					logger.debug("The given user is of teacher role.");
				}
				else if((isModerator.equals("N")) || (isModerator.equals("n")))
				{
					classRegistration.setIsModerator(isModerator);
					logger.debug("The given user is of student role.");
				}
				else
				{
					errorMessage ="The given user is not having a teacher role.";
					return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
				}
			}
			else
			{
				return Response	.status(Status.BAD_REQUEST)
						.entity("class registration id is not valid or doesn't exist")
						.build();
			}
			Institute instituteDetails = InstituteHelper.getInstituteById(classRegistration.getUser().getInstituteId());
			if(instituteDetails != null && instituteDetails.getStatusId() == StatusHelper.getActiveStatusId())
			{
				List<Institute> institutes = InstituteHelper.getAllInstitutesForAdmin(adminId);
				if(institutes != null)
				{
					 isAdmin = institutes.contains(instituteDetails);
				}
			}
			else
			{
				logger.debug("Given institute id doesn't exist");
			}
			Class aviewClass = ClassHelper.getClass(classRegistration.getAviewClass().getClassId());
			String validationErrorMessageForLectureAdmin = null;
			if(aviewClass != null )
			{
				Object validationMessageForLectureAdmin = ClassHelper.adminValidation(aviewClass, admin);
				if(validationMessageForLectureAdmin != null)
				{
					return Response.status(Status.BAD_REQUEST).entity(validationErrorMessageForLectureAdmin.toString()).build();
				}
			}
			if(admin.getRole().equals(Constant.TEACHER_ROLE) && admin.getUserId().equals(classRegistration.getUser().getUserId()))
 			{
				Long resultId = 0l;
				ClassRegistrationHelper.updateClassRegistration(classRegistration, adminId);
				resultId = classRegistration.getClassRegisterId();
				logger.debug("Exit class registration on success::class register.");
				return Response.status(Status.OK).entity("Updated class enrollment (ID: "+resultId+") successfully").build();
			}
			else if(validationErrorMessageForLectureAdmin == null || admin.getRole().equals(Constant.MASTER_ADMIN_ROLE) || (isAdmin == true && admin.getRole().equals(Constant.ADMIN_ROLE)))
			{
				Long resultId = 0l;
				classRegistration.setStatusId(StatusHelper.getActiveStatusId());
				ClassRegistrationHelper.updateClassRegistration(classRegistration, adminId);
				resultId = classRegistration.getClassRegisterId();
				logger.debug("Exit class registration on success::class register.");
				return Response.status(Status.OK).entity("Updated class enrollment (ID: "+resultId+") successfully").build();
			}
			else
			{
				return Response.status(Status.OK).entity("Admin Id is not authorized to perform this operation").build();
			}
			/*Long resultId = 0l;
			ClassRegistrationHelper.updateClassRegistration(classRegistration, adminId);
			resultId = classRegistration.getClassRegisterId();
			logger.debug("Exit class registration on success::class register.");
			return Response.status(Status.OK).entity("Updated class enrollment (ID: "+resultId+") successfully").build();*/
		}
		catch (NumberFormatException nfe) 
		{
			result = "invalid";
			logger.debug("Exit class registration on invalid request::class register.");
			return Response.status(Status.BAD_REQUEST).entity(result).build();    
		}
		catch(AViewException ae)
		{
			if(ae.getMessage().equals("Cannot assign more than one moderator for a class"))
			{
				result = "Already a moderator has been assigned for this class. A class cannot have more than one moderator.";
			}	
			else if(ae.getMessage().equals("Given course is not valid"))
			{
				result = "Please enter a valid classRegisterId.";
			}
			else 
			{
				result = "error during log.Possible reason(s) : 1. Unexpected data, 2. Unknown.";

			}
			logger.debug("Exit class registration on error durning log::class register."); 
			return Response.status(Status.BAD_REQUEST).entity(result).build();    
		}
		//return null;
	}
	
	/**
	 * Get classregistration based on userid and class id
	 * 
	 * @param classId
	 * @param userId
	 * @return list
	 * @throws AViewException
	 */
/*	public static List<ClassRegistration> getClassRegisterationForUser(Long classId,Long userId) throws AViewException	{
		List<ClassRegistration> classesRegister= null;
		Integer activeSId = StatusHelper.getActiveStatusId();
		classesRegister=ClassRegistrationDAO.searchForClassRegisterForUser(userId, classId, null, null, 0l, 0l, activeSId, null, null);
		if(!(classesRegister.isEmpty()))
		{
			populateFKNames(classesRegister);
		}
		return classesRegister;
	}*/
}
