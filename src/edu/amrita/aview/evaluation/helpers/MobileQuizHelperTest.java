/**
 * 
 */
package edu.amrita.aview.evaluation.helpers;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.javacodegeeks.kannel.api.SMSPushRequestException;


/**
 * The Class MobileQuizHelperTest.
 *
 * @author amma
 */
public class MobileQuizHelperTest {

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
	 * Test method for {@link edu.amrita.aview.evaluation.helpers.MobileQuizHelper#createMobileQuizQuestion(java.lang.Long, java.util.List)}.
	 *
	 * @throws SMSPushRequestException the sMS push request exception
	 * @throws Exception the exception
	 */
	@Ignore
	public void testCreateMobileQuizQuestion() throws SMSPushRequestException, Exception
	{
		ArrayList<String> MobileNo = new ArrayList<String>();
		MobileNo.add("9633638483");
		MobileQuizHelper.createMobileQuizQuestion(35l, MobileNo);

	}

	/**
	 * Test method for {@link edu.amrita.aview.evaluation.helpers.MobileQuizHelper#getMobileQuizAnswerChoice(java.lang.String, java.lang.String)}.
	 *
	 * @throws SMSPushRequestException the sMS push request exception
	 * @throws Exception the exception
	 */
	@Test
	public void testGetMobileQuizAnswerChoice() throws SMSPushRequestException, Exception 
	{
		MobileQuizHelper.getMobileQuizAnswerChoice("35#", "+919633638483");

	}

}
