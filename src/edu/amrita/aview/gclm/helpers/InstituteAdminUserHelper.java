/*
 * 
 */
package edu.amrita.aview.gclm.helpers;


import java.util.List;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.gclm.daos.InstituteAdminUserDAO;
import edu.amrita.aview.gclm.entities.InstituteAdminUser;


/**
 * The Class InstituteAdminUserHelper.
 */
public class InstituteAdminUserHelper {
	
	/**
	 * Creates the institute admin user.
	 *
	 * @param instituteAdminUser the institute admin user
	 * @param creatorId the creator id
	 * @throws AViewException
	 */
	public static void createInstituteAdminUser(InstituteAdminUser instituteAdminUser,Long creatorId) throws AViewException{			
		instituteAdminUser.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
		InstituteAdminUserDAO.createInstituteAdminUser(instituteAdminUser);
	}
	
	/**
	 * Update institute admin user.
	 *
	 * @param instituteAdminUser the institute admin user
	 * @param updaterid the updaterid
	 * @throws AViewException
	 */
	public static void updateInstituteAdminUser(InstituteAdminUser instituteAdminUser,Long updaterid) throws AViewException{
		instituteAdminUser.setModifiedAuditData(updaterid, TimestampUtils.getCurrentTimestamp());
		InstituteAdminUserDAO.updateInstituteAdminUser(instituteAdminUser);
	}
	
	/**
	 * Gets the institute admin users.
	 *
	 * @return the institute admin users
	 * @throws AViewException
	 */
	public static List<InstituteAdminUser> getInstituteAdminUsers() throws AViewException{
		List<InstituteAdminUser> instituteAdminUsers = InstituteAdminUserDAO.getInstituteAdminUsers();		
		return instituteAdminUsers;
	}

	/**
	 * Gets the institute admin user.
	 *
	 * @param instituteAdminId the institute admin id
	 * @return the institute admin user
	 * @throws AViewException
	 */
	public static InstituteAdminUser getInstituteAdminUser(Long instituteAdminId) throws AViewException{
		InstituteAdminUser instituteAdminUser = InstituteAdminUserDAO.getInstituteAdminUser(instituteAdminId);		
		return instituteAdminUser;
	}

}
