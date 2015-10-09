/*
 * 
 */
package edu.amrita.aview.feedback.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.amrita.aview.common.entities.Auditable;


/**
 * The Class Module.
 */
@Entity
@Table(name="module")
public class Module extends Auditable{
	
	/** The module id. */
	private Integer moduleId = 0;
	
	/** The module name. */
	private String moduleName = null;
	
	/**
	 * Gets the module id.
	 *
	 * @return the module id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "module_id")
	public Integer getModuleId() {
		return moduleId;
	}
	
	/**
	 * Sets the module id.
	 *
	 * @param moduleId the module id
	 */
	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}
	
	/**
	 * Gets the module name.
	 *
	 * @return the module name
	 */
	@Column(name = "module_name")
	public String getModuleName() {
		return moduleName;
	}
	
	/**
	 * Sets the module name.
	 *
	 * @param moduleName the module name
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((moduleName == null) ? 0 : moduleName.hashCode());
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
		if (!(obj instanceof Module))
			return false;
		Module other = (Module) obj;
		if (moduleName == null) {
			if (other.moduleName != null)
				return false;
		} else if (!moduleName.equals(other.moduleName))
			return false;
		return true;
	}

}
