/*
 * 
 */
package edu.amrita.aview.chat.helpers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.amrita.aview.chat.daos.ChatSessionMemberDAO;
import edu.amrita.aview.chat.entities.ChatSessionMember;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.TimestampUtils;


/**
 * The Class ChatSessionMemberHelper.
 */
public class ChatSessionMemberHelper {
	 
	/**
	 * create a chatSessionMember Entry.
	 *
	 * @param csm the csm
	 * @param creatorId the creator id
	 * @return the chat session member
	 * @throws AViewException
	 */
	public static ChatSessionMember createChatSessionMember(ChatSessionMember csm , Long creatorId) throws AViewException
	{
		csm.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId()) ;
		ChatSessionMember chatSessionMember=ChatSessionMemberDAO.createChatSessionMember(csm);
		return  chatSessionMember;
	}

	/**
	 * create a chatSessionMember Entry.
	 *
	 * @param csm the csm
	 * @param creatorId the creator id
	 * @return the chat session member
	 * @throws AViewException
	 */
	public static List<ChatSessionMember> createChatSessionMembers(List<ChatSessionMember> csms , Long creatorId) throws AViewException
	{
		for(ChatSessionMember csm:csms)
		{
			csm.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId()) ;
		}
		ChatSessionMemberDAO.createChatSessionMembers(csms);
		return  csms;
	}
	
	/**
	 * Retrieves ChatSession Member Entries for a given chatsession id.
	 *
	 * @param sessionId the session id
	 * @return the chat session members by session id
	 * @throws AViewException
	 */
	public static List<ChatSessionMember> getChatSessionMembersBySessionId(Long sessionId) throws AViewException
	{
		List<ChatSessionMember> csmList = ChatSessionMemberDAO.getChatSessionMembersBySessionId(sessionId) ;
		return csmList ;
	}
	
	/**
	 * Retrieves ChatSession Member Entries for a given list of chatsession ids.
	 *
	 * @param List  the session ids
	 * @return the chat session members by session id
	 * @throws AViewException
	 */
	public static List<ChatSessionMember> getChatSessionMembersBySessionIds(List<Long> sessionIds) throws AViewException
	{
		List<ChatSessionMember> csmList = ChatSessionMemberDAO.getChatSessionMembersBySessionIds(sessionIds) ;
		return csmList ;
	}
	
	/**
	 * Retrieves the latest chatSessionMemberId for the given user in chat session.
	 *
	 * @param sessionId the session id
	 * @param memberId the member id
	 * @return the chat session member by member id
	 * @throws AViewException
	 */
	public static List<ChatSessionMember> getChatSessionMemberByMemberId(Long sessionId,Long memberId) throws AViewException
	{
		List<ChatSessionMember> csmList = ChatSessionMemberDAO.getChatSessionMemberByMemberId(sessionId,memberId, StatusHelper.getActiveStatusId()) ;
		return csmList ;
	}
	
	/**
	 * updates a chat sessionMember entry.
	 *
	 * @param csm the ChatSessionMember
	 * @return the chat session member
	 * @throws AViewException
	 */
	public static ChatSessionMember updateChatSessionMember(ChatSessionMember csm,Long updaterId) throws AViewException
	{
		csm.setModifiedAuditData(updaterId, TimestampUtils.getCurrentTimestamp());
		ChatSessionMemberDAO.updateChatSessionMember(csm) ;
		return csm;
	}
	
	/**
	 * Update chat session members.
	 *
	 * @param csmList the csm list
	 * @throws AViewException
	 */
	public static void updateChatSessionMembers(List<ChatSessionMember> csmList,Long updaterId) throws AViewException
	{
		for(ChatSessionMember csm :csmList)
		{
			csm.setModifiedAuditData(updaterId, TimestampUtils.getCurrentTimestamp());
		}
		ChatSessionMemberDAO.updateChatSessionMembers(csmList) ;
	}
	
	/**
	 * Update chat session members.
	 *
	 * @param csms the csm list
	 * @throws AViewException
	 */
	public static void deleteChatSessionMembers(List<ChatSessionMember> csms,Long deleterId) throws AViewException
	{
		for(ChatSessionMember csm :csms)
		{
			csm.setStatusId(StatusHelper.getDeletedStatusId());
		}
		updateChatSessionMembers(csms,deleterId) ;
	}
}
