/*
 * 
 */
package edu.amrita.aview.contacts.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.contacts.entities.GroupUser;


/**
 * The Class GroupUserHelperTest.
 */
public class GroupUserHelperTest {

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
	 * Test create group user.
	 */
	@Ignore
	public void testCreateGroupUser() {
		fail("Not yet implemented");
	}

	/**
	 * Test update group user.
	 */
	@Ignore
	public void testUpdateGroupUser() {
		fail("Not yet implemented");
	}

	/**
	 * Test delete group user.
	 */
	@Ignore
	public void testDeleteGroupUser() {
		fail("Not yet implemented");
	}

	/**
	 * Test add user to group.
	 */
	@Ignore
	public void testAddUserToGroup() {
		fail("Not yet implemented");
	}

	/**
	 * Test add users to group.
	 */
	@Ignore
	public void testAddUsersToGroup() {
		fail("Not yet implemented");
	}

	/**
	 * Test delete user from group.
	 */
	@Ignore
	public void testDeleteUserFromGroup() {
		fail("Not yet implemented");
	}

	/**
	 * Test get group user.
	 */
	@Ignore
	public void testGetGroupUser() {
		fail("Not yet implemented");
	}

	/**
	 * Test get users from group.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetUsersFromGroup() throws AViewException{
		List<GroupUser> grLst = GroupUserHelper.getUsersFromGroup(3l) ;
		
		assertEquals("Didnot get the users", 4l, grLst.size()) ;
	}

	/**
	 * Test delete users from group.
	 */
	@Ignore
	public void testDeleteUsersFromGroup() {
		fail("Not yet implemented");
	}

}
