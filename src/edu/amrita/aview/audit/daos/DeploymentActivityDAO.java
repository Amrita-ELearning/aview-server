package edu.amrita.aview.audit.daos;
import java.util.List;

import org.apache.log4j.Logger;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.utils.JDBCUtils;
import edu.amrita.aview.common.utils.TimestampUtils;

public class DeploymentActivityDAO extends SuperDAO {
	
    private static Logger logger = Logger.getLogger(DeploymentActivityDAO.class);
    /**
	 * This method is called when the method getInstituteMarker is invoked from the corresponding helper function. 
	 * This method is used to plot institute marker using the longitude and latitude
	 * @param statusId,year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	//CRJH: This function is not used now since we don't have position details information of institutes in DB.
  	public static List getInstituteMarker(Integer statusId,Integer year,String instituteType,Integer instCategory,Long parentInst) throws AViewException
	{
		List instituteForPlotMarker = null;
		StringBuilder queryString = new StringBuilder();
		queryString.append("SELECT DISTINCT inst.institute_id,inst.institute_name,inst.latitude," +
							"inst.longitude,s.state_name,district_name FROM " +
							"`user_action` ua,`user` u,`institute` inst,`action` a," +
							"`district` d,`state` s,`institute_category` instcat " +
							"WHERE a.action_name = 'Download AVIEW' AND u.status_id =" + statusId + " " +
							"AND inst.status_id =" + statusId + " AND ua.status_id =" + statusId + " " +
							"AND d.status_id =" + statusId + " AND s.status_id =" + statusId + " " +
							"AND ua.created_by_user_id = u.user_id AND u.institute_id = inst.institute_id " +
							"AND inst.district_id = d.district_id AND d.state_id = s.state_id " +
							"AND ua.action_id = a.action_id ");	
		if(year != 0 && year > 0)
		{
			queryString.append("AND YEAR(ua.created_date)=" + year + " ");
		}
		if(instituteType != null)
		{
			queryString.append("AND inst.institute_type='" + instituteType + "' ");
		}
		if(instCategory != 0 && instCategory > 0)
		{
			queryString.append("AND inst.institute_category_id=" + instCategory + " ");
		}
		if(parentInst != 0l && parentInst > 0l)
		{
			queryString.append("AND (inst.parent_institute_id=" + parentInst + " OR inst.institute_id=" + parentInst + ") ") ;
//							"AND inst.instituteType=(SELECT instituteType " +
//							"FROM Institute WHERE instituteId=:parentInst)";
		}
		instituteForPlotMarker = JDBCUtils.executeSQL(queryString.toString());
		if(instituteForPlotMarker.size() > 0)
		{
			logger.info("Institute available");
		}
		else if(instituteForPlotMarker.size() == 0)
		{
			logger.warn("Warning :: No Institute available");
		}
		return instituteForPlotMarker;
	}	
  	
  	/**
	 * This method is called when the method getDownloadedYear is invoked from the corresponding helper function
	 * This method is used to get list of downloaded years.
	 * @param statusId
	 * @return list
	 **/
  	public static List getDownloadedYears(Integer statusId) throws AViewException
	{
		List deploymentActivities = null;
		StringBuilder queryString = new StringBuilder();
		queryString.append("SELECT DISTINCT YEAR(a.FirstDownload) 'year' FROM " +
							"(SELECT inst.institute_id,MIN(ua.created_date) 'FirstDownload'," +
							"CONCAT(DATE_FORMAT((MIN(ua.created_date)),'%b'),' ',YEAR(MIN(ua.created_date))) 'FirstDownloadMonth'," +
							"EXTRACT(YEAR_MONTH FROM MIN(ua.created_date)) 'FirstDownloadYYYYMM'," +
							"COUNT(*) 'Downloads' FROM `user_action` ua,`user` u,`institute` inst,`action` a," +
							"`district` d,`state` s WHERE a.action_name = 'Download AVIEW' " +
							"AND u.status_id =" + statusId + " AND inst.status_id =" + statusId + " " +
							"AND ua.status_id =" + statusId + " AND d.status_id =" + statusId + " " +
							"AND s.status_id =" + statusId + " AND ua.action_id = a.action_id " +
							"AND u.institute_id = inst.institute_id AND inst.district_id = d.district_id " +
							"AND d.state_id = s.state_id AND ua.created_by_user_id = u.user_id " +
							"GROUP BY inst.institute_id ORDER BY 'FirstDownload')a " +
							"GROUP BY a.FirstDownloadMonth ORDER BY a.FirstDownloadYYYYMM ");
		deploymentActivities = JDBCUtils.executeSQL(queryString.toString());
		if(deploymentActivities.size() > 0)
		{
			logger.info("Year exist");
		}
		else if(deploymentActivities.size() == 0)
		{
			logger.warn("Warning :: No year exist");
		}
		return deploymentActivities;
	}
  	
  	/**
	 * This method is invoked while forming database queries, if there is any filter chosen by the user.
	 * This method is used to append filters to the queries.
	 * @param instituteType,instCategory,parentInst.
	 * @return stringbuilder.
	 **/
  	public static StringBuilder appendFilters(String instituteType,Integer instCategory,Long parentInst)
  	{
  		StringBuilder queryString = new StringBuilder();
  		if(instituteType != null)
		{
			queryString.append("AND inst.institute_type='" + instituteType + "' ");
		}
		if(instCategory != 0 && instCategory > 0)
		{
			queryString.append("AND inst.institute_category_id=" + instCategory + " ");
		}
		if(parentInst != 0l && parentInst > 0l)
		{
			queryString.append("AND (inst.parent_institute_id=" + parentInst + " OR inst.institute_id=" + parentInst + ") ");
		}
		return queryString;
   	}
  	
  	/**
	 * This method is invoked while forming database queries in regional distribution,if the user have chosen year.
	 * This method is used to append filters to the queries for regional distribution.
	 * @param year.
	 * @return stringbuilder.
	 **/
  	public static StringBuilder appendYearForRegionChart(Integer year)
  	{
  		StringBuilder queryString = new StringBuilder();
  		if(year != 0 && year > 0)
		{
			queryString.append("WHERE YEAR(a.FirstDownload) =" + year + " ");
		}
  		return queryString;
  	}
  	
  	/**
	 * This method is invoked while forming database queries in time distribution,if the user have chosen year.
	 * This method is used to append filters to the queries for time distribution.
	 * @param year.
	 * @return stringbuilder.
	 **/
 	public static StringBuilder appendYearForTimeBasedChart(Integer year)
  	{
  		StringBuilder queryString = new StringBuilder();
  		if(year != 0 && year > 0)
		{
			queryString.append("WHERE YEAR(a.FirstDownload) <=" + year + " ");
		}
  		return queryString;
  	}
 	
  	/*institute download starts*/
 	
 	 /**
	 * This method is called when the method getInstituteDownloadByAllStatesRegionalDistribution is invoked from the corresponding helper function
	 * This method is used to get institute download count for all state in region wise distribution.
	 * @param statusId,year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	public static List getInstituteDownloadByAllStatesRegionalDistribution(Integer statusId,Integer year,String instituteType,Integer instCategory,Long parentInst) throws AViewException
	{
		List deploymentActivities = null;
		StringBuilder queryString = new StringBuilder();
		queryString.append( "SELECT COUNT(*) 'downloadCount',a.stateName FROM " +
							"(SELECT inst.institute_id,s.state_name 'stateName'," +
							"MIN(ua.created_date) 'FirstDownload' " +
							"FROM `user_action` ua,`user` u,`institute` inst,`action` a," +
							"`district` d,`state` s  WHERE a.action_name = 'Download AVIEW' " +
							"AND u.status_id =" + statusId + " AND inst.status_id = " + statusId + " " +
							"AND ua.status_id = " + statusId + " AND d.status_id = " + statusId + " " +
							"AND s.status_id =" + statusId + " AND ua.created_by_user_id = u.user_id " +
							"AND u.institute_id = inst.institute_id " +
							"AND inst.district_id=d.district_id AND d.state_id=s.state_id " +
							"AND ua.action_id = a.action_id ");
		queryString.append(appendFilters(instituteType,instCategory,parentInst));
		queryString.append("GROUP BY inst.institute_id,s.state_name)a " );
		queryString.append(appendYearForRegionChart(year));
		queryString.append("GROUP BY a.stateName ");
		deploymentActivities = JDBCUtils.executeSQL(queryString.toString());
		if(deploymentActivities.size() > 0)
		{
			logger.info("Deployment activity by all state for regional distribution exist");
		}
		else if(deploymentActivities.size() == 0)
		{
			logger.warn("Warning :: No Filter values is available");
		}
		return deploymentActivities;
	}
	
	 /**
	 * This method is called when the method getInstituteDownloadByAllStatesRegionalDistribution is invoked from the corresponding helper function
	 * This Method is used to get institute download count for selected state in region wise distribution.
	 * @param statusId,year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	public static List getInstituteDownloadByStateRegionalDistribution(Integer statusId,Integer year,String instituteType,Integer instCategory,Long parentInst,String stateName) throws AViewException
	{
		List deploymentActivities = null;
		StringBuilder queryString = new StringBuilder();
		queryString.append( "SELECT COUNT(*) 'downloadCount',a.districtName FROM " +
							"(SELECT inst.institute_id,d.district_name 'districtName'," +
							"MIN(ua.created_date) 'FirstDownload' " +
							"FROM `user_action` ua,`user` u,`institute` inst,`action` a," +
							"`district` d,`state` s WHERE s.state_name ='" + stateName + "' " +
							"AND a.action_name = 'Download AVIEW' " +
							"AND u.status_id =" + statusId + " AND inst.status_id = " + statusId + " " +
							"AND ua.status_id = " + statusId + " AND d.status_id = " + statusId + " " +
							"AND s.status_id =" + statusId + " AND ua.created_by_user_id = u.user_id " +
							"AND u.institute_id = inst.institute_id " +
							"AND inst.district_id=d.district_id AND d.state_id=s.state_id " +
							"AND ua.action_id = a.action_id ");
		queryString.append(appendFilters(instituteType,instCategory,parentInst));
		queryString.append("GROUP BY inst.institute_id,d.district_name)a " );
		queryString.append(appendYearForRegionChart(year));
		queryString.append("GROUP BY a.districtName ");
		deploymentActivities = JDBCUtils.executeSQL(queryString.toString());
		if(deploymentActivities.size() > 0)
		{
			logger.info("Deployment activity by state for regional distribution exist");
		}
		else if(deploymentActivities.size() == 0)
		{
			logger.warn("Warning :: No Filter values is available");
		}
		return deploymentActivities;
	}
	
	/**
	 * This method is called when the method getInstituteDownloadByAllStatesRegionalDistribution is invoked from the corresponding helper function
	 * This method is used to get institute download count for all state in time based distribution.
	 * @param statusId,year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	public static List getInstituteDownloadByAllStatesTimeDistribution(Integer statusId,Integer year,String instituteType,Integer instCategory,Long parentInst) throws AViewException
	{
		List deploymentActivities = null;
		StringBuilder queryString = new StringBuilder();
		queryString.append("SELECT COUNT(*) 'count',a.FirstDownloadMonth,YEAR(a.FirstDownload) 'year' FROM " +
							"(SELECT inst.institute_id,MIN(ua.created_date) 'FirstDownload'," +
							"CONCAT(DATE_FORMAT((MIN(ua.created_date)),'%b'),' ',YEAR(MIN(ua.created_date))) 'FirstDownloadMonth'," +
							"EXTRACT(YEAR_MONTH FROM MIN(ua.created_date)) 'FirstDownloadYYYYMM'," +
							"COUNT(*) 'Downloads' FROM `user_action` ua,`user` u,`institute` inst,`action` a," +
							"`district` d,`state` s WHERE a.action_name = 'Download AVIEW' " +
							"AND u.status_id =" + statusId + " AND inst.status_id =" + statusId + " " +
							"AND ua.status_id =" + statusId + " AND d.status_id =" + statusId + " " +
							"AND s.status_id =" + statusId + " AND ua.action_id = a.action_id " +
							"AND u.institute_id = inst.institute_id AND inst.district_id = d.district_id " +
							"AND d.state_id = s.state_id AND ua.created_by_user_id = u.user_id ");
		queryString.append(appendFilters(instituteType,instCategory,parentInst));
		queryString.append("GROUP BY inst.institute_id ORDER BY 'FirstDownload')a " );
		queryString.append(appendYearForTimeBasedChart(year));
		queryString.append("GROUP BY a.FirstDownloadMonth ORDER BY a.FirstDownloadYYYYMM ");
		deploymentActivities = JDBCUtils.executeSQL(queryString.toString());
		if(deploymentActivities.size() > 0)
		{
			logger.info("Deployment activity by all state for time distribution exist");
		}
		else if(deploymentActivities.size() == 0)
		{
			logger.warn("Warning :: No Filter values is available");
		}
		return deploymentActivities;
	}
	
	/**
	 * This method is called when the method getInstituteDownloadByAllStatesRegionalDistribution is invoked from the corresponding helper function
	 * This method is used to get institute download count for selected state in time based distribution.
	 * @param statusId,year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	public static List getInstituteDownloadByStateTimeDistribution(Integer statusId,Integer year,String instituteType,Integer instCategory,Long parentInst,String stateName) throws AViewException
	{
		logger.debug("Enter getLineChartDataByState : " + TimestampUtils.getCurrentTimestamp());
		List deploymentActivities = null;
		StringBuilder queryString = new StringBuilder();
		queryString.append("SELECT COUNT(*) 'count',a.FirstDownloadMonth,YEAR(a.FirstDownload) 'year' FROM " +
							"(SELECT inst.institute_id,MIN(ua.created_date) 'FirstDownload'," +
							"CONCAT(DATE_FORMAT((MIN(ua.created_date)),'%b'),' ',YEAR(MIN(ua.created_date))) 'FirstDownloadMonth'," +
							"EXTRACT(YEAR_MONTH FROM MIN(ua.created_date)) 'FirstDownloadYYYYMM'," +
							"COUNT(*) 'Downloads' FROM `user_action` ua,`user` u,`institute` inst,`action` a," +
							"`district` d,`state` s WHERE s.state_name = '" + stateName + "' " +
							"AND a.action_name = 'Download AVIEW' AND u.status_id =" + statusId + " " +
							"AND inst.status_id =" + statusId + " AND ua.status_id =" + statusId + " " +
							"AND d.status_id =" + statusId + " AND s.status_id =" + statusId + " " +
							"AND ua.action_id = a.action_id AND u.institute_id = inst.institute_id " +
							"AND inst.district_id = d.district_id AND d.state_id = s.state_id " +
							"AND ua.created_by_user_id = u.user_id ");						
		queryString.append(appendFilters(instituteType,instCategory,parentInst));
		queryString.append(" GROUP BY inst.institute_id ORDER BY 'FirstDownload')a ");
		queryString.append(appendYearForTimeBasedChart(year));
		queryString.append("GROUP BY a.FirstDownloadMonth ORDER BY a.FirstDownloadYYYYMM ");
		deploymentActivities = JDBCUtils.executeSQL(queryString.toString());
		if(deploymentActivities.size() > 0)
		{
			logger.info("Deployment activity by state for time distribution exist");
		}
		else if(deploymentActivities.size() == 0)
		{
			logger.warn("Warning :: No Filter values is available");
		}
		logger.debug("End getLineChartDataByState : " + TimestampUtils.getCurrentTimestamp());
		return deploymentActivities;
	}
	
	/*institute download ends*/
	
	/*user download starts*/
	
	/**
	 * This method is called when the method getUserDownloadByAllStatesRegionalDistribution is invoked from the corresponding helper function.
	 * This method is used to get user download count for all state in region wise distribution.
	 * @param statusId,year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	public static List getUserDownloadByAllStatesRegionalDistribution(Integer statusId,Integer year,String instituteType,Integer instCategory,Long parentInst) throws AViewException
	{
		List deploymentActivities = null;
		StringBuilder queryString = new StringBuilder();
		queryString.append( "SELECT COUNT(*) 'downloadCount',a.stateName FROM " +
							"(SELECT u.user_id,s.state_name 'stateName'," +
							"MIN(ua.created_date) 'FirstDownload' " +
							"FROM `user_action` ua,`user` u,`institute` inst,`action` a," +
							"`district` d,`state` s  WHERE a.action_name = 'Download AVIEW' " +
							"AND u.status_id =" + statusId + " AND inst.status_id = " + statusId + " " +
							"AND ua.status_id = " + statusId + " AND d.status_id = " + statusId + " " +
							"AND s.status_id =" + statusId + " AND ua.created_by_user_id = u.user_id " +
							"AND u.institute_id = inst.institute_id " +
							"AND inst.district_id=d.district_id AND d.state_id=s.state_id " +
							"AND ua.action_id = a.action_id ");
		queryString.append(appendFilters(instituteType,instCategory,parentInst));
		queryString.append("GROUP BY u.user_id,s.state_name)a ");
		queryString.append(appendYearForRegionChart(year));
		queryString.append("GROUP BY a.stateName ");
		deploymentActivities = JDBCUtils.executeSQL(queryString.toString());
		if(deploymentActivities.size() > 0)
		{
			logger.info("Deployment activity by all state for regional distribution exist");
		}
		else if(deploymentActivities.size() == 0)
		{
			logger.warn("Warning :: No Filter values is available");
		}
		return deploymentActivities;
	}

	/**
	 * This method is called when the method getUserDownloadByStateRegionalDistribution is invoked from the corresponding helper function.
	 * This method is used to get user download count for selected state in region wise distribution.
	 * @param statusId,year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	public static List getUserDownloadByStateRegionalDistribution(Integer statusId,Integer year,String instituteType,Integer instCategory,Long parentInst,String stateName) throws AViewException
	{
		List deploymentActivities = null;
		StringBuilder queryString = new StringBuilder();
		queryString.append( "SELECT COUNT(*) 'downloadCount',a.districtName FROM " +
							"(SELECT u.user_id,d.district_name 'districtName'," +
							"MIN(ua.created_date) 'FirstDownload' " +
							"FROM `user_action` ua,`user` u,`institute` inst,`action` a," +
							"`district` d,`state` s WHERE s.state_name ='" + stateName + "' " +
							"AND a.action_name = 'Download AVIEW' " +
							"AND u.status_id =" + statusId + " AND inst.status_id = " + statusId + " " +
							"AND ua.status_id = " + statusId + " AND d.status_id = " + statusId + " " +
							"AND s.status_id =" + statusId + " AND ua.created_by_user_id = u.user_id " +
							"AND u.institute_id = inst.institute_id " +
							"AND inst.district_id=d.district_id AND d.state_id=s.state_id " +
							"AND ua.action_id = a.action_id ");
		queryString.append(appendFilters(instituteType,instCategory,parentInst));
		queryString.append("GROUP BY u.user_id,d.district_name)a ");
		queryString.append(appendYearForRegionChart(year));
		queryString.append("GROUP BY a.districtName ");
		deploymentActivities = JDBCUtils.executeSQL(queryString.toString());
		if(deploymentActivities.size() > 0)
		{
			logger.info("Deployment activity by state for regional distribution exist");
		}
		else if(deploymentActivities.size() == 0)
		{
			logger.warn("Warning :: No Filter values is available");
		}
		return deploymentActivities;
	}

	/**
	 * This method is called when the method getUserDownloadByAllStatesTimeDistribution is invoked from the corresponding helper function
	 * This method is used to get user download count for all state in time based distribution.
	 * @param statusId,year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	public static List getUserDownloadByAllStatesTimeDistribution(Integer statusId,Integer year,String instituteType,Integer instCategory,Long parentInst) throws AViewException
	{
		List deploymentActivities = null;
		StringBuilder queryString = new StringBuilder();
		queryString.append("SELECT COUNT(*) 'count',a.FirstDownloadMonth,YEAR(a.FirstDownload) 'year' FROM " +
							"(SELECT u.user_id,MIN(ua.created_date) 'FirstDownload'," +
							"CONCAT(DATE_FORMAT((MIN(ua.created_date)),'%b'),' ',YEAR(MIN(ua.created_date))) 'FirstDownloadMonth'," +
							"EXTRACT(YEAR_MONTH FROM MIN(ua.created_date)) 'FirstDownloadYYYYMM'," +
							"COUNT(*) 'Downloads' FROM `user_action` ua,`user` u,`institute` inst,`action` a," +
							"`district` d,`state` s WHERE a.action_name = 'Download AVIEW' " +
							"AND u.status_id =" + statusId + " AND inst.status_id =" + statusId + " " +
							"AND ua.status_id =" + statusId + " AND d.status_id =" + statusId + " " +
							"AND s.status_id =" + statusId + " AND ua.action_id = a.action_id " +
							"AND u.institute_id = inst.institute_id AND inst.district_id = d.district_id " +
							"AND d.state_id = s.state_id AND ua.created_by_user_id = u.user_id ");
		queryString.append(appendFilters(instituteType,instCategory,parentInst));
		queryString.append("GROUP BY  u.user_id ORDER BY 'FirstDownload')a ");
		queryString.append(appendYearForTimeBasedChart(year));
		queryString.append("GROUP BY a.FirstDownloadMonth ORDER BY a.FirstDownloadYYYYMM ");
		deploymentActivities = JDBCUtils.executeSQL(queryString.toString());
		if(deploymentActivities.size() > 0)
		{
			logger.info("Deployment activity by all state for time distribution exist");
		}
		else if(deploymentActivities.size() == 0)
		{
			logger.warn("Warning :: No Filter values is available");
		}
		return deploymentActivities;
	}
	
	/**
	 * This method is called when the method getUserDownloadByStateTimeDistribution is invoked from the corresponding helper function
	 * This method is used to get user download count for selected state in time based distribution.
	 * @param statusId,year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	public static List getUserDownloadByStateTimeDistribution(Integer statusId,Integer year,String instituteType,Integer instCategory,Long parentInst,String stateName) throws AViewException
	{
		logger.debug("Enter getLineChartDataForUserDownloadByState : " + TimestampUtils.getCurrentTimestamp());
		List deploymentActivities = null;
		StringBuilder queryString = new StringBuilder();
		queryString.append( "SELECT COUNT(*) 'count',a.FirstDownloadMonth,YEAR(a.FirstDownload) 'year' FROM " +
							"(SELECT user_id,MIN(ua.created_date) 'FirstDownload'," +
							"CONCAT(DATE_FORMAT((MIN(ua.created_date)),'%b'),' ',YEAR(MIN(ua.created_date))) 'FirstDownloadMonth'," +
							"EXTRACT(YEAR_MONTH FROM MIN(ua.created_date)) 'FirstDownloadYYYYMM'," +
							"COUNT(*) 'Downloads' FROM `user_action` ua,`user` u,`institute` inst,`action` a," +
							"`district` d,`state` s WHERE s.state_name = '" + stateName + "' " +
							"AND a.action_name = 'Download AVIEW' AND u.status_id =" + statusId + " " +
							"AND inst.status_id =" + statusId + " AND ua.status_id =" + statusId + " " +
							"AND d.status_id =" + statusId + " AND s.status_id =" + statusId + " " +
							"AND ua.action_id = a.action_id AND u.institute_id = inst.institute_id " +
							"AND inst.district_id = d.district_id AND d.state_id = s.state_id " +
							"AND ua.created_by_user_id = u.user_id ");						
		queryString.append(appendFilters(instituteType,instCategory,parentInst));
		queryString.append(" GROUP BY user_id ORDER BY 'FirstDownload')a ");
		queryString.append(appendYearForTimeBasedChart(year));
		queryString.append("GROUP BY a.FirstDownloadMonth ORDER BY a.FirstDownloadYYYYMM ");
		deploymentActivities = JDBCUtils.executeSQL(queryString.toString());
		if(deploymentActivities.size() > 0)
		{
			logger.info("Deployment activity by state for time distribution exist");
		}
		else if(deploymentActivities.size() == 0)
		{
			logger.warn("Warning :: No Filter values is available");
		}
		logger.debug("End getLineChartDataForUserDownloadByState : " + TimestampUtils.getCurrentTimestamp());
		return deploymentActivities;
	}
	
	/**
	 * This method is called when the method getDownloadInstituteByDistrict is invoked from the corresponding helper function
	 * This method is used to get downloaded institute  count for selected district
	 * @param statusId,year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	public static List getDownloadInstituteByDistrict(Integer statusId,Integer year,String instituteType,Integer instCategory,Long parentInst,Long districtId) throws AViewException
	{
		logger.debug("Enter getDownloadInstituteByDistrict : " + TimestampUtils.getCurrentTimestamp());
		List deploymentActivities = null;
		StringBuilder queryString = new StringBuilder();
		queryString.append( "SELECT a.* FROM " +
				"(SELECT inst.*,d.district_name 'districtName'," +
				"MIN(ua.created_date) 'FirstDownload' " +
				"FROM `user_action` ua,`user` u,`institute` inst,`action` a," +
				"`district` d,`state` s WHERE " +
				"a.action_name = 'Download AVIEW' " +
				"AND u.status_id =" + statusId + " AND inst.status_id = " + statusId + " " +
				"AND ua.status_id = " + statusId + " AND d.status_id = " + statusId + " " +
				"AND s.status_id =" + statusId + " AND ua.created_by_user_id = u.user_id " +
				"AND u.institute_id = inst.institute_id " +
				"AND inst.district_id=d.district_id AND d.state_id=s.state_id " +
				"AND ua.action_id = a.action_id ");
		if(districtId != 0)
		{
			queryString.append("AND inst.district_id =" + districtId + " ");
		}
		queryString.append(appendFilters(instituteType,instCategory,parentInst));
		if(districtId != 0)
		{
			queryString.append("GROUP BY inst.institute_id,d.district_name)a " );
		}
		queryString.append(appendYearForRegionChart(year));
		queryString.append("GROUP BY a.institute_name ");
		deploymentActivities = JDBCUtils.executeSQL(queryString.toString());
		if(deploymentActivities.size() > 0)
		{
			logger.info("Deploymentinstitute details by district");
		}
		else if(deploymentActivities.size() == 0)
		{
			logger.warn("Warning :: No Filter values is available");
		}
		logger.debug("End getDownloadInstituteByDistrict : " + TimestampUtils.getCurrentTimestamp());
		return deploymentActivities;
	}
	/*user downloads ends*/
	
	/* usage starts */
	
	/**
	 * This method is called when the method getAVIEWUsageData is invoked from the corresponding helper function
	 * This method is used to get aview usage count based on time for all state and particular state.
	 * @param statusId,year,instituteType,instCategory,parentInst.
	 * @return list
	 **/
	public static List getAVIEWUsageData(Integer statusId,Integer year,String instituteType,Integer instCategory,Long parentInst,String stateName) throws AViewException
	{
		logger.debug("Enter getAVIEWUsageData : " + TimestampUtils.getCurrentTimestamp());
		List deploymentActivities = null;
		StringBuilder queryString = new StringBuilder();
		queryString.append( "SELECT CONCAT(DATE_FORMAT(LDate,'%b'),' ',YEAR(LDate)) 'Month'," +
						 	"DATE_FORMAT(LDate,'%Y/%m') 'YYYYMMs',COUNT(DISTINCT CourseInstituteId) Institutes," +
						 	"COUNT(DISTINCT course_id) Courses," +
						 	"COUNT(DISTINCT class_id) Classes," +
						 	"COUNT(*) Lectures, ROUND(AVG(Nodes),2) Nodes," +
						 	"ROUND(SUM(TeachingHours),2) Hours,DATE_FORMAT(LDate,'%Y') 'year' FROM" +
						 	"(SELECT b.LDate,b.CourseInstitute,b.Class,b.Lecture,b.CourseInstituteId,b.course_id," +
						 	"b.class_id,b.lecture_id,s.`state_name`,bi.`institute_type`,COUNT(*) Nodes,	SUM(1)," +
						 	"AVG(b.TotalTime) TeachingSecs,SUM(b.TotalTime) NodalTeachingSecs," +
						 	"AVG(b.TotalTime)/3600 TeachingHours,SUM(b.TotalTime)/3600 NodalTeachingHours FROM " +
						 	"(" +
						 	"-- User level \n " +
						 	"SELECT a.LDate,a.CourseInstitute,a.Class,a.Lecture,a.FirstName,a.LastName,a.UserName," +
						 	"a.UserId,a.email,a.mobile_number,a.UserInstitute,a.CourseInstituteId,a.UserInstituteId," +
						 	"a.course_id,a.class_id,a.lecture_id,COUNT(*) TotalClassEntryTimes," +
						 	"SUM(TIME_TO_SEC(a.TotalTime)) TotalTime ," +
						 	"AVG(TIME_TO_SEC(a.TotalTime)) AVGTime ," +
						 	"MIN(a.LectureEntryTime) FirstClassEntry," +
						 	"MAX(a.LectureExitTime) LastClassExit FROM " +
						 	"(" +
						 	"-- Attendance Login Level Summary\n " +
						 	"SELECT DATE(l.date) LDate,i.institute_name CourseInstitute,cl.class_name Class," +
						 	"l.lecture_name Lecture,u.fname FirstName,u.lname LastName,u.user_name UserName," +
						 	"u.user_id UserId,u.email,u.mobile_number,ui.institute_name UserInstitute," +
						 	"i.institute_id CourseInstituteId,ui.institute_id UserInstituteId,cr.course_id," +
						 	"cl.class_id,l.lecture_id,al.created_date LectureEntryTime," +
						 	"al.last_action_date as LastActionTime," +
						 	"IF(al.created_date = al.modified_date,al.last_action_date,al.modified_date) LectureExitTime," +
						 	"TIMEDIFF(IF(al.created_date = al.modified_date,al.last_action_date,al.modified_date),al.created_date) TotalTime " +
						 	"FROM audit_lecture al,audit_user_login aul,lecture l,class cl,course cr,institute i," +
						 	"`user` u,institute ui WHERE al.audit_user_login_id = aul.audit_user_login_id " +
						 	" AND al.lecture_id = l.lecture_id AND cl.status_id = " + statusId + " " +
						 	" AND al.status_id = " + statusId + " AND aul.status_id = " + statusId + " " +
						 	" AND u.status_id = " + statusId + " AND i.status_id = " + statusId + " " +
						 	" AND cr.status_id = " + statusId + " AND u.status_id = " + statusId + " " +
						 	" AND ui.status_id = " + statusId +  
						 	" AND l.class_id = cl.class_id AND cl.course_id = cr.course_id " +
						 	" AND cr.institute_id = i.institute_id AND aul.user_id = u.user_id " +
						 	" AND u.institute_id = ui.institute_id ");
		if(instituteType != null)
		{
			queryString.append("AND i.institute_type='" + instituteType + "' ");
		}
		if(parentInst != 0l && parentInst > 0l)
		{
			queryString.append("AND (i.parent_institute_id=" + parentInst + " OR i.institute_id=" + parentInst + ") ");
		}					 	
		queryString.append( "AND i.`institute_name` NOT IN ('QA Testing','system testing','Cinstitute163','Institute163') " +
					 		"AND !(i.`institute_name` IN ('Amrita E-Learning Research Lab') " +
					 		"AND (cl.`class_name` LIKE 'NMEICT AVIEW TEST' " +
					 		"OR cl.`class_name` LIKE 'Demo%' OR cl.class_name LIKE 'Training%' " +
					 		"OR cl.`class_name` LIKE 'National AVIEW Workshop%' " +
					 		"OR cr.`course_name` = 'National AVIEW Workshop' " +
					 		"OR cl.class_name = 'Deployment Meeting' " +
					 		"OR cl.class_name LIKE 'AVIEW Workshop Demo')) " +
					 		"GROUP BY DATE(l.date),i.institute_name,cl.class_name,l.lecture_name,u.fname,u.lname," +
					 		"u.user_name,u.email,u.mobile_number,ui.institute_name,aul.audit_user_login_id," +
					 		"i.institute_id,ui.institute_id,cr.course_id,cl.class_id,l.lecture_id" +
					 		") a " +
					 		"GROUP BY a.LDate,a.CourseInstitute,a.Class,a.Lecture,a.FirstName,a.LastName," +
					 		"a.UserName,a.UserId,a.email,a.mobile_number,a.UserInstitute,a.CourseInstituteId," +
					 		"a.UserInstituteId,a.course_id,a.class_id,a.lecture_id" +
					 		")b, district d,state s,institute bi " +
					 		"WHERE b.CourseInstituteId = bi.`institute_id` AND s.status_id = " + statusId + " " +
					 		"AND d.status_id =" + statusId + " " );
		if(instCategory != 0 && instCategory > 0)
		{
			queryString.append("AND bi.institute_category_id=" + instCategory + " ");
		}
	 	if(stateName != null)
		{
			queryString.append("AND s.state_name='" + stateName + "' ");
		}			 	
	 	queryString.append(	"AND bi.`district_id` = d.`district_id` " +
					 		"AND d.`state_id` = s.`state_id` " +
					 		"GROUP BY b.LDate,b.CourseInstitute,b.Class,b.Lecture,b.CourseInstituteId," +
					 		"b.course_id,b.class_id,b.lecture_id,s.`state_name`,bi.`institute_type`" +
					 		")c ") ;
	 	
	 	if(year!=0 && year>0)
		{
			queryString.append("WHERE YEAR(LDate) <=" + year + " ");
		}
	 	queryString.append(	"GROUP BY CONCAT(MONTHNAME(LDate),'/',YEAR(LDate)),DATE_FORMAT(LDate,'%Y/%m') " +
					 		"ORDER BY DATE_FORMAT(LDate,'%Y/%m')");	
	 	deploymentActivities = JDBCUtils.executeSQL(queryString.toString());
		if(deploymentActivities.size() > 0)
		{
			logger.info("Aview usage by state for time distribution exist");
		}
		else if(deploymentActivities.size() == 0)
		{
			logger.warn("Warning :: No Filter values is available");
		}
		logger.debug("End getAVIEWUsageData : " + TimestampUtils.getCurrentTimestamp());
		return deploymentActivities;
	}
	
	/* usage ends */
	
}
