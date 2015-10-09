/*
 * @(#)QbQuestionHelper.java 4.0 2013/09/19
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.AppenderUtils;
import edu.amrita.aview.common.utils.HashCodeUtils;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.evaluation.QuizConstant;
import edu.amrita.aview.evaluation.daos.QbQuestionDAO;
import edu.amrita.aview.evaluation.entities.QbAnswerChoice;
import edu.amrita.aview.evaluation.entities.QbDifficultyLevel;
import edu.amrita.aview.evaluation.entities.QbQuestion;
import edu.amrita.aview.evaluation.entities.QbQuestionMediaFile;
import edu.amrita.aview.evaluation.entities.QbQuestionType;
import edu.amrita.aview.gclm.helpers.CacheHelper;



/**
 * It acts as the connector class between client side and server side.
 * 
 * @version 4.0
 * @author Swati
 * @since 3.0
 */
public class QbQuestionHelper {
	
    /** The logger. */
    private static Logger logger = Logger.getLogger(QbQuestionHelper.class);
    
    /** The qb questions map. */
    private static Map<Long, QbQuestion> qbQuestionsMap = Collections.synchronizedMap(new HashMap<Long, QbQuestion>());
    
    /** The Constant CACHE_CODE. */
    private static final String CACHE_CODE = "QbQuestionHelper";

    /**
     * Populate cache.
     *
     * @param qbQuestionsIdsMap the qb questions ids map
     * @throws AViewException
     */
    private static synchronized void populateCache(Map<Long, QbQuestion> qbQuestionsIdsMap) throws AViewException 
    {
        qbQuestionsMap.clear();
        qbQuestionsMap.putAll(qbQuestionsIdsMap);
        CacheHelper.setCache(CACHE_CODE);
    }

    /**
     * Adds the item to cache.
     *
     * @param qbQuestion the qb question
     */
    private static synchronized void addItemToCache(QbQuestion qbQuestion) 
    {
        qbQuestionsMap.put(qbQuestion.getQbQuestionId(), qbQuestion);
    }
    
	/**
	 * Removes the item from cache.
	 *
	 * @param qbQuestion the qb question
	 */
	private static synchronized void removeItemFromCache(QbQuestion qbQuestion)
	{
		qbQuestionsMap.remove(qbQuestion.getQbQuestionId());
	}
		
    /**
     * Gets the qb questions id map.
     *
     * @return the qb questions id map
     * @throws AViewException
     */
    private static synchronized Map<Long, QbQuestion> getQbQuestionsIdMap() throws AViewException 
    {
        Integer statusId = StatusHelper.getActiveStatusId();
        // If cache is expired or invalidated
        if (!CacheHelper.isCacheValid(CACHE_CODE)) 
        {
            List<QbQuestion> qbQuestions = QbQuestionDAO.getAllActiveQbQuestions(statusId);
            Map<Long, QbQuestion> qbQuestionsIdsMap = new HashMap<Long, QbQuestion>();
            for (QbQuestion qbQuestion : qbQuestions)
            {
            	populateName(qbQuestion);
            	qbQuestionsIdsMap.put(qbQuestion.getQbQuestionId(), qbQuestion);
            }
            populateCache(qbQuestionsIdsMap);
        }
        return qbQuestionsMap;
    }
    
    /**
     * Populate names.
     *
     * @param qbQuestions the qb questions
     * @throws AViewException
     */
    public static void populateNames(List<QbQuestion> qbQuestions) throws AViewException 
    {
    	for (QbQuestion qbQuestion : qbQuestions) 
        {
	       populateName(qbQuestion); 
        }
    }
    
    /**
     * Gets the difficulty level by id.
     *
     * @param qbQuestionId the qb question id
     * @return the difficulty level by id
     * @throws AViewException
     */
    public static QbQuestion getDifficultyLevelById(Long qbQuestionId)
            throws AViewException 
    {
    	return getQbQuestionsIdMap().get(qbQuestionId);
    }
    
    /**
     * Populate the transient attributes referred by qbQuestion entity.
     *
     * @param qbQuestion the qb question
     * @throws AViewException
     */
    public static void populateName(QbQuestion qbQuestion) throws AViewException 
    {
    	if(qbQuestion.getQbDifficultyLevelId() != null)
		{
			//get difficulty level name
			QbDifficultyLevel qbDifficultyLevel = QbDifficultyLevelHelper.getDifficultyLevelById(qbQuestion.getQbDifficultyLevelId());
			qbQuestion.setQbDifficultyLevelName(qbDifficultyLevel.getQbDifficultyLevelName());
		}
		if(qbQuestion.getQbQuestionTypeId() != null)
		{
			//get question type name
			QbQuestionType qbQuestionType = QbQuestionTypeHelper.getQbQuestionTypeById(qbQuestion.getQbQuestionTypeId());
			qbQuestion.setQbQuestionTypeName(qbQuestionType.getQbQuestionTypeName());
		}
		/*List<QbQuestionMediaFile> qbQuestionMediaFiles = QbQuestionMediaFileHelper.getQbQuestionMediaFileForQbQuestionId(qbQuestion.getQbQuestionId());
		if (qbQuestionMediaFiles != null && qbQuestionMediaFiles.size() > 0) 
		{
			Set<QbQuestionMediaFile> dummyqbQuestionMediaFiles = new HashSet<QbQuestionMediaFile>();
			dummyqbQuestionMediaFiles.addAll(qbQuestionMediaFiles);
			qbQuestion.setQbQuestionMediaFiles(dummyqbQuestionMediaFiles);
		}*/
	}

    /**
     * Prints the qb question.
     *
     * @param qbQuestion the qb question
     * @throws AViewException
     */
    private static void printQbQuestion(QbQuestion qbQuestion)throws AViewException 
    {
        logger.debug("QbQuestion:" + qbQuestion.toString());
        if (qbQuestion.getQbAnswerChoices() != null) 
        {
            logger.debug("QbAnswerChoice:"+ qbQuestion.getQbAnswerChoices().size());
            for (QbAnswerChoice qac : qbQuestion.getQbAnswerChoices()) 
            {
                logger.debug("QbAnswerChoice:" + qac.toString());
            }
        }
    }

    /**
     * Gets the all active qb questions.
     *
     * @return the all active qb questions
     * @throws AViewException
     */
    public static List<QbQuestion> getAllActiveQbQuestions() throws AViewException
    {
    	List<QbQuestion> qbQuestions = new ArrayList<QbQuestion>();
    	qbQuestions.addAll(getQbQuestionsIdMap().values());
    	return qbQuestions;
    }
   
    /**
     * Creates the qb question.
     *
     * @param qbQuestion the qb question
     * @param qbAnswerChoices the qb answer choices
     * @param creatorId the creator id
     * @return the qb question
     * @throws AViewException
     */
    public static QbQuestion createQbQuestion(QbQuestion qbQuestion, List<QbAnswerChoice>qbAnswerChoices, Long creatorId) throws AViewException 
   {    
    	// set the question text hash using SHA1 encryption technique
    	qbQuestion.setQuestionTextHash(HashCodeUtils.SHA1(qbQuestion.getQuestionText().toLowerCase()));
    	//clear the answer choices populated from client side
    	// so that the answer choices get populated without any discrepancy
    	qbQuestion.getQbAnswerChoices().clear() ;    	
    	// set the answer choice text hash    	
    	QbAnswerChoiceHelper.setQbAnswerChoiceTextHash(qbAnswerChoices,null);
        for(QbAnswerChoice qbAnswerChoice : qbAnswerChoices)
        {
        	qbQuestion.addQbAnswerChoices(qbAnswerChoice);
    	}
    	/*if (qbQuestion.getQbQuestionMediaFiles() != null && qbQuestion.getQbQuestionMediaFiles().size() > 0) 
    	{
			for (QbQuestionMediaFile qbQuestionMediaFile : qbQuestion.getQbQuestionMediaFiles())
			{
				qbQuestion.addQbQuestionMediaFiles(qbQuestionMediaFile);
			}
		}*/
    	qbQuestion.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
        QbQuestionDAO.createQbQuestion(qbQuestion);
        populateName(qbQuestion);
        addItemToCache(qbQuestion);
        logger.debug("Exited createQbQuestion without throwing any exception:");
        return qbQuestion;
    }

    /**
     * Creates the qb question for polling.
     *
     * @param qbQuestion the qb question
     * @param qbAnswerChoices the qb answer choices
     * @param creatorId the creator id
     * @return the qb question
     * @throws AViewException
     */
    public static QbQuestion createQbQuestionForPolling(QbQuestion qbQuestion, List<QbAnswerChoice>qbAnswerChoices, Long creatorId) throws AViewException 
    {    	
    	// The following are transient variables which have to be set , 
    	// since they are null from client side
    	Long qbSubcategoryId = QbSubcategoryHelper.getQbSubcategoryForName(QuizConstant.POLLING_QUESTION_TYPE).getQbSubcategoryId() ;
    	qbQuestion.setQbSubcategoryId(qbSubcategoryId) ; /* Set the sub-category for polling question type*/
    	Long qbDifficultyLevelId = QbDifficultyLevelHelper.getQbDfLevelForName(QuizConstant.EASY_DIFFICULTY_LEVEL).getQbDifficultyLevelId() ;
    	qbQuestion.setQbDifficultyLevelId(qbDifficultyLevelId) ; /* Set the difficulty level for polling question type*/
    	Long qbQuestionTypeId = QbQuestionTypeHelper.getQbQuestionTypeByName(QuizConstant.POLLING_QUESTION_TYPE).getQbQuestionTypeId() ;     	
    	qbQuestion.setQbQuestionTypeId(qbQuestionTypeId) ; /* Set the question type for polling question type */
     	qbQuestion.setQuestionTextHash(HashCodeUtils.SHA1(qbQuestion.getQuestionText().toLowerCase()));
     	//clear the answer choices populated from client side
     	qbQuestion.getQbAnswerChoices().clear() ;
     	// set the answer choice text hash
     	QbAnswerChoiceHelper.setQbAnswerChoiceTextHash(qbAnswerChoices,null);
         for(QbAnswerChoice qbAnswerChoice : qbAnswerChoices)
         {
         	qbQuestion.addQbAnswerChoices(qbAnswerChoice);
     	}
     	qbQuestion.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
        QbQuestionDAO.createQbQuestion(qbQuestion);
        populateName(qbQuestion);
        addItemToCache(qbQuestion);
        logger.debug("Exited createQbQuestion without throwing any exception:");
        return qbQuestion;
     }
         
    /**
     * Update qb question.
     *
     * @param updatedQbQuestion the updated qb question
     * @param qbAnswerChoices the qb answer choices
     * @param updaterId the updater id
     * @return the qb question
     * @throws AViewException
     */
    public static QbQuestion updateQbQuestion(QbQuestion updatedQbQuestion, List<QbAnswerChoice>qbAnswerChoices, Long updaterId) throws AViewException 
    {
    	Session session = null;
		QbQuestion qbQuestion = null;
		try 
		{
			qbQuestion = getQbQuestion(updatedQbQuestion.getQbQuestionId());
			// set the question text hash using SHA1 encryption technique
	        updatedQbQuestion.setQuestionTextHash(HashCodeUtils.SHA1(updatedQbQuestion.getQuestionText().toLowerCase()));
			session = HibernateUtils.getCurrentHibernateConnection();
			session.beginTransaction();
			qbQuestion.getQbAnswerChoices().clear();
			qbQuestion.updateFrom(updatedQbQuestion);
	
	        //Fix for bug 10929, 10926, 10922
	        //The issue here is that update answer choices do not delete the existing objects before inserting. This may create
	        //problem when we change the display sequence or the fraction values.
	        
	        //First adding some dummy answer choice text hash to avoid equals function throwing unique key constraint error
	        QbAnswerChoiceHelper.setQbAnswerChoiceTextHash(qbAnswerChoices,"*&^__==RANDOM*&^__==");
	        for(QbAnswerChoice qbAnswerChoice : qbAnswerChoices)
	    	{
	        	qbQuestion.addQbAnswerChoices(qbAnswerChoice);
	    	}
	        qbQuestion.setModifiedAuditData(updaterId, TimestampUtils.getCurrentTimestamp());
	        /*if(qbQuestion.getQbQuestionMediaFiles() != null && qbQuestion.getQbQuestionMediaFiles().isEmpty())
			{
	        	QbQuestionMediaFileHelper.deleteQbQuestionMediaFileByQuestionId(qbQuestion.getQbQuestionId());
			}*/
	        //Update the qb question with the dummy answer choice text hash. The other details like choice text, display sequence and 
	        //fraction are the same as it comes from the client
	        QbQuestionDAO.updateQbQuestion(qbQuestion);
	        
	        //Compute the actual answer choice text hash and do one more update. By doing so, we are able to have the primary keys
	        //which are already being used as it is.
	        QbAnswerChoiceHelper.setQbAnswerChoiceTextHash(qbQuestion.getQbAnswerChoices(),null);
	        qbQuestion.setModifiedAuditData(updaterId, TimestampUtils.getCurrentTimestamp());
	        QbQuestionDAO.updateQbQuestion(qbQuestion);
	        session.getTransaction().commit();
		}
        catch (HibernateException he) 
        {
			processException(he);
			session.getTransaction().rollback();
		}
		finally 
        {
			HibernateUtils.closeConnection(session);
		}
        populateName(qbQuestion);
		addItemToCache(qbQuestion);
        logger.debug("Exited updateQbQuestion without throwing any exception:");
        return qbQuestion;
    }
        
    /**
     * Delete qb questions.
     *
     * @param deleteQbQuestions the delete qb questions
     * @param modifiedByUserId the modified by user id
     * @throws AViewException
     */
    public static void deleteQbQuestions(List<QbQuestion> deleteQbQuestions, Long modifiedByUserId) throws AViewException 
    {  
    	Session session = null;
    	String errorMessage = "";
    	List<Long> qbQuestionIds = new ArrayList<Long>();
    	if(deleteQbQuestions != null)
    	{
    		boolean isQbQuestionExistsInQuiz = false;
    		try
    		{
	        	session = HibernateUtils.getCurrentHibernateConnection();
	        	session.beginTransaction();
	        	for (QbQuestion qbQuestion : deleteQbQuestions)
	        	{
					if(qbQuestion != null)
					{
						// delete the question(change status) only if it is not used in quiz`					
						isQbQuestionExistsInQuiz = checkIfQbQuestionExistsInQuizQP(qbQuestion.getQbQuestionId());					
						if(!isQbQuestionExistsInQuiz) 
						{
							qbQuestion.setQuestionText(qbQuestion.getQuestionText() + AppenderUtils.DeleteAppender());
						    qbQuestion.setQuestionTextHash(HashCodeUtils.SHA1(qbQuestion.getQuestionText()));
							qbQuestion.setStatusId(StatusHelper.getDeletedStatusId());
							qbQuestion.setModifiedAuditData(modifiedByUserId,TimestampUtils.getCurrentTimestamp());
							QbQuestionDAO.updateQbQuestion(qbQuestion);
							QbAnswerChoiceHelper.removeQbAnswerChoiceByQbQuestion(qbQuestion.getQbAnswerChoices(), modifiedByUserId);
							/*if (qbQuestion.getQbQuestionMediaFiles().size() > 0) 
						    {
								qbQuestionIds.add(qbQuestion.getQbQuestionId());
							}*/
							removeItemFromCache(qbQuestion);
						} 
						else if (isQbQuestionExistsInQuiz) 
						{
							//Fix for Bug#15947
							if(qbQuestion.getQbQuestionTypeName().equals(Constant.POLLING_QUESTION_TYPE))
							{
								errorMessage = "Question cannot be deleted since it is already used for Polling.";
							}
							else
							{
								errorMessage = "Question cannot be deleted since it is referred in an existing Quiz.";
							}
							break;
						} 
						else 
						{
							errorMessage = "Question not found";
							break;
						}
					}
				}
				if(errorMessage.equals(""))
				{
					session.getTransaction().commit();
				}
				else
				{
					throw new AViewException(errorMessage);
				}
			}
			catch(HibernateException he)
			{
				processException(he);
				session.getTransaction().rollback();
			}
			finally
			{
				HibernateUtils.closeConnection(session);
			}
			
			/*if (qbQuestionIds.size() > 0) 
			{
				QbQuestionMediaFileHelper.deleteQbQuestionMediaFileByQuestionId(qbQuestionIds);
			}*/	
		}
	}

	/**
	 * Gets the qb question.
	 * 
	 * @param qbQuestionId
	 *            the qb question id
	 * @return the qb question
	 * @throws AViewException
	 */
	public static QbQuestion getQbQuestion(Long qbQuestionId)throws AViewException
	{
		QbQuestion qbQuestion = QbQuestionDAO.getQbQuestion(qbQuestionId);
		populateName(qbQuestion);
		return qbQuestion;
	}

    // method added by Radha
    /**
     * Gets the qb questions.
     *
     * @param categoryId the category id
     * @param subcategoryId the subcategory id
     * @param questionTypeId the question type id
     * @param difficultyLevelId the difficulty level id
     * @param qtext the qtext
     * @param userId the user id
     * @return the qb questions
     * @throws AViewException
     */
    public static List<QbQuestion> getQbQuestions(Long categoryId,Long subcategoryId, Long questionTypeId, Long difficultyLevelId,String qtext, Long userId) throws AViewException 
    {
    	Long questionTypeIdForPolling = QbQuestionTypeHelper.getQbQuestionTypeByName(QuizConstant.POLLING_QUESTION_TYPE).getQbQuestionTypeId() ;
        Integer statusId = StatusHelper.getActiveStatusId();     
        List<QbQuestion> qbQuestions = QbQuestionDAO.getQbQuestions(categoryId, subcategoryId,questionTypeId, difficultyLevelId, qtext, userId, questionTypeIdForPolling, statusId);
        populateNames(qbQuestions);
        return qbQuestions;
    }
   
    /**
     * Gets the all active qb questions for subcategory.
     *
     * @param subcategoryId the subcategory id
     * @return the all active qb questions for subcategory
     * @throws AViewException
     */
    public static List<QbQuestion> getAllActiveQbQuestionsForSubcategory(Long subcategoryId) throws AViewException 
    {
    	Integer statusId = StatusHelper.getActiveStatusId();        
    	List<QbQuestion> qbQuestions = QbQuestionDAO.getAllActiveQbQuestionsForSubcategory(subcategoryId, statusId);
    	populateNames(qbQuestions);
    	return qbQuestions;
    }

    /**
     * Gets the all active qb questions for subcategories.
     *
     * @param subcategoryId the subcategory id
     * @return the all active qb questions for subcategories
     * @throws AViewException
     */
    public static List<QbQuestion> getAllActiveQbQuestionsForSubcategories(List<Long> subcategoryId) throws AViewException 
    {
    	Integer statusId = StatusHelper.getActiveStatusId();
    	List<QbQuestion> qbQuestions = QbQuestionDAO.getAllActiveQbQuestionsForSubcategories(subcategoryId, statusId);
    	populateNames(qbQuestions);
    	return qbQuestions;
    }
    
	/**
	 * Gets the qb questions for polling.
	 *
	 * @param userId the user id
	 * @return the qb questions for polling
	 * @throws AViewException
	 */
	public static List<QbQuestion> getQbQuestionsForPolling(Long userId) throws AViewException 
	{		
		Long questionTypeId = QbQuestionTypeHelper.getQbQuestionTypeByName(QuizConstant.POLLING_QUESTION_TYPE).getQbQuestionTypeId() ;
		Integer statusId = StatusHelper.getActiveStatusId();
		List<QbQuestion> qbQuestions = QbQuestionDAO.getQbQuestions(null, null, questionTypeId, null, null, userId, null, statusId);
		populateNames(qbQuestions);
		return qbQuestions;
	}
	
    /**
	 * Gets the qb questions.
	 *
	 * @param qbQuestionIds the qb question ids
	 * @return the qb questions
	 * @throws AViewException
	 */
	public static List<QbQuestion> getQbQuestions(List<Long> qbQuestionIds) throws AViewException 
    {
        List<QbQuestion> qbQuestions = QbQuestionDAO.getQbQuestions(qbQuestionIds, StatusHelper.getActiveStatusId());
        populateNames(qbQuestions);
        return qbQuestions;
    }

    /**
     * Check if question exists in quiz question paper.
     *
     * @param qbQuestionId the qb question id
     * @return the boolean
     * @throws AViewException
     */
    public static Boolean checkIfQbQuestionExistsInQuizQP(Long qbQuestionId) throws AViewException
	{
    	return QbQuestionDAO.checkIfQbQuestionExistsInQuizQP(qbQuestionId) ;
	}

    /**
     * Clear cache.
     */
    public static void clearCache()
    {
    	logger.debug("Entering QbQuestionHelper::clearCache");
    	qbQuestionsMap = null;
    	logger.debug("Entering QbQuestionHelper::clearCache");
    }

	private static void processException(HibernateException he)throws AViewException
	{
		String exceptionMessage = null;
		exceptionMessage = he.getMessage();
		if (he.getCause() != null && he.getCause().getMessage() != null)
		{
			exceptionMessage = he.getCause().getMessage();
		} 
		else
		{
			exceptionMessage = he.getMessage();
		}
		logger.error(exceptionMessage, he);
		throw (new AViewException(exceptionMessage));
	}
}
