/*
 * 
 */
package edu.amrita.aview.gclm.vo;

import java.util.Date;

import edu.amrita.aview.gclm.entities.Class;
import edu.amrita.aview.gclm.entities.ClassRegistration;
import edu.amrita.aview.gclm.entities.Course;
import edu.amrita.aview.gclm.entities.Institute;
import edu.amrita.aview.gclm.entities.Lecture;


/**
 * The Class LectureListVO.
 */
public class LectureListVO {	
	
	/** The lecture. */
	public Lecture lecture = null;
	
	/** The aview class. */
	public Class aviewClass = null;
	
	/** The course. */
	public Course course = null;
	
	/** The institute. */
	public Institute institute = null;
	
	/** The class registration. */
	public ClassRegistration classRegistration = null;
	public Date currentTime = null;
	public String moderatorName=null;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lecture == null) ? 0 : lecture.hashCode());
		return result;
	}
	/** The user name. */
	public String userName = null;
	
	/**
	 * Comparing only the lecture object. Not using the ClassRegistration object. Because we only compare the objects for a given user.
	 *
	 * @param obj the obj
	 * @return true, if equals
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof LectureListVO))
			return false;
		LectureListVO other = (LectureListVO) obj;
		if (lecture == null) {
			if (other.lecture != null)
				return false;
		} else if (!lecture.equals(other.lecture))
			return false;
		return true;
	}
	
	
}
