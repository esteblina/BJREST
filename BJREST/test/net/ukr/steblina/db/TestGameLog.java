package net.ukr.steblina.db;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import net.ukr.steblina.bj.Card;
import net.ukr.steblina.bj.Game;

public class TestGameLog {
	Card card,card2;
	@Before
	public void setUp() throws Exception {
		card=new Card("2","Heard");
	}

	@Test
	public void testUpdateLog() {
		card2=GameLog.updateLog(card, 300, 1, Game.PLAYER, "Hit");
		
		assertEquals(card, card2);
	}

}
