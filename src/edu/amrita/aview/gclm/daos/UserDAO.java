/*
 * 
 */
package edu.amrita.aview.gclm.daos;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.daos.SuperDAO;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.HibernateUtils;
import edu.amrita.aview.common.utils.ListUtils;
import edu.amrita.aview.gclm.entities.User;
import edu.amrita.aview.gclm.helpers.NodeTypeHelper;


/**
 * The Class UserDAO.
 */
public class UserDAO  extends SuperDAO{
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(UserDAO.class);
	
	/**
	 * Creates the user.
	 *
	 * @param user the user
	 * @throws AViewException
	 */
	public static void createUser(User user) throws AViewException{
		Session session = null;
		String creationMessage = null ;
		try {
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();
			session.save(user);
			session.getTransaction().commit();
		} catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		} finally {
			HibernateUtils.closeConnection(session);
		}
	}
	
	/**
	 * Update user.
	 *
	 * @param user the user
	 * @throws AViewException
	 */
	public static void updateUser(User user) throws AViewException{
		Session session = null;
		String creationMessage = null ;		
		try {
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();			
			session.update(user);			
			session.getTransaction().commit();
		} catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		} finally {
			HibernateUtils.closeConnection(session);
		}
	}

	/**
	 * Gets the active users.
	 *
	 * @param statusId the status id
	 * @return the active users
	 * @throws AViewException
	 */
	public static List<User> getActiveUsers(Integer statusId) throws AViewException{
		Session session = null;
		List<User> users = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			Query hqlQuery = session.createQuery("select aviewUser from User aviewUser where statusId=:statusId");
			hqlQuery.setInteger("statusId",statusId);
			users = hqlQuery.list();
		} catch (HibernateException he) {
			processException(he);	
		} finally {
			HibernateUtils.closeConnection(session);
		}

		return users;
	}

	/**
	 * Gets the active guest users.
	 *
	 * @param statusId the status id
	 * @return the active guest users
	 * @throws AViewException
	 */
	public static List<User> getActiveGuestUsers(Integer statusId) throws AViewException{
		Session session = null;
		List<User> users = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			Query hqlQuery = session.createQuery("select u from User u,Institute i where u.statusId=:statusId and role='GUEST' and u.instituteId = i.instituteId and i.instituteName = 'Guest Institute For Direct Landing - India'");
			hqlQuery.setInteger("statusId",statusId);
			users = hqlQuery.list();
		} catch (HibernateException he) {
			processException(he);	
		} finally {
			HibernateUtils.closeConnection(session);
		}

		return users;
	}
	
	/**
	 * Gets the user.
	 *
	 * @param userId the user id
	 * @return the user
	 * @throws AViewException
	 */
	public static User getUser(Long userId) throws AViewException{
		Session session = null;
		User user = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			user = (User)session.get(User.class, userId);
		} catch (HibernateException he) {
			processException(he);	
		} finally {
			HibernateUtils.closeConnection(session);
		}
		return user;
	}
	
	/**
	 * Gets the non deleted users by institute id.
	 *
	 * @param instituteId the institute id
	 * @param statusId the status id
	 * @return the non deleted users by institute id
	 * @throws AViewException
	 */
	public static List<User> getNonDeletedUsersByInstituteId(Long instituteId,Integer statusId) throws AViewException{
		Session session = HibernateUtils.getCurrentHibernateConnection();
		List<User> users = null;
		try {
			String query = "select u from User u where u.instituteId = :instituteId " +
					"and u.statusId != :statusId";
			Query hqlQuery = session.createQuery(query);
			hqlQuery.setLong("instituteId", instituteId);
			hqlQuery.setInteger("statusId", statusId);
			users = hqlQuery.list();
		} catch (HibernateException he) {
			processException(he);	
		}
		return users;
	}
	
	/**
	 * Delete users.
	 *
	 * @param users the users
	 * @throws HibernateException the hibernate exception
	 */
	public static void deleteUsers(List<User> users) throws HibernateException
	{		
		Session session = HibernateUtils.getCurrentHibernateConnection();
		for(int i=0;i<users.size();i++)
		{			
			User user = users.get(i);
			session.saveOrUpdate(user);
			//Once the batch is full, then send them to db
			if((i+1) % HibernateUtils.HIBERNATE_BATCH_SIZE == 0)
			{
				//System.out.println("In batch operation");
				session.flush();
				session.clear();
			}
		}
	}
	
	/**
	 * Gets the users.
	 *
	 * @param upto1000userIds the upto1000user ids
	 * @return the users
	 * @throws AViewException
	 */
	public static List<User> getUsers(List<Long> upto1000userIds) throws AViewException{
		Session session = null;
		List<User> users = null;
		try {
			session = HibernateUtils.getHibernateConnection();
			String query = "select aviewUser from User aviewUser where userId in(#)";
			query = query.replaceAll("#", ListUtils.getNumericListAsCommaDelimitedString(upto1000userIds));
			Query hqlQuery = session.createQuery(query);
			users = hqlQuery.list();
		} catch (HibernateException he) {
			processException(he);	
		} finally {
			HibernateUtils.closeConnection(session);
		}
		return users;
	}
	
	/**
	 * Update users.
	 *
	 * @param users the users
	 * @throws AViewException
	 */
	public static void updateUsers(List<User> users) throws AViewException{
		Session session = null;
		String creationMessage = null ;		
		try {
			session = HibernateUtils.getHibernateConnection();
			session.beginTransaction();	
			
			long count = 0;
			for(User user:users)
			{
				session.update(user);
				count++;
				//Once the batch is full, then send them to db
				if(count % HibernateUtils.HIBERNATE_BATCH_SIZE == 0)
				{
					session.flush();
					session.clear();
				}
			}
			session.getTransaction().commit();
		} catch (HibernateException he) {
			processException(he);	
			session.getTransaction().rollback();
		} finally {
			HibernateUtils.closeConnection(session);
		}
	}

	/**
	 * Gets the user by user name.
	 *
	 * @param userName the user name
	 * @param statusId the status id
	 * @return the user by user name
	 * @throws AViewException
	 */
	public static User getUserByUserName(String userName,Integer statusId) throws AViewException
	{
		Session session = null ;
		User user = null ;
		try{
			session = HibernateUtils.getHibernateConnection() ;			
			Query hqlQuery  = session.createQuery("select u from User u,Institute i " +
					"where u.userName=:userName and u.instituteId = i.instituteId " +
					"and u.statusId = :statusId and i.statusId = :statusId");
			
			hqlQuery.setString("userName", userName);
			hqlQuery.setInteger("statusId", statusId);
			List temp = hqlQuery.list();
			
			if(temp.size() == 1)
			{
				user = (User)(temp.get(0));
			}
			else if(temp.size() == 0)
			{
				logger.debug("Warning :: No Active User returned for the given userName");
			}
			else if(temp.size() > 1)
			{
				logger.debug("Warning :: More than one Active User returned for the given userName");
			}
		}catch(HibernateException he){
			processException(he);	
		}finally{
			HibernateUtils.closeConnection(session);
		}
		
		return user ;
	}
	

	/**
	 * Gets the user by user name password.
	 *
	 * @param userName the user name
	 * @param password the password
	 * @param statusId the status id
	 * @return the user by user name password
	 * @throws AViewException
	 */
	public static User getUserByUserNamePassword(String userName, String password, Integer statusId) throws AViewException
	{
		Session session = null ;
		User user = null ;
		try{
			session = HibernateUtils.getHibernateConnection() ;	
			
			String hqlQueryString = "select u from User u,Institute i " +
									"where u.userName=:userName and u.password = :password and u.instituteId = i.instituteId " +
									"and u.statusId = :statusId and i.statusId = :statusId";
			
			Query hqlQuery = session.createQuery(hqlQueryString);
			
			hqlQuery.setString("userName", userName);
			hqlQuery.setString("password", password);
			hqlQuery.setInteger("statusId", statusId);
			List temp = hqlQuery.list();
			
			if(temp.size() == 1)
			{
				user = (User)(temp.get(0));
			}
			else if(temp.size() == 0)
			{
				logger.debug("Warning :: No Active User returned for the given userName & Password");
			}
			else if(temp.size() > 1)
			{
				logger.debug("Warning :: More than one Active User returned for the given userName & Password");
			}
		}catch(HibernateException he){
			processException(he);	
		}finally{
			HibernateUtils.closeConnection(session);
		}
		
		return user ;
	}
	
	// Fix for bug #2567
	// 1. Added new parameter statusId
	// 2. Check the status id of retrieving users
	/**
	 * Gets the users by role.
	 *
	 * @param role the role
	 * @param statusId the status id
	 * @return the users by role
	 * @throws AViewException
	 */
	public static List<User> getUsersByRole(String role,Integer statusId) throws AViewException
	{
		Session session = null ;
		List<User> users = null ;
		try{
			session = HibernateUtils.getHibernateConnection() ;			
			Query hqlQuery  = session.createQuery("select users from User users where role=:role and statusId = :statusId");
			hqlQuery.setString("role", role);
			hqlQuery.setInteger("statusId", statusId);
			users = hqlQuery.list() ;
		}catch(HibernateException he){
			processException(he);	
		}finally{
			HibernateUtils.closeConnection(session);
		}
		
		return users ;
	}
	
	/**
	 * Gets the user count.
	 *
	 * @param instituteId the institute id
	 * @param statusId the status id
	 * @return the user count
	 * @throws AViewException
	 */
	public static Integer getUserCount(Long instituteId,Integer statusId) throws AViewException{
		Session session = null;
		Integer userCount = -1;
		try {
			session = HibernateUtils.getHibernateConnection();
			Query hqlQuery = session.createQuery("select count(aviewUser.userId) from User aviewUser " +
					"where aviewUser.statusId=:statusId " +
					"and aviewUser.instituteId = :instituteId");
			hqlQuery.setInteger("statusId",statusId);
			hqlQuery.setLong("instituteId",instituteId);
			userCount = Integer.parseInt(hqlQuery.list().get(0).toString());

		} catch (HibernateException he) {
			processException(he);	
		}		
		finally {
			HibernateUtils.closeConnection(session);
		}

		return userCount;
	}
	
	// Fix for Bug #13227 start
	/**
	 * Search users.
	 *
	 * @param firstName the first name
	 * @param lastName the last name
	 * @param userName the user name
	 * @param role the role
	 * @param city the city
	 * @param instituteId the institute id
	 * @param emailId the email id
	 * @param mobileNumber the mobile number
	 * @param instituteAdminId the institute admin id
	 * @param statusId the status id
	 * @return the list
	 * @throws AViewException
	 */
	public static List<User> searchUsers(String firstName ,String lastName , String userName,
			String role,String city,Long instituteId,String emailId, String mobileNumber,
			Long districtId, Long stateId,
			Long instituteAdminId,Integer statusId) throws AViewException
	// Fix for Bug #13227 end		
	{
		Session session = null ;
		List<User> users = null ;
		try{
			session = HibernateUtils.getHibernateConnection() ;	
			String hqlQueryString = 
					"select users from User users,Institute inst " +
					"where users.instituteId = inst.instituteId " +
					"and users.statusId=:statusId and inst.statusId=:activeStatusId";
			if(firstName != null && firstName.trim() != "")
			{
				hqlQueryString += " and users.fname like :firstName" ;
			}
			if(lastName != null && lastName.trim() != "")
			{
				hqlQueryString += " and users.lname like :lastName" ;
			}
			if(userName != null && userName.trim() != "")
			{
				hqlQueryString += " and users.userName like :userName " ;
			}
			if(role != null && role.trim() != "")
			{
				hqlQueryString += " and users.role=:role" ;
			}
			if(city != null && city.trim() != "")
			{
				hqlQueryString += " and users.city like :city" ;
			}
			if(instituteId != null && instituteId > 0)
			{
				hqlQueryString += " and inst.instituteId = :instituteId" ;
			}
			if(emailId != null && emailId.trim() != "")
			{
				hqlQueryString += " and users.email = :emailId ";
			}
			if(mobileNumber != null && mobileNumber.trim() != "")
			{
				hqlQueryString += " and users.mobileNumber = :mobileNumber ";
			}
			// Fix for Bug #13227 start
			if(districtId != null && districtId > 0)
			{
				hqlQueryString += " and users.districtId = :districtId ";
			}
			else if(stateId != null && stateId > 0)
			{
				hqlQueryString += " and users.districtId IN (SELECT d.districtId FROM " +
								  " District d where d.stateId = :stateId) ";
			}
			// Fix for Bug #13227 end
			if(instituteAdminId != null && instituteAdminId > 0)
			{
				hqlQueryString += 
					" AND users.instituteId IN " +
						"(SELECT distinct i.instituteId " +
						"FROM InstituteAdminUser iau,Institute i " +
						"WHERE iau.user.userId = :adminId " +
						"AND (iau.institute.instituteId = i.instituteId " +
							"OR iau.institute.instituteId = i.parentInstituteId))" ;
			}
			
			Query hqlQuery  = session.createQuery(hqlQueryString);
			
			if(firstName != null && firstName.trim() != "")
			{
				hqlQuery.setString("firstName", '%'+ firstName + '%');
			}
			if(lastName != null && lastName.trim() != "")
			{
				hqlQuery.setString("lastName", '%'+ lastName + '%');
			}			
			if(userName != null && userName.trim() != "")
			{
				hqlQuery.setString("userName",'%'+ userName + '%');
			}
			if(role != null && role.trim() != "")
			{
				hqlQuery.setString("role", role);
			}
			if(city != null && city.trim() != "")
			{
				hqlQuery.setString("city",'%'+ city + '%');
			}
			if(instituteId != null && instituteId > 0)
			{
				hqlQuery.setLong("instituteId",instituteId);
			}
			if(instituteAdminId != null && instituteAdminId > 0)
			{
				hqlQuery.setLong("adminId", instituteAdminId);
			}
			if(emailId != null && emailId.trim() != "")
			{
				hqlQuery.setString("emailId", emailId);
			}
			if(mobileNumber != null && mobileNumber.trim() != "")
			{
				hqlQuery.setString("mobileNumber", mobileNumber);
			}
			// Fix for Bug #13227 start
			if(districtId != null && districtId > 0)
			{
				hqlQuery.setLong("districtId", districtId);
			}
			else if(stateId != null && stateId > 0)
			{
				hqlQuery.setLong("stateId", stateId);
			}
			// Fix for Bug #13227 end
			if(instituteAdminId != null && instituteAdminId > 0)
			{
				hqlQuery.setLong("adminId", instituteAdminId);
			}
			
			hqlQuery.setInteger("statusId",statusId);
			hqlQuery.setInteger("activeStatusId",StatusHelper.getActiveStatusId());
			users = hqlQuery.list() ;
		}catch(HibernateException he){
			processException(he);	
		}finally{
			HibernateUtils.closeConnection(session);
		}
		
		return users ;
	}
	
	/**
	 * Gets the users for live quiz.
	 *
	 * @param classId the class id
	 * @param statusId the status id
	 * @return the users for live quiz
	 * @throws AViewException
	 */
	public static List<User> getUsersForLiveQuiz(Long classId, Integer statusId) throws AViewException
	{
		Session session = null ;
		List<User> liveQuizUsers = null ;
		try{
			session = HibernateUtils.getHibernateConnection();
			Query hqlQuery  = session.createQuery("SELECT u " +
												  "FROM User u, NodeType nt, ClassRegistration cr " +
												  "WHERE u.userId = cr.user.userId " + 
												  "AND cr.nodeTypeId = :nodeTypeId " +
												  "AND nt.nodeTypeId = cr.nodeTypeId " +
												  "AND u.role = 'STUDENT' " + 
							  					  "AND cr.aviewClass.classId = :classId " + 
												  "AND u.statusId = :statusId");
			logger.debug(hqlQuery) ;
			hqlQuery.setLong("classId", classId);
			hqlQuery.setInteger("nodeTypeId", NodeTypeHelper.getClassroomNodeType());
			hqlQuery.setInteger("statusId", statusId);
			liveQuizUsers = hqlQuery.list() ;
		}catch(HibernateException he){
			processException(he);
		}finally{
			HibernateUtils.closeConnection(session);
		}		
		return liveQuizUsers ;
	}
	
	/**
	 * Gets the user by mobile number.
	 *
	 * @param mobileNumber the mobile number
	 * @param statusId the status id
	 * @return the user by mobile number
	 * @throws AViewException
	 */
	public static User getUserByMobileNumber(String mobileNumber, Integer statusId) throws AViewException
	{
		Session session = null ;
		User user = null ;
		try
		{
			session = HibernateUtils.getHibernateConnection() ;
			Query hqlQuery  = session.createQuery("select user from User user where mobileNumber=:mobileNumber and statusId = :statusId");
			
			hqlQuery.setString("mobileNumber", mobileNumber);
			hqlQuery.setInteger("statusId", statusId);
			List temp = hqlQuery.list();
			if(temp.size() == 1)
			{
				user = (User)(temp.get(0));
			}
			else if(temp.size() == 0)
			{
				logger.debug("Warning :: No Active User returned for the given mobileNumber");
			}
			else if(temp.size() > 1)
			{
				logger.debug("Warning :: More than one Active User returned for the given mobileNumber");
			}
		}
		catch(HibernateException he)
		{
			processException(he);	
		}
		finally
		{
			HibernateUtils.closeConnection(session);
		}		
		return user;
	}	
	
	/**
	 * Gets the users by institute id.
	 *
	 * @param instituteId the institute id
	 * @param statusId the status id
	 * @return the users by institute id
	 * @throws AViewException
	 */
	@SuppressWarnings("unchecked")
	public static List<User> getUsersByInstituteId(long instituteId, Integer statusId) throws AViewException
	{
		Session session = null ;
		List<User> users=null;
		try
		{
			
			session = HibernateUtils.getHibernateConnection() ;	
			Query hqlQuery  = session.createQuery("select users from User users where instituteId=:instituteId and statusId = :statusId");
			
			hqlQuery.setLong("instituteId", instituteId);
			hqlQuery.setInteger("statusId", statusId);	
			users=hqlQuery.list();
			if(users.size()==0)
			{
				logger.debug("Warning :: No Active User returned for the given InstituteId");
			}
		}
		catch(HibernateException he)
		{
			processException(he);
		}
		return users;
	}
	public static List<User> searchUsersByName(String name,Integer statusId) throws AViewException
	{
		Session session = null ;
		List<User> users = null ;
		try{
			session = HibernateUtils.getHibernateConnection() ;	
			String hqlQueryString = 
					"select users from User users " +
					"where  users.statusId=:statusId ";
			if(name != null && name.trim() != "")
			{
				hqlQueryString += " and (users.fname like :name" ;
			
				hqlQueryString += " or users.lname like :name" ;
			
				hqlQueryString += " or users.userName like :name " ;
				
				hqlQueryString += " or users.email like :name) ";
			}
			Query hqlQuery  = session.createQuery(hqlQueryString);
			hqlQuery.setString("name",'%'+ name + '%');
			hqlQuery.setInteger("statusId",statusId);
			
			users = hqlQuery.list() ;
		}catch(HibernateException he){
			processException(he);	
		}finally{
			HibernateUtils.closeConnection(session);
		}
		
		return users ;
	}
}
