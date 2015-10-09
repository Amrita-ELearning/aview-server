/*
 * @(#)QuizQuestionHelperTest.java 4.0 2013/10/05
 * 
 * Copyright  © 2013 E-Learning Research Lab, 
 * Amrita Vishwa Vidyapeetham. All rights reserved. 
 * E-Learning Research Lab and the A-VIEW logo are trademarks or
 * registered trademarks of E-Learning Research Lab. 
 * All other names used are the trademarks of their respective owners.
 */
package edu.amrita.aview.evaluation.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.evaluation.entities.QuizAnswerChoice;
import edu.amrita.aview.evaluation.entities.QuizQuestion;



/**
 * This class tests all methods of QuizQuestionHelper.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QuizQuestionHelperTest 
{
	
	/** The _creator id. */
	private static Long _creatorId = 0l ;
	
	/** The _updater id. */
	private static Long _updaterId = 0l ;
	
	/** The _created by user id. */
	private static Long _createdByUserId = 0l ;
	
	/** The _modified by user id. */
	private static Long _modifiedByUserId = 0l ;
	
	/** The _quiz id. */
	private static Long _quizId = 0l ;

	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * Tear down after class.
	 *
	 * @throws Exception the exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception 
	{
		_creatorId = 44l ;
		_updaterId = 44l ;
		_createdByUserId = 44l ; 
		_modifiedByUserId = 44l ;
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void tearDown() throws Exception 
	{
		_creatorId = 0l ;
		_updaterId = 0l ;
		_createdByUserId = 0l;  
		_modifiedByUserId = 0l;
		_quizId = 0l ;
		
	}

	

	/**
	 * Test get quiz question.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetQuizQuestion() throws AViewException
	{
		Long _quizQuestionId = 442l ;
		QuizQuestion qq = QuizQuestionHelper.getQuizQuestionId(_quizQuestionId) ;
		
		assertEquals("Didnot get the quiz question", _quizQuestionId, qq.getQuizQuestionId()) ;
	}

	/**
	 * Test create quiz question.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testCreateQuizQuestion() throws AViewException
	{
		Long _quizQuestionId = 442l ;
		Float marks = 15f ;
		String _questionTextHash = "#@@ASDSD#WEWEWE:1319547465966" ;
		//QuizQuestion qq = QuizQuestionHelper.getQuizQuestion(_quizQuestionId) ;
		QuizQuestion qq = new QuizQuestion() ;
		qq.setCategoryId(3l) ;
		qq.setCategoryName("DataCommunications") ;
		qq.setSubcategoryId(2l);
		qq.setSubcategoryName("subcat1") ;
		qq.setQuestionTypeName("Multiple Choice") ;		
		qq.setDifficultyLevelName("difficult") ;
		qq.setQuestionPaperQuestionId(8l) ;
		qq.setQuestionTextHash(_questionTextHash) ;
		qq.setQuestionText("Where does Sun rise from ?:1319547465966") ;
		qq.setMarks(4.0) ;		
		qq.setQuiz(QuizHelper.getQuizById(241l));
		
		/*List<QuizQuestion> qqs = new ArrayList<QuizQuestion>() ;
		qqs.add(QuizQuestionHelper.getQuizQuestion(_quizQuestionId)) ;
		
		List<QuizAnswerChoice> qac = QuizAnswerChoiceHelper.getAnswer(1, qqs) ;*/
		
		qq.addQuizAnswerChoice(createAnswerChoices()) ;
		/*Set<QuizAnswerChoice> qzAns = new HashSet<QuizAnswerChoice>(createAnswerChoices()) ;
		qq.setQuizAnswerChoice(qzAns) ;*/
		
		//assertTrue("Quiz question must be active first", (qq.getStatusId() == StatusHelper.getActiveStatusId()));
		
		QuizQuestion createdQQ = QuizQuestionHelper.createQuizQuestion(qq, _creatorId) ;
		
		assertTrue("Didnot create quiz question", (_questionTextHash.equals(createdQQ.getQuestionTextHash()))) ;
	}

	/**
	 * Test update quiz question.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testUpdateQuizQuestion() throws AViewException
	{
		Long _quizQuestionId = 442l ;
		Double _marks = 22.0;
		QuizQuestion qq = QuizQuestionHelper.getQuizQuestionId(_quizQuestionId) ;
		
		qq.setMarks(_marks) ;
		
		QuizQuestionHelper.updateQuizQuestion(qq, _updaterId) ;
					
		QuizQuestion updatedQQ = QuizQuestionHelper.getQuizQuestionId(_quizQuestionId) ;
		
		assertTrue("Didnot update the quiz question", (_marks.equals(updatedQQ.getMarks()))) ;
	}

	/**
	 * Test delete quiz questions.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testDeleteQuizQuestions() throws AViewException 
	{
		Long _quizQuestionId = 450l ;		
		QuizQuestion qq = QuizQuestionHelper.getQuizQuestionId(_quizQuestionId) ;
		assertTrue("Quiz question must be active first", (qq.getStatusId() == StatusHelper.getActiveStatusId())) ;
		QuizQuestionHelper.deleteQuizQuestions(_quizQuestionId, _modifiedByUserId) ;
		qq = QuizQuestionHelper.getQuizQuestionId(_quizQuestionId) ;
		assertTrue("Didnot delete the quiz question", (qq.getStatusId() == StatusHelper.getDeletedStatusId())) ;
	}

	/**
	 * Test get quiz questions for quiz.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void  testGetQuizQuestionsForQuiz() throws AViewException
	{
		Long quizId = 201l;
		QuizQuestionHelper.getQuizQuestionsForQuiz(quizId);
	}
	
	/**
	 * Test Get polling quiz for student.
	 *
	 * @throws AViewException
	 */
	@Test
	public void  testGetPollingQuizForStudent() throws AViewException
	{
		QuizQuestionHelper.getPollingQuizForStudent(555l);
	}
	
	/**
	 * Test get all active quiz questions.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void  testGetAllActiveQuizQuestions() throws AViewException
	{
		QuizQuestionHelper.getAllActiveQuizQuestions();
	}
	
	/**
	 * Creates the answer choices.
	 *
	 * @return the quiz answer choice
	 */
	private QuizAnswerChoice createAnswerChoices() 
	{
		List<QuizAnswerChoice> qacs = new ArrayList<QuizAnswerChoice>() ;
		
		QuizAnswerChoice qac1 = new QuizAnswerChoice() ;
		qac1.setChoiceText("bb"+System.currentTimeMillis()) ;
		qac1.setChoiceTextHash("asasd#@$%%%#^#$@$$"+System.currentTimeMillis()) ;
		qac1.setFraction(1.0f) ;
		
		qacs.add(qac1) ;
		qacs.add(qac1) ;
		
		return qac1 ;
	}
}
