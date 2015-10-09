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
import edu.amrita.aview.gclm.entities.BrandingAttribute;


/**
 * The Class BrandingAttributeDAO.
 */
public class BrandingAttributeDAO extends SuperDAO {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(BrandingAttributeDAO.class);
	
	/**
	 * Creates the branding attribute.
	 *
	 * @param brandingAttribute the branding attribute
	 * @throws AViewException
	 */
	public static void createBrandingAttribute(BrandingAttribute brandingAttribute) throws AViewException
	{
		Session session=null;
		String creationMessage = null ;
		try{
				session=HibernateUtils.getHibernateConnection();
				session.beginTransaction();
				session.save(brandingAttribute);
				session.getTransaction().commit();
			}catch (HibernateException he) {
				processException(he);	
				session.getTransaction().rollback();
			}finally {
				HibernateUtils.closeConnection(session);
			}
	}
	
	/**
	 * Update branding attribute.
	 *
	 * @param brandingAttribute the branding attribute
	 * @throws AViewException
	 */
	public static void updateBrandingAttribute(BrandingAttribute brandingAttribute) throws AViewException
	{
		Session session=null;
		String creationMessage = null ;
		try
		{
			session=HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.update(brandingAttribute);
			session.getTransaction().commit();
		}catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		}finally {
			HibernateUtils.closeConnection(session);
		}
	}
	
	/**
	 * Gets the branding attributes.
	 *
	 * @param statusId the status id
	 * @return the branding attributes
	 * @throws AViewException
	 */
	public static List<BrandingAttribute> getBrandingAttributes(Integer statusId) throws AViewException
	{
		Session session = null;
		List<BrandingAttribute> brandingAttributes = null;
		try {
				session = HibernateUtils.getHibernateConnection();
				Query hqlQuery = session.createQuery("SELECT ba FROM BrandingAttribute ba where ba.statusId = :statusId");
				hqlQuery.setInteger("statusId", statusId);
				brandingAttributes =  hqlQuery.list();
				if(brandingAttributes.size()>0)
				{
					logger.debug("Return BrandingAttribute");			
				}
				else if(brandingAttributes.size() == 0)
				{
					logger.debug("Warning :: No BrandingAttributes is in list ");
				}

		}catch (HibernateException he) {
			processException(he);	
		}finally {
			HibernateUtils.closeConnection(session);
		}
		return brandingAttributes;
	}

}
