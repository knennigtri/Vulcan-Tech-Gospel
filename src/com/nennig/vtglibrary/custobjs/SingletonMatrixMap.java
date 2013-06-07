/**
 * This class is for parsing and holding the information for the different poi moves. This class allows for parsing of the csv file 
 * and then easily referencing for the detail view of each poi move.
 */

package com.nennig.vtglibrary.custobjs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.nennig.constants.AppConfig;

import android.content.Context;
import android.util.Log;

public class SingletonMatrixMap {
	private static final String DB_FILE = "detail_matrix_db.csv";
	private static final String TAG = AppConfig.APP_TITLE_SHORT + ".SingletonMatrixMap";
	private static SingletonMatrixMap ref;
	/**
	 * Holds all PropMove objects that are parsed from the db file
	 */
	public static Map<String, PropMove> poiMoveMap = new HashMap<String, PropMove>();
	/**
	 * Map of all the header indexes for the db file
	 */
	public static Map<String, Integer> headerIndexMap = new HashMap<String, Integer>();
	
	private static Thread parserThread;
	
	//For debugging dbFile ONLY-------
	public static void main(String[] args) {
		String location = "D:\\My Documents\\workspace\\Vulcan Tech Gospel\\assets\\" + DB_FILE;
		final SingletonMatrixMap obj = SingletonMatrixMap.getSingletonPoiMoveMap(location);
	}
	
	public static SingletonMatrixMap getSingletonPoiMoveMap(final Object obj){
		if(ref == null){
			parserThread = new Thread(new Runnable() {
		        public void run() {
		        	ref = new SingletonMatrixMap(obj);	        	
		        }
		    });
			parserThread.start();
		}
		return ref;
	}
	
	public static boolean ThreadFinished(){
		return parserThread.isAlive();
	}
	
	/**
	 * Creating a factory object so that it can be parsed from Context or from String. *Strictly for debugging purposes.
	 * @param obj
	 */
	private SingletonMatrixMap(Object obj){
		if(obj != null){
			if(obj instanceof Context)
				createSingleton((Context) obj);
			if(obj instanceof String)
				createSingleton((String) obj);
		}
	}
	
	private void createSingleton(String str){
		try{
			BufferedReader bR = new BufferedReader(new FileReader(str));
			parseDBInfo(bR);
		}catch (Exception e) {
			Log.d(TAG, "createSingleton(str) Error: " +  e.getMessage());
		}
	}
	private void createSingleton(Context c){
		try{
			InputStream iS = c.getAssets().open(DB_FILE);
			InputStreamReader iSR = new InputStreamReader(iS);
			BufferedReader bR = new BufferedReader(iSR);
			parseDBInfo(bR);
		}catch (Exception e) {
			Log.d(TAG, "createSingleton(c) Error: " + e.getMessage());
		}
	}
	
	/**
	 * This method parses the csv file and puts it into a hashmap of PoiMoves Objects
	 * @param bR input reader
	 */
	private void parseDBInfo(BufferedReader bR){
		boolean readHeaders = true;
		try {
		      String nextLineStr;
		      String[] nextLineParse;
		      
		      while ((nextLineStr = bR.readLine()) != null) {
//		    	  Log.d(TAG, "LINE: " + nextLineStr);
		    	  nextLineParse = nextLineStr.split(",");
	    		  
		    	  if(readHeaders){
		    		  getheaderIndexes(nextLineParse);
		    		  readHeaders = false;
		    	  }
		    	  else
		    	  {
			    	  PropMove pM = parsePoiMoveLine(nextLineParse);
//			    	  Log.d(TAG, "PM: " + pM.toString());
		    		  poiMoveMap.put(pM.moveID, pM);
		    	  }
		      }
		} catch (Exception e) {
			Log.d(TAG, "Exception: " + e.toString());
		}finally{
			 Log.d(TAG,"Finished Parse");
		}
	}
	
	/**
	 * This parses each line of the db and then creates the PropMove
	 * @param nextLineParse - db Line to be parsed
	 * @return - new PropMove
	 */
	private PropMove parsePoiMoveLine(String[] nextLineParse) {
		PropMove pM = new PropMove();  
		for(int i = 0; i < nextLineParse.length; i++)
		{
			for(Field field : pM.getClass().getFields()){
				if(headerIndexMap.get(getDBColumnName(field.getName())).equals(i))
				{
					pM.add(field.getName(),nextLineParse[i]);
					break;
				}
			}
		}
		return pM;
	}

	/**
	 * This parses the fields in PropMove according to the db standard and then puts the header names into the indexMap
	 */
	private void createIndexMap(){
		PropMove pm = new PropMove();
		//iterates through all fields in the PropMove class so that the indexes for each field can be found in the db file
		for(Field field : pm.getClass().getFields()){
			String columnName = getDBColumnName(field.getName());
			if(!columnName.equals(""))
				headerIndexMap.put(columnName,-1);
		}
	}
	
	/**
	 * Simple String modifier to create the proper readable db headers from the PropMove class fields
	 * @param fieldName - field name from PropMove class that will used to create the db header field
	 * @return - db header field
	 */
	private String getDBColumnName(String fieldName){
		if(fieldName.toLowerCase().equals("moveID".toLowerCase())) //checks for UID in the db file
			return fieldName.toLowerCase();
		else
		{
			String[] split1 = fieldName.split("_");
			if(split1.length > 1) //Make sure it parsed correctly
			{
				String moveSet = split1[0];
				if(moveSet.length() > 2) //Checks for the identifier of "m" and then the number to identify the type of move "11","13","15"
				{
					String moveColumn = split1[1];
					String columnName = moveSet.charAt(1) + ":" + moveSet.charAt(2) +
							"<" + moveColumn.toLowerCase() + ">";
					return columnName;
				}
			}
			else
				Log.d(TAG, "Field: '" +fieldName+ "' could not parse because token: '_' was found.");
		}
		return "";
	}
	
	/**
	 * Takes in the header line and then assigns all PropMove fields to an associated index in the db file.
	 * @param nextLineParse
	 */
	private void getheaderIndexes(String[] nextLineParse) {
		createIndexMap();
		for(int i = 0; i < nextLineParse.length; i++){
			if(headerIndexMap.containsKey(nextLineParse[i].toLowerCase()))
				headerIndexMap.put(nextLineParse[i].toLowerCase(),i);
		}
		Log.d(TAG,"Headers: " + headerIndexMap.toString());
	}

	public PropMove getPoiMove(String moveID){
		return poiMoveMap.get(moveID.toLowerCase());
	}
	
	@Override
	public String toString(){
		String str = "{";
		for(PropMove pm : poiMoveMap.values()){
			str = str + "; " + pm.moveID;
		}
		str = str + "}";
		return str;
	}
}
