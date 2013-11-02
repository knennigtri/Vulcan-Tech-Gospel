package com.nennig.vtglibrary.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.nennig.constants.AppConfig;
import com.nennig.constants.AppConstants;
import com.nennig.constants.AppConstants.Set;
import com.nennig.constants.AppManager;
import com.nennig.vtglibrary.R;
import com.nennig.vtglibrary.custobjs.MatrixID;
import com.nennig.vtglibrary.custobjs.MovePins;
import com.nennig.vtglibrary.custobjs.PropMove;
import com.nennig.vtglibrary.custobjs.SingletonMatrixMap;
import com.nennig.vtglibrary.custobjs.SingletonMovePinMap;
import com.nennig.vtglibrary.custobjs.VTGToast;
import com.nennig.vtglibrary.draw.VTGMove;
import com.nennig.vtglibrary.managers.VideoManager;

import java.io.IOException;
import java.io.InputStream;

@SuppressLint("NewApi")
public class DetailViewActivity extends BaseActivity{
	private static final String TAG = AppConfig.APP_TITLE_SHORT + ".DetialViewActivity";

	private MatrixID _curMatrixID;
    private AppConstants.Set _curSet;
    
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
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        //Get the current set and matrixID for this detail view
        SharedPreferences sP = getSharedPreferences(AppConstants.VTG_PREFS, MODE_PRIVATE);
        _curMatrixID = new MatrixID(sP.getString(AppConstants.CUR_MATRIX_ID, "0x0x0"));
        _curSet = AppConstants.Set.getSet(sP.getString(AppConstants.CUR_SET, Set.ONETHREE.toSetID()));
        Log.d(TAG, "Cur Matrix " + _curMatrixID);

        if(sP.getBoolean(AppConstants.DV_FIRSTTIME, true))
            firstRunOfActivity(sP);
        
        //Get the singleton and get the information for the detail view
  		SingletonMatrixMap sPoi = SingletonMatrixMap.getSingletonPoiMoveMap(this);
  		pMove = sPoi.getPoiMove(_curMatrixID);
  		
  		setupMove();
	}

    private void firstRunOfActivity(SharedPreferences sPref) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("New Feature!");
        alert.setMessage("In the pro version you will be able to swipe through 1:1, 1:3, and 1:5 sets " +
                "that have the same Hand/Prop. You can try this feature out on the Lite version by just " +
                "swiping the image and see what happens!");

        alert.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alert.show();
        sPref.edit().putBoolean(AppConstants.DV_FIRSTTIME,false).commit();
    }

    private void setupMove(){
        setTitle(AppConstants.setTitleString(isLiteVersion(), _curSet));
		
		//Get the singleton to create the move view for this matrixID
  		SingletonMovePinMap sMovePins = SingletonMovePinMap.getSingletonMovePinMap(this);
  		MovePins pMovePins = sMovePins.getMovePins(_curMatrixID.toString());
		
		//Set the move pins to the move view
  		VTGMove drawnMove = (VTGMove) findViewById(R.id.detail_customMoveDraw);
        drawnMove.setBackgroundResource(R.color.trans);

  		if(isLiteVersion() && !_curSet.equals(Set.ONETHREE)){ //Lite Statement
  			drawnMove.removePinsAndIcon();
  			InputStream iStream;
			try {
				iStream = getAssets().open(AppConstants.LOGO_FOLDER + "/" + AppConstants.PRO_ONLY_IMAGE);
				drawnMove.addDefaultIcon(iStream);
			} catch (IOException e) {
				Log.d(TAG,e.getMessage());
			}
            drawnMove.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(((VTGMove) v).iconIsTouched(event.getX(), event.getY())){
                        AppManager.proVersionAlert(DetailViewActivity.this);
                    }
                    return false;
                }
            });
  		}
  		else //Pro Statement
  		{
  			drawnMove.removeDefaultIcon();
	  		InputStream iStream;
			try {
                if(_curSet.equals(Set.ONEFIVE))//TODO 1:5 Coming Soon
                {
                    drawnMove.removePinsAndIcon();
                    iStream = getAssets().open(AppConstants.LOGO_FOLDER + "/" + AppConstants.COMING_SOON_IMAGE);
                    drawnMove.addDefaultIcon(iStream);
                }
                else
                {
				    iStream = getAssets().open(AppConstants.ICON_VIEW_FOLDER + "/" + pMove.getImageFileName(_curSet) +
                           "." + pMove.get_fileExt(_curSet));
				    drawnMove.addPinsAndIcon(pMovePins, iStream);
                }
			} catch (IOException e) {
				drawnMove.addPins(pMovePins);
			}

			drawnMove.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(((VTGMove) v).iconIsTouched(event.getX(), event.getY())){
						//Save new set
			          //  getSharedPreferences(AppConstants.VTG_PREFS, MODE_PRIVATE).edit().putString(AppConstants.CUR_SET, _curSet.toString()).commit();
						
//			            if(_curSet.equals(Set.ONETHREE) || _curSet.equals(Set.ONEONE)){
//                            if(isLiteVersion())
//							    new VideoManager(DetailViewActivity.this,VideoActivity.class, AppConstants.Set.getSet(_curSet.toSetID()),
//                                    _curMatrixID, AppConstants.PropType.getPropType(0)).execute();
//                            else
//                            {
//                                int propID = getSharedPreferences(AppConstants.VTG_PREFS, BaseActivity.MODE_PRIVATE).getInt(AppConstants.MOVE_PROP,0);
//                                //TODO Change when making Clubs, Staff, and Hoops Videos
//                                if(propID == 0)//lock for only showing the poi videos that are made
//                                    new VideoManager(DetailViewActivity.this,VideoActivity.class, _curSet,
//                                    		_curMatrixID, AppConstants.PropType.getPropType(0)).execute();
//                                else
//                                    new VTGToast(DetailViewActivity.this).comingSoonProFeature();
//                            }
//						}
//						else {
//                            //TODO coming soon Insert the videos for 1111
//                            new VTGToast(DetailViewActivity.this).comingSoonFeature();
//						}
			            if(isLiteVersion()){ //Lite Statement
			            	new VideoManager(DetailViewActivity.this,VideoActivity.class, AppConstants.Set.getSet(_curSet.toSetID()),
                                    _curMatrixID, AppConstants.PropType.getPropType(0)).execute();
			            }
			            else //Pro Statement
			            {
			            	if(_curSet.equals(Set.ONETHREE) || _curSet.equals(Set.ONEONE)){
			            		int propID = getSharedPreferences(AppConstants.VTG_PREFS, BaseActivity.MODE_PRIVATE).getInt(AppConstants.MOVE_PROP,0);
                                //TODO Change when making Clubs, Staff, and Hoops Videos
                                if(propID == 0)//lock for only showing the poi videos that are made
                                    new VideoManager(DetailViewActivity.this,VideoActivity.class, _curSet,
                                    		_curMatrixID, AppConstants.PropType.getPropType(0)).execute();
                                else
                                    new VTGToast(DetailViewActivity.this).comingSoonProFeature();
			            	}
			            	else	//Implement new 1:5 videos Here
			            		new VTGToast(DetailViewActivity.this).comingSoonFeature();
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
	
    /*
     * Animation for Switching between Sets
     */
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
	/*
	 * End Animation for Sets
	 */
	
	public Set getPreviousSet(){
		if(_curSet.equals(Set.ONEONE))
			return Set.ONEFIVE;
		if(_curSet.equals(Set.ONEFIVE))
			return Set.ONETHREE;
        if(_curSet.equals(Set.ONETHREE))
            return Set.ONEONE;
		return Set.ONETHREE;
	}
	public Set getNextSet(){
        if(_curSet.equals(Set.ONEONE))
            return Set.ONETHREE;
        if(_curSet.equals(Set.ONETHREE))
            return Set.ONEFIVE;
        if(_curSet.equals(Set.ONEFIVE))
            return Set.ONEONE;
        return Set.ONETHREE;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_share)
        {
            String name = pMove.getName(_curSet);
            AppManager.share(this, "Checking out the " + name +
                    " move in the new Vulcan Tech Gospel App. " + AppConfig.appOnGPlayURL);
            return true;
        }
        else if(item.getItemId() == android.R.id.home)
        {
            // app icon in Action Bar clicked; go home
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }
}


