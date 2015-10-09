/**
 * 
 */
package edu.amrita.aview.contacts.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;
import edu.amrita.aview.gclm.entities.User;


/**
 * The Class GroupUser.
 *
 * @author
 */
@Entity
@Table(name="group_user")
public class GroupUser extends Auditable 
{
	
	/** The group user id. */
	private Long groupUserId = 0l;
	
	/** The contact group id. */
	private Long contactGroupId = 0l; 
	//to display the details of all groupMembers
	/** The user. */
	private User user = null;
	
	/**
	 * Gets the group user id.
	 *
	 * @return the groupUserId
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "group_user_id")
	public Long getGroupUserId() {
		return groupUserId;
	}
	
	/**
	 * Sets the group user id.
	 *
	 * @param groupUserId the groupUserId to set
	 */
	public void setGroupUserId(Long groupUserId) {
		this.groupUserId = groupUserId;
	}
	
	/**
	 * Gets the contact group id.
	 *
	 * @return the group
	 */
	
	@Column(name="contact_group_id")
	public Long getContactGroupId() {
		return contactGroupId;
	}
	
	/**
	 * Sets the contact group id.
	 *
	 * @param contactGroupId the contact group id
	 */
	public void setContactGroupId(Long contactGroupId) {
		this.contactGroupId = contactGroupId;
	}
	
	/**
	 * Gets the user.
	 *
	 * @return the userId
	 */
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name="user_id")
	public User getUser() {
		return user;
	}
	
	/**
	 * Sets the user.
	 *
	 * @param user the user
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.groupUserId);
		sb.append(Constant.DELIMETER);
		sb.append(Constant.DELIMETER);
		sb.append(this.user.getUserId());
		sb.append(Constant.DELIMETER);
		sb.append(super.toString());
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
				+ ((contactGroupId == null) ? 0 : contactGroupId.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		if (getClass() != obj.getClass())
			return false;
		GroupUser other = (GroupUser) obj;
		if (contactGroupId == null) {
			if (other.contactGroupId != null)
				return false;
		} else if (!contactGroupId.equals(other.contactGroupId))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
}
