/*
 * @ (#)QuizConstant.java 4.0 2013/10/04
 * 
 * Copyright  © 2013 E-Learning Research Lab, 
 * Amrita Vishwa Vidyapeetham. All rights reserved. 
 * E-Learning Research Lab and the A-VIEW logo are trademarks or
 * registered trademarks of E-Learning Research Lab. 
 * All other names used are the trademarks of their respective owners.
 */
package edu.amrita.aview.evaluation ;


/*
 * This class consists of all constants used in Quiz module 
 * 
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
/**
 * The Class QuizConstant.
 */
public class QuizConstant {

	/** The Constant QUIZSTATUS. */
	public static final String[] QUIZSTATUS = {"Ready","Active","Completed "} ;
	
	/** The Constant ONLINE_QUIZ_TYPE. */
	public static final String ONLINE_QUIZ_TYPE = "Online"  ;
	
	/** The Constant LIVE_QUIZ_TYPE. */
	public static final String LIVE_QUIZ_TYPE = "Live";
	
	/** The Constant MOBILE_SMS_SPLIT_DELIMETER. */
	public static final String MOBILE_SMS_SPLIT_DELIMETER="&";
	
	/** The Constant MOBILE_SMS_INDEX_SPLIT_DELIMETER. */
	public static final String MOBILE_SMS_INDEX_SPLIT_DELIMETER="#";
	
	/** The Constant RESPONSE_FORMAT_ERROR_MESSAGE. */
	public static final String RESPONSE_FORMAT_ERROR_MESSAGE="Please send the response in correct/specifed format";
	
	/** The Constant ALREADY_RESPONSE_ERROR_MESSAGE. */
	public static final String ALREADY_RESPONSE_ERROR_MESSAGE="Sorry, you already answered this quiz";
	
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
	
	/** The Constant MULTIPLE_CHOICE_QUESTION_TYPE. */
	public static final String MULTIPLE_CHOICE_QUESTION_TYPE = "Multiple Choice";
	
	/** The Constant YES. */
	public static final String YES = "Y" ;
	
	/** The Constant NO. */
	public static final String NO = "N" ;
	
	/** The Constant POLLING_PAPER_NAME. */
	public static final String POLLING_PAPER_NAME = "Pollingquestion_" ;
	
	/** E - Day in week ; a - am/pm marker. */
	public static final String POLLING_DATE_FORMAT = "E yyyy.MM.dd '_at_' hh:mm:ss a" ;
}
