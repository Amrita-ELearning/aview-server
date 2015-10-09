/*
 * 
 */
package edu.amrita.aview.gclm.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;
import java.util.Date;
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
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.gclm.entities.Institute;
import edu.amrita.aview.gclm.entities.InstituteAdminUser;
import edu.amrita.aview.gclm.entities.InstituteServer;
import edu.amrita.aview.gclm.entities.PeopleCount;
import edu.amrita.aview.gclm.entities.Server;


/**
 * The Class InstituteHelperTest.
 */
public class PeopleCountHelperTest {

	/** The logger. */
	private static Logger logger = Logger.getLogger(PeopleCountHelperTest.class);

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
	public void testCreatePeopleCount() throws AViewException{
		PeopleCount peopleCount = new PeopleCount();
		peopleCount.setLectureId(347l);
		peopleCount.setPeopleCountData("hai");
		peopleCount.setPeopleCountTimestamp(TimestampUtils.getCurrentTimestamp());
		PeopleCountHelper.createPeopleCount(peopleCount, 1l);
		
	}
}
