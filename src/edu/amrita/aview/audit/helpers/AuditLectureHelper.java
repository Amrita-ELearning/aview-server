/*
 * 
 */
package edu.amrita.aview.audit.helpers;



import java.sql.Timestamp;
import java.util.List;

import edu.amrita.aview.audit.daos.AuditLectureDAO;
import edu.amrita.aview.audit.entities.AuditLecture;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.TimestampUtils;





// TODO: Auto-generated Javadoc
/**
 * The Class AuditLectureHelper.
 */
public class AuditLectureHelper {
	
	/**
	 * Creates the audit lecture.
	 *
	 * @param auditLecture the audit lecture
	 * @param creatorId the creator id
	 * @return the audit lecture
	 * @throws AViewException the a view exception
	 */
	public static AuditLecture createAuditLecture(AuditLecture auditLecture,Long creatorId) throws AViewException{
		auditLecture.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
		AuditLectureDAO.createLectureSetting(auditLecture);
		return auditLecture;
	}
	
	/**
	 * Gets the audit lecture by id.
	 *
	 * @param auditLectureId the audit lecture id
	 * @return the audit lecture by id
	 * @throws AViewException the a view exception
	 */
	public static AuditLecture getAuditLectureById(Long auditLectureId) throws AViewException{

		AuditLecture auditLecture = AuditLectureDAO.getAuditLectureById(auditLectureId);
		return auditLecture;
	}
	
	/**
	 * Update audit lecture.
	 *
	 * @param auditLecture the audit lecture
	 * @param updatorId the updator id
	 * @return the audit lecture
	 * @throws AViewException the a view exception
	 */
	public static AuditLecture updateAuditLecture(AuditLecture auditLecture,Long updatorId) throws AViewException{
		AuditLecture dbAuditLecture = getAuditLectureById(auditLecture.getAuditLectureId());
		dbAuditLecture.setModifiedAuditData(updatorId, TimestampUtils.getCurrentTimestamp());
		AuditLectureDAO.updateLectureSetting(dbAuditLecture);
		return dbAuditLecture;
	}

	/**
	 * Update audit lecture by id.
	 *
	 * @param auditLectureId the audit lecture id
	 * @param updatorId the updator id
	 * @return the audit lecture
	 * @throws AViewException the a view exception
	 */
	public static AuditLecture updateAuditLectureById(Long auditLectureId,Long updatorId) throws AViewException{
		AuditLecture dbAuditLecture = getAuditLectureById(auditLectureId);
		dbAuditLecture.setModifiedAuditData(updatorId, TimestampUtils.getCurrentTimestamp());
		AuditLectureDAO.updateLectureSetting(dbAuditLecture);
		return dbAuditLecture;
	}
	
	/**
	 * Update last action date.
	 *
	 * @param auditLectureId the audit lecture id
	 * @param lastActionDate the last action date
	 * @throws AViewException the a view exception
	 */
	public static void updateLastActionDate(Long auditLectureId,Timestamp lastActionDate) throws AViewException{
		AuditLecture dbAuditLecture = getAuditLectureById(auditLectureId);
		dbAuditLecture.setLastActionDate(lastActionDate);
		AuditLectureDAO.updateLectureSetting(dbAuditLecture);
	}

	/**
	 * Gets the all currently logged in guest users.
	 *
	 * @param lectureId the lecture id
	 * @return the all currently logged in guest users
	 * @throws AViewException the a view exception
	 */
	public static List<Long> getAllCurrentlyLoggedInGuestUsers(Long lectureId) throws AViewException
	{
		return AuditLectureDAO.getAllCurrentlyLoggedInGuestUsers(lectureId);
	}
	
	/**
	 * @param lectureId the lecture id
	 * @return
	 * @throws AViewException
	 */
	//Fix for Bug#20297,20288
	public static Boolean getAuditLectureByLectureId(Long lectureId) throws AViewException
	{
		return AuditLectureDAO.getAuditLectureByLectureId(lectureId);
	}

}
