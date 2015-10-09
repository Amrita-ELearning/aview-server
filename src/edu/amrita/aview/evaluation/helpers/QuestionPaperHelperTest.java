/*
 * @(#)QuestionPaperHelperTest.java 4.0 2013/10/17
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

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.evaluation.entities.QuestionPaper;



/**
 * This class tests the methods of QuestionPaperHelper.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QuestionPaperHelperTest 
{

	/** The _question paper name. */
	private static String _questionPaperName = null ;
	
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
	public static void setUpBeforeClass() throws Exception 
	{
		
		
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
		_questionPaperName = "Testing -- 27th Oct 2011 "+System.currentTimeMillis() ;
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
		_questionPaperName = null ;
	}

	/**
	 * Test get qp for quiz question.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetQPForQuizQuestion() throws AViewException
	{
		Long _questionPaperId = 1l ;
		//List qps = QuestionPaperHelper.getQPForQuizQuestion(_questionPaperId) ;
		
		//assertEquals("Didnot get the question paper", 160l, qps.size()) ;
	}

	/**
	 * Test get all active question papers.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetAllActiveQuestionPapers() throws AViewException 
	{
		List<QuestionPaper> qps = QuestionPaperHelper.getAllActiveQuestionPapers() ;
		
		assertEquals("Didnot get all question papers", 2l, qps.size()) ;
	}
	
	/**
	 * Test get question paper.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetQuestionPaper() throws AViewException 
	{
		Long _questionPaperId = 1l ;
		QuestionPaper qp = QuestionPaperHelper.getQuestionPaperId(_questionPaperId);		
		assertTrue("Didnot get the question paper", (_questionPaperId.equals(qp.getQuestionPaperId()))) ;
	}

	/**
	 * Test create question paper.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testCreateQuestionPaper() throws AViewException 
	{
		_questionPaperName += " For Creation" ;
		QuestionPaper qp = new QuestionPaper() ;
		qp.setQuestionPaperName(_questionPaperName) ;
		qp.setCurrentTotalMarks(2.0f) ;
		qp.setMaxTotalMarks(5l) ;
		qp.setIsComplete("N") ;
		QuestionPaper createdQuestionPaper = QuestionPaperHelper.createQuestionPaper(qp, _creatorId) ;				
		assertEquals("Didnot create the question paper", _questionPaperName, createdQuestionPaper.getQuestionPaperName()) ;
	}

	/**
	 * Test update question paper.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testUpdateQuestionPaper() throws AViewException 
	{
		Long _questionPaperId = 5l ;
		_questionPaperName += " For updation" ;
		QuestionPaper qp =  QuestionPaperHelper.getQuestionPaperId(_questionPaperId ) ;
		qp.setQuestionPaperName(_questionPaperName) ;
		QuestionPaperHelper.updateQuestionPaper(qp, _updaterId);
		
		List<QuestionPaper> qps = QuestionPaperHelper.getAllActiveQuestionPapers() ;
		
		QuestionPaper updatedQP = new QuestionPaper() ;
		
		for(int i = 0 ; i < qps.size() ; i++)
		{
			if(_questionPaperName.equals(qps.get(i).getQuestionPaperName()))
			{
				updatedQP = (QuestionPaper)qps.get(i) ;
				break ;
			}
		}
		
		assertEquals("Didnot update the question paper", _questionPaperName, updatedQP.getQuestionPaperName()) ;
	}

	/**
	 * Test delete question paper.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testDeleteQuestionPaper() throws AViewException 
	{
		Long _questionPaperId = 5l ;
		QuestionPaper qp = QuestionPaperHelper.getQuestionPaperId(_questionPaperId) ; 
		assertTrue("Question Paper must be active", (qp.getStatusId() == StatusHelper.getActiveStatusId())) ;
		QuestionPaperHelper.deleteQuestionPaper(_questionPaperId, _modifiedByUserId) ;
		 qp = QuestionPaperHelper.getQuestionPaperId(_questionPaperId) ; 
		assertTrue("Didnot delete the question paper", (qp.getStatusId() == StatusHelper.getDeletedStatusId())) ;
	}
	
	/**
	 * Test get all active question papers for user.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetAllActiveQuestionPapersForUser() throws AViewException
	{
		QuestionPaperHelper.getAllActiveQuestionPapersForUser(554l) ;
	}
	
	/**
	 * Test get question paper complete.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetQuestionPaperComplete() throws AViewException
	{
		QuestionPaperHelper.getQuestionPaperComplete(19l);
	}
	
	/**
	 * Test save question paper.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testSaveQuestionPaper() throws AViewException 
	{
		Long userId = 45l;
		QuestionPaperHelper.saveQuestionPaper(QuestionPaperHelper.getQuestionPaperId(18l),userId) ;
	}
	
	/**
	 * Test validate question paper.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testValidateQuestionPaper() throws AViewException 
	{
		Long userId = 5l;
		QuestionPaperHelper.validateQuestionPaper(157l,userId) ;
	}
	
	/**
	 * Test question paper preview.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testQuestionPaperPreview() throws AViewException 
	{
		
		QuestionPaperHelper.questionPaperPreview(159l,5l) ;
	}
}
