/*
 * 
 */
package edu.amrita.aview.biometric;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

import edu.amrita.aview.common.utils.Base64Utils;
import edu.amrita.aview.common.utils.HibernateUtils;


/**
 * The Class Profiler.
 */
public class Profiler extends HttpServlet{

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
	
	/** The rs. */
	private ResultSet rs = null;
	
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
		String userID = request.getParameter("userID");
		
		String outputMessage = "";
		PrintWriter out = response.getWriter();
		
		String connectionURL = "jdbc:mysql://"+ databaseIP +":"+ portNumber +"/"+ databaseName;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL, username, password);
			Statement select = connection.createStatement();
			rs = select.executeQuery("SELECT * FROM face_templates WHERE user_id = " + userID + " AND status_id=1");
			while (rs.next()) {
				File iconFile = new File(rs.getString("iconfile_path") + "/" + rs.getString("iconfile_name"));
				if(iconFile.exists()){
					BufferedImage faceIcon = ImageIO.read(iconFile);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write( faceIcon, "png", baos );
					baos.flush();
					byte[] iconByteArray = baos.toByteArray();
					baos.close();
					outputMessage = outputMessage + rs.getInt("template_id") + "&" + Base64Utils.encode(iconByteArray) + "&";
				}else{
					PreparedStatement pstmt = connection.prepareStatement("UPDATE face_templates SET status_id = 2 WHERE template_id = " + rs.getInt("template_id"));
					pstmt.execute();
				}
			}
			outputMessage = outputMessage.substring(0, outputMessage.length() - 1);
		}catch (Exception e) {
			outputMessage = e.getMessage();
		}
		out.println(outputMessage);
	}

}
