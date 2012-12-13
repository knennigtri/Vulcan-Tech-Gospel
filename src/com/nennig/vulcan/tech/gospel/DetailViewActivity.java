package com.nennig.vulcan.tech.gospel;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class DetailViewActivity extends BaseActivity implements OnTouchListener{
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
			bitmapImage = getBitmapImage(iStream, displayWidth );	
			iv.setImageBitmap(bitmapImage);
			iv.setOnTouchListener(this);
		} catch (IOException e) {
			Log.d(TAG, "The file " + detailName + " was not found...");
			Log.d(TAG, e.toString());
		}

//		//TODO Work on Image Zooming!
//		iv.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				Intent i = new Intent(DetailViewActivity.this, DetailZoomActivity.class);
//		    	startActivity(i);
//				return false;
//			}
//		});
		
		 
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
	
	
	
	// These matrices will be used to move and zoom image
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();

	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	// Remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;
	String savedItemClicked;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
	    // TODO Auto-generated method stub

	    ImageView view = (ImageView) v;
	    dumpEvent(event);

	    // Handle touch events here...
	    switch (event.getAction() & MotionEvent.ACTION_MASK) {
	    case MotionEvent.ACTION_DOWN:
	        savedMatrix.set(matrix);
	        start.set(event.getX(), event.getY());
	        Log.d(TAG, "mode=DRAG");
	        mode = DRAG;
	        break;
	    case MotionEvent.ACTION_POINTER_DOWN:
	        oldDist = spacing(event);
	        Log.d(TAG, "oldDist=" + oldDist);
	        if (oldDist > 10f) {
	            savedMatrix.set(matrix);
	            midPoint(mid, event);
	            mode = ZOOM;
	            Log.d(TAG, "mode=ZOOM");
	        }
	        break;
	    case MotionEvent.ACTION_UP:
	    case MotionEvent.ACTION_POINTER_UP:
	        mode = NONE;
	        Log.d(TAG, "mode=NONE");
	        break;
	    case MotionEvent.ACTION_MOVE:
	        if (mode == DRAG) {
	            // ...
	            matrix.set(savedMatrix);
	            matrix.postTranslate(event.getX() - start.x, event.getY()
	                    - start.y);
	        } else if (mode == ZOOM) {
	            float newDist = spacing(event);
	            Log.d(TAG, "newDist=" + newDist);
	            if (newDist > 10f) {
	                matrix.set(savedMatrix);
	                float scale = newDist / oldDist;
	                matrix.postScale(scale, scale, mid.x, mid.y);
	            }
	        }
	        break;
	    }

	    view.setImageMatrix(matrix);
	    return true;
	}

	private void dumpEvent(MotionEvent event) {
	    String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
	            "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
	    StringBuilder sb = new StringBuilder();
	    int action = event.getAction();
	    int actionCode = action & MotionEvent.ACTION_MASK;
	    sb.append("event ACTION_").append(names[actionCode]);
	    if (actionCode == MotionEvent.ACTION_POINTER_DOWN
	            || actionCode == MotionEvent.ACTION_POINTER_UP) {
	        sb.append("(pid ").append(
	                action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
	        sb.append(")");
	    }
	    sb.append("[");
	    for (int i = 0; i < event.getPointerCount(); i++) {
	        sb.append("#").append(i);
	        sb.append("(pid ").append(event.getPointerId(i));
	        sb.append(")=").append((int) event.getX(i));
	        sb.append(",").append((int) event.getY(i));
	        if (i + 1 < event.getPointerCount())
	            sb.append(";");
	    }
	    sb.append("]");
	    Log.d(TAG, sb.toString());
	}

	/** Determine the space between the first two fingers */
	private float spacing(MotionEvent event) {
	    float x = event.getX(0) - event.getX(1);
	    float y = event.getY(0) - event.getY(1);
	    return FloatMath.sqrt(x * x + y * y);
	}

	/** Calculate the mid point of the first two fingers */
	private void midPoint(PointF point, MotionEvent event) {
	    float x = event.getX(0) + event.getX(1);
	    float y = event.getY(0) + event.getY(1);
	    point.set(x / 2, y / 2);
	}
}


