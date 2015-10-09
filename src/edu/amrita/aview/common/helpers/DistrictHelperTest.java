/*
 * 
 */
package edu.amrita.aview.common.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.entities.District;



/**
 * The Class DistrictHelperTest.
 */
public class DistrictHelperTest {

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
	 * Test get districts.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetDistricts() throws AViewException{
	
		List<District> districts = DistrictHelper.getDistricts();
		assertEquals("Did not get all districts",628,districts.size());

	}

	/**
	 * Test get district.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetDistrict() throws AViewException{

		Integer distId = 150;
		District district = DistrictHelper.getDistrict(distId);
		assertNotNull(district);
		assertEquals("Did not have district",distId,district.getDistrictId());

	}

	/**
	 * Test get district by state id.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetDistrictByStateId() throws AViewException{

		Integer stateId = 2;
		List<District> district = DistrictHelper.getDistrictByStateId(stateId);
		assertEquals("Did not get all districts",23,district.size());

	}

}
