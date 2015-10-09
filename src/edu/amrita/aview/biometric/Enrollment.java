/*
 * 
 */
package edu.amrita.aview.biometric;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
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
 * The Class Enrollment.
 */
public class Enrollment extends HttpServlet{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The connection. */
	private Connection connection = null;
	
	/** The rs. */
	private ResultSet rs = null;
	
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
	
	/** The images path. */
	private String imagesPath;
	
	/**
	 * This method is used to initialize constant values.
	 *
	 * @param config the config
	 * @throws ServletException the servlet exception
	 */
	public synchronized void init(ServletConfig config) throws ServletException {
	    super.init(config);
	    StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		Properties mysql_properties = new EncryptableProperties(encryptor);
		Properties biometric_properties = new EncryptableProperties(encryptor);
		try 
		{
			mysql_properties.load(HibernateUtils.class.getClassLoader().getResourceAsStream("mysql.properties"));
			biometric_properties.load(HibernateUtils.class.getClassLoader().getResourceAsStream("biometric.properties"));
		}
		catch (Exception e)
		{
			//System.out.println(e.getMessage());
		}
		username = mysql_properties.getProperty("mysql.user");
		encryptor.setPassword(username);
		password = mysql_properties.getProperty("mysql.password");
		databaseIP = mysql_properties.getProperty("mysql.databaseIP");
		databaseName = mysql_properties.getProperty("mysql.databaseName");
		portNumber = mysql_properties.getProperty("mysql.port");
		imagesPath = biometric_properties.getProperty("biometric.imagesPath");
	  }
	
	/**
	 * This method receives the input parameters from client and insert those values into the database.
	 *
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected synchronized void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userID = request.getParameter("userID");
		String faceData = request.getParameter("faceData");
		String[] faceImageData = faceData.split("&");
		PrintWriter out = response.getWriter();
		String outputMessage = null;
		
		String connectionURL = "jdbc:mysql://"+ databaseIP +":"+ portNumber +"/"+ databaseName;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL, username, password);
			PreparedStatement pstmt = connection.prepareStatement("Select MAX(template_id) from face_templates");
			int maxID = 0;
			rs = pstmt.executeQuery();
			while(rs.next()){
				maxID = rs.getInt(1);
			}
			pstmt = connection.prepareStatement("INSERT INTO face_templates VALUES (?,?,?,?,?,?,?,?,?,?,?)");
			Calendar calendar = Calendar.getInstance();
			java.sql.Date creationDate = new java.sql.Date( calendar.getTime().getTime() );
				
			String filePath = imagesPath + "/" + userID+ "/" + (maxID + 1);
			File file = new File(filePath);
			if (!file.isDirectory()){
				file.mkdirs();
			}
			String templateFileName = "template.bin";
			String iconFileName = "icon.png";
				
			FileOutputStream fos = new FileOutputStream(filePath + "/" + templateFileName);
			fos.write(Base64Utils.decode(faceImageData[0]));
			fos.close();
				
			BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(Base64Utils.decode(faceImageData[1])));
			ImageIO.write(bufferedImage, "png", new File(filePath + "/" + iconFileName));
				
			pstmt.setInt(1, maxID + 1);
			pstmt.setInt(2, Integer.parseInt(userID));
			pstmt.setString(3, filePath);
			pstmt.setString(4, templateFileName);
			pstmt.setString(5, filePath);
			pstmt.setString(6, iconFileName);
			pstmt.setInt(7, Integer.parseInt(userID));
			pstmt.setDate(8, creationDate);
			pstmt.setInt(9, Integer.parseInt(userID));
			pstmt.setDate(10, creationDate);
			pstmt.setInt(11, 1);
			pstmt.execute();
			outputMessage = "Registered Successfully.";
		} catch (Exception e) {
			outputMessage = e.getMessage();
		} 
		out.println(outputMessage);
	}

}
