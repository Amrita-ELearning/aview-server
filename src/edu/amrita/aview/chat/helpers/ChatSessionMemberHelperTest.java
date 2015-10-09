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

import edu.amrita.aview.chat.entities.ChatSessionMember;
import edu.amrita.aview.common.AViewException;


/**
 * The Class ChatSessionMemberHelperTest.
 */
public class ChatSessionMemberHelperTest {

	/** The creator id_modifier id. */
	private static Long creatorId_modifierId = 0l ;
	
	/** The member id. */
	private static Long memberId = new Long(0) ;
	
	/** The chat session id. */
	private static Long chatSessionId = 0l ;
	
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
	 * Test create chat session member.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testCreateChatSessionMember() throws AViewException 
	{
		memberId = new Long(12) ;
		chatSessionId = 3l ;
		ChatSessionMember csm = new ChatSessionMember() ;
		//csm.setMemberId(memberId) ;
		//csm.setChatSession(ChatSessionHelper.getChatSessionById(chatSessionId)) ;
		ChatSessionMemberHelper.createChatSessionMember(csm, creatorId_modifierId) ;
		
		assertEquals("Didnot create chat session member", csm.getChatSessionMemberId(), csm.getChatSessionMemberId()) ;
	}

	/**
	 * Test get chat session member by session id.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testGetChatSessionMemberBySessionId() throws AViewException
	{
		chatSessionId = 1l ;
		List<ChatSessionMember> csmList = ChatSessionMemberHelper.getChatSessionMembersBySessionId(chatSessionId) ;
		assertEquals("Didnot get all chat session members", 3l, csmList.size()) ;
	}

	/**
	 * Test delete chat session member.
	 */
	@Test
	public void testDeleteChatSessionMember() {
	
	}

	/**
	 * Test delete chat session members.
	 */
	@Ignore
	public void testDeleteChatSessionMembers() {
		fail("Not yet implemented");
	}

}
