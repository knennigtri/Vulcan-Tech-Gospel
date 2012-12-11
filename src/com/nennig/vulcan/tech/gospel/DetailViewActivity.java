package com.nennig.vulcan.tech.gospel;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.PSource;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class DetailViewActivity extends BaseActivity {
	private static final String TAG = "DetialViewActivity";
	private static final String DB_FILE = "db.csv";

    
    private int poiVal = 0;
    private int handVal = 0;
    private int posVal = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        
        SharedPreferences sP = getSharedPreferences(VTG_PREFS, MODE_PRIVATE);
        poiVal = sP.getInt(CUR_POI, 0);
        handVal = sP.getInt(CUR_HAND, 0);
        posVal = sP.getInt(CUR_POS, 0);
        String ext = "png";
        
        TextView poiText = (TextView) findViewById(R.id.detail_poiText);
        TextView handText = (TextView)findViewById(R.id.detail_handText);
        
        poiText.setText(getTimeDirectionString(poiVal));
        handText.setText(getTimeDirectionString(handVal));
		
		//Set The Image
		final ImageView iv = (ImageView) findViewById(R.id.detail_image);
		InputStream iStream = null;
		String detailName = poiVal + "x" + handVal + "x" + posVal;
//		 wv.getSettings().setBuiltInZoomControls(true);
//		 wv.loadUrl("file:///android_asset/" + DETAIL_VIEW_FOLDER + "/" + detailName + "." + ext);
		

		Bitmap bitmapImage = null;
		try {
			iStream = getAssets().open(DETAIL_VIEW_FOLDER + "/" + detailName + "." + ext);
			bitmapImage = getBitmapImage(iStream, displayWidth-10, displayHeight );	
			iv.setImageBitmap(bitmapImage);
		} catch (IOException e) {
			Log.d(TAG, "The file " + detailName + " was not found...");
			Log.d(TAG, e.toString());
		}

		 
//		String[] infoDetails = detailMap.get(detailName);
		String[] infoDetails = getMoveDetails(detailName, DB_FILE);
		
		//Set the Image Name
		String textName = infoDetails[0];
		TextView detailTV = (TextView) findViewById(R.id.detail_photoName);
		if(textName.length()>57)
			detailTV.setTextSize(15);
		detailTV.setText(textName);

		final String url = infoDetails[1];
		final String start = infoDetails[2];
		final String end = infoDetails[3];
		
		//Set the YouTube Video Button
		Button youtubeB = (Button) findViewById(R.id.detail_youtTubeButton);
		youtubeB.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				String fullUrl = url + 
						"&#t=" + start.split(":")[0] + "m" + start.split(":")[1] + "s" +
						"&end=" + end.split(":")[0] + "m" + end.split(":")[1] + "s" +
						"&version=3";
				Log.d(TAG, " URL= "+fullUrl);
		    	Intent i = new Intent(Intent.ACTION_VIEW);
		    	i.setData(Uri.parse(fullUrl));
		    	startActivity(i);
					
				return false;
			}
		});
		
	}
	
	public static Bitmap getBitmapImage(InputStream iStream, int reqWidth, int reqHeight) {

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
    
	//This static HashMap holds all the special details for all of the details of each move.
    //The key is a identifier of where it is in the matirx
    //[0] is the name
    //[1] is the url
    //[2] is the start time
    //[3] is the end time
	private String[] getMoveDetails(String key, String csvFile){
		try {
			InputStream iS = getAssets().open(csvFile);
			InputStreamReader iSR = new InputStreamReader(iS);
			BufferedReader bR = new BufferedReader(iSR);
		      String nextLineStr;
		      String[] nextLineParse;
		      
		      while ((nextLineStr = bR.readLine()) != null) {
		    	  Log.d(TAG, "LINE: " + nextLineStr);
		    	  nextLineParse = nextLineStr.split(",");
		    	  if(key.equals(nextLineParse[0]))
		    	  {
		    		  String n, url, s, e;
		    		  n = nextLineParse[1];
		    		  url = getVideoUrl(Integer.valueOf(nextLineParse[2]));
		    		  s = nextLineParse[3];
		    		  e = nextLineParse[4];
		    		  Log.d(TAG, "n="+n+" url="+url+" s="+s+" e="+e);
		    		  return new String[] {n,url,s,e};
		    	  }
		      }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			 Log.d("db","Finished Parse");
		}
		return null;
	}
	
	public String getVideoUrl(int i){
		if(i == 1)
			return vtg2Index1Of3;
		if(i == 2)
			return vtg2Index2Of3;
		if(i == 3)
			return vtg2Index3Of3;
		return vtg2Index1Of3;
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.general, menu);
        return true;
    }
    

    public class Zoom extends View {

	
	    private Drawable image;
	    ImageButton img,img1;
	
	    private int zoomControler=20;
	
	
	    public Zoom(Context context, Bitmap b)
	
	    {
	
	            super(context);
	
	            image= new BitmapDrawable(getResources(),b);
	            //image=context.getResources().getDrawable(R.drawable.icon);
	
	            setFocusable(true);
	
	
	
	    }
	
	    @Override
	
	    protected void onDraw(Canvas canvas) {
	
	            // TODO Auto-generated method stub
	
	            super.onDraw(canvas);
	
	    //here u can control the width and height of the images........ this line is very important
	
	    image.setBounds((getWidth()/2)-zoomControler, (getHeight()/2)-zoomControler, (getWidth()/2)+zoomControler, (getHeight()/2)+zoomControler);
	
	            image.draw(canvas);
	
	    }


	    @Override
	
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	
	
	
	            if(keyCode==KeyEvent.KEYCODE_DPAD_UP)// zoom in
	
	                    zoomControler+=10;
	
	            if(keyCode==KeyEvent.KEYCODE_DPAD_DOWN) // zoom out
	
	                    zoomControler-=10;
	
	            if(zoomControler<10)
	
	                    zoomControler=10;
	
	
	
	            invalidate();
	
	            return true;
	
	    }
    }
    
}


