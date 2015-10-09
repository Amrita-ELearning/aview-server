/**
 * 
 */
package edu.amrita.aview.common.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.DistrictDAO;
import edu.amrita.aview.common.entities.District;
import edu.amrita.aview.gclm.helpers.CacheHelper;


/**
 * The Class DistrictHelper.
 *
 * @author
 */
public class DistrictHelper {
	
	//Cache code
	/** The districts map. */
	private static HashMap<Integer,District> districtsMap = new HashMap<Integer,District>();
	
	/** The Constant CACHE_CODE. */
	private static final String CACHE_CODE = "DistrictHelper";
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(DistrictHelper.class);
	
	/**
	 * Populate cache.
	 *
	 * @param districts the districts
	 */
	private static synchronized void populateCache(List<District> districts)
	{
		districtsMap.clear();
		for(District district:districts)
		{
			districtsMap.put(district.getDistrictId(), district);
		}
		CacheHelper.setCache(CACHE_CODE, CacheHelper.DAY_IN_MS);
	}
	
	
	/**
	 * Gets the districts id map.
	 *
	 * @return the districts id map
	 * @throws AViewException
	 */
	public static synchronized HashMap<Integer,District> getDistrictsIdMap() throws AViewException
	{
		if(!CacheHelper.isCacheValid(CACHE_CODE))
		{
			populateCache(DistrictDAO.getDistricts(StatusHelper.getActiveStatusId()));
		}
		return districtsMap;
	}
	

	/**
	 * Gets the districts.
	 *
	 * @return the districts
	 * @throws AViewException
	 */
	public static List<District> getDistricts() throws AViewException{
		List<District> districts = new ArrayList<District>();
		districts.addAll(getDistrictsIdMap().values());
		return districts;
	}
	
	/**
	 * Gets the district.
	 *
	 * @param districtId the district id
	 * @return the district
	 * @throws AViewException
	 */
	public static District getDistrict(Integer districtId) throws AViewException
	{
		return getDistrictsIdMap().get(districtId);
	}

	/**
	 * Gets the district by state id.
	 *
	 * @param stateId the state id
	 * @return the district by state id
	 * @throws AViewException
	 */
	public static List<District> getDistrictByStateId(Integer stateId) throws AViewException{
		
		//All Districts
		List<District> districts = getDistricts();
		
		//Remove out of state districts
	   for (Iterator<District> iter = districts.iterator(); iter.hasNext();) 
	   {
		   District dist = iter.next();
	        if (!(dist.getStateId().equals(stateId)))
	        {
	          iter.remove();
	        }
	   }

		return districts;
	}
 
	/**
	 * Clear cache.
	 */
	public static void clearCache()
	{
		logger.debug("Entering DistrictHelper::clearCache");
		districtsMap = null;
		logger.debug("Exit DistrictHelper::clearCache");
	}
}
