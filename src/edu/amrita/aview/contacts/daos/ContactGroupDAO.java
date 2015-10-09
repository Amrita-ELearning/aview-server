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
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.contacts.entities.ContactGroup;
import edu.amrita.aview.contacts.entities.GroupUser;


/**
 * The Class ContactGroupDAO.
 */
public class ContactGroupDAO extends SuperDAO {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(ContactGroupDAO.class);
	
	/**
	 * Creates the group.
	 *
	 * @param group the group to create
	 * @throws AViewException
	 */
	public static void createGroup(ContactGroup group) throws AViewException
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.save(group);
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
	 * Update the group.
	 *
	 * @param group the group to update
	 * @throws AViewException
	 */
	public static void updateGroup(ContactGroup group) throws AViewException
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.update(group);
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
	 * Retrive the group.
	 *
	 * @param groupId the groupId to retrieve
	 * @return the group by id
	 * @throws AViewException
	 */
	public static ContactGroup getGroupById(Long groupId) throws AViewException
	{
		Session session = null;
		ContactGroup group = null;
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			group = (ContactGroup)session.get(ContactGroup.class, groupId);
			group.setMemberCount(GroupUserDAO.getGroupUserCountByGroupId(group.getContactGroupId(),StatusHelper.getActiveStatusId()));
		} 
		catch (HibernateException he) {
			processException(he);	
		}		
		finally 
		{
			HibernateUtils.closeConnection(session);
		}
		return group;
	}
	
	/**
	 * Gets the groups by owner.
	 *
	 * @param ownerId the owner id
	 * @param statusId the status id
	 * @return the groups by owner
	 * @throws AViewException
	 */
	public static List<ContactGroup> getGroupsByOwner(Long ownerId, Integer statusId) throws AViewException
	{
		Session session = null;
		List<ContactGroup> groups = null;
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "Select g from ContactGroup g  where g.groupOwnerId = :ownerId " +
					" AND g.statusId = :statusId";
			
			Query hqlQuery = session.createQuery(hqlQueryString);			
			hqlQuery.setInteger("statusId", statusId);
			hqlQuery.setLong("ownerId", ownerId);			
			groups = hqlQuery.list();
		} 
		catch (HibernateException he) 
		{
			processException(he);	
		}		
		finally 
		{
			HibernateUtils.closeConnection(session);
		}
		return groups;
	}
	public static List<GroupUser> getAllContactsByOwner(Long ownerId, Integer statusId) throws AViewException
	{
		Session session = null;
		List<GroupUser> groupUsers = null;
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "SELECT DISTINCT  gUser FROM " +
					"GroupUser gUser,ContactGroup cGroup WHERE cGroup.contactGroupId=gUser.contactGroupId" +
					" AND cGroup.groupOwnerId=:ownerId AND cGroup.statusId=:statusId";
			
			Query hqlQuery = session.createQuery(hqlQueryString);			
			hqlQuery.setInteger("statusId", statusId);
			hqlQuery.setLong("ownerId", ownerId);			
			groupUsers= hqlQuery.list();
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
	public static List<ContactGroup> searchAllgroupsForOwnerByName(String name,long ownerId,Integer statusId)
	{
		Session session = null;
		List<ContactGroup> groups = null;
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "Select g from ContactGroup g  where g.groupOwnerId = :ownerId " +
					" AND g.statusId = :statusId and g.contactGroupName like :name";
			
			Query hqlQuery = session.createQuery(hqlQueryString);			
			hqlQuery.setInteger("statusId", statusId);
			hqlQuery.setLong("ownerId", ownerId);	
			hqlQuery.setString("name", '%'+ name +'%');
			groups = hqlQuery.list();
		} 
		catch (HibernateException he) 
		{
			processException(he);	
		}		
		finally 
		{
			HibernateUtils.closeConnection(session);
		}
		return groups;
	}
	
}
