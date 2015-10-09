/*
 * @(#)QuestionPaperQuestionHelper.java 4.0 2013/10/11
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
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.evaluation.QuizConstant;
import edu.amrita.aview.evaluation.daos.QuestionPaperQuestionDAO;
import edu.amrita.aview.evaluation.entities.QbCategory;
import edu.amrita.aview.evaluation.entities.QbQuestion;
import edu.amrita.aview.evaluation.entities.QbSubcategory;
import edu.amrita.aview.evaluation.entities.QuestionPaper;
import edu.amrita.aview.evaluation.entities.QuestionPaperQuestion;
import edu.amrita.aview.evaluation.vo.QuestionPaperQuestionListVO;
import edu.amrita.aview.gclm.helpers.CacheHelper;



/**
 * This class connects the client and server.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QuestionPaperQuestionHelper 
{
    
    /** The logger. */
    private static Logger logger = Logger.getLogger(QuestionPaperQuestionHelper.class);

    /** The question paper questions map. */
    private static Map<Long, QuestionPaperQuestion> questionPaperQuestionsMap = Collections.synchronizedMap(new HashMap<Long, QuestionPaperQuestion>());

    /** The Constant CACHE_CODE. */
    private static final String CACHE_CODE = "QuestionPaperQuestionHelper";
  
    /**
     * Populate cache.
     *
     * @param questionPaperQuestionIdsMap the question paper question ids map
     * @throws AViewException
     */
    private static synchronized void populateCache(Map<Long, QuestionPaperQuestion> questionPaperQuestionIdsMap) throws AViewException 
     {
        questionPaperQuestionsMap.clear();
        questionPaperQuestionsMap.putAll(questionPaperQuestionIdsMap);
        CacheHelper.setCache(CACHE_CODE);
    }

    /**
     * Adds the item to cache.
     *
     * @param questionPaperQuestion the question paper question
     * @throws AViewException
     */
    private static synchronized void addItemToCache(QuestionPaperQuestion questionPaperQuestion) throws AViewException 
    {
        questionPaperQuestionsMap.put(questionPaperQuestion.getQuestionPaper().getQuestionPaperId(), questionPaperQuestion);
    }
    
    /**
     * Removes the item from cache.
     *
     * @param qpq the qpq
     */
    private static synchronized void removeItemFromCache(QuestionPaperQuestion qpq)
	{
    	questionPaperQuestionsMap.remove(qpq.getQuestionPaperQuestionId());
	}
    
    /**
     * Question paper question id's map.
     *
     * @return the map
     * @throws AViewException
     */
    private static synchronized Map<Long, QuestionPaperQuestion> questionPaperQuestionIdsMap() throws AViewException 
    {
        Integer statusId = StatusHelper.getActiveStatusId();
        // If cache is expired or invalidated
        if (!CacheHelper.isCacheValid(CACHE_CODE)) 
        {            
            List<QuestionPaperQuestion> questionPapers = QuestionPaperQuestionDAO.getAllActiveQuestionPaperQuestions(null, 
            		null, null, null, null, null, null, statusId);
            // Populate the Map
            Map<Long, QuestionPaperQuestion> questionPaperIdsMap = new HashMap<Long, QuestionPaperQuestion>();
            for (QuestionPaperQuestion questionPapaer : questionPapers) 
            {
            	questionPaperIdsMap.put(questionPapaer.getQuestionPaperQuestionId(), questionPapaer);
            }
            populateCache(questionPaperIdsMap);           
        }
        return questionPaperQuestionsMap;
    }

    /**
     * Populate names.
     *
     * @param questionPaperQuestions the question paper questions
     * @throws AViewException
     */
    public static void populateNames(List<QuestionPaperQuestion> questionPaperQuestions) throws AViewException 
    {
    	for (QuestionPaperQuestion questionPaperQuestion : questionPaperQuestions) 
        {
    		populateNames(questionPaperQuestion); 
        }
    }
    
    /**
     * Populate names.
     *
     * @param questionPaperQuestion the question paper question
     * @throws AViewException
     */
    public static void populateNames(QuestionPaperQuestion questionPaperQuestion) throws AViewException 
    {
    	if(questionPaperQuestion.getQbQuestionId() != null)
		{
			//difficulty level name
    		QbQuestion qbquestion=QbQuestionHelper.getDifficultyLevelById(questionPaperQuestion.getQbQuestionId());
    		questionPaperQuestion.setQuestionText(qbquestion.getQuestionText());
    		questionPaperQuestion.setQbDifficultyLevelId(qbquestion.getQbDifficultyLevelId()) ;
    		questionPaperQuestion.setQbDifficultyLevelName(qbquestion.getQbDifficultyLevelName());
    		questionPaperQuestion.setQbQuestionTypeId(qbquestion.getQbQuestionTypeId()) ;
    		questionPaperQuestion.setQbQuestionTypeName(qbquestion.getQbQuestionTypeName());
    		// Retrieve category details for a sub category
    		QbSubcategory qbSubcat =QbSubcategoryHelper.getQbSubcategoryById(qbquestion.getQbSubcategoryId()) ; 
    		QbCategory qbCat = QbCategoryHelper.getQbCategoryById(qbSubcat.getQbCategoryId()) ;
    		questionPaperQuestion.setQbCategoryId(qbCat.getQbCategoryId()) ;
    		questionPaperQuestion.setQbCategoryName(qbCat.getQbCategoryName()) ;
		}
    	else
    	{
    		QbSubcategory qbSubcat =QbSubcategoryHelper.getQbSubcategoryById(questionPaperQuestion.getQbSubcategoryId()) ; 
    		QbCategory qbCat = QbCategoryHelper.getQbCategoryById(qbSubcat.getQbCategoryId()) ;
    		questionPaperQuestion.setQbCategoryId(qbCat.getQbCategoryId()) ;
    		questionPaperQuestion.setQbCategoryName(qbCat.getQbCategoryName()) ;
    	}
    }

    /**
     * Gets the all active question paper questions.
     *
     * @return the all active question paper questions
     * @throws AViewException
     */
    public static List<QuestionPaperQuestion> getAllActiveQuestionPaperQuestions() throws AViewException 
    {
        List<QuestionPaperQuestion> questionPaperQuestion = new ArrayList<QuestionPaperQuestion>();
        questionPaperQuestion.addAll(questionPaperQuestionIdsMap().values());
        return questionPaperQuestion;
    }

    /**
     * Gets the question paper question.
     *
     * @param questionPaperQuestionId the question paper question id
     * @return the question paper question
     * @throws AViewException
     */
    public static QuestionPaperQuestion getQuestionPaperQuestion(Long questionPaperQuestionId) throws AViewException 
    {
        return QuestionPaperQuestionDAO.getQuestionPaperQuestion(questionPaperQuestionId);
    }

    /**
     * Creates the question paper question.
     *
     * @param questionPaperQuestion the question paper question
     * @param creatorId the creator id
     * @throws AViewException
     */
    public static void createQuestionPaperQuestion(QuestionPaperQuestion questionPaperQuestion, Long creatorId) throws AViewException 
    {
        questionPaperQuestion.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());

        QuestionPaperQuestionDAO.createQuestionPaperQuestion(questionPaperQuestion);

        if ( (questionPaperQuestion.getQuestionPaper().getQuestionPaperId() != null ) && 
        		(questionPaperQuestion.getQuestionPaper().getQuestionPaperId() != 0) ) 
        {          
            addItemToCache(questionPaperQuestion);
        }
        logger.debug("Exited createQuestionPaperQuestion without throwing any exception:");

    }

    /**
     * Update question paper question.
     *
     * @param updateQuestionPaperQuestion the update question paper question
     * @param updaterId the updater id
     * @return the question paper question
     * @throws AViewException
     */
    public static QuestionPaperQuestion updateQuestionPaperQuestion(QuestionPaperQuestion updateQuestionPaperQuestion, Long updaterId) throws AViewException 
    {
        QuestionPaperQuestion questionPaperQuestion = getQuestionPaperQuestion(updateQuestionPaperQuestion.getQuestionPaperQuestionId());        
        if (questionPaperQuestion != null) 
        {
        	updateQuestionPaperQuestion.setModifiedAuditData(updaterId,TimestampUtils.getCurrentTimestamp());
            QuestionPaperQuestionDAO.updateQuestionPaperQuestion(updateQuestionPaperQuestion);            
            addItemToCache(updateQuestionPaperQuestion);
        }
        else 
        {
            throw new AViewException("QuestionPaperQuestion is not found");
        }
        logger.debug("Exited updateQuestionPaperQuestion without throwing any exception:");
        return questionPaperQuestion;
    }

    /**
     * Gets the all active specific questions for question paper.
     *
     * @param questionPaperId the question paper id
     * @return the all active specific questions for question paper
     * @throws AViewException
     */
    public static List<QuestionPaperQuestionListVO> getAllActiveSpecificQuestionsForQuestionPaper(Long questionPaperId) throws AViewException
	{
		Integer activeSId = StatusHelper.getActiveStatusId() ;
		return QuestionPaperQuestionDAO.getAllActiveSpecificQuestionsForQuestionPaper(questionPaperId, activeSId);		
	}
    
    /**
     * Gets the all active question paper questions for question paper.
     *
     * @param questionPaperId the question paper id
     * @param creatorId the creator id
     * @return the all active question paper questions for question paper
     * @throws AViewException
     */
    public static List<QuestionPaperQuestion> getAllActiveQuestionPaperQuestionsForQP(Long questionPaperId,Long creatorId) throws AViewException 
    {
		Integer activeSId = StatusHelper.getActiveStatusId() ;
		List<QuestionPaperQuestion> questionPaperQuestions = QuestionPaperQuestionDAO.getAllActiveQuestionPaperQuestionsForQP(questionPaperId, creatorId,activeSId);
    	populateNames(questionPaperQuestions);
		return questionPaperQuestions;
	}
    
    /**
     * Gets the question bank question from question paper by pattern type.
     *
     * @param patternType the pattern type
     * @param questionPaperId the question paper id
     * @return the question bank question from question paper by pattern type
     * @throws AViewException
     */
    public static List<QuestionPaperQuestion> getQbQuestionFromQuestionPaperByPatternType(String patternType, Long questionPaperId) throws AViewException
    {
    	Integer activeSId = StatusHelper.getActiveStatusId();
    	return QuestionPaperQuestionDAO.getQbQuestionFromQuestionPaperByPatternType(patternType, questionPaperId, activeSId);
    }
    
    /**
     * Gets the sub category id for random question paper questions.
     *
     * @param questionPaperId the question paper id
     * @return the sub category id for random question paper questions
     * @throws AViewException
     */
    public static List<Long> getSubcategoryIdForRandomQuestionPaperQuestions(Long questionPaperId) throws AViewException
	{
    	Integer activeSId = StatusHelper.getActiveStatusId();
    	List<QuestionPaperQuestion> questionPaperQuestions = QuestionPaperQuestionDAO.getAllActiveQuestionPaperQuestions(null, null, null, null, null, questionPaperId, QuizConstant.RANDOM_PATTERN_TYPE, activeSId);
    	List<Long> subCategoryIds = new ArrayList<Long>();
    	for(QuestionPaperQuestion questionPaperQuestion : questionPaperQuestions)
    	{
    		subCategoryIds.add(questionPaperQuestion.getQbSubcategoryId());
    	}
    	return subCategoryIds;
	}
    
    /**
     * Delete question paper question questions.
     *
     * @param deleteQbQuestions the delete question bank questions
     * @param modifiedByUserId the modified by user id
     * @throws AViewException
     */
    public static void deleteQpqQuestions(List<QuestionPaperQuestion> deleteQbQuestions, Long modifiedByUserId) throws AViewException 
    {  
    	//Fix for Bug#16096,16097,16101
    	List<QuestionPaperQuestion> qpqList = new ArrayList<QuestionPaperQuestion>();
    	for(QuestionPaperQuestion qpq : deleteQbQuestions)
    	{
	        if (qpq != null)
	        {	
	        	if (!(checkIfqpqExistsInQuizQP(qpq.getQuestionPaperQuestionId())))
	        	{
	        		//Fix for Bug#16096,16097,16101:Start
					//qpq.setStatusId(StatusHelper.getDeletedStatusId());
					//qpq.setModifiedAuditData(modifiedByUserId, TimestampUtils.getCurrentTimestamp());
					//QuestionPaperQuestionDAO.updateQuestionPaperQuestion(qpq);
	        		qpqList.add(qpq);
	        		//Fix for Bug#16096,16097,16101:End
	        		removeItemFromCache(qpq);
	        	}
	         else
	 	       {
	 	       	throw new AViewException("Question cannot be deleted. please try with some other question") ;
	 	       }
	        } 
	       
	        else 
	        {
	            throw new AViewException("QbQuestion is not found");
	        }
    	}
    	//Fix for Bug#16096,16097,16101:Start
    	if(qpqList.size() > 0)
    	{
    		QuestionPaperQuestionDAO.deleteQuestionPaperQuestions(qpqList);	            
    		//Fix for Bug #19653 Start
    		//updating the questionPaper
    		Long questionPaperId = qpqList.get(0).getQuestionPaper().getQuestionPaperId();
    		QuestionPaper updateQuestionPaper = QuestionPaperHelper.getQuestionPaperId(questionPaperId);
    		updateQuestionPaper.setIsComplete("N");
    		QuestionPaperHelper.updateQuestionPaper(updateQuestionPaper,modifiedByUserId);
    		//Fix for Bug #19653 End
    	}
    	//Fix for Bug#16096,16097,16101:End    	
    }
    
    /**
     * Check if question paper question exists in quiz question paper.
     *
     * @param questionPaperQestionId the question paper qestion id
     * @return the boolean
     * @throws AViewException
     */
    public static Boolean checkIfqpqExistsInQuizQP(Long questionPaperQestionId) throws AViewException
   	{
       	return QuestionPaperQuestionDAO.checkIfqpqExistsInQuizQP(questionPaperQestionId) ;
   	}
    
    /**
     * Clear cache.
     */
    public static void clearCache()
    {
    	logger.debug("Entered QuestionPaperQuestionHelper.clearCache()");
    	questionPaperQuestionsMap.clear();
    	logger.debug("Entered QuestionPaperQuestionHelper.clearCache()");
    }
}
