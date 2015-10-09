/*
 * 
 */
package edu.amrita.aview.common.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.daos.StatusDAO;
import edu.amrita.aview.common.entities.Status;
import edu.amrita.aview.gclm.helpers.CacheHelper;


/**
 * The Class StatusHelper.
 */
public class StatusHelper {

	/** The logger. */
	private static Logger logger = Logger.getLogger(StatusHelper.class);
	
	/** The status map. */
	private static Map<String,Integer> statusMap = Collections.synchronizedMap(new HashMap<String, Integer>());
	
	/** The all statuses map. */
	private static Map<Integer,Status> allStatusesMap = Collections.synchronizedMap(new HashMap<Integer, Status>());
	
	/** The Constant CACHE_CODE. */
	private static final String CACHE_CODE = "StatusHelper";
	
	/**
	 * Populate cache.
	 *
	 * @param statuses the statuses
	 */
	private static synchronized void populateCache(List<Status> statuses)
	{
		statusMap.clear();
		allStatusesMap.clear();
		for(Status status:statuses)
		{
			allStatusesMap.put(status.getStatusId(), status);
			statusMap.put(status.getStatusName(), status.getStatusId());
		}
		CacheHelper.setCache(CACHE_CODE, CacheHelper.DAY_IN_MS);
	}
	
	/**
	 * Gets the statuses id map.
	 *
	 * @return the statuses id map
	 * @throws AViewException
	 */
	private static synchronized Map<Integer, Status> getStatusesIdMap() throws AViewException
	{
		//If cache is expired or invalidated
		if(!CacheHelper.isCacheValid(CACHE_CODE))
		{
			List<Status> allStatuses = StatusDAO.getStatuses();
			populateCache(allStatuses);
		}
		return allStatusesMap;
	}
	
	/**
	 * Gets the deleted status id.
	 *
	 * @return the deleted status id
	 * @throws AViewException
	 */
	public static Integer getDeletedStatusId() throws AViewException{
		
		Integer deletedStatusId = null;
		if((deletedStatusId = statusMap.get(Constant.DELETESTATUS)) == null)
		{
			//logger.debug("Get all status table is called at getDeletedStatusId");
			getStatuses();
			deletedStatusId = statusMap.get(Constant.DELETESTATUS);
			//statusMap.put(Constant.DELETESTATUS, deletedStatusId);
		}		
		return deletedStatusId;
	}
	
	/**
	 * Gets the active status id.
	 *
	 * @return the active status id
	 * @throws AViewException
	 */
	public static Integer getActiveStatusId() throws AViewException{
		Integer activeStatusId = null;
		if((activeStatusId = statusMap.get(Constant.ACTIVESTATUS)) == null)
		{
			//logger.debug("Get all status table is called at getActiveStatusId");
			getStatuses();
			activeStatusId = statusMap.get(Constant.ACTIVESTATUS);
			//statusMap.put(Constant.ACTIVESTATUS, activeStatusId);			
		}
		
		return activeStatusId;
	}

	/**
	 * Gets the closed status id.
	 *
	 * @return the closed status id
	 * @throws AViewException
	 */
	public static Integer getClosedStatusId() throws AViewException{
		
		Integer closedStatusId = null;
		if((closedStatusId = statusMap.get(Constant.CLOSEDSTATUS)) == null)
		{
			//logger.debug("Get all status table is called at getClosedStatusId");
			getStatuses();
			closedStatusId = statusMap.get(Constant.CLOSEDSTATUS);
			//statusMap.put(Constant.CLOSEDSTATUS, closedStatusId);
		}
		
		return closedStatusId;
	}
	
	/**
	 * Gets the approved status id.
	 *
	 * @return the approved status id
	 * @throws AViewException
	 */
	public static Integer getApprovedStatusId() throws AViewException{
		Integer approvedStatusId = null;
		if((approvedStatusId = statusMap.get(Constant.APPROVESTATUS)) == null)
		{
			//logger.debug("Get all status table is called at getApprovedStatusId");
			getStatuses();
			approvedStatusId = statusMap.get(Constant.APPROVESTATUS);
			//statusMap.put(Constant.APPROVESTATUS, approvedStatusId);
		}
		
		return approvedStatusId;
	}

	/**
	 * Gets the pending status id.
	 *
	 * @return the pending status id
	 * @throws AViewException
	 */
	public static Integer getPendingStatusId() throws AViewException{
		Integer pendingStatusId = null;
		if((pendingStatusId = statusMap.get(Constant.PENDINGSTATUS)) == null)
		{
			//logger.debug("Get all status table is called at getPendingStatusId");
			getStatuses();
			pendingStatusId = statusMap.get(Constant.PENDINGSTATUS);
			//statusMap.put(Constant.PENDINGSTATUS, pendingStatusId);
		}
		
		return pendingStatusId;
	}
	
	/**
	 * Gets the communicating status id.
	 *
	 * @return the communicating status id
	 * @throws AViewException
	 */
	public static Integer getCommunicatingStatusId() throws AViewException{
		Integer communicatingStatusId = null;
		if((communicatingStatusId = statusMap.get(Constant.COMMUNICATING_STATUS)) == null)
		{
			//logger.debug("Get all status table is called at getCommunicatingStatusId");
			getStatuses();
			communicatingStatusId = statusMap.get(Constant.COMMUNICATING_STATUS);
			//statusMap.put(Constant.COMMUNICATING_STATUS, communicatingStatusId);
		}		
		return communicatingStatusId;
	}
	
	/**
	 * Gets the testing status id.
	 *
	 * @return the testing status id
	 * @throws AViewException
	 */
	public static Integer getTestingStatusId() throws AViewException{
		Integer testingStatusId = null;
		if((testingStatusId = statusMap.get(Constant.TESTING_STATUS)) == null)
		{
			//logger.debug("Get all status table is called at getTestingStatusId");
			getStatuses();
			testingStatusId = statusMap.get(Constant.TESTING_STATUS);
			//statusMap.put(Constant.TESTING_STATUS, testingStatusId);
		}		
		return testingStatusId;
	}
	
	/**
	 * Gets the failed testing status id.
	 *
	 * @return the failed testing status id
	 * @throws AViewException
	 */
	public static Integer getFailedTestingStatusId() throws AViewException{
		Integer failedTestingStatusId = null;
		if((failedTestingStatusId = statusMap.get(Constant.FAILEDTESTING_STATUS)) == null)
		{
			//logger.debug("Get all status table is called at getFailedTestingStatusId");
			getStatuses();
			failedTestingStatusId = statusMap.get(Constant.FAILEDTESTING_STATUS);
			//statusMap.put(Constant.FAILEDTESTING_STATUS, failedTestingStatusId);
		}		
		return failedTestingStatusId;
	}
	
	/**
	 * Gets the in active status id.
	 *
	 * @return the in active status id
	 * @throws AViewException
	 */
	public static Integer getInActiveStatusId() throws AViewException{
		Integer inActiveStatusId = null;
		if((inActiveStatusId = statusMap.get(Constant.INACTIVE_STATUS)) == null)
		{
			//logger.debug("Get all status table is called at getInActiveStatusId");
			getStatuses();
			inActiveStatusId = statusMap.get(Constant.INACTIVE_STATUS);
			//statusMap.put(Constant.INACTIVE_STATUS, inActiveStatusId);
		}		
		return inActiveStatusId;
	}
	
	/**
	 * Gets the Joined status id.
	 *
	 * @return the Joined status id
	 * @throws AViewException
	 */
	public static Integer getJoinedStatusId() throws AViewException{
		
		Integer statusId = null;
		if((statusId = statusMap.get(Constant.JOINED_STATUS)) == null)
		{
			getStatuses();
			statusId = statusMap.get(Constant.JOINED_STATUS);
		}		
		return statusId;
	}
	
	/**
	 * Gets the Exited status id.
	 *
	 * @return the Exited status id
	 * @throws AViewException
	 */
	public static Integer getExitedStatusId() throws AViewException{
		
		Integer statusId = null;
		if((statusId = statusMap.get(Constant.EXITED_STATUS)) == null)
		{
			getStatuses();
			statusId = statusMap.get(Constant.EXITED_STATUS);
		}		
		return statusId;
	}
	
	/**
	 * Gets the status.
	 *
	 * @param statusId the status id
	 * @return the status
	 * @throws AViewException
	 */
	public static Status getStatus(Integer statusId) throws AViewException
	{
		Status status = null;
		if((status = allStatusesMap.get(statusId)) ==  null)
		{
			//logger.debug("Get all status table is called at getStatus");
			getStatuses();
			status = allStatusesMap.get(statusId);
		}
		return status;
	}
	
	/**
	 * Gets the statuses.
	 *
	 * @return the statuses
	 * @throws AViewException
	 */
	public static List<Status> getStatuses() throws AViewException
	{
		List<Status> allStatuses = new ArrayList<Status>();
		allStatuses.addAll(getStatusesIdMap().values());
		return allStatuses;
	}

	/**
	 * Clear cache.
	 */
	public static void clearCache()
	{
		logger.debug("Entering StatusHelper::clearCache");
		allStatusesMap = null;
		statusMap = null;
		logger.debug("Entering StatusHelper::clearCache");
	}
}
