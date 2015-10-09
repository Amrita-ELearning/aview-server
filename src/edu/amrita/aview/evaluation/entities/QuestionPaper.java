/*
 * @(#)QuestionPaper.java 4.0 2013/10/11
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
@Table(name = "question_paper")
public class QuestionPaper extends Auditable {

	/** The question paper id. */
	private Long questionPaperId = 0l;
	
	/** The question paper name. */
	private String questionPaperName = null ;
	
	/** The current total marks. */
	private double currentTotalMarks = 0.0 ; 
	
	/** The max total marks. */
	private double maxTotalMarks = 0.0 ;
	
	/** The is complete. */
	private String isComplete = null ;
	
	/** The institute id. */
	private Long instituteId = 0l ;
	
	/** The question paper questions. */
	private Set<QuestionPaperQuestion> questionPaperQuestions = new HashSet<QuestionPaperQuestion>();
	
	// Non mapped attributes for Client side only
	/** The created by user name. */
	private String createdByUserName = null ;
	
	/** The modified by user name. */
	private String modifiedByUserName = null ;
	
	/** The total questions. */
	private Integer totalQns = 0;
	
	/**
	 * Update from.
	 *
	 * @param other the other
	 */
	public void updateFrom(QuestionPaper other)
	{
		this.questionPaperName = other.questionPaperName;
		this.currentTotalMarks = other.currentTotalMarks;
		this.maxTotalMarks = other.maxTotalMarks;
		this.isComplete = other.isComplete;
		this.setStatusId(other.getStatusId());
		Set<QuestionPaperQuestion> tempQuestionPaperQuestion = new HashSet<QuestionPaperQuestion>();
		if(this.questionPaperQuestions != null)
        {
			tempQuestionPaperQuestion.addAll(this.questionPaperQuestions);
            this.questionPaperQuestions.clear();
        }
    
		Set<QuestionPaperQuestion> tempOtherQuestionPaperQuestion = new HashSet<QuestionPaperQuestion>();
        if(other.questionPaperQuestions != null)
        {
        	tempOtherQuestionPaperQuestion.addAll(other.questionPaperQuestions);            
        }
      
        super.mergeAssociations(tempQuestionPaperQuestion, tempOtherQuestionPaperQuestion);
        
        this.questionPaperQuestions.addAll(tempQuestionPaperQuestion);
	}
	
	/**
	 * Gets the question paper questions.
	 *
	 * @return the questionPaperQuestion
	 */
	@OneToMany(mappedBy="questionPaper", fetch=FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval=true)	
	@BatchSize(size=1000)
	public Set<QuestionPaperQuestion> getQuestionPaperQuestions() {
		return questionPaperQuestions;
	}

	/**
	 * Sets the question paper questions.
	 *
	 * @param questionPaperQuestion the questionPaperQuestion to set
	 */
	public void setQuestionPaperQuestions(
			Set<QuestionPaperQuestion> questionPaperQuestion) {
		this.questionPaperQuestions = questionPaperQuestion;
	}
	
	/**
	 * Adds the question paper question.
	 *
	 * @param qpq the qpq
	 */
	public synchronized void addQuestionPaperQuestion(QuestionPaperQuestion qpq)
	{
		if(this.questionPaperQuestions == null)
		{
			this.questionPaperQuestions = new HashSet<QuestionPaperQuestion>();
		}
		qpq.setQuestionPaper(this);
		this.questionPaperQuestions.add(qpq);
	}
	
	/**
	 * Gets the question paper id.
	 *
	 * @return the questionPaperId
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)	
	@Column(name = "question_paper_id")
	public Long getQuestionPaperId() {
		return questionPaperId;
	}
	
	/**
	 * Sets the question paper id.
	 *
	 * @param questionPaperId the questionPaperId to set
	 */
	public void setQuestionPaperId(Long questionPaperId) {
		this.questionPaperId = questionPaperId;
	}
	
	/**
	 * Gets the question paper name.
	 *
	 * @return the questionPaperName
	 */
	@Column(name = "question_paper_name")
	public String getQuestionPaperName() {
		return questionPaperName;
	}
	
	/**
	 * Sets the question paper name.
	 *
	 * @param questionPaperName the questionPaperName to set
	 */
	public void setQuestionPaperName(String questionPaperName) {
		this.questionPaperName = questionPaperName;
	}
	
	/**
	 * Gets the current total marks.
	 *
	 * @return the currentTotalMarks
	 */
	@Column(name = "current_total_marks")
	public double getCurrentTotalMarks() {
		return currentTotalMarks;
	}
	
	/**
	 * Sets the current total marks.
	 *
	 * @param currentTotalMarks the currentTotalMarks to set
	 */
	public void setCurrentTotalMarks(double currentTotalMarks) {
		this.currentTotalMarks = currentTotalMarks;
	}
	
	/**
	 * Gets the max total marks.
	 *
	 * @return the maxTotalMarks
	 */
	@Column(name = "max_total_marks")
	public double getMaxTotalMarks() {
		return maxTotalMarks;
	}
	
	/**
	 * Sets the max total marks.
	 *
	 * @param maxTotalMarks the maxTotalMarks to set
	 */
	public void setMaxTotalMarks(double maxTotalMarks) {
		this.maxTotalMarks = maxTotalMarks;
	}
	
	/**
	 * Gets the checks if is complete.
	 *
	 * @return the isComplete
	 */
	@Column(name = "is_complete")
	public String getIsComplete() {
		return isComplete;
	}
	
	/**
	 * Sets the checks if is complete.
	 *
	 * @param isComplete the isComplete to set
	 */
	public void setIsComplete(String isComplete) {
		this.isComplete = isComplete;
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
	 * @see edu.amrita.aview.common.entities.Auditable#toString()
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(this.questionPaperId);
		sb.append(Constant.DELIMETER);
		sb.append(this.questionPaperName);
		sb.append(Constant.DELIMETER);
		sb.append(this.currentTotalMarks);
		sb.append(Constant.DELIMETER);
		sb.append(this.maxTotalMarks);
		sb.append(Constant.DELIMETER);
		sb.append(this.isComplete);
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
				+ ((getCreatedByUserId() == null) ? 0 : getCreatedByUserId().hashCode());
		result = prime
				* result
				+ ((questionPaperName == null) ? 0 : questionPaperName
						.hashCode());
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
		QuestionPaper other = (QuestionPaper) obj;
		if (getCreatedByUserId() == null) {
			if (other.getCreatedByUserId() != null) {
				return false;
			}
		} else if (!getCreatedByUserId().equals(other.getCreatedByUserId())) {
			return false;
		}
		if (questionPaperName == null) {
			if (other.questionPaperName != null) {
				return false;
			}
		} else if (!questionPaperName.equals(other.questionPaperName)) {
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
	public Integer getTotalQns() {
		return totalQns;
	}
	
	/**
	 * Sets the total questions.
	 *
	 * @param totalQns the new total questions
	 */
	public void setTotalQns(Integer totalQns) {
		this.totalQns = totalQns;
	}
	
	
	/* (non-Javadoc)
	 * @see edu.amrita.aview.common.entities.Auditable#setCreatedAuditData(java.lang.Long, java.sql.Timestamp, java.lang.Integer)
	 */
	public void setCreatedAuditData(Long createdUserId,Timestamp createdDate,Integer statusId)
	{
		super.setCreatedAuditData(createdUserId, createdDate, statusId);
		if(this.questionPaperQuestions != null)
		{
			for(QuestionPaperQuestion qpq : questionPaperQuestions)
			{
				qpq.setCreatedAuditData(createdUserId, createdDate, statusId);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.amrita.aview.common.entities.Auditable#setModifiedAuditData(java.lang.Long, java.sql.Timestamp)
	 */
	public void setModifiedAuditData(Long modifiedUserId,Timestamp modifedDate) throws AViewException
	{
		super.setModifiedAuditData(modifiedUserId, modifedDate);
		if(this.questionPaperQuestions != null)
		{
			for(QuestionPaperQuestion qpq : questionPaperQuestions)
			{
				if(qpq.getQuestionPaperQuestionId() != 0) //Update
				{
					qpq.setModifiedAuditData(modifiedUserId, modifedDate);
				}
				else
				{
					qpq.setCreatedAuditData(modifiedUserId, modifedDate, StatusHelper.getActiveStatusId());
				}
			}
		}
	}
	
}
