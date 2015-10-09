/*
 * 
 */
package edu.amrita.aview.gclm.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.gclm.daos.ServerDAO;
import edu.amrita.aview.gclm.entities.Server;


/**
 * The Class ServerHelper.
 */
@Controller
public class ServerHelper {
	//Cache code
	/** The servers map. */
	private static HashMap<Integer,Server> serversMap = new HashMap<Integer,Server>();
	
	/** The Constant CACHE_CODE. */
	private static final String CACHE_CODE = "ServerHelper";
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(ServerHelper.class);
	
	/**
	 * Populate cache.
	 *
	 * @param servers the servers
	 */
	private static synchronized void populateCache(List<Server> servers)
	{
		serversMap.clear();
		for(Server server:servers)
		{
			serversMap.put(server.getServerId(), server);
		}
		CacheHelper.setCache(CACHE_CODE, CacheHelper.DAY_IN_MS);
	}
	
	
	/**
	 * Gets the servers id map.
	 *
	 * @return the servers id map
	 * @throws AViewException
	 */
	public static synchronized HashMap<Integer,Server> getServersIdMap() throws AViewException
	{
		if(!CacheHelper.isCacheValid(CACHE_CODE))
		{
			populateCache(ServerDAO.getServers(StatusHelper.getActiveStatusId()));
		}
		return serversMap;
	}
	
	/**
	 * Adds the item to cache.
	 *
	 * @param server the server
	 */
	private static synchronized void addItemToCache(Server server)
	{
		serversMap.put(server.getServerId(), server);
	}
	

	/**
	 * Gets the servers.
	 *
	 * @return the servers
	 * @throws AViewException
	 */
	public static List<Server> getServers() throws AViewException{
		List<Server> servers = new ArrayList<Server>();
		servers.addAll(getServersIdMap().values());
		return servers;
	}
	
	/**
	 * Gets the server.
	 *
	 * @param serverId the server id
	 * @return the server
	 * @throws AViewException
	 */
	public static Server getServer(Integer serverId) throws AViewException
	{
		return getServersIdMap().get(serverId);
	}

	/**
	 * Creates the server.
	 *
	 * @param server the server
	 * @throws AViewException
	 */
	public static void createServer(Server server) throws AViewException
	{
		ServerDAO.createServer(server);
		addItemToCache(server);
	}
	
	/**
	 * Delete server.
	 *
	 * @param server the server
	 * @throws AViewException
	 */
	public static void deleteServer(Server server) throws AViewException
	{
		int deleteStatus = StatusHelper.getDeletedStatusId();
		server.setStatusId(deleteStatus);
		ServerDAO.updateServer(server);
		addItemToCache(server);
	}
	
	/**
	 * Update server.
	 *
	 * @param server the server
	 * @throws AViewException
	 */
	public static void updateServer(Server server) throws AViewException
	{
		ServerDAO.updateServer(server);
	}
	
	/**
	 * Gets the server by domain.
	 *
	 * @param domain the domain
	 * @return the server by domain
	 */
	@RequestMapping(value = "getServerByDomain.html", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public static Server getServerByDomain(@RequestParam("domain")String domain)
	{
		Server matched = null;
		HashMap<Integer,Server> serversMap = getServersIdMap();
		for(Server server:serversMap.values())
		{
			if(server.getServerIp().equals(domain) || domain.equals(server.getServerDomain()))
			{
				matched = server;
				break;
			}
		}
		
		return matched;
	}
	
	/**
	 * Clear cache.
	 */
	public static void clearCache()
	{
		logger.debug("Entering ServerHelper::clearCache");
		serversMap = null;
		logger.debug("Entering ServerHelper::clearCache");
	}
	
}
