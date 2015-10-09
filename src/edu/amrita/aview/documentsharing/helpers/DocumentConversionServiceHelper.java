package edu.amrita.aview.documentsharing.helpers;

import java.util.List;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.documentsharing.daos.DocumentConversionDAO;
import edu.amrita.aview.documentsharing.entities.DocumentConversion;
import edu.amrita.aview.gclm.helpers.ServerHelper;


public class  DocumentConversionServiceHelper  extends TimerTask{   
	private  MultipartEntity multipartEntity;

	private void getDocumentConversionList() {
		// TODO Auto-generated method stub
		try
		{
			List<DocumentConversion> conversions = DocumentConversionDAO.getAllPendingConversions(StatusHelper.getActiveStatusId());
			for(DocumentConversion conversion:conversions){
			conversion.setConversionStatus(Constant.CONVERTING_CONVERSION_STATUS);
			DocumentConversionDAO.updateConversionRequest(conversion);
			String serverIP=ServerHelper.getServer(conversion.getContentServerId()).getServerIp();
			if(conversion.getIsAnimatedDocument().equals("N"))
			{
				p2fHandler(conversion.getDocumentPath(),conversion.getDocumentName(), serverIP);
			}
			else
			{
				System.out.println("Ispring");
			}
			System.out.println(conversion.getContentServerId());
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	private void p2fHandler(String filePath,String fileName, String serverip){		
		HttpClient httpclient= new DefaultHttpClient();
		try{
			
			multipartEntity=new MultipartEntity();				
			HttpPost conversionRequest = new HttpPost("http://"+serverip+"/AVScript/Upload/Windows/BatchQueDOC/p2fHandler.php");            
	        multipartEntity.addPart("filePath",new StringBody(filePath));
	        multipartEntity.addPart("fileName",new StringBody(fileName));
	        conversionRequest.setEntity(multipartEntity);
	        HttpResponse conversionResponse = httpclient.execute(conversionRequest);	        
	        String message= EntityUtils.toString(conversionResponse.getEntity());
	        System.out.println(message);
	 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		
		}finally{
			httpclient.getConnectionManager().shutdown();
		}

	}
private void ispringHandler(String filePath,String fileName){
	System.out.println("ispring");
	HttpClient httpclient= new DefaultHttpClient();
		try{
			HttpPost conversionRequest = new HttpPost("http://192.168.173.236/AVScript/Upload/Windows/BatchQueDOC/ispringHandler.php");            
	   
	        /*
	        ContentBody contentFile = new FileBody(uploadFile);
	        multipartEntity.addPart("Filedata", contentFile);
	        */
	        multipartEntity.addPart("filePath",new StringBody(filePath));
	        multipartEntity.addPart("fileName",new StringBody(fileName));
	        conversionRequest.setEntity(multipartEntity);
	        HttpResponse conversionResponse = httpclient.execute(conversionRequest);
	        
	       String message= EntityUtils.toString(conversionResponse.getEntity());
		}
		catch(Exception e)
		{
			System.out.println("error in ispring mode");
		}finally{
			httpclient.getConnectionManager().shutdown();
		}

	}

	@Override
	public void run() {
		getDocumentConversionList();
	}
	

}
