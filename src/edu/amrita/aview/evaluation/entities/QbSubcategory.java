/*
 * QbSubcategory.java 4.0 2013/10/18
 * 
 * Copyright  © 2013 E-Learning Research Lab, 
 * Amrita Vishwa Vidyapeetham. All rights reserved. 
 * E-Learning Research Lab and the A-VIEW logo are trademarks or
 * registered trademarks of E-Learning Research Lab. 
 * All other names used are the trademarks of their respective owners.
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
@Table(name = "qb_subcategory")
public class QbSubcategory extends Auditable 
{
	
	/** The qb subcategory id. */
	private Long qbSubcategoryId = 0l ;
	
	/** The qb subcategory name. */
	private String qbSubcategoryName = null ;
	
	/** The qb category id. */
	private Long qbCategoryId = 0l;
	
	// Non mapped attributes
	/** The qb category name. */
	private String qbCategoryName = null;
	
	/** The created by user name. */
	private String createdByUserName = null;
	
	/** The modified by user name. */
	private String modifiedByUserName = null;
	
	/** The total qns. */
	private int totalQns = 0;
	
	/**
	 * Gets the qb subcategory id.
	 *
	 * @return the qbSubcategoryId
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)		
	@Column(name = "qb_subcategory_id")
	public Long getQbSubcategoryId() {
		return qbSubcategoryId;
	}
	
	/**
	 * Sets the qb subcategory id.
	 *
	 * @param qbSubcategoryId the qbSubcategoryId to set
	 */
	public void setQbSubcategoryId(Long qbSubcategoryId) {
		this.qbSubcategoryId = qbSubcategoryId;
	}
	
	/**
	 * Gets the qb subcategory name.
	 *
	 * @return the qbSubcategoryName
	 */
	@Column(name = "qb_subcategory_name")
	public String getQbSubcategoryName() {
		return qbSubcategoryName;
	}
	
	/**
	 * Sets the qb subcategory name.
	 *
	 * @param qbSubcategoryName the qbSubcategoryName to set
	 */
	public void setQbSubcategoryName(String qbSubcategoryName) 
	{
		this.qbSubcategoryName = qbSubcategoryName;
	}
	
	/**
	 * Gets the qb category id.
	 *
	 * @return the qbCategoryId
	 */
	@Column(name = "qb_category_id")
	public Long getQbCategoryId() 
	{
		return qbCategoryId;
	}
	
	/**
	 * Sets the qb category id.
	 *
	 * @param qbCategoryId the qbCategoryId to set
	 */
	public void setQbCategoryId(Long qbCategoryId) 
	{
		this.qbCategoryId = qbCategoryId;
	}
		
	
	/* (non-Javadoc)
	 * @see edu.amrita.aview.common.entities.Auditable#toString()
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(this.qbSubcategoryId);
		sb.append(Constant.DELIMETER);
		sb.append(this.qbSubcategoryName);
		sb.append(Constant.DELIMETER);
		sb.append(this.qbCategoryId);
		sb.append(Constant.DELIMETER);
		sb.append(super.toString());		
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((qbCategoryId == null) ? 0 : qbCategoryId.hashCode());
		result = prime
				* result
				+ ((qbSubcategoryName == null) ? 0 : qbSubcategoryName
						.hashCode());
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
		if (getClass() != obj.getClass())
			return false;
		QbSubcategory other = (QbSubcategory) obj;
		if (qbCategoryId == null) {
			if (other.qbCategoryId != null)
				return false;
		} else if (!qbCategoryId.equals(other.qbCategoryId))
			return false;
		if (qbSubcategoryName == null) {
			if (other.qbSubcategoryName != null)
				return false;
		} else if (!qbSubcategoryName.equals(other.qbSubcategoryName))
			return false;
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
	 * @return the total qns
	 */
	@Transient
	public int getTotalQns() {
		return totalQns;
	}

	/**
	 * Sets the total questions.
	 *
	 * @param totalQns the new total qns
	 */
	public void setTotalQns(int totalQns) {
		this.totalQns = totalQns;
	}

	/**
	 * Gets the qb category name.
	 *
	 * @return the qb category name
	 */
	@Transient
	public String getQbCategoryName() {
		return qbCategoryName;
	}

	/**
	 * Sets the qb category name.
	 *
	 * @param qbCategoryName the new qb category name
	 */
	public void setQbCategoryName(String qbCategoryName) {
		this.qbCategoryName = qbCategoryName;
	}
	
}
