/*
 * 
 */
package edu.amrita.aview.gclm.helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.gclm.entities.ClassRegistration;
import edu.amrita.aview.gclm.entities.Course;
import edu.amrita.aview.gclm.entities.Institute;


/**
 * The Class ClassRepositoryFolderStructureHelper.
 */
public class ClassRepositoryFolderStructureHelper {
	
	/** The institutes. */
	private Set<Institute> institutes = null;
	
	/** The courses map. */
	private Map<Long,Set<Course>> coursesMap = null;
	
	/** The class map. */
	private Map<Long,Set<ClassRegistration>> classMap = null;
	
	/**
	 * Instantiates a new class repository folder structure helper.
	 */
	public ClassRepositoryFolderStructureHelper()
	{
		institutes = new TreeSet<Institute>();
		coursesMap = new HashMap<Long,Set<Course>>();
		classMap = new HashMap<Long,Set<ClassRegistration>>();
	}
	
	/**
	 * Adds the class registraiton.
	 *
	 * @param classRegister the class register
	 * @throws AViewException
	 */
	public void addClassRegistraiton(ClassRegistration classRegister) throws AViewException
	{
		Long courseId = classRegister.getAviewClass().getCourseId();
		Set<ClassRegistration> classRegisters = classMap.get(courseId);
		
		if(classRegisters == null)
		{
			classRegisters = new TreeSet<ClassRegistration>();
			classMap.put(courseId, classRegisters);
		}
		classRegisters.add(classRegister);
		addCourse(CourseHelper.getActiveCoursesIdMap().get(courseId));
	}
	
	/**
	 * Adds the course.
	 *
	 * @param course the course
	 * @throws AViewException
	 */
	private void addCourse(Course course) throws AViewException
	{
		Long instituteId = course.getInstituteId();
		Set<Course> courses = coursesMap.get(instituteId);
		if(courses == null)
		{
			courses = new TreeSet<Course>();
			coursesMap.put(instituteId, courses);
		}
		courses.add(course);
		addInstitute(InstituteHelper.getInstitutesIdMap().get(instituteId));
	}
	
	/**
	 * Adds the institute.
	 *
	 * @param institute the institute
	 */
	private void addInstitute(Institute institute)
	{
		institutes.add(institute);
	}
	
	/**
	 * Gets the folder xml.
	 *
	 * @return the folder xml
	 */
	public String getFolderXML()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<institutes name=\"Institute(s)\">");
		sb.append("\n");
		for(Institute inst:institutes)
		{
			sb.append(getInstituteXML(inst));
		}
		sb.append("</institutes>");
		return sb.toString();
	}
	
	/**
	 * Gets the institute xml.
	 *
	 * @param inst the inst
	 * @return the institute xml
	 */
	private String getInstituteXML(Institute inst)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("<institute id=\"");
		sb.append(inst.getInstituteId());
		sb.append("\" name=\"");
		sb.append(inst.getInstituteName());
		sb.append("\">");
		sb.append("\n");
		
		sb.append("<courses name=\"Courses\">");
		sb.append("\n");
		
		Set<Course> courses = coursesMap.get(inst.getInstituteId());
		for(Course course:courses)
		{
			sb.append(getCourseXML(course));
		}
		sb.append("</courses>\n");
		sb.append("</institute>\n");
		return sb.toString();
	}
	
	/**
	 * Gets the course xml.
	 *
	 * @param course the course
	 * @return the course xml
	 */
	private String getCourseXML(Course course)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("<course id=\"");
		sb.append(course.getCourseId());
		sb.append("\" name=\"");
		sb.append(course.getCourseName());
		sb.append("\">");
		sb.append("\n");
		
		sb.append("<classes name=\"Classes\">\n");
		sb.append("\n");
		
		Set<ClassRegistration> classes = classMap.get(course.getCourseId());
		
		if(classes != null)
		{
			for(ClassRegistration classRegister:classes)
			{
				sb.append(getClassXML(classRegister));
			}
		}
		sb.append("</classes>\n");
		sb.append("</course>\n");
		return sb.toString();
	}
	
	/**
	 * Gets the class xml.
	 *
	 * @param classRegister the class register
	 * @return the class xml
	 */
	private String getClassXML(ClassRegistration classRegister)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<class id=\"");
		sb.append(classRegister.getAviewClass().getClassId());
		sb.append("\" name=\"");
		sb.append(classRegister.getAviewClass().getClassName());
		sb.append("\" is_moderator=\"");
		sb.append(classRegister.getIsModerator());
		sb.append("\" />\n");
		return sb.toString();
	}
}
