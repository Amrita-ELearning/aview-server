/*
 * 
 */
package edu.amrita.aview.gclm.helpers;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.gclm.daos.PeopleCountDAO;
import edu.amrita.aview.gclm.entities.PeopleCount;

public class PeopleCountHelper {	

	/** The Constant CACHE_CODE. */
	private static final String CACHE_CODE = "PeopleCountHelper";
	
	public static void createPeopleCount(PeopleCount peopleCount,Long creatorId) throws AViewException
	{
		peopleCount.setPeopleCountTimestamp(TimestampUtils.getCurrentTimestamp());
		peopleCount.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
		PeopleCountDAO.createPeopleCount(peopleCount);
		
	}

	public static List<PeopleCount> getPeopleCount(Long lectureId) throws AViewException
	{
		List<PeopleCount> peopleCount = PeopleCountDAO.getPeopleCount(lectureId);
		return peopleCount;
//		peopleCount.setPeopleCountTimestamp(TimestampUtils.getCurrentTimestamp());
//		peopleCount.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
//		PeopleCountDAO.createPeopleCount(peopleCount);
		
	}
	/**
	 * Process exception.
	 *
	 * @param he the he
	 * @throws AViewException
	 */
	private static void processException(HibernateException he) throws AViewException
	{
		String exceptionMessage = null ;
		exceptionMessage = he.getMessage();
		if(he.getCause() != null && he.getCause().getMessage() != null)
		{
			exceptionMessage = he.getCause().getMessage();
		}
		else
		{
			exceptionMessage = he.getMessage();
		}
//		logger.error(exceptionMessage,he);
		throw (new AViewException(exceptionMessage));
	}
	
}
