/*
 * @(#)QbQuestionTypeDAO.java 4.0 2013/10/16
 * 
 * Copyright  © 2013 E-Learning Research Lab, 
 * Amrita Vishwa Vidyapeetham. All rights reserved. 
 * E-Learning Research Lab and the A-VIEW logo are trademarks or
 * registered trademarks of E-Learning Research Lab. 
 * All other names used are the trademarks of their respective owners.
 */
package edu.amrita.aview.evaluation.daos;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.evaluation.QuizConstant;
import edu.amrita.aview.evaluation.entities.QbQuestionType;




/**
 * This class consists of queries related to question type.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QbQuestionTypeDAO extends SuperDAO
{
    
    /** The logger. */
    private static Logger logger = Logger.getLogger(QbQuestionTypeDAO.class);

	/**
	 * Gets the all active qb question types.
	 *
	 * @param statusId the status id
	 * @return the all active qb question types
	 * @throws AViewException
	 */
	public static List<QbQuestionType> getAllActiveQbQuestionTypes(Integer statusId) throws AViewException
	{
		Session session = null ;
		List<QbQuestionType> qbQuestionTypes = null ;
		try{
			session = HibernateUtils.getHibernateConnection();
			Query hqlQuery = session.createQuery("select qt from QbQuestionType qt where statusId = :statusId and qt.qbQuestionTypeName NOT LIKE '"+QuizConstant.POLLING_QUESTION_TYPE+"' ");
			hqlQuery.setInteger("statusId",statusId) ;
			qbQuestionTypes = hqlQuery.list() ;
			 if(qbQuestionTypes.size()>0)
             {
                 logger.info("Returned question types ");
             }
             else if(qbQuestionTypes.size() == 0)
             {
                 logger.warn("Warning :: No question type ");
             }
			
		}catch(HibernateException he){
			processException(he);
		}finally{
			HibernateUtils.closeConnection(session);
		}
		
		return qbQuestionTypes ;
	}
	
	/**
	 * Gets the qb question type by id.
	 *
	 * @param qbQuestionTypeId the qb question type id
	 * @return the qb question type by id
	 * @throws AViewException
	 */
	public static QbQuestionType getQbQuestionTypeById(Long qbQuestionTypeId) throws AViewException
	{
		Session session = null ;
		QbQuestionType qbQuestionType = null ;
		try{
		
			session = HibernateUtils.getHibernateConnection() ;
			qbQuestionType = (QbQuestionType)session.get(QbQuestionType.class, qbQuestionTypeId);			
		}
		catch(HibernateException he)
		{
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session);
		}
		return qbQuestionType ;
	}
	
	/**
	 * Creates the qb question type.
	 *
	 * @param qbQuestionType the qb question type
	 * @throws AViewException
	 */
	public static void createQbQuestionType(QbQuestionType qbQuestionType) throws AViewException
	{
		Session session = null ;
		try{
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.save(qbQuestionType);
			session.getTransaction().commit() ;			
		}
		catch(HibernateException he)
		{
			processException(he);
			session.getTransaction().rollback() ;
		}
		finally
		{
			HibernateUtils.closeConnection(session);
		}
	}
	
	/**
	 * Update qb question type.
	 *
	 * @param qbQuestionType the qb question type
	 * @throws AViewException
	 */
	public static void updateQbQuestionType(QbQuestionType qbQuestionType) throws AViewException
	{
		Session session = null ;
		try
		{			
			session = HibernateUtils.getHibernateConnection() ;
			session.beginTransaction();
			session.update(qbQuestionType);
			session.getTransaction().commit() ;			
		}
		catch(HibernateException he)
		{
			processException(he) ;
			session.getTransaction().rollback() ;
		}
		finally
		{
			HibernateUtils.closeConnection(session);
		}
	}
	
	/**
	 * Gets the qb question type by name.
	 *
	 * @param qbQuestionTypeName the qb question type name
	 * @return the qb question type by name
	 * @throws AViewException
	 */
	public static QbQuestionType getQbQuestionTypeByName(String qbQuestionTypeName) throws AViewException
	{
		Session session = null ;
		QbQuestionType qbQT = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			
			Query hqlQuery = session.createQuery("select qt from QbQuestionType qt where qt.statusId = "+StatusHelper.getActiveStatusId()+" " +
																			"	and qt.qbQuestionTypeName ='"+qbQuestionTypeName+"' ") ;
			
			qbQT = (QbQuestionType)hqlQuery.list().get(0) ;
		}
		catch(HibernateException he)
		{
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session) ;
		}
		
		return qbQT ;
	}
}
