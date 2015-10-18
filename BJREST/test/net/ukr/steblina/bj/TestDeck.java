package net.ukr.steblina.bj;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestDeck {
	Deck deck1,deck2;

	@Before
	public void setUp() throws Exception {
		deck1=new Deck();
		deck2=new Deck();
	}

	@Test
	public void testDeck() {
		assertEquals(52, deck1.deck.size());
		assertEquals(52, deck2.deck.size());
		assertFalse(deck1.equals(deck2));
	}

}
