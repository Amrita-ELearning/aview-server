/*
 * 
 */
package edu.amrita.aview.chat.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;
import edu.amrita.aview.gclm.entities.User;


/**
 * The Class ChatSessionMember.
 */
@Entity
@Table(name = "chat_session_member")
public class ChatSessionMember extends Auditable {

	/** The chat session member id. */
	private Long chatSessionMemberId = 0l ;
	
	/** The chat session id. */
	private Long chatSessionId = null ; 


	/** The member. */
	private User member=null;
	
	/**
	 * Gets the chat session member id.
	 *
	 * @return the chatSessionMemberId
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_session_member_id")
	public Long getChatSessionMemberId() {
		return chatSessionMemberId;
	}
	
	/**
	 * Sets the chat session member id.
	 *
	 * @param chatSessionMemberId the chatSessionMemberId to set
	 */
	public void setChatSessionMemberId(Long chatSessionMemberId) {
		this.chatSessionMemberId = chatSessionMemberId;
	}
	

	/**
	 * Gets the member.
	 *
	 * @return the member
	 */
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name="member_id")
	public User getMember() {
		return member;
	}

	/**
	 * Sets the member.
	 *
	 * @param member the member
	 */
	public void setMember(User member) {
		this.member = member;
	}

	/**
	 * Gets the chat session.
	 *
	 * @return the chat session
	 */
	@Column(name="chat_session_id")
	public Long getChatSessionId() {
		return chatSessionId;
	}

	/**
	 * Sets the chat session.
	 *
	 * @param chatSessionId the new chat session
	 */
	public void setChatSessionId(Long chatSessionId) {
		this.chatSessionId = chatSessionId;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((chatSessionId == null) ? 0 : chatSessionId.hashCode());
		result = prime * result + ((member == null) ? 0 : member.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ChatSessionMember))
			return false;
		ChatSessionMember other = (ChatSessionMember) obj;
		if (chatSessionId == null) {
			if (other.chatSessionId != null)
				return false;
		} else if (!chatSessionId.equals(other.chatSessionId))
			return false;
		if (member == null) {
			if (other.member != null)
				return false;
		} else if (!member.equals(other.member))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ChatSessionMember [chatSessionMemberId=" + chatSessionMemberId
				+ ", chatSessionId=" + chatSessionId + ", member=" + member.getUserId() + "]";
	}	
}
