/*
 * 
 */
package edu.amrita.aview.gclm.helpers;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.HibernateException;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.AppenderUtils;
import edu.amrita.aview.gclm.daos.NodeDAO;
import edu.amrita.aview.gclm.entities.Course;
import edu.amrita.aview.gclm.entities.Node;


/**
 * The Class NodeHelper.
 */
public class NodeHelper {

	/**
	 * Creates the node.
	 *
	 * @param node the node
	 * @throws AViewException
	 */
	public static void createNode(Node node) throws AViewException{
		 NodeDAO.createNode(node);	
	}

	/**
	 * Gets the active nodes.
	 *
	 * @return the active nodes
	 * @throws AViewException
	 */
	public static List<Node> getActiveNodes() throws AViewException{
		int activeSId = StatusHelper.getActiveStatusId();
		List<Node> nodes = NodeDAO.getActiveNodes(activeSId);
		return nodes;
	}

	/**
	 * Gets the node for user id.
	 *
	 * @param userId the user id
	 * @return the node for user id
	 * @throws AViewException
	 */
	public static List<Node> getNodeForUserId(int userId) throws AViewException
	{
		int activeSId = StatusHelper.getActiveStatusId();
		List<Node> nodes = NodeDAO.getNodeForUserId(userId, activeSId);
		return nodes ;
	}
	
	/**
	 * Update node.
	 *
	 * @param node the node
	 * @throws AViewException
	 */
	public static void updateNode(Node node) throws AViewException{		
		NodeDAO.updateNode(node);
	}

	/**
	 * Delete node.
	 *
	 * @param nodeId the node id
	 * @param userId the user id
	 * @throws AViewException
	 */
	public static void deleteNode(Long nodeId, Long userId) throws AViewException {	
		int deletedSId = StatusHelper.getDeletedStatusId();
		Node node = NodeDAO.getNode(nodeId);
		node.setStatusId(deletedSId);
		node.setModifiedByUserId(userId);
		node.setModifiedDate(new Timestamp(System.currentTimeMillis()));
		//bug fix for node name not getting added when any deleted node has the same node name
		node.setNodeName(node.getNodeLocation() + AppenderUtils.DeleteAppender());
		// bug fix for #2562 start
		// Restrict deletion of node if it has registered courses
		boolean canDelete = true ;
		List coursesForNode = CourseHelper.searchCourse(null,null,nodeId) ;
		canDelete = (coursesForNode.size()== 0)? false : true ;
		
		if(canDelete)
		{					
			String creationMessage = " Node is registered for the following course(es): \n";
			String courseNames = "";
			Course courseInst = null;		
			int i = 0;
			for(i = 0 ; i < coursesForNode.size() - 1 ; i++)
			{
				Object[] obj = (Object[])coursesForNode.get(i) ;
				//System.out.println();
				courseInst  = (Course)obj[0] ;
				courseNames += courseInst.getCourseName() + ", ";
				
			}
			Object[] obj = (Object[])coursesForNode.get(i) ;
			courseInst = (Course)obj[0] ;			
			if(courseNames != "")
			{
				courseNames += " and ";
			}
			courseNames += courseInst.getCourseName();			
			creationMessage += courseNames;			
			creationMessage += ". \nPlease delete the cours(es) and try again.";
			//System.out.println(creationMessage+" :: error ::");
			throw (new HibernateException(creationMessage));
		}
		else
		{
			NodeDAO.updateNode(node);
		}
		// bug fix for #2562 end

	}

	/**
	 * Test.
	 */
	public static void test() {
		
//		/*GregorianCalendar gr1 = new GregorianCalendar(2010,11,22);	
//		GregorianCalendar gr2 = new GregorianCalendar(2011,00,03);
//		  NodeDAO node1 = new NodeDAO(); 
//		  Node node11 = new Node(); 
//		  NodeType node12 = new NodeType(); 
//		//  node11.setNodeId(3);
//		  node11.setNodeName("amme saranam");
//		  node11.setNodeLocation("Mumbai"); 
//		  node12.setNodeTypeId(1);
//		  node11.setCreatedDate(new Timestamp(gr1.getTime().getTime()));
//			// node11.setEndDate(new Timestamp(gr1.getTime().getTime()));			
//			 node11.setModifiedByUserId(44);
//			 node11.setModifiedDate(new Timestamp(gr1.getTime().getTime()));
//			 node11.setNodeTypeId(1);
//			 node11.setStatusId(1);
//			 node11.setCreatedByUserId(1);
//		  node1.createNode(node11);*/
//		  //node1.updateNode(node11);
//		  List<Node> node = NodeDAO.getActiveNodes(1) ;
//		  
//		  for(Node i:node) { System.out.println(" Node Details "+i); } 
//		//NodeDAO.deleteNode();

	}
}
