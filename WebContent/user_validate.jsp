<%@page import="org.apache.log4j.Logger"%>
<%@page import="edu.amrita.aview.gclm.helpers.UserHelper"%>
<%@page import="edu.amrita.aview.gclm.helpers.ClassHelper"%>
<%@page import="edu.amrita.aview.gclm.helpers.ClassRegistrationHelper"%>
<%@page import="edu.amrita.aview.gclm.entities.ClassRegistration"%>
<%@page import="edu.amrita.aview.common.helpers.StatusHelper"%>
<%@page import="edu.amrita.aview.gclm.entities.User"%>
<%@page import="edu.amrita.aview.gclm.entities.Class"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%!
private static Logger logger = Logger.getLogger("com.amrita.edu.user_validate.jsp");
%>

<%
String userName = request.getParameter("userName");
String password = request.getParameter("password");

Long classId = (Long)session.getAttribute("WorkShopId");

User user = null;
Class workShop = null;

if(classId != null)
{
	workShop = ClassHelper.getClass(classId);	
	if(workShop != null)
	{
		session.setAttribute("Class",workShop);
	}
}

if(userName != null)
{
	user = UserHelper.getUserByUserNamePassword(userName,password);
	if(user != null)
	{
		session.setAttribute("User",user);
	}
}

List<ClassRegistration> existingRegistration = null;
if(user != null && workShop != null)
{
	existingRegistration = 
	ClassRegistrationHelper.searchForClassRegisterForUser(
		user.getUserId(),workShop.getClassId(),null,null,null);
}

String errorMessage = null;
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User Validation</title>
</head>
<body>
	<%
		if(workShop != null && user != null)
		{
			if(workShop.getStatusId()  == StatusHelper.getActiveStatusId())
			{
	%>
				<b>User Found</b><br>
				Name: <%=user.getFname()+" "+user.getLname()%><br>
				Institute: <%=user.getInstituteName()%><br>
				User Name: <%=user.getUserName()%><br>
				Email: <%=user.getUserName()%><br>
				Mobile: <%=user.getMobileNumber()%><br>

				<%
				if(existingRegistration == null || existingRegistration.size() == 0)
				{
				%>
					Click below Register button for workshop '<%=workShop.getClassName()%>' registration.
					<form action="class_registration.jsp" method="post">
						<input type="hidden" name="userId" value=<%=user.getUserId()%> visible="false">
						<input type="hidden" name="classId" value=<%=classId%> visible="false">
						<input type="submit" name="Register" value="Register">
					</form>
				<%
				}
				else
				{
				%>
					<b>Your User Name '<%=user.getUserName()%>' is already registered for the Workshop '<%=workShop.getClassName()%>'</b>
				<%
				}
				%>
	<%
			}
			else
			{
				errorMessage = "The workshop '"+workShop.getClassName()+"' registration is closed.";
			}
		}
		else if(userName == null)
		{
			errorMessage = "Improper submission. User name is not provided";
		}
		else if(user == null)
		{
			errorMessage = "Active user is not found for User Name:'"+userName+"'<br>"+
			"If you have alredy registered, please wait for few hours till it gets activated and then try again <br>"+
			"If you have not registerd, please register <a href='http://aview.in/registration/download.php'>here</a> <br>"+
			"If you want to retry now with a different User Name and/or Password, please <a href='user_search.jsp'>go back</a>.";
		}
		else if(classId == null)
		{
			errorMessage = "Improper submission. Workshop is not selected.";
		}
		else if(workShop == null)
		{
			errorMessage = "Workshop is not found. Please contact administrator at aview@amrita.edu";
		}
	%>
	
	<%
		if(errorMessage != null)
		{
	%>
			<b>We are Sorry!!!. <%=errorMessage%></b>
	<%
		}
	%>
</body>
</html>