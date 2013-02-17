package com.nennig.vulcan.tech.gospel;

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
import android.widget.VideoView;

import com.nennig.constants.AppConfig;
import com.nennig.constants.AppConstants;

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
        
        videoIndex = getIntent().getExtras().getString(AppConstants.MOVE_INDEX);
        Log.d(TAG, "VideoIndex: " + videoIndex);
        
        TextView tv = (TextView) findViewById(R.id.video_moveName);
        tv.setText(getIntent().getExtras().getString(AppConstants.MOVE_NAME));
        
        final SharedPreferences sP = getSharedPreferences(AppConstants.VTG_PREFS, MODE_PRIVATE);
        videoPropIndex = sP.getInt(AppConstants.MOVE_PROP, 0);
        Log.d(TAG, "videoPropIndex: " + videoPropIndex);
        
		vView = (VideoView)findViewById(R.id.videoView);		
		
		videoIndex = "0x0x0";
		
		setupNewVideo();
		
		vView.start();
				
		vView.setOnPreparedListener(new OnPreparedListener() {
		    @Override
		    public void onPrepared(MediaPlayer mp) {
		        mp.setLooping(true);
		    }
		});
		
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
		
		Spinner spinner = (Spinner) findViewById(R.id.video_prop_selector);
		spinner.setSelection(videoPropIndex);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() 
        {    
         @Override
         public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
        	 int newVideoPropIndex = i;
        	 Log.d(TAG,"Prop Changed to: " + newVideoPropIndex);
        	 
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

    private void changeVideo(int prop, Editor e){
    	videoPropIndex = prop;
    	e.putInt(AppConstants.MOVE_PROP, videoPropIndex);
    	e.commit();
    	//recreate activity
    	setupNewVideo();
    }
    
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
    
//    @Override
//    public void onBackPressed() {
//    	startActivity(new Intent(VideoActivity.this, DetailViewActivity.class));
//    	finish();
//    }
//    
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//        	startActivity(new Intent(VideoActivity.this, DetailViewActivity.class));
//        	finish();    
//        	// your code here
//                return false;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
