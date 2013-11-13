/**
 * 
 */
package com.nennig.constants;

import android.util.Log;

/**
 * @author Kevin Nennig (knennig213@gmail.com)
 *
 */
public class Dlog{	
	private static final boolean GLOBAL_DEBUG = true;

	public static void d(String Tag, String message, boolean enableDebug){
		if(GLOBAL_DEBUG && enableDebug)
			Log.d(Tag,message);
	}
	
	public static void i(String Tag, String message, boolean enableDebug){
		if(GLOBAL_DEBUG && enableDebug)
			Log.i(Tag,message);
	}
	
	public static void w(String Tag, String message, boolean enableDebug){
		if(GLOBAL_DEBUG && enableDebug)
			Log.w(Tag,message);
	}
	
	public static void e(String Tag, String message, boolean enableDebug){
		if(GLOBAL_DEBUG && enableDebug)
			Log.e(Tag,message);
	}

	public static void v(String Tag, String message, boolean enableDebug){
		if(GLOBAL_DEBUG && enableDebug)
			Log.e(Tag,message);
	}
	
	public static void wtf(String Tag, String message, boolean enableDebug){
		if(GLOBAL_DEBUG && enableDebug)
			Log.e(Tag,message);
	}
}
