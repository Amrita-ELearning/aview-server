/*
 * 
 */
package edu.amrita.aview.documentsharing.helpers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.documentsharing.daos.DocumentConversionDAO;
import edu.amrita.aview.documentsharing.entities.DocumentConversion;
import edu.amrita.aview.gclm.entities.Server;
import edu.amrita.aview.gclm.helpers.ServerHelper;


/**
 * The Class DocumentConversionHelper.
 */
@Controller
public class DocumentConversionHelper {
	
	/** The logger. */
	private static Logger logger = Logger.getLogger(DocumentConversionHelper.class);

	/** The document conversion qs. */
	private static Map<String,Queue<DocumentConversion>> documentConversionQs = new HashMap<String,Queue<DocumentConversion>>();
	
	/** The populated. */
	private static boolean populated = false;
	
	/**
	 * Populate queues.
	 * @throws AViewException 
	 */
	private static synchronized void populateQueues() throws AViewException
	{
		if(populated)
		{
			return;
		}
		List<DocumentConversion> conversions = DocumentConversionDAO.getAllPendingConversions(StatusHelper.getActiveStatusId());
		for(DocumentConversion conversion:conversions)
		{
			getQ(conversion.getContentServerId()).add(conversion);
		}
		populated = true;
	}
	
	/**
	 * Gets the q.
	 *
	 * @param contentServerId the content server id
	 * @return the q
	 */
	private static synchronized Queue<DocumentConversion> getQ(Integer contentServerId)
	{
		String key = contentServerId.toString() ;
		Queue<DocumentConversion> list = documentConversionQs.get(key);
		if(list == null)
		{
			list = new LinkedList<DocumentConversion>();
			documentConversionQs.put(key, list);
		}
		return list;
	}
	
	/**
	 * Adds the conversion to queue.
	 *
	 * @param conversion the conversion
	 * @return the long
	 * @throws AViewException 
	 */
	@RequestMapping(value = "/addConversionToQueue.html", method = RequestMethod.POST, produces = "application/json", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public static synchronized Long addConversionToQueue(@RequestBody DocumentConversion conversion) throws AViewException
	{
		logger.debug("Entered addConversionToQueue :"+conversion.toString());
		populateQueues();
		
		conversion.setModifiedByUserId(conversion.getCreatedByUserId());
		conversion.setCreatedDate(TimestampUtils.getCurrentTimestamp());
		conversion.setModifiedDate(TimestampUtils.getCurrentTimestamp());
		conversion.setStatusId(StatusHelper.getActiveStatusId());
		conversion.setConversionStatus(Constant.PENDING_CONVERSION_STATUS);
	
		DocumentConversionDAO.createConversionRequest(conversion);
		getQ(conversion.getContentServerId()).add(conversion);
		logger.debug("Successfully added Conversion To Queue :"+conversion.toString());
		return conversion.getDocumentConversionId();
	}
	
	/**
	 * Gets the next document for conversion.
	 *
	 * @param contentServerDomain the content server domain
	 * @return the next document for conversion
	 * @throws AViewException 
	 */
	@RequestMapping(value = "/getNextDocumentForConversion.html", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public static synchronized DocumentConversion getNextDocumentForConversion(@RequestParam("contentServerDomain") String contentServerDomain ) throws AViewException
	{
		logger.debug("Entered getNextDocumentForConversion contentServerDomain:"+contentServerDomain);
		populateQueues();
		Server server = ServerHelper.getServerByDomain(contentServerDomain);
		if(server == null)
		{
			logger.warn("Content server is not found for the domain/ip of :"+contentServerDomain+":. Not able to get the document from Queue");
			return null;
		}
		
		DocumentConversion conversion = getQ(server.getServerId()).poll();
		if(conversion == null)
		{
			logger.info("No more document for conversion in :"+contentServerDomain);
		}
		else
		{
			conversion.setConversionStatus(Constant.CONVERTING_CONVERSION_STATUS);
			DocumentConversionDAO.updateConversionRequest(conversion);
			logger.info("Returning document from queue of "+contentServerDomain+"'s  document:"+conversion.toString());
		}
		return conversion;
	}
	
	/**
	 * Gets the document conversion.
	 *
	 * @param conversionId the conversion id
	 * @return the document conversion
	 * @throws AViewException 
	 */
	@RequestMapping(value = "/getDocumentConversion.html", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public static DocumentConversion getDocumentConversion(@RequestParam("conversionId")Long conversionId) throws AViewException
	{
		logger.debug("Entered getDocumentConversion conversionId:"+conversionId);
		return DocumentConversionDAO.getConversionRequest(conversionId);
	}
	
	/**
	 * Sets the successful conversion.
	 *
	 * @param conversionId the successful conversion
	 * @return true, if sets the successful conversion
	 * @throws AViewException 
	 */
	@RequestMapping(value = "/setSuccessfulConversion.html", method = RequestMethod.POST)
	@ResponseBody
	public static Boolean setSuccessfulConversion(@RequestParam("conversionId")Long conversionId) throws AViewException
	{
		logger.debug("Entered setSuccessfulConversion conversionId:"+conversionId);
		DocumentConversion conversion = DocumentConversionDAO.getConversionRequest(conversionId);
		if(conversion == null)
		{
			logger.warn("Could not find conversion with id :"+conversionId);
			return false;
		}
		else if(conversion.getConversionStatus().equals(Constant.PENDING_CONVERSION_STATUS))
		{
			logger.warn("Could not set conversion with id :"+conversionId+" to successful status, as conversion status is not Converting. Current conversion status is :"+conversion.getConversionStatus());
			return false;
		}
		conversion.setConversionStatus(Constant.CONVERTED_CONVERSION_STATUS);
		DocumentConversionDAO.updateConversionRequest(conversion);
		logger.info("Successfully updated the conversion status for document:"+conversion.toString());
		return true;
	}

	/**
	 * Sets the failed conversion.
	 *
	 * @param conversionId the conversion id
	 * @param failureMsg the failure msg
	 * @return true, if sets the failed conversion
	 * @throws AViewException 
	 */
	@RequestMapping(value = "/setFailedConversion.html", method = RequestMethod.POST)
	@ResponseBody
	public static Boolean setFailedConversion(@RequestParam("conversionId")Long conversionId, @RequestParam("failureMsg")String failureMsg) throws AViewException
	{
		logger.debug("Entered setFailedConversion conversionId:"+conversionId+", failureMsg:"+failureMsg);
		DocumentConversion conversion = DocumentConversionDAO.getConversionRequest(conversionId);
		if(conversion == null)
		{
			logger.warn("Could not find conversion with id :"+conversionId);
			return false;
		}
		else if(!conversion.getConversionStatus().equals(Constant.PENDING_CONVERSION_STATUS))
		{
			logger.warn("Could not set conversion with id :"+conversionId+" to failed status, as conversion status is not pending. Current conversion status is :"+conversion.getConversionStatus());
			return false;
		}
		conversion.setConversionStatus(Constant.FAILED_CONVERSION_STATUS);
		conversion.setConversionMessage(failureMsg);
		DocumentConversionDAO.updateConversionRequest(conversion);
		logger.info("Successfully updated the conversion status for document:"+conversion.toString());
		return true;
	}
	
	/**
	 * Cancel pending conversion.
	 *
	 * @param conversionId the conversion id
	 * @return true, if cancel pending conversion
	 * @throws AViewException 
	 */
	@RequestMapping(value = "/cancelPendingConversion.html", method = RequestMethod.POST)
	@ResponseBody
	public static Boolean cancelPendingConversion(@RequestParam("conversionId")Long conversionId) throws AViewException
	{
		logger.debug("Entered cancelPendingConversion conversionId:"+conversionId);
		DocumentConversion conversion = DocumentConversionDAO.getConversionRequest(conversionId);
		if(conversion == null)
		{
			logger.warn("Could not find conversion with id :"+conversionId);
			return false;
		}
		else if(!conversion.getConversionStatus().equals(Constant.PENDING_CONVERSION_STATUS))
		{
			logger.warn("Could not cancel conversion with id :"+conversionId+" as conversion status is not pending. Current conversion status is :"+conversion.getConversionStatus());
			return false;
		}
		if(!removeFromQueue(conversion))
		{
			logger.warn("Could not calcel conversion with id :"+conversionId+" as conversion is not in Queue. It's might have already been given out for conversion. Current conversion status is :"+conversion.getConversionStatus());
			return false;
		}
		conversion.setStatusId(StatusHelper.getInActiveStatusId());
		DocumentConversionDAO.updateConversionRequest(conversion);
		logger.info("Successfully cancelled the document conversin request:"+conversion.toString());
		return true;
	}
	
	/**
	 * Retry failed conversion.
	 *
	 * @param conversionId the conversion id
	 * @return the document conversion
	 * @throws AViewException 
	 */
	@RequestMapping(value = "/retryFailedConversion.html", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public static DocumentConversion retryFailedConversion(@RequestParam("conversionId")Long conversionId) throws AViewException
	{
		logger.debug("Entered retryFailedConversion conversionId:"+conversionId);
		DocumentConversion conversion = DocumentConversionDAO.getConversionRequest(conversionId);
		if(conversion == null)
		{
			logger.warn("Could not find conversion with id :"+conversionId);
			return null;
		}
		else if(!conversion.getConversionStatus().equals(Constant.FAILED_CONVERSION_STATUS))
		{
			logger.warn("Could not retry conversion with id :"+conversionId+" as conversion status is not failed. Current conversion status is :"+conversion.getConversionStatus());
			return null;
		}
		DocumentConversion retryConversion = new DocumentConversion();
		retryConversion.createFrom(conversion);
		addConversionToQueue(retryConversion);
		logger.info("Successfully retried the document conversin request:"+retryConversion.toString());
		return retryConversion;
	}

	/**
	 * Removes the from queue.
	 *
	 * @param conversion the conversion
	 * @return true, if removes the from queue
	 */
	private static Boolean removeFromQueue(DocumentConversion conversion)
	{
		return getQ(conversion.getContentServerId()).remove(conversion);
	}
	
	/**
	 * Gets the all non processed documents.
	 *
	 * @param userId the user id
	 * @return the all non processed documents
	 * @throws AViewException 
	 */
	@RequestMapping(value = "/getAllNonProcessedDocuments.html", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public static List<DocumentConversion> getAllNonProcessedDocuments(@RequestParam("userId")Long userId) throws AViewException
	{
		return DocumentConversionDAO.getAllNonProcessedDocuments(userId);
	}
	
	/**
	 * Gets the progress.
	 *
	 * @param conversionId the conversion id
	 * @return the progress
	 * @throws AViewException 
	 */
	@RequestMapping(value = "/getProgress.html", method = RequestMethod.POST)
	@ResponseBody
	public static int getProgress(@RequestParam("conversionId")Long conversionId) throws AViewException
	{
		logger.debug("Entered getProgress conversionId:"+conversionId);
		DocumentConversion conversion = DocumentConversionDAO.getConversionRequest(conversionId);
		if(conversion == null)
		{
			logger.warn("Could not find conversion with id :"+conversionId);
			return -1;
		}
		return conversion.getProgressPct();
	}
	
	
	/**
	 * Sets the progress.
	 *
	 * @param conversionId the conversion id
	 * @param progressPct the progress pct
	 * @return true, if sets the progress
	 * @throws AViewException 
	 */
	@RequestMapping(value = "/setProgress.html", method = RequestMethod.POST)
	@ResponseBody
	public static Boolean setProgress(@RequestParam("conversionId")Long conversionId,@RequestParam("progressPct")Integer progressPct) throws AViewException
	{
		logger.debug("Entered setProgress conversionId:"+conversionId+", progressPct:"+progressPct);
		DocumentConversion conversion = DocumentConversionDAO.getConversionRequest(conversionId);
		if(conversion == null)
		{
			logger.warn("Could not find conversion with id :"+conversionId);
			return false;
		}
		else if(!conversion.getConversionStatus().equals(Constant.CONVERTING_CONVERSION_STATUS))
		{
			logger.warn("Could not set progress for conversion with id :"+conversionId+" as conversion status is not converting. Current conversion status is :"+conversion.getConversionStatus());
			return false;
		}
		conversion.setProgressPct(progressPct);
		DocumentConversionDAO.updateConversionRequest(conversion);
		logger.info("Successfully set progress for the document conversin request:"+conversion.toString());
		return true;
	}
}
