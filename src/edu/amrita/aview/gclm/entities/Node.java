/**
 * 
 */
package edu.amrita.aview.gclm.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.entities.Auditable;


/**
 * The Class Node.
 *
 * @author
 */
@Entity
@Table(name = "node")
public class Node extends Auditable {
	
	/** The node id. */
	private int nodeId = 0 ;
	
	/** The node name. */
	private String nodeName = null ;
	
	/** The node type id. */
	private int nodeTypeId = 0 ;
	
	/** The node location. */
	private String nodeLocation = null ;

	/**
	 * Gets the node id.
	 *
	 * @return the node_id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)	
	@Column(name = "node_id")
	public int getNodeId() {
		return nodeId;
	}

	/**
	 * Sets the node id.
	 *
	 * @param nodeId the node_id to set
	 */
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * Gets the node name.
	 *
	 * @return the node_name
	 */
	@Column(name = "node_name")
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * Sets the node name.
	 *
	 * @param nodeName the node_name to set
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * Gets the node type id.
	 *
	 * @return the node_type_id
	 */
	@Column(name = "node_type_id")
	public int getNodeTypeId() {
		return nodeTypeId;
	}

	/**
	 * Sets the node type id.
	 *
	 * @param nodeTypeId the node_type_id to set
	 */
	public void setNodeTypeId(int nodeTypeId) {
		this.nodeTypeId = nodeTypeId;
	}

	/**
	 * Gets the node location.
	 *
	 * @return the node_location
	 */
	@Column(name = "node_location")
	public String getNodeLocation() {
		return nodeLocation;
	}

	/**
	 * Sets the node location.
	 *
	 * @param nodeLocation the node_location to set
	 */
	public void setNodeLocation(String nodeLocation) {
		this.nodeLocation = nodeLocation;
	}

	/**
	 * To string.
	 *
	 * @return the nodeTypeName
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.nodeId);
		sb.append(Constant.DELIMETER);
		sb.append(this.nodeName);
		sb.append(Constant.DELIMETER);
		sb.append(this.nodeLocation);
		sb.append(Constant.DELIMETER);
		sb.append(this.nodeTypeId);
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
				+ ((nodeName == null) ? 0 : nodeName.hashCode());
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
		if (!(obj instanceof Node))
			return false;
		final Node other = (Node) obj;
		if (nodeName == null) {
			if (other.nodeName != null)
				return false;
		} else if (!nodeName.equals(other.nodeName))
			return false;
		return true;
	}

}
