/*
 * 
 */
package edu.amrita.aview.feedback.helpers;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.feedback.entities.Feedback;
import edu.amrita.aview.feedback.entities.FeedbackIssue;


/**
 * The Class FeedbackHelperTest.
 */
public class FeedbackHelperTest {

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
	 * Test create feedback.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testCreateFeedback() throws AViewException
	{
		Feedback fb = new Feedback();
		fb.setAntiVirusName("Dummy AV");
		fb.setAuditUserLoginId(18617l);
		fb.setAuditLectureId(0l);
		fb.setOverallRating(2);
		fb.setAudioRating(2);
		fb.setVideoRating(2);
		fb.setInteractionRating(2);
		fb.setWhiteboardRating(3);
		fb.setUserId(18617l);
		FeedbackIssue fbi = null;
		FeedbackHelper.createFeedback(fb, 18617l);
	}
}
