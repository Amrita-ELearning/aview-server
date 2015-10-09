/*
 * 
 */
package edu.amrita.aview.common.daos;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.entities.SystemParameter;
import edu.amrita.aview.common.utils.HibernateUtils;


/**
 * The Class SystemParameterDAO.
 */
public class SystemParameterDAO  extends SuperDAO{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(SystemParameterDAO.class);
	
	/**
	 * Creates the system parameter.
	 *
	 * @param systemParameter the system parameter
	 * @throws AViewException
	 */
	public static void createSystemParameter(SystemParameter systemParameter) throws AViewException
	{
		Session session=null;
		String creationMessage = null ;
		try{
				session=HibernateUtils.getHibernateConnection();
				session.beginTransaction();
				session.save(systemParameter);
				session.getTransaction().commit();
			}catch (HibernateException he) {
				processException(he);	
				session.getTransaction().rollback();
			}finally {
				HibernateUtils.closeConnection(session);
			}
	}
	
	/**
	 * Update system parameter.
	 *
	 * @param systemParameter the system parameter
	 * @throws AViewException
	 */
	public static void updateSystemParameter(SystemParameter systemParameter) throws AViewException
	{
		Session session=null;
		String creationMessage = null ;
		try
		{
			session=HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.update(systemParameter);
			session.getTransaction().commit();
		}catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		}finally {
			HibernateUtils.closeConnection(session);
		}
	}
	
	/**
	 * Gets the system parameters.
	 *
	 * @param statusId the status id
	 * @return the system parameters
	 * @throws AViewException
	 */
	public static List<SystemParameter> getSystemParameters(Integer statusId) throws AViewException
	{
		Session session = null;
		List<SystemParameter> systemParameters = null;
		try {
				session = HibernateUtils.getHibernateConnection();
				Query hqlQuery = session.createQuery("SELECT s FROM SystemParameter s where s.statusId = :statusId");
				hqlQuery.setInteger("statusId", statusId);
				systemParameters =  hqlQuery.list();
				if(systemParameters.size()>0)
				{
					logger.debug("Return SystemParameter");			
				}
				else if(systemParameters.size() == 0)
				{
					logger.debug("Warning :: No SystemParameters is in list ");
				}

		}catch (HibernateException he) {
			processException(he);	
		}finally {
			HibernateUtils.closeConnection(session);
		}
		return systemParameters;
	}
	
	/**
	 * Gets the system parameters.
	 *
	 * @param systemParameterName the system parameter name
	 * @param statusId the status Id 
	 * @return the system parameters
	 * @throws AViewException
	 */
	public static SystemParameter getSystemParameterByName(String systemParameterName, Integer statusId) throws AViewException
	{
		Session session = null;
		List <SystemParameter> systemParameters = null;
		SystemParameter systemParameter = null; 
		try {
				session = HibernateUtils.getHibernateConnection();
				Query hqlQuery = session.createQuery("SELECT s FROM SystemParameter s where s.parameterName = :systemParameterName and s.statusId = :statusId");
				hqlQuery.setInteger("statusId", statusId);
				hqlQuery.setString("systemParameterName", systemParameterName);
				systemParameters = hqlQuery.list();
				if(systemParameters.size() == 1)
				{
					systemParameter = systemParameters.get(0);
					logger.debug("Return SystemParameter" + systemParameter);					
				}
				else if(systemParameters.size() > 1)
				{
					logger.debug("Found more than one system parameter for the given name");
				}
				else if(systemParameters.size() == 0)
				{
					logger.debug("Warning :: No SystemParameters is in list ");
				}

		}catch (HibernateException he) {
			processException(he);	
		}finally {
			HibernateUtils.closeConnection(session);
		}
		return systemParameter;
	}

}
