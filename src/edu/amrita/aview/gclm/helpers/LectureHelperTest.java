/*
 * 
 */
package edu.amrita.aview.gclm.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.vo.AViewResponse;
import edu.amrita.aview.gclm.daos.LectureDAO;
import edu.amrita.aview.gclm.entities.Class;
import edu.amrita.aview.gclm.entities.Lecture;
import edu.amrita.aview.gclm.vo.LectureListVO;


/**
 * The Class LectureHelperTest.
 */
public class LectureHelperTest {

	/** The logger. */
	private static Logger logger = Logger.getLogger(LectureHelperTest.class);
	
	/** The lecture name. */
	private static String lectureName = null;
	
	/** The date. */
	private static Date date = null;

	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
//		ClassHelper.getActiveClasses();
//		lectureName = "AVIEW Lecture:"+System.currentTimeMillis();
//		GregorianCalendar cal = new GregorianCalendar();
//		cal.set(2011, 9, 10,0, 0, 0);
//		date = new Date(cal.getTimeInMillis());
//		
//		logger.debug("StartDate:"+date.toString());
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
	 * Test create lecture.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testCreateLecture() throws AViewException{

		Lecture lecture = new Lecture();
		lecture.setLectureName(lectureName);
		lecture.setStartDate(date);
		lecture.setRecordedPresenterVideoUrl("localhost/Test");
		lecture.setRecordedVideoFilePath("teacher.flv");
		lecture.setRecordedContentUrl("localhost/Test.xml");
		lecture.setRecordedContentFilePath("TestLecture");
		lecture.setClassId(58l);
		lecture.setKeywords("test");
		lecture.setLectureNumber(21);
		lecture.setStartTime(date);
		lecture.setEndTime(date);
		
		LectureHelper.createLecture(lecture, 44l);
		List<Lecture> lectureLst = LectureHelper.getLecturesForClass(lecture.getClassId());
		int i=0;
		Lecture createdLecture = new Lecture();
		for(i=0;i<=lectureLst.size();i++)
		{
			if(lectureName.equals(lectureLst.get(i).getLectureName()))
			{
				createdLecture = (Lecture)(lectureLst.get(i));
				break;

			}
		}
		assertEquals("Did not create Lecture",lectureName,createdLecture.getLectureName());

	}

	/**
	 * Test create lectures.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testCreateLectures() throws AViewException{

		List<Lecture> aviewLecture =new ArrayList<Lecture>();
		Lecture lecture = new Lecture() ;
		lecture.setLectureName(lectureName);
		lecture.setStartDate(date);
		lecture.setRecordedPresenterVideoUrl("localhost/Test");
		lecture.setRecordedVideoFilePath("teacher.flv");
		lecture.setRecordedContentUrl("localhost/Test.xml");
		lecture.setRecordedContentFilePath("TestLecture");
		lecture.setClassId(58l);
		lecture.setKeywords("test");
		lecture.setLectureNumber(21);
		lecture.setStartTime(date);
		lecture.setEndTime(date);
		aviewLecture.add(lecture);
		LectureHelper.createLectures(aviewLecture, 45l);
		List<Lecture> lectureLst = LectureHelper.getLecturesForClass(lecture.getClassId());
		int i=0;
		Lecture createdLecture = new Lecture();
		for(i=0;i<=lectureLst.size();i++)
		{
			if(lectureName.equals(lectureLst.get(i).getLectureName()))
			{
				createdLecture = (Lecture)(lectureLst.get(i));
				break;
			}
		}
		assertEquals("Did not create Lecture",lectureName,createdLecture.getLectureName());

	}

	/**
	 * Test update lecture.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testUpdateLecture() throws AViewException{
		String keyWord = lectureName; 
		Lecture lecture = LectureDAO.getLecture(23l);
		lecture.setKeywords(keyWord);
	
		LectureHelper.updateLecture(lecture,44l);
		
		Lecture updatedLecture = LectureDAO.getLecture(lecture.getLectureId()); 
		assertEquals("Did not create Lecture",keyWord,updatedLecture.getKeywords());

	}

	/**
	 * Test update lectures.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testUpdateLectures() throws AViewException{
		
		String keyWord = lectureName; 
		String updatedKeyWord = null;
		Long lectureId = 25l;
		Lecture lecture = LectureHelper.getLecture(lectureId);
		lecture.setKeywords(keyWord);
		LectureHelper.updateLecture(lecture,44l);

		Lecture updatedLecture = LectureHelper.getLecture(lectureId);
		updatedKeyWord = updatedLecture.getKeywords();
		assertEquals("Did not updated Lectures",keyWord,updatedKeyWord);
		
	}

	/**
	 * Test delete lecture.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testDeleteLecture() throws AViewException{
		
		Long lectureId = 25l;
		Lecture lecture = LectureDAO.getLecture(lectureId);
		assertTrue("Lecture must be active first",(lecture.getStatusId() == StatusHelper.getActiveStatusId()));
		LectureHelper.deleteLecture(lectureId);
		lecture = LectureDAO.getLecture(lectureId);
		assertTrue("Lecture is not deleted",(lecture.getStatusId() == StatusHelper.getDeletedStatusId()));
	}

	/**
	 * Test delete lecture by class id.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testDeleteLectureByClassId() throws AViewException{
		
		Long lectureId = 25l;
		Long classId = 35l;
		Lecture lecture = LectureDAO.getLecture(lectureId);
		assertTrue("Lecture must be active first",(lecture.getStatusId() == StatusHelper.getActiveStatusId()));
		LectureHelper.deleteLectureByClassId(classId,lectureId);
		lecture = LectureDAO.getLecture(lectureId);
		assertTrue("Lecture is not deleted",(lecture.getStatusId() == StatusHelper.getDeletedStatusId()));
	}

	/**
	 * Test get lecture.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetLecture() throws AViewException{
		
		Long lectureId = 150l;
		Lecture lecture = LectureHelper.getLecture(lectureId);
		assertNotNull(lecture);
	}

	/**
	 * Test get lectures for class.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetLecturesForClass() throws AViewException{

		Long classId = 58l;
		List<Lecture> lectures = LectureHelper.getLecturesForClass(classId);
		assertEquals("Did not have Lecture",195,lectures.size());

	}

	/**
	 * Test is the lecture day.
	 */
	@Ignore
	public void testIsTheLectureDay() {

		boolean flag = LectureHelper.isTheLectureDay("YYYYNYY", 1);
		assertEquals("Did not have lecture",false,flag);

	}

	/**
	 * Test get lectures for class create.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetLecturesForClassCreate() throws AViewException{
		Long classId = 150l;
		Class aviewClass = ClassHelper.getClass(classId);
		List<Lecture> lectures = LectureHelper.getLecturesForClassCreate(aviewClass);
		assertEquals("Did not have Lecture",23,lectures.size());
	}

	/**
	 * Test get lectures for class update.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetLecturesForClassUpdate() throws AViewException{
		Long classId = 150l;
		Class aviewClass = ClassHelper.getClass(classId);
		List<Lecture> lectures = LectureHelper.getLecturesForClassUpdate(aviewClass);
		assertEquals("Did not have Lecture",23,lectures.size());
	}

	/**
	 * Test get todays lectures.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetTodaysLectures() throws AViewException{
		//User u = UserHelper.getUser("administrator");
		List<LectureListVO> lectureList = LectureHelper.getTodaysAllLectures(554l);
		for(LectureListVO vo:lectureList)
		{
			System.out.println(vo.classRegistration.toString()+"::::"+vo.lecture.toString());
		}
//		assertEquals("Incorrect number of lectures returned",3,lectureList.size());
	}
	
	/**
	 * Test deleted recording.
	 */
	@Test
	public void testDeletedRecording()
	{
		AViewResponse vo = LectureHelper.deleteLectureRecording(79934l, 554l);
	}
	
	/**
	 * Test get class room lecture by lecture id.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetClassRoomLectureByLectureId() throws AViewException{
		//User u = UserHelper.getUser("administrator");
		AViewResponse vo = null;
		
//		vo = LectureHelper.getClassRoomLectureByLectureId(554l,49078l);
//		printLectureVO(vo);
//		
//		vo = LectureHelper.getClassRoomLectureByLectureId(554l,75259l);
//		printLectureVO(vo);
		
		vo = LectureHelper.getClassRoomLectureByLectureId(22986l,120780l);
		printLectureVO(vo);
		
//		vo = LectureHelper.getClassRoomLectureByLectureId(44l,49078l);
//		printLectureVO(vo);
//
//		vo = LectureHelper.getClassRoomLectureByLectureId(44l,75259l);
//		printLectureVO(vo);
//		
//		vo = LectureHelper.getClassRoomLectureByLectureId(8729l,49078l);
//		printLectureVO(vo);
//
//		vo = LectureHelper.getClassRoomLectureByLectureId(8729l,75259l);
//		printLectureVO(vo);
		
//		assertEquals("Incorrect number of lectures returned",3,lectureList.size());
	}
	
	/**
	 * Prints the lecture vo.
	 *
	 * @param res the res
	 */
	private void printLectureVO(AViewResponse res)
	{
		LectureListVO vo = null;
		if(res.getResponseId().equals(AViewResponse.REQUEST_SUCCESS))
		{
			vo = (LectureListVO)res.getResult();
			if(vo != null)
			{
				System.out.println(((vo.classRegistration != null)?vo.classRegistration.toString():"None")+"|||"+vo.lecture.toString());
			}
			else
			{
				System.out.println("No Lecture");
			}
		}
		else
		{
			System.out.println("Error occured while fetching the lectureVO."+res.getResponseId()+":"+res.getResponseMessage());
		}
	}
	

}
