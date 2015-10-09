/*
 * 
 */
package edu.amrita.aview.gclm.helpers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.gclm.daos.NodeTypeDAO;
import edu.amrita.aview.gclm.entities.NodeType;


/**
 * The Class NodeTypeHelper.
 */
public class NodeTypeHelper {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(NodeTypeHelper.class);
	
	/** The node type map. */
	private static Map<String,Integer> nodeTypeMap = new HashMap<String,Integer>();
	
	/**
	 * Gets the all node types.
	 *
	 * @return the all node types
	 * @throws AViewException
	 */
	public static List<NodeType> getAllNodeTypes() throws AViewException
	{
		List<NodeType> nodetypes = NodeTypeDAO.getAllNodeTypes();	
		return nodetypes;
	}
	
	/**
	 * Gets the classroom node type.
	 *
	 * @return the classroom node type
	 * @throws AViewException
	 */
	public static Integer getClassroomNodeType() throws AViewException
	{
		Integer classroomNodeTypeId = null;
		if((classroomNodeTypeId = nodeTypeMap.get(Constant.CLASSROOM_COMPUTER_NODETYPE)) == null)
		{
			classroomNodeTypeId = NodeTypeDAO.getNodeTypeId(Constant.CLASSROOM_COMPUTER_NODETYPE, StatusHelper.getActiveStatusId());
			nodeTypeMap.put(Constant.CLASSROOM_COMPUTER_NODETYPE, classroomNodeTypeId);
		}		
		return classroomNodeTypeId;
	}
	
	/**
	 * Gets the personal computer node type.
	 *
	 * @return the personal computer node type
	 * @throws AViewException
	 */
	public static Integer getPersonalComputerNodeType() throws AViewException
	{
		Integer personalNodeTypeId = null;
		if((personalNodeTypeId = nodeTypeMap.get(Constant.PERSONAL_COMPUTER_NODETYPE)) == null)
		{
			personalNodeTypeId = NodeTypeDAO.getNodeTypeId(Constant.PERSONAL_COMPUTER_NODETYPE, StatusHelper.getActiveStatusId());
			nodeTypeMap.put(Constant.PERSONAL_COMPUTER_NODETYPE, personalNodeTypeId);
		}		
		return personalNodeTypeId;
	}
	
	/**
	 * Clear cache.
	 */
	public static void clearCache()
	{
		logger.debug("Entering NodeTypeHelper::clearCache");
		nodeTypeMap = null;
		logger.debug("Entering NodeTypeHelper::clearCache");
	}
}
