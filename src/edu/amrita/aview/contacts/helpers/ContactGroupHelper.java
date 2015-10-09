/*
 * 
 */
package edu.amrita.aview.contacts.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.entities.Auditable;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.AppenderUtils;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.contacts.daos.ContactGroupDAO;
import edu.amrita.aview.contacts.daos.GroupUserDAO;
import edu.amrita.aview.contacts.entities.ContactGroup;
import edu.amrita.aview.contacts.entities.GroupUser;
import edu.amrita.aview.gclm.helpers.UserHelper;


/**
 * The Class ContactGroupHelper.
 */
public class ContactGroupHelper extends Auditable {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(ContactGroupHelper.class);
	//Cache code
	/** The group map. */
	private static Map<Long,ContactGroup> groupMap = Collections.synchronizedMap(new HashMap<Long,ContactGroup>());
	
	/** The Constant CACHE_CODE. */
	private static final String CACHE_CODE = "GroupHelper";
	
//	private static synchronized void populateCache(List<ContactGroup> groups)
//	{
//		groupMap.clear();
//		for(ContactGroup group:groups)
//		{
//			groupMap.put(group.getContactGroupId(), group);
//		}
//		CacheHelper.setCache(CACHE_CODE);
//	}
	
//	private static synchronized void addItemToCache(ContactGroup group) throws AViewException
//	{
//		groupMap.put(group.getContactGroupId(), group);
//	}
//	
//	private static synchronized void removeItemFromCache(ContactGroup group) throws AViewException
//	{
//		groupMap.remove(group.getContactGroupId());
//	}
//	
//	public static synchronized Map<Long,ContactGroup> getActiveGroupsIdMap() throws AViewException
//	{
//		//If cache is expired or invalidated
//		if(!CacheHelper.isCacheValid(CACHE_CODE))
//		{
//			int activeSId = StatusHelper.getActiveStatusId();			
//			List<ContactGroup> groups = ContactGroupDAO.getAllGroups(activeSId);
//			populateCache(groups);
//		}
//		return groupMap;
//	}
	
	/**
 * Creates the group.
 *
 * @param group the group to create
 * @param creatorId the creator id
 * @throws AViewException
 */
	public static ContactGroup createGroup(ContactGroup group, Long creatorId) throws AViewException
	{
		group.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
		ContactGroupDAO.createGroup(group);
		//addItemToCache(group);
		return group;
	}
	
	/**
	 * Update the existing group.
	 *
	 * @param group the group to update
	 * @param updaterId the User id
	 * @throws AViewException
	 */
	public static ContactGroup updateGroup(ContactGroup group, Long updaterId) throws AViewException
	{
		group.setModifiedAuditData(updaterId, TimestampUtils.getCurrentTimestamp());
		ContactGroupDAO.updateGroup(group);
		//addItemToCache(group);
		return group;
	}

	/**
	 * Delete an existing group.
	 *
	 * @param groupId the groupId to delete
	 * @param updaterId the User id
	 * @throws AViewException
	 */
	public static Long deleteGroup(Long groupId, Long updaterId) throws AViewException
	{
		int deletedSId = StatusHelper.getDeletedStatusId();
		ContactGroup group = ContactGroupDAO.getGroupById(groupId);
		if(group != null)
		{
			group.setStatusId(deletedSId);
			group.setContactGroupName(group.getContactGroupName() + AppenderUtils.DeleteAppender());
			updateGroup(group, updaterId);
			//removeItemFromCache(group);
			return groupId;
		}
		else
		{
			throw new AViewException("Group with id :" + groupId + ": is not found");
		}		
	}
	
	/**
	 * Gets the group.
	 *
	 * @param groupId the group id
	 * @return the group
	 * @throws AViewException
	 */
	public static ContactGroup getGroup(Long groupId) throws AViewException
	{
		return ContactGroupDAO.getGroupById(groupId);
	}
	
	/**
	 * Gets the groups by owner.
	 *
	 * @param ownerId the owner id
	 * @return the groups by owner
	 * @throws AViewException
	 */
	public static List<ContactGroup> getGroupsByOwner(Long ownerId) throws AViewException
	{
		int activeSId = StatusHelper.getActiveStatusId();
		List<ContactGroup> groups = ContactGroupDAO.getGroupsByOwner(ownerId, activeSId);
		for(ContactGroup group:groups)
		{
			group.setMemberCount(GroupUserDAO.getGroupUserCountByGroupId(group.getContactGroupId(), StatusHelper.getActiveStatusId()));
		}
		return groups ;
	}
	
    public static List<GroupUser> getAllContactsByUser(Long userId)throws AViewException
    {
    	int activeSId = StatusHelper.getActiveStatusId();
    	List<GroupUser> contacts=ContactGroupDAO.getAllContactsByOwner(userId, activeSId);
//    	List<GroupUser> allContacts=new ArrayList<GroupUser>();
    	for(GroupUser groupUser : contacts)
		{
			UserHelper.populateFKNames(groupUser.getUser());
		}
    	return contacts;
    }
    
    public static List<ContactGroup> getGroupsAndContacts(Long ownerId) throws AViewException
    {
    	List<ContactGroup> groups = getGroupsByOwner(ownerId);
    	List<GroupUser> groupUsers = getAllContactsByUser(ownerId);
    	Map<Long,ContactGroup> contactGroupMap =  new HashMap<Long,ContactGroup>();
    	for(ContactGroup grp:groups)
    	{
    		contactGroupMap.put(grp.getContactGroupId(), grp);
    	}
    	
    	for(GroupUser gUser:groupUsers)
    	{
    		ContactGroup grp = contactGroupMap.get(gUser.getContactGroupId());
    		if(grp != null)
    		{
    			grp.addGroupUser(gUser);
    		}
    	}
    	
    	return groups;
    }
	public static List<ContactGroup> searchAllgroupsForOwnerByName(Long ownerId,String name) throws AViewException
	{
		int activeSId = StatusHelper.getActiveStatusId();
		List<ContactGroup> groups = ContactGroupDAO.searchAllgroupsForOwnerByName(name, ownerId,activeSId);
		for(ContactGroup grp:groups)
		{			
			List<GroupUser> groupUsers=GroupUserHelper.getUsersFromGroup(grp.getContactGroupId());
			for(GroupUser gUser:groupUsers)
			{
				grp.addGroupUser(gUser);
			}
		}
		return groups;
		
	}
	public static void acceptSharedGroup(Long groupId,Long receiverId,Long groupTransferId) throws AViewException
	{
		ContactGroup grp=ContactGroupDAO.getGroupById(groupId);
		ContactGroup newgrp=new ContactGroup();
		newgrp.setContactGroupName(grp.getContactGroupName());
		newgrp.setGroupOwnerId(receiverId);
		newgrp.setCreatedAuditData(receiverId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
		
	    ContactGroupDAO.createGroup(newgrp);
		List<GroupUser> groupUsers=GroupUserHelper.getUsersFromGroup(groupId);
	    List<Long> newGroupUsers=new ArrayList<Long>();
		for (GroupUser gUser:groupUsers)
		{
			if(!(gUser.getUser().getUserId().equals(receiverId)))
			{
			    
				newGroupUsers.add(gUser.getUser().getUserId());
			}
			
		}
		newGroupUsers.add(grp.getGroupOwnerId());
		GroupUserHelper.addUsersToGroup(newgrp.getContactGroupId(), newGroupUsers,receiverId);
		GroupTransferHelper.deleteGroupTransfer(groupTransferId);		
	}
}
