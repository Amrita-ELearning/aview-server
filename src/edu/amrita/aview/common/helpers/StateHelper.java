/**
 * 
 */
package edu.amrita.aview.common.helpers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.StateDAO;
import edu.amrita.aview.common.entities.State;
import edu.amrita.aview.gclm.helpers.CacheHelper;


/**
 * The Class StateHelper.
 *
 * @author
 */
public class StateHelper {

	//Cache code
	/** The states map. */
	private static HashMap<Integer,State> statesMap = new HashMap<Integer,State>();
	
	/** The Constant CACHE_CODE. */
	private static final String CACHE_CODE = "StateHelper";
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(StateHelper.class);
	
	/**
	 * Populate cache.
	 *
	 * @param states the states
	 * @throws AViewException
	 */
	private static synchronized void populateCache(List<State> states) throws AViewException
	{
		statesMap.clear();
		for(State state:states)
		{
			statesMap.put(state.getStateId(), state);
		}
		CacheHelper.setCache(CACHE_CODE, CacheHelper.DAY_IN_MS);
	}
	
	
	/**
	 * Gets the states id map.
	 *
	 * @return the states id map
	 * @throws AViewException
	 */
	public static synchronized HashMap<Integer,State> getStatesIdMap() throws AViewException
	{
		if(!CacheHelper.isCacheValid(CACHE_CODE))
		{
			populateCache(StateDAO.getStates(StatusHelper.getActiveStatusId()));
		}
		return statesMap;
	}
	
	/**
	 * Gets the states.
	 *
	 * @return the states
	 * @throws AViewException
	 */
	public static List<State> getStates() throws AViewException{
		logger.debug("Get all states");
		List<State> states = new ArrayList<State>();
		states.addAll(getStatesIdMap().values());
		return states;
	}
	
	/**
	 * Gets the state.
	 *
	 * @param stateId the state id
	 * @return the state
	 * @throws AViewException
	 */
	public static State getState(Integer stateId) throws AViewException
	{
		return getStatesIdMap().get(stateId);
	}
	
	/**
	 * Clear cache.
	 */
	public static void clearCache()
	{
		logger.debug("Entering StateHelper::clearCache");
		statesMap = null;
		logger.debug("Entering StateHelper::clearCache");
	}
}
