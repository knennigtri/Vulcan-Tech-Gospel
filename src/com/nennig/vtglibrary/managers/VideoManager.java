package com.nennig.vtglibrary.managers;

import com.nennig.constants.AppConfig;
import com.nennig.vtglibrary.R;
import com.nennig.vtglibrary.R.string;

import android.content.Context;
import android.util.Log;



public class VideoManager {
	private static final String TAG = AppConfig.APP_TITLE_SHORT + ".VideoManager";
	
	//TODO rename videos and then update this method.
	public static int getVideoID(Context c, String setType, String video, int prop){
//		String name = "v" + setType + "_" + video + "_" + getVideoProp(c,prop); //Implemented when video files are updated.
		String name = "v" + video + "_" + getVideoProp(c,prop);
		Log.d(TAG, "Loading Video: " + name);
		return c.getResources().getIdentifier(name, "raw", c.getPackageName()); 
	}
	
	private static String getVideoProp(Context c, int i){
		return c.getString(R.string.ext_prop0);
//		switch(i){
//		case 0:
//			return c.getString(R.string.ext_prop0);
//		case 1:
//			return c.getString(R.string.ext_prop1);
//		case 2:
//			return c.getString(R.string.ext_prop2);
//		case 3:
//			return c.getString(R.string.ext_prop3);
//		default:
//			return c.getString(R.string.ext_prop0);
//		}
	}
	
}
