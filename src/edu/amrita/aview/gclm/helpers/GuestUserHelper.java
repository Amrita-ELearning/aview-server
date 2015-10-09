/*
 * 
 */
package edu.amrita.aview.gclm.helpers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.amrita.aview.audit.helpers.AuditLectureHelper;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.gclm.daos.UserDAO;
import edu.amrita.aview.gclm.entities.User;


/**
 * The Class GuestUserHelper.
 */
public class GuestUserHelper {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(GuestUserHelper.class);
	
	/** The global guests. */
	private static List<User> globalGuests = null;
	
	/** The lecture guests. */
	private static Map<Long,Set<Long>> lectureGuests = new HashMap<Long,Set<Long>>();
	
	/** The Constant GLOBAL_GUESTS_LOCK. */
	private final static String GLOBAL_GUESTS_LOCK = "GLOBAL_GUESTS_LOCK";
	
	/** The Constant LECTURE_GUESTS_LOCK. */
	private final static String LECTURE_GUESTS_LOCK = "LECTURE_GUESTS_LOCK";
	
	/**
	 * Populate global guest users.
	 */
	private static void populateGlobalGuestUsers()
	{
		try {
			logger.info("Started to populate Global guest users");
			globalGuests = UserDAO.getActiveGuestUsers(StatusHelper.getActiveStatusId());
			logger.info("Successfully populated Global guest users. Size:"+globalGuests.size());
		} catch (AViewException e) {
			logger.error("Exception while getting Global Guest Users", e);
		}
	}
	
	/**
	 * Populate lecture guest users.
	 *
	 * @param lectureId the lecture id
	 */
	private static void populateLectureGuestUsers(long lectureId)
	{
		List<Long> existingGuestUsers;
		try {
			logger.info("Started to populate Lecture guest users for lecture:"+lectureId);
			existingGuestUsers = AuditLectureHelper.getAllCurrentlyLoggedInGuestUsers(lectureId);
			logger.info("Successfully populated Lecture guest users for lecture:"+lectureId+", Size:"+existingGuestUsers.size());
			lectureGuests.get(lectureId).addAll(existingGuestUsers);
		} catch (Throwable e) {
			logger.error("Exception while getting Guest Users for lecture:"+lectureId, e);
		}
	}
	
	/**
	 * Check and populate global.
	 */
	private static void checkAndPopulateGlobal()
	{
		//If not loaded yet, prepopulate from user table
		if(globalGuests == null || globalGuests.size() == 0)
		{
			populateGlobalGuestUsers();
		}
	}
	
	/**
	 * Gets the global guest id iterator.
	 *
	 * @return the global guest id iterator
	 */
	private static List<User> getGlobalGuestIdIterator()
	{
		synchronized(GLOBAL_GUESTS_LOCK)
		{
			checkAndPopulateGlobal();
			return globalGuests;
		}
	}
	
	/**
	 * Gets the global guest count.
	 *
	 * @return the global guest count
	 */
	private static int getGlobalGuestCount()
	{
		synchronized(GLOBAL_GUESTS_LOCK)
		{
			checkAndPopulateGlobal();
			return globalGuests.size();
		}
	}
	
	/**
	 * Refresh guest users.
	 */
	public void refreshGuestUsers()
	{
		synchronized(GLOBAL_GUESTS_LOCK)
		{
			globalGuests.clear();
			checkAndPopulateGlobal();
		}
	}
	
	/**
	 * Gets the guest user for the class.
	 *
	 * @param lectureId the lecture id
	 * @return the guest user for the class
	 * @throws AViewException
	 */
	public static User getGuestUserForTheClass(long lectureId) throws AViewException
	{
//		logger.info("1.getGuestUserForTheClass lectureId:"+lectureId);
		Set<Long> lectureUserIds = null;
		//If not loaded yet, prepopulate from auditlecture table
		if(!lectureGuests.containsKey(lectureId))
		{
			//Use the generic lock and create a lecture specific lock
//			logger.info("2.getGuestUserForTheClass lectureId:"+lectureId);
			synchronized(LECTURE_GUESTS_LOCK)
			{
				//Using double checked locking
//				logger.info("3.getGuestUserForTheClass lectureId:"+lectureId);
				if(!lectureGuests.containsKey(lectureId))
				{
//					logger.info("4.getGuestUserForTheClass lectureId:"+lectureId);
					lectureGuests.put(lectureId, new HashSet<Long>());
				}
			}
			//Use the lecture specfic lock to make sure that two threads do not pre-populate for the same lecture id
//			logger.info("5.getGuestUserForTheClass lectureId:"+lectureId);
			synchronized(lectureGuests.get(lectureId))
			{
				if(lectureGuests.get(lectureId).size() == 0)
				{
					populateLectureGuestUsers(lectureId);
				}
			}
		}
		
		if((lectureUserIds = lectureGuests.get(lectureId)) != null )
		{
			synchronized(lectureUserIds)
			{
				//Housefull
				if(getGlobalGuestCount() == lectureUserIds.size())
				{
					return null;
				}
				List<User> globalIds = getGlobalGuestIdIterator();
				for(User currentUser:globalIds)
				{
					if(!lectureUserIds.contains(currentUser.getUserId()))
					{
						lectureUserIds.add(currentUser.getUserId());
						return currentUser;
					}
				}
			}
		}
		return null;
	}

}
