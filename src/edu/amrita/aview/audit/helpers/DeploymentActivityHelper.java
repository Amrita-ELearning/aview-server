package edu.amrita.aview.audit.helpers;

import java.util.List;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.audit.daos.DeploymentActivityDAO;

public class DeploymentActivityHelper{
	
	/**
	 * This method is called when remote object invokes the method getLineChartDataByAllState from front end
	 * This method is used to plot institute marker using the longitude and latitude.
	 * @param year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	public static List getInstituteMarker(Integer year,String instituteType,Integer instCategory,Long parentInst) throws AViewException
	{
		Integer activeSId = StatusHelper.getActiveStatusId();
		List instituteForPlotMarker = DeploymentActivityDAO.getInstituteMarker(activeSId,year, instituteType, instCategory, parentInst);	
		return instituteForPlotMarker;
	}
	
	/**
	 * This method is called when remote object invokes the method getDownloadedYears from front end
	 * This method is used to get list of downloaded years
	 * @param null
	 * @return list
	 **/
	public static List getDownloadedYears() throws AViewException
	{
		Integer activeSId = StatusHelper.getActiveStatusId();
		List deploymentActivities= DeploymentActivityDAO.getDownloadedYears(activeSId);		
		return deploymentActivities;
	}
	
	/* institute download starts*/
	
	/**
	 * This method is called when remote object invokes the method getInstituteDownloadByAllStateRegionalDistribution from front end.
	 * This method is used to get institute download count of all state in region wise distribution.
	 * @param year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	public static List getInstituteDownloadByAllStateRegionalDistribution(Integer year,String instituteType,Integer instCategory,Long parentInst) throws AViewException
	{
		Integer activeSId = StatusHelper.getActiveStatusId();
		List deploymentActivities = DeploymentActivityDAO.getInstituteDownloadByAllStatesRegionalDistribution(activeSId,year, instituteType, instCategory, parentInst);		
		return deploymentActivities;
	}
	
	/**
	 * This method is called when remote object invokes the method getInstituteDownloadByStateRegionalDistribution from front end.
	 * This method is used to get institute download count of selected state in region wise distribution.
	 * @param year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	public static List getInstituteDownloadByStateRegionalDistribution(Integer year,String instituteType,Integer instCategory,Long parentInst,String stateName) throws AViewException
	{
		Integer activeSId = StatusHelper.getActiveStatusId();
		List deploymentActivities = DeploymentActivityDAO.getInstituteDownloadByStateRegionalDistribution(activeSId,year, instituteType, instCategory, parentInst,stateName);		
		return deploymentActivities;
	}
	
	/**
	 * This method is called when remote object invokes the method getInstituteDownloadByAllStateTimeDistribution from front end.
	 * This method is used to get institute download count of all state in time based distribution.
	 * @param year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	public static List getInstituteDownloadByAllStateTimeDistribution(Integer year,String instituteType,Integer instCategory,Long parentInst) throws AViewException
	{
		Integer activeSId = StatusHelper.getActiveStatusId();
		List deploymentActivities = DeploymentActivityDAO.getInstituteDownloadByAllStatesTimeDistribution(activeSId,year, instituteType, instCategory, parentInst);		
		return deploymentActivities;
	}
	
	/**
	 * This method is called when remote object invokes the method getInstituteDownloadByStateTimeDistribution from front end.
	 * This method is used to get institute download count of selected state in time based distribution.
	 * @param year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	public static List getInstituteDownloadByStateTimeDistribution(Integer year,String instituteType,Integer instCategory,Long parentInst,String stateName) throws AViewException
	{
		Integer activeSId = StatusHelper.getActiveStatusId();
		List deploymentActivities = DeploymentActivityDAO.getInstituteDownloadByStateTimeDistribution(activeSId, year, instituteType, instCategory, parentInst, stateName);
		return deploymentActivities;
	}
	/*institute download ends*/
	
	/*user download starts*/
	
	/**
	 * This method is called when remote object invokes the method getUserDownloadByAllStateRegionalDistribution from front end
	 * This method is used to get user download count of all state in region wise distribution.
	 * @param statusId,year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	public static List getUserDownloadByAllStateRegionalDistribution(Integer year,String instituteType,Integer instCategory,Long parentInst) throws AViewException
	{
		Integer activeSId = StatusHelper.getActiveStatusId();
		List deploymentActivities = DeploymentActivityDAO.getUserDownloadByAllStatesRegionalDistribution(activeSId, year, instituteType, instCategory, parentInst);		
		return deploymentActivities;
	}

	/**
	 * This method is called when remote object invokes the method getUserDownloadByStateRegionalDistribution from front end
	 * This method is used to get user download count of selected state in region wise distribution.
	 * @param year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	public static List getUserDownloadByStateRegionalDistribution(Integer year,String instituteType,Integer instCategory,Long parentInst,String stateName) throws AViewException
	{
		Integer activeSId = StatusHelper.getActiveStatusId();
		List deploymentActivities = DeploymentActivityDAO.getUserDownloadByStateRegionalDistribution(activeSId,year, instituteType, instCategory, parentInst,stateName);		
		return deploymentActivities;
	}

	/**
	 * This method is called when remote object invokes the method getUserDownloadByAllStateTimeDistribution from front end.
	 * This method is used to get user download count of all state in time based distribution
	 * @param year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	public static List getUserDownloadByAllStateTimeDistribution(Integer year,String instituteType,Integer instCategory,Long parentInst) throws AViewException
	{
		Integer activeSId = StatusHelper.getActiveStatusId();
		List deploymentActivities = DeploymentActivityDAO.getUserDownloadByAllStatesTimeDistribution(activeSId,year, instituteType, instCategory, parentInst);		
		return deploymentActivities;
	}

	/**
	 * This method is called when remote object invokes the method getUserDownloadByStateTimeDistribution from front end
	 * This method is used to get user download count of selected state in time based distribution
	 * @param statusId,year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	public static List getUserDownloadByStateTimeDistribution(Integer year,String instituteType,Integer instCategory,Long parentInst,String stateName) throws AViewException
	{
		Integer activeSId = StatusHelper.getActiveStatusId();
		List deploymentActivities = DeploymentActivityDAO.getUserDownloadByStateTimeDistribution(activeSId, year, instituteType, instCategory, parentInst, stateName);		
		return deploymentActivities;
	}
	
	/**
	 * This method is called when remote object invokes the method getDownloadInstituteByDistrict from front end
	 * This method is used to get download institute by district
	 * @param year,instituteType,instCategory,parentInst,districtId.
	 * @return list
	 **/
	public static List getDownloadInstituteByDistrict(Integer year,String instituteType,Integer instCategory,Long parentInst,Long districtId) throws AViewException
	{
		Integer activeSId = StatusHelper.getActiveStatusId();
		List deploymentActivities = DeploymentActivityDAO.getDownloadInstituteByDistrict(activeSId, year, instituteType, instCategory, parentInst, districtId);		
		return deploymentActivities;
	}
	/*user download ends*/
	
	/* usage starts */
	
	/**
	 * This method is called when remote object invokes the method getAVIEWUsageData from front end.
	 * This Method is used to get aview usage count based on time for all state and particular state.
	 * @param year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	public static List getAVIEWUsageData(Integer year,String instituteType,Integer instCategory,Long parentInst,String stateName) throws AViewException
	{
		Integer activeSId = StatusHelper.getActiveStatusId();
		List deploymentActivities = DeploymentActivityDAO.getAVIEWUsageData(activeSId, year, instituteType, instCategory, parentInst, stateName);		
		return deploymentActivities;
	}
	
	/*usage ends */
}
