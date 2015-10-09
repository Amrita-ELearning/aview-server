/*
 * 
 */
package edu.amrita.aview.chat.helpers;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.HashMap;

import edu.amrita.aview.chat.daos.ChatSessionDAO;
import edu.amrita.aview.chat.entities.ChatMessage;
import edu.amrita.aview.chat.entities.ChatSession;
import edu.amrita.aview.chat.entities.ChatSessionMember;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.AppenderUtils;
import edu.amrita.aview.common.utils.TimestampUtils;




/**
 * The Class ChatSessionHelper.
 */
public class ChatSessionHelper {

	/**
	 * Creates a ChatSession Entity.
	 *
	 * @param chatSession the chat session
	 * @param creatorId the creator id
	 * @return the chat session
	 * @throws AViewException
	 */
	public static ChatSession createChatSession(ChatSession chatSession , Long creatorId) throws AViewException
	{
		chatSession.setNulls();

		if(chatSession.getIsPrivateChat().equalsIgnoreCase("Y"))
		{
			
			if(chatSession.getMembers().size() != 1)
			{
				throw new AViewException("A private chat session should contain exactly one Member");
			}
			
			ChatSessionMember receiver = chatSession.getMembers().iterator().next();
			
			ChatSession privateChatSession=ChatSessionDAO.getPrivateChatSession(chatSession.getLectureId(),
					chatSession.getOwner().getUserId(),
					receiver.getMember().getUserId());
			
			if(privateChatSession != null)
			{
				populateChatSession(privateChatSession);
				return privateChatSession;
			}
		}
		
		
		chatSession.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId()) ;
		chatSession = ChatSessionDAO.createChatSession(chatSession) ;

		List<ChatSessionMember> members = chatSession.getMembers();
		for(ChatSessionMember member:members)
		{
			member.setChatSessionId(chatSession.getChatSessionId());
			member.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId()) ;
		}
		ChatSessionMemberHelper.createChatSessionMembers(members, creatorId);
		
		return chatSession;
	}
	
	public static ChatSession getChatSessionById(Long chatSessionId) throws AViewException
	{
		ChatSession chatSession=ChatSessionDAO.getChatSessionById(chatSessionId) ;
		populateChatSession(chatSession);
		return chatSession;
	}
	
	private static void populateChatSession(ChatSession chatSession)
	{
		if(chatSession == null)
		{
			return;
		}
		List<ChatSessionMember> chatMembers = ChatSessionMemberHelper.getChatSessionMembersBySessionId(chatSession.getChatSessionId());
		chatSession.setMembers(chatMembers);
		
		List<ChatMessage> chatMessages= ChatMessageHelper.getChatMessagesBySessionId(chatSession.getChatSessionId());
		chatSession.setMessages(chatMessages);
	}
	
	private static void populateChatSessions(List<ChatSession> chatSessions)
	{
		if(chatSessions.size() == 0)
		{
			return;
		}
		List<Long> sessionIds = new ArrayList<Long>();
		Map<Long,ChatSession> sessionMap = new HashMap<Long,ChatSession>();
		
		for (ChatSession cs:chatSessions)
		{
			sessionIds.add(cs.getChatSessionId());
			sessionMap.put(cs.getChatSessionId(), cs);
		}
		
		List<ChatSessionMember> chatMembers = ChatSessionMemberHelper.getChatSessionMembersBySessionIds(sessionIds);
		for(ChatSessionMember csm:chatMembers)
		{
			sessionMap.get(csm.getChatSessionId()).addMember(csm);
		}
		
		List<ChatMessage> chatMessages= ChatMessageHelper.getChatMessagesBySessionIds(sessionIds);
		for(ChatMessage cm:chatMessages)
		{
			sessionMap.get(cm.getChatSessionId()).addMessage(cm);
		}
	}
	
	/**
	 * Retrieves the privateChatSession details for a given lecture.
	 *
	 * @param lectureId the lecture id
	 * @param senderId the sender id
	 * @param receiverId the receiver id
	 * @return the private chat session
	 * @throws AViewException
	 */
	public static ChatSession getPrivateChatSession(long lectureId,long senderId,long receiverId)throws AViewException
	{
		ChatSession cs=ChatSessionDAO.getPrivateChatSession(lectureId,senderId,receiverId);
		populateChatSession(cs);
		return cs;
	}
	
	/**
	 * Retrieves the chatSessions of a given user.
	 *
	 * @param memberId the member id
	 * @return the chat session by member id
	 * @throws AViewException
	 */
	public static List<ChatSession> getChatSessionsByMemberId(Long memberId) throws AViewException	
	{
		List<ChatSession> csList = ChatSessionDAO.getChatSessionsByMemberId(memberId) ;
		populateChatSessions(csList);
		return csList ;
	}
	
	/**
	 * deletes a chatSession.
	 *
	 * @param chatSessionId the chat session id
	 * @throws AViewException
	 */
	public static void deleteChatSessions(List<ChatSession> sessions,Long deleterId) throws AViewException
	{
		for(ChatSession cs:sessions)
		{
			cs.setNulls();
			cs.setTitle(cs.getTitle()+AppenderUtils.DeleteAppender());
			cs.setStatusId(StatusHelper.getDeletedStatusId());
			cs.setModifiedAuditData(deleterId, TimestampUtils.getCurrentTimestamp());
		}
		ChatSessionDAO.updateChatSessions(sessions);
	}
	
	/**
	 * Update chat session.
	 *
	 * @param chatSession the chat session
	 * @return the chat session
	 * @throws AViewException update a chatSession
	 */
	public static ChatSession updateChatSession(ChatSession updatedSession,Long updaterId) throws AViewException
	{
		updatedSession.setNulls();
		updatedSession.setModifiedAuditData(updaterId, TimestampUtils.getCurrentTimestamp());
		return ChatSessionDAO.updateChatSession(updatedSession) ; 
	}

}
