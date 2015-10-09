/*
 * @(#)QbAnswerChoiceHelper.java 4.0 2013/10/16
 * 
 * Copyright  © 2013 E-Learning Research Lab, 
 * Amrita Vishwa Vidyapeetham. All rights reserved. 
 * E-Learning Research Lab and the A-VIEW logo are trademarks or
 * registered trademarks of E-Learning Research Lab. 
 * All other names used are the trademarks of their respective owners.
 */
package edu.amrita.aview.evaluation.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.AppenderUtils;
import edu.amrita.aview.common.utils.HashCodeUtils;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.evaluation.daos.QbAnswerChoiceDAO;
import edu.amrita.aview.evaluation.entities.QbAnswerChoice;
import edu.amrita.aview.gclm.helpers.CacheHelper;



/**
 * This class connects the client and server.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QbAnswerChoiceHelper {
    
    /** The logger. */
    private static Logger logger = Logger.getLogger(QbAnswerChoiceHelper.class);

    /** The qb answer choices map. */
    private static Map<Long, QbAnswerChoice> qbAnswerChoicesMap = Collections.synchronizedMap(new HashMap<Long, QbAnswerChoice>());

    /** The Constant CACHE_CODE. */
    private static final String CACHE_CODE = "QbAnswerChoiceHelper";
 
    /**
     * Populate cache.
     *
     * @param qbAnswerChoicesIdMap the qb answer choices id map
     * @throws AViewException
     */
    private static synchronized void populateCache(Map<Long, QbAnswerChoice> qbAnswerChoicesIdMap)throws AViewException 
    {
        qbAnswerChoicesMap.clear();
        qbAnswerChoicesMap.putAll(qbAnswerChoicesIdMap);
        CacheHelper.setCache(CACHE_CODE);
    }

    /**
     * Adds the item to cache.
     *
     * @param qbAnswerChoice the qb answer choice
     * @throws AViewException
     */
    private static synchronized void addItemToCache(QbAnswerChoice qbAnswerChoice) throws AViewException 
    {
        qbAnswerChoicesMap.put(qbAnswerChoice.getQbAnswerChoiceId(),qbAnswerChoice);
    }
    
    /**
     * Removes the item from cache.
     *
     * @param qbAnswerChoice the qb answer choice
     * @throws AViewException
     */
    private static synchronized void removeItemFromCache(QbAnswerChoice qbAnswerChoice) throws AViewException
    {
    	qbAnswerChoicesMap.remove(qbAnswerChoice.getQbAnswerChoiceId());
    }

    /**
     * Gets the qb answer choices id map.
     *
     * @return the qb answer choices id map
     * @throws AViewException
     */
    private static synchronized Map<Long, QbAnswerChoice> getQbAnswerChoicesIdMap()
            throws AViewException 
    {
        Integer activeSId = StatusHelper.getActiveStatusId();
        if (!CacheHelper.isCacheValid(CACHE_CODE)) 
        {            
            List<QbAnswerChoice> qbAnswerChoices = QbAnswerChoiceDAO.getAllActiveQbAnswersChoices(activeSId, activeSId);

            Map<Long, QbAnswerChoice> qbAnswerChoicesIdMap = new HashMap<Long, QbAnswerChoice>();
            for (QbAnswerChoice qbAnswerChoice : qbAnswerChoices) 
            {
            	qbAnswerChoicesIdMap.put(qbAnswerChoice.getQbAnswerChoiceId(),qbAnswerChoice);
            }            
            populateCache(qbAnswerChoicesIdMap);

        }
        return qbAnswerChoicesMap;
    }

    /**
     * Creates the qb answer choice.
     *
     * @param qbAnswerChoice the qb answer choice
     * @param creatorId the creator id
     * @throws AViewException
     */
    public static void createQbAnswerChoice(QbAnswerChoice qbAnswerChoice,Long creatorId) throws AViewException 
    {
        qbAnswerChoice.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
        QbAnswerChoiceDAO.createQbAnswerChoice(qbAnswerChoice);
        if ((qbAnswerChoice.getQbAnswerChoiceId() != null) && 
        		(qbAnswerChoice.getQbAnswerChoiceId() != 0) )
        {
            addItemToCache(qbAnswerChoice);
        }
        logger.debug("Exited createQbAnswerChoice without throwing any exception:");
    }
    
    /**
     * Delete qb answer choice.
     *
     * @param qbAnswerChoice the qb answer choice
     * @param updaterId the updater id
     * @throws AViewException
     */
    public static void deleteQbAnswerChoice(QbAnswerChoice qbAnswerChoice, Long updaterId) throws AViewException 
    {
    	qbAnswerChoice.setChoiceText(qbAnswerChoice.getChoiceText() + AppenderUtils.DeleteAppender());
    	setQbAnswerChoiceTextHash(qbAnswerChoice,null);
    	updateQbAnswerChoice(qbAnswerChoice, updaterId);
    	removeItemFromCache(qbAnswerChoice);
        logger.debug("Exited deleteQbAnswerChoice without throwing any exception:");
    }

    /**
     * Gets the all active qb answers choices.
     *
     * @return the all active qb answers choices
     * @throws AViewException
     */
    public static List<QbAnswerChoice> getAllActiveQbAnswersChoices()throws AViewException 
    {
        List<QbAnswerChoice> qbAnswerChoices = new ArrayList<QbAnswerChoice>();
        qbAnswerChoices.addAll(getQbAnswerChoicesIdMap().values());
        return qbAnswerChoices;
    }
 
    /**
     * Update qb answer choice.
     *
     * @param qbAnswerChoice the qb answer choice
     * @param updaterId the updater id
     * @throws AViewException
     */
    public static void updateQbAnswerChoice(QbAnswerChoice qbAnswerChoice,Long updaterId) throws AViewException 
    {
    	 qbAnswerChoice.setModifiedAuditData(updaterId, TimestampUtils.getCurrentTimestamp());
         QbAnswerChoiceDAO.updateQbAnswerChoice(qbAnswerChoice);       
         addItemToCache(qbAnswerChoice);       
         logger.debug("Exited updateQbAnswerChoice without throwing any exception:");
    }

    /**
     * Gets the qb answers choices.
     *
     * @param qbQuestionId the qb question id
     * @return the qb answers choices
     * @throws AViewException
     */
    public static List<QbAnswerChoice> getQbAnswersChoices(Long qbQuestionId) throws AViewException 
    {
        Integer activeSId = StatusHelper.getActiveStatusId();
        return  QbAnswerChoiceDAO.getQbAnswersChoices(qbQuestionId, activeSId);
    }
    
    //Fix for bug 10929, 10926, 10922
    //Sending a dummy random string while computing hash during update. Once the answer choice is updated with
    //again the update is called with random string as null.
    //This is to make sure that, update works properly if the change is not related to unique key constraints.
	 /**
     * Sets the qb answer choice text hash.
     *
     * @param qbAnswerChoices the new qb answer choice text hash
     * @param random the random
     * @throws AViewException
     */
    public static void setQbAnswerChoiceTextHash(Collection<QbAnswerChoice> qbAnswerChoices,String random) throws AViewException
    {
    	for(QbAnswerChoice qbAnswerChoice : qbAnswerChoices)
    	{
    		setQbAnswerChoiceTextHash(qbAnswerChoice,random);
    	}
    }
   
    //Fix for bug 10929, 10926, 10922
    //Sending a dummy random string while computing hash during update. Once the answer choice is updated with
    //again the update is called with random string as null. 
    //This is to make sure that, update works properly if the change is not related to unique key constraints.
	 /**
     * Sets the qb answer choice text hash.
     *
     * @param qbAnswerChoice the new qb answer choice text hash
     * @param random the random
     * @throws AViewException
     */
    public static void setQbAnswerChoiceTextHash(QbAnswerChoice qbAnswerChoice,String random) throws AViewException
    {
    	qbAnswerChoice.setChoiceTextHash(HashCodeUtils.SHA1(qbAnswerChoice.getChoiceText()+((random==null)?"":random)));    	
    }
    
    /**
     * Removes the qb answer choice by qb question.
     *
     * @param qbAnswerChoices the qb answer choices
     * @param updatedId the updated id
     * @throws AViewException
     */
    public static void removeQbAnswerChoiceByQbQuestion(Set<QbAnswerChoice> qbAnswerChoices, Long updatedId) throws AViewException
    {
    	for(QbAnswerChoice qbAnswerChoice : qbAnswerChoices)
    	{
    		deleteQbAnswerChoice(qbAnswerChoice, updatedId);
    	}
    }
    
    /**
     * Clear cache.
     */
    public static void clearCache()
    {
    	logger.debug("Entering QbAnswerChoiceHelper::clearCache");
    	qbAnswerChoicesMap = null;
    	logger.debug("Entering QbAnswerChoiceHelper::clearCache");
    }
}
