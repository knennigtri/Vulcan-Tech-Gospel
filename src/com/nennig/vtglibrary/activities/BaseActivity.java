/**
 * @author Kevin Nennig
 * This is the base class for this app. It allows for a common menu on all activities extending it. It also gives
 * the displays dimensions for calculating icons. There are a few other various methods needed by multiple extended classes 
 * to make code reuse minimal.
 */
package com.nennig.vtglibrary.activities;

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
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;

import com.nennig.constants.AppConfig;
import com.nennig.constants.AppConstants;
import com.nennig.constants.AppManager;
import com.nennig.constants.ChangeLog;
import com.nennig.vtglibrary.R;
import com.nennig.vtglibrary.managers.VTGLibraryApplication;


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
     * Converts a time direction string into the corresponding index value used in the matrix standard for this app.
     * @param str The time direction index requested.
     * @return index of the time direction
     */
    public int getTimeDirectionInt(String str){
    	if(str.equals(getString(R.string.global_ts)))
    		return 0;
    	if(str.equals(getString(R.string.global_ss)))
    		return 1;
    	if(str.equals(getString(R.string.global_to)))
    		return 2;
    	if(str.equals(getString(R.string.global_so)))
    		return 3;
    	return 0;
    }
    
    /**
     * Converts the index into the corresponding time direction string in the standard for this app
     * @param i index of the time direction
     * @return string of the time direction
     */
    public String getTimeDirectionString(int i){
    	if(i == 0)
    		return getString(R.string.global_ts);
    	if(i == 1)
    		return getString(R.string.global_ss);
    	if(i == 2)
    		return getString(R.string.global_to);
    	if(i == 3)
    		return getString(R.string.global_so);
    	return getString(R.string.global_ts);
    }   
    
    /**
     * This method is built for displaying an icon in this app. It takes in the dimensions of the phone and then resizes 
     * the bitmap to fit the screen.
     * @param iStream The bitmap that needs resized
     * @param reqWidth requested width of the new bitmap
     * @return resized bitmap
     */
	public static Bitmap getBitmapImage(InputStream iStream, int reqWidth) {
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
    		AppManager.aboutAlert(this);
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
    		url = AppConstants.PAYPAL;
        	i = new Intent(Intent.ACTION_VIEW);
        	i.setData(Uri.parse(url));
        	startActivity(i);
    		return true;
		}
		else if(item.getItemId() == R.id.menu_rate_this)
		{
    		url = AppConstants.RATEAPP;
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
		else
		{
    		return super.onOptionsItemSelected(item);
    	}
    }

    public boolean isLiteVersion()  {
    	return ((VTGLibraryApplication)getApplication()).isLiteVersion(); 
    }
}
