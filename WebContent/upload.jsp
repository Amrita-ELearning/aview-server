<%@page import="org.apache.log4j.Logger"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.BufferedOutputStream"%>
<%@page import="java.io.BufferedInputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="com.oreilly.servlet.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!
private static Logger logger = Logger.getLogger("com.amrita.edu.Upload.jsp");

%>
<%
	String realPath = application.getRealPath("/");

	realPath = realPath.replaceAll("\\\\", "/");

	logger.debug("realPath:"+realPath);
	
	String rootFolder = realPath.substring(0, realPath.lastIndexOf("/", realPath.length()-2));

	String fileName = request.getParameter("fileName");
	String folderName = rootFolder+"/ROOT/"+request.getParameter("folderName");
	
		
	logger.debug("fileName:"+fileName+", folderName:"+folderName);
	
	File directory = new File(folderName);
	if(!directory.exists())
	{
		logger.debug("Directory does not exist:"+directory.getAbsolutePath());
		if(directory.mkdirs())
		{
			logger.debug("Created directory");
		}
		else
		{
			logger.debug("Could not create the directory");
		}
	}
	else
	{
		logger.debug("Directory exists");
	}
	
	MultipartRequest fileDownloader = new MultipartRequest(request,folderName);
	File file = fileDownloader.getFile(fileName);
	if(file != null)
	{
		logger.debug("File :"+file.getAbsolutePath()+": is downloaded sucessfully. Filesize:"+file.length());
	}
	else
	{
		logger.debug("File name :"+fileName+": is not uploaded");
	}
	
	/*
	BufferedInputStream bs = new BufferedInputStream(request.getInputStream());
	BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(new File(folderName+"/"+fileName)));
	
	byte[] readA = new byte[4096];
	int readLen = 0;
	while((readLen = bs.read(readA)) != -1)
	{
		logger.debug("Reading and writing bytes :"+readLen);
		bo.write(readA, 0, readLen);
	}
	logger.debug("Finished writing");
	bo.close();
	bs.close();
	*/
	
%>
<!-- html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

</body>
</html -->