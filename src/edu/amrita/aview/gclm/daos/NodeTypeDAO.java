/*
 * 
 */
package edu.amrita.aview.gclm.daos;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.gclm.entities.NodeType;


/**
 * The Class NodeTypeDAO.
 */
public class NodeTypeDAO  extends SuperDAO{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(NodeTypeDAO.class);

	/**
	 * Gets the all node types.
	 *
	 * @return the all node types
	 * @throws AViewException
	 */
	public static List<NodeType> getAllNodeTypes()  throws AViewException{
		Session session = null;
		List<NodeType> nodetypes = null;

		try {
			session = HibernateUtils.getHibernateConnection();
			Query q = session.createQuery("SELECT n FROM NodeType n ORDER BY nodeTypeName ASC ");
			nodetypes = q.list();
			logger.info(nodetypes.size()+" Node types are Retrieved");
		} catch (HibernateException he) {
			processException(he);	
		}	
		finally {
			HibernateUtils.closeConnection(session);
		}
		return nodetypes;
	}
	
	/**
	 * Gets the node type id.
	 *
	 * @param nodeTypeName the node type name
	 * @param statusId the status id
	 * @return the node type id
	 * @throws AViewException
	 */
	public static Integer getNodeTypeId(String nodeTypeName, Integer statusId) throws AViewException
	{
		Session session = null;
		Integer nodeTypeId = null;
		try
		{
			session = HibernateUtils.getHibernateConnection();
			//Query q = session.createQuery("select statusId from Status status where statusName=:statusName");
			Query hqlQuery = session.createQuery("SELECT nodeTypeId FROM NodeType n where nodeTypeName = :nodeTypeName and statusId = :statusId");
			hqlQuery.setString("nodeTypeName", nodeTypeName);
			hqlQuery.setInteger("statusId", statusId);
			List temp = hqlQuery.list();
			if(temp.size() == 1)
			{
				nodeTypeId = Integer.parseInt(temp.get(0).toString());
			}
			else if(temp.size() == 0)
			{
				logger.debug("Warning :: No Node type returned for the given node type name");
			}
			else if(temp.size() > 1)
			{
				logger.debug("Warning :: More than one Node type returned for the given nodeTypeName");
			}
		}
		catch (HibernateException he)
		{
			processException(he);	
		}	
		finally 
		{
			HibernateUtils.closeConnection(session);
		}
		return nodeTypeId;
	}
}
