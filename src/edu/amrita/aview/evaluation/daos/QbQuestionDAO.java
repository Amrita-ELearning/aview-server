/*
 * @(#)QbQuestionDAO.class 4.0 2013/09/17
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
import edu.amrita.aview.evaluation.entities.QbQuestionType;
import edu.amrita.aview.evaluation.helpers.QbCategoryHelper;
import edu.amrita.aview.evaluation.helpers.QbQuestionTypeHelper;
import edu.amrita.aview.evaluation.helpers.QbSubcategoryHelper;



/**
 * This class deals with the queries related to a question .
 *
 * @author Swati 
 * @version 4.0 
 * @since 3.0
 */
public class QbQuestionDAO extends SuperDAO
{
	/** The logger. */
	private static Logger logger = Logger.getLogger(QbQuestionDAO.class);

	/**
	 * Creates the qb question.
	 *
	 * @param qbQuestion the qb question
	 * @throws AViewException
	 */
	public static void createQbQuestion(QbQuestion qbQuestion) throws AViewException
	{
		Session session = null;
		// To avoid constraint violation exception since it references qbQuestionId(primary key)
		if (qbQuestion.getParentId() == 0)
		{
			qbQuestion.setParentId(null);
		}

		try 
		{
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.save(qbQuestion);
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
	 * Update qb question.
	 *
	 * @param qbQuestion the qb question
	 * @throws AViewException
	 */
	/*
	public static void updateQbQuestion(QbQuestion qbQuestion) throws AViewException
	{
		Session session = null;

		// To avoid constraint violation exception since it references qbQuestionId
		if ((qbQuestion.getParentId() != null) && 
				(qbQuestion.getParentId() == 0))
		{
			qbQuestion.setParentId(null);
		}

		try 
		{
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.saveOrUpdate(qbQuestion);
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
	*/

	public static void updateQbQuestion(QbQuestion qbQuestion) throws AViewException
	{
		// To avoid constraint violation exception since it references qbQuestionId
		if ((qbQuestion.getParentId() != null) && 
				(qbQuestion.getParentId() == 0))
		{
			qbQuestion.setParentId(null);
		}				
		Session session = HibernateUtils.getCurrentHibernateConnection();
		session.update(qbQuestion);
		
	}
	
	/**
	 * Gets the all active qb questions.
	 *
	 * @param statusId the status id
	 * @return the all active qb questions
	 * @throws AViewException
	 */
	@SuppressWarnings("unchecked")
	public static List<QbQuestion> getAllActiveQbQuestions(Integer statusId) throws AViewException
			{

		Session session = null;
		List<QbQuestion> qbQuestions = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "select quest from QbQuestion quest,QbSubcategory subcat, QbQuestionType qt, QbDifficultyLevel dl,"
					+ " QbCategory qbc "
					+ " where quest.statusId = :statusId "
					+ " and qbc.statusId = :statusId "
					+ " and subcat.statusId = :statusId "
					+ " and qt.statusId = :statusId "
					+ " and dl.statusId = :statusId "
					+ " and quest.qbSubcategoryId = subcat.qbSubcategoryId "
					+ " and subcat.qbCategoryId = qbc.qbCategoryId "
					+ " and quest.qbQuestionTypeId = qt.qbQuestionTypeId "
					+ " and quest.qbDifficultyLevelId = dl.qbDifficultyLevelId ";
			Query hqlQuery = session.createQuery(hqlQueryString);
			hqlQuery.setInteger("statusId", statusId);
			qbQuestions = hqlQuery.list();

			if(qbQuestions.size()>0)
			{
				logger.info("Returned questions ");
			}
			else if(qbQuestions.size() == 0)
			{
				logger.warn("Warning :: No question ");
			}
		} catch (HibernateException he) {
			processException(he) ;
		} finally {
			HibernateUtils.closeConnection(session);
		}
		return qbQuestions;
			}
	
	/**
	 * Gets the qb question and answer choices.
	 *
	 * @param qbQuestionId the qb question id
	 * @return the qb question
	 * @throws AViewException
	 */
	@SuppressWarnings("unchecked")
	public static QbQuestion getQbQuestion(Long qbQuestionId)throws AViewException 
	{
		Session session = null;
		QbQuestion qbQuestion = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "select distinct q from QbQuestion q left join fetch q.qbAnswerChoices as ans"
					+ " where ans.statusId = :statusId "
					+ " and q.qbQuestionId = :qbQuestionId ORDER BY ans.displaySequence";
			Query hqlQuery = session.createQuery(hqlQueryString);
			hqlQuery.setLong("qbQuestionId", qbQuestionId);	
			hqlQuery.setInteger("statusId", StatusHelper.getActiveStatusId());
			List<QbQuestion> qbQuesLst = hqlQuery.list();
			if(qbQuesLst.size()>0)
			{
				logger.info("Returned questions ");
			}
			else if(qbQuesLst.size() == 0)
			{
				logger.warn("Warning :: No question ");
			}
			if (qbQuesLst.size() == 1) 
			{
				qbQuestion = (QbQuestion) qbQuesLst.get(0);
			}

		} catch (HibernateException he) {
			processException(he);
		} finally {
			HibernateUtils.closeConnection(session);
		}

		return qbQuestion;
	}

	// method added by Radha
	/**
	 * Gets the all active qb questions for subcategory.
	 *
	 * @param subcategoryId the subcategory id
	 * @param activeSId the active s id
	 * @return the all active qb questions for subcategory
	 * @throws AViewException
	 */
	@SuppressWarnings("unchecked")
	public static List<QbQuestion> getAllActiveQbQuestionsForSubcategory(Long subcategoryId, Integer activeSId) throws AViewException
	{
		Session session = null;
		List<QbQuestion> qbQuestions = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "select distinct q from QbQuestion q left join fetch q.qbAnswerChoices as ans"
					+ " where q.statusId = :activeSId "
					+ " and q.qbSubcategoryId = :subcategoryId"
					+ " and ans.statusId = :activeSId" 
					+ " ORDER BY ans.displaySequence";
			Query hqlQuery = session.createQuery(hqlQueryString);
			hqlQuery.setLong("subcategoryId", subcategoryId);
			hqlQuery.setInteger("activeSId", activeSId);
			qbQuestions = hqlQuery.list();
			if(qbQuestions.size()>0)
			{
				logger.info("Returned questions ");
			}
			else if(qbQuestions.size() == 0)
			{
				logger.warn("Warning :: No question ");
			}

		} catch (HibernateException he) {
			processException(he);
		} finally {
			HibernateUtils.closeConnection(session);
		}
		return qbQuestions;
	}
	
	// method added by Radha
	/**
	 * Gets the all active qb questions for subcategories.
	 *
	 * @param subcategoryIds the subcategory ids
	 * @param activeSId the active s id
	 * @return the all active qb questions for subcategories
	 * @throws AViewException
	 */
	@SuppressWarnings("unchecked")
	public static List<QbQuestion> getAllActiveQbQuestionsForSubcategories(List<Long>subcategoryIds, Integer activeSId) throws AViewException
	{
		Session session = null;
		List<QbQuestion> qbQuestions = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "select distinct q from QbQuestion q left join fetch q.qbAnswerChoices as ans"
					+ " where q.statusId = :activeSId "
					+ " and q.qbSubcategoryId IN (:subcategoryIds)"
					+ " and ans.statusId = :activeSId"
					+ " ORDER BY ans.displaySequence";
			Query hqlQuery = session.createQuery(hqlQueryString);
			hqlQuery.setParameterList("subcategoryIds", subcategoryIds);
			hqlQuery.setInteger("activeSId", activeSId);
			qbQuestions = hqlQuery.list();
			if(qbQuestions.size()>0)
			{
				logger.info("Returned questions ");
			}
			else if(qbQuestions.size() == 0)
			{
				logger.warn("Warning :: No question ");
			}

		} catch (HibernateException he) {
			processException(he);
		} finally {
			HibernateUtils.closeConnection(session);
		}
		return qbQuestions;
	}

	/**
	 * Gets the qb questions for a given search criteria.
	 *
	 * @param categoryId the category id
	 * @param subcategoryId the subcategory id
	 * @param questionTypeId the question type id
	 * @param difficultyLevelId the difficulty level id
	 * @param qtext the qtext
	 * @param userId the user id
	 * @param qtForPolling the question type for polling
	 * @param statusId the status id
	 * @return the qb questions
	 * @throws AViewException
	 */
	@SuppressWarnings("unchecked")	
	public static List<QbQuestion> getQbQuestions(Long categoryId, Long subcategoryId, Long questionTypeId, Long difficultyLevelId, 
													String qtext, Long userId, Long qtForPolling, 
													Integer statusId) throws AViewException
	{
		Session session = null;
		List<QbQuestion> qbQuestions = new ArrayList<QbQuestion>();
		
		Long qbCatIdForPolling = QbCategoryHelper.getQbCategoryByName(QuizConstant.POLLING_QUESTION_TYPE) ;
		
		Long qbSubcatIdForPolling = QbSubcategoryHelper.getQbSubcategoryForName(QuizConstant.POLLING_QUESTION_TYPE).getQbSubcategoryId() ;
		QbQuestionType qpQuestionTypeForPolling=QbQuestionTypeHelper.getQbQuestionTypeByName(QuizConstant.POLLING_QUESTION_TYPE);
		try {
			session = HibernateUtils.getHibernateConnection();

			String hqlQueryString = "select quest from QbQuestion quest,QbSubcategory subcat, QbQuestionType qt, QbDifficultyLevel dl,"
									+ " QbCategory qbc "
									+ " where quest.statusId = :statusId "
									+ " and qbc.statusId = :statusId "
									+ " and subcat.statusId = :statusId "
									+ " and qt.statusId = :statusId "
									+ " and dl.statusId = :statusId "
									+ " and quest.qbSubcategoryId = subcat.qbSubcategoryId " 
									+ " and subcat.qbCategoryId = qbc.qbCategoryId "
									+ " and quest.qbQuestionTypeId = qt.qbQuestionTypeId "
									+ " and quest.qbDifficultyLevelId = dl.qbDifficultyLevelId "
									+ " and quest.createdByUserId = :userId " ;
			
			// filter the questions which includes multiple choice and multiple response
			if(qtForPolling != null)
			{
				hqlQueryString += " and quest.qbQuestionTypeId <> "+qtForPolling+" ";
			}

			// filter the questions as per question type
			if((questionTypeId != null) && 
					(questionTypeId > 0))
			{
				hqlQueryString += " and qt.qbQuestionTypeId = :questionTypeId ";
				// filter the particular question type other than polling 
				if(questionTypeId !=qpQuestionTypeForPolling.getQbQuestionTypeId())
				{
					hqlQueryString += " and quest.qbSubcategoryId <> "+qbSubcatIdForPolling+" ";
					hqlQueryString +=" and subcat.qbCategoryId <> "+qbCatIdForPolling+""; 
				}
			}
			
			// filter the questions as per difficulty level
			if((difficultyLevelId != null) && (difficultyLevelId > 0))
			{
				hqlQueryString += " and dl.qbDifficultyLevelId = :difficultyLevelId ";
			}
			
			// filter the questions as per question text
			if (qtext != null)
			{
				hqlQueryString += " and quest.questionText like :qtext ";
			}
			
			// filter the questions as per subcategory
			if((subcategoryId != null) && (subcategoryId > 0))
			{
				hqlQueryString += " and subcat.qbSubcategoryId = :subcategoryId ";
			}
			
			// filter the questions as per category
			if((categoryId != null) && (categoryId > 0))
			{
				hqlQueryString += " and qbc.qbCategoryId = :categoryId ";
			}
		
			Query hqlQuery = session.createQuery(hqlQueryString);
			hqlQuery.setInteger("statusId", statusId);
			hqlQuery.setLong("userId", userId) ;
			if((subcategoryId != null) && (subcategoryId > 0))
			{
				hqlQuery.setLong("subcategoryId", subcategoryId);
			}
			if((categoryId != null) && (categoryId > 0))
			{
				hqlQuery.setLong("categoryId", categoryId);
			}
			if((questionTypeId != null) && (questionTypeId > 0))
			{
				hqlQuery.setLong("questionTypeId", questionTypeId);
			}
			if((difficultyLevelId != null) && (difficultyLevelId > 0))
			{
				hqlQuery.setLong("difficultyLevelId", difficultyLevelId);
			}
			if (qtext != null)
			{
				hqlQuery.setString("qtext",'%'+ qtext + '%');
			}

			qbQuestions = hqlQuery.list() ;

			if(qbQuestions.size()>0)
			{
				logger.info("Returned questions ");
			}
			else if(qbQuestions.size() == 0)
			{
				logger.warn("Warning :: No question ");
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
		return qbQuestions;
	}
	
	/**
	 * Gets list of qb questions and answer choices.
	 *
	 * @param qbQuestionId the qb question id
	 * @param statusId the status id
	 * @return the qb questions
	 * @throws AViewException
	 */
	@SuppressWarnings("unchecked")
	public static List<QbQuestion> getQbQuestions (List<Long> qbQuestionId,Integer statusId) throws AViewException 
	{
		Session session = null;
		List<QbQuestion> qbQuestion = null;
		try {
			session = HibernateUtils.getHibernateConnection();

			String hqlQueryString = "select distinct q from QbQuestion q left join fetch q.qbAnswerChoices as ans"
					+ " where ans.statusId = :statusId "
					+ " and q.statusId = :statusId "
					+ " and q.qbQuestionId IN (:qbQuestionId)"
					+ " ORDER BY ans.displaySequence";
			Query hqlQuery = session.createQuery(hqlQueryString);
			hqlQuery.setParameterList("qbQuestionId", qbQuestionId);
			hqlQuery.setInteger("statusId", statusId);
			qbQuestion = hqlQuery.list();			
			if(qbQuestion.size() > 0)
			{
				logger.info("Returned questions ");
			}
			else if(qbQuestion.size() == 0)
			{
				logger.warn("Warning :: No question ");
			}
		} catch (HibernateException he) {
			processException(he);
		} finally {
			HibernateUtils.closeConnection(session);
		}

		return qbQuestion;
	}	
	
	/**
	 * Check if question exists in quiz question paper.
	 *
	 * @param qbQuestionId the qb question id
	 * @return the boolean
	 * @throws AViewException
	 */
	public static Boolean checkIfQbQuestionExistsInQuizQP(Long qbQuestionId) throws AViewException
	{
		Session session = null ;
		Boolean qbQuestionFlag = false ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			String hqlQueryString = "SELECT qbq.statusId " +
													"FROM QbQuestion qbq , QuestionPaperQuestion qpq " +
													"WHERE qbq.qbQuestionId = qpq.qbQuestionId " +
													"AND qbq.qbQuestionId = :qbQuestionId " +
													" AND qbq.statusId = "+StatusHelper.getActiveStatusId()+" " +
													"GROUP BY qbq.qbQuestionId" +
													" HAVING qbq.statusId = "+StatusHelper.getActiveStatusId()+" " ; 

			Query hqlQuery = session.createQuery(hqlQueryString);
			hqlQuery.setLong("qbQuestionId", qbQuestionId) ;
			List<QbQuestion> qbqList = hqlQuery.list() ;
			
			// set the qbQuestion flag to true , if question exists in quiz question paper
			if(qbqList.size() > 0)
			{
				qbQuestionFlag = true ;
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
		
		return qbQuestionFlag ;
	}
}