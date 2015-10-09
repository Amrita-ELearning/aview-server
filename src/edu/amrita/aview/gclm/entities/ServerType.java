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

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;


/**
 * The Class ServerType.
 */
@Entity
@Table(name="server_type")
public class ServerType extends Auditable{
	
/** The server type id. */
private Integer serverTypeId=0;

/** The server type. */
private String serverType=null;

/**
 * Gets the server type id.
 *
 * @return the server type id
 */
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
@Column(name = "server_type_id")
public Integer getServerTypeId() {
	return serverTypeId;
}

/**
 * Sets the server type id.
 *
 * @param serverTypeId the new server type id
 */
public void setServerTypeId(Integer serverTypeId) {
	this.serverTypeId = serverTypeId;
}


/**
 * Gets the server type.
 *
 * @return the server type
 */
@Column(name = "server_type")
public String getServerType() {
	return serverType;
}

/**
 * Sets the server type.
 *
 * @param serverType the new server type
 */
public void setServerType(String serverType) {
	this.serverType = serverType;
}

/* (non-Javadoc)
 * @see com.amrita.edu.entities.Auditable#toString()
 */
public String toString() {
	StringBuilder sb = new StringBuilder(256);
	sb.append(this.serverTypeId);
	sb.append(Constant.DELIMETER);
	sb.append(this.serverType);
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
			+ ((serverType == null) ? 0 : serverType.hashCode());
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
	final ServerType other = (ServerType) obj;
	if (serverType == null) {
		if (other.serverType != null)
			return false;
	} else if (!serverType.equals(other.serverType))
		return false;
	return true;
}	


}
