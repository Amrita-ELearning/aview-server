/*
 * 
 */
package edu.amrita.aview.documentsharing.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;


/**
 * The Class DocumentConversion.
 *
 * @author rameshg
 */
@Entity
@Table(name = "document_conversion")
@BatchSize(size=1000)
public class DocumentConversion extends Auditable {
	
	/** The document conversion id. */
	private Long documentConversionId = 0l;
	
	/** The user id. */
	private Long userId = 0l;
	
	/** The document name. */
	private String documentName = null;
	
	/** The document path. */
	private String documentPath = null;
	
	/** The content server id. */
	private Integer contentServerId = 0;
	
	/** The conversion status. */
	private String conversionStatus = null;
	
	/** The conversion message. */
	private String conversionMessage = null;
	
	/** The progress pct. */
	private Integer progressPct = 0;
	
	/** The is animated document. */
	private String isAnimatedDocument = null;

	/**
	 * Gets the document conversion id.
	 *
	 * @return the document conversion id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "document_conversion_id")
	public Long getDocumentConversionId() {
		return documentConversionId;
	}
	
	/**
	 * Sets the document conversion id.
	 *
	 * @param documentConversionId the document conversion id
	 */
	public void setDocumentConversionId(Long documentConversionId) {
		this.documentConversionId = documentConversionId;
	}

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	@Column(name = "user_id")
	public Long getUserId() {
		return userId;
	}
	
	/**
	 * Sets the user id.
	 *
	 * @param userId the user id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Gets the document name.
	 *
	 * @return the document name
	 */
	@Column(name = "document_name")
	public String getDocumentName() {
		return documentName;
	}
	
	/**
	 * Sets the document name.
	 *
	 * @param documentName the document name
	 */
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	/**
	 * Gets the document path.
	 *
	 * @return the document path
	 */
	@Column(name = "document_path")
	public String getDocumentPath() {
		return documentPath;
	}
	
	/**
	 * Sets the document path.
	 *
	 * @param documentPath the document path
	 */
	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}
	
	/**
	 * Gets the is animated document.
	 *
	 * @return the checks if is animated document
	 */
	@Column(name = "is_animated_document")
	public String getIsAnimatedDocument() {
		return isAnimatedDocument;
	}
	
	/**
	 * Sets the is animated document.
	 *
	 * @param isAnimatedDocument the checks if is animated document
	 */
	public void setIsAnimatedDocument(String isAnimatedDocument) {
		this.isAnimatedDocument = isAnimatedDocument;
	}

	/**
	 * Gets the content server id.
	 *
	 * @return the content server id
	 */
	@Column(name = "content_server_id")
	public Integer getContentServerId() {
		return contentServerId;
	}
	
	/**
	 * Sets the content server id.
	 *
	 * @param conversionServerId the content server id
	 */
	public void setContentServerId(Integer conversionServerId) {
		this.contentServerId = conversionServerId;
	}

	/**
	 * Gets the conversion status.
	 *
	 * @return the conversion status
	 */
	@Column(name = "conversion_status")
	public String getConversionStatus() {
		return conversionStatus;
	}
	
	/**
	 * Sets the conversion status.
	 *
	 * @param conversionStatus the conversion status
	 */
	public void setConversionStatus(String conversionStatus) {
		this.conversionStatus = conversionStatus;
	}

	/**
	 * Gets the conversion message.
	 *
	 * @return the conversion message
	 */
	@Column(name = "conversion_message")
	public String getConversionMessage() {
		return conversionMessage;
	}
	
	/**
	 * Sets the conversion message.
	 *
	 * @param conversionMessage the conversion message
	 */
	public void setConversionMessage(String conversionMessage) {
		this.conversionMessage = conversionMessage;
	}

	/**
	 * Gets the progress pct.
	 *
	 * @return the progress pct
	 */
	@Column(name = "progress_pct")
	public Integer getProgressPct() {
		return progressPct;
	}
	
	/**
	 * Sets the progress pct.
	 *
	 * @param progressPct the progress pct
	 */
	public void setProgressPct(Integer progressPct) {
		this.progressPct = progressPct;
	}

	/* (non-Javadoc)
	 * @see edu.amrita.aview.common.entities.Auditable#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.documentConversionId);
		sb.append(Constant.DELIMETER);
		sb.append(this.userId);
		sb.append(Constant.DELIMETER);
		sb.append(this.contentServerId);
		sb.append(Constant.DELIMETER);
		sb.append(this.documentName);	
		sb.append(Constant.DELIMETER);
		sb.append(this.documentPath);	
		sb.append(Constant.DELIMETER);
		sb.append(this.isAnimatedDocument);	
		sb.append(Constant.DELIMETER);
		sb.append(this.conversionStatus);	
		sb.append(Constant.DELIMETER);
		sb.append(this.conversionMessage);	
		sb.append(Constant.DELIMETER);
		sb.append(this.progressPct);	
		sb.append(Constant.DELIMETER);
		sb.append(super.toString());	
		return sb.toString();
	}
	
	/**
	 * Creates the from.
	 *
	 * @param other the other
	 */
	public void createFrom(DocumentConversion other)
	{
		super.createFrom(other);
		this.contentServerId = other.contentServerId;
		this.userId = other.userId;
		this.documentName = other.documentName;
		this.documentPath = other.documentPath;
		this.isAnimatedDocument = other.isAnimatedDocument;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((documentConversionId == null) ? 0 : documentConversionId.hashCode());
		result = prime * result
				+ ((documentName == null) ? 0 : documentName.hashCode());
		result = prime * result
				+ ((documentPath == null) ? 0 : documentPath.hashCode());
		result = prime * result
				+ ((isAnimatedDocument == null) ? 0 : isAnimatedDocument.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		if (!(obj instanceof DocumentConversion))
			return false;
		DocumentConversion other = (DocumentConversion) obj;
		if (documentConversionId == null || documentConversionId == 0) {
			if (other.documentConversionId != null && documentConversionId != 0)
				return false;
		} else if (!documentConversionId.equals(other.documentConversionId))
			return false;
		if (documentName == null) {
			if (other.documentName != null)
				return false;
		} else if (!documentName.equals(other.documentName))
			return false;
		if (documentPath == null) {
			if (other.documentPath != null)
				return false;
		} else if (!documentPath.equals(other.documentPath))
			return false;
		if (isAnimatedDocument == null) {
			if (other.isAnimatedDocument != null)
				return false;
		} else if (!isAnimatedDocument.equals(other.isAnimatedDocument))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
}
