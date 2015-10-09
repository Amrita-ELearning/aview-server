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
import edu.amrita.aview.common.utils.ListUtils;
import edu.amrita.aview.gclm.entities.Course;


/**
 * The Class CourseDAO.
 */
public class CourseDAO  extends SuperDAO{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(CourseDAO.class);	
	
	/**
	 * Creates the course.
	 *
	 * @param course the course
	 * @throws AViewException
	 */
	public static void createCourse(Course course) throws AViewException{
		Session session = null;
		String creationMessage = null ;
		try {
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.save(course);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		} finally {
			HibernateUtils.closeConnection(session);
		}
	}

	/**
	 * Update course.
	 *
	 * @param course the course
	 * @throws AViewException
	 */
	public static void updateCourse(Course course) throws AViewException{
		
		Session session = null;
		String creationMessage = null ;
		try {			
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.update(course);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		} finally {
			HibernateUtils.closeConnection(session);
		}
	}

	/**
	 * Gets the active courses.
	 *
	 * @param statusId the status id
	 * @return the active courses
	 * @throws AViewException
	 */
	public static List<Course> getActiveCourses(Integer statusId) throws AViewException{
		Session session = null;
		List<Course> aviewCourses = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			Query hqlQuery = session.createQuery("select aviewCourse from Course aviewCourse,Institute inst where aviewCourse.statusId=:statusId" +
												" and inst.statusId=:statusId" +
												" and aviewCourse.instituteId=inst.instituteId" +
												" order by aviewCourse.courseName ASC");
			hqlQuery.setInteger("statusId",statusId);
			aviewCourses = hqlQuery.list();

		} catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}

		return aviewCourses;
	}
	
	/**
	 * Gets the course count.
	 *
	 * @param instituteId the institute id
	 * @param statusId the status id
	 * @return the course count
	 * @throws AViewException
	 */
	public static Integer getCourseCount(Long instituteId,Integer statusId) throws AViewException{
		Session session = null;
		Integer courseCount = -1;
		try {
			session = HibernateUtils.getHibernateConnection();
			Query hqlQuery = session.createQuery("select count(aviewCourse.courseId) from Course aviewCourse " +
					"where aviewCourse.statusId=:statusId " +
					"and aviewCourse.instituteId = :instituteId");
			hqlQuery.setInteger("statusId",statusId);
			hqlQuery.setLong("instituteId",instituteId);
			courseCount = Integer.parseInt(hqlQuery.list().get(0).toString());

		} catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}

		return courseCount;
	}
	
	/**
	 * Gets the active courses by admin.
	 *
	 * @param adminId the admin id
	 * @param statusId the status id
	 * @return the active courses by admin
	 * @throws AViewException
	 */
	public static List<Course> getActiveCoursesByAdmin(Long adminId,Integer statusId) throws AViewException {
		Session session = null;
		List<Course> aviewCourses = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			Query hqlQuery = session.createQuery(
					"select distinct aviewCourse from Course aviewCourse,Institute inst,InstituteAdminUser instAdmin " +
					"where (instAdmin.institute.instituteId = inst.instituteId " +
						"or instAdmin.institute.instituteId = inst.parentInstituteId) " +
					"and aviewCourse.instituteId=inst.instituteId " +
					"and instAdmin.user.userId = :adminId " +
					"and aviewCourse.statusId=:statusId " +
					"and inst.statusId=:statusId");
			
			hqlQuery.setInteger("statusId",statusId);
			hqlQuery.setLong("adminId",adminId);

			aviewCourses = hqlQuery.list();

		} catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}

		return aviewCourses;
	}
	
	/**
	 * Gets the active courses for user.
	 *
	 * @param userId the user id
	 * @param statusId the status id
	 * @param classStatusIds the class status ids
	 * @return the active courses for user
	 * @throws AViewException
	 */
	public static List<Course> getActiveCoursesForUser(Long userId,Integer statusId, List<Integer>classStatusIds) throws AViewException {
		Session session = null;
		List<Course> aviewCourses = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "select distinct aviewCourse from Course aviewCourse,Institute inst,ClassRegistration clr " +
									"where clr.aviewClass.courseId = aviewCourse.courseId " +
									"and aviewCourse.instituteId=inst.instituteId " +
									"and clr.user.userId = :userId " +
									"and clr.statusId = :statusId " +
									"and clr.aviewClass.statusId IN (#) " +
									"and aviewCourse.statusId=:statusId " +
									"and inst.statusId=:statusId";
			hqlQueryString = hqlQueryString.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(classStatusIds));
			Query hqlQuery = session.createQuery(hqlQueryString);
			hqlQuery.setInteger("statusId",statusId);
			hqlQuery.setLong("userId",userId);

			aviewCourses = hqlQuery.list();

		} catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}

		return aviewCourses;
	}
	
	/**
	 * Gets the course.
	 *
	 * @param courseId the course id
	 * @return the course
	 * @throws AViewException
	 */
	public static Course getCourse(Long courseId) throws AViewException{
		Session session = null;
		Course course = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			course = (Course)session.get(Course.class, courseId);
		} catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}
		return course;
	}
	
	/**
	 * Search course.
	 *
	 * @param courseName the course name
	 * @param courseCode the course code
	 * @param instituteId the institute id
	 * @param adminId the admin id
	 * @param statusId the status id
	 * @return the list
	 * @throws AViewException
	 */
	public static List<Course> searchCourse(String courseName,String courseCode,Long instituteId,Long adminId,Integer statusId) throws AViewException
	{
		Session session = null ;
		List<Course> courses = null ;
		try{
			session = HibernateUtils.getHibernateConnection() ;
			String hqlQueryString = "SELECT co" +
									" FROM Course co,Institute inst" +
									" WHERE co.instituteId=inst.instituteId" + 
									" and co.statusId=:statusId" +
									" and inst.statusId=:statusId";
			
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

			if(courseName != null && courseName.trim() != "")
			{
				hqlQueryString += " and co.courseName like :courseName";
			}
			
			if(courseCode != null && courseCode.trim() != "")
			{
				hqlQueryString += " and co.courseCode like :courseCode" ;
			}
			
			if(instituteId != null && instituteId > 0)
			{
				hqlQueryString += " and co.instituteId=:instituteId" ;
			}
			
			logger.info("Search qurey for courses:"+hqlQueryString);
			
			Query hqlQuery = session.createQuery(hqlQueryString);
			
			if(adminId != null && adminId > 0)
			{
				hqlQuery.setLong("adminId", adminId);
			}
			
			if(courseName != null && courseName.trim() != "")
			{
				hqlQuery.setString("courseName",'%'+courseName+'%');
			}
			
			if(courseCode != null && courseCode.trim() != "")
			{
				hqlQuery.setString("courseCode", '%'+courseCode+'%');
			}
			
			if(instituteId != null && instituteId > 0)
			{
				hqlQuery.setLong("instituteId", instituteId);
			}
			
			hqlQuery.setInteger("statusId", statusId);
			
			courses = hqlQuery.list() ;
			
		}catch(HibernateException he){
			processException(he);	
		}finally{
			HibernateUtils.closeConnection(session);
		}
		
		return courses ;
	}
}