package net.ukr.steblina.bj;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class TestCardsInHandPlayer {
	CardsInHandPlayer playersCards,playersCards2,playersCards3;
	Deck deck;
	@Before
	public void setUp() throws Exception {
		playersCards=new CardsInHandPlayer();
		playersCards2=new CardsInHandPlayer();
		playersCards3=new CardsInHandPlayer();
		deck= new Deck();
		playersCards2.addCard(deck.deck.remove(0));
		
	}


	@Test
	public void testGetPoints() {
		playersCards.addCard(new Card("10", "Spades"));
		assertEquals(10, playersCards.getPoints());
		playersCards.addCard(new Card("Ace", "Spades"));
		assertEquals(21, playersCards.getPoints());
		playersCards.addCard(new Card("Ace", "Hearts"));
		playersCards.addCard(new Card("Ace", "Diamonds"));
		playersCards.addCard(new Card("Ace", "Clubs"));
		assertEquals(14, playersCards.getPoints());
	}

	@Test
	public void testShowCards() {
		assertEquals(1, playersCards2.showCards().size());
		playersCards2.addCard(deck.deck.remove(0));
		assertEquals(2, playersCards2.showCards().size());
		Card card= playersCards2.showCards().get(0);
		assertNotNull(card);
	}

	@Test
	public void testShowCardsStr() {
		assertNotNull(playersCards2.showCardsStr());
	}

	@Test
	public void testAddCard() {
		for(int i=0; i<10;i++)
		playersCards3.addCard(deck.deck.remove(0));
		
		assertEquals(10, playersCards3.showCards().size());
		
	}
	
	@Test
	public void testEquals(){
		playersCards=new CardsInHandPlayer();
		playersCards2=new CardsInHandPlayer();
		playersCards.addCard(new Card("10", "Spades"));
		playersCards2.addCard(new Card("10", "Spades"));
		assertTrue(playersCards.equals(playersCards2));
		playersCards2.addCard(new Card("9", "Spades"));
		assertFalse(playersCards.equals(playersCards2));
	}

}
