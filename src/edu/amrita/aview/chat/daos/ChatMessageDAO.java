/*
 * 
 */
package edu.amrita.aview.chat.daos;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.chat.entities.ChatMessage;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.common.utils.ListUtils;


/**
 * The Class ChatMessageDAO.
 */
public class ChatMessageDAO extends SuperDAO {

	/**
	 * Creates the chat message.
	 *
	 * @param cm the cm
	 * @return the chat message
	 * @throws AViewException
	 */
	public static ChatMessage createChatMessage(ChatMessage cm) throws AViewException
	{
		Session session = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			session.beginTransaction() ;
			session.save(cm) ;
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
		return cm ;
	}
	
	/**
	 * Gets the chat message by id.
	 *
	 * @param chatMessageId the chat message id
	 * @return the chat message by id
	 * @throws AViewException
	 */
	public static ChatMessage getChatMessageById(Long chatMessageId) throws AViewException
	{
		Session session = null ;
		ChatMessage cm = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			cm = (ChatMessage) session.get(ChatMessage.class, chatMessageId) ;			
		}
		catch(HibernateException he)
		{
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session) ;
		}
		return cm ;
	}
	
	/**
	 * Gets the chat message by session id.
	 *
	 * @param sessionId the session id
	 * @param statusId the status id
	 * @return the chat message by session id
	 * @throws AViewException
	 */
	public static List<ChatMessage> getChatMessagesBySessionId(Long sessionId , Integer statusId) throws AViewException
	{
		Session session = null ;
		List<ChatMessage> cmList = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			String hqlQueryString = " Select cm from ChatMessage cm  " +
										" Where cm.chatSessionId =:sessionId " +
										" And cm.statusId = :statusId order by cm.createdDate";
			Query hqlQuery = session.createQuery(hqlQueryString) ;
			hqlQuery.setLong("sessionId", sessionId) ;
			hqlQuery.setInteger("statusId", statusId) ;
			cmList = hqlQuery.list();
		}
		catch(HibernateException he)
		{
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session) ;
		}
		
		return cmList ;
	}
	
	
	/**
	 * Gets the chat message by session ids.
	 *
	 * @param List the session ids
	 * @param statusId the status id
	 * @return the chat message by session ids
	 * @throws AViewException
	 */
	public static List<ChatMessage> getChatMessagesBySessionIds(List<Long> sessionIds , Integer statusId) throws AViewException
	{
		Session session = null ;
		List<ChatMessage> cmList = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			final String hqlQueryStringRaw = " Select cm from ChatMessage cm  " +
										" Where cm.chatSessionId in (~) " +
										" And cm.statusId = :statusId order by cm.createdDate";
			
			String hqlQueryString = hqlQueryStringRaw.replaceAll("~", ListUtils.getNumericListAsCommaDelimitedString(sessionIds));
			Query hqlQuery = session.createQuery(hqlQueryString) ;
			hqlQuery.setInteger("statusId", statusId) ;
			cmList = hqlQuery.list();
		}
		catch(HibernateException he)
		{
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session) ;
		}
		
		return cmList ;
	}

}
