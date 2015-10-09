/*
 * 
 */
package edu.amrita.aview.common.helpers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;

import edu.amrita.aview.common.utils.SimpleEmailSenderUtils;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.gclm.entities.Class;
import edu.amrita.aview.gclm.entities.Lecture;
import edu.amrita.aview.gclm.entities.User;


/**
 * The Class EmailHelper.
 */
public class EmailHelper {
	
	private static Logger logger = Logger.getLogger(EmailHelper.class);
	// user registration
	/** The user registration email subject. */
	private static String userRegistrationEmailSubject = "";
	
	/** The user registration email content. */
	private static String userRegistrationEmailContent = "";

	private static String activeUserRegistrationEmailSubject = "";
	private static String activeUserRegistrationEmailContent = "";
	
	// user registration for Admin
	/** The user registration email subject for admin. */
	private static String userRegistrationEmailSubjectForAdmin = "";
	
	/** The user registration email content for admin. */
	private static String userRegistrationEmailContentForAdmin = "";
	
	/** The user registration approval team email ids. */
	private static String userRegistrationApprovalTeamEmailIds = "";
	
	//institute Registration
	/** The institute registration email subject. */
	private static String instituteRegistrationEmailSubject = "";
	
	/** The institute registration email content. */
	private static String instituteRegistrationEmailContent = "";
	
	/** The institute approval team email ids. */
	private static String instituteApprovalTeamEmailIds = "";
	
	//Inactive status for class registration
	/** The class registration inactive status email subject. */
	private static String classRegistrationInactiveStatusEmailSubject = "";
	
	/** The class registration inactive status email content. */
	private static String classRegistrationInactiveStatusEmailContent = "";
	
	//Active status for class registration
	private static String activeClassRegistrationEmailSubject = "";
	private static String activeClassRegistrationEmailContent = "";
	
	//Communicating status for class registration 
	/** The class registration communicating status email subject. */
	private static String classRegistrationCommunicatingStatusEmailSubject = "";
	
	/** The class registration communicating status email content. */
	private static String classRegistrationCommunicatingStatusEmailContent = "";
	
	//Forgot Password reset for user
	/** The reset password email subject. */
	private static String resetPasswordEmailSubject = "";
	
	/** The reset password email content. */
	private static String resetPasswordEmailContent = "";
	
	//User activation email
	/** The user activated email subject. */
	private static String userActivatedEmailSubject = "";
	
	/** The user activated email content. */
	private static String userActivatedEmailContent = "";
	
	//Workshop registration email
	/** The workshop registration email subject. */
	private static String workshopRegistrationEmailSubject = "";
	
	/** The workshop registration email content. */
	private static String workshopRegistrationEmailContent = "";
	
	//A-VEW Admin email id
	/** The aview admin email id. */
	private static String aviewAdminEmailId = "";
	
	//Common for all
	/** The email signature. */
	private static String emailSignature = "";
	private static String emailSignatureForAPI = "";

	//Class schedule change email
	/** The class schedule change email subject. */
	private static String classScheduleChangeEmailSubject = "";
	
	/** The class schedule change email content. */
	private static String classScheduleChangeEmailContent = "";
	
	//Lecture schedule change email
	/** The lecture schedule change email subject. */
	private static String lectureScheduleChangeEmailSubject = "";
	
	/** The lecture schedule change email content. */
	private static String lectureScheduleChangeEmailContent = "";
	
	//Lecture creation email
	private static String lectureCreationEmailSubject = "";
	private static String lectureCreationEmailContent = "";
	
	//Adhoc Meeting invitation email
	private static String meetingInvitationEmailSubject = "";
	private static String meetingInvitationEmailContent = "";
	//Additional Adhoc Meeting invitation content for guest users
	private static String additionalMeetingInvitationEmailContentForGuestUsers = "";
	private static String additionalScheduledMeetingInvitationEmailContent = "";
	
	//Meeting cancellation email
	private static String meetingCancellationEmailSubject = "";
	private static String meetingCancellationEmailContent = "";
	
	//Meeting schedule change email
	private static String meetingScheduleChangeEmailSubject = "";
	private static String meetingScheduleChangeEmailContent = "";
	
	//Meeting Room cancellation email
	private static String meetingRoomCancellationEmailSubject = "";
	private static String meetingRoomCancellationEmailContent = "";
	
	private static SimpleDateFormat timeinAmPM = new SimpleDateFormat("hh:mm a z");
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
	
    //Variable to hold institute name
	private static String defaultInstituteName = "" ;
	
	static 
	{
		Properties getEmailProps = new Properties();
		try 
		{
			getEmailProps.load(EmailHelper.class.getClassLoader().getResourceAsStream("sendemail.properties"));
			
			userRegistrationEmailSubject = getEmailProps.getProperty("sendemail.userRegistrationEmailSubject");
			
			userRegistrationEmailContent = getEmailProps.getProperty("sendemail.userRegistrationEmailContent");
			
			activeUserRegistrationEmailSubject = getEmailProps.getProperty("sendemail.activeUserRegistrationEmailSubject");
			
			activeUserRegistrationEmailContent = getEmailProps.getProperty("sendemail.activeUserRegistrationEmailContent");
			
			userRegistrationEmailSubjectForAdmin = getEmailProps.getProperty("sendemail.userRegistrationEmailSubjectForAdmin");
			
			userRegistrationEmailContentForAdmin = getEmailProps.getProperty("sendemail.userRegistrationEmailContentForAdmin");
			
			userRegistrationApprovalTeamEmailIds = getEmailProps.getProperty("sendemail.userRegistrationApprovalTeamEmailIds");
			
			instituteRegistrationEmailSubject = getEmailProps.getProperty("sendemail.instituteRegistrationEmailSubject");
			
			instituteRegistrationEmailContent = getEmailProps.getProperty("sendemail.instituteRegistrationEmailContent");
			
			instituteApprovalTeamEmailIds = getEmailProps.getProperty("sendemail.instituteApprovalTeamEmailIds");
			
			classRegistrationInactiveStatusEmailSubject = getEmailProps.getProperty("sendemail.classRegistrationInactiveStatusEmailSubject");
			
			classRegistrationInactiveStatusEmailContent = getEmailProps.getProperty("sendemail.classRegistrationInactiveStatusEmailContent");
			
			activeClassRegistrationEmailSubject = getEmailProps.getProperty("sendemail.activeClassRegistrationEmailSubject");
			
			activeClassRegistrationEmailContent = getEmailProps.getProperty("sendemail.activeClassRegistrationEmailContent");
			
			classRegistrationCommunicatingStatusEmailSubject = getEmailProps.getProperty("sendemail.classRegistrationCommunicatingStatusEmailSubject");
			
			classRegistrationCommunicatingStatusEmailContent = getEmailProps.getProperty("sendemail.classRegistrationCommunicatingStatusEmailContent");

			resetPasswordEmailSubject = getEmailProps.getProperty("sendemail.resetPasswordEmailSubject");
			
			resetPasswordEmailContent = getEmailProps.getProperty("sendemail.resetPasswordEmailContent");

			userActivatedEmailSubject = getEmailProps.getProperty("sendemail.userActivatedEmailSubject");
			
			userActivatedEmailContent = getEmailProps.getProperty("sendemail.userActivatedEmailContent");
			
			workshopRegistrationEmailSubject = getEmailProps.getProperty("sendemail.workshopRegistrationEmailSubject");
			
			workshopRegistrationEmailContent = getEmailProps.getProperty("sendemail.workshopRegistrationEmailContent");
			
			emailSignature = getEmailProps.getProperty("sendemail.emailSignature");
			
			emailSignatureForAPI = getEmailProps.getProperty("sendemail.emailSignatureForAPI");
			
			classScheduleChangeEmailSubject = getEmailProps.getProperty("sendemail.classScheduleChangeEmailSubject");
			
			classScheduleChangeEmailContent = getEmailProps.getProperty("sendemail.classScheduleChangeEmailContent");
			
			lectureScheduleChangeEmailSubject = getEmailProps.getProperty("sendemail.lectureScheduleChangeEmailSubject");
			
			lectureScheduleChangeEmailContent = getEmailProps.getProperty("sendemail.lectureScheduleChangeEmailContent");
			
			lectureCreationEmailSubject = getEmailProps.getProperty("sendemail.lectureCreationEmailSubject");
			
			lectureCreationEmailContent = getEmailProps.getProperty("sendemail.lectureCreationEmailContent");
			
			meetingInvitationEmailSubject = getEmailProps.getProperty("sendemail.meetingInvitationEmailSubject");
		
			meetingInvitationEmailContent = getEmailProps.getProperty("sendemail.meetingInvitationEmailContent");	
			
			meetingCancellationEmailSubject = getEmailProps.getProperty("sendemail.meetingCancellationEmailSubject");
			
			meetingCancellationEmailContent = getEmailProps.getProperty("sendemail.meetingCancellationEmailContent");
			
			meetingScheduleChangeEmailSubject = getEmailProps.getProperty("sendemail.meetingScheduleChangeEmailSubject");
			
			meetingScheduleChangeEmailContent = getEmailProps.getProperty("sendemail.meetingScheduleChangeEmailContent");
		
			additionalMeetingInvitationEmailContentForGuestUsers = getEmailProps.getProperty("sendemail.additionalMeetingInvitationEmailContentForGuestUsers");
			
			additionalScheduledMeetingInvitationEmailContent = getEmailProps.getProperty("sendemail.additionalScheduledMeetingInvitationEmailContent");
			
			meetingRoomCancellationEmailSubject = getEmailProps.getProperty("sendemail.meetingRoomCancellationEmailSubject");
			
			meetingRoomCancellationEmailContent = getEmailProps.getProperty("sendemail.meetingRoomCancellationEmailContent");
			
			defaultInstituteName = getEmailProps.getProperty("sendemail.defaultInstituteName");
		
		} 
		catch (IOException ioe)
		{
			// processException(ioe);
		}
	}

	/**
	 * Send email for in active status.
	 *
	 * @param emailId the email id
	 * @param className the class name
	 */
	public static void sendEmailForInActiveStatus(String emailId, String className) 
	{
		StringBuffer tmpemailContent = new StringBuffer();
		StringBuffer emailSubject = new StringBuffer();
		
		emailSubject.append(classRegistrationInactiveStatusEmailSubject);
		emailSubject.append("\"");
		emailSubject.append(className);
		emailSubject.append("\"");
		
		tmpemailContent.append(classRegistrationInactiveStatusEmailContent);
		tmpemailContent.append(emailSignature);
		
		String emailContent = new String(tmpemailContent);
		emailContent = emailContent.replaceAll("setWorkshopName", className);
		sendEmail(emailId, emailContent, emailSubject.toString());
		
	}

	/**
	 * Send email for communicating status.
	 *
	 * @param emailId the email id
	 * @param mobileNumber the mobile number
	 * @param className the class name
	 */
	public static void sendEmailForCommunicatingStatus(String emailId, String mobileNumber, String className) 
	{
		StringBuffer tmpemailContent = new StringBuffer();		
		StringBuffer emailSubject = new StringBuffer();
		
		emailSubject.append(classRegistrationCommunicatingStatusEmailSubject);
		emailSubject.append("\"");
		emailSubject.append(className);
		emailSubject.append("\"");
		
		tmpemailContent.append(classRegistrationCommunicatingStatusEmailContent);
		tmpemailContent.append(emailSignature);
		
		String emailContent = new String(tmpemailContent);
		emailContent = emailContent.replaceAll("setPhoneNumber", mobileNumber);
		emailContent = emailContent.replaceAll("setWorkshopName", className);
		sendEmail(emailId, emailContent, emailSubject.toString());
	}

	/**
	 * Send email for password reset.
	 *
	 * @param emailId the email id
	 * @param userName the user name
	 * @param newPassword the new password
	 */
	public static void sendEmailForPasswordReset(String emailId, String userName, String newPassword) 
	{		
		StringBuffer tmpemailContent = new StringBuffer();
		StringBuffer emailSubject = new StringBuffer();
		emailSubject.append(resetPasswordEmailSubject);
		tmpemailContent.append(resetPasswordEmailContent);
		tmpemailContent.append(emailSignature);
		String emailContent = new String(tmpemailContent);
		emailContent = emailContent.replaceAll("setUserName", userName);
		emailContent = emailContent.replaceAll("setNewPassword", newPassword);		
		sendEmail(emailId, emailContent, emailSubject.toString());
	}

	/**
	 * Send email for new institute registration.
	 *
	 * @param instituteName the institute name
	 * @param instituteType the institute type
	 */
	public static void sendEmailForNewInstituteRegistration(String instituteName, String instituteType) 
	{
		StringBuffer emailSubject = new StringBuffer();
		
		emailSubject.append(instituteRegistrationEmailSubject);
		emailSubject.append(instituteName);
		
		String emailContent = new String(instituteRegistrationEmailContent);
		emailContent = emailContent.replaceAll("setInstituteName", instituteName);
		emailContent = emailContent.replaceAll("setInstituteType", instituteType);
		
		String[] tmpEmailIdsToSend = instituteApprovalTeamEmailIds.split(",");
		List<String> emailIdsToSend = new ArrayList<String>();
		for (String emailId : tmpEmailIdsToSend) 
		{
			emailIdsToSend.add(emailId);
		}
		
		sendEmail(emailIdsToSend, emailContent, emailSubject.toString());
		
	}

	/**
	 * Send email for new user registration.
	 *
	 * @param emailId the email id
	 */
	public static void sendEmailForNewUserRegistration(String emailId) 
	{
		StringBuffer emailContent = new StringBuffer();
		StringBuffer emailSubject = new StringBuffer();
		
		emailContent.append(userRegistrationEmailContent);
		emailContent.append(emailSignature);
		
		emailSubject.append(userRegistrationEmailSubject);
		
		sendEmail(emailId, emailContent.toString(), emailSubject.toString());
	}

	/**
	 * Send email to admin for new user registration.
	 *
	 * @param userName the user name
	 * @param userFName the user f name
	 * @param userLName the user l name
	 * @param userEmailId the user email id
	 */
	public static void sendEmailToAdminForNewUserRegistration(String userName, String userFName, String userLName, String userEmailId) 
	{
		StringBuffer emailSubject = new StringBuffer();
		
		emailSubject.append(userRegistrationEmailSubjectForAdmin);
		emailSubject.append(userFName);
		emailSubject.append(" ");
		emailSubject.append(userLName);
		
		String emailContent = new String(userRegistrationEmailContentForAdmin);
		emailContent = emailContent.replaceFirst("setUserName", userName);
		emailContent = emailContent.replaceFirst("setEmailId", userEmailId);
		
		//String emailId = new String(aviewAdminEmailId);
		
		sendEmail(userRegistrationApprovalTeamEmailIds, emailContent.toString(), emailSubject.toString());
	}

	/**
	 * Send email for approved user.
	 *
	 * @param emailId the email id
	 * @param userName the user name
	 */
	private static void sendEmailForApprovedUser(String emailId, String userName) 
	{
		StringBuffer tmpemailContent = new StringBuffer();
		StringBuffer emailSubject = new StringBuffer();
		
		emailSubject.append(userActivatedEmailSubject);
		
		tmpemailContent.append(userActivatedEmailContent);
		tmpemailContent.append(emailSignature);
		
		String emailContent = new String(tmpemailContent);
		
		emailContent = emailContent.replaceFirst("setUserName", userName);
		sendEmail(emailId, emailContent.toString(), emailSubject.toString());
	}

	/**
	 * Send email for approved users.
	 *
	 * @param users the users
	 */
	public static void sendEmailForApprovedUsers(List<User> users) 
	{
		for (User user : users) 
		{
			sendEmailForApprovedUser(user.getEmail(), user.getUserName());
		}
	}

	/**
	 * Send email for workshop registration.
	 *
	 * @param u the u
	 * @param workshop the workshop
	 */
	public static void sendEmailForWorkshopRegistration(User u, Class workshop) 
	{
		try
		{
			StringBuffer tmpemailContent = new StringBuffer();
			StringBuffer emailSubject = new StringBuffer();
			
			emailSubject.append(workshopRegistrationEmailSubject);
			emailSubject.append("\"");
			emailSubject.append(workshop.getClassName());
			emailSubject.append("\"");
			
			tmpemailContent.append(workshopRegistrationEmailContent);
			tmpemailContent.append(emailSignature);
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
			String workshopDate = simpleDateFormat.format(workshop.getEndDate());
			
			String emailContent = new String(tmpemailContent);
			emailContent = emailContent.replaceAll("setWorkshopName", workshop.getClassName());
			emailContent = emailContent.replaceFirst("setWorkshopDate", workshopDate);
			emailContent = emailContent.replaceFirst("setUserName", u.getUserName());
			String emailId = u.getEmail();
			sendEmail(emailId, emailContent, emailSubject.toString());
		}
		catch (Exception e) 
		{

		}
	}
	
	//Code change for NIC start
	
	private static String setClassDays(String weekDays)
	{
		String classDays = "";
		
		classDays += (weekDays.charAt(0) == 'Y' ? "Monday, " : "");
		classDays += (weekDays.charAt(1) == 'Y' ? "Tuesday, " : "");
		classDays += (weekDays.charAt(2) == 'Y' ? "Wednesday, " : "");
		classDays += (weekDays.charAt(3) == 'Y' ? "Thursday, " : "");
		classDays += (weekDays.charAt(4) == 'Y' ? "Friday, " : "");
		classDays += (weekDays.charAt(5) == 'Y' ? "Saturday, " : "");
		//Fix for Bug #10420 start
		classDays += (weekDays.charAt(6) == 'Y' ? "Sunday, " : "");
		//Fix for Bug #13704 start
		if(classDays.equals(""))
		{
			//This is a temporary fix.
			//TODO: Need to find a permanent solution
			classDays = "NA";
		}
		else
		{
			classDays = classDays.substring(0, classDays.length() - 2);
		}
		//Fix for Bug #13704 end
		//Fix for Bug #10420 end
		return classDays;
	}
	
	private static String setClassDetails(String tmpEmailContent, Class aviewClass, String meetingName)
	{
		String emailContent = "";
		try
		{
			//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
			//SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm a z");
			String classStartDate = dateFormatter.format(aviewClass.getStartDate());
			String classEndDate = dateFormatter.format(aviewClass.getEndDate());
			//Fix for bug #10254
			String classStartTime = timeinAmPM.format(aviewClass.getStartTime());
			String classEndTime = timeinAmPM.format(aviewClass.getEndTime());
			
			emailContent = tmpEmailContent;
			emailContent = emailContent.replaceAll("setClassName", meetingName);
			emailContent = emailContent.replaceFirst("setClassStartDate", classStartDate);
			emailContent = emailContent.replaceFirst("setClassEndDate", classEndDate);
			emailContent = emailContent.replaceAll("setClassDays", setClassDays(aviewClass.getWeekDays()));
			//Fix for bug #10254. Added start and end time for class
			emailContent = emailContent.replaceAll("setClassStartTime", classStartTime);
			emailContent = emailContent.replaceAll("setClassEndTime", classEndTime);
		}
		catch(Exception e)
		{
			logger.debug(e.getMessage());
		}
		return emailContent;
	}
	
	private static String setLectureDetails(String tmpEmailContent, Lecture lecture)
	{
		String emailContent = "";
		try
		{
			String classStartDate = dateFormatter.format(lecture.getStartDate());
			String classEndDate = dateFormatter.format(lecture.getStartDate());
			//Fix for bug #10254
			String classStartTime = timeinAmPM.format(lecture.getStartTime());
			String classEndTime = timeinAmPM.format(lecture.getEndTime());
			emailContent = tmpEmailContent;
			emailContent = emailContent.replaceFirst("setClassStartDate", classStartDate);
			emailContent = emailContent.replaceFirst("setClassEndDate", classEndDate);
			//This is a temporary fix.
			//TODO: Need to find a permanent solution
			emailContent = emailContent.replaceAll("setClassDays", "NA");
			emailContent = emailContent.replaceAll("setClassStartTime", classStartTime);
			emailContent = emailContent.replaceAll("setClassEndTime", classEndTime);
		}
		catch(Exception e)
		{
			logger.debug(e.getMessage());
		}
		return emailContent;
	}
	
	public static void sendEmailForClassScheduleChange(List<String> emailIds, Class aviewClass)
	{
		sendEmailForClassScheduleChange(emailIds, aviewClass, aviewClass.getClassName());
	}
	
	//Function to send email to all the registered participants for the given class
	//about the change in the class schedule if any.
	/**
	 * Send email for class schedule change.
	 *
	 * @param emailIds the email ids
	 * @param aviewClass the aview class
	 */
	public static void sendEmailForClassScheduleChange(List<String> emailIds, Class aviewClass, String meetingName)
	{
		try
		{
			StringBuffer tmpemailContent = new StringBuffer();
			StringBuffer emailSubject = new StringBuffer();
			emailSubject.append(classScheduleChangeEmailSubject);
			emailSubject.append("\"");
			emailSubject.append(meetingName);
			emailSubject.append("\"");
			tmpemailContent.append(classScheduleChangeEmailContent);
			tmpemailContent.append(emailSignature);
			String emailContent = setClassDetails(tmpemailContent.toString(), aviewClass, meetingName);
			sendEmail(emailIds, emailContent, emailSubject.toString());
		}
		catch (Exception e) 
		{

		}
	}
	//Code change for NIC end
		
	//Code change for NIC start
	//Function to send email to all the registered participants for the given class
	//about the change in the lecture schedule if any.		
	/**
	 * Send email for lecture schedule change.
	 *
	 * @param emailIds the email ids
	 * @param lecture the lecture
	 * @param aviewClass the aview class
	 */
	public static void sendEmailForLectureScheduleChange(List<String> emailIds, Lecture lecture, Class aviewClass)
	{
		try
		{
			StringBuffer tmpemailContent = new StringBuffer();
			StringBuffer emailSubject = new StringBuffer();
			
			String aviewLectureName = lecture.getLectureName();
			String aviewClassName = aviewClass.getClassName();
			
			emailSubject.append(lectureScheduleChangeEmailSubject);
			emailSubject.append("\"");
			emailSubject.append(aviewLectureName);
			emailSubject.append("\"");
			
			tmpemailContent.append(lectureScheduleChangeEmailContent);
			tmpemailContent.append(emailSignature);
			
			SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
			String lectureDate = simpleDateFormat.format(lecture.getStartDate());
			
			String lectureStartTime = timeFormat.format(lecture.getStartTime());
			String lectureEndTime = timeFormat.format(lecture.getEndTime());
							
			String emailContent = new String(tmpemailContent);
			emailContent = emailContent.replaceAll("setLectureName", aviewLectureName);
			emailContent = emailContent.replaceAll("setClassName", aviewClassName);
			emailContent = emailContent.replaceFirst("setLectureDate", lectureDate);
			emailContent = emailContent.replaceFirst("setLectureStartTime", lectureStartTime);
			emailContent = emailContent.replaceAll("setLectureEndTime", lectureEndTime);
			sendEmail(emailIds, emailContent, emailSubject.toString());
		}
		catch (Exception e) 
		{

		}
	}
	//Code change for NIC end
	
	//Code for sending meeting invitation
	private static String prepareEmailSubjectForMeeting(String meetingName)
	{
		StringBuffer emailSubject = new StringBuffer();
		emailSubject.append(meetingInvitationEmailSubject);
		emailSubject.append("\"");
		emailSubject.append(meetingName);
		emailSubject.append("\"");
		return emailSubject.toString();
	}
	
	private static String prepareEmailContentForMeeting(String meetingName, Object aviewClass, User moderator, boolean isGuestUser, boolean isScheduled)
	{
		String emailContent = "";
		try
		{
			StringBuffer tmpEmailContent = new StringBuffer();
			tmpEmailContent.append(meetingInvitationEmailContent);
			emailContent = new String(tmpEmailContent);
			emailContent = emailContent.replaceAll("setMeetingName", ("\"" + meetingName + "\""));
			emailContent = emailContent.replaceAll("setModeratorFirstName", moderator.getFname());
			emailContent = emailContent.replaceAll("setModeratorLastName", moderator.getLname());
			emailContent = emailContent.replaceAll("setModeratorInstitute", moderator.getInstituteName());
			String additionalInfo = ""; 
			if(isGuestUser)
			{
				//additionalMeetingInvitationEmailContentForGuestUser
				additionalInfo = additionalMeetingInvitationEmailContentForGuestUsers;
			}
			emailContent = emailContent.replaceAll("setAdditionalInfoForGuest", additionalInfo);
			additionalInfo = "";
			if(isScheduled)
			{
				if(aviewClass instanceof Class)
				{
					additionalInfo = setClassDetails(additionalScheduledMeetingInvitationEmailContent, (Class) aviewClass, meetingName);
				}
				else if(aviewClass instanceof Lecture)
				{
					additionalInfo = setLectureDetails(additionalScheduledMeetingInvitationEmailContent, (Lecture)aviewClass);
				}
			}
			emailContent = emailContent.replaceAll("setAdditionalInfoForScheduledMeeting", additionalInfo);
		}
		catch (Exception e) 
		{

		}
		return emailContent;
	}	

	//This function is called when new users are added during the 
	//update of single meeting
	public static void sendMeetingInvitation(Lecture lecture, List<User> users, User moderator, boolean isGuestUser)
	{
		String emailSubject = prepareEmailSubjectForMeeting(lecture.getDisplayName());
		String preFormattedEmailContent = prepareEmailContentForMeeting(lecture.getDisplayName(), lecture, moderator, isGuestUser, true);
		String emailContent = null;
		for(User user : users)
		{
			emailContent = new String(preFormattedEmailContent);
			//Fix for Bug #13494 end
			emailContent = emailContent.replaceAll("setUserName", user.getUserName());
			//User name is same as password in case of guest 
			emailContent = emailContent.replaceAll("setUserPassword", user.getUserName());
			emailContent = emailContent.replaceAll("setEncPassword", user.getPassword());
			//logger.debug("*************** " + emailContent);
			sendEmail(user.getEmail(), emailContent, emailSubject);
		}
	}
	
	public static void sendMeetingInvitation(String meetingName, Class aviewClass, List<User> users, User moderator, boolean isGuestUser, boolean isScheduled)
	{
		String emailSubject = prepareEmailSubjectForMeeting(meetingName);
		//Fix for Bug #13494 start
		String preFormattedEmailContent = prepareEmailContentForMeeting(meetingName, aviewClass, moderator, isGuestUser, isScheduled);
		String emailContent = null;
		for(User user : users)
		{
			emailContent = new String(preFormattedEmailContent);
			//Fix for Bug #13494 end
			emailContent = emailContent.replaceAll("setUserName", user.getUserName());
			//User name is same as password in case of guest 
			emailContent = emailContent.replaceAll("setUserPassword", user.getUserName());
			emailContent = emailContent.replaceAll("setEncPassword", user.getPassword());
			//logger.debug("*****************" + emailSubject);
			//logger.debug("*****************" + emailContent);
			sendEmail(user.getEmail(), emailContent, emailSubject);
		}
	}
	
	public static void notifyMembersOnMeetingRoomCancellation(List<User> users, String meetingName, User creator) throws AViewException
	{
		try
		{
			String emailSubject = new String();
			emailSubject = meetingRoomCancellationEmailSubject;
			String emailContent = new String(meetingRoomCancellationEmailContent);
			emailContent = emailContent.replaceAll("setMeetingName", meetingName);
			emailContent = emailContent.replaceAll("setModeratorName", creator.getUserName());
			emailContent = emailContent.replaceAll("setModeratorOrganizationName", creator.getInstituteName());
			//logger.debug("*****************" + emailSubject);
			//logger.debug("***************" + emailContent);
			for(User user : users)
			{
				sendEmail(user.getEmail(), emailContent, emailSubject.toString());	
			}
		}
		catch (Exception e) 
		{

		}
	}
	
	public static void notifyMembersOnMeetingCancellation(List<User> users, Lecture meeting, User creator)
	{
		try
		{
			String emailSubject = new String();
			emailSubject = meetingCancellationEmailSubject;
			emailSubject = emailSubject.replaceAll("setMeetingName", "\"" + meeting.getDisplayName() + "\"");
			String emailContent = new String(meetingCancellationEmailContent);
			//Fix for bug #13624 start
			emailContent = emailContent.replaceAll("setMeetingName", ("\"" + meeting.getDisplayName() + "\""));
			//Fix for bug #13624 end
			emailContent = emailContent.replaceAll("setModeratorName", creator.getFname() + " " + creator.getLname());
			String tmpDate = dateFormatter.format(meeting.getStartDate());
			emailContent = emailContent.replaceAll("setMeetingDate", tmpDate);
			String tmpTime = timeinAmPM.format(meeting.getStartTime());
			emailContent = emailContent.replaceAll("setMeetingTime", tmpTime);
			emailContent = emailContent.replaceAll("setModeratorOrganizationName", creator.getInstituteName());
			//logger.debug("*****************" + emailSubject);
			//logger.debug("***************" + emailContent);
			for(User user : users)
			{
				sendEmail(user.getEmail(), emailContent, emailSubject.toString());	
			}
		}
		catch (Exception e) 
		{

		}
	}
	
	public static void notifyMembersOnMeetingScheduleChange(List<User> users, Lecture oldMeeting, Lecture newMeeting, User creator)
	{
		String tmpDate = "";
		String tmpTime = "";
		try
		{
			StringBuffer tmpemailContent = new StringBuffer();
			StringBuffer emailSubject = new StringBuffer();
			emailSubject.append(meetingScheduleChangeEmailSubject);
			emailSubject.append("\"");
			emailSubject.append(newMeeting.getDisplayName());
			emailSubject.append("\"");
			tmpemailContent.append(meetingScheduleChangeEmailContent);
			String emailContent = new String(tmpemailContent);
			tmpDate = dateFormatter.format(oldMeeting.getStartDate());
			emailContent = emailContent.replaceAll("setMeeetingOldDate", tmpDate);
			tmpTime = timeinAmPM.format(oldMeeting.getStartTime());
			emailContent = emailContent.replaceAll("setMeetingOldStartTime", tmpTime);
			tmpTime = timeinAmPM.format(oldMeeting.getEndTime());
			emailContent = emailContent.replaceAll("setMeetingOldEndTime", tmpTime);
			tmpDate = dateFormatter.format(newMeeting.getStartDate());
			emailContent = emailContent.replaceAll("setMeetingNewDate", tmpDate);
			tmpTime = timeinAmPM.format(newMeeting.getStartTime());
			emailContent = emailContent.replaceAll("setMeetingNewStartTime", tmpTime);
			tmpTime = timeinAmPM.format(newMeeting.getEndTime());
			emailContent = emailContent.replaceAll("setMeetingNewEndTime", tmpTime);
			emailContent = emailContent.replaceAll("setMeetingName",  ("\"" + newMeeting.getDisplayName() + "\""));
			emailContent = emailContent.replaceAll("setModeratorName", creator.getFname() + " " + creator.getLname());
			emailContent = emailContent.replaceFirst("setModeratorOrganizationName", creator.getInstituteName());
			//logger.debug("*****************" + emailSubject);
			//logger.debug("***************" + emailContent);
			for(User user : users)
			{
				sendEmail(user.getEmail(), emailContent, emailSubject.toString());	
			}
		}
		catch (Exception e)
		{

		}
	}
	
	/**
	 * Send email.
	 *
	 * @param emailIds the email ids
	 * @param emailContent the email content
	 * @param emailSubject the email subject
	 */
	private static void sendEmail(List<String> emailIds, String emailContent, String emailSubject)
	{
		try
		{
			SimpleEmailSenderUtils.addEmailToQueue(emailIds, emailContent, emailSubject);
		}
		catch(Exception e)
		{

		}
	}
	
	/**
	 * Send email.
	 *
	 * @param emailId the email id
	 * @param emailContent the email content
	 * @param emailSubject the email subject
	 */
	private static void sendEmail(String emailId, String emailContent, String emailSubject)
	{
		try
		{
			SimpleEmailSenderUtils.addEmailToQueue(emailId, emailContent, emailSubject);
		}
		catch(Exception e)
		{
			
		}
	}

	/**
	 * This is used when user account is active from the time of registration itself.
	 * Added as a requirement of SYMBIOSIS.
	 * @param emailId
	 * @param userName
	 * @param instituteName
	 */
	public static void sendEmailForNewActiveUserRegistration(String emailId, String userName, String instituteName) 
	{
		StringBuffer tmpemailContent = new StringBuffer();
		StringBuffer emailSubject = new StringBuffer();
		
		emailSubject.append(activeUserRegistrationEmailSubject);
		
		tmpemailContent.append(activeUserRegistrationEmailContent);
		tmpemailContent.append(emailSignatureForAPI);
		
		String emailContent = new String(tmpemailContent);
		emailContent = emailContent.replaceAll("setUserName", userName);
		if(instituteName == null)
		{
			instituteName = defaultInstituteName;
		}
		emailContent = emailContent.replaceAll("setInstituteName", instituteName);
		sendEmail(emailId, emailContent, emailSubject.toString());
	}
	
	/**
	 * This is used when user registrated to lecture
	 * Added as a requirement of SYMBIOSIS. 
	 * @param emailIds
	 * @param lecture
	 * @param aviewClass
	 * @param instituteName
	 */
	public static void sendEmailForLectureCreation(List<String> emailIds, Lecture lecture, Class aviewClass, String instituteName)
	{
		try
		{
			StringBuffer tmpemailContent = new StringBuffer();
			StringBuffer emailSubject = new StringBuffer();
			
			String aviewLectureName = lecture.getLectureName();
			String aviewClassName = aviewClass.getClassName();
			
			emailSubject.append(lectureCreationEmailSubject);
			emailSubject.append("\"");
			emailSubject.append(aviewLectureName);
			emailSubject.append("\"");
			
			tmpemailContent.append(lectureCreationEmailContent);
			tmpemailContent.append(emailSignatureForAPI);
			
			SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
			String lectureDate = simpleDateFormat.format(lecture.getStartDate());
			
			String lectureStartTime = timeFormat.format(lecture.getStartTime());
			String lectureEndTime = timeFormat.format(lecture.getEndTime());
			
			String emailContent = new String(tmpemailContent);
			emailContent = emailContent.replaceAll("setLectureName", aviewLectureName);
			emailContent = emailContent.replaceAll("setClassName", aviewClassName);
			emailContent = emailContent.replaceFirst("setLectureDate", lectureDate);
			emailContent = emailContent.replaceFirst("setLectureStartTime", lectureStartTime);
			emailContent = emailContent.replaceAll("setLectureEndTime", lectureEndTime);
			if(instituteName == null)
			{
				instituteName = defaultInstituteName;
			}
			emailContent = emailContent.replaceAll("setInstituteName", instituteName);
			sendEmail(emailIds, emailContent, emailSubject.toString());
		}
		catch (Exception e) 
		{
			
		}
	}
	
	/**
	 * This is used when user account is active from the time of class registration itself.
	 * Added as a requirement of SYMBIOSIS. 
	 * @param emailId
	 * @param className
	 * @param instituteName
	 * @param courseName
	 * @param isClassRegistration
	 */
	public static void sendEmailForActiveClassRegistration(String emailId, String className, String instituteName, String courseName, boolean isClassRegistration) 
	{
		StringBuffer tmpemailContent = new StringBuffer();
		StringBuffer tmpemailSubject = new StringBuffer();
		
		String emailContent = new String();
		String emailSubject = new String();
		
		tmpemailSubject.append(activeClassRegistrationEmailSubject);
		tmpemailSubject.append("\"");
		tmpemailContent.append(activeClassRegistrationEmailContent);
		tmpemailContent.append(emailSignatureForAPI);
		emailContent = new String(tmpemailContent);
		
		if(isClassRegistration)
		{
			emailContent = emailContent.replaceAll("class","course");
			emailContent = emailContent.replaceAll("setClassName",courseName);
			emailSubject = new String(tmpemailSubject);
			emailSubject = emailSubject.replaceAll("Class", "Course");
			emailSubject += courseName + "\"";
		}
		else
		{
			emailContent = emailContent.replaceAll("setClassName",className);
			emailSubject = new String(tmpemailSubject);
			emailSubject += className + "\"";
		}
		if(instituteName == null)
		{
			instituteName = defaultInstituteName;
		}
		emailContent = emailContent.replaceAll("setInstituteName", instituteName);
		sendEmail(emailId, emailContent, emailSubject.toString());
	}
}
