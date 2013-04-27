/**
 * This is an implementation of the abstract StatsManager class specifically
 * for the Android environment
 */
package com.nennig.vtglibrary.managers;

import java.sql.SQLException;

/**
 * @author tredontho
 *
 */
public class AndroidStatsManager extends StatsManager {

	private AndroidStatsManager() {
		
	}


	@Override
	protected void connect() throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	private static class SingletonHolder {
		public static final AndroidStatsManager INSTANCE = new AndroidStatsManager();
	}
	
	public static StatsManager getInstance() {
		
		return SingletonHolder.INSTANCE;
	}

}
