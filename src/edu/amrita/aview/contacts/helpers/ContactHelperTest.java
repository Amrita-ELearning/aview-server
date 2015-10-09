/*
 * 
 */
package edu.amrita.aview.contacts.helpers;

import java.util.List;

import org.junit.Test;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.contacts.entities.Contact;


/**
 * The Class ContactHelperTest.
 */
public class ContactHelperTest {

	/**
	 * Test create contact.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testCreateContact() throws AViewException{
		Contact cont=new Contact();
		//cont.setContactId(1l);
		cont.setContactName("ajay");
		cont.setMobileNumber("1212456432");
		cont.setWorkNumber("12147852369");
		cont.setEmail("dsasf@yahoo.com");
		cont.setInstituteId(1l);
		ContactHelper.createContact(cont,44l);
	}

	/**
	 * Test get contact by institute.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetContactByInstitute() throws AViewException{
		List<Contact> ds = ContactHelper.getContactByInstitute(9l);
		for(int i=0;i<ds.size();i++)
		{
			System.out.println("  name :: "+ds.get(i));
		}	
	}

}
