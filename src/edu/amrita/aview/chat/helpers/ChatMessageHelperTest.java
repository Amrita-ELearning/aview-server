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

import edu.amrita.aview.chat.entities.ChatMessage;
import edu.amrita.aview.common.AViewException;


/**
 * The Class ChatMessageHelperTest.
 */
public class ChatMessageHelperTest {

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
	 * Test create chat message.
	 *
	 * @throws AViewException
	 */
	@Ignore
	public void testCreateChatMessage() throws AViewException{
		ChatMessage cm = new ChatMessage() ;
		cm.setChatMessageText("AMMA HARI OM "+System.currentTimeMillis()) ;
		//cm.setChatSession(ChatSessionHelper.getChatSessionById(1l)) ;
		ChatMessageHelper.createChatMessage(cm, creatorId_modifierId) ;
		
		assertEquals("Didnot create chat message", cm.getChatMessageId(), cm.getChatMessageId()) ;
	}

	/**
	 * Test get chat message by session id.
	 *
	 * @throws AViewException
	 */
	@Test
	public void testGetChatMessageBySessionId() throws AViewException {
		List<ChatMessage> cmList = ChatMessageHelper.getChatMessagesBySessionId(1l) ;		
		assertEquals("Didnot get all chat messages", 3l, cmList.size()) ;
	}

	/**
	 * Test delete chat message.
	 */
	@Ignore
	public void testDeleteChatMessage() {
		fail("Not yet implemented");
	}

	/**
	 * Test delete chat messages.
	 */
	@Ignore
	public void testDeleteChatMessages() {
		fail("Not yet implemented");
	}

	/**
	 * Test delete chat message by session id.
	 */
	@Test
	public void testDeleteChatMessageBySessionId() {
		fail("Not yet implemented");
	}

}
