/*
 * @(#)QbSubcategoryDAO.java 4.0 2013/10/16
 * 
 * Copyright  © 2013 E-Learning Research Lab, 
 * Amrita Vishwa Vidyapeetham. All rights reserved. 
 * E-Learning Research Lab and the A-VIEW logo are trademarks or
 * registered trademarks of E-Learning Research Lab. 
 * All other names used are the trademarks of their respective owners.
 */
package edu.amrita.aview.evaluation.daos;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.evaluation.entities.QbSubcategory;



/**
 * This class consists of queries related to sub category.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QbSubcategoryDAO extends SuperDAO 
{
    
    /** The logger. */
    private static Logger logger = Logger.getLogger(QbSubcategoryDAO.class);

 	/**
	  * Creates the qb subcategory.
	  *
	  * @param qbSubcategory the qb subcategory
	  * @throws AViewException
	  */
	 public static void createQbSubcategory(QbSubcategory qbSubcategory) throws AViewException
	{
		Session session = null ;		
		try{
			session = HibernateUtils.getHibernateConnection() ;
			session.beginTransaction();
			session.save(qbSubcategory);
			session.getTransaction().commit();
			
		}catch(HibernateException he){
			processException(he) ;
			session.getTransaction().rollback();
		}finally{
			HibernateUtils.closeConnection(session);
		}		
	}
	
	/**
	 * Update qb subcategory.
	 *
	 * @param qbSubcategory the qb subcategory
	 * @throws AViewException
	 */
	public static void updateQbSubcategory(QbSubcategory qbSubcategory) throws AViewException
	{
		Session session = null ;
		try{
			session = HibernateUtils.getHibernateConnection() ;
			session.beginTransaction();
			session.update(qbSubcategory);		
			session.getTransaction().commit() ;
		}
		catch(HibernateException he)
		{
			processException(he);
			session.getTransaction().rollback();			
		}finally
		{
			HibernateUtils.closeConnection(session);
		}
	}
	
	/**
	 * Gets the all active qb subcategories.
	 *
	 * @param statusId the status id
	 * @return the all active qb subcategories
	 * @throws AViewException
	 */
	public static List<QbSubcategory> getAllActiveQbSubcategories(Integer statusId) throws AViewException
	{
		Session session = null ;
		List<QbSubcategory> qbSubcategories = null ;
		try{			
			session = HibernateUtils.getHibernateConnection();
			String hqlQueryString = "select sc from QbSubcategory sc, QbCategory cat where " +
									"cat.statusId = :statusId and sc.statusId = :statusId " +
									"and sc.qbCategoryId = cat.qbCategoryId";
			Query hqlQuery = session.createQuery(hqlQueryString);
			hqlQuery.setInteger("statusId",statusId);			
			qbSubcategories = hqlQuery.list() ;
			
			 if(qbSubcategories.size()>0)
             {
                 logger.info("Returned subcategories ");
             }
             else if(qbSubcategories.size() == 0)
             {
                 logger.warn("Warning :: No subcategory ");
             }
			
		}catch(HibernateException he){
			processException(he);
		}finally{
			HibernateUtils.closeConnection(session);
		}
		
		return qbSubcategories ;
	}
	
	/**
	 * Gets the qb subcategory by id.
	 *
	 * @param qbSubcategoryId the qb subcategory id
	 * @return the qb subcategory by id
	 * @throws AViewException
	 */
	public static QbSubcategory getQbSubcategoryById(Long qbSubcategoryId) throws AViewException
	{
		Session session = null;
	    QbSubcategory subcatagory = null;
        try
        {
        	session = HibernateUtils.getHibernateConnection();
            subcatagory = (QbSubcategory)session.get(QbSubcategory.class, qbSubcategoryId);               
        }
        catch (HibernateException he) 
        {
            processException(he);   
        }
        finally 
        {
            HibernateUtils.closeConnection(session);
        }
        return subcatagory;
    }
	
	/**
	 * Gets the all active qb subcategories summary for category.
	 *
	 * @param qbCategoryId the qb category id
	 * @param statusId the status id
	 * @return the all active qb subcategories summary for category
	 * @throws AViewException
	 */
	@SuppressWarnings("unchecked")
	public static List<QbSubcategory> getAllActiveQbSubcategoriesSummaryForCategory(Long qbCategoryId, Integer statusId) throws AViewException
	{
		Session session = null ;
		List<QbSubcategory> qbSubcategories = new ArrayList<QbSubcategory>() ;
		try{
			session = HibernateUtils.getHibernateConnection() ;
					
			String hql = "SELECT s, " +
						"(SELECT u.userName FROM User u WHERE s.createdByUserId = u.userId) AS createdByUserName, " +
						"(SELECT u.userName FROM User u WHERE s.modifiedByUserId = u.userId) AS modifiedByUserName, " +
						"(SELECT COUNT(q.qbSubcategoryId)FROM QbQuestion q " +
						"WHERE q.qbSubcategoryId = s.qbSubcategoryId AND q.statusId= :statusId " + 
						"GROUP BY s.qbSubcategoryId) AS totalQns " +
						"FROM QbSubcategory s " +
						"WHERE s.qbCategoryId = :qbCategoryId AND s.statusId = :statusId ";
			Query sqlQuery = session.createQuery(hql);
			sqlQuery.setLong("qbCategoryId", qbCategoryId);
			sqlQuery.setInteger("statusId", statusId);
			List<Object[]> temp = sqlQuery.list();
			QbSubcategory qbSubCat = null;
			for(Object[] objA:temp)
			{
				qbSubCat = new QbSubcategory();
				qbSubCat = (QbSubcategory)objA[0] ;
				qbSubCat.setCreatedByUserName(objA[1].toString() ) ;
				qbSubCat.setModifiedByUserName(objA[2].toString()) ;				
				if(objA[3] != null)
				{
					qbSubCat.setTotalQns(Integer.parseInt(objA[3] .toString())) ;
				}
				qbSubcategories.add(qbSubCat) ;
			}
			
		}catch(HibernateException he){
			processException(he) ;
		}finally{
			HibernateUtils.closeConnection(session );
		}
		return qbSubcategories ;
	}

	/**
	 * Gets the all active qb subcategories for category.
	 *
	 * @param qbCategoryId the qb category id
	 * @param statusId the status id
	 * @return the all active qb subcategories for category
	 * @throws AViewException
	 */
	@SuppressWarnings("unchecked")
	public static List<QbSubcategory> getAllActiveQbSubcategoriesForCategory(Long qbCategoryId, Integer statusId) throws AViewException
	{
		Session session = null ;
		List<QbSubcategory> qbSubcategories = null ;
		try{
			session = HibernateUtils.getHibernateConnection() ;
			Query hqlQuery = session.createQuery("select sc from QbSubcategory sc" +
							 " where qbCategoryId=:qbCategoryId and statusId = :statusId");
			hqlQuery.setLong("qbCategoryId", qbCategoryId);			
			hqlQuery.setInteger("statusId", statusId);
			qbSubcategories = hqlQuery.list() ;
		}catch(HibernateException he){
			processException(he) ;
		}finally{
			HibernateUtils.closeConnection(session );
		}
		return qbSubcategories ;
	}
	
	/**
	 * Gets the all active qb subcategories for user.
	 *
	 * @param userId the user id
	 * @param statusId the status id
	 * @return the all active qb subcategories for user
	 * @throws AViewException
	 */
	@SuppressWarnings("unchecked")
	public static List<QbSubcategory> getAllActiveQbSubcategoriesForUser(Long userId, Integer statusId) throws AViewException
	{
		Session session = null ;
		List<QbSubcategory> qbSubcategories = null ;
		try{
			session = HibernateUtils.getHibernateConnection() ;
			Query hqlQuery = session.createQuery("select sc from QbSubcategory sc" +
					" where createdByUserId=:userId and statusId = :statusId" +
					" and qbCategoryId in (select c.qbCategoryId from QbCategory c" +
					" where c.createdByUserId=:userId and c.statusId = :statusId)");
			hqlQuery.setLong("userId", userId);			
			hqlQuery.setInteger("statusId", statusId);
			qbSubcategories = hqlQuery.list() ;
			 if(qbSubcategories.size()>0)
             {
                 logger.info("Returned subcategories ");
             }
             else if(qbSubcategories.size() == 0)
             {
                 logger.warn("Warning :: No subcategory ");
             }
		}catch(HibernateException he){
			processException(he) ;
		}finally{
			HibernateUtils.closeConnection(session );
		}
		return qbSubcategories ;
	}
	
	/**
	 * Gets the qb subcategory for name.
	 *
	 * @param qbSubcategoryName the qb subcategory name
	 * @return the qb subcategory for name
	 * @throws AViewException
	 */
	public static QbSubcategory getQbSubcategoryForName(String qbSubcategoryName) throws AViewException
	{
		Session session = null ;
		QbSubcategory qbSubcat = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			
			Query hqlQuery = session.createQuery("select s from QbSubcategory s where s.qbSubcategoryName ='"+qbSubcategoryName+"'  and s.statusId = "+StatusHelper.getActiveStatusId()+" ") ;
			
			qbSubcat = (QbSubcategory)hqlQuery.list().get(0) ;
		}
		catch(HibernateException he)
		{
			processException(he) ;
		}
		finally
		{
			HibernateUtils.closeConnection(session) ;
		}
		
		return qbSubcat ;
	}
}
