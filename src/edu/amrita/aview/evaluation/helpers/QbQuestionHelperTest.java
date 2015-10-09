/*
 * @(#)QbQuestionHelperTest.java 4.0 2013/10/16
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.evaluation.entities.QbAnswerChoice;
import edu.amrita.aview.evaluation.entities.QbQuestion;


/**
 * This class tests the methods of QbQuestionHelper.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QbQuestionHelperTest {

	/** The _question text. */
	private static String _questionText = null ;
	
	/** The _question text hash. */
	private static String _questionTextHash = null ;
	
	/** The _marks. */
	private static Float _marks = 0.0F ;
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		_questionText = "AWWQQQQQQ ?:TEST"+System.currentTimeMillis() ;
		_questionTextHash = "#ER$$%%%^^^:%$"+System.currentTimeMillis() ;
		_marks = 2.0F ;
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
	public void setUp() throws Exception {
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test create qb question.
	 *
	 * @throws Exception the exception
	 */
	@Ignore
	public void testCreateQbQuestion() throws Exception
	{
		QbQuestion  qbQuestion = new QbQuestion() ;
		qbQuestion.setQbSubcategoryId(9l) ;
		qbQuestion.setQbQuestionTypeId(1l) ;
		qbQuestion.setQbDifficultyLevelId(2l) ;
		qbQuestion.setQuestionText(_questionText);
		//qbQuestion.setMarks(_marks);
		//qbQuestion.setParentId(10l) ;
		qbQuestion.setQuestionTextHash(_questionTextHash) ;
		
		
		Set<QbAnswerChoice> ans = new HashSet<QbAnswerChoice>(QbAnswerChoiceHelper.getQbAnswersChoices(1l)) ;
		qbQuestion.setQbAnswerChoices(ans);
		QbQuestionHelper.createQbQuestion(qbQuestion,QbAnswerChoiceHelper.getQbAnswersChoices(1l), 44l) ;
		
		List<QbQuestion> qbQuestions = QbQuestionHelper.getQbQuestions(null, 89l, null, null, null, 44l) ;
		QbQuestion createdQbQuestion = new QbQuestion() ;
		for(int i = 0 ; i < qbQuestions.size() ; i++)
		{
			if(_questionText.equals(qbQuestions.get(i).getQuestionText()))
			{
				createdQbQuestion = qbQuestions.get(i) ;
			}
		}
		
		assertEquals("Did not create Question",_questionText,createdQbQuestion.getQuestionText());
		
	}

	/**
	 * Test update qb question.
	 *
	 * @throws Exception the exception
	 */
	@Ignore
	public void testUpdateQbQuestion() throws Exception 
	{
		QbQuestion qbQuestion = QbQuestionHelper.getQbQuestion(23l);
		//qbQuestion.setMarks(_marks);
		
		//qbQuestion.setQuestionText("bbww"+System.currentTimeMillis()) ;
		
		Set<QbAnswerChoice> qbAnswerChoices = new HashSet<QbAnswerChoice>(qbQuestion.getQbAnswerChoices());
		
		QbAnswerChoice qbac = new QbAnswerChoice();
		for(QbAnswerChoice qbac1 : qbAnswerChoices)
		{
			if(qbac1.getFraction() == 1f)
			{
				qbAnswerChoices.remove(qbac1);
				qbac1.setFraction(0f);				
				qbAnswerChoices.add(qbac1);
				qbac.setChoiceText(qbac1.getChoiceText());
			break;
		}
		}				
		//qbac.setChoiceText("New Answer Choice " + _questionTextHash);
		qbac.setFraction(1f);
		qbac.setDisplaySequence(qbAnswerChoices.size() + 1);
		qbac.setQbQuestion(qbQuestion);
		
		qbAnswerChoices.add(qbac);
		
		QbQuestionHelper.updateQbQuestion(qbQuestion, new ArrayList<QbAnswerChoice>(qbAnswerChoices), 45l);
		assertEquals("Did not update Quiz",_marks,qbQuestion.getMarks());
	}

	/**
	 * Test delete qb questions.
	 *
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unused")
	@Test
	public void testDeleteQbQuestions() throws Exception
	{
		List<QbQuestion> qbQuestions = new ArrayList<QbQuestion>();
		Long qbQuestionId = 13l;
		QbQuestion qbQuestion = QbQuestionHelper.getQbQuestion(qbQuestionId);
		qbQuestions.add(qbQuestion);
		qbQuestion = null;
		qbQuestions.add(qbQuestion);
		qbQuestionId = 14l;
		qbQuestion = QbQuestionHelper.getQbQuestion(qbQuestionId);
		qbQuestions.add(qbQuestion);
		/*List<QbQuestion> qbQuestions = QbQuestionHelper.getAllActiveQbQuestionsForSubcategory(1l) ;
		for(int i = 0 ; i < qbQuestions.size() ; i++)
		{
			assertTrue("Question must be active first",(qbQuestions.get(i).getStatusId() == StatusHelper.getActiveStatusId()));
			break ;
		}*/
		QbQuestionHelper.deleteQbQuestions(qbQuestions, 44l) ;		
		qbQuestions = QbQuestionHelper.getAllActiveQbQuestionsForSubcategory(1l);
		assertEquals("Didnot delete the questions", 0l, qbQuestions.size()) ;
	//	assertTrue("Quiz is not deleted",(qbQuestion.getStatusId() == StatusHelper.getDeletedStatusId()));
	}

	/**
	 * Test get qb question.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetQbQuestion() throws AViewException 
	{
		Long qbQuestionId = 9l;
		QbQuestion qbQuestion = QbQuestionHelper.getQbQuestion(qbQuestionId) ;		
		assertEquals("Did not get the question",qbQuestionId,qbQuestion.getQbQuestionId());
	}

	/**
	 * Test get qb questions.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetQbQuestions() throws AViewException
	{
		List<QbQuestion> qbQuestions = QbQuestionHelper.getQbQuestions(null, 90l, null, null, null, 554l) ;		
		assertEquals("Did not get all Question",18,qbQuestions.size());
	}

	/**
	 * Test get all active qb questions for subcategory.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetAllActiveQbQuestionsForSubcategory() throws AViewException 
	{
		List<QbQuestion> qbQuestions = QbQuestionHelper.getAllActiveQbQuestionsForSubcategory(79l);
		assertEquals("Did not get all questions",18,qbQuestions.size());
	}

	/**
	 * Test get all active qb questions for subcategories.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetAllActiveQbQuestionsForSubcategories() throws AViewException 
	{
		List<Long> subcats = QuestionPaperQuestionHelper.getSubcategoryIdForRandomQuestionPaperQuestions(47l);
		List<QbQuestion> qbQuestions = QbQuestionHelper.getAllActiveQbQuestionsForSubcategories(subcats);
		assertEquals("Did not get all questions",18,qbQuestions.size());
	}
	/*private List<QbAnswerChoice> createAnsChoices(Long qbQuestionId)
	{
		String choiceText1 = "Mango:"+System.currentTimeMillis() ;
		String choiceText2 = "Temple:"+System.currentTimeMillis() ;
	}*/
}
