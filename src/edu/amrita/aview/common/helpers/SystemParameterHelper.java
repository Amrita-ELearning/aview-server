/*
 * 
 */
package edu.amrita.aview.common.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.daos.SystemParameterDAO;
import edu.amrita.aview.common.entities.Auditable;
import edu.amrita.aview.common.entities.SystemParameter;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.gclm.helpers.CacheHelper;


/**
 * The Class SystemParameterHelper.
 */
public class SystemParameterHelper extends Auditable
{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(SystemParameterHelper.class);
	
	//Cache code
	/** The system parameters map. */
	private static HashMap<Integer,SystemParameter> systemParametersMap = new HashMap<Integer,SystemParameter>();
	
	/** The Constant CACHE_CODE. */
	private static final String CACHE_CODE = "SystemParameterHelper";
	
	/**
	 * Populate cache.
	 *
	 * @param systemParameters the system parameters
	 */
	private static synchronized void populateCache(List<SystemParameter> systemParameters)
	{
		systemParametersMap.clear();
		for(SystemParameter systemParameter:systemParameters)
		{
			systemParametersMap.put(systemParameter.getParameterId(), systemParameter);
		}
		CacheHelper.setCache(CACHE_CODE);
	}
	
	
	/**
	 * Gets the system parameters id map.
	 *
	 * @return the system parameters id map
	 * @throws AViewException
	 */
	public static synchronized HashMap<Integer,SystemParameter> getSystemParametersIdMap() throws AViewException
	{
		if(!CacheHelper.isCacheValid(CACHE_CODE))
		{
			populateCache(SystemParameterDAO.getSystemParameters(StatusHelper.getActiveStatusId()));
		}
		return systemParametersMap;
	}
	
	/**
	 * Adds the item to cache.
	 *
	 * @param systemParameter the system parameter
	 */
	private static synchronized void addItemToCache(SystemParameter systemParameter)
	{
		systemParametersMap.put(systemParameter.getParameterId(), systemParameter);
	}
	
	/**
	 * Removes the item from cache.
	 *
	 * @param systemParameter the system parameter
	 */
	private static synchronized void removeItemFromCache(SystemParameter systemParameter)
	{
		systemParametersMap.remove(systemParameter);
	}

	/**
	 * Gets the system parameters.
	 *
	 * @return the system parameters
	 * @throws AViewException
	 */
	public static List<SystemParameter> getSystemParameters() throws AViewException{
		List<SystemParameter> systemParameters = new ArrayList<SystemParameter>();
		systemParameters.addAll(getSystemParametersIdMap().values());
		return systemParameters;
	}
	
	/**
	 * Gets the system parameter.
	 *
	 * @param systemParameterId the system parameter id
	 * @return the system parameter
	 * @throws AViewException
	 */
	public static SystemParameter getSystemParameter(Integer systemParameterId) throws AViewException
	{
		return getSystemParametersIdMap().get(systemParameterId);
	}

	/**
	 * Gets the system parameter.
	 *
	 * @param systemParameterName the system parameter name
	 * @return the system parameter
	 * @throws AViewException
	 */
	public static SystemParameter getSystemParameterByName(String systemParameterName) throws AViewException
	{
		SystemParameter s = SystemParameterDAO.getSystemParameterByName(systemParameterName, StatusHelper.getActiveStatusId());
		logger.debug("System Pararmeter for Allowed characters is : " + s);
		return s;
	}
	
	
	/**
	 * Creates the system parameter.
	 *
	 * @param systemParameter the system parameter
	 * @param creatorId the creator id
	 * @throws AViewException
	 */
	public static void createSystemParameter(SystemParameter systemParameter, Long creatorId) throws AViewException
	{
		logger.debug("Adding system parameter : " + systemParameter);
		systemParameter.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
		SystemParameterDAO.createSystemParameter(systemParameter);
		addItemToCache(systemParameter);
	}
	
	/**
	 * Delete system parameter.
	 *
	 * @param systemParameter the system parameter
	 * @param modifierId the modifier id
	 * @throws AViewException
	 */
	public static void deleteSystemParameter(SystemParameter systemParameter, Long modifierId) throws AViewException
	{
		int deleteStatus = StatusHelper.getDeletedStatusId();
		systemParameter.setStatusId(deleteStatus);
		updateSystemParameter(systemParameter, modifierId);
		removeItemFromCache(systemParameter);
	}
	
	/**
	 * Update system parameter.
	 *
	 * @param systemParameter the system parameter
	 * @param modifierId the modifier id
	 * @throws AViewException
	 */
	public static void updateSystemParameter(SystemParameter systemParameter, Long modifierId) throws AViewException
	{
		systemParameter.setModifiedAuditData(modifierId, TimestampUtils.getCurrentTimestamp());
		SystemParameterDAO.updateSystemParameter(systemParameter);
		addItemToCache(systemParameter);
	}
	
	/**
	 * Get system parameter used for naming convention in the UI.
	 *
	 * @return the system parameter
	 * @throws AViewException
	 */
	public static SystemParameter getSystemParameterForAllowedCharactersInNamingConvention() throws AViewException
	{
		return getSystemParameterByName(Constant.ALLOWED_CHARACTERS_FOR_NAMING);
	}
	
	/**
	 * Clear cache.
	 */
	public static void clearCache()
	{
		logger.debug("Entering SystemParameterHelper::clearCache");
		systemParametersMap = null;
		logger.debug("Entering SystemParameterHelper::clearCache");
	}
	
}
