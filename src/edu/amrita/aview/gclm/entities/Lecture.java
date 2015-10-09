/*
 * 
 */
package edu.amrita.aview.gclm.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;

import org.hibernate.annotations.GenericGenerator;

/**
 * The Class Lecture.
 */
@Entity
@Table(name = "lecture")
public class Lecture extends Auditable {

	/** The lecture id. */
	private Long lectureId = 0l ;
	
	/** The lecture name. */
	private String lectureName = null;
	
	/** The start date. */
	private Date startDate = null ; 
	
	/** The recorded presenter video url. */
	private String recordedPresenterVideoUrl = null;
	
	/** The recorded viewer video url. */
	private String recordedViewerVideoUrl = null;
	
	/** The recorded video file path. */
	private String recordedVideoFilePath = null ;
	
	/** The recorded content url. */
	private String recordedContentUrl = null ;
	
	/** The recorded content file path. */
	private String recordedContentFilePath = null ;	
	
	/** The class id. */
	private Long classId = 0l ;
	
	/** The keywords. */
	private String keywords = null ;	
	
	/** The lecture number. */
	private Integer lectureNumber = 0 ;
	
	/** The start time. */
	private Date startTime = null ;
	
	/** The end time. */
	private Date endTime = null ;
	
	/** The recorded desktop video url. */
	private String recordedDesktopVideoUrl = null;
	
	

	//Non mapped attributes
	/** The class name. */
	private String className = null;
	
	/** The course name. */
	private String courseName = null;
	
	/** The institute name. */
	private String instituteName = null;
	private String displayName = null;
	
	/**
	 * Update from.
	 *
	 * @param other the other
	 */
	public void updateFrom(Lecture other)
	{
		this.lectureName = other.lectureName;
		this.startDate = other.startDate ; 
		this.recordedPresenterVideoUrl = other.recordedPresenterVideoUrl;
		this.recordedViewerVideoUrl = other.recordedViewerVideoUrl;
		this.recordedVideoFilePath = other.recordedVideoFilePath ;
		this.recordedContentUrl = other.recordedContentUrl ;
		this.recordedContentFilePath = other.recordedContentFilePath ;	
		this.recordedDesktopVideoUrl = other.recordedDesktopVideoUrl ;	
		this.classId = other.classId ;
		this.keywords = other.keywords ;	
		this.lectureNumber = other.lectureNumber ;
		this.startTime = other.startTime ;
		this.endTime = other.endTime ;
		this.displayName = other.displayName;
	}
	
	/**
	 * Gets the lecture id.
	 * CustomIdGenerator is setted for give id as a optional field for API's
	 * @return the lectureId
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY,generator="CustomIdGenerator")
	@GenericGenerator(name="CustomIdGenerator", strategy="edu.amrita.aview.common.utils.CustomIdGenerator")
	@Column(name = "lecture_id")
	public Long getLectureId() {
		return lectureId;
	}
	
	/**
	 * Sets the lecture id.
	 *
	 * @param lectureId the lectureId to set
	 */
	public void setLectureId(Long lectureId) {
		this.lectureId = lectureId;
	}

	/**
	 * Gets the lecture name.
	 *
	 * @return the lectureName
	 */
	@Column(name = "lecture_name")
	public String getLectureName() {
		return lectureName;
	}

	/**
	 * Sets the lecture name.
	 *
	 * @param lectureName the lectureName to set
	 */
	public void setLectureName(String lectureName) {
		this.lectureName = lectureName;
		this.displayName = this.getDisplayName();
	}

	/**
	 * Gets the start date.
	 *
	 * @return the startDate
	 */
	@Column(name = "date")
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Sets the start date.
	 *
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Gets the recorded desktop video url.
	 *
	 * @return the recordedDesktopVideoUrl
	 */
	@Column(name = "recorded_desktop_url")
	public String getRecordedDesktopVideoUrl() {
		return recordedDesktopVideoUrl;
	}

	/**
	 * Sets the recorded desktop video url.
	 *
	 * @param recordedDesktopVideoUrl the recordedDesktopVideoUrl to set
	 */
	public void setRecordedDesktopVideoUrl(String recordedDesktopVideoUrl) {
		this.recordedDesktopVideoUrl = recordedDesktopVideoUrl;
	}

	/**
	 * Gets the recorded presenter video url.
	 *
	 * @return the recordedPresenterVideoUrl
	 */
	@Column(name = "recorded_presenter_video_url")
	public String getRecordedPresenterVideoUrl() {
		return recordedPresenterVideoUrl;
	}

	/**
	 * Sets the recorded presenter video url.
	 *
	 * @param recordedPresenterVideoUrl the recordedPresenterVideoUrl to set
	 */
	public void setRecordedPresenterVideoUrl(String recordedPresenterVideoUrl) {
		this.recordedPresenterVideoUrl = recordedPresenterVideoUrl;
	}

	/**
	 * Gets the recorded viewer video url.
	 *
	 * @return the recordedViewerVideoUrl
	 */
	@Column(name = "recorded_viewer_video_url")
	public String getRecordedViewerVideoUrl() {
		return recordedViewerVideoUrl;
	}

	/**
	 * Sets the recorded viewer video url.
	 *
	 * @param recordedViewerVideoUrl the recordedViewerVideoUrl to set
	 */
	public void setRecordedViewerVideoUrl(String recordedViewerVideoUrl) {
		this.recordedViewerVideoUrl = recordedViewerVideoUrl;
	}

	/**
	 * Gets the recorded video file path.
	 *
	 * @return the recordedVideoFilePath
	 */
	@Column(name = "recorded_video_file_path")
	public String getRecordedVideoFilePath() {
		return recordedVideoFilePath;
	}

	/**
	 * Sets the recorded video file path.
	 *
	 * @param recordedVideoFilePath the recordedVideoFilePath to set
	 */
	public void setRecordedVideoFilePath(String recordedVideoFilePath) {
		this.recordedVideoFilePath = recordedVideoFilePath;
	}

	/**
	 * Gets the recorded content url.
	 *
	 * @return the recordedContentUrl
	 */
	@Column(name = "recorded_content_url")
	public String getRecordedContentUrl() {
		return recordedContentUrl;
	}

	/**
	 * Sets the recorded content url.
	 *
	 * @param recordedContentUrl the recordedContentUrl to set
	 */
	public void setRecordedContentUrl(String recordedContentUrl) {
		this.recordedContentUrl = recordedContentUrl;
	}

	/**
	 * Gets the recorded content file path.
	 *
	 * @return the recordedContentFilePath
	 */
	@Column(name = "recorded_content_file_path")
	public String getRecordedContentFilePath() {
		return recordedContentFilePath;
	}

	/**
	 * Sets the recorded content file path.
	 *
	 * @param recordedContentFilePath the recordedContentFilePath to set
	 */
	public void setRecordedContentFilePath(String recordedContentFilePath) {
		this.recordedContentFilePath = recordedContentFilePath;
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
	 * Gets the keywords.
	 *
	 * @return the keywords
	 */
	@Column(name = "keywords")
	public String getKeywords() {
		return keywords;
	}

	/**
	 * Sets the keywords.
	 *
	 * @param keywords the keywords to set
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 * Gets the lecture number.
	 *
	 * @return the lectureNumber
	 */
	@Column(name = "lecture_number")
	public int getLectureNumber() {
		return lectureNumber;
	}

	/**
	 * Sets the lecture number.
	 *
	 * @param lectureNumber the lectureNumber to set
	 */
	public void setLectureNumber(int lectureNumber) {
		this.lectureNumber = lectureNumber;
	}

	/**
	 * Gets the start time.
	 *
	 * @return the startTime
	 */
	@Column(name = "start_time")
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * Sets the start time.
	 *
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * Gets the end time.
	 *
	 * @return the endTime
	 */
	@Column(name = "end_time")
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * Sets the end time.
	 *
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.lectureId);
		sb.append(Constant.DELIMETER);
		sb.append(this.lectureName);
		sb.append(Constant.DELIMETER);
		sb.append(this.startDate);	
		sb.append(Constant.DELIMETER);
		sb.append(this.recordedPresenterVideoUrl);
		sb.append(Constant.DELIMETER);
		sb.append(this.recordedViewerVideoUrl);
		sb.append(Constant.DELIMETER);
		sb.append(this.recordedVideoFilePath);
		sb.append(Constant.DELIMETER);
		sb.append(this.recordedContentUrl);
		sb.append(Constant.DELIMETER);
		sb.append(this.recordedContentFilePath);
		sb.append(Constant.DELIMETER);
		sb.append(this.classId);
		sb.append(Constant.DELIMETER);
		sb.append(this.keywords);
		sb.append(Constant.DELIMETER);
		sb.append(this.lectureNumber);
		sb.append(Constant.DELIMETER);
		sb.append(this.startTime);
		sb.append(Constant.DELIMETER);
		sb.append(this.endTime);
		sb.append(Constant.DELIMETER);
		sb.append(super.toString());
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
		result = prime * result + ((classId == null) ? 0 : classId.hashCode());
		result = prime * result
				+ ((lectureName == null) ? 0 : lectureName.hashCode());
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
		if (!(obj instanceof Lecture))
			return false;
		Lecture other = (Lecture) obj;
		if (classId == null) {
			if (other.classId != null)
				return false;
		} else if (!classId.equals(other.classId))
			return false;
		if (lectureName == null) {
			if (other.lectureName != null)
				return false;
		} else if (!lectureName.equals(other.lectureName))
			return false;
		return true;
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
	 * Gets the institute name.
	 *
	 * @return the institute name
	 */
	@Transient
	public String getInstituteName() {
		return instituteName;
	}

	/**
	 * Sets the institute name.
	 *
	 * @param instituteName the new institute name
	 */
	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}

	@Transient
	public String getDisplayName()
	{
		String displayName = this.lectureName;
		int lectureNameSeparatorIndex = displayName.indexOf("~");
		if(lectureNameSeparatorIndex > 0)
		{
			displayName = displayName.substring(0, lectureNameSeparatorIndex);
		}
		return displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

}
