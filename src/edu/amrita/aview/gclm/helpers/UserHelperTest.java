/*
 * 
 */
package edu.amrita.aview.gclm.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.gclm.entities.InstituteAdminUser;
import edu.amrita.aview.gclm.entities.User;


/**
 * The Class UserHelperTest.
 */
public class UserHelperTest {

	/** The name. */
	private static String name = null;
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		name = "Test:"+System.currentTimeMillis();

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
	 * Test create user.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testCreateUser() throws AViewException {
		User user = new User();
		user.setInstituteId(32l);
		user.setUserName(name);
		user.setPassword("amma");
		user.setRole("TEACHER");
		user.setFname(name);
		user.setLname(name);
		user.setCity("Kollam");
		user.setMobileNumber("1234567890");
		user.setEmail("test@gmail.com");

		UserHelper.createUser(user,44l,StatusHelper.getActiveStatusId());

		User createdUser = UserHelper.getUserByUserName(user.getUserName());
		assertEquals("Did not create User",name,createdUser.getUserName());

	}

	/**
	 * Test update user.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testUpdateUser() throws AViewException {

		String emailId = name+"@gmail.com";
		User user = UserHelper.getUser(50l);
		user.setEmail(emailId);

		UserHelper.updateUser(user, 44l);

		User createdUser = UserHelper.getUser(user.getUserId());
		assertTrue("User not updated:",(emailId.equals(createdUser.getEmail()) && user.getUserId().equals(createdUser.getUserId())));

	}

	/**
	 * Test update user change pass.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testUpdateUserChangePass() throws AViewException {

		String newPswd = name;
		User user = UserHelper.getUser(50l);
		UserHelper.updateUserChangePass(newPswd, user.getUserId(),44l);

		User createdUser = UserHelper.getUser(user.getUserId());
		assertTrue("Password not updated:",(name.equals(createdUser.getPassword())));
	}

	/**
	 * Test get active users.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetActiveUsers() throws AViewException {
		List<User> users = UserHelper.getActiveUsers();
		assertEquals("Did not get all users",413,users.size());


	}

	/**
	 * Test delete user.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testDeleteUser() throws AViewException {

		Long userId = 45l;
		User user = UserHelper.getUser(userId);
		assertTrue("User must be active first",(user.getStatusId() == StatusHelper.getActiveStatusId()));
		UserHelper.deleteUser(userId, 44l);
		user = UserHelper.getUser(userId);
		assertTrue("User is not deleted",(user.getStatusId() == StatusHelper.getDeletedStatusId()));
	}

	/**
	 * Test get user.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetUser() throws AViewException {

		Long userId = 150l;
		User user = UserHelper.getUser(userId);
		assertNotNull(user);
		assertEquals("Did not have user",userId,user.getUserId());

	}
	
	/**
	 * Test get users by role.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetUsersByRole() throws AViewException {

		List<User> users = UserHelper.getUsersByRole(Constant.ADMIN_ROLE);
		assertEquals("Did not get all users",2,users.size());

	}

	/**
	 * Test search active users.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testSearchActiveUsers() throws AViewException {

		List<User> users = UserHelper.searchActiveUsers(null, null, null, null, null, null, null ,null,"1234567890",0l,1l);
		assertEquals("Did not get all users",2,users.size());

	}

	/**
	 * Test search pending users.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testSearchPendingUsers() throws AViewException {

		List<User> users = UserHelper.searchPendingUsers(null, null, null, null, null, null, 0l,null,null, null,null);
		assertEquals("Did not get all users",2,users.size());

	}
	@Test
	public void testSearchUsersByName() throws AViewException {

		List<User> users = UserHelper.searchUsersByName("test");
		assertEquals("Did not get all users",2,users.size());
		

	}
	/**
	 * Creates the admin user.
	 *
	 * @param instId the inst id
	 * @return the institute admin user
	 * @throws AViewException
	 */
	private InstituteAdminUser createAdminUser(Long instId) throws AViewException
	{
		InstituteAdminUser iau = new InstituteAdminUser();
		iau.setInstitute(InstituteHelper.getInstituteById(instId));
		return iau;
	}

	@Test
	public void testResetPassword() throws AViewException {
		UserHelper.resetPassword("student1", "a@a.com") ;
	}
}
