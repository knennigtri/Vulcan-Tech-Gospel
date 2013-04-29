package com.nennig.vtglibrary.managers;

import java.sql.SQLException;

import logging.Log;

/**
 * @author Kevin Nennig (knennig213@gmail.com)
 *
 */
public abstract class StatsManager {

	private final String TAG = "StatsManager"; 
	
	/**
	 * This is not how we should actually control the environment.
	 * You'll want to probably use a property file, or some other way to
	 * detect the android/java environment in your final code.
	 */
	private static final String ENVIRONMENT = "JAVA";
	

	/**
	 * This is the only public method for this class. This will take in information from the client, connect, 
	 * insert, and close the connection to the server.
	 * @return
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public void send(String userID, String appVersion, String appType, 
			String moveSet, String moveID, StatType type) throws ClassNotFoundException, SQLException {
		//The only value you need to dynamically create to insert into the table is the DATE, everything else will
		//be added through the parameters
		
		connect();
		Log.d(TAG, "Sent data successfully!");
		//Insert statement for the Stat Table
		
	}
	
	/**
	 * Implementations of connect() should set up the connection once and then
	 * re-use that connection (lazy-loading).  We should only need one connection
	 * per app, but if we thread it (which we *should* probably do), then we
	 * may want to set up some sort of connection pooling
	 * (possibly associate them with the threads residing in a thread pool?)
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	abstract protected void connect() throws SQLException, ClassNotFoundException;
	
	/**
	 * disconnect() is public so that it can be called as a cleanup when the
	 * app closes.  We don't want to be doing this everytime we send a stat.
	 */
	abstract public void disconnect();
	
	
	
	/**
	 * Stuff to take care of the singleton issues
	 * Likely, we'll change this to get a singleton instance of one of the subclasses instead.
	 * @author tredontho
	 *
	 */
	
	public static StatsManager createInstance() {
		if(ENVIRONMENT.equals("JAVA")) {
			return JavaStatsManager.getInstance();
		} else {
			return AndroidStatsManager.getInstance();
		}
		
	}
	
	
}
