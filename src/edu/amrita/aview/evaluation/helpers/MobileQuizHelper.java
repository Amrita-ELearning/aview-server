/*
 * 
 */
package edu.amrita.aview.evaluation.helpers;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;

import com.javacodegeeks.kannel.api.SMSPushRequestException;

import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.utils.SendSMSUtils;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.evaluation.entities.QuizAnswerChoice;
import edu.amrita.aview.evaluation.entities.QuizQuestion;
import edu.amrita.aview.evaluation.entities.QuizQuestionChoiceResponse;
import edu.amrita.aview.evaluation.entities.QuizQuestionResponse;
import edu.amrita.aview.evaluation.entities.QuizResponse;
import edu.amrita.aview.gclm.entities.User;
import edu.amrita.aview.gclm.helpers.UserHelper;


/**
 * The Class MobileQuizHelper.
 */
public class MobileQuizHelper 
{
	
	/** The Constant ASCII_FOR_SMALL_A. */
	public static final int ASCII_FOR_SMALL_A = 97;
	
	/**
	 * Creates the mobile quiz question.
	 *
	 * @param quizId the quiz id
	 * @param MobileNo the mobile no
	 * @throws SMSPushRequestException the sMS push request exception
	 * @throws Exception the exception
	 */
	public static void createMobileQuizQuestion(Long quizId, List<String> MobileNo) throws SMSPushRequestException, Exception
	{ 	
		List<QuizQuestion> quizQuestionsList = new ArrayList<QuizQuestion>();
		List<QuizAnswerChoice> quizQuestionChoiceList = new ArrayList<QuizAnswerChoice>();
		ArrayList<String> quizQuestions = new ArrayList<String>();
		ArrayList<String>quizQuestionChoice=new ArrayList<String>();
		String[] quizQuestion = null ;
		int quizQuestionNo=1,i=1,totalQuestions;
		String quizMessage ="";	
		String appstr="Your Quiz id:"+ quizId.toString() + "\n" + "Please send the answer(s) in the following format: <Quizid># " +
				"<ans_choice1><ans_choice2>\nFor unknown answers leave a hyphen.";		
		try 
		{	
			//geting the questions for quiz id	  
			quizQuestionsList= QuizQuestionHelper.getQuizQuestionsForQuiz(quizId);
			for(int j=0;j<quizQuestionsList.size();j++)
			{   
				QuizQuestion quizQuestionObj = (QuizQuestion) quizQuestionsList. get(j) ;
				quizQuestions. add("Q"+quizQuestionNo + ")" + quizQuestionObj.getQuestionText());
				quizQuestionNo++;
			}
			totalQuestions=quizQuestions. size();
			quizQuestion = new String[quizQuestions.size()] ;
			for(i=0; i<quizQuestions.size(); i++)
			{
				quizQuestion[i] = quizQuestions.get(i) ; 
			}
			//geting the option for all questions
			quizQuestionChoiceList=QuizAnswerChoiceHelper.getAnswer(totalQuestions,quizQuestionsList);
			for(i=0; i<totalQuestions; i++)
			{
				Long qqQuest = quizQuestionsList.get(i).getQuizQuestionId() ;
				int quizChoiceOption = ASCII_FOR_SMALL_A;//ascii value of a 	        
				for(int j=0;j<quizQuestionChoiceList. size();j++)
				{   
					Long qqAns = quizQuestionChoiceList. get(j).getQuizQuestion().getQuizQuestionId() ;
					if( qqQuest.equals(qqAns))
					{
						QuizAnswerChoice quizAnswerChoiceObj = (QuizAnswerChoice) quizQuestionChoiceList.get(j) ;
						quizQuestionChoice.add(quizAnswerChoiceObj. getChoiceText());
						String choice=Character.toString((char)quizChoiceOption);
						quizQuestion[i] = quizQuestion[i]+ "\n" + "(" + choice + ")" + 
								quizAnswerChoiceObj.getChoiceText();
						quizChoiceOption++;
					}   	   	     
				}
				quizQuestion[i] = quizQuestion[i] + "\n-----";
			}
			for(i=0;i<totalQuestions;i++)
			{
				quizMessage += quizQuestion[i] + "\n";
			}
			quizMessage = quizMessage + appstr;
			//the send the following no's
			//System.out.println(quizMessage);
			SendSMSUtils.sendSMS(MobileNo, quizMessage);
		}
		catch (Exception e) 
		{
			e. printStackTrace();		
		}
	}
	
	/**
	 * Gets the mobile quiz answer choice.
	 *
	 * @param responseMessage the response message
	 * @param mobileNumber the mobile number
	 * @return the mobile quiz answer choice
	 * @throws SMSPushRequestException the sMS push request exception
	 * @throws Exception the exception
	 */
	public static void getMobileQuizAnswerChoice(String responseMessage,String mobileNumber) throws SMSPushRequestException, Exception
	{
		ArrayList<String> mobileNumLst = new ArrayList<String>();
		try
		{
			int j=0; 
			String senderMobileNo = mobileNumber.substring(3);
			String[] tempList = responseMessage.split(Constant.MOBILE_SMS_SPLIT_DELIMETER);
			String originalResponseMessage = tempList[0].substring(responseMessage.indexOf(Constant.MOBILE_SMS_INDEX_SPLIT_DELIMETER) + 1);
			int answerChoiceLength=originalResponseMessage.length();

			String senderQuizAnswerChoice[] = new String[answerChoiceLength];
			char[] chrs = originalResponseMessage.toCharArray();
			for(char c: chrs)
			{	   
				String ans_char = Character. toString(Character.toLowerCase(c));
				if(ans_char.equals("-"))
				{
					ans_char="*";
				}
				senderQuizAnswerChoice[j++] = ans_char;	
			}
			String quizid=responseMessage.substring(0, responseMessage.indexOf(Constant.MOBILE_SMS_INDEX_SPLIT_DELIMETER));
			Long quizId=Long.parseLong(quizid);
			mobileNumLst.add(senderMobileNo);
			if(studentResponseMessageValidation(quizId, senderMobileNo))
			{
				if(senderQuizAnswerChoice.length>=1)
				{
					createMobileQuizResponse(quizId,senderQuizAnswerChoice,senderMobileNo);
				}
				else
				{
					SendSMSUtils.sendSMS(mobileNumLst,Constant.NOT_RESPONSE_ERROR_MESSAGE);
				}
			}
			else
			{
				SendSMSUtils.sendSMS(mobileNumLst,Constant.ALREAY_RESPONSE_ERROR_MESSAGE);
			}
		}
		catch (Exception e) 
		{
			SendSMSUtils.sendSMS(mobileNumLst,Constant.RESPONSE_FORMAT_ERROR_MESSAGE);
		}
	}
	
	/**
	 * Student response message validation.
	 *
	 * @param quizId the quiz id
	 * @param mobileNumber the mobile number
	 * @return true, if successful
	 */
	public static boolean studentResponseMessageValidation(Long quizId,String mobileNumber)
	{
		boolean flag=false;
		try
		{
			User user = UserHelper.getUserByMobileNumber(mobileNumber);
			Long studentId=user.getUserId();
			ArrayList<String> mobileNumLst = new ArrayList<String>();
			mobileNumLst.add(mobileNumber);
			QuizResponse quizResponse=QuizResponseHelper.getQuizResponseForStudent(quizId, studentId);
			if(quizResponse==null)
			{
				flag=true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * Creates the mobile quiz response.
	 *
	 * @param quizId the quiz id
	 * @param quizResponseAnswerChoice the quiz response answer choice
	 * @param mobileNumber the mobile number
	 * @throws SMSPushRequestException the sMS push request exception
	 * @throws Exception the exception
	 */
	public static void createMobileQuizResponse(Long quizId,String[]quizResponseAnswerChoice,String mobileNumber) throws SMSPushRequestException, Exception
	{
		List<QuizQuestionResponse>quizQuestionResponselist=new ArrayList<QuizQuestionResponse>();
		List<QuizQuestionChoiceResponse>quizQuestionChoiceResponselist= new ArrayList<QuizQuestionChoiceResponse>();
		List<QuizAnswerChoice> quizAnswerChoices = new ArrayList<QuizAnswerChoice>() ;
		Long quizQuestionId[] = new Long[quizResponseAnswerChoice.length];
		List<String>quizResponseAnswers=new ArrayList<String>();
		ArrayList<String> mobileNumLst = new ArrayList<String>();
		mobileNumLst.add(mobileNumber);
		Long studentId=0l;
		int totalQuestions = -1;
		Double studentQuizScore=0.0;
		String QuizMessage = "";
		QuizResponse quizResponseObj=new QuizResponse(); 
		try
		{
			for(int k=0; k<quizResponseAnswerChoice.length; k++)
			{		
				String ch=quizResponseAnswerChoice[k];				
				quizResponseAnswers.add(quizResponseAnswerChoice[k]);
			}
			//get the all active users details and check the mobile no who is sent the result
			User user = UserHelper.getUserByMobileNumber(mobileNumber);
			studentId=user.getUserId();
			if(studentId!=0 || studentId!=null)
			{
				//based on the response receive the question id and mark ,add the list
				List<QuizQuestion> quizQuestions = QuizQuestionHelper.getQuizQuestionsForQuiz(quizId);
				totalQuestions = quizQuestions.size() ;
				for(int i=0; i <quizQuestions.size() ; i++)
				{
					QuizQuestion temp1 = quizQuestions. get(i) ;						
					quizQuestionId[i] = temp1. getQuizQuestionId() ;
					QuizQuestionResponse obj = new QuizQuestionResponse();
					obj.setQuizQuestionId(temp1.getQuizQuestionId());
					obj.setScore(temp1.getMarks());
					quizQuestionResponselist.add(obj);
				}
				//retriving the options and option id for quiz questions add the list ,here count useful for more than one question in the quiz.
				quizAnswerChoices = QuizAnswerChoiceHelper.getAnswer(totalQuestions, quizQuestions);
				int j = 0;
				for(int i = 0; i < quizQuestions.size(); i++)
				{
					int k = 0;
					int optionchoiceid = quizResponseAnswerChoice[i].charAt(0);
					optionchoiceid = optionchoiceid - ASCII_FOR_SMALL_A;				       
					for(int count = 0; k < quizAnswerChoices.size(); k++, count++)
					{	
						if(quizQuestions.get(i).getQuizQuestionId().equals(quizAnswerChoices.get(k).getQuizQuestion().getQuizQuestionId()))
						{
							if(optionchoiceid== count)
							{
								QuizQuestionChoiceResponse quizQuestionChoiceObj = new QuizQuestionChoiceResponse() ; 
								quizQuestionChoiceObj.setQuizAnswerChoiceId(quizAnswerChoices.get(k).getQuizAnswerChoiceId());
								quizQuestionChoiceObj.setQuizQuestionId(quizAnswerChoices.get(k).getQuizQuestion().getQuizQuestionId());
								quizQuestionChoiceResponselist.add(quizQuestionChoiceObj);
								break;
							}
						}
						else
						{
							count = -1;
						}
					}
				} 
				//checking the answers for quiz
				for(int i=0;i<quizQuestions.size();i++)
				{
					for(j=0;j<quizAnswerChoices.size();j++)
					{
						if((quizAnswerChoices.get(j).getFraction()==1.00)&&(quizAnswerChoices.get(j).getQuizQuestion().getQuizQuestionId().equals(quizQuestions.get(i).getQuizQuestionId())))
						{
							//QuizMessage=QuizMessage+quizAnswerChoices.get(j).getChoiceText()+",";
							for(int k=0;k< quizQuestionChoiceResponselist.size();k++)
							{
								if((quizQuestionChoiceResponselist.get(k).getQuizAnswerChoiceId()==quizAnswerChoices.get(j).getQuizAnswerChoiceId())
										&&((quizAnswerChoices.get(j).getQuizQuestion().getQuizQuestionId().equals(quizQuestionChoiceResponselist.get(k).getQuizQuestionId()))))
								{
									studentQuizScore=studentQuizScore+quizQuestions.get(i).getMarks();
								}
							}
							
							
						}
					}
				}
				quizResponseObj.setUserId(studentId);
				quizResponseObj.setQuizId(quizId);
				quizResponseObj.setTotalScore(Float.valueOf(studentQuizScore.toString()));
				quizResponseObj.setTimeStart(TimestampUtils.getCurrentTimestamp());
				quizResponseObj.setTimeEnd(TimestampUtils.getCurrentTimestamp());			
				quizResponseObj.setQuizResponseType("Mobile");
				//Updating the quizResponse Table
				QuizResponseHelper.createQuizResponse(quizResponseObj, quizQuestionResponselist, quizQuestionChoiceResponselist, studentId);
				String quizresultsms="Your Score: " + studentQuizScore;
						//+ ", Correct Answers: " + QuizMessage;
				//sending quizResult to student
				SendSMSUtils.sendSMS(mobileNumLst, quizresultsms);
			}
			else
			{
				SendSMSUtils.sendSMS(mobileNumLst, Constant.STUDENT_DETAILS_NOT_AVAILABLE_ERROR_MESSAGE);
			}
		}
		catch(HibernateException he)
		{
			he.printStackTrace();
			//SendSMSUtils.sendSMS(mobileNumLst, Constant.STUDENT_DETAILS_NOT_AVAILABLE_ERROR_MESSAGE);
		}
		finally
		{
			//System.out.println("successful");
		}  
	}



}
