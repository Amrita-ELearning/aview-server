/*
 * @(#)QuestionPaperQuestionDAO.java 4.0 2013/10/11
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
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.evaluation.QuizConstant;
import edu.amrita.aview.evaluation.entities.QbQuestion;
import edu.amrita.aview.evaluation.entities.QuestionPaperQuestion;
import edu.amrita.aview.evaluation.vo.QuestionPaperQuestionListVO;



/**
 * This class consists of queries related to question paper question.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QuestionPaperQuestionDAO extends SuperDAO 
{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(QuestionPaperQuestionDAO.class);

	/**
	 * Gets the all active question paper questions.
	 *
	 * @param qbSubCategoryId the qb sub category id
	 * @param qbQuestionTypeId the qb question type id
	 * @param qbCategoryId the qb category id
	 * @param qbDifficultyLevelId the qb difficulty level id
	 * @param qbQuestionId the qb question id
	 * @param questionPaperId the question paper id
	 * @param patternType the pattern type
	 * @param statusId the status id
	 * @return the all active question paper questions
	 * @throws AViewException
	 */
	@SuppressWarnings("unchecked")
	public static List<QuestionPaperQuestion> getAllActiveQuestionPaperQuestions(Long qbSubCategoryId, Long qbQuestionTypeId, 
						Long qbCategoryId, Long qbDifficultyLevelId, Long qbQuestionId, 
						Long questionPaperId, String patternType, Integer statusId) throws AViewException
	{
		Session session = null;
		List<QuestionPaperQuestion> questionPaperQuestions = null;
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "select qpq from QuestionPaperQuestion qpq , QbSubcategory subcat,"
					+ " QbDifficultyLevel dl ,QuestionPaper qp,QbQuestionType qt, QbQuestion qbq, QbCategory qbcat"
					+ " where subcat.statusId = :statusId"
					+ " and qpq.statusId = :statusId"
					+ " and dl.statusId = :statusId"
					+ " and qp.statusId = :statusId"
					+ " and qt.statusId = :statusId"
					+ " and qbqt.statusId = :statusId"
					+ " and qbcat.statusId = :statusId"
					+ " and subcat.qbSubcategoryId = qpq.qbSubcategoryId"
					+ " and subcat.qbCategoryId = qbcat.qbCategoryId"
					+ " and dl.qbDifficultyLevelId = qpq.qbDifficultyLevelId"
					+ " and qp.questionPaperId = qpq.questionPaper.questionPaperId"
					+ " and qt.qbQuestionTypeId = qpq.qbQuestionTypeId"
					+ " and qbq.qbQuestionId = qpq.qbQuestionId";
			
			if(qbSubCategoryId != null)
			{
				hqlQueryString += " and subcat.qbSubCategoryId = :qbSubCategoryId";
			}
			if(qbQuestionTypeId != null)
			{
				hqlQueryString += " and qt.qbQuestionTypeId = :qbQuestionTypeId";
			}
			if(qbCategoryId != null)
			{
				hqlQueryString += " and qbcat.qbCategoryId = :qbCategoryId";
			}
			if(qbQuestionId != null)
			{
				hqlQueryString += " and qbq.qbQuestionId = :qbQuestionId";
			}
			if(qbDifficultyLevelId != null)
			{
				hqlQueryString += " and dl.qbDifficultyLevelId = :qbDifficultyLevelId";
			}
			if(questionPaperId != null)
			{
				hqlQueryString += " and qp.questionPaperId = :questionPaperId";
			}
			if(patternType != null)
			{
				hqlQueryString += " qpq.patternType = :patternType";
			}
			Query hqlQuery = session.createQuery(hqlQueryString);
			hqlQuery.setInteger("statusId", statusId);
			
			if(qbSubCategoryId != null)
			{
				hqlQuery.setLong("qbSubCategoryId", qbSubCategoryId);
			}
			if(qbQuestionTypeId != null)
			{
				hqlQuery.setLong("qbQuestionTypeId", qbQuestionTypeId);
			}
			if(qbCategoryId != null)
			{
				hqlQuery.setLong("qbCategoryId", qbCategoryId);
			}
			if(qbQuestionId != null)
			{
				hqlQuery.setLong("qbQuestionId" ,qbQuestionId);
			}
			if(qbDifficultyLevelId != null)
			{
				hqlQuery.setLong("qbDifficultyLevelId" ,qbDifficultyLevelId);
			}
			if(questionPaperId != null)
			{
				hqlQuery.setLong("questionPaperId" ,questionPaperId);
			}
			if(patternType != null)
			{
				hqlQuery.setString("patternType", patternType);
			}
			questionPaperQuestions = hqlQuery.list();
			if(questionPaperQuestions.size()>0)
			{
				logger.info("Returned question paper questions ");
			}
			else if(questionPaperQuestions.size() == 0)
			{
				logger.warn("Warning :: No question paper question ");
			}

		} 
		catch (HibernateException he) 
		{
			processException(he);
		} 
		finally 
		{
			HibernateUtils.closeConnection(session);
		}

		return questionPaperQuestions;
	}
	
	/**
	 * Gets the question paper question.
	 *
	 * @param questionPaperQuestionId the question paper question id
	 * @return the question paper question
	 * @throws AViewException
	 */
	public static QuestionPaperQuestion getQuestionPaperQuestion(Long questionPaperQuestionId) throws AViewException 
	{
		Session session = null;
		QuestionPaperQuestion questionpaper = null;
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			questionpaper = (QuestionPaperQuestion)session.get(QuestionPaperQuestion.class, questionPaperQuestionId);
		}
		catch (HibernateException he) 
		{
			processException(he);   
		}
		finally 
		{
			HibernateUtils.closeConnection(session);
		}
		return questionpaper;
	}

	/**
	 * Creates the question paper question.
	 *
	 * @param questionPaperQuestion the question paper question
	 * @throws AViewException
	 */
	public static void createQuestionPaperQuestion(QuestionPaperQuestion questionPaperQuestion) throws AViewException
	{
		Session session = null;
		
		// Set nos. of random questions to null , for specific question
		if (questionPaperQuestion.getNumRandomQuestions() == 0)
		{
			questionPaperQuestion.setNumRandomQuestions(null);
		}
		
		// Set the question id as null for random question
		if (questionPaperQuestion.getQbQuestionId() == 0)
		{
			questionPaperQuestion.setQbQuestionId(null);
		}
		
		// Set the subcategory id as null for specific question
		if (questionPaperQuestion.getQbSubcategoryId() == 0)
		{
			questionPaperQuestion.setQbSubcategoryId(null);
		}
		
		// Set the difficulty level id as null for random question
		if (questionPaperQuestion.getQbDifficultyLevelId() == 0)
		{
			questionPaperQuestion.setQbDifficultyLevelId(null);
		}
		
		// Set the question type id as null for random question
		if (questionPaperQuestion.getQbQuestionTypeId() == 0)
		{
			questionPaperQuestion.setQbQuestionTypeId(null);
		}
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.save(questionPaperQuestion);
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
	}

	/**
	 * Update question paper question.
	 *
	 * @param questionPaperQuestion the question paper question
	 * @throws AViewException
	 */
	public static void updateQuestionPaperQuestion(QuestionPaperQuestion questionPaperQuestion) throws AViewException
	{
		Session session = null;
		
		// Set nos. of random questions to null , for specific question
		if (questionPaperQuestion.getNumRandomQuestions() == 0)
		{
			questionPaperQuestion.setNumRandomQuestions(null);
		}
		
		// Set the question id as null for random question
		if (questionPaperQuestion.getQbQuestionId() == 0)
		{
			questionPaperQuestion.setQbQuestionId(null);
		}
		
		// Set the subcategory id as null for specific question
		if (questionPaperQuestion.getQbSubcategoryId() == 0)
		{
			questionPaperQuestion.setQbSubcategoryId(null);
		}
		
		//Fix for Bug#16096,16097,16101
		// Set the difficulty level id as null for specific question and for random question,if it is set to zero. 
		if (questionPaperQuestion.getPatternType().equals(QuizConstant.SPECIFIC_PATTERN_TYPE) || questionPaperQuestion.getQbDifficultyLevelId() == 0)
		{
			questionPaperQuestion.setQbDifficultyLevelId(null);
		}
		
		//Fix for Bug#16096,16097,16101
		// Set the question type id as null for specific question and for random question,if it is set to zero. 
		if (questionPaperQuestion.getPatternType().equals(QuizConstant.SPECIFIC_PATTERN_TYPE) || questionPaperQuestion.getQbQuestionTypeId() == 0)
		{
			questionPaperQuestion.setQbQuestionTypeId(null);
		}
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.update(questionPaperQuestion);
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
	}

	/**
	 * Gets the all active specific questions for question paper.
	 *
	 * @param questionPaperId the question paper id
	 * @param activeSId the active s id
	 * @return the all active specific questions for question paper
	 * @throws AViewException
	 */
	@SuppressWarnings("unchecked")
	public static List<QuestionPaperQuestionListVO> getAllActiveSpecificQuestionsForQuestionPaper(Long questionPaperId, Integer activeSId) throws AViewException
	{
		Session session = null;
		List<QuestionPaperQuestionListVO> questionPaperSpecificQuestions = new ArrayList<QuestionPaperQuestionListVO>();
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			Query hqlQuery = session
					.createQuery("select q"
							// Get the name of difficulty level
							+ " , (select dl.qbDifficultyLevelName from QbDifficultyLevel dl"
							+ " where dl.qbDifficultyLevelId=q.qbDifficultyLevelId) as qbDifficultyLevelName"
							// Get the name of question type
							+ " , (select qt.qbQuestionTypeName from QbQuestionType qt"
							+ " where qt.qbQuestionTypeId=q.qbQuestionTypeId) as qbQuestionTypeName"
							+ " from QbQuestion q"
							// Get the question which are there in question paper question
							+ " where q.qbQuestionId in "
							+ " (select qpq.qbQuestionId from QuestionPaperQuestion qpq"
							+ " where qpq.questionPaper.questionPaperId = :questionPaperId"
							+ " and qpq.statusId = :activeSId)");

			hqlQuery.setLong("questionPaperId", questionPaperId);
			hqlQuery.setInteger("activeSId", activeSId);

			List<Object[]> temp = hqlQuery.list() ;

			for(Object[] obj:temp)
			{
				QuestionPaperQuestionListVO qpq = new QuestionPaperQuestionListVO() ;
				QbQuestion qb = (QbQuestion)obj[0] ;
				qb.setQbDifficultyLevelName(obj[1].toString() ) ;
				qb.setQbQuestionTypeName(obj[2].toString()) ;
				qpq.qbQuestion = qb ;
				questionPaperSpecificQuestions.add(qpq) ;
			}

		} 
		catch (HibernateException he) 
		{
			processException(he);
		} 
		finally 
		{
			HibernateUtils.closeConnection(session);
		}

		return questionPaperSpecificQuestions;
	}

	/**
	 * Gets the all active question paper questions for qp.
	 *
	 * @param questionPaperId the question paper id
	 * @param userId the user id
	 * @param statusId the status id
	 * @return the all active question paper questions for qp
	 * @throws AViewException
	 */
	@SuppressWarnings("unchecked")
	public static List<QuestionPaperQuestion> getAllActiveQuestionPaperQuestionsForQP(Long questionPaperId, Long userId, Integer statusId)throws AViewException 
	{
		Session session = null;
		List<QuestionPaperQuestion> questionPaperQuestions = null;
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "select qpq from QuestionPaperQuestion qpq,QbSubcategory subcat, QbCategory cat " +
									" where qpq.questionPaper.questionPaperId = :questionPaperId " +
									" AND subcat.qbSubcategoryId = qpq.qbSubcategoryId " +
									" AND subcat.createdByUserId = :userId and subcat.statusId = :statusId" +
									" AND subcat.qbCategoryId = cat.qbCategoryId AND cat.createdByUserId = :userId and cat.statusId = :statusId " +
									" and qpq.statusId = :statusId order by qpq.createdDate";
			Query hqlQuery = session.createQuery(hqlQueryString);
			hqlQuery.setLong("questionPaperId", questionPaperId);
			hqlQuery.setLong("userId", userId) ;
			hqlQuery.setInteger("statusId",	statusId);
			questionPaperQuestions = hqlQuery.list();
			if(questionPaperQuestions.size()>0)
			{
				logger.info("Returned question paper questions ");
			}
			else if(questionPaperQuestions.size() == 0)
			{
				logger.warn("Warning :: No question paper questions");
			}

		} 
		catch (HibernateException he) 
		{
			processException(he);
		} 
		finally 
		{
			HibernateUtils.closeConnection(session);
		}

		return questionPaperQuestions;
	}

	/**
	 * Gets the qb question from question paper by pattern type.
	 *
	 * @param patternType the pattern type
	 * @param questionPaperId the question paper id
	 * @param statusId the status id
	 * @return the qb question from question paper by pattern type
	 * @throws AViewException
	 */
	public static List<QuestionPaperQuestion> getQbQuestionFromQuestionPaperByPatternType(String patternType, Long questionPaperId, Integer statusId) throws AViewException
	{
		Session session = null;
		Query hqlQuery = null;
		List<QuestionPaperQuestion> questionPaperQuestions = null;
		String sqlQuery = "SELECT qpq FROM QuestionPaperQuestion qpq WHERE " +
						  "qpq.patternType = :patternType AND " +
						  "qpq.questionPaper.questionPaperId = :questionPaperId AND " +
						  "qpq.statusId = :statusId ";
		try
		{
			session = HibernateUtils.getHibernateConnection();
			hqlQuery = session.createQuery(sqlQuery);
			hqlQuery.setString("patternType", patternType);
			hqlQuery.setLong("questionPaperId", questionPaperId);
			hqlQuery.setInteger("statusId", StatusHelper.getActiveStatusId());
			questionPaperQuestions = hqlQuery.list();
		}
		catch(HibernateException he)
		{
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session);
		}return questionPaperQuestions;
	}
	
	/**
	 * Check ifqpq exists in quiz qp.
	 *
	 * @param questionPaperQuestionId the question paper question id
	 * @return the boolean
	 * @throws AViewException
	 */
	public static Boolean checkIfqpqExistsInQuizQP(Long questionPaperQuestionId) throws AViewException
	{
		Session session = null ;
		Boolean qbQuestionflag = false ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			String hqlQueryString = "SELECT qpq.statusId " +
													"FROM QuizQuestion qq , QuestionPaperQuestion qpq " +
													"WHERE qpq.questionPaperQuestionId = qq.questionPaperQuestionId " +
													"AND qpq.questionPaperQuestionId = :questionPaperQuestionId " +
													" AND qpq.statusId = "+StatusHelper.getActiveStatusId()+" " +
													"GROUP BY qpq.questionPaperQuestionId" +
													" HAVING qpq.statusId = "+StatusHelper.getActiveStatusId()+" " ;
			
			Query hqlQuery = session.createQuery(hqlQueryString);
			hqlQuery.setLong("questionPaperQuestionId", questionPaperQuestionId) ;
			List<QuestionPaperQuestion> qbqList = hqlQuery.list() ;
			
			if(qbqList.size() > 0)
			{
				qbQuestionflag = true ;
			}
			
		}
		catch(HibernateException he)
		{
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session) ;
		}
		
		return qbQuestionflag ;
	}
	/**
	 * Delete question paper question.
	 *
	 * @param questionPaperQuestions the question paper questions to be deleted
	 * @throws AViewException
	 */
	//Fix for Bug#16096,16097,16101 :Start
	public static void deleteQuestionPaperQuestions(List<QuestionPaperQuestion> questionPaperQuestions) throws AViewException
	{
		Session session = null;
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();

			for(int i = 0;i < questionPaperQuestions.size();i++)
			{
				QuestionPaperQuestion questionPaperQuestion = questionPaperQuestions.get(i);
				session.delete(questionPaperQuestion);
				if((i+1) % HibernateUtils.HIBERNATE_BATCH_SIZE == 0)
				{
					session.flush();
					session.clear();								
				}
			}
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
	}
	//Fix for Bug#16096,16097,16101:End
}
