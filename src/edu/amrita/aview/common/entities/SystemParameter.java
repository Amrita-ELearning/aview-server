/*
 * 
 */
package edu.amrita.aview.common.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



/**
 * The Class SystemParameter.
 */
@Entity
@Table(name="system_parameter")
public class SystemParameter extends Auditable {
	
	/** The parameter id. */
	private Integer parameterId = 0;
	
	/** The parameter name. */
	private String parameterName = null;
	
	/** The parameter value. */
	private String parameterInfo = null;

	/**
	 * Gets the parameter id.
	 *
	 * @return the parameter id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "parameter_id")
	public Integer getParameterId() {
		return parameterId;
	}
	
	/**
	 * Sets the parameter id.
	 *
	 * @param parameterId the new parameter id
	 */
	public void setParameterId(Integer parameterId) {
		this.parameterId = parameterId;
	}
	
	/**
	 * Gets the parameter name.
	 *
	 * @return the parameter name
	 */
	@Column(name = "parameter_name")
	public String getParameterName() {
		return parameterName;
	}
	
	/**
	 * Sets the parameter name.
	 *
	 * @param parameterName the new parameter name
	 */
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	/**
	 * Gets the parameter info.
	 *
	 * @return the parameterInfo
	 */
	@Column(name = "parameter_value")
	public String getParameterInfo() {
		return parameterInfo;
	}

	/**
	 * Sets the parameter info.
	 *
	 * @param parameterInfo the parameterInfo to set
	 */
	public void setParameterInfo(String parameterInfo) {
		this.parameterInfo = parameterInfo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((parameterName == null) ? 0 : parameterName.hashCode());
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
		if (!(obj instanceof SystemParameter))
			return false;
		SystemParameter other = (SystemParameter) obj;
		if (parameterName == null) {
			if (other.parameterName != null)
				return false;
		} else if (!parameterName.equals(other.parameterName))
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	@Override
	public String toString() {
		return "SystemParameter [parameterId=" + parameterId
				+ ", parameterName=" + parameterName + ", parameterInfo ="
				+ parameterInfo + "]";
	}
	
}
