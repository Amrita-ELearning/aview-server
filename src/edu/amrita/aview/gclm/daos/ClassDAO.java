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
import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.common.utils.ListUtils;
import edu.amrita.aview.gclm.entities.Class;


/**
 * The Class ClassDAO.
 */
public class ClassDAO extends SuperDAO{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(ClassDAO.class);
	
	/**
	 * getActiveClasses retrieves all the active classes.
	 *
	 * @param activeSId the active s id
	 * @param statusIds the status ids
	 * @return List<Class>
	 * @throws AViewException
	 */
	public static List<Class> getActiveClasses(Integer activeSId, List<Integer> statusIds) throws AViewException{		
		Session session = null;
		List<Class> aviewClasses = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "select aviewClass from Class aviewClass,Course aviewCourse,Institute inst where " +
									" aviewClass.courseId = aviewCourse.courseId " +
									" and aviewCourse.instituteId = inst.instituteId "+
									" and aviewCourse.statusId=:activeSId " +
									" and aviewClass.statusId IN (#)" +
									" and inst.statusId=:activeSId" +
									" order by aviewClass.className ASC";
			hqlQueryString = hqlQueryString.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(statusIds));
			Query hqlQuery = session.createQuery(hqlQueryString);		
			hqlQuery.setInteger("activeSId", activeSId);
			aviewClasses = hqlQuery.list();			
		} catch (HibernateException he) {
			processException(he);	
		}
		finally {
			HibernateUtils.closeConnection(session);
		}
		return aviewClasses;
	}
	
	 /**
	 * getActiveClasses retrieves all the active classes
	 * 
	 * @param statusId
	 * @param classStatusId
	 * @param instituteStatusId
	 * @return List<Class>
	 */
	public static List<Class> getActiveMeetingsByInstitute(Long instituteId, Integer activeSId, List<Integer> statusIds) throws AViewException{		
		Session session = null;
		List<Class> aviewClasses = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "select aviewClass from Class aviewClass,Course aviewCourse where " +
									" aviewClass.courseId = aviewCourse.courseId " +
									" and aviewCourse.instituteId = :instituteId "+
									" and aviewCourse.statusId=:activeSId " +
									" and aviewClass.classType = 'Meeting'" +
									" and aviewClass.statusId IN (#)" +
									" order by aviewClass.className ASC";
			hqlQueryString = hqlQueryString.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(statusIds));
			Query hqlQuery = session.createQuery(hqlQueryString);		
			hqlQuery.setInteger("activeSId", activeSId);
			hqlQuery.setLong("instituteId", instituteId);
			
			aviewClasses = hqlQuery.list();			
		} catch (HibernateException he) {
			processException(he);	
		}
		finally {
			HibernateUtils.closeConnection(session);
		}
		return aviewClasses;
	}
	
	/**
	 * Gets the class count.
	 *
	 * @param courseId the course id
	 * @param statusIds the status ids
	 * @return the class count
	 * @throws AViewException
	 */
	public static Integer getClassCount(Long courseId, List<Integer> statusIds) throws AViewException{
		Session session = null;
		Integer classCount = -1;
		try {
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "select count(aviewUser.classId) from Class aviewUser " +
							  "where aviewUser.statusId IN (#) " +
							  "and aviewUser.courseId = :courseId";
			hqlQueryString = hqlQueryString.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(statusIds));
			Query hqlQuery = session.createQuery(hqlQueryString);
			hqlQuery.setLong("courseId",courseId);
			classCount = Integer.parseInt(hqlQuery.list().get(0).toString());

		} catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}

		return classCount;
	}

	/**
	 * Gets the active classes by admin.
	 *
	 * @param adminId the admin id
	 * @param activeSId the active s id
	 * @param statusIds the status ids
	 * @return the active classes by admin
	 * @throws AViewException
	 */
	public static List<Class> getActiveClassesByAdmin(Long adminId, Integer activeSId, List<Integer> statusIds)  throws AViewException{		
		Session session = null;
		List<Class> aviewClasses = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = 
				"select distinct aviewClass from Class aviewClass,Course aviewCourse,Institute inst,InstituteAdminUser instAdmin " +
				"where (instAdmin.institute.instituteId = inst.instituteId " +
				"or instAdmin.institute.instituteId = inst.parentInstituteId) " +
				"and aviewCourse.instituteId=inst.instituteId " +
				"and aviewClass.courseId=aviewCourse.courseId " +
				"and instAdmin.user.userId = :adminId " +
				"and aviewClass.statusId IN (#) " +
				"and aviewCourse.statusId=:statusId " +
				"and inst.statusId=:statusId";
			hqlQueryString = hqlQueryString.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(statusIds));
			Query hqlQuery = session.createQuery(hqlQueryString);		
			hqlQuery.setInteger("statusId", activeSId);
			hqlQuery.setLong("adminId", adminId);
			
			aviewClasses = hqlQuery.list();			
		} catch (HibernateException he) {
			processException(he);	
		}
		finally {
			HibernateUtils.closeConnection(session);
		}
		return aviewClasses;
	}

	/**
	 * getClass retrieves the class details for a particular class id.
	 *
	 * @param classId the class id
	 * @return Class
	 * @throws AViewException
	 */
	public static Class getClass(Long classId)  throws AViewException{
		Session session = null;
		Class aviewClass = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			aviewClass = (Class)session.get(Class.class, classId);

		} catch (HibernateException he) {
			processException(he);	
		}	
		finally {
			HibernateUtils.closeConnection(session);
		}
		return aviewClass;
	}
	
	/**
	 * getClassByClassName retrieves the class details for a unique combination of class name and course id.
	 *
	 * @param className the class name
	 * @param courseId the course id
	 * @return Class
	 * @throws AViewException
	 */
	public static Class getClassByClassName(String className,Long courseId)  throws AViewException{
		Session session = null;
		Class aviewClass = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			Query hqlQuery = session.createQuery("select aviewClass from Class aviewClass where aviewClass.className=:className and aviewClass.courseId=:courseId");
			hqlQuery.setString("className", className);
			hqlQuery.setLong("courseId",courseId);
			
			List <Class> classLst = hqlQuery.list();
			if(classLst.size() == 1)
			{
				aviewClass = (Class)(classLst.get(0));
			}
			else if(classLst.size() == 0)
			{
				logger.warn("Warning :: No Class returned for the given class name");
			}
			else if(classLst.size() > 1)
			{
				logger.warn("Warning :: More than one Class returned for the given class name");
			}

		} catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}
	return aviewClass;
	}
	
	/**
	 * searchClass searches for class/classes if either of course id/class name is passed .Also searchClass searches for a
	 * class/classes if none of the above is passed .
	 *
	 * @param instituteId the institute id
	 * @param courseId the course id
	 * @param className the class name
	 * @param adminId the admin id
	 * @param statusIds the status ids
	 * @return List
	 * @throws AViewException
	 */
	public static List <Class> searchClass(
			Long instituteId,Long courseId,String className,Long adminId,List<Integer> statusIds) throws AViewException
	{
		Session session = null ;
		List <Class> classes = null ;
		try{
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "SELECT cl" +
									" FROM Class cl , Course co, Institute inst" +
									" WHERE cl.courseId = co.courseId" +
									" and co.instituteId = inst.instituteId" +
									" and inst.statusId in (#)" +
									" and cl.statusId in (#)" +
									" and co.statusId in (#)" ;
			
			hqlQueryString = hqlQueryString.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(statusIds));
			
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

			if(instituteId != null && instituteId > 0)
			{
				hqlQueryString += " and inst.instituteId=:instituteId" ;
			}
			
			if(courseId != null && courseId > 0)
			{
				hqlQueryString += " and cl.courseId=:courseId" ;
			}
			
			if(className != null && className.trim() != "")
			{
				hqlQueryString += " and cl.className like :className" ;
			}
			
			logger.info("Query for searchClass :"+hqlQueryString);
			Query hqlQuery = session.createQuery(hqlQueryString);
			
			if(adminId != null && adminId > 0)
			{
				hqlQuery.setLong("adminId", adminId);
			}

			if(instituteId != null && instituteId > 0)
			{
				hqlQuery.setLong("instituteId", instituteId);
			}
			
			if(courseId != null && courseId > 0)
			{
				hqlQuery.setLong("courseId", courseId);
			}
			
			if(className != null && className.trim() != "")
			{
				hqlQuery.setString("className", '%'+ className +'%');
			}
			
			classes = hqlQuery.list() ;
			
		}catch(HibernateException he){
			processException(he);	
		}finally{
			HibernateUtils.closeConnection(session);
		}
		
		return classes ;
	}
	
	/**
	 * Gets the class by user id.
	 *
	 * @param userId the user id
	 * @param activeSId the active s id
	 * @param statusIds the status ids
	 * @return the class by user id
	 * @throws AViewException
	 */
	public static List <Class> getClassByUserId(Long userId,Integer activeSId, List<Integer> statusIds) throws AViewException
	{
		Session session = null ;
		List <Class> classes = null ;
		try{
			session = HibernateUtils.getHibernateConnection() ;
			String hqlQueryString = "SELECT clr.aviewClass" +
									" FROM ClassRegistration clr, Institute inst, Course co" +
									" WHERE clr.aviewClass.courseId = co.courseId" +
									" and co.instituteId = inst.instituteId" +
									" AND clr.user.userId=:userId" +
									" and clr.statusId=:statusId" +
									" and clr.aviewClass.statusId IN (#)" +
									" and inst.statusId=:statusId" +
									" and co.statusId=:statusId";
			hqlQueryString = hqlQueryString.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(statusIds));
			Query hqlQuery = session.createQuery(hqlQueryString);
			hqlQuery.setLong("userId", userId);
			hqlQuery.setInteger("statusId", activeSId);
			classes = hqlQuery.list() ;
			
		}catch(HibernateException he){
			processException(he);	
		}finally{
			HibernateUtils.closeConnection(session) ;
		}
		
		return classes ;
	}
	
	//Fix for Bug id 2450 start
	//Get the list of classes for which the given user is a moderator
	/**
	 * Gets the class by moderator.
	 *
	 * @param userId the user id
	 * @param activeSId the active s id
	 * @param statusIds the status ids
	 * @return the class by moderator
	 * @throws AViewException
	 */
	public static List<Class> getClassByModerator(Long userId, Integer activeSId, List<Integer> statusIds) throws AViewException
	{
		Session session = null ;
		List<Class> classes = null ;
		try{
			session = HibernateUtils.getHibernateConnection() ;
			String hqlQueryString = "SELECT clr.aviewClass" +
									" FROM ClassRegistration clr, Institute inst, Course co" +
									" WHERE clr.isModerator = 'Y'" + 
									" AND clr.aviewClass.courseId = co.courseId" +
									" AND co.instituteId = inst.instituteId" +
									" AND clr.user.userId=:userId" +									
									" AND clr.statusId=:statusId" +
									" AND clr.aviewClass.statusId IN (#)" +
									" AND inst.statusId=:statusId" +
									" AND co.statusId=:statusId";
			hqlQueryString = hqlQueryString.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(statusIds));
			Query hqlQuery = session.createQuery(hqlQueryString);
			hqlQuery.setLong("userId", userId);			
			hqlQuery.setInteger("statusId", activeSId);
			classes = hqlQuery.list() ;
			
		}catch(HibernateException he){
			processException(he);	
		}finally{
			HibernateUtils.closeConnection(session) ;
		}
		
		return classes ;
	}
	//Fix for Bug id 2450 end
	
	/**
	 * Creates the class.
	 *
	 * @param aviewClass the aview class
	 * @throws HibernateException the hibernate exception
	 */
	public static void createClass(Class aviewClass)  throws HibernateException
	{		
		Session session = HibernateUtils.getCurrentHibernateConnection();
		session.save(aviewClass);
	}
	
	/**
	 * Update class.
	 *
	 * @param aviewClass the aview class
	 * @throws HibernateException the hibernate exception
	 */
	 public static void updateClass(Class aviewClass)  throws HibernateException
	 {
		Session session = HibernateUtils.getCurrentHibernateConnection();
		session.update(aviewClass);
	 }

	 /**
	 * searchMeetingClassType searches for class/classes which are of meeting type.
	 * 
	 * @param instituteId
	 * @param List
	 * @param className
	 * @param adminId
	 * @return List
	 */
	public static List <Class> searchMeetingClassType(List<Long> courseIds,List<Integer> statusIds) throws AViewException
	{
		Session session = null ;
		List <Class> classes = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "SELECT cl " +
									"FROM Class cl , Course co, Institute inst " +
									"WHERE cl.classType = '" + Constant.MEETING_CLASS_TYPE + "' " +
									"and cl.statusId in (#) " +
									"and cl.courseId = co.courseId " +
									"and co.courseId in (~) " + 
									"and co.instituteId = inst.instituteId " +
									"and inst.statusId in (#) " +
									"and co.statusId in (#) ";
			
			hqlQueryString = hqlQueryString.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(statusIds));
			hqlQueryString = hqlQueryString.replaceAll("~", ListUtils.getNumericListAsCommaDelimitedString(courseIds));
			logger.info("Query for searchClass :"+hqlQueryString);
			Query hqlQuery = session.createQuery(hqlQueryString);
			classes = hqlQuery.list() ;
			
		}catch(HibernateException he)
		{
			processException(he);	
		}
		finally
		{
			HibernateUtils.closeConnection(session);
		}		
		return classes ;
	}
}
