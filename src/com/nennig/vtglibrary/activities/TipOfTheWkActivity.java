/**
 * 
 */
package com.nennig.vtglibrary.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
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
 * TODO Create previous tips in tip of the week.
 */
public class TipOfTheWkActivity extends BaseActivity{
	private static final String TIPS_FILE = "Tip of the Week - Sheet1.csv";
	private static final String TAG = "TipOfTheWkActivity";
	private static final boolean ENABLE_DEBUG = true;
	private WebView wv;
	private ArrayList<Tip> list;
	private int curTipIndex;
	
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
	
//	        mViewFlipper = (ViewFlipper) findViewById(R.id.html_flipper);
//	        initAnimations();
	
	        parseTips();
	        String htmlCode = "";
	        
	        if(!list.isEmpty())
	        {
	        	curTipIndex = list.size()-1;
	        	Tip t = list.get(curTipIndex);
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
			parseTips(bR);
		} catch (Exception e) {
			Dlog.d(TAG, "Pasring Error: " + e.getMessage() + "**Type: " + e.toString(), ENABLE_DEBUG);
		}
	}
	
	/**
	 * 
	 */
	private void parseTips(BufferedReader bR){
		String nextLineStr;
		Date today = new Date();
		Date tipRelease;
		String lineZero = "";
		try {
			while ((nextLineStr = bR.readLine()) != null) {
				String[] line = nextLineStr.split(",");
				if(line.length == 3)
				{
					lineZero = line[0];
					Dlog.d(TAG, "Adding Tip for Date: " + lineZero , ENABLE_DEBUG);
					tipRelease = new SimpleDateFormat("mm/dd/yy").parse(lineZero);
					if(tipRelease.before(today) || tipRelease.equals(today)){
						list.add(new Tip(lineZero,line[1],line[2]));
					}
				}
				else
				{
					Dlog.d(TAG, "Line does not have 3 identifiers", ENABLE_DEBUG);
				}
			
			}
		} catch (IOException e) {			
			Dlog.d(TAG, "IOException in Parsing: " + e.getMessage() + "**Type: " + e.toString(), ENABLE_DEBUG);
		} catch (ParseException e) {
			Dlog.d(TAG, "ParseException in Parsing: " + e.getMessage() + "**Type: " + e.toString(), ENABLE_DEBUG);
		}
		Dlog.d(TAG, "List: " + list.toString(), ENABLE_DEBUG);
	}
	
    /**
	 * @param htmlCode
	 */
	private void refreshTip(String htmlCode) {
        wv.loadData(htmlCode,"text/html", "UTF-8");
		Dlog.d(TAG, "Refreshing Tip", ENABLE_DEBUG);
	}

	/**
	 * @return
	 */
	private String getOlderTip() {
		Dlog.d(TAG, "Getting Older Tip", ENABLE_DEBUG);
		curTipIndex--;
		Tip t = list.get(curTipIndex);
		String str = htmlString(t.getDate(), t.getTip(), t.getWriter());
		return str;
	}

	/**
	 * @return
	 */
	private String getNewerTip() {
		Dlog.d(TAG, "Getting newer Tip", ENABLE_DEBUG);
		curTipIndex++;
		Tip t = list.get(curTipIndex);
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
	        Dlog.d(TAG, "Gesture Created", ENABLE_DEBUG);
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
            	if(curTipIndex != (list.size()-1))
            	{
            		Dlog.d(TAG, "Going to newer Tip.", ENABLE_DEBUG);
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
                mViewFlipper.setInAnimation(mInFromLeft);
                mViewFlipper.setOutAnimation(mOutToRight);
            	if(curTipIndex != 0)
            	{
            		Dlog.d(TAG, "Going to older Tip.", ENABLE_DEBUG);
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
