/*
 * 
 */
package edu.amrita.aview.gclm.daos;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.gclm.entities.Node;


/**
 * The Class NodeDAO.
 */
public class NodeDAO  extends SuperDAO{
	
	/**
	 * Creates the node.
	 *
	 * @param node the node
	 * @throws AViewException
	 */
	public static void createNode(Node node) throws AViewException {
		Session session = null;
		String creationMessage = null ;
		try {
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.save(node);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		} finally {			
			HibernateUtils.closeConnection(session);
		}
	}

	/**
	 * Gets the active nodes.
	 *
	 * @param statusId the status id
	 * @return the active nodes
	 * @throws AViewException
	 */
	public static List<Node> getActiveNodes(int statusId) throws AViewException{
		Session session = null;
		List<Node> nodes = null;
		try {
			session = HibernateUtils.getHibernateConnection();			
			Query hqlQuery = session.createQuery("SELECT aviewNode FROM Node aviewNode WHERE statusId=:statusId" +
												 " ORDER BY nodeLocation ASC, nodeName ASC ");
			hqlQuery.setInteger("statusId", statusId);
			nodes = hqlQuery.list();

		} catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}
		return nodes;
	}

	/**
	 * Gets the node for user id.
	 *
	 * @param userId the user id
	 * @param statusId the status id
	 * @return the node for user id
	 * @throws AViewException
	 */
	public static List<Node> getNodeForUserId(int userId,int statusId) throws AViewException
	{
		Session session = null ;
		List<Node> nodes = null ;
		
		try{
			session = HibernateUtils.getHibernateConnection() ;
			String hqlQueryString = "SELECT DISTINCT n" +
									" FROM Node n , Course co , ClassRegistration clr,User u" +
									" WHERE n.nodeId = co.nodeId" +
									" AND clr.user.userId = u.userId" +
									" and n.statusId=:statusId" +
									" AND clr.user.userId=:userId" ;
			Query hqlQuery = session.createQuery(hqlQueryString);
			hqlQuery.setInteger("userId", userId);
			hqlQuery.setInteger("statusId", statusId);
			
			nodes = hqlQuery.list() ;
		}catch(HibernateException he){
			processException(he);	
		}finally{
			HibernateUtils.closeConnection(session);
		}
		
		return nodes ;
	}
	
	/**
	 * Gets the node.
	 *
	 * @param nodeId the node id
	 * @return the node
	 * @throws AViewException
	 */
	public static Node getNode(Long nodeId) throws AViewException{
		Session session = null;
		Node node = null;	
		try {
			session = HibernateUtils.getHibernateConnection();
			Query hqlQuery = session.createQuery("select aviewNode from Node aviewNode where nodeId=:nodeId");
			hqlQuery.setLong("nodeId", nodeId);
			List temp = hqlQuery.list();
			if(temp.size() == 1)
			{
				node = (Node)(temp.get(0));
			}
			else if(temp.size() == 0)
			{
				//System.out.println("Warning :: No node was returned for the given node id");
			}
			else if(temp.size() > 1)
			{
				//System.out.println("Warning :: More than one node was returned for the given node id");
			}
			
		} catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}	
		return node;
	}

	/**
	 * Update node.
	 *
	 * @param node the node
	 * @throws AViewException
	 */
	public static void updateNode(Node node) throws AViewException{	
		Session session = null;
		String creationMessage = null ;
		try {
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();			
			session.update(node);
			session.getTransaction().commit();

		} catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		} finally {
			HibernateUtils.closeConnection(session);
		}
	}
}
