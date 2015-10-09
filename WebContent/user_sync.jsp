<%@page import="edu.amrita.aview.gclm.entities.ClassRegistration"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="edu.amrita.aview.gclm.helpers.UserHelper"%>
<%@page import="edu.amrita.aview.gclm.helpers.CourseHelper"%>
<%@page import="edu.amrita.aview.gclm.helpers.ClassHelper"%>
<%@page import="edu.amrita.aview.gclm.helpers.InstituteHelper"%>
<%@page import="edu.amrita.aview.common.helpers.StatusHelper"%>
<%@page import="edu.amrita.aview.gclm.helpers.ClassRegistrationHelper"%>
<%@page import="edu.amrita.aview.gclm.helpers.NodeTypeHelper"%>
<%@page import="edu.amrita.aview.gclm.entities.User"%>
<%@page import="edu.amrita.aview.gclm.entities.Class"%>
<%@page import="edu.amrita.aview.gclm.entities.Course"%>
<%@page import="edu.amrita.aview.gclm.entities.Institute"%>
<%@page import="edu.amrita.aview.common.AViewException"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%!
private static Logger logger = Logger.getLogger("com.amrita.edu.user_sync.jsp");

private static final String INSTITUTE_NAME = "Indian Institute of Technology Bombay";
private static final String IITB_ADMIN_USER_NAME = "iitb_admin";
private static final String WORKSHOP_NAME = "Akash Workshop Class";
private static final String COURSE_NAME = "Akash Workshop";

private ClassRegistration getClassRegistration(Class workshop,User user) throws AViewException
{
	ClassRegistration cr = new ClassRegistration();
	cr.setAviewClass(workshop);
	cr.setUser(user);
	cr.setIsModerator("N");
	cr.setNodeTypeId(NodeTypeHelper.getClassroomNodeType());
	
	return cr;
}

private void populateUser(User user,String userName,HttpServletRequest request)
{
	user.setUserName(userName);
	user.setFname(request.getParameter("Fname"));
	user.setLname(request.getParameter("Lname"));
	user.setPassword(request.getParameter("Password"));
	user.setMobileNumber(request.getParameter("MobileNumber"));
	user.setRole(request.getParameter("Role"));
	user.setCity(request.getParameter("Location"));
	
	if(request.getParameter("DistrictId") != null)
	{
		user.setDistrictId(Integer.parseInt(request.getParameter("DistrictId")));
	}
	user.setInstituteId(Long.parseLong(request.getParameter("InstituteId")));
	user.setEmail(request.getParameter("Email"));
}

%>

<%
String message = null;

Map<String,String> paramMap = request.getParameterMap();
Set<String> keySet = paramMap.keySet();
logger.info("Parameters Size:"+keySet.size());
for(String param:keySet)
{
	logger.info("Param name:"+param+": value:"+request.getParameter(param));
}

String operation = request.getParameter("Operation");
String userName = request.getParameter("UserName");
String syncPassword = request.getParameter("SyncPassword");

User modifyingUser = UserHelper.getUserByUserNamePassword(IITB_ADMIN_USER_NAME,syncPassword);

Institute institute = InstituteHelper.getInstituteByName(INSTITUTE_NAME);
Course course = null;
Class workshop = null;

if(institute == null)
{
	message = "Error: Institute with name :"+INSTITUTE_NAME+" is not found. Could not proceed with Sync operation.";
}
else
{
	List<Course> courses = CourseHelper.searchCourse(COURSE_NAME,null,institute.getInstituteId());
	if(courses != null && courses.size() > 0)
	{
		List<Class> workshops = ClassHelper.searchClass(institute.getInstituteId(), courses.get(0).getCourseId(), WORKSHOP_NAME);
		
		if(workshops == null || workshops.size() == 0)
		{
			message = "Error: Class with name :"+WORKSHOP_NAME+" is not found. Could not proceed with Sync operation.";
		}
		else
		{
			workshop = workshops.get(0);
		}
	}
	else
	{
		message = "Error: Course with name :"+COURSE_NAME+" is not found. Could not proceed with Sync operation.";
	}
}


//Validations..
if(message == null && userName == null || userName.trim().length() == 0)
{
	message = "Error: Username is Empty. Could not proceed with Sync operation.";
}

if(message == null && modifyingUser == null)
{
	message = "Error: Admin User with Username :iitb_admin: is not found. Could not proceed with Sync operation.";
}

if(message == null && operation.equals("DELETE"))
{
	User user = UserHelper.getUserByUserName(userName);
	try
	{
		if(user != null)
		{
			UserHelper.deleteUser(user.getUserId(),modifyingUser.getUserId());
			message = "Success: Deleted the user with userName :"+userName+": id:"+user.getUserId();
		}
		else
		{
			message = "Error: User with Username :"+userName+": is not found. Could not delete the user.";
		}
	}
	catch(AViewException ae)
	{
		message = "Error: Could not delete the user. Message:"+ae.toString();
		logger.error(message,ae);
	}
}
else if(message == null && operation.equals("UPDATE"))
{
	String oldUserName = request.getParameter("OldUserName");

	//Validations..
	if(oldUserName == null || oldUserName.trim().length() == 0)
	{
		message = "Error: Username is Empty. Could not proceed with Update operation.";
	}

	User user = UserHelper.getUserByUserName(oldUserName);
	if(user != null)
	{
		populateUser(user,userName,request);
		try
		{
			UserHelper.updateUser(user,modifyingUser.getUserId());

			List<ClassRegistration> crs = ClassRegistrationHelper.searchForClassRegisterForUser(user.getUserId(), workshop.getClassId(), null, null, null);
			if(crs == null || crs.size() == 0)
			{
				String crMsg = ClassRegistrationHelper.createClassRegistration(getClassRegistration(workshop,user), modifyingUser.getUserId(), StatusHelper.getActiveStatusId());
				
				if(crMsg != null && crMsg.indexOf("Success") != -1)
				{
					message = "Success: Updated the user with with details."+user+". User successfully registered for class :"+WORKSHOP_NAME;
				}
				else
				{
					message = "Error: Updated the user with with details."+user+
							". But failed to register user for class :"+WORKSHOP_NAME+". Registration Message:"+crMsg;
				}
			}
			else
			{
				message = "Success: Updated the user with with details."+user+". User alredy registered for class :"+WORKSHOP_NAME;
			}

		}
		catch(AViewException ae)
		{
			message = "Error: Could not update the user with details."+user+", Message:"+ae.toString();
			logger.error(message,ae);
		}
		
	}
	else
	{
		message = "Error: User with Username :"+oldUserName+": is not found. Could not update the user.";
	}
	
}
else if(message == null && operation.equals("CREATE"))
{

	User user = new User();
	populateUser(user,userName,request);

	
	try
	{
		UserHelper.createUser(user,modifyingUser.getUserId(),StatusHelper.getActiveStatusId());

		String crMsg = ClassRegistrationHelper.createClassRegistration(getClassRegistration(workshop,user), modifyingUser.getUserId(), StatusHelper.getActiveStatusId());
		
		if(crMsg == null)
		{
			message = "Success: Created the user with with details."+user+". User successfully registered for class :"+WORKSHOP_NAME;
		}
		else
		{
			message = "Error: Created the user with with details."+user+
					". But failed to register user for class :"+WORKSHOP_NAME+". Registration Message:"+crMsg;
		}
		
	}
	catch(AViewException ae)
	{
		message = "Error: Could not create the user with details."+user+", Message:"+ae.toString();
		logger.error(message,ae);
	}
}
else if(message == null)
{
	message = "Error: Invalid Operation :"+operation+". Could not proceed with Sync operation.";
}

%>

<%=message%>
