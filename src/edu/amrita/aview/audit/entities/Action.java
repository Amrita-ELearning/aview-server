/*
 * 
 */
package edu.amrita.aview.audit.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;




/**
 * The Class Action.
 */
@Entity
@Table(name = "action")
public class Action extends Auditable{
	
	/** The action id. */
	private Integer actionId = 0 ;
	
	/** The action name. */
	private String actionName = null ;
	
	/** The attribute1 name. */
	private String attribute1Name = null ;
	
	/** The attribute2 name. */
	private String attribute2Name = null ;
	
	/** The attribute3 name. */
	private String attribute3Name = null ;
	
	/**
	 * Gets the action id.
	 *
	 * @return the action_id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "action_id")
	public Integer getActionId() {
		return actionId;
	}

	/**
	 * Sets the action id.
	 *
	 * @param actionId the action_id to set
	 */
	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}

	/**
	 * Gets the action name.
	 *
	 * @return the action_name
	 */
	@Column(name = "action_name")
	public String getActionName() {
		return actionName;
	}

	/**
	 * Sets the action name.
	 *
	 * @param actionName the action_name to set
	 */
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	/**
	 * Gets the attribute1 name.
	 *
	 * @return the attribute1 name
	 */
	@Column(name = "attr1_name")
	public String getAttribute1Name() {
		return attribute1Name;
	}

	/**
	 * Sets the attribute1 name.
	 *
	 * @param attribute1Name the new attribute1 name
	 */
	public void setAttribute1Name(String attribute1Name) {
		this.attribute1Name = attribute1Name;
	}

	/**
	 * Gets the attribute2 name.
	 *
	 * @return the attribute2 name
	 */
	@Column(name = "attr2_name")
	public String getAttribute2Name() {
		return attribute2Name;
	}

	/**
	 * Sets the attribute2 name.
	 *
	 * @param attribute2Name the new attribute2 name
	 */
	public void setAttribute2Name(String attribute2Name) {
		this.attribute2Name = attribute2Name;
	}

	/**
	 * Gets the attribute3 name.
	 *
	 * @return the attribute3 name
	 */
	@Column(name = "attr3_name")
	public String getAttribute3Name() {
		return attribute3Name;
	}

	/**
	 * Sets the attribute3 name.
	 *
	 * @param attribute3Name the new attribute3 name
	 */
	public void setAttribute3Name(String attribute3Name) {
		this.attribute3Name = attribute3Name;
	}

	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.actionId);
		sb.append(Constant.DELIMETER);
		sb.append(this.actionName);
		sb.append(Constant.DELIMETER);
		sb.append(this.attribute1Name);
		sb.append(Constant.DELIMETER);
		sb.append(this.attribute2Name);
		sb.append(Constant.DELIMETER);
		sb.append(this.attribute3Name);
		sb.append(Constant.DELIMETER);
		sb.append(super.toString());
		sb.append(Constant.DELIMETER);
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actionName == null) ? 0 : actionName.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Action))
			return false;
		final Action other = (Action) obj;
		if (actionName == null) {
			if (other.actionName != null)
				return false;
		} else if (!actionName.equals(other.actionName))
			return false;
		return true;
	}

}
