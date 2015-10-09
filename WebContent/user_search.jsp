<%@page import="org.apache.log4j.Logger"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%!
private static Logger logger = Logger.getLogger("com.amrita.edu.user_search.jsp");
%>

<%

String classIdStr = request.getParameter("clsId");
Long classId = null;
if(classIdStr == null)
{
	classId = (Long)session.getAttribute("WorkShopId");
}
else
{
	classId = Long.parseLong(classIdStr);
	session.setAttribute("WorkShopId",classId);
}
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User Search</title>
</head>
<body>
	<form action="user_validate.jsp" method="post">
		User Name: <input type="text" name="userName"><br>
		Password: <input type="password" name="password"><br>
		<input type="submit" value="Validate">
	</form>
</body>
</html>