/*
 * 
 */
package edu.amrita.aview.contacts.helpers;

import java.util.List;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.contacts.daos.ContactDAO;
import edu.amrita.aview.contacts.entities.Contact;


/**
 * The Class ContactHelper.
 */
public class ContactHelper {
	
	
	/**
	 * Creates the contact.
	 *
	 * @param contact the contact
	 * @param creatorId the creator id
	 * @throws AViewException
	 */
	public static void createContact(Contact contact,Long creatorId) throws AViewException
	{			
		contact.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
		ContactDAO.createContact(contact);
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
		List<Contact> contacts= ContactDAO.getContactByInstitute(instituteId);
		return contacts;
	}
}
