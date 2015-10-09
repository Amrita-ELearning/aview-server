/*
 * 
 */
package edu.amrita.aview.gclm.helpers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.gclm.daos.ServerTypeDAO;
import edu.amrita.aview.gclm.entities.ServerType;


/**
 * The Class ServerTypeHelper.
 */
public class ServerTypeHelper {
	
	/** The cached server types. */
	private static List<ServerType> cachedServerTypes = null;
	
	/** The cached server type map. */
	private static Map<Integer,ServerType> cachedServerTypeMap = null;
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(ServerTypeHelper.class);
	
	/**
	 * Gets the all server types.
	 *
	 * @return the all server types
	 * @throws AViewException
	 */
	public static List<ServerType> getAllServerTypes() throws AViewException
	{
		if(cachedServerTypes == null)
		{
			cachedServerTypes = ServerTypeDAO.getAllServerTypes();
			
			cachedServerTypeMap = new HashMap<Integer,ServerType>();
			
			for(ServerType serverType:cachedServerTypes)
			{
				cachedServerTypeMap.put(serverType.getServerTypeId(), serverType);
			}
			
		}
		return cachedServerTypes;
	}
	
	/**
	 * Gets the server type name.
	 *
	 * @param serverTypeId the server type id
	 * @return the server type name
	 * @throws AViewException
	 */
	public static String getServerTypeName(Integer serverTypeId) throws AViewException
	{
		if(cachedServerTypeMap == null)
		{
			getAllServerTypes(); //Populates the map
		}
		return cachedServerTypeMap.get(serverTypeId).getServerType();
	}
	
	/**
	 * Clear cache.
	 */
	public static void clearCache()
	{
		logger.debug("Entering ServerTypeHelper::clearCache"); 
		cachedServerTypes = null;
		cachedServerTypeMap = null;
		logger.debug("Exit ServerTypeHelper::clearCache");
	}	
}
