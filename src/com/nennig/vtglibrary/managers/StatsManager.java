/**
 * 
 */
package com.nennig.vtglibrary.managers;

/**
 * @author Kevin Nennig (knennig213@gmail.com)
 *
 */
public class StatsManager {
	/**
	 * Dummy values that I will integrate to the class
	 */
	private static String APP_VERSION = "1";
	private static String APP_TYPE = "lite";
	
	
	/**
	 * These are the values that would be inserted for StatType
	 */
	public enum StatType {
		DETAILS_PAGE,
		VIDEOS_PAGE
	}
	
	/**
	 * This method is used when inserting on the server
	 * @param type
	 * @return
	 */
	private static String getStatTypeEnumString(StatType type){
		switch (type){
			case DETAILS_PAGE:
				return "details.page";
			case VIDEOS_PAGE:
				return "videos.page";
			default:
				return "error";
		}
	}
	
	/**
	 * This is the only public method for this class. This will take in information from the client, connect, 
	 * insert, and close the connection to the server.
	 * @return
	 */
	public static int send(String userID, String appVersion, String appType, 
			String moveSet, String moveID, StatType type){
		//The only value you need to dynamically create to insert into the table is the DATE, everything else will
		//be added through the parameters
		
		connect();
		
		//Insert statement for the Stat Table
		
		disconnect();
		return -1;
	}
	
	private static void connect(){
		
	}
	
	private static void disconnect(){
		
	}
	

}
