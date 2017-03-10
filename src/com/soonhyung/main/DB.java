package com.soonhyung.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;

public class DB {
	
	private static Connection conn;
	private static Statement stmt;
	private static ResultSet rs;
	private static Logger logger = Logger.getLogger(DB.class);

	private static void dbConnection() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			String url = "jdbc:oracle:thin:@soonhyung.gonetis.com:1521:XE";

			String user = "lotto";
			String pwd = "1234";

			conn = DriverManager.getConnection(url, user, pwd);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void dbClose() {
		try {
			if (conn != null) conn.close();
			if (stmt != null) stmt.close();
			if (rs != null) rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void iudQuery(String sql) {
		logger.info(sql);
		try {
			dbConnection();
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			dbClose();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int getLastInning(){
		int returnValue = 0;
		try {
			dbConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT NVL(MAX(INNING_NO), 1) FROM LOTTO_INNING_INFO");
			rs.next();
			returnValue = rs.getInt(1);
			dbClose();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnValue;
	}
}
