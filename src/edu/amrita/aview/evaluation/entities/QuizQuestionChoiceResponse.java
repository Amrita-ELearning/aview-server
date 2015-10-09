/*
 * @(#)QuizQuestionChoiceResponse.java 4.0 2013/10/05
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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "quiz_question_choice_response")
public class QuizQuestionChoiceResponse extends Auditable
{
    
    /** The quiz question choice response id. */
    private Long quizQuestionChoiceResponseId = 0l ;
	
	/** The quiz question id. */
	private Long quizQuestionId = 0l ;
	
	/** The quiz answer choice id. */
	private Long quizAnswerChoiceId = 0l ;	
	
	/** The quiz question response. */
	private QuizQuestionResponse quizQuestionResponse ;
	
	/**
	 * Gets the quiz question response.
	 *
	 * @return the quizQuestionResponse
	 */
	@ManyToOne(fetch= FetchType.EAGER)
	@JoinColumn(name="quiz_question_response_id", nullable=false)
	public QuizQuestionResponse getQuizQuestionResponse() {
		return quizQuestionResponse;
	}


	/**
	 * Sets the quiz question response.
	 *
	 * @param quizQuestionResponse the quizQuestionResponse to set
	 */
	public void setQuizQuestionResponse(QuizQuestionResponse quizQuestionResponse) {
		this.quizQuestionResponse = quizQuestionResponse;
	}
	
	/**
	 * Gets the quiz question choice response id.
	 *
	 * @return the quizQuestionChoiceResponseId
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "quiz_question_choice_response_id")
	public Long getQuizQuestionChoiceResponseId() {
		return quizQuestionChoiceResponseId;
	}

	/**
	 * Sets the quiz question choice response id.
	 *
	 * @param quizQuestionChoiceResponseId the quizQuestionChoiceResponseId to set
	 */
	public void setQuizQuestionChoiceResponseId(Long quizQuestionChoiceResponseId) {
		this.quizQuestionChoiceResponseId = quizQuestionChoiceResponseId;
	}

	/**
	 * Gets the quiz question id.
	 *
	 * @return the quiz question id
	 */
	@Transient
	public Long getQuizQuestionId() {
		return quizQuestionId;
	}


	/**
	 * Sets the quiz question id.
	 *
	 * @param quizQuestionId the new quiz question id
	 */
	public void setQuizQuestionId(Long quizQuestionId) {
		this.quizQuestionId = quizQuestionId;
	}


	/**
	 * Gets the quiz answer choice id.
	 *
	 * @return the quizAnswerChoiceId
	 */
	@Column(name = "quiz_answer_choice_id")
	public Long getQuizAnswerChoiceId() {
		return quizAnswerChoiceId;
	}

	/**
	 * Sets the quiz answer choice id.
	 *
	 * @param quizAnswerChoiceId the quizAnswerChoiceId to set
	 */
	public void setQuizAnswerChoiceId(Long quizAnswerChoiceId) {
		this.quizAnswerChoiceId = quizAnswerChoiceId;
	}
	
	
	/* (non-Javadoc)
	 * @see edu.amrita.aview.common.entities.Auditable#toString()
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(this.quizQuestionChoiceResponseId);
		sb.append(Constant.DELIMETER);
		sb.append(this.quizQuestionResponse.getQuizQuestionResponseId());
		sb.append(Constant.DELIMETER);
		sb.append(this.quizAnswerChoiceId);
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
				+ ((quizAnswerChoiceId == null) ? 0 : quizAnswerChoiceId
						.hashCode());
		result = prime
				* result
				+ ((quizQuestionResponse == null) ? 0
						: quizQuestionResponse.getQuizQuestionResponseId().hashCode());
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
		final QuizQuestionChoiceResponse other = (QuizQuestionChoiceResponse) obj;
		if (quizAnswerChoiceId == null) {
			if (other.quizAnswerChoiceId != null)
				return false;
		} else if (!quizAnswerChoiceId.equals(other.quizAnswerChoiceId))
			return false;
		if (quizQuestionResponse == null) {
			if (other.quizQuestionResponse != null)
				return false;
		} else if (!quizQuestionResponse.getQuizQuestionResponseId().equals(other.quizQuestionResponse.getQuizQuestionResponseId()))
			return false;
		return true;
	}

}
