package net.ukr.steblina.bj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.ukr.steblina.db.DBFactory;

public class TestGame {
	long gameId,gameId2, userId;
	double bet,bet2;
	Deck deck,deck2,deck3;
	static Connection connection=DBFactory.getConnection();
	Game game,game2,game3;
	CardsInHandPlayer playersCards,playersCards2;
	CardsInHandDealer dealersCards, dealersCards2, dealersCards3;

	@Before
	public void setUp() throws Exception {
		userId=1;
		bet=100.0;
		bet2=200.5;
		game =new Game(userId,bet);
		game2 =new Game(userId,bet2);
		game3 =new Game(userId,-100.5);
		gameId=game.createGame();
		gameId2=game2.createGame();
		deck=game.deck;
		deck2=Game.getDeckById(gameId, userId);
		deck3=new Deck();
		playersCards = new CardsInHandPlayer();	
		dealersCards= new CardsInHandDealer();
		dealersCards3= new CardsInHandDealer();
		playersCards.addCard(new Card("2", "Clubs"));
		dealersCards.addCard(new Card("7","Diamonds"));
		dealersCards.addCard(new Card("10","Diamonds"));
		dealersCards3.addCard(new Card("6","Diamonds"));
		dealersCards3.addCard(new Card("8","Diamonds"));
		Game.updateGame(gameId, "test", "none", playersCards, dealersCards, deck);
	}
	
	@Test
	public void testCreateGame() throws SQLException {
		
		assertEquals("Bet must be positive", -1, game3.createGame());
		assertNotEquals("Unique game ID", gameId, gameId2);
		
	}
	
	@Test	
	public void testGetDeckById(){
		assertTrue("Game's deck is't static", deck.equals(deck2));
		assertFalse("Game's deck is't shuffled", deck.equals(deck3));
	}
	
	@Test(expected = Exception.class)
	public void testGetCardsById(){

		playersCards2 = (CardsInHandPlayer) Game.getCardsById(gameId, Game.PLAYER);
		dealersCards2 = (CardsInHandDealer) Game.getCardsById(gameId, Game.PLAYER);
		assertTrue(playersCards.equals(playersCards2));
	}
	
	@Test	
	public void testUpdateGame(){
		
		playersCards2 = (CardsInHandPlayer) Game.getCardsById(gameId, Game.PLAYER);
		dealersCards2 = (CardsInHandDealer) Game.getCardsById(gameId, Game.DEALER);
		assertTrue(playersCards.equals(playersCards2));
		assertTrue(dealersCards.equals(dealersCards2));
		assertTrue(deck.equals(deck2));	
	}
	
	@Test	
	public void testGetWinner(){
		assertEquals("Player win", 1, Game.getWinner(21, 20));
		assertEquals("Player win", 1, Game.getWinner(22, 22));
		assertEquals("Dealer win", 2, Game.getWinner(23, 20));
		assertEquals("Push", 0, Game.getWinner(17, 17));
	}
	
	@Test	
	public void testDealerGame(){
		Game.dealerGame(dealersCards, deck);
		assertEquals(2, dealersCards.showCards().size());
		assertEquals(17, dealersCards.getPoints());
		Game.dealerGame(dealersCards3, deck);
		assertNotEquals(2, dealersCards3.showCards().size());
	}
	
	 @After
     public void tearDown() throws Exception {
		game =null;
		game2 =null;
		game3 =null;
	    connection.close();
     }
}
