package edu.amrita.aview.documentsharing.helpers;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.mortbay.util.UrlEncoded;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.gclm.entities.Class;
import edu.amrita.aview.gclm.entities.ClassServer;
import edu.amrita.aview.documentsharing.entities.DocumentConversion;
import edu.amrita.aview.gclm.entities.User;
import edu.amrita.aview.gclm.helpers.ClassHelper;
import edu.amrita.aview.gclm.helpers.ClassRegistrationHelper;
import edu.amrita.aview.gclm.helpers.UserHelper;

import com.sun.jersey.api.client.ClientResponse.Status;

@Controller
public class DocumentUploadHelper {
	private static Logger logger = Logger.getLogger(DocumentUploadHelper.class);
	
	//Document upload failure message from uploadApi.php
	private String	UPLOAD_FAULT_RESPONSE = "Error in uploading the file";
	private String	CLASS_FAULT_RESPONSE = "Given class Id is not valid or does not exist";
	
	//Document upload success message from uploadApi.php
	//private String UPLOAD_SUCCESS_RESPONSE = "uploaded Successfully";
	@RequestMapping(value = "/fileupload.html", method = RequestMethod.POST)
	@ResponseBody
	public Response uploadFile(@RequestParam("userId") Long userId,
			@RequestParam("file") MultipartFile multipartFile,
			@RequestParam("classId") Long classId,
			@RequestParam("isAnimated") String isAnimated,
			@RequestParam("overwrite") String overwrite)
			throws AViewException {
		
		logger.debug("Entered all fileuplaod details on success::all fileupload details");
		DocumentConversion docConversion = new DocumentConversion();
		User user = null;
		HttpClient httpclient = null;
		BufferedOutputStream bos = null;
		//File uploadFile = null;
		Class aviewClass = null;
		String errorMessage = "";
		String responseMessage = new String();
		String animated = new String();
		String contentServerIp = "";
		
		overwrite = overwrite.trim();
		if(overwrite.equals("") && overwrite.length()== 0)
		{
			//If the overwrite value is not send, No is taken as default
			overwrite = "n";
		}
		else
		{
			overwrite = overwrite.toUpperCase();
			if(!overwrite.equals("Y") && !overwrite.equals("N"))
			{
				return Response	.status(Status.BAD_REQUEST).entity("Overwrite should be Y/N/y/n.").build();
			}
			
		}
		String animatedDocument = isAnimated.trim();
		if(animatedDocument!= null && animatedDocument.length()!= 0)
		{
			animated = animatedDocument.toUpperCase();
			if(!animated.equals("Y") && !animated.equals("N"))
			{
				responseMessage = "Animated should be 'y' or 'n'";
				return Response.status(Status.BAD_REQUEST).entity(responseMessage).build();
			}
		}
		else
		{
			animated = "N";						
		}
		docConversion.setIsAnimatedDocument(animated);
		Object aviewUser = UserHelper.userValidCheck(Constant.USER_ID,userId);
		if (!User.class.isInstance(aviewUser)) 
		{
			errorMessage = aviewUser.toString();
			return Response.status(Status.BAD_REQUEST).entity(errorMessage)
					.build();
		}
		user = (User) aviewUser;
/*		if(user.getStatusId().equals(StatusHelper.getActiveStatusId()))
		{
			if (!user.getRole().equals(Constant.TEACHER_ROLE)) 
			{
				return Response.status(Status.BAD_REQUEST)
						.entity("Only Moderator can upload document").build();
			}
		}
		else
		{
			return Response.status(Status.BAD_REQUEST).entity("Moderator is not valid or doesn't exist").build();
		}*/		
		try 
		{
			//Currently destinationFolderPath is hard coded here, for the next release this can be taken from a property file.
			String destinationFolderPath = "../../../AVContent/Upload/Personal/"+user.getUserName()+"/My Documents/@@-OriginalDocs-@@";
			String pathToCheckfileExistence= "../../AVContent/Upload/Personal/"+user.getUserName()+"/My Documents/@@-OriginalDocs-@@";
			
			if(classId != null && classId != 0)
			{
				Object resultObjectClass = ClassHelper.classValidCheck(classId);
				if(Class.class.isInstance(resultObjectClass))
				{
					aviewClass = (Class)resultObjectClass;
				}
				else
				{
					errorMessage = resultObjectClass.toString();
					return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
				}
				String serverTypeName = "";
				List classRegistration = ClassRegistrationHelper.searchForClassRegisterForUser(userId, classId, null, 0l, 0l);
				if(classRegistration.isEmpty())
				{
					return Response.status(Status.BAD_REQUEST).entity("Upload failed, user not enrolled to the class").build();
				}
				Set<ClassServer> classServers = aviewClass.getClassServers();
				boolean hasContentServer = false;
				if(classServers != null)
				{
					for (ClassServer classServer : classServers) 
					{
						serverTypeName = classServer.getServerTypeName();
						if (serverTypeName.equals(ClassHelper.CONTENT_SERVER)) 
						{
							//Check if this is required
							if((classServer.getServer() != null) && 
								(classServer.getServer().getStatusId().equals(StatusHelper.getActiveStatusId())))
							{
								contentServerIp = classServer.getServer().getServerIp();
								hasContentServer = true;
								break;
							}
						}						
					}
				}
				if(hasContentServer == false)
				{
					//return Response.status(Status.BAD_REQUEST).entity("Internal Error, please contact administrator").build();
					return Response.status(Status.BAD_REQUEST).entity("Internal Error with server allocation. Please contact the administrator").build();
				}
			}
			else
			{
				return Response.status(Status.BAD_REQUEST).entity(CLASS_FAULT_RESPONSE).build();
			}
			/*if(!aviewClass.getUser().getUserId().equals(user.getUserId()) || !(aviewClass.getIsModerator().equals("Y")))
			{
				return Response.status(Status.BAD_REQUEST).entity("User is not the moderator for the given class").build();
			}*/
			//File upload portion
			String fileName = multipartFile.getOriginalFilename().trim();
			String pathExtension = fileName.substring(fileName.lastIndexOf(".") + 1).trim();
			fileName = fileName.replace(" ", "_");
			httpclient = new DefaultHttpClient();
			if (Constant.docExtensions.contains(pathExtension))
			{		
				/*
				String userHome = System.getProperty("user.home");
				InputStream inputStream = multipartFile.getInputStream();
				uploadFile = new File(userHome + File.separator + fileName);
				byte[] byt = new byte[1024];
				bos = new BufferedOutputStream(new FileOutputStream(uploadFile));
				while (inputStream.read(byt) != -1) 
				{
					bos.write(byt);
				}
				bos.flush();
				logger.debug("Absolute Path "+ uploadFile.getAbsolutePath());"+conetentServerIp+"
				*/
				HttpPost fileUploadRequest = new HttpPost("http://"+contentServerIp+"/AVScript/Upload/Windows/print2flashforApi.php");			
				InputStreamBody uploadFilePart = new InputStreamBody(multipartFile.getInputStream(), "application/octet-stream", fileName);
				MultipartEntity multipartEntity = new MultipartEntity();
				multipartEntity.addPart("Filedata", uploadFilePart);
				/*
				ContentBody contentFile = new FileBody(uploadFile);
				multipartEntity.addPart("Filedata", contentFile);
				*/
				multipartEntity.addPart("folderPath",new StringBody(destinationFolderPath));
				multipartEntity.addPart("userId",new StringBody(user.getUserId().toString()));
				fileUploadRequest.setEntity(multipartEntity);
				// file existence  check
				destinationFolderPath = UrlEncoded.encodeString(pathToCheckfileExistence);
				HttpPost fileExistRequest = new HttpPost("http://"+contentServerIp+"/AVScript/Upload/checkFileExistance.php?filePath=" + destinationFolderPath + "/" + fileName);
				HttpResponse fileExistResponse = httpclient.execute(fileExistRequest);
				responseMessage =  EntityUtils.toString(fileExistResponse.getEntity());
				if((responseMessage.equals("Error: File already exists")))
				{	
					if(overwrite.equalsIgnoreCase("Y"))
					{
						HttpResponse fileUploadResponse = httpclient.execute(fileUploadRequest);
						HttpEntity resultEntity = fileUploadResponse.getEntity();
						if(resultEntity.equals(null))
						{
							return Response.status(Status.BAD_REQUEST).entity("File uploading failed").build();
						}
						responseMessage = EntityUtils.toString(resultEntity);
						if(responseMessage.indexOf("Finished Converting") > 0)
						{
							responseMessage = "File replaced successfully";
						}
						else// if(responseMessage.indexOf(UPLOAD_FAULT_RESPONSE) > 0)
						{
							return Response.status(Status.BAD_REQUEST).entity("File uploading  Failed").build();
						}
					}
					else if(overwrite.equalsIgnoreCase("N"))
					{
						return Response.status(Status.BAD_REQUEST).entity("File already exists").build();
					}					
				}
				else if( responseMessage.equals("Error:Please specify a File with Path"))
				{
					return Response.status(Status.BAD_REQUEST).entity(responseMessage).build();
				}
				else
				{
					HttpResponse fileUploadResponse = httpclient.execute(fileUploadRequest);
					HttpEntity resultEntity = fileUploadResponse.getEntity();
					if(resultEntity.equals(null))
					{
						return Response.status(Status.BAD_REQUEST).entity("File uploading failed").build();
					}
					responseMessage = EntityUtils.toString(resultEntity);
					//int index = responseMessage.indexOf(UPLOAD_FAULT_RESPONSE);
					//if(index>0)
					if(responseMessage.indexOf(UPLOAD_FAULT_RESPONSE) > 0)
					{
						return Response.status(Status.BAD_REQUEST).entity("File uploading  Failed").build();
					}
					if(responseMessage.indexOf("Finished Converting") > 0)
					{
						responseMessage = "File uploaded successfully";
					}
					else
					{
						return Response.status(Status.BAD_REQUEST).entity("File uploading  Failed").build();
					}
				}
			}
			else 
			{
				return Response.status(Status.BAD_REQUEST)
						.entity("Fileformat not supported.").build();
			}
			return Response.status(Status.OK).entity(responseMessage).build();
		}
		catch (IOException ioe) 
		{
			return Response.status(Status.BAD_REQUEST).entity(ioe.getMessage()).build();
		} 
		catch(AViewException ae)
		{
			String result = null;
			if(ae.getMessage().equals("Given course is not valid"))
			{
				result = "Class Id doesn't exist or not valid"	;
			}
			logger.debug("Exit upload details on error during log::fileupload");
			return Response.status(Status.BAD_REQUEST).entity(result).build();
		} 
		finally 
		{
			IOUtils.closeQuietly(bos);
			if(httpclient != null)
			{
				httpclient.getConnectionManager().shutdown();
			}
			//FileUtils.deleteQuietly(uploadFile);
		}		
	}
}
