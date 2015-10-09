/**
 * 
 */
package edu.amrita.aview.gclm.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;
import edu.amrita.aview.common.entities.District;
import edu.amrita.aview.common.entities.State;
import edu.amrita.aview.common.helpers.DistrictHelper;
import edu.amrita.aview.common.helpers.EmailHelper;
import edu.amrita.aview.common.helpers.StateHelper;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.AppenderUtils;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.common.utils.ListUtils;
import edu.amrita.aview.common.utils.OutErrLogger;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.gclm.daos.InstituteDAO;
import edu.amrita.aview.gclm.entities.Institute;
import edu.amrita.aview.gclm.entities.InstituteAdminUser;
import edu.amrita.aview.gclm.entities.InstituteBranding;
import edu.amrita.aview.gclm.entities.InstituteCategory;
import edu.amrita.aview.gclm.entities.InstituteServer;
import edu.amrita.aview.gclm.entities.User;
import edu.amrita.aview.meeting.helpers.MeetingManagerHelper;

import com.sun.jersey.api.client.ClientResponse.Status;
@Controller
/**
 * The Class InstituteHelper.
 *
 * @author
 */
public class InstituteHelper extends Auditable {

	//Cache code
	/** The institutes map. */
	private static Map<Long,Institute> institutesMap = Collections.synchronizedMap(new HashMap<Long,Institute>());
	
	/** The Constant CACHE_CODE. */
	private static final String CACHE_CODE = "InstituteHelper";
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(InstituteHelper.class);
	
	static
	{
		OutErrLogger.setErrToLog();
	}
	
	/**
	 * Populate cache.
	 *
	 * @param institutesIdMap the institutes id map
	 */
	private static synchronized void populateCache(Map<Long,Institute> institutesIdMap)
	{
		institutesMap.clear();
		institutesMap.putAll(institutesIdMap);
		CacheHelper.setCache(CACHE_CODE);
	}
	
	/**
	 * Adds the item to cache.
	 *
	 * @param institute the institute
	 */
	private static synchronized void addItemToCache(Institute institute)
	{
		institutesMap.put(institute.getInstituteId(), institute);
	}
	
	/**
	 * Removes the item from cache.
	 *
	 * @param institute the institute
	 */
	private static synchronized void removeItemFromCache(Institute institute)
	{
		institutesMap.remove(institute.getInstituteId());
	}

	
	/**
	 * Gets the institutes details id map.
	 *
	 * @return the institutes details id map
	 * @throws AViewException
	 */
	public static synchronized Map<Long,Institute> getInstitutesDetailsIdMap() throws AViewException 
	{
		//If cache is expired or invalidated
		if(!CacheHelper.isCacheValid(CACHE_CODE))
		{
			logger.debug("Coming inside invalidate cache");
			List<Institute> institutes = InstituteDAO.getAllInstitutes(StatusHelper.getActiveStatusId());	
			
			//Populate the Map
			Map<Long,Institute> institutesIdMap = new HashMap<Long,Institute>();
			for(Institute institute:institutes)
			{
				institutesIdMap.put(institute.getInstituteId(), institute);
			}
			
			//Warning. populateNames call should be at the end of this method. Otherwise it will go into infinite loop
			populateCache(institutesIdMap);
			//populateNames(institutes,institutesIdMap);
			populateParentRefernce(institutes,institutesIdMap);
			//PopulateNames
		}
		return institutesMap;
	}
	
	/**
	 * Populate parent refernce.
	 *
	 * @param institutes the institutes
	 * @param institutesIdMap the institutes id map
	 * @throws AViewException
	 */
	private static void populateParentRefernce(List<Institute> institutes,Map<Long,Institute> institutesIdMap) throws AViewException 
	{
		for(Institute institute:institutes)
		{
			Long parentId = institute.getParentInstituteId();
			String parentInstituteName = null;
			if(parentId != null && parentId != 0)
			{
				Institute parentInstitue = institutesIdMap.get(parentId);
				if(parentInstitue != null)
				{
					parentInstituteName = parentInstitue.getInstituteName();
				}
				
			}				
			institute.setParentInstituteName(parentInstituteName);
		}
	}
	
	/**
	 * Gets the institutes id map.
	 *
	 * @return the institutes id map
	 * @throws AViewException
	 */
	public static synchronized Map<Long,Institute> getInstitutesIdMap() throws AViewException 
	{
		//If cache is expired or invalidated
		if(!CacheHelper.isCacheValid(CACHE_CODE))
		{
			logger.debug("Coming inside invalidate cache");
			List<Institute> institutes = InstituteDAO.getAllInstitutes(StatusHelper.getActiveStatusId());	
			
			//Populate the Map
			Map<Long,Institute> institutesIdMap = new HashMap<Long,Institute>();
			for(Institute institute:institutes)
			{
				institutesIdMap.put(institute.getInstituteId(), institute);
			}
			
			//Warning. populateNames call should be at the end of this method. Otherwise it will go into infinite loop
			populateCache(institutesIdMap);
			populateNames(institutes,institutesIdMap);
			//PopulateNames
		}
		return institutesMap;
	}
	
	/**
	 * Gets the institutes data id map.
	 *
	 * @param instName the inst name
	 * @param cityName the city name
	 * @param pInstName the p inst name
	 * @return the institutes data id map
	 * @throws AViewException
	 */
	public static synchronized Map<Long,Institute> getInstitutesDataIdMap(String instName,String cityName,String pInstName) throws AViewException 
	{
		//If cache is expired or invalidated
		if(!CacheHelper.isCacheValid(CACHE_CODE))
		{
			logger.debug("Coming inside invalidate cache");
			List<Institute> institutes = InstituteDAO.getAllInstitutesData(StatusHelper.getActiveStatusId(), instName, cityName, pInstName);	
			
			//Populate the Map
			Map<Long,Institute> institutesIdMap = new HashMap<Long,Institute>();
			for(Institute institute:institutes)
			{
				institutesIdMap.put(institute.getInstituteId(), institute);
			}
			
			//Warning. populateNames call should be at the end of this method. Otherwise it will go into infinite loop
			populateCache(institutesIdMap);
			populateNamesData(institutes,institutesIdMap);
		}
		return institutesMap;
	}
	
	/**
	 * Populate names.
	 *
	 * @param institutes the institutes
	 * @param institutesIdMap the institutes id map
	 * @throws AViewException
	 */
	private static void populateNames(List<Institute> institutes,Map<Long,Institute> institutesIdMap) throws AViewException 
	{
		for(Institute institute:institutes)
		{
			Long parentId = institute.getParentInstituteId();
			String parentInstituteName = null;
			if(parentId != null && parentId != 0)
			{
				Institute parentInstitue = institutesIdMap.get(parentId);
				if(parentInstitue != null)
				{
					parentInstituteName = parentInstitue.getInstituteName();
				}
			}				
			populateNames(institute,parentInstituteName);
		}
	}
	
	/**
	 * Populate names data.
	 *
	 * @param institutes the institutes
	 * @param institutesIdMap the institutes id map
	 * @throws AViewException
	 */
	private static void populateNamesData(List<Institute> institutes,Map<Long,Institute> institutesIdMap) throws AViewException 
	{
		for(Institute institute:institutes)
		{
			Long parentId = institute.getParentInstituteId();
			String parentInstituteName = null;
			if(parentId != null && parentId != 0)
			{
				Institute parentInstitue = getInstituteById(parentId);
				if(parentInstitue != null)
				{
					parentInstituteName = parentInstitue.getInstituteName();
				}
			}				
			populateNames(institute,parentInstituteName);
		}
	}
	
//	private static void populateNamesData(List<Institute> institutes,Map<Long,Institute> institutesIdMap, String pInstName) throws AViewException 
//	{
//		List<Institute> tempList=new ArrayList<Institute>();
//		for(Institute institute:institutes)
//		{
//			Long parentId = institute.getParentInstituteId();
//			String parentInstituteName = null;
//			if(parentId != null && parentId != 0)
//			{
//				Institute parentInstitue = institutesIdMap.get(parentId);
//				if(parentInstitue != null)
//				{
//					parentInstituteName = parentInstitue.getInstituteName();
//				}
//			}	
//			if(pInstName.isEmpty() || (parentInstituteName!=null && parentInstituteName.toLowerCase().contains(pInstName.toLowerCase())))
//			{
//				populateNames(institute,parentInstituteName);
//			}
//			else if(!pInstName.isEmpty() && (parentInstituteName == null || parentInstituteName.toLowerCase().contains(pInstName.toLowerCase())))
//			{
//				institutesIdMap.remove(institute.getInstituteId());
//			}
//		}
//		//institutes.removeAll(tempList);
//	}
	
	/**
   * Populate names.
   *
   * @param institute the institute
   * @param parentInstituteName the parent institute name
   * @throws AViewException
   */
	public static void populateNames(Institute institute,String parentInstituteName) throws AViewException
	{
		Integer districtId = institute.getDistrictId();
		if(districtId != null && districtId != 0)
		{
			District district = DistrictHelper.getDistrict(districtId);
			institute.setDistrictName(district.getDistrictName());
			
			State state = StateHelper.getState(district.getStateId());
			institute.setStateName(state.getStateName());
		}
		institute.setParentInstituteName(parentInstituteName);
		
		//Populate the Admin User's fk names
		Set<InstituteAdminUser> admins = institute.getinstituteAdminUsers();
		if(admins != null)
		{
			for(InstituteAdminUser admin:admins)
			{
				UserHelper.populateFKNames(admin.getUser());
			}
		}
	}
	
	/**
	 * Update parent insitute name.
	 *
	 * @param parentInsitute the parent insitute
	 * @throws AViewException
	 */
	private static void updateParentInsituteName(Institute parentInsitute) throws AViewException
	{
		Long parentInstituteId = parentInsitute.getInstituteId();
		logger.info("updateParentInsituteName parentInsitute id:"+parentInstituteId);
		List<Institute> institutes = getAllInstitutes(); //active classs for active crs
		for(Institute institute:institutes)
		{
			if(institute.getParentInstituteId() != null && institute.getParentInstituteId().equals(parentInstituteId))
			{
				logger.info("Updating insitute  '"+institute.getInstituteName()+"'s parentInstituteName");
				institute.setParentInstituteName(parentInsitute.getInstituteName());
			}
		}
	}

	/**
	 * Prints the institute.
	 *
	 * @param institute the institute
	 */
	private static void printInstitute(Institute institute)
	{
		logger.debug("Institute:"+institute.toString());
		
		if(institute.getinstituteAdminUsers() != null)
		{
			logger.debug("InstituteAdminUsers:"+institute.getinstituteAdminUsers().size());
			for(InstituteAdminUser iau:institute.getinstituteAdminUsers())
			{
				logger.debug("InstituteAdminUser:"+iau.toString());
			}
		}

		if(institute.getInstituteServers() != null)
		{
			logger.debug("InstituteServers:"+institute.getInstituteServers().size());
			for(InstituteServer is:institute.getInstituteServers())
			{
				logger.debug("InstituteServer:"+is.toString());
			}
		}

		if(institute.getInstituteBrandings() != null)
		{
			logger.debug("InstituteBrandings:"+institute.getInstituteBrandings().size());
			for(InstituteBranding ib:institute.getInstituteBrandings())
			{
				logger.debug("InstituteBranding:"+ib.toString());
			}
		}
	}
	
	/**
	 * parentInstituteId is a nullable FK. So we should either store it as null or a valid value
	 * In Flex side, we can't assign null to a Number. So we are assigning to 0.
	 * So on the java side, we are converting the 0 back to Null
	 *
	 * @param institute the to nulls on zeros
	 */
	private static void setToNullsOnZeros(Institute institute)
	{
		if(institute.getParentInstituteId() == 0)
		{
			institute.setParentInstituteId(null);
		}
	}

	/**
	 * To create an Institute.
	 *
	 * @param institute the institute
	 * @param creatorId the creator id
	 * @return the institute
	 * @throws AViewException
	 */	
	public static Institute createInstitute(Institute institute,Long creatorId)  throws AViewException {
		setToNullsOnZeros(institute);
		institute.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
		printInstitute(institute);
		InstituteDAO.createInstitute(institute);
		
		if(institute.getInstituteId() != null && institute.getInstituteId() != 0)
		{
			populateNames(institute,getParentInstituteName(institute.getParentInstituteId()));
			addItemToCache(institute);
		}
		logger.debug("Exited createInstitute without throwing any exception:");
		return institute;
	}
	
	/**
	 * To update an Institute.
	 *
	 * @param updatedInstitute the updated institute
	 * @param updaterId the updater id
	 * @return the institute
	 * @throws AViewException
	 */
		
	public static Institute updateInstitute(Institute updatedInstitute,Long updaterId)  throws AViewException {
		Institute institute = getInstituteById(updatedInstitute.getInstituteId());
		
		if(institute != null)
		{
			boolean instituteServerChanged = didInstituteMeetingServersChange(institute,updatedInstitute);
			if(instituteServerChanged)
			{
				logger.info("updateInstitute: Institute server changed for the institute :"
						+updatedInstitute.getInstituteId()+"-"+updatedInstitute.getInstituteName());
			}
			boolean nameChanged = !institute.getInstituteName().equals(updatedInstitute.getInstituteName());
			
			printInstitute(institute);
			institute.updateFrom(updatedInstitute);
			institute.setModifiedAuditData(updaterId, TimestampUtils.getCurrentTimestamp());
			printInstitute(institute);
			setToNullsOnZeros(institute);

			Session session = null;	
			try {
				session = HibernateUtils.getCurrentHibernateConnection();
				session.beginTransaction();

				InstituteDAO.updateInstitute(institute);
				
				session.getTransaction().commit();

				populateNames(institute,getParentInstituteName(institute.getParentInstituteId()));
				addItemToCache(institute);
				
				//If institute Name is changed, Then 
				//1. Update the parent insitute name in all the child institutes in cache
				//2. Update the Institute name in the related courses and classes in cache
				
				if(nameChanged)
				{
					updateParentInsituteName(institute);
					CourseHelper.updateInstituteNameInCache(institute);
				}
				
				if(instituteServerChanged)
				{
					logger.debug("updateInstitute: Calling ClassHelper to notify ongoing meetings  :"
							+updatedInstitute.getInstituteId()+"-"+updatedInstitute.getInstituteName());
					ClassHelper.notifyInstituteMeetingServerChange(institute.getInstituteId());
				}
			} catch (HibernateException he) {
				processException(he);
				session.getTransaction().rollback();
			}
			finally {
				HibernateUtils.closeConnection(session);
			}
		}
		else
		{
			throw new AViewException("Institute with id :"+updatedInstitute.getInstituteId()+": is not found");
		}
		logger.debug("Exited updateInstitute without throwing any exception:");
		return institute;
	}
	
	private static Set<InstituteServer> getMeetingServers(Set<InstituteServer> instituteServers)  throws AViewException
	{
		Set<InstituteServer> instituteMeetingServers = new HashSet<InstituteServer>();
		for(InstituteServer is: instituteServers)
		{
			if(MeetingManagerHelper.isMeetingServer(is)){
				instituteMeetingServers.add(is);
			}
		}
		return instituteMeetingServers;
	}

	private static boolean didInstituteMeetingServersChange(Institute oldInstitute,Institute newInstitute)  throws AViewException
	{
		boolean serversChanged = false;

		if(oldInstitute != null && newInstitute != null)
		{
			Set<InstituteServer> oldMeetingSs = getMeetingServers(oldInstitute.getInstituteServers());
			//Fix for Bug #8192 start
			Set<InstituteServer> newMeetingSs = getMeetingServers(newInstitute.getInstituteServers());
			//Fix for Bug #8192 end
			
			if(oldMeetingSs.size() != newMeetingSs.size())
			{
				serversChanged = true;
			}
			else 
			{
				for(InstituteServer oldMeetingS:oldMeetingSs)
				{
					for(InstituteServer newMeetingS:newMeetingSs)
					{
						//Fix for Bug #8192 start
						//Use equals instead of == 
						if( (oldMeetingS.getServerTypeId().equals(newMeetingS.getServerTypeId())))
						{
							//Use equals instead of == 
							if(!(oldMeetingS.getServer().getServerId().equals(newMeetingS.getServer().getServerId())))
							{
								//Fix for Bug #8192 end
								serversChanged = true;
								//Fix for Bug #8192 start.
								//Come out of the for loop after the first detection
								break;
								//Fix for Bug #8192 end
							}
						}
					}
					//Fix for Bug #8192 start
					if(serversChanged)
					{
						//Come out of the for loop after the first detection
						break;
					}
				}
			}
		}
		else
		{
			serversChanged = true;
		}
		return serversChanged;
	}

	/**
	 * Process exception.
	 *
	 * @param he the he
	 * @throws AViewException
	 */
	private static void processException(HibernateException he) throws AViewException
	{
		String exceptionMessage = null ;
		exceptionMessage = he.getMessage();
		if(he.getCause() != null && he.getCause().getMessage() != null)
		{
			exceptionMessage = he.getCause().getMessage();
		}
		else
		{
			exceptionMessage = he.getMessage();
		}
		logger.error(exceptionMessage,he);
		throw (new AViewException(exceptionMessage));
	}
	
	/**
	 * To delete an Institute.
	 *
	 * @param instituteId the institute id
	 * @param modifiedByUserId the modified by user id
	 * @throws AViewException
	 */
	
	public static void deleteInstitute(Long instituteId, Long modifiedByUserId)  throws AViewException {

		Institute institute = getInstituteById(instituteId); 
		if(institute != null)
		{
			Session session = null;	
			try {
				session = HibernateUtils.getCurrentHibernateConnection();
				session.beginTransaction();
				deleteInstitute(institute, modifiedByUserId);
				//delete the child institutes if any for this
				deleteInsitutesbyParentId(institute.getInstituteId(), modifiedByUserId);
				session.getTransaction().commit();
			}
			catch (HibernateException he) 
			{
				processException(he);
				session.getTransaction().rollback();
			}
			finally {
				HibernateUtils.closeConnection(session);
			}
		}
		else
		{
			throw new AViewException("Institute with id :"+instituteId+": is not found");
		}
	}
	
	/**
	 * To activate a deleted Institute.
	 *
	 * @param instituteId the institute id
	 * @param modifiedByUserId the modified by user id
	 * @throws AViewException
	 */
	
	public static void activateInstitute(Long instituteId, Long modifiedByUserId) throws AViewException {

		Institute institute = InstituteDAO.getInstituteById(instituteId);
		if(institute != null)
		{
			institute.setStatusId(StatusHelper.getActiveStatusId());
			updateInstitute(institute,modifiedByUserId);
		}
		else
		{
			throw new AViewException("Institute with id :"+instituteId+": is not found");
		}
	}
	
	/**
	 * To get all Institutes.
	 *
	 * @return the all institutes
	 * @throws AViewException
	 */
	
	public static List<Institute> getAllInstitutes() throws AViewException {
		List<Institute> institutes= new ArrayList<Institute>();
		institutes.addAll(getInstitutesIdMap().values());
		return institutes;
	}
	
	/**
	 * To get all Institutes by filtering.
	 *
	 * @param instName the inst name
	 * @param cityName the city name
	 * @param pInstName the p inst name
	 * @return the all institutes data
	 * @throws AViewException
	 */
	
	public static List<Institute> getAllInstitutesData(String instName,String cityName,String pInstName) throws AViewException {
		List<Institute> institutes= new ArrayList<Institute>();
		institutes.addAll(getInstitutesDataIdMap(instName, cityName, pInstName).values());
		return institutes;
	}
	
	/**
	 * To get Institute id, Institute Name and Parent Institute  details .
	 *
	 * @return the all institutes essential details
	 * @throws AViewException
	 */
	
	public static List<Institute> getAllInstitutesEssentialDetails() throws AViewException {
		List<Institute> institutes= new ArrayList<Institute>();
		institutes.addAll(getInstitutesDetailsIdMap().values());
		return institutes;
	}
	
	/**
	 * To get institute details.
	 *
	 * @param instituteId the institute id
	 * @return the institute by id
	 * @throws AViewException
	 */
	public static Institute getInstituteById(Long instituteId) throws AViewException {
		Institute inst = InstituteDAO.getInstituteById(instituteId);
		if(inst != null)
		{
			populateNames(inst,getParentInstituteName(inst.getParentInstituteId()));
	}
		return inst;
	}
	
	/**
	 * Gets the parent institute name.
	 *
	 * @param parentId the parent id
	 * @return the parent institute name
	 * @throws AViewException
	 */
	public static String getParentInstituteName(Long parentId) throws AViewException {
		String parentName = null;
		if(parentId != null && parentId != 0)
		{
//			logger.debug("getParentInstituteName-Step1:");
			Institute parentInstitue = getInstitutesIdMap().get(parentId);
//			logger.debug("getParentInstituteName-Step2:");
			if(parentInstitue != null)
			{
//				logger.debug("getParentInstituteName-Step3:");
				parentName = parentInstitue.getInstituteName();
//				logger.debug("getParentInstituteName-Step4:");
			}
		}				
		return parentName;
	}
	
	/**
	 * Gets the institute by name.
	 *
	 * @param instituteName the institute name
	 * @return the institute by name
	 * @throws AViewException
	 */
	public static Institute getInstituteByName(String instituteName) throws AViewException 
	{
		Institute inst = InstituteDAO.getInstituteByName(instituteName, StatusHelper.getActiveStatusId());
		if(inst != null)
		{
			populateNames(inst,getParentInstituteName(inst.getParentInstituteId()));
		}
		return inst;
	}
	
	/**
	 * Search institutes by name.
	 *
	 * @param instituteName the institute name
	 * @return the list< institute>
	 * @throws AViewException
	 */
	public static List<Institute> searchInstitutesByName(String instituteName) throws AViewException
	{
		List<Integer> statusIds = new ArrayList<Integer>();
		statusIds.add(StatusHelper.getActiveStatusId());
		return searchInstitutes(instituteName, null, null, null, statusIds);
	}
	
	/**
	 * Gets the all institutes for admin.
	 *
	 * @param adminUserId the admin user id
	 * @return the all institutes for admin
	 * @throws AViewException
	 */
	public static List<Institute> getAllInstitutesForAdmin(Long adminUserId) throws AViewException 
	{
		List<Institute> institutes = InstituteDAO.getAllInstitutesForAdmin(adminUserId, StatusHelper.getActiveStatusId()); 
		populateNames(institutes,getInstitutesIdMap());
		return institutes;
	}
	
	/**
	 * Gets the all institutes data for admin.
	 *
	 * @param adminUserId the admin user id
	 * @param instName the inst name
	 * @param cityName the city name
	 * @param pInstName the p inst name
	 * @return the all institutes data for admin
	 * @throws AViewException
	 */
	public static List<Institute> getAllInstitutesDataForAdmin(Long adminUserId,String instName,String cityName,String pInstName) throws AViewException 
	{
		List<Institute> institutes = InstituteDAO.getAllInstitutesDataForAdmin(adminUserId, StatusHelper.getActiveStatusId(), instName, cityName, pInstName); 
		populateNamesData(institutes,getInstitutesIdMap());
		return institutes;
	}
	
	/**
	 * Gets the all institutes essential details for admin.
	 *
	 * @param adminUserId the admin user id
	 * @return the all institutes essential details for admin
	 * @throws AViewException
	 */
	public static List<Institute> getAllInstitutesEssentialDetailsForAdmin(Long adminUserId) throws AViewException 
	{
		List<Institute> institutes = InstituteDAO.getAllInstitutesForAdmin(adminUserId, StatusHelper.getActiveStatusId()); 
		populateParentRefernce(institutes,getInstitutesIdMap());
		return institutes;
	}
	
	/**
	 * Gets the all course offering institutes for admin.
	 *
	 * @param adminUserId the admin user id
	 * @return the all course offering institutes for admin
	 * @throws AViewException
	 */
	public static List<Institute> getAllCourseOfferingInstitutesForAdmin(Long adminUserId) throws AViewException 
	{
		List<Institute> institutes = InstituteDAO.getAllCourseOfferingInstitutesForAdmin(adminUserId, StatusHelper.getActiveStatusId()); 
		populateNames(institutes,getInstitutesIdMap());
		return institutes;
	}
	
	/**
	 * To get all Institutes.
	 *
	 * @return the all course offering institutes
	 * @throws AViewException
	 */
	
	public static List<Institute> getAllCourseOfferingInstitutes() throws AViewException {
		List<Institute> institutes= InstituteDAO.getAllCourseOfferingInstitutes(StatusHelper.getActiveStatusId());
		populateNames(institutes, getInstitutesIdMap());
		return institutes;
	}
	
	/**
	 * To get parent Institute.
	 *
	 * @return the all parent institutes
	 * @throws AViewException
	 */
	public static List<Institute> getAllParentInstitutes() throws AViewException {
		List<Institute> parentInstitutes= new ArrayList<Institute>();
		for(Institute institute:getInstitutesIdMap().values())
		{
			//Parent institute
			if(institute.getParentInstituteId() == null)
			{
				parentInstitutes.add(institute);
			}
		}
		return parentInstitutes;
	}
	
	/**
	 * Search institutes.
	 *
	 * @param instituteName the institute name
	 * @param city the city
	 * @param districtId the district id
	 * @param stateId the state id
	 * @param statusIds the status ids
	 * @return the list< institute>
	 * @throws AViewException
	 */
	public static List<Institute> searchInstitutes(String instituteName, String city, Integer districtId, Integer stateId, List<Integer> statusIds) throws AViewException
	{
		List<Institute> institutes = InstituteDAO.searchInstitutes(instituteName, city, districtId, stateId, statusIds);
		populateNames(institutes,getInstitutesIdMap());
		return institutes;
	}
	
	// For creating institute from website for registration
	/**
	 * Creates the institute for approval.
	 *
	 * @param institute the institute
	 * @return the institute
	 * @throws AViewException
	 */
	public static Institute createInstituteForApproval(Institute institute) throws AViewException
	{
		setToNullsOnZeros(institute);
		// Since more than one user is available with MASTER ADMIN Role, choose the user 
		// by administrator user name
		//List<User> users = UserHelper.getUsersByRole(Constant.MASTER_ADMIN_ROLE);
		User masterAdmin = UserHelper.getUserByUserName(Constant.MASTER_ADMIN_USER_NAME);
		
		//Adding the min and max bandwith for institutes
		//institute.setMinPublishingBandwidthKbps(0);
		institute.setMaxPublishingBandwidthKbps(0);
		institute.setMinPublishingBandwidthKbps(0);
		institute.setCreatedAuditData(masterAdmin.getUserId(), TimestampUtils.getCurrentTimestamp(), StatusHelper.getPendingStatusId());
		InstituteDAO.createInstitute(institute);
		if(institute.getInstituteId() != null && institute.getInstituteId() != 0)
		{
			populateNames(institute,getParentInstituteName(institute.getParentInstituteId()));
			//addItemToCache(institute);
		}
		EmailHelper.sendEmailForNewInstituteRegistration(institute.getInstituteName(), institute.getInstituteType());
		logger.debug("Exited createInstitute without throwing any exception:");
		return institute;		
	}
	
	/**
	 * Search pending approval institutes.
	 *
	 * @param instituteName the institute name
	 * @return the list< institute>
	 * @throws AViewException
	 */
	public static List<Institute> searchPendingApprovalInstitutes(String instituteName) throws AViewException
	{
		List<Integer> statusIds = new ArrayList<Integer>();
		statusIds.add(StatusHelper.getPendingStatusId());
		return searchInstitutes(instituteName, null, null, null, statusIds);
	}
	
	/**
	 * Approve pending institutes.
	 *
	 * @param instituteIds the institute ids
	 * @param modifiedByUserId the modified by user id
	 * @throws AViewException
	 */
	public static void approvePendingInstitutes(List<Long> instituteIds, Long modifiedByUserId) throws AViewException
	{
		List<List> brokenInstituteLists = ListUtils.breakListInto1000s(instituteIds);
		
		Integer pendingStatus = StatusHelper.getPendingStatusId();
		Integer activeStatus = StatusHelper.getActiveStatusId();
		
		List<Institute> institutesToBeApproved = new ArrayList<Institute>();
		
		for(List brokenInstituteIds:brokenInstituteLists)
		{
			List<Institute> institutes = InstituteDAO.getInstitutes(brokenInstituteIds);
			for(Institute institute:institutes)
			{
				if(institute.getStatusId() != pendingStatus) //Only approve pending users
				{
					continue;
				}
				institute.setStatusId(activeStatus);
				institute.setModifiedAuditData(modifiedByUserId, TimestampUtils.getCurrentTimestamp());
				institutesToBeApproved.add(institute);
				addItemToCache(institute);
			}
		}
		InstituteDAO.updateInstitutes(institutesToBeApproved);
	}
	
	/**
	 * Delete approval pending institutes.
	 *
	 * @param instituteIds the institute ids
	 * @param modifiedByUserId the modified by user id
	 * @throws AViewException
	 */
	public static void deleteApprovalPendingInstitutes(List<Long> instituteIds, Long modifiedByUserId) throws AViewException
	{
		Integer pendingStatus = StatusHelper.getPendingStatusId();
		try
		{
			List<Institute> institutes = InstituteDAO.getInstitutes(instituteIds);
			for(Institute institute:institutes)
			{
				if(institute.getStatusId() != pendingStatus) //Only delete pending institutes
				{
					continue;
				}
				else
				{
					deleteInstitute(institute.getInstituteId(), modifiedByUserId);
				}
			}
		}
		catch (HibernateException he) 
		{
			processException(he);
		}				
	}
	
	/**
	 * Sets the institute to delete.
	 *
	 * @param institute the institute
	 * @param modifiedByUserId the modified by user id
	 * @throws AViewException
	 */
	private static void setInstituteToDelete(Institute institute, Long modifiedByUserId) throws AViewException
	{
		institute.setInstituteName(institute.getInstituteName() + AppenderUtils.DeleteAppender());
		institute.setStatusId(StatusHelper.getDeletedStatusId());
		institute.setModifiedAuditData(modifiedByUserId, TimestampUtils.getCurrentTimestamp());
	}
	
	/**
	 * Delete insitutesby parent id.
	 *
	 * @param parentInstituteId the parent institute id
	 * @param modifiedByUserId the modified by user id
	 * @throws AViewException
	 */
	public static void deleteInsitutesbyParentId(Long parentInstituteId, Long modifiedByUserId) throws AViewException
	{
		//To remove all the child institutes and its users.
		List<Institute> institutes = InstituteDAO.getNonDeletedInstitutesByParentInstituteId(parentInstituteId, StatusHelper.getDeletedStatusId());
		if((institutes != null) && (institutes.size() > 0))
		{
			for(Institute inst:institutes)
			{
				deleteInstitute(inst, modifiedByUserId);
			}
		}
	}
	
	/**
	 * Delete institute.
	 *
	 * @param institute the institute
	 * @param modifiedByUserId the modified by user id
	 * @throws AViewException
	 */
	private static void deleteInstitute(Institute institute, Long modifiedByUserId) throws AViewException
	{
		setInstituteToDelete(institute, modifiedByUserId);
		//delete the institute by adding delete appender
		InstituteDAO.updateInstitute(institute);
		
		//delete courses offered by that institute
		//Remove the Course & Class from the cache
		//We do not need to explicitly delete the Classes and Courses from the database as 
		//They are unique with in the Institute and Course respectively
		//Hence a new Course or Class with the same name can be created under another Institute and Course respectively
		//Where as the user is unique across the entire database, hence we need to delete the user explictly in 
		//the database, so that a new user with the same name can be created.
		
		//delete users of that institute
		UserHelper.deleteUsers(institute.getInstituteId(), modifiedByUserId);
		CourseHelper.removeCoursesBasedOnInstitute(institute.getInstituteId());
		removeItemFromCache(institute);
	}
	
	/**
	 * Clear cache.
	 */
	public static void clearCache()
	{
		logger.debug("Entered InstituteHelper.clearCache()");
		institutesMap = null;
		logger.debug("Exit InstituteHelper.clearCache()");
	}

	/**
	 * API to search institute.
	 * @param adminId
	 * @param instituteName
	 * @return Response
	 * @throws AViewException
	 */
	@RequestMapping(value = "/searchinstitute.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response searchInstitute(@RequestParam("adminId") Long adminId,
			@RequestParam("instituteName") String instituteName ) throws AViewException
	{
		logger.debug("Enter institute search::institute search ");
		String errorMessage = null;
		Institute institute = null;
		Object instituteObj = new Object();
		ArrayList instituteDetailsArray = new ArrayList();
		User admin = null;
		Object resultObjectAdmin = UserHelper.userValidCheck(Constant.ADMIN_ID,adminId);
		if(User.class.isInstance(resultObjectAdmin))
		{
			admin = (User)resultObjectAdmin;
		}
		else
		{
			errorMessage = resultObjectAdmin.toString();
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		String trimInstituteName = instituteName.trim();
		if(trimInstituteName.equals(null) || trimInstituteName.equals(""))
		{
			return Response.status(Status.BAD_REQUEST).entity("Please provide valid InstituteName").build();
		}
		List<Institute> instituteDetailList;
		if(admin.getRole().equals(Constant.MASTER_ADMIN_ROLE))
		{
			adminId = null;
			instituteDetailList = searchInstitutesByName(trimInstituteName);
		}
		else
		{
			return Response.status(Status.BAD_REQUEST).entity("Only master admin has the permission to search the institute").build();
		}
		String instituteAddress = "";
		if(instituteDetailList == null || instituteDetailList.size() == 0) 
		{
			return Response.status(Status.BAD_REQUEST).entity("No active institute details returned for the given institutename").build();
		} 
		else 
		{
			ArrayList arrToAddInstituteDetails = new ArrayList();
			String categoryName = null;
			for(Institute inst:instituteDetailList)
			{
				arrToAddInstituteDetails = new ArrayList();
				arrToAddInstituteDetails.add("institute: " + inst.getInstituteName());
				arrToAddInstituteDetails.add("instituteId: " + inst.getInstituteId());
				arrToAddInstituteDetails.add("instituteType: " + inst.getInstituteType());
				InstituteCategory instituteCategory = InstituteCategoryHelper.getInstituteCategory(inst.getInstituteCategoryId());
				arrToAddInstituteDetails.add("category: " + instituteCategory.getInstituteCategoryName());
				instituteAddress = inst.getAddress();
				if(instituteAddress != null)
				{
					instituteAddress = instituteAddress.replaceAll("\r", " ");	
				}
				else
				{
					instituteAddress = "";
				}
				arrToAddInstituteDetails.add("address: " + instituteAddress);
				arrToAddInstituteDetails.add("city: " + inst.getCity());
				arrToAddInstituteDetails.add("district: " + inst.getDistrictName());	
				arrToAddInstituteDetails.add("parentInstitute: " + inst.getParentInstituteName());	
				instituteDetailsArray.add(arrToAddInstituteDetails);
			}
			logger.debug("Exit institute search on success:institute search");
		}
		return Response.status(Status.OK).entity(instituteDetailsArray).build();
	}
}
