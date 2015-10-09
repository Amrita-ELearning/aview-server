/*
 * 
 */
package edu.amrita.aview.biometric;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
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
 * The Class Extractor.
 */
public class Extractor extends HttpServlet{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The license state. */
	private boolean licenseState = false;
	
	/** The output message. */
	private String outputMessage = null;
	
	/** The biometric login main. */
	private BiometricManager biometricLoginMain = null;
	
	/** The records list. */
	private List<Templates> recordsList;
	
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
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
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
	 * from the input image. Finally it sends the extracted face icon or error message to the client.
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
		BufferedImage faceImageInput = null;
		BufferedImage faceImageOutput = null;
		NLTemplate faceTemplate = null;
		String faceImageString = null;
		String faceTemplateString = null;
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
			if(recordsList.size() < 5){
				try {
					faceImageInput = ImageIO.read(new ByteArrayInputStream(Base64Utils.decode(imageData)));
			    	File file = new File(imageFileDirectory);
					if (!file.isDirectory()){
						file.mkdirs();
					}
				   	ImageIO.write(faceImageInput, "png", new File(imageFilePath));
				} catch (Exception e) {
			    	e.printStackTrace();
				} 
			
				if(licenseState){
					NLExtractor.ExtractResult extractionResult = biometricLoginMain.detectFacialFeatures(imageFilePath);
					if(extractionResult != null){
						faceTemplate = extractionResult.getTemplate();
						if(faceTemplate != null){
							dataList = biometricLoginMain.detectFace(faceTemplate, recordsList);
							if(((dataList != null) && (dataList.size() != 0)) || (recordsList.size() == 0)){
								ByteBuffer buffer = faceTemplate.save();
								faceTemplate.dispose();
								extractionResult.getTemplate().dispose();
								byte[] faceTemplateBytes = new byte[buffer.capacity()];
								buffer.clear();
								buffer.get(faceTemplateBytes, 0, faceTemplateBytes.length);
								faceTemplateString = Base64Utils.encode(faceTemplateBytes);
								
								faceImageOutput = biometricLoginMain.drawFace(faceImageInput, extractionResult.getDetectionDetails().getFace());
								ByteArrayOutputStream faceImageBytes = new ByteArrayOutputStream();
								ImageIO.write( faceImageOutput, "png", faceImageBytes );
								faceImageBytes.flush();
								faceImageString = Base64Utils.encode(faceImageBytes.toByteArray());
								outputMessage = "Enrolled Successfully";
							}else{
								outputMessage = "Similarity checking failed. This face does not match with the previously registered face for this user.";
							}
							
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
				outputMessage = "You cannot register more than five faces.";
			}

		} catch (Exception e) {
			outputMessage = e.getMessage();
		} 
		
		out.println(outputMessage+ "&" + faceTemplateString + "&" + faceImageString);
		
	}
	
}
