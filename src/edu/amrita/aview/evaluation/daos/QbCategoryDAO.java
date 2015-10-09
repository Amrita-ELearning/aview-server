/*
 * @ (#)QbCategoryDAO.java 4.0 2013/10/16
 * 
 * Copyright  © 2013 E-Learning Research Lab, 
 * Amrita Vishwa Vidyapeetham. All rights reserved. 
 * E-Learning Research Lab and the A-VIEW logo are trademarks or
 * registered trademarks of E-Learning Research Lab. 
 * All other names used are the trademarks of their respective owners.
 */
package edu.amrita.aview.evaluation.daos;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.evaluation.entities.QbCategory;


/**
 * This class consists of queries related to category.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QbCategoryDAO extends SuperDAO
{
    
    /** The logger. */
    private static Logger logger = Logger.getLogger(QbCategoryDAO.class);

	/**
	 * Creates the qb category.
	 *
	 * @param qbCategory the qb category
	 * @throws AViewException
	 */
	public static void createQbCategory(QbCategory qbCategory) throws AViewException
	{
		Session session = null;		
		try {
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.save(qbCategory);
			session.getTransaction().commit();

		} catch (HibernateException he) {
			processException(he);
			session.getTransaction().rollback();
		} finally {
			HibernateUtils.closeConnection(session);
		}		
	}
	
	/**
	 * Update qb category.
	 *
	 * @param qbCategory the qb category
	 * @throws AViewException
	 */
	public static void updateQbCategory(QbCategory qbCategory) throws AViewException 
	{
		Session session = null;
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.update(qbCategory);
			session.getTransaction().commit() ;
		}
		catch (HibernateException he) 
		{
			processException(he);
			session.getTransaction().rollback();		
		} finally 
		{
			HibernateUtils.closeConnection(session);
		}
	}
	
	/**
	 * Gets the all active qb categories.
	 *
	 * @param statusId the status id
	 * @return the all active qb categories
	 * @throws AViewException
	 */
	public static List<QbCategory> getAllActiveQbCategories(Integer statusId) throws AViewException
	{
		Session session = null;
		List<QbCategory> qbCategories = null;

		try {
			session = HibernateUtils.getHibernateConnection();
			String sqlQuery = "select qbCategory from QbCategory qbCategory where statusId = :statusId";
			Query hqlQuery = session.createQuery(sqlQuery);
			hqlQuery.setInteger("statusId", statusId);
			qbCategories = hqlQuery.list();
			 if(qbCategories.size()>0)
             {
                 logger.info("Returned categories ");
             }
             else if(qbCategories.size() == 0)
             {
                 logger.warn("Warning :: No  category ");
             }
		} catch (HibernateException he) {
			processException(he);
		} finally {
			HibernateUtils.closeConnection(session);
		}

		return qbCategories;
	}

	/**
	 * Gets the qb category by id.
	 *
	 * @param qbCategoryId the qb category id
	 * @return the qb category by id
	 * @throws AViewException
	 */
	public static QbCategory getQbCategoryById(Long qbCategoryId) throws AViewException
	{
		Session session = null;
		QbCategory qbCategory = null;
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			qbCategory = (QbCategory)session.get(QbCategory.class, qbCategoryId);
		} 
		catch (HibernateException he) 
		{
			processException(he);
		}
		finally 
		{
			HibernateUtils.closeConnection(session);
		}
		return qbCategory;
	}

	/**
	 * Gets the all active qb categories for user.
	 *
	 * @param userId the user id
	 * @param statusId the status id
	 * @return the all active qb categories for user
	 * @throws AViewException
	 */
	@SuppressWarnings("unchecked")	
	public static List<QbCategory> getAllActiveQbCategoriesForUser(Long userId, Integer statusId) throws AViewException 
	{
		Session session = null;
		List<QbCategory> qbCategories = new ArrayList<QbCategory>();
		try 
		{
			session = HibernateUtils.getHibernateConnection();			
			String sql = "SELECT c.*, " +
						 "(SELECT u.user_name FROM user u WHERE c.created_by_user_id = u.user_id) AS createdByUserName, " +
						 "(SELECT u.user_name FROM user u WHERE c.modified_by_user_id = u.user_id) AS modifiedByUserName, " +
						 "(SELECT SUM(totalQns)FROM (" +
						 "SELECT s.qb_category_id AS cat_id, COUNT(q.qb_subcategory_id) AS totalQns " +
						 "FROM qb_question q, qb_subcategory s " +
						 "WHERE s.qb_subcategory_id = q.qb_subcategory_id AND q.status_id = :statusId " + 
						 "GROUP BY s.qb_category_id) AS derived " +
						 "WHERE c.qb_category_id = derived.cat_id) AS totalQns " +
						 "FROM qb_category c WHERE c.status_id= :statusId " + 
						 "AND c.created_by_user_id = :userId";			
			SQLQuery sqlQuery = session.createSQLQuery(sql);		
			sqlQuery.setLong("userId", userId);
			sqlQuery.setInteger("statusId", statusId);
			List<Object[]> temp = sqlQuery.list();
			
			QbCategory qbCategory = null;
			for(Object[] objA:temp)
			{
				qbCategory = new QbCategory();
				
				qbCategory.setQbCategoryId(Long.parseLong(objA[0].toString()));
				qbCategory.setQbCategoryName(objA[1].toString());
				qbCategory.setCreatedAuditData(Long.parseLong(objA[2].toString()), Timestamp.valueOf(objA[3].toString()), Integer.parseInt(objA[6].toString()));
				qbCategory.setModifiedAuditData(Long.parseLong(objA[4].toString()), Timestamp.valueOf(objA[5].toString()));
				qbCategory.setInstituteId(Long.parseLong(objA[7].toString())) ;
				//qbCategory = (QbCategory)objA[0];
				qbCategory.setCreatedByUserName(objA[8].toString());
				qbCategory.setModifiedByUserName(objA[9].toString());
				if(objA[10] != null)
				{
					qbCategory.setTotalQuestions(Integer.parseInt(objA[10].toString()));
				}
				qbCategories.add(qbCategory);				
			}
		} 
		catch (HibernateException he) 
		{
			processException(he);
		} 
		finally 
		{
			HibernateUtils.closeConnection(session);
		}

		return qbCategories;
	}

}
