/*
 * 
 */
package edu.amrita.aview.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.amrita.aview.audit.helpers.UserActionHelper;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.gclm.entities.User;
import edu.amrita.aview.gclm.helpers.ClassRegistrationHelper;
import edu.amrita.aview.gclm.service.RegistrationService;
import edu.amrita.aview.gclm.vo.WorkshopClassVO;


/**
 * The Class LoginController.
 *
 * @author Krishnakumar.R
 */
@Controller
public class LoginController {

	/** The registration service. */
	static Logger logger = Logger.getLogger(LoginController.class);
	
	/** The registration service. */
	@Autowired
	private RegistrationService registrationService;

	/**
	 * Do login.
	 *
	 * @param userDetails the user details
	 * @return the object
	 * @throws AViewException
	 */
	@RequestMapping(value = "/login.html", method = RequestMethod.POST, produces = "application/json", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object doLogin(@RequestBody HashMap<String, String> userDetails) throws AViewException 
	{
		//User user = UserHelper.getUserByUserNamePassword(userDetails.get("username"), userDetails.get("password"));
		User user = registrationService.userLogin(userDetails.get("username"), userDetails.get("password"),
												  userDetails.get("ipAddress"), userDetails.get("additionalInfo"));
		if (user == null) {
			return "failed";
		}
		return user;
	}

	/**
	 * Do register.
	 *
	 * @param userid the userid
	 * @param workshopid the workshopid
	 * @return the string
	 */
	@RequestMapping(value = "{userid}/{workshopid}/register.html", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String doRegister(@PathVariable("userid") String userid,
			@PathVariable("workshopid") String workshopid) {
		String result = new String();
		try {
			result = registrationService.registerClass(Long.parseLong(userid),
					Long.parseLong(workshopid));
		} catch (NumberFormatException nre) {
			result = "invalid";
		}
		return result;
	}

	/**
	 * List workshops.
	 *
	 * @return the map
	 */
	@RequestMapping(value = "/list.html", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map<String, List<WorkshopClassVO>> listWorkshops() {
		return populateData(registrationService.listWorkshops(true),false);
	}

	/**
	 * Find past workshops.
	 *
	 * @return the map
	 */
	@RequestMapping(value = "/viewPastWorkshops.html", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map<String, List<WorkshopClassVO>> findPastWorkshops() {
		return populateData(registrationService.listWorkshops(false),true);
	}
	
	/**
	 * Create UserAction For AVIEWDownload.
	 *
	 * @param userid the user who downloads
	 * @param osName the OS for which AVIEW is downloaded
	 * @return the string
	 */	
	@RequestMapping(value = "{userid}/{osName}/createUserActionForAVIEWDownload.html", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String createUserActionForAVIEWDownload(@PathVariable("userid") String userid,
			@PathVariable("osName") String osName) {
		String result = new String();
		try 
		{
			UserActionHelper.createUserActionForAVIEWDownload(Long.parseLong(userid), osName);
			result = "success";
		}
		catch (NumberFormatException nre) 
		{
			result = "invalid";
		}
		catch(AViewException ae)
		{
			result = "error during log";
		}
		return result;
	}

	/**
	 * Populate data.
	 *
	 * @param workshopList the workshop list
	 * @param toFindPastWorkshop the to find past workshop
	 * @return the map
	 */	
	private synchronized Map<String, List<WorkshopClassVO>> populateData(
			LinkedHashMap<edu.amrita.aview.gclm.entities.Class, Integer> workshopList, boolean toFindPastWorkshop) {
		List<WorkshopClassVO> workshopClassList = new ArrayList<WorkshopClassVO>();
		Calendar now = Calendar.getInstance(TimeZone.getTimeZone("IST"));
		now.set(Calendar.HOUR, 00);
		now.set(Calendar.AM_PM, Calendar.AM);
		now.clear(Calendar.MINUTE);
		now.clear(Calendar.SECOND);
		now.clear(Calendar.MILLISECOND);
		int classRegistrationCount = 0;
		int userAttendedCount = 0;
		edu.amrita.aview.gclm.entities.Class workshopClass = null;
		for(Map.Entry<edu.amrita.aview.gclm.entities.Class, Integer> workshopClassWithClassCount : workshopList.entrySet())
		{
			userAttendedCount = workshopClassWithClassCount.getValue();
			workshopClass = workshopClassWithClassCount.getKey();
			try
			{
				classRegistrationCount = ClassRegistrationHelper.getClassRegistrationCount(workshopClass.getClassId());
			}
			catch(AViewException aviewException)
			{
				logger.error(aviewException.getMessage());				
			}
			if ((workshopClass.getEndDate().compareTo(now.getTime()) >= 0)
					&& !toFindPastWorkshop) {
				workshopClassList.add(new WorkshopClassVO(workshopClass
						.getClassId(), workshopClass.getStartDate(), workshopClass.getEndDate(),
						workshopClass.getStartTime(), workshopClass
								.getEndTime(), workshopClass.getClassName(),
						workshopClass.getStatusId(), workshopClass.getClassDescription(), classRegistrationCount, userAttendedCount));
			} else if ((workshopClass.getEndDate().compareTo(now.getTime()) < 0)
					&& toFindPastWorkshop) {
				workshopClassList.add(new WorkshopClassVO(workshopClass
						.getClassId(), workshopClass.getStartDate(),workshopClass.getEndDate(),
						workshopClass.getStartTime(), workshopClass
								.getEndTime(), workshopClass.getClassName(),
						workshopClass.getStatusId(),workshopClass.getClassDescription(), classRegistrationCount, userAttendedCount));
			}			
		}
		Map<String, List<WorkshopClassVO>> workshops = new HashMap<String, List<WorkshopClassVO>>();
		workshops.put("workshops", workshopClassList);
		return workshops;
	}
}
