/*
 * 
 */
package edu.amrita.aview.gclm.helpers;

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
import edu.amrita.aview.gclm.entities.Class;
import edu.amrita.aview.gclm.entities.ClassRegistration;
import edu.amrita.aview.gclm.entities.User;



/**
 * The Class ClassRegistrationHelperTest.
 */
public class ClassRegistrationHelperTest {

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
	 * Test get active classes registers.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetActiveClassesRegisters() throws AViewException {
		
		List<ClassRegistration> clr = ClassRegistrationHelper.getActiveClassesRegisters();
		assertEquals("Did not get all ClassRegistration",411,clr.size());
	}

	/**
	 * Test create class registration.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testCreateClassRegistration() throws AViewException {

		ClassRegistration clr = new ClassRegistration();
		User user =  UserHelper.getUser(554l);
		Class aviewClass = ClassHelper.getClass(14l);
		clr.setIsModerator("N");
		clr.setAviewClass(aviewClass);
		clr.setUser(user);
		clr.setEnable2DSharing("Y");
		clr.setEnable3DSharing("Y");
		clr.setEnableAudioVideo("Y");
		clr.setEnableDesktopSharing("Y");
		clr.setEnableDocumentSharing("Y");
		clr.setEnableVideoSharing("Y");
		clr.setNodeTypeId(NodeTypeHelper.getClassroomNodeType());
		ClassRegistrationHelper.createClassRegistration(clr,44l,StatusHelper.getActiveStatusId());
		List<ClassRegistration> clrLst = 
			ClassRegistrationHelper.searchForClassRegister(
					user.getUserName(),user.getFname(),user.getLname(), aviewClass.getClassId(), clr.getIsModerator(),aviewClass.getCourseId(),null);
		assertEquals("Did not create CreateClassRegistration",128l,clrLst.size());

	}

	/**
	 * Test update class registration.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testUpdateClassRegistration() throws AViewException {
		Integer nodeTypeId = 2;
		Long clrId=522l;
		User user =  UserHelper.getUser(591l);
		Class aviewClass = ClassHelper.getClass(77l);
		List<ClassRegistration> clrLst = 
			ClassRegistrationHelper.searchForClassRegister(
					user.getUserName(),user.getFname(),user.getLname(), aviewClass.getClassId(), "Y",null,null);
		int i=0;
		ClassRegistration clr = null;
		for(i=0;i<=clrLst.size();i++)
		{
			if(clrId.equals(clrLst.get(i).getClassRegisterId()))
			{
				clr = (ClassRegistration)(clrLst.get(i));
				break;
			}
		}

		clr.setNodeTypeId(nodeTypeId);
		ClassRegistrationHelper.updateClassRegistration(clr,44l);
		List<ClassRegistration> updatedClrLst = ClassRegistrationHelper.searchForClassRegister(
				user.getUserName(),user.getFname(),user.getLname(), aviewClass.getClassId(), "Y",null,null);
		int j=0;
		ClassRegistration updatedClr = null;
		for(j=0;j<=updatedClrLst.size();j++)
		{
			if(clrId.equals(clrLst.get(i).getClassRegisterId()))
			{
				updatedClr = (ClassRegistration)(updatedClrLst.get(i));
				break;
			}
		}
		
		assertEquals("Did not Update CreateClassRegistration",updatedClr.getNodeTypeId(),clr.getNodeTypeId());
	}

	/**
	 * Test delete class register.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testDeleteClassRegister() throws AViewException {

		Long clrId = 2l;
		User user =  UserHelper.getUser(594l);
		Class aviewClass = ClassHelper.getClass(58l);

		List<ClassRegistration> clrLst = ClassRegistrationHelper.searchForClassRegister(
				user.getUserName(),user.getFname(),user.getLname(), aviewClass.getClassId(), "Y",null,null);
		int i=0;
		ClassRegistration clr = new ClassRegistration();
		for(i=0;i<=clrLst.size();i++)
		{
			if(clrId.equals(clrLst.get(i).getClassRegisterId()))
			{
				clr = (ClassRegistration)(clrLst.get(i));
				break;
			}
		}
		assertTrue("User must be active first",(clr.getStatusId() == StatusHelper.getActiveStatusId()));
		UserHelper.deleteUser(clrId, 44l);
		List<ClassRegistration> updatedClrLst = 
			ClassRegistrationHelper.searchForClassRegister(
					user.getUserName(),user.getFname(),user.getLname(), aviewClass.getClassId(), "Y",null,null);
		int j=0;
		ClassRegistration updatedClr = new ClassRegistration();
		for(j=0;j<=updatedClrLst.size();j++)
		{
			if(clrId.equals(clrLst.get(i).getClassRegisterId()))
			{
				updatedClr = (ClassRegistration)(updatedClrLst.get(i));
				break;
			}
		}
		assertTrue("User is not deleted",(updatedClr.getStatusId() == StatusHelper.getDeletedStatusId()));
	}

	/**
	 * Test search for class register.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testSearchForClassRegister() throws AViewException{
		
		List<ClassRegistration> clr = ClassRegistrationHelper.searchForClassRegister(null, null, null, null, null,1l,null,null);
		assertEquals("Did not get all ClassRegistration",411,clr.size());
	
	}

	/**
	 * Test get class registers for master admin approval.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetClassRegistersForMasterAdminApproval() throws AViewException{

		List<ClassRegistration> clr = ClassRegistrationHelper.getClassRegistersForMasterAdminApproval(null);
		assertEquals("Did not get all ClassRegistration",423,clr.size());

	}

	/**
	 * Test get class registers for institute admin approval.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetClassRegistersForInstituteAdminApproval() throws AViewException{

		List<ClassRegistration> clr = ClassRegistrationHelper.getClassRegistersForInstituteAdminApproval(null, 501l);
		assertEquals("Did not get all ClassRegistration",554,clr.size());

	}
	
	/**
	 * Test get class repository folder structure.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetClassRepositoryFolderStructure() throws AViewException{
		String xml = ClassRegistrationHelper.getClassRepositoryFolderStructure(554l);
		System.out.println(xml);
	}
	
	/**
	 * Test get class registers for moderator approval.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetClassRegistersForModeratorApproval() throws AViewException
	{
		List<ClassRegistration> classRegisters = ClassRegistrationHelper.getClassRegistersForModeratorApproval(0l, 4l);
		System.out.println(classRegisters);
	}
	@Test
	public void getClassRegistersForClass()throws AViewException
	{
		List<ClassRegistration> classRegisters = ClassRegistrationHelper.getClassRegistersForClass(new Long(30));
		System.out.println(classRegisters);
	}
	
}
