/*
 * @(#)QbCategoryHelperTest.java 4.0 2013/10/16
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
import edu.amrita.aview.evaluation.entities.QbCategory;



/**
 * This class tests the methods of QbCategoryHelper.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QbCategoryHelperTest {

	/** The _qb category name. */
	private static String _qbCategoryName = null ; 
	
	/** The _modified by user id. */
	private static Long _modifiedByUserId = 0l; 
	
	/** The _updater id. */
	private static Long _updaterId = 0l ; 
	
	/** The _creator id. */
	private static Long _creatorId = 0l ; 

	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		_qbCategoryName = "Maths - I"+System.currentTimeMillis() ; 
		_modifiedByUserId = 44l ; 
		_updaterId = 44l ; 
		_creatorId = 44l ; 
		
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
		_qbCategoryName = null ; 
		_modifiedByUserId = 0l; 
		_updaterId = 0l ; 
		_creatorId = 0l ;
		
	}

	/**
	 * Test get all active qb categories.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetAllActiveQbCategories() throws AViewException 
	{
		List<QbCategory> qbCategories = QbCategoryHelper.getAllActiveQbCategories() ;
		
		assertEquals("Didnot get all categories", 12l, qbCategories.size()) ;
	}

	/**
	 * Test get qb category.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetQbCategory() throws AViewException 
	{
		Long _qbCategoryId = 2l ;
		QbCategory qbCat = QbCategoryHelper.getQbCategoryById(_qbCategoryId) ;
		
		assertTrue("Didnot get the category", (qbCat.getQbCategoryId().equals(_qbCategoryId))) ;
	}

	/**
	 * Test create qb category.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testCreateQbCategory() throws AViewException 
	{
		QbCategory qbCat = new QbCategory() ;
		qbCat.setQbCategoryName(_qbCategoryName) ;
		Long catId = 1l;
		List<QbCategory> qbCategories = QbCategoryHelper.getAllActiveQbCategories() ;		 
		
		QbCategory createdQbCategory = new QbCategory() ;
		for(int i = 0 ; i < qbCategories.size() ; i++)
		{
			if(_qbCategoryName.equals(qbCategories.get(i).getQbCategoryName()))
			{
				createdQbCategory = (QbCategory)qbCategories.get(i) ;
				break ;
			}
		}
		
		assertTrue("Couldnot create the category",_qbCategoryName.equals(createdQbCategory.getQbCategoryName())) ;
	}

	/**
	 * Test update qb category.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testUpdateQbCategory() throws AViewException 
	{		
		QbCategory qbCat = QbCategoryHelper.getQbCategoryById(2l) ;
		String _newQbCatName = "Updated " + qbCat.getQbCategoryName().substring(0, qbCat.getQbCategoryName().indexOf("Updated", 0)) + System.currentTimeMillis() ;
		qbCat.setQbCategoryName(_newQbCatName) ;
		
		QbCategoryHelper.updateQbCategory(qbCat, _updaterId) ;
		
		List<QbCategory> qbCategories = QbCategoryHelper.getAllActiveQbCategories() ;		 
		QbCategory updatedQbCategory = new QbCategory() ;
		for(int i = 0 ; i < qbCategories.size() ; i++)
		{
			if(_newQbCatName.equals(qbCategories.get(i).getQbCategoryName()))
			{
				updatedQbCategory = (QbCategory)qbCategories.get(i) ;
				break ;
			}
		}
		
		assertEquals("Didnot update the category", _newQbCatName, updatedQbCategory.getQbCategoryName()) ;
	}

	/**
	 * Test delete qb category.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testDeleteQbCategory() throws AViewException 
	{		
		Long _qbCategoryId = 16l ;
		QbCategory qbCat = QbCategoryHelper.getQbCategoryById(_qbCategoryId) ;
		assertTrue("Course must be active first",(qbCat.getStatusId() == StatusHelper.getActiveStatusId()));
		QbCategoryHelper.deleteQbCategory(_qbCategoryId, _modifiedByUserId) ;		
		qbCat = QbCategoryHelper.getQbCategoryById(_qbCategoryId) ;
		
		assertTrue("Didnot delete the category", (qbCat.getStatusId() == StatusHelper.getDeletedStatusId()));
	}

	/**
	 * Test get all active qb categories for user.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetAllActiveQbCategoriesForUser() throws AViewException 
	{
		Long _userId = 554l ;
		Long _newUserId = 0l; 
		List<QbCategory> qbCategories = QbCategoryHelper.getAllActiveQbCategoriesForUser(_userId) ;
		
		for(int i = 0 ; i < qbCategories.size() ; i++)
		{
			Object temp = (Object) qbCategories.get(i) ;
			Object[] t = (Object[]) temp ;
			System.out.println(t[0]);
			if(_userId.equals(Long.parseLong(t[2].toString())))
			{
				_newUserId = Long.parseLong(t[2].toString()) ;				
				break ;
			}
		}
		
		assertEquals("Didnot get all categories", _userId,_newUserId) ;
	}

}
