/*
 * 
 */
package edu.amrita.aview.audit.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;



/**
 * The Class AuditUserSetting.
 */
@Entity
@Table(name = "audit_user_setting")
public class AuditUserSetting extends Auditable {
	
	/** The audit user setting id. */
	private Long auditUserSettingId = 0l ;
	
	/** The audit user login id. */
	private Long auditUserLoginId=0l;
	
	/** The audit lecture id. */
	private Long auditLectureId=0l ;
	
	/** The screen resolution. */
	private String screenResolution=null ;
	
	/** The video driver. */
	private String videoDriver=null ;
	
	/** The microphone driver. */
	private String microphoneDriver =null;
	
	/** The publishing bw. */
	private String publishingBW=null;;
	
	/** The pixel aspect ratio. */
	private int pixelAspectRatio=0;
	
	/**
	 * Gets the audit user setting id.
	 *
	 * @return the audit user setting id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "audit_user_setting_id")
	public long getAuditUserSettingId() {
		return auditUserSettingId;
	}
	
	/**
	 * Sets the audit user setting id.
	 *
	 * @param auditUserSettingId the new audit user setting id
	 */
	public void setAuditUserSettingId(long auditUserSettingId) {
		this.auditUserSettingId = auditUserSettingId;
	}
	
	/**
	 * Gets the audit user login id.
	 *
	 * @return the audit user login id
	 */
	@Column(name = "audit_user_login_id")
	public long getAuditUserLoginId() {
		return auditUserLoginId;
	}
	
	/**
	 * Sets the audit user login id.
	 *
	 * @param auditUserLoginId the new audit user login id
	 */
	public void setAuditUserLoginId(long auditUserLoginId) {
		this.auditUserLoginId = auditUserLoginId;
	}
	
	/**
	 * Gets the audit lecture id.
	 *
	 * @return the audit lecture id
	 */
	@Column(name = "audit_lecture_id")
	public long getAuditLectureId() {
		return auditLectureId;
	}
	
	/**
	 * Sets the audit lecture id.
	 *
	 * @param auditLectureId the new audit lecture id
	 */
	public void setAuditLectureId(long auditLectureId) {
		this.auditLectureId = auditLectureId;
	}
	
	/**
	 * Gets the screen resolution.
	 *
	 * @return the screen resolution
	 */
	@Column(name = "screen_resolution")
	public String getScreenResolution() {
		return screenResolution;
	}
	
	/**
	 * Sets the screen resolution.
	 *
	 * @param screenResolution the new screen resolution
	 */
	public void setScreenResolution(String screenResolution) {
		this.screenResolution = screenResolution;
	}
	
	/**
	 * Gets the video driver.
	 *
	 * @return the video driver
	 */
	@Column(name = "video_driver")
	public String getVideoDriver() {
		return videoDriver;
	}
	
	/**
	 * Sets the video driver.
	 *
	 * @param videoDriver the new video driver
	 */
	public void setVideoDriver(String videoDriver) {
		this.videoDriver = videoDriver;
	}
	
	/**
	 * Gets the microphone driver.
	 *
	 * @return the microphone driver
	 */
	@Column(name = "microphone_driver")
	public String getMicrophoneDriver() {
		return microphoneDriver;
	}
	
	/**
	 * Sets the microphone driver.
	 *
	 * @param microphoneDriver the new microphone driver
	 */
	public void setMicrophoneDriver(String microphoneDriver) {
		this.microphoneDriver = microphoneDriver;
	}
	
	/**
	 * Gets the publishing bw.
	 *
	 * @return the publishing bw
	 */
	@Column(name = "publishing_bw")
	public String getPublishingBW() {
		return publishingBW;
	}
	
	/**
	 * Sets the publishing bw.
	 *
	 * @param publishingBW the new publishing bw
	 */
	public void setPublishingBW(String publishingBW) {
		this.publishingBW = publishingBW;
	}
	
	/**
	 * Gets the pixel aspect ratio.
	 *
	 * @return the pixel aspect ratio
	 */
	@Column(name = "pixel_aspect_ratio")	
	public int getPixelAspectRatio() {
		return pixelAspectRatio;
	}
	
	/**
	 * Sets the pixel aspect ratio.
	 *
	 * @param pixelAspectRatio the new pixel aspect ratio
	 */
	public void setPixelAspectRatio(int pixelAspectRatio) {
		this.pixelAspectRatio = pixelAspectRatio;
	}
	
	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.auditUserSettingId);
		sb.append(Constant.DELIMETER);
		sb.append(this.auditUserLoginId);
		sb.append(Constant.DELIMETER);
		sb.append(this.auditLectureId);
		sb.append(Constant.DELIMETER);
		sb.append(this.screenResolution);
		sb.append(Constant.DELIMETER);
		sb.append(this.videoDriver);
		sb.append(Constant.DELIMETER);
		sb.append(this.microphoneDriver);
		sb.append(Constant.DELIMETER);
		sb.append(this.publishingBW);
		sb.append(Constant.DELIMETER);
		sb.append(this.pixelAspectRatio);
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
				+ ((auditLectureId == null) ? 0 : auditLectureId.hashCode());
		result = prime
				* result
				+ ((auditUserLoginId == null) ? 0 : auditUserLoginId.hashCode());
		result = prime
				* result
				+ ((microphoneDriver == null) ? 0 : microphoneDriver.hashCode());
		result = prime * result + pixelAspectRatio;
		result = prime * result + publishingBW.hashCode();
		result = prime
				* result
				+ ((screenResolution == null) ? 0 : screenResolution.hashCode());
		result = prime * result
				+ ((videoDriver == null) ? 0 : videoDriver.hashCode());
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
		AuditUserSetting other = (AuditUserSetting) obj;
		if (auditLectureId == null) {
			if (other.auditLectureId != null)
				return false;
		} else if (!auditLectureId.equals(other.auditLectureId))
			return false;
		if (auditUserLoginId == null) {
			if (other.auditUserLoginId != null)
				return false;
		} else if (!auditUserLoginId.equals(other.auditUserLoginId))
			return false;
		if (microphoneDriver == null) {
			if (other.microphoneDriver != null)
				return false;
		} else if (!microphoneDriver.equals(other.microphoneDriver))
			return false;
		if (pixelAspectRatio != other.pixelAspectRatio)
			return false;
		if (publishingBW != other.publishingBW)
			return false;
		if (screenResolution == null) {
			if (other.screenResolution != null)
				return false;
		} else if (!screenResolution.equals(other.screenResolution))
			return false;
		if (videoDriver == null) {
			if (other.videoDriver != null)
				return false;
		} else if (!videoDriver.equals(other.videoDriver))
			return false;
		return true;
	}

}
