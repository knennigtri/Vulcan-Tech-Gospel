/**
 * This class is for parsing and holding the information for the different poi moves. This class allows for parsing of the csv file 
 * and then easily referencing for the detail view of each poi move.
 */

package com.nennig.vtglibrary.managers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.nennig.constants.AppConfig;
import com.nennig.constants.AppConstants;

import android.content.Context;
import android.util.Log;

public class SingletonPoiMoveMap {
	private static final String DB_FILE = "db_v2.csv";
	private static final String TAG = AppConfig.APP_PNAME + ".singleton";
	private static SingletonPoiMoveMap ref;
	/**
	 * Holds all PoiMove objects that are parsed from the db file
	 */
	public static Map<String, PoiMove> poiMoveMap = new HashMap<String, PoiMove>();
	/**
	 * Map of all the header indexes for the db file
	 */
	public static Map<String, Integer> headerIndexMap = new HashMap<String, Integer>();
	
	//For debugging dbFile ONLY-------
	public static void main(String[] args) {
		String location = "D:\\My Documents\\workspace\\Vulcan Tech Gospel\\assets\\db_v2.csv";
		final SingletonPoiMoveMap obj = SingletonPoiMoveMap.getSingletonPoiMoveMap(location);
	}
	
	public static SingletonPoiMoveMap getSingletonPoiMoveMap(Object obj){
		if(ref == null){
			ref = new SingletonPoiMoveMap(obj);
		}
		return ref;
	}
	
	/**
	 * Creating a factory object so that it can be parsed from Context or from String. *Strictly for debugging purposes.
	 * @param obj
	 */
	private SingletonPoiMoveMap(Object obj){
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
			Log.d(TAG, "" + e.getMessage());
		}
	}
	private void createSingleton(Context c){
		try{
			InputStream iS = c.getAssets().open(DB_FILE);
			InputStreamReader iSR = new InputStreamReader(iS);
			BufferedReader bR = new BufferedReader(iSR);
			parseDBInfo(bR);
		}catch (Exception e) {
			Log.d(TAG, "" + e.getMessage());
		}
	}
	
	/**
	 * This method parses the csv file and puts it into a hashmap of PoiMoves Objects
	 * @param c
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
			    	  PoiMove pM = parsePoiMoveLine(nextLineParse);
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
	 * This parses each line of the db and then creates the PoiMove
	 * @param nextLineParse - db Line to be parsed
	 * @return - new PoiMove
	 */
	private PoiMove parsePoiMoveLine(String[] nextLineParse) {
		PoiMove pM = new PoiMove();  
		for(int i = 0; i < nextLineParse.length; i++)
		{
			for(Field field : pM.getClass().getFields()){
				if(headerIndexMap.get(getDBColumnName(field.getName())) == i)
				{
					pM.add(field.getName(),nextLineParse[i]);
					break;
				}
			}
		}
		return pM;
	}

	/**
	 * This parses the fields in PoiMove according to the db standard and then puts the header names into the indexMap
	 */
	private void createIndexMap(){
		PoiMove pm = new PoiMove();
		//iterates through all fields in the PoiMove class so that the indexes for each field can be found in the db file
		for(Field field : pm.getClass().getFields()){
			String columnName = getDBColumnName(field.getName());
			if(!columnName.equals(""))
				headerIndexMap.put(columnName,-1);
		}
	}
	
	/**
	 * Simple String modifier to create the proper readable db headers from the PoiMove class fields
	 * @param fieldName - field name from PoiMove class that will used to create the db header field
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
	 * Takes in the header line and then assigns all PoiMove fields to an associated index in the db file.
	 * @param nextLineParse
	 */
	private void getheaderIndexes(String[] nextLineParse) {
		createIndexMap();
		for(int i = 0; i < nextLineParse.length; i++){
			if(headerIndexMap.containsKey(nextLineParse[i].toLowerCase()))
				headerIndexMap.put(nextLineParse[i].toLowerCase(),i);
		}
		Log.d(TAG,headerIndexMap.toString());
	}

	public PoiMove getPoiMove(String moveID){
		return poiMoveMap.get(moveID.toLowerCase());
	}
	
	@Override
	public String toString(){
		String str = "{";
		for(PoiMove pm : poiMoveMap.values()){
			str = str + "; " + pm.moveID;
		}
		str = str + "}";
		return str;
	}
	
	public class PoiMove{
		public String moveID;
		public String m13_name;
		public String m13_image;
		public String m13_imageExt;
		public String m13_imageFileName;
		public String m13_type;
		public String m13_drawX;
		public String m13_drawY;
		public String m13_pdf;
		public String m13_youtubeStart;
		public String m13_youtubeEnd;
		
		public String m11_name;
		public String m11_image;
		public String m11_imageExt;
		public String m11_imageFileName;
		public String m11_type;
		public String m11_drawX;
		public String m11_drawY;

		public String getName(String curSet){
			if(curSet.equals(AppConstants.SET_1313)){
				return m13_name;
			}
			else if(curSet.equals(AppConstants.SET_1111))
			{
				return m11_name;
			}
			return m13_name;
		}
		public String getImage(String curSet){
			if(curSet.equals(AppConstants.SET_1313)){
				return m13_image;
			}
			else if(curSet.equals(AppConstants.SET_1111))
			{
				return m11_image;
			}
			return m13_image;
		}
		public String getImageExt(String curSet){
			if(curSet.equals(AppConstants.SET_1313)){
				return m13_imageExt;
			}
			else if(curSet.equals(AppConstants.SET_1111))
			{
				return m11_imageExt;
			}
			return m13_imageExt;
		}
		public String getImageFileName(String curSet){
			if(curSet.equals(AppConstants.SET_1313)){
				return m13_imageFileName;
			}
			else if(curSet.equals(AppConstants.SET_1111))
			{
				return m11_imageFileName;
			}
			return m13_imageFileName;
		}
		public String getType(String curSet){
			if(curSet.equals(AppConstants.SET_1313)){
				return m13_type;
			}
			else if(curSet.equals(AppConstants.SET_1111))
			{
				return m11_type;
			}
			return m13_type;
		}
		public String getDrawX(String curSet){
			if(curSet.equals(AppConstants.SET_1313)){
				return m13_drawX;
			}
			else if(curSet.equals(AppConstants.SET_1111))
			{
				return m11_drawX;
			}
			return m13_drawX;
		}
		public String getDrawY(String curSet){
			if(curSet.equals(AppConstants.SET_1313)){
				return m13_drawY;
			}
			else if(curSet.equals(AppConstants.SET_1111))
			{
				return m11_drawY;
			}
			return m13_drawY;
		}
		
		/**
		 * Huge adder that assigns the field value to the correct value.
		 * @param name
		 * @param fieldValue
		 */
		public void add(String name, String fieldValue) {
			if(name.toLowerCase().equals("moveid".toLowerCase()))
				moveID = fieldValue;
			else if(name.toLowerCase().equals("m13_name".toLowerCase()))
				m13_name = fieldValue;
			else if(name.toLowerCase().equals("m13_image".toLowerCase()))
				m13_image = fieldValue;
			else if(name.toLowerCase().equals("m13_imageExt".toLowerCase()))
				m13_imageExt = fieldValue;
			else if(name.toLowerCase().equals("m13_imageFileName".toLowerCase()))
				m13_imageFileName = fieldValue;
			else if(name.toLowerCase().equals("m13_type".toLowerCase()))
				m13_type = fieldValue;
			else if(name.toLowerCase().equals("m13_drawX".toLowerCase()))
				m13_drawX = fieldValue;
			else if(name.toLowerCase().equals("m13_drawY".toLowerCase()))
				m13_drawY = fieldValue;
			else if(name.toLowerCase().equals("m13_pdf".toLowerCase()))
				m13_pdf = fieldValue;
			else if(name.toLowerCase().equals("m13_youtubeStart".toLowerCase()))
				m13_youtubeStart = fieldValue;
			else if(name.toLowerCase().equals("m13_youtubeEnd".toLowerCase()))
				m13_youtubeEnd = fieldValue;
			
			
			else if(name.toLowerCase().equals("m11_name".toLowerCase()))
				m11_name = fieldValue;
			else if(name.toLowerCase().equals("m11_image".toLowerCase()))
				m11_image = fieldValue;
			else if(name.toLowerCase().equals("m11_imageExt".toLowerCase()))
				m11_imageExt = fieldValue;
			else if(name.toLowerCase().equals("m11_imageFileName".toLowerCase()))
				m11_imageFileName = fieldValue;
			else if(name.toLowerCase().equals("m11_type".toLowerCase()))
				m11_type = fieldValue;
			else if(name.toLowerCase().equals("m11_drawX".toLowerCase()))
				m11_drawX = fieldValue;
			else if(name.toLowerCase().equals("m11_drawY".toLowerCase()))
				m11_drawY = fieldValue;	
		}	
	}
}
