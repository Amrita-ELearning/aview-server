/*
 * 
 */
package edu.amrita.aview.audit.helpers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.amrita.aview.audit.daos.UserActionDAO;
import edu.amrita.aview.audit.entities.UserAction;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.gclm.helpers.UserHelper;


/**
 * The Class UserActionHelper.
 */
public class UserActionHelper implements Runnable{

	/** The logger. */
	private static Logger logger = Logger.getLogger(UserActionHelper.class);
	
	/** The user actions. */
	private static List<UserAction> userActions = new ArrayList<UserAction>();
	
	/** The Constant INSERT_LIST_SIZE. */
	private static final int INSERT_LIST_SIZE = HibernateUtils.HIBERNATE_BATCH_SIZE;
	
	/** The Constant WAIT_MS. */
	private static final long WAIT_MS = 2*60*1000;
	
	/** The stop flag. */
	private static boolean stopFlag = false;
	
	static
	{
		UserActionHelper helper = new UserActionHelper();
		Thread insertThread = new Thread(helper);
		insertThread.start();
		logger.info("UserActionHelper thread Started");
	}
	
	/**
	 * Creates the user action for aview download.
	 *
	 * @param creatorId the creator id
	 * @param os the os
	 * @throws AViewException
	 */
	public static void createUserActionForAVIEWDownload(Long creatorId, String os) throws AViewException
	{
		logger.info("AVIEW Download action " + creatorId + " and os: " + os);
		UserAction userAction = new UserAction();
		userAction.setActionId(ActionHelper.getActionId(Constant.DOWNLOAD_AVIEW_ACTION));
		userAction.setAuditUserLoginId(null);
		userAction.setLectureId(null);
		userAction.setAuditLectureId(null);
		userAction.setAttribute1Value(os);
		//createUserAction(userAction, creatorId);
		//Directly saving the user action details
		userAction.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
		UserActionDAO.createDownloadUserAction(userAction);
		logger.info("AVIEW Download action by " + creatorId + " for os: " + os + " completed successfully");
	}
	
	/**
	 * Gets the future license validation count.
	 *
	 * @param licValId the lic val id
	 * @return the future license validation count
	 * @throws AViewException
	 */
	private static int getFutureLicenseValidationCount(int licValId) throws AViewException
	{
		return UserActionDAO.getFutureLicenseValidationCount(TimestampUtils.getCurrentTimestamp(),licValId);
	}
	
	/**
	 * Record license validations.
	 *
	 * @param adminUserId the admin user id
	 * @param productKey the product key
	 * @param isLicenseValid the is license valid
	 * @param details the details
	 * @return the string
	 * @throws AViewException
	 */
	public static String recordLicenseValidations(Long adminUserId,String productKey,Boolean isLicenseValid,String details) throws AViewException
	{
		String errorMsg = null;
		int licValId = ActionHelper.getActionId(Constant.VALLIC_AVIEW_ACTION);
		int licValCount = getFutureLicenseValidationCount(licValId);
		if(licValCount == 0)
		{
			UserAction userAction = new UserAction();
			userAction.setActionId(licValId);
			userAction.setAuditUserLoginId(null);
			userAction.setLectureId(null);
			userAction.setAuditLectureId(null);
			userAction.setAttribute1Value(productKey);
			userAction.setAttribute2Value(isLicenseValid+"");
			userAction.setAttribute3Value(details);
			userAction.setCreatedAuditData(adminUserId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
			userActions.add(userAction);
		}
		else
		{
			errorMsg = "Found :"+licValCount+": future license validations from the current time :"+TimestampUtils.getCurrentTimestamp();
		}
		return errorMsg;
	}
	
	/**
	 * Creates the user action for login attempt failed.
	 *
	 * @param ipAddress the IP Address from which user tries to login
	 * @param connectingApp the application through which the user tries to connect
	 * @param additionalInfo additional information like browser info, if login is through browser
	 * @param creatorId the user id who tries to login
	 * @throws AViewException
	 */
	public static void createUserActionForFailedLoginAttempt(String ipAddress, String connectingApp, String additionalInfo, Long creatorId) throws AViewException
	{
		logger.info("Login attempt with wrong password for UserId: " + creatorId + ", from: " + connectingApp + " with ip: " + ipAddress );
		UserAction userAction = new UserAction();
		userAction.setActionId(ActionHelper.getActionId(Constant.FAILED_LOGIN_ATTEMPT));
		userAction.setAuditUserLoginId(null);
		userAction.setLectureId(null);
		userAction.setAuditLectureId(null);
		if(ipAddress != null)
		{
			userAction.setAttribute1Value("From IP Address: " + ipAddress);
		}
		userAction.setAttribute2Value(connectingApp);
		userAction.setAttribute3Value(additionalInfo);
		//createUserAction(userAction, creatorId);
		//Directly saving the user action details
		if(creatorId == null)
		{
			creatorId = UserHelper.getUserByUserName(Constant.MASTER_ADMIN_USER_NAME).getUserId();
		}
		userAction.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
		userActions.add(userAction);
	}
	
	/**
	 * Creates the user action for login attempt failed because of invalid user name.
	 *
	 * @param userName the user name with which the login attempt was made
	 * @param ipAddress the IP Address from which user tries to login
	 * @param connectingApp the application through which the user tries to connect
	 * @param additionalInfo additional information like browser info, if login is through browser
	 * @throws AViewException
	 */
	public static void createUserActionForFailedLoginAttemptWrongUserName(String userName, String ipAddress, String connectingApp, String additionalInfo) throws AViewException
	{
		logger.info("Login attempt with wrong user name. UserName: " + userName + ", from: " + connectingApp + " with ip: " + ipAddress );
		UserAction userAction = new UserAction();
		userAction.setActionId(ActionHelper.getActionId(Constant.FAILED_LOGIN_ATTEMPT_WRONG_USERNAME_ACTION));
		userAction.setAuditUserLoginId(null);
		userAction.setLectureId(null);
		userAction.setAuditLectureId(null);
		if(ipAddress != null)
		{
			userAction.setAttribute1Value("From IP Address: " + ipAddress);
		}
		userAction.setAttribute2Value("User name entered: " + userName);
		userAction.setAttribute3Value("Connecting App: " + connectingApp + " " + additionalInfo);
		//Directly saving the user action details
		Long creatorId = UserHelper.getUserByUserName(Constant.MASTER_ADMIN_USER_NAME).getUserId();	
		userAction.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
		userActions.add(userAction);
	}
	
	/**
	 * Creates the user action for reset password.
	 *
	 * @param attr1Value the attr1 value
	 * @param creatorId the creator id
	 * @param status the status
	 * @throws AViewException
	 */
	public static void createUserActionForResetPassword(String attr1Value, Long creatorId, String status) throws AViewException
	{
		logger.info("Password reset request " + creatorId);
		UserAction userAction = new UserAction();
		userAction.setActionId(ActionHelper.getActionId(Constant.RESET_PASSWORD_ACTION));
		userAction.setAuditUserLoginId(null);
		userAction.setLectureId(null);
		userAction.setAuditLectureId(null);
		userAction.setAttribute1Value(attr1Value);
		userAction.setAttribute2Value(status);
		//createUserAction(userAction, creatorId);
		//Directly saving the user action details
		userAction.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
		userActions.add(userAction);
		logger.info("Password reset request by " + creatorId + " completed successfully");
	}
	
	/**
	 * Creates the user action.
	 *
	 * @param userAction the user action
	 * @param creatorId the creator id
	 * @throws AViewException
	 */
	public static synchronized void createUserAction(UserAction userAction,Long creatorId) throws AViewException
	{
		userAction.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
		userActions.add(userAction);
	}
	
	/**
	 * Creates the user actions.
	 *
	 * @param userActionsToBeInserted the user actions to be inserted
	 * @param creatorId the creator id
	 * @param callTimeMS the call time ms
	 * @throws AViewException
	 */
	public static void createUserActions(List<UserAction> userActionsToBeInserted,Long creatorId,Long callTimeMS) throws AViewException
	{
		if(creatorId != null)
		{
			for(UserAction userAction:userActionsToBeInserted)
			{
				if(userAction.getLectureId() == 0l)
				{
					userAction.setLectureId(null);
				}
				if(userAction.getAuditLectureId() == 0l)
				{
					userAction.setAuditLectureId(null);
				}
				userAction.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(userAction.getActionTimeMS()-callTimeMS), StatusHelper.getActiveStatusId());
			}
			userActions.addAll(userActionsToBeInserted);
		}
	}
	
	/**
	 * Gets the user actions for insert.
	 *
	 * @return the user actions for insert
	 * @throws AViewException
	 */
	private static synchronized List<UserAction> getUserActionsForInsert() throws AViewException
	{
		List<UserAction> returnList = new ArrayList<UserAction>();
		returnList.addAll(userActions);
		userActions.clear();
		return returnList;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		long lastInserTime = System.currentTimeMillis(); //Default initialization
		while(!stopFlag)
		{
			//Insert if the size is more than batch size or if the events are waited enough time
			if(userActions.size() > INSERT_LIST_SIZE || ((System.currentTimeMillis() - lastInserTime) > WAIT_MS && userActions.size() > 0))
			{
				try {
					List<UserAction> insertList = getUserActionsForInsert();
					UserActionDAO.createUserActions(insertList);
					logger.info("Inserting :"+insertList.size()+": UserActions into db");
					insertList.clear();
					insertList = null;
					lastInserTime = System.currentTimeMillis();
				} catch (AViewException e) {
					logger.error("Error while inserting UserActions into db", e);
				}
			}
			try
			{
				Thread.sleep(5000); //Wait for 5 seconds before checking again 
			}catch(InterruptedException ignore){};
			
			//Update Last Action Date once a day, right after mid night..for last 3 days..
			updateLastActionDate();
		}
	}
	
	private long nextRunTimeMS_LastActionDate = -1;
	public void updateLastActionDate()
	{
		try {
			if(System.currentTimeMillis() > nextRunTimeMS_LastActionDate)
			{
//				Map<Long,Timestamp> auditLectureMap = UserActionDAO.getLastActionTimesForLast3Days();
//				Set<Long> keySet = auditLectureMap.keySet();
//				for(Long auditLectureId:keySet)
//				{
//					AuditLectureHelper.updateLastActionDate(auditLectureId, auditLectureMap.get(auditLectureId));
//				}
				UserActionDAO.updateLastActionTimesForLast3Days();
				GregorianCalendar gc = new GregorianCalendar();
				gc.set(GregorianCalendar.DAY_OF_MONTH, gc.get(GregorianCalendar.DAY_OF_MONTH)+1);
				Date nextDayMidnight = TimestampUtils.removeTime(gc.getTime());
				nextRunTimeMS_LastActionDate = nextDayMidnight.getTime();
			}
		} catch (AViewException e) {
			// TODO Auto-generated catch block
			logger.error("Exception while updating last action time", e);
		}
	}
	
	
	/**
	 * Stop thread.
	 *
	 * @throws AViewException
	 */
	public static void stopThread() throws AViewException
	{
		stopFlag = true;
		if(userActions.size() > 0)
		{
			List<UserAction> insertList = getUserActionsForInsert();
			UserActionDAO.createUserActions(insertList);
			logger.debug("Inserting :"+insertList.size()+": UserActions into db before undeploying the Aview App********");
		}
		clearCache();
	}
	
	/**
	 * Clear cache.
	 */
	public static void clearCache()
	{
		logger.debug("Entered UserActionHelper.clearCache()");
		userActions = null;
		logger.debug("Exit UserActionHelper.clearCache()");
	}
}
