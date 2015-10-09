/**
 * 
 */
package edu.amrita.aview.gclm.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;


/**
 * The Class InstituteAdminUser.
 *
 * @author
 */
@Entity
@Table(name = "institute_admin_user")
public class InstituteAdminUser extends Auditable {
	
	/** The institute admin user id. */
	private Long instituteAdminUserId=0l;
	
	/** The user. */
	private User user = null;
	
	/** The institute. */
	private Institute institute = null;
	
	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name="user_id", nullable=false)
	@BatchSize(size=1000)	
	public User getUser() {
		return user;
	}
	
	/**
	 * Sets the user.
	 *
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * Gets the institute.
	 *
	 * @return the institute
	 */
	@ManyToOne (fetch=FetchType.EAGER)
	@JoinColumn(name="institute_id", nullable=false)
	public Institute getinstitute() {
		return institute;
	}
	
	/**
	 * Sets the institute.
	 *
	 * @param institute the institute to set
	 */
	public void setInstitute(Institute institute) {
		this.institute = institute;
	}
	
	/**
	 * Gets the institute admin user id.
	 *
	 * @return the institute_admin_user_id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "institute_admin_user_id")
	public Long getInstituteAdminUserId() {
		return instituteAdminUserId;
	}
	
	/**
	 * Sets the institute admin user id.
	 *
	 * @param instituteAdminUserId the institute_admin_user_id to set
	 */
	public void setInstituteAdminUserId(Long instituteAdminUserId) {
		this.instituteAdminUserId = instituteAdminUserId;
	}
	
	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.instituteAdminUserId);
		sb.append(Constant.DELIMETER);
		sb.append(this.user.getUserId());
		sb.append(Constant.DELIMETER);
		sb.append(this.institute.getInstituteId());	
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
		result = prime * result
				+ ((institute == null) ? 0 : institute.getInstituteId().hashCode());
		result = prime * result + ((user == null) ? 0 : user.getUserId().hashCode());
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
		if (!(obj instanceof InstituteAdminUser))
			return false;
		InstituteAdminUser other = (InstituteAdminUser) obj;
		if (this.institute == null) {
			if (other.institute != null)
				return false;
		} else if (!this.institute.getInstituteId().equals(other.institute.getInstituteId()))
			return false;
		if (this.user == null) {
			if (other.user != null)
				return false;
		} else if (!this.user.getUserId().equals(other.user.getUserId()))
			return false;
		return true;
	}
}
