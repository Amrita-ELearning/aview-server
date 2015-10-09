/*
 * 
 */
package edu.amrita.aview.common.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.amrita.aview.common.Constant;




/**
 * The Class Status.
 */
@Entity
@Table(name = "status")
public class Status {
	
	/** The status id. */
	private Integer statusId = 0 ;
	
	/** The status name. */
	private String statusName = null ;
	
	/** The created by user id. */
	private Long createdByUserId = 0l;
	
	/** The modified by user id. */
	private Long modifiedByUserId = 0l;
	
	/** The created date. */
	private Timestamp createdDate = null;
	
	/** The modified date. */
	private Timestamp modifiedDate = null;

	/**
	 * Gets the status id.
	 *
	 * @return the status_id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "status_id")
	public Integer getStatusId() {
		return statusId;
	}

	/**
	 * Sets the status id.
	 *
	 * @param statusId the status_id to set
	 */
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	/**
	 * Gets the status name.
	 *
	 * @return the staus_name
	 */
	@Column(name = "status_name")
	public String getStatusName() {
		return statusName;
	}

	/**
	 * Sets the status name.
	 *
	 * @param statusName the staus_name to set
	 */
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.statusId);
		sb.append(Constant.DELIMETER);
		sb.append(this.statusName);
		sb.append(Constant.DELIMETER);
		sb.append(this.createdByUserId);
		sb.append(Constant.DELIMETER);
		sb.append(this.modifiedByUserId);
		sb.append(Constant.DELIMETER);
		sb.append(this.createdDate);
		sb.append(Constant.DELIMETER);
		sb.append(this.modifiedDate);
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
				+ ((statusName == null) ? 0 : statusName.hashCode());
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
		if (!(obj instanceof Status))
			return false;
		final Status other = (Status) obj;
		if (statusName == null) {
			if (other.statusName != null)
				return false;
		} else if (!statusName.equals(other.statusName))
			return false;
		return true;
	}

}
