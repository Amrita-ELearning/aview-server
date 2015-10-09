/*
 * @(#)QuizQuestionHelper.java 4.0 2013/10/05
 * 
 * Copyright  © 2013 E-Learning Research Lab, 
 * Amrita Vishwa Vidyapeetham. All rights reserved. 
 * E-Learning Research Lab and the A-VIEW logo are trademarks or
 * registered trademarks of E-Learning Research Lab. 
 * All other names used are the trademarks of their respective owners.
 */
package edu.amrita.aview.evaluation.helpers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.evaluation.daos.QuizQuestionDAO;
import edu.amrita.aview.evaluation.entities.QuizAnswerChoice;
import edu.amrita.aview.evaluation.entities.QuizQuestion;
import edu.amrita.aview.gclm.helpers.CacheHelper;


/**
 * This class connects the client and server.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QuizQuestionHelper 
{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(QuizQuestionHelper.class);
	
	/** The quiz question map. */
	private static Map<Long,QuizQuestion> quizQuestionMap = Collections.synchronizedMap(new HashMap<Long,QuizQuestion>());
	
	/** The Constant CACHE_CODE. */
	private static final String CACHE_CODE = "QuizQuestionHelper";
	
	/**
	 * Populate cache.
	 *
	 * @param quizQuestionIdMap the quiz question id map
	 * @throws AViewException
	 */
	private static synchronized void populateCache(Map<Long,QuizQuestion> quizQuestionIdMap) throws AViewException
	{
		quizQuestionMap.clear();
		quizQuestionMap.putAll(quizQuestionIdMap);
		CacheHelper.setCache(CACHE_CODE);
	}

	/**
	 * Adds the item to cache.
	 *
	 * @param quizQuestion the quiz question
	 * @throws AViewException
	 */
	private static synchronized void addItemToCache(QuizQuestion quizQuestion) throws AViewException
	{
		quizQuestionMap.put(quizQuestion.getQuizQuestionId(), quizQuestion);
	}

	/**
	 * Removes the item from cache.
	 *
	 * @param quizQuestion the quiz question
	 */
	private static synchronized void removeItemFromCache(QuizQuestion quizQuestion)
	{
		quizQuestionMap.remove(quizQuestion.getQuizQuestionId());
	}

	/**
	 * Quiz questions id map.
	 *
	 * @return the map
	 * @throws AViewException
	 */
	private static synchronized Map<Long,QuizQuestion> quizQuestionsIdMap() throws AViewException
	{  
		if(!CacheHelper.isCacheValid(CACHE_CODE))
		{			           
			List<QuizQuestion> quizQuestions =QuizQuestionDAO.getAllActiveQuizQuestions(StatusHelper.getActiveStatusId());          
			Map<Long,QuizQuestion> quizQuestionIdMap = new HashMap<Long,QuizQuestion>();
			for(QuizQuestion questions:quizQuestions)
			{
				quizQuestionIdMap.put(questions.getQuizQuestionId(), questions);
			}			
			populateCache(quizQuestionIdMap);
		}
		return quizQuestionMap;
	}
	/*private static void populateNames(List<QuizQuestion> quizQuestions,Map<Long,QuizQuestion> quizQuestionIdMap) throws AViewException
    {
        for(QuizQuestion quizQuestion:quizQuestions)
        {
            Long parentId = quizQuestion.getQuizQuestionId();
            Long quizQuestionId = 0l;
            if(parentId != null && parentId != 0)
            {
                quizQuestionId = quizQuestionIdMap.get(parentId).getQuizQuestionId();
            }               
            populateNames(quizQuestion,quizQuestionId);
        }
    }
    public static void populateNames(QuizQuestion quiz,Long quizQuestionId) throws AViewException
    {
        quiz.setQuizQuestionId(quizQuestionId);
    }  */ 
	//---------------------------------------------------------------------

	/**
	 * Gets the quiz question id.
	 *
	 * @param quizQuestionId the quiz question id
	 * @return the quiz question id
	 * @throws AViewException
	 */
	public static QuizQuestion getQuizQuestionId(Long quizQuestionId) throws AViewException
	{
		return QuizQuestionDAO.getQuizQuestionId(quizQuestionId);       
	}

	/**
	 * Creates the quiz question.
	 *
	 * @param quizQuestion the quiz question
	 * @param creatorId the creator id
	 * @return the quiz question
	 * @throws AViewException
	 */
	public static QuizQuestion createQuizQuestion(QuizQuestion quizQuestion,Long creatorId) throws AViewException
	{
		quizQuestion.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());

		printQuizQuestion(quizQuestion);

		QuizQuestionDAO.createQuizQuestion(quizQuestion);

		if((quizQuestion.getQuizQuestionId() != null) && 
				(quizQuestion.getQuizQuestionId() != 0))
		{			
			addItemToCache(quizQuestion);
		}
		logger.debug("Exited createQuizQuestion without throwing any exception:");
		return quizQuestion; 
	}

	/**
	 * Update quiz question.
	 *
	 * @param updateQuizQuestion the update quiz question
	 * @param updaterId the updater id
	 * @throws AViewException
	 */
	public static void updateQuizQuestion(QuizQuestion updateQuizQuestion,Long updaterId) throws AViewException
	{    
		QuizQuestion quizQuestion = getQuizQuestionId(updateQuizQuestion.getQuizQuestionId());
		if(quizQuestion != null)
		{
			printQuizQuestion(quizQuestion);
			quizQuestion.updateFrom(updateQuizQuestion);
			quizQuestion.setModifiedAuditData(updaterId, TimestampUtils.getCurrentTimestamp());
			printQuizQuestion(quizQuestion);
			QuizQuestionDAO.updateQuizQuestion(quizQuestion);			
			addItemToCache(quizQuestion); 			
		}
	}

	/**
	 * Delete quiz questions.
	 *
	 * @param quizQuestionId the quiz question id
	 * @param modifiedByUserId the modified by user id
	 * @throws AViewException
	 */
	public static void deleteQuizQuestions(Long quizQuestionId,Long modifiedByUserId) throws AViewException
	{
		QuizQuestion quizQuestion = getQuizQuestionId(quizQuestionId); 
		if(quizQuestion != null)
		{            
			quizQuestion.setStatusId(StatusHelper.getDeletedStatusId());
			quizQuestion.setModifiedAuditData(modifiedByUserId, TimestampUtils.getCurrentTimestamp());
			QuizQuestionDAO.updateQuizQuestion(quizQuestion);
			removeItemFromCache(quizQuestion);
		}
		else
		{
			throw new AViewException("Quiz Question with id :"+quizQuestionId+": is not found");
		}

	}

	/**
	 * Prints the quiz question.
	 *
	 * @param quizQuestion the quiz question
	 * @throws AViewException
	 */
	private static void printQuizQuestion(QuizQuestion quizQuestion) throws AViewException
	{
		logger.debug("QuizQuestion:"+quizQuestion.toString());

		if(quizQuestion.getQuizAnswerChoices() != null)
		{
			logger.debug("QuizAnswerChoice:"+quizQuestion.getQuizAnswerChoices().size());

			for(QuizAnswerChoice qac:quizQuestion.getQuizAnswerChoices())
			{
				logger.debug("QuizAnswerChoice:"+qac.toString());
			}
		}

	}
	
	/**
	 * Gets the all active quiz questions.
	 *
	 * @return the all active quiz questions
	 * @throws AViewException
	 */
	public static List<QuizQuestion> getAllActiveQuizQuestions() throws AViewException
	{
		Integer activeSId = StatusHelper.getActiveStatusId() ;
		List<QuizQuestion> quizQuestions = QuizQuestionDAO.getAllActiveQuizQuestions(activeSId);
		return quizQuestions ;
	}

	/**
	 * Gets the quiz questions for quiz.
	 *
	 * @param quizId the quiz id
	 * @return the quiz questions for quiz
	 * @throws AViewException
	 */
	public static List<QuizQuestion> getQuizQuestionsForQuiz(Long quizId) throws AViewException
	{
		Integer activeSId = StatusHelper.getActiveStatusId() ;
		List<QuizQuestion> quizQuestions = QuizQuestionDAO.getQuizQuestionsForQuiz(quizId,activeSId) ;
		return quizQuestions ;
	}
	
	/**
	 * Gets the polling quiz for student.
	 *
	 * @param userId the user id
	 * @return the polling quiz for student
	 * @throws AViewException
	 */
	public static List<QuizQuestion> getPollingQuizForStudent(Long userId) throws AViewException
	{
		Integer activeSId = StatusHelper.getActiveStatusId() ;
		List<QuizQuestion> quizQuestions = QuizQuestionDAO.getPollingQuizForStudent(userId) ;
		return quizQuestions ;
	}
	
	/**
	 * Clear cache.
	 */
	public static void clearCache()
	{
		logger.debug("Entering QuizQuestionHelper::clearCache");
		quizQuestionMap = null;
		logger.debug("Entering QuizQuestionHelper::clearCache");
	}

}
