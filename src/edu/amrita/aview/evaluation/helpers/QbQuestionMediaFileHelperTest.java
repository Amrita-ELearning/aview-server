/**
 * 
 */
package edu.amrita.aview.evaluation.helpers;

import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.evaluation.entities.QbQuestion;


/**
 * @author mathiyalakanp
 *
 */
public class QbQuestionMediaFileHelperTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	public void testgetQbQuestionMediaFileForQbQuestionId() throws AViewException 
	{
		//estionimage = QbQuestionMediaFileHelper.getQbQuestionMediaFileForQbQuestionId(342l);
		System.out.print("hi");
		
	}
	@Test
	public void testdeleteQbQuestionMediaFileByQuestionId() throws AViewException 
	{
		List<QbQuestion> qbQuestions = QbQuestionHelper.getAllActiveQbQuestionsForSubcategory(37l);
		List<Long> qbQuestionIds = new ArrayList<Long>();
		for(QbQuestion qbquestion : qbQuestions)
		{
			/*if(qbquestion.getQbQuestionMediaFiles().size() > 0)
				qbQuestionIds.add(qbquestion.getQbQuestionId());*/
		}
		/*if(qbQuestionIds.size() > 0)
			QbQuestionMediaFileHelper.deleteQbQuestionMediaFileByQuestionId(qbQuestionIds);*/
		
	}

}
