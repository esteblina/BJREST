package net.ukr.steblina.db;

import static org.junit.Assert.*;

import javax.json.JsonObject;

import org.junit.Before;
import org.junit.Test;

public class TestUser {
	User user1, user2, user3;

	@Before
	public void setUp() throws Exception {
		user1=new User(0);
		user2=new User(1);
		user3=new User(2);

		
	}

	@Test
	public void testUser() {
	
		assertEquals(0, user1.getId());
		assertNull(user1.getName());

		assertEquals(1, user2.getId());
		assertEquals("Jhon", user2.getName());
	}

	@Test
	public void testGetJsonObject() {
		JsonObject json = user2.getJsonObject();
		assertNotNull(json);
		String name = json.getString("Name");
		assertEquals("Jhon", name);
	}

	@Test
	public void testRefill() {
		double balance = user3.getBalance();
		user3.refill(100.5);
		Double expected = balance + 100.5;
		user3 = new User(2);
		Double actual = user3.getBalance();
		assertEquals(expected, actual);
	}
	
	

}
