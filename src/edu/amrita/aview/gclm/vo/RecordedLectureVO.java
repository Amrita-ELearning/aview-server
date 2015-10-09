/*
 * 
 */
package edu.amrita.aview.gclm.vo;

import edu.amrita.aview.gclm.entities.Lecture;


/**
 * The Class RecordedLectureVO.
 */
public class RecordedLectureVO 
{	
	
	/** The lecture. */
	private Lecture lecture = null;
	
	/** The is moderator. */
	private String isModerator = null;
	
	/** The use new player. */
	private boolean useNewPlayer = true;
	
	/**
	 * Gets the lecture.
	 *
	 * @return the lecture
	 */
	public Lecture getLecture() {
		return lecture;
	}
	
	/**
	 * Sets the lecture.
	 *
	 * @param lecture the lecture to set
	 */
	public void setLecture(Lecture lecture) {
		this.lecture = lecture;
	}
	
	/**
	 * Gets the checks if is moderator.
	 *
	 * @return the isModerator
	 */
	public String getIsModerator() {
		return isModerator;
	}
	
	/**
	 * Sets the checks if is moderator.
	 *
	 * @param isModerator the isModerator to set
	 */
	public void setIsModerator(String isModerator) {
		this.isModerator = isModerator;
	}
	
	/**
	 * Gets the use new player.
	 *
	 * @return the videoPlayer
	 */
	public boolean getUseNewPlayer() {
		return useNewPlayer;
	}
	
	/**
	 * Sets the use new player.
	 *
	 * @param useNewPlayer the new use new player
	 */
	public void setUseNewPlayer(boolean useNewPlayer) {
		this.useNewPlayer = useNewPlayer;
	}
	
	
	
}
