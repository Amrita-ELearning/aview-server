/*
 * @(#)QuizQuestionChoiceResponseHelper.java 4.0 2013/10/05
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
import edu.amrita.aview.evaluation.daos.QuizQuestionChoiceResponseDAO;
import edu.amrita.aview.evaluation.entities.QuizQuestionChoiceResponse;



/**
 * This class acts as connector between client and server.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QuizQuestionChoiceResponseHelper  
{

    /** The logger. */
    private static Logger logger = Logger.getLogger(QuizQuestionChoiceResponseHelper.class);
   
    /* private static Map<Long,QuizQuestionChoiceResponse> quizQuestionChoicesMap = Collections.synchronizedMap(new HashMap<Long,QuizQuestionChoiceResponse>());
    private static final String CACHE_CODE = "QuizQuestionChoiceResponseHelper";
   
    private static synchronized void populateCache(Map<Long,QuizQuestionChoiceResponse> quizQuestionChoiceIdsMap)throws AViewException
    {
        quizQuestionChoicesMap.clear();
        quizQuestionChoicesMap.putAll(quizQuestionChoiceIdsMap);
        CacheHelper.setCache(CACHE_CODE);
    }
    private static synchronized void addItemToCache(QuizQuestionChoiceResponse quizQuestionChoiceResponse)throws AViewException
    {
        quizQuestionChoicesMap.put(quizQuestionChoiceResponse.getQuizQuestionChoiceResponseId(), quizQuestionChoiceResponse);
    }
    private static synchronized Map<Long,QuizQuestionChoiceResponse> quizQuestionChoiceResponseIdsMap()throws AViewException
    {  
        Integer activeSID=StatusHelper.getActiveStatusId();
        //If cache is expired or invalidated
        if(!CacheHelper.isCacheValid(CACHE_CODE))
        {
            System.out.println("Coming inside invalidate cache");            
            List<QuizQuestionChoiceResponse> _quizQuestionChoiceResponses =QuizQuestionChoiceResponseDAO.getAllActiveQuizQuestionChoiceResponses(activeSID, activeSID, activeSID);
            //Populate the Map
            Map<Long,QuizQuestionChoiceResponse> _quizQuestionChoiceResponseIdsMap = new HashMap<Long,QuizQuestionChoiceResponse>();
            for(QuizQuestionChoiceResponse _quizQuestionChoiceResponse:_quizQuestionChoiceResponses)
            {
            	_quizQuestionChoiceResponseIdsMap.put(_quizQuestionChoiceResponse.getQuizQuestionChoiceResponseId(), _quizQuestionChoiceResponse);
            }
            //populateNames(_quizQuestionChoiceResponses,_quizQuestionChoiceResponseIdsMap);
            populateCache(_quizQuestionChoiceResponseIdsMap);
            //PopulateNames
        }
        return quizQuestionChoicesMap;
    }*/
    /*private static void populateNames(List<QuizQuestionChoiceResponse> quizQuestionChoiceResponses,Map<Long,QuizQuestionChoiceResponse> quizQuestionChoiceResponseIdsMap)throws AViewException
    {
      
        for(QuizQuestionChoiceResponse quizQuestionChoiceResponse:quizQuestionChoiceResponses)
        {
            Long parentId = quizQuestionChoiceResponse.getQuizQuestionChoiceResponseId();
            Long quizQuestionChoiceResponseId = 0l;
            if(parentId != null && parentId != 0)
            {
                quizQuestionChoiceResponseId = quizQuestionChoiceResponseIdsMap.get(parentId).getQuizQuestionChoiceResponseId();
            }               
            populateNames(quizQuestionChoiceResponse,quizQuestionChoiceResponseId);
        }
    }
    public static void populateNames(QuizQuestionChoiceResponse quiz,Long quizChoiceResponseId)throws AViewException
    {
        quiz.setQuizQuestionChoiceResponseId(quizChoiceResponseId);
    }   */
    //---------------------------------------------------------------------
    
   /* Query hqlQuery = session.createQuery("SELECT qqcr" +
			  " FROM QuizQuestionChoiceResponse qqcr," +
			  " QuizQuestionResponse qqr" +
			  " WHERE" +
			  " qqcr.quizQuestionResponse.quizQuestionResponseId = qqr.quizQuestionResponseId" +
			  " AND qqr.quizResponse.quizResponseId = " + quizResponseId);
   */
   
    /**
     * Gets the quiz question choice response by quiz response id.
     *
     * @param quizResponseId the quiz response id
     * @return the quiz question choice response by quiz response id
     * @throws AViewException
     */
    public static List<QuizQuestionChoiceResponse> getQuizQuestionChoiceResponseByQuizResponseId(Long quizResponseId) throws AViewException
    {
    	return QuizQuestionChoiceResponseDAO.getQuizQuestionChoiceResponseByQuizResponseId(quizResponseId, StatusHelper.getActiveStatusId());
    }
   
	
}
