/*
 * 
 */
package edu.amrita.aview.chat.helpers;

import java.util.List;

import edu.amrita.aview.chat.daos.ChatMessageDAO;
import edu.amrita.aview.chat.entities.ChatMessage;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.TimestampUtils;


/**
 * The Class ChatMessageHelper.
 */
public class ChatMessageHelper {

	/**
	 * Creates the chat message.
	 *
	 * @param cm the cm
	 * @param creatorId the creator id
	 * @return the chat message
	 * @throws AViewException
	 */
	public static ChatMessage createChatMessage(ChatMessage cm , Long creatorId) throws AViewException
	{
		cm.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId()) ;
		return ChatMessageDAO.createChatMessage(cm) ;
	}
	
	/**
	 * Gets the chat message by session id.
	 *
	 * @param sessionId the session id
	 * @return the chat message by session id
	 * @throws AViewException
	 */
	public static List<ChatMessage> getChatMessagesBySessionId(Long sessionId ) throws AViewException
	{
		return ChatMessageDAO.getChatMessagesBySessionId(sessionId, StatusHelper.getActiveStatusId()) ;
	}
	
	/**
	 * Gets the chat message by session ids.
	 *
	 * @param List the session ids
	 * @return the chat message by session ids
	 * @throws AViewException
	 */
	public static List<ChatMessage> getChatMessagesBySessionIds(List<Long> sessionIds) throws AViewException
	{
		return ChatMessageDAO.getChatMessagesBySessionIds(sessionIds, StatusHelper.getActiveStatusId()) ;
	}	

}
