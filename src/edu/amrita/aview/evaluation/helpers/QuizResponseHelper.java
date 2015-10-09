/*
 * @(#)QuizResponseHelper.java 4.0 2013/10/04
 * 
 * Copyright  © 2013 E-Learning Research Lab, 
 * Amrita Vishwa Vidyapeetham. All rights reserved. 
 * E-Learning Research Lab and the A-VIEW logo are trademarks or
 * registered trademarks of E-Learning Research Lab. 
 * All other names used are the trademarks of their respective owners.
 */
package edu.amrita.aview.evaluation.helpers;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.entities.Auditable;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.evaluation.daos.QuizResponseDAO;
import edu.amrita.aview.evaluation.entities.QuizQuestionChoiceResponse;
import edu.amrita.aview.evaluation.entities.QuizQuestionResponse;
import edu.amrita.aview.evaluation.entities.QuizResponse;



/**
 * This class connects the client side and server side .
 * 
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QuizResponseHelper  extends Auditable
{
    
    /** The logger. */
    private static Logger logger = Logger.getLogger(QuizResponseHelper.class);
    
    /**
     * Prints the quiz response.
     *
     * @param quizResponse the quiz response
     * @throws AViewException
     */
    private static void printQuizResponse(QuizResponse quizResponse) throws AViewException
	{
		logger.debug("Quiz Response:"+quizResponse.toString());
		
		if(quizResponse.getQuizQuestionResponse() != null)
		{
			logger.debug("QuizQuestionResponse:"+quizResponse.getQuizQuestionResponse().size());
			for(QuizQuestionResponse qqr:quizResponse.getQuizQuestionResponse())			
			{
				logger.debug("QuizQuestionResponse:"+qqr.toString());
			}
		}	
	}
    
    /**
     * Creates the quiz response.
     *
     * @param quizResponse the quiz response
     * @param quizQuesRes the quiz ques res
     * @param quizAnsChoice the quiz ans choice
     * @param creatorId the creator id
     * @return the quiz response
     * @throws AViewException
     */
    public static QuizResponse createQuizResponse(QuizResponse quizResponse, List<QuizQuestionResponse> quizQuesRes, 
			List<QuizQuestionChoiceResponse> quizAnsChoice, Long creatorId) throws AViewException
	{
		printQuizResponse(quizResponse) ;
		Set<QuizQuestionResponse> qqrlst = new HashSet<QuizQuestionResponse>() ;
		Set<QuizQuestionChoiceResponse> qqcrlst = new HashSet<QuizQuestionChoiceResponse>();
		Timestamp currentTime=TimestampUtils.getCurrentTimestamp();
		quizResponse.setCreatedAuditData(creatorId,currentTime, StatusHelper.getActiveStatusId());
		QuizQuestionResponse qqr = new QuizQuestionResponse(); 
		// set the auditable(date,id) attribute for the associated classes quiz question response
		// and quiz question choice response
		for(int i=0;i<quizQuesRes.size();i++)
		{
			qqcrlst = new HashSet<QuizQuestionChoiceResponse>();
			 
			qqr = (QuizQuestionResponse)quizQuesRes.get(i);     
			qqr.setCreatedAuditData(creatorId,currentTime, StatusHelper.getActiveStatusId());
			qqr.setQuizResponse(quizResponse);
			for(int j = 0 ; j < quizAnsChoice.size() ; j++)
			{
				QuizQuestionChoiceResponse qqcr = new QuizQuestionChoiceResponse();
				
				qqcr =(QuizQuestionChoiceResponse)quizAnsChoice.get(j) ;			
				if(qqr.getQuizQuestionId().equals(qqcr.getQuizQuestionId()))
				{
					qqcr.setCreatedAuditData(creatorId,currentTime, StatusHelper.getActiveStatusId());
					qqcr.setQuizQuestionResponse(qqr);
					qqcrlst.add(qqcr);
					//break ;
				}
			}
			qqr.setQuizQuestionChoiceResponse(qqcrlst);            
			qqrlst.add(qqr);
		
		}

		
		quizResponse.setQuizQuestionResponse(qqrlst);
		QuizResponseDAO.createQuizResponse(quizResponse);

		return quizResponse ;
	}
    
    /**
     * Gets the quiz response for student.
     *
     * @param quizId the quiz id
     * @param userId the user id
     * @return the quiz response for student
     * @throws AViewException
     */
    public static QuizResponse getQuizResponseForStudent(Long quizId, Long userId) throws AViewException
    {
    	return QuizResponseDAO.getQuizResponseForStudent(quizId, userId, StatusHelper.getActiveStatusId());
    }
	
}
