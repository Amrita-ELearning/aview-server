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
import javax.persistence.Transient;

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;
import org.hibernate.annotations.GenericGenerator;


/**
 * The Class Course.
 */
@Entity
@Table(name = "course")
public class Course extends Auditable implements Comparable{	

	/** The course id. */
	private Long courseId = 0l ;
	
	/** The course name. */
	private String courseName = null ;
	
	/** The course code. */
	private String courseCode = null ;
	
	/** The institute id. */
	private Long instituteId  = 0l ;	
	
	//Non mapped attributes
	/** The institute name. */
	private String instituteName = null;

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

	/**
	 * Gets the course id.
	 * CustomIdGenerator is setted for give id as a optional field for API's *
	 * @return the course_id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY,generator="CustomIdGenerator")
	@GenericGenerator(name="CustomIdGenerator", strategy="edu.amrita.aview.common.utils.CustomIdGenerator")
	@Column(name = "course_id")
	public Long getCourseId() {
		return courseId;
	}

	/**
	 * Sets the course id.
	 *
	 * @param courseId the course_id to set
	 */
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	/**
	 * Gets the course name.
	 *
	 * @return the course_name
	 */
	@Column(name = "course_name")
	public String getCourseName() {
		return courseName;
	}

	/**
	 * Sets the course name.
	 *
	 * @param courseName the course_name to set
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	
	/**
	 * Gets the course code.
	 *
	 * @return the course_code
	 */
	@Column(name = "course_code")
	public String getCourseCode() {
		return courseCode;
	}

	/**
	 * Sets the course code.
	 *
	 * @param courseCode the course_code to set
	 */
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
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
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.courseId);
		sb.append(Constant.DELIMETER);
		sb.append(this.courseName);
		sb.append(Constant.DELIMETER);
		sb.append(this.courseCode);	
		sb.append(Constant.DELIMETER);
		sb.append(this.instituteId);	
		sb.append(Constant.DELIMETER);
		sb.append(super.toString());
		sb.append(Constant.DELIMETER);
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object obj)
	{
		if (getClass() != obj.getClass())
		{
			return -1;
		}
		else
		{
			return this.courseName.compareTo(((Course)obj).getCourseName());
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((courseCode == null) ? 0 : courseCode.hashCode());
		result = prime * result
				+ ((instituteId == null) ? 0 : instituteId.hashCode());
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
		if (!(obj instanceof Course))
			return false;
		Course other = (Course) obj;
		if (courseCode == null) {
			if (other.courseCode != null)
				return false;
		} else if (!courseCode.equals(other.courseCode))
			return false;
		if (instituteId == null) {
			if (other.instituteId != null)
				return false;
		} else if (!instituteId.equals(other.instituteId))
			return false;
		return true;
	}

}
