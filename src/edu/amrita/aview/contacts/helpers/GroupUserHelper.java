/*
 * 
 */
package edu.amrita.aview.contacts.helpers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.entities.Auditable;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.ListUtils;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.contacts.daos.GroupUserDAO;
import edu.amrita.aview.contacts.entities.GroupUser;
import edu.amrita.aview.gclm.entities.User;
import edu.amrita.aview.gclm.helpers.UserHelper;


/**
 * The Class GroupUserHelper.
 */
public class GroupUserHelper extends Auditable {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(GroupUserHelper.class);
	
	/**
	 * Creates the group.
	 *
	 * @param groupUser the group user
	 * @param creatorId the creator id
	 * @throws AViewException
	 */
	public static void createGroupUser(GroupUser groupUser, Long creatorId) throws AViewException
	{
		groupUser.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
		GroupUserDAO.createGroupUser(groupUser);		
	}
	
	/**
	 * Update the existing group.
	 *
	 * @param groupUser the groupUser to update
	 * @param updaterId the User id
	 * @throws AViewException
	 */
	public static void updateGroupUser(GroupUser groupUser, Long updaterId) throws AViewException
	{
		groupUser.setModifiedAuditData(updaterId, TimestampUtils.getCurrentTimestamp());
		GroupUserDAO.updateGroupUser(groupUser);		
	}

	/**
	 * Delete an existing group.
	 *
	 * @param groupUserId the groupId to delete
	 * @param updaterId the User id
	 * @throws AViewException
	 */
	public static void deleteGroupUser(Long groupUserId, Long updaterId) throws AViewException	//RadhaCR - parameter updaterId never used.
	{
		GroupUserDAO.deleteGroupUser(groupUserId);
	}
	
	/**
	 * Add an user to an existing group.
	 *
	 * @param groupId the groupId
	 * @param userId the User id
	 * @throws AViewException
	 */
	//RadhaCR - could use createGroupUser method
	public static void addUserToGroup(Long groupId, Long userId) throws AViewException
	{
		GroupUser groupUser = new GroupUser();
		groupUser.setUser(UserHelper.getUser(userId));
		groupUser.setContactGroupId(groupId);
		createGroupUser(groupUser, userId);
	}
	
	/**
	 * Add list of users to an existing group.
	 *
	 * @param groupId the groupId
	 * @param userIds the User id
	 * @param creatorId the creator id
	 * @throws AViewException
	 */	
	//RadhaCR - createGroupUsers(List<GroupUser>) instead of addUsersToGroup,
	public static void addUsersToGroup(Long groupId, List<Long> userIds, Long creatorId) throws AViewException
	{
		List<List> brokenUserIdsList = ListUtils.breakListInto1000s(userIds);
		
		List<GroupUser> groupUsersToAdd = new ArrayList<GroupUser>();
		
		GroupUser groupUser = null; 
		
		for(List brokenUserIds:brokenUserIdsList)
		{
			List<User> users =UserHelper.getUsers(brokenUserIds);
			for(User user :users)
			{
				groupUsersToAdd.clear();
				groupUser = new GroupUser();
				groupUser.setUser(user);
				groupUser.setContactGroupId(groupId);
				groupUser.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
				groupUsersToAdd.add(groupUser);
				GroupUserDAO.createGroupUsers(groupUsersToAdd);
			}
		}
		
	}
	
	/**
	 * Delete an user belonging to a group.
	 *
	 * @param groupUserId the group user id
	 * @param updaterId the updater Id
	 * @throws AViewException
	 */
	//RadhaCR - could use deleteGroupUser
	public static void deleteUserFromGroup(Long groupUserId, Long updaterId) throws AViewException	//RadhaCR - parameter updaterId never used.
	{	
		GroupUserDAO.deleteGroupUser(groupUserId);	
	}
	
	
	
	/**
	 * Get the users belonging to a group.
	 *
	 * @param groupId the group Id
	 * @return Users
	 * @throws AViewException
	 */
	public static List<GroupUser> getUsersFromGroup(Long groupId) throws AViewException
	{
		List<GroupUser> groupUsers = GroupUserDAO.getUsersByGroupId(groupId, StatusHelper.getActiveStatusId());		
		for(GroupUser groupUser : groupUsers)
		{
			UserHelper.populateFKNames(groupUser.getUser()) ;			
		}
		return groupUsers;
	}
	
	/**
	 * Delete list of users belonging to a group.
	 *
	 * @param groupUserIds the group user ids
	 * @param updaterId the updater Id
	 * @throws AViewException
	 */
	//RAdhaCR - deleteUsersFromGroups
	public static void deleteUsersFromGroup(List<Long> groupUserIds, Long updaterId) throws AViewException	//RadhaCR - parameter updaterId never used.
	{
		 List<List> brokenUserIdsList = ListUtils.breakListInto1000s(groupUserIds);         
         List<GroupUser> groupUsersToDelete = new ArrayList<GroupUser>();          
         for(List brokenUserIds:brokenUserIdsList)
         {
        	 groupUsersToDelete.clear(); 
        	 List<GroupUser> users = GroupUserDAO.getGroupUsers(brokenUserIds);      	
        	 groupUsersToDelete.addAll(users);
        	 GroupUserDAO.deleteGroupUsers(groupUsersToDelete);
        	
         }
         
	}
}
