package net.ukr.steblina;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletContext;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hsqldb.types.Types;

import net.ukr.steblina.bj.Card;
import net.ukr.steblina.bj.CardsInHandDealer;
import net.ukr.steblina.bj.CardsInHandPlayer;
import net.ukr.steblina.bj.Deck;
import net.ukr.steblina.bj.Game;
import net.ukr.steblina.db.DBFactory;
import net.ukr.steblina.db.User;

@Path("/")				
public class IncomRequest {
	@Context 
	ServletContext servletContext;
	
	@GET
	@Produces({MediaType.TEXT_HTML})
	public InputStream getIndex() {
		return servletContext.getResourceAsStream("/WEB-INF/index.html"); 
	}
	
	@GET
	@Path("/{userId}")
	public JsonObject getLoginFromId(@PathParam("userId") Long userId) {
		User user = new User(userId);
		if(user.getName()==null)
			return  returnJsonInfo("Problem", "User does not exist").build();
		JsonObject json =user.getJsonObject();
		
		//System.out.println(json);
		return json;
		
	}
	
	@POST
	@Path("/{userId}/refill/{sum}")
	public Response setBalance(@PathParam("userId") long userId,@PathParam("sum") double sum) {
		User user =new User(userId);
		int result=user.refill(sum);
		if(result==1)
		return Response.status(200).build();
		
		return Response.status(500).build();
	}
	
	@GET
	@Path("/{userId}/newgame/{bet}")
	public JsonObject createGame(@PathParam("userId") long userId,@PathParam("bet") double bet) {
		Game game =new Game(userId, bet);
		Long gameID=game.createGame();
		if(gameID==-1)
			return returnJsonInfo("Problem", "You dont have enough money!").build();
		return returnJsonInfo("gameID", gameID.toString()).build();
	}
	
	@GET
	@Path("/{userId}/{gameId}")
	public JsonObject startGame(@PathParam("userId") long userId,@PathParam("gameId") long gameID) {
		Deck deck=null;
		try{
			deck= Game.getDeckById(gameID, userId);
		}catch(ForbiddenException e){
			return returnJsonInfo("Problem", "It is not your game").build();
		}
		CardsInHandPlayer playersCards = (CardsInHandPlayer) Game.getCardsById(gameID, Game.PLAYER);//new CardsInHandPlayer();
		CardsInHandDealer dealersCards = (CardsInHandDealer) Game.getCardsById(gameID, Game.DEALER);//new CardsInHandDealer();
		
		if(deck==null){
			return returnJsonInfo("Problem", "Game does not exist").build();
		}
		if(playersCards==null||dealersCards==null){
			playersCards = new CardsInHandPlayer();	
			dealersCards= new CardsInHandDealer();
			playersCards.addCard(deck.deck.remove(0));
			dealersCards.addCard(deck.deck.remove(0));
			playersCards.addCard(deck.deck.remove(0));
			dealersCards.addCard(deck.deck.remove(0));
			}
		
		try {
			Connection connection = DBFactory.getConnection();
			PreparedStatement pst= connection.prepareStatement("UPDATE games SET status='started', playersCards=?, dealerCards=?, deck=?");
			pst.setObject(1, playersCards, Types.OTHER);
			pst.setObject(2, dealersCards, Types.OTHER);
			pst.setObject(3, deck, Types.OTHER);
			pst.execute();
			pst.close();
			connection.close();
		} catch (SQLException e) {
			System.err.println("Save game problem");
			e.printStackTrace();
		}
		
		return createJson(playersCards, dealersCards).build();
	}
	
	@POST
	@Path("/{userId}/{gameId}/hit")
	public JsonObject hit(@PathParam("userId") long userId,@PathParam("gameId") long gameID) {
		Deck deck=null;
		String status="started";
		String winner="none";
		try{
			deck= Game.getDeckById(gameID, userId);
		}catch(ForbiddenException e){
			return returnJsonInfo("Problem", "It is not your game").build();
		}
		CardsInHandPlayer playersCards = (CardsInHandPlayer) Game.getCardsById(gameID, Game.PLAYER);//new CardsInHandPlayer();
		CardsInHandDealer dealersCards = (CardsInHandDealer) Game.getCardsById(gameID, Game.DEALER);//new CardsInHandDealer();
		
		if(deck==null){
			return returnJsonInfo("Problem", "Game does not exist").build();
		}
		int pPoints =playersCards.getPoints();
		if(pPoints<21){
			playersCards.addCard(deck.deck.remove(0));
		}
		pPoints =playersCards.getPoints();
		if(pPoints>=21){
			status="Dealer Game";
			Game.dealerGame(dealersCards, deck);
			switch(Game.getWinner(playersCards.getPoints(), dealersCards.getPoints())){
			case Game.PUSH:
				winner="PUSH";
				status="Ended";
				Game.getWin(userId, gameID, 1);
				break;
			case Game.PLAYER_WIN:
				winner="Player";
				status="Ended";
				if(playersCards.getPoints()==21)
					Game.getWin(userId, gameID, 2.5);
				else
					Game.getWin(userId, gameID, 2);
				break;
			case Game.DEALER_WIN:
				winner="Dealer";
				status="Ended";
				break;
			}
			Game.updateGame(gameID, status, winner, playersCards, dealersCards, deck);
			return createJson(playersCards, dealersCards).add("Winner", winner).build();
		}else{
			Game.updateGame(gameID, status, winner, playersCards, dealersCards, deck);
			return createJson(playersCards, dealersCards).build();
		}
		
	}
	
	@POST
	@Path("/{userId}/{gameId}/stand")
	public JsonObject stand(@PathParam("userId") long userId,@PathParam("gameId") long gameID) {
		Deck deck=null;
		String status="started";
		String winner="none";
		try{
			deck= Game.getDeckById(gameID, userId);
		}catch(ForbiddenException e){
			return returnJsonInfo("Problem", "It is not your game").build();
		}
		CardsInHandPlayer playersCards = (CardsInHandPlayer) Game.getCardsById(gameID, Game.PLAYER);//new CardsInHandPlayer();
		CardsInHandDealer dealersCards = (CardsInHandDealer) Game.getCardsById(gameID, Game.DEALER);//new CardsInHandDealer();
		
		if(deck==null){
			return returnJsonInfo("Problem", "Game does not exist").build();
		}
		status="Dealer Game";
		Game.dealerGame(dealersCards, deck);
		switch(Game.getWinner(playersCards.getPoints(), dealersCards.getPoints())){
			case Game.PUSH:
				winner="PUSH";
				status="Ended";
				Game.getWin(userId, gameID, 1);
				break;
			case Game.PLAYER_WIN:
				winner="Player";
				status="Ended";
				if(playersCards.getPoints()==21)
					Game.getWin(userId, gameID, 2.5);
				else
					Game.getWin(userId, gameID, 2);
				break;
			case Game.DEALER_WIN:
				winner="Dealer";
				status="Ended";
				break;
		}
		Game.updateGame(gameID, status, winner, playersCards, dealersCards, deck);
		return createJson(playersCards, dealersCards).add("Winner", winner).build();
		
	}
	
	
	private JsonObjectBuilder createJson(CardsInHandPlayer playersCards, CardsInHandDealer dealersCards){
		JsonObjectBuilder json = Json.createObjectBuilder();
		JsonArrayBuilder jsonArr= Json.createArrayBuilder();
		JsonArrayBuilder jsonCardArr= Json.createArrayBuilder();
		for(Card card : playersCards.showCards()){
			jsonCardArr.add(Json.createObjectBuilder()
				.add("rank", card.rank)
				.add("color", card.color));
		}
		jsonArr.add(Json.createObjectBuilder().add("Cards", jsonCardArr)).add(Json.createObjectBuilder().add("Points", playersCards.getPoints()));
		
		json.add("Player", jsonArr);
		jsonArr = Json.createArrayBuilder();
		jsonCardArr= Json.createArrayBuilder();
		for(Card card : dealersCards.showCards()){
			jsonCardArr.add(Json.createObjectBuilder()
					.add("rank", card.rank)
					.add("color", card.color));
			}
			jsonArr.add(Json.createObjectBuilder().add("Cards", jsonCardArr)).add(Json.createObjectBuilder().add("Points", dealersCards.getPoints()));
		json.add("Dealer", jsonArr);

		return json;
	}
	
	private JsonObjectBuilder returnJsonInfo(String name, String message){
		JsonObjectBuilder json = Json.createObjectBuilder();
		json.add(name, message);
		return json;
	}
	
}
