package edu.amrita.aview.audit.helpers;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import edu.amrita.aview.common.AViewException;

public class DeploymentActivityHelperTest {

	/**
	 * This method is called to test the method getLineChartDataByAllState 
	 * This Method is used to plotting institute marker using the longitude and latitude,
	 * using the method getInstituteMarker
	 * @param year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	@Test
	public void testGetInstituteMarker() throws AViewException
	{
		List instituteMarker = DeploymentActivityHelper.getInstituteMarker(2011, "university", 1, 18l);
		for(int i = 0; i < instituteMarker.size(); i++)
		{
			System.out.println("instituteMarker :: "+instituteMarker.get(i));
		}
	}
	
	/**
	 * This method is called to test the method getDownloadedYears
	 * This Method is used to get list of downloaded years,using the method getDownloadedYear.
	 * @param null
	 * @return list
	 **/
	@Test
	public void getDownloadedYears() throws AViewException
	{
		List downloadedYear = DeploymentActivityHelper.getDownloadedYears();
		for(int i = 0; i < downloadedYear.size(); i++)
		{
			System.out.println("downloadedYear :: "+downloadedYear.get(i));
		}
	}
	
	/* institute download test starts */
	
	/**
	 * This method is called to test the method getInstituteDownloadByAllStateRegionalDistribution 
	 * This Method is used to get institute download count for all state using the method 
	 * getInstituteDownloadByAllStateRegionalDistribution in region wise distribution.
	 * @param year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	@Test
	public void testGetInstituteDownloadByAllStateRegionalDistribution() throws AViewException
	{
		List instituteDownloads = DeploymentActivityHelper.getInstituteDownloadByAllStateRegionalDistribution(0,null, 1, 0l);
		for(int i = 0; i < instituteDownloads.size(); i++)
		{
			System.out.println("instituteDownloads :: "+instituteDownloads.get(i));
		}
	}
	
	/**
	 * This method is called to test the method getInstituteDownloadByStateRegionalDistribution
	 * This Method is used to get institute download count for selected state using the method 
	 * getInstituteDownloadByStateRegionalDistribution in region wise distribution.
	 * @param year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	@Test
	public void testGetInstituteDownloadByStateRegionalDistribution() throws AViewException
	{
		List instituteDownloads = DeploymentActivityHelper.getInstituteDownloadByStateRegionalDistribution(2012,"college", 1, 0l,"kerala");
		for(int i = 0; i < instituteDownloads.size(); i++)
		{
			System.out.println("instituteDownloads :: "+instituteDownloads.get(i));
		}
	}
	
	/**
	 * This method is called to test the method getInstituteDownloadByAllStateTimeDistribution
	 * This Method is used to get institute download count based on time for all state using the method 
	 * getInstituteDownloadByAllStateTimeDistribution.
	 * @param year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	@Test
	public void testGetInstituteDownloadByAllStateTimeDistribution() throws AViewException
	{
		List instituteDownloads = DeploymentActivityHelper.getInstituteDownloadByAllStateTimeDistribution(0,null, 1, 0l);		
		for(int i = 0; i < instituteDownloads.size(); i++)
		{
			System.out.println("instituteDownloads :: "+instituteDownloads.get(i));
		}
	}
	
	/**
	 * This method is called to test the method getInstituteDownloadByStateTimeDistribution
	 * This Method is used to get institute download count based on time for selected state using the method 
	 * getInstituteDownloadByStateTimeDistribution.
	 * @param year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	@Test
	public void testGetInstituteDownloadByStateTimeDistribution() throws AViewException
	{
		List instituteDownloads = DeploymentActivityHelper.getInstituteDownloadByStateTimeDistribution(2012,"college", 1, 0l,"tamil nadu");
		for(int i = 0; i < instituteDownloads.size(); i++)
		{
			System.out.println("instituteDownloads :: "+instituteDownloads.get(i));
		}
	}
	
	/* institute download test ends */
	
	/* user download test starts */ 
	
	/**
	 * This method is called to test the method getUserDownloadByAllStatesRegionalDistribution
	 * This Method is used to get user download count for all state using the method 
	 * getUserDownloadByAllStatesRegionalDistribution in region wise distribution.
	 * @param statusId,year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	@Test
	public void testGetUserDownloadByAllStateRegionalDistribution() throws AViewException
	{
		List userDownloads = DeploymentActivityHelper.getUserDownloadByAllStateRegionalDistribution(0,null, 1, 0l);
		for(int i = 0; i < userDownloads.size(); i++)
		{
			System.out.println("userDownloads :: "+userDownloads.get(i));
		}
	}
	
	/**
	 * This method is called to test the method getUserDownloadByStateRegionalDistribution
	 * This Method is used to get user download count for selected state using the method 
	 * getUserDownloadByStateRegionalDistribution in region wise distribution.
	 * @param year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	@Test
	public void testGetUserDownloadByStateRegionalDistribution() throws AViewException
	{
		List userDownloads = DeploymentActivityHelper.getUserDownloadByStateRegionalDistribution(2012,null, 1, 0l,"kerala");
		for(int i = 0; i < userDownloads.size(); i++)
		{
			System.out.println("userDownloads :: "+userDownloads.get(i));
		}
	}
	
	/**
	 * This method is called to test the method getUserDownloadByAllStateTimeDistribution
	 * This Method is used to get user download count based on time for all state using the method 
	 * getUserDownloadByAllStatesTimeDistribution.
	 * @param year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	@Test
	public void testGetUserDownloadByAllStateTimeDistribution() throws AViewException
	{
		List userDownloads = DeploymentActivityHelper.getUserDownloadByAllStateTimeDistribution(0,null, 1, 0l);		
		for(int i = 0; i < userDownloads.size(); i++)
		{
			System.out.println("userDownloads :: "+userDownloads.get(i));
		}
	}
	
	/**
	 * This method is called to test the method getUserDownloadByStateTimeDistribution
	 * This Method is used to get user download count based on time for selected state using the method 
	 * getUserDownloadByStateTimeDistribution.
	 * @param statusId,year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	@Test
	public void testGetUserDownloadByStateTimeDistribution() throws AViewException
	{
		List userDownloads = DeploymentActivityHelper.getUserDownloadByStateTimeDistribution(2012,"college", 1, 0l,"tamil nadu");
		for(int i = 0; i < userDownloads.size(); i++)
		{
			System.out.println("userDownloads :: "+userDownloads.get(i));
		}
	}
	
	
	/**
	 * This method is called to test the method getDownloadInstituteByDistrict
	 * This Method is used to get user download count based on district using the method 
	 * getDownloadInstituteByDistrict.
	 * @param statusId,year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	@Test
	public void testGetDownloadInstituteByDistrict() throws AViewException
	{
		List userDownloads = DeploymentActivityHelper.getDownloadInstituteByDistrict(2014,"college", 1, 0l,19l);
		for(int i = 0; i < userDownloads.size(); i++)
		{
			System.out.println("instituteDownloads :: "+userDownloads.get(i));
		}
	}
	/* user download test ends */ 
	
	/* aview usage test starts */ 
	
	/**
	 * This method is called to test the method getAVIEWUsageData.
	 * This Method is used to get aview usage count based on time for all state using the method getAVIEWUsageData.
	 * @param year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	@Test
	public void testAVIEWUsage() throws AViewException
	{
		List aviewUsage = DeploymentActivityHelper.getAVIEWUsageData(2013,"college", 1, 0l,"tamil nadu");
		for(int i = 0; i < aviewUsage.size(); i++)
		{
			System.out.println("aviewUsage :: "+aviewUsage.get(i));
		}
	}
	
	/* aview usage test ends */ 
}
