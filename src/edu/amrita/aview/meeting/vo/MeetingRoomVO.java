/**
 * 
 */
package edu.amrita.aview.meeting.vo;

import java.util.List;

import edu.amrita.aview.gclm.entities.Class;
import edu.amrita.aview.gclm.entities.ClassRegistration;
import edu.amrita.aview.gclm.entities.Lecture;

/**
 * @author sethus
 *
 */
public class MeetingRoomVO 
{
	public MeetingRoomVO(List<Lecture> meetings, Class meetingRoom,List<ClassRegistration> meetingMembers) 
	{
		super();
		this.lecturesAC = meetings;
		this.meetingRoom = meetingRoom;
		this.meetingRoomMembers=meetingMembers;
	}
	private List<Lecture> lecturesAC = null;
	private Class meetingRoom = null;
	private List <ClassRegistration> meetingRoomMembers=null;
	
	/**
	 * @return meetingRoomMembers
	 */
	public List<ClassRegistration> getMeetingRoomMembers() {
		return meetingRoomMembers;
	}
	/**
	 * @param meetingRoomMembers
	 */
	public void setMeetingRoomMembers(List<ClassRegistration> meetingRoomMembers) {
		this.meetingRoomMembers = meetingRoomMembers;
	}
	/**
	 * @return the lecture
	 */
	public List<Lecture> getLecturesAC() {
		return lecturesAC;
	}
	/**
	 * @param lecture the lecture to set
	 */
	public void setLecturesAC(List<Lecture> meetings) {
		this.lecturesAC = meetings;
	}
	/**
	 * @return the meetingRoom
	 */
	public Class getMeetingRoom() {
		return meetingRoom;
	}
	/**
	 * @param meetingRoom the meetingRoom to set
	 */
	public void setMeetingRoom(Class meetingRoom) {
		this.meetingRoom = meetingRoom;
	}

}
