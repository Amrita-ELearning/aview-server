<%@page import="org.apache.log4j.Logger"%>
<%@page import="edu.amrita.aview.gclm.helpers.CourseHelper"%>
<%@page import="edu.amrita.aview.gclm.helpers.InstituteHelper"%>
<%@page import="edu.amrita.aview.gclm.helpers.ClassHelper"%>
<%@page import="edu.amrita.aview.common.helpers.StatusHelper"%>
<%@page import="edu.amrita.aview.gclm.entities.Class"%>
<%@page import="edu.amrita.aview.gclm.entities.Course"%>
<%@page import="edu.amrita.aview.gclm.entities.Institute"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%!
	private static Logger logger = Logger.getLogger("com.amrita.edu.workshop_list.jsp");
%>

<%
final String COURSE_NAME = "National AVIEW Workshop";
final String INSTITITE_NAME = "Amrita E-Learning Research Lab";

Institute institute = InstituteHelper.getInstituteByName(INSTITITE_NAME);
Course course = null;
if(institute != null)
{
	List<Course> courses = CourseHelper.searchCourse(COURSE_NAME, null, institute.getInstituteId());
	logger.debug("Course is null. COURSE_NAME:"+courses.size());
	if(courses != null && courses.size() > 0)
	{
		for(Course tempCourse:courses)
		{
			if(tempCourse.getCourseName().equals(COURSE_NAME))
			{
				course = tempCourse;
				break;
			}
		}
	}
	else
	{
		logger.debug("Course is null. COURSE_NAME:"+COURSE_NAME+":"+institute.getInstituteId());
	}
}
else
{
	logger.debug("Institute is null");
}

List<Class>  classes = null;

if(institute != null && course != null)
{
	List<Integer> statuses = new ArrayList<Integer>();
	statuses.add(StatusHelper.getActiveStatusId());
	statuses.add(StatusHelper.getClosedStatusId());
	classes = ClassHelper.searchClass(institute.getInstituteId(),course.getCourseId(),null,null,statuses);
}


%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Workshop List</title>
</head>
<body>
	<form action="user_search.jsp" method="post">
		<%
		 	if(classes != null)
		 	{
				for(Class workshop:classes)
				{
			%>		
					<input type="radio" name="clsId" value="<%=workshop.getClassId()%>"><%=workshop.getClassName()%><br>
			<%
				}
			}
		%>

		<input type="submit" value="Register">
	</form>
</body>
</html>