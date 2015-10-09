/*
 * 
 */
package edu.amrita.aview.chat.entities;





import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;
import edu.amrita.aview.gclm.entities.User;


/**
 * The Class ChatSession.
 */
@Entity
@Table(name = "chat_session")
public class ChatSession extends Auditable {

	/** The chat session id. */
	private Long chatSessionId = 0l ;
	
	/** The owner */
	private User owner = null ;
	
	/** The title. */
	private String title = null ;
	
	/** The group id. */
	private Long groupId = 0l ;
	
	/** The lecture id. */
	private Long lectureId= 0l;
		
	/** The lecture id. */
	private String isPrivateChat= null;

	/** The participants of chat. */
	private List<ChatSessionMember> members = new ArrayList<ChatSessionMember>();

	/** The participants of chat. */
	private List<ChatMessage> messages = new ArrayList<ChatMessage>();
	
	/**
	 * Gets the chat session id.
	 *
	 * @return the chatSessionId
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_session_id")
	public Long getChatSessionId() {
		return chatSessionId;
	}
	
	

	/**
	 * Sets the chat session id.
	 *
	 * @param chatSessionId the chatSessionId to set
	 */
	public void setChatSessionId(Long chatSessionId) {
		this.chatSessionId = chatSessionId;
	}
	
	/**
	 * Gets the owner.
	 *
	 * @return the owner
	 */
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name="owner_id", referencedColumnName="user_id")
	public User getOwner() {
		return owner;
	}
	
	/**
	 * Sets the owner id.
	 *
	 * @param ownerId the ownerId to set
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	@Column(name = "title")
	public String getTitle() {
		return title;
	}
	
	/**
	 * Sets the title.
	 *
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the group id.
	 *
	 * @return the groupId
	 */
	@Column(name = "group_id")
	public Long getGroupId() {
		return groupId;
	}

	/**
	 * Sets the group id.
	 *
	 * @param groupId the groupId to set
	 */
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
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
	 * @param lectureId the lecture id
	 */
	public void setLectureId(Long lectureId) {
		this.lectureId = lectureId;
	}
	
	/**
	 * Gets the chat session members.
	 *
	 * @return the chat session members
	 */
	@Transient
	public List<ChatSessionMember> getMembers() {
		return members;
	}

	/**
	 * Adds the institute server.
	 *
	 * @param chatSessionMember the chat session member
	 */
	public synchronized void addMember(ChatSessionMember chatSessionMember)
	{
		this.members.add(chatSessionMember);
	}


	/**
	 * Sets the chat session members.
	 *
	 * @param members the chat session members
	 */
	public void setMembers(List<ChatSessionMember> chatSessionMembers) {
		this.members = chatSessionMembers;
	}


	/**
	 * Gets the chat messages.
	 *
	 * @return the chat messages
	 */
	@Transient
	public List<ChatMessage> getMessages() {
		return messages;
	}

	/**
	 * Adds the institute server.
	 *
	 * @param chatMessage the chat session member
	 */
	public synchronized void addMessage(ChatMessage chatMessage)
	{
		this.messages.add(chatMessage);
	}


	/**
	 * Sets the chat messages.
	 *
	 * @param members the chat messages
	 */

	public void setMessages(List<ChatMessage> messages) {
		this.messages = messages;
	}

	/**
	 * Gets the isPrivateChat value.
	 *
	 * @return the lecture id
	 */
	@Column(name = "is_private_chat")
	public String getIsPrivateChat() {
		return isPrivateChat;
	}

	/**
	 * Gets the isPrivateChat value.
	 *
	 * @return the lecture id
	 */
	public void setIsPrivateChat(String isPrivateChat) {
		this.isPrivateChat = isPrivateChat;
	}

	@Override
	public String toString() {
		return "ChatSession [chatSessionId=" + chatSessionId + ", owner="
				+ owner.getUserName() + ", title=" + title + ", groupId=" + groupId
				+ ", lectureId=" + lectureId + ", members="
				+ members.size() + "]";
	}

	
	public void setNulls()
	{
		if(this.getGroupId() == 0)
		{
			this.setGroupId(null);
		}
		if(this.getLectureId() == 0)
		{
			this.setLectureId(null);
		}
	}

}
