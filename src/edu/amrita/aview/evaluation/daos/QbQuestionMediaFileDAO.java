package edu.amrita.aview.evaluation.daos;
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
import edu.amrita.aview.common.utils.ListUtils;
import edu.amrita.aview.evaluation.entities.QbQuestionMediaFile;
public class QbQuestionMediaFileDAO  extends SuperDAO{
	private static Logger logger = Logger.getLogger(QbQuestionMediaFileDAO.class);

	public static void createQbQuestionMediaFile(QbQuestionMediaFile qbQuestionMediaFile) throws AViewException{
		Session session = null;
		//String creationMessage = null ;
		try 
		{
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();	
			session.save(qbQuestionMediaFile);
			session.getTransaction().commit();
		}
		catch (HibernateException he) 
		{
			processException(he);	
			session.getTransaction().rollback();
		} finally {
			HibernateUtils.closeConnection(session);
		}
	}
	public static List<QbQuestionMediaFile> getQbQuestionMediaFileForQbQuestionId(Long qbquestionId) throws AViewException
	{
		Session session = null ;
		//QbQuestionImage quizQuest = null ;
		List<QbQuestionMediaFile> quizQuest = new ArrayList<QbQuestionMediaFile>();
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			
			String hqlQueryString = "select qbQuestionMediaFile " +
									" from QbQuestionMediaFile qbQuestionMediaFile " +
									" where qbQuestionMediaFile.qbQuestion.qbQuestionId = :qbquestionId " ;
			
			Query hqlQuery = session.createQuery(hqlQueryString) ;
			hqlQuery.setLong("qbquestionId", qbquestionId) ;
			//quizQuest = (QbQuestionImage) hqlQuery.list() ;		
			quizQuest = hqlQuery.list();
			if(quizQuest.size()>0)
			{
				logger.info("Returned questions ");
			}
			else if(quizQuest.size() == 0)
			{
				logger.warn("Warning :: No question ");
			}
			/*
			else if (quizQuest.size() == 1) 
			{
				quizquestionimage = (QbQuestionImage) quizQuest.get(0);
			} 		
			*/
		}
		catch(HibernateException he)
		{
			processException(he);			
		}
		finally
		{
			HibernateUtils.closeConnection(session) ;
		}
		
		return quizQuest ;
	}
	
	public static void updateQbQuestionMediaFile(QbQuestionMediaFile qbQuestionMediaFile) throws AViewException
	{
		Session session = null;

		try 
		{
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.saveOrUpdate(qbQuestionMediaFile);
			session.getTransaction().commit();
		}
		catch (HibernateException he)
		{
			processException(he);
			session.getTransaction().rollback();
		}
		finally
		{
			HibernateUtils.closeConnection(session);
		}
	}
	
	public static void deleteQbQuestionMediaFileByQuestionId(List<Long> qbQuestionIds) throws AViewException
	{
		Session session = null; 
		try
		{
			String questionIds = ListUtils.getNumericListAsCommaDelimitedString(qbQuestionIds);
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			String sqlString = "DELETE from qb_question_media_file where qb_question_id IN (" + questionIds + " )";
			
			SQLQuery sqlQuery = session.createSQLQuery(sqlString);
//			sqlQuery.setLong("qbQuestionId", qbQuestionId);
			int result = sqlQuery.executeUpdate();
			session.getTransaction().commit();
					
			if (result == 0)
			{
				logger.debug("Didn't delete any row!");
			}
			else
			{
				logger.debug("Deleted Row: " + result);
			}
		}
		catch (HibernateException e) {
			processException(e);
		}
		finally
		{
			
		}
//			session.delete(qbQuestionMediaFile);
	}
}
