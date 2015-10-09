/*
 * @(#)QuestionPaperQuestion.java 4.0 2013/10/11
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
@Table(name = "question_paper_question")
public class QuestionPaperQuestion extends Auditable {

	/** The question paper question id. */
	private Long questionPaperQuestionId = 0l ;
	
	/** The pattern type. */
	private String patternType = null ;
	
	/** The number of random questions. */
	private Long numRandomQuestions = null ;
	
	/** The question bank question id. */
	private Long qbQuestionId = null ;
	
	/** The question bank sub category id. */
	private Long qbSubcategoryId = null ;
	
	/** The question bank difficulty level id. */
	private Long qbDifficultyLevelId = null ;
	
	/** The question bank question type id. */
	private Long qbQuestionTypeId = null ;
	
	/** The marks. */
	private double marks = 0.0;
	
	/** The question paper. */
	private QuestionPaper questionPaper;
	
	//Non mapped attributes
	/** The question bank sub category name. */
	private String qbSubcategoryName = null;
	
	/** The question bank difficulty level name. */
	private String qbDifficultyLevelName = null;
	
	/** The question bank question type name. */
	private String qbQuestionTypeName = null;
	
	/** The question bank category name. */
	private String qbCategoryName = null;
	
	/** The question bank category id. */
	private Long qbCategoryId = null;
	
	/** The question text. */
	private String questionText=null;
	
	/**
	 * Gets the question paper question id.
	 *
	 * @return the questionPaperQuestionId
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "question_paper_question_id")
	public Long getQuestionPaperQuestionId() {
		return questionPaperQuestionId;
	}
	
	/**
	 * Sets the question paper question id.
	 *
	 * @param questionPaperQuestionId the questionPaperQuestionId to set
	 */
	public void setQuestionPaperQuestionId(Long questionPaperQuestionId) {
		this.questionPaperQuestionId = questionPaperQuestionId;
	}
	
	/**
	 * Gets the question paper.
	 *
	 * @return the questionPaper
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="question_paper_id", nullable=false)	
	@BatchSize(size=1000)
	public QuestionPaper getQuestionPaper() {
		return questionPaper;
	}
	
	/**
	 * Sets the question paper.
	 *
	 * @param questionPaper the questionPaper to set
	 */
	public void setQuestionPaper(QuestionPaper questionPaper) {
		this.questionPaper = questionPaper;
	}
	
	/**
	 * Gets the pattern type.
	 *
	 * @return the patternType
	 */
	@Column(name = "pattern_type")
	public String getPatternType() {
		return patternType;
	}
	
	/**
	 * Sets the pattern type.
	 *
	 * @param patternType the patternType to set
	 */
	public void setPatternType(String patternType) {
		this.patternType = patternType;
	}
	
	/**
	 * Gets the number of random questions.
	 *
	 * @return the numRandomQuestions
	 */
	@Column(name = "num_random_questions")
	public Long getNumRandomQuestions() {
		return numRandomQuestions;
	}
	
	/**
	 * Sets the number of random questions.
	 *
	 * @param numRandomQuestions the numRandomQuestions to set
	 */
	public void setNumRandomQuestions(Long numRandomQuestions) {
		this.numRandomQuestions = numRandomQuestions;
	}
	
	/**
	 * Gets the question bank question id.
	 *
	 * @return the qbQuestionId
	 */
	@Column(name = "qb_question_id")
	public Long getQbQuestionId() {
		return qbQuestionId;
	}
	
	/**
	 * Sets the question bank question id.
	 *
	 * @param qbQuestionId the qbQuestionId to set
	 */
	public void setQbQuestionId(Long qbQuestionId) {
		this.qbQuestionId = qbQuestionId;
	}
	
	/**
	 * Gets the question bank sub category id.
	 *
	 * @return the qbSubcategoryId
	 */
	@Column(name = "qb_subcategory_id")
	public Long getQbSubcategoryId() {
		return qbSubcategoryId;
	}
	
	/**
	 * Sets the question bank sub category id.
	 *
	 * @param qbSubcategoryId the qbSubcategoryId to set
	 */
	public void setQbSubcategoryId(Long qbSubcategoryId) {
		this.qbSubcategoryId = qbSubcategoryId;
	}
	
	/**
	 * Gets the question bank difficulty level id.
	 *
	 * @return the qbDifficultyLevelId
	 */
	@Column(name = "qb_difficulty_level_id")
	public Long getQbDifficultyLevelId() {
		return qbDifficultyLevelId;
	}
	
	/**
	 * Sets the question bank difficulty level id.
	 *
	 * @param qbDifficultyLevelId the qbDifficultyLevelId to set
	 */
	public void setQbDifficultyLevelId(Long qbDifficultyLevelId) {
		this.qbDifficultyLevelId = qbDifficultyLevelId;
	}
	
	/**
	 * Gets the question bank question type id.
	 *
	 * @return the qbQuestionTypeId
	 */
	@Column(name = "qb_question_type_id")
	public Long getQbQuestionTypeId() {
		return qbQuestionTypeId;
	}
	
	/**
	 * Sets the question bank question type id.
	 *
	 * @param qbQuestionTypeId the qbQuestionTypeId to set
	 */
	public void setQbQuestionTypeId(Long qbQuestionTypeId) {
		this.qbQuestionTypeId = qbQuestionTypeId;
	}
	
	/**
	 * Gets the marks.
	 *
	 * @return the marks
	 */
	@Column(name = "marks")
	public double getMarks() {
		return marks;
	}
	
	/**
	 * Sets the marks.
	 *
	 * @param marks the marks to set
	 */
	public void setMarks(double marks) {
		this.marks = marks;
	}
	
	
	/* (non-Javadoc)
	 * @see edu.amrita.aview.common.entities.Auditable#toString()
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(this.questionPaperQuestionId);
		sb.append(Constant.DELIMETER);
		sb.append(this.patternType);
		sb.append(Constant.DELIMETER);
		sb.append(this.numRandomQuestions);
		sb.append(Constant.DELIMETER);
		sb.append(this.qbQuestionId);
		sb.append(Constant.DELIMETER);
		sb.append(this.qbSubcategoryId);
		sb.append(Constant.DELIMETER);
		sb.append(this.qbDifficultyLevelId);
		sb.append(Constant.DELIMETER);
		sb.append(this.qbQuestionTypeId);
		sb.append(Constant.DELIMETER);
		sb.append(this.marks);
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
				+ ((qbDifficultyLevelId == null) ? 0 : qbDifficultyLevelId
						.hashCode());
		result = prime * result
				+ ((qbQuestionId == null) ? 0 : qbQuestionId.hashCode());
		result = prime
				* result
				+ ((qbQuestionTypeId == null) ? 0 : qbQuestionTypeId.hashCode());
		result = prime * result
				+ ((qbSubcategoryId == null) ? 0 : qbSubcategoryId.hashCode());
		result = prime * result
				+ ((questionPaper == null) ? 0 : questionPaper.getQuestionPaperId().hashCode());
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
		QuestionPaperQuestion other = (QuestionPaperQuestion) obj;
		if (qbDifficultyLevelId == null) {
			if (other.qbDifficultyLevelId != null)
				return false;
		} else if (!qbDifficultyLevelId.equals(other.qbDifficultyLevelId))
			return false;
		if (qbQuestionId == null) {
			if (other.qbQuestionId != null)
				return false;
		} else if (!qbQuestionId.equals(other.qbQuestionId))
			return false;
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
		if (questionPaper == null) {
			if (other.questionPaper != null)
				return false;
		} else if (!questionPaper.getQuestionPaperId().equals(other.questionPaper.getQuestionPaperId()))
			return false;
		return true;
	}
	
	/**
	 * Gets the question bank sub category name.
	 *
	 * @return the qbSubcategoryName
	 */
	@Transient
	public String getQbSubcategoryName() {
		return qbSubcategoryName;
	}
	
	/**
	 * Sets the question bank sub category name.
	 *
	 * @param qbSubcategoryName the qbSubcategoryName to set
	 */
	public void setQbSubcategoryName(String qbSubcategoryName) {
		this.qbSubcategoryName = qbSubcategoryName;
	}
	
	/**
	 * Gets the question bank difficulty level name.
	 *
	 * @return the qbDifficultyLevelName
	 */
	@Transient
	public String getQbDifficultyLevelName() {
		return qbDifficultyLevelName;
	}
	
	/**
	 * Sets the question bank difficulty level name.
	 *
	 * @param qbDifficultyLevelName the qbDifficultyLevelName to set
	 */
	public void setQbDifficultyLevelName(String qbDifficultyLevelName) {
		this.qbDifficultyLevelName = qbDifficultyLevelName;
	}
	
	/**
	 * Gets the question bank question type name.
	 *
	 * @return the qbQuestionTypeName
	 */
	@Transient
	public String getQbQuestionTypeName() {
		return qbQuestionTypeName;
	}
	
	/**
	 * Sets the question bank question type name.
	 *
	 * @param qbQuestionTypeName the qbQuestionTypeName to set
	 */
	public void setQbQuestionTypeName(String qbQuestionTypeName) {
		this.qbQuestionTypeName = qbQuestionTypeName;
	}
	
	/**
	 * Gets the question bank category name.
	 *
	 * @return the qbCategoryName
	 */
	@Transient
	public String getQbCategoryName() {
		return qbCategoryName;
	}
	
	/**
	 * Sets the question bank category name.
	 *
	 * @param qbCategoryName the qbCategoryName to set
	 */
	public void setQbCategoryName(String qbCategoryName) {
		this.qbCategoryName = qbCategoryName;
	}
	
	/**
	 * Gets the question bank category id.
	 *
	 * @return the qbCategoryId
	 */
	@Transient
	public Long getQbCategoryId() {
		return qbCategoryId;
	}
	
	/**
	 * Sets the question bank category id.
	 *
	 * @param qbCategoryId the qbCategoryId to set
	 */
	public void setQbCategoryId(Long qbCategoryId) {
		this.qbCategoryId = qbCategoryId;
	}
	
	/**
	 * Gets the question text.
	 *
	 * @return the question text
	 */
	@Transient
	public String getQuestionText() {
		return questionText;
	}
	
	/**
	 * Sets the question text.
	 *
	 * @param questionText the new question text
	 */
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	} 
	
	
	
}
