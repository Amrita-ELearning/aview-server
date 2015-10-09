/*
/*
 * @(#)QuestionPaperHelper.java 4.0 2013/10/10
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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import com.ibm.icu.text.SimpleDateFormat;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.AppenderUtils;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.evaluation.QuizConstant;
import edu.amrita.aview.evaluation.daos.QuestionPaperDAO;
import edu.amrita.aview.evaluation.entities.QbQuestion;
import edu.amrita.aview.evaluation.entities.QuestionPaper;
import edu.amrita.aview.evaluation.entities.QuestionPaperQuestion;
import edu.amrita.aview.evaluation.entities.Quiz;
import edu.amrita.aview.evaluation.vo.QuestionPaperQuestionListVO;
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
public class QuestionPaperHelper 
{ 
    /** The logger. */
    private static Logger logger = Logger.getLogger(QuestionPaperHelper.class);

    /** The question papers map. */
    private static Map<Long, QuestionPaper> questionPapersMap = Collections.synchronizedMap(new HashMap<Long, QuestionPaper>());

    /** The Constant CACHE_CODE. */
    private static final String CACHE_CODE = "QuestionPaperHelper";
   
    /**
     * Populate cache.
     *
     * @param questionPaperIdsMap the question paper ids map
     * @throws AViewException
     */
    private static synchronized void populateCache(Map<Long, QuestionPaper> questionPaperIdsMap)
            throws AViewException 
    {
        questionPapersMap.clear();
        questionPapersMap.putAll(questionPaperIdsMap);
        CacheHelper.setCache(CACHE_CODE);
    }

    /**
     * Adds the item to cache.
     *
     * @param questionPaper the question paper
     * @throws AViewException
     */
    private static synchronized void addItemToCache(QuestionPaper questionPaper)
            throws AViewException 
    {
        questionPapersMap.put(questionPaper.getQuestionPaperId(),questionPaper);
    }

    /**
     * Removes the item from cache.
     *
     * @param questionPaper the question paper
     */
    private static synchronized void removeItemFromCache(QuestionPaper questionPaper)
	{
		questionPapersMap.remove(questionPaper.getQuestionPaperId());
	}
    
    /**
     * Question paper ids map.
     *
     * @return the map
     * @throws AViewException
     */
    private static synchronized Map<Long, QuestionPaper> questionPaperIdsMap()
            throws AViewException 
    {
        Integer activeSID = StatusHelper.getActiveStatusId();
        // If cache is expired or invalidated
        if (!CacheHelper.isCacheValid(CACHE_CODE)) 
        {         
            List<QuestionPaper> questionPapers = QuestionPaperDAO.getAllActiveQuestionPapers(activeSID);
            // Populate the Map
            Map<Long, QuestionPaper> questionPaperIdsMap = new HashMap<Long, QuestionPaper>();
            for (QuestionPaper questionPaper : questionPapers) 
            {
            	questionPaperIdsMap.put(questionPaper.getQuestionPaperId(),questionPaper);
            }
            populateCache(questionPaperIdsMap);
        }
        return questionPapersMap;
    }
   
    /**
     * Gets the all active question papers.
     *
     * @return the all active question papers
     * @throws AViewException
     */
    public static List<QuestionPaper> getAllActiveQuestionPapers() throws AViewException 
    {
        List<QuestionPaper> questionPapers = new ArrayList<QuestionPaper>();
        questionPapers.addAll(questionPaperIdsMap().values());
        return questionPapers;
    }
    
    /**
     * Gets the question paper id.
     *
     * @param questionPaperId the question paper id
     * @return the question paper id
     * @throws AViewException
     */
    public static QuestionPaper getQuestionPaperId(Long questionPaperId) throws AViewException 
    {
        return QuestionPaperDAO.getQuestionPaperId(questionPaperId);
    }

   /* private static void populateNames(List<QuestionPaper> qpQuestionPapers)  throws AViewException
    {
    	for(QuestionPaper qpQuestionPaper : qpQuestionPapers)
    	{
    		populateNames(qpQuestionPaper);
    	}
    }*/
    
    /**
    * Populate user names.
    *
    * @param qpQuestionPaper the qp question paper
    * @throws AViewException
    */
   private static void populateUserNames(QuestionPaper qpQuestionPaper)  throws AViewException
    {
    	if(qpQuestionPaper.getCreatedByUserId() != null)
    	{
    		User user = UserHelper.getUser(qpQuestionPaper.getCreatedByUserId());
    		qpQuestionPaper.setCreatedByUserName(user.getUserName());
    	}
    	
    	if(qpQuestionPaper.getModifiedByUserId() != null)
    	{
    		User user = UserHelper.getUser(qpQuestionPaper.getModifiedByUserId());
    		qpQuestionPaper.setModifiedByUserName(user.getUserName());
    	}
    }
    
    /**
     * Creates the question paper.
     *
     * @param questionPaper the question paper
     * @param creatorId the creator id
     * @return the question paper
     * @throws AViewException
     */
    public static QuestionPaper createQuestionPaper(QuestionPaper questionPaper, Long creatorId) throws AViewException 
    {
    	if(questionPaper.getQuestionPaperName().equals(QuizConstant.POLLING_QUESTION_TYPE))
    	{
    		throw new AViewException("Please enter a different questionpaper name") ;
    	}
    	User tempU = UserHelper.getUser(creatorId) ;
    	questionPaper.setInstituteId(tempU.getInstituteId()) ;
        questionPaper.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
        QuestionPaperDAO.createQuestionPaper(questionPaper);
        if ((questionPaper.getQuestionPaperId() != null) && 
        		(questionPaper.getQuestionPaperId() != 0) ) 
        {
        	populateUserNames(questionPaper);
        	addItemToCache(questionPaper);
        }
        logger.debug("Exited createQuestionPaper without throwing any exception:");

        return questionPaper;
    }

    /**
     * Update question paper.
     *
     * @param updateQuestionPaper the update question paper
     * @param updaterId the updater id
     * @throws AViewException
     */
    public static void updateQuestionPaper(QuestionPaper updateQuestionPaper,Long updaterId)  throws AViewException 
   {
        QuestionPaper questionPaper1 = getQuestionPaperId(updateQuestionPaper.getQuestionPaperId());
       
        if (questionPaper1 != null) 
        {
            questionPaper1.updateFrom(updateQuestionPaper);
            Object[] questionPaperQuestionList = questionPaper1.getQuestionPaperQuestions().toArray() ;	
      		questionPaper1.getQuestionPaperQuestions().clear() ;
      		for(int i = 0 ; i < questionPaperQuestionList.length ; i++)
      		{
      			QuestionPaperQuestion qpq = (QuestionPaperQuestion) questionPaperQuestionList[i] ;
      			
      			// set the fields related to random question as null
      			if(qpq.getPatternType().equals(QuizConstant.SPECIFIC_PATTERN_TYPE))
      			{
      				QbQuestion question = QbQuestionHelper.getQbQuestion(qpq.getQbQuestionId()) ;
      				qpq.setNumRandomQuestions(null) ;
      				qpq.setQbSubcategoryId(question.getQbSubcategoryId()) ;
      				qpq.setQbDifficultyLevelId(null) ;
      				qpq.setQbQuestionTypeId(null);
      			}
      			// set the fields related to specific question as null
      			else
      			{
      				qpq.setQbQuestionId(null);
      				if(qpq.getQbDifficultyLevelId() == 0)
      				{
      					qpq.setQbDifficultyLevelId(null) ;
      				}
      				
      				if(qpq.getQbQuestionTypeId() == 0)
      				{
      					qpq.setQbQuestionTypeId(null);
      				}
      			}
      			  
      			questionPaper1.addQuestionPaperQuestion(qpq) ;
      		}
      		  
            questionPaper1.setModifiedAuditData(updaterId, TimestampUtils.getCurrentTimestamp());
            QuestionPaperDAO.updateQuestionPaper(questionPaper1);
            addItemToCache(questionPaper1);
        } 
        else 
        {
            throw new AViewException("QuestionPaper with id :"+ updateQuestionPaper.getQuestionPaperName() + ": is not found");
        }
        logger.debug("Exited updateQuestionPaper without throwing any exception:");

    }

    /**
     * Delete question paper.
     *
     * @param questionPaperId the question paper id
     * @param modifiedByUserId the modified by user id
     * @throws AViewException
     */
    public static void deleteQuestionPaper(Long questionPaperId,Long modifiedByUserId) throws AViewException 
    {
        QuestionPaper questionPaper = getQuestionPaperId(questionPaperId);

        if (questionPaper != null) 
        {
        	questionPaper.setQuestionPaperName(questionPaper.getQuestionPaperName() + AppenderUtils.DeleteAppender()) ;
            questionPaper.setStatusId(StatusHelper.getDeletedStatusId());
            questionPaper.setModifiedAuditData(modifiedByUserId,TimestampUtils.getCurrentTimestamp());
            QuestionPaperDAO.updateQuestionPaper(questionPaper);
            removeItemFromCache(questionPaper);
        } 
        else 
        {
            throw new AViewException("QuestionPaper with id :"+ questionPaperId + ": is not found");
        }
    }
    
    //method added by Radha
  	/**
     * Gets the all active question papers for user.
     *
     * @param userId the user id
     * @return the all active question papers for user
     * @throws AViewException
     */
    public static List<QuestionPaper> getAllActiveQuestionPapersForUser(Long userId) throws AViewException
  	{
  		Integer activeSId = StatusHelper.getActiveStatusId();
  		return getAllActiveQuestionPapersForUser(userId, null); 
  	}
    	
  	/**
	   * Gets the all active question papers for user which are complete.
	   *
	   * @param userId the user id
	   * @param isComplete string to specify paper is complete
	   * @return the all active question papers for user
	   * @throws AViewException
	   */
  	public static List<QuestionPaper> getAllActiveQuestionPapersForUser(Long userId , String isComplete) throws AViewException
  	{
  		Integer activeSId = StatusHelper.getActiveStatusId();
  		return QuestionPaperDAO.getAllActiveQuestionPapersForUser(userId, isComplete ,activeSId); 
  	}
  	
	/**
	   * Gets the question paper complete.
	   *
	   * @param questionPaperId the question paper id
	   * @return the question paper complete
	   * @throws AViewException
	*/
  	public static List<QuestionPaper> getQuestionPaperComplete(Long questionPaperId) throws AViewException
  	{
  		Integer activeSId = StatusHelper.getActiveStatusId() ;
		return  QuestionPaperDAO.getQuestionPaperComplete(questionPaperId,activeSId);
	}
  	
  	/**
	   * Question paper preview.
	   *
	   * @param questionPaperId the question paper id
	   * @param userId the user id
	   * @return the list
	   * @throws AViewException
	   */
	  public static List<QuestionPaperQuestionListVO> questionPaperPreview(Long questionPaperId,Long userId) throws AViewException
  	{  	
  		List<QuestionPaperQuestionListVO> questionPaperQuestionListVo=QuestionPaperDAO.getSpecificQuestionsForQP(questionPaperId, StatusHelper.getActiveStatusId()) ;
  		populateRandomQuestions(questionPaperId,questionPaperQuestionListVo,userId);
  		return questionPaperQuestionListVo;	
  	}
  	
	  /**
	   * Populate randomquestions.
	   *
	   * @param questionPaperId the question paper id
	   * @param questionPaperQuestionListVo the question paper question list vo
	   * @param userId the user id
	   * @return the list
	   * @throws AViewException
	   */
	  public static List<QuestionPaperQuestionListVO> populateRandomQuestions(Long questionPaperId,List<QuestionPaperQuestionListVO> questionPaperQuestionListVo,Long userId) throws AViewException
  	{  	
  		List<QuestionPaperQuestion> questionPaper=QuestionPaperQuestionHelper.getAllActiveQuestionPaperQuestionsForQP(questionPaperId,userId);  		
  		
		QuestionPaperQuestion qpq=new QuestionPaperQuestion();
		QuestionPaperQuestionListVO qpqlistVo=new QuestionPaperQuestionListVO();
		long numRandomquestions=0;
		for(int j=0;j<questionPaper.size();j++)
		{
			if(questionPaper.get(j).getNumRandomQuestions()!=null)
			{
				numRandomquestions=numRandomquestions+questionPaper.get(j).getNumRandomQuestions();
			}
		}
		qpq.setNumRandomQuestions(numRandomquestions);
		qpqlistVo =new QuestionPaperQuestionListVO();
		qpqlistVo.questionPaperQuestion=qpq;
		questionPaperQuestionListVo.add(qpqlistVo);
  			
  		
  		return questionPaperQuestionListVo;	
  	}
  	
	  /**
	 * Validate question paper. Checks if : - total marks are equal to the max
	 * marks - question paper name is blank - invalid value for marks -
	 * availability of random questions
	 * 
	 * @param questionPaperId
	 *            the question paper id
	 * @param creatorId
	 *            the creator id
	 * @return true, if successful
	 * @throws AViewException
	 *             the a view exception
	 */
	  public static boolean validateQuestionPaper(Long questionPaperId, Long creatorId) throws AViewException
  	{  		
  		boolean validateFlag = true;
  		QuestionPaper questionPaper=new QuestionPaper();
  		List<QuestionPaper> questionList= new ArrayList<QuestionPaper>() ;
  		questionList=getQuestionPaperComplete(questionPaperId);
  		questionPaper=(QuestionPaper) questionList.get(0);
  		if(questionPaper.getCurrentTotalMarks() != questionPaper.getMaxTotalMarks())
		{
  			validateFlag = false;
			throw new AViewException("Current total marks is not equal to Max. total marks") ;
		}
		else if(questionPaper.getQuestionPaperName() == null)
		{
			validateFlag = false;
			throw new AViewException("Question paper name can not be blank.") ;
		}			
		else if(questionPaper.getCurrentTotalMarks() == 0)
		{
			validateFlag = false;
			throw new AViewException("Invalid integer value for Max. total marks.") ;
		}
  		// check for random question availability in given sub category
		else
		{
	  		Long tempQuestionPaperId = questionPaper.getQuestionPaperId();
	  		
	  		/** specific questions from question paper  */
	  		List<QuestionPaperQuestion> specificQuestionsFromQp = QuestionPaperQuestionHelper.getQbQuestionFromQuestionPaperByPatternType(QuizConstant.SPECIFIC_PATTERN_TYPE, tempQuestionPaperId);
	  		
	  		/** Question Id's of specific type of question */
	  		List<Long> specificQuestionIds = new ArrayList<Long>();
	  		
	  		/** specific questions from question bank */
	  		List<QbQuestion> specificQuestionsFromQb = new ArrayList<QbQuestion>();
	  		
	  		if((specificQuestionsFromQp != null) && 
	  				(specificQuestionsFromQp.size() > 0))
	  		{
		  		for (QuestionPaperQuestion specificQuestion : specificQuestionsFromQp)
		  		{
		  			specificQuestionIds.add(specificQuestion.getQbQuestionId());
		  		}
		  		specificQuestionsFromQb = QbQuestionHelper.getQbQuestions(specificQuestionIds);
	  		}
	  		
	  		/** random questions from question paper */
	  		List<QuestionPaperQuestion> randomQuestionsFromQp = QuestionPaperQuestionHelper.getQbQuestionFromQuestionPaperByPatternType(QuizConstant.RANDOM_PATTERN_TYPE, tempQuestionPaperId);
	
	  		Long requiredRandomQuestion = 0l;
			for (QuestionPaperQuestion randomQuestion : randomQuestionsFromQp)
			{
				requiredRandomQuestion = randomQuestion.getNumRandomQuestions();
				 Long qbSubcategoryId = randomQuestion.getQbSubcategoryId();
				Long questionTypeId=null;
				Long difficultyLevelId=null;
				if(randomQuestion.getQbQuestionTypeId() != null)
				{
					questionTypeId=randomQuestion.getQbQuestionTypeId();
				}
				if(randomQuestion.getQbDifficultyLevelId() != null)
				{
					difficultyLevelId = randomQuestion.getQbDifficultyLevelId();
				}
				
				// Get questions for a subcategory
				List<QbQuestion> allQbQuestionsBySubcategory = QbQuestionHelper.getQbQuestions(0l, qbSubcategoryId,questionTypeId, difficultyLevelId,null,randomQuestion.getCreatedByUserId());
		
				// Remove the specific questions from the random question list
				if((specificQuestionsFromQp != null) && 
						(specificQuestionsFromQp.size() > 0))
				{
					allQbQuestionsBySubcategory.removeAll(specificQuestionsFromQb);
				}
				
				if((allQbQuestionsBySubcategory.size() == 0) || (allQbQuestionsBySubcategory.size() < requiredRandomQuestion))
				{
					validateFlag = false;
					throw new HibernateException("Sorry, Sufficient questions are not available in the question bank to generate Random Questions.");					
				}				
			}
		}
  		return validateFlag;
  	}
  	
  	/**
	   * Gets the question paper questions for quiz.
	   *
	   * @param questionPaperId the question paper id
	   * @return the question paper questions for quiz
	   * @throws AViewException
	   */
	  public static List<QuestionPaperQuestionListVO> getQuestionPaperQuestionsForQuiz(Long questionPaperId) throws AViewException 
    {
        Integer activeSId = StatusHelper.getActiveStatusId();
        List<QuestionPaperQuestionListVO> specificQuestionsForQP = QuestionPaperDAO.getSpecificQuestionsForQP(questionPaperId, activeSId);
		List<QuestionPaperQuestion> randomQuestionsForQP = QuestionPaperDAO.getRandomQuestionsForQP(questionPaperId, activeSId);
		if(randomQuestionsForQP.size() > 0)
		{
			QuestionPaperDAO.prepareRandomQuestions(specificQuestionsForQP, randomQuestionsForQP);
		}		
		return specificQuestionsForQP;
    }
  	
  	 /**
 	   * Save question paper.
 	   *
 	   * @param updatedQuestionPaper the updated question paper
 	   * @param creatorId the creator id
 	   * @return the question paper
 	   * @throws AViewException
 	   */
 	  public static QuestionPaper saveQuestionPaper(QuestionPaper updatedQuestionPaper,Long creatorId)
     throws AViewException
     {  		
		 QuestionPaper questionPaper = getQuestionPaperId(updatedQuestionPaper.getQuestionPaperId()) ;
		 questionPaper.updateFrom(updatedQuestionPaper) ;
		 Object[] questionPaperQuestionList = questionPaper.getQuestionPaperQuestions().toArray(); 
		 questionPaper.getQuestionPaperQuestions().clear() ;
		 
		 for(int i = 0 ; i < questionPaperQuestionList.length ; i++)
		{
			QuestionPaperQuestion qpq = (QuestionPaperQuestion) questionPaperQuestionList[i] ;

			// Set the fields to null , which are used for random pattern type
			if(qpq.getPatternType().equals(QuizConstant.SPECIFIC_PATTERN_TYPE))
			{
				QbQuestion question = QbQuestionHelper.getQbQuestion(qpq.getQbQuestionId()) ;
				qpq.setNumRandomQuestions(null) ;
				qpq.setQbSubcategoryId(question.getQbSubcategoryId()) ;
				qpq.setQbDifficultyLevelId(null) ;
				qpq.setQbQuestionTypeId(null);
			}
			
			// Set the fields to null , which are used for specific pattern type
			else
			{
				qpq.setQbQuestionId(null);
				if(qpq.getQbDifficultyLevelId() == 0)
				{
					qpq.setQbDifficultyLevelId(null) ;
				}
				
				if(qpq.getQbQuestionTypeId() == 0)
				{
					qpq.setQbQuestionTypeId(null);
				}
			}
			  
			questionPaper.addQuestionPaperQuestion(qpq) ;
		}
  		  		
  		questionPaper.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
  		questionPaper.setModifiedAuditData(creatorId, TimestampUtils.getCurrentTimestamp());  		
     	QuestionPaperDAO.saveQuestionPaper(questionPaper);     	
     	QuestionPaperQuestionHelper.populateNames(new ArrayList<QuestionPaperQuestion>(questionPaper.getQuestionPaperQuestions())) ;
     	return  questionPaper ;
 	} 
  	 
	  /**
	   * Format question paper name.
	   *
	   * @param questionPaperName the question paper name
	   */
	  public static String formatQuestionPaperName(String questionPaperName)
	 {
		Date dNow =new Date();
		SimpleDateFormat date =new SimpleDateFormat (QuizConstant.POLLING_DATE_FORMAT);
		questionPaperName = new String(QuizConstant.POLLING_PAPER_NAME + date.format(dNow));
		return questionPaperName;
	 }
	/**
	 * Creates the polling question paper.
	 *
	 * @param qbQuestions the qb questions
	 * @param classId the class id
	 * @param courseId the course id
	 * @param creatorId the creator id
	 * @return the quiz
	 * @throws AViewException
	 */
	public static Quiz createPollingQuestionPaper(List<QbQuestion> qbQuestions,Long classId,Long courseId,Long creatorId) throws AViewException 
	{
		String questionPaperName = "" ;
		QuestionPaper questionPaper=new QuestionPaper();
		Quiz quiz=new Quiz();
		questionPaperName = formatQuestionPaperName(questionPaperName);
		questionPaper.setQuestionPaperName(questionPaperName);
		questionPaper.setIsComplete(QuizConstant.YES);
		User tempU = UserHelper.getUser(creatorId) ;
		questionPaper.setInstituteId(tempU.getInstituteId()) ;
		questionPaper.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
		populateQuestionPaperQuestions(questionPaper ,qbQuestions, creatorId) ;
		
		QuestionPaperDAO.createQuestionPaper(questionPaper);
		if ((questionPaper.getQuestionPaperId() != null) && 
				(questionPaper.getQuestionPaperId() != 0) ) 
		{
			quiz=QuizHelper.createPollingQuiz(questionPaper,classId,courseId,creatorId);

		}
		logger.debug("Exited createQuestionPaper without throwing any exception:");
		return quiz;
	}
	
	/**
	 * Populate the question paper questions.
	 *
	 * @param questionPaper the question paper
	 * @param qbqList the list of questions
	 * @param creatorId the creator id
	 */
	public static void populateQuestionPaperQuestions(QuestionPaper questionPaper ,List<QbQuestion> qbqList , Long creatorId)
	{		
		double currentMark=0;
		Long totalMark=0l;
		for(int i=0;i<qbqList.size();i++)
		{
			QuestionPaperQuestion questionPaperQuestion=new QuestionPaperQuestion();
			questionPaperQuestion.setQbSubcategoryId(qbqList.get(i).getQbSubcategoryId());
			questionPaperQuestion.setPatternType(QuizConstant.SPECIFIC_PATTERN_TYPE);
			questionPaperQuestion.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
			questionPaperQuestion.setQbQuestionId(qbqList.get(i).getQbQuestionId());
			currentMark=qbqList.get(i).getMarks();
			questionPaperQuestion.setMarks((long) currentMark);
			totalMark= (long)(totalMark+qbqList.get(i).getMarks());
			questionPaper.addQuestionPaperQuestion(questionPaperQuestion);
		}
	
		questionPaper.setCurrentTotalMarks(totalMark);
		questionPaper.setMaxTotalMarks(totalMark);
		
	}
	/**
	 * Gets the question paper if questions exist.
	 *
	 * @return the question paper if questions exist
	 * @throws AViewException
	 */
	public static List<QuestionPaper> getQuestionPaperIfQuestionsExist()  throws AViewException
	{
		 return QuestionPaperDAO.getQuestionPaperIfQuestionsExist() ;
	}
	
	/**
	 * Clear cache.
	 */
	public static void clearCache()
	{
		logger.debug("Entered QuestionPaperHelper.clearCache()");
		questionPapersMap.clear();
		logger.debug("Entered QuestionPaperHelper.clearCache()");
	}
}
