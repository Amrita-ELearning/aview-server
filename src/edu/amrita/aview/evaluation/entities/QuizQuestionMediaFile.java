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

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;


@Entity
@Table(name = "quiz_question_media_file")
public class QuizQuestionMediaFile extends Auditable 
{
	private Long quizQuestionMediaFileId  = 0l ;
	private String quizQuestionMediaFileName = null;
	private QuizQuestion quizQuestion = null; 
	private String quizQuestionMediaFolderPath = null;
	private String quizQuestionMediaFileType=null;
	
	@ManyToOne (fetch=FetchType.EAGER)
	@JoinColumn(name="quiz_question_id", nullable=false)
	public QuizQuestion getQuizQuestion() {
		return quizQuestion;
	}
	
	public void setQuizQuestion(QuizQuestion quizQuestion) {
		this.quizQuestion = quizQuestion;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "quiz_question_media_file_id")
	public Long getQuizQuestionMediaFileId() {
		return quizQuestionMediaFileId;
	}


	public void setQuizQuestionMediaFileId(Long quizQuestionMediaFileId) {
		this.quizQuestionMediaFileId = quizQuestionMediaFileId;
	}

	@Column(name = "quiz_question_media_file_name")
	public String getQuizQuestionMediaFileName() {
		return quizQuestionMediaFileName;
	}

	public void setQuizQuestionMediaFileName(String quizQuestionMediaFileName) {
		this.quizQuestionMediaFileName = quizQuestionMediaFileName;
	}


	@Column(name = "quiz_question_media_folder_path")
	public String getQuizQuestionMediaFolderPath() {
		return quizQuestionMediaFolderPath;
	}

	public void setQuizQuestionMediaFolderPath(String quizQuestionMediaFolderPath) {
		this.quizQuestionMediaFolderPath = quizQuestionMediaFolderPath;
	}

	

	@Column(name = "quiz_question_media_file_type")
	public String getQuizQuestionMediaFileType() {
		return quizQuestionMediaFileType;
	}

	public void setQuizQuestionMediaFileType(String quizQuestionMediaFileType) {
		this.quizQuestionMediaFileType = quizQuestionMediaFileType;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((quizQuestion == null) ? 0 : quizQuestion.getQuizQuestionId().hashCode());
		result = prime
				* result
				+ ((quizQuestionMediaFileName == null) ? 0
						: quizQuestionMediaFileName.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QuizQuestionMediaFile other = (QuizQuestionMediaFile) obj;
		if (quizQuestion == null) {
			if (other.quizQuestion != null)
				return false;
		} else if (!quizQuestion.getQuizQuestionId().equals(other.quizQuestion.getQuizQuestionId()))
			return false;
		if (quizQuestionMediaFileName == null) {
			if (other.quizQuestionMediaFileName != null)
				return false;
		} else if (!quizQuestionMediaFileName
				.equals(other.quizQuestionMediaFileName))
			return false;
		return true;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.quizQuestionMediaFileId);
		sb.append(Constant.DELIMETER);
		sb.append(this.quizQuestionMediaFileName);
		sb.append(Constant.DELIMETER);
		if(this.quizQuestion != null)
		{
			sb.append(this.quizQuestion.getQuizQuestionId());		
			sb.append(Constant.DELIMETER);
		}
		sb.append(this.quizQuestionMediaFolderPath);
		sb.append(Constant.DELIMETER);
		sb.append(this.quizQuestionMediaFileType);
		sb.append(Constant.DELIMETER);
		sb.append(super.toString());
		sb.append(Constant.DELIMETER);
		return sb.toString();
	}
}