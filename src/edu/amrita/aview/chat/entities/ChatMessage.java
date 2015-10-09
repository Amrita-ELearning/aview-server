/*
 * 
 */
package edu.amrita.aview.chat.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;


/**
 * The Class ChatMessage.
 */
@Entity
@Table(name = "chat_message")
public class ChatMessage extends Auditable {

	/** The chat message id. */
	private Long chatMessageId = 0l ;
	
	/** The chat message text. */
	private String chatMessageText = null ;
	
	/** The chat session id. */
	private Long chatSessionId = null;
	
	

	/**
	 * Gets the chat message id.
	 *
	 * @return the chatMessageId
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_message_id")
	public Long getChatMessageId() {
		return chatMessageId;
	}
	
	/**
	 * Sets the chat message id.
	 *
	 * @param chatMessageId the chatMessageId to set
	 */
	public void setChatMessageId(Long chatMessageId) {
		this.chatMessageId = chatMessageId;
	}
	
	/**
	 * Gets the chat message text.
	 *
	 * @return the chatMessageText
	 */
	@Column(name = "chat_message_text")
	public String getChatMessageText() {
		return chatMessageText;
	}
	
	/**
	 * Sets the chat message text.
	 *
	 * @param chatMessageText the chatMessageText to set
	 */
	public void setChatMessageText(String chatMessageText) {
		this.chatMessageText = chatMessageText;
	}
	
	/**
	 * Gets the chat session id.
	 *
	 * @return the chat session id
	 */
	@Column(name="chat_session_id")
	public Long getChatSessionId() {
		return chatSessionId;
	}

	/**
	 * Sets the chat session id.
	 *
	 * @param chatSessionId the chat session
	 */
	public void setChatSessionId(Long chatSessionId) {
		this.chatSessionId = chatSessionId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("chatMessageId");
		builder.append(chatMessageId);
		builder.append(Constant.DELIMETER) ;
		builder.append("chatMessageText");
		builder.append(chatMessageText);
		builder.append(Constant.DELIMETER) ;
		builder.append("chatSessionId");
		builder.append(chatSessionId);
		builder.append(Constant.DELIMETER) ;
		return builder.toString();
	}

	
}
