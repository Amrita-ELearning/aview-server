/*
 * 
 */
package edu.amrita.aview.feedback.helpers;

import org.apache.log4j.Logger;

import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.MathUtils;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.feedback.daos.FeedbackDAO;
import edu.amrita.aview.feedback.entities.Feedback;
import edu.amrita.aview.feedback.entities.FeedbackIssue;


/**
 * The Class FeedbackHelper.
 */
public class FeedbackHelper {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(FeedbackHelper.class);
	
	/**
	 * Creates the feedback.
	 *
	 * @param feedback the feedback
	 * @param creatorId the creator id
	 * @return the feedback
	 */
	public static Feedback createFeedback(Feedback feedback,Long creatorId){
		try {
			logger.info("Entered createFeedback with feedback "+feedback+", and creatorId"+creatorId);
			//Set nulls
			if(feedback.getAuditLectureId() == 0l)
			{
				feedback.setAuditLectureId(null);
			}
			
			//Round the BandwidthBarsAvg
			feedback.setBandwidthRating(MathUtils.round(feedback.getBandwidthRating(),2));
			feedback.setLeftMM(MathUtils.round(feedback.getLeftMM(),3));
			feedback.setMemoryUsedMB(MathUtils.round(feedback.getMemoryUsedMB(),3));
			feedback.setGivenMM(MathUtils.round(feedback.getGivenMM(),3));
			
			feedback.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
			for(FeedbackIssue issue:feedback.getFeedbackIssues())
			{
				issue.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
			}
			
			FeedbackDAO.createFeedback(feedback);
			logger.info("Successfully created feedback "+feedback+", with creatorId"+creatorId);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			logger.error("Error while creating feedback "+feedback+", with creatorId"+creatorId, e);
			return null;
		}
		return feedback;
	}

}
