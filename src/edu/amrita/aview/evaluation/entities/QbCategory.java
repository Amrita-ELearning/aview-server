/*
 * @(#)QbCategory.java 4.0 2013/09/17
 * 
 * Copyright  © 2013 E-Learning Research Lab, 
 * Amrita Vishwa Vidyapeetham. All rights reserved. 
 * E-Learning Research Lab and the A-VIEW logo are trademarks or
 * registered trademarks of E-Learning Research Lab. 
 * All other names used are the trademarks of their respective owners.
 * 
 */
package edu.amrita.aview.evaluation.entities;

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
 * This is an entity class.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
@Entity
@Table(name = "qb_category")
public class QbCategory extends Auditable{

	/** The qb category id. */
	private Long qbCategoryId = 0l ;
	
	/** The qb category name. */
	private String qbCategoryName = null ;
	
	/** The institute id. */
	private Long instituteId = 0l ;
	
	// Non mapped attributes
	/** The created by user name. */
	private String createdByUserName = null ;
	
	/** The modified by user name. */
	private String modifiedByUserName = null ;
	
	/** The total questions. */
	private int totalQuestions = 0 ;
	
	/**
	 * Gets the qb category id.
	 *
	 * @return the qbCategoryId
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "qb_category_id")
	public Long getQbCategoryId() {
		return qbCategoryId;
	}
	
	/**
	 * Sets the qb category id.
	 *
	 * @param qbCategoryId the qbCategoryId to set
	 */
	public void setQbCategoryId(Long qbCategoryId) {
		this.qbCategoryId = qbCategoryId;
	}
	
	/**
	 * Gets the qb category name.
	 *
	 * @return the qbCategoryName
	 */
	@Column(name = "qb_category_name")
	public String getQbCategoryName() {
		return qbCategoryName;
	}
	
	/**
	 * Sets the qb category name.
	 *
	 * @param qbCategoryName the qbCategoryName to set
	 */
	public void setQbCategoryName(String qbCategoryName) {
		this.qbCategoryName = qbCategoryName;
	}
	
	/**
	 * Gets the institute id.
	 *
	 * @return the instituteId
	 */
	@Column(name = "institute_id")
	public Long getInstituteId() {
		return instituteId;
	}
	
	/**
	 * Sets the institute id.
	 *
	 * @param instituteId the instituteId to set
	 */
	public void setInstituteId(Long instituteId) {
		this.instituteId = instituteId;
	}
	
	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.qbCategoryId);
		sb.append(Constant.DELIMETER);
		sb.append(this.qbCategoryName);
		sb.append(Constant.DELIMETER);	
		sb.append(super.toString());	
		return sb.toString() ;
	}
	
    /* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((qbCategoryName == null) ? 0 : qbCategoryName.hashCode());
		result = prime * result
				+ ((getCreatedByUserId() == null) ? 0 : getCreatedByUserId().hashCode());
		return result;
	}
    /* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		QbCategory other = (QbCategory) obj;
		if (qbCategoryName == null) {
			if (other.qbCategoryName != null) {
				return false;
			}
		} else if (!qbCategoryName.equals(other.qbCategoryName)) {
			return false;
		}
		if (getCreatedByUserId() == null) {
			if (other.getCreatedByUserId() != null) {
				return false;
			}
		} else if (!getCreatedByUserId().equals(other.getCreatedByUserId())) {
			return false;
		}
		return true;
	}
    
    /**
     * Gets the created by user name.
     *
     * @return the created by user name
     */
    @Transient
	public String getCreatedByUserName() {
		return createdByUserName;
	}
	
	/**
	 * Sets the created by user name.
	 *
	 * @param createdByUserName the new created by user name
	 */
	public void setCreatedByUserName(String createdByUserName) {
		this.createdByUserName = createdByUserName;
	}
	
	/**
	 * Gets the modified by user name.
	 *
	 * @return the modified by user name
	 */
	@Transient
	public String getModifiedByUserName() {
		return modifiedByUserName;
	}
	
	/**
	 * Sets the modified by user name.
	 *
	 * @param modifiedByUserName the new modified by user name
	 */
	public void setModifiedByUserName(String modifiedByUserName) {
		this.modifiedByUserName = modifiedByUserName;
	}
	
	/**
	 * Gets the total questions.
	 *
	 * @return the total questions
	 */
	@Transient
	public int getTotalQuestions() {
		return totalQuestions;
	}
	
	/**
	 * Sets the total questions.
	 *
	 * @param totalQuestions the new total questions
	 */
	public void setTotalQuestions(int totalQuestions) {
		this.totalQuestions = totalQuestions;
	}
	
	
}
