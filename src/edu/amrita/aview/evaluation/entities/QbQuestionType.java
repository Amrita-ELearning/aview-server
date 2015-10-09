/*
 * @(#)QbQuestionType.java 4.0 2013/09/17
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
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name = "qb_question_type")
public class QbQuestionType extends Auditable{

	/** The qb question type id. */
	private Long qbQuestionTypeId = 0l;
	
	/** The qb question type name. */
	private String qbQuestionTypeName = null ;
	
	/**
	 * Gets the qb question type id.
	 *
	 * @return the qbQuestionTypeId
	 */
	@Id
	@Column(name = "qb_question_type_id")
	public Long getQbQuestionTypeId() {
		return qbQuestionTypeId;
	}
	
	/**
	 * Sets the qb question type id.
	 *
	 * @param qbQuestionTypeId the qbQuestionTypeId to set
	 */
	public void setQbQuestionTypeId(Long qbQuestionTypeId) {
		this.qbQuestionTypeId = qbQuestionTypeId;
	}
	
	/**
	 * Gets the qb question type name.
	 *
	 * @return the qbQuestionTypeName
	 */
	@Column(name = "qb_question_type_name")
	public String getQbQuestionTypeName() {
		return qbQuestionTypeName;
	}
	
	/**
	 * Sets the qb question type name.
	 *
	 * @param qbQuestionTypeName the qbQuestionTypeName to set
	 */
	public void setQbQuestionTypeName(String qbQuestionTypeName) {
		this.qbQuestionTypeName = qbQuestionTypeName;
	}
	
	
	/* (non-Javadoc)
	 * @see edu.amrita.aview.common.entities.Auditable#toString()
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(this.qbQuestionTypeId);
		sb.append(Constant.DELIMETER);
		sb.append(this.qbQuestionTypeName);
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
        result = prime
                * result
                + ((qbQuestionTypeName == null) ? 0 : qbQuestionTypeName
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
        final QbQuestionType other = (QbQuestionType) obj;
        if (qbQuestionTypeName == null) {
            if (other.qbQuestionTypeName != null)
                return false;
        } else if (!qbQuestionTypeName.equals(other.qbQuestionTypeName))
            return false;
        return true;
    }
	
	
	
}
