/*
 * 
 */
package edu.amrita.aview.contacts.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.amrita.aview.contacts.entities.ContactGroup;
import edu.amrita.aview.gclm.entities.User;
import edu.amrita.aview.gclm.helpers.UserHelper;


/**
 * The Class ContactGroupHelperTest.
 */
public class ContactGroupHelperTest {

	/** The group name. */
	private static String groupName = null ;
	
	/** The user id. */
	private static Long userId = 0l ;
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		groupName = "Test "+System.currentTimeMillis() ;
		userId = 15l ;
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
	 * Test create group.
	 */
	@Test
	public void testCreateGroup() {
		ContactGroup gr = new ContactGroup() ;
		
		gr.setContactGroupName("grp1") ;
		gr.setGroupOwnerId(new Long(165));
		ContactGroup createdGroup = new ContactGroup() ;
		
		ContactGroupHelper.createGroup(gr, 44l) ;
		System.out.println(gr.getContactGroupId());
//		List<ContactGroup> groups = ContactGroupHelper.getAllGroups() ;\
//		
//		for(int i = 0 ; i < groups.size() ; i++)
//		{
//			if(groupName.equals(groups.get(i).getContactGroupName()))
//			{
//				createdGroup = groups.get(i) ;
//				break ;
//			}
//				
//		}
		
		assertEquals("Didnot create the group", createdGroup.getContactGroupName()) ;
		
	}

	/**
	 * Test update group.
	 */
	@Test
	public void testUpdateGroup() {
		fail("Not yet implemented");
	}

	/**
	 * Test delete group.
	 */
	@Test
	public void testDeleteGroup() {
		fail("Not yet implemented");
	}

	/**
	 * Test get group.
	 */
	@Test
	public void testGetGroup() {
		fail("Not yet implemented");
	}

	/**
	 * Test get groups by owner.
	 */
	@Test
	public void testGetGroupsByOwner() {
		fail("Not yet implemented");
	}

	/**
	 * Test get all groups.
	 */
	@Test
	public void testGetAllGroups() {
		fail("Not yet implemented");
	}

}
