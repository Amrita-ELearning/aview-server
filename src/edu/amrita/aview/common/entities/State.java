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
 * The Class State.
 *
 * @author
 */
@Entity
@Table(name="state")
public class State extends Auditable {
		
	/** The state id. */
	private Integer stateId=0;
	
	/** The state name. */
	private String stateName=null;
	
	/** The country id. */
	private Integer countryId=0;
		
	
	/**
	 * Gets the state id.
	 *
	 * @return the state_id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	 * Gets the state name.
	 *
	 * @return the state_name
	 */
	@Column(name = "state_name")
	public String getStateName() {
		return stateName;
	}
	
	/**
	 * Sets the state name.
	 *
	 * @param stateName the state_name to set
	 */
	public void setStateName(String stateName) {
		this.stateName =stateName;
	}
	
	/**
	 * Gets the country id.
	 *
	 * @return the country_id
	 */
	@Column(name = "country_id")
	public Integer getCountryId() {
		return countryId;
	}

	/**
	 * Sets the country id.
	 *
	 * @param countryId the country_id to set
	 */
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	
	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.stateId);
		sb.append(Constant.DELIMETER);
		sb.append(this.stateName);
		sb.append(Constant.DELIMETER);
		sb.append(this.countryId);
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
				+ ((stateName == null) ? 0 : stateName.hashCode());
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
		final State other = (State) obj;
		if (stateName == null) {
			if (other.stateName != null)
				return false;
		} else if (!stateName.equals(other.stateName))
			return false;
		return true;
	}


}
