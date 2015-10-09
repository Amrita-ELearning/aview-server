/*
 * 
 */
package edu.amrita.aview.biometric;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

import com.neurotec.biometrics.NLExtractor;
import com.neurotec.biometrics.NLTemplate;

import edu.amrita.aview.common.utils.Base64Utils;
import edu.amrita.aview.common.utils.HibernateUtils;


/**
 * The Class Matcher.
 */
public class Matcher extends HttpServlet{

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
	
	/** The images path. */
	private String imagesPath;

	/** The connection. */
	private Connection connection = null;
	
	/** The rs. */
	private ResultSet rs = null;
	
	/** The records list. */
	private List<Templates> recordsList;
	
	/** The license state. */
	private boolean licenseState = false;
	
	/** The output message. */
	private String outputMessage = null;
	
	/** The biometric login main. */
	private BiometricManager biometricLoginMain = null;
	
	/** The buffered image. */
	private BufferedImage bufferedImage = null;

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
		try {
			mysql_properties.load(HibernateUtils.class.getClassLoader().getResourceAsStream("mysql.properties"));
			biometric_properties.load(HibernateUtils.class.getClassLoader().getResourceAsStream("biometric.properties"));
		}catch (Exception e){
			outputMessage = e.getMessage();
		}
		username = mysql_properties.getProperty("mysql.user");
		encryptor.setPassword(username);
		password = mysql_properties.getProperty("mysql.password");
		databaseIP = mysql_properties.getProperty("mysql.databaseIP");
		databaseName = mysql_properties.getProperty("mysql.databaseName");
		portNumber = mysql_properties.getProperty("mysql.port");
		imagesPath = biometric_properties.getProperty("biometric.imagesPath");
	    try {
			licenseState = LicenseManager.getInstance().obtain();
			if(licenseState){
				biometricLoginMain = new BiometricManager();
				biometricLoginMain.initializeMatcher();
			}
	    }catch (Exception e) {
			outputMessage = e.getMessage();
		} 
	  }
	
	/**
	 * This method receives the input parameters from client and extract the face features
	 * from the input image. Finally it sends the matched faceID or error message to the client.
	 *
	 * @param request the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected synchronized void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userID = request.getParameter("userID");
		String imageData = request.getParameter("image_data");

		String imageFileDirectory = imagesPath + "/" + userID;
		String imageFilePath = imageFileDirectory + "/" + userID + ".png";
		String matchedUserID = null;
		String matchedTemplateID = null;
		NLTemplate faceTemplate = null;
		List<Templates> dataList;
		PrintWriter out = response.getWriter();
		
		String connectionURL = "jdbc:mysql://"+ databaseIP +":"+ portNumber +"/"+ databaseName;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL, username, password);
			Statement select = connection.createStatement();
			rs = select.executeQuery("SELECT * FROM face_templates WHERE user_id = " + userID + " AND status_id=1");
			
			recordsList = new ArrayList<Templates>();
			while (rs.next()) {
				Templates fr = new Templates();
				fr.setTemplateId(rs.getInt("template_id"));
				fr.setUserId(rs.getInt("user_id"));
				File templateFile = new File(rs.getString("templatefile_path") + "/" + rs.getString("templatefile_name"));
				if(templateFile.exists()){
					FileInputStream fileinputstream = new FileInputStream(rs.getString("templatefile_path") + "/" + rs.getString("templatefile_name"));
		            int numberBytes = fileinputstream.available();
		            byte templateByteArray[] = new byte[numberBytes];
		            fileinputstream.read(templateByteArray);
		            fileinputstream.close();
					fr.setFaceTempalte(templateByteArray);
					recordsList.add(fr);
				}else{
					PreparedStatement pstmt = connection.prepareStatement("UPDATE face_templates SET status_id = 2 WHERE template_id = " + rs.getInt("template_id"));
					pstmt.execute();
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		if(recordsList.size() != 0){
			try {
		    	bufferedImage = ImageIO.read(new ByteArrayInputStream(Base64Utils.decode(imageData)));
		    	File file = new File(imageFileDirectory);
				if (!file.isDirectory()){
					file.mkdirs();
				}
			   	ImageIO.write(bufferedImage, "png", new File(imageFilePath));
			} catch (Exception e) {
		    	e.printStackTrace();
			}
			
			if(licenseState){
				NLExtractor.ExtractResult extractionResult = biometricLoginMain.detectFacialFeatures(imageFilePath);
				if(extractionResult != null){
					faceTemplate = extractionResult.getTemplate();
					if(faceTemplate != null){
						dataList = biometricLoginMain.detectFace(faceTemplate, recordsList);
						if(dataList != null){
							for (Templates record : dataList) {
								matchedUserID = "" + record.getUserId();
								matchedTemplateID = "" + record.getTemplateId();
							}
							outputMessage = "Matched Successfully";
						}else{
							outputMessage = "Face verification failed.";
						}
						faceTemplate.dispose();
						extractionResult.getTemplate().dispose();
					}else{
						outputMessage = "Face detection failed. Try again.";
					}
				}else{
					outputMessage = "Face detection failed. Try again.";
				}
			}else{
				outputMessage = "License not obtained";
			}
		}else{
			outputMessage = "Face not registered for this user. \nLogin through Normal mode";
		}
		out.println(outputMessage+ "&" + matchedUserID + "&" + matchedTemplateID);
	}
}
