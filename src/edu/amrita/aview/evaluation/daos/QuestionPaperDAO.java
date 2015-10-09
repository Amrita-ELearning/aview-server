/*
 * @(#)QuestionPaperDAO.java 4.0 2013/09/13
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
import edu.amrita.aview.common.utils.RandomUtils;
import edu.amrita.aview.evaluation.QuizConstant;
import edu.amrita.aview.evaluation.entities.QbQuestion;
import edu.amrita.aview.evaluation.entities.QuestionPaper;
import edu.amrita.aview.evaluation.entities.QuestionPaperQuestion;
import edu.amrita.aview.evaluation.vo.QuestionPaperQuestionListVO;

/**
 * This class consists of queries related to question paper.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QuestionPaperDAO extends SuperDAO 
{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(QuestionPaperDAO.class);

	/**
	 * Creates the question paper.
	 *
	 * @param questionPaper the question paper
	 * @throws AViewException
	 */
	public static void createQuestionPaper(QuestionPaper questionPaper) throws AViewException
	{
		Session session = null ;		
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			session.beginTransaction() ;			
			session.save(questionPaper);
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
	 * Update question paper.
	 *
	 * @param questionPaper the question paper
	 * @throws AViewException
	 */
	public static void updateQuestionPaper(QuestionPaper questionPaper) throws AViewException
	{
		Session session = null ;
		try{
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction() ;
			session.update(questionPaper);
			session.getTransaction().commit() ;

		}catch(HibernateException he){
			processException(he) ;
			session.getTransaction().rollback() ;
		}finally{
			HibernateUtils.closeConnection(session );
		}
	}

	/**
	 * Gets the all active question papers.
	 *
	 * @param statusId the status id
	 * @return the all active question papers
	 * @throws AViewException
	 */
	@SuppressWarnings("unchecked")
	public static List<QuestionPaper> getAllActiveQuestionPapers(Integer statusId) throws AViewException
	{
		Session session = null ;
		List<QuestionPaper> questionPapers = null ;
		try
		{			
			session = HibernateUtils.getHibernateConnection();
			Query hqlQuery = session.createQuery("select qp from QuestionPaper qp where statusId = :statusId");
			hqlQuery.setInteger("statusId", statusId);
			questionPapers = hqlQuery.list() ;
			if(questionPapers.size()>0)
			{
				logger.info("Returned question papers ");
			}
			else if(questionPapers.size() == 0)
			{
				logger.warn("Warning :: No question paper ");
			}
		}
		catch(HibernateException he)
		{
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session);
		}

		return questionPapers ;
	}

	/**
	 * Gets the question paper id.
	 *
	 * @param questionPaperId the question paper id
	 * @return the question paper id
	 * @throws AViewException
	 */
	public static QuestionPaper getQuestionPaperId(Long questionPaperId) throws AViewException
	{
		Session session = null;
		QuestionPaper questionpaper = null;
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			questionpaper = (QuestionPaper)session.get(QuestionPaper.class, questionPaperId);
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


	//method added by Radha
	/**
	 * Gets the all active question papers for user.
	 *
	 * @param userId the user id
	 * @param isComplete the is complete
	 * @param statusId the status id
	 * @return the all active question papers for user
	 * @throws AViewException
	 */
	@SuppressWarnings("unchecked")
	public static List<QuestionPaper> getAllActiveQuestionPapersForUser(Long userId, String isComplete ,Integer statusId) throws AViewException
	{
		Session session = null ;
		List<QuestionPaper> questionPapers = new ArrayList<QuestionPaper>() ;
		//Long qbSubcategoryId = QbSubcategoryHelper.getQbSubcategoryForName(Constant.POLLING_QUESTION_TYPE).getQbSubcategoryId() ;
		try
		{
			// Query changed for getting the question paper questions based on the
			// created user
			session = HibernateUtils.getHibernateConnection();
			String hql = "SELECT qp," +
					" (SELECT u.userName FROM User u WHERE qp.createdByUserId=u.userId) AS createdByUserName," +
					" (SELECT u.userName FROM User u WHERE qp.modifiedByUserId=u.userId) AS modifiedByUserName," +
					" (SELECT COUNT(qpq.questionPaper.questionPaperId)FROM QuestionPaperQuestion qpq , QbSubcategory subcat, QbCategory cat" +
					" WHERE qpq.questionPaper.questionPaperId = qp.questionPaperId AND qpq.statusId = :statusId" +
					" AND subcat.qbSubcategoryId = qpq.qbSubcategoryId" +					
					" AND subcat.createdByUserId = :userId and subcat.statusId = :statusId" +
					" AND subcat.qbCategoryId = cat.qbCategoryId AND cat.createdByUserId = :userId and cat.statusId = :statusId " +					
					" GROUP BY qpq.questionPaper.questionPaperId) AS totalQns" +
					" FROM QuestionPaper qp" +
					" WHERE qp.createdByUserId = :userId AND qp.statusId = :statusId " +
					" AND qp.questionPaperName NOT LIKE '"+QuizConstant.POLLING_QUESTION_TYPE+"%' " ;
			if(isComplete != null)
			{
				hql += " AND qp.isComplete = '"+isComplete+"' " ;
			}
			Query hqlQuery = session.createQuery(hql);
			hqlQuery.setLong("userId", userId);
			hqlQuery.setInteger("statusId", statusId);		

			List<Object[]> temp = hqlQuery.list() ;

			for(Object[] obj:temp)
			{
				QuestionPaper qp =  (QuestionPaper) obj[0] ;
				qp.setCreatedByUserName(obj[1].toString()) ;
				qp.setModifiedByUserName(obj[2].toString()) ;
				if(obj[3] != null)
				{
					qp.setTotalQns(Integer.parseInt(obj[3].toString())) ;
				}
				questionPapers.add(qp) ;
			}		

		}
		catch(HibernateException he)
		{
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session);
		}

		return questionPapers ;
	}
		
	//changed the validation function for random quiz questions	
	/**
	 * Gets the completed question papers.
	 *
	 * @param questionPaperId the question paper id
	 * @param statusId the status id
	 * @return the list of completed question papers
	 * @throws AViewException
	 */
	public static List getQuestionPaperComplete(Long questionPaperId,Integer statusId) throws AViewException
	{
		Session session = null ;
		List questionPaperComplete = new ArrayList() ;
		try
		{
			QuestionPaper questionPaper=getQuestionPaperId(questionPaperId);
			questionPaperComplete.add(questionPaper);
		}
		catch(HibernateException he){
			processException(he) ;
		}finally{
			HibernateUtils.closeConnection(session);
		}		
		return  questionPaperComplete ;
	}
	
	/**
	 * Question paper preview.
	 *
	 * @param questionPaperId the question paper id
	 * @param statusId the status id
	 * @return the list of question paper questions
	 * @throws AViewException
	 */
	public static List questionPaperPreview(Long questionPaperId,Integer statusId) throws AViewException
	{
		Session session = null ;
		List questionPaperComplete =null;
		
		try
		{
			session = HibernateUtils.getHibernateConnection();
			Query hqlQuery = session.createQuery("select qp from QuestionPaper qp where questionPaperId = :questionPaperId");
			hqlQuery.setLong("questionPaperId", questionPaperId);
			List<QuestionPaper> questionPaper = hqlQuery.list();
			questionPaperComplete = new ArrayList();
			questionPaperComplete.add(questionPaper.get(0));
			
			// Get question bank questions for the corresponding
			// question paper questions , which are specific type
			hqlQuery = session.createQuery("SELECT qbq FROM QbQuestion qbq, QuestionPaperQuestion qpq" +
											" WHERE qbq.qbQuestionId = qpq.qbQuestionId" +
											" AND qpq.patternType =:patternType " +
											" AND qpq.questionPaper.questionPaperId = :questionPaperId");
			hqlQuery.setString("patternType", QuizConstant.SPECIFIC_PATTERN_TYPE);
			hqlQuery.setLong("questionPaperId", questionPaperId);
			List<QbQuestion> questions = hqlQuery.list();	
			questionPaperComplete.add(questions) ;
			
			// Get question paper question for random pattern type
			// Since the actual questions are generated runtime
			hqlQuery = session.createQuery("select qpq from QuestionPaperQuestion qpq where " +
					   " questionPaper.questionPaperId = :questionPaperId AND qpq.patternType =:patternType ");
			hqlQuery.setString("patternType", QuizConstant.RANDOM_PATTERN_TYPE);
			hqlQuery.setLong("questionPaperId", questionPaperId);
			List<QuestionPaper> questionPaperQuestion = hqlQuery.list();
			if(questionPaperQuestion.size()!=0)
			{
				questionPaperComplete.add(questionPaperQuestion.get(0));
			}
		}
		catch(HibernateException he){
			processException(he) ;
		}finally{
			HibernateUtils.closeConnection(session);
		}		
		return questionPaperComplete ;
	}

	/**
	 * Gets the specific questions for question paper.
	 *
	 * @param questionPaperId the question paper id
	 * @param statusId the status id
	 * @return the list of specific questions for question paper
	 * @throws AViewException
	 */
	public static List<QuestionPaperQuestionListVO> getSpecificQuestionsForQP(Long questionPaperId , Integer statusId) throws AViewException
	{
		Session session = null;
		List<QuestionPaperQuestionListVO> specificQuestionsForQP = new ArrayList<QuestionPaperQuestionListVO>();
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			String sqlQuery = "SELECT qbq, qpq FROM QbCategory qbc, QbSubcategory qbsc, " + 
							  "QuestionPaperQuestion qpq, QbDifficultyLevel qbd, QbQuestionType qbt, " +
							  "QbQuestion qbq, QuestionPaper qp " +
							  // Check for all status id's to be active
							  "WHERE qbc.statusId = :statusId AND qbd.statusId = :statusId AND " +
							  "qbq.statusId = :statusId AND qbt.statusId = :statusId AND qbsc.statusId = :statusId AND " +
							  "qpq.statusId = :statusId AND qp.statusId = :statusId AND " +
							  // Compare subcategory in question bank question table
							  // and subcategory table
							  "qbq.qbSubcategoryId = qbsc.qbSubcategoryId AND " +
							  
							  // Compare category in category table and subcategory table
							  "qbsc.qbCategoryId = qbc.qbCategoryId AND " +
							  
							  // Compare question type , difficulty level
							  // with question bank question table
							  "qbq.qbQuestionTypeId = qbt.qbQuestionTypeId AND " +
							  "qbq.qbDifficultyLevelId = qbd.qbDifficultyLevelId AND " +
							  
							  // Compare the question in question paper question table
							  // and question bank question table
							  "qpq.qbQuestionId = qbq.qbQuestionId AND " +
							  
							  // Compare question paper in question paper table
							  // and question paper question table
							  "qp.questionPaperId = qpq.questionPaper.questionPaperId AND " +
							  "qp.questionPaperId = :questionPaperId ";								  

			Query hqlQuery = session.createQuery(sqlQuery);
			hqlQuery.setLong("questionPaperId", questionPaperId);
			hqlQuery.setInteger("statusId", statusId);
			List<Object[]> temp = hqlQuery.list();		
			if(temp.size()>0)
			{
				logger.info("Returned question papers ");			
			}
			else if(temp.size() == 0)
			{
				logger.warn("Warning :: No question paper ");
			}
			QuestionPaperQuestionListVO qpqlvo = null;
			// Populate the result to QuestionPaperQuestionListVO
			for(Object[] objA:temp)
			{
				qpqlvo = new QuestionPaperQuestionListVO();
				qpqlvo.qbQuestion= (QbQuestion) objA[0];
				qpqlvo.questionPaperQuestion=(QuestionPaperQuestion) objA[1];
				specificQuestionsForQP.add(qpqlvo);			
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
		return specificQuestionsForQP;
	}
	
	/**
	 * Gets the random questions for question paper.
	 *
	 * @param questionPaperId the question paper id
	 * @param statusId the status id
	 * @return the list of random questions for question paper
	 * @throws AViewException
	 */
	public static List<QuestionPaperQuestion> getRandomQuestionsForQP(Long questionPaperId , Integer statusId) throws AViewException
	{
		Session session = null;
		List<QuestionPaperQuestion> randomQuestions = new ArrayList<QuestionPaperQuestion>();		
		try
		{
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "SELECT qpq FROM QuestionPaperQuestion qpq " +
									"WHERE qpq.patternType = '"+QuizConstant.RANDOM_PATTERN_TYPE+"' AND qpq.statusId = :statusId " +
									"AND qpq.questionPaper.questionPaperId = :questionPaperId";			
			Query hqlQuery1 = session.createQuery(hqlQueryString);
			hqlQuery1.setLong("questionPaperId", questionPaperId);
			hqlQuery1.setInteger("statusId", statusId) ;
			randomQuestions = hqlQuery1.list();			
		}
		catch (HibernateException he) 
		{
			processException(he);
		}
		finally 
		{
			HibernateUtils.closeConnection(session);
		}
		return randomQuestions;
	}
		
	/**
	 * Gets the qP for quiz question.
	 *
	 * @param questionPaperId the question paper id
	 * @param statusId the status id
	 * @return the qP for quiz question
	 * @throws AViewException
	 */
	public static List<QuestionPaperQuestionListVO> getQPForQuizQuestion(Long questionPaperId , Integer statusId) throws AViewException
	{
		List<QuestionPaperQuestionListVO> questionPapers = getSpecificQuestionsForQP(questionPaperId, statusId);
		List<QuestionPaperQuestion> randomQuestionsForQP = getRandomQuestionsForQP(questionPaperId, statusId);
		if(randomQuestionsForQP.size() > 0)
		{
			prepareRandomQuestions(questionPapers, randomQuestionsForQP);
		}		
		return questionPapers;
	}
	
	//Added new function for Validation random quiz questions
	/**
	 * Random question validation.
	 *
	 * @param specificQuestions the question papers
	 * @param randomQuestions the random questions
	 * @throws AViewException
	 */
	public static void prepareRandomQuestions(List<QuestionPaperQuestionListVO> specificQuestions, 
												List<QuestionPaperQuestion> randomQuestions) throws AViewException 
	{
		List<QuestionPaperQuestionListVO> availableQuestionList = new ArrayList<QuestionPaperQuestionListVO>();
		List<QuestionPaperQuestionListVO> availableQuestionList1 = new ArrayList<QuestionPaperQuestionListVO>();
		Session session = null ;
		Long samplesNeeded = 0l;
		double randomQuestionMark = 0.0;
		session = HibernateUtils.getHibernateConnection();
		// To refrain from adding questions already added  
		for( int k = 0 ; k < randomQuestions.size() ; k++ )
		{			
			QuestionPaperQuestion temp = (QuestionPaperQuestion)randomQuestions.get(k);
			samplesNeeded=temp.getNumRandomQuestions();
			randomQuestionMark=temp.getMarks();
			
			String sqlQuery = "SELECT qbq, qpq FROM QbCategory qbc, QbSubcategory qbsc, " + 
							  "QuestionPaperQuestion qpq, QbDifficultyLevel qbd, QbQuestionType qbt, " +
							  "QbQuestion qbq, QuestionPaper qp " +
							  // Check the status of data in all tables as active
							  "WHERE qbc.statusId = :statusId AND qbd.statusId = :statusId AND " +
							  "qbq.statusId = :statusId AND qbt.statusId = :statusId AND qbsc.statusId = :statusId AND " +
							  "qpq.statusId = :statusId AND qp.statusId = :statusId AND " +
							  // Compare subcategory ,category , question type
							  // and difficulty level
							  "qbq.qbSubcategoryId = qbsc.qbSubcategoryId AND " +
							  "qbsc.qbCategoryId = qbc.qbCategoryId AND " +
							  "qbq.qbQuestionTypeId = qbt.qbQuestionTypeId AND " +
							  "qbq.qbDifficultyLevelId = qbd.qbDifficultyLevelId AND " +
							  // Compare question paper in question paper table
							  // and question paper question table
							  "qp.questionPaperId = qpq.questionPaper.questionPaperId AND " +
							  "qp.questionPaperId = :questionPaperId AND " +
							  // Check the subcategory , since random questions
							  // are generated at runtime
							  "qbsc.qbSubcategoryId = :qbSubcategoryId AND " + 
							  "qpq.patternType = '"+QuizConstant.RANDOM_PATTERN_TYPE+"' "; 
			
			if(temp.getQbDifficultyLevelId() != null)
			{
				sqlQuery += " AND qbd.qbDifficultyLevelId = :qbDifficultyLevelId ";
			}
			if(temp.getQbQuestionTypeId() != null)
			{
				sqlQuery += " AND qbt.qbQuestionTypeId = :qbQuestionTypeId ";
			}
			sqlQuery += " GROUP BY(qbq.qbQuestionId)";
			
			Query hqlQuery = session.createQuery(sqlQuery);
			
			hqlQuery.setLong("questionPaperId", temp.getQuestionPaper().getQuestionPaperId());
			hqlQuery.setInteger("statusId", StatusHelper.getActiveStatusId());
			hqlQuery.setLong("qbSubcategoryId", temp.getQbSubcategoryId());
			
			if(temp.getQbDifficultyLevelId() != null)
			{
				hqlQuery.setLong("qbDifficultyLevelId", temp.getQbDifficultyLevelId());
			}
			if(temp.getQbQuestionTypeId() != null)
			{
				hqlQuery.setLong("qbQuestionTypeId", temp.getQbQuestionTypeId());
			}
			
			
			List<Object[]> tmpList = hqlQuery.list();
			if(availableQuestionList.size()>0)
			{
				logger.info("Returned question paper ");
			}
			else if(availableQuestionList.size() == 0)
			{
				logger.warn("Warning :: No question papers ");
			}
			
			QuestionPaperQuestionListVO qpqlvo = null;
			
			for(Object[] obj : tmpList)
			{
				qpqlvo = new QuestionPaperQuestionListVO();
				qpqlvo.qbQuestion= (QbQuestion) obj[0];
				qpqlvo.questionPaperQuestion=(QuestionPaperQuestion) obj[1];
				availableQuestionList.add(qpqlvo);
			}
			
			for(QuestionPaperQuestionListVO randomQuestion : availableQuestionList)
			{
				if((specificQuestions != null) && (specificQuestions.contains(randomQuestion)))
				{
					
				}
				else
				{
					availableQuestionList1.add(randomQuestion);					
				}
			}
			randomQuestionMark = randomQuestionMark/samplesNeeded;	    
			randomQuestionsGeneration(availableQuestionList1, samplesNeeded, specificQuestions, randomQuestionMark);
			availableQuestionList.clear();
			availableQuestionList1.clear();			
		}		
	}
	//Added new function for generating random questions
	/**
	 * Generate random questions.
	 *
	 * @param availableQuestionList the available question list
	 * @param sampleQuestionsNeeded the sample questions needed
	 * @param questionPapers the question papers
	 * @param randomQuestionMark the random question mark
	 */
	private static void randomQuestionsGeneration(List<QuestionPaperQuestionListVO> availableQuestionList,
							Long sampleQuestionsNeeded, List<QuestionPaperQuestionListVO> questionPapers, double randomQuestionMark) 
	{
		
		List<Integer> randomQuestionIndices = RandomUtils.generateRandomNumbers(sampleQuestionsNeeded, availableQuestionList.size());
		for(Integer randomQuestionIndex : randomQuestionIndices)
		{
			QuestionPaperQuestionListVO questionPaperQuestionListVo = availableQuestionList.get(randomQuestionIndex);
			questionPaperQuestionListVo.questionPaperQuestion.setMarks(randomQuestionMark);
			questionPapers.add(questionPaperQuestionListVo);
		}
				
	}

	/**
	 * Save question paper.
	 *
	 * @param questionPaper the question paper
	 * @throws AViewException
	 */
	public static void saveQuestionPaper(QuestionPaper questionPaper) 
			throws AViewException
			{
		Session session = null;			
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();				
			session.saveOrUpdate(questionPaper);				
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
	 * Gets the question paper if questions exist.
	 *
	 * @return the question paper if questions exist
	 * @throws AViewException
	 */
	public static List<QuestionPaper> getQuestionPaperIfQuestionsExist() throws AViewException
	{
		List<QuestionPaper> qpList = null ;
		Session session = null ;
		Integer statusId = StatusHelper.getActiveStatusId() ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			String hqlQueryString = "select qp from QuestionPaper qp, QuestionPaperQuestion qpq" +
														" where qp.questionPaperId = qpq.questionPaper.questionPaperId" +
														" and qp.statusId = '"+statusId+"' " +
														" group by qp.questionPaperId" ;
			Query hqlQuery = session.createQuery(hqlQueryString) ;
			qpList = hqlQuery.list() ;
		}
		catch(HibernateException he){
			processException(he) ;
			HibernateUtils.closeConnection(session) ;
		}
		
		return qpList ;
	}
}

