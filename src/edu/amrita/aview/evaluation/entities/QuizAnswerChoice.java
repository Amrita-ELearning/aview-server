/*
 * @(#)QuizAnswerChoice.java 4.0 2013/10/18
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
@Table(name = "quiz_answer_choice")
public class QuizAnswerChoice extends Auditable {

	/** The quiz answer choice id. */
	private Long quizAnswerChoiceId = 0l;
	
	/** The choice text. */
	private String choiceText = null ;
	
	/** The choice text hash. */
	private String choiceTextHash = null ;
	
	/** The fraction. */
	private float fraction = 0.0f ;
	
	/** The display sequence. */
	private Integer displaySequence = 0;
	
	/** The quiz question. */
	private QuizQuestion quizQuestion ;
	
	/** The has student answered. */
	private int hasStudentAnswered = 0;
	
	/**
	 * Gets the quiz question.
	 *
	 * @return the quizQuestion
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="quiz_question_id", nullable=false)
	public QuizQuestion getQuizQuestion() {
		return quizQuestion;
	}
	
	/**
	 * Sets the quiz question.
	 *
	 * @param quizQuestion the quizQuestion to set
	 */
	public void setQuizQuestion(QuizQuestion quizQuestion) {
		this.quizQuestion = quizQuestion;
	}
	
	/**
	 * Gets the quiz answer choice id.
	 *
	 * @return the quizAnswerChoiceId
	 */
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
		
	/**
	 * Gets the choice text.
	 *
	 * @return the choiceText
	 */
	@Column(name = "choice_text")
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
	 * Gets the choice text hash.
	 *
	 * @return the choiceTextHash
	 */
		
	@Column(name = "choice_text_hash")
	public String getChoiceTextHash() {
		return choiceTextHash;
	}
	
	/**
	 * Sets the choice text hash.
	 *
	 * @param choiceTextHash the choiceTextHash to set
	 */
	public void setChoiceTextHash(String choiceTextHash) {
		this.choiceTextHash = choiceTextHash;
	}
	
	/**
	 * Gets the fraction.
	 *
	 * @return the fraction
	 */	
		
	@Column(name = "fraction")
	public float getFraction() {
		return fraction;
	}
	
	/**
	 * Gets the display sequence.
	 *
	 * @return the displaySequence
	 */
	@Column(name = "display_sequence")
	public Integer getDisplaySequence() {
		return displaySequence;
	}
	
	/**
	 * Sets the display sequence.
	 *
	 * @param displaySequence the displaySequence to set
	 */
	public void setDisplaySequence(Integer displaySequence) {
		this.displaySequence = displaySequence;
	}
	
	/**
	 * Sets the fraction.
	 *
	 * @param fraction the fraction to set
	 */
	public void setFraction(float fraction) {
		this.fraction = fraction;
	}
	
	/**
	 * Gets the student ans fraction.
	 *
	 * @return the studentAnsFraction
	 */
	@Transient
	public int getStudentAnsFraction() {
		return hasStudentAnswered;
	}
	
	/**
	 * Sets the student ans fraction.
	 *
	 * @param hasStudentAnswered the new student ans fraction
	 */
	public void setStudentAnsFraction(int hasStudentAnswered) {
		this.hasStudentAnswered = hasStudentAnswered;
	}
	
	
	/* (non-Javadoc)
	 * @see edu.amrita.aview.common.entities.Auditable#toString()
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(this.quizAnswerChoiceId);
		sb.append(Constant.DELIMETER);
		sb.append(this.quizQuestion.getQuizQuestionId());
		sb.append(Constant.DELIMETER);
		sb.append(this.choiceText);
		sb.append(Constant.DELIMETER);
		sb.append(this.choiceTextHash);
		sb.append(Constant.DELIMETER);
		sb.append(this.fraction);
		sb.append(Constant.DELIMETER);
		sb.append(this.displaySequence);
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
				+ ((choiceTextHash == null) ? 0 : choiceTextHash.hashCode());
		result = prime * result
				+ ((quizQuestion == null) ? 0 : quizQuestion.getQuizQuestionId().hashCode());
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
		final QuizAnswerChoice other = (QuizAnswerChoice) obj;
		if (choiceTextHash == null) {
			if (other.choiceTextHash != null)
				return false;
		} else if (!choiceTextHash.equals(other.choiceTextHash))
			return false;
		if (quizQuestion == null) {
			if (other.quizQuestion != null)
				return false;
		} else if (!quizQuestion.getQuizQuestionId().equals(other.quizQuestion.getQuizQuestionId()))
			return false;
		return true;
	}	
}
