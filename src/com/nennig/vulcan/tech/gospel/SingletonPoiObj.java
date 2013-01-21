/**
 * This class is for parsing and holding the information for the different poi moves. This class allows for parsing of the csv file 
 * and then easily referencing for the detail view of each poi move.
 */

package com.nennig.vulcan.tech.gospel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.nennig.constants.AppConfig;
import com.nennig.constants.AppConstants;

import android.content.Context;
import android.util.Log;

public class SingletonPoiObj {
	private static final String DB_FILE = "db.csv";
	private static final String TAG = AppConfig.APP_PNAME + ".singleton";
	private static SingletonPoiObj ref;
	public static Map<String, PoiMove> poiMoveMap = new HashMap<String, PoiMove>();
	
	private SingletonPoiObj(Context c){
		parseDBInfo(c);
	}
	
	public static SingletonPoiObj getSingletonPoiObj(Context c){
		if(ref == null){
			ref = new SingletonPoiObj(c);
		}
		return ref;
	}
	
	/**
	 * This method parses the csv file and puts it into a hashmap of PoiMoves Objects
	 * @param c
	 */
	private void parseDBInfo(Context c){
		try {
			InputStream iS = c.getAssets().open(DB_FILE);
			InputStreamReader iSR = new InputStreamReader(iS);
			BufferedReader bR = new BufferedReader(iSR);
		      String nextLineStr;
		      String[] nextLineParse;
		      
		      while ((nextLineStr = bR.readLine()) != null) {
		    	  Log.d(TAG, "LINE: " + nextLineStr);
		    	  nextLineParse = nextLineStr.split(",");
	    		  
		    	  PoiMove pM = new PoiMove();  
		    	  pM.moveID = nextLineParse[0];
	    		  pM.name = nextLineParse[1];
	    		  pM.youtubeVideo = getVideoUrl(Integer.valueOf(nextLineParse[2]));
	    		  pM.sTime = nextLineParse[3];
	    		  pM.eTime = nextLineParse[4];
	    		  
	    		  poiMoveMap.put(pM.moveID, pM);
		      }
		} catch (Exception e) {
			Log.d(TAG, "" + e.getMessage());
		}finally{
			 Log.d(TAG,"Finished Parse");
		}
	}
	
	public PoiMove getPoiMove(String moveID){
		return poiMoveMap.get(moveID);
	}
	
	public String getVideoUrl(int i){
		if(i == 1)
			return AppConstants.vtg2Index1Of3;
		if(i == 2)
			return AppConstants.vtg2Index2Of3;
		if(i == 3)
			return AppConstants.vtg2Index3Of3;
		return AppConstants.vtg2Index1Of3;
	} 
	
	public class PoiMove{
		public String moveID;
		public String name;
		public String youtubeVideo;
		public String sTime;
		public String eTime;
	}
}
