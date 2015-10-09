/*
 * @(#)QbQuestionTypeHelper.java 4.0 2013/10/16
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
import edu.amrita.aview.evaluation.daos.QbQuestionTypeDAO;
import edu.amrita.aview.evaluation.entities.QbQuestionType;
import edu.amrita.aview.gclm.helpers.CacheHelper;



/**
 * This class connects the server and client.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QbQuestionTypeHelper 
{
    
    /** The logger. */
    private static Logger logger = Logger.getLogger(QbQuestionTypeHelper.class);

    /** The qb question types map. */
    private static HashMap<Long, QbQuestionType> qbQuestionTypesMap = new HashMap<Long, QbQuestionType>();
    
    /** The Constant CACHE_CODE. */
    private static final String CACHE_CODE = "QbQuestionTypeHelper";

     /**
     * Populate cache.
     *
     * @param questionTypeIds the question type ids
     * @throws AViewException
     */
    private static synchronized void populateCache(List<QbQuestionType> questionTypeIds) 
    	throws AViewException 
    {
        qbQuestionTypesMap.clear();
        for(QbQuestionType qbQuestionType : questionTypeIds)
        {
        	qbQuestionTypesMap.put(qbQuestionType.getQbQuestionTypeId(), qbQuestionType);
        }        
        CacheHelper.setCache(CACHE_CODE);
    }

    /**
     * Adds the item to cache.
     *
     * @param qbQuestionType the qb question type
     * @throws AViewException
     */
    private static synchronized void addItemToCache(QbQuestionType qbQuestionType) 
    	throws AViewException 
    {
        qbQuestionTypesMap.put(qbQuestionType.getQbQuestionTypeId(),qbQuestionType);
    }
    
    /**
     * Gets the question type ids map.
     *
     * @return the question type ids map
     * @throws AViewException
     */
    private static synchronized Map<Long, QbQuestionType> getQuestionTypeIdsMap() throws AViewException 
    {
        Integer activeSID = StatusHelper.getActiveStatusId();
        // If cache is expired or invalidated
        if (!CacheHelper.isCacheValid(CACHE_CODE)) 
        {
            populateCache(QbQuestionTypeDAO.getAllActiveQbQuestionTypes(activeSID));
        }
        return qbQuestionTypesMap;
    }

    /**
     * Gets the all active qb question types.
     *
     * @return the all active qb question types
     * @throws AViewException
     */
    public static List<QbQuestionType> getAllActiveQbQuestionTypes()
            throws AViewException 
    {
        List<QbQuestionType> qbQuestionTypes = new ArrayList<QbQuestionType>();
        qbQuestionTypes.addAll(getQuestionTypeIdsMap().values());
        return qbQuestionTypes;
    }

    /**
     * Gets the qb question type.
     *
     * @param qbQuestionTypeId the qb question type id
     * @return the qb question type
     * @throws AViewException
     */
    public static QbQuestionType getQbQuestionType(Long qbQuestionTypeId) throws AViewException 
    {
        return getQuestionTypeIdsMap().get(qbQuestionTypeId);
    }

    /**
     * Creates the qb question type.
     *
     * @param qbQuestionType the qb question type
     * @param creatorId the creator id
     * @return the qb question type
     * @throws AViewException
     */
    public static QbQuestionType createQbQuestionType(QbQuestionType qbQuestionType,Long creatorId) 
    	   throws AViewException 
    {
        qbQuestionType.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());

        QbQuestionTypeDAO.createQbQuestionType(qbQuestionType);

        if ((qbQuestionType.getQbQuestionTypeId() != null) && 
        		(qbQuestionType.getQbQuestionTypeId() != 0)) 
        {
            addItemToCache(qbQuestionType);
        }
        logger.debug("Exited createQbQuestionType without throwing any exception:");
        return qbQuestionType;
    }

    /**
     * Update qb question type.
     *
     * @param updateQbQuestionType the update qb question type
     * @param updaterId the updater id
     * @return the qb question type
     * @throws AViewException
     */
    public static QbQuestionType updateQbQuestionType(QbQuestionType updateQbQuestionType,Long updaterId) 
    	   throws AViewException 
    {
    	QbQuestionType qbQuestionType = getQbQuestionType(updateQbQuestionType.getQbQuestionTypeId());
    	if(qbQuestionType != null)
    	{
    		updateQbQuestionType.setModifiedAuditData(updaterId, TimestampUtils.getCurrentTimestamp());
    		QbQuestionTypeDAO.updateQbQuestionType(updateQbQuestionType);
    		addItemToCache(updateQbQuestionType);
    	}
    	else
    	{
    		throw new AViewException("QbQuestionType with id :" + updateQbQuestionType.getQbQuestionTypeId() + ": is not found");
    	}
    	logger.debug("Exited updateQbQuestionType without throwing any exception:");
    	return updateQbQuestionType;
    }  
    
    /*public static void deleteQbQuestionType(Long qbQuestionTypeId, Long updaterId) 
     	   throws AViewException 
     {
    	QbQuestionType qbQuestionType = getQbQuestionType(qbQuestionTypeId);
    	if(qbQuestionType != null)
    	{
    		qbQuestionType.setStatusId(StatusHelper.getDeletedStatusId());
    		qbQuestionType.setModifiedAuditData(updaterId, TimestampUtils.getCurrentTimestamp());
    		qbQuestionType.setQbQuestionTypeName(qbQuestionType.getQbQuestionTypeName() + AppenderUtils.DeleteAppender());    		
    		QbQuestionTypeDAO.updateQbQuestionType(qbQuestionType);    		
    	}
    	else
    	{
    		throw new AViewException("QbQuestionType with id : " + qbQuestionTypeId + " : is not found");
    	}
    	logger.debug("Exited updateQbQuestionType without throwing any exception:");
     }*/
    
    /**
     * Gets the qb question type by name.
     *
     * @param qbQuestionTypeName the qb question type name
     * @return the qb question type by name
     * @throws AViewException
     */
    public static QbQuestionType getQbQuestionTypeByName(String qbQuestionTypeName) throws AViewException
	{
    	return QbQuestionTypeDAO.getQbQuestionTypeByName(qbQuestionTypeName) ;
	}
    
    /**
     * Gets the qb question type by id.
     *
     * @param qbQuestionTypeId the qb question type id
     * @return the qb question type by id
     * @throws AViewException
     */
    public static QbQuestionType getQbQuestionTypeById(Long qbQuestionTypeId) throws AViewException
	{
    	return QbQuestionTypeDAO.getQbQuestionTypeById(qbQuestionTypeId) ;
	}
}
