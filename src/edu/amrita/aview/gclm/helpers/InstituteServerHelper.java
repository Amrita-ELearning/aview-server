/*
 * 
 */
package edu.amrita.aview.gclm.helpers;

import java.util.ArrayList;
import java.util.List;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.gclm.daos.InstituteServerDAO;
import edu.amrita.aview.gclm.entities.InstituteServer;


/**
 * The Class InstituteServerHelper.
 */
public class InstituteServerHelper {

		/**
		 * Creates the institute server.
		 *
		 * @param instituteServer the institute server
		 * @throws AViewException
		 */
		public static void createInstituteServer(InstituteServer instituteServer) throws AViewException
		{
			InstituteServerDAO.createInstituteServer(instituteServer);
		}
		
		/**
		 * Delete institute server.
		 *
		 * @param instituteServer the institute server
		 * @throws AViewException
		 */
		public static void deleteInstituteServer(InstituteServer instituteServer) throws AViewException
		{
			int deleteStatus = StatusHelper.getDeletedStatusId();
			instituteServer.setStatusId(deleteStatus);
			InstituteServerDAO.updateInstituteServer(instituteServer);
		}
		
		/**
		 * Update institute server.
		 *
		 * @param instituteServer the institute server
		 * @throws AViewException
		 */
		public static void updateInstituteServer(InstituteServer instituteServer) throws AViewException
		{
			InstituteServerDAO.updateInstituteServer(instituteServer);
		}
		
		/**
		 * Gets the institute servers.
		 *
		 * @param instituteId the institute id
		 * @return the institute servers
		 * @throws AViewException
		 */
		public static List<InstituteServer> getInstituteServers(Long instituteId) throws AViewException{
			Integer activeStatusId = StatusHelper.getActiveStatusId();
			List<InstituteServer> temp = InstituteServerDAO.getInstituteServers(instituteId, activeStatusId);
			List<InstituteServer> servers = new ArrayList<InstituteServer>();
			for (InstituteServer server:temp){
				int serverTypeId = server.getServerTypeId();
				String serverType =	ServerTypeHelper.getServerTypeName(serverTypeId);
				//System.out.println("serverType: " + serverType);
				server.setServerType(serverType);
				servers.add(server);
			}
			
			return servers;
		}
}
