/**
 * @author Kevin Nennig
 * This is the base class for this app. It allows for a common menu on all activities extending it. It also gives
 * the displays dimensions for calculating icons. There are a few other various methods needed by multiple extended classes 
 * to make code reuse minimal.
 */
package com.nennig.vtglibrary.activities;

import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
//import com.google.analytics.tracking.android.EasyTracker;
import com.nennig.constants.AppConstants;
import com.nennig.constants.AppManager;
import com.nennig.constants.ChangeLog;
import com.nennig.constants.DevConstants;
import com.nennig.vtglibrary.R;
import com.nennig.vtglibrary.managers.VTGLibraryApplication;
import com.nennig.vtglibrary.managers.VTGLibraryApplication.TrackerName;


public class BaseActivity extends Activity {
	

	// Physical display width and height.
    public static int displayWidth = 0;
    public static int displayHeight = 0;
	public static final String ROOT_FOLDER = Environment.getExternalStorageDirectory().toString();
	private static final String TAG = "BaseActivity";
	
	
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //Forces app to be portrait 
        
        Display display = getWindowManager().getDefaultDisplay();
        
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2){
			Point displayBox = new Point();
	        display.getSize(displayBox);
			displayWidth = displayBox.x;          
			displayHeight = displayBox.y;
		} else{
	        displayWidth = display.getWidth();             
	        displayHeight = display.getHeight();  
		}		
    }
	
    /**
     * This method is built for displaying an icon in this app. It takes in the dimensions of the phone and then resizes 
     * the bitmap to fit the screen.
     * @param iStream The bitmap that needs resized
     * @param reqWidth requested width of the new bitmap
     * @return resized bitmap
     */
	public static Bitmap getBitmapImage(InputStream iStream, int reqWidth) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();

        Bitmap newBitmap = BitmapFactory.decodeStream(iStream, null, options);
        
        final int height = options.outHeight;
        final int width = options.outWidth;
       
        int newHeight = (int) Math.round(((float) reqWidth / (float) width) * (float) height);
        
        int newWidth= reqWidth;
       
        Log.d(TAG, "{H:W}: {" + height+":"+width+"} "+"NewHeight: " + newHeight + "NewWidth: "+newWidth);
        
    	newBitmap = Bitmap.createScaledBitmap(newBitmap, newWidth, newHeight, true);
        
        return newBitmap;
    }
	
	/**
	 * This is mainly used to dynamically resize the pdfs in the DetailViewActivity, but was moved to the BaseActivity
	 * because it made sense to keep it with resizing the bitmap. 
     * @param iStream The bitmap that needs resized
     * @param reqWidth requested width of the new matrix
	 * @return resized matrix of the icon
	 */
	public static Matrix getImageMatrix(InputStream iStream, int reqWidth) {
		Log.d(TAG, "ReqSize: " + reqWidth);
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(iStream, null, options);
        
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap newBitmap = BitmapFactory.decodeStream(iStream, null, options);
        
        
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d(TAG, "H: " + height + " W: " + width);
       
        int newHeight = (int) Math.round(((float) reqWidth / (float) width)*height);
        int newWidth= reqWidth;
       
        Log.d(TAG, "nH: " + newHeight + " nW: " + newWidth);
        Matrix matrix = new Matrix();
        matrix.postScale(newWidth, newHeight);
        
        return matrix;
    }
	
	/**
	 * This is a generalized menu for all activities in this app. This allows users to access the most important information
	 * about this app and it's market and social capabilities.
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent i;
    	String url;    	
    	if(item.getItemId() == R.id.menu_about)
    	{
//    		AppManager.aboutAlert(this);
            AppManager.getAboutDialog(this);
    		return true;
    	}
    	else if(item.getItemId() == R.id.menu_facebook)
    	{
    		url =AppConstants.FACEBOOK;
        	i = new Intent(Intent.ACTION_VIEW);
        	i.setData(Uri.parse(url));
        	startActivity(i);
    		return true;
    	}
		else if(item.getItemId() == R.id.menu_donate)
		{
			i = new Intent(Intent.ACTION_VIEW);
        	i.setData(Uri.parse(AppConstants.PAYPAL));
        	startActivity(i);
    		return true;
		}
		else if(item.getItemId() == R.id.menu_rate_this)
		{
    		url = DevConstants.GOOGLE_PLAY + getPackageName();
    		i = new Intent(Intent.ACTION_VIEW);
        	i.setData(Uri.parse(url));
        	startActivity(i);
    		return true;
		}
		else if(item.getItemId() == R.id.menu_change_log)
		{
    		ChangeLog cl = new ChangeLog(this);
            cl.getFullLogDialog().show();
            return true;
		}
		else if( item.getItemId() == android.R.id.home)
		{
			NavUtils.navigateUpFromSameTask(this);
	        return true;
		}	
    	else
		{
    		return super.onOptionsItemSelected(item);
    	}
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.general, menu);
		return true;
	}
    
    public boolean isLiteVersion()  {
    	return ((VTGLibraryApplication)getApplication()).isLiteVersion(); 
    }
    
    @Override
    public void onStart() {
      super.onStart();
      
      // Get tracker.
      Tracker t = ((VTGLibraryApplication) getApplication()).getTracker(
          TrackerName.APP_TRACKER, this);

      // Set screen name.
      // Where path is a String representing the screen name.
      t.setScreenName(getClass().getName());

      // Send a screen view.
      t.send(new HitBuilders.AppViewBuilder().build());
    }

    @Override
    public void onStop() {
      super.onStop();
    }
}
