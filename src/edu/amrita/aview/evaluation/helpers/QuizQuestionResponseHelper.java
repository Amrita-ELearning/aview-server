/**
 * @(#)QuizQuestionResponseHelper.java 4.0 2013/10/05
 * 
 * Copyright  © 2013 E-Learning Research Lab, 
 * Amrita Vishwa Vidyapeetham. All rights reserved. 
 * E-Learning Research Lab and the A-VIEW logo are trademarks or
 * registered trademarks of E-Learning Research Lab. 
 * All other names used are the trademarks of their respective owners.
 */
package edu.amrita.aview.evaluation.helpers;

import java.util.List;

import org.apache.log4j.Logger;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.evaluation.daos.QuizQuestionResponseDAO;
import edu.amrita.aview.evaluation.entities.QuizQuestionResponse;
import edu.amrita.aview.evaluation.vo.StudentQuizResultVO;
import edu.amrita.aview.gclm.entities.User;
import edu.amrita.aview.gclm.helpers.UserHelper;



/**
 * This class acts as connector between client and server .
 * 
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QuizQuestionResponseHelper 
{
    
    /** The logger. */
    private static Logger logger = Logger.getLogger(QuizQuestionResponseHelper.class);
 
    //Cache code
    /*private static Map<Long,QuizQuestionResponse> quizQuestionResponsesMap = Collections.synchronizedMap(new HashMap<Long,QuizQuestionResponse>());
    private static final String CACHE_CODE = "QuizQuestionResponseHelper";
    //Newly added
    private static synchronized void populateCache(Map<Long,QuizQuestionResponse> quizQuestionResponseIdsMap)throws AViewException
    {
        quizQuestionResponsesMap.clear();
        quizQuestionResponsesMap.putAll(quizQuestionResponseIdsMap);
        CacheHelper.setCache(CACHE_CODE);
    }
    private static synchronized void addItemToCache(QuizQuestionResponse quizQuestionResponse)
    {
        quizQuestionResponsesMap.put(quizQuestionResponse.getQuizQuestionResponseId(), quizQuestionResponse);
    }
    private static synchronized Map<Long,QuizQuestionResponse> quizQuestionResponseIdsMap()throws AViewException
    {  
        Integer activeSID=StatusHelper.getActiveStatusId();
        //If cache is expired or invalidated
        if(!CacheHelper.isCacheValid(CACHE_CODE))
        {
            System.out.println("Coming inside invalidate cache");
              
            List<QuizQuestionResponse> quizQuestionResponses = QuizQuestionResponseDAO.getAllActiveQuizQuestionResponses(activeSID, activeSID, activeSID);
            //Populate the Map
            Map<Long,QuizQuestionResponse> quizQuestionResponseIdsMap = new HashMap<Long,QuizQuestionResponse>();
            for(QuizQuestionResponse _quizQuestionResponse:quizQuestionResponses)
            {
            	quizQuestionResponseIdsMap.put(_quizQuestionResponse.getQuizQuestionResponseId(), _quizQuestionResponse);
            }
            populateNames(quizQuestionResponses,quizQuestionResponseIdsMap);
            populateCache(quizQuestionResponseIdsMap);
            //PopulateNames
        }
        return quizQuestionResponsesMap;
    }
    private static void populateNames(List<QuizQuestionResponse> _quizQuestionResponses,Map<Long,QuizQuestionResponse> _quizQuestionResponseIdsMap)throws AViewException
    {
        for(QuizQuestionResponse quizQuestionResponse:_quizQuestionResponses)
        {
            Long parentId = quizQuestionResponse.getQuizQuestionResponseId();
            Long quizName = 0l;
            if(parentId != null && parentId != 0)
            {
                quizName = _quizQuestionResponseIdsMap.get(parentId).getQuizQuestionResponseId();
            }               
            populateNames(quizQuestionResponse,quizName);
        }
    }
    public static void populateNames(QuizQuestionResponse quizResponse,Long responseId)
    {
        quizResponse.setQuizQuestionResponseId(responseId);
    }   
    //---------------------------------------------------------------------
        
    
    private static void printQuizQuestionResponse(QuizQuestionResponse quizQuestionResponse)
	{
		logger.debug("QuizQuestionResponse:"+quizQuestionResponse.toString());
		
		if(quizQuestionResponse.getQuizQuestionChoiceResponse() != null)
		{
			logger.debug("QuizQuestionChoiceResponse:"+quizQuestionResponse.getQuizQuestionChoiceResponse().size());
			for(QuizQuestionChoiceResponse qqcr:quizQuestionResponse.getQuizQuestionChoiceResponse())			
			{
				logger.debug("QuizQuestionChoiceResponse:"+qqcr.toString());
			}
		}
		
	}*/
	
    /**
     * Gets the student answer sheet.
     *
     * @param quizId the quiz id
     * @param studentName the student name
     * @return the student answer sheet
     * @throws AViewException
     */
    public static StudentQuizResultVO getStudentAnswerSheet(Long quizId , String studentName) throws AViewException
    {
    	int statusId = StatusHelper.getActiveStatusId() ;
    	User tempU = UserHelper.getUserByUserName(studentName) ;
    	return QuizQuestionResponseDAO.getStudentAnswerSheet(quizId, tempU.getUserId(),statusId);
    }
    
    /**
     * Gets the result for polling quiz.
     *
     * @param quizId the quiz id
     * @param qbQuestionId the qb question id
     * @return the result for polling quiz
     * @throws AViewException
     */
	//Fix for Bug #19388
    public static List<QuizQuestionResponse> getResultForPollingQuiz(Long quizQuestionId, Long qbQuestionId) throws AViewException    
    {
    	return QuizQuestionResponseDAO.getResultForPollingQuiz(quizQuestionId,qbQuestionId);
    }
}
