/*
 * @(#)QuizQuestionDAO.java 4.0 2013/10/05
 * 
 * Copyright  © 2013 E-Learning Research Lab, 
 * Amrita Vishwa Vidyapeetham. All rights reserved. 
 * E-Learning Research Lab and the A-VIEW logo are trademarks or
 * registered trademarks of E-Learning Research Lab. 
 * All other names used are the trademarks of their respective owners.
 */
package edu.amrita.aview.evaluation.daos;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.evaluation.QuizConstant;
import edu.amrita.aview.evaluation.entities.QuizQuestion;



/**
 * This class consists of queries related to quiz question.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QuizQuestionDAO extends SuperDAO 
{
    
    /** The logger. */
    private static Logger logger = Logger.getLogger(QuizQuestionDAO.class);

    /**
     * Creates the quiz question.
     *
     * @param quizQuestion the quiz question
     * @throws AViewException
     */
    public static void createQuizQuestion(QuizQuestion quizQuestion) throws AViewException
	{
		Session session = null;		
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			session.beginTransaction();
			session.saveOrUpdate(quizQuestion);			
			quizQuestion.getQuizQuestionId() ;
			session.getTransaction().commit() ;			
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
	}
	
	/**
	 * Update quiz question.
	 *
	 * @param quizQuestion the quiz question
	 * @throws AViewException
	 */
	public static void updateQuizQuestion(QuizQuestion quizQuestion) throws AViewException
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			session.beginTransaction();
			session.update(quizQuestion);
			session.getTransaction().commit() ;
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
	}
    
    
    /**
     * Gets the all active quiz questions.
     *
     * @param statusId the status id
     * @return the all active quiz questions
     * @throws AViewException
     */
    public static List<QuizQuestion> getAllActiveQuizQuestions(Integer statusId)
															   throws AViewException															  
	{
		Session session = null ;
		List<QuizQuestion> quizQuestions = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "select qzquest from QuizQuestion qzquest where statusId = :statusId";
		    Query hqlQuery = session.createQuery(hqlQueryString);
			hqlQuery.setInteger("statusId", statusId);
			quizQuestions = hqlQuery.list();
			if(quizQuestions.size()>0)
            {
                logger.info("Returned quiz questions ");
            }
            else if(quizQuestions.size() == 0)
            {
                logger.warn("Warning :: No quiz question ");
            }
		}
		catch(HibernateException he)
		{
			processException(he);
		}
		finally
		{
			HibernateUtils.closeConnection(session) ;
		}
		return quizQuestions ;
	}
	
	/**
	 * Gets the quiz question id.
	 *
	 * @param quizQuestionId the quiz question id
	 * @return the quiz question id
	 * @throws AViewException
	 */
	public static QuizQuestion getQuizQuestionId(Long quizQuestionId) throws AViewException
	{
	    Session session = null;
	    QuizQuestion quizQuestion = null;
        try 
        {
            session = HibernateUtils.getHibernateConnection();
            quizQuestion = (QuizQuestion)session.get(QuizQuestion.class, quizQuestionId);
        }
        catch (HibernateException he) 
        {
            processException(he);   
        }
        finally 
        {
            HibernateUtils.closeConnection(session);
        }
        return quizQuestion;
	}
	
	
	
	/**
	 * Gets the quiz questions id for quiz.
	 *
	 * @param quizId the quiz id
	 * @return the quiz questions id for quiz
	 * @throws AViewException
	 */
	public static List<Long> getQuizQuestionsIdForQuiz(Long quizId) throws AViewException
	{
		int i ;
		Session session = null ;
		List<Long> quizQuest = null ;
		String creationMessage = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			
			String hqlQueryString = "select qq.quizQuestionId " +
									" from QuizQuestion qq " +
									" where qq.quiz.quizId = :quizId " ;
			
			Query hqlQuery = session.createQuery(hqlQueryString) ;
			hqlQuery.setLong("quizId", quizId) ;
			if(hqlQuery.list().size() > 0)
			{
				quizQuest = hqlQuery.list() ;		
			}
			else
			{
				return quizQuest;
			}
			
		}
		catch(HibernateException he)
		{
			processException(he);			
		}
		finally
		{
			HibernateUtils.closeConnection(session) ;
		}
		
		return quizQuest ;
	}
	
	/**
	 * Gets the quiz questions for quiz.
	 *
	 * @param quizId the quiz id
	 * @param statusId the status id
	 * @return the quiz questions for quiz
	 * @throws AViewException
	 */
	public static List<QuizQuestion> getQuizQuestionsForQuiz(Long quizId,Integer statusId) throws AViewException
    {
        Session session = null ;
        List<QuizQuestion> quizQuestions = new ArrayList<QuizQuestion>() ;
            try
            {
                session = HibernateUtils.getHibernateConnection() ;
                String hqlQueryString = "select distinct qzquest from QuizQuestion qzquest " +
                						"left join fetch qzquest.quizAnswerChoices as qzAns " +
                						"where qzquest.quiz.quizId = :quizId " +
                						"AND qzAns.statusId = :statusId ORDER BY qzAns.displaySequence";
                Query hqlQuery = session.createQuery(hqlQueryString);
                hqlQuery.setInteger("statusId", statusId);
                hqlQuery.setLong("quizId", quizId);
                quizQuestions = hqlQuery.list() ;
                if(quizQuestions.size()>0)
                {
                    logger.info("Returned quiz questions ");
                }
                else if(quizQuestions.size() == 0)
                {
                    logger.warn("Warning :: No quiz question ");
                }
        }catch(HibernateException he){
            processException(he) ;
        }finally{
            HibernateUtils.closeConnection(session) ;
        }
        return quizQuestions;
    }
	
	/**
	 * Gets the polling quiz for student.
	 *
	 * @param userId the user id
	 * @param statusId the status id
	 * @return the polling quiz for student
	 * @throws AViewException
	 */
	public static List<QuizQuestion> getPollingQuizForStudent(Long userId) throws AViewException
    {
        Session session = null ;
        List<QuizQuestion> quizQuestions = new ArrayList<QuizQuestion>() ;
            try
            {
                session = HibernateUtils.getHibernateConnection() ;
                String hqlQueryString = "select qq FROM QuizQuestion qq,QuizResponse qr WHERE qq.quiz.quizId=qr.quizId" +
                						" AND qq.categoryName='"+QuizConstant.POLLING_QUESTION_TYPE+"' AND qq.subcategoryName='"+QuizConstant.POLLING_QUESTION_TYPE+"' AND qr.userId=:userId";
                Query hqlQuery = session.createQuery(hqlQueryString);             
                hqlQuery.setLong("userId", userId);
                quizQuestions = hqlQuery.list() ;
                if(quizQuestions.size()>0)
                {
                    logger.info("Returned quiz questions ");
                }
                else if(quizQuestions.size() == 0)
                {
                    logger.warn("Warning :: No quiz question ");
                }
        }catch(HibernateException he){
            processException(he) ;
        }finally{
            HibernateUtils.closeConnection(session) ;
        }
        return quizQuestions;
    }
	
}
