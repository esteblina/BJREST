package net.ukr.steblina.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBFactory {

	private static Connection connection;
	
	public static Connection getConnection(){
		try {
			if(connection!=null&&!connection.isClosed())
				return connection;
			Class.forName("org.hsqldb.jdbcDriver");
			connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/xdb", "sa", "");
		}catch(ClassNotFoundException e1){
			System.err.println("DB Driver problem");
		}catch(SQLException e2){
			System.err.println("SQL Problem: "+ e2.getMessage());
		}
		
		return connection;
	}
}
