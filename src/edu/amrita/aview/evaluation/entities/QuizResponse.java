/*
 * @(#)QuizResponse.java 4.0 2013/10/04
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
@Table(name = "quiz_response")
public class QuizResponse extends Auditable 
{
	
	/** The quiz response id. */
	private Long quizResponseId = 0l ;
	
	/** The user id. */
	private Long userId = 0l ;
	
	/** The quiz id. */
	private Long quizId = 0l ;
	
	/** The total score. */
	private float totalScore = 0.0f ;
	
	/** The time start. */
	private Timestamp timeStart = null ;
	
	/** The time end. */
	private Timestamp timeEnd = null ;
	
	/** The quiz response type. */
	private String quizResponseType=null;
	
	/** The quiz question responses. */
	private Set<QuizQuestionResponse> quizQuestionResponses = new HashSet<QuizQuestionResponse>();
	
	/**
	 * Update from.
	 *
	 * @param other the other
	 */
	public void updateFrom(QuizResponse other)
	{
		this.userId=other.userId;
		this.quizId=other.quizId;
		this.totalScore=other.totalScore;
		this.timeStart=other.timeStart;
		this.timeEnd=other.timeEnd;
		this.quizResponseType=other.quizResponseType;
		this.setStatusId(other.getStatusId());
		
		Set<QuizQuestionResponse> tempQuizQuestionResponse = new HashSet<QuizQuestionResponse>();		
		if(this.quizQuestionResponses != null)
        {
			tempQuizQuestionResponse.addAll(this.quizQuestionResponses);
			this.quizQuestionResponses.clear();
        }
        
		Set<QuizQuestionResponse> tempOtherQuizQuestionResponse = new HashSet<QuizQuestionResponse>();
        if(other.quizQuestionResponses != null)
        {
        	tempOtherQuizQuestionResponse.addAll(other.quizQuestionResponses);            
        }
       	super.mergeAssociations(tempQuizQuestionResponse, tempOtherQuizQuestionResponse);
       	this.setQuizQuestionResponse(tempQuizQuestionResponse);
	}
	
	/**
	 * Gets the quiz question response.
	 *
	 * @return the quizQuestionResponse
	 */
	@OneToMany(mappedBy="quizResponse",fetch=FetchType.EAGER, cascade={CascadeType.ALL},orphanRemoval = true)
	@BatchSize(size=1000)
	public Set<QuizQuestionResponse> getQuizQuestionResponse() {
		return quizQuestionResponses;
	}
	
	/**
	 * Sets the quiz question response.
	 *
	 * @param quizQuestionResponse the quizQuestionResponse to set
	 */
	public void setQuizQuestionResponse(
			Set<QuizQuestionResponse> quizQuestionResponse) {
		this.quizQuestionResponses = quizQuestionResponse;
	}
	
	/**
	 * Adds the quiz question response.
	 *
	 * @param quizquestionresponse the quizquestionresponse
	 */
	public synchronized void addQuizQuestionResponse(QuizQuestionResponse quizquestionresponse)
	{
		if(this.quizQuestionResponses == null)
		{
			this.quizQuestionResponses = new HashSet<QuizQuestionResponse>();
		}
		quizquestionresponse.setQuizResponse(this);
		this.quizQuestionResponses.add(quizquestionresponse);
	}
	
	/**
	 * Gets the quiz response id.
	 *
	 * @return the quizResponseId
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "quiz_response_id")
	public Long getQuizResponseId() {
		return quizResponseId;
	}
	
	/**
	 * Sets the quiz response id.
	 *
	 * @param quizResponseId the quizResponseId to set
	 */
	public void setQuizResponseId(Long quizResponseId) {
		this.quizResponseId = quizResponseId;
	}
	
	/**
	 * Gets the user id.
	 *
	 * @return the userId
	 */
	@Column(name = "user_id")
	public Long getUserId() {
		return userId;
	}
	
	/**
	 * Sets the user id.
	 *
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * Gets the quiz id.
	 *
	 * @return the quizId
	 */
	@Column(name = "quiz_id")
	public Long getQuizId() {
		return quizId;
	}
	
	/**
	 * Sets the quiz id.
	 *
	 * @param quizId the quizId to set
	 */
	public void setQuizId(Long quizId) {
		this.quizId = quizId;
	}
	
	/**
	 * Gets the total score.
	 *
	 * @return the totalScore
	 */
	@Column(name = "total_score")
	public float getTotalScore() {
		return totalScore;
	}
	
	/**
	 * Sets the total score.
	 *
	 * @param totalScore the totalScore to set
	 */
	public void setTotalScore(float totalScore) {
		this.totalScore = totalScore;
	}
	
	/**
	 * Gets the time start.
	 *
	 * @return the timeStart
	 */
	@Column(name = "time_start")
	public Timestamp getTimeStart() {
		return timeStart;
	}
	
	/**
	 * Sets the time start.
	 *
	 * @param timeStart the timeStart to set
	 */
	public void setTimeStart(Timestamp timeStart) {
		this.timeStart = timeStart;
	}
	
	/**
	 * Gets the time end.
	 *
	 * @return the timeEnd
	 */
	@Column(name="time_end")
	public Timestamp getTimeEnd() {
		return timeEnd;
	}
	
	/**
	 * Sets the time end.
	 *
	 * @param timeEnd the timeEnd to set
	 */
	public void setTimeEnd(Timestamp timeEnd) {
		this.timeEnd = timeEnd;
	}
	
	/**
	 * Gets the quiz response type.
	 *
	 * @return the quizResponseType
	 */
	@Column(name="quiz_response_type")
	public String getQuizResponseType() {
		return quizResponseType;
	}
	
	/**
	 * Sets the quiz response type.
	 *
	 * @param quizResponseType the quizResponseType to set
	 */
	public void setQuizResponseType(String quizResponseType) 
	{
		this.quizResponseType = quizResponseType;
	}
	
	
	/* (non-Javadoc)
	 * @see edu.amrita.aview.common.entities.Auditable#toString()
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(this.quizResponseId);
		sb.append(Constant.DELIMETER);
		sb.append(this.userId);
		sb.append(Constant.DELIMETER);
		sb.append(this.quizId);
		sb.append(Constant.DELIMETER);
		sb.append(this.totalScore);
		sb.append(Constant.DELIMETER);
		sb.append(this.timeStart);
		sb.append(Constant.DELIMETER);
		sb.append(this.timeEnd);
		sb.append(Constant.DELIMETER);
		sb.append(this.quizResponseType);
		sb.append(Constant.DELIMETER);
		sb.append(super.toString());
			
		return sb.toString() ;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((quizId == null) ? 0 : quizId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final QuizResponse other = (QuizResponse) obj;
		if (quizId == null) {
			if (other.quizId != null)
				return false;
		} else if (!quizId.equals(other.quizId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see edu.amrita.aview.common.entities.Auditable#setCreatedAuditData(java.lang.Long, java.sql.Timestamp, java.lang.Integer)
	 */
	public void setCreatedAuditData(Long createdUserId,Timestamp createdDate,Integer statusId)
	{
		super.setCreatedAuditData(createdUserId, createdDate, statusId);
		if(this.quizQuestionResponses != null)
		{
			for(QuizQuestionResponse response:quizQuestionResponses)
			{
				response.setCreatedAuditData(createdUserId, createdDate, statusId);
			}
		}
	}
	
	
	/* (non-Javadoc)
	 * @see edu.amrita.aview.common.entities.Auditable#setModifiedAuditData(java.lang.Long, java.sql.Timestamp)
	 */
	public void setModifiedAuditData(Long modifiedUserId,Timestamp modifedDate) throws AViewException
	{
		super.setModifiedAuditData(modifiedUserId, modifedDate);
		if(this.quizQuestionResponses != null)
		{
			for(QuizQuestionResponse qqr:quizQuestionResponses)
			{
				if(qqr.getQuizQuestionResponseId() != 0) //Update
				{
					qqr.setModifiedAuditData(modifiedUserId, modifedDate);
				}
				else
				{
					qqr.setCreatedAuditData(modifiedUserId, modifedDate, StatusHelper.getActiveStatusId ());
				}
			}
		}
	}
}

