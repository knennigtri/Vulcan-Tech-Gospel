package com.nennig.vtglibrary;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
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
import com.nennig.vtglibrary.R;

public class VideoActivity extends Activity {
	private static final String TAG = AppConfig.APP_PNAME + ".DetialViewActivity";
	
    boolean play = true;
    String videoIndex = "";
    int videoPropIndex = 0;
    VideoView vView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTheme(android.R.style.Theme_Black_NoTitleBar);
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        
        //Get video values from bundle and preferences
        videoIndex = getIntent().getExtras().getString(AppConstants.MOVE_INDEX);
        Log.d(TAG, "VideoIndex: " + videoIndex);
        
        final SharedPreferences sP = getSharedPreferences(AppConstants.VTG_PREFS, MODE_PRIVATE);
        videoPropIndex = sP.getInt(AppConstants.MOVE_PROP, 0);
        Log.d(TAG, "videoPropIndex: " + videoPropIndex);
        
        //Set Video Name
        TextView tv = (TextView) findViewById(R.id.video_moveName);
        tv.setText(getIntent().getExtras().getString(AppConstants.MOVE_NAME));
        
        //Setup Video
		vView = (VideoView)findViewById(R.id.videoView);		
		setupNewVideo();
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
		Spinner spinner = (Spinner) findViewById(R.id.video_prop_selector);
		spinner.setSelection(videoPropIndex);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() 
        {    
         @Override
         public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
        	 int newVideoPropIndex = i;
        	 Log.d(TAG,"Prop Changed to: " + newVideoPropIndex);
        	 
        	 //TODO Add Prop Videos
        	 if(newVideoPropIndex != 0)
        		 Toast.makeText(VideoActivity.this, "Currently only Poi Props are supported", Toast.LENGTH_SHORT).show();
        	 
        	 if(videoPropIndex != newVideoPropIndex){
        		 changeVideo(newVideoPropIndex, sP.edit());
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
    	setupNewVideo();
    }
    
    /**
     * This retrieves the new video from the VideoManager and then runs the video
     */
    public void setupNewVideo(){
    	int videoID = VideoManager.getVideoID(this, videoIndex, videoPropIndex);
		
		String path = "android.resource://"+getPackageName()+"/" + videoID;  //R.raw.v0x0x0;
//		String path = "android.resource://"+getPackageName()+"/raw/v0x0x0.mp4";

		vView.setVideoURI(Uri.parse(path));
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.general, menu);
        return true;
    }
}
