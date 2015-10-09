/*
 * @(#)QuizQuestionChoiceResponseDAO.java 4.0 2013/10/05
 * 
 * Copyright  © 2013 E-Learning Research Lab, 
 * Amrita Vishwa Vidyapeetham. All rights reserved. 
 * E-Learning Research Lab and the A-VIEW logo are trademarks or
 * registered trademarks of E-Learning Research Lab. 
 * All other names used are the trademarks of their respective owners.
 */
package edu.amrita.aview.evaluation.daos;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.evaluation.entities.QuizQuestionChoiceResponse;



/**
 * This class consists of queries related to answer response for a quiz.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QuizQuestionChoiceResponseDAO extends SuperDAO 
{
    
    /** The logger. */
    private static Logger logger = Logger.getLogger(QuizQuestionChoiceResponseDAO.class);

    
	/**
	 * Gets the all active quiz question choice responses.
	 *
	 * @param qzQsChRsStatusId the qz qs ch rs status id
	 * @param qzAnsChStatusId the qz ans ch status id
	 * @param qzQsRsStatusId the qz qs rs status id
	 * @return the all active quiz question choice responses
	 * @throws AViewException
	 */
	public static List<QuizQuestionChoiceResponse> getAllActiveQuizQuestionChoiceResponses(Integer qzQsChRsStatusId,Integer qzAnsChStatusId,Integer qzQsRsStatusId)
		throws AViewException
	{
		Session session = null ;
		List<QuizQuestionChoiceResponse> quizQuestionChoiceResponses = null ;
		
		try{
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "select qzQsChRs from QuizQuestionChoiceResponse qzQsChRs,QuizAnswerChoice qzAnsCh , QuizQuestionResponse qzQsRs" +
									" where qzQsChRs.statusId = :qzQsChRsStatusId " +
									" and qzAnsCh.statusId = :qzAnsChStatusId " +
									" and qzQsRs.statusId = :qzQsRsStatusId" +
									" and qzQsChRs.quizAnswerChoiceId = qzAnsCh.quizAnswerChoiceId " +
									" and qzQsChRs.quizQuestionResponse.quizQuestionResponseId = qzQsRs.quizQuestionResponseId";
			Query hqlQuery = session.createQuery(hqlQueryString);
			hqlQuery.setInteger("qzQsChRsStatusId", qzQsChRsStatusId);
			hqlQuery.setInteger("qzAnsChStatusId", qzAnsChStatusId);
			hqlQuery.setInteger("qzQsRsStatusId", qzQsRsStatusId);
			
			quizQuestionChoiceResponses = hqlQuery.list() ;
			 if(quizQuestionChoiceResponses.size()>0)
             {
                 logger.info("Returned quiz question choice responses ");
             }
             else if(quizQuestionChoiceResponses.size() == 0)
             {
                 logger.warn("Warning :: No quiz question choice response ");
             }
			
		}catch(HibernateException he){
			processException(he);
		}finally{
			HibernateUtils.closeConnection(session);
		}
		
		return quizQuestionChoiceResponses ;
	}
		
	/**
	 * Gets the quiz question choice response by quiz response id.
	 *
	 * @param quizResponseId the quiz response id
	 * @param statusId the status id
	 * @return the quiz question choice response by quiz response id
	 * @throws AViewException
	 */
	public static List<QuizQuestionChoiceResponse> getQuizQuestionChoiceResponseByQuizResponseId(Long quizResponseId, Integer statusId) throws AViewException
	{
		Session session = null ;
		List<QuizQuestionChoiceResponse> quizQuestionChoiceResponses = null ;
		
		String hqlQueryString = "SELECT qqcr " +
								"FROM QuizQuestionChoiceResponse qqcr, " +
								"QuizQuestionResponse qqr " +
								"WHERE qqcr.quizQuestionResponse.quizQuestionResponseId = qqr.quizQuestionResponseId " +
								"AND qqr.quizResponse.quizResponseId = :quizResponseId AND qqcr.statusId = :statusId";		
		try
		{
			session = HibernateUtils.getHibernateConnection();
			Query hqlQuery = session.createQuery(hqlQueryString);
			hqlQuery.setLong("quizResponseId", quizResponseId);
			hqlQuery.setInteger("statusId", statusId);			
			quizQuestionChoiceResponses = hqlQuery.list() ;
			if(quizQuestionChoiceResponses.size()>0)
	        {
				logger.info("Returned quiz question choice responses ");
	        }
			else if(quizQuestionChoiceResponses.size() == 0)
			{
				logger.warn("Warning :: No quiz question choice response ");
			}			
		}
		catch(HibernateException he)
		{
			processException(he);
		}
		finally
		{
			HibernateUtils.closeConnection(session);
		}
		return quizQuestionChoiceResponses ;
	}
}