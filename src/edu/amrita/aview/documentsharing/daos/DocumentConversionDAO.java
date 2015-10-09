/*
 * 
 */
package edu.amrita.aview.documentsharing.daos;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.documentsharing.entities.DocumentConversion;


/**
 * The Class DocumentConversionDAO.
 */
public class DocumentConversionDAO extends SuperDAO {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(DocumentConversionDAO.class);
	
	/**
	 * Gets the all pending conversions.
	 *
	 * @param statusId the status id
	 * @return the all pending conversions
	 */
	public static List<DocumentConversion> getAllPendingConversions(int statusId)
	{
		Session session = null;
		List<DocumentConversion> pendingConversions = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "select conv from DocumentConversion conv where conv.conversionStatus = :conversionStatus and statusId = :statusId";
			Query hqlQuery = session.createQuery(hqlQueryString);		
			hqlQuery.setString("conversionStatus", Constant.PENDING_CONVERSION_STATUS);
			hqlQuery.setInteger("statusId", statusId);
			pendingConversions = (List<DocumentConversion>)hqlQuery.list();			
		} catch (HibernateException he) {
			processException(he);	
		}
		finally {
			HibernateUtils.closeConnection(session);
		}
		return pendingConversions;
	}
	
	/**
	 * Creates the conversion request.
	 *
	 * @param conversion the conversion
	 */
	public static void createConversionRequest(DocumentConversion conversion)
	{
		Session session = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.save(conversion);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			processException(he);	
		}
		finally {
			HibernateUtils.closeConnection(session);
		}
	}

	
	/**
	 * Update conversion request.
	 *
	 * @param conversion the conversion
	 */
	public static void updateConversionRequest(DocumentConversion conversion)
	{
		Session session = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.update(conversion);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			processException(he);	
		}
		finally {
			HibernateUtils.closeConnection(session);
		}
	}
	
	
	/**
	 * Gets the conversion request.
	 *
	 * @param conversionId the conversion id
	 * @return the conversion request
	 */
	public static DocumentConversion getConversionRequest(Long conversionId)
	{
		DocumentConversion conversion = null;
		Session session = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			conversion = (DocumentConversion) session.get(DocumentConversion.class, conversionId);
		} catch (HibernateException he) {
			processException(he);	
		}
		finally {
			HibernateUtils.closeConnection(session);
		}
		return conversion;
	}
	
	/**
	 * Gets the all non processed documents.
	 *
	 * @param userId the user id
	 * @return the all non processed documents
	 */
	public static List<DocumentConversion> getAllNonProcessedDocuments(Long userId)
	{
		List<DocumentConversion> conversions = null;
		Session session = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "select conv from DocumentConversion conv where conv.conversionStatus not in ('Converted') " +
					"and statusId = :statusId and userId = :userId";
			Query hqlQuery = session.createQuery(hqlQueryString);		
			hqlQuery.setInteger("statusId", StatusHelper.getActiveStatusId());
			hqlQuery.setLong("userId", userId);
			conversions = (List<DocumentConversion>)hqlQuery.list();			
		} catch (HibernateException he) {
			processException(he);	
		}
		finally {
			HibernateUtils.closeConnection(session);
		}
		return conversions;
	}
	
	
	
	
}
