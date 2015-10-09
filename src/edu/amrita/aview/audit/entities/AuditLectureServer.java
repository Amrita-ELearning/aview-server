/*
 * 
 */
package edu.amrita.aview.audit.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;



/**
 * The Class AuditLectureServer.
 */
@Entity
@Table(name = "audit_lecture_server")
public class AuditLectureServer extends Auditable {

	/** The audit lecture server id. */
	private Long auditLectureServerId = 0l;
	
	/** The server name. */
	private String serverName = null;
	
	/** The server ip. */
	private String serverIP = null;
	
	/** The server domain. */
	private String serverDomain = null;
	
	/** The server port. */
	private Integer serverPort = 0;
	
	/** The presenter publishing bandwidth kbps. */
	private Integer presenterPublishingBandwidthKbps = 0;
	
	/** The server type id. */
	private Integer serverTypeId = 0;
	
	/** The aview audit lecture. */
	private AuditLecture aviewAuditLecture = null;
	
	/*
	 * @return the aviewAuditLecture
	 */
	/**
	 * Gets the aview audit lecture.
	 *
	 * @return the aview audit lecture
	 */
	@ManyToOne
	@JoinColumn(name="audit_lecture_id",nullable=false)
	public AuditLecture getAviewAuditLecture()
	{
		return aviewAuditLecture;
	}
	
	/*
	 *  @param aviewAuditLecture to set aviewAuditLecture
	 */
	/**
	 * Sets the aview audit lecture.
	 *
	 * @param aviewAuditLecture the new aview audit lecture
	 */
	public void setAviewAuditLecture(AuditLecture aviewAuditLecture)
	{
		this.aviewAuditLecture = aviewAuditLecture;
	}
	
	/*
	 * return auditLectureServerId
	 */
	/**
	 * Gets the audit lecture server id.
	 *
	 * @return the audit lecture server id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="audit_lecture_server_id")
	public Long getAuditLectureServerId()
	{
		return auditLectureServerId;
	}
	
	/*
	 * @param auditLectureServerId to set auditLectureServerId
	 */
	/**
	 * Sets the audit lecture server id.
	 *
	 * @param auditLectureServerId the new audit lecture server id
	 */
	public void setAuditLectureServerId(Long auditLectureServerId)
	{
		this.auditLectureServerId = auditLectureServerId;
	}
	
	/*
	 * return serverName
	 */
	/**
	 * Gets the server name.
	 *
	 * @return the server name
	 */
	@Column(name="server_name")
	public String getServerName()
	{
		return serverName;
	}
	
	/*
	 * @param serverName to set serverName
	 */
	/**
	 * Sets the server name.
	 *
	 * @param serverName the new server name
	 */
	public void setServerName(String serverName)
	{
		this.serverName = serverName;
	}
	
	/*
	 * return serverIP
	 */
	/**
	 * Gets the server ip.
	 *
	 * @return the server ip
	 */
	@Column(name="server_ip")
	public String getServerIP()
	{
		return serverIP;
	}
	
	/*
	 * @param serverName to set serverName
	 */
	/**
	 * Sets the server ip.
	 *
	 * @param serverIP the new server ip
	 */
	public void setServerIP(String serverIP)
	{
		this.serverIP = serverIP;
	}
	
	/*
	 * return serverDomain
	 */
	/**
	 * Gets the server domain.
	 *
	 * @return the server domain
	 */
	@Column(name="server_domain")
	public String getServerDomain()
	{
		return serverDomain;
	}
	
	/*
	 * @param serverName to set serverName
	 */
	/**
	 * Sets the server domain.
	 *
	 * @param serverDomain the new server domain
	 */
	public void setServerDomain(String serverDomain)
	{
		this.serverDomain = serverDomain;
	}
	
	/*
	 * return serverPort
	 */
	/**
	 * Gets the server port.
	 *
	 * @return the server port
	 */
	@Column(name="server_port")
	public Integer getServerPort()
	{
		return serverPort;
	}
	
	/*
	 * @param serverPort to set serverPort
	 */
	/**
	 * Sets the server port.
	 *
	 * @param serverPort the new server port
	 */
	public void setServerPort(Integer serverPort)
	{
		this.serverPort = serverPort;
	}
	
	/*
	 * return presenterPublishingBandwidthKbps
	 */
	/**
	 * Gets the presenter publishing bandwidth kbps.
	 *
	 * @return the presenter publishing bandwidth kbps
	 */
	@Column(name="presenter_publishing_bw_kbps")
	public Integer getPresenterPublishingBandwidthKbps()
	{
		return presenterPublishingBandwidthKbps;
	}
	
	/*
	 * @param presenterPublishingBandwidthKbps to set presenterPublishingBandwidthKbps
	 */
	/**
	 * Sets the presenter publishing bandwidth kbps.
	 *
	 * @param presenterPublishingBandwidthKbps the new presenter publishing bandwidth kbps
	 */
	public void setPresenterPublishingBandwidthKbps(Integer presenterPublishingBandwidthKbps)
	{
		this.presenterPublishingBandwidthKbps = presenterPublishingBandwidthKbps;
	}
	
	/*
	 * return serverType
	 */
	/**
	 * Gets the server type id.
	 *
	 * @return the server type id
	 */
	@Column(name="server_type_id")
	public Integer getServerTypeId()
	{
		return serverTypeId;
	}
	
	/*
	 * @param serverType to set serverType
	 */
	/**
	 * Sets the server type id.
	 *
	 * @param serverTypeId the new server type id
	 */
	public void setServerTypeId(Integer serverTypeId)
	{
		this.serverTypeId = serverTypeId;
	}
	
	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.auditLectureServerId);
		sb.append(Constant.DELIMETER);
		sb.append(this.aviewAuditLecture.getAuditLectureId());
		sb.append(Constant.DELIMETER);
		sb.append(this.serverName);
		sb.append(Constant.DELIMETER);
		sb.append(this.serverIP);
		sb.append(Constant.DELIMETER);
		sb.append(this.serverDomain);
		sb.append(Constant.DELIMETER);
		sb.append(this.serverPort);
		sb.append(Constant.DELIMETER);
		sb.append(this.serverTypeId);
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
		result = prime
				* result
				+ ((aviewAuditLecture == null) ? 0 : aviewAuditLecture
						.hashCode());
		result = prime
				* result
				+ ((presenterPublishingBandwidthKbps == null) ? 0
						: presenterPublishingBandwidthKbps.hashCode());
		result = prime * result
				+ ((serverDomain == null) ? 0 : serverDomain.hashCode());
		result = prime * result
				+ ((serverIP == null) ? 0 : serverIP.hashCode());
		result = prime * result
				+ ((serverName == null) ? 0 : serverName.hashCode());
		result = prime * result
				+ ((serverPort == null) ? 0 : serverPort.hashCode());
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
		if (!(obj instanceof AuditLectureServer))
			return false;
		AuditLectureServer other = (AuditLectureServer) obj;
		if (aviewAuditLecture == null) {
			if (other.aviewAuditLecture != null)
				return false;
		} else if (!aviewAuditLecture.equals(other.aviewAuditLecture))
			return false;
		if (presenterPublishingBandwidthKbps == null) {
			if (other.presenterPublishingBandwidthKbps != null)
				return false;
		} else if (!presenterPublishingBandwidthKbps
				.equals(other.presenterPublishingBandwidthKbps))
			return false;
		if (serverDomain == null) {
			if (other.serverDomain != null)
				return false;
		} else if (!serverDomain.equals(other.serverDomain))
			return false;
		if (serverIP == null) {
			if (other.serverIP != null)
				return false;
		} else if (!serverIP.equals(other.serverIP))
			return false;
		if (serverName == null) {
			if (other.serverName != null)
				return false;
		} else if (!serverName.equals(other.serverName))
			return false;
		if (serverPort == null) {
			if (other.serverPort != null)
				return false;
		} else if (!serverPort.equals(other.serverPort))
			return false;
		if (serverTypeId == null) {
			if (other.serverTypeId != null)
				return false;
		} else if (!serverTypeId.equals(other.serverTypeId))
			return false;
		return true;
	}
	
}
