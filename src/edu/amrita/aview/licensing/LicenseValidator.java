package edu.amrita.aview.licensing;

import java.net.URLDecoder;
import java.util.Scanner;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import edu.amrita.aview.audit.helpers.UserActionHelper;
import edu.amrita.aview.gclm.helpers.UserHelper;
import edu.amrita.licensing.License;

public class LicenseValidator extends TimerTask 
{
	private static Logger logger = Logger.getLogger(LicenseValidator.class);

	private static final String LICENSE_FILE = "server.license";
	private static final String KEY_FILE = "license.key";
	private static final String APPLICATION_SERVER_NAME = "A-VIEW Server";
	private static final String APPLICATION_VERSION = "3.6";
	private static final String LICENSE_SERVER_IP = "licensing.aview.in";
	private static final String LICENSE_SERVER_PORT = "8080";

	public static boolean validationStatus = true;
	public static String licenseStatus = "Success";
	private String productKey = null;
	private String productName = null;
	private String productVersion = null;

	private boolean validationResult = false;

	public LicenseValidator() 
	{
		try 
		{
			License license = new License(getLicensePath());
			license.setKeyFilePath(getLicenseKeyPath());
			license.setProductSupported(APPLICATION_SERVER_NAME);
			license.setProductVersionSupported(APPLICATION_VERSION);
			license.setLicensingServerURL(LICENSE_SERVER_IP + ":"
					+ LICENSE_SERVER_PORT);
			
			if(license!=null)
			{
				productKey = license.getProductKey();
				productName = license.getProductName();
				productVersion = license.getProductVersion();	
			}

			validationResult = license.isValid();
			//licenseStatus = license.getLicenseStatus();

		}
		catch (Exception e) 
		{
			logger.error("License Validation error");
		}

		if (!validationResult) 
		{
			try 
			{
				 UserActionHelper.recordLicenseValidations(UserHelper
						.getUserByUserName("administrator").getUserId(),
						productKey, this.validationResult, productName + "-"
								+ productVersion);
			}
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				logger.error("Failed updating the DB");
			}

		} else 
		{
			String dateValidation = null;
			try 
			{
				dateValidation = UserActionHelper.recordLicenseValidations(
						UserHelper.getUserByUserName("administrator")
								.getUserId(), productKey,
						this.validationResult, productName + "-"
								+ productVersion);
			}
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				logger.error("Failed updating the DB");
			}
			// Check against system date change issue. If there is license
			// validation happened before on the same date and if found yes
			// declare the license as invalid one.

			if (dateValidation == null && validationResult) 
			{
				 logger.info("Validated license successfully");
				 this.validationStatus = true;
			} 
			else
			{
				logger.info("License validation failed");
				this.validationStatus = false;
			}
		}
	}

	private String getLicensePath() 
	{
		String filePath = "";
		try 
		{
			//filePath = getClass().getResource(LICENSE_FILE).toString();
			filePath = getClass().getResource("/" + LICENSE_FILE).toString();
			logger.debug("License file path is : " + filePath);
			//RGCR: Check for null etc..log it if file not found..Log for every statement
			if(filePath==null)
			{
				logger.error("License filepath is null");
			}
			filePath = filePath.substring(5, filePath.length());
			String a = null;
			String cleanFileName = null;
			Scanner scan = new Scanner(filePath);
			a = scan.nextLine();
			cleanFileName = a.replaceAll("\\s+", "\\\\");
			filePath = URLDecoder.decode(cleanFileName, "utf-8");
		}
		catch (Exception e) 
		{
			//RGCR: Log this
			logger.error("License file not found");
		}
		//RGCR: Log the found path
		return filePath;
	}

	private String getLicenseKeyPath() 
	{
		String filePath = "";
		try 
		{
			//filePath = getClass().getResource(LICENSE_FILE).toString();
			filePath = getClass().getResource("/" + LICENSE_FILE).toString();
			
			filePath = filePath.substring(5, filePath.indexOf(LICENSE_FILE))
					+ "/" + KEY_FILE;
			logger.debug("License Key file path is : " + filePath);
			String a = null;
			String cleanFileName = null;
			Scanner scan = new Scanner(filePath);
			a = scan.nextLine();
			cleanFileName = a.replaceAll("\\s+", "\\\\");
			filePath = URLDecoder.decode(cleanFileName, "utf-8");
		}
		catch (Exception e) 
		{
			logger.error("License key not found");
		}
		return filePath;
	}
	
	@Override
	public void run() 
	{
		new LicenseValidator();
	}

}
