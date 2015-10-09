/*
 * @(#)QbCategoryHelper.java 4.0 2013/10/16
 * 
 * Copyright  © 2013 E-Learning Research Lab, 
 * Amrita Vishwa Vidyapeetham. All rights reserved. 
 * E-Learning Research Lab and the A-VIEW logo are trademarks or
 * registered trademarks of E-Learning Research Lab. 
 * All other names used are the trademarks of their respective owners.
 */
package edu.amrita.aview.evaluation.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.AppenderUtils;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.evaluation.QuizConstant;
import edu.amrita.aview.evaluation.daos.QbCategoryDAO;
import edu.amrita.aview.evaluation.entities.QbCategory;
import edu.amrita.aview.gclm.entities.User;
import edu.amrita.aview.gclm.helpers.CacheHelper;
import edu.amrita.aview.gclm.helpers.UserHelper;



/**
 * This class connects the client and server.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QbCategoryHelper 
{
    /** The logger. */
    private static Logger logger = Logger.getLogger(QbCategoryHelper.class);

    /** The qb category map. */
    private static Map<Long, QbCategory> qbCategoryMap = Collections.synchronizedMap(new HashMap<Long, QbCategory>());

    /** The Constant CACHE_CODE. */
    private static final String CACHE_CODE = "QbCategoryHelper";

    /**
     * Populate cache.
     *
     * @param qbCategoryIdsMap the qb category ids map
     * @throws AViewException
     */
    private static synchronized void populateCache(Map<Long, QbCategory> qbCategoryIdsMap) throws AViewException 
    {
        qbCategoryMap.clear();
        qbCategoryMap.putAll(qbCategoryIdsMap);
        CacheHelper.setCache(CACHE_CODE, CacheHelper.DAY_IN_MS);
    }

    /**
     * Adds the item to cache.
     *
     * @param qbCategory the qb category
     * @throws AViewException
     */
    private static synchronized void addItemToCache(QbCategory qbCategory)throws AViewException 
    {
        qbCategoryMap.put(qbCategory.getQbCategoryId(), qbCategory);
    }

    /**
     * Removes the item from cache.
     *
     * @param qbCategory the qb category
     * @throws AViewException
     */
    private static synchronized void removeItemFromCache(QbCategory qbCategory) throws AViewException
    {
    	qbCategoryMap.remove(qbCategory.getQbCategoryId());
    }
    
    /**
     * Qb category ids map.
     *
     * @return the map
     * @throws AViewException
     */
    private static synchronized Map<Long, QbCategory> qbCategoryIdsMap()throws AViewException 
    {
        Integer activeSId = StatusHelper.getActiveStatusId();
        // If cache is expired or invalidated
        if (!CacheHelper.isCacheValid(CACHE_CODE)) 
        {          
            List<QbCategory> qbCategories = QbCategoryDAO.getAllActiveQbCategories(activeSId);
            // Populate the Map
            Map<Long, QbCategory> qbCategoryIdsMap = new HashMap<Long, QbCategory>();
            for (QbCategory qbCategory : qbCategories) 
            {
            	qbCategoryIdsMap.put(qbCategory.getQbCategoryId(), qbCategory);
            }
            populateCache(qbCategoryIdsMap);            
        }
        return qbCategoryMap;
    }
   
    // ---------------------------------------------------------------------
    /**
     * Gets the all active qb categories.
     *
     * @return the all active qb categories
     * @throws AViewException
     */
    public static List<QbCategory> getAllActiveQbCategories()throws AViewException 
    {
        List<QbCategory> qbCategories = new ArrayList<QbCategory>();
        qbCategories.addAll(qbCategoryIdsMap().values());
        return qbCategories;
    }

    /**
     * Gets the qb category by id.
     *
     * @param qbCategoryId the qb category id
     * @return the qb category by id
     * @throws AViewException
     */
    public static QbCategory getQbCategoryById(Long qbCategoryId)throws AViewException 
    {
        return QbCategoryDAO.getQbCategoryById(qbCategoryId);
    }

    /**
     * Creates the qb category.
     *
     * @param qbCategory the qb category
     * @param creatorId the creator id
     * @return the qb category
     * @throws AViewException
     */
    public static QbCategory createQbCategory(QbCategory qbCategory, Long creatorId)throws AViewException 
    {
    	if(qbCategory.getQbCategoryName().equals(QuizConstant.POLLING_QUESTION_TYPE))
    	{
    		throw new AViewException("Please enter a different category name") ;
    	}
    	User tempU = UserHelper.getUser(creatorId) ;
    	qbCategory.setInstituteId(tempU.getInstituteId()) ;
    	qbCategory.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
        QbCategoryDAO.createQbCategory(qbCategory);
        if ((qbCategory.getQbCategoryId() != null) && 
        		(qbCategory.getQbCategoryId() != 0) )
        {
            addItemToCache(qbCategory);
        }
        logger.debug("Exited createQbCategory without throwing any exception:");
        return qbCategory ;
    }

    /**
     * Update qb category.
     *
     * @param updateQbCategory the update qb category
     * @param updaterId the updater id
     * @return the qb category
     * @throws AViewException
     */
    public static QbCategory updateQbCategory(QbCategory updateQbCategory, Long updaterId)throws AViewException 
    {
    	if(updateQbCategory.getQbCategoryName().equals(QuizConstant.POLLING_QUESTION_TYPE))
    	{
    		throw new AViewException("Please enter a different category name") ;
    	}
    	getAllActiveQbCategories() ;
        QbCategory qbCategory = getQbCategoryById(updateQbCategory.getQbCategoryId());
        if (qbCategory != null) 
        {
        	updateQbCategory.setModifiedAuditData(updaterId, TimestampUtils.getCurrentTimestamp());
            QbCategoryDAO.updateQbCategory(updateQbCategory);            
            addItemToCache(updateQbCategory);
        } 
        else
        {
            throw new AViewException("QbCategory with id :"+ updateQbCategory.getQbCategoryName() + ": is not found");
        }
        logger.debug("Exited updateQbCategory without throwing any exception:");
        return updateQbCategory;
    }

    /**
     * Delete qb category.
     *
     * @param qbCategoryId the qb category id
     * @param modifiedByUserId the modified by user id
     * @throws AViewException
     */
    public static void deleteQbCategory(Long qbCategoryId, Long modifiedByUserId)throws AViewException 
    {    	
    	getAllActiveQbCategories() ;
        QbCategory qbCategory = getQbCategoryById(qbCategoryId);
        if (qbCategory != null) 
        {
            qbCategory.setQbCategoryName(qbCategory.getQbCategoryName() + AppenderUtils.DeleteAppender());
        	qbCategory.setStatusId(StatusHelper.getDeletedStatusId());
            qbCategory.setModifiedAuditData(modifiedByUserId, TimestampUtils.getCurrentTimestamp());            
            logger.debug("QbCategory:"+qbCategory.toString());
            QbCategoryDAO.updateQbCategory(qbCategory);
            removeItemFromCache(qbCategory);
            QbSubcategoryHelper.removeQbSubCategoriesBasedOnQbCategory(qbCategory.getQbCategoryId());
        }
        else 
        {
            throw new AViewException("QbCategory with id :" + qbCategoryId+ ": is not found");
        }
    }

    /**
     * Gets the all active qb categories for user.
     *
     * @param userId the user id
     * @return the all active qb categories for user
     * @throws AViewException
     */
    public static List<QbCategory> getAllActiveQbCategoriesForUser(Long userId)throws AViewException 
    {
        Integer activeSId = StatusHelper.getActiveStatusId();
        List<QbCategory> qbCategories = QbCategoryDAO.getAllActiveQbCategoriesForUser(userId, activeSId);      
        return qbCategories;
    }
     
    
    /**
     * Gets the qb category by name.
     *
     * @param qbCategoryName the qb category name
     * @return the qb category by name
     * @throws AViewException
     */
    public static Long getQbCategoryByName(String qbCategoryName) throws AViewException
    {
    	getAllActiveQbCategories() ;    	
    	Long qbCategoryId = 0l ;
    	List<QbCategory> qbCatList = new ArrayList<QbCategory>(qbCategoryMap.values()) ;
    	
    	for(QbCategory qbCat : qbCatList) 
    	{
    		if(qbCat.getQbCategoryName().startsWith(qbCategoryName))
    		{
    			qbCategoryId = qbCat.getQbCategoryId() ;
    			break ;
    		}
    	}
    	
    	return qbCategoryId  ;
    }
    
    /**
     * Clear cache.
     */
    public static void clearCache()
    {
    	logger.debug("Entering QbCategoryHelper::clearCache");
    	qbCategoryMap = null;
    	logger.debug("Entering QbCategoryHelper::clearCache");
    }
}
