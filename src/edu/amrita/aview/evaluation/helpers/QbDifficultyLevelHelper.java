/*
 * @(#)QbDifficultyLevelHelper.java 4.0 2013/10/16
 * 
 * Copyright  © 2013 E-Learning Research Lab, 
 * Amrita Vishwa Vidyapeetham. All rights reserved. 
 * E-Learning Research Lab and the A-VIEW logo are trademarks or
 * registered trademarks of E-Learning Research Lab. 
 * All other names used are the trademarks of their respective owners.
 */
package edu.amrita.aview.evaluation.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.evaluation.daos.QbDifficultyLevelDAO;
import edu.amrita.aview.evaluation.entities.QbDifficultyLevel;
import edu.amrita.aview.gclm.helpers.CacheHelper;



/**
 * This class connects client and server.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QbDifficultyLevelHelper 
{
    
    /** The logger. */
    private static Logger logger = Logger.getLogger(QbDifficultyLevelHelper.class);

    /** The qb difficulty levels map. */
    private static HashMap<Long, QbDifficultyLevel> qbDifficultyLevelsMap = new HashMap<Long, QbDifficultyLevel>();
    
    /** The Constant CACHE_CODE. */
    private static final String CACHE_CODE = "QbDifficultyLevelHelper";
    
    /**
     * Populate cache.
     *
     * @param qbDifficultyLevels the qb difficulty levels
     * @throws AViewException
     */
    private static synchronized void populateCache(List<QbDifficultyLevel> qbDifficultyLevels)throws AViewException 
    {
        qbDifficultyLevelsMap.clear();
        for(QbDifficultyLevel qbDifficultyLevel : qbDifficultyLevels)
        {
        	qbDifficultyLevelsMap.put(qbDifficultyLevel.getQbDifficultyLevelId(), qbDifficultyLevel);
        }
        CacheHelper.setCache(CACHE_CODE, CacheHelper.DAY_IN_MS);
    }

    /**
     * Gets the difficulty levels id map.
     *
     * @return the difficulty levels id map
     * @throws AViewException
     */
    private static synchronized Map<Long, QbDifficultyLevel> getdifficultyLevelsIdMap()throws AViewException 
    {
    	if(!CacheHelper.isCacheValid(CACHE_CODE))
		{
			populateCache(QbDifficultyLevelDAO.getAllActiveDifficultyLevels(StatusHelper.getActiveStatusId()));
		}
		return qbDifficultyLevelsMap;
    }
    
    /**
     * Adds the item to cache.
     *
     * @param qbDifficultyLevel the qb difficulty level
     */
    private static synchronized void addItemToCache(QbDifficultyLevel qbDifficultyLevel)
	{
    	qbDifficultyLevelsMap.put(qbDifficultyLevel.getQbDifficultyLevelId(), qbDifficultyLevel);
	}
	
    // ---------------------------------------------------------------------

    /**
     * Gets the all active difficulty levels.
     *
     * @return the all active difficulty levels
     * @throws AViewException
     */
    public static List<QbDifficultyLevel> getAllActiveDifficultyLevels()
            throws AViewException 
    {
        List<QbDifficultyLevel> qbDifficultyLevels = new ArrayList<QbDifficultyLevel>();
        qbDifficultyLevels.addAll(getdifficultyLevelsIdMap().values());
        return qbDifficultyLevels;
    }

    /**
     * Gets the difficulty level by id.
     *
     * @param qbDifficultyLevelId the qb difficulty level id
     * @return the difficulty level by id
     * @throws AViewException
     */
    public static QbDifficultyLevel getDifficultyLevelById(Long qbDifficultyLevelId)
            throws AViewException 
    {
    	return getdifficultyLevelsIdMap().get(qbDifficultyLevelId);
    }

    /**
     * Creates the difficulty level.
     *
     * @param qbDifficultyLevel the qb difficulty level
     * @param creatorId the creator id
     * @throws AViewException
     */
    public static void createDifficultyLevel(QbDifficultyLevel qbDifficultyLevel, Long creatorId)
            throws AViewException 
    {
        qbDifficultyLevel.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());

        QbDifficultyLevelDAO.createDifficultyLevel(qbDifficultyLevel);

        if ((qbDifficultyLevel.getQbDifficultyLevelId() != null) && 
        		(qbDifficultyLevel.getQbDifficultyLevelId() != 0) ) 
        {
            addItemToCache(qbDifficultyLevel);
        }
        logger.debug("Exited createDifficultyLevel without throwing any exception:");       
    }

    /**
     * Update difficulty level.
     *
     * @param qbDifficultyLevel the qb difficulty level
     * @param updaterId the updater id
     * @throws AViewException
     */
    public static void updateDifficultyLevel(QbDifficultyLevel qbDifficultyLevel, Long updaterId)
            throws AViewException 
    {
    	qbDifficultyLevel.setModifiedAuditData(updaterId, TimestampUtils.getCurrentTimestamp());
        QbDifficultyLevelDAO.updateDifficultyLevel(qbDifficultyLevel);            
        addItemToCache(qbDifficultyLevel);
        logger.debug("Exited updateDifficultyLevel without throwing any exception:");        
    }    
    
    /**
     * Gets the qb df level for name.
     *
     * @param qbDifficultyLevelName the qb difficulty level name
     * @return the qb df level for name
     * @throws AViewException
     */
    public static QbDifficultyLevel getQbDfLevelForName(String qbDifficultyLevelName) throws AViewException
	{
    	return QbDifficultyLevelDAO.getQbDfLevelForName(qbDifficultyLevelName) ;
	}
    
    /**
     * Clear cache.
     */
    public static void clearCache()
    {
    	logger.debug("Entering QbDifficultyLevelHelper::clearCache");
    	qbDifficultyLevelsMap = null;
    	logger.debug("Exit QbDifficultyLevelHelper::clearCache");
    }
}
