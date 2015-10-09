/*
 * @(#)QuizAnswerChoiceDAO.java 4.0 2013/10/05
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
import edu.amrita.aview.evaluation.entities.QuizAnswerChoice;
import edu.amrita.aview.evaluation.entities.QuizQuestion;



/**
 * This class consists of queries related to quiz answer choice.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QuizAnswerChoiceDAO extends SuperDAO 
{
    
    /** The logger. */
    private static Logger logger = Logger.getLogger(QuizAnswerChoiceDAO.class);

	/**
	 * Gets the answer.
	 *
	 * @param quizLen the length of quiz list
	 * @param quizQuestionIds the quiz question ids
	 * @return the answer
	 * @throws AViewException
	 */
	public static  List<QuizAnswerChoice> getAnswer(Integer quizLen,List<QuizQuestion>quizQuestionIds) throws AViewException
	{
		Session session = null ;
		List<QuizAnswerChoice> quizzes = new ArrayList<QuizAnswerChoice>() ;
		
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			for(int i=0;i<quizLen;i++)
			{
			    Long quizQuestionId = quizQuestionIds.get(i).getQuizQuestionId() ;
				String hqlQueryString = "select u from QuizAnswerChoice u where u.quizQuestion.quizQuestionId = :quizQuestionId ORDER BY u.displaySequence";
				Query hqlQuery = session.createQuery(hqlQueryString);
				hqlQuery.setLong("quizQuestionId", quizQuestionId);
				quizzes.addAll((ArrayList<QuizAnswerChoice>)hqlQuery.list()) ;
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
		
		return quizzes ;
		
	}
}
