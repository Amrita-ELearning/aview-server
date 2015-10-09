/*
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
 * The Class NodeType.
 */
@Entity
@Table(name = "node_type")
public class NodeType extends Auditable {
	
	/** The node type id. */
	private Integer nodeTypeId = 0 ;
	
	/** The node type name. */
	private String nodeTypeName = null ;

	/**
	 * Gets the node type id.
	 *
	 * @return the node_type_id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)	
	@Column(name = "node_type_id")
	public Integer getNodeTypeId() {
		return nodeTypeId;
	}

	/**
	 * Sets the node type id.
	 *
	 * @param nodeTypeId the node_type_id to set
	 */
	public void setNodeTypeId(Integer nodeTypeId) {
		this.nodeTypeId = nodeTypeId;
	}

	/**
	 * Gets the node type name.
	 *
	 * @return the node_type_name
	 */
	@Column(name = "node_type_name")
	public String getNodeTypeName() {
		return nodeTypeName;
	}

	/**
	 * Sets the node type name.
	 *
	 * @param nodeTypeName the node_type_name to set
	 */
	public void setNodeTypeName(String nodeTypeName) {
		this.nodeTypeName = nodeTypeName;
	}

	/* (non-Javadoc)
	 * @see com.amrita.edu.entities.Auditable#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder(256);
		sb.append(this.nodeTypeId);
		sb.append(Constant.DELIMETER);
		sb.append(this.nodeTypeName);
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
				+ ((nodeTypeName == null) ? 0 : nodeTypeName.hashCode());
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
		if (!(obj instanceof NodeType))
			return false;
		final NodeType other = (NodeType) obj;
		if (nodeTypeName == null) {
			if (other.nodeTypeName != null)
				return false;
		} else if (!nodeTypeName.equals(other.nodeTypeName))
			return false;
		return true;
	}
}
