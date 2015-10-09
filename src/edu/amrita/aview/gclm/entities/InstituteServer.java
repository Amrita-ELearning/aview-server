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
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;


/**
 * The Class InstituteServer.
 */
@Entity
@Table(name = "institute_server")
@BatchSize(size=1000)
public class InstituteServer extends Auditable {
	
	/** The institute server id. */
	private Long instituteServerId = 0l;
	
	/** The server type id. */
	private Integer serverTypeId = 0;
	
	/** The institute. */
	private Institute institute;
	
	/** The server. */
	private Server server;
	
	//non-mapped attribute
	/** The server type. */
	private String serverType = "";
	
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
	 * @param institute the institute to set
	 */
	public void setInstitute(Institute institute) {
		this.institute = institute;
	}
	
	/**
	 * Gets the institute server id.
	 *
	 * @return the instituteServerId
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "institute_server_id")
	public Long getInstituteServerId() {
		return instituteServerId;
	}
	
	/**
	 * Sets the institute server id.
	 *
	 * @param instituteServerId the instituteServerId to set
	 */	
	public void setInstituteServerId(Long instituteServerId) {
		this.instituteServerId = instituteServerId;
	}
	
	/**
	 * Gets the server type id.
	 *
	 * @return the serverTypeId
	 */	
	@Column(name = "server_type_id")
	public Integer getServerTypeId() {
		return serverTypeId;
	}
	
	/**
	 * Sets the server type id.
	 *
	 * @param serverTypeId the serverTypeId to set
	 */		
	public void setServerTypeId(Integer serverTypeId) {
		this.serverTypeId = serverTypeId;
	}
	
	/**
	 * Gets the server.
	 *
	 * @return the server
	 */
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name="server_id", nullable=false)
	public Server getServer() {
		return server;
	}

	/**
	 * Sets the server.
	 *
	 * @param server the new server
	 */
	public void setServer(Server server) {
		this.server = server;
	}

	/**
	 * Gets the server type.
	 *
	 * @return the server type
	 */
	@Transient
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
		sb.append(this.instituteServerId);
		sb.append(Constant.DELIMETER);
		sb.append(this.institute.getInstituteId());
		sb.append(Constant.DELIMETER);
		sb.append(this.getServer().getServerId());
		sb.append(Constant.DELIMETER);
		sb.append(this.serverTypeId);
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
				+ ((institute == null) ? 0 : institute.getInstituteId().hashCode());
		result = prime * result + ((server == null) ? 0 : server.getServerId().hashCode());
		result = prime * result
				+ ((serverTypeId == null) ? 0 : serverTypeId.hashCode());
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
		if (!(obj instanceof InstituteServer))
			return false;
		InstituteServer other = (InstituteServer) obj;
		if (institute == null) {
			if (other.institute != null)
				return false;
		} else if (!institute.getInstituteId().equals(other.institute.getInstituteId()))
			return false;
		if (server == null) {
			if (other.server != null)
				return false;
		} else if (!server.getServerId().equals(other.server.getServerId()))
			return false;
		if (serverTypeId == null) {
			if (other.serverTypeId != null)
				return false;
		} else if (!serverTypeId.equals(other.serverTypeId))
			return false;
		return true;
	}
	
}
