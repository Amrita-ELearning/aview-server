/*
 * 
 */
package edu.amrita.aview.gclm.daos;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.common.utils.ListUtils;
import edu.amrita.aview.gclm.entities.ClassRegistration;
import edu.amrita.aview.gclm.entities.User;
import edu.amrita.aview.gclm.entities.Class;


/**
 * The Class ClassRegistrationDAO.
 */
public class ClassRegistrationDAO  extends SuperDAO{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(ClassRegistrationDAO.class);
	
	/**
	 * Gets the active classes register.
	 *
	 * @param activeStatusId the active status id
	 * @return the active classes register
	 * @throws AViewException
	 */
	public static List<ClassRegistration> getActiveClassesRegister(Integer activeStatusId) throws AViewException{
		Session session = null;
		List<ClassRegistration> classesRegister = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			Query hqlQuery = session.createQuery("select clr from ClassRegistration clr,Course cr,Institute ai " +
					"where clr.statusId=:activeStatusId" +
					" and clr.aviewClass.statusId=:activeStatusId" +
					" and clr.user.statusId=:activeStatusId" +
					" and ai.statusId=:activeStatusId" +
					" and cr.statusId=:activeStatusId"
					);
			hqlQuery.setInteger("activeStatusId", activeStatusId);
			classesRegister = hqlQuery.list();
		} catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}

		return classesRegister;
	}
	
	/**
	 * Creates the class registration.
	 *
	 * @param classRegistration the class registration
	 * @return the string
	 * @throws AViewException
	 */
	public static String createClassRegistration(ClassRegistration classRegistration) throws AViewException{
		Session session = null;
		String errorMessage = null ;		
			try {
				session = HibernateUtils.getHibernateConnection();
				session.beginTransaction();
				/*if(classRegistration.getNodeId() == 0)
				{
					classRegistration.setNodeId(null);
				}
				*/
				logger.debug(" moderator :: "+classRegistration.getIsModerator());
				if(classRegistration.getIsModerator().equalsIgnoreCase("Y"))
				{
					if(getModeratorUserForClass(classRegistration.getAviewClass().getClassId(),StatusHelper.getActiveStatusId()) == null)
					{
						session.save(classRegistration);
					}
					else
					{
						errorMessage = "Cannot assign more than one moderator for a class" ;
						throw (new HibernateException(errorMessage));
					}
				}
				else
				{
					session.save(classRegistration);
				}
				//session.save(classRegistration);				
				session.getTransaction().commit();
			} catch (HibernateException he) {
				processException(he);	
				session.getTransaction().rollback() ;
			}
		 finally {
			HibernateUtils.closeConnection(session);
		}
		logger.debug("Successfully inserted Class Registration record");
		return errorMessage ;
	}
	
	/**
	 * Gets the class registration count.
	 *
	 * @param classId the class id
	 * @param statusId the status id
	 * @return the class registration count
	 * @throws AViewException
	 */
	public static Integer getClassRegistrationCount(Long classId,Integer statusId) throws AViewException{
		Session session = null;
		Integer classRegistrationCount = -1;
		try {
			session = HibernateUtils.getHibernateConnection();
			//Fix for Bug#20297
			Query hqlQuery = session.createQuery("select count(aviewUser.classRegisterId) from ClassRegistration aviewUser " +
					"where aviewUser.aviewClass.classId = :classId and aviewUser.statusId=:statusId");
			hqlQuery.setLong("classId",classId);
			hqlQuery.setInteger("statusId", statusId);
			classRegistrationCount = Integer.parseInt(hqlQuery.list().get(0).toString());

		} catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}

		return classRegistrationCount;
	}
	
	/**
	 * Gets the user class registration count.
	 *
	 * @param userId the user id
	 * @param statusId the status id
	 * @return the user class registration count
	 * @throws AViewException
	 */
	public static Integer getUserClassRegistrationCount(Long userId,Integer statusId) throws AViewException{
		Session session = null;
		Integer userCount = -1;
		try {
			session = HibernateUtils.getHibernateConnection();
			Query hqlQuery = session.createQuery("select count(aviewUser.classRegisterId) from ClassRegistration aviewUser " +
					"where aviewUser.user.userId = :userId");
			hqlQuery.setLong("userId",userId);
			userCount = Integer.parseInt(hqlQuery.list().get(0).toString());

		} catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}

		return userCount;
	}
	
	/**
	 * Update class registration.
	 *
	 * @param classRegistration the class registration
	 * @return the string
	 * @throws AViewException
	 */
	public static String updateClassRegistration(ClassRegistration classRegistration) throws AViewException{
		
		Session session = null;
		String creationMessage = null ;
		try {
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();		
			logger.debug(" moderator :: "+classRegistration.getIsModerator());
			if(classRegistration.getIsModerator().equalsIgnoreCase("Y"))
			{
				User moderator=getModeratorUserForClass(classRegistration.getAviewClass().getClassId(),StatusHelper.getActiveStatusId());
				long userId =0;
				if(moderator!=null)
				{
					userId= moderator.getUserId().longValue();
				}
				// Fix Bug #1816 start
				// Check while editing a registered class ,it exists or not
				logger.debug("Long user id :: Moderator Yes :: "+userId);
				if(userId == 0 || userId == classRegistration.getUser().getUserId())
				//Fix Bug #1816 end	
				{
					logger.debug("In updating class registration ... ") ;
					session.update(classRegistration);
				}
				else if (userId != classRegistration.getUser().getUserId())
				{
					creationMessage = "Cannot assign more than one moderator for a class" ;
					throw (new HibernateException(creationMessage));
				}
				//If the moder is already the current user, then do nothing.
			}
			else
			{
				session.update(classRegistration);
			}
						
			session.getTransaction().commit();
		} catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		} finally {
			HibernateUtils.closeConnection(session);
		}
		
		return creationMessage ;
	}
	
	/**
	 * Delete class register.
	 *
	 * @param classRegisterId the class register id
	 * @throws AViewException
	 */
	public static void deleteClassRegister(Long classRegisterId) throws AViewException
	{		
		Session session = null;
		ClassRegistration classRegister = getClassRegister(classRegisterId) ;
		try {
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.delete(classRegister);
			session.getTransaction().commit();
			
		} catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		} finally {
			HibernateUtils.closeConnection(session);
		}
	}
	// No need of checking status id , since the registered class has to be deleted
	/**
	 * Gets the class register.
	 *
	 * @param classRegisterId the class register id
	 * @return the class register
	 * @throws AViewException
	 */
	public static ClassRegistration getClassRegister(Long classRegisterId) throws AViewException{
		Session session = null;
		ClassRegistration classRegister = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			classRegister = (ClassRegistration)session.get(ClassRegistration.class, classRegisterId);

		} catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}
		return classRegister;
	}
	
	/**
	 * Gets the class registers for master admin approval.
	 *
	 * @param instituteId the institute id
	 * @param pendingStatusId the pending status id
	 * @param activeStatusId the active status id
	 * @param classStatusesId the class statuses id
	 * @return the class registers for master admin approval
	 * @throws AViewException
	 */
	public static List<ClassRegistration> getClassRegistersForMasterAdminApproval(Long instituteId, Integer pendingStatusId, Integer activeStatusId, List<Integer> classStatusesId) throws AViewException
	{
		// Bug fix for retrieving the class registration approvals for 
		// the classes which are closed for registration
		Session session = null ;
		List<ClassRegistration> classesRegistered = null ;
		try{
			session = HibernateUtils.getHibernateConnection() ;			
			String hqlQueryString = "SELECT clr" +
									" FROM ClassRegistration clr,Course cr,Institute ai" +
									" WHERE clr.aviewClass.courseId = cr.courseId " +
									" and cr.instituteId = ai.instituteId" +
									" and clr.statusId=:pendingStatusId" +
									" and clr.aviewClass.statusId IN (#) " +
									" and clr.user.statusId=:activeStatusId" +
									" and ai.statusId=:activeStatusId" +
									" and cr.statusId=:activeStatusId";
			
			hqlQueryString = hqlQueryString.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(classStatusesId));
			if((instituteId != null) && (instituteId != 0))
			{
				hqlQueryString += " and ai.instituteId=:instituteId";
			}

			Query hqlQuery = session.createQuery(hqlQueryString);
			
			hqlQuery.setInteger("pendingStatusId", pendingStatusId);
			hqlQuery.setInteger("activeStatusId", activeStatusId);
			if((instituteId != null) && (instituteId != 0))
			{
				hqlQuery.setLong("instituteId", instituteId);
			}
			classesRegistered = hqlQuery.list() ;
			logger.debug(" Search Result Size :: "+classesRegistered.size()) ;
		}catch(HibernateException he)
		{
			processException(he);	
		}finally{
			HibernateUtils.closeConnection(session);
		}
		
		return classesRegistered ;
	}
	
	/**
	 * Gets the class registers for institute admin approval.
	 *
	 * @param instituteId the institute id
	 * @param instAdminId the inst admin id
	 * @param pendingStatusId the pending status id
	 * @param activeStatusId the active status id
	 * @param classStatusesId the class statuses id
	 * @return the class registers for institute admin approval
	 * @throws AViewException
	 */
	public static List<ClassRegistration> getClassRegistersForInstituteAdminApproval(Long instituteId, Long instAdminId,Integer pendingStatusId,Integer activeStatusId, List<Integer> classStatusesId) throws AViewException
	{
		// Bug fix for retrieving the class registration approvals for 
		//the classes which are closed for registration
		Session session = null ;
		List<ClassRegistration> classesRegistered = null ;
		try{
			session = HibernateUtils.getHibernateConnection() ;			
			String hqlQueryString = "SELECT distinct clr" +
									" FROM ClassRegistration clr,Course cr,Institute ai,InstituteAdminUser iau" +
									" WHERE (iau.institute.instituteId = ai.instituteId or iau.institute.instituteId = ai.parentInstituteId) " +
									" and clr.aviewClass.courseId = cr.courseId " +
									" and cr.instituteId = ai.instituteId" +
									" and iau.user.userId = :instAdminId" +
									" and clr.statusId=:pendingStatusId" +
									" and clr.aviewClass.statusId IN (#) " +
									" and clr.user.statusId=:activeStatusId" +
									" and ai.statusId=:activeStatusId" +
									" and cr.statusId=:activeStatusId";
			
			hqlQueryString = hqlQueryString.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(classStatusesId));
			
			if((instituteId != null) && (instituteId != 0))
			{
				hqlQueryString += " and ai.instituteId=:instituteId";
			}
			Query hqlQuery = session.createQuery(hqlQueryString);
			
			hqlQuery.setLong("instAdminId", instAdminId);
			hqlQuery.setInteger("pendingStatusId", pendingStatusId);
			hqlQuery.setInteger("activeStatusId", activeStatusId);
			if((instituteId != null) && (instituteId != 0))
			{
				hqlQuery.setLong("instituteId", instituteId);
			}		
			classesRegistered = hqlQuery.list() ;
			logger.debug(" Search Result Size :: "+classesRegistered.size()) ;
		}catch(HibernateException he)
		{
			processException(he);	
		}finally{
			HibernateUtils.closeConnection(session);
		}
		
		return classesRegistered ;
	}
	
	/**
	 * Gets the class registrations.
	 *
	 * @param upto1000classRegisterIds the upto1000class register ids
	 * @return the class registrations
	 * @throws AViewException
	 */
	public static List<ClassRegistration> getClassRegistrations(List<Long> upto1000classRegisterIds) throws AViewException{
		Session session = null;
		List<ClassRegistration> classRegistrations = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			String query = "select clr from ClassRegistration clr where clr.classRegisterId in(#)";
			query = query.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(upto1000classRegisterIds));
			Query hqlQuery = session.createQuery(query);
			classRegistrations = hqlQuery.list();
		} catch (HibernateException he) {
			processException(he);	
		} finally {
			HibernateUtils.closeConnection(session);
		}
		return classRegistrations;
	}
	
	/**
	 * Update class registrations.
	 *
	 * @param classRegistrations the class registrations
	 * @throws AViewException
	 */
	public static void updateClassRegistrations(List<ClassRegistration> classRegistrations) throws AViewException{
		Session session = null;
		String creationMessage = null ;		
		try {
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();	
			
			long count = 0;
			for(ClassRegistration classRegistration:classRegistrations)
			{
				session.update(classRegistration);
				count++;
				//Once the batch is full, then send them to db
				if(count % HibernateUtils.HIBERNATE_BATCH_SIZE == 0)
				{
					session.flush();
					session.clear();
				}
			}
			session.getTransaction().commit();
		} catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		} finally {
			HibernateUtils.closeConnection(session);
		}
	}
	
	/**
	 * Search for class register.
	 *
	 * @param userName the user name
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param classId the class id
	 * @param moderator the moderator
	 * @param courseId the course id
	 * @param instituteId the institute id
	 * @param adminId the admin id
	 * @param statusId the status id
	 * @param clrStatusIds the clr status ids
	 * @param classStatusIds the class status ids
	 * @return the list
	 * @throws AViewException
	 */
	public static List<ClassRegistration> searchForClassRegister(
			String userName,String firstName,String lastName,Long classId ,String moderator,
			Long courseId,Long instituteId,Long adminId,Integer statusId, List<Integer> clrStatusIds,
			List<Integer> classStatusIds) throws AViewException
	{
		Session session = null ;
		List<ClassRegistration> classesRegistered = null ;
		try{
			session = HibernateUtils.getHibernateConnection() ;			
			String hqlQueryString = "SELECT clr" +
									" FROM ClassRegistration clr,Institute inst ,Course co" +
									" WHERE clr.aviewClass.courseId = co.courseId" +
									" and co.instituteId = inst.instituteId" +
									" and clr.statusId in (~)" +
									" and clr.aviewClass.statusId in (#)" +
									" and clr.user.statusId=:statusId" +
									" and inst.statusId=:statusId" +
									" and co.statusId=:statusId";
			
			hqlQueryString = hqlQueryString.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(classStatusIds));
			hqlQueryString = hqlQueryString.replaceAll("~", ListUtils.getNumericListAsCommaDelimitedString(clrStatusIds));
			
			if(adminId != null && adminId > 0)
			{
				hqlQueryString += 
					" AND inst.instituteId IN " +
						"(SELECT distinct i.instituteId " +
						"FROM InstituteAdminUser iau,Institute i " +
						"WHERE iau.user.userId = :adminId " +
						"AND (iau.institute.instituteId = i.instituteId " +
							"OR iau.institute.instituteId = i.parentInstituteId))" ;
			}

			if(userName != null)
			{
				hqlQueryString += " and clr.user.userName like :userName" ;
			}
			
			if(firstName != null)
			{
				hqlQueryString += " and clr.user.fname like :fname" ;
			}
			
			if(lastName != null)
			{
				hqlQueryString += " and clr.user.lname like :lname" ;
			}
			
			if(classId != null && classId > 0)
			{
				hqlQueryString += " and clr.aviewClass.classId=:classId" ;
			}
			
			if(courseId != null && courseId > 0)
			{
				hqlQueryString += " and co.courseId=:courseId" ;
			}
			
			if(instituteId != null && instituteId > 0)
			{
				hqlQueryString += " and inst.instituteId=:instituteId" ;
			}
			
			if(moderator != null)
			{
				hqlQueryString += " and clr.isModerator=:moderator" ;
			}
			Query hqlQuery = session.createQuery(hqlQueryString);
			
			if(adminId != null && adminId > 0)
			{
				hqlQuery.setLong("adminId", adminId);
			}

			if(userName != null)
			{
				hqlQuery.setString("userName", '%'+userName+'%');
			}
			
			if(firstName != null)
			{
				hqlQuery.setString("fname", '%'+firstName+'%');
			}
			
			if(lastName != null)
			{
				hqlQuery.setString("lname", '%'+lastName+'%');
			}
			
			if(classId != null && classId > 0)
			{
				hqlQuery.setLong("classId", classId);
			}
			
			if(courseId != null && courseId > 0)
			{
				hqlQuery.setLong("courseId", courseId);
			}
			
			if(instituteId != null && instituteId > 0)
			{
				hqlQuery.setLong("instituteId", instituteId);
			}
			
			if(moderator != null)
			{
				hqlQuery.setString("moderator", moderator);
			}
			
			hqlQuery.setInteger("statusId", statusId);
			
			classesRegistered = hqlQuery.list() ;
			logger.debug(" Search Result Size :: "+classesRegistered.size()) ;
		}catch(HibernateException he)
		{
			processException(he);	
		}finally{
			HibernateUtils.closeConnection(session);
		}
		
		return classesRegistered ;
	}
	
	/**
	 * Search for class register for user.
	 *
	 * @param userId the user id
	 * @param classId the class id
	 * @param moderator the moderator
	 * @param courseId the course id
	 * @param instituteId the institute id
	 * @param statusId the status id
	 * @param clrStatusIds the clr status ids
	 * @param classStatusIds the class status ids
	 * @return the list
	 * @throws AViewException
	 */
	public static List<ClassRegistration> searchForClassRegisterForUser(
			Long userId,Long classId ,String moderator, String classType,
			Long courseId,Long instituteId,Integer statusId, List<Integer> clrStatusIds, List<Integer> classStatusIds) throws AViewException
	{
		Session session = null ;
		List<ClassRegistration> classesRegistered = null ;
		try{
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "SELECT clr" +
									" FROM ClassRegistration clr,Institute inst ,Course co" +
									" WHERE clr.aviewClass.courseId = co.courseId" +
									" and co.instituteId = inst.instituteId" +
									" and clr.statusId IN (~)" +
									" and clr.aviewClass.statusId IN (#)" +
									" and clr.user.statusId=:statusId" +
									" and inst.statusId=:statusId" +
									" and co.statusId=:statusId";
			
			hqlQueryString = hqlQueryString.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(classStatusIds));
			hqlQueryString = hqlQueryString.replaceAll("~", ListUtils.getNumericListAsCommaDelimitedString(clrStatusIds));
			
			if(userId != null && userId > 0)
			{
				hqlQueryString += " and clr.user.userId=:userId" ;
			}
			
			if(classId != null && classId > 0)
			{
				hqlQueryString += " and clr.aviewClass.classId=:classId" ;
			}
			
			if(courseId != null && courseId > 0)
			{
				hqlQueryString += " and co.courseId=:courseId" ;
			}
			
			if(instituteId != null && instituteId > 0)
			{
				hqlQueryString += " and inst.instituteId=:instituteId" ;
			}
			
			if(moderator != null)
			{
				hqlQueryString += " and clr.isModerator=:moderator" ;
			}
			if(classType != null)
			{
				hqlQueryString += " and clr.aviewClass.classType = :classType" ;
			}
 			Query hqlQuery = session.createQuery(hqlQueryString);
			
			if(userId != null && userId > 0)
			{
				hqlQuery.setLong("userId", userId);
			}
			
			if(classId != null && classId > 0)
			{
				hqlQuery.setLong("classId", classId);
			}
			
			if(courseId != null && courseId > 0)
			{
				hqlQuery.setLong("courseId", courseId);
			}
			
			if(instituteId != null && instituteId > 0)
			{
				hqlQuery.setLong("instituteId", instituteId);
			}
			
			if(moderator != null)
			{
				hqlQuery.setString("moderator", moderator);
			}
			if(classType != null)
			{
				hqlQuery.setString("classType", classType);
			}
			hqlQuery.setInteger("statusId", statusId);
			
			classesRegistered = hqlQuery.list() ;
			logger.debug(" Search Result Size :: "+classesRegistered.size()) ;
		}catch(HibernateException he)
		{
			processException(he);	
		}finally{
			HibernateUtils.closeConnection(session);
		}
		
		return classesRegistered ;
	}
	
	public static List<ClassRegistration> getAllRegisteredClassesForUser(User user,Integer activeStatusId, List<Integer> classStatusIds, List<Integer> classRegStatusIds) throws AViewException{
		Session session = null;
		List<ClassRegistration> registrations = new ArrayList<ClassRegistration>();
		try {
			session = HibernateUtils.getHibernateConnection();
			String query = 
				"select clr " +
				"from ClassRegistration clr,Course co,Institute i " +
				"where clr.aviewClass.courseId = co.courseId " +
				"and co.instituteId = i.instituteId " +
				"and clr.user.userId = :userId " +
				"and clr.statusId IN (~) " +
				"and clr.aviewClass.statusId IN (#) " +
				"and co.statusId = :activeStatusId " +
				"and i.statusId = :activeStatusId " +
				"order by clr.aviewClass.className";

			query = query.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(classStatusIds));
			query = query.replaceAll("~", ListUtils.getNumericListAsCommaDelimitedString(classRegStatusIds));
			Query q = session.createQuery(query);
			q.setInteger("activeStatusId", activeStatusId);
			
			q.setLong("userId", user.getUserId());

			registrations = q.list();

		} catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}
		return registrations;
	}
	
	public static List<ClassRegistration> getAllRegisteredClassesForAdmin(User user,Integer activeStatusId, List<Integer> classStatusIds, List<Integer> classRegStatusIds) throws AViewException{
		Session session = null;
		List<ClassRegistration> registrations = new ArrayList<ClassRegistration>();
		try {
			session = HibernateUtils.getHibernateConnection();
			
			final String adminQuery = 
				"select cl " +
				"from Class cl,Course co,Institute i " +
				"where cl.courseId = co.courseId " +
				"and co.instituteId = i.instituteId " +
				"and i.instituteId in " +
					"(select distinct inst.instituteId " +
					"from InstituteAdminUser iau,Institute inst " +
					"where iau.user.userId = :adminId " +
					"and (iau.institute.instituteId = inst.instituteId " +
						"or iau.institute.instituteId = inst.parentInstituteId))" +
				"and cl.statusId IN (#) " +
				"and co.statusId = :activeStatusId " +
				"and i.statusId = :activeStatusId "+
				"order by cl.className";

			final String masterAdminQuery = 
				"select cl " +
				"from Class cl,Course co,Institute i " +
				"where cl.courseId = co.courseId " +
				"and co.instituteId = i.instituteId " +
				"and cl.statusId IN (#) " +
				"and co.statusId = :activeStatusId " +
				"and i.statusId = :activeStatusId "+
				"order by cl.className";

			String query = null;
			if(user.getRole().equals(Constant.MASTER_ADMIN_ROLE))
			{
				query = masterAdminQuery;
			}
			else
			{
				query = adminQuery;
			}
			query = query.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(classStatusIds));
			Query q = session.createQuery(query);
			q.setInteger("activeStatusId", activeStatusId);
			
			if(user.getRole().equals(Constant.ADMIN_ROLE))
			{
				q.setLong("adminId", user.getUserId());
			}

			List<Class> classes = q.list();
			for(Class aviewClass:classes)
			{
				registrations.add(ClassRegistration.prepareDummyClassRegistration(aviewClass, user));
			}

		} catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}
		return registrations;
	}
	
	public static List<ClassRegistration> getAllOpenClassesForUser(User user,Integer activeStatusId, List<Integer> classStatusIds, List<Integer> classRegStatusIds) throws AViewException{
		Session session = null;
		List<ClassRegistration> registrations = new ArrayList<ClassRegistration>();
		try {
			session = HibernateUtils.getHibernateConnection();

			String openClassQuery = 
				"select cl " +
				"from Class cl,Course co,Institute i " +
				"where cl.courseId = co.courseId " +
				"and co.instituteId = i.instituteId " +
				"and cl.registrationType in ('OpenWithLogin','Open') " +
				"and cl.statusId IN (#) " +
				"and co.statusId = :activeStatusId " +
				"and i.statusId = :activeStatusId "+
				"order by cl.className";

			openClassQuery = openClassQuery.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(classStatusIds));
			Query q = session.createQuery(openClassQuery);
			q.setInteger("activeStatusId", activeStatusId);

			List<Class> classes = q.list();
			for(Class aviewClass:classes)
			{
				registrations.add(ClassRegistration.prepareDummyClassRegistration(aviewClass, user));
			}

		} catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}
		return registrations;
	}	
	//No need of checking classregister status id , since the entries in class register table is deleted , once a registration is deleted
	//But we need to check the user.statusId
	/**
	 * Gets the moderator user id for class.
	 *
	 * @param classId the class id
	 * @param statusId the status id
	 * @return the moderator user id for class
	 * @throws AViewException
	 */
	public static User getModeratorUserForClass(Long classId,Integer statusId) throws AViewException
	{
		Session session = null ;
		User moderatorUser =null ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			String hqlQueryString = " select clr.user from ClassRegistration clr where" +
									" clr.aviewClass.classId=:classId" +
									" and clr.user.statusId = :statusId" +
									" and clr.isModerator ='Y' ";
			
			Query hqlQuery = session.createQuery(hqlQueryString);
			
			hqlQuery.setLong("classId", classId);	
			hqlQuery.setInteger("statusId", statusId);
			
			List temp = hqlQuery.list();
			if(temp.size() == 1)
			{
				moderatorUser = (User) temp.get(0);
			}
			else if(temp.size() == 0)
			{
				logger.debug(" No count of moderator returned for the given class");
			}
			else if(temp.size() > 1)
			{
				throw new HibernateException(" More than one count of moderator returned for the given class");
			}
			
		}
		catch(HibernateException he)
		{
			processException(he);	
		}
		finally{
			HibernateUtils.closeConnection(session);
		}
		
		return moderatorUser ;
	}

	//Code change for NIC start
	//Get the pending approval class registrations for those classes in which the teacher is the moderator
	/**
	 * Gets the class registers for moderator approval.
	 *
	 * @param instituteId the institute id
	 * @param classModeratorId the class moderator id
	 * @param pendingSId the pending s id
	 * @param activeStatusId the active status id
	 * @param classStatusesId the class statuses id
	 * @return the class registers for moderator approval
	 * @throws AViewException
	 */
	public static List<ClassRegistration> getClassRegistersForModeratorApproval(Long instituteId, Long classModeratorId, Integer pendingSId, Integer activeStatusId, List<Integer> classStatusesId) throws AViewException
	{
		Session session = null ;
		List<ClassRegistration> classesRegistered = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "SELECT clr " +
									"FROM ClassRegistration clr, Class cl, Course co, Institute i, User u WHERE " +
									"cl.courseId = co.courseId AND co.instituteId = i.instituteId AND clr.user.userId = u.userId AND " +
									"clr.statusId = :pendingSId AND " +
									"co.statusId = :activeStatusId AND " +
									"u.statusId = :activeStatusId AND " + 
									"i.statusId = :activeStatusId AND " + 
									"clr.aviewClass.classId = cl.classId AND cl.classId IN (Select cl1.classId from Class cl1, " +
									"ClassRegistration clr1, User u1 where clr1.isModerator = 'Y' AND cl1.classId = clr1.aviewClass.classId AND " +
									"clr1.user.userId = u1.userId AND clr1.statusId = :activeStatusId AND u1.userId = :classModeratorId AND " +
									"cl1.statusId IN (#))"; 
					
			hqlQueryString = hqlQueryString.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(classStatusesId));
			if((instituteId != null) && (instituteId != 0))
			{
				hqlQueryString += " and i.instituteId=:instituteId";
			}
			Query hqlQuery = session.createQuery(hqlQueryString);
			
			if((instituteId != null) && (instituteId != 0))
			{
				hqlQuery.setLong("instituteId", instituteId);
			}
			hqlQuery.setInteger("activeStatusId", activeStatusId);
			hqlQuery.setInteger("pendingSId", pendingSId);
			hqlQuery.setLong("classModeratorId", classModeratorId);
			classesRegistered = hqlQuery.list() ;
			logger.debug(" Search Result Size :: "+classesRegistered.size()) ;
		}catch(HibernateException he)
		{
			processException(he);	
		}
		finally
		{
			HibernateUtils.closeConnection(session);
		}		
		return classesRegistered;
	}
	public static List<ClassRegistration> getClassRegistersForClass(Long classId,Integer statusId)throws AViewException
	{
		Session session = null ;
		List<ClassRegistration> classesRegistrations = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;	
			String hqlQueryString ="SELECT clr FROM ClassRegistration clr where clr.aviewClass.classId=:classId "+
			                       " AND clr.statusId=:statusId";
			 Query hqlQuery = session.createQuery(hqlQueryString);                       
			hqlQuery.setInteger("statusId", statusId);
			hqlQuery.setLong("classId", classId);
			classesRegistrations=hqlQuery.list();
			
		}
		catch(HibernateException he)
		{
			processException(he);
		}
		finally
		{
			HibernateUtils.closeConnection(session);
		}	
		return classesRegistrations;
	}
	//Code change for NIC end
	
	
	public static void createBulkClassRegistrationsForNonModerators(List<ClassRegistration> classRegistrations) throws AViewException{
		Session session = null;
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();	
			
			long count = 0;
			for(ClassRegistration classRegistration:classRegistrations)
			{
				session.save(classRegistration);
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
	
	public static void deleteClassRegisters(List<ClassRegistration> classRegistrations) throws AViewException
	{
		Session session = null;
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();	
			
			long count = 0;
			for(ClassRegistration classRegistration:classRegistrations)
			{
				session.delete(classRegistration);
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
}
