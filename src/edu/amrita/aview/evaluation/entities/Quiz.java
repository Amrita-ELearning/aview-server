/*
 * @(#)Quiz.java 4.0 2013/10/04
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
@Table(name = "quiz")
public class Quiz extends Auditable
{
	
	/** The quiz id. */
	private Long quizId = 0l;
	
	/** The quiz name. */
	private String quizName = null ;
	
	/** The class id. */
	private Long classId = 0l;
	
	/** The question paper id. */
	private Long questionPaperId = 0l ;
	
	/** The total marks. */
	private double totalMarks = 0.0;
	
	/** The time open. */
	private Timestamp timeOpen = null ;
	
	/** The time close. */
	private Timestamp timeClose = null ;
	
	/** The duration seconds. */
	private Long durationSeconds = 0l ;
	
	/** The quiz status. */
	private String quizStatus = null ;
	
	/** The quiz type. */
	private String quizType = null ;
	
	/** The institute id. */
	private Long instituteId = 0l ;
	
	/** The quiz questions. */
	private Set<QuizQuestion> quizQuestions = new HashSet<QuizQuestion>();
	
	// Non mapped attributes
	/** The question paper name. */
	private String questionPaperName = null ;
	
	/** The class name. */
	private String className = null;
	
	/** The course id. */
	private Long courseId = 0l;
	
	/** The course name. */
	private String courseName = null;
	
	/** The user name. */
	private String userName = null;
	
	/** The count quiz question id. */
	private Long countQuizQuestionId = 0l;
	
	/** The answer choice count. */
	private Long answerChoiceCount=0l;
	
	/** The percentage. */
	private  double percentage = 0.0f ;
	
	/** The attempted questions. */
	private Integer attemptedQuestions = 0 ;
	
	/** The user id. */
	private Long userId = 0l ;
	
	/** The score. */
	private double score = 0.0;
	
	/** The total score. */
	private double totalScore = 0.0;
	
	/** The fraction. */
	private Long fraction = 0l;
	
	/** The choice text. */
	private String choiceText = null;
	
	/** The quiz answer choice id. */
	private Long quizAnswerChoiceId = 0l;
	
	/** The quiz question id. */
	private Long quizQuestionId = 0l ;
	
	/**
	 * Update from.
	 *
	 * @param other the other
	 */
	public void updateFrom(Quiz other)
	{
		this.quizName=other.quizName;
		this.classId=other.classId;
		this.questionPaperId=other.questionPaperId;
		this.totalMarks=other.totalMarks;
		this.timeOpen=other.timeOpen;
		this.timeClose=other.timeClose;
		this.durationSeconds = other.durationSeconds;
		this.quizStatus=other.quizStatus;
		this.quizType=other.quizType;
		this.instituteId=other.instituteId;
		this.setStatusId(other.getStatusId());
		Set<QuizQuestion> tempQuizQuestion = new HashSet<QuizQuestion>();
		if(this.quizQuestions != null)
        {
			tempQuizQuestion.addAll(this.quizQuestions);
            this.quizQuestions.clear();
        }
        
		Set<QuizQuestion> tempOtherQuizQuestion = new HashSet<QuizQuestion>();
        if(other.quizQuestions != null)
        {
        	tempOtherQuizQuestion.addAll(other.quizQuestions);     
        	other.quizQuestions.clear();
        }
		super.mergeAssociations(tempQuizQuestion, tempOtherQuizQuestion);
		this.quizQuestions.addAll(tempQuizQuestion);
		
	}
	
	/**
	 * Gets the quiz question.
	 *
	 * @return the quizQuestion
	 */	
	@OneToMany(mappedBy="quiz" ,fetch=FetchType.EAGER, 
			cascade={CascadeType.ALL},orphanRemoval = true)	
	@BatchSize(size=1000)	
	public Set<QuizQuestion> getQuizQuestion() {
		return quizQuestions;
	}
	
	/**
	 * Sets the quiz question.
	 *
	 * @param quizQuestion the quizQuestion to set
	 */
	public void setQuizQuestion(Set<QuizQuestion> quizQuestion) {
		this.quizQuestions = quizQuestion;
	}
	
	/**
	 * Adds the quiz question.
	 *
	 * @param quizQuestion the quiz question
	 */
	public synchronized void addQuizQuestion(QuizQuestion quizQuestion)
	{
		if(this.quizQuestions == null)
		{
			this.quizQuestions = new HashSet<QuizQuestion>();
		}
		quizQuestion.setQuiz(this);
		this.quizQuestions.add(quizQuestion);
	}
	
	/**
	 * Gets the quiz id.
	 *
	 * @return the quizId
	 */		
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	 * Gets the quiz name.
	 *
	 * @return the quizName
	 */
	@Column(name = "quiz_name")
	public String getQuizName() {
		return quizName;
	}
	
	/**
	 * Sets the quiz name.
	 *
	 * @param quizName the quizName to set
	 */
	public void setQuizName(String quizName) {
		this.quizName = quizName;
	}
	
	/**
	 * Gets the class id.
	 *
	 * @return the classId
	 */	
	@Column(name = "class_id")
	public Long getClassId() {
		return classId;
	}
	
	/**
	 * Sets the class id.
	 *
	 * @param classId the classId to set
	 */
	public void setClassId(Long classId) {
		this.classId = classId;
	}
	
	/**
	 * Gets the question paper id.
	 *
	 * @return the questionPaperId
	 */
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
	 * Gets the total marks.
	 *
	 * @return the totalMarks
	 */
	@Column(name = "total_marks")
	public double getTotalMarks() {
		return totalMarks;
	}
	
	/**
	 * Sets the total marks.
	 *
	 * @param totalMarks the totalMarks to set
	 */
	public void setTotalMarks(double totalMarks) {
		this.totalMarks = totalMarks;
	}	
	
	/**
	 * Gets the time open.
	 *
	 * @return the timeOpen
	 */	
	@Column(name = "time_open")
	public Timestamp getTimeOpen() {
		return timeOpen;
	}
	
	/**
	 * Sets the time open.
	 *
	 * @param timeOpen the timeOpen to set
	 */
	public void setTimeOpen(Timestamp timeOpen) {
		this.timeOpen = timeOpen;
	}
	
	/**
	 * Gets the time close.
	 *
	 * @return the timeClose
	 */
	@Column(name = "time_close")
	public Timestamp getTimeClose() {
		return timeClose;
	}
	
	/**
	 * Sets the time close.
	 *
	 * @param timeClose the timeClose to set
	 */
	public void setTimeClose(Timestamp timeClose) {
		this.timeClose = timeClose;
	}
	
	/**
	 * Gets the duration seconds.
	 *
	 * @return the durationSeconds
	 */
	@Column(name = "duration_seconds")
	public Long getDurationSeconds() {
		return durationSeconds;
	}
	
	/**
	 * Sets the duration seconds.
	 *
	 * @param durationSeconds the durationSeconds to set
	 */
	public void setDurationSeconds(Long durationSeconds) {
		this.durationSeconds = durationSeconds;
	}
	
	/**
	 * Gets the quiz status.
	 *
	 * @return the quizStatus
	 */
	@Column(name = "quiz_status")
	public String getQuizStatus() {
		return quizStatus;
	}
	
	/**
	 * Sets the quiz status.
	 *
	 * @param quizStatus the quizStatus to set
	 */
	public void setQuizStatus(String quizStatus) {
		this.quizStatus = quizStatus;
	}
	
	/**
	 * Gets the quiz type.
	 *
	 * @return the quizType
	 */
	@Column(name = "quiz_type")
	public String getQuizType() {
		return quizType;
	}
	
	/**
	 * Sets the quiz type.
	 *
	 * @param quizType the quizType to set
	 */
	public void setQuizType(String quizType) {
		this.quizType = quizType;
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
		sb.append(this.quizId);
		sb.append(Constant.DELIMETER);
		sb.append(this.quizName);
		sb.append(Constant.DELIMETER);
		sb.append(this.classId);
		sb.append(Constant.DELIMETER);
		sb.append(this.questionPaperId);
		sb.append(Constant.DELIMETER);
		sb.append(this.totalMarks);
		sb.append(Constant.DELIMETER);
		sb.append(this.timeOpen);
		sb.append(Constant.DELIMETER);
		sb.append(this.timeClose);
		sb.append(Constant.DELIMETER);
		sb.append(this.durationSeconds);
		sb.append(Constant.DELIMETER);
		sb.append(this.quizStatus);
		sb.append(Constant.DELIMETER);		
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
		result = prime * result
				+ ((quizName == null) ? 0 : quizName.hashCode());
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
		Quiz other = (Quiz) obj;
		if (getCreatedByUserId() == null) {
			if (other.getCreatedByUserId() != null) {
				return false;
			}
		} else if (!getCreatedByUserId().equals(other.getCreatedByUserId())) {
			return false;
		}
		if (quizName == null) {
			if (other.quizName != null) {
				return false;
			}
		} else if (!quizName.equals(other.quizName)) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * Gets the user name.
	 *
	 * @return the userName
	 */
	@Transient
	public String getUserName() {
		return userName;
	}
	
	/**
	 * Sets the user name.
	 *
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}	
	
	/**
	 * Gets the count quiz question id.
	 *
	 * @return the count of quiz questions
	 */
	@Transient
	public Long getCountQuizQuestionId() {
		return countQuizQuestionId;
	}

	/**
	 * Sets the count quiz question id.
	 *
	 * @param countQuizQuestionId the countQuizQuestionId to set
	 */
	public void setCountQuizQuestionId(Long countQuizQuestionId) {
		this.countQuizQuestionId = countQuizQuestionId;
	}

	/**
	 * Gets the score.
	 *
	 * @return the score
	 */
	@Transient
	public double getScore() {
		return score;
	}
	
	/**
	 * Sets the score.
	 *
	 * @param score the score to set
	 */
	public void setScore(double score) {
		this.score = score;
	}
	
	
	
	/**
	 * Gets the total score.
	 *
	 * @return the totalScore
	 */
	@Transient
	public double getTotalScore() {
		return totalScore;
	}

	/**
	 * Sets the total score.
	 *
	 * @param totalScore the totalScore to set
	 */
	public void setTotalScore(double totalScore) {
		this.totalScore = totalScore;
	}

	/**
	 * Gets the fraction.
	 *
	 * @return the fraction
	 */
	@Transient
	public long getFraction() {
		return fraction;
	}
	
	/**
	 * Sets the fraction.
	 *
	 * @param fraction the fraction to set
	 */
	public void setFraction(long fraction) {
		this.fraction = fraction;
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
	 * Gets the quiz answer choice id.
	 *
	 * @return the quizAnswerChoiceId
	 */
	@Transient
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
	 * Gets the quiz question id.
	 *
	 * @return the quizQuestionId
	 */
	@Transient
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
	 * Gets the question paper name.
	 *
	 * @return the question paper name
	 */
	@Transient
	public String getQuestionPaperName() {
		return questionPaperName;
	}
	
	/**
	 * Sets the question paper name.
	 *
	 * @param questionPaperName the new question paper name
	 */
	public void setQuestionPaperName(String questionPaperName) {
		this.questionPaperName = questionPaperName;
	}
	
	/**
	 * Gets the class name.
	 *
	 * @return the class name
	 */
	@Transient
	public String getClassName() {
		return className;
	}
	
	/**
	 * Sets the class name.
	 *
	 * @param className the new class name
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	
	/**
	 * Gets the course id.
	 *
	 * @return the course id
	 */
	@Transient
	public Long getCourseId() {
		return courseId;
	}
	
	/**
	 * Sets the course id.
	 *
	 * @param courseId the new course id
	 */
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	
	/**
	 * Gets the course name.
	 *
	 * @return the course name
	 */
	@Transient
	public String getCourseName() {
		return courseName;
	}
	
	/**
	 * Sets the course name.
	 *
	 * @param courseName the new course name
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	
	/**
	 * Gets the answer choice count.
	 *
	 * @return the answer choice count
	 */
	@Transient
	public Long getAnswerChoiceCount() {
		return answerChoiceCount;
	}
	
	/**
	 * Sets the answer choice count.
	 *
	 * @param answerChoiceCount1 the new answer choice count
	 */
	public void setAnswerChoiceCount(Long answerChoiceCount1) {
		answerChoiceCount = answerChoiceCount1;
	}
	
	/**
	 * Gets the percentage.
	 *
	 * @return the percentage
	 */
	@Transient
	public double getPercentage() {
		return percentage;
	}

	/**
	 * Sets the percentage.
	 *
	 * @param percentage the percentage to set
	 */
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	/**
	 * Gets the attempted questions.
	 *
	 * @return the attemptedQuestions
	 */
	@Transient
	public Integer getAttemptedQuestions() {
		return attemptedQuestions;
	}

	/**
	 * Sets the attempted questions.
	 *
	 * @param attemptedQuestions the attemptedQuestions to set
	 */
	public void setAttemptedQuestions(Integer attemptedQuestions) {
		this.attemptedQuestions = attemptedQuestions;
	}

	/**
	 * Gets the user id.
	 *
	 * @return the userId
	 */
	@Transient
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

	
	/* (non-Javadoc)
	 * @see edu.amrita.aview.common.entities.Auditable#setCreatedAuditData(java.lang.Long, java.sql.Timestamp, java.lang.Integer)
	 */
	public void setCreatedAuditData(Long createdUserId,Timestamp createdDate,Integer statusId)
	{
		super.setCreatedAuditData(createdUserId, createdDate, statusId);
		if(this.quizQuestions != null)
		{
			for(QuizQuestion question:quizQuestions)
			{
				question.setCreatedAuditData(createdUserId, createdDate, statusId);
				if(question.getQuizAnswerChoices() != null)
				{
					for(QuizAnswerChoice quizAnswerChoice : question.getQuizAnswerChoices())
					{
						quizAnswerChoice.setCreatedAuditData(createdUserId, createdDate, statusId);
					}
				}
				/*if(question.getQuizQuestionMediaFiles()!= null)
				{
					for(QuizQuestionMediaFile quizQuestionMediaFile : question.getQuizQuestionMediaFiles())
					{
						quizQuestionMediaFile.setCreatedAuditData(createdUserId, createdDate, statusId);
					}
				}*/
			}
		}
		
	}
	
	
	/* (non-Javadoc)
	 * @see edu.amrita.aview.common.entities.Auditable#setModifiedAuditData(java.lang.Long, java.sql.Timestamp)
	 */
	public void setModifiedAuditData(Long modifiedUserId,Timestamp modifedDate) throws AViewException
	{
		super.setModifiedAuditData(modifiedUserId, modifedDate);
		if(this.quizQuestions != null)
		{
			for(QuizQuestion question:quizQuestions)
			{
				if(question.getQuizQuestionId() != 0) //Update
				{
					question.setModifiedAuditData(modifiedUserId, modifedDate);
					if(question.getQuizAnswerChoices() != null)
					{
						for(QuizAnswerChoice quizAnswerChoice : question.getQuizAnswerChoices())
						{
							if(quizAnswerChoice.getQuizAnswerChoiceId() != 0)
							{
								quizAnswerChoice.setModifiedAuditData(modifiedUserId, modifedDate);
							}
							else
							{
								quizAnswerChoice.setCreatedAuditData(modifiedUserId, modifedDate, StatusHelper.getActiveStatusId ());
							}
						}
					}
					/*if(question.getQuizQuestionMediaFiles() != null)
					{
						for(QuizQuestionMediaFile quizQuestionMediaFile : question.getQuizQuestionMediaFiles())
						{
							if(quizQuestionMediaFile.getQuizQuestionMediaFileId() != 0)
							{
								quizQuestionMediaFile.setModifiedAuditData(modifiedUserId, modifedDate);
							}
							else
							{
								quizQuestionMediaFile.setCreatedAuditData(modifiedUserId, modifedDate, StatusHelper.getActiveStatusId ());
							}
						}
					}*/
				}
				else 
				{
					question.setCreatedAuditData(modifiedUserId, modifedDate, StatusHelper.getActiveStatusId ());
				}
			}
		}				
	}
}
