/**
 * 
 */
package edu.amrita.aview.gclm.entities;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;
import edu.amrita.aview.common.helpers.StatusHelper;


/**
 * The Class Institute.
 *
 * @author
 */
@Entity
@Table(name="institute")
public class Institute extends Auditable implements Comparable{
	
	/** The institute id. */
	private Long instituteId=0l;
	
	/** The institute name. */
	private String instituteName=null;
	
	/** The institute type. */
	private String instituteType=null;
	
	/** The address. */
	private String address=null;
	
	/** The institute category id. */
	private Integer instituteCategoryId=null;
	
	/** The city. */
	private String city=null;
	
	/** The district id. */
	private Integer districtId=0;
	
	/** The parent institute id. */
	private Long parentInstituteId = null;
	
	/** The latitude. */
	private Float latitude=0.0f;
	
	/** The longitude. */
	private Float longitude=0.0f;
	
	/** The district name. */
	private String districtName = null;
	
	/** The state name. */
	private String stateName = null;
	
	/** The parent institute name. */
	private String parentInstituteName = null;
	
	/** The max publishing bandwidth kbps. */
	private int maxPublishingBandwidthKbps = 0 ;
		
	/** The max receiving bandwidth kbps. */
	private int minPublishingBandwidthKbps = 0 ;
	
	/** The institute admin users. */
	private Set<InstituteAdminUser> instituteAdminUsers = new HashSet<InstituteAdminUser>();
	
	/** The institute servers. */
	private Set<InstituteServer> instituteServers = new HashSet<InstituteServer>();
	
	/** The institute brandings. */
	private Set<InstituteBranding> instituteBrandings = new HashSet<InstituteBranding>();
	
	/**
	 * Update from.
	 *
	 * @param other the other
	 */
	public void updateFrom(Institute other)
	{
		this.instituteName=other.instituteName;
		this.instituteType=other.instituteType;
		this.address=other.address;
		this.instituteCategoryId=other.instituteCategoryId;
		this.city=other.city;
		this.districtId=other.districtId;
		this.parentInstituteId = other.parentInstituteId;
		this.latitude=other.latitude;
		this.longitude=other.longitude;
		this.maxPublishingBandwidthKbps=other.maxPublishingBandwidthKbps;
		this.minPublishingBandwidthKbps=other.minPublishingBandwidthKbps;
		this.setStatusId(other.getStatusId());
		
		/**
		 * Below code is the work around to the bug in hibernate's implementation of HashSet.
		 */
		//InstituteServers
		Set<InstituteServer> tempInstituteServers = new HashSet<InstituteServer>();
		if(this.instituteServers != null)
		{
			tempInstituteServers.addAll(this.instituteServers);
			this.instituteServers.clear();
		}

		Set<InstituteServer> tempOtherInstituteServers = new HashSet<InstituteServer>();
		if(other.instituteServers != null)
		{
			tempOtherInstituteServers.addAll(other.instituteServers);
		}

		//AdminUsers
		Set<InstituteAdminUser> tempInstituteAdminUsers = new HashSet<InstituteAdminUser>();
		if(this.instituteAdminUsers != null)
		{
			tempInstituteAdminUsers.addAll(this.instituteAdminUsers);
			this.instituteAdminUsers.clear();
		}
		
		Set<InstituteAdminUser> tempOtherInstituteAdminUsers = new HashSet<InstituteAdminUser>();
		if(other.instituteAdminUsers != null)
		{
			tempOtherInstituteAdminUsers.addAll(other.instituteAdminUsers);
		}
		
		//Brandings
		Set<InstituteBranding> tempInstituteBrandings = new HashSet<InstituteBranding>();
		if(this.instituteBrandings != null)
		{
			tempInstituteBrandings.addAll(this.instituteBrandings);
			this.instituteBrandings.clear();
		}

		Set<InstituteBranding> tempOtherInstituteBrandings = new HashSet<InstituteBranding>();
		if(other.instituteBrandings != null)
		{
			tempOtherInstituteBrandings.addAll(other.instituteBrandings);
		}		
		
		super.mergeAssociations(tempInstituteServers, tempOtherInstituteServers);
		super.mergeAssociations(tempInstituteAdminUsers, tempOtherInstituteAdminUsers);
		super.mergeAssociations(tempInstituteBrandings, tempOtherInstituteBrandings);
		
		this.instituteServers.addAll(tempInstituteServers);
		this.instituteAdminUsers.addAll(tempInstituteAdminUsers);
		this.instituteBrandings.addAll(tempInstituteBrandings);
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
			return this.instituteName.compareTo(((Institute)obj).getInstituteName());
		}
	}

	/**
	 * Gets the institute admin users.
	 *
	 * @return the instituteAdminUser
	 */
	
	@OneToMany(mappedBy="institute", fetch=FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval=true)	
	@BatchSize(size=1000)
	public Set<InstituteAdminUser> getinstituteAdminUsers() {
		return instituteAdminUsers;
	}
	
	/**
	 * Sets the institute admin users.
	 *
	 * @param instituteAdminUsers the new institute admin users
	 */
	public void setInstituteAdminUsers(Set<InstituteAdminUser> instituteAdminUsers) {
		this.instituteAdminUsers = instituteAdminUsers;		
	}
	
	/**
	 * Adds the institute admin user.
	 *
	 * @param instituteAdminUser the institute admin user
	 */
	public synchronized void addInstituteAdminUser(InstituteAdminUser instituteAdminUser)
	{
		if(this.instituteAdminUsers == null)
		{
			this.instituteAdminUsers = new HashSet<InstituteAdminUser>();
		}
		instituteAdminUser.setInstitute(this);
		this.instituteAdminUsers.add(instituteAdminUser);
	}

	/**
	 * Gets the institute servers.
	 *
	 * @return the instituteServer
	 */
	@OneToMany(mappedBy="institute",fetch=FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval=true)	
	@BatchSize(size=1000)
	public Set<InstituteServer> getInstituteServers() {
		return instituteServers;
	}
	
	/**
	 * Sets the institute servers.
	 *
	 * @param instituteServers the new institute servers
	 */
	public void setInstituteServers(Set<InstituteServer> instituteServers) {
		this.instituteServers = instituteServers;		
	}

	/**
	 * Adds the institute server.
	 *
	 * @param instituteServer the institute server
	 */
	public synchronized void addInstituteServer(InstituteServer instituteServer)
	{
		if(this.instituteServers == null)
		{
			this.instituteServers = new HashSet<InstituteServer>();
		}
		instituteServer.setInstitute(this);
		this.instituteServers.add(instituteServer);
	}

	/**
	 * Gets the institute brandings.
	 *
	 * @return the instituteBranding
	 */
	@OneToMany(mappedBy="institute",fetch=FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval=true)	
	@BatchSize(size=1000)
	public Set<InstituteBranding> getInstituteBrandings() {
		return instituteBrandings;
	}
	
	/**
	 * Sets the institute brandings.
	 *
	 * @param instituteBrandings the new institute brandings
	 */
	public void setInstituteBrandings(Set<InstituteBranding> instituteBrandings) {
		this.instituteBrandings = instituteBrandings;		
	}

	/**
	 * Adds the institute branding.
	 *
	 * @param instituteBranding the institute branding
	 */
	public synchronized void addInstituteBranding(InstituteBranding instituteBranding)
	{
		if(this.instituteBrandings == null)
		{
			this.instituteBrandings = new HashSet<InstituteBranding>();
		}
		instituteBranding.setInstitute(this);
		this.instituteBrandings.add(instituteBranding);
	}


	/**
	 * Gets the institute id.
	 *
	 * @return the institute_id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "institute_id")
	public Long getInstituteId() {
		return instituteId;
	}

	/**
	 * Sets the institute id.
	 *
	 * @param instituteId the institute_id to set
	 */
	public void setInstituteId(Long instituteId) {
		this.instituteId = instituteId;
	}

	/**
	 * Gets the institute name.
	 *
	 * @return the institute_name
	 */
	@Column(name = "institute_name")
	public String getInstituteName() {
		return instituteName;
	}
	
	/**
	 * Sets the institute name.
	 *
	 * @param instituteName the institute_name to set
	 */
	public void setInstituteName(String instituteName) {
		this.instituteName =instituteName;
	}
	
	/**
	 * Gets the institute type.
	 *
	 * @return the institute_type
	 */
	@Column(name = "institute_type")
	public String getInstituteType() {
		return instituteType;
	}
	
	/**
	 * Sets the institute type.
	 *
	 * @param instituteType the institute_type to set
	 */
	public void setInstituteType(String instituteType) {
		this.instituteType =instituteType;
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
		this.address =address;
	}
	
	
	/**
	 * Gets the institute category id.
	 *
	 * @return the institute_category_id
	 */
	@Column(name = "institute_category_id")
	public Integer getInstituteCategoryId() {
		return instituteCategoryId;
	}

	/**
	 * Sets the institute category id.
	 *
	 * @param instituteCategoryId the institute_category_id to set
	 */
	public void setInstituteCategoryId(Integer instituteCategoryId) {
		this.instituteCategoryId = instituteCategoryId;
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
		this.city =city;
	}
	
	/**
	 * Gets the district id.
	 *
	 * @return the district_id
	 */
	@Column(name = "district_id")
	public Integer getDistrictId() {
		return districtId;
	}

	/**
	 * Sets the district id.
	 *
	 * @param districtId the district_id to set
	 */
	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}
	
	/**
	 * Gets the parent institute id.
	 *
	 * @return the parent_institute_id
	 */
	@Column(name = "parent_institute_id")
	public Long getParentInstituteId() {
		return parentInstituteId;
	}
	/**
	 * Gets the max publishing bandwidth kbps.
	 *
	 * @return the maxPublishingBandwidthKbps
	 */

	@Column(name = "max_publishing_bandwidth_kbps")
	public int getMaxPublishingBandwidthKbps() {
		return maxPublishingBandwidthKbps;
	}
	/**
	 * Sets the max publishing bandwidth kbps.
	 *
	 * @param maxPublishingBandwidthKbps the maxPublishingBandwidthKbps to set
	 */

	public void setMaxPublishingBandwidthKbps(int maxPublishingBandwidthKbps) {
		this.maxPublishingBandwidthKbps = maxPublishingBandwidthKbps;
	}
	/**
	 * Gets the max receiving bandwidth kbps.
	 *
	 * @return the maxReceivingBandwidthKbps
	 */

	@Column(name = "min_publishing_bandwidth_kbps")
	public int getMinPublishingBandwidthKbps() {
		return minPublishingBandwidthKbps;
	}
	
	/**
	 * Sets the max receiving bandwidth kbps.
	 *
	 * @param maxReceivingBandwidthKbps the maxReceivingBandwidthKbps to set
	 */

	public void setMinPublishingBandwidthKbps(int minPublishingBandwidthKbps) {
		this.minPublishingBandwidthKbps = minPublishingBandwidthKbps;
	}

	/**
	 * Sets the parent institute id.
	 *
	 * @param parentInstituteId the parent_institute_id to set
	 */
	public void setParentInstituteId(Long parentInstituteId) {
		this.parentInstituteId = parentInstituteId;
	}
	
	/**
	 * Gets the latitude.
	 *
	 * @return the latitude
	 */
	@Column(name = "latitude")
	public Float getLatitude() {
		return latitude;
	}

	/**
	 * Sets the latitude.
	 *
	 * @param latitude the new latitude
	 */
	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * Gets the longitude.
	 *
	 * @return the longitude
	 */
	@Column(name = "longitude")
	public Float getLongitude() {
		return longitude;
	}

	/**
	 * Sets the longitude.
	 *
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	
	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.instituteId);
		sb.append(Constant.DELIMETER);
		sb.append(this.instituteName);
		sb.append(Constant.DELIMETER);
		sb.append(this.instituteType);	
		sb.append(Constant.DELIMETER);
		sb.append(this.address);
		sb.append(Constant.DELIMETER);
		sb.append(this.instituteCategoryId);
		sb.append(Constant.DELIMETER);
		sb.append(this.city);
		sb.append(Constant.DELIMETER);
		sb.append(this.districtId);
		sb.append(Constant.DELIMETER);
		sb.append(this.parentInstituteId);
		sb.append(Constant.DELIMETER);
		sb.append(this.latitude);
		sb.append(Constant.DELIMETER);
		sb.append(this.longitude);
		sb.append(Constant.DELIMETER);
		sb.append(this.maxPublishingBandwidthKbps);
		sb.append(Constant.DELIMETER);
		sb.append(this.minPublishingBandwidthKbps);
		sb.append(Constant.DELIMETER);
		sb.append(super.toString());
		sb.append(Constant.DELIMETER);
		return sb.toString();
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((instituteName == null) ? 0 : instituteName.hashCode());
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
		final Institute other = (Institute) obj;
		if (instituteName == null) {
			if (other.instituteName != null)
				return false;
		} else if (!instituteName.equals(other.instituteName))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#setCreatedAduitData(java.lang.Long, java.sql.Timestamp, java.lang.Integer)
	 */
	public void setCreatedAuditData(Long createdUserId,Timestamp createdDate,Integer statusId)
	{
		super.setCreatedAuditData(createdUserId, createdDate, statusId);
		if(this.instituteServers != null)
		{
			for(InstituteServer server:instituteServers)
			{
				server.setCreatedAuditData(createdUserId, createdDate, statusId);
			}
		}
		
		if(this.instituteAdminUsers != null)
		{
			for(InstituteAdminUser instituteAdminUser:instituteAdminUsers)
			{
				instituteAdminUser.setCreatedAuditData(createdUserId, createdDate, statusId);
			}
		}

		if(this.instituteBrandings != null)
		{
			for(InstituteBranding branding:instituteBrandings)
			{
				branding.setCreatedAuditData(createdUserId, createdDate, statusId);
			}
		}
		
	}
	
	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#setModifiedAuditData(java.lang.Long, java.sql.Timestamp)
	 */
	public void setModifiedAuditData(Long modifiedUserId,Timestamp modifedDate) throws AViewException
	{
		super.setModifiedAuditData(modifiedUserId, modifedDate);
		if(this.instituteServers != null)
		{
			for(InstituteServer server:instituteServers)
			{
				if(server.getInstituteServerId() != 0) //Update
				{
					server.setModifiedAuditData(modifiedUserId, modifedDate);
				}
				else //Create
				{
					server.setCreatedAuditData(modifiedUserId, modifedDate, StatusHelper.getActiveStatusId());
				}
			}
		}
		
		if(this.instituteAdminUsers != null)
		{
			for(InstituteAdminUser instituteAdminUser:instituteAdminUsers)
			{
				if(instituteAdminUser.getInstituteAdminUserId() != 0) //Update
				{
					instituteAdminUser.setModifiedAuditData(modifiedUserId, modifedDate);
				}
				else //Create
				{
					instituteAdminUser.setCreatedAuditData(modifiedUserId, modifedDate, StatusHelper.getActiveStatusId());
				}
			}
		}

		if(this.instituteBrandings != null)
		{
			for(InstituteBranding branding:instituteBrandings)
			{
				if(branding.getInstituteBrandingId() != 0) //Update
				{
					branding.setModifiedAuditData(modifiedUserId, modifedDate);
				}
				else //Create
				{
					branding.setCreatedAuditData(modifiedUserId, modifedDate, StatusHelper.getActiveStatusId());
				}
			}
		}
	}
	
}
