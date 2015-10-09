/**
 * 
 */
package edu.amrita.aview.gclm.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;


/**
 * The Class InstituteCategory.
 *
 * @author
 */
@Entity
@Table(name="institute_category")
public class InstituteCategory extends Auditable {
	
	/** The institute category id. */
	private Integer instituteCategoryId=0;
	
	/** The institute category name. */
	private String instituteCategoryName=null;
	
	/**
	 * Gets the institute category id.
	 *
	 * @return the institute_category_id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	 * Gets the institute category name.
	 *
	 * @return the institute_category_name
	 */
	@Column(name = "institute_category_name")
	public String getInstituteCategoryName() {
		return instituteCategoryName;
	}
	
	/**
	 * Sets the institute category name.
	 *
	 * @param instituteCategoryName the institute_category_name to set
	 */
	public void setInstituteCategoryName(String instituteCategoryName) {
		this.instituteCategoryName =instituteCategoryName;
	}
	
	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.instituteCategoryId);
		sb.append(Constant.DELIMETER);
		sb.append(this.instituteCategoryName);
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
		result = prime
				* result
				+ ((instituteCategoryName == null) ? 0 : instituteCategoryName
						.hashCode());
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
		final InstituteCategory other = (InstituteCategory) obj;
		if (instituteCategoryName == null) {
			if (other.instituteCategoryName != null)
				return false;
		} else if (!instituteCategoryName.equals(other.instituteCategoryName))
			return false;
		return true;
	}
	
}
