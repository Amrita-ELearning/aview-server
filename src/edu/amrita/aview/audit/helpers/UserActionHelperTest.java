/*
 * 
 */
package edu.amrita.aview.audit.helpers;

import org.junit.Ignore;
import org.junit.Test;


import edu.amrita.aview.audit.daos.UserActionDAO;
import edu.amrita.aview.common.AViewException;


/**
 * The Class UserActionHelperTest.
 */
public class UserActionHelperTest{
	
	/**
	 * Record license validations.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void recordLicenseValidations() throws AViewException
	{
		String errorMessage = UserActionHelper.recordLicenseValidations(554l, "ABCD", true, "");
		System.out.println(errorMessage);
		
	}
	@Test
	public void lastActionDateTest() throws AViewException
	{
		UserActionDAO.updateLastActionTimesForLast3Days();
	}
}
