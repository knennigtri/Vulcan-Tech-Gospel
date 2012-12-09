package com.nennig.vulcan.tech.gospel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

@SuppressLint("NewApi")
public class AssetManagement {
	private static final String TAG = "AssetManagement";
	private static String[] acceptedExtensions = {".jpg",".png"};
	public static final String ROOT_FOLDER = Environment.getExternalStorageDirectory().toString();
	
    public static String getPhotoName(String name){
    	
    	for(int i = 0; i<acceptedExtensions.length;i++){
    		if(name.contains(acceptedExtensions[i]))
    		{
    			return name.substring(0, name.length()-4);
    		}
    	}   	
    	return name;
    }
	
	 public static int getNumberOfAssets(Context c) throws IOException{
    		return getAssetPhotos(c).length;
	 }
	
	 private static Object[] getAssetPhotos(Context c) throws IOException{
		 return getAssetPhotos(c,"");
	 }
	 
    private static Object[] getAssetPhotos(Context c, String wPaths) throws IOException{
    	String[] assets;
    	if(wPaths == "")
    	{
    		String[] arr = c.getAssets().list("");
        	ArrayList<String> tempAL = new ArrayList<String>();
        	for(int i = 0; i<arr.length; i++)
        	{
        		for(int j = 0; j<acceptedExtensions.length;j++)
        		{
        			if(arr[i].contains(acceptedExtensions[j]))
        			{
        				tempAL.add(arr[i]);
        				j = acceptedExtensions.length;
        			}	
        		}
        	}
        	assets = new String[tempAL.size()];
	    	for(int i = 0 ;i< assets.length;i++){
	    		assets[i] = tempAL.get(i);
	    		Log.d(TAG, "ALL AssetPath: " + assets[i]);
	    	}
    	}
    	else
    	{
    		ArrayList<String> wrongPaths = new ArrayList<String>();
    		String[] split = wPaths.split(",");
    		for(int i = 0; i < split.length; i++)
    			wrongPaths.add(split[i]);
    		
    		assets = new String[wrongPaths.size()];
    		for(int i = 0 ;i< assets.length;i++){
	    		assets[i] = wrongPaths.get(i);
	    		Log.d(TAG, "WRONG AssetPath: " + assets[i]);
	    	}
    	}
    	
    	Log.d(TAG, "Asset ArrayList finished.");
    	return assets;
    }
    
	public static Bitmap getBitmapImageResize(InputStream iStream, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(iStream, null, options);
        
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap newBitmap = BitmapFactory.decodeStream(iStream, null, options);
        
        final int height = options.outHeight;
        final int width = options.outWidth;
        
        int newHeight = 0;
        if(width>reqWidth){
        	newHeight = (int) Math.round(((float) reqWidth / (float) width)*height);
        }
        else
        {
        	newHeight = (int) Math.round(((float) width / (float) reqWidth)*reqHeight);
        }
        int newWidth= reqWidth;
    	newBitmap = Bitmap.createScaledBitmap(newBitmap, newWidth, newHeight, true);
        
        return newBitmap;
    }
    
    public static Bitmap getBitmapImage(InputStream iStream, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(iStream, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap newBitmap = BitmapFactory.decodeStream(iStream, null, options);
        
        final int height = options.outHeight;
        final int width = options.outWidth;
        
        if((width < reqWidth) && (height < reqHeight)){
        	//TODO DIVISION: WHY DOES THIS NOT WORK!
        	int newHeight = (int) Math.round(((float) width / (float) reqWidth)*reqHeight);
        	int newWidth= reqWidth;
        	newBitmap = Bitmap.createScaledBitmap(newBitmap, newWidth, newHeight, true);
        }
        
        return newBitmap;
    }
    
    public static int calculateInSampleSize(BitmapFactory.Options o, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = o.outHeight;
	    final int width = o.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) {
	        if (width > height) {
	            inSampleSize = Math.round((float)height / (float)reqHeight);
	        } else {
	            inSampleSize = Math.round((float)width / (float)reqWidth);
	        }
	    }
	    return inSampleSize;
    }
}
