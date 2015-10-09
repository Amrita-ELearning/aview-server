/**
 * 
 */
package edu.amrita.aview.gclm.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.gclm.daos.InstituteCategoryDAO;
import edu.amrita.aview.gclm.entities.InstituteCategory;


/**
 * The Class InstituteCategoryHelper.
 *
 * @author
 */
public class InstituteCategoryHelper {
	
	/** The institute categories. */
	private static List<InstituteCategory> instituteCategories = null;
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(InstituteCategoryHelper.class);
	private static Map<Integer,InstituteCategory> allInstituteCategoryMap = Collections.synchronizedMap(new HashMap<Integer, InstituteCategory>());
	private static final String CACHE_CODE = "InstituteCategoryHelper";
	
	private static synchronized void populateCache(List<InstituteCategory> instituteCategories)
	{
		allInstituteCategoryMap.clear();
		for(InstituteCategory instituteCategory:instituteCategories)
		{
			allInstituteCategoryMap.put(instituteCategory.getInstituteCategoryId(), instituteCategory);
		}
		CacheHelper.setCache(CACHE_CODE);
	}
	
	private static synchronized Map<Integer, InstituteCategory> getInstituteCategoriesIdMap() throws AViewException
	{
		//If cache is expired or invalidated
		if(!CacheHelper.isCacheValid(CACHE_CODE))
		{
			List<InstituteCategory> instituteCategories = new ArrayList<InstituteCategory>();	
			instituteCategories = InstituteCategoryDAO.getInstituteCategories();
			populateCache(instituteCategories);
		}
		return allInstituteCategoryMap;
	}
	
	public static InstituteCategory getInstituteCategory(Integer instituteCategoryId) throws AViewException
	{
		InstituteCategory instituteCategory = null;
		if((instituteCategory = allInstituteCategoryMap.get(instituteCategoryId)) ==  null)
		{
			getInstituteCategoriesIdMap();
			instituteCategory = allInstituteCategoryMap.get(instituteCategoryId);
		}
		return instituteCategory;
	}
	/**
	 * Gets the institute categories.
	 *
	 * @return the institute categories
	 * @throws AViewException
	 */
	public static List<InstituteCategory> getInstituteCategories() throws AViewException{
		List<InstituteCategory> instituteCategories = new ArrayList<InstituteCategory>();	
		instituteCategories.addAll(getInstituteCategoriesIdMap().values());
		return instituteCategories;
	}

	/**
	 * Clear cache.
	 */
	public static void clearCache()
	{
		logger.debug("Entered InstituteCategoryHelper::clearCache");
		allInstituteCategoryMap = null;
		logger.debug("Exit InstituteCategoryHelper::clearCache");
	}
}
