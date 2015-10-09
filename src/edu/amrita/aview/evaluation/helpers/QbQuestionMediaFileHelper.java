package edu.amrita.aview.evaluation.helpers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.evaluation.daos.QbQuestionMediaFileDAO;
import edu.amrita.aview.evaluation.entities.QbQuestionMediaFile;
public class QbQuestionMediaFileHelper {
	private static Logger logger = Logger.getLogger(QbQuestionMediaFileHelper.class);

	public static void createQbQuestionMediaFile(QbQuestionMediaFile qbQuestionMediaFile,Long creatorId) throws AViewException
	{			
		qbQuestionMediaFile.setCreatedAuditData(creatorId, TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
		QbQuestionMediaFileDAO.createQbQuestionMediaFile(qbQuestionMediaFile);
		logger.debug("Exited createQbQuestionMediaFile without throwing any exception:");
	}
	public static void updateQbQuestionMediaFile(QbQuestionMediaFile qbQuestionMediaFile,Long updaterId) throws AViewException 
    {
    	 qbQuestionMediaFile.setModifiedAuditData(updaterId, TimestampUtils.getCurrentTimestamp());
         QbQuestionMediaFileDAO.updateQbQuestionMediaFile(qbQuestionMediaFile);       
         logger.debug("Exited updateQbQuestionMediaFile without throwing any exception:");
    }
	public static void deleteQbQuestionMediaFileByQuestionId(Long qbQuestionId) throws AViewException 
	{
		List<Long> qbQuestionIds = new ArrayList<Long>();
		qbQuestionIds.add(qbQuestionId);
		QbQuestionMediaFileDAO.deleteQbQuestionMediaFileByQuestionId(qbQuestionIds);
		
	}
	public static void deleteQbQuestionMediaFileByQuestionId(List<Long> qbQuestionIds) throws AViewException 
	{
		QbQuestionMediaFileDAO.deleteQbQuestionMediaFileByQuestionId(qbQuestionIds);
	}
	public static List<QbQuestionMediaFile> getQbQuestionMediaFileForQbQuestionId(long qbquestionId) throws AViewException
	{			
		return QbQuestionMediaFileDAO.getQbQuestionMediaFileForQbQuestionId(qbquestionId);
	}	
}