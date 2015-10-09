/*
 * @(#)QuizHelper.java 4.0 2013/10/04
 * 
 * Copyright  © 2013 E-Learning Research Lab, 
 * Amrita Vishwa Vidyapeetham. All rights reserved. 
 * E-Learning Research Lab and the A-VIEW logo are trademarks or
 * registered trademarks of E-Learning Research Lab. 
 * All other names used are the trademarks of their respective owners.
 */
package edu.amrita.aview.evaluation.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ibm.icu.text.SimpleDateFormat;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.AppenderUtils;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.evaluation.QuizConstant;
import edu.amrita.aview.evaluation.daos.QuizDAO;
import edu.amrita.aview.evaluation.entities.QbAnswerChoice;
import edu.amrita.aview.evaluation.entities.QbDifficultyLevel;
import edu.amrita.aview.evaluation.entities.QbQuestion;
import edu.amrita.aview.evaluation.entities.QbQuestionMediaFile;
import edu.amrita.aview.evaluation.entities.QbQuestionType;
import edu.amrita.aview.evaluation.entities.QbSubcategory;
import edu.amrita.aview.evaluation.entities.QuestionPaper;
import edu.amrita.aview.evaluation.entities.Quiz;
import edu.amrita.aview.evaluation.entities.QuizAnswerChoice;
import edu.amrita.aview.evaluation.entities.QuizQuestion;
import edu.amrita.aview.evaluation.entities.QuizQuestionMediaFile;
import edu.amrita.aview.evaluation.vo.QuestionPaperQuestionListVO;
import edu.amrita.aview.gclm.entities.Class;
import edu.amrita.aview.gclm.entities.ClassRegistration;
import edu.amrita.aview.gclm.entities.Course;
import edu.amrita.aview.gclm.entities.User;
import edu.amrita.aview.gclm.helpers.CacheHelper;
import edu.amrita.aview.gclm.helpers.ClassHelper;
import edu.amrita.aview.gclm.helpers.ClassRegistrationHelper;
import edu.amrita.aview.gclm.helpers.CourseHelper;
import edu.amrita.aview.gclm.helpers.UserHelper;



/**
 * This class is the connector between client side and server side .
 * 
 * @author Swati
 * @version 4.0
 * @since 3.0
 */


/**
 * @author swatiiyer
 *
 */
public class QuizHelper extends Auditable {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(QuizHelper.class);
	
	/** The quizzes map. */
	private static Map<Long, Quiz> quizzesMap = Collections
			.synchronizedMap(new HashMap<Long, Quiz>());
	
	/** The Constant CACHE_CODE. */
	private static final String CACHE_CODE = "QuizHelper";
	
	/**
	 * Populate cache.
	 *
	 * @param quizIdsMap the quiz ids map
	 * @throws AViewException
	 */
	private static synchronized void populateCache(Map<Long, Quiz> quizIdsMap)throws AViewException 
	{
		quizzesMap.clear();
		quizzesMap.putAll(quizIdsMap);
		CacheHelper.setCache(CACHE_CODE);
	}

	/**
	 * Adds the item to cache.
	 *
	 * @param quiz the quiz
	 * @throws AViewException
	 */
	private static synchronized void addItemToCache(Quiz quiz)throws AViewException 
	{
		quizzesMap.put(quiz.getQuizId(), quiz);
	}
	
	/**
	 * Removes the item from cache.
	 *
	 * @param quiz the quiz
	 */
	private static synchronized void removeItemFromCache(Quiz quiz)
	{
		quizzesMap.remove(quiz.getQuizId());
	}

	/**
	 * Quiz ids map.
	 *
	 * @return the map
	 * @throws AViewException
	 */
	private static synchronized Map<Long, Quiz> quizIdsMap() throws AViewException 
	{
		Integer activeSID = StatusHelper.getActiveStatusId();
		// If cache is expired or invalidated
		if (!CacheHelper.isCacheValid(CACHE_CODE)) 
		{
			List<Quiz> quizess = QuizDAO.getAllActiveQuizzes(activeSID);
			// Populate the Map
			Map<Long, Quiz> quizIdMap = new HashMap<Long, Quiz>();
			
			for(Quiz quiz : quizess)
			{
				quizIdMap.put(quiz.getQuizId(), quiz);
			}

			populateCache(quizIdMap);			
		}
		return quizzesMap;
	}

	/**
	 * Prints the quiz.
	 *
	 * @param quiz the quiz
	 */
	private static void printQuiz(Quiz quiz) 
	{
		logger.debug("Quiz:" + quiz.toString());

		if (quiz.getQuizQuestion() != null) 
		{
			logger.debug("QuizQuestion:" + quiz.getQuizQuestion().size());
			for (QuizQuestion qq : quiz.getQuizQuestion()) 
			{
				logger.debug("QuizQuestion:" + qq.toString());
			}
		}
	}
	
	/**
	 * Gets the all active quizzes.
	 *
	 * @return the all active quizzes
	 * @throws AViewException
	 */
	public static List<Quiz> getAllActiveQuizzes() throws AViewException 
	{
		List<Quiz> quizList = new ArrayList<Quiz>();
		quizList.addAll(quizIdsMap().values());
		return quizList;
	}

	/**
	 * Gets the quiz by id.
	 *
	 * @param quizId the quiz id
	 * @return the quiz by id
	 * @throws AViewException
	 */
	public static Quiz getQuizById(Long quizId) throws AViewException 
	{
		//Fix for Bug #10430
		Quiz quiz = QuizDAO.getQuizById(quizId);
		populateFKNames(quiz);
		return quiz;
	}
		
	/**
	 * Creates the quiz.
	 *
	 * @param quiz the quiz
	 * @param creatorId the creator id
	 * @return the quiz
	 * @throws AViewException
	 */
	public static Quiz createQuiz(Quiz quiz, Long creatorId) throws AViewException 
	{
		setQuizTransientProperties(quiz, creatorId) ;
		List<QuestionPaperQuestionListVO> quizQuestionListVo = QuestionPaperHelper.getQuestionPaperQuestionsForQuiz(quiz.getQuestionPaperId());			
		populateQuizQuestionsList(quiz, quizQuestionListVo);		
		quiz.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
		//Setting same time for quiz start time and end time in live quiz
		if(quiz.getQuizType().equals(Constant.LIVE_QUIZ_MODE))
		{
			//Bug #10691
			quiz.setTimeOpen(quiz.getCreatedDate());
			quiz.setTimeClose(quiz.getCreatedDate());
		}
		QuizDAO.createQuiz(quiz);		
		return quiz;
	}
	
	/**
	 * Set the transient attributes of quiz.
	 *
	 * @param quiz the quiz object
	 * @param creatorId the creator id
	 */
	private static void setQuizTransientProperties(Quiz quiz , Long creatorId)
	{		
		User tempUser = UserHelper.getUser(creatorId) ;
		quiz.setInstituteId(tempUser.getInstituteId()) ;
		quiz.setUserName(tempUser.getUserName()) ;
		
		Class tempClass = ClassHelper.getClass(quiz.getClassId()) ;
		quiz.setClassName(tempClass.getClassName()) ;
		
		QuestionPaper tempQP = QuestionPaperHelper.getQuestionPaperId(quiz.getQuestionPaperId()) ;
		quiz.setQuestionPaperName(tempQP.getQuestionPaperName()) ;
		
		Course tempCourse = CourseHelper.getCourse(quiz.getCourseId()) ;
		quiz.setCourseName(tempCourse.getCourseName()) ;			
		
	}
	
	/**
	 * Populate quiz questions list.
	 *
	 * @param quiz the quiz
	 * @param quizQuestionListVo the quiz question list vo
	 * @throws AViewException
	 */
	private static void populateQuizQuestionsList(Quiz quiz, List<QuestionPaperQuestionListVO> quizQuestionListVo) throws AViewException 
	{
		QuizQuestion quizQuestion = null;
		for(int i = 0; i < quizQuestionListVo.size(); i++)
		{
			QuestionPaperQuestionListVO questionPaperQuestionVO = quizQuestionListVo.get(i);
			quizQuestion = new QuizQuestion();			
			populateNames(questionPaperQuestionVO.qbQuestion, quizQuestion);
			quizQuestion.setQuestionPaperQuestionId(questionPaperQuestionVO.questionPaperQuestion.getQuestionPaperQuestionId());
			quizQuestion.setQuestionText(questionPaperQuestionVO.qbQuestion.getQuestionText());
			quizQuestion.setQuestionTextHash(questionPaperQuestionVO.qbQuestion.getQuestionTextHash());
			quizQuestion.setQbQuestionId(questionPaperQuestionVO.qbQuestion.getQbQuestionId());
			quizQuestion.setMarks(questionPaperQuestionVO.questionPaperQuestion.getMarks());
			populateQuizAnswerChoice(quizQuestion, questionPaperQuestionVO.qbQuestion.getQbAnswerChoices());
			/*if(questionPaperQuestionVO.qbQuestion.getQbQuestionMediaFiles() != null)
			{
				populateQuizQuestionMediaFile(quizQuestion, questionPaperQuestionVO.qbQuestion.getQbQuestionMediaFiles());
			}*/
			quizQuestion.setQuiz(quiz);
			quiz.addQuizQuestion(quizQuestion);
		}		
	}
	
	/**
	 * Populate names.
	 *
	 * @param qbQuestion the qb question
	 * @param quizQuestion the quiz question
	 * @throws AViewException
	 */
	private static void populateNames(QbQuestion qbQuestion, QuizQuestion quizQuestion) throws AViewException 
	{
		populateCategoryDetails(qbQuestion, quizQuestion);
		populateDifficultyLevelDetails(qbQuestion, quizQuestion);
		populateQuestionTypeDetails(qbQuestion, quizQuestion);		
	}
	
	/**
	 * Populate quiz answer choice.
	 *
	 * @param quizQuestion the quiz question
	 * @param qbAnswerChoice the qb answer choice
	 * @throws AViewException
	 */
	private static void populateQuizAnswerChoice(QuizQuestion quizQuestion, Set<QbAnswerChoice> qbAnswerChoice)throws AViewException 
	{
		QuizAnswerChoice tmpQuizAnswerChoice = null;		
				
		for(QbAnswerChoice qbAnsChoice : qbAnswerChoice)
		{
			tmpQuizAnswerChoice = new QuizAnswerChoice();
			tmpQuizAnswerChoice.setChoiceText(qbAnsChoice.getChoiceText());
			tmpQuizAnswerChoice.setDisplaySequence(qbAnsChoice.getDisplaySequence());
			tmpQuizAnswerChoice.setChoiceTextHash(qbAnsChoice.getChoiceTextHash());
			tmpQuizAnswerChoice.setFraction(qbAnsChoice.getFraction());			
			quizQuestion.addQuizAnswerChoice(tmpQuizAnswerChoice);
		}
	}
	private static void populateQuizQuestionMediaFile(QuizQuestion quizQuestion, Set<QbQuestionMediaFile> qbQuestionMediaFiles)throws AViewException 
	{
		/*QuizQuestionMediaFile tmpQuizQuestionMediaFile = null;		
		
		for(QbQuestionMediaFile qbQuestionMediaFile : qbQuestionMediaFiles)
		{
			tmpQuizQuestionMediaFile = new QuizQuestionMediaFile();
			tmpQuizQuestionMediaFile.setQuizQuestionMediaFileName(qbQuestionMediaFile.getQbQuestionMediaFileName());
			tmpQuizQuestionMediaFile.setQuizQuestionMediaFolderPath(qbQuestionMediaFile.getQbQuestionMediaFolderPath());
			tmpQuizQuestionMediaFile.setQuizQuestionMediaFileType(qbQuestionMediaFile.getQbQuestionMediaFileType());
			quizQuestion.addQuizQuestionMediaFiles(tmpQuizQuestionMediaFile);
		}*/
	}
	
	/**
	 * Populate category details.
	 *
	 * @param qbQuestion the qb question
	 * @param quizQuestion the quiz question
	 * @throws AViewException
	 */
	private static void populateCategoryDetails(QbQuestion qbQuestion, QuizQuestion quizQuestion) throws AViewException 
	{
		Long qbSubcategoryId = qbQuestion.getQbSubcategoryId();
		QbSubcategory qbSubcategory = QbSubcategoryHelper.getQbSubcategoryById(qbSubcategoryId);
		quizQuestion.setCategoryId(qbSubcategory.getQbCategoryId());
		quizQuestion.setCategoryName(qbSubcategory.getQbCategoryName());
		quizQuestion.setSubcategoryId(qbSubcategory.getQbSubcategoryId());
		quizQuestion.setSubcategoryName(qbSubcategory.getQbSubcategoryName());
	}
	
	/**
	 * Populate difficulty level details.
	 *
	 * @param qbQuestion the qb question
	 * @param quizQuestion the quiz question
	 * @throws AViewException
	 */
	private static void populateDifficultyLevelDetails(QbQuestion qbQuestion, QuizQuestion quizQuestion) throws AViewException 
	{
		if(qbQuestion.getQbDifficultyLevelId() != null)
		{
			QbDifficultyLevel qbDifficultyLevel = QbDifficultyLevelHelper.getDifficultyLevelById(qbQuestion.getQbDifficultyLevelId());
			quizQuestion.setDifficultyLevelName(qbDifficultyLevel.getQbDifficultyLevelName());
		}
	}
	
	/**
	 * Populate question type details.
	 *
	 * @param qbQuestion the qb question
	 * @param quizQuestion the quiz question
	 * @throws AViewException
	 */
	private static void populateQuestionTypeDetails(QbQuestion qbQuestion, QuizQuestion quizQuestion) throws AViewException 
	{
		if(qbQuestion.getQbQuestionTypeId() != null)
		{			
			QbQuestionType qbQuestionType = QbQuestionTypeHelper.getQbQuestionTypeById(qbQuestion.getQbQuestionTypeId()) ; 					
			quizQuestion.setQuestionTypeName(qbQuestionType.getQbQuestionTypeName());
		}
	}
	
	/**
	 * Launch quiz instance for mobile.
	 *
	 * @param quizId the quiz id
	 * @throws AViewException
	 */
	public static void launchQuizInstanceForMobile(Long quizId)throws AViewException 
	{
		//Commenting this block because mobile quiz is not using 
		//Bug fix #11695
		/*ArrayList<String> newNodeTypePersonalList = new ArrayList<String>();
		Quiz quiz = QuizDAO.getQuizById(quizId);		
		if (quiz.getQuizType().equals(Constant.LIVE_QUIZ_MODE)) 
		{
			List<User> users = UserHelper.getUsersForLiveQuiz(quiz.getClassId());

			for (User user : users) 
			{
				if(user.getMobileNumber() != null)
				{
					newNodeTypePersonalList.add(user.getMobileNumber());
				}
			}
		}
		try 
		{
			MobileQuizHelper.createMobileQuizQuestion(quizId, newNodeTypePersonalList);	
		} 
		catch (Exception e) 
		{
			throw new AViewException("Error while starting Live Quiz for mobile :: "+ e.getMessage());
		}*/
	}

	/**
	 * Update quiz.
	 *
	 * @param updateQuiz the update quiz
	 * @param updaterId the updater id
	 * @return the quiz
	 * @throws AViewException
	 */
	public static Quiz updateQuiz(Quiz updateQuiz, Long updaterId)throws AViewException 
	{
		Quiz quiz = getQuizById(updateQuiz.getQuizId());
		if (quiz != null) 
		{
			List<QuestionPaperQuestionListVO> quizQuestionListVo = QuestionPaperHelper.getQuestionPaperQuestionsForQuiz(updateQuiz.getQuestionPaperId());			
			populateQuizQuestionsList(updateQuiz, quizQuestionListVo);
			quiz.updateFrom(updateQuiz);
			quiz.setModifiedAuditData(updaterId,TimestampUtils.getCurrentTimestamp());
			printQuiz(updateQuiz);
			QuizDAO.updateQuiz(quiz);
			addItemToCache(quiz);
		} 
		else 
		{
			throw new AViewException("Quiz with id :" + updateQuiz.getQuizName()+ ": is not found");
		}
		logger.debug("Exited updateQuiz without throwing any exception:");
		return quiz;
	}

	/**
	 * Delete quiz.
	 *
	 * @param quizId the quiz id
	 * @param updaterId the updater id
	 * @throws AViewException
	 */
	public static void deleteQuiz(Long quizId, Long updaterId) throws AViewException 
	{
		Quiz quiz = getQuizById(quizId);
		if (quiz != null) 
		{
			quiz.setStatusId(StatusHelper.getDeletedStatusId());
			quiz.setQuizName(quiz.getQuizName() + AppenderUtils.DeleteAppender());
			quiz.setModifiedAuditData(updaterId,TimestampUtils.getCurrentTimestamp());			
			QuizDAO.updateQuiz(quiz);
			removeItemFromCache(quiz);
		} 
		else 
		{
			throw new AViewException("Quiz with id :" + quizId+ ": is not found");
		}
	}

	/**
	 * Gets the all active quizzes for user.
	 *
	 * @param userId the user id
	 * @return the all active quizzes for user
	 * @throws AViewException
	 */
	public static List<Quiz> getAllActiveQuizzesForUser(Long userId) throws AViewException 
	{
		Integer activeSId = StatusHelper.getActiveStatusId();		
		return QuizDAO.getAllActiveQuizzesForUser(userId,activeSId);
	}
	
	/**
	 * Gets the all active quizzes for question paper.
	 * 
	 * @param userId the user id
	 * @param questionPaperId the question paper id
	 * @return the all active quizzes for question paper
	 * @throws AViewException
	 */
	public static List<Quiz> getAllActiveQuizzesForQuestionPaper(Long userId,Long questionPaperId) throws AViewException 
	{
		Integer activeSId = StatusHelper.getActiveStatusId();		
		return QuizDAO.getAllActiveQuizzesForQuestionPaper(userId,questionPaperId,activeSId);
	}

	/**
	 * Gets the all active quizzes for student.
	 *
	 * @param userId the user id
	 * @return the all active quizzes for student
	 * @throws AViewException
	 */
	public static List<Quiz> getAllActiveQuizzesForStudent(Long userId) throws AViewException 
	{
		Integer activeSId = StatusHelper.getActiveStatusId();		
		return QuizDAO.getAllActiveQuizzesForStudent(userId,activeSId);
	}

	/**
	 * Gets the quiz question answer.
	 *
	 * @param quizId the quiz id
	 * @return the quiz question answer
	 * @throws AViewException
	 */
	public static List getQuizQuestionAnswer(Long quizId) throws AViewException 
	{
		return QuizDAO.getQuizQuestionAnswer(quizId);
	}

	/**
	 * Gets the quiz result for student.
	 *
	 * @param quizId the quiz id
	 * @param userId the user id
	 * @return the quiz result for student
	 * @throws AViewException
	 */
	public static List<Quiz> getQuizResultForStudent(Long quizId,Long userId)throws AViewException 
	{
		User tempUser = UserHelper.getUser(userId) ;
		//Fix for Bug #11534		
		Quiz tempQuiz = QuizHelper.getQuizById(quizId);
		Boolean isModerator = checkIsModerator(userId,tempQuiz.getClassId());
		return QuizDAO.getQuizResultForStudent(quizId,userId,tempUser.getRole(),isModerator);
	}

	/**
	 * Gets the quiz question result.
	 *
	 * @param quizId the quiz id
	 * @return the quiz question result
	 * @throws AViewException
	 */
	public static List<Quiz> getQuizResultByQuestion(Long quizId)throws AViewException 
	{
		List<Quiz> quizList = QuizDAO.getQuizResultByQuestion(quizId) ;
		return quizList;		
	}
	 
 	/**
 	 * Format quiz name.
 	 *
 	 * @param quizName the quiz name
	 * @return String
 	 */
 	public static String formatQuizName(String quizName,Long classId)
  	 {
  		Date dNow =new Date();
  		SimpleDateFormat date =new SimpleDateFormat (QuizConstant.POLLING_DATE_FORMAT);
  		Class cls = ClassHelper.getClass(classId);
		quizName = new String(QuizConstant.POLLING_PAPER_NAME + cls.getClassName() + '_' + date.format(dNow));	
		return quizName;
  	 }
	/**
	 * Creates the polling quiz.
	 *
	 * @param questionPaper the question paper
	 * @param classId the class id
	 * @param courseId the course id
	 * @param creatorId the creator id
	 * @return the quiz
	 * @throws AViewException
	 */
	public static Quiz createPollingQuiz(QuestionPaper questionPaper,Long classId,Long courseId,Long creatorId) throws AViewException
	{
		String quizName = "" ;
		Quiz quiz=new Quiz();
		User tempU = UserHelper.getUser(creatorId);
		quizName = formatQuizName(quizName,classId) ;
		quiz.setQuizName(quizName);		
		quiz.setTotalMarks(questionPaper.getMaxTotalMarks());
		// Set the status as ready while creating a quiz
		quiz.setQuizStatus(QuizConstant.QUIZSTATUS[0]); 
		quiz.setInstituteId(tempU.getInstituteId()) ;
		quiz.setQuestionPaperId(questionPaper.getQuestionPaperId());
		quiz.setTimeOpen(TimestampUtils.getCurrentTimestamp());
		quiz.setTimeClose(TimestampUtils.getCurrentTimestamp());
		quiz.setQuizType(QuizConstant.ONLINE_QUIZ_TYPE);
		// Set the transient variables
		quiz.setClassId(classId);
		quiz.setCourseId(courseId);		
		quiz=QuizHelper.createQuiz(quiz, creatorId);		
		return quiz;	
	}
	

	/**
	 * Gets the question paper result.
	 *
	 * @param quizId the quiz id
	 * @return the question paper result
	 * @throws AViewException
	 */
	public static List<Quiz> getQuizResultByQuestionPaper(Long quizId)throws AViewException 
	{
		List<Quiz> quizList = QuizDAO.getQuizResultByQuestionPaper(quizId) ;
		return quizList;		
	}
	
	/**
	 * Gets the question paper result for chart.
	 *
	 * @param quizId the quiz id
	 * @return the question paper result for chart
	 * @throws AViewException
	 */
	public static List<Quiz> getQuestionPaperResultForChart(Long quizId)throws AViewException 
	{
		List<Quiz> quizList = QuizDAO.getQuestionPaperResultForChart(quizId) ;		
		return quizList;		
	}
	
	/**
	 * Gets the all active quizzes off line for student.
	 *
	 * @param userId the user id
	 * @return the all active quizzes off line for student
	 * @throws AViewException
	 */
	public static List<Quiz> getAllActiveQuizzesOffLineForStudent(Long userId) throws AViewException 
	{
		Integer activeSId = StatusHelper.getActiveStatusId();		
		return QuizDAO.getAllActiveQuizzesOffLineForStudent(userId,activeSId);
	}
	
	/**
	 * Gets the location based result.
	 *
	 * @param quizId the quiz id
	 * @return the location based result
	 * @throws AViewException
	 */
	public static List<Quiz> getQuizResultByLocation(Long quizId)throws AViewException 
	{
		List<Quiz> quizList = QuizDAO.getQuizResultByLocation(quizId) ;		
		return quizList;		
	}
	
	/**
	 * Gets the category based result.
	 *
	 * @param quizId the quiz id
	 * @return the category based result
	 * @throws AViewException
	 */
	public static List<Quiz> getCategoryBasedResult(Long quizId)throws AViewException 
	{
		List<Quiz> quizList = QuizDAO.getCategoryBasedResult(quizId) ;		
		return quizList;		
	}
	//Fix for Bug #10430
	/**
	 * Populate fk names.
	 * 
	 * @param quiz the quiz
	 * @throws AViewException
	 */
	private static void populateFKNames(Quiz quiz) throws AViewException
    {
        Class aviewClass = ClassHelper.getClass(quiz.getClassId());
        quiz.setClassName(aviewClass.getClassName());
        quiz.setCourseName(CourseHelper.getCourse(aviewClass.getCourseId()).getCourseName());            
    }
	//Fix for Bug #11534
	/**
	 * Check is moderator.
	 * 
	 * @param userId the user id
	 * @param classId the class id
	 * @return the boolean
	 * @throws AViewException
	 */
	private static Boolean checkIsModerator(Long userId,Long classId) throws AViewException
	{
		Boolean returnValue = false;
		List<ClassRegistration> clsregList = ClassRegistrationHelper.searchForClassRegisterForUser(userId, classId, null, 0l, 0l);
		if(clsregList.size() > 0)
		{
			// basically the search should return only a single user(with class reg details) who is a moderator
			ClassRegistration clsreg = clsregList.get(0); 
			if(clsreg.getIsModerator().equals(QuizConstant.YES))
			{
				returnValue = true;
			}
		}
		
		return returnValue;
	}
	
	/**
	 * Clear cache.
	 */
	public static void clearCache()
	{
		logger.debug("Entering QuizHelper::clearCache");
		quizzesMap = null;
		logger.debug("Entering QuizHelper::clearCache");
	}
}
