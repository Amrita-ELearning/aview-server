/*
 * @(#)QbSubcategoryHelperTest.java 4.0 2013/10/16
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
import edu.amrita.aview.evaluation.entities.QbSubcategory;



/**
 * This class tests the methods of QbSubcategoryHelper.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QbSubcategoryHelperTest 
{

	/** The _sub cat id. */
	private static Long _subCatId = 0l ;
	
	/** The _sub cat name. */
	private static String _subCatName = null ;
	
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
		_creatorId = 44l ;
		_updaterId = 44l ;
		_createdByUserId = 44l ; 
		_modifiedByUserId = 44l ;
		_subCatId = 2l ;
		_subCatName = " Atomic Physics "+System.currentTimeMillis() ;
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
		_createdByUserId = 0l ; 
		_modifiedByUserId = 0l ;
		_subCatId = 0l ;
	}

	/**
	 * Test get all active qb subcategories.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetAllActiveQbSubcategories() throws AViewException 
	{
		List<QbSubcategory> qbSubcats = QbSubcategoryHelper.getAllActiveQbSubcategories() ;
		
		assertEquals("Didnot get all subcategories", 9l, qbSubcats.size());
		
	}

	/**
	 * Test get qb subcategory.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetQbSubcategory() throws AViewException 
	{
		QbSubcategory qbSubcat = QbSubcategoryHelper.getQbSubcategoryById(_subCatId) ;
		
		assertTrue("Didnot get the subcategory", (_subCatId.equals(qbSubcat.getQbSubcategoryId()))) ;
	}

	/**
	 * Test create qb subcategory.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testCreateQbSubcategory() throws AViewException 
	{
		Long _qbCategoryId = 1l ;
		_subCatName += " For Create" ;
		QbSubcategory qbSubcat = new QbSubcategory() ;
		qbSubcat.setQbCategoryId(_qbCategoryId);
		qbSubcat.setQbSubcategoryName(_subCatName) ;
		
		QbSubcategoryHelper.createQbSubcategory(qbSubcat, _creatorId) ;
		
		List<QbSubcategory> qbSubcats = QbSubcategoryHelper.getAllActiveQbSubcategories() ;
		
		QbSubcategory createdSubcat = new QbSubcategory() ;
		
		for(int i = 0 ; i < qbSubcats.size() ; i++)
		{
			if(_subCatName.equals(qbSubcats.get(i).getQbSubcategoryName()))
			{
				createdSubcat = (QbSubcategory)qbSubcats.get(i) ;
				break ;
			}
		}
		
		assertTrue("Didnot create subcategory", (_subCatName.equals(createdSubcat.getQbSubcategoryName()))) ;		
		
	}

	/**
	 * Test update qb subcategory.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testUpdateQbSubcategory() throws AViewException
	{
		_subCatId = 12l ;
		_subCatName += " For Updation" ;
		QbSubcategory qbSubcat = QbSubcategoryHelper.getQbSubcategoryById(_subCatId) ;
		qbSubcat.setQbSubcategoryName(_subCatName) ;
		QbSubcategoryHelper.updateQbSubcategory(qbSubcat, _updaterId) ;
		qbSubcat = QbSubcategoryHelper.getQbSubcategoryById(_subCatId) ;
		
		assertTrue("Didnot update subcategory", (_subCatName.equals(qbSubcat.getQbSubcategoryName()))) ;
	}

	/**
	 * Test delete qb subcategory.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testDeleteQbSubcategory() throws AViewException 
	{
		_subCatId = 5l ;
		QbSubcategory qbSubcat = QbSubcategoryHelper.getQbSubcategoryById(_subCatId) ;
		assertTrue("Subcategory must be active ", (qbSubcat.getStatusId() == StatusHelper.getActiveStatusId())) ;
		QbSubcategoryHelper.deleteQbSubcategory(_subCatId, _modifiedByUserId) ;
		qbSubcat = QbSubcategoryHelper.getQbSubcategoryById(_subCatId) ;
		assertTrue("Couldnot delete the subcategory", (qbSubcat.getStatusId() == StatusHelper.getDeletedStatusId())) ;
	}

	/**
	 * Test get all active qb subcategories for user.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetAllActiveQbSubcategoriesForUser() throws AViewException 
	{
		Long _userId = 1l ;
		List<QbSubcategory> qbSubcats = QbSubcategoryHelper.getAllActiveQbSubcategoriesForUser(_userId) ;
		
		assertEquals("Didnot get all subcategories", 1l, qbSubcats.size()) ;
		
	}

	/**
	 * Test get all active qb subcategories summary for category.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetAllActiveQbSubcategoriesSummaryForCategory() throws AViewException 
	{
		List qbs = QbSubcategoryHelper.getAllActiveQbSubcategoriesSummaryForCategory(1l) ;
		
		assertEquals("Didnot get all subcategories", 1, qbs.size()) ;
	}

}
