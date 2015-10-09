/*
 *@(#)QuizQuestion.java 4.0 2013/10/05
 *
 *Copyright  © 2013 E-Learning Research Lab, 
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "quiz_question")
public class QuizQuestion extends Auditable {

	/** The quiz question id. */
	private Long quizQuestionId = 0l ;
	
	/** The category id. */
	private Long categoryId = 0l ;
	
	/** The category name. */
	private String categoryName = null ;
	
	/** The subcategory id. */
	private Long subcategoryId = 0l ;
	
	/** The subcategory name. */
	private String subcategoryName = null ;
	
	/** The question type name. */
	private String questionTypeName = null ;
	
	/** The difficulty level name. */
	private String difficultyLevelName = null ;
	
	/** The question paper question id. */
	private Long questionPaperQuestionId = 0l ;
	
	/** The qb question id. */
	private Long qbQuestionId = 0l ;
	
	/** The question text hash. */
	private String questionTextHash = null ;
	
	/** The question text. */
	private String questionText = null ;
	
	/** The marks. */
	private Double marks = 0.0;
	
	/** The quiz. */
	private Quiz quiz;	
	
	/** The quiz answer choices. */
	private Set<QuizAnswerChoice> quizAnswerChoices = new HashSet<QuizAnswerChoice>();
	
	/** The quiz question media files. */
//	private Set<QuizQuestionMediaFile> quizQuestionMediaFiles = new HashSet<QuizQuestionMediaFile>();
	

	/**
	 * Update from.
	 *
	 * @param other the other
	 */
	public void updateFrom(QuizQuestion other)
	{
		this.quiz = other.quiz;
		this.categoryId=other.categoryId;
		this.categoryName=other.categoryName;
		this.subcategoryId=other.subcategoryId;
		this.subcategoryName=other.subcategoryName;
		this.questionTypeName=other.questionTypeName;
		this.difficultyLevelName = other.difficultyLevelName;
		this.questionPaperQuestionId=other.questionPaperQuestionId;
		this.qbQuestionId=other.qbQuestionId;
		this.questionTextHash=other.questionTextHash;
		this.questionText=other.questionText;
		this.setStatusId(other.getStatusId());
		
		Set<QuizAnswerChoice> tempQuizAnswerChoice = new HashSet<QuizAnswerChoice>();
		if(this.quizAnswerChoices != null)
        {
			tempQuizAnswerChoice.addAll(this.quizAnswerChoices);
            this.quizAnswerChoices.clear();
        }
        
		Set<QuizAnswerChoice> tempOtherQuizAnswerChoice = new HashSet<QuizAnswerChoice>();
        if(other.quizAnswerChoices != null)
        {
        	tempOtherQuizAnswerChoice.addAll(other.quizAnswerChoices);        	
        }
        super.mergeAssociations(tempQuizAnswerChoice, tempOtherQuizAnswerChoice);
        this.setQuizAnswerChoices(tempQuizAnswerChoice);
        
        Set<QuizQuestionMediaFile> tempQuizQuestionMediaFile = new HashSet<QuizQuestionMediaFile>();
		/*if(this.quizQuestionMediaFiles != null)
		{
			tempQuizQuestionMediaFile.addAll(this.quizQuestionMediaFiles);
			this.quizQuestionMediaFiles.clear();
		}	
		Set<QuizQuestionMediaFile> tempOtherQuizQuestionMediaFile = new HashSet<QuizQuestionMediaFile>();
		if(other.quizQuestionMediaFiles != null)
		{
			tempOtherQuizQuestionMediaFile.addAll(other.quizQuestionMediaFiles);
		}		
		super.mergeAssociations(tempQuizQuestionMediaFile, tempOtherQuizQuestionMediaFile);
		
		this.quizQuestionMediaFiles.addAll(tempQuizQuestionMediaFile);*/
	}

	/**
	 * Gets the quiz.
	 *
	 * @return the quiz
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="quiz_id", nullable=false)
	public Quiz getQuiz() {
		return quiz;
	}


	/**
	 * Sets the quiz.
	 *
	 * @param quiz the quiz to set
	 */
	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	
	/**
	 * Gets the quiz answer choices.
	 *
	 * @return the quizAnswerChoices
	 */
	@OneToMany(mappedBy="quizQuestion",fetch=FetchType.EAGER, cascade={CascadeType.ALL},orphanRemoval = true)	
	@BatchSize(size=1000)	
	public Set<QuizAnswerChoice> getQuizAnswerChoices() {
		return quizAnswerChoices;
	}

	/**
	 * Sets the quiz answer choices.
	 *
	 * @param quizAnswerChoices the quizAnswerChoices to set
	 */
	public void setQuizAnswerChoices(Set<QuizAnswerChoice> quizAnswerChoices) {
		this.quizAnswerChoices = quizAnswerChoices;
	}
	
	/**
	 * Adds the quiz answer choice.
	 *
	 * @param quizAnswerchoice the quiz answerchoice
	 */
	public synchronized void addQuizAnswerChoice(QuizAnswerChoice quizAnswerchoice)
	{
		if(this.quizAnswerChoices == null)
		{
			this.quizAnswerChoices = new HashSet<QuizAnswerChoice>();
		}
		quizAnswerchoice.setQuizQuestion(this);
		this.quizAnswerChoices.add(quizAnswerchoice);
	}
	
	/**
	 * Gets the quiz question media files.
	 * @return QuizQuestionMediaFile
	 */
	/*@OneToMany(mappedBy="quizQuestion",fetch=FetchType.EAGER,cascade={CascadeType.ALL},orphanRemoval=true)
  	@BatchSize(size=1000)
	public Set<QuizQuestionMediaFile> getQuizQuestionMediaFiles() {
		return quizQuestionMediaFiles;
	}*/

	/**
	 * Sets the quiz question media files.
	 * @param quizQuestionMediaFiles
	 */
	/*public void setQuizQuestionMediaFiles(
			Set<QuizQuestionMediaFile> quizQuestionMediaFiles) {
		this.quizQuestionMediaFiles = quizQuestionMediaFiles;
	}*/
	/**
	 * Adds the quiz question media file.
	 *
	 * @param quizQuestionMediaFile the quiz question media file
	 */
	/*public synchronized void addQuizQuestionMediaFiles(QuizQuestionMediaFile quizQuestionMediaFile)
	{
		if(this.quizQuestionMediaFiles == null)
		{
			this.quizQuestionMediaFiles = new HashSet<QuizQuestionMediaFile>();
		}
		quizQuestionMediaFile.setQuizQuestion(this) ;
		this.quizQuestionMediaFiles.add(quizQuestionMediaFile);
	}*/

	/**
	 * Gets the quiz question id.
	 *
	 * @return the quizQuestionId
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	 * Gets the category id.
	 *
	 * @return the categoryId
	 */
	@Column(name = "category_id")
	public Long getCategoryId() {
		return categoryId;
	}


	/**
	 * Sets the category id.
	 *
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}


	/**
	 * Gets the category name.
	 *
	 * @return the categoryName
	 */
	@Column(name = "category_name")
	public String getCategoryName() {
		return categoryName;
	}


	/**
	 * Sets the category name.
	 *
	 * @param categoryName the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}


	/**
	 * Gets the subcategory id.
	 *
	 * @return the subcategoryId
	 */
	@Column(name = "subcategory_id")
	public Long getSubcategoryId() {
		return subcategoryId;
	}


	/**
	 * Sets the subcategory id.
	 *
	 * @param subcategoryId the subcategoryId to set
	 */
	public void setSubcategoryId(long subcategoryId) {
		this.subcategoryId = subcategoryId;
	}


	/**
	 * Gets the subcategory name.
	 *
	 * @return the subcategoryName
	 */
	@Column(name = "subcategory_name")
	public String getSubcategoryName() {
		return subcategoryName;
	}

	/**
	 * Sets the subcategory name.
	 *
	 * @param subcategoryName the subcategoryName to set
	 */
	public void setSubcategoryName(String subcategoryName) {
		this.subcategoryName = subcategoryName;
	}

	/**
	 * Gets the question type name.
	 *
	 * @return the questionTypeName
	 */
	@Column(name = "question_type_name")
	public String getQuestionTypeName() {
		return questionTypeName;
	}

	/**
	 * Sets the question type name.
	 *
	 * @param questionTypeName the questionTypeName to set
	 */
	public void setQuestionTypeName(String questionTypeName) {
		this.questionTypeName = questionTypeName;
	}

	/**
	 * Gets the difficulty level name.
	 *
	 * @return the difficultyLevelName
	 */
	@Column(name = "difficulty_level_name")
	public String getDifficultyLevelName() {
		return difficultyLevelName;
	}

	/**
	 * Sets the difficulty level name.
	 *
	 * @param difficultyLevelName the difficultyLevelName to set
	 */
	public void setDifficultyLevelName(String difficultyLevelName) {
		this.difficultyLevelName = difficultyLevelName;
	}


	/**
	 * Gets the question paper question id.
	 *
	 * @return the questionPaperQuestionId
	 */
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
	 * Gets the qb question id.
	 *
	 * @return the qbQuestionId
	 */
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

	
	/* (non-Javadoc)
	 * @see edu.amrita.aview.common.entities.Auditable#toString()
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(this.quizQuestionId);
		sb.append(Constant.DELIMETER);
		sb.append(this.quiz.getQuizId());
		sb.append(Constant.DELIMETER);
		sb.append(this.categoryId);
		sb.append(Constant.DELIMETER);
		sb.append(this.categoryName);
		sb.append(Constant.DELIMETER);
		sb.append(this.subcategoryId);
		sb.append(Constant.DELIMETER);
		sb.append(this.subcategoryName);
		sb.append(Constant.DELIMETER);
		sb.append(this.questionTypeName);
		sb.append(Constant.DELIMETER);
		sb.append(this.difficultyLevelName);
		sb.append(Constant.DELIMETER);
		sb.append(this.questionPaperQuestionId);
		sb.append(Constant.DELIMETER);
		sb.append(this.qbQuestionId);
		sb.append(Constant.DELIMETER);
		sb.append(this.questionTextHash);
		sb.append(Constant.DELIMETER);
		sb.append(this.questionText);
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
		result = prime * result
				+ ((qbQuestionId == null) ? 0 : qbQuestionId.hashCode());
		result = prime * result + ((quiz == null) ? 0 : quiz.getQuizId().hashCode());
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
		QuizQuestion other = (QuizQuestion) obj;
		if (qbQuestionId == null) {
			if (other.qbQuestionId != null) {
				return false;
			}
		} else if (!qbQuestionId.equals(other.qbQuestionId)) {
			return false;
		}
		if (quiz == null) {
			if (other.quiz != null) {
				return false;
			}
		} else if (!quiz.getQuizId().equals(other.quiz.getQuizId())) {
			return false;
		}
		return true;
	}
	
	
	/* (non-Javadoc)
	 * @see edu.amrita.aview.common.entities.Auditable#setCreatedAuditData(java.lang.Long, java.sql.Timestamp, java.lang.Integer)
	 */
	public void setCreatedAuditData(Long createdUserId,Timestamp createdDate,Integer statusId)
	{
		super.setCreatedAuditData(createdUserId, createdDate, statusId);
		if(this.quizAnswerChoices != null)
		{
			for(QuizAnswerChoice answerChoice:quizAnswerChoices)
			{
				answerChoice.setCreatedAuditData(createdUserId, createdDate, statusId);
			}
		}		
		 /*if(this.quizQuestionMediaFiles != null)
	        {
	        	for(QuizQuestionMediaFile quizQuestionMediaFile : quizQuestionMediaFiles)
	    		{
	        		quizQuestionMediaFile.setCreatedAuditData(createdUserId, createdDate, statusId);
	    		}
	        }*/   
	}
	
	
	/* (non-Javadoc)
	 * @see edu.amrita.aview.common.entities.Auditable#setModifiedAuditData(java.lang.Long, java.sql.Timestamp)
	 */
	public void setModifiedAuditData(Long modifiedUserId,Timestamp modifedDate) throws AViewException
	{
		super.setModifiedAuditData(modifiedUserId, modifedDate);
		if(this.quizAnswerChoices != null)
		{
			for(QuizAnswerChoice answerChoice:quizAnswerChoices)
			{
				if(!answerChoice.getQuizAnswerChoiceId() .equals(new Long(0l))) //Update
				{
					answerChoice.setModifiedAuditData(modifiedUserId, modifedDate);
				}
				else
				{
					answerChoice.setCreatedAuditData(modifiedUserId, modifedDate, StatusHelper.getActiveStatusId());
				}
			}
		}
		
		/*if(this.quizQuestionMediaFiles != null)
	    {
	    	for(QuizQuestionMediaFile quizQuestionMediaFile : quizQuestionMediaFiles)
	        {
	            if(!quizQuestionMediaFile.getQuizQuestionMediaFileId().equals(new Long(0l))) //Update
	            {
	            	quizQuestionMediaFile.setModifiedAuditData(modifiedUserId, modifedDate);
	            }
	            else
	            {
	            	quizQuestionMediaFile.setCreatedAuditData(modifiedUserId, modifedDate, StatusHelper.getActiveStatusId());
	            }
	        }
	    }*/
	}
	
}
