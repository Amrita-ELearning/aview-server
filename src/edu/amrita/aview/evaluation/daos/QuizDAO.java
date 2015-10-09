/*
 * @(#)QuizDAO.java 4.0 2013/09/20
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

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.ibm.icu.text.DecimalFormat;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.evaluation.QuizConstant;
import edu.amrita.aview.evaluation.entities.Quiz;


/**
 * This class consists of queries related to quiz.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QuizDAO extends SuperDAO
{

	/** The logger. */
	private static Logger logger = Logger.getLogger(QuizDAO.class);

	/**
	 * Creates a quiz.
	 *
	 * @param aviewQuiz the quiz object
	 * @throws AViewException if the quiz creation fails due to any constraint violation 
	 */
	public static void createQuiz(Quiz aviewQuiz) throws AViewException
	{				
		Session session = null ;		
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			session.beginTransaction() ;
			session.save(aviewQuiz);	
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
	 * Update quiz.
	 *
	 * @param aviewQuiz the quiz object
	 * @throws AViewException if updation of a quiz fails due to constraint violation .
	 */
	public static void updateQuiz(Quiz aviewQuiz) throws AViewException
	{
		Session session = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			session.beginTransaction() ;
			session.saveOrUpdate(aviewQuiz);
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
	 * Gets the all active quizzes.
	 *
	 * @param statusId the status id
	 * @return the all active quizzes
	 * @throws AViewException if a named parameter is not set 
	 * 												 or referencing a variable in case of association mapping
	 * 												 or syntactical error in query
	 */
	public static List<Quiz> getAllActiveQuizzes(Integer statusId) throws AViewException
	{
		Session session = null ;
		List<Quiz> quizzes = null ;		
		try
		{
			session = HibernateUtils.getHibernateConnection() ;			
			String hqlQueryString = "select qz" +
					" from Quiz qz,QuestionPaper qp,Class cl,ClassRegistration clr,QuestionPaperQuestion qpq,Course co " +
					" where qz.statusId = :statusId " +
					" and qp.statusId = :statusId" +
					" and cl.statusId = :statusId " +
					" and qz.questionPaperId = qp.questionPaperId"+
					" and qz.classId = clr.aviewClass.classId" +
					" and clr.aviewClass.classId = cl.classId " +
					" and cl.courseId = co.courseId " +
					" and qpq.questionPaper.questionPaperId = qz.questionPaperId" +
					" GROUP BY qz.quizName";
			Query hqlQuery = session.createQuery(hqlQueryString);
			hqlQuery.setInteger("statusId", statusId);
			quizzes = hqlQuery.list() ;
			if(quizzes.size()>0)
			{
				logger.info("Returned quizzes ");
			}
			else if(quizzes.size() == 0)
			{
				logger.warn("Warning :: No quiz ");
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

	/**
	 * Gets the quiz by id.
	 *
	 * @param quizId the quiz id
	 * @return the quiz by id
	 * @throws AViewException if incorrect entity is specified
	 * 												 or incorrect / not existing quiz id specified
	 */
	public static Quiz getQuizById(Long quizId) throws AViewException
	{
		Session session = null ;
		Quiz aviewQuiz = null ;

		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			aviewQuiz = (Quiz)session.get(Quiz.class,quizId) ;						
		}
		catch(HibernateException he)
		{
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session);
		}

		return aviewQuiz ;
	}	

	/**
	 * Gets the quiz question answer.
	 *
	 * @param quizId the quiz id
	 * @return the quiz question answer
	 * @throws AViewException  for syntactical error in query
	 * 												 or if a named parameter is not set
	 * 												 or referencing a variable in case of association mapping
	 */	
	public static List getQuizQuestionAnswer(Long quizId) throws AViewException
	{
		Session session = null ;
		List quizQuestionAnswers = null ;

		try
		{
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "SELECT quest, GROUP_CONCAT(answer.choiceText,'--',answer.fraction,'&&',answer.quizAnswerChoiceId)," +
					" quiz.quizName,quiz.quizId" +
					" FROM QuizQuestion quest,Quiz quiz,QbSubcategory qbsub,QuizAnswerChoice answer" +
					" WHERE qbsub.qbSubcategoryId = quest.subcategoryId" +
					" AND quest.quizQuestionId = answer.quizQuestion.quizQuestionId" +
					" AND quiz.quizId = quest.quiz.quizId" + 
					" AND quiz.quizId = :quizId" +
					" GROUP BY quest.qbQuestionId ";

			Query hqlQuery = session.createQuery(hqlQueryString) ;
			hqlQuery.setLong("quizId", quizId);
			quizQuestionAnswers = hqlQuery.list() ;
			if(quizQuestionAnswers.size()>0)
			{
				logger.info("Returned quizzes ");
			}
			else if(quizQuestionAnswers.size() == 0)
			{
				logger.warn("Warning :: No quiz ");
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

		return quizQuestionAnswers ;
	}	

	//Fix for Bug #11534 : Added argument 'isModerator'
	/**
	 * Get the quiz results for a student.
	 *
	 * @param quizId id of a quiz
	 * @param userId id of a user
	 * @param userRole role of a user
	 * @param isModerator boolean value whether the user is a moderator or not
	 * @return list of quiz results
	 * @throws AViewException  for syntactical error in query
	 * or if a named parameter is not set
	 * or referencing a variable in case of association mapping
	 */
	public static List<Quiz> getQuizResultForStudent(Long quizId,Long userId,String userRole, Boolean isModerator) throws AViewException
	{
		Session session = null ;
		List<Quiz> quizzes = new ArrayList<Quiz>();  ;

		try
		{		
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "SELECT u.userName, COUNT(*), qr.totalScore, qz.totalMarks, " +
									"SUM(CASE WHEN (qqr.score > 0) THEN 1 ELSE 0 END), " +
									"qz.quizId, (SELECT COUNT(quizQuestionId) FROM QuizQuestion qq WHERE qq.quiz.quizId = :quizId), u.userId " +
									"FROM " +
									"QuizResponse qr, QuizQuestionResponse qqr, User u, Quiz qz " +
									"WHERE " +
									"qz.quizId = qr.quizId " +
									"AND qr.quizResponseId = qqr.quizResponse.quizResponseId " +
									"AND qr.createdByUserId = u.userId " +
									"AND qz.quizId = :quizId ";
			//Fix for Bug #11534
			if(!isModerator)
			{
				hqlQueryString += " AND u.userId = :userId";
			}
			hqlQueryString += " GROUP BY qr.quizId,u.userId ORDER BY qr.quizId,u.userId ";
			
			Query hqlQuery = session.createQuery(hqlQueryString) ;
			hqlQuery.setLong("quizId", quizId);
			//Fix for Bug #11534
			if(!isModerator)
			{
				hqlQuery.setLong("userId", userId);
			}
			List<Object[]> temp = hqlQuery.list();
			for(Object[] objA:temp)
			{
				Quiz qz=new Quiz();	
				qz.setUserName((String) objA[0]);
				//Count of attempted questions by the student
				qz.setAttemptedQuestions(Integer.parseInt(objA[1].toString()));
				//Score obtained by the student for the given quiz
				qz.setScore((Float)objA[2]);
				//Max mark for the quiz
				qz.setTotalScore((Double)objA[3] );
				//No of correct answer count from quiz question response
				qz.setFraction((Long)objA[4]);
				qz.setQuizId((Long) objA[5]);
				//Total questions for the given quiz
				qz.setCountQuizQuestionId((Long)objA[6]) ;
				double p = ( (qz.getScore()/qz.getTotalScore()) * 100 );
				// Parse the percentage to 2 digit number including 2 digit rounding i.e 50.46 etc
				DecimalFormat dec = new DecimalFormat("##.##");
				qz.setPercentage(Double.parseDouble(dec.format(p))) ;
				qz.setUserId((Long)objA[7]) ;
				quizzes.add(qz);
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

		return quizzes;
	}

	/**
	 * Gets the quiz question result.
	 *
	 * @param quizId the quiz id
	 * @return the quiz question result
	 * @throws AViewException for syntactical error in query
	 * 												 or if a named parameter is not set
	 * 												 or referencing a variable in case of association mapping
	 */
	public static List<Quiz> getQuizResultByQuestion(Long quizId) throws AViewException
	{
		Session session = null ;
		List<Quiz> quizQuestionResultList = new ArrayList<Quiz>();         
		try
		{
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "SELECT COUNT(qqcr.quizAnswerChoiceId)," +
					" qac.choiceText,qqcr.quizAnswerChoiceId ,qqr.quizQuestionId,qr.quizId" +
					" FROM QuizAnswerChoice qac,QuizQuestionChoiceResponse qqcr,QuizQuestionResponse qqr,QuizResponse qr " +
					" WHERE qqcr.quizQuestionResponse.quizQuestionResponseId = qqr.quizQuestionResponseId " +
					" AND qqr.quizResponse.quizResponseId = qr.quizResponseId AND qqcr.quizAnswerChoiceId=qac.quizAnswerChoiceId " +
					" AND qr.quizId = :quizId " +
					" GROUP BY qqr.quizQuestionId,qqcr.quizAnswerChoiceId" ;
			Query hqlQuery = session.createQuery(hqlQueryString) ;
			hqlQuery.setLong("quizId", quizId);			
			List<Object[]> temp = hqlQuery.list() ;
			for(Object[] objA:temp)
			{
				Quiz qz=new Quiz();
				qz.setAnswerChoiceCount((Long) objA[0]);
				qz.setChoiceText((String) objA[1]);
				qz.setQuizAnswerChoiceId((Long)objA[2]);
				qz.setQuizQuestionId((Long)objA[3]);
				qz.setQuizId((Long)objA[4]);
				quizQuestionResultList.add(qz);
			}
			if(quizQuestionResultList.size()>0)
			{
				logger.info("Returned quizzes ");
			}
			else if(quizQuestionResultList.size() == 0)
			{
				logger.warn("Warning :: No quiz ");
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

		return quizQuestionResultList ;
	}

	/**
	 * Gets the all active quizzes for a user.
	 *
	 * @param userId the user id
	 * @param statusId the status id
	 * @return the all active quizzes for user
	 * @throws AViewException for syntactical error in query
	 * 												 or if a named parameter is not set	  												 
	 */
	public static List<Quiz> getAllActiveQuizzesForUser(Long userId, Integer statusId) throws AViewException
	{
		Session session = null ;
		List<Quiz> quizzes = new ArrayList<Quiz>() ;

		try{
			session = HibernateUtils.getHibernateConnection() ;	

			//Modified query common for both student and teacher			
			String sqlQueryString = "SELECT  DISTINCT qz.quiz_id, qz.quiz_name, qz.class_id, cl.course_id ,qz.question_paper_id, " +
					"qz.total_marks, qz.time_open, qz.time_close, qz.duration_seconds, " + 
					"cast(qz.quiz_status AS char), cast(qz.quiz_type as char), qz.created_by_user_id, qz.created_date, " + 
					"qz.modified_by_user_id, qz.modified_date, qz.status_id," +
					"qp.question_paper_name," +
					" cl.class_name,c.course_name , qp.status_id,qz.institute_id " +
					"FROM quiz qz LEFT JOIN quiz_response qr ON " +
					"qr.quiz_id = qz.quiz_id,question_paper qp, class cl ,course c " +
					"WHERE qp.question_paper_id = qz.question_paper_id " +
					"AND qz.class_id = cl.class_id " +
					"and cl.course_id = c.course_id " +
					"AND qz.status_id = :statusId " +				
					"AND ((qz.created_by_user_id = :userId) OR (qr.user_id = :userId))" +
					" AND qz.quiz_name NOT LIKE '"+QuizConstant.POLLING_QUESTION_TYPE+"%' ";
			SQLQuery hqlQuery = session.createSQLQuery(sqlQueryString);			
			hqlQuery.setInteger("statusId", statusId);
			hqlQuery.setLong("userId", userId);		
			List<Object[]> temp = hqlQuery.list();
			String qpName = null ;			
			// To display the quiz that have been deleted , we search for the following 
			// pattern and replace it with an empty string .
			String appenderString = "_DELETED.*" ; 
			for(Object[] objA:temp)
			{
				Quiz qz=new Quiz();
				qz.setQuizId(Long.parseLong(objA[0].toString()));
				qz.setQuizName((String) objA[1]);
				qz.setClassId(Long.parseLong(objA[2].toString()));
				qz.setCourseId(Long.parseLong(objA[3].toString()));
				qz.setQuestionPaperId(Long.parseLong(objA[4].toString()));
				qz.setTotalMarks(Float.parseFloat(objA[5].toString()));
				qz.setTimeOpen((Timestamp) objA[6]);
				qz.setTimeClose((Timestamp) objA[7]);
				qz.setDurationSeconds(Long.parseLong(objA[8].toString()));
				qz.setQuizStatus((String) objA[9]);
				qz.setQuizType((String) objA[10]);
				qz.setCreatedByUserId(Long.parseLong(objA[11].toString()));
				qz.setCreatedDate((Timestamp) objA[12]);
				qz.setModifiedByUserId(Long.parseLong(objA[13].toString())); 
				qz.setModifiedDate((Timestamp)objA[14]);
				qz.setStatusId((Integer) objA[15]) ;		
				qpName = (String) objA[16] ;						
				qz.setQuestionPaperName(qpName.replaceAll(appenderString, "")) ;				
				qz.setClassName((String) objA[17]);
				qz.setCourseName((String) objA[18]);    
				qz.setInstituteId(Long.parseLong(objA[20].toString()));    
				quizzes.add(qz);
			}
			if(quizzes.size()>0)
			{
				logger.info("Returned quizzes ");
			}
			else if(quizzes.size() == 0)
			{
				logger.warn("Warning :: No quiz ");
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

	/**
	 * Gets the all active quizzes for student.
	 *
	 * @param userId the user id
	 * @param statusId the status id
	 * @return the all active quizzes for student
	 * @throws AViewException for syntactical error in query
	 * 												 or if a named parameter is not set .
	 */
	public static   List<Quiz> getAllActiveQuizzesForStudent(Long userId,Integer statusId) throws AViewException
	{
		Session session;
		List<Quiz> quiz = new ArrayList<Quiz>() ;
		session = null;

		try
		{
			session = HibernateUtils.getHibernateConnection();
			String sqlQueryString = "SELECT qr.quiz_id ,q.quiz_name, qp.question_paper_name, cl.class_name,  q.time_open," +
					" q.time_close, q.duration_seconds ,q.total_marks,cast(q.quiz_status AS char),co.course_name  FROM quiz_response qr, " +
					" quiz q, class cl, question_paper qp,course co  WHERE qr.quiz_id = q.quiz_id AND qr.user_id = :userId AND" +
					" q.status_id = :statusId  AND cl.class_id = q.class_id AND " +
					" qp.question_paper_id = q.question_paper_id AND" +
					" cl.course_id = co.course_id  and qp.status_id = :statusId " +
					" AND q.quiz_name NOT LIKE '"+QuizConstant.POLLING_QUESTION_TYPE+"%' ";
			SQLQuery hqlQuery = session.createSQLQuery(sqlQueryString);
			hqlQuery.setLong("userId", userId);
			hqlQuery.setInteger("statusId", statusId);
			List<Object[]> temp = hqlQuery.list();
			for(Object[] objA:temp)
			{
				Quiz qz=new Quiz();
				qz.setQuizId(Long.parseLong(objA[0].toString()));
				qz.setQuizName((String) objA[1]);
				qz.setQuestionPaperName((String) objA[2]);
				qz.setClassName((String) objA[3]);
				qz.setTimeOpen((Timestamp) objA[4]);
				qz.setTimeClose((Timestamp) objA[5]);
				qz.setDurationSeconds(Long.parseLong(objA[6].toString()));
				qz.setTotalMarks(Float.parseFloat(objA[7].toString()));
				qz.setQuizStatus((String) objA[8].toString());
				qz.setCourseName((String)objA[9].toString()) ;
				quiz.add(qz);
			}
			if(quiz.size()>0)
			{
				logger.info("Returned quizzes ");
			}
			else if(quiz.size() == 0)
			{
				logger.warn("Warning :: No quiz ");
			}
		}
		catch(HibernateException he)
		{
			processException(he);
		}

		return quiz;

	}

	/**
	 * Gets the quiz question paper result.
	 *
	 * @param quizId the quiz id
	 * @return the quiz for question paper result display 
	 * @throws AViewException for syntactical error in query
	 * 												 or if a named parameter is not set
	 */
	public static List<Quiz> getQuizResultByQuestionPaper(Long quizId) throws AViewException
	{
		Session session = null ;
		List<Quiz> QuestionPaperResult = new ArrayList<Quiz>();  ;

		try
		{		
			session = HibernateUtils.getHibernateConnection();			
			String sqlQueryString = "SELECT q.quiz_id,q.quiz_name,quest.question_paper_name,cl.class_name,cr.course_name," +
					" COUNT(DISTINCT qq.quiz_question_id),COUNT(DISTINCT creg.user_id),COUNT(DISTINCT qr.quiz_response_id)" +
					"  FROM quiz q,class cl,course cr,class_register creg,question_paper quest ,quiz_question qq," +
					"  quiz_response qr,user u  WHERE cl.class_id=q.class_id AND cr.course_id=cl.course_id AND creg.class_id=cl.class_id " +
					" AND creg.user_id=u.user_id AND creg.is_moderator='"+QuizConstant.NO+"' AND qr.quiz_id=:quizId AND qq.quiz_id=:quizId AND" +
					"  quest.question_paper_id=q.question_paper_id AND q.quiz_id=:quizId GROUP BY cl.class_id";	
			SQLQuery hqlQuery = session.createSQLQuery(sqlQueryString);
			hqlQuery.setLong("quizId", quizId);
			QuestionPaperResult = hqlQuery.list() ;		          				
		}
		catch(HibernateException he)
		{
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session) ;
		}

		return QuestionPaperResult ;
	}

	/**
	 * Gets the question paper result for chart display.
	 *
	 * @param quizId the quiz id
	 * @return the question paper result for chart
	 * @throws AViewException for syntactical error in query
	 * 												 or if a named parameter is not set	
	 */
	public static List<Quiz> getQuestionPaperResultForChart(Long quizId) throws AViewException
	{
		Session session = null ;
		List<Quiz> questionPaperResultList = new ArrayList<Quiz>();  ;

		try
		{		
			session = HibernateUtils.getHibernateConnection();			
			String sqlQueryString = "SELECT qr.user_id,qr.total_score,SUM(q.total_marks) FROM quiz_response qr,quiz q " +
					"WHERE qr.quiz_id=q.quiz_id AND qr.quiz_id=:quizId GROUP BY qr.user_id ";
			SQLQuery hqlQuery = session.createSQLQuery(sqlQueryString);
			hqlQuery.setLong("quizId", quizId);
			questionPaperResultList = hqlQuery.list();
		}
		catch(HibernateException he)
		{
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session) ;
		}

		return questionPaperResultList ;
	}

	/**
	 * Gets the all active quizzes off line for student.
	 *
	 * @param userId the user id
	 * @param quizStatusId the quiz status id
	 * @return the all active quizzes off line for student
	 * @throws AViewException for syntactical error in query
	 * 												 or if a named parameter is not set
	 */
	public static   List<Quiz> getAllActiveQuizzesOffLineForStudent(Long userId,Integer quizStatusId) throws AViewException
	{
		Session session;
		List<Quiz> quizList = new ArrayList<Quiz>() ;
		session = null;

		try
		{
			session = HibernateUtils.getHibernateConnection();
			String sqlQueryString = "SELECT  qz.quiz_id,qz.quiz_name, qp.question_paper_name, cl.class_name,qz.time_open,qz.time_close," +
					"   qz.duration_seconds, qz.total_marks,cast(qz.quiz_status  as CHAR),co.course_name, cast(qz.quiz_type as CHAR)," +
					"  (SELECT COUNT(quiz_question_id) FROM quiz_question qzq WHERE qzq.quiz_id = qz.quiz_id) FROM" +
					"   quiz qz, class cl ,class_register clr , course co,question_paper qp " +
					"  WHERE "+userId+" NOT IN (SELECT qr.user_id FROM quiz_response qr WHERE qz.quiz_id = qr.quiz_id)" +
					"  AND qz.status_id =:quizStatusId AND cl.class_id = qz.class_id AND qp.question_paper_id = qz.question_paper_id AND qz.quiz_type='Online'" +
					" AND cl.course_id = co.course_id AND NOW() BETWEEN qz.time_open AND qz.time_close GROUP BY qz.quiz_id";
			SQLQuery hqlQuery = session.createSQLQuery(sqlQueryString);
			hqlQuery.setInteger("quizStatusId", quizStatusId);
			List<Object[]> temp = hqlQuery.list();
			for(Object[] objA:temp)
			{
				Quiz qz=new Quiz();
				qz.setQuizId(Long.parseLong(objA[0].toString()));
				qz.setQuizName((String) objA[1]);
				qz.setQuestionPaperName((String) objA[2]);
				qz.setClassName((String) objA[3]);
				qz.setTimeOpen((Timestamp) objA[4]);
				qz.setTimeClose((Timestamp) objA[5]);
				qz.setDurationSeconds(Long.parseLong(objA[6].toString()));
				qz.setTotalMarks(Float.parseFloat(objA[7].toString()));
				qz.setQuizStatus((String) objA[8].toString());
				qz.setCourseName((String)objA[9].toString()) ;
				qz.setQuizType((String)objA[10].toString()) ;
				qz.setCountQuizQuestionId(Long.parseLong(objA[11].toString()));
				quizList.add(qz);
			}
			if(quizList.size()>0)
			{
				logger.info("Returned quizzes ");
			}
			else if(quizList.size() == 0)
			{
				logger.warn("Warning :: No quiz ");
			}
		}
		catch(HibernateException he)
		{
			processException(he);
		}

		return quizList;

	}

	/**
	 * Gets the list of quizzes on basis of location  .
	 *
	 * @param quizId the quiz id
	 * @return the location based result
	 * @throws AViewException for syntactical error in query
	 * 												 or if a named parameter is not set	
	 */
	public static List<Quiz> getQuizResultByLocation(Long quizId) throws AViewException
	{
		Session session = null ;
		List<Quiz> quizList = new ArrayList<Quiz>();  ;

		try
		{		
			session = HibernateUtils.getHibernateConnection();			
			String sqlQueryString =" SELECT qq.question_text ,qq.quiz_question_id,COUNT(DISTINCT creg.user_id)," +
					" COUNT(DISTINCT qr.quiz_response_id) ,qr.quiz_response_type" +
					" FROM quiz_response qr ,quiz_question_response qqr ,quiz_question qq ," +
					" class cl,course cr,class_register creg,quiz q ,`user` u WHERE" +
					" qr.quiz_response_id = qqr.quiz_response_id AND qq.quiz_question_id = qqr.quiz_question_id" +
					" AND cl.class_id=q.class_id AND cr.course_id=cl.course_id " +
					" AND creg.class_id=cl.class_id	AND creg.user_id=u.user_id " +
					" AND creg.is_moderator='"+QuizConstant.NO+"'	AND q.quiz_id = :quizId	AND q.quiz_id = qr.quiz_id" +
					" GROUP BY qr.quiz_response_type";
			SQLQuery hqlQuery = session.createSQLQuery(sqlQueryString);
			hqlQuery.setLong("quizId", quizId);
			quizList = hqlQuery.list();
		}
		catch(HibernateException he)
		{
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session) ;
		}

		return quizList ;
	}

	/**
	 * Gets the list of quizzes for category based result.
	 *
	 * @param quizId the quiz id
	 * @return the category based result
	 * @throws AViewException for syntactical error in query
	 * 												 or if a named parameter is not set
	 */
	public static List<Quiz> getCategoryBasedResult(Long quizId) throws AViewException
	{
		Session session = null ;
		List<Quiz> categoryBasedResult = new ArrayList<Quiz>();  ;

		try
		{		
			session = HibernateUtils.getHibernateConnection();			
			String sqlQueryString =" SELECT qq.question_text ,qq.quiz_question_id,qq.subcategory_name,qq.difficulty_level_name,COUNT(DISTINCT creg.user_id)," +
					" COUNT(DISTINCT qr.quiz_response_id) ,qr.quiz_response_type,qq.category_name" +
					" FROM quiz_response qr ,quiz_question_response qqr ,quiz_question qq ," +
					" class cl,course cr,class_register creg,quiz q ,`user` u WHERE" +
					" qr.quiz_response_id = qqr.quiz_response_id AND qq.quiz_question_id = qqr.quiz_question_id" +
					" AND cl.class_id=q.class_id AND cr.course_id=cl.course_id " +
					" AND creg.class_id=cl.class_id	AND creg.user_id=u.user_id " +
					" AND creg.is_moderator='"+QuizConstant.NO+"'	AND q.quiz_id = :quizId	AND q.quiz_id = qr.quiz_id" +
					" GROUP BY qr.quiz_response_type";
			SQLQuery hqlQuery = session.createSQLQuery(sqlQueryString);
			hqlQuery.setLong("quizId", quizId);
			categoryBasedResult = hqlQuery.list();
		}
		catch(HibernateException he)
		{
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session) ;
		}
		return categoryBasedResult;
	}

	/**
	 * Gets active quizzes for a question paper.
	 *
	 * @param userId id of a user
	 * @param questionPaperId id of a question paper
	 * @param statusId id of a status
	 * @return list of quiz
	 * @throws AViewException  for syntactical error in query
	 * or if a named parameter is not set
	 */
	public static List<Quiz> getAllActiveQuizzesForQuestionPaper(Long userId,Long questionPaperId, Integer statusId) throws AViewException
	{
		Session session = null ;
		List<Quiz> quizzes = new ArrayList<Quiz>() ;

		try{
			session = HibernateUtils.getHibernateConnection() ;	

			//Modified query common for both student and teacher			
			String sqlQueryString = "SELECT  DISTINCT qz.quiz_id, qz.quiz_name, qz.class_id, cl.course_id ,qz.question_paper_id, " +
					"qz.total_marks, qz.time_open, qz.time_close, qz.duration_seconds, " + 
					"cast(qz.quiz_status AS char), cast(qz.quiz_type as char), qz.created_by_user_id, qz.created_date, " + 
					"qz.modified_by_user_id, qz.modified_date, qz.status_id," +
					"qp.question_paper_name," +
					" cl.class_name,c.course_name " +
					"FROM quiz qz LEFT JOIN quiz_response qr ON " +
					"qr.quiz_id = qz.quiz_id,question_paper qp, class cl ,course c " +
					"WHERE qp.question_paper_id = qz.question_paper_id " +
					"AND qp.question_paper_id = :questionPaperId " +
					"AND qz.class_id = cl.class_id " +
					"and cl.course_id = c.course_id " +
					"AND qz.status_id = :statusId " +
					"AND qp.status_id= :statusId " +
					"AND ((qz.created_by_user_id = :userId) OR (qr.user_id = :userId))" +
					" AND qz.quiz_name NOT LIKE '"+QuizConstant.POLLING_QUESTION_TYPE+"%' " +
					"ORDER BY qz.time_open DESC";
			SQLQuery hqlQuery = session.createSQLQuery(sqlQueryString);			
			hqlQuery.setInteger("statusId", statusId);
			hqlQuery.setLong("userId", userId);		
			hqlQuery.setLong("questionPaperId", questionPaperId);		
			List<Object[]> temp = hqlQuery.list();
			for(Object[] objA:temp)
			{
				Quiz qz=new Quiz();
				qz.setQuizId(Long.parseLong(objA[0].toString()));
				qz.setQuizName((String) objA[1]);
				qz.setClassId(Long.parseLong(objA[2].toString()));
				qz.setCourseId(Long.parseLong(objA[3].toString()));
				qz.setQuestionPaperId(Long.parseLong(objA[4].toString()));
				qz.setTotalMarks(Float.parseFloat(objA[5].toString()));
				qz.setTimeOpen((Timestamp) objA[6]);
				qz.setTimeClose((Timestamp) objA[7]);
				qz.setDurationSeconds(Long.parseLong(objA[8].toString()));
				qz.setQuizStatus((String) objA[9]);
				qz.setQuizType((String) objA[10]);
				qz.setCreatedByUserId(Long.parseLong(objA[11].toString()));
				qz.setCreatedDate((Timestamp) objA[12]);
				qz.setModifiedByUserId(Long.parseLong(objA[13].toString())); 
				qz.setModifiedDate((Timestamp)objA[14]);
				qz.setStatusId((Integer) objA[15]) ;		
				qz.setQuestionPaperName((String) objA[16]);
				qz.setClassName((String) objA[17]);
				qz.setCourseName((String) objA[18]);    
				quizzes.add(qz);
			}
			if(quizzes.size()>0)
			{
				logger.info("Returned quizzes ");
			}
			else if(quizzes.size() == 0)
			{
				logger.warn("Warning :: No quiz ");
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