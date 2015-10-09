/*
 * @(#)QuizQuestionResponseHelperTest.java 4.0 2013/10/05
 * 
 * Copyright  © 2013 E-Learning Research Lab, 
 * Amrita Vishwa Vidyapeetham. All rights reserved. 
 * E-Learning Research Lab and the A-VIEW logo are trademarks or
 * registered trademarks of E-Learning Research Lab. 
 * All other names used are the trademarks of their respective owners.
 */
package edu.amrita.aview.evaluation.helpers;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.evaluation.entities.QuizQuestionChoiceResponse;
import edu.amrita.aview.evaluation.entities.QuizQuestionResponse;
import edu.amrita.aview.evaluation.vo.StudentQuizResultVO;



/**
 * This class tests all methods in QuizQuestionResponseHelper.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QuizQuestionResponseHelperTest 
{
	
	/** The _creator id. */
	private static Long _creatorId = 0l ;
	
	/** The _updater id. */
	private static Long _updaterId = 0l ;
	
	/** The _created by user id. */
	private static Long _createdByUserId = 0l ;
	
	/** The _modified by user id. */
	private static Long _modifiedByUserId = 0l ;
	
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
		_createdByUserId = 0l ; 
		_modifiedByUserId =  0l ;
	}

	

	/**
	 * Creates the answer.
	 *
	 * @return the quiz question choice response
	 */
	private QuizQuestionChoiceResponse createAnswer()
	{
		QuizQuestionChoiceResponse qqcr = new QuizQuestionChoiceResponse() ;
		qqcr.setQuizAnswerChoiceId(1419l) ;
		
		return qqcr ;
	}
	
	/**
	 * Test get student answer sheet.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public final void testGetStudentAnswerSheet() throws AViewException
	{
		//QuizQuestionResponseHelper.getResultForPollingQuiz(21l);
		StudentQuizResultVO studentAnsSheet = QuizQuestionResponseHelper.getStudentAnswerSheet(93l, "student1") ;
		assertEquals("Didnot get the answersheet", 1l, studentAnsSheet) ;
	}
	
	/**
	 * Test get result for polling quiz.
	 *
	 * @throws AViewException
	 */
	//Fix for Bug #19388 
	@Test
	public final void testGetResultForPollingQuiz() throws AViewException {
		List<QuizQuestionResponse> qqrList = QuizQuestionResponseHelper
				.getResultForPollingQuiz(3280l, 411l);
		assertEquals("Didnot get the result", 2l , qqrList.size()) ;
	}
	
}
