/*
 * 
 */
package edu.amrita.aview.gclm.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.gclm.entities.Class;
import edu.amrita.aview.gclm.entities.ClassServer;
import edu.amrita.aview.gclm.entities.Lecture;
import edu.amrita.aview.gclm.entities.Server;



/**
 * The Class ClassHelperTest.
 */
public class ClassHelperTest {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(ClassHelperTest.class);
	
	/** The class name. */
	private static String className = null;
	
	/** The date. */
	private static java.sql.Date date = null;
	
	/** The end date. */
	private static java.sql.Date endDate = null;
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		className = "AVIEW Class:"+System.currentTimeMillis();
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(2011, 9, 10,0, 0, 0);
		date = new Date(cal.getTimeInMillis());
		
		cal.set(2011, 11, 30,0, 0, 0);
		
		endDate = new Date(cal.getTimeInMillis());
		
		logger.debug("StartDate:"+date.toString());
		logger.debug("EndDate:"+endDate.toString());
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
	 * Test get active classes by admin.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetActiveClassesByAdmin() throws AViewException {

		List<Class> aviewClass = ClassHelper.getActiveClassesByAdmin(2l);
		assertEquals("Did not get all Class",20l,aviewClass.size());

	}

	/**
	 * Test get active classes.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetActiveClasses() throws AViewException {

		List<Class> aviewClass = ClassHelper.getActiveClasses();
		assertEquals("Did not get all Class",8,aviewClass.size());

	}

	/**
	 * Test create class.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testCreateClass() throws AViewException {
		
		Long courseId = 42l;
		Class aviewClass = new Class();
		aviewClass.setClassName(className);
		aviewClass.setStartDate(date);
		aviewClass.setEndDate(endDate);
		aviewClass.setCourseId(courseId);
		aviewClass.setStartTime(date);	
		aviewClass.setEndTime(date);
		aviewClass.setWeekDays("YYYYYYY"); 
		aviewClass.setMaxStudents(5); 
		aviewClass.setMaxPublishingBandwidthKbps(512); 
		aviewClass.setMinPublishingBandwidthKbps(128); 
		aviewClass.setVideoCodec("VP6");
		aviewClass.setVideoStreamingProtocol("rtmp");
		aviewClass.setIsMultiBitrate("y");
		aviewClass.setPresenterPublishingBwsKbps("128");
		aviewClass.setAllowDynamicSwitching("N");
		Server server = ServerHelper.getServer(2);
		
		aviewClass.addClassServer(createClassServer(server,3,1935,128));

		
		ClassHelper.createClass(aviewClass, 44l);
		Class createdClass = ClassHelper.getClassByClassName(aviewClass.getClassName(), courseId);
		assertEquals("Did not create Class",courseId,createdClass.getCourseId());
		assertEquals("Did not create Class servers",aviewClass.getClassServers().size(),createdClass.getClassServers().size());
		
	}

	/**
	 * Test get class by class name.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetClassByClassName() throws AViewException {
		
		Long courseId = 42l;
		Class aviewClass = ClassHelper.getClassByClassName("Whiteboard", courseId);
		assertEquals("Did not get Class",courseId,aviewClass.getCourseId());
	}

	/**
	 * Test get class by moderator.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetClassByModerator() throws AViewException {
		List<Class> aviewClasses = ClassHelper.getClassByModerator(554l);
		assertEquals("Did not get all Class",5l,aviewClasses.size());
	}

	/**
	 * Test update class.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testUpdateClass() throws AViewException {
		
		int maxStudents = 20; 
		Class aviewClass = ClassHelper.getClass(4l);
		aviewClass.setWeekDays("YNYYNYY"); 
		aviewClass.setMaxStudents(maxStudents);
		aviewClass.getClassServers().clear();
		
//		Iterator<ClassServer> iter = aviewClass.getClassServers().iterator();
//		while(iter.hasNext())
//		{
//			ClassServer cls = iter.next();
//			if(cls.getServerTypeId() == 3)
//			{
//				iter.remove();
//			}
//		}
		
		Server server = ServerHelper.getServer(8);
		aviewClass.addClassServer(createClassServer(server,3,1935,null));
		
		ClassHelper.updateClass(aviewClass,44l);
		Class updatedClass = ClassHelper.getClass(aviewClass.getClassId()); 
		assertEquals("Did not update Class",maxStudents,updatedClass.getMaxStudents());
	}

	/**
	 * Test update lecture by class id.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testUpdateLectureByClassId() throws AViewException {

		Long classId = 20l; 
		ClassHelper.updateLectureByClassId(classId, 20l, StatusHelper.getActiveStatusId());
		List<Lecture> updatedLecture = LectureHelper.getLecturesForClass(classId) ;
		assertEquals("Did not Lecture for Class",50l,updatedLecture.size());

	}

	/**
	 * Test delete class.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testDeleteClass() throws AViewException {
		
		Long classId = 104l;
		Class aviewClass = ClassHelper.getClass(classId);
		assertTrue("Course must be active first",(aviewClass.getStatusId() == StatusHelper.getActiveStatusId()));
		ClassHelper.deleteClass(classId, 44l);
		aviewClass =  ClassHelper.getClass(classId);
		assertTrue("Class is not deleted",(aviewClass.getStatusId() == StatusHelper.getDeletedStatusId()));
	
	}
	
	/**
	 * Test search class.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testSearchClass() throws AViewException {
		
		List<Class> aviewClass = ClassHelper.searchClass(0l,0l,null,0l);
		assertEquals("Did not get all Class",20l,aviewClass.size());
		
	}
	
	/**
	 * Test get class.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetClass() throws AViewException {
		Long classId = 109l;
		Class aviewClass = ClassHelper.getClass(classId);
		assertEquals("Did not get all Class",classId,aviewClass.getClassId());
		
	}

	/**
	 * Creates the class server.
	 *
	 * @param server the server
	 * @param serverTypeId the server type id
	 * @param serverPort the server port
	 * @param presenterPublishingBandwidthKbps the presenter publishing bandwidth kbps
	 * @return the class server
	 */
	private ClassServer createClassServer(Server server,Integer serverTypeId,Integer serverPort,Integer presenterPublishingBandwidthKbps)
	{
		ClassServer classServer = new ClassServer();
		classServer.setServer(server);
		classServer.setServerTypeId(serverTypeId);
		classServer.setServerPort(serverPort);
		classServer.setPresenterPublishingBandwidthKbps(presenterPublishingBandwidthKbps);
		return classServer;
	}

}
