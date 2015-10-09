/*
 * StudentQuizResultVO.java 4.0 2013/10/17
 */
package edu.amrita.aview.evaluation.vo;

import java.util.List;

import edu.amrita.aview.evaluation.entities.Quiz;
import edu.amrita.aview.evaluation.entities.QuizQuestion;
import edu.amrita.aview.evaluation.entities.QuizResponse;


/**
 * This class is used to populate the transient attributes of student quiz result.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class StudentQuizResultVO 
{
	
	/** The quiz. */
	public Quiz quiz;
	
	/** The quiz response. */
	public QuizResponse quizResponse;
	
	/** The quiz questions. */
	public List<QuizQuestion> quizQuestions;
}
