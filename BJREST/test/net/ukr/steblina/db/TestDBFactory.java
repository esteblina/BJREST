package net.ukr.steblina.db;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestDBFactory {
	Connection connection;

	@Before
	public void setUp() throws Exception {
		connection = DBFactory.getConnection();
	}

	@After
	public void tearDown() throws Exception {
		connection.close();
	}

	@Test
	public void testGetConnection() throws SQLException {
		assertNotNull(connection);
		assertFalse(connection.isClosed());
	}

}
