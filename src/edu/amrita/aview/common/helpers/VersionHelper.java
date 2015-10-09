/*
 * 
 */
package edu.amrita.aview.common.helpers;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.VersionDAO;


/**
 * The Class VersionHelper.
 */
public class VersionHelper {

	/**
	 * Gets the statuses.
	 *
	 * @return the statuses
	 * @throws AViewException
	 */
	public static boolean checkClientServerCompatibility(String clientVersion) throws AViewException
	{
		return (VersionDAO.isClientServerCompatible(clientVersion));
	}

	
}
