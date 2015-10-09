/*
 * 
 */
package edu.amrita.aview.gclm.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.gclm.entities.Institute;
import edu.amrita.aview.gclm.entities.InstituteAdminUser;
import edu.amrita.aview.gclm.entities.InstituteServer;
import edu.amrita.aview.gclm.entities.Server;


/**
 * The Class InstituteHelperTest.
 */
public class InstituteHelperTest {

	/** The logger. */
	private static Logger logger = Logger.getLogger(InstituteHelperTest.class);

	/** The name. */
	private static String name = null;
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		name = "Amrita :"+System.currentTimeMillis();
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
	 * Test create institute.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testCreateInstitute() throws AViewException{
		Institute inst = new Institute();
		inst.setAddress("Amritapuri");
		inst.setCity("Amritapuri");
		inst.setDistrictId(355);
		inst.setInstituteCategoryId(3);
		inst.setInstituteName(name);
		inst.setInstituteType("College");
		inst.setParentInstituteId(342l);
		inst.setMaxPublishingBandwidthKbps(128);
		inst.setMinPublishingBandwidthKbps(56);
		
		inst.addInstituteAdminUser(createAdminUser(43l));
		inst.addInstituteAdminUser(createAdminUser(590l));
		
		inst.addInstituteServer(createServer(2,3));
		inst.addInstituteServer(createServer(2,8));
//		inst.addInstituteServer(createServer(2,11));
//		inst.addInstituteServer(createServer(3,12));
//		inst.addInstituteServer(createServer(3,13));
//		
		InstituteHelper.createInstitute(inst, 44l);
		
		Institute createdInst = InstituteHelper.getInstituteByName(name);
		assertEquals("Did not create institute",name,createdInst.getInstituteName());
		assertEquals("Did not create institute admins",inst.getinstituteAdminUsers().size(),createdInst.getinstituteAdminUsers().size());
		assertEquals("Did not create institute servers",inst.getInstituteServers().size(),createdInst.getInstituteServers().size());
	}

	/**
	 * Test update institute.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testUpdateInstitute() throws AViewException{
		
		String updated = ":Changed"; 
		
		Institute inst = InstituteHelper.getInstituteByName(name);
//		inst.setInstituteName(inst.getInstituteName()+updated);
		inst.setAddress(inst.getAddress()+updated);
		inst.setStatusId(StatusHelper.getActiveStatusId());
		
		inst.addInstituteAdminUser(createAdminUser(2l));

		Iterator iau = inst.getInstituteServers().iterator();
		while(iau.hasNext())
		{
			Object o = iau.next();
			iau.remove();
			break;
		}
		inst.addInstituteServer(createServer(2,11));
		inst.addInstituteServer(createServer(3,12));
//		inst.addInstituteServer(createServer(3,13));
		
		for(InstituteServer instituteServer:inst.getInstituteServers())
		{
			logger.debug("HashCode:"+instituteServer.hashCode()+":"+instituteServer.toString());
		}
		
		InstituteHelper.updateInstitute(inst, 44l);
		
		Institute newInst = InstituteHelper.getInstituteById(inst.getInstituteId()); 
		assertTrue("Institue not updated:",(inst.getAddress().equals(newInst.getAddress()) && inst.getInstituteName().equals(newInst.getInstituteName())));
	}
	
	/**
	 * Test update institute delete admin user.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testUpdateInstituteDeleteAdminUser() throws AViewException{
		
		String updated = ":ChangedDeleteInstituteAdminUser"; 
		
		Institute inst = InstituteHelper.getInstituteByName(name); 
			//InstituteHelper.getInstituteById(1222l);
		inst.setInstituteName(inst.getInstituteName()+updated);
		inst.setAddress(inst.getAddress()+updated);
		inst.setStatusId(StatusHelper.getActiveStatusId());
		
		Iterator iau = inst.getinstituteAdminUsers().iterator();
		while(iau.hasNext())
		{
			Object o = iau.next();
			iau.remove();
			break;
		}
		
		
		for(InstituteServer instituteServer:inst.getInstituteServers())
		{
			logger.debug("HashCode:"+instituteServer.hashCode()+":"+instituteServer.toString());
		}
		
		InstituteHelper.updateInstitute(inst, 44l);
		
		Institute newInst = InstituteHelper.getInstituteById(inst.getInstituteId()); 
		assertTrue("Institue not updated:",(inst.getAddress().equals(newInst.getAddress()) && inst.getInstituteName().equals(newInst.getInstituteName())));
	}

	/**
	 * Test delete institute.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testDeleteInstitute() throws AViewException{
		//Long instId = 1202l;
		//Institute inst = InstituteHelper.getInstituteById(instId);
		Institute inst = InstituteHelper.getInstituteByName(name);
		assertTrue("Institute must be active first",(inst.getStatusId() == StatusHelper.getActiveStatusId()));
		InstituteHelper.deleteInstitute(inst.getInstituteId(), 45l);
		inst = InstituteHelper.getInstituteById(inst.getInstituteId());
		assertTrue("Institute is not deleted",(inst.getStatusId() == StatusHelper.getDeletedStatusId()));
	}

	/**
	 * Test activate institute.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testActivateInstitute() throws AViewException{
		Long instId = 1202l;
		Institute inst = InstituteHelper.getInstituteById(instId);
		assertTrue("Institute must be deleted first",(inst.getStatusId() == StatusHelper.getDeletedStatusId()));
		InstituteHelper.activateInstitute(instId, 44l);
		inst = InstituteHelper.getInstituteById(instId);
		assertTrue("Institute is not activated",(inst.getStatusId() == StatusHelper.getActiveStatusId()));
	}

	/**
	 * Test get all institutes.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetAllInstitutes() throws AViewException{
		List<Institute> institutes = InstituteHelper.getAllInstitutes();
		assertEquals("Did not get all institutes",1150,institutes.size());
	}

	/**
	 * Test get institute by id.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetInstituteById() throws AViewException{
		Long id = 7l;
		Institute inst = InstituteHelper.getInstituteById(id);
		assertNotNull(inst);
		assertEquals("Did not institute",id,inst.getInstituteId());
	}

	/**
	 * Test get all parent institutes.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetAllParentInstitutes() throws AViewException{
		List<Institute> institutes = InstituteHelper.getAllParentInstitutes();
		assertEquals("Did not get all parent institutes",1132,institutes.size());
		
	}
	
	/**
	 * Test get all institutes for admin.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetAllInstitutesForAdmin() throws AViewException{
		List<Institute> institutes = InstituteHelper.getAllInstitutesForAdmin(44l);
		assertEquals("Did not get all institutes for Admin",12,institutes.size());
		
	}
	
	/**
	 * Test remove admin users.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testRemoveAdminUsers() throws AViewException
	{
		Institute inst = InstituteHelper.getInstituteById(7l);
		inst.getinstituteAdminUsers().clear();
		InstituteHelper.updateInstitute(inst, 44l);
		inst = InstituteHelper.getInstituteById(7L);
		assertEquals("Did not remove all users",0,inst.getinstituteAdminUsers().size());
	}

	/**
	 * Test add admin users.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testAddAdminUsers() throws AViewException
	{
		Institute inst = InstituteHelper.getInstituteById(342L);
		int adminUsers = inst.getinstituteAdminUsers().size();
		inst.addInstituteAdminUser(createAdminUser(1l));
		inst.addInstituteAdminUser(createAdminUser(2l));
		InstituteHelper.updateInstitute(inst, 44l);
		inst = InstituteHelper.getInstituteById(7L);
		assertEquals("Did not add new userss",adminUsers+2,inst.getinstituteAdminUsers().size());
	}
	
	/**
	 * Test add remove admin users.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testAddRemoveAdminUsers() throws AViewException
	{
		Institute inst = InstituteHelper.getInstituteById(8L);
		int adminUsers = inst.getinstituteAdminUsers().size();
		assertTrue("Institute must have some admin users inititally",(adminUsers>0));
		inst.getinstituteAdminUsers().clear();
		inst.addInstituteAdminUser(createAdminUser(43l));
		inst.addInstituteAdminUser(createAdminUser(45l));
		InstituteHelper.updateInstitute(inst, 44l);
		inst = InstituteHelper.getInstituteById(7L);
		assertEquals("Did not add new userss",2,inst.getinstituteAdminUsers().size());
	}

	/**
	 * Creates the admin user.
	 *
	 * @param userID the user id
	 * @return the institute admin user
	 * @throws AViewException
	 */
	private InstituteAdminUser createAdminUser(Long userID) throws AViewException
	{
		InstituteAdminUser iau = new InstituteAdminUser();
		iau.setUser(UserHelper.getUser(userID));
		return iau;
	}
	
	/**
	 * Creates the server.
	 *
	 * @param serverId the server id
	 * @param serverTypeId the server type id
	 * @return the institute server
	 * @throws AViewException
	 */
	private InstituteServer createServer(Integer serverId,Integer serverTypeId) throws AViewException
	{
		InstituteServer instituteServer = new InstituteServer();
		Server server =  ServerHelper.getServer(serverId);
		instituteServer.setServer(server);
		instituteServer.setServerTypeId(serverTypeId);
		return instituteServer;
	}
	
}
