<%@page import="org.apache.log4j.Logger"%>
<%@page import="edu.amrita.aview.gclm.helpers.UserHelper"%>
<%@page import="edu.amrita.aview.gclm.helpers.ClassHelper"%>
<%@page import="edu.amrita.aview.gclm.helpers.ClassRegistrationHelper"%>
<%@page import="edu.amrita.aview.gclm.helpers.NodeTypeHelper"%>
<%@page import="edu.amrita.aview.common.helpers.StatusHelper"%>
<%@page import="edu.amrita.aview.gclm.entities.User"%>
<%@page import="edu.amrita.aview.gclm.entities.Class"%>
<%@page import="edu.amrita.aview.gclm.entities.ClassRegistration"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%!
private static Logger logger = Logger.getLogger("com.amrita.edu.class_registration.jsp");
%>

<%
String errorMessage = null;
String successMessage = null;
User user = (User)session.getAttribute("User");

Class workShop = (Class)session.getAttribute("Class");

if(user != null && workShop != null)
{
	ClassRegistration clr = new ClassRegistration();
	clr.setIsModerator("N");
	clr.setAviewClass(workShop);
	clr.setUser(user);
	clr.setNodeTypeId(NodeTypeHelper.getClassroomNodeType());
	ClassRegistrationHelper.createClassRegistration(clr,user.getUserId(),StatusHelper.getActiveStatusId());
	
	if(clr.getClassRegisterId() != 0)
	{
		successMessage = "You have successfully registered for workshop '"+workShop.getClassName()+"'";
	}
	else
	{
		errorMessage = "Workshop '"+workShop.getClassName()+"' registration failed. Please contact administrator at aview@amrita.edu";
	}
}
else
{
	errorMessage = "Unable ot register for workshop '"+workShop.getClassName()+"'. Session expired. Please start again.";
}

%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User Registration</title>
</head>
<body>
	<%
		if(errorMessage != null)
		{
	%>
			<b>We are Sorry!!!. <%=errorMessage%></b>
	<%
		}
		else if(successMessage != null)
		{
	%>
			<b>Success. <%=successMessage%></b>
	<%
		}
	%>
</body>
</html>