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
import edu.amrita.aview.gclm.entities.Server;


/**
 * The Class ServerDAO.
 */
public class ServerDAO  extends SuperDAO{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(ServerDAO.class);
	
	/**
	 * Creates the server.
	 *
	 * @param server the server
	 * @throws AViewException
	 */
	public static void createServer(Server server) throws AViewException
	{
		Session session=null;
		try{
				session=HibernateUtils.getHibernateConnection();
				session.beginTransaction();
				session.save(server);
				session.getTransaction().commit();
			}catch (HibernateException he) {
				processException(he);	
				session.getTransaction().rollback();
			}finally {
				HibernateUtils.closeConnection(session);
			}
	}
	
	/**
	 * Update server.
	 *
	 * @param server the server
	 * @throws AViewException
	 */
	public static void updateServer(Server server) throws AViewException
	{
		Session session=null;
		try
		{
			session=HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.update(server);
			session.getTransaction().commit();
		}catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		}finally {
			HibernateUtils.closeConnection(session);
		}
	}
	
	/**
	 * Gets the servers.
	 *
	 * @param statusId the status id
	 * @return the servers
	 * @throws AViewException
	 */
	public static List<Server> getServers(Integer statusId) throws AViewException
	{
		Session session = null;
		List<Server> servers = null;
		try {
				session = HibernateUtils.getHibernateConnection();
				Query hqlQuery = session.createQuery("SELECT s FROM Server s where s.statusId = :statusId");
				hqlQuery.setInteger("statusId", statusId);
				servers =  hqlQuery.list();
				if(servers.size()>0)
				{
					logger.debug("Return Server");			
				}
				else if(servers.size() == 0)
				{
					logger.debug("Warning :: No Servers is in list ");
				}

		}catch (HibernateException he) {
			processException(he);	
		}finally {
			HibernateUtils.closeConnection(session);
		}
		return servers;
	}

}
