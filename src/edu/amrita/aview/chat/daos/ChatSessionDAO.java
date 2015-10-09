/*
 * 
 */
package edu.amrita.aview.chat.daos;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.chat.entities.ChatSession;
import edu.amrita.aview.chat.entities.ChatSessionMember;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.gclm.entities.Class;


/**
 * The Class ChatSessionDAO.
 */
public class ChatSessionDAO extends SuperDAO {

	/**
	 * create a chatSession.
	 *
	 * @param chatSession the chat session
	 * @return the chat session
	 * @throws AViewException
	 */
	public static ChatSession createChatSession(ChatSession chatSession) throws AViewException
	{
		Session session = null ;		
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			session.beginTransaction() ;
			session.save(chatSession) ;
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
		return chatSession ;
	}
	
	/**
	 * update the given chatsession.
	 *
	 * @param chatSession the chat session
	 * @return the chat session
	 * @throws AViewException
	 */
	public static ChatSession updateChatSession(ChatSession chatSession) throws AViewException
	{
		Session session = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			session.beginTransaction() ;
			session.update(chatSession) ;
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
		return chatSession ;
	}
	
	
	/**
	 * update the given chatsession.
	 *
	 * @param chatSession the chat session
	 * @return the chat session
	 * @throws AViewException
	 */
	public static void updateChatSessions(List<ChatSession> sessions) throws AViewException
	{
		Session session = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			session.beginTransaction() ;
			long count = 0;
			for(ChatSession cs:sessions)
			{
				session.update(cs);
				count++;
				//Once the batch is full, then send them to db
				if(count % HibernateUtils.HIBERNATE_BATCH_SIZE == 0)
				{
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
	 * Gets the chat session by id.
	 *
	 * @param chatSessionId the chat session id
	 * @return the chat session by id
	 * @throws AViewException Retrieves a chatSession by chatSessionId
	 */
	public static ChatSession getChatSessionById(Long chatSessionId) throws AViewException
	{
		Session session = null ;
		ChatSession cs = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			cs = (ChatSession) session.get(ChatSession.class, chatSessionId) ;
		}
		catch(HibernateException he)
		{
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session) ;
		}
		
		return cs ;
	}
	
	/**
	 * Retrieves the private chat session.
	 *
	 * @param lectureId the lecture id
	 * @param memberId1 the member id1
	 * @param memberId2 the member id2
	 * @param statusId the status id
	 * @return the private chat session
	 * @throws AViewException
	 */
	public static ChatSession getPrivateChatSession(Long lectureId,Long memberId1,Long memberId2) throws AViewException
	{
		Session session = null ;
		ChatSession cs = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			String hqlQueryString = " Select  cs from ChatSession cs , ChatSessionMember csm" +
													" where cs.chatSessionId = csm.chatSessionId " +
													" and cs.isPrivateChat='Y'" +
													" and cs.owner.userId=:ownerId" +
													" and csm.member.userId=:memberId" +
													" and cs.statusId != :statusId" +
													" and csm.statusId != :statusId"
													 ;
			
			if(lectureId != null && lectureId != 0)
			{
				hqlQueryString += " and cs.lectureId=:lectureId";
			}
			
			Query hqlQuery  = session.createQuery(hqlQueryString) ;
			
			cs = setAndExecutePrivateChat(hqlQuery,lectureId,memberId1,memberId2) ;
			if(cs == null) //Search again with owner and member reversed
			{
				cs = setAndExecutePrivateChat(hqlQuery,lectureId,memberId2,memberId1) ;
			}
		}
		catch(HibernateException he)
		{
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session) ;
		}
		return cs;
	}
	
	private static ChatSession setAndExecutePrivateChat(Query hqlQuery,Long lectureId,Long memberId1,Long memberId2)
	{
		ChatSession cs = null ;
		
		hqlQuery.setInteger("statusId", StatusHelper.getDeletedStatusId()) ;
		hqlQuery.setLong("ownerId", memberId1);
		hqlQuery.setLong("memberId", memberId2);

		if(lectureId != null && lectureId != 0)
		{
			hqlQuery.setLong("lectureId", lectureId);
		}
		
		List csList = hqlQuery.list() ;
		if(csList.size() > 0)
		{
			cs = (ChatSession) csList.get(0);
		}
		return cs;
	}
	

	
	/**
	 * Retrieves all chatSession entries for a given user.
	 *
	 * @param memberId the member id
	 * @param statusId the status id
	 * @return the chat session by member id
	 * @throws AViewException
	 */
	public static List<ChatSession> getChatSessionsByMemberId(Long memberId) throws AViewException	
	{
		Session session = null ;
		List<ChatSession> csList = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			String hqlQueryString = " Select distinct cs from ChatSession cs , ChatSessionMember csm" +
													" where cs.chatSessionId = csm.chatSessionId " +
													" and csm.member.userId=:memberId " +
													" and cs.statusId != :statusId "+
													" and csm.statusId != :statusId ";
			Query hqlQuery  = session.createQuery(hqlQueryString) ;
			hqlQuery.setLong("memberId", memberId) ;
			hqlQuery.setInteger("statusId", StatusHelper.getDeletedStatusId()) ;
			csList = hqlQuery.list() ;
			
			hqlQueryString = " Select cs from ChatSession cs" +
					" where cs.owner.userId=:memberId " +
					" and cs.statusId != :statusId  ";
			hqlQuery  = session.createQuery(hqlQueryString) ;
			hqlQuery.setLong("memberId", memberId) ;
			hqlQuery.setInteger("statusId", StatusHelper.getDeletedStatusId()) ;
			csList.addAll(hqlQuery.list()) ;
			
			Collections.sort(csList, new Comparator<ChatSession>() {
				  public int compare(ChatSession o1, ChatSession o2) {
				      return o1.getCreatedDate().compareTo(o2.getCreatedDate());
				  }
			});
		}
		catch(HibernateException he)
		{
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session) ;
		}
		
		return csList ;
	}
	

	
	
}
