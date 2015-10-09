package edu.amrita.aview.common.utils;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IdentityGenerator;

import edu.amrita.aview.gclm.entities.Class;
import edu.amrita.aview.gclm.entities.Course;
import edu.amrita.aview.gclm.entities.Lecture;

public class CustomIdGenerator extends IdentityGenerator {

	@Override
	public Serializable generate(SessionImplementor session, Object obj)
			throws HibernateException
	{
		Long entityId = null;
		if (obj instanceof Class) 
		{
			Class aviewClass = (Class) obj;
			entityId = aviewClass.getClassId();
		} 
		else if (obj instanceof Lecture) 
		{
			Lecture lecture = (Lecture) obj;
			entityId = lecture.getLectureId();
		}
		else if (obj instanceof Course) 
		{
			Course course = (Course) obj;
			entityId = course.getCourseId();
		}
		if (entityId == null || entityId == 0) 
		{
			Serializable id = super.generate(session, obj);
			return id;
		}
		else 
		{
			return entityId;
		}
	}
}