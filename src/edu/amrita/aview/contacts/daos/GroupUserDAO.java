/*
 * 
 */
package edu.amrita.aview.contacts.daos;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.common.utils.ListUtils;
import edu.amrita.aview.contacts.entities.GroupUser;


/**
 * The Class GroupUserDAO.
 */
public class GroupUserDAO extends SuperDAO {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(GroupUserDAO.class);
	
	/**
	 * Creates the group user.
	 *
	 * @param groupUser the groupUser to create
	 * @throws AViewException
	 */
	public static void createGroupUser(GroupUser groupUser) throws AViewException
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.save(groupUser);
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

	/**
	 * Update the group user.
	 *
	 * @param groupUser the groupUser to update
	 * @throws AViewException
	 */
	public static void updateGroupUser(GroupUser groupUser) throws AViewException
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.update(groupUser);
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
	
	/**
	 * Retrieve the group user.
	 *
	 * @param groupUserId the group user id
	 * @return GroupUser
	 * @throws AViewException
	 */
	public static GroupUser getGroupUserById(Long groupUserId) throws AViewException
	{
		Session session = null;
		GroupUser groupUser = null;
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			groupUser = (GroupUser)session.get(GroupUser.class, groupUserId);
		} 
		catch (HibernateException he) {
			processException(he);	
		}		
		finally 
		{
			HibernateUtils.closeConnection(session);
		}
		return groupUser;
	}
	
	/**
	 * Gets the group users.
	 *
	 * @param upto1000userIds the upto1000user ids
	 * @return the group users
	 * @throws AViewException
	 */
	public static List<GroupUser> getGroupUsers(List<Long> upto1000userIds) throws AViewException{
		Session session = null;
		List<GroupUser> users = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			String query = "select gUser from GroupUser gUser where groupUserId in(#)";
			query = query.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(upto1000userIds));
			Query hqlQuery = session.createQuery(query);
			users = hqlQuery.list();
		} catch (HibernateException he) {
			processException(he);	
		} finally {
			HibernateUtils.closeConnection(session);
		}
		return users;
	}
	
	/**
	 * Delete the group user.
	 *
	 * @param groupUserId the groupUserId to delete
	 * @throws AViewException
	 */
	public static void deleteGroupUser(Long groupUserId) throws AViewException
	{
		Session session = null;
		GroupUser groupUser = getGroupUserById(groupUserId);
		try
		{
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.delete(groupUser);
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
	
	/**
	 * Creates the group users.
	 *
	 * @param groupUsers the groupUsers to create
	 * @throws AViewException
	 */
	public static void createGroupUsers(List<GroupUser> groupUsers) throws AViewException
	{
		Session session = null;		
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();	
			
			long count = 0;
			for(GroupUser groupUser : groupUsers)
			{
				session.save(groupUser);
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

	//RGCR: I think we also need to add some pagination logic here. 
	//May not be now, but later we can consider it.
	//Not Done
	/**
	 * Gets the users by group id.
	 *
	 * @param groupId the group id
	 * @param activeSId the active s id
	 * @return the users by group id
	 * @throws AViewException
	 */
	public static List<GroupUser> getUsersByGroupId( Long groupId, Integer activeSId) throws AViewException
	{
		Session session = null;
		List<GroupUser> groupUsers = null;
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			
			String hqlQueryString = "Select groupUser from GroupUser groupUser where " +
									" groupUser.statusId = :activeSId and groupUser.contactGroupId =:groupId";			
			
			Query hqlQuery = session.createQuery(hqlQueryString);
			hqlQuery.setLong("groupId", groupId);
			hqlQuery.setInteger("activeSId", activeSId);			
			groupUsers = hqlQuery.list();		
		} 
		catch (HibernateException he) 
		{
			processException(he);	
		}		
		finally 
		{
			HibernateUtils.closeConnection(session);
		}
		return groupUsers;
	}
	
	public static Long getGroupUserCountByGroupId(Long groupId, Integer statusId) throws AViewException
	{
		Session session = null;
		session = HibernateUtils.getHibernateConnection();
		String hqlQueryString = "Select count(gu.groupUserId) from GroupUser gu  where gu.contactGroupId = :groupId " +
				" AND gu.statusId = :statusId";
		
		Query hqlQuery = session.createQuery(hqlQueryString);			
		hqlQuery.setInteger("statusId", statusId);
		hqlQuery.setLong("groupId", groupId);	
		return (Long) hqlQuery.list().get(0);
	}
	
	/**
	 * Delete the group users.
	 *
	 * @param groupUsers the groupUsers to delete
	 * @throws AViewException
	 */
	public static void deleteGroupUsers(List<GroupUser> groupUsers) throws AViewException
	{
		Session session = null;		
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();	
			
			long count = 0;
			for(GroupUser groupUser : groupUsers)
			{
				session.delete(groupUser);
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
