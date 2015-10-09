/*
 * @(#)QbAnswerChoice.java 4.0 2013/10/13
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

import org.hibernate.annotations.BatchSize;

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
@Table(name = "qb_answer_choice")
public class QbAnswerChoice extends Auditable {

	
	/** The qb answer choice id. */
	private Long qbAnswerChoiceId = 0l;
	
	/** The choice text. */
	private String choiceText = null ;
	
	/** The fraction. */
	private Float fraction = 0.0f ;
	
	/** The display sequence. */
	private Integer displaySequence = 0;
	
	/** The choice text hash. */
	private String choiceTextHash = null ;
	
	/** The qb question. */
	private QbQuestion qbQuestion = null ;
		
	/**
	 * Gets the qb answer choice id.
	 *
	 * @return the qbAnswerChoiceId
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)		
	@Column(name = "qb_answer_choice_id")
	public Long getQbAnswerChoiceId() {
		return qbAnswerChoiceId;
	}
	
	/**
	 * Sets the qb answer choice id.
	 *
	 * @param qbAnswerChoiceId the qbAnswerChoiceId to set
	 */
	public void setQbAnswerChoiceId(Long qbAnswerChoiceId) {
		this.qbAnswerChoiceId = qbAnswerChoiceId;
	}
	
	/**
	 * Gets the qb question.
	 * This method associates a question and answer (many to one)
	 * @return the qbQuestion
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="qb_question_id", nullable=false)	
	@BatchSize(size=1000)
	public QbQuestion getQbQuestion() {
		
		return qbQuestion;
	}
	
	/**
	 * Sets the qb question.
	 *
	 * @param qbQuestion the qbQuestion to set
	 */
	public void setQbQuestion(QbQuestion qbQuestion) {
		this.qbQuestion = qbQuestion;
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
	 * Gets the fraction.
	 *
	 * @return the fraction
	 */
	@Column(name = "fraction")
	public Float getFraction() {
		return fraction;
	}
	
	/**
	 * Sets the fraction.
	 *
	 * @param fraction the fraction to set
	 */
	public void setFraction(Float fraction) {
		this.fraction = fraction;
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
	
	/*
	 * This method is used for concatenation
	 * of various variables in this class
	 * 
	 */
	/* (non-Javadoc)
	 * @see edu.amrita.aview.common.entities.Auditable#toString()
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(this.qbAnswerChoiceId);
		sb.append(Constant.DELIMETER);
		sb.append(this.qbQuestion.getQbQuestionId());
		sb.append(Constant.DELIMETER);
		sb.append(this.choiceText);
		sb.append(Constant.DELIMETER);
		sb.append(this.fraction);
		sb.append(Constant.DELIMETER);
		sb.append(this.displaySequence);
		sb.append(Constant.DELIMETER);
		sb.append(this.choiceTextHash);
		sb.append(Constant.DELIMETER);
		
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
				+ ((choiceTextHash == null) ? 0 : choiceTextHash.hashCode());
		/*
		result = prime * result
				+ ((fraction == null) ? 0 : fraction.hashCode());
		*/
		result = prime * result
				+ ((qbQuestion == null) ? 0 : qbQuestion.getQbQuestionId().hashCode());
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
		QbAnswerChoice other = (QbAnswerChoice) obj;
		if (choiceTextHash == null) {
			if (other.choiceTextHash != null) {
				return false;
			}
		} else if (!choiceTextHash.equals(other.choiceTextHash)) {
			return false;
		}
		/*
		if (fraction == null) {
			if (other.fraction != null) {
				return false;
			}
		} else if (!fraction.equals(other.fraction)) {
			return false;
		}*/
		if (qbQuestion == null) {
			if (other.qbQuestion != null) {
				return false;
			}
		} else if (!qbQuestion.getQbQuestionId().equals(other.qbQuestion.getQbQuestionId())) {
			return false;
		}
		return true;
	}
}
