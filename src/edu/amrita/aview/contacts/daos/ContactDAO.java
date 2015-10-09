/*
 * 
 */

package edu.amrita.aview.contacts.daos;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.contacts.entities.Contact;


/**
 * The Class ContactDAO.
 */
public class ContactDAO extends SuperDAO {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(ContactDAO.class);
	
	/**
	 * Creates the contact.
	 *
	 * @param contact the contact
	 * @throws AViewException
	 */
	public static void createContact(Contact contact) throws AViewException
	{
		Session session = null;
		try	{
				session = HibernateUtils.getHibernateConnection();
				session.beginTransaction();	
				session.save(contact);
				session.getTransaction().commit();
		}catch (HibernateException he){
			processException(he);	
			session.getTransaction().rollback();
		}finally{
			HibernateUtils.closeConnection(session);
		}
		logger.info("Contact successfully inserted");
	}
	
	/**
	 * Gets the contact by institute.
	 *
	 * @param instituteId the institute id
	 * @return the contact by institute
	 * @throws AViewException
	 */
	public static List<Contact> getContactByInstitute(Long instituteId) throws AViewException
	{
		Session session = null;
		List<Contact>  contacts= null;
		try {
				session = HibernateUtils.getHibernateConnection();
				Query hqlQuery = session.createQuery("SELECT c.contactId,c.contactName," +
												 	 "inst.instituteId FROM Contact c," +
												 	 "Institute inst WHERE " +
												 	 "c.instituteId=inst.instituteId " +
												 	 "AND c.instituteId=:instituteId");		
				hqlQuery.setLong("instituteId",instituteId);
				contacts =  hqlQuery.list();
				if(contacts.size()>0)
				{
					logger.info("Contact Details by institute available ");
				}
				else if(contacts.size() == 0)
				{
					logger.warn("Warning :: No Contact Details available ");
				}
		}catch (HibernateException he) {
			processException(he);	
		}finally {
			HibernateUtils.closeConnection(session);
		}
		return contacts;
	}
}
