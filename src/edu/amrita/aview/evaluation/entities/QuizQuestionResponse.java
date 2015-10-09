/*
 * @(#)QuizQuestionResponse.java 4.0 2013/10/05
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
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "quiz_question_response")
public class QuizQuestionResponse extends Auditable
{
	
	/** The quiz question response id. */
	private Long quizQuestionResponseId = 0l ;
	
	/** The quiz question id. */
	private Long quizQuestionId = 0l ;
	
	/** The score. */
	private Double score = 0.0 ;	
	
	/** The quiz response. */
	private QuizResponse quizResponse;
	
	/** The quiz question choice responses. */
	private Set<QuizQuestionChoiceResponse> quizQuestionChoiceResponses = new HashSet<QuizQuestionChoiceResponse>();
	
	// user defined variables for polling result
	/** The quiz question id polling. */
	private int quizQuestionIdPolling = 0;
	
	/** The question text. */
	private String questionText = null ;
	
	/** The quiz answer choice id. */
	private int quizAnswerChoiceId = 0;
	
	/** The choice text. */
	private String choiceText = null ;
	
	/** The correct count for answer choice. */
	private int correctCountForAnswerChoice = 0 ;
	
	/** The student count for class. */
	private int studentCountForClass = 0 ;
	
	/** The display sequence. */
	private int displaySequence = 0 ;
	
	/** The quiz answer choices. */
	private List<QuizQuestionResponse> quizAnswerChoices = null ;
	
	/**
	 * Update from.
	 *
	 * @param other the other
	 */
	public void updateFrom(QuizQuestionResponse other)
	{
		this.quizResponse = other.quizResponse;
		this.quizQuestionId=other.quizQuestionId;
		this.score=other.score;
		this.setStatusId(other.getStatusId());
		
		Set<QuizQuestionChoiceResponse> tempQuizQuestionChoiceResponse = new HashSet<QuizQuestionChoiceResponse>();
		if(this.quizQuestionChoiceResponses != null)
        {
         	tempQuizQuestionChoiceResponse.addAll(this.quizQuestionChoiceResponses);
            this.quizQuestionChoiceResponses.clear();
        }
        
		Set<QuizQuestionChoiceResponse> tempOtherQuizQuestionChoiceResponse = new HashSet<QuizQuestionChoiceResponse>();
        if(other.quizQuestionChoiceResponses != null)
        {
        	tempOtherQuizQuestionChoiceResponse.addAll(other.quizQuestionChoiceResponses);
        }
        super.mergeAssociations(tempQuizQuestionChoiceResponse, tempOtherQuizQuestionChoiceResponse);
        this.setQuizQuestionChoiceResponse(tempQuizQuestionChoiceResponse);
	}
	
	/**
	 * Gets the quiz response.
	 *
	 * @return the quizResponse
	 */
	@ManyToOne(fetch= FetchType.EAGER)
	@JoinColumn(name="quiz_response_id", nullable=false)
	public QuizResponse getQuizResponse() {
		return quizResponse;
	}

	/**
	 * Sets the quiz response.
	 *
	 * @param quizResponse the quizResponse to set
	 */
	public void setQuizResponse(QuizResponse quizResponse) {
		this.quizResponse = quizResponse;
	}

	/**
	 * Gets the quiz question choice response.
	 *
	 * @return the quizQuestionChoiceResponse
	 */
	@OneToMany(mappedBy="quizQuestionResponse" ,fetch=FetchType.EAGER, cascade={CascadeType.ALL},orphanRemoval=true)	
	@BatchSize(size= 1000)
	public Set<QuizQuestionChoiceResponse> getQuizQuestionChoiceResponse() {
		return quizQuestionChoiceResponses;
	}

	/**
	 * Sets the quiz question choice response.
	 *
	 * @param quizQuestionChoiceResponse the quizQuestionChoiceResponse to set
	 */
	public void setQuizQuestionChoiceResponse(
			Set<QuizQuestionChoiceResponse> quizQuestionChoiceResponse) {
		this.quizQuestionChoiceResponses = quizQuestionChoiceResponse;
	}

	/**
	 * Adds the quiz question choice response.
	 *
	 * @param quizquestionchoiceresponse the quizquestionchoiceresponse
	 */
	public synchronized void addQuizQuestionChoiceResponse(QuizQuestionChoiceResponse quizquestionchoiceresponse)
	{
		if(this.quizQuestionChoiceResponses == null)
		{
			this.quizQuestionChoiceResponses = new HashSet<QuizQuestionChoiceResponse>();
		}
		quizquestionchoiceresponse.setQuizQuestionResponse(this);
		this.quizQuestionChoiceResponses.add(quizquestionchoiceresponse);
	}
	

	/**
	 * Gets the quiz question response id.
	 *
	 * @return the quizQuestionResponseId
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "quiz_question_response_id")
	public Long getQuizQuestionResponseId() {
		return quizQuestionResponseId;
	}

	/**
	 * Sets the quiz question response id.
	 *
	 * @param quizQuestionResponseId the quizQuestionResponseId to set
	 */
	public void setQuizQuestionResponseId(Long quizQuestionResponseId) {
		this.quizQuestionResponseId = quizQuestionResponseId;
	}

	/**
	 * Gets the quiz question id.
	 *
	 * @return the quizQuestionId
	 */
	@Column(name = "quiz_question_id")
	public Long getQuizQuestionId() {
		return quizQuestionId;
	}

	/**
	 * Sets the quiz question id.
	 *
	 * @param quizQuestionId the quizQuestionId to set
	 */
	public void setQuizQuestionId(Long quizQuestionId) {
		this.quizQuestionId = quizQuestionId;
	}

	/**
	 * Gets the score.
	 *
	 * @return the score
	 */
	@Column(name = "score")
	public Double getScore() {
		return score;
	}

	/**
	 * Sets the score.
	 *
	 * @param score the score to set
	 */
	public void setScore(Double score) {
		this.score = score;
	}
	
	/**
	 * Gets the quiz question id polling.
	 *
	 * @return the quizQuestionIdPolling
	 */
	@Transient
	public int getQuizQuestionIdPolling() {
		return quizQuestionIdPolling;
	}

	/**
	 * Sets the quiz question id polling.
	 *
	 * @param quizQuestionIdPolling the quizQuestionIdPolling to set
	 */
	public void setQuizQuestionIdPolling(int quizQuestionIdPolling) {
		this.quizQuestionIdPolling = quizQuestionIdPolling;
	}

	/**
	 * Gets the question text.
	 *
	 * @return the questionText
	 */
	@Transient
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
	 * Gets the quiz answer choice id.
	 *
	 * @return the quizAnswerChoiceId
	 */
	@Transient
	public int getQuizAnswerChoiceId() {
		return quizAnswerChoiceId;
	}

	/**
	 * Sets the quiz answer choice id.
	 *
	 * @param quizAnswerChoiceId the quizAnswerChoiceId to set
	 */
	public void setQuizAnswerChoiceId(int quizAnswerChoiceId) {
		this.quizAnswerChoiceId = quizAnswerChoiceId;
	}

	/**
	 * Gets the choice text.
	 *
	 * @return the choiceText
	 */
	@Transient
	public String getChoiceText() {
		return choiceText;
	}

	/**
	 * Sets the choice text.
	 *
	 * @param choiceText the choiceText to set
	 */
	public void setChoiceText(String choiceText) {
		this.choiceText = choiceText;
	}

	/**
	 * Gets the correct count for answer choice.
	 *
	 * @return the correctCountForAnswerChoice
	 */
	@Transient
	public int getCorrectCountForAnswerChoice() {
		return correctCountForAnswerChoice;
	}

	/**
	 * Sets the correct count for answer choice.
	 *
	 * @param correctCountForAnswerChoice the correctCountForAnswerChoice to set
	 */
	public void setCorrectCountForAnswerChoice(int correctCountForAnswerChoice) {
		this.correctCountForAnswerChoice = correctCountForAnswerChoice;
	}

	/**
	 * Gets the student count for class.
	 *
	 * @return the studentCountForClass
	 */
	@Transient
	public int getStudentCountForClass() {
		return studentCountForClass;
	}

	/**
	 * Sets the student count for class.
	 *
	 * @param studentCountForClass the studentCountForClass to set
	 */
	public void setStudentCountForClass(int studentCountForClass) {
		this.studentCountForClass = studentCountForClass;
	}

	/**
	 * Gets the quiz answer choices.
	 *
	 * @return the quizAnswerChoices
	 */
	@Transient
	public List<QuizQuestionResponse> getQuizAnswerChoices() {
		return quizAnswerChoices;
	}

	/**
	 * Sets the quiz answer choices.
	 *
	 * @param quizAnswerChoices the quizAnswerChoices to set
	 */
	public void setQuizAnswerChoices(List<QuizQuestionResponse> quizAnswerChoices) {
		this.quizAnswerChoices = quizAnswerChoices;
	}

	/**
	 * Gets the display sequence.
	 *
	 * @return the displaySequence
	 */
	@Transient
	public int getDisplaySequence() {
		return displaySequence;
	}

	/**
	 * Sets the display sequence.
	 *
	 * @param displaySequence the displaySequence to set
	 */
	public void setDisplaySequence(int displaySequence) {
		this.displaySequence = displaySequence;
	}


	/* (non-Javadoc)
	 * @see edu.amrita.aview.common.entities.Auditable#toString()
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(this.quizQuestionResponseId);
		sb.append(Constant.DELIMETER);
		sb.append(this.quizResponse.getQuizResponseId());
		sb.append(Constant.DELIMETER);
		sb.append(this.quizQuestionId);
		sb.append(Constant.DELIMETER);
		sb.append(this.score);
		sb.append(Constant.DELIMETER);
		sb.append(super.toString());			
		return sb.toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((quizQuestionId == null) ? 0 : quizQuestionId.hashCode());
		result = prime * result
				+ ((quizResponse == null) ? 0 : quizResponse.getQuizResponseId().hashCode());
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
		final QuizQuestionResponse other = (QuizQuestionResponse) obj;
		if (quizQuestionId == null) {
			if (other.quizQuestionId != null)
				return false;
		} else if (!quizQuestionId.equals(other.quizQuestionId))
			return false;
		if (quizResponse == null) {
			if (other.quizResponse != null)
				return false;
		} else if (!quizResponse.getQuizResponseId().equals(other.quizResponse.getQuizResponseId()))
			return false;
		return true;
	}

	
	/* (non-Javadoc)
	 * @see edu.amrita.aview.common.entities.Auditable#setCreatedAuditData(java.lang.Long, java.sql.Timestamp, java.lang.Integer)
	 */
	public void setCreatedAuditData(Long createdUserId,Timestamp createdDate,Integer statusId)
	{
		super.setCreatedAuditData(createdUserId, createdDate, statusId);
		if(this.quizQuestionChoiceResponses != null)
		{
			for(QuizQuestionChoiceResponse qqcr:quizQuestionChoiceResponses)
			{
				qqcr.setCreatedAuditData(createdUserId, createdDate, statusId);
			}
		}	
		
	}
	
	
	/* (non-Javadoc)
	 * @see edu.amrita.aview.common.entities.Auditable#setModifiedAuditData(java.lang.Long, java.sql.Timestamp)
	 */
	public void setModifiedAuditData(Long modifiedUserId,Timestamp modifedDate) throws AViewException
	{
		super.setModifiedAuditData(modifiedUserId, modifedDate);
		if(this.quizQuestionChoiceResponses != null)
		{
			for(QuizQuestionChoiceResponse qqcr:quizQuestionChoiceResponses)
			{
				if(qqcr.getQuizQuestionChoiceResponseId()!= 0) //Update
				{
					qqcr.setModifiedAuditData(modifiedUserId, modifedDate);
				}
				else
				{
					qqcr.setCreatedAuditData(modifiedUserId, modifedDate, StatusHelper.getActiveStatusId());
				}
			}
		}		
	}
	
}
