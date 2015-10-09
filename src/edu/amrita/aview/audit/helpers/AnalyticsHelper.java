package edu.amrita.aview.audit.helpers;


import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.sun.jersey.api.client.ClientResponse.Status;

import edu.amrita.aview.audit.daos.AnalyticsDAO;
import edu.amrita.aview.audit.entities.AuditLecture;
import edu.amrita.aview.audit.entities.AuditUserLogin;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.Constant;
import edu.amrita.aview.common.helpers.StatusHelper;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.gclm.daos.PeopleCountDAO;
import edu.amrita.aview.gclm.entities.Class;
import edu.amrita.aview.gclm.entities.ClassRegistration;
import edu.amrita.aview.gclm.entities.Lecture;
import edu.amrita.aview.gclm.entities.PeopleCount;
import edu.amrita.aview.gclm.entities.User;
import edu.amrita.aview.gclm.helpers.ClassHelper;
import edu.amrita.aview.gclm.helpers.ClassRegistrationHelper;
import edu.amrita.aview.gclm.helpers.LectureHelper;
import edu.amrita.aview.gclm.helpers.UserHelper;


@Controller
public class AnalyticsHelper {
	private static Logger logger = Logger.getLogger(AnalyticsHelper.class);

	private static final String QUESTION_ASKED = "QuestionAsk";
	public static final String QUESTION_ANSWER = "QuestionAnswer";
	private static final String QUESTION_VOTE = "QuestionVote";
	
	private static final String CONNECTION_SUCCESS = "ConnectionSuccess";
	private static final String CONNECTION_FAIL = "ConnectionFail";
	private static final String CONNECTION_REJECT = "ConnectionReject";
	
	private static final String INTERACTING = "Interacting";
	private static final String INTERACTION_END = "InteractionEnded";
	
	private static final String AUDIO_PUBLISH ="AudioOnly";
	private static final String AUDIOVIDEO_PUBLISH = "AudioVideo"; 
	
	private static final String NO_PUBLISH_VALUE = "0";
	private static final String AUDIO_PUBLISH_VALUE = "1";
	private static final String AUDIOVIDEO_PUBLISH_VALUE = "2";
	
	private static final String ACTUAL_CHAT_TO_BE_CLEAR = "999 *#& Clear Aum Chat Namah Area Shivaya !%* 999";
	private static final String CHAT_MESSAGE_TO_BE_MESSAGE = "<< Moderator/Presenter cleared all chats >>";
	
	/**
	 * Function to check whether the lecture is on going.
	 * @param lecture
	 * @return response
	 * @throws AViewException
	 */
	private static Object validateLecture(Lecture lecture)throws AViewException
	{
		Date startTime = TimestampUtils.removeDateAndMillis(lecture.getStartTime());
		Date endTime = TimestampUtils.removeDateAndMillis(lecture.getEndTime());
		Date currentTime = TimestampUtils.removeDateAndMillis(TimestampUtils.getCurrentTimestamp());
		if(TimestampUtils.removeTime(lecture.getStartDate()).equals(TimestampUtils.removeTime(TimestampUtils.getCurrentDate())))
		{
			if(startTime.after(currentTime))
			{
				logger.debug("Lecture is not on going lecture ");
			}
			else if(startTime.before(currentTime) && endTime.before(currentTime) )
			{
				logger.debug("Lecture is not on going lecture ");
			}
			//Removed the check for getting the details of ongoing lectures 
			/*else if(startTime.before(currentTime) && endTime.after(currentTime) )
			{
				return Response.status(Status.BAD_REQUEST).entity("Details can’t be seen for the on going lecture").build();
			}
			else if(startTime.equals(currentTime) && endTime.after(currentTime))
			{
				return Response.status(Status.BAD_REQUEST).entity("Details can’t be seen for the on going lecture").build();
			}*/
		}
		return null;
	}

	/**
	 * Function to get users login details of particular lecture
	 * @param adminId
	 * @param lectureId
	 * @param userId
	 * @return Object
	 * @throws AViewException
	 */
	private static Object getUserLoginLogoutArraybyLectureId(Long adminId,Long lectureId,Long userId) throws AViewException
	{
		String result = new String();
		logger.debug("Enter user login and logout details ::analytics.");
		Lecture lecture = null;
		String errorMessage = null;
		User user = null;
		User admin = null;
		Class aviewClass = null;
		Object resultObjectAdmin =  UserHelper.userValidCheck(Constant.ADMIN_ID,adminId);
		if(User.class.isInstance(resultObjectAdmin))
		{
			admin = (User)resultObjectAdmin;
		}
		else
		{
			errorMessage = resultObjectAdmin.toString();
			return errorMessage;
		}
		if(userId != null)
		{
			Object resultObjectUser =  UserHelper.userValidCheck(Constant.USER_ID,userId);
			if(User.class.isInstance(resultObjectUser))
			{
				user = (User)resultObjectUser;
			}
			else
			{
				errorMessage = resultObjectUser.toString();
				return errorMessage;
			}
		}
		else
		{
			userId = 0l;
		}
		if(lectureId == null || lectureId.equals(" ")|| lectureId.equals(0))
		{
			lectureId = 0l;
		}
		Object resultObjectLecture =  LectureHelper.getLecture(lectureId);
		if(Lecture.class.isInstance(resultObjectLecture))
		{
			lecture = (Lecture)resultObjectLecture;
		}
		else
		{
			if(resultObjectLecture == null)
			{
				return "Entered lecture Id doesn't exist or not valid";
			}
			else
			{
				errorMessage = resultObjectLecture.toString();
				return errorMessage;
			}
		}
		ArrayList logDetailsArray = new ArrayList();
		if(lecture != null)
		{
			Object validationMessageForLecture = validateLecture(lecture);
			if(validationMessageForLecture != null)
			{
				return validationMessageForLecture.toString();
			}
			try 
			{
				aviewClass = ClassHelper.getClass(lecture.getClassId());
				if(aviewClass != null && aviewClass.getStatusId() == StatusHelper.getActiveStatusId())
				{
					Object validationMessageForAdmin = ClassHelper.adminValidation(aviewClass, admin);
					if(validationMessageForAdmin != null)
					{
						return validationMessageForAdmin.toString();
					}
				}
				else
				{
					return "The class of the lecture id is not valid";
				}			
				int statusId = StatusHelper.getActiveStatusId();
				List logDetails = AnalyticsDAO.getUserLoginLogoutbyLectureId(lecture.getLectureId(),userId,statusId);
				AuditLecture auditLecture = null;
				AuditUserLogin auditUserLogin = null;
				String lastActionTime = new String();
				String aviewlogoutTime = new String();
				if(!(logDetails.isEmpty()))
				{
					for (int i = 0; i < logDetails.size(); i++)
					{
						lastActionTime = "";
						aviewlogoutTime = "";
						ArrayList arrToAddLogDetails = new ArrayList();
						Object[] objArray = (Object[])(logDetails.get(i));
						//user = (User) objArray[0];
						//auditUserLogin = (AuditUserLogin) objArray[1];
						//auditLecture = (AuditLecture) objArray[2];
						arrToAddLogDetails.add("userId: " + objArray[0]);
						arrToAddLogDetails.add("userName: " +  objArray[1]);
						String aviewLoginTime = objArray[2].toString().substring(0, 19);
						arrToAddLogDetails.add("aviewLoginTime: " + aviewLoginTime);
						String sessionLogInTime = objArray[3].toString().substring(0, 19);
						arrToAddLogDetails.add("sessionLoginTime: " + sessionLogInTime);
						if(objArray[4] != null)
						{
							lastActionTime = objArray[4].toString().substring(0, 19);
							arrToAddLogDetails.add("lastActionTimeInSession: " + lastActionTime);
						}
						else
						{
							lastActionTime = "Unknown";
							arrToAddLogDetails.add("lastActionTimeInSession: " + lastActionTime);
						}
						if(objArray[5] != null)
						{
							aviewlogoutTime = objArray[5].toString().substring(0, 19);
							arrToAddLogDetails.add("aviewLogoutTime: " + aviewlogoutTime);
						}
						else
						{
							aviewlogoutTime = "Unknown";
							arrToAddLogDetails.add("aviewLogoutTime: " + aviewlogoutTime);
						}
						logDetailsArray.add(arrToAddLogDetails);
					}
				}
				else
				{
					Date startDate = TimestampUtils.removeTime(lecture.getStartDate());
					Date currentDate = TimestampUtils.removeTime(TimestampUtils.getCurrentDate());
					Date startTime = TimestampUtils.removeDateAndMillis(lecture.getStartTime());
					Date currentTime = TimestampUtils.removeDateAndMillis(TimestampUtils.getCurrentTimestamp());
					if(startDate.after(currentDate))
					{
						return "Details are not available as this is a future lecture";
					}
					else if(startDate.equals(currentDate) && startTime.after(currentTime))
					{
						return "Details are not available as this is a future lecture";
					}
					else
					{
						return "No login details are available for this lecture id";
					}
				}
				logger.debug("Exit user login and logout details on success::analytics.");
				return logDetailsArray; 
			}
			catch(NumberFormatException nfe) 
			{
				result = "invalid";
				logger.debug("Exit user login and logout details on invalid request::analytics.");
				return result;    
			}
			catch(AViewException ae)
			{
				result = "Error during log. Possible reason(s) : 1. Unexpected data, 2. Unknown.";
				logger.debug("Exit user login and logout details on error durning log::analytics.");
				return result;
			} 
		}
		else
		{
			return errorMessage;
		}
	}
	
	/**
	 * Function to get users login details of particular lecture 
	 * @param adminId
	 * @param lectureId
	 * @return response
	 * @throws AViewException
	 */	
	@RequestMapping(value = "/logindetailsforlecture.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response getUserLoginLogoutbyLectureId(@RequestParam("adminId") Long adminId,
			@RequestParam("lectureId") Long lectureId,
			@RequestParam("userId") Long userId) throws AViewException {
		
		Object resultObject =  getUserLoginLogoutArraybyLectureId(adminId, lectureId, userId);
		String errorMessage = null;
		ArrayList resultArray = new ArrayList();
		Response response = null;
		if(resultObject instanceof ArrayList)
		{
			resultArray = (ArrayList)resultObject;
			return Response.status(Status.OK).entity(resultArray).build(); 
		}
		else
		{
			errorMessage = resultObject.toString();
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();			
		}
	}
	
	private static Object  getUserInteractionDetailsbyLectureIdReturnArrayList(Long adminId,Long lectureId,Long userId) throws AViewException
	{
		String result = new String();
		logger.debug("Enter user interaction details on success::analytics.");
		Lecture lecture = null;
		String errorMessage = null;
		User admin = null;
		User user = null;
		Object resultObjectAdmin =  UserHelper.userValidCheck(Constant.ADMIN_ID,adminId);
		if(User.class.isInstance(resultObjectAdmin))
		{
			admin = (User)resultObjectAdmin;
		}
		else
		{
			errorMessage = resultObjectAdmin.toString();
			return errorMessage;
		}
		if(userId != null)
		{
			Object resultObjectUser =  UserHelper.userValidCheck(Constant.USER_ID,userId);
			if(User.class.isInstance(resultObjectUser))
			{
				user = (User)resultObjectUser;
			}
			else
			{
				errorMessage = resultObjectUser.toString();
				return errorMessage;
			}
		}
		else
		{
			userId = 0l;
		}
		if(lectureId == null || lectureId.equals(" ")|| lectureId == 0)
		{
			lectureId = 0l;
		}
		Object resultObjectLecture =  LectureHelper.getLecture(lectureId);
		if(Lecture.class.isInstance(resultObjectLecture))
		{
			lecture = (Lecture)resultObjectLecture;
		}
		else
		{
			if(resultObjectLecture == null)
			{
				return "Entered lecture Id doesn't exist or not valid";
			}
			else
			{
				errorMessage = resultObjectLecture.toString();
				return errorMessage;
			}
		}
		ArrayList logDetailsArray = new ArrayList();
		List tmpUserInteractionDetails = null;
		if(lecture != null)
		{
			Object validationMessageForLecture = validateLecture(lecture);
			if(validationMessageForLecture != null)
			{
				return validationMessageForLecture;
			}
			try 
			{
				Class aviewClass = ClassHelper.getClass(lecture.getClassId());
				if(aviewClass != null && aviewClass.getStatusId() == StatusHelper.getActiveStatusId())
				{
					Object validationMessageeForAdmin = ClassHelper.adminValidation(aviewClass, admin);
					if(validationMessageeForAdmin != null)
					{
						return validationMessageeForAdmin;
					}
				}
				else
				{
					return "The class of the lecture id is not valid";
				}
				int statusId = StatusHelper.getActiveStatusId();
				tmpUserInteractionDetails = AnalyticsDAO.getUserInteractionDetailsbyLectureId(lecture.getLectureId(),userId,statusId);
				HashMap<String, String[]> userInteractionDetails = new HashMap<String, String[]>();
				String[] oldValue = null;
				Object[] obj = null;
				if(!(tmpUserInteractionDetails.isEmpty()))
				{
					for (int i = 0; i < tmpUserInteractionDetails.size(); i++)
					{
						oldValue = null;
						obj = (Object[]) tmpUserInteractionDetails.get(i);
						if(userInteractionDetails.containsKey(obj[1]))
						{
							oldValue = userInteractionDetails.get(obj[1]);
							if(obj[2].equals(INTERACTING))
							{
								if(oldValue[3] != null)
								{
									oldValue[2] = (Integer.parseInt(oldValue[2]) + 1) + "";
									String timeofInteraction = obj[3].toString().substring(0, 19);
									oldValue[3] = oldValue[3] + " ~ , ~ " + timeofInteraction;
								}
								else
								{
									oldValue[2] = "1";
									String timeofInteraction = obj[3].toString().substring(0, 19);
									oldValue[3] = timeofInteraction;
								}
							}
							if(obj[2].equals(INTERACTION_END))
							{
								if(oldValue[4] != null)
								{
									String timeofInteractionEnded = obj[3].toString().substring(0, 19);
									oldValue[4] = oldValue[4] + " ~ , ~ " + timeofInteractionEnded;
								}
								else
								{
									String timeofInteractionEnded = obj[3].toString().substring(0, 19);
									oldValue[4] = timeofInteractionEnded;
								}
							}
							userInteractionDetails.put(obj[1].toString(), oldValue);
						}
						else
						{
							oldValue = new String[6];
							oldValue[0] = obj[0].toString();
							oldValue[1] = obj[1].toString();
							oldValue[2] = "0";
							if(obj[2].equals(INTERACTING))
							{
								oldValue[2] = "1";
								String timeofInteraction = obj[3].toString().substring(0, 19);
								oldValue[3] = timeofInteraction;
							}
							if(obj[2].equals(INTERACTION_END))
							{
								String timeofInteractionEnded = "";
								timeofInteractionEnded = obj[3].toString().substring(0, 19);
								oldValue[4] = timeofInteractionEnded;
							}
							oldValue[5] = obj[4].toString();
							userInteractionDetails.put(obj[1].toString(), oldValue);
						} 
					}
					return userInteractionDetails;
				}
				else
				{
					Date startDate = TimestampUtils.removeTime(lecture.getStartDate());
					Date currentDate = TimestampUtils.removeTime(TimestampUtils.getCurrentDate());
					Date startTime = TimestampUtils.removeDateAndMillis(lecture.getStartTime());
					Date currentTime = TimestampUtils.removeDateAndMillis(TimestampUtils.getCurrentTimestamp());
					if(startDate.after(currentDate))
					{
						return "Details are not available as this is a future lecture";
					}
					else if(startDate.equals(currentDate) && startTime.after(currentTime))
					{
						return "Details are not available as this is a future lecture";
					}
					else
					{
						return "No interaction details happened in this session";
					}
				}
			}
			catch(NumberFormatException nfe) 
			{
				result = "invalid";
				logger.debug("Exit user interaction details on invalid request::analytics.");
				return result;    
			}
			catch(AViewException ae)
			{
				result = "Error during log. Possible reason(s) : 1. Unexpected data, 2. Unknown.";
				logger.debug("Exit user interaction details on error durning log::analytics.");
				return result;
			} 
		}
		else
		{
			return errorMessage;
		}
	}
	/**
	 * Function to get users interaction details of particular lecture 
	 * @param adminId
	 * @param lectureId
	 * @return response
	 * @throws AViewException
	 */
	@RequestMapping(value = "/userinteractiondetailsforlecture.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response getUserInteractionDetailsbyLectureId(@RequestParam("adminId") Long adminId,
			@RequestParam("lectureId") Long lectureId,
			@RequestParam("userId") Long userId) throws AViewException {
		
		logger.debug("Enter user interaction details on success::analytics. ");
		String errorMessage = null;
		Object resultObjectResult =  getUserInteractionDetailsbyLectureIdReturnArrayList(adminId, lectureId, userId);
		HashMap<String, String[]> userInteractionDetails = new HashMap<String, String[]>();
		String[] oldValue = null;
		String[] publishingStatusValue = null;
		String interactEndDateTime = new String();
		String interactStartDateTime = new String();
		ArrayList userInteractionDetailsWithCount = new ArrayList();
		ArrayList tmpUserInteractionDetailsWithCount = null;
		String key = null;
		if(resultObjectResult instanceof HashMap)
		{
			userInteractionDetails = (HashMap<String, String[]>)resultObjectResult;
		}
		else
		{
			errorMessage = resultObjectResult.toString();
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		Lecture lecture = null;
		if(lectureId == null || lectureId.equals(" ")|| lectureId == 0)
		{
			lectureId = 0l;
		}
		Object resultObjectLecture =  LectureHelper.getLecture(lectureId);
		if(Lecture.class.isInstance(resultObjectLecture))
		{
			lecture = (Lecture)resultObjectLecture;
		}
		else
		{
			if(resultObjectLecture == null)
			{
				return Response.status(Status.BAD_REQUEST).entity("Entered lecture Id doesn't exist or not valid").build();
			}
			else
			{
				errorMessage = resultObjectLecture.toString();
				return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
			}
		}
		Object resultObjectAudiVideoResult = getuserAudioVideoPublishingStatusByLectureIdArrayList(lecture);
		HashMap<String, String[]> userAudioVideoPublishingDetails = new HashMap<String, String[]>();
		if(resultObjectAudiVideoResult instanceof HashMap)
		{
			userAudioVideoPublishingDetails = (HashMap<String, String[]>)resultObjectAudiVideoResult;
		}
		else
		{
			errorMessage = resultObjectResult.toString();
			 Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		if(userInteractionDetails != null || userInteractionDetails.size() > 0)
		{
			for (Map.Entry<String, String[]> entry : userInteractionDetails.entrySet()) 
			{
			    key = entry.getKey();
			    oldValue = entry.getValue();
				tmpUserInteractionDetailsWithCount = new ArrayList();
				tmpUserInteractionDetailsWithCount.add("interactedUserId: " + oldValue[0]);
				tmpUserInteractionDetailsWithCount.add("interactedUserName: " + oldValue[1]);
				tmpUserInteractionDetailsWithCount.add("interactionCount: " + oldValue[2]);
				if(oldValue[3] != null)
				{
					tmpUserInteractionDetailsWithCount.add("interactionStartDateTime: " + oldValue[3]);
				}
				else
				{
					interactStartDateTime = "Unknown";
					tmpUserInteractionDetailsWithCount.add("interactionStartDateTime: " + interactStartDateTime);
				}
				if(oldValue[4] != null)
				{
					tmpUserInteractionDetailsWithCount.add("interactionEndDateTime: " + oldValue[4]);
				}
				else
				{
					interactEndDateTime = "Unknown";
					tmpUserInteractionDetailsWithCount.add("interactionEndDateTime: " + interactEndDateTime);
				}
				tmpUserInteractionDetailsWithCount.add("interactedUserInstitute: " + oldValue[5]);
				tmpUserInteractionDetailsWithCount.add("publishingStatus: " + 0);
				for (Map.Entry<String, String[]> publishingStatusentry : userAudioVideoPublishingDetails.entrySet()) 
				{
				    key = publishingStatusentry.getKey();
				    publishingStatusValue = publishingStatusentry.getValue();
				    if(publishingStatusValue[0].equals(oldValue[1]))
				    {
				    	tmpUserInteractionDetailsWithCount.set(6,"publishingStatus: " + publishingStatusValue[2]);	
				    }
				}
				userInteractionDetailsWithCount.add(tmpUserInteractionDetailsWithCount);
			}
			logger.debug("Exit user interaction details on success::analytics.");
			return Response.status(Status.OK).entity(userInteractionDetailsWithCount).build();
		}	
		else
		{
			return Response.status(Status.OK).entity("No interaction details are available for this lecture id").build();
		}
	}
	
	/**
	 * Function to get question details of particular lecture 
	 * @param adminId
	 * @param lectureId
	 * @return Object
	 * @throws AViewException
	 */
	public static Object getQuestionDetailsbyLectureIdReturnArrayList(Long adminId,Long lectureId) throws AViewException
	{
		String result = new String();
		logger.debug("Enter question details on success::analytics. ");
		Lecture lecture = null;
		String errorMessage = null;
		User admin = null;
		Object resultObjectAdmin =  UserHelper.userValidCheck(Constant.ADMIN_ID,adminId);
		if(User.class.isInstance(resultObjectAdmin))
		{
			admin = (User)resultObjectAdmin;
		}
		else
		{
			errorMessage = resultObjectAdmin.toString();
			return errorMessage;
		}
		if(lectureId == null || lectureId.equals(" ")|| lectureId == 0)
		{
			lectureId = 0l;
		}
		Object resultObjectLecture =  LectureHelper.getLecture(lectureId);
		if(Lecture.class.isInstance(resultObjectLecture))
		{
			lecture = (Lecture)resultObjectLecture;
		}
		else
		{
			if(resultObjectLecture == null)
			{
				errorMessage = "Entered lecture Id doesn't exist or not valid";
				return errorMessage;
			}
			else
			{
				errorMessage = resultObjectLecture.toString();
				return errorMessage;
			}
		}
		ArrayList logDetailsArray = new ArrayList();
		if(lecture != null)
		{
			Object validationMessageForLecture = validateLecture(lecture);
			if(validationMessageForLecture != null)
			{
				return validationMessageForLecture.toString();
			}
			try 
			{
				Class aviewClass = ClassHelper.getClass(lecture.getClassId());
				if(aviewClass != null && aviewClass.getStatusId() == StatusHelper.getActiveStatusId())
				{
					Object validationMessageForAdmin = ClassHelper.adminValidation(aviewClass, admin);
					if(validationMessageForAdmin != null)
					{
						return validationMessageForAdmin.toString();
					}
				}
				else
				{
					return "The class of the lecture id is not valid";
				}
				int statusId = StatusHelper.getActiveStatusId();
				List tmpQuestionDetails = AnalyticsDAO.getQuestionDetailsbyLectureId(lecture.getLectureId(), statusId);
				
				HashMap<String, String[]> questionDetails = new HashMap<String, String[]>();
				String[] oldValue = null;
				Object[] obj = null;
				if((tmpQuestionDetails != null) && (tmpQuestionDetails.size() > 0))
				{
					for (int i = 0; i < tmpQuestionDetails.size(); i++)
					{
						oldValue = null;
						obj = (Object[]) tmpQuestionDetails.get(i);
						if(questionDetails.containsKey(obj[0]))
						{
							oldValue = questionDetails.get(obj[0]);
							if((oldValue[2] =="Unknown" || oldValue[2] == null) && obj[2].toString().equals(QUESTION_ASKED))
							{
								String timeofInteraction = obj[3].toString().substring(0, 19);
								oldValue[2] = timeofInteraction;
							}
							else if(oldValue[2] == null && obj[2].toString().equals(QUESTION_ASKED))
							{
								oldValue[2] = "Unknown";
							}
							if(oldValue[2] == null && obj[2].toString().equals(QUESTION_ANSWER))
							{
								oldValue[2] = "Unknown";
							}
							if(obj[2].toString().equals(QUESTION_ASKED)  && oldValue[1] == null )
							{
								oldValue[1] = obj[1].toString();
							}
							else if(obj[2].toString().equals(QUESTION_ASKED)  && !(oldValue[1]).equals(obj[1]))
							{
								oldValue[1] = oldValue[1]+","+obj[1].toString();
								String timeofInteraction = obj[3].toString().substring(0, 19);
								if(oldValue[2].equals("Unknown"))
								{
									oldValue[2] = timeofInteraction;
								}
								else
								{
									oldValue[2] = oldValue[2]+","+timeofInteraction;
								}
							}
							else if(obj[2].toString().equals(QUESTION_VOTE))
							{
								int vote1 = Integer.parseInt(oldValue[3]);
								int vote2 =Integer.parseInt(obj[4].toString());
								logger.debug("vote1"+vote1+"vote2"+vote2);
								if(Integer.parseInt(oldValue[3]) > Integer.parseInt(obj[4].toString()))
								{
									oldValue[3] = oldValue[3];
								}
								else
								{
									oldValue[3] = obj[4].toString();
								}
							}
							else if(obj[2].toString().equals(QUESTION_ANSWER))
							{
								oldValue[4] = "Yes";
								String timeofInteraction = obj[3].toString().substring(0, 19);
								oldValue[5] = timeofInteraction;	
							}	
							questionDetails.put(obj[0].toString(), oldValue);
						}
						else
						{
							oldValue = new String[6];
							//Question Text						
							oldValue[0] = obj[0].toString();
							//User Name
							oldValue[1] = null;
							oldValue[3] = "0";
							oldValue[4] = "No";
							oldValue[5] = "NA";
							if(obj[2].toString().equals(QUESTION_ASKED))
							{
								oldValue[1] = obj[1].toString();
								String timeofInteraction = obj[3].toString().substring(0, 19);
								oldValue[2] = timeofInteraction;							
							}
							else if(obj[2].toString().equals(QUESTION_VOTE))
							{
								oldValue[3] = obj[4].toString();
							}
							else if(obj[2].toString().equals(QUESTION_ANSWER))
							{
								oldValue[4] = "Yes";
								String timeofInteraction = obj[3].toString().substring(0, 19);
								oldValue[5] = timeofInteraction;	
							}						
							questionDetails.put(obj[0].toString(), oldValue);
						}
					}
					return questionDetails;
				}
				else
				{
					Date startDate = TimestampUtils.removeTime(lecture.getStartDate());
					Date currentDate = TimestampUtils.removeTime(TimestampUtils.getCurrentDate());
					Date startTime = TimestampUtils.removeDateAndMillis(lecture.getStartTime());
					Date currentTime = TimestampUtils.removeDateAndMillis(TimestampUtils.getCurrentTimestamp());
					if(startDate.after(currentDate))
					{
						return "Details are not available as this is a future lecture";
					}
					else if(startDate.equals(currentDate) && startTime.after(currentTime))
					{
						return "Details are not available as this is a future lecture";
					}
					else
					{
						return "No question details are available for this lecture Id";
					}
				}
			}
			catch(NumberFormatException nfe) 
			{
				result = "invalid";
				return result;    
			}
			catch(AViewException ae)
			{
				result = "Error during log. Possible reason(s) : 1. Unexpected data, 2. Unknown.";
				logger.debug("Exit question details on error durning log::analytics.");
				return result;
			} 
		}
		else
		{
			return errorMessage;
		}
	}
	
	/**
	 * Function to get question details of particular lecture 
	 * @param adminId
	 * @param lectureId
	 * @return response
	 * @throws AViewException
	 */
	@RequestMapping(value = "/questiondetailsforlecture.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response getQuestionDetailsbyLectureId(@RequestParam("adminId") Long adminId,
			@RequestParam("lectureId") Long lectureId) throws AViewException {
		
		String errorMessage = null;
		Object resultObjectResult =  getQuestionDetailsbyLectureIdReturnArrayList(adminId, lectureId);
		HashMap<String, String[]> questionDetails = new HashMap<String, String[]>();
		String[] oldValue = null;
		if(resultObjectResult instanceof HashMap)
		{
			questionDetails = (HashMap<String, String[]>)resultObjectResult;
		}
		else
		{
			errorMessage = resultObjectResult.toString();
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		ArrayList QuestionWithCount = new ArrayList();
		ArrayList tmpQuestionDetailsWithCount = null;
		String key = null;
		if((questionDetails != null) && (questionDetails.size() > 0))
		{
			for (Map.Entry<String, String[]> entry : questionDetails.entrySet()) 
			{
			    key = entry.getKey();
			    oldValue = entry.getValue();
				tmpQuestionDetailsWithCount = new ArrayList();
				tmpQuestionDetailsWithCount.add("questionText: " + oldValue[0]);
				if(oldValue[1] == null)
				{
					oldValue[1] = "Unknown";
					tmpQuestionDetailsWithCount.add("questionRaisedBy: " + oldValue[1]);
				}
				else
				{
					tmpQuestionDetailsWithCount.add("questionRaisedBy: " + oldValue[1]);
				}
				if(oldValue[2] == null)
				{
					oldValue[2] = "Unknown";
					tmpQuestionDetailsWithCount.add("questionRaisedDateTime: " + oldValue[2]);
				}
				else
				{
					tmpQuestionDetailsWithCount.add("questionRaisedDateTime: " + oldValue[2]);
				}
				tmpQuestionDetailsWithCount.add("questionVote: " + oldValue[3]);
				tmpQuestionDetailsWithCount.add("isQuestionAnswered: " + oldValue[4]);
				tmpQuestionDetailsWithCount.add("questionAnswerDateTime: " + oldValue[5]);
				QuestionWithCount.add(tmpQuestionDetailsWithCount);
			}
			logger.debug("Exit question details on success::analytics.");
			return Response.status(Status.OK).entity(QuestionWithCount).build();
		}
		else
		{
			return Response.status(Status.BAD_REQUEST).entity("No question details are available for this lecture Id").build();
		}
		
	}
	
	/**
	 * Function to get reconnection details of particular lecture 
	 * @param adminId
	 * @param lectureId
	 * @return response
	 * @throws AViewException
	 */
	@RequestMapping(value = "/reconnectiondetailsforlecture.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response getConnectionDetailsbyLectureId(@RequestParam("adminId") Long adminId,
			@RequestParam("lectureId") Long lectureId) throws AViewException {
		
		String result = new String();
		logger.debug("Enter reconnection details on success ::analytics. ");
		Lecture lecture = null;
		String errorMessage = null;
		User admin = null;
		Object resultObjectAdmin =  UserHelper.userValidCheck(Constant.ADMIN_ID,adminId);
		if(User.class.isInstance(resultObjectAdmin))
		{
			admin = (User)resultObjectAdmin;
		}
		else
		{
			errorMessage = resultObjectAdmin.toString();
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		if(lectureId == null || lectureId.equals(" ")|| lectureId == 0)
		{
			lectureId = 0l;
		}
		Object resultObjectLecture =  LectureHelper.getLecture(lectureId);
		if(Lecture.class.isInstance(resultObjectLecture))
		{
			lecture = (Lecture)resultObjectLecture;
		}
		else
		{
			
			if(resultObjectLecture == null)
			{
				return Response.status(Status.BAD_REQUEST).entity("Entered lecture Id doesn't exist or not valid").build();
			}
			else
			{
				errorMessage = resultObjectLecture.toString();
				return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
			}
		}
		ArrayList logDetailsArray = new ArrayList();
		if(lecture != null)
		{
			Object validationMessageForLecture = validateLecture(lecture);
			if(validationMessageForLecture != null)
			{
				return Response.status(Status.BAD_REQUEST).entity(validationMessageForLecture.toString()).build();
			}
			try 
			{
				Class aviewClass = ClassHelper.getClass(lecture.getClassId());
				if(aviewClass != null && aviewClass.getStatusId() == StatusHelper.getActiveStatusId())
				{
					Object validationMessageForAdmin = ClassHelper.adminValidation(aviewClass, admin);
					if(validationMessageForAdmin != null)
					{
						return Response.status(Status.BAD_REQUEST).entity(validationMessageForAdmin.toString()).build();
					}
				}
				else
				{
					return Response.status(Status.BAD_REQUEST).entity("The class of the lecture id is not valid").build();
				}
				int statusId = StatusHelper.getActiveStatusId();
				List tmpConnectionDetails = AnalyticsDAO.getConnectionDetailsbyLectureId(lecture.getLectureId(), statusId);
				List reconnectionDetail = null;
				Object[] connectionDetail = null;
				ArrayList reconnectionDetails = new ArrayList();
				boolean statusCheckFlag = false;
				boolean failureFirstFlagSelect = false;
				String userName = "";
				if(!(tmpConnectionDetails.isEmpty()))
				{
					for (int i = 0; i < tmpConnectionDetails.size(); i++)
						
					{
						connectionDetail = (Object[]) tmpConnectionDetails.get(i);
						if((connectionDetail[1].equals(CONNECTION_FAIL) || connectionDetail[1].equals(CONNECTION_REJECT)) && failureFirstFlagSelect == false)
						{
							reconnectionDetail = new ArrayList();
							statusCheckFlag=true;
							reconnectionDetail.add("userName: "+connectionDetail[0]);
							userName = connectionDetail[0].toString();
							reconnectionDetail.add("failureType: "+connectionDetail[1]);
							String timeofInteraction = connectionDetail[2].toString().substring(0, 19);
							reconnectionDetail.add("failureTime: "+timeofInteraction);
							failureFirstFlagSelect = true;
						}
						else if(connectionDetail[1].equals(CONNECTION_SUCCESS) && statusCheckFlag == true && userName.equals(connectionDetail[0]))
						{
							String timeofInteraction = connectionDetail[2].toString().substring(0, 19);
							reconnectionDetail.add("sucessTime: "+timeofInteraction);
							reconnectionDetails.add(reconnectionDetail);
							statusCheckFlag = false;
							userName="";
							failureFirstFlagSelect=false;
						}
						else
						{
							failureFirstFlagSelect = false;
						}
					}
				}
				else
				{
					return Response.status(Status.OK).entity("No reconnection details are available for this lecture id").build();
				}
				logger.debug("Exit reconnection details on success::analytics.");
				if(!(reconnectionDetails.isEmpty()))
				{
					return Response.status(Status.OK).entity(reconnectionDetails).build();
				}
				else
				{
					return Response.status(Status.OK).entity("No reconnection details are available for this lecture id").build();
				}
			}
			catch(NumberFormatException nfe) 
			{
				result = "invalid";
				logger.debug("Exit reconnection details on invalid request::analytics.");
				return Response.status(Status.BAD_REQUEST).entity(result).build();    
			}
			catch(AViewException ae)
			{
				result = "Error during log. Possible reason(s) : 1. Unexpected data, 2. Unknown.";
				logger.debug("Exit reconnection details on error durning log::analytics.");
				return Response.status(Status.BAD_REQUEST).entity(result).build();
			} 
		}
		else
		{
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
	}
	
	/**
	 * Function to get user participation of particular class 
	 * @param adminId
	 * @param userId
	 * @param classId
	 * @return response
	 * @throws AViewException
	 */
	@RequestMapping(value = "/userparticipationbyclass.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response getUserParticipationbyclassId(@RequestParam("adminId") Long adminId,
			@RequestParam("userId") Long userId,
			@RequestParam("classId") Long classId) throws AViewException {
		
		String result = new String();
		logger.debug("Enter all analytics details on success ::analytics. ");
		Class aviewClass = null;
		String errorMessage = null;
		User admin = null;
		User user = null;
		Object resultObjectAdmin =  UserHelper.userValidCheck(Constant.ADMIN_ID,adminId);
		if(User.class.isInstance(resultObjectAdmin))
		{
			admin = (User)resultObjectAdmin;
		}
		else
		{
			errorMessage = resultObjectAdmin.toString();
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		if(classId == null || userId == null)
		{
			return Response.status(Status.BAD_REQUEST).entity("Please provide both the search criteria").build();
		}
		if(classId == null || classId.equals(" ")|| classId == 0)
		{
			classId = 0l;
		}
		try
		{
			Object resultObjectClass =  ClassHelper.getClass(classId);
			if(Class.class.isInstance(resultObjectClass))
			{
				aviewClass = (Class)resultObjectClass;
			}
			else
			{
				if(resultObjectClass == null)
				{
					return Response.status(Status.BAD_REQUEST).entity("Entered class Id doesn't exist or not valid").build();
				}
				else
				{
					errorMessage = resultObjectClass.toString();
					return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
				}
			}
			if(userId != null)
			{
				Object resultObjectUser =  UserHelper.userValidCheck(Constant.USER_ID,userId);
				if(User.class.isInstance(resultObjectUser))
				{
					user = (User)resultObjectUser;
				}
				else
				{
					errorMessage = resultObjectUser.toString();
					return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
				}
			}
			else
			{
				userId = 0l;
			}
			Object logDetailsArray = null;
			Object logDetailsArrayList = null;
			Response userInteractionDetailsArray = null;
			Object userInteractionDetailsArrayList = null;
			Response questionDetailsArray = null;
			Lecture lectureDet = null;
			ArrayList allDetails = null;
			ArrayList tmpAllDetails = null;
			if(aviewClass != null)
			{
				List lectureDetails = LectureHelper.getLecturesForClass(aviewClass.getClassId());
				allDetails = new ArrayList();
				ArrayList QuestionWithCount = new ArrayList();
				ArrayList tmpQuestionDetailsWithCount = null;
				String key = null;
				for(int i = 0 ; i < lectureDetails.size() ; i++)
				{
					lectureDet =(Lecture)lectureDetails.get(i);
					QuestionWithCount = new ArrayList();
					tmpAllDetails = new ArrayList();
					
					Date startDate = TimestampUtils.removeTime(lectureDet.getStartDate());
					Date currentDate = TimestampUtils.removeTime(TimestampUtils.getCurrentDate());
					Date startTime = TimestampUtils.removeDateAndMillis(lectureDet.getStartTime());
					Date currentTime = TimestampUtils.removeDateAndMillis(TimestampUtils.getCurrentTimestamp());
					if(startDate.before(currentDate) || startDate.equals(currentDate))
					{
						tmpAllDetails.add("lecture : "+lectureDet.getLectureName());
						tmpAllDetails.add("lectureId : "+lectureDet.getLectureId());			
						logDetailsArray = getUserLoginLogoutArraybyLectureId(adminId,lectureDet.getLectureId(), user.getUserId());
						logDetailsArrayList = logDetailsArray; 
						tmpAllDetails.add("loginDetails : "+logDetailsArrayList);
						
						userInteractionDetailsArray = getUserInteractionDetailsbyLectureId(adminId, lectureDet.getLectureId(), userId);
						userInteractionDetailsArrayList = userInteractionDetailsArray.getEntity(); 
						tmpAllDetails.add("userInteractionDetails : "+userInteractionDetailsArrayList);
						
						questionDetailsArray = getQuestionDetailsbyLectureId(adminId, lectureDet.getLectureId());
						Object resultObjectResult =  getQuestionDetailsbyLectureIdReturnArrayList(adminId, lectureDet.getLectureId());
						HashMap<String, String[]> questionDetails = new HashMap<String, String[]>();
						String[] oldValue = null;
						if(resultObjectResult instanceof HashMap)
						{
							questionDetails = (HashMap<String, String[]>)resultObjectResult;
						}
						if((questionDetails != null) && (questionDetails.size() > 0))
						{
							QuestionWithCount = new ArrayList();
							for (Map.Entry<String, String[]> entry : questionDetails.entrySet()) 
							{
							    key = entry.getKey();
							    oldValue = entry.getValue();
								tmpQuestionDetailsWithCount = new ArrayList();
								if(oldValue[1] == null)
								{
									oldValue[1] = "Unknown";
								}
								if(oldValue[1].equals(user.getUserName()) || oldValue[1].contains(user.getUserName()))
								{
									tmpQuestionDetailsWithCount.add("questionText: " + oldValue[0]);
									if(oldValue[2] == null)
									{
										oldValue[2] = "Unknown";
										tmpQuestionDetailsWithCount.add("questionRaisedDateTime: " + oldValue[2]);
									}
									else
									{
										tmpQuestionDetailsWithCount.add("questionRaisedDateTime: " + oldValue[2]);
									}
									tmpQuestionDetailsWithCount.add("questionVote: " + oldValue[3]);
									tmpQuestionDetailsWithCount.add("isQuestionAnswered: " + oldValue[4]);
									tmpQuestionDetailsWithCount.add("questionAnswerDateTime: " + oldValue[5]);
									QuestionWithCount.add(tmpQuestionDetailsWithCount);
								}
							}
							logger.debug("Exit question details on success::analytics.");
						}
						if(QuestionWithCount.isEmpty() )
						{
							tmpAllDetails.add("questionDetails : No question details are available for this lecture Id");
						}
						else
						{
							tmpAllDetails.add("questionDetails : ["+QuestionWithCount+" ]");
						}
						allDetails.add(tmpAllDetails);	
					}
				}
				logger.debug("Exit all analytics details on success::analytics.");
				return Response.status(Status.OK).entity(allDetails).build();
			}
			else
			{
				return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
			}
		}
		catch(NumberFormatException nfe) 
		{
			result = "invalid";
			logger.debug("Exit all analytics details on invalid request::analytics.");
			return Response.status(Status.BAD_REQUEST).entity(result).build();    
		}
		catch(AViewException ae)
		{
			if(ae.getMessage().equals("Given course is not valid"))
			{
				result = "Given search criteria provided is not valid or doesn't exist";
			}
			logger.debug("Exit all analytics details on error durning log::analytics.");
			return Response.status(Status.BAD_REQUEST).entity(result).build();
		} 
		
	}
	
	/**
	 * Functions to get live lectures
	 * @param adminId
	 * @return Response 
	 * @throws AViewException
	 */
	@RequestMapping(value = "/livesessions.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response getLiveSessionsbyAdminId(@RequestParam("adminId") Long adminId) throws AViewException
	{		
		String result = new String();
		logger.debug("Enter live sessions on success::analytics. ");
		String errorMessage = null;
		User admin = null;
		Object resultObjectAdmin =  UserHelper.userValidCheck(Constant.ADMIN_ID,adminId);
		if(User.class.isInstance(resultObjectAdmin))
		{
			admin = (User)resultObjectAdmin;
		}
		else
		{
			errorMessage = resultObjectAdmin.toString();
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		List<Lecture> lectureArray = null;
		ArrayList liveSessionDetails = new ArrayList() ;
		List tmpLectureDetails = null; 
		try 
		{
			int statusId = StatusHelper.getActiveStatusId();
			lectureArray = (List<Lecture>) AnalyticsDAO.getLiveSessionsByAdmin(adminId, statusId);
			if(!(lectureArray.isEmpty()))
			{
				for(Lecture lectureDetails:lectureArray)
				{
					tmpLectureDetails = new ArrayList();
					tmpLectureDetails.add("lecture: " + lectureDetails.getDisplayName());
					tmpLectureDetails.add("lectureId: " + lectureDetails.getLectureId());
					//we are added substring for trimming the time to timeformat hh:mm:ss 
					// for eg:startTime = 2011-06-14 00:00:00.0 in db by trimming startTime becomes 00:00:00
					String startTime = lectureDetails.getStartTime().toString().substring(11,19);
					tmpLectureDetails.add("startTime: " + startTime);
					//we are added substring for trimming the time to timeformat hh:mm:ss 
					// for eg:endTime = 2011-06-14 00:00:00.0 in db by trimming endTime becomes 00:00:00
					String endTime = lectureDetails.getEndTime().toString().substring(11,19);
					tmpLectureDetails.add("endTime: " + endTime);
					liveSessionDetails.add(tmpLectureDetails);
				}
			}
			else
			{
				return Response.status(Status.OK).entity("No live session details available").build();
			}
			logger.debug("Exit live sessions details on success::analytics.");
			return Response.status(Status.OK).entity(liveSessionDetails).build();
		}
		catch(NumberFormatException nfe) 
		{
			result = "invalid";
			logger.debug("Exit live sessions details on invalid request::analytics.");
			return Response.status(Status.BAD_REQUEST).entity(result).build();    
		}
		catch(AViewException ae)
		{
			result = "Error during log. Possible reason(s) : 1. Unexpected data, 2. Unknown.";
			logger.debug("Exit live sessions details on error durning log::analytics.");
			return Response.status(Status.BAD_REQUEST).entity(result).build();
		} 

	}
	
	/**
	 * Function to get attending users in a session
	 * @param adminId
	 * @param lectureId
	 * @return Response
	 * @throws AViewException
	 */
	@RequestMapping(value = "/attendingusers.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response getAttendingUsersbyLectureId(@RequestParam("adminId") Long adminId,
			@RequestParam("lectureId") Long lectureId) throws AViewException
	{	
		String result = new String();
		logger.debug("Enter attending user details ::analytics.");
		Lecture lecture = null;
		String errorMessage = null;
		User admin = null;
		Class aviewClass = null;
		Object resultObjectAdmin =  UserHelper.userValidCheck(Constant.ADMIN_ID,adminId);
		if(User.class.isInstance(resultObjectAdmin))
		{
			admin = (User)resultObjectAdmin;
		}
		else
		{
			errorMessage = resultObjectAdmin.toString();
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		if(lectureId == null || lectureId.equals(" ")|| lectureId == 0)
		{
			lectureId = 0l;
		}
		Object resultObjectLecture =  LectureHelper.getLecture(lectureId);
		if(Lecture.class.isInstance(resultObjectLecture))
		{
			lecture = (Lecture)resultObjectLecture;
		}
		else
		{
			if(resultObjectLecture == null)
			{
				return Response.status(Status.BAD_REQUEST).entity("Entered lecture Id doesn't exist or not valid").build();
			}
			else
			{
				errorMessage = resultObjectLecture.toString();
				return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
			}
		}
		ArrayList attendingUserDetails = new ArrayList();
		if(lecture != null)
		{
			Object validationMessageForLecture = validateLecture(lecture);
			if(validationMessageForLecture != null)
			{
				return Response.status(Status.BAD_REQUEST).entity(validationMessageForLecture.toString()).build();
			}
			try 
			{
				aviewClass = ClassHelper.getClass(lecture.getClassId());
				if(aviewClass != null && aviewClass.getStatusId() == StatusHelper.getActiveStatusId())
				{
					Object validationMessageForAdmin = ClassHelper.adminValidation(aviewClass, admin);
					if(validationMessageForAdmin != null)
					{
						return Response.status(Status.BAD_REQUEST).entity(validationMessageForAdmin.toString()).build();
					}
				}
				else
				{
					return Response.status(Status.BAD_REQUEST).entity("The class of the lecture id is not valid").build();
				}			
				int statusId = StatusHelper.getActiveStatusId();
				List<User> attendingUserArray = AnalyticsDAO.getAttendingUsersbyLectureId(lecture.getLectureId(),statusId);
				if(!(attendingUserArray.isEmpty()))
				{
					for (User attendingUser:attendingUserArray)
					{
						ArrayList tmpAttendingUserDetails = new ArrayList();
						tmpAttendingUserDetails.add("userName: " +  attendingUser.getUserName());
						tmpAttendingUserDetails.add("userId: " + attendingUser.getUserId());
						attendingUserDetails.add(tmpAttendingUserDetails);
					}
				}
				else
				{
					Date startDate = TimestampUtils.removeTime(lecture.getStartDate());
					Date currentDate = TimestampUtils.removeTime(TimestampUtils.getCurrentDate());
					Date startTime = TimestampUtils.removeDateAndMillis(lecture.getStartTime());
					Date currentTime = TimestampUtils.removeDateAndMillis(TimestampUtils.getCurrentTimestamp());
					if(startDate.after(currentDate))
					{
						return Response.status(Status.OK).entity("Details are not available as this is a future lecture").build();
					}
					else if(startDate.equals(currentDate) && startTime.after(currentTime))
					{
						return Response.status(Status.OK).entity("Details are not available as this is a future lecture").build();
					}
					else
					{
						return Response.status(Status.BAD_REQUEST).entity("No users have attended this lecture").build();
					}
				}
				logger.debug("Exit attending users details on success::analytics.");
				return Response.status(Status.OK).entity(attendingUserDetails).build(); 
			}
			catch(NumberFormatException nfe) 
			{
				result = "invalid";
				logger.debug("Exit attending users details on invalid request::analytics.");
				return Response.status(Status.BAD_REQUEST).entity(result).build();    
			}
			catch(AViewException ae)
			{
				result = "Error during log. Possible reason(s) : 1. Unexpected data, 2. Unknown.";
				logger.debug("Exit attending users on error durning log::analytics.");
				return Response.status(Status.BAD_REQUEST).entity(result).build();
			} 
		}
		else
		{
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
	
	}
	
	/**
	 * Function to get user chat interaction details
	 * @param adminId
	 * @param lectureId
	 * @return response
	 * @throws AViewException
	 */

	@RequestMapping(value = "/chatinteractedusers.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response getUserChatDetailsbyLetureId(@RequestParam("adminId") Long adminId,
			@RequestParam("lectureId") Long lectureId) throws AViewException {
		
		String result = new String();
		logger.debug("Entered user chat details ::analytics. ");
		Lecture lecture = null;
		String errorMessage = null;
		User admin = null;
		Class aviewClass = null;
		Object resultObjectAdmin =  UserHelper.userValidCheck(Constant.ADMIN_ID,adminId);
		if(User.class.isInstance(resultObjectAdmin))
		{
			admin = (User)resultObjectAdmin;
		}
		else
		{
			errorMessage = resultObjectAdmin.toString();
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		if(lectureId == null || lectureId.equals(" ")|| lectureId == 0)
		{
			lectureId = 0l;
		}
		Object resultObjectLecture =  LectureHelper.getLecture(lectureId);
		if(Lecture.class.isInstance(resultObjectLecture))
		{
			lecture = (Lecture)resultObjectLecture;
		}
		else
		{
			if(resultObjectLecture == null)
			{
				return Response.status(Status.BAD_REQUEST).entity("Entered lecture Id doesn't exist or not valid").build();
			}
			else
			{
				errorMessage = resultObjectLecture.toString();
				return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
			}
		}
		ArrayList userChatDetails = new ArrayList();
		if(lecture != null)
		{
			Object validationMessageForLecture = validateLecture(lecture);
			if(validationMessageForLecture != null)
			{
				return Response.status(Status.BAD_REQUEST).entity(validationMessageForLecture.toString()).build();
			}
			try 
			{
				aviewClass = ClassHelper.getClass(lecture.getClassId());
				if(aviewClass != null && aviewClass.getStatusId() == StatusHelper.getActiveStatusId())
				{
					Object validationMessageForAdmin = ClassHelper.adminValidation(aviewClass, admin);
					if(validationMessageForAdmin != null)
					{
						return Response.status(Status.BAD_REQUEST).entity(validationMessageForAdmin.toString()).build();
					}
				}
				else
				{
					return Response.status(Status.BAD_REQUEST).entity("The class of the lecture id is not valid").build();
				}			
				int statusId = StatusHelper.getActiveStatusId();
				List chatDetails = AnalyticsDAO.getUserChatDetailsbyLectureId(lecture.getLectureId(),statusId);
				if(!(chatDetails.isEmpty()))
				{
					for(Object tmpChatUser:chatDetails)
					{
						ArrayList tmpUserChatDetails = new ArrayList();
						Object[] objArray = (Object[])(tmpChatUser);
						tmpUserChatDetails.add("userId: " + objArray[0]);
						tmpUserChatDetails.add("userName: " +  objArray[1]);
						if(objArray[2].equals(ACTUAL_CHAT_TO_BE_CLEAR))
						{
							tmpUserChatDetails.add("chatText: " + CHAT_MESSAGE_TO_BE_MESSAGE);
						}
						else
						{
							tmpUserChatDetails.add("chatText: " + objArray[2]);
						}
						String sessionLogInTime = objArray[3].toString().substring(0, 19);
						tmpUserChatDetails.add("chatInteractionDateTime: " + sessionLogInTime);
						userChatDetails.add(tmpUserChatDetails);
					}
				}
				else
				{
					Date startDate = TimestampUtils.removeTime(lecture.getStartDate());
					Date currentDate = TimestampUtils.removeTime(TimestampUtils.getCurrentDate());
					Date startTime = TimestampUtils.removeDateAndMillis(lecture.getStartTime());
					Date currentTime = TimestampUtils.removeDateAndMillis(TimestampUtils.getCurrentTimestamp());
					if(startDate.after(currentDate))
					{
						return Response.status(Status.OK).entity("Details are not available as this is a future lecture").build();
					}
					else if(startDate.equals(currentDate) && startTime.after(currentTime))
					{
						return Response.status(Status.OK).entity("Details are not available as this is a future lecture").build();
					}
					else
					{
						return Response.status(Status.BAD_REQUEST).entity("No chat interaction details are available for this lecture id").build();
					}
				}
				logger.debug("Exit user chat details on success::analytics.");
				return Response.status(Status.OK).entity(userChatDetails).build(); 
			}
			catch(NumberFormatException nfe) 
			{
				result = "invalid";
				logger.debug("Exit user chat details on invalid request::analytics.");
				return Response.status(Status.BAD_REQUEST).entity(result).build();    
			}
			catch(AViewException ae)
			{
				result = "Error during log. Possible reason(s) : 1. Unexpected data, 2. Unknown.";
				logger.debug("Exit user chat details on error durning log::analytics.");
				return Response.status(Status.BAD_REQUEST).entity(result).build();
			} 
		}
		else
		{
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
	}
	
	/**
	 * Function to get audio video publishing status
	 * @param lecture
	 * @return Object
	 * @throws AViewException
	 */
	public static Object getuserAudioVideoPublishingStatusByLectureIdArrayList(Lecture lecture) throws AViewException
	{
		int statusId = StatusHelper.getActiveStatusId();
		ArrayList usersPublishingStatusDetails = new ArrayList();
		List<User> attendingUserArray = AnalyticsDAO.getAttendingUsersbyLectureId(lecture.getLectureId(),statusId);
		List audioVideoPublishingArray = AnalyticsDAO.getUserAudioVideoPublishingDetailsbyLectureId(lecture.getLectureId(), statusId);
		ArrayList tmpPublishingUserDetails = new ArrayList();
		HashMap<String, String[]> userAudioVideoPublishingDetails = new HashMap<String, String[]>();
		String[] tmpAudioVideoArray = null;
		String key  = null;
		if(!(attendingUserArray.isEmpty()))
		{
			for (User attendingUser:attendingUserArray)
			{
				tmpAudioVideoArray = new String[3];
				tmpAudioVideoArray[0] = attendingUser.getUserName();
				tmpAudioVideoArray[1] = attendingUser.getUserId().toString();
				tmpAudioVideoArray[2] = NO_PUBLISH_VALUE;
				userAudioVideoPublishingDetails.put(attendingUser.getUserName().toString(), tmpAudioVideoArray);
			}
			if(!(audioVideoPublishingArray.isEmpty()))
			{
				for(Object publishingStatusUser:audioVideoPublishingArray)
				{
					Object[] objArray = (Object[])(publishingStatusUser);
					for (Map.Entry<String, String[]> entry : userAudioVideoPublishingDetails.entrySet()) 
					{
						key = entry.getKey();
					    tmpAudioVideoArray = entry.getValue();
					    if(tmpAudioVideoArray[0].equals(objArray[1]))
					    {
					    	if(objArray[2].equals(AUDIO_PUBLISH))
							{
					    		tmpAudioVideoArray[2] = AUDIO_PUBLISH_VALUE;
							}
							else if(objArray[2].equals(AUDIOVIDEO_PUBLISH))
							{
								tmpAudioVideoArray[2] = AUDIOVIDEO_PUBLISH_VALUE;
							}
					    }
					    userAudioVideoPublishingDetails.put(key, tmpAudioVideoArray);
					}
				}
			}
			return userAudioVideoPublishingDetails;
			
		}
		else
		{
			Date startDate = TimestampUtils.removeTime(lecture.getStartDate());
			Date currentDate = TimestampUtils.removeTime(TimestampUtils.getCurrentDate());
			Date startTime = TimestampUtils.removeDateAndMillis(lecture.getStartTime());
			Date currentTime = TimestampUtils.removeDateAndMillis(TimestampUtils.getCurrentTimestamp());
			if(startDate.after(currentDate))
			{
				return "Details are not available as this is a future lecture";
			}
			else if(startDate.equals(currentDate) && startTime.after(currentTime))
			{
				return "Details are not available as this is a future lecture";
			}
			else
			{
				return "No users have attended this lecture";
			}
		}
	}
	/**
	 * Function to get audio video publishing status
	 * @param adminId
	 * @param lectureId
	 * @return
	 * @throws AViewException
	 */
	@RequestMapping(value = "/audiovideopublishingstatus.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response getUserAudioVideoPublishingStatusbyLectureId(@RequestParam("adminId") Long adminId,
			@RequestParam("lectureId") Long lectureId) throws AViewException
	{	
		String result = new String();
		logger.debug("Enter audio video publishing status of  users ::analytics.");
		Lecture lecture = null;
		String errorMessage = null;
		User admin = null;
		Class aviewClass = null;
		Object resultObjectAdmin =  UserHelper.userValidCheck(Constant.ADMIN_ID,adminId);
		if(User.class.isInstance(resultObjectAdmin))
		{
			admin = (User)resultObjectAdmin;
		}
		else
		{
			errorMessage = resultObjectAdmin.toString();
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		if(lectureId == null || lectureId.equals(" ")|| lectureId == 0)
		{
			lectureId = 0l;
		}
		Object resultObjectLecture =  LectureHelper.getLecture(lectureId);
		if(Lecture.class.isInstance(resultObjectLecture))
		{
			lecture = (Lecture)resultObjectLecture;
		}
		else
		{
			if(resultObjectLecture == null)
			{
				return Response.status(Status.BAD_REQUEST).entity("Entered lecture Id doesn't exist or not valid").build();
			}
			else
			{
				errorMessage = resultObjectLecture.toString();
				return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
			}
		}
		ArrayList usersPublishingStatusDetails = new ArrayList();
		if(lecture != null)
		{
			Object validationMessageForLecture = validateLecture(lecture);
			if(validationMessageForLecture != null)
			{
				return Response.status(Status.BAD_REQUEST).entity(validationMessageForLecture.toString()).build();
			}
			Calendar lectureEndTime = Calendar.getInstance();
			lectureEndTime.setTime(TimestampUtils.removeDateAndMillis(lecture.getEndTime()));
	        Calendar currentdate = Calendar.getInstance();
	        lectureEndTime.add(lectureEndTime.MINUTE, 30);
	        Date currentTimeCheck = TimestampUtils.removeDateAndMillis(currentdate.getTime());
	        currentdate.setTime(currentTimeCheck);
	    	if(TimestampUtils.removeTime(lecture.getStartDate()).compareTo(TimestampUtils.removeTime(TimestampUtils.getCurrentDate())) <= 0)
	        {
	    	   if(lectureEndTime.getTime().after(currentdate.getTime()))
		       {
	    			logger.debug("Lecture is on going lecture ");
		       }
		       /*
	    	   else
		       {
		    	   return Response.status(Status.BAD_REQUEST).entity("This lecture is not active now, data available only for active sessions.").build();
		       }
		       */
	        }
	        else
	        {
	    	   return Response.status(Status.BAD_REQUEST).entity("This lecture is not active now, data available only for active sessions.").build();
	        }
			try 
			{
				aviewClass = ClassHelper.getClass(lecture.getClassId());
				if(aviewClass != null && aviewClass.getStatusId() == StatusHelper.getActiveStatusId())
				{
					Object validationMessageForAdmin = ClassHelper.adminValidation(aviewClass, admin);
					if(validationMessageForAdmin != null)
					{
						return Response.status(Status.BAD_REQUEST).entity(validationMessageForAdmin.toString()).build();
					}
				}
				else
				{
					return Response.status(Status.BAD_REQUEST).entity("The class of the lecture id is not valid").build();
				}	
				Object usersPublishingStatusObject = getuserAudioVideoPublishingStatusByLectureIdArrayList(lecture);
				HashMap<String, String[]> userAudioVideoPublishingDetails = new HashMap<String, String[]>();
				if(usersPublishingStatusObject instanceof HashMap)
				{
					userAudioVideoPublishingDetails = (HashMap<String, String[]>)usersPublishingStatusObject;
				}
				else
				{
					errorMessage = usersPublishingStatusObject.toString();
					return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
				}
				ArrayList tmpPublishingUserDetails = new ArrayList();
				String[] tmpAudioVideoArray = null;
				String key  = null;
				if(!(userAudioVideoPublishingDetails.isEmpty()))
				{
					for (Map.Entry<String, String[]> entry : userAudioVideoPublishingDetails.entrySet()) 
					{
					    key = entry.getKey();
					    tmpAudioVideoArray = entry.getValue();
					    tmpPublishingUserDetails = new ArrayList();
						tmpPublishingUserDetails.add("userName: " +  tmpAudioVideoArray[0]);
						tmpPublishingUserDetails.add("userId: " + tmpAudioVideoArray[1]);
						tmpPublishingUserDetails.add("publishingStatus: " + tmpAudioVideoArray[2]);
						usersPublishingStatusDetails.add(tmpPublishingUserDetails);
					}
				}
				logger.debug("Exit audio video publishing status of  users on success::analytics.");
				return Response.status(Status.OK).entity(usersPublishingStatusDetails).build(); 
			}
			catch(NumberFormatException nfe) 
			{
				result = "invalid";
				logger.debug("Exit audio video publishing status of  users on invalid request::analytics.");
				return Response.status(Status.BAD_REQUEST).entity(result).build();    
			}
			catch(AViewException ae)
			{
				result = "Error during log. Possible reason(s) : 1. Unexpected data, 2. Unknown.";
				logger.debug("Exit audio video publishing status of  users on error durning log::analytics.");
				return Response.status(Status.BAD_REQUEST).entity(result).build();
			} 
		}
		else
		{
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
	}
	
	/**
	 * Function to get audio-video interacted users
	 * @param adminId
	 * @param lectureId
	 * @return
	 * @throws AViewException
	 */
	@RequestMapping(value = "/audiovideointeractedusers.html", method = RequestMethod.POST)
	@ResponseBody
	public static Response getAudioVideoInteractedUserbyLectureId(@RequestParam("adminId") Long adminId,
			@RequestParam("lectureId") Long lectureId) throws AViewException
	{	
		String result = new String();
		logger.debug("Enter audio video interacted users::analytics.");
		Lecture lecture = null;
		String errorMessage = null;
		User admin = null;
		Class aviewClass = null;
		Object resultObjectAdmin =  UserHelper.userValidCheck(Constant.ADMIN_ID,adminId);
		if(User.class.isInstance(resultObjectAdmin))
		{
			admin = (User)resultObjectAdmin;
		}
		else
		{
			errorMessage = resultObjectAdmin.toString();
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
		if(lectureId == null || lectureId.equals(" ")|| lectureId == 0)
		{
			lectureId = 0l;
		}
		Object resultObjectLecture =  LectureHelper.getLecture(lectureId);
		if(Lecture.class.isInstance(resultObjectLecture))
		{
			lecture = (Lecture)resultObjectLecture;
		}
		else
		{
			if(resultObjectLecture == null)
			{
				return Response.status(Status.BAD_REQUEST).entity("Entered lecture Id doesn't exist or not valid").build();
			}
			else
			{
				errorMessage = resultObjectLecture.toString();
				return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
			}
		}
		ArrayList usersAudioVideoInteractionDetails = new ArrayList();
		if(lecture != null)
		{
			Object validationMessageForLecture = validateLecture(lecture);
			if(validationMessageForLecture != null)
			{
				return Response.status(Status.BAD_REQUEST).entity(validationMessageForLecture.toString()).build();
			}
			try 
			{
				aviewClass = ClassHelper.getClass(lecture.getClassId());
				if(aviewClass != null && aviewClass.getStatusId() == StatusHelper.getActiveStatusId())
				{
					Object validationMessageForAdmin = ClassHelper.adminValidation(aviewClass, admin);
					if(validationMessageForAdmin != null)
					{
						return Response.status(Status.BAD_REQUEST).entity(validationMessageForAdmin.toString()).build();
					}
				}
				else
				{
					return Response.status(Status.BAD_REQUEST).entity("The class of the lecture id is not valid").build();
				}			
				int statusId = StatusHelper.getActiveStatusId();
				List audioVideoPublishingArray = AnalyticsDAO.getUserAudioVideoPublishingDetailsbyLectureId(lecture.getLectureId(), statusId);
				Object resultObjectResult =  getUserInteractionDetailsbyLectureIdReturnArrayList(adminId, lectureId, null);
				HashMap<String, String[]> userInteractionDetails = new HashMap<String, String[]>();
				String[] tmpAudioVideoInteractionArray = null;
				if(resultObjectResult instanceof HashMap)
				{
					userInteractionDetails = (HashMap<String, String[]>)resultObjectResult;
				}
				else
				{
					errorMessage = resultObjectResult.toString();
					return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
				}
				Object[] obj = null;
				HashMap<String, String[]> userAudioVideoInteractionDetails = new HashMap<String, String[]>();
				ArrayList userInteractionDetailsWithCount = new ArrayList();
				ArrayList tmpUserAudioVideoInteractionDetails = null;
				String key = null;
				if(audioVideoPublishingArray.size()>0)
				{
					for(Object audioVideoPublishingUser:audioVideoPublishingArray)
					{
						Object[] tmpAudioVideoPublishingUserArray =(Object[])audioVideoPublishingUser;
						if(userInteractionDetails != null || userInteractionDetails.size() > 0 )
						{
							for (Map.Entry<String, String[]> entry : userInteractionDetails.entrySet()) 
							{
							    key = entry.getKey();
							    tmpAudioVideoInteractionArray = entry.getValue();
							    if(tmpAudioVideoInteractionArray[1].equals(tmpAudioVideoPublishingUserArray[1]))
							    {
							    	if(userAudioVideoInteractionDetails.containsKey(tmpAudioVideoInteractionArray[0]))
							    	{
							    		if(tmpAudioVideoPublishingUserArray[2].equals(AUDIO_PUBLISH))
										{
								    		tmpAudioVideoInteractionArray[2] = AUDIO_PUBLISH_VALUE;
										}
										else if(tmpAudioVideoPublishingUserArray[2].equals(AUDIOVIDEO_PUBLISH))
										{
											tmpAudioVideoInteractionArray[2] = AUDIOVIDEO_PUBLISH_VALUE;
										}
							    	}
							    	else
							    	{
							    		tmpAudioVideoInteractionArray = new String[3];
										tmpAudioVideoInteractionArray[0] = tmpAudioVideoPublishingUserArray[0].toString();
										tmpAudioVideoInteractionArray[1] = tmpAudioVideoPublishingUserArray[1].toString();
										if(tmpAudioVideoPublishingUserArray[2].equals(AUDIO_PUBLISH))
										{
								    		tmpAudioVideoInteractionArray[2] = AUDIO_PUBLISH_VALUE;
										}
										else if(tmpAudioVideoPublishingUserArray[2].equals(AUDIOVIDEO_PUBLISH))
										{
											tmpAudioVideoInteractionArray[2] = AUDIOVIDEO_PUBLISH_VALUE;
										}
										userAudioVideoInteractionDetails.put(tmpAudioVideoPublishingUserArray[1].toString(), tmpAudioVideoInteractionArray);
							    	}
							    }
							}
						}
					}
					if(!(userAudioVideoInteractionDetails.isEmpty()))
					{
						for (Map.Entry<String, String[]> entry : userAudioVideoInteractionDetails.entrySet()) 
						{
						    key = entry.getKey();
						    tmpAudioVideoInteractionArray = entry.getValue();
						    tmpUserAudioVideoInteractionDetails = new ArrayList();
						    tmpUserAudioVideoInteractionDetails.add("userName: " +  tmpAudioVideoInteractionArray[1]);
						    tmpUserAudioVideoInteractionDetails.add("userId: " + tmpAudioVideoInteractionArray[0]);
						    tmpUserAudioVideoInteractionDetails.add("publishingStatus: " + tmpAudioVideoInteractionArray[2]);
						    usersAudioVideoInteractionDetails.add(tmpUserAudioVideoInteractionDetails);
						}
					}
				}
				else
				{
					Date startDate = TimestampUtils.removeTime(lecture.getStartDate());
					Date currentDate = TimestampUtils.removeTime(TimestampUtils.getCurrentDate());
					Date startTime = TimestampUtils.removeDateAndMillis(lecture.getStartTime());
					Date currentTime = TimestampUtils.removeDateAndMillis(TimestampUtils.getCurrentTimestamp());
					if(startDate.after(currentDate))
					{
						return Response.status(Status.OK).entity("Details are not available as this is a future lecture").build();
					}
					else if(startDate.equals(currentDate) && startTime.after(currentTime))
					{
						return Response.status(Status.OK).entity("Details are not available as this is a future lecture").build();
					}
					else
					{
						return Response.status(Status.BAD_REQUEST).entity("No users have attended this lecture").build();
					}
				}
				logger.debug("Exit audio video publishing status of  users on success::analytics.");
				return Response.status(Status.OK).entity(usersAudioVideoInteractionDetails).build(); 
			}
			catch(NumberFormatException nfe) 
			{
				result = "invalid";
				logger.debug("Exit audio video publishing status of  users on invalid request::analytics.");
				return Response.status(Status.BAD_REQUEST).entity(result).build();    
			}
			catch(AViewException ae)
			{
				result = "Error during log. Possible reason(s) : 1. Unexpected data, 2. Unknown.";
				logger.debug("Exit audio video publishing status of  users on error durning log::analytics.");
				return Response.status(Status.BAD_REQUEST).entity(result).build();
			} 
		}
		else
		{
			return Response.status(Status.BAD_REQUEST).entity(errorMessage).build();
		}
	}

	//getting the user and lecture details for lecture
	public static ArrayList getUserAndLectureDetailsForLecture(Long lectureId) throws AViewException, JsonParseException, JsonMappingException, IOException
	{
		Integer classRegistrationCount = null;
		List<User> attendedUserDetails = null;
		ArrayList<Object> resultList = new ArrayList<Object>();
		ArrayList registeredUserDetails = new ArrayList();
		//get the lecture details using lecture id
		Lecture lectureDetails = LectureHelper.getLecture(lectureId);
		//getting the status id from lecture details
		Integer statusId = lectureDetails.getStatusId();
		Long classId = lectureDetails.getClassId();
		
		//getting the attended user details using lecture id and status id
		attendedUserDetails = AnalyticsDAO.getAttendingUsersbyLectureId(lectureId, statusId);
		//checking attended user details is not null and its size not less than zero
		if((attendedUserDetails != null) && (attendedUserDetails.size() > 0))
		{
			//To get the foreign key details
			for(User user : attendedUserDetails)
			{
				UserHelper.populateFKNames(user);
			}
			//adding attended user details to array list
			resultList.add(attendedUserDetails);
		}
		else
		{
			//setting attended user details to zero
			resultList.add(0);
		}
		//getting the class registration count using class id
		classRegistrationCount = ClassRegistrationHelper.getClassRegistrationCount(classId);
		//checking class registration count not zero
		if(!classRegistrationCount.equals(0))
		{
			//adding class registration count to array list
			resultList.add(classRegistrationCount);	
		}
		else
		{
			//setting class registration count to zero
			resultList.add(0);
		}
		//get all the registered user
		List<ClassRegistration> classRegistrationList = ClassRegistrationHelper.getClassRegistersForClass(classId);
		//List<Object> peopleCountNameArrayList=new ArrayList();
		for(ClassRegistration classRegister:classRegistrationList)
		{
			registeredUserDetails.add(classRegister.getUser());
		}
		if(registeredUserDetails != null && registeredUserDetails.size()> 0)
		{
			resultList.add(registeredUserDetails);
		}
		else
		{
			resultList.add(0);
		}
		// get the userLogin Details to find the moderator
		List auditUserLoginList =  AnalyticsDAO.getUserLoginLogoutbyLectureId(lectureId, 0l, statusId);
		if(auditUserLoginList != null && auditUserLoginList.size() > 0)
		{
			Object[] auditUserDetails = (Object[])auditUserLoginList.get(0);
			Long auditUserId = (Long) auditUserDetails[0];
			User userDetails = UserHelper.getUser(auditUserId);
			String firstName = userDetails.getFname();
			String lastName = userDetails.getLname();
			String prodfessorName = firstName + lastName;
			resultList.add(prodfessorName);
		}
		else
		{
			resultList.add(0);
		}
		//peopleCount Details
		ArrayList<Object> peopleCountValues = new ArrayList<Object>();
		//Get peopleCount using lectureId
		List<PeopleCount> peopleCountDetails = PeopleCountDAO.getPeopleCount(lectureId);
		ArrayList<Object> peopleCountDetailsList = null;
		ArrayList<Object> peopleCountList = null;
		ArrayList<Object> peopleCountDataList = null;
		if(peopleCountDetails != null && peopleCountDetails.size()> 0)
		{
			for(PeopleCount pplCountObject:peopleCountDetails)
			{
				peopleCountDetailsList = new ArrayList<Object>();
				Timestamp pplCountTime = pplCountObject.getPeopleCountTimestamp();
				String pplCountData = pplCountObject.getPeopleCountData();
				if(pplCountData != null && pplCountData != "")
				{
					String[] pplCountDataArray = pplCountData.split(";");
					for(String pplCountDetails:pplCountDataArray)
					{
						peopleCountList = new ArrayList<Object>();
						String[] pplCountString = pplCountDetails.split("`");
						String pplCountUserName = pplCountString[0];
						pplCountUserName = pplCountUserName.replace("(", "");
						User user = UserHelper.getUserByUserName(pplCountUserName);
						String pplCountName = user.getFname()+ user.getLname();
						String pplCount = pplCountString[1];
						peopleCountList.add(pplCountName);
						peopleCountList.add(pplCount);
						peopleCountDetailsList.add(peopleCountList);
					}					
					peopleCountDataList = new ArrayList<Object>();
					peopleCountDataList.add(pplCountTime);
					peopleCountDataList.add(peopleCountDetailsList);
					peopleCountValues.add(peopleCountDataList);					
				}				
				else
				{
					peopleCountValues.add(null);
				}
				
			}
			resultList.add(peopleCountValues);
		}
		else
		{
			resultList.add(0);
		}
		return resultList;
	}	
}
