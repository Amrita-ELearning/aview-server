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
import edu.amrita.aview.common.utils.ListUtils;
import edu.amrita.aview.gclm.entities.Institute;




/**
 * The Class InstituteDAO.
 *
 * @author
 */
public class InstituteDAO extends SuperDAO{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(InstituteDAO.class);	
	
	/**
	 * Creates the institute.
	 *
	 * @param institute the institute
	 * @throws AViewException
	 */
	public static void createInstitute(Institute institute) throws AViewException {
		Session session = null;
		try {
				session = HibernateUtils.getHibernateConnection();
				session.beginTransaction();	
				session.save(institute);
				session.getTransaction().commit();
		}catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		}finally {
				HibernateUtils.closeConnection(session);
		}
	}
//	public static void updateInstitute(Institute institute) throws AViewException {
//		
//		Session session = null;
//		try {			
//				session = HibernateUtils.getHibernateConnection();
//				session.beginTransaction();
//				session.update(institute);
//				session.getTransaction().commit();
//		}catch (HibernateException he) {
//			processException(he);	
//			session.getTransaction().rollback();
//		}finally {
//			HibernateUtils.closeConnection(session);
//		}
//	}

	/**
 * Update institute.
 *
 * @param institute the institute
 * @throws AViewException
 */
public static void updateInstitute(Institute institute) throws AViewException {
		
		Session session = HibernateUtils.getCurrentHibernateConnection();
		session.update(institute);
	}
	
	/**
	 * Gets the all institutes.
	 *
	 * @param statusId the status id
	 * @return the all institutes
	 * @throws AViewException
	 */
	public static List<Institute> getAllInstitutes(Integer statusId) throws AViewException 
	{
		Session session = null;
		List<Institute> institutes= null;
		try {
				session = HibernateUtils.getHibernateConnection();
				String hqlQueryString = "SELECT inst FROM Institute inst where statusId = :statusId";
				Query hqlQuery = session.createQuery(hqlQueryString);
				hqlQuery.setInteger("statusId", statusId);
				institutes = hqlQuery.list();
				if(institutes.size()>0)
				{
					logger.warn("Retured Institutes");			
				}
				else if(institutes.size() == 0)
				{
					logger.warn("Warning :: No Institute ");
				}
		}catch (HibernateException he) {
			processException(he);	
		}finally {
			HibernateUtils.closeConnection(session);
		}
		return institutes;
		
	}
	
	/**
	 * Gets the all institutes data.
	 *
	 * @param statusId the status id
	 * @param instName the inst name
	 * @param cityName the city name
	 * @param pInstName the p inst name
	 * @return the all institutes data
	 * @throws AViewException
	 */
	public static List<Institute> getAllInstitutesData(Integer statusId, String instName,String cityName, String pInstName) throws AViewException 
	{
		Session session = null;
		List<Institute> institutes= null;
		try {
				session = HibernateUtils.getHibernateConnection();
				String hqlQueryString = null;
				if(pInstName == "")
				{
					hqlQueryString = "SELECT inst FROM Institute inst where statusId = :statusId";
				}
				else
				{
					hqlQueryString = "SELECT inst FROM Institute inst, Institute inst1 WHERE inst.statusId = :statusId and "+
					"inst.parentInstituteId = inst1.instituteId AND inst1.instituteName like :pInstName";
				}
				if(instName != "")
				{
					hqlQueryString += " and inst.instituteName like :instName ";
				}
				if(cityName != "")
				{
					hqlQueryString += " and inst.city like :cityName ";
				}
				
				Query hqlQuery = session.createQuery(hqlQueryString);
				
				hqlQuery.setInteger("statusId", statusId);
				if(instName != "")
				{
					hqlQuery.setString("instName", ("%" + instName + "%"));
				}
				if(cityName != "")
				{
					hqlQuery.setString("cityName", ("%" + cityName + "%"));
				}
				if(pInstName != "")
				{
					hqlQuery.setString("pInstName", ("%"+ pInstName + "%"));
				}
				
				institutes = hqlQuery.list();
				if(institutes.size()>0)
				{
					logger.warn("Retured Institutes");			
				}
				else if(institutes.size() == 0)
				{
					logger.warn("Warning :: No Institute ");
				}
		}catch (HibernateException he) {
			processException(he);	
		}finally {
			HibernateUtils.closeConnection(session);
		}
		return institutes;
		
	}
	
	/**
	 * Gets the all institutes details.
	 *
	 * @param statusId the status id
	 * @return the all institutes details
	 * @throws AViewException
	 */
	public static List<Institute> getAllInstitutesDetails(Integer statusId) throws AViewException 
	{
		Session session = null;
		List<Institute> institutes= null;
		try {
				session = HibernateUtils.getHibernateConnection();
				String hqlQueryString = "SELECT inst FROM Institute inst where statusId = :statusId";
				Query hqlQuery = session.createQuery(hqlQueryString);
				hqlQuery.setInteger("statusId", statusId);
				institutes = hqlQuery.list();
				if(institutes.size()>0)
				{
					logger.warn("Retured Institutes");			
				}
				else if(institutes.size() == 0)
				{
					logger.warn("Warning :: No Institute ");
				}
		}catch (HibernateException he) {
			processException(he);	
		}finally {
			HibernateUtils.closeConnection(session);
		}
		return institutes;
		
	}
	
	/**
	 * Gets the all course offering institutes.
	 *
	 * @param statusId the status id
	 * @return the all course offering institutes
	 * @throws AViewException
	 */
	public static List<Institute> getAllCourseOfferingInstitutes(Integer statusId) throws AViewException 
	{
		Session session = null;
		List<Institute> institutes= null;
		try {
				session = HibernateUtils.getHibernateConnection();
				String hqlQueryString = "SELECT distinct inst FROM Institute inst,Course cr " +
						"where cr.instituteId = inst.instituteId " +
						"and cr.statusId = :statusId "+
						"and inst.statusId = :statusId";
				Query hqlQuery = session.createQuery(hqlQueryString);
				hqlQuery.setInteger("statusId", statusId);
				institutes = hqlQuery.list();
				if(institutes.size()>0)
				{
					logger.warn("Retured Institutes");
				}
				else if(institutes.size() == 0)
				{
					logger.warn("Warning :: No Institute ");
				}
		}catch (HibernateException he) {
			processException(he);	
		}finally {
			HibernateUtils.closeConnection(session);
		}
		return institutes;
		
	}
	
	/**
	 * Gets the all course offering institutes for admin.
	 *
	 * @param adminUserId the admin user id
	 * @param statusId the status id
	 * @return the all course offering institutes for admin
	 * @throws AViewException
	 */
	public static List<Institute> getAllCourseOfferingInstitutesForAdmin(Long adminUserId, Integer statusId) throws AViewException 
	{
		Session session = null;
		List<Institute> institutes= null;
		try {
				session = HibernateUtils.getHibernateConnection();
				String hqlQueryString = "SELECT distinct inst FROM Institute inst,InstituteAdminUser iau,Course cr " +
										"where (iau.institute.instituteId = inst.instituteId " +
										"or iau.institute.instituteId = inst.parentInstituteId) " +
										"and cr.instituteId = inst.instituteId and cr.statusId = :statusId " +
										"and iau.user.userId = :userId and inst.statusId = :statusId";
				
				Query hqlQuery = session.createQuery(hqlQueryString);
				hqlQuery.setLong("userId", adminUserId);
				hqlQuery.setInteger("statusId", statusId);
				institutes = hqlQuery.list();
				
				if(institutes.size()>0)
				{
					logger.warn("Retured Institutes");			
				}
				else if(institutes.size() == 0)
				{
					logger.warn("Warning :: No Institute ");
				}
		}catch (HibernateException he) {
			processException(he);	
		}finally {
				HibernateUtils.closeConnection(session);
		}
		return institutes;
		
	}
	
	/**
	 * Gets the all institutes for admin.
	 *
	 * @param adminUserId the admin user id
	 * @param statusId the status id
	 * @return the all institutes for admin
	 * @throws AViewException
	 */
	public static List<Institute> getAllInstitutesForAdmin(Long adminUserId, Integer statusId) throws AViewException 
	{
		Session session = null;
		List<Institute> institutes= null;
		try {
				session = HibernateUtils.getHibernateConnection();
				String hqlQueryString = "SELECT distinct inst FROM Institute inst,InstituteAdminUser iau " +
										"where (iau.institute.instituteId = inst.instituteId " +
										"or iau.institute.instituteId = inst.parentInstituteId) " +
										"and iau.user.userId = :userId and inst.statusId = :statusId";
				
				Query hqlQuery = session.createQuery(hqlQueryString);
				hqlQuery.setLong("userId", adminUserId);
				hqlQuery.setInteger("statusId", statusId);
				institutes = hqlQuery.list();
				
				if(institutes.size()>0)
				{
					logger.warn("Retured Institutes");
				}
				else if(institutes.size() == 0)
				{
					logger.warn("Warning :: No Institute ");
				}
		}catch (HibernateException he) {
			processException(he);	
		}finally {
				HibernateUtils.closeConnection(session);
		}
		return institutes;
		
	}
	
	/**
	 * Gets the all institutes data for admin.
	 *
	 * @param adminUserId the admin user id
	 * @param statusId the status id
	 * @param instName the inst name
	 * @param cityName the city name
	 * @param pInstName the p inst name
	 * @return the all institutes data for admin
	 * @throws AViewException
	 */
	public static List<Institute> getAllInstitutesDataForAdmin(Long adminUserId, Integer statusId, String instName, String cityName, String pInstName) throws AViewException 
	{
		Session session = null;
		List<Institute> institutes= null;
		try {
				session = HibernateUtils.getHibernateConnection();
				String hqlQueryString = null;
				if(pInstName == "")
				{
					hqlQueryString = "SELECT distinct inst FROM Institute inst,InstituteAdminUser iau " +
							"where (iau.institute.instituteId = inst.instituteId " +
							"or iau.institute.instituteId = inst.parentInstituteId) " +
							"and iau.user.userId = :userId and inst.statusId = :statusId";
				}
				else
				{
					hqlQueryString = "SELECT distinct inst FROM Institute inst,InstituteAdminUser iau , Institute inst1 " +
							"WHERE (iau.institute.instituteId = inst.instituteId " +
							"or iau.institute.instituteId = inst.parentInstituteId) " +
							"and iau.user.userId = :userId and inst.statusId = :statusId and "+
					"inst.parentInstituteId = inst1.instituteId AND inst1.instituteName like :pInstName";
				}
				
				if(instName != "")
				{
					hqlQueryString += " and inst.instituteName like :instName ";
				}
				if(cityName != "")
				{
					hqlQueryString += " and inst.city like :cityName ";
				}
				Query hqlQuery = session.createQuery(hqlQueryString);
				
				if(instName != "")
				{
					hqlQuery.setString("instName", ("%" + instName + "%"));
				}
				if(cityName != "")
				{
					hqlQuery.setString("lectureTopic", ("%" + cityName + "%"));
				}
				if(pInstName != "")
				{
					hqlQuery.setString("pInstName", ("%"+ pInstName + "%"));
				}
				
				hqlQuery.setLong("userId", adminUserId);
				hqlQuery.setInteger("statusId", statusId);
				institutes = hqlQuery.list();
				
				if(institutes.size()>0)
				{
					logger.warn("Retured Institutes");			
				}
				else if(institutes.size() == 0)
				{
					logger.warn("Warning :: No Institute ");
				}
		}catch (HibernateException he) {
			processException(he);	
		}finally {
				HibernateUtils.closeConnection(session);
		}
		return institutes;
		
	}
	
	/**
	 * Gets the institute by name.
	 *
	 * @param instituteName the institute name
	 * @param statusId the status id
	 * @return the institute by name
	 * @throws AViewException
	 */
	public static Institute getInstituteByName(String instituteName, Integer statusId) throws AViewException 
	{
		Session session = null;
		Institute institute= null;
		try {
				session = HibernateUtils.getHibernateConnection();
				String hqlQueryString = "SELECT inst FROM Institute inst where inst.instituteName = :instituteName and inst.statusId = :statusId";
				Query hqlQuery = session.createQuery(hqlQueryString);
				
				hqlQuery.setString("instituteName", instituteName);
				hqlQuery.setInteger("statusId", statusId);
				
				List temp = hqlQuery.list();
				
				if(temp.size() == 1)
				{
					institute = (Institute)temp.get(0);
				}
				else if(temp.size() > 0)
				{
					logger.warn("Warning :: More than one institute found ");
				}
				else if(temp.size() == 0)
				{
					logger.warn("Warning :: No Institute ");
				}
		}catch (HibernateException he) {
			processException(he);	
		}finally {
			HibernateUtils.closeConnection(session);
		}
		return institute;
		
	}
	
	/*public static List<Institute> searchInstitutesByName(String instituteName, Integer statusId) throws AViewException
	{
		Session session = null;
		List<Institute> institutes= null;
		try {
				session = HibernateUtils.getHibernateConnection();
				String hqlQueryString = "SELECT inst FROM Institute inst where inst.statusId = :statusId ";
				if(instituteName != null && instituteName.trim() != "")
				{
					hqlQueryString += "and inst.instituteName like :instituteName";
				}
				Query hqlQuery = session.createQuery(hqlQueryString);
				
				logger.info("Search query for Institutes: " + hqlQueryString);
				
				if(instituteName != null && instituteName.trim() != "")
				{
					hqlQuery.setString("instituteName", ('%' + instituteName + '%'));
				}
				hqlQuery.setInteger("statusId", statusId);
				institutes = hqlQuery.list();
				
				if(institutes.size()>0)
				{
					logger.warn("Retured Institutes");			
				}
				else if(institutes.size() == 0)
				{
					logger.warn("Warning :: No Institute ");
				}
		}catch (HibernateException he) {
			processException(he);	
		}finally {
				HibernateUtils.closeConnection(session);
		}
		return institutes;
	}*/
	
	/**
	 * Gets the institute by id.
	 *
	 * @param instituteId the institute id
	 * @return the institute by id
	 * @throws AViewException
	 */
	public static Institute getInstituteById(Long instituteId) throws AViewException 
	{
		Session session = null;
		Institute institute = null;
		try {
				session = HibernateUtils.getHibernateConnection();
				institute = (Institute)session.get(Institute.class, instituteId);
		}catch (HibernateException he) {
			processException(he);	
		}finally {
			HibernateUtils.closeConnection(session);
		}
		return institute;
	}
	
	/**
	 * Search institutes.
	 *
	 * @param instituteName the institute name
	 * @param city the city
	 * @param districtId the district id
	 * @param stateId the state id
	 * @param instituteStatusIds the institute status ids
	 * @return the list< institute>
	 * @throws AViewException
	 */
	public static List<Institute> searchInstitutes(String instituteName, String city, Integer districtId, Integer stateId, List<Integer> instituteStatusIds) throws AViewException
	{
		Session session = null;
		List<Institute> institutes= null;
		try {
				session = HibernateUtils.getHibernateConnection();
				String hqlQueryString = "SELECT inst FROM Institute inst where inst.statusId in (#) ";
				hqlQueryString = hqlQueryString.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(instituteStatusIds));
				if(instituteName != null && instituteName.trim() != "")
				{
					hqlQueryString += "and inst.instituteName like :instituteName ";
				}
				if(city != null && city.trim() != "")
				{
					hqlQueryString += "and inst.city like :city ";
				}
				if( (districtId != null) && (districtId != 0))
				{
					hqlQueryString += "and inst.districtId = :districtId ";
				}
				
				if(((districtId == null) || (districtId == 0)) && ((stateId != null) && (stateId != 0)))
				{
					hqlQueryString += "and inst.districtId IN (Select d.districtId from District d where d.stateId = :stateId)";
				}
				logger.info("Query for searchInstitute :"+hqlQueryString);
				Query hqlQuery = session.createQuery(hqlQueryString);
				if(instituteName != null && instituteName.trim() != "")
				{
					hqlQuery.setString("instituteName", '%'+ instituteName + '%');
				}
				if(city != null && city.trim() != "")
				{
					hqlQuery.setString("city", '%'+ city + '%');
				}
				if( (districtId != null) && (districtId != 0))
				{
					hqlQuery.setInteger("districtId", districtId);
				}
				if(((districtId == null) || (districtId == 0)) && ((stateId != null) && (stateId != 0)))
				{
					hqlQuery.setInteger("stateId", stateId);
				}
				institutes = hqlQuery.list();
				if(institutes.size()>0)
				{
					logger.warn("Retured Institutes");
				}
				else if(institutes.size() == 0)
				{
					logger.warn("Warning :: No Institute ");
				}
		}catch (HibernateException he) {
			processException(he);	
		}finally {
			HibernateUtils.closeConnection(session);
		}
		return institutes;
	}
	
	/**
	 * Gets the institutes.
	 *
	 * @param upto1000InstituteIds the upto1000 institute ids
	 * @return the institutes
	 * @throws AViewException
	 */
	public static List<Institute> getInstitutes(List<Long> upto1000InstituteIds) throws AViewException{
		Session session = null;
		List<Institute> institutes = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			String query = "select institute from Institute institute where instituteId in(#)";
			query = query.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(upto1000InstituteIds));
			Query hqlQuery = session.createQuery(query);
			institutes = hqlQuery.list();
		} catch (HibernateException he) {
			processException(he);	
		} finally {
			HibernateUtils.closeConnection(session);
		}
		return institutes;
	}
	
	/**
	 * Update institutes.
	 *
	 * @param institutes the institutes
	 * @throws AViewException
	 */
	public static void updateInstitutes(List<Institute> institutes) throws AViewException
	{
		Session session = null;
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();	
			long count = 0;
			for(Institute institute:institutes)
			{
				session.update(institute);
				count++;
				//Once the batch is full, then send them to db
				if(count % HibernateUtils.HIBERNATE_BATCH_SIZE == 0)
				{
					session.flush();
					session.clear();
				}
			}
			session.getTransaction().commit();
		} 
		catch (HibernateException he) 
		{
			processException(he);	
			session.getTransaction().rollback();
		}
		finally 
		{
			HibernateUtils.closeConnection(session);
		}
	}
	
	/**
	 * Gets the non deleted institutes by parent institute id.
	 *
	 * @param parentInstituteId the parent institute id
	 * @param deletedStatusId the deleted status id
	 * @return the non deleted institutes by parent institute id
	 * @throws AViewException
	 */
	public static List<Institute> getNonDeletedInstitutesByParentInstituteId(Long parentInstituteId, Integer deletedStatusId) throws AViewException
	{		
		Session session = HibernateUtils.getCurrentHibernateConnection();
		List<Institute> institutes = null;
		try 
		{
			String query = "select inst from Institute inst, Institute pinst where " +
						   "inst.parentInstituteId = pinst.instituteId and " +
						   "inst.statusId != :deletedStatusId and " +
						   "pinst.instituteId = :parentInstituteId ";
			Query hqlQuery = session.createQuery(query);
			hqlQuery.setLong("parentInstituteId", parentInstituteId);
			hqlQuery.setInteger("deletedStatusId", deletedStatusId);
			institutes = hqlQuery.list();
		}
		catch (HibernateException he) 
		{
			processException(he);	
		}
		return institutes;
	}
	
	/**
	 * Delete institutes.
	 *
	 * @param institutes the institutes
	 * @throws HibernateException the hibernate exception
	 */
	public static void deleteInstitutes(List<Institute> institutes) throws HibernateException
	{		
		Session session = HibernateUtils.getCurrentHibernateConnection();
		for(int i = 0; i < institutes.size(); i++)
		{			
			Institute inst = institutes.get(i);
			session.saveOrUpdate(inst);
			//Once the batch is full, then send them to db
			if((i + 1) % HibernateUtils.HIBERNATE_BATCH_SIZE == 0)
			{
				session.flush();
				session.clear();
			}
		}
	}
}
