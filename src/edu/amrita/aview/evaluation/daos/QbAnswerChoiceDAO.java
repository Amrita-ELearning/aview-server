/*
 * @ (#)QbAnswerChoiceDAO.java 4.0 2013/10/16
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
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.evaluation.entities.QbAnswerChoice;




/**
 * This class consists of queries related to answer choices of a question.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QbAnswerChoiceDAO extends SuperDAO 
{
    
    /** The logger. */
    private static Logger logger = Logger.getLogger(QbAnswerChoiceDAO.class);

    /**
     * Creates the qb answer choice.
     *
     * @param qbAnswerChoice the qb answer choice
     * @throws AViewException
     */
    public static void createQbAnswerChoice(QbAnswerChoice qbAnswerChoice) throws AViewException
    {
        Session session = null;
        try 
        {
            session = HibernateUtils.getHibernateConnection();
            session.beginTransaction();
            session.save(qbAnswerChoice);
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
     
    /**
     * Update qb answer choice.
     *
     * @param qbAnswerChoice the qb answer choice
     * @throws AViewException
     */
    public static void updateQbAnswerChoice(QbAnswerChoice qbAnswerChoice) throws AViewException 
    {
//        Session session = null;
//        try 
//        {
//            session = HibernateUtils.getHibernateConnection();
//            session.beginTransaction();
        	Session session = HibernateUtils.getCurrentHibernateConnection();
            session.update(qbAnswerChoice);
//            session.getTransaction().commit();
//        } 
//        catch (HibernateException he) 
//        {
//            processException(he);
//            session.getTransaction().rollback();
//        }
//        finally 
//        {
//            HibernateUtils.closeConnection(session);
//        }
    }

    /**
     * Gets the all active qb answers choices.
     *
     * @param qbqStatusId the qbq status id
     * @param qbAnsStatusId the qb ans status id
     * @return the all active qb answers choices
     * @throws AViewException
     */
    public static List<QbAnswerChoice> getAllActiveQbAnswersChoices(Integer qbqStatusId, Integer qbAnsStatusId) throws AViewException
    {
        Session session = null;
        List<QbAnswerChoice> qbAnswersChoices = null;

        try 
        {
            session = HibernateUtils.getHibernateConnection();
            Query hqlQuery = session.createQuery("select qbAns from QbAnswerChoice qbAns,QbQuestion qbq where qbAns.statusId = :qbAnsStatusId "
                            + "and qbq.statusId = :qbqStatusId and qbAns.qbQuestion.qbQuestionId = qbq.qbQuestionId ORDER BY qbAns.displaySequence");
            hqlQuery.setInteger("qbqStatusId", qbqStatusId);
            hqlQuery.setInteger("qbAnsStatusId", qbAnsStatusId);

            qbAnswersChoices = hqlQuery.list();
            if (qbAnswersChoices.size() > 0) 
            {
                logger.info("Returned answer choices ");
            } 
            else if (qbAnswersChoices.size() == 0) 
            {
                logger.warn("Warning :: No answer choice ");
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

        return qbAnswersChoices;
    }
  
    /**
     * Gets the qb answers choices of a question.
     *
     * @param qbQuestionId the qb question id
     * @param qbAnsStatusId the qb ans status id
     * @return the qb answers choices
     * @throws AViewException
     */
    @SuppressWarnings("unchecked")
    public static List<QbAnswerChoice> getQbAnswersChoices(Long qbQuestionId,Integer qbAnsStatusId) throws AViewException
    {
        Session session = null;
        List<QbAnswerChoice> qbAnswersChoices = null;

        try 
        {
            session = HibernateUtils.getHibernateConnection();
            Query hqlQuery = session.createQuery("select qbAns from QbAnswerChoice qbAns,QbQuestion qbq where qbAns.statusId = :qbAnsStatusId "
                            + " and qbAns.qbQuestion.qbQuestionId = qbq.qbQuestionId ORDER BY qbAns.displaySequence");
            hqlQuery.setInteger("qbAnsStatusId", qbAnsStatusId);

            qbAnswersChoices = hqlQuery.list();
            if (qbAnswersChoices.size() > 0) 
            {
                logger.info("Returned answer choices ");
            } 
            else if (qbAnswersChoices.size() == 0) 
            {
                logger.warn("Warning :: No answer choice ");
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

        return qbAnswersChoices;
    }
}
