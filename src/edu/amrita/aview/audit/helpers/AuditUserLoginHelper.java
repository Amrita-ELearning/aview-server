/*
 * 
 */
package edu.amrita.aview.audit.helpers;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;

import edu.amrita.aview.audit.daos.AuditUserLoginDAO;
import edu.amrita.aview.audit.entities.AuditUserLogin;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.TimestampUtils;
import flex.messaging.FlexContext;



/**
 * The Class AuditUserLoginHelper.
 */
public class AuditUserLoginHelper {

	/** The logger. */
	private static Logger logger = Logger.getLogger(AuditUserLoginHelper.class);
	
	/**
	 * Creates the user login.
	 *
	 * @param userLogin the user login
	 * @param creatorId the creator id
	 * @return the audit user login
	 * @throws AViewException
	 */
	public static AuditUserLogin createUserLogin(AuditUserLogin userLogin,Long creatorId) throws AViewException
	{
		userLogin.setExternalIPAddress(FlexContext.getHttpRequest().getRemoteAddr());
		userLogin.setLoginTime(TimestampUtils.getCurrentTimestamp());
		userLogin.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
		logger.info("UserLoginAudit:"+userLogin);
		AuditUserLoginDAO.createUserLogin(userLogin);
		return userLogin;
	}
	
	/**
	 * Update user login.
	 *
	 * @param userLogin the user login
	 * @param updatorId the updator id
	 * @return the audit user login
	 * @throws AViewException
	 */
	public static AuditUserLogin updateUserLogin(AuditUserLogin userLogin,Long updatorId) throws AViewException 
	{
		userLogin.setLogOutTime(TimestampUtils.getCurrentTimestamp());
		userLogin.setModifiedAuditData(updatorId, TimestampUtils.getCurrentTimestamp());
		AuditUserLoginDAO.updateUserLogin(userLogin);		
		return userLogin;
	}
	
	/**
	 * Gets the audit user login id.
	 *
	 * @param loginTime the login time
	 * @param userId the user id
	 * @return the audit user login id
	 * @throws AViewException
	 */
	public static AuditUserLogin getAuditUserLoginId(Date loginTime,int userId) throws AViewException{

		Timestamp loginTimeStamp = new Timestamp(loginTime.getTime());
		AuditUserLogin userLogin = AuditUserLoginDAO.getUserLogin(loginTimeStamp,userId);
		return userLogin;
	}

	/**
	 * Gets the user login count for lecture.
	 *
	 * @param lectureId the lecture id
	 * @return the user login count for lecture
	 * @throws AViewException
	 */
	public static Integer getUserLoginCountForLecture(Long lectureId) throws AViewException
	{
		return AuditUserLoginDAO.getUserLoginCountForLecture(lectureId);
	}
}
