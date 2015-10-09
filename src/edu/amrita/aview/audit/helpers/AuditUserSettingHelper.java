/*
 * 
 */
package edu.amrita.aview.audit.helpers;



import edu.amrita.aview.audit.daos.AuditUserSettingDAO;
import edu.amrita.aview.audit.entities.AuditUserSetting;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.TimestampUtils;


/**
 * The Class AuditUserSettingHelper.
 */
public class AuditUserSettingHelper {
	
	/**
	 * Creates the user setting.
	 *
	 * @param userSetting the user setting
	 * @param creatorId the creator id
	 * @return the audit user setting
	 * @throws AViewException
	 */
	public static AuditUserSetting createUserSetting(AuditUserSetting userSetting,Long creatorId) throws AViewException{
		userSetting.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
		AuditUserSettingDAO.createUserSetting(userSetting);		
		return userSetting;
	}

}
