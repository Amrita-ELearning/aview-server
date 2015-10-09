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

import edu.amrita.aview.common.entities.Auditable;


/**
 * The Class BrandingAttribute.
 */
@Entity
@Table(name="branding_attribute")
public class BrandingAttribute extends Auditable {
	
	/** The branding attribute id. */
	private Integer brandingAttributeId=0;
	
	/** The branding attribute name. */
	private String brandingAttributeName= null;
	
	/**
	 * Gets the branding attribute id.
	 *
	 * @return the branding attribute id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "branding_attribute_id")
	public Integer getBrandingAttributeId() {
		return brandingAttributeId;
	}
	
	/**
	 * Sets the branding attribute id.
	 *
	 * @param brandingAttributeId the new branding attribute id
	 */
	public void setBrandingAttributeId(Integer brandingAttributeId) {
		this.brandingAttributeId = brandingAttributeId;
	}

	/**
	 * Gets the branding attribute name.
	 *
	 * @return the branding attribute name
	 */
	@Column(name = "branding_attribute_name")
	public String getBrandingAttributeName() {
		return brandingAttributeName;
	}
	
	/**
	 * Sets the branding attribute name.
	 *
	 * @param brandingAttributeName the new branding attribute name
	 */
	public void setBrandingAttributeName(String brandingAttributeName) {
		this.brandingAttributeName = brandingAttributeName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((brandingAttributeName == null) ? 0 : brandingAttributeName
						.hashCode());
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
		if (!(obj instanceof BrandingAttribute)) {
			return false;
		}
		BrandingAttribute other = (BrandingAttribute) obj;
		if (brandingAttributeName == null) {
			if (other.brandingAttributeName != null) {
				return false;
			}
		} else if (!brandingAttributeName.equals(other.brandingAttributeName)) {
			return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	@Override
	public String toString() {
		return "BrandingAttribute [brandingAttributeId=" + brandingAttributeId
				+ ", brandingAttributeName=" + brandingAttributeName+ "]";
	}

}
