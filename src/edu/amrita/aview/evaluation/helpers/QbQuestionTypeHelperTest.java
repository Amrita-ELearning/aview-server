/*
 * @(#)QbQuestionTypeHelperTest.java 4.0 2013/10/16
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
import edu.amrita.aview.evaluation.entities.QbQuestionType;



/**
 * This class tests the methods of QbQuestionTypeHelper.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QbQuestionTypeHelperTest 
{

	/** The _qb question type name. */
	private static String _qbQuestionTypeName = null ;
	
	/** The _creator id. */
	private static Long _creatorId = 0l ;
	
	/** The _updater id. */
	private static Long _updaterId = 0l ;
	
	/** The _created by user id. */
	private static Long _createdByUserId = 0l ;
	
	/** The _qb question type id. */
	private static Long _qbQuestionTypeId = 0l ;
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		_qbQuestionTypeName = "Test Question Type "+System.currentTimeMillis() ;
		_creatorId = 44l ;
		_updaterId = 44l ;
		_createdByUserId = 44l ;
		_qbQuestionTypeId = 2l ;
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
	public void tearDown() throws Exception 
	{
		_qbQuestionTypeName = null ;
		_creatorId = 0l ;
		_updaterId = 0l ;
		_createdByUserId = 0l ;
		_qbQuestionTypeId = 0l ;
	}

	/**
	 * Test get all active qb question types.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetAllActiveQbQuestionTypes() throws AViewException 
	{
		
		List<QbQuestionType> qbQuestionTypes = QbQuestionTypeHelper.getAllActiveQbQuestionTypes() ;
		
		QbQuestionType qbQuestionType = new QbQuestionType() ;
		
		for(int i = 0 ; i < qbQuestionTypes.size() ; i++)
		{
			if(_qbQuestionTypeId.equals(qbQuestionTypes.get(i).getQbQuestionTypeId()))
			{
				qbQuestionType = (QbQuestionType)qbQuestionTypes.get(i) ;
				break ;
			}
		}
		
		assertEquals("Didnot get all the question types", _qbQuestionTypeId, qbQuestionType.getQbQuestionTypeId()) ;
		
	}

	/**
	 * Test get qb question type.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetQbQuestionType() throws AViewException 
	{
		QbQuestionType qbQuestionType = QbQuestionTypeHelper.getQbQuestionType(_qbQuestionTypeId) ;
		
		assertTrue("Didnot get the question type", (_qbQuestionTypeId.equals(qbQuestionType.getQbQuestionTypeId()))) ;
	}

	/**
	 * Test create qb question type.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testCreateQbQuestionType() throws AViewException
	{
		_qbQuestionTypeName += "For Creation" ;
		QbQuestionType qbQuest = new QbQuestionType() ;
		qbQuest.setQbQuestionTypeName(_qbQuestionTypeName) ;
		QbQuestionTypeHelper.createQbQuestionType(qbQuest, _creatorId) ;
		
		
		List<QbQuestionType> qbQuestTypes = QbQuestionTypeHelper.getAllActiveQbQuestionTypes() ;
		QbQuestionType createdQbQuestionType = new QbQuestionType() ;
		
		for(int i = 0 ; i < qbQuestTypes.size() ; i++)
		{
			if(_qbQuestionTypeName.equals(qbQuestTypes.get(i).getQbQuestionTypeName()))
			{
				createdQbQuestionType = (QbQuestionType)qbQuestTypes.get(i) ;
				break ;
			}
		}
		
		assertTrue("Didnot create the question type", (_qbQuestionTypeName.equals(createdQbQuestionType.getQbQuestionTypeName()))) ;
	}

	/**
	 * Test update qb question type.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testUpdateQbQuestionType() throws AViewException 
	{
		_qbQuestionTypeId = 4l ;
		_qbQuestionTypeName += "For Updation" ;
		QbQuestionType qbQuestType = QbQuestionTypeHelper.getQbQuestionType(_qbQuestionTypeId) ;
		qbQuestType.setQbQuestionTypeName(_qbQuestionTypeName);
		QbQuestionTypeHelper.updateQbQuestionType(qbQuestType, _updaterId) ;
		
		List<QbQuestionType> qbQuestTypes = QbQuestionTypeHelper.getAllActiveQbQuestionTypes() ;
		QbQuestionType updatedQbQuestionType = new QbQuestionType() ;
		
		for(int i = 0 ; i < qbQuestTypes.size() ; i++)
		{
			if(_qbQuestionTypeName.equals(qbQuestTypes.get(i).getQbQuestionTypeName()))
			{
				updatedQbQuestionType = (QbQuestionType)qbQuestTypes.get(i) ;
				break ;
			}
		}
		
		assertTrue("Didnot update the question type", (_qbQuestionTypeName.equals(updatedQbQuestionType.getQbQuestionTypeName()))) ;
		
	}

}
