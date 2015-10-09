/*
 * 
 */
package edu.amrita.aview.gclm.helpers;
import java.util.ArrayList;
import java.util.List;

import com.sun.jersey.api.client.ClientResponse.Status;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.amrita.aview.audit.helpers.UserActionHelper;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.District;
import edu.amrita.aview.common.entities.State;
import edu.amrita.aview.common.helpers.DistrictHelper;
import edu.amrita.aview.common.helpers.EmailHelper;
import edu.amrita.aview.common.helpers.StateHelper;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.AppenderUtils;
import edu.amrita.aview.common.utils.HashCodeUtils;
import edu.amrita.aview.common.utils.ListUtils;
import edu.amrita.aview.common.utils.RandomUtils;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.common.utils.JSONParserUtils;
import edu.amrita.aview.common.utils.ValidationUtils;
import edu.amrita.aview.gclm.daos.UserDAO;
import edu.amrita.aview.gclm.entities.Class;
import edu.amrita.aview.gclm.entities.ClassRegistration;
import edu.amrita.aview.gclm.entities.Institute;
import edu.amrita.aview.gclm.entities.User;
import edu.amrita.aview.gclm.entities.InstituteAdminUser;
import edu.amrita.aview.licensing.LicenseValidator;


/**
 * The Class UserHelper.
 */
@Controller
public class UserHelper {

	/** The logger. */
	private static Logger logger = Logger.getLogger(UserHelper.class);
	
	private static String[] userRole = { Constant.ADMIN_ROLE, Constant.GUEST_ROLE, Constant.MASTER_ADMIN_ROLE,Constant.STUDENT_ROLE,Constant.TEACHER_ROLE,Constant.MONITOR_ROLE};
	
	/**
	 * Creates the user.
	 *
	 * @param user the user
	 * @param creatorId the creator id
	 * @param statusId the status id
	 * @throws AViewException
	 */
	public static void createUser(User user,Long creatorId,Integer statusId) throws AViewException{
		// Code added to use the master admin id, if the user registers through aview website
		User adminUser = null;
		logger.info("User creation " + user.getUserName() + " by creator: " + creatorId + " with status " + statusId );
		//Fix for Bug #13729 start 
		if(creatorId.equals(new Long(0)))
		//Fix for Bug #13729 end
		{
			statusId = StatusHelper.getPendingStatusId();
			//adminUser = getUsersByRole(Constant.MASTER_ADMIN_ROLE);
			adminUser = getUserByUserName(Constant.MASTER_ADMIN_USER_NAME);
			creatorId = adminUser.getUserId();
			//Fix for Bug #13729 start
			user.setCreatedFrom(Constant.CREATED_FROM_WEB) ;
			//Fix for Bug #13729 end
		}
		user.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), statusId);
		UserDAO.createUser(user);
		if(adminUser != null)
		{
			EmailHelper.sendEmailForNewUserRegistration(user.getEmail());
			EmailHelper.sendEmailToAdminForNewUserRegistration(user.getUserName(), user.getFname(), user.getLname(), user.getEmail());
		}
	}
	
	/**
	 * Gets the guest user for the class.
	 *
	 * @param lecture_id the lecture_id
	 * @return the guest user for the class
	 * @throws AViewException
	 */
	public static User getGuestUserForTheClass(long lecture_id) throws AViewException
	{
//		logger.info("getGuestUserForTheClass Helper: ");
		return GuestUserHelper.getGuestUserForTheClass(lecture_id);
	}
	
	/**
	 * Creates the user.
	 *
	 * @param user the user
	 * @param creatorId the creator id
	 * @param classId the class id
	 * @param statusId the status id
	 * @throws AViewException
	*/
	public static void createUser(User user,Long creatorId,Long classId,Integer statusId) throws AViewException
	{
		logger.info("User created for workshop registration: " + user.getUserName() + " for workshop class id " + classId);
		createUser(user, creatorId, statusId);
		//Registration for the workshop class
		Class aviewClass = ClassHelper.getClass(classId);
		ClassRegistration workshopClassReg = new ClassRegistration();
		workshopClassReg.setAviewClass(aviewClass);
		workshopClassReg.setUser(user);
		workshopClassReg.setIsModerator("N");
		workshopClassReg.setEnable2DSharing("Y");
		workshopClassReg.setEnable3DSharing("Y");
		workshopClassReg.setEnableAudioVideo("Y");
		workshopClassReg.setEnableDesktopSharing("Y");
		workshopClassReg.setEnableDocumentSharing("Y");
		workshopClassReg.setEnableVideoSharing("Y");
		workshopClassReg.setNodeTypeId(NodeTypeHelper.getPersonalComputerNodeType());
		ClassRegistrationHelper.createClassRegistration(workshopClassReg, user.getUserId(), StatusHelper.getPendingStatusId());
	} 

	public static String checkIfModerator(User user) throws AViewException
	{
		String result = null;
		List<Class> classlst = null;	
			classlst = ClassHelper.getClassByModerator(user.getUserId());
		if(classlst.size() > 0)
		{
			result = "User is a moderator for the following class(es): ";
			String classNames = "";
			Class c = null;
			int j = 0;
			for(j = 0; j < classlst.size() - 1; j++)
			{
				c = classlst.get(j);
				classNames += c.getClassName() + ", ";
			}
			c = classlst.get(j);
			if(classNames != "")
			{
				classNames += "and ";
			}
			classNames += c.getClassName();
			result += classNames;	
			result += ". Please assign a different moderator to make this user as a student.";
		}
		return result;
	}
	
	public static void updateUser(User user,Long updaterId) throws AViewException {	
		//Fix for Bug id 2450 start
		//Check if this user is already a teacher with moderator privileges for any class.
		//If so, do not update the role to student.
		boolean canUpdate = true;
		//List<Class> classlst = null;		
		String errorMessage = null;
		if(user.getRole().equals(Constant.STUDENT_ROLE))
		{
			errorMessage = checkIfModerator(user);
		}
		
		user.setModifiedAuditData(updaterId, TimestampUtils.getCurrentTimestamp());
		
		if(errorMessage == null)
		{
			UserDAO.updateUser(user);
		}
		else
		{
			logger.debug(errorMessage+" :: error ::");
			throw (new HibernateException(errorMessage));			
		}
		//Fix for Bug id 2450 end
	}

	/**
	 * Update user change pass.
	 *
	 * @param newPass the new pass
	 * @param userId the user id
	 * @param updatingUserId the updating user id
	 * @throws AViewException
	 */
	public static void updateUserChangePass(String newPass,Long userId,Long updatingUserId) throws AViewException
	{
		User user = UserDAO.getUser(userId);
		if(user != null)
		{
			user.setPassword(newPass);
			user.setModifiedAuditData(updatingUserId,TimestampUtils.getCurrentTimestamp());
			UserDAO.updateUser(user);
		}
		else
		{
			throw new AViewException("User with id :"+userId+": is not found");
		}
	}
	
	/**
	 * Gets the active users.
	 *
	 * @return the active users
	 * @throws AViewException
	 */
	public static List<User> getActiveUsers() throws AViewException {
		int activeSId = StatusHelper.getActiveStatusId();
		List<User> users= UserDAO.getActiveUsers(activeSId);
		populateFKNames(users);
		return users;
	}

	/**
	 * Sets the user to deleted.
	 *
	 * @param aviewUser the aview user
	 * @param deletedSId the deleted s id
	 * @param modifiedByUserId the modified by user id
	 * @throws AViewException
	 */
	private static void setUserToDeleted(User aviewUser,int deletedSId,Long modifiedByUserId) throws AViewException
	{
		aviewUser.setUserName(aviewUser.getUserName() + AppenderUtils.DeleteAppender());
		aviewUser.setStatusId(deletedSId);
		aviewUser.setModifiedAuditData(modifiedByUserId, TimestampUtils.getCurrentTimestamp());
	}
	
	/**
	 * Delete user.
	 *
	 * @param userId the user id
	 * @param modifiedByUserId the modified by user id
	 * @throws AViewException
	 */
	public static void deleteUser(Long userId, Long modifiedByUserId) throws AViewException{
		int deletedSId = StatusHelper.getDeletedStatusId();
		User aviewUser = UserDAO.getUser(userId);
			
		if(aviewUser != null)
		{
			//bug fix for changing the user name for the deleted user, so that the new user can have 
			//the same name
			setUserToDeleted(aviewUser,deletedSId,modifiedByUserId);
			UserDAO.updateUser(aviewUser);
		}
		else
		{
			throw new AViewException("User with id :"+userId+": is not found");
		}
	}
	
	/**
	 * Gets the user.
	 *
	 * @param userId the user id
	 * @return the user
	 * @throws AViewException
	 */
	public static User getUser(Long userId) throws AViewException {
		User user = UserDAO.getUser(userId) ;
		populateFKNames(user);
		return user ;
	}

	/**
	 * Gets the users.
	 *
	 * @param userIds the user ids
	 * @return the users
	 * @throws AViewException
	 */
	public static List<User> getUsers(List<Long> userIds) throws AViewException {
		List<User> users = UserDAO.getUsers(userIds);
		populateFKNames(users);
		return users;
	}
	
	/**
	 * Gets the user by user name.
	 *
	 * @param userName the user name
	 * @return the user by user name
	 * @throws AViewException
	 */
	public static User getUserByUserName(String userName) throws AViewException {
		User user = UserDAO.getUserByUserName(userName,StatusHelper.getActiveStatusId()) ;
		populateFKNames(user);
		return user ;
	}

	/**
	 * Gets the user by user name password.
	 *
	 * @param userName the user name
	 * @param password the password
	 * @return the user by user name password
	 * @throws AViewException
	 */
	public static User getUserByUserNamePassword(String userName,String password) throws AViewException 
	{
		User user = null;
		if(LicenseValidator.validationStatus)
		{
			user = getUserByUserName(userName);
			//Bug fix : Bug #605:Login Password for A-View Class Room is not case sensitive.
			if(user != null && user.getPassword().equals(password))
			{
				//do nothing
			}
			else if(user != null && user.getRole().equals(Constant.GUEST_ROLE))
			{
				logger.info("Login not allowed for guest users: " + user.getUserName());
				String exceptionMessage = "Login is not allowed for Guest users. Please register on A-View website";
		    	throw (new AViewException(exceptionMessage));
			}
			else if((user != null) && (!(user.getPassword().equals(password))))
			{
				logger.info("Wrong password attempt by user: " + user.getUserName());
				String exceptionMessage = "Please check your user name/password";
		    	throw (new AViewException(exceptionMessage));
			}
			else
			{
				logger.info("User does not exist for: " + userName);
				String exceptionMessage = "User name does not exist";
				throw (new AViewException(exceptionMessage));
			}
		}
		else
		{		
			throw new AViewException("The server you are connecting is not licensed. Please contact your A-VIEW Administrator!");
		}
		return user ;
	}
	
	/**
	 * Approve pending users.
	 *
	 * @param userIds the user ids
	 * @param adminUserId the admin user id
	 * @throws AViewException
	 */
	public static void approvePendingUsers(List<Long> userIds,Long adminUserId) throws AViewException
	{
		List<List> brokenUserLists = ListUtils.breakListInto1000s(userIds);
		
		Integer pendingStatus = StatusHelper.getPendingStatusId();
		Integer activeStatus = StatusHelper.getActiveStatusId();
		
		List<User> usersToBeApproved = new ArrayList<User>();
		
		for(List brokenUserIds:brokenUserLists)
		{
			List<User> users = UserDAO.getUsers(brokenUserIds);
			for(User user:users)
			{
				if(user.getStatusId() != pendingStatus) //Only approve pending users
				{
					continue;
				}
				user.setStatusId(activeStatus);
				user.setModifiedAuditData(adminUserId, TimestampUtils.getCurrentTimestamp());
				usersToBeApproved.add(user);
			}
		}
		
		UserDAO.updateUsers(usersToBeApproved);
		
		EmailHelper.sendEmailForApprovedUsers(usersToBeApproved);
	}
	
	/**
	 * Gets the users by role.
	 *
	 * @param role the role
	 * @return the users by role
	 * @throws AViewException
	 */
	public static List<User> getUsersByRole(String role) throws AViewException
	{
		// Fix for bug #2567
		// 1. Added new parameter statusId
		// 2. Check the status id of retrieving users
		int activeSId = StatusHelper.getActiveStatusId();
		List<User> users = UserDAO.getUsersByRole(role,activeSId);
		populateFKNames(users);
		return users ;
	}

	// Fix for Bug #13227 start
	/**
	 * Search users.
	 *
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param userName the user name
	 * @param role the role
	 * @param city the city
	 * @param instituteId the institute id
	 * @param instituteAdminId the institute admin id
	 * @param emailId the email id
	 * @param mobileNumber the mobile number
	 * @param statusId the status id
	 * @return the list
	 * @throws AViewException
	 */
	private static List<User> searchUsers(String firstName,String lastName,
			String userName,String role,String city, Long instituteId, Long instituteAdminId, String emailId, String mobileNumber,
			Long districtId, Long stateId, Integer statusId) throws AViewException
	{
		List<User> users = UserDAO.searchUsers(firstName,lastName,userName, role,city,instituteId, emailId, mobileNumber, districtId, stateId, instituteAdminId,statusId);
		populateFKNames(users);
		return users ;
	}
	
	/**
	 * Search active users.
	 *
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param userName the user name
	 * @param role the role
	 * @param city the city
	 * @param instituteId the institute id
	 * @param instituteAdminId the institute admin id
	 * @param emailId the email id
	 * @param mobileNumber the mobile number
	 * @return the list
	 * @throws AViewException
	 */
	public static List<User> searchActiveUsers(String firstName,String lastName,
			String userName,String role,String city,Long instituteId,Long instituteAdminId, String emailId, String mobileNumber,
			Long districtId, Long stateId)
			 throws AViewException
	{
		return searchUsers(firstName,lastName,userName, role,city, instituteId, instituteAdminId, emailId, mobileNumber, districtId, stateId, StatusHelper.getActiveStatusId());
	}
	
	/**
	 * Search pending users.
	 *
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param userName the user name
	 * @param role the role
	 * @param city the city
	 * @param instituteId the institute id
	 * @param instituteAdminId the institute admin id
	 * @param emailId the email id
	 * @param mobileNumber the mobile number
	 * @return the list
	 * @throws AViewException
	 */
	public static List<User> searchPendingUsers(String firstName,String lastName,
			String userName,String role,String city,Long instituteId,Long instituteAdminId,String emailId, String mobileNumber,
			Long districtId, Long stateId)
			 throws AViewException
	{
		//Fix for Bug #13787 start
		return searchUsers(firstName,lastName,userName, role,city, instituteId, instituteAdminId, emailId, mobileNumber, districtId, stateId, StatusHelper.getPendingStatusId());
		//Fix for Bug #13787 end
	}
	// Fix for Bug #13227 end
	
	/**
	 * Search  to retrieve users with matching first name, lastname,username or e-mail id
	 * @param name the string to be searched among users
	 * @return
	 * @throws AViewException
	 */
	public static List<User> searchUsersByName(String name) throws AViewException
	{
		int statusId=StatusHelper.getActiveStatusId();
		List<User> users = UserDAO.searchUsersByName(name, statusId);
		if(users!=null)
			populateFKNames(users);
		return users ;

	}
	
	/**
	 * Populate fk names.
	 *
	 * @param users the users
	 * @throws AViewException
	 */
	private static void populateFKNames(List<User> users) throws AViewException
	{
		for(User user:users)
		{
			populateFKNames(user);
		}
	}
	
	/**
	 * Populate fk names.
	 *
	 * @param user the user
	 * @throws AViewException
	 */
	public static void populateFKNames(User user) throws AViewException
	{
		if(user == null)
		{
			return;
		}
		Long instituteId = user.getInstituteId();
		if(instituteId != null && instituteId != 0)
		{
			Institute institute = InstituteHelper.getInstitutesIdMap().get(instituteId); //Cached, so do not worry :-)
			if(institute != null)
			{
				user.setInstituteName(institute.getInstituteName());
				
				if(institute.getParentInstituteId() != null)
				{
					//logger.debug(institute.getInstituteId() + "************************");
					Institute parentInstitute = InstituteHelper.getInstitutesIdMap().get(institute.getParentInstituteId()); //Cached, so do not worry :-)
					user.setParentInstituteName(parentInstitute.getInstituteName());
				}
			}
		}
		
		Integer districtId = user.getDistrictId();
		if(districtId != null && districtId != 0)
		{
			District district = DistrictHelper.getDistrict(districtId);
			user.setDistrictName(district.getDistrictName());
			
			State state = StateHelper.getState(district.getStateId());
			user.setStateName(state.getStateName());
		}
		
	}
	
	/**
	 * Gets the user count.
	 *
	 * @param instituteId the institute id
	 * @return the user count
	 * @throws AViewException
	 */
	public static Integer getUserCount(Long instituteId) throws AViewException
	{
		return UserDAO.getUserCount(instituteId, StatusHelper.getActiveStatusId());
	}
	
	/**
	 * Delete users.
	 *
	 * @param instituteId the institute id
	 * @param modifiedByUserId the modified by user id
	 * @throws AViewException
	 */
	public static void deleteUsers(Long instituteId, Long modifiedByUserId) throws AViewException
	{
		int deletedSId = StatusHelper.getDeletedStatusId();
		List<User> users = UserDAO.getNonDeletedUsersByInstituteId(instituteId, StatusHelper.getDeletedStatusId());
			
		if((users != null) && (users.size() > 0))
		{
			for(User user:users)
			{
				setUserToDeleted(user, deletedSId, modifiedByUserId);
			}
			UserDAO.deleteUsers(users);
		}
	}
	
	/**
	 * Gets the users for live quiz.
	 *
	 * @param classId the class id
	 * @return the users for live quiz
	 * @throws AViewException
	 */
	public static List<User> getUsersForLiveQuiz(Long classId) throws AViewException
	{
		List<User> users = UserDAO.getUsersForLiveQuiz(classId, StatusHelper.getActiveStatusId());
		//populateFKNames(users);
		return users;
	}
	
	/**
	 * Gets the user by mobile number.
	 *
	 * @param mobileNumber the mobile number
	 * @return the user by mobile number
	 * @throws AViewException
	 */
	public static User getUserByMobileNumber(String mobileNumber) throws AViewException
	{
		User user = UserDAO.getUserByMobileNumber(mobileNumber, StatusHelper.getActiveStatusId());
		populateFKNames(user);
		return user;
	}
	
	/**
	 * Gets the users by institute id.
	 *
	 * @param instituteId the institute id
	 * @return the users by institute id
	 * @throws AViewException
	 */
	public static  List<User> getUsersByInstituteId(long instituteId)throws AViewException
	{
		List<User> users=null;
		users=UserDAO.getUsersByInstituteId(instituteId,  StatusHelper.getActiveStatusId());		
		return users;		
	}
	
	/**
	 * Creates the webinar guest user.
	 *
	 * @param user the user
	 * @throws AViewException
	 */
	public static void createWebinarGuestUser(User user) throws AViewException
	{
		user.setRole(Constant.GUEST_ROLE);
		Integer statusId = StatusHelper.getActiveStatusId();
		User webinarAdminUser = getUserByUserName(Constant.WEBINAR_ADMIN);
		UserHelper.createUser(user, webinarAdminUser.getUserId(), statusId);		
	}
	
	/**
	 * Reset password.
	 *
	 * @param userName the user name
	 * @param emailId the email id
	 * @throws AViewException
	 */
	public static void resetPassword(String userName, String emailId) throws AViewException
	{
		User user = UserHelper.getUserByUserName(userName);
		//User masterAdminUser = (UserHelper.getUsersByRole(Constant.MASTER_ADMIN_ROLE)).get(0);
		// Since more than one user is available with MASTER ADMIN Role, choose the user 
		// by administrator user name
		User masterAdminUser = UserHelper.getUserByUserName(Constant.MASTER_ADMIN_USER_NAME);
		String attr1Value = "User details for password reset: ";
		if((user != null) && (user.getEmail().equals(emailId)))
		{
			attr1Value += user.getUserId();
			// User name and email id matches. Generate a new random password and encrypt using SHA1
			String newPassword = RandomUtils.generateRandomString();
			//Fix for Bug 11036, 11038. 
			//Send the email after resetting the password
			UserHelper.updateUserChangePass(HashCodeUtils.SHA1(newPassword), user.getUserId(), masterAdminUser.getUserId());
			EmailHelper.sendEmailForPasswordReset(user.getEmail(), user.getUserName(), newPassword);
			UserActionHelper.createUserActionForResetPassword(attr1Value, masterAdminUser.getUserId(), Constant.SUCCESS);
		}
		else
		{
			if(user != null)
			{
				attr1Value += (user != null ? user.getUserId() : (userName + " " + emailId));
			}
			UserActionHelper.createUserActionForResetPassword(attr1Value, masterAdminUser.getUserId(), Constant.FAILED);
			throw new AViewException("User name and email do not match. Please enter your registered user name and email id.");
		}			
	}

	/**
	 * Function to check the user id is valid 
	 * @param context
	 * @param userId
	 * @return object
	 * @throws AViewException
	 */
	public static Object userValidCheck(String context,Long userId) throws AViewException
	{
		Object userActErrorMessage = new Object();
		User user = null;
		if(userId == null)
		{
			userId = 0l;
		}
		else
		{
			user = UserHelper.getUser(userId);
		}
		if(user == null)
		{
			userActErrorMessage = "Given "+context+" is not valid or doesn't exist.";
		}
		else if(user != null && user.getStatusId() == StatusHelper.getActiveStatusId())
		{
			logger.debug("Given "+context+" exist");
			if(context.equals("adminId"))
			{
				logger.debug("Given "+context+" exist");
				userActErrorMessage = user;
			}
			else if(context.equals("userId"))
			{
				logger.debug("Given "+context+" exist");
				userActErrorMessage = user;
			}
			else if(context.equals("moderatorId"))
			{
				logger.debug("Given "+context+" exist");
				userActErrorMessage = user;
			}
			else
			{
				userActErrorMessage = "Given "+context+" is not valid.";
			}
		}
		else if(user != null && user.getStatusId() == StatusHelper.getDeletedStatusId())
		{
			userActErrorMessage = "Given "+context+" is already deleted.";
		}
		else
		{
			userActErrorMessage = "Given "+context+" is not valid or doesn't exist.";
		}
		return userActErrorMessage;
	}

	/**
	 * Function to validate the user 
	 * @param user
	 * @param adminId
	 * @return string
	 * @throws AViewException
	 */
	public static Object validationCheckForUser(User user,User admin) throws AViewException
	{
		String validationErrorMessage = "";
		if (user.getDistrictId() == null || user.getDistrictId() == 0) 
		{
			validationErrorMessage = "Entered District Id is not valid or District id is not given.";
			return validationErrorMessage;
  		} 
		else 
		{
			String intError = null;
			District district = DistrictHelper.getDistrict(user.getDistrictId());
			if(district != null && district.getStatusId() == StatusHelper.getActiveStatusId() )
			{
				logger.debug("Enter district id is active::user creation.");
			}
			else
			{
				validationErrorMessage = "District Id is not valid or doesn't exist.";
				return validationErrorMessage;
			}
			intError = ValidationUtils.integerOnly(user.getDistrictId().toString());
            if (intError != null) 
            {
            	validationErrorMessage = intError+" in District Id.";
            	return validationErrorMessage;
            }
		}
		// removed EXECUTIVE_AVIEW from role since it s not their in db
		if (user.getRole() != null && user.getRole() != "") 
		{
			
			String name = user.getRole().toUpperCase();
			int i = 0;
			while (i < userRole.length) 
			{
				if (name.equals(userRole[i])) 
				{
					break;
				}
				i++;
			}
			if (!(i < userRole.length))
			{
				validationErrorMessage = "Role should be "+Constant.ADMIN_ROLE +"," +Constant.GUEST_ROLE+","+Constant.MASTER_ADMIN_ROLE+","+Constant.STUDENT_ROLE+","+Constant.TEACHER_ROLE+","+Constant.MONITOR_ROLE+" others not valid.";
				return validationErrorMessage;
			}
		}
		else if(user.getRole() == null || user.getRole()== "")
		{
			user.setRole(Constant.STUDENT_ROLE);
		}			
		if (user.getUserName() == null || user.getUserName() == "" || user.getUserName().length() > 100) 
		{
			validationErrorMessage = "Entered username is not valid or username is not given." ;
			return validationErrorMessage;
		} 
		else 
		{
			String CharForUserAndpass = null;
			CharForUserAndpass = ValidationUtils.AllowedCharForUserAndpass(user.getUserName());
			if (CharForUserAndpass != null) 
			{
				validationErrorMessage = CharForUserAndpass+" in the userName.";
				return validationErrorMessage;
			}
		}
		if (user.getPassword() == null || user.getPassword() == "") 
		{
			validationErrorMessage = "Entered Password is not valid or Password is not given.";
			return validationErrorMessage;
		} 
		else 
		{
            String CharForUserAndpass = null;
            CharForUserAndpass = ValidationUtils.AllowedCharForUserAndpass(user.getPassword());
            if (CharForUserAndpass != null) 
            {
            	validationErrorMessage = CharForUserAndpass+" in Password.";
            	return validationErrorMessage;
            }
            if (user.getPassword().length() > 50) 
            {
            	validationErrorMessage ="Length exceeds in the password.";
            	return validationErrorMessage;
            }
		}
		if(user.getCreatedFrom() == null || user.getCreatedFrom() == "")
		{
			user.setCreatedFrom("API");
		}
		if (user.getInstituteId() == null || user.getInstituteId() == 0) 
		{			
			validationErrorMessage = "Entered Institute Id is not valid or Institute Id is not given.";
			return validationErrorMessage;
		}
		else 
		{
			String intError = null;
			boolean isAdmin = false;
			intError = ValidationUtils.integerOnly(user.getInstituteId().toString());
			if (intError != null) 
			{
				validationErrorMessage = intError+" in the instituteId.";
				return validationErrorMessage;
			}
			Institute institute =InstituteHelper.getInstituteById(user.getInstituteId());
			if(institute != null && institute.getStatusId() == StatusHelper.getActiveStatusId())
			{
				List<InstituteAdminUser> instituteAdminUser = InstituteAdminUserHelper.getInstituteAdminUsers();
				List<Institute> institutes = InstituteHelper.getAllInstitutesForAdmin(admin.getUserId());
				Institute inst = InstituteHelper.getInstituteById(user.getInstituteId());
				if(institutes != null)
				{
					 isAdmin = institutes.contains(inst);
				}
				for (int j = 0;j < instituteAdminUser.size(); j++)
				{
					InstituteAdminUser instAdminUser = (InstituteAdminUser)instituteAdminUser.get(j) ;
					if(isAdmin == true && admin.getUserId().equals(instAdminUser.getUser().getUserId()) || admin.getRole().equals(Constant.MASTER_ADMIN_ROLE))
					{
						logger.debug("Enter institute id is a active institute id::user creation.");
						break;
					}
					else
					{
						if(j == (instituteAdminUser.size()-1))
						{
							validationErrorMessage ="Given admin is not Administrator of the particular institute or Master Admin.";
							return validationErrorMessage;
						}
					}
				}
			}
			else
			{
				validationErrorMessage = "InstituteId is not valid or doesn't exist.";
				return validationErrorMessage;
			}
		}
		if (user.getFname() == null || user.getFname() == "") 
		{
			validationErrorMessage = "Entered Firstname is not valid or Firstname is not given.";
			return validationErrorMessage;
		} 
		else 
		{
			String nameError = null;
			nameError = ValidationUtils.validateFnameAndLname(user.getFname());
			if (nameError != null) 
			{
				validationErrorMessage = nameError + " in the FirstName.";
				return validationErrorMessage;
			}
		}
		if (user.getLname() == null || user.getLname() == "") 
		{
			validationErrorMessage = "Entered LastName is not valid or LastName is not given.";
			return validationErrorMessage;
		} 
		else 
		{
			String nameError=null;
			nameError = ValidationUtils.validateFnameAndLname(user.getLname());
			if (nameError != null) 
			{
				validationErrorMessage = nameError +" in the LastName.";
				return validationErrorMessage;
			}
		}
		if (user.getAddress() == null || user.getAddress() == "")
		{
			validationErrorMessage = "Entered Address is not valid or Address is not given.";
			return validationErrorMessage;
		} 
		else 
		{
			String AddError=null;
			AddError = ValidationUtils.validateAddress(user.getAddress());
			if (AddError != null) 
			{
				validationErrorMessage = AddError+" in user Address";
				return validationErrorMessage;
			}
		}
		if (user.getCity() == null || user.getCity() == "") 
		{
			validationErrorMessage = "Entered City is not valid or City is not given.";
			return validationErrorMessage;
		} 
		else 
		{
			String cityError=null;
			cityError = ValidationUtils.validateCity(user.getCity());
			if (cityError != null) 
			{
				validationErrorMessage = cityError+" in the city";
				return validationErrorMessage;
			}
		}
		if (user.getEmail() == null || user.getEmail() == "") 
		{
			validationErrorMessage = "Entered Email id is not valid or Email id is not given.";
			return validationErrorMessage;
		} 
		else 
		{
			String emailError = null;
			emailError = ValidationUtils.EmailValidation(user.getEmail());
			if (emailError != null) 
			{
				validationErrorMessage = emailError;
				return validationErrorMessage;
			}
		}
		if (user.getZipId() == null || user.getZipId() == "") 
		{
			validationErrorMessage = "Entered Zip id is not valid or Zip id is not given.";
			return validationErrorMessage;
		}
		else 
		{
			String zipError = null;
			zipError = ValidationUtils.ZipcodeValidation(user.getZipId());
			if (zipError != null) 
			{
				validationErrorMessage = zipError;
				return validationErrorMessage;
			}
		}
		if (user.getMobileNumber() == null || user.getMobileNumber() == "") 
		{
			validationErrorMessage = "Entered Mobile Number is not valid or Mobile Number is not given.";
			return validationErrorMessage;
		} 
		else 
		{
			String mobileError = null;
			mobileError = ValidationUtils.MobNumValidation(user.getMobileNumber());
			if (mobileError != null) 
			{
				validationErrorMessage = mobileError;
				return validationErrorMessage;
			}
		}
		return null;
	}
	
	/**
	 * API to create user.
	 * @param adminId
	 * @param userDetails as JSON
	 * @param isMailSend as String
	 * @return Response
	 * @throws AViewException
	 */
	@RequestMapping(value ="/createuser.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response createUser(@RequestParam("adminId") Long adminId,
			@RequestParam("userDetails") String userDetails,
			@RequestParam("sendEmail")String sendEmail) throws AViewException
		{
		String result = new String();
		logger.debug("Enter user creation::user creation ");
		User user = null;
		String errorMessage = null;
		User admin = null;
		Object resultObjectAdmin = userValidCheck(Constant.ADMIN_ID,adminId);
		if(User.class.isInstance(resultObjectAdmin))
		{
			admin = (User)resultObjectAdmin;
		}
		else
		{
			errorMessage = resultObjectAdmin.toString();
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		int statusId = StatusHelper.getActiveStatusId();
		Object resultObject = JSONParserUtils.readJSONAsObject(userDetails, User.class);
		if(User.class.isInstance(resultObject))
		{
			user = (User)resultObject;
		}
		else
		{
			errorMessage = resultObject.toString();
			logger.error(errorMessage);
		}
		String validationErrorMessage = null;
		if(user != null)
		{
			Object validateErrorMessage = validationCheckForUser(user,admin);
			if(validateErrorMessage != null)
			{
				return Response.status(Status.BAD_REQUEST).entity(validateErrorMessage.toString()).build();
			}
		}
		else
		{
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		try 
		{
			//result = "success";
			Long resultId = 0l;
			UserHelper.createUser(user, adminId, statusId);
			resultId = user.getUserId();
			if((sendEmail.equals("Y")|| sendEmail.equals("y")) && !user.getEmail().equals(null))
			{
				String instituteName = null;
				if(admin != null)
				{
					instituteName = admin.getInstituteName();
				}
				EmailHelper.sendEmailForNewActiveUserRegistration(user.getEmail(), user.getUserName(), instituteName);
			}
			logger.debug("Exit user creation on success::user creation");
			return Response.status(Status.OK).entity(resultId).build();
		} 
		catch (NumberFormatException nfe) 
		{
			result = "invalid";
			logger.debug("Exit user creation on invalid request::user creation");
			return Response.status(Status.BAD_REQUEST).entity(result).build();    
		}
		catch(AViewException ae)
		{
			if(ae.getMessage().equals("Duplicate entry '"+ user.getUserName()+"' for key 'user_name'"))
			{
				result = "The given user name already exists. Please try a different user name.";
			}
			else
			{
				result = "Error during log. Possible reason(s): 1. Unexpected data 3. Unknown.";
			}
			logger.debug("Exit user creation on error durning log::user creation");
	    	return Response.status(Status.BAD_REQUEST).entity(result).build();
		}
	}

	/**
	 * API to search user.
	 * @param adminId
	 * @param userName
	 * @param fname 
	 * @param lname
	 * @param role
	 * @param city
	 * @param instituteid
	 * @return Response
	 * @throws AViewException
	 */
	@RequestMapping(value = "/searchuser.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response searchUser(@RequestParam("adminId") Long adminId,
			@RequestParam("role") String role,
			@RequestParam("userName") String userName,
			@RequestParam("fname") String fname,
			@RequestParam("lname") String lname,
			@RequestParam("city") String city,
			@RequestParam("instituteId") Long instituteId) throws AViewException 
	{
		logger.debug("Enter user search::user search ");
		String errorMessage = null;
		ArrayList userDetailsArray = new ArrayList();
		User admin = null;
		Institute institute = null;
		String trimUserName = userName.trim();
		String trimFirstName = fname.trim();
		String trimLastName = lname.trim();
		String trimRole = role.trim();
		String trimCity = city.trim();
		Object resultObjectAdmin = userValidCheck(Constant.ADMIN_ID,adminId);
		if(User.class.isInstance(resultObjectAdmin))
		{
			 admin = (User)resultObjectAdmin;
			 if(admin.getRole().equals(Constant.GUEST_ROLE))
			 {
				 return Response.status(Status.BAD_REQUEST).entity("Given admin Id is not valid or doesn't exist").build();
			 }
		}
		else
		{
			errorMessage = resultObjectAdmin.toString();
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		if(instituteId != null)
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
				return Response.status(Status.BAD_REQUEST).entity("Entered Institute Id is not valid or doesn't exist").build();
			}
		}
		String userRole = null;
		if(trimRole.equals(null) || trimRole.equals(""))
		{
		}
		else
		{
			userRole = trimRole.toUpperCase();
			if(!userRole.equals(Constant.ADMIN_ROLE) && !userRole.equals(Constant.TEACHER_ROLE) && !userRole.equals(Constant.STUDENT_ROLE))
			{
				return Response.status(Status.BAD_REQUEST).entity("Role should be Administrator,Teacher or Student").build();
			}
		}
		if((trimUserName.equals(null) || trimUserName.equals("")) && (trimFirstName.equals(null) || trimFirstName.equals("")) && (trimLastName.equals(null) || 
				trimLastName.equals(""))  && (trimCity.equals(null) || trimCity.equals("")) && (trimRole.equals(null) || trimRole.equals("")) &&
				(instituteId == null ))
		{
			return Response.status(Status.BAD_REQUEST).entity("Please provide any of the search criteria").build();
		}
		List<User> userDetailList = null;
		Long instituteIdValue = (institute == null) ? null : institute.getInstituteId();
		if(admin.getRole().equals(Constant.MASTER_ADMIN_ROLE))
		{
			adminId = null;
			userDetailList = searchActiveUsers(trimFirstName, trimLastName, trimUserName, userRole, trimCity, instituteIdValue, adminId, null, null, 0l, 0l);	
		}
		else 
		{
			userDetailList = searchActiveUsers(trimFirstName, trimLastName, trimUserName, userRole, trimCity, instituteIdValue, adminId, null, null,0l, 0l);	
		}
		if(userDetailList == null || userDetailList.size()== 0) 
		{
			return Response.status(Status.BAD_REQUEST).entity("No active user details returned for the given search criteria").build();
		} 
		else 
		{
			ArrayList userArray = new ArrayList();
			for(User userDetails:userDetailList)
			{
				userArray = new ArrayList();
				userArray.add("userName:" + userDetails.getUserName());
				userArray.add("userId:" + userDetails.getUserId());
				userArray.add("firstName:" + userDetails.getFname());
				userArray.add("lastName:" + userDetails.getLname());
				userArray.add("institute:" + userDetails.getInstituteName());
				userArray.add("parentInstitute:" + userDetails.getParentInstituteName());
				userArray.add("role:" + userDetails.getRole());
				userArray.add("email:" + userDetails.getEmail());
				userArray.add("mobileNumber:" + userDetails.getMobileNumber());
				userDetailsArray.add(userArray);
			}
		}
		logger.debug("Exit user search on success:user search");
		return Response.status(Status.OK).entity(userDetailsArray).build();
	}
	/**
	 * API to delete user.
	 * @param adminId
	 * @param userId 
	 * @return Response
	 * @throws AViewException
	 */
	@RequestMapping(value = "/deleteuser.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response userDelete(@RequestParam("adminId") Long adminId,@RequestParam("userId") Long userId) throws AViewException 
	{
		logger.debug("Enter user delete::user delete ");
		User user = null;
		String errorMessage = null;
		InstituteAdminUser instAdminUser = null;
		boolean isAdmin = false;
		User admin = null;
		Object resultObjectAdmin = userValidCheck(Constant.ADMIN_ID,adminId);
		if(User.class.isInstance(resultObjectAdmin))
		{
			admin = (User)resultObjectAdmin;
		}
		else
		{
			errorMessage = resultObjectAdmin.toString();
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		Object resultObjectUser = userValidCheck(Constant.USER_ID,userId);
		if(User.class.isInstance(resultObjectUser))
		{
			user = (User)resultObjectUser;
		}
		else
		{
			errorMessage = resultObjectUser.toString();
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		if (user!= null && user.getStatusId() == StatusHelper.getActiveStatusId())
		{
			Institute instituteDetails = InstituteHelper.getInstituteById(user.getInstituteId());
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
			if (admin.getRole().equals(Constant.MASTER_ADMIN_ROLE) || (isAdmin == true && admin.getRole().equals(Constant.ADMIN_ROLE)) ) 
			{
				deleteUser(user.getUserId(), admin.getUserId());
				logger.debug("Exit user delete on success:user delete");
				return Response.status(Status.OK).entity("Deleted "+user.getUserName()+"(ID: " +userId + ") successfully").build();
			} 
			else 
			{
				return Response.status(Status.BAD_REQUEST).entity("Admin Id is not authorized to perform this operation").build();
			}
		}
		else
		{
			return Response.status(Status.BAD_REQUEST).entity("User "+userId+" doesn't exist").build();
		}
	}
	
	/**
	 * API to update user
	 * @param adminId
	 * @param userDetails
	 * @return response
	 * @throws AViewException
	 */
	@RequestMapping(value ="/updateuser.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response updateUser(@RequestParam("adminId") Long adminId,@RequestParam("userDetails") String userDetails) throws AViewException 
	{
		logger.debug("Enter user updation::user updation ");
		User tempUserArray = null;
		User user = null;
		String errorMessage = null;
		InstituteAdminUser instAdminUser = null;
		boolean isAdmin = false;
		User admin = null;
		Object resultObjectAdmin = userValidCheck(Constant.ADMIN_ID,adminId);
		if(User.class.isInstance(resultObjectAdmin))
		{
			admin = (User)resultObjectAdmin;
		}
		else
		{
			errorMessage = resultObjectAdmin.toString();
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		Object resultObject = JSONParserUtils.readJSONAsObject(userDetails, User.class);
		if(User.class.isInstance(resultObject))
		{
			tempUserArray = (User)resultObject;
			Object resultObjectUpdater = userValidCheck(Constant.USER_ID,tempUserArray.getUserId());
			if(User.class.isInstance(resultObjectUpdater))
			{
				user = (User)resultObjectUpdater;
			}
			else
			{
				errorMessage = resultObjectUpdater.toString();
				return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
			}
			if(tempUserArray.getUserName() != null)
			{
				return Response.status(Status.BAD_REQUEST).entity("Username cannot be updated").build();
			}
			if(tempUserArray.getPassword() != null)
			{
				return Response.status(Status.BAD_REQUEST).entity("Password cannot be updated").build();
			}
			if(tempUserArray.getRole() != null)
			{
				if(admin.getRole().equals(Constant.MASTER_ADMIN_ROLE) || admin.getRole().equals(Constant.ADMIN_ROLE))
				{
					user.setRole(tempUserArray.getRole().toUpperCase());
				}
			}
			if(tempUserArray.getFname() != null)
			{
				user.setFname(tempUserArray.getFname());
			}
			if(tempUserArray.getLname() != null)
			{
				user.setLname(tempUserArray.getLname());
			}
			if(tempUserArray.getEmail() != null)
			{
				user.setEmail(tempUserArray.getEmail());
			}
			if(tempUserArray.getAddress() != null)
			{
				user.setAddress(tempUserArray.getAddress());
			}
			if(tempUserArray.getCity() != null)
			{
				user.setCity(tempUserArray.getCity());
			}
			if(tempUserArray.getDistrictId() != null)
			{
				user.setDistrictId(tempUserArray.getDistrictId());
			}
			if(tempUserArray.getZipId() != null)
			{
				user.setZipId(tempUserArray.getZipId());
			}
			if(tempUserArray.getMobileNumber() != null)
			{
				user.setMobileNumber(tempUserArray.getMobileNumber());
			}
			if(tempUserArray.getInstituteId() != 0l)
			{
				user.setInstituteId(tempUserArray.getInstituteId());
			}
		}
		else
		{
			errorMessage = resultObject.toString();
			logger.error(errorMessage);
		}
		String validationErrorMessage = null;
		if(tempUserArray != null)
		{
			Object validateErrorMessage = validationCheckForUser(user,admin);
			if(validateErrorMessage != null)
			{
				return Response.status(Status.BAD_REQUEST).entity(validateErrorMessage.toString()).build();
			}
		}
		else
		{
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		String result = null;
		try 
		{
			Long resultId = 0l;
			UserHelper.updateUser(user,adminId);
			resultId = user.getUserId();
			result = "Updated user (ID: "+resultId+") successfully";	
			logger.debug("Exit user updation on success::user updation");
			return Response.status(Status.OK).entity(result).build();
		} 
		catch (NumberFormatException nfe) 
		{
			result = "invalid";
			logger.debug("Exit user updation on invalid request::user updation");
			return Response.status(Status.BAD_REQUEST).entity(result).build();
		}
		catch(AViewException ae)
		{
			if(ae.getMessage().equals("Duplicate entry '"+ tempUserArray.getUserName()+"' for key 'user_name'"))
			{
				result = "The given user name already exists. Please try a different user name.";
			}
			else
			{
				result = "Error during log. Possible reason(s): 1. Unexpected data 3. Unknown.";
			}
			logger.debug("Exit user updation on error durning log::user updation");
			return Response.status(Status.BAD_REQUEST).entity(result).build();
		}
		catch(HibernateException he)
		{
			result = he.getMessage();
			logger.debug(result);
			return Response.status(Status.BAD_REQUEST).entity(result).build();
		}
		
	}

	/**
	 * function is used to get user details from the given userName.
	 * @param userName
	 * @param newPassword
	 * @return Response
	 * @throws AViewException
	 */
	//TODO: The implementation of this function needs be thought about.
	@RequestMapping(value = "/changepassword.html",method = RequestMethod.POST)
	@ResponseBody
	public static Response changePassword(@RequestParam("userId")  Long userId,
			@RequestParam("newPassword") String newPassword) throws AViewException 
	{
		String errorMessage = null;
		String result = null; 
		if(userId == null || userId == 0)
		{
			userId = 0l;
		}
		User user = UserHelper.getUser(userId);
		if(user == null)
		{
			errorMessage ="Given user Id doesn't exist or not valid";
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		else if(user != null && user.getStatusId() == StatusHelper.getActiveStatusId())
		{
		    String CharForUserAndpass = null;
	        CharForUserAndpass = ValidationUtils.AllowedCharForUserAndpass(newPassword);
	        if (CharForUserAndpass != null) 
	        {
	            return Response.status(Status.BAD_REQUEST).entity(CharForUserAndpass+" in Password").build();
	        }
		}
		else if(user != null && user.getStatusId() != StatusHelper.getActiveStatusId())
		{
			errorMessage ="Given user Id doesn't exist or not valid";
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		try 
		{
			if(user.getPassword().equals(newPassword))
			{
				return Response.status(Status.BAD_REQUEST).entity("New password and Current password cannot be the same").build();
			}
			Long resultId = 0l;
			UserHelper.updateUserChangePass(newPassword, userId, userId);
			resultId = user.getUserId();
			result = "User Password has updated successfully";
			logger.debug("Exit password change on success::user creation");
			return Response.status(Status.OK).entity(result).build();
		} 
		catch (NumberFormatException nfe) 
		{
			result = "invalid";
			logger.debug("Exit password change on invalid request::user creation");
			return Response.status(Status.BAD_REQUEST).entity(result).build();
		}
		catch(AViewException ae)
		{
			result = "Error during log. Possible reason(s): 1. Password change is not acceptable 2. Unexpected data 3. Unknown";
			logger.debug("Exit password change on error durning log::user creation");
			return Response.status(Status.BAD_REQUEST).entity(result).build();
		}
	}
	
	/**
	 * This function can be used to get user details from the given userName.
	 * @param userName
	 * @param userPassword
	 * @return object
	 * @throws AViewException
	 */
	//TODO: The implementation of this function needs be thought about.
	@RequestMapping(value = "/userDetailsByUserName.html", produces= "application/json", method = RequestMethod.GET)
	@ResponseBody
	public static Object userDetailsByUserName(@RequestParam("userName")  String userName,
			@RequestParam("userPassword") String userPassword) throws AViewException 
	{
		User user = UserHelper.getUserByUserNamePassword(userName, userPassword);
		if (user == null) {
			return "failed";
		}
		return user;
	}
}
