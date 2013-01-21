package com.nennig.vulcan.tech.gospel;

import java.io.IOException;
import java.io.InputStream;

import com.nennig.constants.AppConstants;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;


public class DetailZoomActivity extends BaseActivity implements OnTouchListener{
	private static final String TAG = "DetialZoomActivity";
	 	private int poiVal = 0;
	    private int handVal = 0;
	    private int posVal = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_zoom);
        
        SharedPreferences sP = getSharedPreferences(AppConstants.VTG_PREFS, MODE_PRIVATE);
        poiVal = sP.getInt(AppConstants.CUR_POI, 0);
        handVal = sP.getInt(AppConstants.CUR_HAND, 0);
        posVal = sP.getInt(AppConstants.CUR_POS, 0);
        String ext = "png";
        
//        WebView view = (WebView)findViewById(R.id.zoom_webview);
        
        ImageView view = (ImageView) findViewById(R.id.zoom_image);
        InputStream iStream = null;
        String detailName = poiVal + "x" + handVal + "x" + posVal;
        Log.d(TAG, "The file " + detailName + " is loading...");
        
//        view.loadUrl("file:///android_asset/" + DETAIL_VIEW_FOLDER + "/"+ detailName + "." + ext);
        
        Bitmap bitmapImage = null;
		try {
			iStream = getAssets().open(AppConstants.DETAIL_VIEW_FOLDER + "/" + detailName + "." + ext);
			bitmapImage = getBitmapImage(iStream, displayWidth-10, displayHeight );	
			view.setImageBitmap(bitmapImage);
		    view.setOnTouchListener(this);
		} catch (IOException e) {
			Log.d(TAG, "The file " + detailName + " was not found...");
			Log.d(TAG, e.toString());
			Toast.makeText(this, detailName + " was not found.", Toast.LENGTH_SHORT).show();
			finish();
		}   
		
	     
	     
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
