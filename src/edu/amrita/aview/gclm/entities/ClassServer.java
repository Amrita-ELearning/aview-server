/**
 * 
 */
package edu.amrita.aview.gclm.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;


/**
 * The Class ClassServer.
 *
 * @author
 */
@Entity
@Table(name = "class_server")
public class ClassServer extends Auditable {

	/** The class server id. */
	private Long classServerId = 0l;
	
	/** The server type id. */
	private Integer serverTypeId = 0;
	
	/** The server port. */
	private Integer serverPort = 0;
	
	/** The presenter publishing bandwidth kbps. */
	private Integer presenterPublishingBandwidthKbps = 0;

	/** The aview class. */
	private Class aviewClass = null;
	
	/** The server. */
	private Server server = null;
	
	//Non mapped attribute
	/** The server type name. */
	private String serverTypeName = null;
	
	/**
	 * Gets the aview class.
	 *
	 * @return the aviewClass
	 */
	@ManyToOne
	@JoinColumn(name="class_id", nullable=false)
	public Class getAviewClass() {
		return aviewClass;
	}
	
	/**
	 * Sets the aview class.
	 *
	 * @param aviewClass the aviewClass to set
	 */
	public void setAviewClass(Class aviewClass) {
		this.aviewClass = aviewClass;
	}
	

	/**
	 * Gets the server.
	 *
	 * @return the server
	 */
	@OneToOne
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
	 * Gets the class server id.
	 *
	 * @return the classServerId
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "class_server_id")
	public Long getClassServerId() {
		return classServerId;
	}
	
	/**
	 * Sets the class server id.
	 *
	 * @param classServerId the classServerId to set
	 */
	public void setClassServerId(Long classServerId) {
		this.classServerId = classServerId;
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
	 * Gets the server port.
	 *
	 * @return the serverPort
	 */			
	@Column(name = "server_port")
	public Integer getServerPort() {
		return serverPort;
	}
	
	/**
	 * Sets the server port.
	 *
	 * @param serverPort the new server port
	 */	
	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}
	
	/**
	 * Gets the presenter publishing bandwidth kbps.
	 *
	 * @return the presenterPublishingBandwidthKbps
	 */	
	@Column(name = "presenter_publishing_bw_kbps")
	public Integer getPresenterPublishingBandwidthKbps() {
		return presenterPublishingBandwidthKbps;
	}
	
	/**
	 * Sets the presenter publishing bandwidth kbps.
	 *
	 * @param presenterPublishingBandwidthKbps the presenterPublishingBandwidthKbps to set
	 */				
	public void setPresenterPublishingBandwidthKbps(
			Integer presenterPublishingBandwidthKbps) {
		this.presenterPublishingBandwidthKbps = presenterPublishingBandwidthKbps;
	}

	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.classServerId);
		sb.append(Constant.DELIMETER);
		sb.append(this.aviewClass.getClassId());
		sb.append(Constant.DELIMETER);
		sb.append(this.server.getServerId());
		sb.append(Constant.DELIMETER);
		sb.append(this.serverTypeId);
		sb.append(Constant.DELIMETER);
		sb.append(this.serverPort);
		sb.append(Constant.DELIMETER);
		sb.append(this.presenterPublishingBandwidthKbps);
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
				+ ((aviewClass == null) ? 0 : aviewClass.getClassId().hashCode());
		result = prime
				* result
				+ ((presenterPublishingBandwidthKbps == null) ? 0
						: presenterPublishingBandwidthKbps.hashCode());
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
		if (!(obj instanceof ClassServer))
			return false;
		ClassServer other = (ClassServer) obj;
		if (aviewClass == null) {
			if (other.aviewClass != null)
				return false;
		} else if (!aviewClass.getClassId().equals(other.aviewClass.getClassId()))
			return false;
		if (presenterPublishingBandwidthKbps == null) {
			if (other.presenterPublishingBandwidthKbps != null)
				return false;
		} else if (!presenterPublishingBandwidthKbps
				.equals(other.presenterPublishingBandwidthKbps))
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

	/**
	 * Gets the server type name.
	 *
	 * @return the server type name
	 */
	@Transient
	public String getServerTypeName() {
		return serverTypeName;
	}

	/**
	 * Sets the server type name.
	 *
	 * @param serverTypeName the new server type name
	 */
	public void setServerTypeName(String serverTypeName) {
		this.serverTypeName = serverTypeName;
	}


}
