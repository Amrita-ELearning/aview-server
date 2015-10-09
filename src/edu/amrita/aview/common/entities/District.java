/**
 * 
 */
package edu.amrita.aview.common.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.amrita.aview.common.Constant;


/**
 * The Class District.
 *
 * @author
 */
@Entity
@Table(name="district")
public class District extends Auditable {
	
	/** The district id. */
	private Integer districtId=0;
	
	/** The state id. */
	private Integer stateId=0;
	
	/** The district name. */
	private String districtName=null;
	
		
	/**
	 * Gets the district id.
	 *
	 * @return the district_id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	 * Gets the state id.
	 *
	 * @return the state_id
	 */
	@Column(name = "state_id")
	public Integer getStateId() {
		return stateId;
	}

	/**
	 * Sets the state id.
	 *
	 * @param stateId the state_id to set
	 */
	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}
	
	/**
	 * Gets the district name.
	 *
	 * @return the district_name
	 */
	@Column(name = "district_name")
	public String getDistrictName() {
		return districtName;
	}
	
	/**
	 * Sets the district name.
	 *
	 * @param districtName the district_name to set
	 */
	public void setDistrictName(String districtName) {
		this.districtName =districtName;
	}
	
	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.districtId);
		sb.append(Constant.DELIMETER);
		sb.append(this.stateId);
		sb.append(Constant.DELIMETER);
		sb.append(this.districtName);
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
				+ ((districtName == null) ? 0 : districtName.hashCode());
		result = prime * result + ((stateId == null) ? 0 : stateId.hashCode());
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
		if (!(obj instanceof District))
			return false;
		District other = (District) obj;
		if (districtName == null) {
			if (other.districtName != null)
				return false;
		} else if (!districtName.equals(other.districtName))
			return false;
		if (stateId == null) {
			if (other.stateId != null)
				return false;
		} else if (!stateId.equals(other.stateId))
			return false;
		return true;
	}

}
