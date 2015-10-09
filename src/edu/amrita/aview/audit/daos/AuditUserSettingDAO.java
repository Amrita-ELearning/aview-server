/*
 * 
 */
package edu.amrita.aview.audit.daos;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import edu.amrita.aview.audit.entities.AuditUserSetting;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.utils.HibernateUtils;


/**
 * The Class AuditUserSettingDAO.
 */
public class AuditUserSettingDAO extends SuperDAO{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(AuditUserSettingDAO.class);
	
	/**
	 * Creates the user setting.
	 *
	 * @param userSetting the user setting
	 * @throws AViewException
	 */
	public static void createUserSetting(AuditUserSetting userSetting) throws AViewException
	{		
		Session session = null;	
		try {
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.save(userSetting);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			processException(he);
			session.getTransaction().rollback();
		}
		finally {
			HibernateUtils.closeConnection(session);
		}
	}
}
