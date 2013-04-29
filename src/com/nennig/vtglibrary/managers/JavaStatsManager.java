package com.nennig.vtglibrary.managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import logging.Log;

public class JavaStatsManager extends StatsManager {
	
	/**
	 * These should not be in the final codebase (and obviously not in the repo).
	 * If they are, someone is gonna get punched.
	 */
	private final String USERNAME = "you fill it in";
	private final String PASSWORD = "not telling!";
	
	private final String TAG = "JavaStatsManager"; 
	private Connection conn;

	private JavaStatsManager() {
		
	}
	
	@Override
	protected void connect() throws SQLException, ClassNotFoundException{
		Log.d(TAG, "connect");
		if(conn == null) {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
			     "jdbc:mysql://host306.hostmonster.com:3306/noelyeec_vtg",
			     USERNAME,
			     PASSWORD );
		}
		Log.d(TAG, "Connection Successful!");
	}
	
	@Override
	public void disconnect() {
		// TODO Auto-generated method stub

	}
	
	private static class SingletonHolder {
		public static final JavaStatsManager INSTANCE = new JavaStatsManager();
	}
	
	public static StatsManager getInstance() {
		
		return SingletonHolder.INSTANCE;
	}

}
