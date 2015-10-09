/*
 * 
 */
package edu.amrita.aview.feedback.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.feedback.daos.ModuleDAO;
import edu.amrita.aview.feedback.entities.Module;
import edu.amrita.aview.gclm.helpers.CacheHelper;


/**
 * The Class ModuleHelper.
 */
public class ModuleHelper {
	//Cache code
	/** The modules map. */
	private static HashMap<Integer,Module> modulesMap = new HashMap<Integer,Module>();
	
	/** The Constant CACHE_CODE. */
	private static final String CACHE_CODE = "ModuleHelper";
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(ModuleHelper.class);
	
	/**
	 * Populate cache.
	 *
	 * @param modules the modules
	 */
	private static synchronized void populateCache(List<Module> modules)
	{
		modulesMap.clear();
		for(Module module:modules)
		{
			modulesMap.put(module.getModuleId(), module);
		}
		CacheHelper.setCache(CACHE_CODE, CacheHelper.DAY_IN_MS);
	}
	
	
	/**
	 * Gets the modules id map.
	 *
	 * @return the modules id map
	 * @throws AViewException
	 */
	public static synchronized HashMap<Integer,Module> getModulesIdMap() throws AViewException
	{
		if(!CacheHelper.isCacheValid(CACHE_CODE))
		{
			populateCache(ModuleDAO.getModules(StatusHelper.getActiveStatusId()));
		}
		return modulesMap;
	}
	
	/**
	 * Adds the item to cache.
	 *
	 * @param module the module
	 */
	private static synchronized void addItemToCache(Module module)
	{
		modulesMap.put(module.getModuleId(), module);
	}
	

	/**
	 * Gets the modules.
	 *
	 * @return the modules
	 * @throws AViewException
	 */
	public static List<Module> getModules() throws AViewException{
		List<Module> modules = new ArrayList<Module>();
		modules.addAll(getModulesIdMap().values());
		return modules;
	}
	
	/**
	 * Gets the module.
	 *
	 * @param moduleId the module id
	 * @return the module
	 * @throws AViewException
	 */
	public static Module getModule(Integer moduleId) throws AViewException
	{
		return getModulesIdMap().get(moduleId);
	}

	/**
	 * Creates the module.
	 *
	 * @param module the module
	 * @throws AViewException
	 */
	public static void createModule(Module module) throws AViewException
	{
		ModuleDAO.createModule(module);
		addItemToCache(module);
	}
	
	/**
	 * Delete module.
	 *
	 * @param module the module
	 * @throws AViewException
	 */
	public static void deleteModule(Module module) throws AViewException
	{
		int deleteStatus = StatusHelper.getDeletedStatusId();
		module.setStatusId(deleteStatus);
		ModuleDAO.updateModule(module);
		addItemToCache(module);
	}
	
	/**
	 * Update module.
	 *
	 * @param module the module
	 * @throws AViewException
	 */
	public static void updateModule(Module module) throws AViewException
	{
		ModuleDAO.updateModule(module);
	}

	/**
	 * Clear cache.
	 */
	public static void clearCache()
	{
		logger.debug("Entering ModuleHelper::clearCache");
		modulesMap = null;
		logger.debug("Exit ModuleHelper::clearCache");
	}
}
