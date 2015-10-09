/*
 * 
 */
package edu.amrita.aview.common.helpers;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.entities.State;


/**
 * The Class StateHelperTest.
 */
public class StateHelperTest {

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
	 * Test get states.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetStates() throws AViewException{

		List<State> states = StateHelper.getStates();
		assertEquals("Did not get all States",35,states.size());

	}

	/**
	 * Test get state.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetState() throws AViewException{

		Integer stateId = 1;
		State state = StateHelper.getState(stateId);
		assertEquals("Did not have State",stateId,state.getStateId());
	}

}
