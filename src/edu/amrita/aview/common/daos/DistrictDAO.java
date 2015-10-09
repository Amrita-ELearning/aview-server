/**
 * 
 */
package edu.amrita.aview.common.daos;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.entities.District;
import edu.amrita.aview.common.utils.HibernateUtils;


/**
 * The Class DistrictDAO.
 *
 * @author
 */
public class DistrictDAO  extends SuperDAO{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(DistrictDAO.class);	
	
	/**
	 * Gets the districts.
	 *
	 * @param statusId the status id
	 * @return the districts
	 * @throws AViewException
	 */
	public static List<District> getDistricts(Integer statusId) throws AViewException
	{
		Session session = null;
		List<District> districts = null;
		try {
				session = HibernateUtils.getHibernateConnection();
				Query hqlQuery = session.createQuery("SELECT d FROM District d where d.statusId = :statusId order by d.districtName");
				
				hqlQuery.setInteger("statusId", statusId);
				
				districts =  hqlQuery.list();
				if(districts.size()>0)
				{
					logger.debug("Return Districts");			
				}
				else if(districts.size() == 0)
				{
					logger.debug("Warning :: No district is in list ");
				}

		}catch (HibernateException he) {
			processException(he);	
		}finally {
				HibernateUtils.closeConnection(session);
		}
		return districts;
			
	}

	/**
	 * Gets the districts by state id.
	 *
	 * @param stateId the state id
	 * @param statusId the status id
	 * @return the districts by state id
	 * @throws AViewException
	 */
	public static List<District> getDistrictsByStateId(Integer stateId,Integer statusId) throws AViewException
	{
		Session session = null;
		List<District> districts = null;
		try {
				session = HibernateUtils.getHibernateConnection();
				Query hqlQuery = session.createQuery("SELECT d FROM District d WHERE d.stateId=:stateId and d.statusId = :statusId");
				
				hqlQuery.setInteger("statusId", statusId);
				hqlQuery.setInteger("stateId",stateId);

				districts =  hqlQuery.list();
				if(districts.size()>0)
				{
					logger.debug("Return District Name");			
		
				}
				else if(districts.size() == 0)
				{
					logger.debug("Warning :: No district is in list ");
				}

		}catch (HibernateException he) {
			processException(he);	
		}finally {
				HibernateUtils.closeConnection(session);
		}
		return districts;
			
	}
}
