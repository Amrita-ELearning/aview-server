/*
 * @(#)QbAnswerChoiceHelperTest.java 4.0 2013/10/16
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
import edu.amrita.aview.evaluation.entities.QbAnswerChoice;



/**
 * This class tests various methods of QbAnswerChoiceHelper.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QbAnswerChoiceHelperTest 
{
	
	/** The _choice hash text. */
	private static String _choiceHashText = "@#$%^^a"+System.currentTimeMillis() ;
	
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
	 * Test create qb answer choice.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testCreateQbAnswerChoice() throws AViewException 
	{
		QbAnswerChoice qbAns = new QbAnswerChoice() ;
		qbAns.setChoiceText("a") ;
		qbAns.setChoiceTextHash(_choiceHashText) ;
		qbAns.setQbQuestion(QbQuestionHelper.getQbQuestion(9l));
		qbAns.setFraction(1.0f);
		QbAnswerChoiceHelper.createQbAnswerChoice(qbAns, 44l) ;
		
		List<QbAnswerChoice> qbAnsChoices = QbAnswerChoiceHelper.getQbAnswersChoices(9l) ;
		
		QbAnswerChoice createdQbAnswerChoice = new QbAnswerChoice() ;
		for(int i = 0 ; i < qbAnsChoices.size() ; i++)
		{
			if(_choiceHashText.equals(qbAnsChoices.get(i).getChoiceTextHash()))
			{
				createdQbAnswerChoice = (QbAnswerChoice)qbAnsChoices.get(i) ;
				break ;
			}
		}
		
		assertEquals("Did not create answer choice",createdQbAnswerChoice.getChoiceTextHash(),qbAns.getChoiceTextHash());
	}

	/**
	 * Test get all active qb answers choices.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetAllActiveQbAnswersChoices() throws AViewException
	{
		List<QbAnswerChoice> qbAnsChoices = QbAnswerChoiceHelper.getAllActiveQbAnswersChoices();
		assertEquals("Did not get all answer choices",86l,qbAnsChoices.size());
	}
	

	/**
	 * Test update qb answer choice.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testUpdateQbAnswerChoice() throws AViewException 
	{
		String _choiceText = "we3445" ; 
		QbAnswerChoice qbAns = QbAnswerChoiceHelper.getQbAnswersChoices(2l) .get(0);
		qbAns.setChoiceText(_choiceText) ;
		//qbAns.setChoiceTextHash("sda22424@@$%%"+System.currentTimeMillis()) ;
		//qbAns.setQbQuestion(QbQuestionHelper.getQbQuestion(52l)) ;
		QbAnswerChoiceHelper.updateQbAnswerChoice(qbAns,44l);
		
		QbAnswerChoice updatedQbAnsChoice = QbAnswerChoiceHelper.getQbAnswersChoices(2l).get(0) ;
		assertEquals("Did not update answer choice",_choiceText ,updatedQbAnsChoice.getChoiceText()) ;
	}

	/**
	 * Test get qb answers choices.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetQbAnswersChoices() throws AViewException
	{
		Long qbQuestionId = 9l;
		List<QbAnswerChoice> qbAnsChoices = QbAnswerChoiceHelper.getQbAnswersChoices(qbQuestionId);
		
		QbAnswerChoice updatedQbAnsChoice = new QbAnswerChoice() ;
		
		for(int i = 0 ; i < qbAnsChoices.size() ; i++)
		{			
			if(qbQuestionId.equals(qbAnsChoices.get(i).getQbQuestion().getQbQuestionId()))
					{
						updatedQbAnsChoice = (QbAnswerChoice)qbAnsChoices.get(i) ;
						break ; 
					}
		}
				
		assertEquals("Didnot get all answer choices", qbQuestionId, updatedQbAnsChoice.getQbQuestion().getQbQuestionId()) ;
	}

}
