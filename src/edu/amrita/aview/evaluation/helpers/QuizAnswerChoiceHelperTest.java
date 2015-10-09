/*
 * @(#)QuizAnswerChoiceHelperTest.java 4.0 2013/10/07
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
import org.junit.Test;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.evaluation.entities.QuizAnswerChoice;
import edu.amrita.aview.evaluation.entities.QuizQuestion;



/**
 * This class tests all methods in QuizAnswerChoiceHelper.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QuizAnswerChoiceHelperTest 
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
	 * Test get answer.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetAnswer() throws AViewException
	{
		List<QuizQuestion> qqs = QuizQuestionHelper.getQuizQuestionsForQuiz(241l) ;
		List<QuizAnswerChoice> qqsRcvd = QuizAnswerChoiceHelper.getAnswer(qqs.size(), qqs) ;
		
		assertEquals("Didnot get all quiz questions", qqs.size(), qqsRcvd.size()) ;
	}
	
}
