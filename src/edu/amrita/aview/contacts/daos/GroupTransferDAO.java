package edu.amrita.aview.contacts.daos;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.contacts.entities.ContactGroup;
import edu.amrita.aview.contacts.entities.GroupTransfer;

public class GroupTransferDAO extends SuperDAO
{
	public static void createGroupTransfer(GroupTransfer groupTransfer) throws AViewException
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.save(groupTransfer);
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
	public static void createGroupTransfers(List<GroupTransfer> groupTransfers) throws AViewException
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			for(int i=0;i<groupTransfers.size();i++)
			{
				session.save(groupTransfers.get(i));
				if((i+1) % HibernateUtils.HIBERNATE_BATCH_SIZE == 0)
				{
					//System.out.println("In batch operation");
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
//	public static void updateGroupTransfer(GroupTransfer groupTransfer) throws AViewException
//	{
//		Session session = null;
//		try
//		{
//			session = HibernateUtils.getHibernateConnection();
//			session.beginTransaction();
//			session.update(groupTransfer);
//			session.getTransaction().commit();
//		}
//		catch (HibernateException he)
//		{
//			processException(he);
//			session.getTransaction().rollback();
//		}
//		finally
//		{
//			HibernateUtils.closeConnection(session);
//		}
//	}
	public static void deleteGroupTransfer(GroupTransfer groupTransfer) throws AViewException
	{
		Session session = null;
		try
		{
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.delete(groupTransfer);
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
	public static GroupTransfer getGroupTransferById(Long groupTransferId) throws AViewException
	{
		Session session = null;
		GroupTransfer group = null;
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			group = (GroupTransfer)session.get(GroupTransfer.class, groupTransferId);
			
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
	public static List<GroupTransfer> getTransferredGroupsByReceiver(Long userId,Integer statusId) throws AViewException
	{
		Session session=null;
		List<GroupTransfer> groups=null;
		try
		{
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "Select g from GroupTransfer g  where g.receiver.userId = :receiverId " +
					" AND g.statusId = :statusId";
			
			Query hqlQuery = session.createQuery(hqlQueryString);			
			hqlQuery.setInteger("statusId", statusId);
			hqlQuery.setLong("receiverId", userId);			
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
