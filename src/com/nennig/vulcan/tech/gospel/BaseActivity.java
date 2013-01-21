package com.nennig.vulcan.tech.gospel;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.nennig.constants.AppConfig;
import com.nennig.constants.AppConstants;
import com.nennig.constants.AppManager;
import com.nennig.constants.DevConstants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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


public class BaseActivity extends Activity {
	

	// Physical display width and height.
    public static int displayWidth = 0;
    public static int displayHeight = 0;

	public static final String ROOT_FOLDER = Environment.getExternalStorageDirectory().toString();
	private static final String TAG = AppConfig.APP_PNAME + ".Base";
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
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
   
    public static void helpAlert(final Activity c){
    	
    }
    
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
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent i;
    	String url;
    	switch(item.getItemId()){
    	case R.id.menu_about:
    		AppManager.aboutAlert(this);
    		return true;
    	case R.id.menu_facebook:
    		url =AppConstants.FACEBOOK;
        	i = new Intent(Intent.ACTION_VIEW);
        	i.setData(Uri.parse(url));
        	startActivity(i);
    		return true;
    	case R.id.menu_donate:
    		url = AppConstants.PAYPAL;
        	i = new Intent(Intent.ACTION_VIEW);
        	i.setData(Uri.parse(url));
        	startActivity(i);
    		return true;
    	case R.id.menu_rate_this:
    		url = AppConstants.RATEAPP;
    		i = new Intent(Intent.ACTION_VIEW);
        	i.setData(Uri.parse(url));
        	startActivity(i);
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }

}
