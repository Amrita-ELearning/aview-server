/**
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.gclm.helpers.NodeTypeHelper;


/**
 * The Class ClassRegistration.
 *
 * @author amma
 */
@Entity
@Table(name = "class_register")
public class ClassRegistration extends Auditable implements Comparable{
	
	/** The class register id. */
	private Long classRegisterId = 0l ;
	
	/** The user. */
	private User user = null;
	
	/** The aview class. */
	private Class aviewClass = null;
	
	/** The node type id. */
	private Integer nodeTypeId = null;
	
	/** The is moderator. */
	private String isModerator = null ;
	
	/** The status name. */
	private String statusName = null;
	
	/** The enable audio video. */
	private String enableAudioVideo= null;
	
	/** The enable document sharing. */
	private String enableDocumentSharing= null;
	
	/** The enable desktop sharing. */
	private String enableDesktopSharing= null;
	
	/** The enable video sharing. */
	private String enableVideoSharing= null;
	
	/** The enable2 d sharing. */
	private String enable2DSharing= null;
	
	/** The enable3 d sharing. */
	private String enable3DSharing= null;

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name="user_id")
	public User getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Gets the aview class.
	 *
	 * @return the aview class
	 */
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name="class_id")
	public Class getAviewClass() {
		return aviewClass;
	}

	/**
	 * Sets the aview class.
	 *
	 * @param aviewClass the new aview class
	 */
	public void setAviewClass(Class aviewClass) {
		this.aviewClass = aviewClass;
	}

	/**
	 * Gets the class register id.
	 *
	 * @return the classRegisterId
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "class_register_id")
	public Long getClassRegisterId() {
		return classRegisterId;
	}

	/**
	 * Sets the class register id.
	 *
	 * @param classRegisterId the classRegisterId to set
	 */
	public void setClassRegisterId(Long classRegisterId) {
		this.classRegisterId = classRegisterId;
	}		

	/**
	 * Gets the node type id.
	 *
	 * @return the nodeId
	 */
	@Column(name = "node_type_id")	
	public Integer getNodeTypeId() {
		return nodeTypeId;
	}

	/**
	 * Sets the node type id.
	 *
	 * @param nodeTypeId the new node type id
	 */
	public void setNodeTypeId(Integer nodeTypeId) {
		this.nodeTypeId = nodeTypeId;
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
	 * Gets the status name.
	 *
	 * @return the status name
	 */
	@Transient
	public String getStatusName() {
		return statusName;
	}

	/**
	 * Sets the status name.
	 *
	 * @param statusName the new status name
	 */
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	/**
	 * Gets the enable audio video.
	 *
	 * @return the enable audio video
	 */
	@Column(name = "enable_audio_video")
	public String getEnableAudioVideo() {
		return enableAudioVideo;
	}

	/**
	 * Sets the enable audio video.
	 *
	 * @param enableAudioVideo the new enable audio video
	 */
	public void setEnableAudioVideo(String enableAudioVideo) {
		this.enableAudioVideo = enableAudioVideo;
	}

	/**
	 * Gets the enable document sharing.
	 *
	 * @return the enable document sharing
	 */
	@Column(name = "enable_document_sharing")
	public String getEnableDocumentSharing() {
		return enableDocumentSharing;
	}

	/**
	 * Sets the enable document sharing.
	 *
	 * @param enableDocumentSharing the new enable document sharing
	 */
	public void setEnableDocumentSharing(String enableDocumentSharing) {
		this.enableDocumentSharing = enableDocumentSharing;
	}

	/**
	 * Gets the enable desktop sharing.
	 *
	 * @return the enable desktop sharing
	 */
	@Column(name = "enable_desktop_sharing")
	public String getEnableDesktopSharing() {
		return enableDesktopSharing;
	}

	/**
	 * Sets the enable desktop sharing.
	 *
	 * @param enableDesktopSharing the new enable desktop sharing
	 */
	public void setEnableDesktopSharing(String enableDesktopSharing) {
		this.enableDesktopSharing = enableDesktopSharing;
	}

	/**
	 * Gets the enable video sharing.
	 *
	 * @return the enable video sharing
	 */
	@Column(name = "enable_video_sharing")
	public String getEnableVideoSharing() {
		return enableVideoSharing;
	}

	/**
	 * Sets the enable video sharing.
	 *
	 * @param enableVideoSharing the new enable video sharing
	 */
	public void setEnableVideoSharing(String enableVideoSharing) {
		this.enableVideoSharing = enableVideoSharing;
	}

	/**
	 * Gets the enable2 d sharing.
	 *
	 * @return the enable2 d sharing
	 */
	@Column(name = "enable_2d_sharing")
	public String getEnable2DSharing() {
		return enable2DSharing;
	}

	/**
	 * Sets the enable2 d sharing.
	 *
	 * @param enable2dSharing the new enable2 d sharing
	 */
	public void setEnable2DSharing(String enable2dSharing) {
		enable2DSharing = enable2dSharing;
	}

	/**
	 * Gets the enable3 d sharing.
	 *
	 * @return the enable3 d sharing
	 */
	@Column(name = "enable_3d_sharing")
	public String getEnable3DSharing() {
		return enable3DSharing;
	}

	/**
	 * Sets the enable3 d sharing.
	 *
	 * @param enable3dSharing the new enable3 d sharing
	 */
	public void setEnable3DSharing(String enable3dSharing) {
		enable3DSharing = enable3dSharing;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object obj)
	{
		if (getClass() != obj.getClass())
		{
			return -1;
		}
		else
		{
			return this.aviewClass.getClassName().compareTo(((ClassRegistration)obj).getAviewClass().getClassName());
		}
	}

	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.classRegisterId);
		sb.append(Constant.DELIMETER);
		sb.append(this.user.getUserName());
		sb.append(Constant.DELIMETER);
		sb.append(this.aviewClass.getClassName());	
		sb.append(Constant.DELIMETER);
		sb.append(this.nodeTypeId);
		sb.append(Constant.DELIMETER);
		sb.append(this.isModerator);
		sb.append(Constant.DELIMETER);
		sb.append(this.enableAudioVideo);
		sb.append(Constant.DELIMETER);
		sb.append(this.enableDocumentSharing);
		sb.append(Constant.DELIMETER);
		sb.append(this.enableDesktopSharing);
		sb.append(Constant.DELIMETER);
		sb.append(this.enableVideoSharing);
		sb.append(Constant.DELIMETER);
		sb.append(this.enable2DSharing);
		sb.append(Constant.DELIMETER);
		sb.append(this.enable3DSharing);
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
		result = prime * result + ((aviewClass == null) ? 0 : aviewClass.getClassId().hashCode());
		result = prime * result + ((user == null) ? 0 : user.getUserId().hashCode());
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
		if (!(obj instanceof ClassRegistration))
			return false;
		ClassRegistration other = (ClassRegistration) obj;
		if (aviewClass == null) {
			if (other.aviewClass != null)
				return false;
		} else if (!aviewClass.getClassId().equals(other.aviewClass.getClassId()))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.getUserId().equals(other.user.getUserId()))
			return false;
		return true;
	}
	
	public static ClassRegistration prepareDummyClassRegistration(Class aviewClass,User user) throws AViewException
	{
		ClassRegistration classRegistration = new ClassRegistration();
		classRegistration.setAviewClass(aviewClass);
		classRegistration.setUser(user);
		classRegistration.setIsModerator("N");
		classRegistration.setEnable2DSharing("Y");
		classRegistration.setEnable3DSharing("Y");
		classRegistration.setEnableAudioVideo("Y");
		classRegistration.setEnableDesktopSharing("Y");
		classRegistration.setEnableDocumentSharing("Y");
		classRegistration.setEnableVideoSharing("Y");
		classRegistration.setNodeTypeId(NodeTypeHelper.getClassroomNodeType());
		classRegistration.setStatusId(StatusHelper.getActiveStatusId());
		return classRegistration;
	}

}
