/*
 * 
 */
package edu.amrita.aview.gclm.helpers;


import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.gclm.daos.ClassServerDAO;
import edu.amrita.aview.gclm.entities.ClassServer;


/**
 * The Class ClassServerHelper.
 */
public class ClassServerHelper {
	
	/**
	 * Creates the class server.
	 *
	 * @param classserver the classserver
	 * @throws AViewException
	 */
	public static void createClassServer(ClassServer classserver) throws AViewException
	{
		ClassServerDAO.createClassServer(classserver);
	}
	
	/**
	 * Delete class server.
	 *
	 * @param classserver the classserver
	 * @throws AViewException
	 */
	public static void deleteClassServer(ClassServer classserver) throws AViewException
	{
		int deletestatus=StatusHelper.getDeletedStatusId();
		classserver.setStatusId(deletestatus);
		ClassServerDAO.updateClassServer(classserver);
	}
	
	/**
	 * Update class server.
	 *
	 * @param classserver the classserver
	 * @throws AViewException
	 */
	public static void updateClassServer(ClassServer classserver) throws AViewException
	{
		ClassServerDAO.updateClassServer(classserver);
	}
}
