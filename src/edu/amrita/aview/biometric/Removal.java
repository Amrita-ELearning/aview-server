/*
 * 
 */
package edu.amrita.aview.biometric;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

import edu.amrita.aview.common.utils.HibernateUtils;


/**
 * The Class Removal.
 */
public class Removal extends HttpServlet{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The username. */
	private String username;
	
	/** The password. */
	private String password;
	
	/** The database ip. */
	private String databaseIP;
	
	/** The database name. */
	private String databaseName;
	
	/** The port number. */
	private String portNumber;

	/** The connection. */
	private Connection connection = null;
	
	/**
	 * This method is used to initialize constant values.
	 *
	 * @param config the config
	 * @throws ServletException the servlet exception
	 */
	public synchronized void init(ServletConfig config) throws ServletException {
	    super.init(config);
	    StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		Properties properties = new EncryptableProperties(encryptor);
		try {
			properties.load(HibernateUtils.class.getClassLoader().getResourceAsStream("mysql.properties"));
		}
		catch (Exception e)
		{
			//System.out.println(e.getMessage());
		}
		username = properties.getProperty("mysql.user");
		encryptor.setPassword(username);
		password = properties.getProperty("mysql.password");
		databaseIP = properties.getProperty("mysql.databaseIP");
		databaseName = properties.getProperty("mysql.databaseName");
		portNumber = properties.getProperty("mysql.port");
	  }
	
	/**
	 * This method is used to detect the registered faces based on the userID.
	 *
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected synchronized void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String templateID = request.getParameter("templateID");
	
		String outputMessage = "";
		PrintWriter out = response.getWriter();
		
		String connectionURL = "jdbc:mysql://"+ databaseIP +":"+ portNumber +"/"+ databaseName;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL, username, password);
			PreparedStatement pstmt = connection.prepareStatement("UPDATE face_templates SET status_id = 2 WHERE template_id = " + templateID);
			pstmt.execute();
			outputMessage = "Deleted Successfully.";
		} catch (Exception e) {
			outputMessage = e.getMessage();
		}
		out.println(outputMessage);
	}

}
