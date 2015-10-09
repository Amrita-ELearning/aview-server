/*
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

import edu.amrita.aview.common.entities.Auditable;


/**
 * The Class InstituteBranding.
 */
@Entity
@Table(name = "institute_branding")
@BatchSize(size=1000)
public class InstituteBranding extends Auditable {
	
	/** The institute branding id. */
	private Long instituteBrandingId = 0l;
	
	/** The institute. */
	private Institute institute;
	
	/** The branding attribute. */
	private BrandingAttribute brandingAttribute;
	
	/** The branding attribute value. */
	private String brandingAttributeValue;

	/**
	 * Gets the institute branding id.
	 *
	 * @return the institute branding id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "institute_branding_id")
	public Long getInstituteBrandingId() {
		return instituteBrandingId;
	}
	
	/**
	 * Sets the institute branding id.
	 *
	 * @param instituteBrandingId the new institute branding id
	 */
	public void setInstituteBrandingId(Long instituteBrandingId) {
		this.instituteBrandingId = instituteBrandingId;
	}
	
	/**
	 * Gets the institute.
	 *
	 * @return the institute
	 */
	@ManyToOne (fetch=FetchType.EAGER)
	@JoinColumn(name="institute_id", nullable=false)
	public Institute getInstitute() {
		return institute;
	}
	
	/**
	 * Sets the institute.
	 *
	 * @param institute the new institute
	 */
	public void setInstitute(Institute institute) {
		this.institute = institute;
	}
	
	/**
	 * Gets the branding attribute.
	 *
	 * @return the branding attribute
	 */
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name="branding_attribute_id", nullable=false)
	public BrandingAttribute getBrandingAttribute() {
		return brandingAttribute;
	}
	
	/**
	 * Sets the branding attribute.
	 *
	 * @param brandingAttribute the new branding attribute
	 */
	public void setBrandingAttribute(BrandingAttribute brandingAttribute) {
		this.brandingAttribute = brandingAttribute;
	}	

	/**
	 * Gets the branding attribute value.
	 *
	 * @return the branding attribute value
	 */
	@Column(name = "branding_attribute_value")
	public String getBrandingAttributeValue() {
		return brandingAttributeValue;
	}
	
	/**
	 * Sets the branding attribute value.
	 *
	 * @param brandingAttributeValue the new branding attribute value
	 */
	public void setBrandingAttributeValue(String brandingAttributeValue) {
		this.brandingAttributeValue = brandingAttributeValue;
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
				+ ((brandingAttribute == null) ? 0 : brandingAttribute.getBrandingAttributeId()
						.hashCode());
		result = prime * result
				+ ((institute == null) ? 0 : institute.getInstituteId().hashCode());
		result = prime * result
				+ ((brandingAttributeValue == null) ? 0 : brandingAttributeValue.hashCode());
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
		if (!(obj instanceof InstituteBranding)) {
			return false;
		}
		InstituteBranding other = (InstituteBranding) obj;
		if (brandingAttribute == null) {
			if (other.brandingAttribute != null) {
				return false;
			}
		} else if (!brandingAttribute.getBrandingAttributeId().equals(other.brandingAttribute.getBrandingAttributeId())) {
			return false;
		}
		if (institute == null) {
			if (other.institute != null) {
				return false;
			}
		} else if (!institute.getInstituteId().equals(other.institute.getInstituteId())) {
			return false;
		}
		if (brandingAttributeValue == null) {
			if (other.brandingAttributeValue != null) {
				return false;
			}
		} else if (!brandingAttributeValue.equals(other.brandingAttributeValue)) {
			return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	@Override
	public String toString() {
		return "InstituteBranding [instituteBrandingId=" + instituteBrandingId
				+ ", instituteId=" + institute.getInstituteId() + ", brandingAttributeId="
				+ brandingAttribute.getBrandingAttributeId()+ ", brandingAttributeValue="
						+ brandingAttributeValue + "]";
	}
	
}
