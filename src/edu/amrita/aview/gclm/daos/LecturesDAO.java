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
import edu.amrita.aview.gclm.entities.Lectures;


/**
 * The Class LecturesDAO.
 */
public class LecturesDAO  extends SuperDAO
{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(LecturesDAO.class);

	/**
	 * Gets the old recorded video lectures.
	 *
	 * @param courseId the course id
	 * @param userId the user id
	 * @param userRole the user role
	 * @param lectureKeyword the lecture keyword
	 * @param lectureTopic the lecture topic
	 * @return the old recorded video lectures
	 * @throws AViewException
	 */
	public static List<Lectures> getOldRecordedVideoLectures(Long courseId, Long userId, String userRole, String lectureKeyword, String lectureTopic) throws AViewException
	{
		//For Master Admin and Admin
		/*
		"SELECT DISTINCT l.lecture_topic,STR_TO_DATE(IF(l.date = 'null','1/1/1990',l.date),'%d/%m/%Y') 
		ldaten,l.videofile_path recorded_presenter_video_url, '' recorded_viewer_video_url, " +
		"l.videofile_name recorded_video_file_path, '' recorded_content_url,
		l.xmlfile_path recorded_content_file_path,l.keywords,l.lecture_id, 
		'N' is_moderator, 'OLD' PLAYER " +
		"FROM lectures l,lecture rl " +
		"WHERE l.recorded_lecture_id = rl.lecture_id " +
		"and l.course_id = "+CmbCourse.selectedItem.course_id +
		*/
		
		//For other users
		/*
		"SELECT DISTINCT l.lecture_topic,STR_TO_DATE(IF(l.date = 'null','1/1/1990',l.date),'%d/%m/%Y') ldaten,
		l.videofile_path recorded_presenter_video_url, '' recorded_viewer_video_url, " +
		"l.videofile_name recorded_video_file_path, '' recorded_content_url,
		l.xmlfile_path recorded_content_file_path,l.keywords,l.lecture_id,cr.is_moderator,'OLD' PLAYER " +
		"FROM lectures l,lecture rl,class_register cr " +
		"WHERE l.recorded_lecture_id = rl.lecture_id and rl.class_id = cr.class_id " +
		"and l.course_id = "+CmbCourse.selectedItem.course_id +
		" and cr.user_id = "+ClassroomContext.userVO.userId +
		*/
		
		String adminQuery = "SELECT oldLecture from Lectures oldLecture, Lecture newLecture " +
							"WHERE oldLecture.recordedLectureId = newLecture.lectureId " +
							"and oldLecture.courseId = :courseId";

		String userQuery = "SELECT oldLecture FROM Lectures oldLecture, Lecture newLecture, ClassRegistration cr " +
						   "WHERE oldLecture.recordedLectureId = newLecture.lectureId and newLecture.classId = cr.aviewClass.classId " +
						   "and oldLecture.courseId = :courseId and cr.user.userId = :userId"; 
		
		String hqlQuery = null;
		if((userRole.equals(Constant.MASTER_ADMIN_ROLE) || (userRole.equals(Constant.ADMIN_ROLE))))
		{
			hqlQuery = adminQuery;
		}
		else 
		{
			hqlQuery = userQuery;
		}
		if(lectureKeyword != null)
		{
			hqlQuery += " and oldLecture.keyWords like :lectureKeyword ";
		}
		if(lectureTopic != null)
		{
			hqlQuery += " and oldLecture.lectureTopic like :lectureTopic ";
		}
		List<Lectures> lectures = null;
		Session session = null;
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			Query query = session.createQuery(hqlQuery);
			query.setLong("courseId", courseId);
			//Set the user id only for student/teacher
			if((userRole.equals(Constant.STUDENT_ROLE) || (userRole.equals(Constant.TEACHER_ROLE))))
			{
				query.setLong("userId", userId);
			}
			if(lectureKeyword != null)
			{
				query.setString("lectureKeyword", ("%" + lectureKeyword + "%"));
			}
			if(lectureTopic != null)
			{
				query.setString("lectureTopic", ("%" + lectureTopic + "%"));
			}
			lectures = query.list();
		}
		catch (HibernateException he) 
		{
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}
		return lectures;
	}	
}
