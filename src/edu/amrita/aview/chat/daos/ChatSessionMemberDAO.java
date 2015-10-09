/*
 * 
 */
package edu.amrita.aview.chat.daos;

import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.chat.entities.ChatSessionMember;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.common.utils.ListUtils;


/**
 * The Class ChatSessionMemberDAO.
 */
public class ChatSessionMemberDAO extends SuperDAO {

	/**
	 * Create a chatSessionMember Entry.
	 *
	 * @param chatSM the chat sm
	 * @return the chat session member
	 * @throws AViewException
	 */
	public static ChatSessionMember createChatSessionMember(ChatSessionMember chatSM) throws AViewException
	{
		Session session = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			session.beginTransaction() ;
			session.save(chatSM) ;
			session.getTransaction().commit() ;
		}
		catch(HibernateException he)
		{
			session.getTransaction().rollback() ;
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session) ;
		}
		
		return chatSM ;
	}

	/**
	 * Create a chatSessionMember Entry.
	 *
	 * @param csms the chat sm
	 * @return the chat session member
	 * @throws AViewException
	 */
	public static void createChatSessionMembers(List<ChatSessionMember> csms) throws AViewException
	{
		Session session = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			session.beginTransaction() ;
			int i=0;
			for(ChatSessionMember csm:csms)
			{			
				i++;
				session.save(csm);
				
				//Once the batch is full, then send them to db
				if(i % HibernateUtils.HIBERNATE_BATCH_SIZE == 0)
				{
					//System.out.println("In batch operation");
					session.flush();
					session.clear();
				}
			}
			session.getTransaction().commit() ;
		}
		catch(HibernateException he)
		{
			session.getTransaction().rollback() ;
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session) ;
		}
	}
	
	/**
	 * Retrieves the  chatSessionMember entry for a given chat session.
	 *
	 * @param sessionId the session id
	 * @param memberId the member id
	 * @param statusId the status id
	 * @return the chat session member by member id
	 * @throws AViewException
	 */
	public static List<ChatSessionMember> getChatSessionMemberByMemberId(Long sessionId ,Long memberId, Integer statusId) throws AViewException
	{
		Session session = null ;
		List<ChatSessionMember> csmList = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			String hqlQueryString = " Select csm from ChatSessionMember csm  " +
									" Where csm.statusId =:statusId AND "+
									" csm.chatSessionMemberId = " +
									"(Select MAX(csm2.chatSessionMemberId) " +
									" from ChatSessionMember csm2 "+
									" Where csm2.chatSessionId = :sessionId " +
									" and csm.member.userId= :memberId)" ;
			Query hqlQuery = session.createQuery(hqlQueryString) ;
			hqlQuery.setLong("sessionId", sessionId) ;
			hqlQuery.setLong("memberId",memberId) ;
			hqlQuery.setInteger("statusId", statusId) ;
			csmList = hqlQuery.list() ;
		}
		catch(HibernateException he)
		{
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session) ;
		}
		
		return csmList ;
	}
	
	/**
	 * Gets the chat session members by session id.
	 *
	 * @param sessionId the session id
	 * @return the chat session members by session id
	 * @throws AViewException retrieve the chatSessionMembers for a given chatSession
	 * Invoked when the members of a private chat session are retrieved
	 */
	public static List<ChatSessionMember> getChatSessionMembersBySessionId(Long sessionId)throws AViewException
	{
		Session session = null ;
		List<ChatSessionMember> csmList = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			String hqlQueryString = " Select csm from ChatSessionMember csm  " +
									" where csm.chatSessionId= :sessionId " +
									" and csm.chatSessionMemberId  in " +
									" (Select max(csm.chatSessionMemberId) from ChatSessionMember csm  " +
										" where csm.chatSessionId= :sessionId group by csm.member.userId)";
			Query hqlQuery = session.createQuery(hqlQueryString) ;
			hqlQuery.setLong("sessionId", sessionId) ;
			
			csmList = hqlQuery.list() ;
		}
		catch(HibernateException he)
		{
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session) ;
		}
		
		return csmList ;
	}
	
	/**
	 * Gets the chat session members by session ids.
	 *
	 * @param List the session ids list
	 * @return the chat session members by session id
	 * @throws AViewException retrieve the chatSessionMembers for a given chatSession
	 * Invoked when the members of a private chat session are retrieved
	 */
	public static List<ChatSessionMember> getChatSessionMembersBySessionIds(List<Long> sessionIds)throws AViewException
	{
		Session session = null ;
		List<ChatSessionMember> csmList = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			final String hqlQueryStringRaw = " Select csm from ChatSessionMember csm  " +
									" where csm.chatSessionId in (~) "+
									" and csm.chatSessionMemberId  in " +
									" (Select max(csm.chatSessionMemberId) from ChatSessionMember csm  " +
										" where csm.chatSessionId in (~) group by csm.chatSessionId,csm.member.userId)";
					
			
			String hqlQueryString = hqlQueryStringRaw.replaceAll("~", ListUtils.getNumericListAsCommaDelimitedString(sessionIds));
			Query hqlQuery = session.createQuery(hqlQueryString) ;
			
			csmList = hqlQuery.list() ;
		}
		catch(HibernateException he)
		{
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session) ;
		}
		
		return csmList ;
	}

	
	/**
	 * Gets the chat session member by id.
	 *
	 * @param chatSessionMemberId the chat session member id
	 * @return the chat session member by id
	 * @throws AViewException
	 */
	public static ChatSessionMember getChatSessionMemberById(Long chatSessionMemberId) throws AViewException
	{
		Session session = null ;
		ChatSessionMember csm = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			csm = (ChatSessionMember) session.get(ChatSessionMember.class, chatSessionMemberId) ;
		}
		catch(HibernateException he)
		{
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session) ;
		}
		
		return csm ;
	}
	
	/**
	 * Update chat session member.
	 *
	 * @param csm the csm
	 * @throws AViewException update chatSessionMember
	 */
	public static void updateChatSessionMember(ChatSessionMember csm) throws AViewException
	{
		Session session = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			session.beginTransaction() ;
			session.update(csm) ;
			session.getTransaction().commit() ;
		}
		catch(HibernateException he)
		{
			session.getTransaction().rollback() ;
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session) ;
		}
	}
	
	/**
	 * Update chat session members.
	 *
	 * @param csmList the csm list
	 * @throws AViewException update chatsession members
	 */
	public static void updateChatSessionMembers(List<ChatSessionMember> csmList) throws AViewException
	{
		Session session = null;				
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();	
			long count = 0;
			for(ChatSessionMember csm:csmList)
			{
				session.update(csm);
				count++;
				//Once the batch is full, then send them to db
				if(count % HibernateUtils.HIBERNATE_BATCH_SIZE == 0)
				{
					session.flush();
					session.clear();								
				}
			}
			session.getTransaction().commit();
		} 
		catch (HibernateException he) 
		{
			processException(he);	
			session.getTransaction().rollback();
		}
		finally 
		{
			HibernateUtils.closeConnection(session);
		}
	}
	

}
