/*
 * @(#)QbDifficultyLevelHelperTest.java 4.0 2013/10/16
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
import edu.amrita.aview.evaluation.entities.QbDifficultyLevel;



/**
 * This class tests the methods of QbDifficultyLevelHelper.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QbDifficultyLevelHelperTest {

	/** The _creator id. */
	private static Long _creatorId = 0l  ;
	
	/** The _updater id. */
	private static Long _updaterId  = 0l ;
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		_creatorId = 44l ;
		_updaterId = 44l ;
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
		_creatorId = 0l ;
		_updaterId = 0l ;
	}

	/**
	 * Test get all active difficulty levels.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetAllActiveDifficultyLevels() throws AViewException 
	{
		List<QbDifficultyLevel> qbDiffLevel = QbDifficultyLevelHelper.getAllActiveDifficultyLevels() ;
		
		assertEquals("Didnot get all difficulty levels", 2l, qbDiffLevel.size()) ;
	}

	/**
	 * Test get difficulty level.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetDifficultyLevel() throws AViewException 
	{
		Long qbDiffLevelId = 2l ;
		
		QbDifficultyLevel qbDiffLevel = QbDifficultyLevelHelper.getDifficultyLevelById(qbDiffLevelId) ;
		
		assertEquals("Didnot get the difficulty level", qbDiffLevelId, qbDiffLevel.getQbDifficultyLevelId()) ;
	}

	/**
	 * Test create difficulty level.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testCreateDifficultyLevel() throws AViewException 
	{
		String _qbDiffLevelName = "TestQBDiff"+System.currentTimeMillis() ;
		QbDifficultyLevel qbDiffLevel = new QbDifficultyLevel() ;
		qbDiffLevel.setQbDifficultyLevelName(_qbDiffLevelName) ;
		QbDifficultyLevelHelper.createDifficultyLevel(qbDiffLevel, _creatorId) ;
		
		List<QbDifficultyLevel> qbDiffLevels = QbDifficultyLevelHelper.getAllActiveDifficultyLevels() ;
		
		QbDifficultyLevel createdQbDiffLevel = new QbDifficultyLevel() ;
		for(int i = 0 ; i < qbDiffLevels.size() ; i++)
		{
			if(_qbDiffLevelName.equals(qbDiffLevels.get(i).getQbDifficultyLevelName()))
			{
				createdQbDiffLevel = (QbDifficultyLevel)qbDiffLevels.get(i) ;
				break ;
			}
		}
		
		assertEquals("Didnot create the difficulty level", _qbDiffLevelName, createdQbDiffLevel.getQbDifficultyLevelName()) ;
	}

	/**
	 * Test update difficulty level.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testUpdateDifficultyLevel() throws AViewException 
	{
		String _qbDiffLevelName = "hello Test"+System.currentTimeMillis() ;
		QbDifficultyLevel qbDiffLevel = QbDifficultyLevelHelper.getDifficultyLevelById(3l) ;
		
		qbDiffLevel.setQbDifficultyLevelName(_qbDiffLevelName) ;
		QbDifficultyLevelHelper.updateDifficultyLevel(qbDiffLevel, _updaterId) ;
		
		List<QbDifficultyLevel> qbDiffLevels = QbDifficultyLevelHelper.getAllActiveDifficultyLevels() ;
		
		QbDifficultyLevel updatedQbDiffLevel = new QbDifficultyLevel() ;
		for(int i = 0 ; i < qbDiffLevels.size() ; i++)
		{
			if(_qbDiffLevelName.equals(qbDiffLevels.get(i).getQbDifficultyLevelName()))
			{
				updatedQbDiffLevel = (QbDifficultyLevel)qbDiffLevels.get(i) ;
				break ;
			}
		}
		
		assertEquals("Didnot update the difficulty level", _qbDiffLevelName, updatedQbDiffLevel.getQbDifficultyLevelName()) ;
		
	}

}
