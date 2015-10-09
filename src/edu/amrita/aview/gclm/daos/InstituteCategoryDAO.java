/**
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
import edu.amrita.aview.gclm.entities.InstituteCategory;


/**
 * The Class InstituteCategoryDAO.
 *
 * @author
 */
public class InstituteCategoryDAO  extends SuperDAO{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(InstituteCategoryDAO.class);	
	
	/**
	 * Gets the institute categories.
	 *
	 * @return the institute categories
	 * @throws AViewException
	 */
	public static List<InstituteCategory> getInstituteCategories() throws AViewException
	{
		Session session = null;
		List<InstituteCategory> instituteCategories = null;
		try {
				session = HibernateUtils.getHibernateConnection();
				Query hqlQuery = session.createQuery("SELECT instCat FROM InstituteCategory instCat order by instituteCategoryName");
				instituteCategories =  hqlQuery.list();
				if(instituteCategories.size()>0)
				{
					logger.info("Returned instituteCategories ");
				}
				else if(instituteCategories.size() == 0)
				{
					logger.warn("Warning :: No institute category ");
				}
		
		}catch (HibernateException he) {
			processException(he);	
		}finally {
				HibernateUtils.closeConnection(session);
		}
		return instituteCategories;
			
	}

}
