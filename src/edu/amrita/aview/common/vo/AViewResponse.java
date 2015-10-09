/*
 * 
 */
package edu.amrita.aview.common.vo;


/**
 * The Class AViewResponse.
 */
public class AViewResponse {
	
	/** The request success. */
	public static String REQUEST_SUCCESS = "REQUEST_SUCCESS";
	
	/** The error client. */
	public static String ERROR_CLIENT = "ERROR_CLIENT";
	
	/** The error server. */
	public static String ERROR_SERVER = "ERROR_SERVER";
	
	
	/** The response id. */
	private String responseId = null;
	
	/** The response message. */
	private String responseMessage = null;
	
	/** The result. */
	private Object result = null;
	
	/**
	 * Gets the response id.
	 *
	 * @return the response id
	 */
	public String getResponseId() {
		return responseId;
	}
	
	/**
	 * Sets the response id.
	 *
	 * @param responseCode the response id
	 */
	public void setResponseId(String responseCode) {
		this.responseId = responseCode;
	}
	
	/**
	 * Gets the response message.
	 *
	 * @return the response message
	 */
	public String getResponseMessage() {
		return responseMessage;
	}
	
	/**
	 * Sets the response message.
	 *
	 * @param responseMessage the response message
	 */
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	/**
	 * Gets the result.
	 *
	 * @return the result
	 */
	public Object getResult() {
		return result;
	}
	
	/**
	 * Sets the result.
	 *
	 * @param result the result
	 */
	public void setResult(Object result) {
		this.result = result;
	}

}
