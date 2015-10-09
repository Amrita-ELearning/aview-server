/*
 * @(#)QbQuestion.java 4.0 2013/09/17
 * 
 * Copyright  © 2013 E-Learning Research Lab, 
 * Amrita Vishwa Vidyapeetham. All rights reserved. 
 * E-Learning Research Lab and the A-VIEW logo are trademarks or
 * registered trademarks of E-Learning Research Lab. 
 * All other names used are the trademarks of their respective owners.
 */
package edu.amrita.aview.evaluation.entities;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;
import edu.amrita.aview.common.helpers.StatusHelper;



/**
 * This is an entity class.
 * 
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
@Entity
@Table(name = "qb_question")
public class QbQuestion extends Auditable{

	/** The qb question id. */
	private Long qbQuestionId = 0l;
	
	/** The qb subcategory id. */
	private Long qbSubcategoryId = 0l;
	
	/** The qb question type id. */
	private Long qbQuestionTypeId = 0l ;
	
	/** The qb difficulty level id. */
	private Long qbDifficultyLevelId = 0l;
	
	/** The question text. */
	private String questionText = null ;
	
	/** The marks. */
	private double marks = 0.0F ;
	
	/** The parent id. */
	private Long parentId = 0l;
	
	/** The question text hash. */
	private String questionTextHash = null ;
	
	// Non mapped attributes
	/** The qb difficulty level name. */
	private String qbDifficultyLevelName = null ;
	
	/** The qb question type name. */
	private String qbQuestionTypeName = null ;
	
	/** The qb answer choices. */
	private Set<QbAnswerChoice> qbAnswerChoices = new HashSet<QbAnswerChoice>();
	
	/** The qb question media files. */
//	private Set<QbQuestionMediaFile> qbQuestionMediaFiles = new HashSet<QbQuestionMediaFile>();
	
	//Fix for bug 10929, 10926, 10922.
	//Update from should not deal with qb anwer choices but handled separately 
	//from QbQuestion helper class directly.
	/**
	 * Update from.
	 *
	 * @param other the other
	 */
	public void updateFrom(QbQuestion other)
    {
        this.qbSubcategoryId=other.qbSubcategoryId;
        this.qbQuestionTypeId=other.qbQuestionTypeId;
        this.qbDifficultyLevelId=other.qbDifficultyLevelId;
        this.questionText=other.questionText;
        this.marks=other.marks;
        this.parentId=other.parentId;
        this.questionTextHash=other.questionTextHash;
        this.setStatusId(other.getStatusId()); 
        Set<QbQuestionMediaFile> tempQbQuestionMediaFile = new HashSet<QbQuestionMediaFile>();
		/*if(this.qbQuestionMediaFiles != null)
		{
			tempQbQuestionMediaFile.addAll(this.qbQuestionMediaFiles);
			this.qbQuestionMediaFiles.clear();
		}	
		Set<QbQuestionMediaFile> tempOtherQbQuestionMediaFile = new HashSet<QbQuestionMediaFile>();
		if(other.qbQuestionMediaFiles != null)
		{
			tempOtherQbQuestionMediaFile.addAll(other.qbQuestionMediaFiles);
		}		
		super.mergeAssociations(tempQbQuestionMediaFile, tempOtherQbQuestionMediaFile);
		
		this.qbQuestionMediaFiles.addAll(tempQbQuestionMediaFile);*/
		
    }
 
	/**
	 * Gets the qb answer choices.
	 *
	 * Mapping of a question with answer choices
	 * @return the qb answer choices
	 */
	@OneToMany(mappedBy ="qbQuestion" , fetch=FetchType.EAGER, cascade={CascadeType.ALL} ,orphanRemoval = true)	
	@BatchSize(size=1000)
	public Set<QbAnswerChoice> getQbAnswerChoices() {
		return qbAnswerChoices;
	}

	/**
	 * Sets the qb answer choices.
	 *
	 * @param qbAnswerChoices the new qb answer choices
	 */
	public void setQbAnswerChoices(Set<QbAnswerChoice> qbAnswerChoices) {
		this.qbAnswerChoices = qbAnswerChoices;
	}
	
	/**
	 * Adds the qb answer choices.
	 *
	 * @param qbAnswerChoice the qbAnswerChoice to set
	 */
     public synchronized void addQbAnswerChoices(QbAnswerChoice qbAnswerChoice)
    {
        if(this.qbAnswerChoices == null)
        {
            this.qbAnswerChoices = new HashSet<QbAnswerChoice>();
        }
        qbAnswerChoice.setQbQuestion(this) ;
        this.qbAnswerChoices.add(qbAnswerChoice);
    }
	
 	/*@OneToMany(mappedBy="qbQuestion",fetch=FetchType.EAGER,cascade={CascadeType.ALL},orphanRemoval=true)
  	@BatchSize(size=1000)
	public Set<QbQuestionMediaFile> getQbQuestionMediaFiles() {
		return qbQuestionMediaFiles;
	}

	public void setQbQuestionMediaFiles(
			Set<QbQuestionMediaFile> qbQuestionMediaFiles) {
		this.qbQuestionMediaFiles = qbQuestionMediaFiles;
	}
	public synchronized void addQbQuestionMediaFiles(QbQuestionMediaFile qbQuestionMediaFile)
	{
		if(this.qbQuestionMediaFiles == null)
		{
			this.qbQuestionMediaFiles = new HashSet<QbQuestionMediaFile>();
		}
		qbQuestionMediaFile.setQbQuestion(this) ;
		this.qbQuestionMediaFiles.add(qbQuestionMediaFile);
	}*/

	/**
	 * Gets the qb question id.
	 *
	 * @return the qbQuestionId
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "qb_question_id")
	public Long getQbQuestionId() {
		return qbQuestionId;
	}
	
	/**
	 * Sets the qb question id.
	 *
	 * @param qbQuestionId the qbQuestionId to set
	 */
	public void setQbQuestionId(Long qbQuestionId) {
		this.qbQuestionId = qbQuestionId;
	}

	/**
	 * Gets the qb subcategory id.
	 *
	 * @return the qbSubcategoryId
	 */
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
	 * Gets the qb question type id.
	 *
	 * @return the qbQuestionTypeId
	 */
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
	 * Gets the qb difficulty level id.
	 *
	 * @return the qbDifficultyLevelId
	 */
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
	 * Gets the question text.
	 *
	 * @return the questionText
	 */
	@Column(name = "question_text")
	public String getQuestionText() {
		return questionText;
	}
	
	/**
	 * Sets the question text.
	 *
	 * @param questionText the questionText to set
	 */
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	
	/**
	 * Gets the marks.
	 *
	 * @return the marks
	 */
	@Column(name = "marks")
	public Double getMarks() {
		return marks;
	}
	
	/**
	 * Sets the marks.
	 *
	 * @param marks the marks to set
	 */
	public void setMarks(Double marks) {
		this.marks = marks;
	}
	
	/**
	 * Gets the parent id.
	 *
	 * @return the parentId
	 */
	@Column(name = "parent_id")
	public Long getParentId() {
		return parentId;
	}
	
	/**
	 * Sets the parent id.
	 *
	 * @param parentId the parentId to set
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	/**
	 * Gets the question text hash.
	 *
	 * @return the questionTextHash
	 */
	@Column(name = "question_text_hash")
	public String getQuestionTextHash() {
		return questionTextHash;
	}
	
	/**
	 * Sets the question text hash.
	 *
	 * @param questionTextHash the questionTextHash to set
	 */
	public void setQuestionTextHash(String questionTextHash) {
		this.questionTextHash = questionTextHash;
	}

	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(this.qbQuestionId);
		sb.append(Constant.DELIMETER);
		sb.append(this.qbSubcategoryId);
		sb.append(Constant.DELIMETER);
		sb.append(this.qbQuestionTypeId);
		sb.append(Constant.DELIMETER);
		sb.append(this.qbDifficultyLevelId);
		sb.append(Constant.DELIMETER);
		sb.append(this.questionText);
		sb.append(Constant.DELIMETER);
		sb.append(this.marks);
		sb.append(Constant.DELIMETER);
		sb.append(this.parentId);
		sb.append(Constant.DELIMETER);
		sb.append(this.questionTextHash);
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
				+ ((qbQuestionTypeId == null) ? 0 : qbQuestionTypeId.hashCode());
		result = prime * result
				+ ((qbSubcategoryId == null) ? 0 : qbSubcategoryId.hashCode());
		result = prime
				* result
				+ ((questionTextHash == null) ? 0 : questionTextHash.hashCode());
		result = prime
				* result
				+ ((getCreatedByUserId() == null) ? 0 : getCreatedByUserId().hashCode());
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
		QbQuestion other = (QbQuestion) obj;
		if (qbQuestionTypeId == null) {
			if (other.qbQuestionTypeId != null)
				return false;
		} else if (!qbQuestionTypeId.equals(other.qbQuestionTypeId))
			return false;
		if (qbSubcategoryId == null) {
			if (other.qbSubcategoryId != null)
				return false;
		} else if (!qbSubcategoryId.equals(other.qbSubcategoryId))
			return false;
		if (questionTextHash == null) {
			if (other.questionTextHash != null)
				return false;
		} else if (!questionTextHash.equals(other.questionTextHash))
			return false;
		if (getCreatedByUserId() == null) {
			if (other.getCreatedByUserId() != null)
				return false;
		} else if (!getCreatedByUserId().equals(other.getCreatedByUserId()))
			return false;
		return true;
	}
    
   
    /* (non-Javadoc)
     * @see edu.amrita.aview.common.entities.Auditable#setCreatedAuditData(java.lang.Long, java.sql.Timestamp, java.lang.Integer)
     */
    public void setCreatedAuditData(Long createdUserId,Timestamp createdDate,Integer statusId)
    {
        super.setCreatedAuditData(createdUserId, createdDate, statusId);
        if(this.qbAnswerChoices != null)
        {
            for(QbAnswerChoice qbanswerchoice:qbAnswerChoices)
            {
                qbanswerchoice.setCreatedAuditData(createdUserId, createdDate, statusId);
            }
        }
        /*if(this.qbQuestionMediaFiles != null)
        {
        	for(QbQuestionMediaFile qbQuestionMediaFile : qbQuestionMediaFiles)
    		{
        		qbQuestionMediaFile.setCreatedAuditData(createdUserId, createdDate, statusId);
    		}
        } */       
    }
    
  
    /* (non-Javadoc)
     * @see edu.amrita.aview.common.entities.Auditable#setModifiedAuditData(java.lang.Long, java.sql.Timestamp)
     */
    public void setModifiedAuditData(Long modifiedUserId,Timestamp modifedDate) throws AViewException
    {
        super.setModifiedAuditData(modifiedUserId, modifedDate);
        if(this.qbAnswerChoices != null)
        {
            for(QbAnswerChoice qbanswerchoice:qbAnswerChoices)
            {
                if(qbanswerchoice.getQbAnswerChoiceId() != 0) //Update
                {
                    qbanswerchoice.setModifiedAuditData(modifiedUserId, modifedDate);
                }
                else
                {
                    qbanswerchoice.setCreatedAuditData(modifiedUserId, modifedDate, StatusHelper.getActiveStatusId());
                }
            }
        }
        
       /*if(this.qbQuestionMediaFiles != null)
        {
        	for(QbQuestionMediaFile qbQuestionMediaFile : qbQuestionMediaFiles)
            {
                if(qbQuestionMediaFile.getQbQuestionMediaFileId() != 0) //Update
                {
                	qbQuestionMediaFile.setModifiedAuditData(modifiedUserId, modifedDate);
                }
                else
                {
                	qbQuestionMediaFile.setCreatedAuditData(modifiedUserId, modifedDate, StatusHelper.getActiveStatusId());
                }
            }
        }*/
        
    }
    
    /**
     * Gets the qb difficulty level name.
     *
     * @return the qb difficulty level name
     */
    @Transient
	public String getQbDifficultyLevelName() {
		return qbDifficultyLevelName;
	}
	
	/**
	 * Sets the qb difficulty level name.
	 *
	 * @param qbDifficultyLevelName the new qb difficulty level name
	 */
	public void setQbDifficultyLevelName(String qbDifficultyLevelName) {
		this.qbDifficultyLevelName = qbDifficultyLevelName;
	}
	
	/**
	 * Gets the qb question type name.
	 *
	 * @return the qb question type name
	 */
	@Transient
	public String getQbQuestionTypeName() {
		return qbQuestionTypeName;
	}
	
	/**
	 * Sets the qb question type name.
	 *
	 * @param qbQuestionTypeName the new qb question type name
	 */
	public void setQbQuestionTypeName(String qbQuestionTypeName) {
		this.qbQuestionTypeName = qbQuestionTypeName;
	}

}
