/*
 * 
 */
package edu.amrita.aview.common.entities;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.apache.log4j.Logger;

import edu.amrita.aview.audit.helpers.ActionHelper;
import edu.amrita.aview.audit.helpers.UserActionHelper;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.helpers.DistrictHelper;
import edu.amrita.aview.common.helpers.StateHelper;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.helpers.SystemParameterHelper;
import edu.amrita.aview.evaluation.helpers.QbAnswerChoiceHelper;
import edu.amrita.aview.evaluation.helpers.QbCategoryHelper;
import edu.amrita.aview.evaluation.helpers.QbDifficultyLevelHelper;
import edu.amrita.aview.evaluation.helpers.QbQuestionHelper;
import edu.amrita.aview.evaluation.helpers.QuizHelper;
import edu.amrita.aview.evaluation.helpers.QuizQuestionHelper;
import edu.amrita.aview.feedback.helpers.ModuleHelper;
import edu.amrita.aview.gclm.helpers.BrandingAttributeHelper;
import edu.amrita.aview.gclm.helpers.ClassHelper;
import edu.amrita.aview.gclm.helpers.CourseHelper;
import edu.amrita.aview.gclm.helpers.InstituteCategoryHelper;
import edu.amrita.aview.gclm.helpers.InstituteHelper;
import edu.amrita.aview.gclm.helpers.NodeTypeHelper;
import edu.amrita.aview.gclm.helpers.ServerHelper;
import edu.amrita.aview.gclm.helpers.ServerTypeHelper;
import edu.amrita.aview.common.utils.SimpleEmailSenderUtils;


/**
 * The Class Auditable.
 */
@MappedSuperclass
public class Auditable {

	/** The logger. */
	protected Logger logger = Logger.getLogger(Auditable.class);

	/** The created by user id. */
	private Long createdByUserId = 0l;

	/** The modified by user id. */
	private Long modifiedByUserId = 0l;

	/** The created date. */
	private Timestamp createdDate = null;

	/** The modified date. */
	private Timestamp modifiedDate = null;

	/** The status id. */
	private Integer statusId = 0;

	/**
	 * Gets the created by user id.
	 *
	 * @return the created by user id
	 */
	@Column(name = "created_by_user_id")
	public Long getCreatedByUserId() {
		return createdByUserId;
	}

	/**
	 * Sets the created by user id.
	 *
	 * @param createdByUserId the new created by user id
	 */
	public void setCreatedByUserId(Long createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	/**
	 * Gets the modified by user id.
	 *
	 * @return the modified by user id
	 */
	@Column(name = "modified_by_user_id")
	public Long getModifiedByUserId() {
		return modifiedByUserId;
	}

	/**
	 * Sets the modified by user id.
	 *
	 * @param modifiedByUserId the new modified by user id
	 */
	public void setModifiedByUserId(Long modifiedByUserId) {
		this.modifiedByUserId = modifiedByUserId;
	}

	/**
	 * Gets the created date.
	 *
	 * @return the created date
	 */
	@Column(name = "created_date")
	public Timestamp getCreatedDate() {
		return createdDate;
	}

	/**
	 * Sets the created date.
	 *
	 * @param createdDate the new created date
	 */
	public void setCreatedDate(Timestamp createdDate) {		
		this.createdDate = createdDate;
	}

	/**
	 * Gets the modified date.
	 *
	 * @return the modified date
	 */
	@Column(name = "modified_date")
	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * Sets the modified date.
	 *
	 * @param modifiedDate the new modified date
	 */
	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * Gets the status id.
	 *
	 * @return the status id
	 */
	@Column(name = "status_id")
	public Integer getStatusId() {
		return statusId;
	}

	/**
	 * Sets the status id.
	 *
	 * @param statusId the new status id
	 */
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.createdByUserId);
		sb.append(Constant.DELIMETER);
		sb.append(this.modifiedByUserId);
		sb.append(Constant.DELIMETER);
		sb.append(this.createdDate);
		sb.append(Constant.DELIMETER);
		sb.append(this.modifiedDate);
		sb.append(Constant.DELIMETER);
		sb.append(this.statusId);
		sb.append(Constant.DELIMETER);
		return sb.toString();
	}

	/**
	 * Creates the from.
	 *
	 * @param auditable the auditable
	 */
	protected void createFrom(Auditable auditable)
	{
		this.createdByUserId = auditable.createdByUserId;
		this.modifiedByUserId = auditable.modifiedByUserId;
		this.statusId = auditable.statusId;
		this.createdDate = auditable.createdDate;
		this.modifiedDate = auditable.modifiedDate;
	}

	/**
	 * Sets the created aduit data.
	 *
	 * @param createdUserId the created user id
	 * @param createdDate the created date
	 * @param statusId the status id
	 */
	public void setCreatedAuditData(Long createdUserId,Timestamp createdDate,Integer statusId)
	{
		this.createdByUserId = createdUserId;
		this.createdDate = createdDate;
		//Set same data as created
		this.modifiedByUserId = createdUserId;
		this.modifiedDate = createdDate; 
		this.statusId = statusId;
	}

	/**
	 * Sets the modified audit data.
	 *
	 * @param modifiedUserId the modified user id
	 * @param modifedDate the modifed date
	 * @throws AViewException
	 */
	public void setModifiedAuditData(Long modifiedUserId,Timestamp modifedDate) throws AViewException
	{
		this.modifiedByUserId = modifiedUserId;
		this.modifiedDate = modifedDate; 
	}

	/**
	 * Merge associations.
	 *
	 * @param currentAssociations the current associations
	 * @param newAssociations the new associations
	 */
	protected void mergeAssociations(Set currentAssociations,Set newAssociations)
	{
		logger.debug("Current Associations:"+currentAssociations.size()+":"+currentAssociations.getClass().getName());
		for(Object assoc:currentAssociations)
		{
			logger.debug(assoc.hashCode()+":"+assoc.toString());
		}
		logger.debug("New Associations:"+newAssociations.size()+":"+newAssociations.getClass().getName());
		for(Object assoc:newAssociations)
		{
			logger.debug(assoc.hashCode()+":"+assoc.toString());
		}
		//		
		//		
		//		//Add new associations to current
		//		for(Object newAssociation:newAssociations)
		//		{
		//			boolean contains = false;
		//			for(Object currentAssociation:currentAssociations)
		//			{
		//				if(currentAssociation.equals(newAssociation))
		//				{
		//					contains = true;
		//					break;
		//				}
		//			}
		//			
		//			if(!contains)
		//			{
		//				currentAssociations.add(newAssociation);
		//			}
		//		}

		//Fix for Bug # 3054, 3055 start
		//Remove non existing associations from current
		/*
		Iterator currentAssociationsIter = currentAssociations.iterator();
		while(currentAssociationsIter.hasNext())
		{
			Object currentAssociation = currentAssociationsIter.next();
			if(!newAssociations.contains(currentAssociation))
			{
				currentAssociationsIter.remove();
			}
		}	

		for(Object newAssociation:newAssociations)
		{
			if(!currentAssociations.contains(newAssociation))
			{
				currentAssociations.add(newAssociation);
			}
		}
		 */

		currentAssociations.retainAll(newAssociations);

		logger.debug("Current Associations Step 1:"+currentAssociations.size()+":"+currentAssociations.getClass().getName());
		for(Object assoc:currentAssociations)
		{
			logger.debug(assoc.hashCode()+":"+assoc.toString());
		}
		logger.debug("New Associations:"+newAssociations.size()+":"+newAssociations.getClass().getName());
		for(Object assoc:newAssociations)
		{
			logger.debug(assoc.hashCode()+":"+assoc.toString());
		}

		newAssociations.removeAll(currentAssociations);

		logger.debug("Current Associations Step 2:"+currentAssociations.size()+":"+currentAssociations.getClass().getName());
		for(Object assoc:currentAssociations)
		{
			logger.debug(assoc.hashCode()+":"+assoc.toString());
		}
		logger.debug("New Associations:"+newAssociations.size()+":"+newAssociations.getClass().getName());
		for(Object assoc:newAssociations)
		{
			logger.debug(assoc.hashCode()+":"+assoc.toString());
		}

		currentAssociations.addAll(newAssociations);
		//Fix for Bug # 3054, 3055 end
		logger.debug("Current Associations Step 3:"+currentAssociations.size()+":"+currentAssociations.getClass().getName());
		for(Object assoc:currentAssociations)
		{
			logger.debug(assoc.hashCode()+":"+assoc.toString());
		}
		logger.debug("New Associations:"+newAssociations.size()+":"+newAssociations.getClass().getName());
		for(Object assoc:newAssociations)
		{
			logger.debug(assoc.hashCode()+":"+assoc.toString());
		}

	}

	/**
	 * To get all the initial level data required for running 
	 * avc client. Since most of the values are stored in the
	 * cache, the response time is much faster 
	 */
	public static void getAllInitialData()
	{
		StatusHelper.getStatuses();
		InstituteHelper.getAllInstitutes();
		InstituteCategoryHelper.getInstituteCategories();

		ActionHelper.getActions();

		DistrictHelper.getDistricts();
		StateHelper.getStates();

		ModuleHelper.getModules();

		NodeTypeHelper.getAllNodeTypes();
		ServerTypeHelper.getAllServerTypes();
		ServerHelper.getServers();

		QbCategoryHelper.getAllActiveQbCategories();
		QbDifficultyLevelHelper.getAllActiveDifficultyLevels();		
	}


	/**
	 * To set for garbage collection of all static variables used as cache.
	 */
	public static void cleanup()
	{
		InstituteHelper.clearCache();
		ClassHelper.clearCache();
		CourseHelper.clearCache();
		DistrictHelper.clearCache();
		ModuleHelper.clearCache();
		QbAnswerChoiceHelper.clearCache();
		QbCategoryHelper.clearCache();
		StatusHelper.clearCache();
		StateHelper.clearCache();
		QuizQuestionHelper.clearCache();
		QuizHelper.clearCache();
		QbQuestionHelper.clearCache();
		QbDifficultyLevelHelper.clearCache();
		ServerHelper.clearCache();
		InstituteCategoryHelper.clearCache();
		ServerTypeHelper.clearCache();
		NodeTypeHelper.clearCache();
		SystemParameterHelper.clearCache();
		BrandingAttributeHelper.clearCache();
		try
		{
			UserActionHelper.stopThread();
			SimpleEmailSenderUtils.stopThread();
		}
		catch(Exception e)
		{
			// some error
		}	
	}
}
