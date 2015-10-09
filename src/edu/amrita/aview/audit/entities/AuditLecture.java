/*
 * 
 */
package edu.amrita.aview.audit.entities;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;


/**
 * The Class AuditLecture.
 */
@Entity
@Table(name = "audit_lecture")
public class AuditLecture  extends Auditable {
	
	/** The audit lecture id. */
	private Long auditLectureId=0l;                      
	
	/** The audit user loginid. */
	private Long auditUserLoginid=0l;
	
	/** The lecture id. */
	private Long lectureId=0l;	
	
	/** The is multi bitrate. */
	private String isMultiBitrate=null;            
	
	/** The max students. */
	private int maxStudents=0;         
	
	/** The min publishing bandwidth kbps. */
	private int minPublishingBandwidthKbps=0;         
	
	/** The max publishing bandwidth kbps. */
	private int maxPublishingBandwidthKbps=0;
	
	/** The is moderator. */
	private String isModerator = null ;
	
	/** The video codec. */
	private String videoCodec = null ;
	
	/** Last timestamp of the last audit action for that user in this session. */
	private Timestamp lastActionDate = null;
	
	/** The audit servers. */
	private Set<AuditLectureServer> auditServers = new HashSet<AuditLectureServer>();
	
	/**
	 * Update from.
	 *
	 * @param other the other
	 */
	public void updateFrom(AuditLecture other)
	{
		this.auditUserLoginid = other.auditUserLoginid;
		this.lectureId = other.lectureId;
		this.isMultiBitrate = other.isMultiBitrate ;
		this.maxStudents = other.maxStudents ;
		this.isModerator = other.isModerator ;		
		this.videoCodec = other.videoCodec ;	
		this.maxPublishingBandwidthKbps = other.maxPublishingBandwidthKbps ;
		this.minPublishingBandwidthKbps = other.minPublishingBandwidthKbps ;
		this.videoCodec = other.videoCodec ;
		
		Set<AuditLectureServer> temp = new HashSet<AuditLectureServer>();
		if(this.auditServers != null)
		{
			temp.addAll(this.auditServers);
			this.auditServers.clear();
		}
	
		Set<AuditLectureServer> tempOther = new HashSet<AuditLectureServer>();
		if(other.auditServers != null)
		{
			tempOther.addAll(other.auditServers);
		}
		
		super.mergeAssociations(temp, tempOther);
		this.auditServers.addAll(temp);
	}
	
	/**
	 * Gets the audit lecture servers.
	 *
	 * @return the AuditLectureServer
	 */
	
	@OneToMany(mappedBy="aviewAuditLecture",fetch=FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval=true)	
	@BatchSize(size=1000)
	public Set<AuditLectureServer> getAuditLectureServers() {
		return auditServers;
	}
	
	/**
	 * Sets the audit lecture servers.
	 *
	 * @param auditServers the new audit lecture servers
	 */
	public void setAuditLectureServers(Set<AuditLectureServer> auditServers) {
		this.auditServers = auditServers;		
	}
	
	/**
	 * Adds the audit lecture server.
	 *
	 * @param auditLectureServer the audit lecture server
	 */
	public synchronized void addAuditLectureServer(AuditLectureServer auditLectureServer)
	{
		if(this.auditServers == null)
		{
			this.auditServers = new HashSet<AuditLectureServer>();
		}
		auditLectureServer.setAviewAuditLecture(this);
		this.auditServers.add(auditLectureServer);
	}
	
	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#setCreatedAduitData(java.lang.Long, java.sql.Timestamp, java.lang.Integer)
	 */
	public void setCreatedAuditData(Long createdUserId,Timestamp createdDate,Integer statusId)
	{
		super.setCreatedAuditData(createdUserId, createdDate, statusId);
		if(this.auditServers != null)
		{
			for(AuditLectureServer server:auditServers)
			{
				server.setCreatedAuditData(createdUserId, createdDate, statusId);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#setModifiedAuditData(java.lang.Long, java.sql.Timestamp)
	 */
	public void setModifiedAuditData(Long modifiedUserId,Timestamp modifedDate) throws AViewException
	{
		super.setModifiedAuditData(modifiedUserId, modifedDate);
//		if(this.auditServers != null)
//		{
//			for(AuditLectureServer server:auditServers)
//			{
//				if(server.getAuditLectureServerId() != 0) //Update
//				{
//					server.setModifiedAuditData(modifiedUserId, modifedDate);
//				}
//				else //Create
//				{
//					server.setCreatedAduitData(modifiedUserId, modifedDate, StatusHelper.getActiveStatusId());
//				}
//			}
//		}
				}
	
	/**
	 * Gets the audit lecture id.
	 *
	 * @return the audit lecture id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "audit_lecture_id")
	public Long getAuditLectureId() {
		return auditLectureId;
	}
	
	/**
	 * Sets the audit lecture id.
	 *
	 * @param auditLectureId the new audit lecture id
	 */
	public void setAuditLectureId(Long auditLectureId) {
		this.auditLectureId = auditLectureId;
	}
	
	/**
	 * Gets the audit user loginid.
	 *
	 * @return the audit user loginid
	 */
	@Column(name = "audit_user_login_id")
	public Long getAuditUserLoginid() {
		return auditUserLoginid;
	}
	
	/**
	 * Sets the audit user loginid.
	 *
	 * @param auditUserLoginid the new audit user loginid
	 */
	public void setAuditUserLoginid(Long auditUserLoginid) {
		this.auditUserLoginid = auditUserLoginid;
	}
	
	/**
	 * Gets the lecture id.
	 *
	 * @return the lecture id
	 */
	@Column(name = "lecture_id")
	public Long getLectureId() {
		return lectureId;
	}
	
	/**
	 * Sets the lecture id.
	 *
	 * @param lectureid the new lecture id
	 */
	public void setLectureId(Long lectureid) {
		this.lectureId = lectureid;
	}
	
	/**
	 * Gets the checks if is multi bitrate.
	 *
	 * @return the checks if is multi bitrate
	 */
	@Column(name = "is_multi_bitrate")
	public String getIsMultiBitrate() {
		return isMultiBitrate;
	}
	
	/**
	 * Sets the checks if is multi bitrate.
	 *
	 * @param isMultiBitrate the new checks if is multi bitrate
	 */
	public void setIsMultiBitrate(String isMultiBitrate) {
		this.isMultiBitrate = isMultiBitrate;
	}
	
	/**
	 * Gets the max students.
	 *
	 * @return the max students
	 */
	@Column(name = "max_students")
	public int getMaxStudents() {
		return maxStudents;
	}
	
	/**
	 * Sets the max students.
	 *
	 * @param maxStudents the new max students
	 */
	public void setMaxStudents(int maxStudents) {
		this.maxStudents = maxStudents;
	}
	
	/**
	 * Gets the min publishing bandwidth kbps.
	 *
	 * @return the min publishing bandwidth kbps
	 */
	@Column(name = "min_publishing_bandwidth_kbps")
	public int getMinPublishingBandwidthKbps() {
		return minPublishingBandwidthKbps;
	}
	
	/**
	 * Sets the min publishing bandwidth kbps.
	 *
	 * @param minPublishingBandwidthKbps the new min publishing bandwidth kbps
	 */
	public void setMinPublishingBandwidthKbps(int minPublishingBandwidthKbps) {
		this.minPublishingBandwidthKbps = minPublishingBandwidthKbps;
	}
	
	/**
	 * Gets the max publishing bandwidth kbps.
	 *
	 * @return the max publishing bandwidth kbps
	 */
	@Column(name = "max_publishing_bandwidth_kbps")
	public int getMaxPublishingBandwidthKbps() {
		return maxPublishingBandwidthKbps;
	}
	
	/**
	 * Sets the max publishing bandwidth kbps.
	 *
	 * @param maxPublishingBandwidthKbps the new max publishing bandwidth kbps
	 */
	public void setMaxPublishingBandwidthKbps(int maxPublishingBandwidthKbps) {
		this.maxPublishingBandwidthKbps = maxPublishingBandwidthKbps;
	}         
	
	/**
	 * Gets the checks if is moderator.
	 *
	 * @return the isModerator
	 */
	@Column(name = "is_moderator")
	public String getIsModerator() {
		return isModerator;
	}

	/**
	 * Sets the checks if is moderator.
	 *
	 * @param isModerator the isModerator to set
	 */
	public void setIsModerator(String isModerator) {
		this.isModerator = isModerator;
	}

	/**
	 * Gets the video codec.
	 *
	 * @return the videoCodec
	 */
	@Column(name = "video_codec")
	public String getVideoCodec() {
		return videoCodec;
	}

	/**
	 * Sets the video codec.
	 *
	 * @param videoCodec the videoCodec to set
	 */
	public void setVideoCodec(String videoCodec) {
		this.videoCodec = videoCodec;
	}
	
	/**
	 * Gets the last action date.
	 *
	 * @return the last action date
	 */
	@Column(name = "last_action_date")
	public Timestamp getLastActionDate() {
		return lastActionDate;
	}

	/**
	 * Sets the last action date.
	 *
	 * @param lastActionDate the new last action date
	 */
	public void setLastActionDate(Timestamp lastActionDate) {
		this.lastActionDate = lastActionDate;
	}

	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.auditLectureId);
		sb.append(Constant.DELIMETER);
		sb.append(this.auditUserLoginid);
		sb.append(Constant.DELIMETER);
		sb.append(this.lectureId);
		sb.append(Constant.DELIMETER);		
		sb.append(this.isMultiBitrate);
		sb.append(Constant.DELIMETER);
		sb.append(this.maxStudents);
		sb.append(Constant.DELIMETER);
		sb.append(this.minPublishingBandwidthKbps);
		sb.append(Constant.DELIMETER);
		sb.append(this.maxPublishingBandwidthKbps);
		sb.append(Constant.DELIMETER);
		sb.append(this.isModerator);
		sb.append(Constant.DELIMETER);
		sb.append(this.videoCodec);
		sb.append(Constant.DELIMETER);
		sb.append(this.lastActionDate);
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
				+ ((auditUserLoginid == null) ? 0 : auditUserLoginid.hashCode());
		result = prime * result
				+ ((lectureId == null) ? 0 : lectureId.hashCode());
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
		AuditLecture other = (AuditLecture) obj;
		if (auditUserLoginid == null) {
			if (other.auditUserLoginid != null)
				return false;
		} else if (!auditUserLoginid.equals(other.auditUserLoginid))
			return false;
		if (lectureId == null) {
			if (other.lectureId != null)
				return false;
		} else if (!lectureId.equals(other.lectureId))
			return false;
		return true;
	}

}
