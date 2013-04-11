/**
 * 
 */
package com.nennig.vtglibrary.managers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.nennig.constants.AppConfig;
import com.nennig.constants.AppConstants;

/**
 * @author Kevin Nennig (knennig213@gmail.com)
 *
 */
public class SingletonMovePinMap {
	private static String db13 = "1313_move_db.csv";
	private static String db11 = "1111_move_db.csv";
	
	private static final String TAG = AppConfig.APP_PNAME + ".singleton.pin";
	private static SingletonMovePinMap ref;
	private static String[] headers = new String[]{"Prop","Hand","Position","Pin0","Pin1","Pin2","Pin3",
		"Pin4","Pin5","Pin6","Pin7"};
	
	/**
	 * Holds all PoiMove objects that are parsed from the db file
	 */
	public static Map<String, MovePins> movePinMap = new HashMap<String, MovePins>();
	/**
	 * Map of all the header indexes for the db file
	 */
	public static Map<Integer, String> headerIndexMap = new HashMap<Integer, String>();

	//For debugging dbFile ONLY-------
	public static void main(String[] args) {
		String location = "D:\\My Documents\\workspace\\Vulcan Tech Gospel\\assets\\1313_move_db.csv";
		final SingletonMovePinMap obj = SingletonMovePinMap.getSingletonMovePinMap(location, "1313");
		for(MovePins mp : obj.movePinMap.values()){
			System.out.println(mp.toString());
		}
	}
	
	public static SingletonMovePinMap getSingletonMovePinMap(Object obj, String set){
		if(ref == null){
			ref = new SingletonMovePinMap(obj, set);
		}
		return ref;
	}

	/**
	 * Creating a factory object so that it can be parsed from Context or from String. *Strictly for debugging purposes.
	 * @param obj
	 */
	private SingletonMovePinMap(Object obj, String set){
		if(obj != null){
			if(obj instanceof Context)
				createSingleton((Context) obj, set);
			if(obj instanceof String)
				createSingleton((String) obj, set);
		}
	}
	
	private void createSingleton(String str, String set){
		try{
			BufferedReader bR = new BufferedReader(new FileReader(str));
			parseDBInfo(bR);
		}catch (Exception e) {
			Log.d(TAG, "" + e.getMessage());
		}
	}
	private void createSingleton(Context c, String set){
		try{
			InputStream iS = c.getAssets().open(getdbFile(set));
			InputStreamReader iSR = new InputStreamReader(iS);
			BufferedReader bR = new BufferedReader(iSR);
			parseDBInfo(bR);
		}catch (Exception e) {
			Log.d(TAG, "" + e.getMessage());
		}
	}
	
	/**
	 * @return
	 */
	private String getdbFile(String curSet) {
		if(curSet.equals(AppConstants.SET_1313)){
			return db13;
		}
		else if(curSet.equals(AppConstants.SET_1111))
		{
			return db11;
		}
		return db13;
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
		    	  if(!nextLineStr.contains("-")){
			    	  nextLineParse = nextLineStr.split(",");
		    		  
			    	  if(readHeaders){
			    		  getheaderIndexes(nextLineParse);
			    		  readHeaders = false;
			    	  }
			    	  else
			    	  {
				    	  MovePins mP = parseDBLine(nextLineParse);
			    		  movePinMap.put(mP.matrixID, mP);
			    	  }
		    	  }
		      }
		} catch (Exception e) {
			Log.d(TAG, "Exception: " + e.toString());
		}finally{
			 Log.d(TAG,"Finished Parse");
		}
	}
	
	/**
	 * @param nextLineParse
	 * @return
	 */
	private MovePins parseDBLine(String[] nextLineParse) {
		MovePins mP= new MovePins();
		String prop="",hand="",pos="";
		for(int i = 0; i < nextLineParse.length; i++){
			String hName = headerIndexMap.get(i);
			if(hName.equals("Prop".toLowerCase()))
				prop =  nextLineParse[i];
			else if(hName.equals("Hand".toLowerCase()))
				hand =  nextLineParse[i];
			else if(hName.equals("Position".toLowerCase()))
				pos =  nextLineParse[i];
			else if(hName.equals("Pin0".toLowerCase())){
				String[] fields = nextLineParse[i].split("\\.");
				if(fields.length > 1)
					mP.pin0 = new Pin(fields[0],fields[1]);
			}
			else if(hName.equals("Pin1".toLowerCase())){
				String[] fields = nextLineParse[i].split("\\.");
				if(fields.length > 1)
					mP.pin1 = new Pin(fields[0],fields[1]);
			}
			else if(hName.equals("Pin2".toLowerCase())){
				String[] fields = nextLineParse[i].split("\\.");
				if(fields.length > 1)
					mP.pin2 = new Pin(fields[0],fields[1]);
			}
			else if(hName.equals("Pin3".toLowerCase())){
				String[] fields = nextLineParse[i].split("\\.");
				if(fields.length > 1)
					mP.pin3 = new Pin(fields[0],fields[1]);
			}
			else if(hName.equals("Pin4".toLowerCase())){
				String[] fields = nextLineParse[i].split("\\.");
				if(fields.length > 1)
					mP.pin4 = new Pin(fields[0],fields[1]);
			}
			else if(hName.equals("Pin5".toLowerCase())){
				String[] fields = nextLineParse[i].split("\\.");
				if(fields.length > 1)
					mP.pin5 = new Pin(fields[0],fields[1]);
			}
			else if(hName.equals("Pin6".toLowerCase())){
				String[] fields = nextLineParse[i].split("\\.");
				if(fields.length > 1)
					mP.pin6 = new Pin(fields[0],fields[1]);
			}
			else if(hName.equals("Pin7".toLowerCase())){
				String[] fields = nextLineParse[i].split("\\.");
				if(fields.length > 1)
					mP.pin7 = new Pin(fields[0],fields[1]);
			}
		}
		mP.matrixID = prop + "x" + hand + "x" + pos;
		return mP;
	}

	/**
	 * Takes in the header line and then assigns all PoiMove fields to an associated index in the db file.
	 * @param nextLineParse
	 */
	private void getheaderIndexes(String[] nextLineParse) {
		for(int i = 0; i < nextLineParse.length; i++){
			for(int j = 0; j < headers.length; j++){
				if(nextLineParse[i].equals(headers[j])){
					headerIndexMap.put(i,nextLineParse[i].toLowerCase());
					j = headers.length;
				}
			}
		}
		Log.d(TAG,headerIndexMap.toString());
	}
	
	@Override
	public String toString(){
		String str = "{";
		for(MovePins pm : movePinMap.values()){
			str = str + pm.toString();
		}
		str = str + "}";
		return str;
	}
	
	public class MovePins{
		public String matrixID="";
		public Pin pin0;
		public Pin pin1;
		public Pin pin2;
		public Pin pin3;
		public Pin pin4;
		public Pin pin5;
		public Pin pin6;
		public Pin pin7;
		
		@Override
		public String toString(){
			String str = "<"+matrixID+">:";
			if(pin0 != null)
				str = str + " 0=" + pin0.toString();
			if(pin1 != null)
				str = str + " 1=" + pin1.toString();
			if(pin2 != null)
				str = str + " 2=" + pin2.toString();
			if(pin3 != null)
				str = str + " 3=" + pin3.toString();
			if(pin4 != null)
				str = str + " 4=" + pin4.toString();
			if(pin5 != null)
				str = str + " 5=" + pin5.toString();
			if(pin6 != null)
				str = str + " 6=" + pin6.toString();
			if(pin7 != null)
				str = str + " 7=" + pin7.toString();
			return str;
		}
	}
	
	
	public class Pin{
		private String direction;
		private String color;
		
		public Pin(String dir, String col){
			setDirection(dir);
			setColor(col);
		}

		public Pin(){
			direction = "";
			color = "";
		}
		
		/**
		 * @return the direction
		 */
		public String getDirection() {
			return direction;
		}

		/**
		 * @param direction the direction to set
		 */
		public void setDirection(String direction) {
			this.direction = direction;
		}

		/**
		 * @return the color
		 */
		public String getColor() {
			return color;
		}

		/**
		 * @param color the color to set
		 */
		public void setColor(String color) {
			this.color = color;
		}
		 
		@Override
		public String toString(){
			return direction + "." + color;
		}
	}
}
