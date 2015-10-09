/*
 * @(#)QuizQuestionResponseDAO.java 4.0 2013/10/04
 * 
 * Copyright  © 2013 E-Learning Research Lab, 
 * Amrita Vishwa Vidyapeetham. All rights reserved. 
 * E-Learning Research Lab and the A-VIEW logo are trademarks or
 * registered trademarks of E-Learning Research Lab. 
 * All other names used are the trademarks of their respective owners.
 */
package edu.amrita.aview.evaluation.daos;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.evaluation.QuizConstant;
import edu.amrita.aview.evaluation.entities.Quiz;
import edu.amrita.aview.evaluation.entities.QuizAnswerChoice;
import edu.amrita.aview.evaluation.entities.QuizQuestion;
import edu.amrita.aview.evaluation.entities.QuizQuestionChoiceResponse;
import edu.amrita.aview.evaluation.entities.QuizQuestionResponse;
import edu.amrita.aview.evaluation.entities.QuizResponse;
import edu.amrita.aview.evaluation.helpers.QuizHelper;
import edu.amrita.aview.evaluation.helpers.QuizQuestionChoiceResponseHelper;
import edu.amrita.aview.evaluation.helpers.QuizQuestionHelper;
import edu.amrita.aview.evaluation.helpers.QuizResponseHelper;
import edu.amrita.aview.evaluation.vo.StudentQuizResultVO;



/**
 * This class consists of queries related to quiz question response.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QuizQuestionResponseDAO extends SuperDAO 
{
    
    /** The logger. */
    private static Logger logger = Logger.getLogger(QuizQuestionResponseDAO.class);

    /**
     * Gets the student answer sheet.
     *
     * @param quizId the quiz id
     * @param studentId the student id
     * @param statusId the status id
     * @return the student answer sheet
     * @throws AViewException
     */
    public static StudentQuizResultVO getStudentAnswerSheet(Long quizId , Long studentId,int statusId) throws AViewException
    {
    	 Session session = null ;
    	 StudentQuizResultVO studentQuizResultVO = new StudentQuizResultVO();
    		
    	 try
    	 {
    		 session = HibernateUtils.getHibernateConnection() ;
    		 
    		 Quiz quiz = QuizHelper.getQuizById(quizId);
    		 studentQuizResultVO.quiz = quiz;
    		 
    		 // Gets the quiz response for a student(user)
    		 QuizResponse studentQuizResponse = QuizResponseHelper.getQuizResponseForStudent(quizId, studentId);
    		 studentQuizResultVO.quizResponse = studentQuizResponse;
    		 Long quizResponseId = studentQuizResponse.getQuizResponseId();
    		 
    		 List<QuizQuestion> quizQuestions =  QuizQuestionHelper.getQuizQuestionsForQuiz(quizId);
    		 
    		 // Gets the user response for the quiz
    		 List<QuizQuestionChoiceResponse> studentResponses = QuizQuestionChoiceResponseHelper.getQuizQuestionChoiceResponseByQuizResponseId(quizResponseId);    	
    		 logger.debug("Loop started :" + TimestampUtils.getCurrentTimestamp());
    		 for(QuizQuestion quizQuestion : quizQuestions)
    		 {
    			 Set<QuizAnswerChoice> quizAnswerChoices = quizQuestion.getQuizAnswerChoices();
    			 
    			 for(QuizAnswerChoice quizAnswerChoice : quizAnswerChoices)
    			 {
    				 for(QuizQuestionChoiceResponse quizQuestionChoiceResponse : studentResponses)
    				 {
    					 if(quizQuestionChoiceResponse.getQuizAnswerChoiceId().equals(quizAnswerChoice.getQuizAnswerChoiceId()))
    					 {
    						 quizAnswerChoice.setStudentAnsFraction(1);
    						 // break from the loop if the question type is multiple choice.
    						 if(quizQuestion.getQuestionTypeName().equals(QuizConstant.MULTIPLE_CHOICE_QUESTION_TYPE))
    						 {
    							 break;
    						 }
    					 }
    				 }
    			 }
    		 }
    		 logger.debug("Loop Ended :" + TimestampUtils.getCurrentTimestamp());
    		 studentQuizResultVO.quizQuestions = quizQuestions;    		 
    	 }
    	 catch(HibernateException he)
    	 {
    		 processException(he) ;
    	 }finally{
    		 HibernateUtils.closeConnection(session);
    	 }
    	 
    	 return studentQuizResultVO ;
    }
	
    /**
     * Gets the result for polling quiz.
     *
     * @param quizId the quiz id
     * @param qbQuestionId the qb question id
     * @return the result for polling quiz
     * @throws AViewException
     */
	//Fix for Bug #19388 Start
    public static List<QuizQuestionResponse> getResultForPollingQuiz(Long quizQuestionId, Long qbQuestionId) throws AViewException
    {
    	Session session = null  ;
    	List<QuizQuestionResponse> pollingResult =  new ArrayList<QuizQuestionResponse>() ;
    	/* Loop variables */
    	int i , j ; 
    	try
    	{
    		session = HibernateUtils.getHibernateConnection() ;
    		String sqlQueryString = "";
    		Query sqlQuery = null;
    		if(quizQuestionId.equals(0l))
    		{
    			sqlQueryString = "SELECT qq.quiz_question_id FROM quiz qz, quiz_question qq WHERE " + 
								 "qb_question_id = " + qbQuestionId + " AND qq.quiz_id  =  qz.quiz_id " +  
								 "ORDER BY qz.created_date DESC LIMIT 1";    			
    			sqlQuery = session.createSQLQuery(sqlQueryString);
    			List<Object> tmpQuizQuestionId = sqlQuery.list();
    			if((tmpQuizQuestionId != null) && (tmpQuizQuestionId.size() > 0))
    			{
    				//quizQuestionId = Long.parseLong(tmpQuizQuestionId.toString());
    				quizQuestionId = Long.parseLong(tmpQuizQuestionId.get(0).toString());
    			}    			
    		}
    		
    		sqlQueryString = "SELECT " +    				
							" qq.quiz_question_id , qq.question_text, " +
							" qac.quiz_answer_choice_id , qac.choice_text, " +
							" COUNT( IF(qqcr.quiz_answer_choice_id = qac.quiz_answer_choice_id,1,NULL))," +
							" (SELECT COUNT(clr.user_id) FROM  quiz qz , class_register clr WHERE qz.class_id = " +
							" clr.class_id AND qr.quiz_id = qz.quiz_id AND clr.user_id IN (SELECT user_id FROM user u " +
							" WHERE u.user_id = clr.user_id)), " +
							" qr.created_date , qac.display_sequence" +
							" FROM " +
							" quiz_question qq , " +
							" quiz_answer_choice qac , " +
							" quiz_response qr , " +
							" quiz_question_response qqr , " +
							" quiz_question_choice_response qqcr ," +
							" qb_question qbq"+  
							" WHERE " +
							" qqr.quiz_question_id = qq.quiz_question_id " +
							" AND qq.quiz_question_id = qac.quiz_question_id " +    											
							" AND qqr.quiz_response_id = qr.quiz_response_id " +
							" AND qqcr.quiz_question_response_id = qqr.quiz_question_response_id " +
							" AND qbq.qb_question_id = qq.qb_question_id AND  qbq.qb_question_id = " + qbQuestionId + 
							" AND qq.quiz_question_id = " + quizQuestionId + 
							" GROUP BY qac.quiz_answer_choice_id ORDER BY qr.created_date DESC";
			
			sqlQuery = session.createSQLQuery(sqlQueryString) ;
    		//sqlQuery.setLong("quizId", quizId) ;    		
    		
    		List<Object[]> tempResponse = sqlQuery.list() ;
    		List<QuizQuestionResponse> temp1=  new ArrayList<QuizQuestionResponse>() ;
    		//int quizQuestionId = 0 ; 
    		int correctAnsCount = 0 ;
    		
    		// Set the quiz question id
    		/*if(tempResponse.size() > 0)
    		{	
    			Object[] objB =(Object[]) tempResponse.get(0) ;
    			quizQuestionId = Integer.parseInt(objB[0].toString()) ;
    		}*/
    		
    		// Create a temporary response object , 
    		//consisting of question as well as answer
    		QuizQuestionResponse qqr = null;
    		for(Object[] objA:tempResponse)
    		{            	            	
    			qqr = new QuizQuestionResponse() ;
    			qqr.setQuizQuestionIdPolling(Integer.parseInt(objA[0].toString()));
    			qqr.setQuestionText(objA[1].toString()) ;
    			qqr.setQuizAnswerChoiceId(Integer.parseInt(objA[2].toString())) ;
    			qqr.setChoiceText(objA[3].toString()) ;
    			qqr.setCorrectCountForAnswerChoice(Integer.parseInt(objA[4].toString())) ;
    			qqr.setStudentCountForClass(Integer.parseInt(objA[5].toString())) ;
    			qqr.setCreatedDate((Timestamp)objA[6]) ;	  
    			qqr.setDisplaySequence(Integer.parseInt(objA[7].toString())) ;
    			temp1.add(qqr);
    		}
    		//Fix for Bug #19388 End 
            // Loop through the temporary created response object
            // to separate question and answer for display on client side
        	for(i = 0 ; i < temp1.size() ; i++ )
        	{
        		// set the question related data
        		QuizQuestionResponse qqr1 = temp1.get(i) ;
        		List<QuizQuestionResponse> ansList = new ArrayList<QuizQuestionResponse>() ;
        		QuizQuestionResponse outerqqr = new QuizQuestionResponse() ;
        		// Add question					
				outerqqr.setQuizQuestionIdPolling(qqr1.getQuizQuestionIdPolling()) ;
				outerqqr.setQuestionText(qqr1.getQuestionText()) ;
				outerqqr.setStudentCountForClass(qqr1.getStudentCountForClass()) ;
        		j = i  ;
	        	for( ; j < temp1.size() ; j++ )
    			{
					QuizQuestionResponse qqr2 = temp1.get(j) ;
					// commented the checking for each question , since
					// we are displaying only the last submitted quiz result
					/*if(qqr1.getQuizQuestionIdPolling() == qqr2.getQuizQuestionIdPolling())
					{*/
						// Add answer	        							
						QuizQuestionResponse ansqqr = new QuizQuestionResponse() ;
						ansqqr.setQuizAnswerChoiceId(qqr2.getQuizAnswerChoiceId()) ;
						ansqqr.setChoiceText(qqr2.getChoiceText()) ; 
						ansqqr.setCorrectCountForAnswerChoice(qqr2.getCorrectCountForAnswerChoice()) ;
						ansqqr.setStudentCountForClass(qqr2.getStudentCountForClass()) ;
						ansqqr.setDisplaySequence(qqr2.getDisplaySequence()) ;	        	
						correctAnsCount += qqr2.getCorrectCountForAnswerChoice() ;
						ansList.add(ansqqr);	        								        							
					/*}
					else
					{    
						// Add the answer choices to the question
						outerqqr.setQuizAnswerChoices(ansList);	  
						outerqqr.setCorrectCountForAnswerChoice(correctAnsCount) ;
						pollingResult.add(outerqqr) ;
						i = j - 1;	        							
						break ;
					}*/
    			}
        		if(j == temp1.size())
        		{
        			outerqqr.setQuizAnswerChoices(ansList);	      
        			outerqqr.setCorrectCountForAnswerChoice(correctAnsCount) ;
        			pollingResult.add(outerqqr) ;
        			break ;
        		}
        	}
            if(pollingResult.size() > 0)
    		{
    			logger.warn("Returned polling result") ;
    		}
    		else if(pollingResult.size() == 0)
    		{
    			logger.warn("No polling result returned") ;
    		}        	    		
    		
    	}
    	catch (HibernateException he) 
    	{
			processException(he) ;
		}
    	finally
    	{
    		HibernateUtils.closeConnection(session);
    	}
    	return pollingResult ;   
    }
}
