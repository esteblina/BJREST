package net.ukr.steblina.bj;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TestCard {
	private final String[] ranks={"2","3","4","5","6","7","8","9","10","Jack","Queen","King","Ace"};
	private final String color="Spades";
	Card card1, card2, card3;
	List<Card> cards= new ArrayList<Card>();
	
	@Before
	public void setUp() throws Exception {
		for(String rank : ranks){
			cards.add(new Card(rank, color));
		}
	}

	@Test
	public void testCard() {
		
		assertEquals(ranks.length, cards.size());
		for(int i=0; i<ranks.length;i++){
			assertEquals(color, cards.get(i).color);
			assertEquals(ranks[i], cards.get(i).rank);
		}
	}

	@Test
	public void testToString() {
		card1=cards.get(0);
		assertNotNull(card1.toString());
	}

	@Test
	public void testEqualsCard() {
		card1=cards.get(0);
		card2=cards.get(1);
		card3=cards.get(0);
		assertFalse(card1.equals(card2));
		assertTrue(card1.equals(card3));
	}

}
