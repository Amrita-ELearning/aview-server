/*
 * @(#)QbSubcategoryHelper.java 4.0 2013/10/16
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
import edu.amrita.aview.evaluation.daos.QbSubcategoryDAO;
import edu.amrita.aview.evaluation.entities.QbCategory;
import edu.amrita.aview.evaluation.entities.QbSubcategory;
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
public class QbSubcategoryHelper 
{
    
    /** The logger. */
    private static Logger logger = Logger.getLogger(QbSubcategoryHelper.class);

    /** The qb subcategories map. */
    private static Map<Long, QbSubcategory> qbSubcategoriesMap = Collections.synchronizedMap(new HashMap<Long, QbSubcategory>());

    /** The Constant CACHE_CODE. */
    private static final String CACHE_CODE = "QbSubcategoryHelper";

    /**
     * Populate cache.
     *
     * @param subCatIdMap the sub cat id map
     * @throws AViewException
     */
    private static synchronized void populateCache(Map<Long, QbSubcategory> subCatIdMap) 
    	    throws AViewException 
    {
        qbSubcategoriesMap.clear();
        qbSubcategoriesMap.putAll(subCatIdMap);
        CacheHelper.setCache(CACHE_CODE);
    }

    /**
     * Adds the item to cache.
     *
     * @param qbSubCategory the qb sub category
     * @throws AViewException
     */
    private static synchronized void addItemToCache(QbSubcategory qbSubCategory)
            throws AViewException 
    {
        qbSubcategoriesMap.put(qbSubCategory.getQbSubcategoryId(), qbSubCategory);
    }
    
    /**
     * Removes the item from cache.
     *
     * @param qbSubCategory the qb sub category
     * @throws AViewException
     */
    private static synchronized void removeItemFromCache(QbSubcategory qbSubCategory)
            throws AViewException 
    {
        qbSubcategoriesMap.remove(qbSubCategory.getQbSubcategoryId());
    }

    /**
     * Qb subcategory ids map.
     *
     * @return the map
     * @throws AViewException
     */
    private static synchronized Map<Long, QbSubcategory> qbSubcategoryIdsMap()
            throws AViewException 
    {
        Integer activeSID = StatusHelper.getActiveStatusId();
        // If cache is expired or invalidated
        if (!CacheHelper.isCacheValid(CACHE_CODE)) 
        {           
            List<QbSubcategory> qbSubcategories = QbSubcategoryDAO.getAllActiveQbSubcategories(activeSID);
            // Populate the Map
            Map<Long, QbSubcategory> qbSubcategoryIdsMap = new HashMap<Long, QbSubcategory>();
            for (QbSubcategory qbSubcategory : qbSubcategories) 
            {
            	qbSubcategoryIdsMap.put(qbSubcategory.getQbSubcategoryId(),qbSubcategory);
            }
            populateNames(qbSubcategories);
            populateCache(qbSubcategoryIdsMap);           
        }
        return qbSubcategoriesMap;
    }

   /**
    * Populate names.
    *
    * @param qbSubcategories the qb subcategories
    * @throws AViewException
    */
   private static void populateNames(List<QbSubcategory> qbSubcategories) throws AViewException 
   {
	   for (QbSubcategory qbSubcategory : qbSubcategories) 
       {
		   populateNames(qbSubcategory);
       }
   }

    /**
     * Populate names.
     *
     * @param qbSubcategory the qb subcategory
     * @throws AViewException
     */
    public static void populateNames(QbSubcategory qbSubcategory) 
    	   throws AViewException 
    {
    	populateCategoryDetails(qbSubcategory);
    	populateUserDetails(qbSubcategory);    	
    }
    
    /**
     * Populate category details.
     *
     * @param qbSubcategory the qb subcategory
     * @throws AViewException
     */
    private static void populateCategoryDetails(QbSubcategory qbSubcategory) throws AViewException
    {
    	Long qbCategoryId = qbSubcategory.getQbCategoryId();
    	QbCategory qbCategory = QbCategoryHelper.getQbCategoryById(qbCategoryId);
    	if(qbCategory != null)
    	{
    		qbSubcategory.setQbCategoryName(qbCategory.getQbCategoryName());
    	}
    }
    
    /**
     * Populate user details.
     *
     * @param qbSubcategory the qb subcategory
     * @throws AViewException
     */
    private static void populateUserDetails(QbSubcategory qbSubcategory) throws AViewException
    {
    	User user = UserHelper.getUser(qbSubcategory.getCreatedByUserId());
    	if(user != null)
    	{
    		qbSubcategory.setCreatedByUserName(user.getUserName());
    	}
    	user = UserHelper.getUser(qbSubcategory.getModifiedByUserId());
    	if(user != null)
    	{
    		qbSubcategory.setModifiedByUserName(user.getUserName());
    	}
    }

    // ---------------------------------------------------------------------
    /**
     * Gets the all active qb subcategories.
     *
     * @return the all active qb subcategories
     * @throws AViewException
     */
    public static List<QbSubcategory> getAllActiveQbSubcategories()
            throws AViewException 
    {
        /*List<QbSubcategory> qbSubcategories = new ArrayList<QbSubcategory>();
        qbSubcategories.addAll(qbSubcategoryIdsMap().values());*/
    	Integer activeSID = StatusHelper.getActiveStatusId();
    	List<QbSubcategory> qbSubcategories = QbSubcategoryDAO.getAllActiveQbSubcategories(activeSID);
    	populateNames(qbSubcategories);
        return qbSubcategories;
    }

    /**
     * Gets the qb subcategory by id.
     *
     * @param qbSubcategoryId the qb subcategory id
     * @return the qb subcategory by id
     * @throws AViewException
     */
    public static QbSubcategory getQbSubcategoryById(Long qbSubcategoryId)
            throws AViewException 
    {
        QbSubcategory qbSubcategory = QbSubcategoryDAO.getQbSubcategoryById(qbSubcategoryId);
        if(qbSubcategory != null)
        {
        	populateNames(qbSubcategory);
        }
    	return qbSubcategory;
    }

    /**
     * Creates the qb subcategory.
     *
     * @param qbSubCategory the qb sub category
     * @param creatorId the creator id
     * @return the qb subcategory
     * @throws AViewException
     */
    public static QbSubcategory createQbSubcategory(QbSubcategory qbSubCategory,Long creatorId) 
    	   throws AViewException 
    {
    	if(qbSubCategory.getQbSubcategoryName().equals(QuizConstant.POLLING_QUESTION_TYPE))
    	{
    		throw new AViewException("Please enter a different sub category name") ;
    	}
        qbSubCategory.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
        QbSubcategoryDAO.createQbSubcategory(qbSubCategory);
        if ((qbSubCategory.getQbSubcategoryId() != null) && 
        		(qbSubCategory.getQbSubcategoryId() != 0) ) 
        {
            //populateNames(qbSubCategory, (qbSubCategory.getQbSubcategoryName()));
            addItemToCache(qbSubCategory);
        }
        logger.debug("Exited createQbSubcategory without throwing any exception:");
        return qbSubCategory;
    }

    /**
     * Update qb subcategory.
     *
     * @param updateQbSubCategory the update qb sub category
     * @param updaterId the updater id
     * @return the qb subcategory
     * @throws AViewException
     */
    public static QbSubcategory updateQbSubcategory(QbSubcategory updateQbSubCategory,Long updaterId) 
    	   throws AViewException 
    {
    	if(updateQbSubCategory.getQbSubcategoryName().equals(QuizConstant.POLLING_QUESTION_TYPE))
    	{
    		throw new AViewException("Please enter a different sub category name") ;
    	}
    	QbSubcategory qbSubCategory = getQbSubcategoryById(updateQbSubCategory.getQbSubcategoryId());
    	if(qbSubCategory != null)
    	{
    		updateQbSubCategory.setModifiedAuditData(updaterId, TimestampUtils.getCurrentTimestamp());
	        QbSubcategoryDAO.updateQbSubcategory(updateQbSubCategory);	        
	        addItemToCache(updateQbSubCategory);
    	}
    	else
		{
			throw new AViewException("QbSubcategory with id :"+updateQbSubCategory.getQbSubcategoryId()+": is not found");
		}
        logger.debug("Exited updateQbSubcategory without throwing any exception:");
        return updateQbSubCategory;
    }

    /**
     * Removes the qb sub categories based on qb category.
     *
     * @param qbCategoryId the qb category id
     * @throws AViewException
     */
    public static void removeQbSubCategoriesBasedOnQbCategory(Long qbCategoryId) throws AViewException
    {
    	logger.info("Deleting subcategories based on category id: " + qbCategoryId);
    	List<QbSubcategory> qbSubcategories = new ArrayList<QbSubcategory>();
        qbSubcategories.addAll(qbSubcategoryIdsMap().values());		
    }
    
    /**
     * Delete qb subcategory.
     *
     * @param qbSubcategoryId the qb subcategory id
     * @param modifiedByUserId the modified by user id
     * @throws AViewException
     */
    public static void deleteQbSubcategory(Long qbSubcategoryId,Long modifiedByUserId) 
    	   throws AViewException 
    {
        QbSubcategory qbSubCategory = getQbSubcategoryById(qbSubcategoryId);
        if (qbSubCategory != null) 
        {
        	qbSubCategory.setStatusId(StatusHelper.getDeletedStatusId());
        	qbSubCategory.setQbSubcategoryName(qbSubCategory.getQbSubcategoryName() + AppenderUtils.DeleteAppender());
        	qbSubCategory.setModifiedAuditData(modifiedByUserId, TimestampUtils.getCurrentTimestamp());
            QbSubcategoryDAO.updateQbSubcategory(qbSubCategory);
            removeItemFromCache(qbSubCategory);
        } 
        else
        {
            throw new AViewException("QbSubcategory with id :"+ qbSubcategoryId + ": is not found");
        }
        logger.debug("Exited deleteQbSubcategory without throwing any exception:");
    }

    /**
     * Gets the all active qb subcategories for user.
     *
     * @param userId the user id
     * @return the all active qb subcategories for user
     * @throws AViewException
     */
    public static List<QbSubcategory> getAllActiveQbSubcategoriesForUser(Long userId) 
           throws AViewException 
    {
        Integer statusId = StatusHelper.getActiveStatusId();
        List<QbSubcategory> qbSubcategories = QbSubcategoryDAO.getAllActiveQbSubcategoriesForUser(userId, statusId);
        populateNames(qbSubcategories);
        return qbSubcategories;
    }

 	/**
	  * Gets the all active qb subcategories summary for category.
	  *
	  * @param qbCategoryId the qb category id
	  * @return the all active qb subcategories summary for category
	  * @throws AViewException
	  */
	 public static List<QbSubcategory> getAllActiveQbSubcategoriesSummaryForCategory(Long qbCategoryId) throws AViewException
  	{
  		Integer statusId = StatusHelper.getActiveStatusId() ;
  		List<QbSubcategory> qbSubcategories = QbSubcategoryDAO.getAllActiveQbSubcategoriesSummaryForCategory(qbCategoryId, statusId);
  		populateNames(qbSubcategories);
  		return qbSubcategories ;
  	}
 	
 	/**
	  * Gets the qb subcategory for name.
	  *
	  * @param qbSubcategoryName the qb subcategory name
	  * @return the qb subcategory for name
	  * @throws AViewException
	  */
	 public static QbSubcategory getQbSubcategoryForName(String qbSubcategoryName) throws AViewException
	{
 		return QbSubcategoryDAO.getQbSubcategoryForName(qbSubcategoryName) ;
	}
 	
 	/**
	  * Clear cache.
	  */
	 public static void clearCache()
 	{
 		logger.debug("Entered QbSubcategoryHelper.clearCache()");
 		qbSubcategoriesMap.clear();
 		logger.debug("Entered QbSubcategoryHelper.clearCache()");
 	}
}
