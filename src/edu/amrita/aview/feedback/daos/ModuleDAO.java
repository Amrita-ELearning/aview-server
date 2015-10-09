/*
 * 
 */
package edu.amrita.aview.feedback.daos;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.feedback.entities.Module;


/**
 * The Class ModuleDAO.
 */
public class ModuleDAO extends SuperDAO {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(ModuleDAO.class);
	
	/**
	 * Creates the module.
	 *
	 * @param module the module
	 * @throws AViewException
	 */
	public static void createModule(Module module) throws AViewException
	{
		Session session=null;
		try{
				session=HibernateUtils.getHibernateConnection();
				session.beginTransaction();
				session.save(module);
				session.getTransaction().commit();
			}catch (HibernateException he) {
				processException(he);	
				session.getTransaction().rollback();
			}finally {
				HibernateUtils.closeConnection(session);
			}
	}
	
	/**
	 * Update module.
	 *
	 * @param module the module
	 * @throws AViewException
	 */
	public static void updateModule(Module module) throws AViewException
	{
		Session session=null;
		try
		{
			session=HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.update(module);
			session.getTransaction().commit();
		}catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		}finally {
			HibernateUtils.closeConnection(session);
		}
	}
	
	/**
	 * Gets the modules.
	 *
	 * @param statusId the status id
	 * @return the modules
	 * @throws AViewException
	 */
	public static List<Module> getModules(Integer statusId) throws AViewException
	{
		Session session = null;
		List<Module> modules = null;
		try {
				session = HibernateUtils.getHibernateConnection();
				Query hqlQuery = session.createQuery("SELECT s FROM Module s where s.statusId = :statusId");
				hqlQuery.setInteger("statusId", statusId);
				modules =  hqlQuery.list();
				if(modules.size()>0)
				{
					logger.debug("Return Module");			
				}
				else if(modules.size() == 0)
				{
					logger.debug("Warning :: No Modules is in list ");
				}

		}catch (HibernateException he) {
			processException(he);	
		}finally {
			HibernateUtils.closeConnection(session);
		}
		return modules;
	}
	

}
