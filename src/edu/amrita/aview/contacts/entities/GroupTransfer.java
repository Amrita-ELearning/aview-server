package edu.amrita.aview.contacts.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import edu.amrita.aview.common.entities.Auditable;
import edu.amrita.aview.gclm.entities.User;

@Entity
@Table(name="group_transfer")
public class GroupTransfer extends Auditable{
	
	
	private Long groupTransferId=0l;
	
	private ContactGroup group=null;
	
	private User receiver=null;
	
	private User sender=null;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "group_transfer_id")
	public Long getGroupTransferId() {
		return groupTransferId;
	}

	public void setGroupTransferId(Long groupTransferId) {
		this.groupTransferId = groupTransferId;
	}

	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name="group_id")
	public ContactGroup getGroup() {
		return group;
	}

	public void setGroup(ContactGroup group) {
		this.group = group;
	}

	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name="receiver_id")
	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}
	
	@OneToOne (fetch=FetchType.EAGER)
	@JoinColumn(name="sender_id")
	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}
	

}
