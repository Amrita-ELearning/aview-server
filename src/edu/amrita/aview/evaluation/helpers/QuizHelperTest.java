/*
 * @(#)QuizHelperTest.java 4.0 2013/10/17
 * 
 * Copyright  © 2013 E-Learning Research Lab, 
 * Amrita Vishwa Vidyapeetham. All rights reserved. 
 * E-Learning Research Lab and the A-VIEW logo are trademarks or
 * registered trademarks of E-Learning Research Lab. 
 * All other names used are the trademarks of their respective owners.
 */
package edu.amrita.aview.evaluation.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.evaluation.entities.Quiz;
import edu.amrita.aview.evaluation.entities.QuizQuestion;
import edu.amrita.aview.gclm.helpers.ClassHelperTest;



/**
 * This class tests all methods of QuizHelper.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QuizHelperTest {
    
    /** The logger. */
    private static Logger logger = Logger.getLogger(ClassHelperTest.class);
    
    /** The quiz name. */
    private static String quizName = null;
    
    /** The date. */
    private static Date date = null;
    
    /** The quizid. */
    private static Long  quizid= 244l;
    
    /** The end date. */
    private static java.sql.Date endDate = null;
    
    /**
     * Sets the up before class.
     *
     * @throws Exception the exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception 
    {
        quizName = "AVIEW Quiz:"+System.currentTimeMillis();
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(2011, 9, 10,0, 0, 0);
        date = new Date(cal.getTimeInMillis());        
        cal.set(2011, 11, 30,0, 0, 0);        
        endDate = new Date(cal.getTimeInMillis());        
        logger.debug("StartDate:"+date.toString());
        logger.debug("EndDate:"+endDate.toString());
        
    }

    /**
     * Tear down after class.
     *
     * @throws Exception the exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * Sets the up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Tear down.
     *
     * @throws Exception the exception
     */
    @After
    public void tearDown() throws Exception {
    }

    
    /**
     * Test populate names.
     */
    @Ignore
    public final void testPopulateNames() 
    {
        
    }

    
    /**
     * Test get all active quizzes.
     *
     * @throws AViewException
     */
    @Ignore
    public final void testGetAllActiveQuizzes() throws AViewException
    {
        List<Quiz> quizzes = QuizHelper.getAllActiveQuizzes();
        
        /*List<Quiz> activeQuizzes = new ArrayList<Quiz>() ;
        for(int i = 0 ; i < quizzes.size() ; i++)
		{
			Object t = (Object) quizzes.get(i) ;
			Object[] temp = (Object[]) t;
			
			Quiz quiz = (Quiz)temp[0] ;
			
			activeQuizzes.add(quiz) ;
		}*/
        assertEquals("Did not get all quiz",4,quizzes.size());
    
    }

    
    /**
     * Test get quiz.
     *
     * @throws AViewException
     */
    @Ignore
    public final void testGetQuiz() throws AViewException 
    {
        Quiz quiz = QuizHelper.getQuizById(quizid);
       assertEquals("Didnot get the quiz", Long.parseLong(quizid.toString()), Long.parseLong(quiz.getQuizId().toString())) ;
    }

    
  
   
    /**
     * Test get quiz by cat_ subcat.
     *
     * @throws AViewException
     */
    @Ignore
    // Not using this method right now ... 
    public final void testGetQuizByCat_Subcat() throws AViewException
    {
    	/*Long _subCatId = 1l ;
    	Long _catId = 2l ;
        List<Quiz> quiz = QuizHelper.getQuizByCat_Subcat(_subCatId,_catId);
        
        Quiz catSubcatQuiz = new Quiz() ;
        for(int i = 0 ; i < quiz.size() ; i++)
        {
        	if(_subCatId.equals(quiz.get(i).get))
        }
        assertTrue("Institute is not activated",(quiz.getStatusId() == StatusHelper.getActiveStatusId()));*/
    }

   
    /**
     * Test create quiz.
     *
     * @throws AViewException
     */
    @Ignore
    public final void testCreateQuiz() throws AViewException
    {
    	quizName += " For Insertion";
        Quiz qz=new  Quiz();
        qz.setQuizName(quizName);
        qz.setClassId(54l);
        qz.setCourseId(11l);
        qz.setQuestionPaperId(157l);
        qz.setTotalMarks(5l);
        qz.setTimeOpen(TimestampUtils.getCurrentTimestamp());
        qz.setTimeClose(TimestampUtils.getCurrentTimestamp());
        qz.setDurationSeconds(2l);
        qz.setQuizStatus("Ready");
        qz.setQuizType("Live");
        QuizHelper.createQuiz(qz,1l);
        
        List<Quiz> quizzes = QuizHelper.getAllActiveQuizzes() ;
        
        Quiz createdQuiz = new Quiz() ;
        for(int i = 0 ; i < quizzes.size() ; i++)
        {
        	if(quizName.equals(quizzes.get(i).getQuizName()))
        	{
        		createdQuiz = (Quiz)quizzes.get(i) ;
        		break ;
        	}
        }
        assertEquals("Did not create quiz",quizName,createdQuiz.getQuizName());        

    }

    
    /**
     * Test launch quiz instance for mobile.
     */
    @Ignore
    public final void testLaunchQuizInstanceForMobile() {
    
    }

    
    /**
     * Test update quiz.
     *
     * @throws AViewException
     */
    @Ignore
    public final void testUpdateQuiz() throws AViewException 
    {
        quizName += ":Changed";        
        Quiz quiz = QuizHelper.getQuizById(quizid);
        quiz.setQuizName(quizName) ;
        quiz.setTotalMarks(10l);
        quiz.setStatusId(StatusHelper.getActiveStatusId());        
        quiz.addQuizQuestion(QuizQuestionHelper.getQuizQuestionId(447l));        
        for(QuizQuestion quizQuestion:quiz.getQuizQuestion())
        {
            logger.debug("HashCode:"+quizQuestion.hashCode()+":"+quizQuestion.toString());
        }        
        QuizHelper.updateQuiz(quiz, 44l);        
        Quiz newQuiz = QuizHelper.getQuizById(quiz.getQuizId()); 
        assertTrue("Quiz not updated:",(quiz.getTotalMarks()==(newQuiz.getTotalMarks()) && quiz.getQuizId().equals(newQuiz.getQuizId())));

    }

    /**
     * Test delete quiz.
     *
     * @throws AViewException
     */
    @Ignore
    public final void testDeleteQuiz() throws AViewException 
    {
    	quizid = 251l ;
        String updated = ":ChangedDeleteInstituteAdminUser";        
        Quiz quiz = QuizHelper.getQuizById(quizid); 
       /* quiz.setQuizName(quiz.getQuizName()+updated);
        quiz.setQuizStatus(quiz.getTotalMarks()+updated);
        quiz.setStatusId(StatusHelper.getActiveStatusId());        
        Iterator iau = quiz.getQuizQuestion().iterator();
        while(iau.hasNext())
        {
            Object o = iau.next();
            iau.remove();
            break;
        }       
        for(QuizQuestion quizQuestion:quiz.getQuizQuestion())
        {
            logger.debug("HashCode:"+quizQuestion.hashCode()+":"+quizQuestion.toString());
        }*/
        assertTrue("Quiz must be active first", (quiz.getStatusId() == StatusHelper.getActiveStatusId())) ;
        QuizHelper.deleteQuiz(quizid, 44l) ;
       
        //Quiz newquiz = QuizHelper.getQuizById(quiz.getQuizId()); 
        quiz = QuizHelper.getQuizById(quizid);
        assertTrue("Quiz is not deleted:",(quiz.getStatusId() == StatusHelper.getDeletedStatusId()));

    }

   
    /**
     * Test get quiz by id.
     *
     * @throws AViewException
     */
    @Ignore
    public final void testGetQuizById() throws AViewException 
    {
        Quiz quiz = (Quiz) QuizHelper.getQuizById(quizid);
        assertEquals("Did not get all quiz",quizid,quiz.getQuizId());
    }

   
    /**
     * Test get all active quizzes for user.
     *
     * @throws AViewException
     */
    @Ignore
    public final void testGetAllActiveQuizzesForUser() throws AViewException 
    {
    	Long _userId = 554l ;
        List temp = QuizHelper.getAllActiveQuizzesForUser(_userId);
        Long rcvdUserId = 0l; 
        for(int i = 0 ; i <temp.size() ; i++)
        {
        	Object temp1 = (Object) temp.get(i) ;
        	
        	Object[] temp2 = (Object[]) temp1 ;
        	
        	if(_userId.equals(Long.parseLong(temp2[11].toString())))
        	{
        		rcvdUserId = Long.parseLong(temp2[11].toString()) ;
        		break ;
        	}
        }
        assertEquals("Did not get all quiz",_userId,rcvdUserId);
    }

    
    
    /**
     * Test get all active quizzes for student.
     *
     * @throws AViewException
     */
    @Ignore
    public final void testGetAllActiveQuizzesForStudent() throws AViewException
    {
       // Quiz quiz = (Quiz) QuizHelper.getAllActiveQuizzesForStudent(4l);
        Long _studentId = 2l ;
        Long _quizId = 241l ;
        List temp = QuizHelper.getAllActiveQuizzesForStudent(_studentId);
        Long rcvdQuizId = 0l; 
        for(int i = 0 ; i <temp.size() ; i++)
        {
        	Object temp1 = (Object) temp.get(i) ;
        	
        	Object[] temp2 = (Object[]) temp1 ;
        	
        	if(_quizId.equals(Long.parseLong(temp2[0].toString())))
        	{
        		rcvdQuizId = Long.parseLong(temp2[0].toString()) ;
        		break ;
        	}
        }
        assertEquals("Did not get all quiz",_quizId,rcvdQuizId);
    }

   
    /**
     * Test get quiz question answer.
     *
     * @throws AViewException
     */
    @Ignore
    public final void testGetQuizQuestionAnswer() throws AViewException
    {
        //Quiz quiz = (Quiz) QuizHelper.getQuizQuestionAnswer(quizid);
    	Long _quizId = 244l ;
       /* List<Quiz_QuizQuestionAnswerListVO> temp = QuizHelper.getQuizQuestionAnswer(_quizId);
        Long rcvdQuizId = 0l; 
        for(int i = 0 ; i <temp.size() ; i++)
        {
        	Object temp1 = (Object) temp.get(i) ;
        	
        	Object[] temp2 = (Object[]) temp1 ;
        	
        	if(_quizId.equals(Long.parseLong(temp2[3].toString())))
        	{
        		rcvdQuizId = Long.parseLong(temp2[3].toString()) ;
        		break ;
        	}
        }
        assertEquals("Did not get all quiz",_quizId,rcvdQuizId);*/
    }

   
    /**
     * Test get quiz student result.
     *
     * @throws AViewException
     */
    @Ignore
    public final void testGetQuizStudentResult() throws AViewException
    {
      //  Quiz quiz = (Quiz) QuizHelper.getQuizQuestionResult(quizid);
        Long _quizId = 203l ;
        Long _userId = 45l ;
        List temp = QuizHelper.getQuizResultForStudent(_quizId,_userId);
        Long rcvdQuizId = 0l; 
        for(int i = 0 ; i <temp.size() ; i++)
        {
        	Object temp1 = (Object) temp.get(i) ;
        	
        	Object[] temp2 = (Object[]) temp1 ;
        	
        	if(_quizId.equals(Long.parseLong(temp2[5].toString())))
        	{
        		rcvdQuizId = Long.parseLong(temp2[5].toString()) ;
        		break ;
        	}
        }
        assertEquals("Did not get all quiz",_quizId,rcvdQuizId);
    }

   
    /**
     * Test get quiz question result.
     *
     * @throws AViewException
     */
    @Ignore
    public final void testGetQuizQuestionResult() throws AViewException 
    {
       // Quiz quiz = (Quiz) QuizHelper.getQuizQuestionResult(quizid);
     //   assertEquals("Did not get all quiz",1150,quiz);
        Long _quizId = 21l ;
        List temp = QuizHelper.getQuizResultByQuestion(_quizId);
        Long rcvdQuizId = 0l; 
        for(int i = 0 ; i <temp.size() ; i++)
        {
        	Object temp1 = (Object) temp.get(i) ;
        	
        	Object[] temp2 = (Object[]) temp1 ;
        	
        	if(_quizId.equals(Long.parseLong(temp2[4].toString())))
        	{
        		rcvdQuizId = Long.parseLong(temp2[4].toString()) ;
        		break ;
        	}
        }
        assertEquals("Did not get all quiz",_quizId,rcvdQuizId);
    }
    
    /**
     * Testget question paper result for chart.
     *
     * @throws AViewException
     */
    @Ignore
    public final void testgetQuestionPaperResultForChart()throws AViewException
    {
        // Quiz quiz = (Quiz) QuizHelper.getQuizQuestionResult(quizid);
      //   assertEquals("Did not get all quiz",1150,quiz);
         Long _quizId = 232l ;
         List temp;
		try {
			temp = QuizHelper.getQuestionPaperResultForChart(_quizId);
		} catch (AViewException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         Long rcvdQuizId = 0l; 
       
     }
     
    /**
     * Creates the quiz question.
     *
     * @return the quiz question
     * @throws AViewException
     */
    private QuizQuestion createQuizQuestion() throws AViewException
    {
        QuizQuestion qq=new QuizQuestion();
        qq.setCategoryId(1l);
        qq.setCategoryName("Programming");
        qq.setCreatedAuditData(1l,TimestampUtils.getCurrentTimestamp(), StatusHelper.getActiveStatusId());
        qq.setSubcategoryId(11);
        qq.setSubcategoryName("cpp");
        qq.setQuestionTypeName("MultipleResponse");
        qq.setDifficultyLevelName("easy");
        return qq;
        
    }

}
