/*
 * 
 */
package edu.amrita.aview.feedback.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import edu.amrita.aview.common.entities.Auditable;


/**
 * The Class FeedbackIssue.
 */
@Entity
@Table(name = "feedback_issue")
public class FeedbackIssue extends Auditable {
	
	/** The feedback issue id. */
	private Long feedbackIssueId = 0l;
	
	/** The feedback. */
	private Feedback feedback = null;
	
	/** The module id. */
	private Integer moduleId = null;
	
	/** The issue title. */
	private String issueTitle = null;
	
	/** The issue description. */
	private String issueDescription = null;
	
//	public void copyFrom(FeedbackIssue issue)
//	{
//		this.feedbackIssueId = issue.feedbackIssueId;
//		this.moduleId = issue.moduleId;
//		this.issueTitle = issue.issueTitle;
//		this.issueDescription = issue.issueDescription;
//	}
	
	/**
 * Gets the feedback issue id.
 *
 * @return the feedback issue id
 */
@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "feedback_issue_id")
	public Long getFeedbackIssueId() {
		return feedbackIssueId;
	}
	
	/**
	 * Sets the feedback issue id.
	 *
	 * @param feedbackIssueId the feedback issue id
	 */
	public void setFeedbackIssueId(Long feedbackIssueId) {
		this.feedbackIssueId = feedbackIssueId;
	}
	
	/**
	 * Gets the feedback.
	 *
	 * @return the feedback
	 */
	@ManyToOne
	@JoinColumn(name="feedback_id", nullable=false)
	public Feedback getFeedback() {
		return feedback;
	}
	
	/**
	 * Sets the feedback.
	 *
	 * @param feedback the feedback
	 */
	public void setFeedback(Feedback feedback) {
		this.feedback = feedback;
	}
	
	/**
	 * Gets the issue title.
	 *
	 * @return the issue title
	 */
	@Column(name = "issue_title")
	public String getIssueTitle() {
		return issueTitle;
	}
	
	/**
	 * Sets the issue title.
	 *
	 * @param issueTitle the issue title
	 */
	public void setIssueTitle(String issueTitle) {
		this.issueTitle = issueTitle;
	}

	/**
	 * Gets the issue description.
	 *
	 * @return the issue description
	 */
	@Column(name = "issue_description")
	public String getIssueDescription() {
		return issueDescription;
	}
	
	/**
	 * Sets the issue description.
	 *
	 * @param issueDescription the issue description
	 */
	public void setIssueDescription(String issueDescription) {
		this.issueDescription = issueDescription;
	}
	
	/**
	 * Gets the module id.
	 *
	 * @return the module id
	 */
	@Column(name="module_id")
	public Integer getModuleId() {
		return moduleId;
	}
	
	/**
	 * Sets the module id.
	 *
	 * @param moduleId the module id
	 */
	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((feedback == null) ? 0 : feedback.getFeedbackId().hashCode());
		result = prime * result + ((moduleId == null) ? 0 : moduleId.hashCode());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof FeedbackIssue))
			return false;
		FeedbackIssue other = (FeedbackIssue) obj;
		if (feedback == null) {
			if (other.feedback != null)
				return false;
		} else if (!feedback.getFeedbackId().equals(other.feedback.getFeedbackId()))
			return false;
		if (moduleId == null) {
			if (other.moduleId != null)
				return false;
		} else if (!moduleId.equals(other.moduleId))
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see edu.amrita.aview.common.entities.Auditable#toString()
	 */
	@Override
	public String toString() {
		return "FeedbackIssue [feedbackIssueId=" + feedbackIssueId
				+ ", moduleId=" + moduleId
				+ ", issueTitle=" + issueTitle + ", issueDescription="
				+ issueDescription+ "]";
	}

}
