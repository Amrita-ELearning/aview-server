/*
 * 
 */
package edu.amrita.aview.biometric;


/**
 * The Class Templates.
 */
public class Templates {
	
	/** The template id. */
	private int templateId;
	
	/** The user id. */
	private int userId;
	
	/** The face tempalte. */
	private byte[] faceTempalte;
	
	/** The face icon. */
	private byte[] faceIcon;
	
	/**
	 * Gets the template id.
	 *
	 * @return the template id
	 */
	public int getTemplateId() {
		return templateId;
	}
	
	/**
	 * Sets the template id.
	 *
	 * @param templateId the new template id
	 */
	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}
	
	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public int getUserId() {
		return userId;
	}
	
	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	/**
	 * Gets the face tempalte.
	 *
	 * @return the face tempalte
	 */
	public byte[] getFaceTempalte() {
		return faceTempalte;
	}
	
	/**
	 * Sets the face tempalte.
	 *
	 * @param faceTempalte the new face tempalte
	 */
	public void setFaceTempalte(byte[] faceTempalte) {
		this.faceTempalte = faceTempalte;
	}
	
	/**
	 * Gets the face icon.
	 *
	 * @return the face icon
	 */
	public byte[] getFaceIcon() {
		return faceIcon;
	}
	
	/**
	 * Sets the face icon.
	 *
	 * @param faceIcon the new face icon
	 */
	public void setFaceIcon(byte[] faceIcon) {
		this.faceIcon = faceIcon;
	}
	
}
