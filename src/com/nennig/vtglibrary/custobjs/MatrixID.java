/**
 * 
 */
package com.nennig.vtglibrary.custobjs;

import java.util.ArrayList;

import com.nennig.vtglibrary.R;

import android.content.Context;
import android.util.Log;

/**
 * @author Kevin Nennig (knennig213@gmail.com)
 *
 */
public class MatrixID {
	private static final String TAG = "MatrixID Obj";
	private int propID;
	private int handID;
	private int posID;
	
	private String delim = "x";
	
	public MatrixID(String id){
		if(id.length() == 5){
			String[] parse = id.split(delim);
			if(parse.length == 3){
				propID = Integer.valueOf(parse[0]);
				handID = Integer.valueOf(parse[1]);
				posID = Integer.valueOf(parse[2]);
			}
			else
				Log.d(TAG, "Could not parse matrix properly");
		}
		else
			Log.d(TAG, "MatrixID not correct Length: " +id.length());
	}
	
	public MatrixID(int handID, int propID, int positionID){
		this.propID = propID;
		this.handID = handID;
		this.posID = positionID;
	}

	public String getMatrixID(){
		return propID + delim + handID + delim + posID;
	}
	
	/**
	 * @return the propID
	 */
	public int getPropID() {
		return propID;
	}

	/**
	 * @param propID the propID to set
	 */
	public void setPropID(int propID) {
		this.propID = propID;
	}

	/**
	 * @return the handID
	 */
	public int getHandID() {
		return handID;
	}

	/**
	 * @param handID the handID to set
	 */
	public void setHandID(int handID) {
		this.handID = handID;
	}

	/**
	 * @return the posID
	 */
	public int getPosID() {
		return posID;
	}

	/**
	 * @param posID the posID to set
	 */
	public void setPosID(int posID) {
		this.posID = posID;
	}
	
    public static class MCategory{
    	private static int TS_Index = 0;
    	private static int SS_Index = 1;
    	private static int TO_Index = 2;
    	private static int SO_Index = 3;
    	
    	private static String TS_Long = "Together Time/Same Direction";
    	private static String SS_Long = "Split Time/Same Direction";
    	private static String TO_Long = "Together Time/Opposite Direction";
    	private static String SO_Long = "Split Time/Opposite Direction";
    	
    	private static String TS_Abbr = "T/S";
    	private static String SS_Abbr = "S/S";
    	private static String TO_Abbr = "T/O";
    	private static String SO_Abbr = "S/O";
    	
    	public static ArrayList<String> getSpinnerArrayList(){
    		ArrayList<String> al = new ArrayList<String>();
    		al.add(TS_Long);
    		al.add(SS_Long);
    		al.add(TO_Long);
    		al.add(SO_Long);
    		return al;
    	}
    	
    	public static String getStringLongFromIndex(int i){
    		switch(i){
	    		case 0:
	    			return TS_Long;
	    		case 1:
	    			return SS_Long;
	    		case 2:
	    			return TO_Long;
	    		case 3:
	    			return SO_Long;
	    		default:
	    			return TS_Long;
    		}
    	}
    	public static String getStringLongFromAbbr(String str){
    		if(str.equals(TS_Abbr))
    			return TS_Long;
    		if(str.equals(SS_Abbr))
    			return SS_Long;
    		if(str.equals(TO_Abbr))
    			return TO_Long;
    		if(str.equals(SO_Abbr))
    			return SO_Long;
    		return TS_Long;
    	}
    	public static String getStringAbbrFromIndex(int i){
    		switch(i){
	    		case 0:
	    			return TS_Abbr;
	    		case 1:
	    			return SS_Abbr;
	    		case 2:
	    			return TO_Abbr;
	    		case 3:
	    			return SO_Abbr;
	    		default:
	    			return TS_Abbr;
    		}
    	}
    	public static String getStringAbbrFromLong(String str){
    		if(str.equals(TS_Long))
    			return TS_Abbr;
    		if(str.equals(SS_Long))
    			return SS_Abbr;
    		if(str.equals(TO_Long))
    			return TO_Abbr;
    		if(str.equals(SO_Long))
    			return SO_Abbr;
    		return TS_Abbr;
    	}
    	public static int getIndex(String str){
    		if(str.equals(TS_Long) || str.equals(TS_Abbr))
    			return TS_Index;
    		if(str.equals(SS_Long) || str.equals(SS_Abbr))
    			return SS_Index;
    		if(str.equals(TO_Long) || str.equals(TO_Abbr))
    			return TO_Index;
    		if(str.equals(SO_Long) || str.equals(SO_Abbr))
    			return SO_Index;
    		return TS_Index;
    	}
    }
}
