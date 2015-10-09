/**
 * 
 */
package edu.amrita.aview.contacts.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;


/**
 * The Class ContactGroup.
 *
 * @author
 */
@Entity
@Table(name="contact_group")
public class ContactGroup extends Auditable {
		
	/** The contact group id. */
	private Long contactGroupId = 0l;
	
	/** The contact group name. */
	private String contactGroupName = null;
	
	/** The group owner id. */
	private Long groupOwnerId = 0l;
	
	private Long memberCount= 0l;
	
	private List<GroupUser> groupUsers = new ArrayList<GroupUser>();
	
	/**
	 * Gets the contact group id.
	 *
	 * @return the groupId
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "contact_group_id")
	public Long getContactGroupId() {
		return contactGroupId;
	}
	
	/**
	 * Sets the contact group id.
	 *
	 * @param groupId the groupId to set
	 */
	public void setContactGroupId(Long groupId) {
		this.contactGroupId = groupId;
	}
	
	/**
	 * Gets the contact group name.
	 *
	 * @return the groupName
	 */
	@Column(name = "contact_group_name")
	public String getContactGroupName() {
		return contactGroupName;
	}
	
	/**
	 * Sets the contact group name.
	 *
	 * @param groupName the groupName to set
	 */
	public void setContactGroupName(String groupName) {
		this.contactGroupName = groupName;
	}
	
	/**
	 * Gets the group owner id.
	 *
	 * @return the group owner id
	 */
	@Column(name = "group_owner_user_id")
	public Long getGroupOwnerId() {
		return groupOwnerId;
	}

	/**
	 * Sets the group owner id.
	 *
	 * @param groupOwnerId the group owner id
	 */
	public void setGroupOwnerId(Long groupOwnerId) {
		this.groupOwnerId = groupOwnerId;
	}
	
    @Transient 
	public Long getMemberCount() 
	{
		return memberCount;
	}

	public void setMemberCount(Long memberCount) 
	{
		this.memberCount = memberCount;
	}
	
	@Transient
	public List<GroupUser> getGroupUsers() {
		return groupUsers;
	}

	public void setGroupUsers(List<GroupUser> groupUsers) {
		this.groupUsers = groupUsers;
	}
	
	public void addGroupUser(GroupUser user)
	{
		groupUsers.add(user);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.contactGroupId);
		sb.append(Constant.DELIMETER);
		sb.append(this.contactGroupName);
		sb.append(Constant.DELIMETER);
		sb.append(this.groupOwnerId);
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
				+ ((contactGroupName == null) ? 0 : contactGroupName.hashCode());
		result = prime
				* result
				+ ((groupOwnerId == null) ? 0 : groupOwnerId.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ContactGroup other = (ContactGroup) obj;
		if (contactGroupName == null) {
			if (other.contactGroupName != null) {
				return false;
			}
		} else if (!contactGroupName.equals(other.contactGroupName)) {
			return false;
		}
		if (groupOwnerId == null) {
			if (other.getGroupOwnerId() != null) {
				return false;
			}
		} else if (!groupOwnerId.equals(other.getGroupOwnerId())) {
			return false;
		}
		return true;
	}	
}
