/*
 * @(#)QuestionPaperQuestionHelperTest.java 4.0 2013/10/17
 * 
 * Copyright  © 2013 E-Learning Research Lab, 
 * Amrita Vishwa Vidyapeetham. All rights reserved. 
 * E-Learning Research Lab and the A-VIEW logo are trademarks or
 * registered trademarks of E-Learning Research Lab. 
 * All other names used are the trademarks of their respective owners.
 */
package edu.amrita.aview.evaluation.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.evaluation.entities.QuestionPaperQuestion;



/**
 * This class tests the methods of QuestionPaperQuestionHelper.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QuestionPaperQuestionHelperTest 
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
		_modifiedByUserId = 0l ;
	}

	/**
	 * Test get all active question paper questions.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetAllActiveQuestionPaperQuestions() throws AViewException
	{
		List<QuestionPaperQuestion> qpqs = QuestionPaperQuestionHelper.getAllActiveQuestionPaperQuestions() ;
		
		assertEquals("Didnot get all question paper questions", 1l , qpqs.size()) ;
	}

	/**
	 * Test get question paper question.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetQuestionPaperQuestion() throws AViewException 
	{
		Long _questionPaperQuestionId = 5l ;
		QuestionPaperQuestion qpq = QuestionPaperQuestionHelper.getQuestionPaperQuestion(_questionPaperQuestionId) ;
		
		assertEquals("Didnot get the question paper question", _questionPaperQuestionId, qpq.getQuestionPaperQuestionId()) ;
	}

	/**
	 * Test create question paper question.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testCreateQuestionPaperQuestion() throws AViewException 
	{
		Long _questionPaperQuestionId = 4l ;
		Long _qbQuestionId = 41l ;
		QuestionPaperQuestion qpq = QuestionPaperQuestionHelper.getQuestionPaperQuestion(_questionPaperQuestionId) ;
		qpq.setQbDifficultyLevelId(2l) ;
		qpq.setQbQuestionId(_qbQuestionId) ;
		qpq.setQbSubcategoryId(9l) ;
		qpq.setNumRandomQuestions(0l) ;		
		
		QuestionPaperQuestionHelper.createQuestionPaperQuestion(qpq, _creatorId) ;
		
		List<QuestionPaperQuestion> qpqs = QuestionPaperQuestionHelper.getAllActiveQuestionPaperQuestions() ;
		
		QuestionPaperQuestion createdQPQ = new QuestionPaperQuestion() ;
				
		for(int i = 0 ; i < qpqs.size() ; i++)
		{					
			if(_qbQuestionId.equals(qpqs.get(i).getQbQuestionId()))
			{
				createdQPQ = qpqs.get(i) ;				
				break;
			}
		}
		
		assertNotNull(_questionPaperQuestionId) ;
		
		assertEquals("Didnot create question paper question", _qbQuestionId, createdQPQ.getQbQuestionId()) ;
		
	}

	/**
	 * Test update question paper question.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testUpdateQuestionPaperQuestion() throws AViewException 
	{
		Long _questionPaperQuestionId = 9l ;
		Long _diffLevelId = 1l ;
		QuestionPaperQuestion qpq = QuestionPaperQuestionHelper.getQuestionPaperQuestion(_questionPaperQuestionId) ;
		qpq.setQbDifficultyLevelId(_diffLevelId) ;
		qpq.setNumRandomQuestions(0l) ;		
		QuestionPaperQuestionHelper.updateQuestionPaperQuestion(qpq, _updaterId) ;
		
		List<QuestionPaperQuestion> qpqs = QuestionPaperQuestionHelper.getAllActiveQuestionPaperQuestions() ;
		
		QuestionPaperQuestion updatedQPQ = new QuestionPaperQuestion() ;
				
		for(int i = 0 ; i < qpqs.size() ; i++)
		{					
			if(_diffLevelId.equals(qpqs.get(i).getQbDifficultyLevelId()))
			{
				updatedQPQ = qpqs.get(i) ;				
				break;
			}
		}
		
		assertEquals("Didnot update the question paper question", _diffLevelId, updatedQPQ.getQbDifficultyLevelId()) ;
		
	}

	/**
	 * Test get all activ specifice questions for question paper.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetAllActivSpecificeQuestionsForQuestionPaper() throws AViewException 
	{
		QuestionPaperQuestionHelper.getAllActiveSpecificQuestionsForQuestionPaper(19l);
	}

	/**
	 * Test get all active question paper questions for qp.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetAllActiveQuestionPaperQuestionsForQP() throws AViewException 
	{
		QuestionPaperQuestionHelper.getAllActiveQuestionPaperQuestionsForQP(19l,45l) ;
	}
		
	/**
	 * Test get subcategory id for question paper_ random type.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetSubcategoryIdForQuestionPaper_RandomType() throws AViewException 
	{
		List<Long> subCats = QuestionPaperQuestionHelper.getSubcategoryIdForRandomQuestionPaperQuestions(47l);
		assertEquals("Didnot get all subcategories",3l , subCats.size()) ;
	}
}
