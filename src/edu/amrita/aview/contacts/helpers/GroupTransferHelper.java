package edu.amrita.aview.contacts.helpers;

import java.util.List;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.contacts.daos.GroupTransferDAO;
import edu.amrita.aview.contacts.entities.GroupTransfer;

public class GroupTransferHelper {
	
	public static GroupTransfer createGroupTransfer(GroupTransfer group, Long creatorId) throws AViewException
	{
		group.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
		GroupTransferDAO.createGroupTransfer(group);
		//addItemToCache(group);
		return group;
	}
	
	public static void createPendingGroupTransfers(List<GroupTransfer> groups, Long creatorId) throws AViewException
	{
		for(GroupTransfer group:groups)
		{
			group.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getPendingStatusId());
		}
		GroupTransferDAO.createGroupTransfers(groups);
		//addItemToCache(group);
		 
	}
	
	/**
	 * Update the existing group transfer entry.
	 *
	 * @param group the group transfer entry to update
	 * @param updaterId the User id
	 * @throws AViewException
	 */
//	public static GroupTransfer updateGroupTransfer(GroupTransfer group, Long updaterId) throws AViewException
//	{
//		group.setModifiedAuditData(updaterId, TimestampUtils.getCurrentTimestamp());
//		GroupTransferDAO.updateGroupTransfer(group);
//		//addItemToCache(group);
//		return group;
//	}

	/**
	 * Delete an existing group transfer entry.
	 *
	 * @param groupId the groupId to delete
	 * @param updaterId the User id
	 * @throws AViewException
	 */
	public static void deleteGroupTransfer(Long groupTransferId) throws AViewException
	{
		
		GroupTransfer grp= GroupTransferDAO.getGroupTransferById(groupTransferId);	
		if(grp!=null)
		GroupTransferDAO.deleteGroupTransfer(grp);
		else
		{
			throw new AViewException("Group Transfer entry is not forund");
		}
		
	}
	public static List<GroupTransfer> getTransferredGroupsByReceiver(Long receiverId)
	{
		int pendingSId=StatusHelper.getPendingStatusId();
		List<GroupTransfer> groups=GroupTransferDAO.getTransferredGroupsByReceiver(receiverId,pendingSId);
		return groups;
	}
	
}
