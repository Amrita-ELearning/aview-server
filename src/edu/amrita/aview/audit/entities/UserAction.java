/*
 * 
 */
package edu.amrita.aview.audit.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;


/**
 * The Class UserAction.
 */
@Entity
@Table(name = "user_action")
public class UserAction extends Auditable {

	/** The user action id. */
	private Long userActionId = 0l;
	
	/** The audit user login id. */
	private Long auditUserLoginId = 0l;
	
	/** The action id. */
	private Integer actionId = 0;
	
	/** The attribute1 value. */
	private String attribute1Value = null;
	
	/** The attribute2 value. */
	private String attribute2Value = null;
	
	/** The attribute3 value. */
	private String attribute3Value = null;
	
	/** The lecture id. */
	private Long lectureId = 0l;
	
	/** The audit lecture id. */
	private Long auditLectureId = 0l;

	//Non Mapped attributes
	/** The action time ms. */
	private Long actionTimeMS = 0L;
	
	/**
	 * Gets the user action id.
	 *
	 * @return the user Action Id
	 */
	@Id
	@Column(name = "user_action_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getUserActionId() {
		return userActionId;
	}

	/**
	 * Sets the user action id.
	 *
	 * @param userActionId the new user action id
	 */
	public void setUserActionId(Long userActionId) {
		this.userActionId = userActionId;
	}

	/**
	 * Gets the audit user login id.
	 *
	 * @return the audit user login id
	 */
	@Column(name = "audit_user_login_id")
	public Long getAuditUserLoginId() {
		return auditUserLoginId;
	}

	/**
	 * Sets the audit user login id.
	 *
	 * @param auditUserLoginId the new audit user login id
	 */
	public void setAuditUserLoginId(Long auditUserLoginId) {
		this.auditUserLoginId = auditUserLoginId;
	}
	
	/**
	 * Gets the action id.
	 *
	 * @return the action id
	 */
	@Column(name = "action_id")
	public Integer getActionId() {
		return actionId;
	}

	/**
	 * Sets the action id.
	 *
	 * @param actionId the action to set
	 */
	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}
	
	/**
	 * Gets the lecture id.
	 *
	 * @return the lecture id
	 */
	@Column(name = "lecture_id")
	public Long getLectureId() {
		return lectureId;
	}
	
	/**
	 * Sets the lecture id.
	 *
	 * @param lectureid the new lecture id
	 */
	public void setLectureId(Long lectureid) {
		this.lectureId = lectureid;
	}
	
	/**
	 * Gets the audit lecture id.
	 *
	 * @return the audit lecture id
	 */
	@Column(name = "audit_lecture_id")
	public Long getAuditLectureId() {
		return auditLectureId;
	}

	/**
	 * Sets the audit lecture id.
	 *
	 * @param auditLectureId the new audit lecture id
	 */
	public void setAuditLectureId(Long auditLectureId) {
		this.auditLectureId = auditLectureId;
	}

	/**
	 * Gets the attribute1 value.
	 *
	 * @return the attribute1 value
	 */
	@Column(name = "attr1_value")
	public String getAttribute1Value() {
		return attribute1Value;
	}

	/**
	 * Sets the attribute1 value.
	 *
	 * @param attribute1Value the new attribute1 value
	 */
	public void setAttribute1Value(String attribute1Value) {
		this.attribute1Value = attribute1Value;
	}

	/**
	 * Gets the attribute2 value.
	 *
	 * @return the attribute2 value
	 */
	@Column(name = "attr2_value")
	public String getAttribute2Value() {
		return attribute2Value;
	}

	/**
	 * Sets the attribute2 value.
	 *
	 * @param attribute2Value the new attribute2 value
	 */
	public void setAttribute2Value(String attribute2Value) {
		this.attribute2Value = attribute2Value;
	}

	/**
	 * Gets the attribute3 value.
	 *
	 * @return the attribute3 value
	 */
	@Column(name = "attr3_value")
	public String getAttribute3Value() {
		return attribute3Value;
	}

	/**
	 * Sets the attribute3 value.
	 *
	 * @param attribute3Value the new attribute3 value
	 */
	public void setAttribute3Value(String attribute3Value) {
		this.attribute3Value = attribute3Value;
	}

	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.userActionId);
		sb.append(Constant.DELIMETER);
		sb.append(this.auditUserLoginId);
		sb.append(Constant.DELIMETER);		
		sb.append(this.actionId);
		sb.append(Constant.DELIMETER);
		sb.append(this.attribute1Value);
		sb.append(Constant.DELIMETER);		
		sb.append(this.attribute2Value);
		sb.append(Constant.DELIMETER);		
		sb.append(this.attribute3Value);
		sb.append(Constant.DELIMETER);		
		sb.append(this.lectureId);
		sb.append(Constant.DELIMETER);		
		sb.append(super.toString());
		sb.append(Constant.DELIMETER);
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((userActionId == 0) ? 0 : (new Long(userActionId)).hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UserAction))
			return false;
		final UserAction other = (UserAction) obj;
		if (userActionId == 0) {
			if (other.userActionId != 0)
				return false;
		} else if (!(userActionId == other.userActionId))
			return false;
		return true;
	}

	/**
	 * Gets the action time ms.
	 *
	 * @return the action time ms
	 */
	@Transient
	public Long getActionTimeMS() {
		return actionTimeMS;
	}

	/**
	 * Sets the action time ms.
	 *
	 * @param actionTimeMS the new action time ms
	 */
	public void setActionTimeMS(Long actionTimeMS) {
		this.actionTimeMS = actionTimeMS;
	}
}
