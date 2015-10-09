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

import org.hibernate.annotations.BatchSize;

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;


/**
 * The Class User.
 */
@Entity
@Table(name = "user")
@BatchSize(size=1000)
public class User extends Auditable {

	/** The user id. */
	private Long userId = 0l ;
	
	/** The institute id. */
	private Long instituteId=0l;
	
	/** The user name. */
	private String userName = null ;
	
	/** The password. */
	private String password = null ;
	
	/** The role. */
	private String role = null ;
	
	/** The fname. */
	private String fname = null ;
	
	/** The lname. */
	private String lname = null ;
	
	/** The address. */
	private String address = null;
	
	/** The city. */
	private String city = null ;
	
	/** The zip id. */
	private String zipId = null ;
	
	/** The email. */
	private String email  = null ;
	
	/** The mobile number. */
	private String mobileNumber = null ;
	
	/** The district id. */
	private Integer districtId = null;
	private String createdFrom = null ;
	
	/** The photo capture frequency secs. */
	private Integer photoCaptureFrequencySecs=-1;

	//non mapped attributes
	/** The institute name. */
	private String instituteName = null;
	
	/** The parent institute name. */
	private String parentInstituteName = null;
	
	/** The state name. */
	private String stateName = null;
	
	/** The district name. */
	private String districtName = null;
	
	/**
	 * Gets the user id.
	 *
	 * @return the userId
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "user_id")
	public Long getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * Gets the institute id.
	 *
	 * @return the institute id
	 */
	@Column(name = "institute_id")
	public Long getInstituteId() {
		return instituteId;
	}

	/**
	 * Sets the institute id.
	 *
	 * @param instituteId the new institute id
	 */
	public void setInstituteId(Long instituteId) {
		this.instituteId = instituteId;
	}


	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	@Column(name = "user_name")
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the user name.
	 *
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	@Column(name = "password")
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the role.
	 *
	 * @return the role
	 */
	@Column(name = "role")
	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Sets the role.
	 *
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * Gets the fname.
	 *
	 * @return the fname
	 */
	@Column(name = "fname")
	public String getFname() {
		return fname;
	}

	/**
	 * Sets the fname.
	 *
	 * @param fname the fname to set
	 */
	public void setFname(String fname) {
		this.fname = fname;
	}

	/**
	 * Gets the lname.
	 *
	 * @return the lname
	 */
	@Column(name = "lname")
	public String getLname() {
		return lname;
	}

	/**
	 * Sets the lname.
	 *
	 * @param lname the lname to set
	 */
	public void setLname(String lname) {
		this.lname = lname;
	}
	
	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	@Column(name = "address")
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address.
	 *
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	@Column(name = "city")
	public String getCity() {
		return city;
	}

	/**
	 * Sets the city.
	 *
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Gets the zip id.
	 *
	 * @return the zipCode
	 */
	@Column(name = "pincode")
	public String getZipId() {
		return zipId;
	}

	/**
	 * Sets the zip id.
	 *
	 * @param zipId the new zip id
	 */
	public void setZipId(String zipId) {
		this.zipId = zipId;
	}
	
	/**
	 * @return the createdFrom
	 */
	@Column(name = "created_from")
	public String getCreatedFrom() {
		return createdFrom;
	}

	/**
	 * @param createdFrom the createdFrom to set
	 */
	public void setCreatedFrom(String createdFrom) {
		this.createdFrom = createdFrom;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	@Column(name = "email")
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the mobile number.
	 *
	 * @return the mobileNumber
	 */
	@Column(name = "mobile_number")
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * Sets the mobile number.
	 *
	 * @param mobileNumber the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	/**
	 * Gets the district id.
	 *
	 * @return the districtId
	 */
	@Column(name = "district_id")
	public Integer getDistrictId() {
		return districtId;
	}

	/**
	 * Sets the district id.
	 *
	 * @param districtId the districtId to set
	 */
	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}
	
	/**
	 * Gets the photo capture frequency secs.
	 *
	 * @return the photo capture frequency secs
	 */
	@Column(name = "photo_capture_frequency_secs")
	public Integer getPhotoCaptureFrequencySecs() {
		return photoCaptureFrequencySecs;
	}

	/**
	 * Sets the photo capture frequency secs.
	 *
	 * @param photoCaptureFrequencySecs the new photo capture frequency secs
	 */
	public void setPhotoCaptureFrequencySecs(Integer photoCaptureFrequencySecs) {
		this.photoCaptureFrequencySecs = photoCaptureFrequencySecs;
	}

	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.userId);
		sb.append(Constant.DELIMETER);
		sb.append(this.userName);
		sb.append(Constant.DELIMETER);
		sb.append(this.password);
		sb.append(Constant.DELIMETER);
		sb.append(this.role);
		sb.append(Constant.DELIMETER);
		sb.append(this.fname);
		sb.append(Constant.DELIMETER);
		sb.append(this.lname);
		sb.append(Constant.DELIMETER);
		sb.append(this.address);
		sb.append(Constant.DELIMETER);
		sb.append(this.city);
		sb.append(Constant.DELIMETER);
		sb.append(this.zipId);
		sb.append(Constant.DELIMETER);
		sb.append(this.email);
		sb.append(Constant.DELIMETER);
		sb.append(this.mobileNumber);
		sb.append(Constant.DELIMETER);
		sb.append(this.instituteId);
		sb.append(Constant.DELIMETER);
		sb.append(this.districtId);
		sb.append(Constant.DELIMETER);
		sb.append(this.photoCaptureFrequencySecs);
		sb.append(Constant.DELIMETER);
		sb.append(super.toString());
		sb.append(Constant.DELIMETER);
		return sb.toString();
	}

	/**
	 * Gets the state name.
	 *
	 * @return the state name
	 */
	@Transient
	public String getStateName() {
		return stateName;
	}

	/**
	 * Sets the state name.
	 *
	 * @param stateName the new state name
	 */
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	/**
	 * Gets the district name.
	 *
	 * @return the district name
	 */
	@Transient
	public String getDistrictName() {
		return districtName;
	}

	/**
	 * Sets the district name.
	 *
	 * @param districtName the new district name
	 */
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
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

	/**
	 * Gets the parent institute name.
	 *
	 * @return the parent institute name
	 */
	@Transient
	public String getParentInstituteName() {
		return parentInstituteName;
	}

	/**
	 * Sets the parent institute name.
	 *
	 * @param parentInstituteName the new parent institute name
	 */
	public void setParentInstituteName(String parentInstituteName) {
		this.parentInstituteName = parentInstituteName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof User))
			return false;
		final User other = (User) obj;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	
}
