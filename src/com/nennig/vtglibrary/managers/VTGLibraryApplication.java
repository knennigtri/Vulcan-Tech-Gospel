/**
 * This is a class to create a custom class that helps the code base stay in the same branch of code.
 * 
 * @author Kevin Nennig
 */

package com.nennig.vtglibrary.managers;

import java.util.HashMap;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.nennig.vtglibrary.R;

public class VTGLibraryApplication extends Application{
	/**
	   * Enum used to identify the tracker that needs to be used for tracking.
	   *
	   * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
	   * storing them all in Application object helps ensure that they are created only once per
	   * application instance.
	   */
	  public enum TrackerName {
	    APP_TRACKER, // Tracker used only in this app.
	  }
	  private static final String PROPERTY_PROID = "UA-45811565-2";
	  private static final String PROPERTY_LITEID = "UA-45811565-1";
	  public static int GENERAL_TRACKER = 0;

	  HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
	
	  public VTGLibraryApplication(){
		  super();
	  }
	  
	public boolean isLiteVersion() {
		Log.d("VTGLibrary", getPackageName());
		return !getPackageName().toLowerCase().contains("pro"); 
	}
	
	public synchronized Tracker getTracker(TrackerName trackerId, Context c) {
	    if (!mTrackers.containsKey(trackerId)) {
	    	String propID;
	    	propID = isLiteVersion() ? PROPERTY_LITEID : PROPERTY_PROID;
	    	
	      GoogleAnalytics analytics = GoogleAnalytics.getInstance(c);
	      Tracker t = analytics.newTracker(propID);
	      mTrackers.put(trackerId, t);

	    }
	    return mTrackers.get(trackerId);
	  }
}
