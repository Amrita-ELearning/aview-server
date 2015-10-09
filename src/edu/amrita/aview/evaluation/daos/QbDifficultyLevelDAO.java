/*
 * @ (#)QbDifficultyLevelDAO.java 4.0 2013/10/06
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
import edu.amrita.aview.evaluation.entities.QbDifficultyLevel;




/**
 * This class consists of queries related to difficulty level.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QbDifficultyLevelDAO extends SuperDAO 
{
    
    /** The logger. */
    private static Logger logger = Logger.getLogger(QbDifficultyLevelDAO.class);

    /**
     * Creates the difficulty level.
     *
     * @param qbDifficultyLevel the qb difficulty level
     * @throws AViewException
     */
    public static void createDifficultyLevel(QbDifficultyLevel qbDifficultyLevel) throws AViewException
	{
		Session session = null ;
		try{
		
			session = HibernateUtils.getHibernateConnection() ;
			session.beginTransaction();
			session.save(qbDifficultyLevel);
			session.getTransaction().commit() ;
		}catch(HibernateException he){
			processException(he) ;
			session.getTransaction().rollback() ;
		}finally{
			HibernateUtils.closeConnection(session) ;
		}
		
	}
	
	/**
	 * Update difficulty level.
	 *
	 * @param qbDifficultyLevel the qb difficulty level
	 * @throws AViewException
	 */
	public static void updateDifficultyLevel(QbDifficultyLevel qbDifficultyLevel) throws AViewException
	{
		Session session = null ;
		try{
			session = HibernateUtils.getHibernateConnection() ;
			session.beginTransaction();
			session.update(qbDifficultyLevel);
			session.getTransaction().commit();
			
		}catch(HibernateException he){
			processException(he) ;
			session.getTransaction().rollback() ;
		}finally{
			HibernateUtils.closeConnection(session);
		}
	}
	
	/**
	 * Gets the all active difficulty levels.
	 *
	 * @param statusId the status id
	 * @return the all active difficulty levels
	 * @throws AViewException
	 */
	public static List<QbDifficultyLevel>getAllActiveDifficultyLevels(Integer statusId) throws AViewException
	{
		Session session = null ;
		List<QbDifficultyLevel> qbDifficultyLevels = null ;
		try{
			session = HibernateUtils.getHibernateConnection() ;
			Query hqlQuery = session.createQuery("select dl from QbDifficultyLevel dl where statusId=:statusId");
			hqlQuery.setInteger("statusId", statusId) ;
			qbDifficultyLevels = hqlQuery.list() ;
			if(qbDifficultyLevels.size()> 0)
            {
				logger.info("Returned difficulty levels ");
            }
			else if(qbDifficultyLevels.size() == 0)
			{
				logger.warn("Warning :: No difficulty level ");
			}				
		}catch(HibernateException he){
			processException(he);
		}finally{
			HibernateUtils.closeConnection(session) ;
		}
		
		return qbDifficultyLevels ;
	}
	
	/**
	 * Gets the difficulty level by id.
	 *
	 * @param qbDifficultyLevelId the qb difficulty level id
	 * @return the difficulty level by id
	 * @throws AViewException
	 */
	public static QbDifficultyLevel getDifficultyLevelById(Long qbDifficultyLevelId) throws AViewException
	{
		Session session = null ;
		QbDifficultyLevel qbDifficultyLevel = null ;
		try{
			
			session = HibernateUtils.getHibernateConnection() ;
			qbDifficultyLevel = (QbDifficultyLevel)session.get(QbDifficultyLevel.class, qbDifficultyLevelId);			
		}
		catch(HibernateException he)
		{
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session );
		}
		return qbDifficultyLevel ;
	}
	
	/**
	 * Gets the qb df level for name.
	 *
	 * @param qbDifficultyLevelName the qb difficulty level name
	 * @return the qb df level for name
	 * @throws AViewException
	 */
	public static QbDifficultyLevel getQbDfLevelForName(String qbDifficultyLevelName) throws AViewException
	{
		Session session = null ;
		QbDifficultyLevel qbdl = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			
			Query hqlQuery = session.createQuery("select d from QbDifficultyLevel d where d.qbDifficultyLevelName ='"+qbDifficultyLevelName+"' and d.statusId = "+StatusHelper.getActiveStatusId()+" ") ;
			
			qbdl = (QbDifficultyLevel) hqlQuery.list().get(0) ;
		}
		catch(HibernateException he)
		{
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session) ;
		}
		
		return qbdl ;
	}
}
