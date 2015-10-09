/*
 * 
 */
package edu.amrita.aview.gclm.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.gclm.entities.Course;


/**
 * The Class CourseHelperTest.
 */
public class CourseHelperTest {

	/** The course name. */
	private static String courseName = null;
	
	/** The course code. */
	private static String courseCode = null;
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		courseName = "AVIEW Course:"+System.currentTimeMillis();
		courseCode = "AVC:"+System.currentTimeMillis();
	}

	/**
	 * Tear down after class.
	 *
	 * @throws Exception the exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception the exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test create course.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testCreateCourse() throws AViewException{
		
		Course course = new Course();
		course.setCourseName(courseName);
		course.setCourseCode(courseCode);
		course.setInstituteId(150l);
		CourseHelper.createCourse(course, 44l);
		List<Course> courseLst = CourseHelper.searchCourse(courseName, courseCode, 150l);
		int i=0;
		Course createdCourse = new Course();
		for(i=0;i<=courseLst.size();i++)
		{
			if(courseName.equals(courseLst.get(i).getCourseName()))
			{
				createdCourse = (Course)(courseLst.get(i));
				break;
			}
		}
		assertEquals("Did not create Course",courseName,createdCourse.getCourseName());

	}
	
	/**
	 * Test get course count.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetCourseCount() throws AViewException {
		
		Long instId = 1184l; 
		Integer courseCount = CourseHelper.getCourseCount(instId);
		System.out.println("CourseCount:"+courseCount);
	}


	/**
	 * Test update course.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testUpdateCourse() throws AViewException {
		
		Long instId = 342l; 
		Course course = CourseHelper.getCourse(18l);
		course.setInstituteId(instId);
	
		CourseHelper.updateCourse(course,44l);
		
		Course updatedCourse = CourseHelper.getCourse(course.getCourseId()); 
		assertEquals("Did not create Course",instId,updatedCourse.getInstituteId());
	}

	/**
	 * Test get active courses.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetActiveCourses() throws AViewException{
		List<Course> course = CourseHelper.getActiveCourses();
		assertEquals("Did not get all Course",23,course.size());
	}

	/**
	 * Test get active courses by admin.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetActiveCoursesByAdmin() throws AViewException{
		List<Course> course = CourseHelper.getActiveCoursesByAdmin(2l);
		assertEquals("Did not get all Course",5,course.size());
	}

	/**
	 * Test get active courses for user.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetActiveCoursesForUser() throws AViewException{
		List<Course> course = CourseHelper.getActiveCoursesForUser(554l);
		assertEquals("Did not get all Course",5,course.size());
	}

	/**
	 * Test delete course.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testDeleteCourse() throws AViewException{
		
		Long courseId = 164l;
		Course course = CourseHelper.getCourse(courseId);
		assertTrue("Course must be active first",(course.getStatusId() == StatusHelper.getActiveStatusId()));
		CourseHelper.deleteCourse(courseId, 44l);
		course = CourseHelper.getCourse(courseId);
		assertTrue("Course is not deleted",(course.getStatusId() == StatusHelper.getDeletedStatusId()));
	}

	/**
	 * Test get course.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetCourse() throws AViewException{

		Long courseId = 122l;
		Course course = CourseHelper.getCourse(courseId);
		assertNotNull(course);
		assertEquals("Did not have course",courseId,course.getCourseId());

	}

	/**
	 * Test search course.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testSearchCourse() throws AViewException{
			List<Course> course = CourseHelper.searchCourse(null, null, null,1l);
		assertNotNull(course);
		assertEquals("Did not have course",10l,course.size());
	}

}
