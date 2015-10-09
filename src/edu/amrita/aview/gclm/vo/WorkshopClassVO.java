/*
 * 
 */
package edu.amrita.aview.gclm.vo;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * The Class WorkshopClassVO.
 */
public class WorkshopClassVO {

	/** The class id. */
	private long classID;
	
	/** The start date. */
	private Date startDate;
	
	/** The end date. */
	private Date endDate;
	
	/** The start time. */
	private Date startTime;
	
	/** The end time. */
	private Date endTime;
	
	/** The class name. */
	private String className;
	
	/** The class status. */
	private int classStatus;
	
	/** The class description. */
	private String classDescription;
	
	/** The date formatter. */
	private int classRegistrationCount;
	
	/** The user attended count. */
	private int userAttendedCount;
	
	/** The date formatter. */
	private Format dateFormatter = new SimpleDateFormat("dd-MMMM-yyyy");
	
	/** The hour formatter. */
	private Format hourFormatter = new SimpleDateFormat("hh:mm a");

	/**
	 * Instantiates a new workshop class vo.
	 *
	 * @param classID the class id
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param startTime the start time
	 * @param endTime the end time
	 * @param className the class name
	 * @param classStatus the class status
	 * @param classDescription the class description
	 * @param classRegistrationCount the class registration count
	 * @param userAttendedCount the user attended count
	 */
	public WorkshopClassVO(long classID, Date startDate, Date endDate, Date startTime,
			Date endTime, String className, int classStatus, String classDescription, int classRegistrationCount, int userAttendedCount) {
		this.classID = classID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.className = className;
		this.classStatus = classStatus;
		this.classRegistrationCount = classRegistrationCount;
		this.userAttendedCount = userAttendedCount; 
		this.classDescription=classDescription;
	}

	/**
	 * Gets the class id.
	 *
	 * @return the class id
	 */
	public long getClassID() {
		return classID;
	}

	/**
	 * Sets the class id.
	 *
	 * @param classID the new class id
	 */
	public void setClassID(long classID) {
		this.classID = classID;
	}

	/**
	 * Gets the start date.
	 *
	 * @return the start date
	 */
	public String getStartDate() {
		return dateFormatter.format(startDate);
	}

	/**
	 * Sets the start date.
	 *
	 * @param startDate the new start date
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	/**
	 * Gets the end date.
	 *
	 * @return the end date
	 */
	public String getEndDate() {
		return dateFormatter.format(endDate);
	}

	/**
	 * Sets the end date.
	 *
	 * @param endDate the new end date
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Gets the start time.
	 *
	 * @return the start time
	 */
	public String getStartTime() {
		return this.startTime != null ? hourFormatter.format(startTime) : "";
	}

	/**
	 * Sets the start time.
	 *
	 * @param startTime the new start time
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * Gets the end time.
	 *
	 * @return the end time
	 */
	public String getEndTime() {
		return this.endTime != null ? hourFormatter.format(endTime) : "";
	}

	/**
	 * Sets the end time.
	 *
	 * @param endTime the new end time
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * Gets the class name.
	 *
	 * @return the class name
	 */
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
	 * Gets the class status.
	 *
	 * @return the class status
	 */
	public int getClassStatus() {
		return classStatus;
	}

	/**
	 * Sets the class status.
	 *
	 * @param classStatus the new class status
	 */
	public void setClassStatus(int classStatus) {
		this.classStatus = classStatus;
	}

	/**
	 * Gets the class description.
	 *
	 * @return the class description
	 */
	public String getClassDescription() {
		return classDescription;
	}

	/**
	 * Sets the class description.
	 *
	 * @param classDescription the class description
	 */
	public void setClassDescription(String classDescription) {
		this.classDescription = classDescription;
	}

	/**
	 * Gets the class registration count.
	 *
	 * @return the classRegistrationCount
	 */
	public int getClassRegistrationCount() {
		return classRegistrationCount;
	}

	/**
	 * Sets the class registration count.
	 *
	 * @param classRegistrationCount the classRegistrationCount to set
	 */
	public void setClassRegistrationCount(int classRegistrationCount) {
		this.classRegistrationCount = classRegistrationCount;
	}

	/**
	 * Gets the user attended count.
	 *
	 * @return the userAttendedCount
	 */
	public int getUserAttendedCount() {
		return userAttendedCount;
	}

	/**
	 * Sets the user attended count.
	 *
	 * @param userAttendedCount the userAttendedCount to set
	 */
	public void setUserAttendedCount(int userAttendedCount) {
		this.userAttendedCount = userAttendedCount;
	}

	
}
