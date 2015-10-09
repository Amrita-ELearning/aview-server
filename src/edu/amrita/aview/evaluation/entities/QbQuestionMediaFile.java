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
@Table(name = "qb_question_media_file")
public class QbQuestionMediaFile extends Auditable 
{
	private Long qbQuestionMediaFileId  = 0l ;
	private String qbQuestionMediaFileName = null;
	private QbQuestion qbQuestion = null; 
	private String qbQuestionMediaFolderPath = null;
	private String qbQuestionMediaFileType=null;

	/**
	 * @return the qbQuestionMediaFileId
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "qb_question_media_file_id")
	public Long getQbQuestionMediaFileId() {
		return qbQuestionMediaFileId;
	}

	/**
	 * @param  qbQuestionMediaFileId the qbQuestionMediaFileId to set
	 */
	public void setQbQuestionMediaFileId(Long qbQuestionMediaFileId) {
		this.qbQuestionMediaFileId = qbQuestionMediaFileId;
	}


	/**
	 * @return the qbQuestionMediaFileName
	 */
	@Column(name = "qb_question_media_file_name")
	public String getQbQuestionMediaFileName() {
		return qbQuestionMediaFileName;
	}

	/**
	 * @param qbQuestionMediaFileName the qbQuestionMediaFileName to set
	 */
	public void setQbQuestionMediaFileName(String qbQuestionMediaFileName) {
		this.qbQuestionMediaFileName = qbQuestionMediaFileName;
	}


	/**
	 * @return the qbQuestionMediaFolderPath
	 */
	@Column(name = "qb_question_media_folder_path")
	public String getQbQuestionMediaFolderPath() {
		return qbQuestionMediaFolderPath;
	}

	/**
	 * @param qbQuestionMediaFolderPath the qbQuestionMediaFolderPath to set
	 */
	public void setQbQuestionMediaFolderPath(String qbQuestionMediaFolderPath) {
		this.qbQuestionMediaFolderPath = qbQuestionMediaFolderPath;
	}

	/**
	 * @return the qbQuestionMediaFileType
	 */
	@Column(name = "qb_question_media_file_type")
	public String getQbQuestionMediaFileType() {
		return qbQuestionMediaFileType;
	}
	/**
	 * @param qbQuestionMediaFileType the qbQuestionMediaFileType to set
	 */
	public void setQbQuestionMediaFileType(String qbQuestionMediaFileType) {
		this.qbQuestionMediaFileType = qbQuestionMediaFileType;
	}
	
	@ManyToOne (fetch=FetchType.EAGER)
	@JoinColumn(name="qb_question_id", nullable=false)
	public QbQuestion getQbQuestion() {
		return qbQuestion;
	}

	public void setQbQuestion(QbQuestion qbQuestion) {
		this.qbQuestion = qbQuestion;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.qbQuestionMediaFileId);
		sb.append(Constant.DELIMETER);
		sb.append(this.qbQuestionMediaFileName);
		sb.append(Constant.DELIMETER);
		if(this.qbQuestion != null)
		{
			sb.append(this.qbQuestion.getQbQuestionId());		
			sb.append(Constant.DELIMETER);
		}
		sb.append(this.qbQuestionMediaFolderPath);
		sb.append(Constant.DELIMETER);
		sb.append(this.qbQuestionMediaFileType);
		sb.append(Constant.DELIMETER);
		sb.append(super.toString());
		sb.append(Constant.DELIMETER);
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((qbQuestion == null) ? 0 : qbQuestion.getQbQuestionId().hashCode());
		result = prime
				* result
				+ ((qbQuestionMediaFileName == null) ? 0
						: qbQuestionMediaFileName.hashCode());
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
		QbQuestionMediaFile other = (QbQuestionMediaFile) obj;
		if (qbQuestion == null) {
			if (other.qbQuestion != null)
				return false;
		} else if (!qbQuestion.getQbQuestionId().equals(other.qbQuestion.getQbQuestionId()))
			return false;
		if (qbQuestionMediaFileName == null) {
			if (other.qbQuestionMediaFileName != null)
				return false;
		} else if (!qbQuestionMediaFileName
				.equals(other.qbQuestionMediaFileName))
			return false;
		return true;
	}
}