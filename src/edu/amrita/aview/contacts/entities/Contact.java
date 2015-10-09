/*
 * 
 */
package edu.amrita.aview.contacts.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;


/**
 * The Class Contact.
 *
 * @author
 */
@Entity
@Table(name="contact")
public class Contact extends Auditable {
	
	/** The contact id. */
	private Long contactId=0l;
	
	/** The contact name. */
	private String contactName=null;
	
	/** The mobile number. */
	private String mobileNumber=null;
	
	/** The work number. */
	private String workNumber=null;
	
	/** The fax number. */
	private String faxNumber=null;
	
	/** The email. */
	private String email=null;
	
	/** The institute id. */
	private Long instituteId=0l;
	
	/**
	 * Gets the contact id.
	 *
	 * @return the contact_id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "contact_id")
	public Long getContactId() 
	{
		return contactId;
	}
	
	/**
	 * Sets the contact id.
	 *
	 * @param contactId the contact_id to set
	 */
	public void setContactId(Long contactId) 
	{
		this.contactId = contactId;
	}
	
	/**
	 * Gets the contact name.
	 *
	 * @return the contact_name
	 */
	@Column(name = "contact_name")
	public String getContactName() 
	{
		return contactName;
	}
	
	/**
	 * Sets the contact name.
	 *
	 * @param contactName the contact_name to set
	 */
	public void setContactName(String contactName) 
	{
		this.contactName =contactName;
	}
	
	/**
	 * Gets the mobile number.
	 *
	 * @return the mobile_number
	 */
	@Column(name = "mobile_number")
	public String getMobileNumber() 
	{
		return mobileNumber;
	}
	
	/**
	 * Sets the mobile number.
	 *
	 * @param mobileNumber the mobile_number to set
	 */
	public void setMobileNumber(String mobileNumber) 
	{
		this.mobileNumber =mobileNumber;
	}
	
	/**
	 * Gets the work number.
	 *
	 * @return the work_number
	 */
	@Column(name = "work_number")
	public String getWorkNumber() 
	{
		return workNumber;
	}
	
	/**
	 * Sets the work number.
	 *
	 * @param workNumber the work_number to set
	 */
	public void setWorkNumber(String workNumber) 
	{
		this.workNumber =workNumber;
	}
	
	/**
	 * Gets the fax number.
	 *
	 * @return the fax_number
	 */
	@Column(name = "fax_number")
	public String getFaxNumber() 
	{
		return faxNumber;
	}
	
	/**
	 * Sets the fax number.
	 *
	 * @param faxNumber the fax_number to set
	 */
	public void setFaxNumber(String faxNumber) 
	{
		this.faxNumber =faxNumber;
	}
	
	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	@Column(name = "email")
	public String getEmail() 
	{
		return email;
	}
	
	/**
	 * Sets the email.
	 *
	 * @param email the email to set
	 */
	public void setEmail(String email) 
	{
		this.email =email;
	}
	
	/**
	 * Gets the institute id.
	 *
	 * @return the institute_id
	 */
	@Column(name = "institute_id")
	public Long getInstituteId() 
	{
		return instituteId;
	}
	
	/**
	 * Sets the institute id.
	 *
	 * @param instituteId the institute_id to set
	 */
	public void setInstituteId(Long instituteId) 
	{
		this.instituteId = instituteId;
	}
	
	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	public String toString() 
	{
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.contactId);
		sb.append(Constant.DELIMETER);
		sb.append(this.contactName);
		sb.append(Constant.DELIMETER);
		sb.append(this.mobileNumber);	
		sb.append(Constant.DELIMETER);
		sb.append(this.workNumber);
		sb.append(Constant.DELIMETER);
		sb.append(this.faxNumber);
		sb.append(Constant.DELIMETER);
		sb.append(this.email);
		sb.append(Constant.DELIMETER);
		sb.append(this.instituteId);
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
				+ ((contactName == null) ? 0 : contactName.hashCode());
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
		if (getClass() != obj.getClass())
			return false;
		final Contact other = (Contact) obj;
		if (contactName == null) {
			if (other.contactName != null)
				return false;
		} else if (!contactName.equals(other.contactName))
			return false;
		if (instituteId == null) {
			if (other.instituteId != null)
				return false;
		} else if (!instituteId.equals(other.instituteId))
			return false;
		return true;
	}
	
}
