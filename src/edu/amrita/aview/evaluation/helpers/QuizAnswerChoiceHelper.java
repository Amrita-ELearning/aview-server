/*
 * @(#)QuizAnswerChoiceHelper.java 4.0 2013/10/06
 * 
 * Copyright  © 2013 E-Learning Research Lab, 
 * Amrita Vishwa Vidyapeetham. All rights reserved. 
 * E-Learning Research Lab and the A-VIEW logo are trademarks or
 * registered trademarks of E-Learning Research Lab. 
 * All other names used are the trademarks of their respective owners.
 */
package edu.amrita.aview.evaluation.helpers;

import java.util.List;

import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.evaluation.daos.QuizAnswerChoiceDAO;
import edu.amrita.aview.evaluation.entities.QuizAnswerChoice;
import edu.amrita.aview.evaluation.entities.QuizQuestion;


/**
 * This class acts as connector between client and server.
 *
 * @author Swati
 * @version 4.0
 * @since 3.0
 */
public class QuizAnswerChoiceHelper 
{
    /*private static Logger logger = Logger.getLogger(QuizAnswerChoiceHelper.class);

    // Cache code
    private static Map<Long, QuizAnswerChoice> QuizAnswerChoiceMap = Collections.synchronizedMap(new HashMap<Long, QuizAnswerChoice>());

    private static final String CACHE_CODE = "QuizAnswerChoiceHelper";

    // Newly added
    private static synchronized void populateCache(Map<Long, QuizAnswerChoice> quizAnsChoiceIdMap)throws AViewException 
    {
        QuizAnswerChoiceMap.clear();
        QuizAnswerChoiceMap.putAll(quizAnsChoiceIdMap);
        CacheHelper.setCache(CACHE_CODE);
    }
   
    private static synchronized void addItemToCache(QuizAnswerChoice quizAnsChoice) throws AViewException 
    {
        QuizAnswerChoiceMap.put(quizAnsChoice.getQuizAnswerChoiceId(),quizAnsChoice);
    }

    private static synchronized Map<Long, QuizAnswerChoice> quizAnsChoiceIdMap() throws AViewException 
    {
        Integer status_id = StatusHelper.getActiveStatusId();
       
        if (!CacheHelper.isCacheValid(CACHE_CODE)) 
        {
            System.out.println("Coming inside invalidate cache");
            List<QuizAnswerChoice> quizeAnsChoice = QuizAnswerChoiceDAO.getAllActiveQuizAnswersChoices(status_id, status_id);

            Map<Long, QuizAnswerChoice> quizAnsChoiceIdMap = new HashMap<Long, QuizAnswerChoice>();
            for (QuizAnswerChoice quizChoices : quizeAnsChoice) 
            {
            	quizAnsChoiceIdMap.put(quizChoices.getQuizAnswerChoiceId(),
                        quizChoices);
            }
            //populateNames(quizeAnsChoice, quizAnsChoiceIdMap);
            populateCache(quizAnsChoiceIdMap);
          
        }
        return QuizAnswerChoiceMap;
    }*/

   /* private static void populateNames(List<QuizAnswerChoice> quizChoice,Map<Long, QuizAnswerChoice> choiceidmap) throws AViewException 
    {
        for (QuizAnswerChoice quizChoices : quizChoice) 
        {
            Long parentId = quizChoices.getQuizAnswerChoiceId();
            Long choiceId = 0l;
            if (parentId != null && parentId != 0) 
            {
                choiceId = choiceidmap.get(parentId).getQuizAnswerChoiceId();
            }
            populateNames(quizChoices, choiceId);
        }
    }

    public static void populateNames(QuizAnswerChoice quiz, Long choiceId) 
    {
        quiz.setQuizAnswerChoiceId(choiceId);
    }*/

    // ---------------------------------------------------------------------

    /**
     * Gets the answer.
     *
     * @param Quiz_len the quiz_len
     * @param quizquestionid the quizquestionid
     * @return the answer
     * @throws AViewException
     */
    public static List<QuizAnswerChoice> getAnswer(int Quiz_len,List<QuizQuestion>quizquestionid)
    	   throws AViewException
    {         
        return QuizAnswerChoiceDAO.getAnswer(Quiz_len,quizquestionid) ;
    }


}
