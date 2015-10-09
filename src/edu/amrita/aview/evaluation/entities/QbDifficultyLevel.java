/*
 * @(#)QbDifficultyLevel.java 4.0 2013/09/17
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
@Table(name = "qb_difficulty_level")
public class QbDifficultyLevel extends Auditable{

	/** The qb difficulty level id. */
	private Long qbDifficultyLevelId = 0l ;
	
	/** The qb difficulty level name. */
	private String qbDifficultyLevelName = null ;
	
	/**
	 * Gets the qb difficulty level id.
	 *
	 * @return the qbDifficultyLevelId
	 */
	@Id
	@Column(name = "qb_difficulty_level_id")
	public Long getQbDifficultyLevelId() {
		return qbDifficultyLevelId;
	}
	
	/**
	 * Sets the qb difficulty level id.
	 *
	 * @param qbDifficultyLevelId the qbDifficultyLevelId to set
	 */
	public void setQbDifficultyLevelId(Long qbDifficultyLevelId) {
		this.qbDifficultyLevelId = qbDifficultyLevelId;
	}
	
	/**
	 * Gets the qb difficulty level name.
	 *
	 * @return the qbDifficultyLevelName
	 */
	@Column(name = "qb_difficulty_level_name")
	public String getQbDifficultyLevelName() {
		return qbDifficultyLevelName;
	}
	
	/**
	 * Sets the qb difficulty level name.
	 *
	 * @param qbDifficultyLevelName the qbDifficultyLevelName to set
	 */
	public void setQbDifficultyLevelName(String qbDifficultyLevelName) {
		this.qbDifficultyLevelName = qbDifficultyLevelName;
	}
	
	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder() ;
		sb.append(this.qbDifficultyLevelId);
		sb.append(Constant.DELIMETER);
		sb.append(this.qbDifficultyLevelName);
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
        result = prime
                * result
                + ((qbDifficultyLevelName == null) ? 0 : qbDifficultyLevelName
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
        final QbDifficultyLevel other = (QbDifficultyLevel) obj;
        if (qbDifficultyLevelName == null) {
            if (other.qbDifficultyLevelName != null)
                return false;
        } else if (!qbDifficultyLevelName.equals(other.qbDifficultyLevelName))
            return false;
        return true;
    }
}
