package net.ukr.steblina.bj;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.ws.rs.ForbiddenException;

import org.hsqldb.types.Types;

import net.ukr.steblina.db.DBFactory;
import net.ukr.steblina.db.GameLog;

public class Game {
	/**
	 * 
	 */

	public static final String PLAYER="playersCards";
	public static final String DEALER="dealerCards";
	
	public static final int PUSH=0;
	public static final int PLAYER_WIN=1;
	public static final int DEALER_WIN=2;
	
	long gameId;
	long userId;
	double bet;
	public Deck deck;
	public Game(long userId, double bet){
		
		this.userId=userId;
		this.bet=bet;
		deck= new Deck();
	}
	
	public long createGame(){
		if(bet<1){
			return -1;
		}
		Connection connection = DBFactory.getConnection();
		try{
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery("SELECT balance FROM users WHERE id="+userId);
			while(rs.next())
				if(rs.getDouble(1)<bet)
					return -1;
			PreparedStatement prstInsert = connection.prepareStatement("INSERT INTO games (userID, Bank, status, deck) VALUES (?, ?, ?, ?)",  Statement.RETURN_GENERATED_KEYS);
			prstInsert.setLong(1, userId);
			prstInsert.setDouble(2, bet);
			prstInsert.setString(3, "created");
			prstInsert.setObject(4, deck, Types.OTHER);
			prstInsert.execute();
			rs=prstInsert.getGeneratedKeys();
			while(rs.next())
			 gameId=rs.getLong(1);
			
			prstInsert.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
			return -1;
		}
		try {
			Statement st = connection.createStatement();
			st.executeUpdate("UPDATE users SET balance=balance-"+bet+" WHERE ID="+userId);
			Timestamp date = new Timestamp(new java.util.Date().getTime());
			PreparedStatement pst = connection.prepareStatement("INSERT INTO BALANCE_LOG (userID, gameID, bal, type, datetime) VALUES (?,?,?,'bet',?)");
			pst.setLong(1, userId);
			pst.setLong(2, gameId);
			pst.setDouble(3, bet);
			pst.setTimestamp(4, date);
			pst.execute();
			pst.close();
			st.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	
		return gameId;
	}
	
	public static Deck getDeckById(long gameId, long userId) throws ForbiddenException{
		Connection connection = DBFactory.getConnection();
		Deck deck=null;
		try {
			Statement st=connection.createStatement();
			ResultSet rs =st.executeQuery("SELECT userID, deck FROM games WHERE id="+gameId);
			while(rs.next()){
				if(userId!=rs.getLong(1))
					throw new ForbiddenException();
				deck=(Deck)rs.getObject(2);
			}
			
		} catch (SQLException e) {
			return null;
		}
		
		return deck;
	}
	
	
	public static CardsInHand getCardsById(long gameId, String who){
		Connection connection = DBFactory.getConnection();
		CardsInHand cards=null;
		try {
			Statement st=connection.createStatement();
			ResultSet rs =st.executeQuery("SELECT "+who+" FROM games WHERE id="+gameId);
			while(rs.next())
				cards=(CardsInHand)rs.getObject(1);
			st.close();
			connection.close();
		} catch (SQLException e) {
			return null;
		}
		
		return cards;
	}
	
	public static void updateGame(long gameID, String status, String winner, CardsInHandPlayer playersCards, CardsInHandDealer dealersCards, Deck deck) throws Exception{
		Connection connection = DBFactory.getConnection();
		try {
			PreparedStatement prstUpdate = connection.prepareStatement("UPDATE games SET status='"+status+"', winner='"+winner+"', "+PLAYER+"=?, "+DEALER+"=?, deck=? WHERE ID="+gameID);
			prstUpdate.setObject(1, playersCards, Types.OTHER);
			prstUpdate.setObject(2, dealersCards, Types.OTHER);
			prstUpdate.setObject(3, deck, Types.OTHER);
			prstUpdate.execute();
			prstUpdate.close();
			connection.close();
		} catch (SQLException e) {
			throw new Exception("Can't save game"); 
		}
	}
	
	public static int getWinner(int pPoint, int dPoint){
		int result=PUSH;
		if((pPoint<=21)&(dPoint<=21)){
			if(pPoint>dPoint)
				result=PLAYER_WIN;
			else if(pPoint<dPoint)
				result=DEALER_WIN;
			else
				result=PUSH;
		}else if(dPoint>21)
			result=PLAYER_WIN;
		else if(pPoint>21&dPoint<=21)
			result=DEALER_WIN;
		
		return result;
	}
	
	public static void dealerGame(CardsInHandDealer dealersCards, Deck deck,long gameID, long userId){
		dealersCards.setVisible();
		int dPoints =dealersCards.getPoints();
		while(dPoints<17){
			dealersCards.addCard(GameLog.updateLog(deck.deck.remove(0), gameID, userId, Game.DEALER, "Dealer game"));
			dPoints=dealersCards.getPoints();
		}
	}

	public static void getWin(long userId, long gameID, double d) throws Exception {
		Connection connection = DBFactory.getConnection();
		try {
			Statement stUpdate = connection.createStatement();
			stUpdate.executeQuery("UPDATE USERS SET USERS.balance=USERS.balance+("+d+"*(SELECT bank FROM GAMES WHERE GAMES.ID="+gameID+")) WHERE USERS.ID="+userId);
			Timestamp date = new Timestamp(new java.util.Date().getTime());
			PreparedStatement pst = connection.prepareStatement("INSERT INTO BALANCE_LOG (userID, gameID, bal, type, datetime) VALUES (?,?,?*(SELECT bank FROM GAMES WHERE GAMES.ID="+gameID+"),'win',?)");
			pst.setLong(1, userId);
			pst.setLong(2, gameID);
			pst.setDouble(3, d);
			pst.setTimestamp(4, date);
			pst.execute();
			pst.close();
			stUpdate.close();
			connection.close();
		} catch (SQLException e) {
			throw new Exception("The problem with money transfer");
		}
		
	}
}
