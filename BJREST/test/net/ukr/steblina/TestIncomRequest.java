package net.ukr.steblina;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Scanner;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.ukr.steblina.db.User;

public class TestIncomRequest {
	Client client;
	WebTarget wt;
	User user;
	long gameID;

	@Before
	public void setUp() throws Exception {
		client = ClientBuilder.newClient();
		client.property(ClientProperties.JSON_PROCESSING_FEATURE_DISABLE, true);
		wt=client.target("http://localhost:8080/BJREST");
		user=new User(1);
		gameID=100;
	}
	@After
	public void tearDown() throws Exception {
		client.close();
	}
	
	@Test
	public void testGetIndex() throws IOException {
		InputStream in = wt.request(MediaType.TEXT_HTML, MediaType.TEXT_PLAIN).get(InputStream.class);
		Scanner scanner = new Scanner(in);
		assertEquals("<!DOCTYPE html>",scanner.nextLine());
		scanner.close();
		in.close();
	}

	@Test
	public void testGetLoginFromId() {
		WebTarget wtUser=wt.path(String.valueOf(user.getId()));
		JsonReader jsonReader = Json.createReader(new StringReader(wtUser.request(MediaType.APPLICATION_JSON).get().readEntity(String.class)));
		JsonObject jsonObj = jsonReader.readObject();
		assertEquals("Jhon", jsonObj.getString("Name"));
	}

	@Test
	public void testSetBalance() {
		Double balance=user.getBalance();
		Double delta=10.5;
		WebTarget wtBal=wt.path(String.valueOf(user.getId())+"/refill/"+delta);
		Response res = wtBal.request().post(Entity.entity("", MediaType.APPLICATION_JSON));
		assertEquals(200, res.getStatus());
		user=new User(1);
		Double expected=balance+delta;
		Double actual=user.getBalance();
		assertEquals(expected, actual);
		wtBal=wt.path("0/refill/"+delta);
		res = wtBal.request().post(Entity.entity("", MediaType.APPLICATION_JSON));
		assertEquals(500, res.getStatus());
	}

	
	@Test
	public void testCreateGame() {
		Double bet = 100.0;
		WebTarget wtCGame=wt.path(String.valueOf(user.getId())+"/newgame/"+bet);
		JsonReader jsonReader = Json.createReader(new StringReader(wtCGame.request(MediaType.APPLICATION_JSON).get().readEntity(String.class)));
		JsonObject jsonObj = jsonReader.readObject();
		gameID=Long.parseLong(jsonObj.getString("gameID"));
		
		assertNotEquals(-1, gameID);
		bet=-100.0;
		wtCGame=wt.path(String.valueOf(user.getId())+"/newgame/"+bet);
		jsonReader = Json.createReader(new StringReader(wtCGame.request(MediaType.APPLICATION_JSON).get().readEntity(String.class)));
		jsonObj = jsonReader.readObject();
		try {
			jsonObj.getString("Problem");
		} catch (NullPointerException e) {
			fail("Negative bet without problem");
		}
	}

	@Test
	public void testStartGame() {
		WebTarget wtCGame=wt.path(String.valueOf(user.getId())+"/"+gameID);
		JsonReader jsonReader = Json.createReader(new StringReader(wtCGame.request(MediaType.APPLICATION_JSON).get().readEntity(String.class)));
		JsonObject jsonObj = jsonReader.readObject();	
		try {
			jsonObj.getString("Problem");
			fail("Problem must be NULL");
		} catch (NullPointerException e) {}
	}

	@Test
	public void testHit() {
		WebTarget wtCGame=wt.path(String.valueOf(user.getId())+"/"+gameID+"/hit");
		JsonReader jsonReader = Json.createReader(new StringReader(wtCGame.request(MediaType.APPLICATION_JSON).post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE)).readEntity(String.class)));
		JsonObject jsonObj = jsonReader.readObject();
		try {
			jsonObj.getString("Problem");
			fail("Problem must be NULL");
		} catch (NullPointerException e) {}
	}

	@Test
	public void testStand() {
		WebTarget wtCGame=wt.path(String.valueOf(user.getId())+"/"+gameID+"/hit");
		JsonReader jsonReader = Json.createReader(new StringReader(wtCGame.request(MediaType.APPLICATION_JSON).post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE)).readEntity(String.class)));
		JsonObject jsonObj = jsonReader.readObject();
		try {
			jsonObj.getString("Winner");
		} catch (NullPointerException e) {
			fail("Null winner");
		}
		try {
			jsonObj.getString("Problem");
			fail("Problem must be NULL");
		} catch (NullPointerException e) {}
	}

}
