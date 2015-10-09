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
 * The Class Server.
 */
@Entity
@Table(name="server")
public class Server extends Auditable
{
	
	/** The server id. */
	private Integer serverId=0;
	
	/** The server name. */
	private String serverName= null;
	
	/** The server ip. */
	private String serverIp= null;
	
	/** The server domain. */
	private String serverDomain= null;
	
	/** The server category. */
	private String serverCategory = null;
	
	/** The supports animation. */
	private String supportsAnimation = null;
	
	
	/**
	 * Gets the server id.
	 *
	 * @return the server id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "server_id")
	public Integer getServerId() {
		return serverId;
	}
	
	/**
	 * Sets the server id.
	 *
	 * @param serverId the new server id
	 */
	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}
	
	/**
	 * Gets the server name.
	 *
	 * @return the server name
	 */
	@Column(name = "server_name")
	public String getServerName() {
		return serverName;
	}
	
	/**
	 * Sets the server name.
	 *
	 * @param serverName the new server name
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	/**
	 * Gets the server ip.
	 *
	 * @return the server ip
	 */
	@Column(name = "server_ip")
	public String getServerIp() {
		return serverIp;
	}
	
	/**
	 * Sets the server ip.
	 *
	 * @param serverIp the new server ip
	 */
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	
	/**
	 * Gets the server domain.
	 *
	 * @return the server domain
	 */
	@Column(name = "server_domain")
	public String getServerDomain() {
		return serverDomain;
	}
	
	/**
	 * Sets the server domain.
	 *
	 * @param serverDomain the new server domain
	 */
	public void setServerDomain(String serverDomain) {
		this.serverDomain = serverDomain;
	}
	
	/**
	 * Gets the server category.
	 *
	 * @return the server category
	 */
	@Column(name = "server_category")
	public String getServerCategory() {
		return serverCategory;
	}
	
	/**
	 * Sets the server category.
	 *
	 * @param serverCategory the new server category
	 */
	public void setServerCategory(String serverCategory) {
		this.serverCategory = serverCategory;
	}
	
	/**
	 * Gets the supports animation.
	 *
	 * @return the supports animation
	 */
	@Column(name = "supports_animation")
	public String getSupportsAnimation() {
		return supportsAnimation;
	}
	
	/**
	 * Sets the supports animation.
	 *
	 * @param supportsAnimation the new supports animation
	 */
	public void setSupportsAnimation(String supportsAnimation) {
		this.supportsAnimation = supportsAnimation;
	}
	
	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.serverId);
		sb.append(Constant.DELIMETER);
		sb.append(this.serverName);
		sb.append(Constant.DELIMETER);
		sb.append(this.serverIp);
		sb.append(Constant.DELIMETER);
		sb.append(this.serverDomain);	
		sb.append(Constant.DELIMETER);
		sb.append(this.supportsAnimation);	
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
				+ ((serverName == null) ? 0 : serverName.hashCode());
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
		final Server other = (Server) obj;
		if (serverName == null) {
			if (other.serverName != null)
				return false;
		} else if (!serverName.equals(other.serverName))
			return false;
		return true;
	}	
	
}
