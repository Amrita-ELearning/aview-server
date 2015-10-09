<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	File file = new File("/"+request.getParameter("filePath"));
%>
<%=file.exists()%>
<!--html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Check File</title>
</head>
<body>
	Selected file '<%=request.getParameter("filePath") %>' <%=((file.exists())?"Exists" : "Doesnot Exist")%>
</body>
</html-->