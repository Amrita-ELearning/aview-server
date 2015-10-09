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
import edu.amrita.aview.gclm.entities.InstituteServer;


/**
 * The Class InstituteServerDAO.
 */
public class InstituteServerDAO  extends SuperDAO{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(InstituteServerDAO.class);	
	
	/**
	 * Creates the institute server.
	 *
	 * @param instituteServer the institute server
	 * @throws AViewException
	 */
	public static void createInstituteServer(InstituteServer instituteServer) throws AViewException
	{
		Session session=null;
		String creationMessage = null ;
		try{
				session=HibernateUtils.getHibernateConnection();
				session.beginTransaction();
				session.save(instituteServer);
				session.getTransaction().commit();
			}catch (HibernateException he) {
				processException(he);	
				session.getTransaction().rollback();
			}finally {
				HibernateUtils.closeConnection(session);
			}
	}
	
	/**
	 * Update institute server.
	 *
	 * @param instituteServer the institute server
	 * @throws AViewException
	 */
	public static void updateInstituteServer(InstituteServer instituteServer) throws AViewException
	{
		Session session=null;
		String creationMessage = null ;
		try
		{
			session=HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.update(instituteServer);
			session.getTransaction().commit();
		}catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		}finally {
			HibernateUtils.closeConnection(session);
		}
	}

	/**
	 * Gets the institute servers.
	 *
	 * @param instituteId the institute id
	 * @param statusId the status id
	 * @return the institute servers
	 * @throws AViewException
	 */
	@SuppressWarnings("unchecked")
	public static List<InstituteServer> getInstituteServers(Long instituteId, Integer statusId) throws AViewException{
		Session session = HibernateUtils.getHibernateConnection();
		List<InstituteServer> servers = null;
		try {
			String query = "select ins from InstituteServer ins where ins.institute.instituteId = :instituteId " +
					"and ins.statusId = :statusId";
			Query hqlQuery = session.createQuery(query);
			hqlQuery.setLong("instituteId", instituteId);
			hqlQuery.setInteger("statusId", statusId);
			servers = hqlQuery.list();
		} catch (HibernateException he) {
			processException(he);	
		} finally {
			HibernateUtils.closeConnection(session);
		}
		return servers;
	}
}
