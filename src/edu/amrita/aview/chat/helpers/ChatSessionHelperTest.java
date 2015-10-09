/*
 * 
 */
package edu.amrita.aview.chat.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.amrita.aview.chat.entities.ChatSession;
import edu.amrita.aview.chat.entities.ChatSessionMember;
import edu.amrita.aview.common.AViewException;
import edu.amrita.aview.common.utils.TimestampUtils;
import edu.amrita.aview.gclm.helpers.UserHelper;


/**
 * The Class ChatSessionHelperTest.
 */
public class ChatSessionHelperTest {

	/** The creator id_modifier id. */
	private static Long creatorId_modifierId = 0l ;
	
	/**
	 * Sets the up before class.
	 *
	 * @throws Exception the exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		creatorId_modifierId = 44l ;
	}

	/**
	 * Tear down after class.
	 *
	 * @throws Exception the exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		creatorId_modifierId = 0l ;
	}

	/**
	 * Test create chat session.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testCreateChatSession() throws AViewException 
	{
		ChatSession cs = new ChatSession() ;
		cs.setOwner(UserHelper.getUser(new Long(5))) ;
		cs.setTitle("Test "+System.currentTimeMillis() ) ;
		cs.setGroupId(new Long(7)) ;
		cs = ChatSessionHelper.createChatSession(cs, creatorId_modifierId) ;
		
		assertEquals("Couldnot create chat session", cs.getChatSessionId(), cs.getChatSessionId()) ;
		
	}

	/**
	 * Test get chat session by member id.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetChatSessionByMemberId() throws AViewException 
	{
		Long memberId = 12l ;
		List<ChatSession>  cs =  ChatSessionHelper.getChatSessionsByMemberId(memberId) ;
		assertEquals("Didnot get all chat sessions", 3l, cs.size()) ;
	}
	
	/**
	 * Test update chat session.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testUpdateChatSession() throws AViewException 
	{
		
		//ChatSession cs =  ChatSessionHelper.getChatSessionById(new Long(13));
		ChatSessionMember member=new ChatSessionMember();
		//member.setChatSession(cs);
		member.setCreatedByUserId(new Long(165));
		member.setModifiedByUserId(new Long(165));
		member.setModifiedDate(TimestampUtils.getCurrentTimestamp());
		member.setCreatedDate(TimestampUtils.getCurrentTimestamp());
		member.setStatusId(1);
		//member.setMemberId(new Long(630));
		
		//cs.getChatSessionMembers().add(member);
		//ChatSessionHelper.updateChatSession(cs);
	}
	
	/**
	 * Test delete chat session.
	 */
	@Ignore
	public void testDeleteChatSession() {
		fail("Not yet implemented");
	}

	/**
	 * Test delete chat sessions.
	 */
	@Ignore
	public void testDeleteChatSessions() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testGetChatSessionsByMemberId(){
		List<ChatSession> sessions = ChatSessionHelper.getChatSessionsByMemberId(554l);
		System.out.println(sessions.size());
	}

}
