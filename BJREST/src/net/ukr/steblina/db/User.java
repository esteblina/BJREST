package net.ukr.steblina.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class User {
	private long id;
	

	private String name;
	private double balance;
	private Date added;
	
	public User(long id){
		init(id);
	}
	
	private void init(long id){
		Connection connection = DBFactory.getConnection();
		ResultSet rs = null;
		
		try {
			rs = connection.prepareStatement("select name, balance, added from users where id='"+id+"';").executeQuery();
			while(rs.next()){
			this.id=id;
			name=rs.getString(1);
			balance=rs.getDouble(2);
			added=rs.getDate(3);
			}
			
			connection.close();
		} catch (SQLException e) {
			System.err.println("SQL Problem: "+ e.getMessage());
		}
	}
	
	public String getName() {return this.name;}
	public long getId() {return this.id;}
	public double getBalance() {return this.balance;}
	
	public JsonObject getJsonObject(){
		JsonObjectBuilder json = Json.createObjectBuilder();
		json.add("ID", id);
		json.add("Name", name);
		json.add("Balance", balance);
		json.add("Created date", added.toLocalDate().toString());
		return json.build();
	}
	
	public int refill(double sum){
		this.balance+=sum;
		int result=0;
		Connection connection = DBFactory.getConnection();
		try{
			Statement st=connection.createStatement();
			result=st.executeUpdate("UPDATE users SET balance="+this.balance+" WHERE ID="+this.id);
			Timestamp date = new Timestamp(new java.util.Date().getTime());
			PreparedStatement pst = connection.prepareStatement("INSERT INTO BALANCE_LOG (userID, bal, type, datetime) VALUES (?,?,'refill',?)");
			pst.setLong(1, id);
			pst.setDouble(2, sum);
			pst.setTimestamp(3, date);
			pst.execute();
			st.close();
			pst.close();
			connection.close();
			return result;
		} catch (SQLException e) {
			
			e.printStackTrace();
			return -1;
		}
	}
}
