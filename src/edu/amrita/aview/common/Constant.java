/*
 * 
 */
package edu.amrita.aview.common;

import java.util.Arrays;
import java.util.List;


/**
 * The Class Constant.
 */
public class Constant {

	/** The Constant DELIMETER. */
	public static final String DELIMETER = " | ";
	
	/** The Constant ACTIVESTATUS. */
	public static final String ACTIVESTATUS = "Active";
	
	/** The Constant DELETESTATUS. */
	public static final String DELETESTATUS = "Deleted" ;
	
	/** The Constant CLOSEDSTATUS. */
	public static final String CLOSEDSTATUS = "Closed" ;
	
	/** The Constant APPROVESTATUS. */
	public static final String APPROVESTATUS = "Approved";
	
	/** The Constant PENDINGSTATUS. */
	public static final String PENDINGSTATUS = "Pending" ;
	
	/** The Constant CLOSED_STATUS. */
	public static final String CLOSED_STATUS = "Closed";
	
	/** The Constant COMMUNICATING_STATUS. */
	public static final String COMMUNICATING_STATUS = "Communicating";
	
	/** The Constant TESTING_STATUS. */
	public static final String TESTING_STATUS = "Testing";
	
	/** The Constant FAILEDTESTING_STATUS. */
	public static final String FAILEDTESTING_STATUS = "FailedTesting";
	
	/** The Constant INACTIVE_STATUS. */
	public static final String INACTIVE_STATUS = "InActive";
	
	/** The Constant JOINED_STATUS. */
	public static final String JOINED_STATUS = "Joined";
	
	/** The Constant EXITED_STATUS. */
	public static final String EXITED_STATUS = "Exited";
	
	/** The Constant QUIZSTATUS. */
	public static final String QUIZSTATUS = "Ready"+","+"Active"+","+"Completed " ;
	
	/** The Constant TEACHER_ROLE. */
	public static final String TEACHER_ROLE = "TEACHER" ;
	
	/** The Constant STUDENT_ROLE. */
	public static final String STUDENT_ROLE = "STUDENT" ;
	
	/** The Constant ADMIN_ROLE. */
	public static final String ADMIN_ROLE = "ADMINISTRATOR" ;
	
	/** The Constant GUEST_ROLE. */
	public static final String GUEST_ROLE = "GUEST" ;
	
	/** The Constant MONITOR_ROLE. */
	public static final String MONITOR_ROLE = "MONITOR" ;
	
	/** The Constant OPEN_CLASS_REGISTRATION. */
	public static final String OPEN_CLASS_REGISTRATION = "Open" ;
	
	/** The Constant OPEN_WITH_LOGIN_CLASS_REGISTRATION. */
	public static final String OPEN_WITH_LOGIN_CLASS_REGISTRATION = "OpenWithLogin" ;
	
	/** The Constant APPROVAL_CLASS_REGISTRATION. */
	public static final String APPROVAL_CLASS_REGISTRATION = "Approval" ;
	
	/** The Constant NO_APPROVAL_CLASS_REGISTRATION. */
	public static final String NO_APPROVAL_CLASS_REGISTRATION = "NoApproval" ;
	
	/** The Constant MASTER_ADMIN_ROLE. */
	public static final String MASTER_ADMIN_ROLE = "MASTER_ADMIN" ;
	
	/** The Constant WEBINAR_ADMIN. */
	public static final String WEBINAR_ADMIN = "WEBINARADMIN";
	
	/** The Constant LIVE_QUIZ_MODE. */
	public static final String LIVE_QUIZ_MODE = "Live";
	
	/** The Constant PERSONAL_COMPUTER_NODETYPE. */
	public static final String PERSONAL_COMPUTER_NODETYPE = "PERSONAL COMPUTER";
	
	/** The Constant CLASSROOM_COMPUTER_NODETYPE. */
	public static final String CLASSROOM_COMPUTER_NODETYPE = "CLASSROOM COMPUTER";
	
	/** The Constant MOBILE_SMS_SPLIT_DELIMETER. */
	public static final String MOBILE_SMS_SPLIT_DELIMETER="&";
	
	/** The Constant MOBILE_SMS_INDEX_SPLIT_DELIMETER. */
	public static final String MOBILE_SMS_INDEX_SPLIT_DELIMETER="#";
	
	/** The Constant RESPONSE_FORMAT_ERROR_MESSAGE. */
	public static final String RESPONSE_FORMAT_ERROR_MESSAGE="Please send the response in correct/specifed format";
	
	/** The Constant ALREAY_RESPONSE_ERROR_MESSAGE. */
	public static final String ALREAY_RESPONSE_ERROR_MESSAGE="Sorry, you already answered this quiz";
	
	/** The Constant NOT_RESPONSE_ERROR_MESSAGE. */
	public static final String NOT_RESPONSE_ERROR_MESSAGE="Sorry, you haven't answered any of the questions";
	
	/** The Constant STUDENT_DETAILS_NOT_AVAILABLE_ERROR_MESSAGE. */
	public static final String STUDENT_DETAILS_NOT_AVAILABLE_ERROR_MESSAGE="Sorry, your details are not available, so please register in Aview";
	
	/** The Constant SPECIFIC_PATTERN_TYPE. */
	public static final String SPECIFIC_PATTERN_TYPE = "Specific";
	
	/** The Constant RANDOM_PATTERN_TYPE. */
	public static final String RANDOM_PATTERN_TYPE = "Random";
	
	/** The Constant POLLING_QUESTION_TYPE. */
	public static final String POLLING_QUESTION_TYPE = "Polling" ;
	
	/** The Constant EASY_DIFFICULTY_LEVEL. */
	public static final String EASY_DIFFICULTY_LEVEL = "Easy" ;
	
	
	/** List the file format to upload */
	public static final List<String> docExtensions = Arrays.asList(new String[]{ "pdf", "ppt", "jpg", "doc", "docx", "pptx",
		"xlsx", "bmp", "gif", "xls", "txt" });
	
	
	/** The Constant PENDING_CONVERSION_STATUS. */
	public static final String PENDING_CONVERSION_STATUS = "Pending" ;
	
	/** The Constant CONVERTING_CONVERSION_STATUS. */
	public static final String CONVERTING_CONVERSION_STATUS = "Converting" ;
	
	/** The Constant CONVERTED_CONVERSION_STATUS. */
	public static final String CONVERTED_CONVERSION_STATUS = "Converted" ;
	
	/** The Constant FAILED_CONVERSION_STATUS. */
	public static final String FAILED_CONVERSION_STATUS = "Failed" ;
	
	
	/** The Constant MULTIPLE_CHOICE_QUESTION_TYPE. */
	public static final String MULTIPLE_CHOICE_QUESTION_TYPE = "Multiple Choice";
	
	/** The Constant LOGIN_PASSWORD_LENGTH. */
	public static final int LOGIN_PASSWORD_LENGTH = 8;
	
	/** The Constant DOWNLOAD_AVIEW_ACTION. */
	public static final String DOWNLOAD_AVIEW_ACTION = "Download AVIEW";
	
	/** The Constant VALLIC_AVIEW_ACTION. */
	public static final String VALLIC_AVIEW_ACTION = "VALLIC";

	/** The Constant RESET_PASSWORD_ACTION. */
	public static final String RESET_PASSWORD_ACTION = "Reset Password";
	
	/** The Constant SUCCESS. */
	public static final String SUCCESS = "Success";
	
	/** The Constant FAILED. */
	public static final String FAILED = "Failed";
	
	/** The Constant SCHEDULED_CLASS_TYPE. */
	public static final String SCHEDULED_CLASS_TYPE = "Scheduled";
	
	/** The Constant ADHOC_CLASS_TYPE. */
	public static final String ADHOC_CLASS_TYPE = "Adhoc";
	public static final String MEETING_CLASS_TYPE = "Meeting";
	
	/** The Constant AVIEW_WORKSHOP_INSTITUTE_NAME. */
	public static final String AVIEW_WORKSHOP_INSTITUTE_NAME = "Amrita E-Learning Research Lab";
	
	/** The Constant AVIEW_WORKSHOP_COURSE_NAME. */
	public static final String AVIEW_WORKSHOP_COURSE_NAME = "National AVIEW Workshop";
	
	/** The Constant FAILED_LOGIN_ATTEMPT_WRONG_USERNAME_ACTION. */
	public static final String FAILED_LOGIN_ATTEMPT_WRONG_USERNAME_ACTION = "FailedLoginAttemptWrongUserName";
	
	/** The Constant FAILED_LOGIN_ATTEMPT. */
	public static final String FAILED_LOGIN_ATTEMPT = "FailedLoginAttempt";
	
	/** The Constant MASTER_ADMIN_USER_NAME. */
	public static final String MASTER_ADMIN_USER_NAME = "administrator";
	
	//added for getting the allowed characters for naming, in the UI.
	/** The Constant ALLOWED_CHARACTERS_FOR_NAMING. */
	public static final String ALLOWED_CHARACTERS_FOR_NAMING = "AllowedCharactersForName";
	
	// The type of creation types while creating user
	public static final String CREATED_FROM_WEB = "Web" ;
	public static final String CREATED_FROM_MEETING = "Meeting" ;

	public static final String USER_ID = "userId" ;
	public static final String ADMIN_ID = "adminId" ;
	// The default value for audio video interaction mode
	public static final String DEFAULT_AUDIO_VIDEO_INTERACTION_MODE = "Full";
	public static final String DEFAULT_CAN_MONITOR = "No";
	public static final String STATUS_NO = "No";
	public static final String STATUS_YES = "Yes";

}
