package net.ukr.steblina.bj;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestCardsInHandDealer {
	CardsInHandDealer dealersCards,dealersCards2,dealersCards3;
	Deck deck;
	
	@Before
	public void setUp() throws Exception {
		dealersCards=new CardsInHandDealer();
		dealersCards2=new CardsInHandDealer();
		dealersCards3=new CardsInHandDealer();
		deck= new Deck();
		dealersCards2.addCard(deck.deck.remove(0));
		
	}


	@Test
	public void testGetPoints() {
		dealersCards.addCard(new Card("10", "Spades"));
		assertEquals(10, dealersCards.getPoints());
		dealersCards.addCard(new Card("Ace", "Spades"));
		assertEquals(10, dealersCards.getPoints());
		dealersCards.addCard(new Card("Ace", "Hearts"));
		dealersCards.setVisible();
		dealersCards.addCard(new Card("Ace", "Diamonds"));
		dealersCards.addCard(new Card("Ace", "Clubs"));
		assertEquals(14, dealersCards.getPoints());
	}

	@Test
	public void testShowCards() {
		assertEquals(1, dealersCards2.showCards().size());
		dealersCards2.addCard(deck.deck.remove(0));
		assertEquals(1, dealersCards2.showCards().size());
		dealersCards2.addCard(deck.deck.remove(0));
		dealersCards2.setVisible();
		assertEquals(3, dealersCards2.showCards().size());
		Card card= dealersCards2.showCards().get(0);
		assertNotNull(card);
	}

	@Test
	public void testShowCardsStr() {
		assertNotNull(dealersCards2.showCardsStr());
	}

	@Test
	public void testAddCard() {
		for(int i=0; i<10;i++)
		dealersCards3.addCard(deck.deck.remove(0));
		
		assertEquals(1, dealersCards3.showCards().size());
		dealersCards3.setVisible();
		assertEquals(10, dealersCards3.showCards().size());
		
	}
	
	@Test
	public void testEquals(){
		dealersCards=new CardsInHandDealer();
		dealersCards2=new CardsInHandDealer();
		dealersCards.addCard(new Card("10", "Spades"));
		dealersCards2.addCard(new Card("10", "Spades"));
		assertTrue(dealersCards.equals(dealersCards2));
		dealersCards2.addCard(new Card("9", "Spades"));
		assertTrue(dealersCards.equals(dealersCards2));
		dealersCards2.setVisible();
		assertFalse(dealersCards.equals(dealersCards2));
	}

}
