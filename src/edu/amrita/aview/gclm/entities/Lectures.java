/*
 * 
 */
package edu.amrita.aview.gclm.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * The Class Lectures.
 */
@Entity
@Table(name = "lectures")
public class Lectures {

	/** The lecture id. */
	private Long lectureId = 0l;
	
	/** The lecture topic. */
	private String lectureTopic = null;
	
	/** The start date. */
	private String startDate = null; 
	
	/** The video file path. */
	private String videoFilePath = null;
	
	/** The video file name. */
	private String videoFileName = null;
	
	/** The xml file path. */
	private String xmlFilePath = null;
	
	/** The xml file name. */
	private String xmlFileName = null;
	
	/** The teacher id. */
	private Long teacherId = 0l;	
	
	/** The course id. */
	private Long courseId = 0l;
	
	/** The recorded lecture id. */
	private Long recordedLectureId = 0l;	
	
	/** The key words. */
	private String keyWords = null;
	
	/** The lecture number. */
	private Long lectureNumber = 0l ;
	
	
	/**
	 * Gets the lecture id.
	 *
	 * @return the lectureId
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	 * Gets the lecture topic.
	 *
	 * @return the lectureTopic
	 */
	@Column(name = "lecture_topic")
	public String getLectureTopic() {
		return lectureTopic;
	}
	
	/**
	 * Sets the lecture topic.
	 *
	 * @param lectureTopic the lectureTopic to set
	 */
	public void setLectureTopic(String lectureTopic) {
		this.lectureTopic = lectureTopic;
	}
	
	/**
	 * Gets the start date.
	 *
	 * @return the startDate
	 */
	@Column(name = "date")
	public String getStartDate() {
		return startDate;
	}
	
	/**
	 * Sets the start date.
	 *
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	/**
	 * Gets the video file path.
	 *
	 * @return the videoFilePath
	 */
	@Column(name = "videofile_path")
	public String getVideoFilePath() {
		return videoFilePath;
	}
	
	/**
	 * Sets the video file path.
	 *
	 * @param videoFilePath the videoFilePath to set
	 */
	public void setVideoFilePath(String videoFilePath) {
		this.videoFilePath = videoFilePath;
	}
	
	/**
	 * Gets the video file name.
	 *
	 * @return the videoFileName
	 */
	@Column(name = "videofile_name")
	public String getVideoFileName() {
		return videoFileName;
	}
	
	/**
	 * Sets the video file name.
	 *
	 * @param videoFileName the videoFileName to set
	 */
	public void setVideoFileName(String videoFileName) {
		this.videoFileName = videoFileName;
	}
	
	/**
	 * Gets the xml file path.
	 *
	 * @return the xmlFilePath
	 */
	@Column(name = "xmlfile_path")
	public String getXmlFilePath() {
		return xmlFilePath;
	}
	
	/**
	 * Sets the xml file path.
	 *
	 * @param xmlFilePath the xmlFilePath to set
	 */
	public void setXmlFilePath(String xmlFilePath) {
		this.xmlFilePath = xmlFilePath;
	}
	
	/**
	 * Gets the xml file name.
	 *
	 * @return the xmlFileName
	 */
	@Column(name = "xmlfile_name")
	public String getXmlFileName() {
		return xmlFileName;
	}
	
	/**
	 * Sets the xml file name.
	 *
	 * @param xmlFileName the xmlFileName to set
	 */
	public void setXmlFileName(String xmlFileName) {
		this.xmlFileName = xmlFileName;
	}
	
	/**
	 * Gets the teacher id.
	 *
	 * @return the teacherId
	 */
	@Column(name = "teacher_id")
	public Long getTeacherId() {
		return teacherId;
	}
	
	/**
	 * Sets the teacher id.
	 *
	 * @param teacherId the teacherId to set
	 */
	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}
	
	/**
	 * Gets the course id.
	 *
	 * @return the courseId
	 */
	@Column(name = "course_id")
	public Long getCourseId() {
		return courseId;
	}
	
	/**
	 * Sets the course id.
	 *
	 * @param courseId the courseId to set
	 */
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	
	/**
	 * Gets the recorded lecture id.
	 *
	 * @return the recordedLectureId
	 */
	@Column(name = "recorded_lecture_id")
	public Long getRecordedLectureId() {
		return recordedLectureId;
	}
	
	/**
	 * Sets the recorded lecture id.
	 *
	 * @param recordedLectureId the recordedLectureId to set
	 */
	public void setRecordedLectureId(Long recordedLectureId) {
		this.recordedLectureId = recordedLectureId;
	}
	
	/**
	 * Gets the key words.
	 *
	 * @return the keyWords
	 */
	@Column(name = "keywords")
	public String getKeyWords() {
		return keyWords;
	}
	
	/**
	 * Sets the key words.
	 *
	 * @param keyWords the keyWords to set
	 */
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	
	/**
	 * Gets the lecture number.
	 *
	 * @return the lectureNumber
	 */
	@Column(name = "lecture_number")
	public Long getLectureNumber() {
		return lectureNumber;
	}
	
	/**
	 * Sets the lecture number.
	 *
	 * @param lectureNumber the lectureNumber to set
	 */
	public void setLectureNumber(Long lectureNumber) {
		this.lectureNumber = lectureNumber;
	}
}
