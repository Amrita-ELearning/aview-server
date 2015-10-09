/*
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
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.helpers.EmailHelper;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.AppenderUtils;
import edu.amrita.aview.common.utils.JSONParserUtils;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.common.utils.ValidationUtils;

import edu.amrita.aview.gclm.entities.User;
import edu.amrita.aview.gclm.entities.ClassRegistration;
import edu.amrita.aview.gclm.daos.CourseDAO;
import edu.amrita.aview.gclm.entities.Course;
import edu.amrita.aview.gclm.entities.Class;
import edu.amrita.aview.gclm.entities.Institute;

import com.sun.jersey.api.client.ClientResponse.Status;

@Controller
/**
 * The Class CourseHelper.
 */
public class CourseHelper {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(CourseHelper.class);
	//Cache code
	/** The active courses map. */
	private static Map<Long,Course> activeCoursesMap = Collections.synchronizedMap(new HashMap<Long,Course>());
	
	/** The Constant CACHE_CODE. */
	private static final String CACHE_CODE = "CourseHelper";
	
	/**
	 * Populate cache.
	 *
	 * @param courses the courses
	 */
	private static synchronized void populateCache(List<Course> courses)
	{
		activeCoursesMap.clear();
		for(Course course:courses)
		{
			activeCoursesMap.put(course.getCourseId(), course);
		}
		CacheHelper.setCache(CACHE_CODE);
	}
	
	/**
	 * Adds the item to cache.
	 *
	 * @param course the course
	 * @throws AViewException
	 */
	private static synchronized void addItemToCache(Course course) throws AViewException
	{
		populateNames(course);
		activeCoursesMap.put(course.getCourseId(), course);
	}
	
	/**
	 * Removes the item from cache.
	 *
	 * @param course the course
	 * @throws AViewException
	 */
	private static synchronized void removeItemFromCache(Course course) throws AViewException
	{
		activeCoursesMap.remove(course.getCourseId());
	}
	
	/**
	 * Removes the courses based on institute.
	 *
	 * @param instituteId the institute id
	 * @throws AViewException
	 */
	public static void removeCoursesBasedOnInstitute(Long instituteId) throws AViewException
	{
		logger.info("Deleting courses based on institute id:"+instituteId);
		List<Course> aviewCourses = new ArrayList<Course>(); //active classs for active crs
		aviewCourses.addAll(getActiveCoursesIdMap().values());
		for(Course course:aviewCourses)
		{
			if(course.getInstituteId().equals(instituteId))
			{
				ClassHelper.removeClassBasedOnCourse(course.getCourseId());
				logger.info("Deleting course '"+course.getCourseName()+"' based on institute id:"+instituteId);
				removeItemFromCache(course);
			}
		}
	}
	
	/**
	 * Update institute name in cache.
	 *
	 * @param institute the institute
	 * @throws AViewException
	 */
	public static void updateInstituteNameInCache(Institute institute) throws AViewException
	{
		Long instituteId = institute.getInstituteId();
		logger.info("updateInstituteNameInCache based on institute id:"+instituteId);
		List<Course> aviewCourses = new ArrayList<Course>(); //active classs for active crs
		aviewCourses.addAll(getActiveCoursesIdMap().values());
		for(Course course:aviewCourses)
		{
			if(course.getInstituteId().equals(instituteId))
			{
				ClassHelper.updateInstituteNameInCache(institute, course.getCourseId());
				logger.info("Updating course '"+course.getCourseName()+"'s instituteName based on institute id:"+instituteId);
				course.setInstituteName(institute.getInstituteName());
			}
		}
	}

	/**
	 * Gets the active courses id map.
	 *
	 * @return the active courses id map
	 * @throws AViewException
	 */
	public static synchronized Map<Long,Course> getActiveCoursesIdMap() throws AViewException
	{
		//If cache is expired or invalidated
		if(!CacheHelper.isCacheValid(CACHE_CODE))
		{
			int activeSId = StatusHelper.getActiveStatusId();
			List<Course> aviewCourses = CourseDAO.getActiveCourses(activeSId);
			populateNames(aviewCourses);
			populateCache(aviewCourses);
		}
		return activeCoursesMap;
	}
	
	/**
	 * Creates the course.
	 *
	 * @param course the course
	 * @param creatorId the creator id
	 * @throws AViewException
	 */
	public static void createCourse(Course course,Long creatorId) throws AViewException{
		course.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
		CourseDAO.createCourse(course);
		addItemToCache(course);
	}
	
	/**
	 * Update course.
	 *
	 * @param course the course
	 * @param updatorId the updator id
	 * @throws AViewException
	 */
	public static void updateCourse(Course course,Long updatorId) throws AViewException{
		course.setModifiedAuditData(updatorId, TimestampUtils.getCurrentTimestamp());
		CourseDAO.updateCourse(course);
		addItemToCache(course);
	}

	/**
	 * Gets the active courses.
	 *
	 * @return the active courses
	 * @throws AViewException
	 */
	public static List<Course> getActiveCourses() throws AViewException{
		List<Course> aviewCourses = new ArrayList<Course>();	
		aviewCourses.addAll(getActiveCoursesIdMap().values());
		return aviewCourses;
	}
	
	/**
	 * Gets the active courses by admin.
	 *
	 * @param adminId the admin id
	 * @return the active courses by admin
	 * @throws AViewException
	 */
	public static List<Course> getActiveCoursesByAdmin(Long adminId)  throws AViewException{
		int activeSId = StatusHelper.getActiveStatusId();
		List<Course> courses = CourseDAO.getActiveCoursesByAdmin(adminId, activeSId) ;
		populateNames(courses);
		return courses ;
	}
	
	/**
	 * Gets the active courses for user.
	 *
	 * @param userId the user id
	 * @return the active courses for user
	 * @throws AViewException
	 */
	public static List<Course> getActiveCoursesForUser(Long userId)  throws AViewException{
		int activeSId = StatusHelper.getActiveStatusId();
		List<Integer> statuses = new ArrayList<Integer>();
		statuses.add(activeSId);
		statuses.add(StatusHelper.getClosedStatusId());
		//List<Course> courses = CourseDAO.getActiveCoursesForUser(userId, activeSId) ;
		List<Course> courses = CourseDAO.getActiveCoursesForUser(userId, activeSId, statuses);
		populateNames(courses);
		return courses ;
	}
	
	/**
	 * Delete course.
	 *
	 * @param courseId the course id
	 * @param userId the user id
	 * @throws AViewException
	 */
	public static void deleteCourse(Long courseId, Long userId) throws AViewException{		
		int deletedSId = StatusHelper.getDeletedStatusId();
		Course course = CourseDAO.getCourse(courseId);
		if(course != null)
		{
			course.setStatusId(deletedSId);
			//bug fix for creating new course name with the same course name which has been deleted earlier
			course.setCourseName(course.getCourseName() + AppenderUtils.DeleteAppender());
			// bug fix course name and course code should be in the deleted state
			course.setCourseCode(course.getCourseCode() + AppenderUtils.DeleteAppender());
			course.setModifiedAuditData(userId, TimestampUtils.getCurrentTimestamp());
			CourseDAO.updateCourse(course);
			removeItemFromCache(course);
			ClassHelper.removeClassBasedOnCourse(courseId);
		}
		else
		{
			throw new AViewException("Course with id :"+courseId+": is not found");
		}
	}
	
	/**
	 * Gets the course.
	 *
	 * @param courseId the course id
	 * @return the course
	 * @throws AViewException
	 */
	public static Course getCourse(Long courseId) throws AViewException{
		return CourseDAO.getCourse(courseId) ;
	}
	
	/**
	 * Search course.
	 *
	 * @param courseName the course name
	 * @param courseCode the course code
	 * @param instituteId the institute id
	 * @return the list
	 * @throws AViewException
	 */
	public static List<Course> searchCourse(String courseName,String courseCode,Long instituteId) throws AViewException
	{	
		return searchCourse(courseName,courseCode,instituteId,null) ;
	}
	
	/**
	 * Gets the course count.
	 *
	 * @param instituteId the institute id
	 * @return the course count
	 * @throws AViewException
	 */
	public static Integer getCourseCount(Long instituteId) throws AViewException
	{
		return CourseDAO.getCourseCount(instituteId, StatusHelper.getActiveStatusId());
	}
	
	/**
	 * Search course.
	 *
	 * @param courseName the course name
	 * @param courseCode the course code
	 * @param instituteId the institute id
	 * @param adminId the admin id
	 * @return the list
	 * @throws AViewException
	 */
	public static List<Course> searchCourse(String courseName,String courseCode,Long instituteId,Long adminId) throws AViewException
	{	
		int activeSId = StatusHelper.getActiveStatusId();
		List<Course> courses = CourseDAO.searchCourse(courseName, courseCode, instituteId,adminId, activeSId);
		//Fix for Bug #13269 start
		removeMeetingCourses(courses);
		//Fix for Bug #13269 end
		populateNames(courses);
		return courses ;
	}
	
	/**
	 * Populate names.
	 *
	 * @param courses the courses
	 * @throws AViewException
	 */
	private static void populateNames(List<Course> courses) throws AViewException
	{
		if((courses != null) && (courses.size() > 0))
		{
			for(Course course:courses)
			{
				populateNames(course);
			}
		}
	}
	
	/**
	 * Populate names.
	 *
	 * @param course the course
	 * @throws AViewException
	 */
	public static void populateNames(Course course) throws AViewException
	{
		Institute institute = InstituteHelper.getInstitutesIdMap().get(course.getInstituteId());
		course.setInstituteName(institute.getInstituteName());
	}
	
	/**
	 * Clear cache.
	 */
	public static void clearCache()
	{
		logger.debug("Entered CourseHelper.clearCache()");
		activeCoursesMap = null;
		logger.debug("Exit CourseHelper.clearCache()");
	}

	//Fix for Bug #13269 start
	private static void removeMeetingCourses(List<Course> courses) throws AViewException
	{
		Set<Course> meetingCoursesToRemove = new HashSet<Course>();
		if((courses != null) && (courses.size() > 0))
		{
			List<Long> courseIds = new ArrayList<Long>();
			for(Course course : courses)
			{
				courseIds.add(course.getCourseId());
			}
			List<Class> meetingClasses = ClassHelper.searchMeetingClassType(courseIds);
			if((meetingClasses != null) && (meetingClasses.size() > 0))
			{
				for(Class meetingClass : meetingClasses)
				{
					meetingCoursesToRemove.add(CourseHelper.getCourse(meetingClass.getCourseId()));
				}
			}
		}
		if(meetingCoursesToRemove.size() > 0)
		{
			courses.removeAll(meetingCoursesToRemove);
		}
	}
	//Fix for Bug #13269 end
	
	
	/**
	 * Function to create course
	 * @param adminId
	 * @param courseDetails
	 * @return Response
	 * @throws AViewException
	 */
	@RequestMapping(value = "/createcourse.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response createCourse(@RequestParam("adminId") Long adminId,
			@RequestParam("courseDetails") String courseDetails) throws AViewException {
		
		String result = new String();
		logger.debug("Enter course creation::course creation ");
		Course course = null;
		String errorMessage = null;
		User admin = null;
		Object resultObjectAdmin =  UserHelper.userValidCheck(Constant.ADMIN_ID,adminId);
		if(User.class.isInstance(resultObjectAdmin))
		{
			admin = (User)resultObjectAdmin;
		}
		else
		{
			errorMessage = resultObjectAdmin.toString();
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		Object resultObject = JSONParserUtils.readJSONAsObject(courseDetails,Course.class);
		if(Course.class.isInstance(resultObject))
			course = (Course)resultObject;
		else
		{
			errorMessage = resultObject.toString();
			logger.error(errorMessage);
		}
		try 
		{
			if(course != null)
			{
				Object validateMessageForCourse = validationCheckForCourse(course,admin);
				if(validateMessageForCourse != null)
				{
					return Response.status(Status.BAD_REQUEST).entity(validateMessageForCourse.toString()).build();
				}
			}
			else
			{
				return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
			}
			Long resultId = 0l;							
			createCourse(course, adminId);
			resultId = course.getCourseId();
			logger.debug("Exit course creation on success::course creation.");
			return Response.status(Status.OK).entity(resultId).build(); 
		}
		catch(NumberFormatException nfe) 
		{
			result = "invalid";
			logger.debug("Exit course creation on invalid request::course creation.");
			return Response.status(Status.BAD_REQUEST).entity(result).build();    
		}
		catch(AViewException ae)
		{
			if(ae.getMessage().equals("Duplicate entry '"+ course.getCourseName()+"-"+course.getInstituteId() +"' for key 'course_name_institute'"))
			{
				result = "The given course name already exists. Please try a different course name.";
			}
			else if(ae.getMessage().equals("Duplicate entry '"+ course.getCourseCode()+"-"+course.getInstituteId() +"' for key 'course_code_institute'"))
			{
				result = "The given course code already exists. Please try a different course code.";
			}
			else if(ae.getMessage().equals("Duplicate entry '"+course.getCourseId()+"' for key 'PRIMARY'"))
			{
				result = "The given course id already exists. Please try a different course id.";
			}
			else
			{
				result = "Error during log. Possible reason(s) : 1. Unexpected data, 2. Unknown.";
			}
			logger.debug("Exit course creation on error durning log::course creation");
			return Response.status(Status.BAD_REQUEST).entity(result).build();
		} 
	}
	
	/**
	 * Function for validate course
	 * @param course
	 * @param admin
	 * @return Response
	 * @throws AViewException
	 */
	
	public static Object validationCheckForCourse(Course course,User admin) throws AViewException
	{
		boolean isAdmin = false;
		if (course.getCourseName() == null || course.getCourseName() == "" || course.getCourseName().length() > 99) 
		{
			return "Entered coursename is not valid or coursename is not given.";
		}
		else 
		{
			String courseError = null;
			courseError = ValidationUtils.AllowedCharForclass(course.getCourseName());
			if (courseError != null)
			{
				return courseError+" in Coursename.";
			}
		}
		
		if (course.getInstituteId() == null) 
		{
			return "Entered instituteId is not valid or instituteId doesn't exist.";
		} 
		else 
		{
			String intError = null;
			intError = ValidationUtils.integerOnly(course.getInstituteId().toString());
			if (intError != null) 
			{
				return intError +" in instituteId.";
			}
		}
		
					
		if (course.getCourseCode() == null || course.getCourseCode() == "")
		{
			return "Entered coursecode is not valid or coursecode is not given.";
		} 
		else 
		{
			String courseDescError = null;
			courseDescError = ValidationUtils.validateCity(course.getCourseCode());
			if (courseDescError != null) 
			{
				return courseDescError+" in Course code.";
			}
		}
		Institute instituteDetails = InstituteHelper.getInstituteById(course.getInstituteId());
		if(instituteDetails != null && instituteDetails.getStatusId() == StatusHelper.getActiveStatusId())
		{
			List<Institute> institutes = InstituteHelper.getAllInstitutesForAdmin(admin.getUserId());
			if(institutes != null)
			{
				 isAdmin = institutes.contains(instituteDetails);
			}
		}
		else
		{
			return "Given institute id doesn't exist";
		}
		if (admin.getRole().equals(Constant.MASTER_ADMIN_ROLE) || (isAdmin == true && admin.getRole().equals(Constant.ADMIN_ROLE)) ) 
		{
		}
		else
		{
			return "Admin Id is not authorized to perform this operation";
		}
		return null;
	}
	
	/**
	 * Function to check course validity
	 * @param courseId
	 * @return object
	 * @throws AViewException
	 */
	public  static Object courseValidCheck(Long courseId) throws AViewException
	{
		Object convertedObject = new Object();
		Course course = null;
		if(courseId == null)
		{
			courseId = 0l;
		}
		else
		{
			course = getCourse(courseId);
		}
		if(course == null)
		{
			//Fix for bug no:18990
			convertedObject = "Entered course id is not valid or doesn't exist.";
		}
		else
		{
			if(course.getStatusId() == StatusHelper.getActiveStatusId())
			{ 
				logger.debug("Given course id is valid.");
				convertedObject = course;
			}
			else if(course.getStatusId() ==  StatusHelper.getDeletedStatusId())
			{ 
				convertedObject = "Given course id is already deleted";
			}
			else
			{
				convertedObject = "Given course id is not valid.";
			}
		}
		return convertedObject;
	}
	
	/**
	 * Functions to get course registration
	 * @param adminId
	 * @param courseId
	 * @param userId
	 * @param isModerator
	 * @param sendEmail
	 * @return response
	 * @throws AViewException
	 */
	@RequestMapping(value = "/createcourseregisteration.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response createcourseregisteration(@RequestParam("adminId") Long adminId,
			@RequestParam("courseId") Long courseId,
			@RequestParam("userId") Long userId,
			@RequestParam("isModerator") String isModerator,
			@RequestParam("sendEmail") String sendEmail) throws AViewException 
	{
		String result = new String();
		Class aviewClass = null;
		logger.debug("Enter course registration::course registration");
		Course course = null;
		String errorMessage = null;
		User admin = null;
		Object resultObjectAdmin =  UserHelper.userValidCheck(Constant.ADMIN_ID,adminId);
		if(User.class.isInstance(resultObjectAdmin))
		{
			admin = (User)resultObjectAdmin;
		}
		else
		{
			errorMessage = resultObjectAdmin.toString();
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		User user = null;
		Object resultObjectUser = UserHelper.userValidCheck(Constant.USER_ID,userId);
		if(User.class.isInstance(resultObjectUser))
		{
			user = (User)resultObjectUser;
		}
		else
		{
			errorMessage = resultObjectUser.toString();
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		try 
		{
			Object resultObjectCourse = courseValidCheck(courseId);
			if(Course.class.isInstance(resultObjectCourse))
			{
				course = (Course)resultObjectCourse;
			}
			else
			{
				errorMessage = resultObjectCourse.toString();
				return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
			}
			ClassRegistration classRegistration = null;
			List<ClassRegistration> classRegistrationArray = new ArrayList<ClassRegistration>();
			List<Class> classDetails = null;
			if(admin.getRole().equals(Constant.MASTER_ADMIN_ROLE))
			{
				classDetails = ClassHelper.searchClass(0l, courseId, null, null);
			}
			else if(admin.getRole().equals(Constant.ADMIN_ROLE))
			{
				classDetails = ClassHelper.searchClass(0l, courseId, null, adminId);
			}
			else
			{
				return Response.status(Status.BAD_REQUEST).entity("Given admin id is not a valid user to perform this operation").build();
			}
			if(!classDetails.isEmpty())
			{
				for(Class classDetailArray : classDetails)
				{
					Object resultObject = ClassHelper.classValidCheck(classDetailArray.getClassId());
					if(Class.class.isInstance(resultObject))
					{
						aviewClass = (Class)resultObject;
						if(aviewClass != null && aviewClass.getStatusId() == StatusHelper.getClosedStatusId())
						{
							return Response.status(Status.BAD_REQUEST).entity("Registration for this class has been closed.").build();
						}
					}
					else
					{
						String classErrorMessage = resultObject.toString();
						return Response.status(Status.BAD_REQUEST).entity(classErrorMessage).build();
					}
					Object objectForCourseRegistration =  ClassRegistrationHelper.validationofClassRegistration(admin, user, isModerator, aviewClass) ;;
					if(ClassRegistration.class.isInstance(objectForCourseRegistration))
					{
						classRegistration = (ClassRegistration)objectForCourseRegistration;
						List<ClassRegistration> classregistrationCheck = ClassRegistrationHelper.searchForClassRegisterForUser(classRegistration.getUser().getUserId(), classRegistration.getAviewClass().getClassId(), null,  0l, 0l);
						//ClassRegistrationHelper.getClassRegisterationForUser(classRegistration.getAviewClass().getClassId(), classRegistration.getUser().getUserId());
						if(classregistrationCheck.isEmpty())
						{
							classRegistrationArray.add(classRegistration);
						}
					}
					else
					{
						errorMessage = objectForCourseRegistration.toString();
						return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
					}
					
				}
				if(!(classRegistrationArray.isEmpty()))
				{
					if(errorMessage == null || admin.getRole().equals(Constant.MASTER_ADMIN_ROLE) || (admin.getRole().equals(Constant.ADMIN_ROLE)))
					{
						ClassRegistrationHelper.createBulkClassRegistrationsForNonModerators(classRegistrationArray, adminId, StatusHelper.getActiveStatusId());
						if(sendEmail.equals("Y") || sendEmail.equals("y"))
						{
							String instituteName = null;
							if(classRegistration.getAviewClass() != null)
							{
								instituteName = classRegistration.getAviewClass().getInstituteName();
							}
							EmailHelper.sendEmailForActiveClassRegistration(classRegistration.getUser().getEmail(), classRegistration.getAviewClass().getClassName(), instituteName,course.getCourseName(),true);
						}
						result ="Successfully registered for the Course"; 
					}
					else if(admin.getRole().equals(Constant.STUDENT_ROLE) ||  admin.getRole().equals(Constant.TEACHER_ROLE))
		 			{
						ClassRegistrationHelper.createBulkClassRegistrationsForNonModerators(classRegistrationArray, adminId, StatusHelper.getPendingStatusId());
						result = "Successfully registered for the Course"; 
					}
				}
				else
				{
					result = "User is already registered for all the classes under this course"; 
				}
			}
			else
			{
				result = "No class exist under this course"; 
			}
			logger.debug("Exit course registration on success::course creation.");
			return Response.status(Status.OK).entity(result).build(); 
		}
		catch(NumberFormatException nfe) 
		{
			result = "invalid";
			logger.debug("Exit course registration on invalid request::course creation.");
			return Response.status(Status.BAD_REQUEST).entity(result).build();    
		}
		catch(AViewException ae)
		{
			if(ae.getMessage().equals("Duplicate entry '"+ course.getCourseName()+"-"+course.getInstituteId() +"' for key 'course_name_institute'"))
			{
				result = "The given course name already exists. Please try a different course name.";
			}
			else if(ae.getMessage().equals("Duplicate entry '"+ course.getCourseCode()+"-"+course.getInstituteId() +"' for key 'course_code_institute'"))
			{
				result = "The given course code already exists. Please try a different course code.";
			}
			else if(ae.getMessage().equals("Duplicate entry '"+course.getCourseId()+"' for key 'PRIMARY'"))
			{
				result = "The given course id already exists. Please try a different course id.";
			}
			else
			{
				result = "Error during log. Possible reason(s) : 1. Unexpected data, 2. Unknown.";
			}
			logger.debug("Exit course registration on error durning log::course creation");
			return Response.status(Status.BAD_REQUEST).entity(result).build();
		} 
	}
	
	
	@RequestMapping(value = "/createclassforcourses.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response createclassforcourse(@RequestParam("adminId") Long adminId,
			@RequestParam("courses") String courses,
			@RequestParam("classDetails") String classDetails,
			@RequestParam("classServerDetails") String classServerDetails) throws AViewException 
	{
		Class aviewClass = null; 
		Course courseDetails= null;
		String validationErrorMessage = null;
		String result = new String();
		logger.debug("Enter class creation for multiple course::class for multiple course");
		Course course = null;
		String errorMessage = null;
		User admin = null;
		Object resultObjectAdmin =  UserHelper.userValidCheck(Constant.ADMIN_ID,adminId);
		if(User.class.isInstance(resultObjectAdmin))
		{
			admin = (User)resultObjectAdmin;
		}
		else
		{
			errorMessage = resultObjectAdmin.toString();
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		if(classDetails == null || classDetails == "" )
		{
			return Response.status(Status.BAD_REQUEST).entity("Please enter the class Details").build();
		}
		if(courses == null || courses == "")
		{
			return Response.status(Status.BAD_REQUEST).entity("Please enter list of courses").build();
		}
		List<Class> classArray = new ArrayList<Class>();
        // Fix for Bug #18779 :start
		String courseErrorMessage = "";
		try 
		{
			JSONObject obj = new JSONObject(courses);
			String courseList = obj.getString("courses");
			String courseListArray=courseList.replaceAll("\\[", "").replaceAll("\\]","");
			String[] strarray=courseListArray.split("},");
			for(Object courseArray:strarray)
			{
				String string=""+courseArray+"}";
				Object resultObjectCourse =JSONParserUtils.readJSONAsObject(string,Course.class);
				if(Course.class.isInstance(resultObjectCourse))
				{
					course = (Course)resultObjectCourse; 
					courseDetails = getCourse(course.getCourseId());
					if(courseDetails==null)
					{
						return Response.status(Status.BAD_REQUEST).entity("The given course id '"+course.getCourseId()+"' invalid. Please try a different course id").build();
					}
					if(courseDetails.getStatusId() != StatusHelper.getActiveStatusId())
					{
						courseErrorMessage += "course id : "+courseDetails.getCourseId();
						courseErrorMessage+=",";
						
					}
					 // Fix for Bug #18779 :End
				}
				else
				{
					errorMessage = resultObjectCourse.toString();
					return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
				}
				if(courseDetails != null)
				{
					if (courseDetails.getStatusId() == StatusHelper.getActiveStatusId())
					{
						String classDetail = classDetails.substring(0, classDetails.length()-1)+" , ";
						classDetail = classDetail + "\"courseId\" : "+course.getCourseId() + " }";
						Object resultObject = ClassHelper.classCreationObject(classDetail, classServerDetails);
						if(Class.class.isInstance(resultObject))
						{
							aviewClass = (Class)resultObject;
							Object validationMessageForClass = ClassHelper.validationCheckForClass(aviewClass,admin);
							if(validationMessageForClass != null)
							{
								return Response.status(Status.BAD_REQUEST).entity(validationMessageForClass.toString()).build();
							}
							classArray.add(aviewClass);
						}
						else
						{
							String classErrorMessage = resultObject.toString();
							return Response.status(Status.BAD_REQUEST).entity(classErrorMessage).build();
						}
					}
				}
			}
            // Fix for Bug #18779:start
			if(!courseErrorMessage.equals(""))
			{
				courseErrorMessage += " are invalid";
				return Response.status(Status.BAD_REQUEST).entity(courseErrorMessage).build();
			}
 			if(!classArray.isEmpty())
			{
				for(Class classDetailArray : classArray)
				{
					course=getCourse(classDetailArray.getCourseId());
					ClassHelper.createClass(classDetailArray, adminId);
				}
				 // Fix for Bug #18779:End
			}
			logger.debug("Exit class creation for multiple course on success::class for multiple course.");
			return Response.status(Status.OK).entity("Successfully class created for multiple courses").build(); 
		}
		catch(NumberFormatException nfe) 
		{
			result = "invalid";
			logger.debug("Exit class creation for multiple course on invalid request::class for multiple course.");
			return Response.status(Status.BAD_REQUEST).entity(result).build();    
		}
		catch(AViewException ae)
		{
			if(ae.getMessage().equals("Duplicate entry '"+ course.getCourseName()+"-"+course.getInstituteId() +"' for key 'course_name_institute'"))
			{
				result = "The given course name already exists. Please try a different course name.";
			}
			else if(ae.getMessage().equals("Duplicate entry '"+ course.getCourseCode()+"-"+course.getInstituteId() +"' for key 'course_code_institute'"))
			{
				result = "The given course code already exists. Please try a different course code.";
			}
			else if(ae.getMessage().equals("Duplicate entry '"+course.getCourseId()+"' for key 'PRIMARY'"))
			{
				result = "The given course id '"+course.getCourseId()+"' already exists. Please try a different course id.";
			}
			else if((ae.getMessage().indexOf("Duplicate entry", 0) != -1) && (ae.getMessage().endsWith("' for key 'class_name_course'")))
			{
				result = "The given class name with course id '"+course.getCourseId()+"' already exists. Please try a different course id/classname.";
			}
			else
			{
				result = "Error during log. Possible reason(s) : 1. Unexpected data, 2. Unknown.";
			}
			logger.debug("Exit class creation for multiple course on error durning log::class for multiple course");
			return Response.status(Status.BAD_REQUEST).entity(result).build();
		} 
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(Status.BAD_REQUEST).entity("Error while entering class server details").build();
		}
	}

	/**
	 * API to search course.
	 * @param adminId
	 * @param course name 
	 * @return Response
	 * @throws AViewException
	 */
	@RequestMapping(value = "/searchcourse.html", method = RequestMethod.POST)
	@ResponseBody              
	public static Response searchCourses(
			@RequestParam("adminId") Long adminId,
			@RequestParam("courseName") String courseName,
			@RequestParam("instituteId") Long instituteId) throws AViewException {
		logger.debug("Enter course search::course search ");
		String errorMessage = null;
		Institute institute = null;
		ArrayList courseDetailsArray = new ArrayList();
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
		if(instituteId != null)
		{
			Institute resultObjectInstitute = InstituteHelper.getInstituteById(instituteId);
			if(resultObjectInstitute != null && resultObjectInstitute.getStatusId() == StatusHelper.getActiveStatusId())
			{
				if(Institute.class.isInstance(resultObjectInstitute))
				{
					institute = (Institute)resultObjectInstitute;
				}
				else
				{
					errorMessage = resultObjectInstitute.toString();
					errorMessage = (institute == null) ? null : errorMessage;
					return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
				}
			}
			else
			{
				return Response.status(Status.BAD_REQUEST).entity("instituteId is not valid or doesn't exist").build();
			}
		}
		if(instituteId == null && (courseName == null || courseName == ""))
		{
			return Response.status(Status.BAD_REQUEST).entity("Please provide any of the search criteria").build();
		}
		List<Course> courseDetailList = null;
		Long instituteIdValue =  (institute == null) ? null : institute.getInstituteId();
		String trimCourseName = courseName.trim();
		String course = (trimCourseName == null) ? null : trimCourseName;
		if(admin.getRole().equals(Constant.MASTER_ADMIN_ROLE))
		{
			adminId = null;
			courseDetailList = searchCourse(course, null, instituteIdValue, adminId);
		}
		else 
		{
			courseDetailList = searchCourse(course, null, instituteIdValue, adminId);	
		}
		if(courseDetailList == null || courseDetailList.size() == 0) 
		{
			return Response.status(Status.BAD_REQUEST).entity("No active course details returned for the given search criteria").build();
		} 
		else 
		{
			ArrayList arrToAddCourseDetails = new ArrayList();
			for(Course courseDetails:courseDetailList)
			{
				arrToAddCourseDetails = new ArrayList();
				arrToAddCourseDetails.add("course: " + courseDetails.getCourseName());
				arrToAddCourseDetails.add("courseId: " + courseDetails.getCourseId());
				arrToAddCourseDetails.add("courseCode: " + courseDetails.getCourseCode());
				arrToAddCourseDetails.add("institute: " + courseDetails.getInstituteName());
				courseDetailsArray.add(arrToAddCourseDetails);
			}
		}
		logger.debug("Exit course search on success:course search");
		return Response.status(Status.OK).entity(courseDetailsArray).build();
	}

}
