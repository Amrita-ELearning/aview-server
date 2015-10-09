/*
 * QuestionPaperQuestionListVO.java 4.0 2013/10/17
 */
package edu.amrita.aview.evaluation.vo;

import edu.amrita.aview.evaluation.entities.QbQuestion;
import edu.amrita.aview.evaluation.entities.QuestionPaperQuestion;




/**
 * This class is used to populate the transient attributes of question paper question.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QuestionPaperQuestionListVO 
{
	
	/** The qb question. */
	public QbQuestion qbQuestion = null ;
	
	/** The question paper question. */
	public QuestionPaperQuestion questionPaperQuestion = null;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((qbQuestion.getQbQuestionId() == null) ? 0 : qbQuestion.getQbQuestionId().hashCode());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		QuestionPaperQuestionListVO other = (QuestionPaperQuestionListVO) obj;
		if (qbQuestion.getQbQuestionId() == null) {
			if (other.qbQuestion.getQbQuestionId() != null) {
				return false;
			}
		} else if (!qbQuestion.getQbQuestionId().equals(other.qbQuestion.getQbQuestionId())) {
			return false;
		}
		return true;
	}
	
	
}
