package net.ukr.steblina.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.hsqldb.types.Types;

import net.ukr.steblina.bj.Card;

public class GameLog {
	
	
	public static Card updateLog(Card card, long gameID, long userID, String who, String operation){
		Connection connection =DBFactory.getConnection();
		
		try {
			PreparedStatement pst = connection.prepareStatement("INSERT INTO game_log "
					+ "(gameID, userID, card, who, operation, datetime) VALUES (?,?,?,?,?,?)");
			pst.setLong(1, gameID);
			pst.setLong(2, userID);
			pst.setObject(3, card, Types.OTHER);
			pst.setString(4, who);
			pst.setString(5, operation);
			Timestamp datetime = new Timestamp(new Date().getTime());
			pst.setTimestamp(6, datetime);
			pst.execute();
			pst.close();
			connection.close();
		} catch (SQLException e) {
			System.out.print("Can't save game logs");
		}
		
		return card;
	}
}
