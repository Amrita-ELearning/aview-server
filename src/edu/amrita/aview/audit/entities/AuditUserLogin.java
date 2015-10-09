/*
 * 
 */
package edu.amrita.aview.audit.entities;



import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;


/**
 * The Class AuditUserLogin.
 */
@Entity
@Table(name = "audit_user_login")
public class AuditUserLogin extends Auditable{

	/** The audit user login id. */
	private Long auditUserLoginId = 0l ;
	
	/** The user id. */
	private Long userId=0l;
	
	/** The ip address. */
	private String ipAddress = null;
	
	/** The login time. */
	private Timestamp loginTime =null;
	
	/** The log out time. */
	private Timestamp logOutTime=null ;
	
	/** The operating system. */
	private String operatingSystem=null;
	
	/** The flash player version. */
	private String flashPlayerVersion=null ;
	
	/** The network connection type. */
	private String networkConnectionType=null ;
	
	/** The aview version. */
	private String aviewVersion=null;
	
	/** The gui mode. */
	private String guiMode=null;
	
	/** The auth mode. */
	private String authMode=null;
	
	/** The hardware address. */
	private String hardwareAddress = null;
	
	/** The external ip address. */
	private String externalIPAddress = null;
	
	
	/**
	 * Gets the audit user login id.
	 *
	 * @return the audit user login id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "audit_user_login_id")
	public Long getAuditUserLoginId() {
		return auditUserLoginId;
	}

	/**
	 * Sets the audit user login id.
	 *
	 * @param auditUserLoginId the new audit user login id
	 */
	public void setAuditUserLoginId(Long auditUserLoginId) {
		this.auditUserLoginId = auditUserLoginId;
	}

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	@Column(name = "user_id")
	public Long getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Gets the login time.
	 *
	 * @return the login time
	 */
	@Column(name = "login_time")
	public Timestamp getLoginTime() {
		return loginTime;
	}
	
	/**
	 * Sets the login time.
	 *
	 * @param loginTime the new login time
	 */
	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}
	
	/**
	 * Gets the log out time.
	 *
	 * @return the log out time
	 */
	@Column(name = "logout_time")
	public Timestamp getLogOutTime() {
		return logOutTime;
	}
	
	/**
	 * Sets the log out time.
	 *
	 * @param logOutTime the new log out time
	 */
	public void setLogOutTime(Timestamp logOutTime) {
		this.logOutTime = logOutTime;
	}
	
	/**
	 * Gets the operating system.
	 *
	 * @return the operating system
	 */
	@Column(name = "operating_system")
	public String getOperatingSystem() {
		return operatingSystem;
	}
	
	/**
	 * Sets the operating system.
	 *
	 * @param operatingSystem the new operating system
	 */
	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}
	
	/**
	 * Gets the flash player version.
	 *
	 * @return the flash player version
	 */
	@Column(name = "flash_player_version")
	public String getFlashPlayerVersion() {
		return flashPlayerVersion;
	}
	
	/**
	 * Sets the flash player version.
	 *
	 * @param flashPlayerVersion the new flash player version
	 */
	public void setFlashPlayerVersion(String flashPlayerVersion) {
		this.flashPlayerVersion = flashPlayerVersion;
	}
	
	/**
	 * Gets the network connection type.
	 *
	 * @return the network connection type
	 */
	@Column(name = "network_connection_type")
	public String getNetworkConnectionType() {
		return networkConnectionType;
	}
	
	/**
	 * Sets the network connection type.
	 *
	 * @param networkConnectionType the new network connection type
	 */
	public void setNetworkConnectionType(String networkConnectionType) {
		this.networkConnectionType = networkConnectionType;
	}
	
	/**
	 * Gets the aview version.
	 *
	 * @return the aview version
	 */
	@Column(name = "aview_version")
	public String getAviewVersion() {
		return aviewVersion;
	}
	
	/**
	 * Sets the aview version.
	 *
	 * @param aviewVersion the new aview version
	 */
	public void setAviewVersion(String aviewVersion) {
		this.aviewVersion = aviewVersion;
	}
	
	/**
	 * Gets the ip address.
	 *
	 * @return the ip address
	 */
	@Column(name = "ip_address")
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * Sets the ip address.
	 *
	 * @param ipAddress the new ip address
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * Gets the hardware address.
	 *
	 * @return the hardware address
	 */
	@Column(name = "hardware_address")
	public String getHardwareAddress() {
		return hardwareAddress;
	}

	/**
	 * Sets the hardware address.
	 *
	 * @param hardwareAddress the new hardware address
	 */
	public void setHardwareAddress(String hardwareAddress) {
		this.hardwareAddress = hardwareAddress;
	}
	
	/**
	 * Gets the external ip address.
	 *
	 * @return the external ip address
	 */
	@Column(name = "external_ip_address")
	public String getExternalIPAddress() {
		return externalIPAddress;
	}

	/**
	 * Sets the external ip address.
	 *
	 * @param externalIPAddress the new external ip address
	 */
	public void setExternalIPAddress(String externalIPAddress) {
		this.externalIPAddress = externalIPAddress;
	}

	/**
	 * Gets the auth mode.
	 *
	 * @return the auth mode
	 */
	@Column(name = "auth_mode")
	public String getAuthMode() {
		return authMode;
	}

	/**
	 * Sets the auth mode.
	 *
	 * @param authMode the new auth mode
	 */
	public void setAuthMode(String authMode) {
		this.authMode = authMode;
	}

	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.auditUserLoginId);
		sb.append(Constant.DELIMETER);
		sb.append(this.userId);
		sb.append(Constant.DELIMETER);
		sb.append(this.ipAddress);
		sb.append(Constant.DELIMETER);
		sb.append(this.externalIPAddress);
		sb.append(Constant.DELIMETER);
		sb.append(this.hardwareAddress);
		sb.append(Constant.DELIMETER);
		sb.append(this.loginTime);
		sb.append(Constant.DELIMETER);
		sb.append(this.logOutTime);
		sb.append(Constant.DELIMETER);
		sb.append(this.operatingSystem);
		sb.append(Constant.DELIMETER);
		sb.append(this.flashPlayerVersion);
		sb.append(Constant.DELIMETER);
		sb.append(this.networkConnectionType);
		sb.append(Constant.DELIMETER);
		sb.append(this.aviewVersion);
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
				+ ((loginTime == null) ? 0 : loginTime.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		AuditUserLogin other = (AuditUserLogin) obj;
		if (loginTime == null) {
			if (other.loginTime != null)
				return false;
		} else if (!loginTime.equals(other.loginTime))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	/**
	 * Gets the gui mode.
	 *
	 * @return the gui mode
	 */
	@Column(name = "gui_mode")
	public String getGuiMode() {
		return guiMode;
	}

	/**
	 * Sets the gui mode.
	 *
	 * @param guiMode the new gui mode
	 */
	public void setGuiMode(String guiMode) {
		this.guiMode = guiMode;
	}

}




