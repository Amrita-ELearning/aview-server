/*
 * 
 */
package edu.amrita.aview.gclm.service;

import java.util.LinkedHashMap;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.gclm.entities.User;


/**
 * The Interface RegistrationService.
 *
 * @author Krishnakumar.R
 */
public interface RegistrationService {

	/**
	 * Register class.
	 *
	 * @param userId the user id
	 * @param classId the class id
	 * @return the string
	 */
	public String registerClass(Long userId, Long classId);

	/**
	 * List workshops.
	 *
	 * @param ascending the ascending
	 * @return the linked hash map<edu.amrita.aview.gclm.entities. class, integer>
	 */
	public LinkedHashMap<edu.amrita.aview.gclm.entities.Class,Integer> listWorkshops(boolean ascending);

	/**
	 * User login.
	 *
	 * @param userName the user name
	 * @param password the password
	 * @param ipAddress the ip address
	 * @param additionalInfo the additional info
	 * @return the user
	 * @throws AViewException
	 */
	public User userLogin(String userName, String password, String ipAddress, String additionalInfo) throws AViewException;

}
