/**
 * 
 */
package com.nennig.vtglibrary.activities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.nennig.constants.AppConfig;
import com.nennig.constants.AppManager;
import com.nennig.constants.Dlog;
import com.nennig.vtglibrary.R;

/**
 * @author Kevin Nennig (knennig213@gmail.com)
 *
 */
public class TipOfTheWkActivity extends BaseActivity{
	private static final String TIPS_FILE = "Tip of the Week - Sheet1.csv";
	private static final String TAG = "TipOfTheWkActivity";
	private static final boolean ENABLE_DEBUG = true;
	private WebView wv;
	private ArrayList<Tip> list;
	ListIterator<Tip> iterator;
	
	private class Tip{
		private String date, tip, writer;
		public Tip(String d, String t, String w){
			date = d;
			tip = t;
			writer = w;
		}
		public String getDate(){ return date;}
		public String getTip(){ return tip;}
		public String getWriter(){ return writer;}
	}
	
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_webview);
	        getActionBar().setDisplayHomeAsUpEnabled(true);
	
	        parseTips();
	        
	        String htmlCode = "";
//	        date = "Dec 12, 2013";
//	        quote = "1:3 stands for hand rotation: prop rotation of one arm.  Hand rotation is often called arm rotation.";
//	        writer = "Noer";
	        
	        if(iterator.hasNext())
	        {
	        	Tip t = iterator.next();
	    		htmlCode = htmlString(t.getDate(), t.getTip(), t.getWriter());
	        }
	        
	        wv = (WebView) findViewById(R.id.survey_webView);
	        wv.setBackgroundColor(0); // transparent
	        
	        refreshTip(htmlCode);
	 }
	
	/**
	 * 
	 */
	private void parseTips() {
		list = new ArrayList<Tip>();		
		try {
			InputStream iS = this.getAssets().open(TIPS_FILE);
			InputStreamReader iSR = new InputStreamReader(iS);
			BufferedReader bR = new BufferedReader(iSR);
			String nextLineStr;
			Date today = new Date();
			Date tipRelease;
			while ((nextLineStr = bR.readLine()) != null) {
				String[] line = nextLineStr.split(",");
				if(line.length == 3)
				{
					tipRelease = new SimpleDateFormat("MM/dd/YYYY").parse(line[0]);
					if(tipRelease.before(today) || tipRelease.equals(today)){
						list.add(new Tip(line[0],line[1],line[2]));
						Dlog.d(TAG, "Adding Tip for Date: " + line[0] , ENABLE_DEBUG);
					}
				}
				else
				{
					Dlog.d(TAG, "Error parsing Tip File", ENABLE_DEBUG);
				}
			
			}
		} catch (Exception e) {
			//TODO Figure out why this is being displayed!! Debug it.
			Dlog.d(TAG, "Pasring Error: " + e.getMessage() + "**Type: " + e.toString(), ENABLE_DEBUG);
		}
		
		iterator = list.listIterator();
	}
	
    /**
	 * @param htmlCode
	 */
	private void refreshTip(String htmlCode) {
        wv.loadData(htmlCode,"text/html", "UTF-8");
	}

	/**
	 * @return
	 */
	private String getOlderTip() {
		Dlog.d(TAG, "Getting Older Tip", ENABLE_DEBUG);
		Tip t = iterator.next();
		String str = htmlString(t.getDate(), t.getTip(), t.getWriter());
		return str;
	}

	/**
	 * @return
	 */
	private String getNewerTip() {
		Dlog.d(TAG, "Getting newer Tip", ENABLE_DEBUG);
		Tip t = iterator.previous();
		String str = htmlString(t.getDate(), t.getTip(), t.getWriter());
		return str;
	}
	
	private String htmlString(String date, String tip, String writer){
		String code = "<!DOCTYPE html><html>" +
        		"<h1>Tip of the Week:</h1>" +
        		"<h2>" + date + "</h2>" +
        		"<body>" +
        		"<p>\"" + tip + "\"</p>" +
        		"<div align='right'><i>-" + writer + "</i></div>"+
        		"</body>";
		Dlog.d(TAG, "New Code: \n" +code, ENABLE_DEBUG);
		return code;
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
            	if(iterator.hasNext())
            	{
	                mViewFlipper.setInAnimation(mInFromRight);
	                mViewFlipper.setOutAnimation(mOutToLeft);
	                String code = getNewerTip();
	                refreshTip(code);
	                mViewFlipper.showNext();
            	}
            	else
            	{
            		Toast.makeText(TipOfTheWkActivity.this, "This is the newest tip.", Toast.LENGTH_SHORT).show();
            	}
                
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            	if(iterator.hasPrevious())
            	{
	                mViewFlipper.setInAnimation(mInFromLeft);
	                mViewFlipper.setOutAnimation(mOutToRight);
	                String code = getOlderTip();
	                refreshTip(code);
	                mViewFlipper.showPrevious();		
	            }
	        	else
	        	{
	        		Toast.makeText(TipOfTheWkActivity.this, "This is the newest tip.", Toast.LENGTH_SHORT).show();
	        	}
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
	 
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        if(item.getItemId() == R.id.menu_share)
	        {
	            AppManager.share(this, "Vulcan Tech Gospel is now on Android! Check it out: " + AppConfig.appOnGPlayURL);
	            return true;
	        }
	        else if(item.getItemId() == android.R.id.home)
	        {
	            // app icon in Action Bar clicked; go home
	        	NavUtils.navigateUpFromSameTask(this);
	            return true;
	        }
	        else
	        {
	            return super.onOptionsItemSelected(item);
	        }
	    }

}
