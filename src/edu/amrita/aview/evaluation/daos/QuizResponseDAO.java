/*
 * @(#)QuizResponseDAO.java 4.0 2013/10/04
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
import edu.amrita.aview.evaluation.entities.QuizResponse;



/**
 * This class consists of queries related to quiz response.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QuizResponseDAO extends SuperDAO 
{
    
    /** The logger. */
    private static Logger logger = Logger.getLogger(QuizResponseDAO.class);

    /**
     * Creates the quiz response.
     *
     * @param quizResponse the quiz response
     * @throws AViewException
     */
    public static void createQuizResponse(QuizResponse quizResponse) throws AViewException
	{
		Session session = null ;		
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			session.beginTransaction() ;
			session.saveOrUpdate(quizResponse);			
			session.getTransaction().commit() ;
		}
		catch(HibernateException he)
		{
			processException(he) ;
			session.getTransaction().rollback() ;
		}
		finally
		{
			HibernateUtils.closeConnection(session);
		}
			
	}
    
    /**
     * Gets the quiz response for student.
     *
     * @param quizId the quiz id
     * @param userId the user id
     * @param statusId the status id
     * @return the quiz response for student
     * @throws AViewException
     */
    public static QuizResponse getQuizResponseForStudent(Long quizId, Long userId, Integer statusId) throws AViewException
    {
    	Session session = null;
    	QuizResponse quizResponse = null;
    	try
    	{
    		session = HibernateUtils.getHibernateConnection() ;
			String sqlQuery = "SELECT qr from QuizResponse qr where qr.quizId = :quizId AND " +
							  "qr.userId = :userId AND qr.statusId = :statusId ";
			
			Query hqlQuery = session.createQuery(sqlQuery);
			hqlQuery.setLong("quizId", quizId) ;
			hqlQuery.setLong("userId", userId) ;
			hqlQuery.setInteger("statusId", statusId) ;
			List<QuizResponse> result = hqlQuery.list();			
			if(result.size() == 0)
			{
				logger.warn("No quiz response for the given quiz id and student id");
			}
			else if(result.size() > 0)
    		{
				quizResponse = result.get(0);
    			logger.warn("Returned qr result");
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
    	return quizResponse;
    }
}