/*
 * 
 */
package edu.amrita.aview.gclm.helpers;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.gclm.entities.InstituteAdminUser;


/**
 * The Class InstituteAdminUserHelperTest.
 */
public class InstituteAdminUserHelperTest {

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
	 * Test create institute admin user.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testCreateInstituteAdminUser() throws AViewException{
		
		InstituteAdminUser instituteAdminUser = new InstituteAdminUser();
		instituteAdminUser.setInstitute(InstituteHelper.getInstituteById(1001l));
		instituteAdminUser.setUser(UserHelper.getUser(150L));
		InstituteAdminUserHelper.createInstituteAdminUser(instituteAdminUser,44l);
		List<InstituteAdminUser> createdInstituteAdminUser = InstituteAdminUserHelper.getInstituteAdminUsers();
		assertEquals("Did not create institute admins",27,createdInstituteAdminUser.size());

	}

	/**
	 * Test update institute admin user.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testUpdateInstituteAdminUser() throws AViewException {
		
		InstituteAdminUser instituteAdminUser = new InstituteAdminUser();
		instituteAdminUser =  InstituteAdminUserHelper.getInstituteAdminUser(2l);
		instituteAdminUser.setUser(UserHelper.getUser(150L));		
		InstituteAdminUserHelper.updateInstituteAdminUser(instituteAdminUser, 44l);
		InstituteAdminUser newInstituteAdminUser = InstituteAdminUserHelper.getInstituteAdminUser(2l); 
		
		assertEquals("InstituteAdminUser not updated:",instituteAdminUser.getUser().getUserId(),newInstituteAdminUser.getUser().getUserId());
	}

	/**
	 * Test get institute admin users.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetInstituteAdminUsers() throws AViewException{
		
		List<InstituteAdminUser> instituteAdminUsers = InstituteAdminUserHelper.getInstituteAdminUsers();
		assertEquals("Did not get all InstituteAdminUsers",27,instituteAdminUsers.size());
		
	}

	/**
	 * Test get institute admin user.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetInstituteAdminUser() throws AViewException{
		
		Long id = 47l;
		InstituteAdminUser instituteAdminUser = InstituteAdminUserHelper.getInstituteAdminUser(id);
		assertEquals("Did not InstituteAdminUsers",id,instituteAdminUser.getInstituteAdminUserId());
		
	}

}
