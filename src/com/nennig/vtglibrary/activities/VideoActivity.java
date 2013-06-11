package com.nennig.vtglibrary.activities;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.nennig.constants.AppConfig;
import com.nennig.constants.AppConstants;
import com.nennig.constants.AppConstants.*;
import com.nennig.constants.AppManager;
import com.nennig.vtglibrary.R;
import com.nennig.vtglibrary.custobjs.MatrixID;
import com.nennig.vtglibrary.custobjs.PropMove;
import com.nennig.vtglibrary.custobjs.SingletonMatrixMap;
import com.nennig.vtglibrary.managers.VideoManager;

public class VideoActivity extends BaseActivity {
	private static final String TAG = AppConfig.APP_TITLE + ".DetialViewActivity";
	
    boolean play = true;
    Set _curSet;
    VideoView vView;
    
    int videoPropIndex = 0;
    MatrixID _curMatrixID;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTheme(android.R.style.Theme_Holo_NoActionBar_Fullscreen);
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        
        final SharedPreferences sP = getSharedPreferences(AppConstants.VTG_PREFS, MODE_PRIVATE);
        _curMatrixID = new MatrixID(sP.getString(AppConstants.CUR_MATRIX_ID, "0x0x0"));
		_curSet = Set.getSet(getIntent().getExtras().getString(AppConstants.CUR_SET,Set.ONETHREE.toSetID()));
		if(_curSet == null)
			_curSet = Set.ONETHREE;
        
        //Create the singleton and get the information for the detail view
  		SingletonMatrixMap sPoi = SingletonMatrixMap.getSingletonPoiMoveMap(this);
  		PropMove pMove = sPoi.getPoiMove(_curMatrixID);
        
        Log.d(TAG, "videoPropIndex: " + videoPropIndex);
        
        //Set Video Name
        TextView tvName = (TextView) findViewById(R.id.video_moveName);
        tvName.setText(pMove.getName(_curSet));
        TextView tvMove = (TextView) findViewById(R.id.video_move_hand_prop);
        tvMove.setText("Hand: " + MatrixID.MCategory.getStringAbbrFromIndex(_curMatrixID.getHandID()) +
        		"  Prop: " + MatrixID.MCategory.getStringAbbrFromIndex(_curMatrixID.getPropID())
        		);
        
        
        //Setup Video
		vView = (VideoView)findViewById(R.id.videoView);		
//		setupNewVideo();
        vView.setVideoPath(setupNewVideo().getVideoPath());

		vView.start();
		//Loops the video clip infinitely 		
		vView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
		        mp.setLooping(true);
		        
		    }
		});
		//Allows user to pause/play the move anytime
		vView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(play)
				{
					vView.start();
					play = false;
				}
				else
				{
					vView.pause();
					play = true;
				}
				return false;
			}
		});
		
		//This sets up the spinner for changing different props. 
		final Spinner spinner = (Spinner) findViewById(R.id.video_prop_selector);
		spinner.setSelection(videoPropIndex);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() 
        {    
         @Override
         public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
        	 int newVideoPropIndex = i;
        	 Log.d(TAG,"Prop Changed to: " + newVideoPropIndex);
        	 if(newVideoPropIndex != 0)
        	 {
	        	 if(isLiteVersion())
	        	 {
	        		 AppManager.proVersionAlert(VideoActivity.this);
	        		 spinner.setSelection(0);
	        	 }
	        	 else
	        	 {
	        		 Toast.makeText(VideoActivity.this,
							"This pro feature will be added soon.",
							Toast.LENGTH_LONG).show();
					//TODO uncomment this out when adding props
//	        		 if(videoPropIndex != newVideoPropIndex){
//	        			 changeVideo(newVideoPropIndex, sP.edit());
//	        		 }
	        	 }
        	 }
        } 
          @Override     
          public void onNothingSelected(AdapterView<?> parentView) 
        {         

         }
        });
    }

    /**
     * Updates the current prop used and then makes a call to swap the video
     * @param prop index for new prop
     * @param e editor to commit the preferences
     */
    private void changeVideo(int prop, Editor e){
    	videoPropIndex = prop;
    	e.putInt(AppConstants.MOVE_PROP, videoPropIndex);
    	e.commit();
//    	setupNewVideo();
        setupNewVideo().execute();
    }
    
    /**
     * This retrieves the new video from the VideoManager and then runs the video
     */
    public VideoManager setupNewVideo(){
//    	int videoID = VideoManager.getVideoID(this, _curSet, _curMatrixID.toString(), videoPropIndex);
//		String path = "android.resource://"+getPackageName()+"/" + videoID;
//
//		vView.setVideoURI(Uri.parse(path));

        //TODO FIX!
        return new VideoManager(this,_curSet,
                _curMatrixID, AppConstants.PropType.getPropType(videoPropIndex));

    }
    
    public String getTimeDirectionStringShort(int i){
    	if(i == 0)
    		return "T/S";
    	if(i == 1)
    		return "S/S";
    	if(i == 2)
    		return "T/O";
    	if(i == 3)
    		return "S/O";
    	return "TS";
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.general, menu);
        return true;
    }
}
