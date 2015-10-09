
/**
 * 
 */
package edu.amrita.aview.gclm.entities;

import java.sql.Timestamp;
import java.util.Date;
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
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;
import edu.amrita.aview.common.helpers.StatusHelper;


/**
 * The Class Class.
 *
 * @author amma
 */
@Entity
@Table(name = "class")
@BatchSize(size=1000)
public class Class extends Auditable {
	
	/** The class id. */
	private Long classId = 0l ;
	
	/** The class name. */
	private String className = null ;
	
	/** The class description. */
	private String classDescription = null;
	
	/** The start date. */
	private Date startDate = null ;
	
	/** The end date. */
	private Date endDate = null ;		
	
	/** The course id. */
	private Long courseId = 0l ;	
	
	/** The start time. */
	private Date startTime = null ;
	
	/** The end time. */
	private Date endTime = null ;
	
	/** The week days. */
	private String weekDays = null ;
	
	/** The schedule type. */
	private String scheduleType = null;
	
	/** The max students. */
	private int maxStudents = 0 ;
	
	/** The max publishing bandwidth kbps. */
	private int maxPublishingBandwidthKbps = 0 ;
	
	/** The min publishing bandwidth kbps. */
	private int minPublishingBandwidthKbps = 0 ;
	
	/** The max viewer interaction. */
	private int maxViewerInteraction = 1;
	
	/** The video codec. */
	private String videoCodec = null ;
	
	/** The video streaming protocol. */
	private String videoStreamingProtocol = null ;
	
	/** The is multi bitrate. */
	private String isMultiBitrate = null ;
	
	/** The presenter publishing bws kbps. */
	private String presenterPublishingBwsKbps = null ;
	
	/** The allow dynamic switching. */
	private String allowDynamicSwitching = null;
	
	/** The audit level. */
	private String auditLevel = null;
	
	/** The registration type. */
	private String registrationType = null;

	/** The class servers. */
	private Set<ClassServer> classServers = new HashSet<ClassServer>();

	/** The class type. */
	private String classType = null;
	
	/** The audio video interaction mode type. */
	private String audioVideoInteractionMode = Constant.DEFAULT_AUDIO_VIDEO_INTERACTION_MODE;
	
	/** The can monitor value. */
	private String  canMonitor = Constant.STATUS_NO;
	
	/** The monitor interval frequency. */
	private int monitorIntervalFreq = 0;
	
	/** The enable people count value. */
	private String  enablePeopleCount = Constant.STATUS_NO;


	//Non Mapped attributes
	/** The course name. */
	private String courseName = null;
	
	/** The institute name. */
	private String instituteName = null;
	
	/**
	 * Update from.
	 *
	 * @param other the other
	 */
	public void updateFrom(Class other)
	{
		this.className = other.className ;
		this.classDescription = other.classDescription;
		this.startDate = other.startDate ;
		this.endDate = other.endDate ;		
		this.courseId = other.courseId ;	
		this.startTime = other.startTime ;
		this.endTime = other.endTime ;
		this.weekDays = other.weekDays ;	
		this.scheduleType = other.scheduleType;
		this.maxStudents = other.maxStudents ;
		this.maxPublishingBandwidthKbps = other.maxPublishingBandwidthKbps ;
		this.minPublishingBandwidthKbps = other.minPublishingBandwidthKbps ;
		this.videoCodec = other.videoCodec ;
		this.videoStreamingProtocol = other.videoStreamingProtocol ;
		this.isMultiBitrate = other.isMultiBitrate ;
		this.presenterPublishingBwsKbps = other.presenterPublishingBwsKbps ;
		this.allowDynamicSwitching = other.allowDynamicSwitching;
		this.auditLevel = other.auditLevel;
		this.classType = other.classType;
		this.registrationType = other.registrationType;
		this.maxViewerInteraction = other.maxViewerInteraction;
	    this.canMonitor = other.canMonitor;
	    this.monitorIntervalFreq = other.monitorIntervalFreq;
	    this.audioVideoInteractionMode = other.audioVideoInteractionMode;
	    this.enablePeopleCount = other.enablePeopleCount;
		this.setStatusId(other.getStatusId());
		
		Set<ClassServer> temp = new HashSet<ClassServer>();
		if(this.classServers != null)
		{
			temp.addAll(this.classServers);
			this.classServers.clear();
		}
	
		Set<ClassServer> tempOther = new HashSet<ClassServer>();
		if(other.classServers != null)
		{
			tempOther.addAll(other.classServers);
		}
		
		super.mergeAssociations(temp, tempOther);
		this.classServers.addAll(temp);
	}
	
	/**
	 * Gets the registration type.
	 *
	 * @return the registration type
	 */
	@Column(name = "registration_type")
	public String getRegistrationType() {
		return registrationType;
	}

	/**
	 * Sets the registration type.
	 *
	 * @param registrationType the registration type
	 */
	public void setRegistrationType(String registrationType) {
		this.registrationType = registrationType;
	}

	/**
	 * Gets the max viewer interaction.
	 *
	 * @return the maxViewerInteraction
	 */
	@Column(name = "max_viewer_interaction")
	public int getMaxViewerInteraction() {
		return maxViewerInteraction;
	}

	/**
	 * Sets the max viewer interaction.
	 *
	 * @param maxViewerInteraction the maxViewerInteraction to set
	 */
	public void setMaxViewerInteraction(int maxViewerInteraction) {
		this.maxViewerInteraction = maxViewerInteraction;
	}

	/**
	 * Gets the class servers.
	 *
	 * @return the classServer
	 */
	
	@OneToMany(mappedBy="aviewClass",fetch=FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval=true)	
	@BatchSize(size=1000)
	public Set<ClassServer> getClassServers() {
		return classServers;
	}
	
	/**
	 * Sets the class servers.
	 *
	 * @param classServers the new class servers
	 */
	public void setClassServers(Set<ClassServer> classServers) {
		this.classServers = classServers;		
	}
	
	/**
	 * Adds the class server.
	 *
	 * @param classServer the class server
	 */
	public synchronized void addClassServer(ClassServer classServer)
	{
		if(this.classServers == null)
		{
			this.classServers = new HashSet<ClassServer>();
		}
		classServer.setAviewClass(this);
		this.classServers.add(classServer);
	}

	/**
	 * Gets the class id.
	 * CustomIdGenerator is setted for give id as a optional field for API's
	 * @return the _class_id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY ,generator="CustomIdGenerator")
	@GenericGenerator(name="CustomIdGenerator", strategy="edu.amrita.aview.common.utils.CustomIdGenerator")
	@Column(name = "class_id")
	public Long getClassId() {
		
		return classId;
	}

	/**
	 * Sets the class id.
	 *
	 * @param classId the new class id
	 */
	public void setClassId(Long classId) {		
		this.classId = classId;		
	}
	
	/**
	 * Gets the class name.
	 *
	 * @return the className
	 */
	@Column(name = "class_name")
	public String getClassName() {
		return className;
	}

	/**
	 * Sets the class name.
	 *
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	
	/**
	 * Gets the class description.
	 *
	 * @return the classDescription
	 */
	@Column(name = "class_description")
	public String getClassDescription() {
		return classDescription;
	}

	/**
	 * Sets the class description.
	 *
	 * @param classDescription the classDescription to set
	 */
	public void setClassDescription(String classDescription) {
		this.classDescription = classDescription;
	}

	/**
	 * Gets the start date.
	 *
	 * @return the startDate
	 */
	@Column(name = "start_date")
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Sets the start date.
	 *
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Gets the end date.
	 *
	 * @return the endDate
	 */
	@Column(name = "end_date")
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Sets the end date.
	 *
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Gets the course id.
	 *
	 * @return the courseId
	 */
	@Column(name = "course_id")
	public Long getCourseId() {
		return courseId;
	}

	/**
	 * Sets the course id.
	 *
	 * @param courseId the courseId to set
	 */
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	/**
	 * Gets the start time.
	 *
	 * @return the startTime
	 */
	@Column(name = "start_time")
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * Sets the start time.
	 *
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * Gets the end time.
	 *
	 * @return the endTime
	 */ 
	@Column(name = "end_time")
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * Sets the end time.
	 *
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * Gets the week days.
	 *
	 * @return the weekDays
	 */
	@Column(name = "weekdays")
	public String getWeekDays() {
		return weekDays;
	}

	/**
	 * Sets the week days.
	 *
	 * @param weekDays the weekDays to set
	 */
	public void setWeekDays(String weekDays) {
		this.weekDays = weekDays;
	}
	
	/**
	 * Gets the schedule type.
	 *
	 * @return the classMode
	 */
	@Column(name = "schedule_type")
	public String getScheduleType() {
		return scheduleType;
	}

	/**
	 * Sets the schedule type.
	 *
	 * @param scheduleType the new schedule type
	 */
	public void setScheduleType(String scheduleType) {
		this.scheduleType = scheduleType;
	}

	/**
	 * Gets the max students.
	 *
	 * @return the maxStudents
	 */
	@Column(name = "max_students")
	public int getMaxStudents() {
		return maxStudents;
	}

	/**
	 * Sets the max students.
	 *
	 * @param maxStudents the maxStudents to set
	 */
	public void setMaxStudents(int maxStudents) {
		this.maxStudents = maxStudents;
	}

	/**
	 * Gets the max publishing bandwidth kbps.
	 *
	 * @return the maxPublishingBandwidthKbps
	 */
	@Column(name = "max_publishing_bandwidth_kbps")
	public int getMaxPublishingBandwidthKbps() {
		return maxPublishingBandwidthKbps;
	}

	/**
	 * Sets the max publishing bandwidth kbps.
	 *
	 * @param maxPublishingBandwidthKbps the maxPublishingBandwidthKbps to set
	 */
	public void setMaxPublishingBandwidthKbps(int maxPublishingBandwidthKbps) {
		this.maxPublishingBandwidthKbps = maxPublishingBandwidthKbps;
	}

	/**
	 * Gets the min publishing bandwidth kbps.
	 *
	 * @return the minPublishingBandwidthKbps
	 */
	@Column(name = "min_publishing_bandwidth_kbps")
	public int getMinPublishingBandwidthKbps() {
		return minPublishingBandwidthKbps;
	}

	/**
	 * Sets the min publishing bandwidth kbps.
	 *
	 * @param minPublishingBandwidthKbps the minPublishingBandwidthKbps to set
	 */
	public void setMinPublishingBandwidthKbps(int minPublishingBandwidthKbps) {
		this.minPublishingBandwidthKbps = minPublishingBandwidthKbps;
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
	 * Gets the video streaming protocol.
	 *
	 * @return the videoStreamingProtocol
	 */
	@Column(name = "video_streaming_protocol")
	public String getVideoStreamingProtocol() {
		return videoStreamingProtocol;
	}

	/**
	 * Sets the video streaming protocol.
	 *
	 * @param videoStreamingProtocol the videoStreamingProtocol to set
	 */
	public void setVideoStreamingProtocol(String videoStreamingProtocol) {
		this.videoStreamingProtocol = videoStreamingProtocol;
	}

	/**
	 * Gets the checks if is multi bitrate.
	 *
	 * @return the isMultiBitrate
	 */
	@Column(name = "is_multi_bitrate")
	public String getIsMultiBitrate() {
		return isMultiBitrate;
	}

	/**
	 * Sets the checks if is multi bitrate.
	 *
	 * @param isMultiBitrate the isMultiBitrate to set
	 */
	public void setIsMultiBitrate(String isMultiBitrate) {
		this.isMultiBitrate = isMultiBitrate;
	}

	/**
	 * Gets the presenter publishing bws kbps.
	 *
	 * @return the presenterPublishingBwsKbps
	 */
	@Column(name = "presenter_publishing_bws_kbps")
	public String getPresenterPublishingBwsKbps() {
		return presenterPublishingBwsKbps;
	}

	/**
	 * Sets the presenter publishing bws kbps.
	 *
	 * @param presenterPublishingBwsKbps the presenterPublishingBwsKbps to set
	 */
	public void setPresenterPublishingBwsKbps(String presenterPublishingBwsKbps) {
		this.presenterPublishingBwsKbps = presenterPublishingBwsKbps;
	}

	/**
	 * Gets the allow dynamic switching.
	 *
	 * @return the allow dynamic switching
	 */
	@Column(name = "allow_dynamic_switching")
	public String getAllowDynamicSwitching() {
		return allowDynamicSwitching;
	}

	/**
	 * Sets the allow dynamic switching.
	 *
	 * @param allowDynamicSwitching the new allow dynamic switching
	 */
	public void setAllowDynamicSwitching(String allowDynamicSwitching) {
		this.allowDynamicSwitching = allowDynamicSwitching;
	}

	/**
	 * Gets the class type.
	 *
	 * @return the class type
	 */
	@Column(name = "class_type")
	public String getClassType() {
		return classType;
	}

	/**
	 * Sets the class type.
	 *
	 * @param classType the new class type
	 */
	public void setClassType(String classType) {
		this.classType = classType;
	}
	
	/**
	 * Gets the audio Video Interaction Mode.
	 *
	 * @return the audio Video Interaction Mode
	 */
	@Column(name = "audio_video_interaction_mode")
	public String getAudioVideoInteractionMode() {
		return audioVideoInteractionMode;
	}

	/**
	 * Sets the audio Video Interaction Mode.
	 *
	 * @param audioVideoInteractionMode the audioVideoInteractionMode
	 */
	public void setAudioVideoInteractionMode(String audioVideoInteractionMode) {
		this.audioVideoInteractionMode = audioVideoInteractionMode;
	}

	/**
	 * Gets the can monitor value.
	 *
	 * @return the can monitor value
	 */

	
	@Column(name = "can_monitor")
	public String getCanMonitor() {
		return canMonitor;
	}
	/**
	 * Sets the can monitor value.
	 *
	 * @param canMonitor the canMonitor value
	 */
	public void setCanMonitor(String canMonitor) {
		this.canMonitor = canMonitor;
	}
	/**
	 * Gets the monitor interval frequency value.
	 *
	 * @return the monitor interval frequency value
	 */


	@Column(name = "monitor_interval_freq")
	public int getMonitorIntervalFreq() {
		return monitorIntervalFreq;
	}
	/**
	 * Sets the monitor interval frequency value.
	 *
	 * @param monitorIntervalFreq the monitorIntervalFreq value
	 */


	public void setMonitorIntervalFreq(int monitorIntervalFreq) {
		this.monitorIntervalFreq = monitorIntervalFreq;
	}
	/**
	 * Gets the enable people count value.
	 *
	 * @return the enable people count value
	 */

	
	@Column(name = "enable_people_count")
	public String getEnablePeopleCount() {
		return enablePeopleCount;
	}
	/**
	 * Sets the enable people count value.
	 *
	 * @param enablePeopleCount the enablePeopleCount value
	 */
	public void setEnablePeopleCount(String enablePeopleCount) {
		this.enablePeopleCount = enablePeopleCount;
	}

	
    
	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.classId);
		sb.append(Constant.DELIMETER);
		sb.append(this.className);
		sb.append(Constant.DELIMETER);
		sb.append(this.classDescription);
		sb.append(Constant.DELIMETER);
		sb.append(this.startDate+":"+((this.startDate != null)?this.startDate.getTime():0));
		sb.append(Constant.DELIMETER);
		sb.append(this.endDate+":"+((this.endDate != null)?this.endDate.getTime():0));	
		sb.append(Constant.DELIMETER);
		sb.append(this.courseId);
		sb.append(Constant.DELIMETER);
		sb.append(this.startTime+":"+((this.startTime != null)?this.startTime.getTime():0));
		sb.append(Constant.DELIMETER);
		sb.append(this.endTime+":"+((this.endTime != null)?this.endTime.getTime():0));
		sb.append(Constant.DELIMETER);
		sb.append(this.weekDays);
		sb.append(Constant.DELIMETER);
		sb.append(this.scheduleType);
		sb.append(Constant.DELIMETER);
		sb.append(this.maxStudents);
		sb.append(Constant.DELIMETER);
		sb.append(this.maxPublishingBandwidthKbps);
		sb.append(Constant.DELIMETER);
		sb.append(this.videoCodec);
		sb.append(Constant.DELIMETER);
		sb.append(this.videoStreamingProtocol);
		sb.append(Constant.DELIMETER);
		sb.append(this.isMultiBitrate);
		sb.append(Constant.DELIMETER);
		sb.append(this.presenterPublishingBwsKbps);
		sb.append(Constant.DELIMETER);
		sb.append(this.allowDynamicSwitching);
		sb.append(Constant.DELIMETER);
		sb.append(this.auditLevel);
		sb.append(Constant.DELIMETER);
		sb.append(this.maxViewerInteraction);
		sb.append(Constant.DELIMETER);
		sb.append(this.registrationType);
		sb.append(Constant.DELIMETER);
		sb.append(this.classType);
		sb.append(Constant.DELIMETER);
		sb.append(this.canMonitor);
		sb.append(Constant.DELIMETER);
		sb.append(this.monitorIntervalFreq);
		sb.append(Constant.DELIMETER);
		sb.append(this.audioVideoInteractionMode);
		sb.append(Constant.DELIMETER);
		sb.append(this.enablePeopleCount);
		sb.append(Constant.DELIMETER);
		sb.append(super.toString());
		sb.append(Constant.DELIMETER);
		return sb.toString();
	}

	/**
	 * To string class servers.
	 *
	 * @return the string
	 */
	public String toStringClassServers()
	{
		StringBuilder sb = new StringBuilder(256);
		for(ClassServer classServer:this.classServers)
		{
			sb.append(classServer.toString());
			sb.append(Constant.DELIMETER);
			sb.append(classServer.getServer().toString());
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Gets the course name.
	 *
	 * @return the course name
	 */
	@Transient
	public String getCourseName() {
		return courseName;
	}

	/**
	 * Sets the course name.
	 *
	 * @param courseName the new course name
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	/**
	 * Gets the institute name.
	 *
	 * @return the institute name
	 */
	@Transient
	public String getInstituteName() {
		return instituteName;
	}

	/**
	 * Sets the institute name.
	 *
	 * @param instituteName the new institute name
	 */
	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}

	/**
	 * Gets the audit level.
	 *
	 * @return the audit level
	 */
	@Column(name = "audit_level")
	public String getAuditLevel() {
		return auditLevel;
	}

	/**
	 * Sets the audit level.
	 *
	 * @param auditLevel the new audit level
	 */
	public void setAuditLevel(String auditLevel) {
		this.auditLevel = auditLevel;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		result = prime * result
				+ ((courseId == null) ? 0 : courseId.hashCode());
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
		if (!(obj instanceof Class))
			return false;
		Class other = (Class) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (courseId == null) {
			if (other.courseId != null)
				return false;
		} else if (!courseId.equals(other.courseId))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#setCreatedAduitData(java.lang.Long, java.sql.Timestamp, java.lang.Integer)
	 */
	public void setCreatedAuditData(Long createdUserId,Timestamp createdDate,Integer statusId)
	{
		super.setCreatedAuditData(createdUserId, createdDate, statusId);
		if(this.classServers != null)
		{
			for(ClassServer server:classServers)
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
		if(this.classServers != null)
		{
			for(ClassServer server:classServers)
			{
				if(server.getClassServerId() != 0) //Update
				{
					server.setModifiedAuditData(modifiedUserId, modifedDate);
				}
				else //Create
				{
					server.setCreatedAuditData(modifiedUserId, modifedDate, StatusHelper.getActiveStatusId());
				}
			}
		}
	}
	

	

}
