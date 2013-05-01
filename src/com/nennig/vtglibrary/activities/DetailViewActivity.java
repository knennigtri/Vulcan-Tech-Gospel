package com.nennig.vtglibrary.activities;


import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.nennig.constants.AppConfig;
import com.nennig.constants.AppConstants;
import com.nennig.constants.AppManager;
import com.nennig.vtglibrary.R;
import com.nennig.vtglibrary.custobjs.MatrixID;
import com.nennig.vtglibrary.custobjs.MovePins;
import com.nennig.vtglibrary.custobjs.PropMove;
import com.nennig.vtglibrary.custobjs.SingletonMovePinMap;
import com.nennig.vtglibrary.custobjs.SingletonMatrixMap;
import com.nennig.vtglibrary.draw.VTGMove;

@SuppressLint("NewApi")
public class DetailViewActivity extends BaseActivity{
	private static final String TAG = AppConfig.APP_PNAME + ".DetialViewActivity";

	private MatrixID _curMatrixID;
    private String _curSet = "";
    
    static PropMove pMove = new PropMove();
    
    //For Testing Purposes ONLY
//    static {
//    	pMove.m13_name = "This is m13";
//    	pMove.m11_name = "this is m11";
//    }
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        
        //Get the current set and matrixID for this detail view
        SharedPreferences sP = getSharedPreferences(AppConstants.VTG_PREFS, MODE_PRIVATE);
        _curMatrixID = new MatrixID(sP.getString(AppConstants.CUR_MATRIX_ID, "0x0x0"));
        _curSet = sP.getString(AppConstants.CUR_SET, AppConstants.SET_1313);
        Log.d(TAG, "Cur Matrix " + _curMatrixID);
        
        //Get the singleton and get the information for the detail view
  		SingletonMatrixMap sPoi = SingletonMatrixMap.getSingletonPoiMoveMap(this);
  		pMove = sPoi.getPoiMove(_curMatrixID.getMatrixID());
  		
  		setupMove();
	}
	
	private void setupMove(){
        setTitle(AppConstants.setTitleString(isLiteVersion(), _curSet));
		
		//Get the singleton to create the move view for this matrixID
  		SingletonMovePinMap sMovePins = SingletonMovePinMap.getSingletonMovePinMap(this, _curSet);
  		MovePins pMovePins = sMovePins.getMovePins(_curMatrixID.getMatrixID());
		
		//Set the move pins to the move view
  		VTGMove drawnMove = (VTGMove) findViewById(R.id.detail_customMoveDraw);

  		if(isLiteVersion() && _curSet.equals(AppConstants.SET_1111)){
  			drawnMove.removePinsAndIcon();
  			drawnMove.addProMessage("This swipe feature is \n available through the \n Pro version only");
  			drawnMove.setOnTouchListener(null);
  		}
  		else
  		{
  			drawnMove.removeProMessage();
  			
	  		InputStream iStream;
			try {
				iStream = getAssets().open(AppConstants.ICON_VIEW_FOLDER + "/" + pMove.getImageFileName(_curSet));
				drawnMove.addPinsAndIcon(pMovePins, iStream);
			} catch (IOException e) {
				drawnMove.addPins(pMovePins);
			}
	  		
			drawnMove.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(((VTGMove) v).iconIsTouched(event.getX(), event.getY())){
						if(_curSet.equals(AppConstants.SET_1313)){
							Intent i = new Intent(DetailViewActivity.this, VideoActivity.class);
							i.putExtra(AppConstants.CUR_SET, _curSet);
							startActivity(i);
						}
						else {
							//TODO Insert the videos for 1111
							Toast.makeText(DetailViewActivity.this, "Videos will be available soon!", Toast.LENGTH_SHORT).show();
						}
					}
					return false;
				}
			});
  		}
		
		final GestureDetector gestureDetector;
        gestureDetector = new GestureDetector(new MyGestureDetector());
        
        mViewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
        initAnimations();
  		
  		//The following code sets up all of the text details for the move
        TextView propText = (TextView) findViewById(R.id.detail_poiText);
        TextView handText = (TextView)findViewById(R.id.detail_handText);

        int pInt = _curMatrixID.getPropID();
        int hInt = _curMatrixID.getHandID();
        String pText = MatrixID.MCategory.getStringLongFromIndex(pInt);
        String hText = MatrixID.MCategory.getStringLongFromIndex(hInt);
        propText.setText(pText);
        handText.setText(hText);
        
				
		//Set the Image Name
		TextView detailTV = (TextView) findViewById(R.id.detail_moveName);
		if(pMove.getName(_curSet).length()>57)
			detailTV.setTextSize(15);
		detailTV.setText(pMove.getName(_curSet));
	}
	
	private Animation mInFromRight;
    private Animation mOutToLeft;
    private Animation mInFromLeft;
    private Animation mOutToRight;
    private ViewFlipper mViewFlipper;
	   private void initAnimations() {
	        mInFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
	                +1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f);
	        mInFromRight.setDuration(500);
	        AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
	        mInFromRight.setInterpolator(accelerateInterpolator);

	        mInFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
	                -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f);
	        mInFromLeft.setDuration(500);
	        mInFromLeft.setInterpolator(accelerateInterpolator);

	        mOutToRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
	                0.0f, Animation.RELATIVE_TO_PARENT, +1.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f);
	        mOutToRight.setDuration(500);
	        mOutToRight.setInterpolator(accelerateInterpolator);

	        mOutToLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, -1.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f,
	                Animation.RELATIVE_TO_PARENT, 0.0f);
	        mOutToLeft.setDuration(500);
	        mOutToLeft.setInterpolator(accelerateInterpolator);

	        final GestureDetector gestureDetector;
	        gestureDetector = new GestureDetector(new MyGestureDetector());

	        mViewFlipper.setOnTouchListener(new OnTouchListener() {

	            public boolean onTouch(View v, MotionEvent event) {
	                if (gestureDetector.onTouchEvent(event)) {
	                    return false;
	                } else {
	                    return true;
	                }
	            }
	        });
	    }
	
	private class MyGestureDetector extends SimpleOnGestureListener {

        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY) {
            System.out.println(" in onFling() :: ");
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                mViewFlipper.setInAnimation(mInFromRight);
                mViewFlipper.setOutAnimation(mOutToLeft);
                _curSet = getNextSet();
                setupMove();
                mViewFlipper.showNext();
                
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                mViewFlipper.setInAnimation(mInFromLeft);
                mViewFlipper.setOutAnimation(mOutToRight);
                _curSet = getPreviousSet();
                setupMove();
                mViewFlipper.showPrevious();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
	
	public String getNextSet(){
		if(_curSet.equals(AppConstants.SET_1111))
			return AppConstants.SET_1313;
		if(_curSet.equals(AppConstants.SET_1313))
			return AppConstants.SET_1111;
		return AppConstants.SET_1313;
	}
	public String getPreviousSet(){
		if(_curSet.equals(AppConstants.SET_1111))
			return AppConstants.SET_1313;
		if(_curSet.equals(AppConstants.SET_1313))
			return AppConstants.SET_1111;
		return AppConstants.SET_1313;
	}
}


