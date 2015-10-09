/*
 * 
 */
package edu.amrita.aview.feedback.entities;

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

import edu.amrita.aview.common.entities.Auditable;


/**
 * The Class Feedback.
 */
@Entity
@Table(name = "feedback")
public class Feedback extends Auditable {
	
	/** The feedback id. */
	private Long feedbackId = 0l;
	
	/** The user id. */
	private Long userId = 0l;
	
	/** The audit user login id. */
	private Long auditUserLoginId = 0l;
	
	/** The audit lecture id. */
	private Long auditLectureId = 0l;
	
	/** The bandwidth rating. */
	private Double bandwidthRating = 0.0d;
	
	/** The overall rating. */
	private Integer overallRating = 0;
	
	/** The audio rating. */
	private Integer audioRating = 0;
	
	/** The video rating. */
	private Integer videoRating = 0;
	
	/** The interaction rating. */
	private Integer interactionRating = 0;
	
	/** The whiteboard rating. */
	private Integer whiteboardRating = 0;
	
	/** The document sharing rating. */
	private Integer documentSharingRating = 0;
	
	/** The desktop sharing rating. */
	private Integer desktopSharingRating = 0;
	
	/** The usability rating. */
	private Integer usabilityRating = 0;
	
	/** The overall feedback. */
	private String overallFeedback = null;
	
	/** The audio feedback. */
	private String audioFeedback = null;
	
	/** The video feedback. */
	private String videoFeedback = null;
	
	/** The interaction feedback. */
	private String interactionFeedback = null;
	
	/** The whiteboard feedback. */
	private String whiteboardFeedback = null;
	
	/** The document sharing feedback. */
	private String documentSharingFeedback = null;
	
	/** The desktop sharing feedback. */
	private String desktopSharingFeedback = null;
	
	/** The usability feedback. */
	private String usabilityFeedback = null;
	
	/** The other feedback. */
	private String otherFeedback = null;
	
	/** The given mm. */
	private Double givenMM = 0.0d;
	
	/** The left mm. */
	private Double leftMM = 0.0d;
	
	/** The memory used mb. */
	private Double memoryUsedMB = 0.0d;
	
	/** The used cpu percentage. */
	private Integer usedCPUPercentage = 0;
	
	/** The cpu architecture. */
	private String cpuArchitecture = null;
	
	/** The max idc level. */
	private String maxIDCLevel = null;
	
	/** The operating system. */
	private String operatingSystem = null;
	
	/** The screen resolution x. */
	private Integer screenResolutionX = 0;
	
	/** The screen resolution y. */
	private Integer screenResolutionY = 0;
	
	/** The stage width. */
	private Integer stageWidth = 0;
	
	/** The stage height. */
	private Integer stageHeight = 0;
	
	/** The has audio video. */
	private String hasAudioVideo = null;
	
	/** The has file read permission. */
	private String hasFileReadPermission = null;
	
	/** The is64bit support. */
	private String is64bitSupport = null;
	
	/** The runtime version. */
	private String runtimeVersion = null;
	
	/** The upload bw kb. */
	private Integer uploadBWKb = 0;
	
	/** The download bw kb. */
	private Integer downloadBWKb = 0;
	
	/** The has proxy server. */
	private String hasProxyServer = null;
	
	/** The is behind firewall. */
	private String isBehindFirewall = null;
	
	/** The has avs. */
	private String hasAVS = null;
	
	/** The anti virus name. */
	private String antiVirusName = null;
	
	/** The feedback issues. */
	private Set<FeedbackIssue> feedbackIssues = new HashSet<FeedbackIssue>();
//	
//	public void copyFrom(Feedback feedback,List<FeedbackIssue> issues)
//	{
//		this.feedbackId = feedback.feedbackId;
//		this.userId = feedback.userId;
//		this.auditUserLoginId = feedback.auditUserLoginId;
//		this.auditLectureId = feedback.auditLectureId;
//		this.bandwidthBarsAvg = feedback.bandwidthBarsAvg;
//
//		this.overallRating = feedback.overallRating;
//		this.audioRating = feedback.audioRating;
//		this.videoRating = feedback.videoRating;
//		this.interactionRating = feedback.interactionRating;
//		this.whiteboardRating = feedback.whiteboardRating;
//		this.documentSharingRating = feedback.documentSharingRating;
//		this.desktopSharingRating = feedback.desktopSharingRating;
//		this.usabilityRating = feedback.usabilityRating;
//		this.overallFeedback = feedback.overallFeedback;
//		this.audioFeedback = feedback.audioFeedback;
//		this.videoFeedback = feedback.videoFeedback;
//		this.interactionFeedback = feedback.interactionFeedback;
//		this.whiteboardFeedback = feedback.whiteboardFeedback;
//		this.documentSharingFeedback = feedback.documentSharingFeedback;
//		this.desktopSharingFeedback = feedback.desktopSharingFeedback;
//		this.usabilityFeedback = feedback.usabilityFeedback;
//		this.otherFeedback = feedback.otherFeedback;
//
//		this.totalMemoryMB = feedback.totalMemoryMB;
//		this.freeMemoryMB = feedback.freeMemoryMB;
//		this.usedMemoryMB = feedback.usedMemoryMB;
//		this.cpuUsedPct = feedback.cpuUsedPct;
//		this.cpuArchitecture = feedback.cpuArchitecture;
//
//		this.operatingSystem = feedback.operatingSystem;
//		this.screenResolutionX = feedback.screenResolutionX;
//		this.screenResolutionY = feedback.screenResolutionY;
//		this.isAVAccess = feedback.isAVAccess;
//		this.islocalFileReadAccess = feedback.islocalFileReadAccess;
//		this.is64bitSupport = feedback.is64bitSupport;
//		this.runtimeVersion = feedback.runtimeVersion;
//
//		this.uploadBWKb = feedback.uploadBWKb;
//		this.downloadBWKb = feedback.downloadBWKb;
//		this.isBehindProxy = feedback.isBehindProxy;
//		this.isBehindFirewall = feedback.isBehindFirewall;
//		this.isAntiVirusProtected = feedback.isAntiVirusProtected;
//		this.antiVirusName = feedback.antiVirusName;
//
//		for(FeedbackIssue issue:issues)
//		{
//			FeedbackIssue newIssue = new FeedbackIssue();
//			newIssue.copyFrom(issue);
//			newIssue.setFeedback(this);
//			this.feedbackIssues.add(newIssue);
//		}
//		
//	}
	
	/**
 * Gets the feedback id.
 *
 * @return the feedback id
 */
@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "feedback_id")
	public Long getFeedbackId() {
		return feedbackId;
	}
	
	/**
	 * Sets the feedback id.
	 *
	 * @param feedbackId the feedback id
	 */
	public void setFeedbackId(Long feedbackId) {
		this.feedbackId = feedbackId;
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
	 * @param userId the user id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Gets the audit user login id.
	 *
	 * @return the audit user login id
	 */
	@Column(name = "audit_user_login_id")
	public Long getAuditUserLoginId() {
		return auditUserLoginId;
	}
	
	/**
	 * Sets the audit user login id.
	 *
	 * @param auditUserLoginId the audit user login id
	 */
	public void setAuditUserLoginId(Long auditUserLoginId) {
		this.auditUserLoginId = auditUserLoginId;
	}

	/**
	 * Gets the audit lecture id.
	 *
	 * @return the audit lecture id
	 */
	@Column(name = "audit_lecture_id")
	public Long getAuditLectureId() {
		return auditLectureId;
	}
	
	/**
	 * Sets the audit lecture id.
	 *
	 * @param auditLectureId the audit lecture id
	 */
	public void setAuditLectureId(Long auditLectureId) {
		this.auditLectureId = auditLectureId;
	}

	/**
	 * Gets the bandwidth rating.
	 *
	 * @return the bandwidth rating
	 */
	@Column(name = "average_bandwidth_bars")
	public Double getBandwidthRating() {
		return bandwidthRating;
	}
	
	/**
	 * Sets the bandwidth rating.
	 *
	 * @param bandwidthBarsAvg the bandwidth rating
	 */
	public void setBandwidthRating(Double bandwidthBarsAvg) {
		this.bandwidthRating = bandwidthBarsAvg;
	}

	/**
	 * Gets the overall rating.
	 *
	 * @return the overall rating
	 */
	@Column(name = "overall_rating")
	public Integer getOverallRating() {
		return overallRating;
	}
	
	/**
	 * Sets the overall rating.
	 *
	 * @param overallRating the overall rating
	 */
	public void setOverallRating(Integer overallRating) {
		this.overallRating = overallRating;
	}

	/**
	 * Gets the audio rating.
	 *
	 * @return the audio rating
	 */
	@Column(name = "audio_rating")
	public Integer getAudioRating() {
		return audioRating;
	}
	
	/**
	 * Sets the audio rating.
	 *
	 * @param audioRating the audio rating
	 */
	public void setAudioRating(Integer audioRating) {
		this.audioRating = audioRating;
	}

	/**
	 * Gets the video rating.
	 *
	 * @return the video rating
	 */
	@Column(name = "video_rating")
	public Integer getVideoRating() {
		return videoRating;
	}
	
	/**
	 * Sets the video rating.
	 *
	 * @param videoRating the video rating
	 */
	public void setVideoRating(Integer videoRating) {
		this.videoRating = videoRating;
	}

	/**
	 * Gets the interaction rating.
	 *
	 * @return the interaction rating
	 */
	@Column(name = "interaction_rating")
	public Integer getInteractionRating() {
		return interactionRating;
	}
	
	/**
	 * Sets the interaction rating.
	 *
	 * @param interactionRating the interaction rating
	 */
	public void setInteractionRating(Integer interactionRating) {
		this.interactionRating = interactionRating;
	}

	/**
	 * Gets the whiteboard rating.
	 *
	 * @return the whiteboard rating
	 */
	@Column(name = "whiteboard_rating")
	public Integer getWhiteboardRating() {
		return whiteboardRating;
	}
	
	/**
	 * Sets the whiteboard rating.
	 *
	 * @param whiteboardRating the whiteboard rating
	 */
	public void setWhiteboardRating(Integer whiteboardRating) {
		this.whiteboardRating = whiteboardRating;
	}

	/**
	 * Gets the document sharing rating.
	 *
	 * @return the document sharing rating
	 */
	@Column(name = "document_sharing_rating")
	public Integer getDocumentSharingRating() {
		return documentSharingRating;
	}
	
	/**
	 * Sets the document sharing rating.
	 *
	 * @param documentSharingRating the document sharing rating
	 */
	public void setDocumentSharingRating(Integer documentSharingRating) {
		this.documentSharingRating = documentSharingRating;
	}

	/**
	 * Gets the desktop sharing rating.
	 *
	 * @return the desktop sharing rating
	 */
	@Column(name = "desktop_sharing_rating")
	public Integer getDesktopSharingRating() {
		return desktopSharingRating;
	}
	
	/**
	 * Sets the desktop sharing rating.
	 *
	 * @param desktopSharingRating the desktop sharing rating
	 */
	public void setDesktopSharingRating(Integer desktopSharingRating) {
		this.desktopSharingRating = desktopSharingRating;
	}

	/**
	 * Gets the usability rating.
	 *
	 * @return the usability rating
	 */
	@Column(name = "usability_rating")
	public Integer getUsabilityRating() {
		return usabilityRating;
	}
	
	/**
	 * Sets the usability rating.
	 *
	 * @param usabilityRating the usability rating
	 */
	public void setUsabilityRating(Integer usabilityRating) {
		this.usabilityRating = usabilityRating;
	}

	/**
	 * Gets the overall feedback.
	 *
	 * @return the overall feedback
	 */
	@Column(name = "overall_feedback")
	public String getOverallFeedback() {
		return overallFeedback;
	}
	
	/**
	 * Sets the overall feedback.
	 *
	 * @param overallFeedback the overall feedback
	 */
	public void setOverallFeedback(String overallFeedback) {
		this.overallFeedback = overallFeedback;
	}

	/**
	 * Gets the audio feedback.
	 *
	 * @return the audio feedback
	 */
	@Column(name = "audio_feedback")
	public String getAudioFeedback() {
		return audioFeedback;
	}
	
	/**
	 * Sets the audio feedback.
	 *
	 * @param audioFeedback the audio feedback
	 */
	public void setAudioFeedback(String audioFeedback) {
		this.audioFeedback = audioFeedback;
	}

	/**
	 * Gets the video feedback.
	 *
	 * @return the video feedback
	 */
	@Column(name = "video_feedback")
	public String getVideoFeedback() {
		return videoFeedback;
	}
	
	/**
	 * Sets the video feedback.
	 *
	 * @param videoFeedback the video feedback
	 */
	public void setVideoFeedback(String videoFeedback) {
		this.videoFeedback = videoFeedback;
	}

	/**
	 * Gets the interaction feedback.
	 *
	 * @return the interaction feedback
	 */
	@Column(name = "interaction_feedback")
	public String getInteractionFeedback() {
		return interactionFeedback;
	}
	
	/**
	 * Sets the interaction feedback.
	 *
	 * @param interactionFeedback the interaction feedback
	 */
	public void setInteractionFeedback(String interactionFeedback) {
		this.interactionFeedback = interactionFeedback;
	}

	/**
	 * Gets the whiteboard feedback.
	 *
	 * @return the whiteboard feedback
	 */
	@Column(name = "whiteboard_feedback")
	public String getWhiteboardFeedback() {
		return whiteboardFeedback;
	}
	
	/**
	 * Sets the whiteboard feedback.
	 *
	 * @param whiteboardFeedback the whiteboard feedback
	 */
	public void setWhiteboardFeedback(String whiteboardFeedback) {
		this.whiteboardFeedback = whiteboardFeedback;
	}

	/**
	 * Gets the document sharing feedback.
	 *
	 * @return the document sharing feedback
	 */
	@Column(name = "document_sharing_feedback")
	public String getDocumentSharingFeedback() {
		return documentSharingFeedback;
	}
	
	/**
	 * Sets the document sharing feedback.
	 *
	 * @param documentSharingFeedback the document sharing feedback
	 */
	public void setDocumentSharingFeedback(String documentSharingFeedback) {
		this.documentSharingFeedback = documentSharingFeedback;
	}

	/**
	 * Gets the desktop sharing feedback.
	 *
	 * @return the desktop sharing feedback
	 */
	@Column(name = "desktop_sharing_feedback")
	public String getDesktopSharingFeedback() {
		return desktopSharingFeedback;
	}
	
	/**
	 * Sets the desktop sharing feedback.
	 *
	 * @param desktopSharingFeedback the desktop sharing feedback
	 */
	public void setDesktopSharingFeedback(String desktopSharingFeedback) {
		this.desktopSharingFeedback = desktopSharingFeedback;
	}

	/**
	 * Gets the usability feedback.
	 *
	 * @return the usability feedback
	 */
	@Column(name = "usability_feedback")
	public String getUsabilityFeedback() {
		return usabilityFeedback;
	}
	
	/**
	 * Sets the usability feedback.
	 *
	 * @param usabilityFeedback the usability feedback
	 */
	public void setUsabilityFeedback(String usabilityFeedback) {
		this.usabilityFeedback = usabilityFeedback;
	}

	/**
	 * Gets the other feedback.
	 *
	 * @return the other feedback
	 */
	@Column(name = "other_feedback")
	public String getOtherFeedback() {
		return otherFeedback;
	}
	
	/**
	 * Sets the other feedback.
	 *
	 * @param otherFeedback the other feedback
	 */
	public void setOtherFeedback(String otherFeedback) {
		this.otherFeedback = otherFeedback;
	}

	/**
	 * Gets the given mm.
	 *
	 * @return the given mm
	 */
	@Column(name = "total_memory_mb")
	public Double getGivenMM() {
		return givenMM;
	}
	
	/**
	 * Sets the given mm.
	 *
	 * @param totalMemoryMB the given mm
	 */
	public void setGivenMM(Double totalMemoryMB) {
		this.givenMM = totalMemoryMB;
	}

	/**
	 * Gets the left mm.
	 *
	 * @return the left mm
	 */
	@Column(name = "free_memory_mb")
	public Double getLeftMM() {
		return leftMM;
	}
	
	/**
	 * Sets the left mm.
	 *
	 * @param freeMemoryMB the left mm
	 */
	public void setLeftMM(Double freeMemoryMB) {
		this.leftMM = freeMemoryMB;
	}

	/**
	 * Gets the memory used mb.
	 *
	 * @return the memory used mb
	 */
	@Column(name = "memory_used_mb")
	public Double getMemoryUsedMB() {
		return memoryUsedMB;
	}
	
	/**
	 * Sets the memory used mb.
	 *
	 * @param usedMemoryMB the memory used mb
	 */
	public void setMemoryUsedMB(Double usedMemoryMB) {
		this.memoryUsedMB = usedMemoryMB;
	}

	/**
	 * Gets the used cpu percentage.
	 *
	 * @return the used cpu percentage
	 */
	@Column(name = "cpu_used_pct")
	public Integer getUsedCPUPercentage() {
		return usedCPUPercentage;
	}
	
	/**
	 * Sets the used cpu percentage.
	 *
	 * @param cpuUsedPct the used cpu percentage
	 */
	public void setUsedCPUPercentage(Integer cpuUsedPct) {
		this.usedCPUPercentage = cpuUsedPct;
	}

	/**
	 * Gets the cpu architecture.
	 *
	 * @return the cpu architecture
	 */
	@Column(name = "cpu_architecture")
	public String getCpuArchitecture() {
		return cpuArchitecture;
	}
	
	/**
	 * Sets the cpu architecture.
	 *
	 * @param cpuArchitecture the cpu architecture
	 */
	public void setCpuArchitecture(String cpuArchitecture) {
		this.cpuArchitecture = cpuArchitecture;
	}

	/**
	 * Gets the max idc level.
	 *
	 * @return the max idc level
	 */
	@Column(name = "is_max_idc_level")
	public String getMaxIDCLevel() {
		return maxIDCLevel;
	}
	
	/**
	 * Sets the max idc level.
	 *
	 * @param maxIDCLevel the max idc level
	 */
	public void setMaxIDCLevel(String maxIDCLevel) {
		this.maxIDCLevel = maxIDCLevel;
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
	 * @param operatingSystem the operating system
	 */
	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	/**
	 * Gets the screen resolution x.
	 *
	 * @return the screen resolution x
	 */
	@Column(name = "screen_resolution_x")
	public Integer getScreenResolutionX() {
		return screenResolutionX;
	}
	
	/**
	 * Sets the screen resolution x.
	 *
	 * @param screenResolutionX the screen resolution x
	 */
	public void setScreenResolutionX(Integer screenResolutionX) {
		this.screenResolutionX = screenResolutionX;
	}

	/**
	 * Gets the screen resolution y.
	 *
	 * @return the screen resolution y
	 */
	@Column(name = "screen_resolution_y")
	public Integer getScreenResolutionY() {
		return screenResolutionY;
	}
	
	/**
	 * Sets the screen resolution y.
	 *
	 * @param screenResolutionY the screen resolution y
	 */
	public void setScreenResolutionY(Integer screenResolutionY) {
		this.screenResolutionY = screenResolutionY;
	}

	/**
	 * Gets the has audio video.
	 *
	 * @return the checks for audio video
	 */
	@Column(name = "is_audio_video_access")
	public String getHasAudioVideo() {
		return hasAudioVideo;
	}
	
	/**
	 * Sets the has audio video.
	 *
	 * @param isAVAccess the checks for audio video
	 */
	public void setHasAudioVideo(String isAVAccess) {
		this.hasAudioVideo = isAVAccess;
	}

	/**
	 * Gets the has file read permission.
	 *
	 * @return the checks for file read permission
	 */
	@Column(name = "is_local_file_read_access")
	public String getHasFileReadPermission() {
		return hasFileReadPermission;
	}
	
	/**
	 * Sets the has file read permission.
	 *
	 * @param islocalFileReadAccess the checks for file read permission
	 */
	public void setHasFileReadPermission(String islocalFileReadAccess) {
		this.hasFileReadPermission = islocalFileReadAccess;
	}

	/**
	 * Gets the is64bit support.
	 *
	 * @return the is64bit support
	 */
	@Column(name = "is_64bit_support")
	public String getIs64bitSupport() {
		return is64bitSupport;
	}
	
	/**
	 * Sets the is64bit support.
	 *
	 * @param is64bitSupport the is64bit support
	 */
	public void setIs64bitSupport(String is64bitSupport) {
		this.is64bitSupport = is64bitSupport;
	}

	/**
	 * Gets the runtime version.
	 *
	 * @return the runtime version
	 */
	@Column(name = "runtime_version")
	public String getRuntimeVersion() {
		return runtimeVersion;
	}
	
	/**
	 * Sets the runtime version.
	 *
	 * @param runtimeVersion the runtime version
	 */
	public void setRuntimeVersion(String runtimeVersion) {
		this.runtimeVersion = runtimeVersion;
	}

	/**
	 * Gets the upload bw kb.
	 *
	 * @return the upload bw kb
	 */
	@Column(name = "upload_bandwidth_kb")
	public Integer getUploadBWKb() {
		return uploadBWKb;
	}
	
	/**
	 * Sets the upload bw kb.
	 *
	 * @param uploadBWKb the upload bw kb
	 */
	public void setUploadBWKb(Integer uploadBWKb) {
		this.uploadBWKb = uploadBWKb;
	}

	/**
	 * Gets the download bw kb.
	 *
	 * @return the download bw kb
	 */
	@Column(name = "download_bandwidth_kb")
	public Integer getDownloadBWKb() {
		return downloadBWKb;
	}
	
	/**
	 * Sets the download bw kb.
	 *
	 * @param downloadBWKb the download bw kb
	 */
	public void setDownloadBWKb(Integer downloadBWKb) {
		this.downloadBWKb = downloadBWKb;
	}

	/**
	 * Gets the has proxy server.
	 *
	 * @return the checks for proxy server
	 */
	@Column(name = "is_behind_proxy")
	public String getHasProxyServer() {
		return hasProxyServer;
	}
	
	/**
	 * Sets the has proxy server.
	 *
	 * @param isBehindProxy the checks for proxy server
	 */
	public void setHasProxyServer(String isBehindProxy) {
		this.hasProxyServer = isBehindProxy;
	}

	/**
	 * Gets the is behind firewall.
	 *
	 * @return the checks if is behind firewall
	 */
	@Column(name = "is_behind_firewall")
	public String getIsBehindFirewall() {
		return isBehindFirewall;
	}
	
	/**
	 * Sets the is behind firewall.
	 *
	 * @param isBehindFirewall the checks if is behind firewall
	 */
	public void setIsBehindFirewall(String isBehindFirewall) {
		this.isBehindFirewall = isBehindFirewall;
	}

	/**
	 * Gets the has avs.
	 *
	 * @return the checks for avs
	 */
	@Column(name = "is_antivirus_protected")
	public String getHasAVS() {
		return hasAVS;
	}
	
	/**
	 * Sets the has avs.
	 *
	 * @param isAntiVirusProtected the checks for avs
	 */
	public void setHasAVS(String isAntiVirusProtected) {
		this.hasAVS = isAntiVirusProtected;
	}

	/**
	 * Gets the anti virus name.
	 *
	 * @return the anti virus name
	 */
	@Column(name = "anti_virus_name")
	public String getAntiVirusName() {
		return antiVirusName;
	}
	
	/**
	 * Sets the anti virus name.
	 *
	 * @param antiVirusName the anti virus name
	 */
	public void setAntiVirusName(String antiVirusName) {
		this.antiVirusName = antiVirusName;
	}
	
	/**
	 * Gets the feedback issues.
	 *
	 * @return the feedback issues
	 */
	@OneToMany(mappedBy="feedback",fetch=FetchType.EAGER, cascade = {CascadeType.ALL}, orphanRemoval=true)	
	@BatchSize(size=1000)
	public Set<FeedbackIssue> getFeedbackIssues() {
		return feedbackIssues;
	}
	
	/**
	 * Sets the feedback issues.
	 *
	 * @param feedbackIssues the feedback issues
	 */
	public void setFeedbackIssues(Set<FeedbackIssue> feedbackIssues) {
		this.feedbackIssues = feedbackIssues;
	}

	/**
	 * Gets the stage width.
	 *
	 * @return the stage width
	 */
	@Column(name = "stage_width")
	public Integer getStageWidth() {
		return stageWidth;
	}
	
	/**
	 * Sets the stage width.
	 *
	 * @param stageWidth the stage width
	 */
	public void setStageWidth(Integer stageWidth) {
		this.stageWidth = stageWidth;
	}

	/**
	 * Gets the stage height.
	 *
	 * @return the stage height
	 */
	@Column(name = "stage_height")
	public Integer getStageHeight() {
		return stageHeight;
	}
	
	/**
	 * Sets the stage height.
	 *
	 * @param stageHeight the stage height
	 */
	public void setStageHeight(Integer stageHeight) {
		this.stageHeight = stageHeight;
	}
	
	/* (non-Javadoc)
	 * @see edu.amrita.aview.common.entities.Auditable#toString()
	 */
	@Override
	public String toString() {
		return "Feedback [feedbackId=" + feedbackId + ", userId=" + userId
				+ ", auditUserLoginId=" + auditUserLoginId
				+ ", auditLectureId=" + auditLectureId + ", bandwidthBarsAvg="
				+ bandwidthRating + ", overallRating=" + overallRating
				+ ", audioRating=" + audioRating + ", videoRating="
				+ videoRating + ", interactionRating=" + interactionRating
				+ ", whiteboardRating=" + whiteboardRating
				+ ", documentSharingRating=" + documentSharingRating
				+ ", desktopSharingRating=" + desktopSharingRating
				+ ", usabilityRating=" + usabilityRating + ", overallFeedback="
				+ overallFeedback + ", audioFeedback=" + audioFeedback
				+ ", videoFeedback=" + videoFeedback + ", interactionFeedback="
				+ interactionFeedback + ", whiteboardFeedback="
				+ whiteboardFeedback + ", documentSharingFeedback="
				+ documentSharingFeedback + ", desktopSharingFeedback="
				+ desktopSharingFeedback + ", usabilityFeedback="
				+ usabilityFeedback + ", otherFeedback=" + otherFeedback
				+ ", totalMemoryMB=" + givenMM + ", freeMemoryMB="
				+ leftMM + ", usedMemoryMB=" + memoryUsedMB
				+ ", cpuUsedPct=" + usedCPUPercentage + ", cpuArchitecture="
				+ cpuArchitecture + ", operatingSystem=" + operatingSystem
				+ ", stageWidth=" + stageWidth
				+ ", stageHeight=" + stageHeight
				+ ", maxIDCLevel=" + maxIDCLevel
				+ ", screenResolutionX=" + screenResolutionX
				+ ", screenResolutionY=" + screenResolutionY + ", isAVAccess="
				+ hasAudioVideo + ", islocalFileReadAccess="
				+ hasFileReadPermission + ", is64bitSupport=" + is64bitSupport
				+ ", runtimeVersion=" + runtimeVersion + ", uploadBWKb="
				+ uploadBWKb + ", downloadBWKb=" + downloadBWKb
				+ ", isBehindProxy=" + hasProxyServer + ", isBehindFirewall="
				+ isBehindFirewall + ", isAntiVirusProtected="
				+ hasAVS + ", antiVirusName=" + antiVirusName
				+ "]\n"+toStringIssues();
	}
	
	/**
	 * To string issues.
	 *
	 * @return the string
	 */
	public String toStringIssues()
	{
		StringBuilder sb = new StringBuilder();
		if(feedbackIssues != null)
		{
			for(FeedbackIssue issue:feedbackIssues)
			{
				sb.append(issue);
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	
}
