package com.nennig.vulcan.tech.gospel;


import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.nennig.constants.AppConfig;
import com.nennig.constants.AppConstants;
import com.nennig.vulcan.tech.gospel.SingletonPoiMoveMap.PoiMove;

@SuppressLint("NewApi")
public class DetailViewActivity extends BaseActivity implements OnTouchListener{
	private static final String TAG = AppConfig.APP_PNAME + ".DetialViewActivity";
	private static final String accepted_ext = "png";

    
    private int poiVal = 0;
    private int handVal = 0;
    private int posVal = 0;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        
        SharedPreferences sP = getSharedPreferences(AppConstants.VTG_PREFS, MODE_PRIVATE);
        poiVal = sP.getInt(AppConstants.CUR_POI, 0);
        handVal = sP.getInt(AppConstants.CUR_HAND, 0);
        posVal = sP.getInt(AppConstants.CUR_POS, 0);

        TextView poiText = (TextView) findViewById(R.id.detail_poiText);
        TextView handText = (TextView)findViewById(R.id.detail_handText);
        
        poiText.setText(getTimeDirectionString(poiVal));
        handText.setText(getTimeDirectionString(handVal));
		
        final String detailName = poiVal + "x" + handVal + "x" + posVal;	
        
		//Set The Image
		final ImageView iv = (ImageView) findViewById(R.id.detail_image);
		InputStream iStream = null;
			

		//Set the pdf view of the activity
		Bitmap bitmapImage = null;
		try {
			iStream = getAssets().open(AppConstants.DETAIL_VIEW_FOLDER + "/" + detailName + "." + accepted_ext);
			bitmapImage = getBitmapImage(iStream, displayWidth );	
			iv.setImageBitmap(bitmapImage);
			iv.setOnTouchListener(this);
		} catch (IOException e) {
			Log.d(TAG, "The file " + detailName + " was not found...");
			Log.d(TAG, e.toString());
		}
		
		//Create the singleton and get the information for the detail view
		SingletonPoiMoveMap sPoi = SingletonPoiMoveMap.getSingletonPoiMoveMap(this);
		PoiMove pMove = sPoi.getPoiMove(detailName);
		
		//Set the Image Name
		final String textName = pMove.m13_name;
		TextView detailTV = (TextView) findViewById(R.id.detail_photoName);
		if(textName.length()>57)
			detailTV.setTextSize(15);
		detailTV.setText(textName);

//		final String url = pMove.youtubeVideo;
//		final String start = pMove.sTime;
//		final String end = pMove.eTime;
		
		//Set the Button
		Button videoButton = (Button) findViewById(R.id.detail_button);
		videoButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Intent i = new Intent(DetailViewActivity.this, VideoActivity.class);
				i.putExtra(AppConstants.MOVE_INDEX, detailName);
				i.putExtra(AppConstants.MOVE_NAME, textName);
				startActivity(i);
				return false;
			}
		});	
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


