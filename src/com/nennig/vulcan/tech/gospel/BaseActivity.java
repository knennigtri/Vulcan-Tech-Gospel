package com.nennig.vulcan.tech.gospel;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;

public class BaseActivity extends Activity {
	public static final String VTG_PREFS = "com.nennig.vulcan.tech.gospel.prefs";
	public static final String CUR_POI = "com.nennig.vulcan.tech.gospel.cur.poi";
	public static final String CUR_HAND = "com.nennig.vulcan.tech.gospel.cur.hand";
	public static final String CUR_POS = "com.nennig.vulcan.tech.gospel.cur.pos";
	public static final String DETAIL_VIEW_FOLDER = "detailView";
	public static final String ICON_VIEW_FOLDER = "iconView";
	public static final String DEFAULT_ICON = "default_icon";
	public static final String MAIN_IMAGE = "main_image.jpg";

	// Physical display width and height.
    public static int displayWidth = 0;
    public static int displayHeight = 0;
	
	public static final String ROOT_FOLDER = Environment.getExternalStorageDirectory().toString();
	private static final String TAG = "com.nennig.dma.truth.or.dare.Base";
    @SuppressLint("NewApi")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        Display display = getWindowManager().getDefaultDisplay();
        
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2){
			Point displayBox = new Point();
	        display.getSize(displayBox);
			displayWidth = displayBox.x;          
			displayHeight = displayBox.y;
		} else{
	        displayWidth = display.getWidth();             
	        displayHeight = display.getHeight();  
		}
        
    }
    
    
    
    public int getTimeDirectionInt(String str){
    	if(str.equals(getString(R.string.global_ts)))
    		return 0;
    	if(str.equals(getString(R.string.global_ss)))
    		return 1;
    	if(str.equals(getString(R.string.global_to)))
    		return 2;
    	if(str.equals(getString(R.string.global_so)))
    		return 3;
    	return 0;
    }
    
    public String getTimeDirectionString(int i){
    	if(i == 0)
    		return getString(R.string.global_ts);
    	if(i == 1)
    		return getString(R.string.global_ss);
    	if(i == 2)
    		return getString(R.string.global_to);
    	if(i == 3)
    		return getString(R.string.global_so);
    	return getString(R.string.global_ts);
    }
    
    public static void aboutAlert(final Activity c){
    	AlertDialog.Builder alert = new AlertDialog.Builder(c); 

        alert.setTitle("About"); 
        alert.setMessage("Vulcan Tech Gospel #2 was created by David Cantor and Noel Yee." +
        		"Vulcan Tech Gospel Writers:  " +
        		"Brian Thompson, Lorq Nichols, Kevin Nennig, Davis Cantor and Noel Yee ");
        
        alert.setPositiveButton("View Site", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	String url = "http://noelyee.weebly.com/index.html";
            	Intent i = new Intent(Intent.ACTION_VIEW);
            	i.setData(Uri.parse(url));
            	c.startActivity(i);
            } 
        }); 
        
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
              // Canceled. 
            } 
      }); 
      alert.show();
    }    
    
    public static void helpAlert(final Activity c){
    	
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case R.id.menu_about:
    		aboutAlert(this);
    		return true;
    	case R.id.menu_help:
    		helpAlert(this);
    		return true;
    	case R.id.menu_facebook:
    		String url = "https://www.facebook.com/groups/113059425470308/";
        	Intent i = new Intent(Intent.ACTION_VIEW);
        	i.setData(Uri.parse(url));
        	startActivity(i);
    		return true;
    	case R.id.menu_rate_this:
    		String str ="https://play.google.com/store/apps/details?id=com.nennig.vulcan.tech.gospel";
    		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    //This static HashMap holds all the special details for all of the details of each move.
    //The key is a identifier of where it is in the matirx
    //[0] is the name
    //[1] is the url
    //[2] is the start time
    //[3] is the end time
    public static String vtg2Index1Of3 = "http://www.youtube.com/watch?v=gT6SKnBiZ1Q";
    public static String vtg2Index2Of3 = "http://www.youtube.com/watch?v=evUnR4God6Q";
    public static String vtg2Index3Of3 = "http://www.youtube.com/watch?v=fbdJOOkniF0";
    public static Map<String, String[]> detailMap = new HashMap<String, String[]>();
    static {
    	detailMap.put("m0x0x0", new String[] {"2 Petal Spin Flower Vertical Orientation", vtg2Index1Of3, "1:02","1:16"});
	    detailMap.put("m0x0x1", new String[] {"2 Petal Spin Flower Horizontal Orientation", "", "",""});
	    detailMap.put("m0x0x2", new String[] {"4 Petal Antispin Flower Diamond Orientation", "", "",""});
	    detailMap.put("m0x0x3", new String[] {"4 Petal Antispin Flower Box Orientation", "", "",""});
	    detailMap.put("m0x1x0", new String[] {"Value", "", "",""});
	    detailMap.put("m0x1x1", new String[] {"Value", "", "",""});
	    detailMap.put("m0x2x0", new String[] {"Value", "", "",""});
	    detailMap.put("m0x2x1", new String[] {"Value", "", "",""});
	    detailMap.put("m0x3x0", new String[] {"Value", "", "",""});
	    detailMap.put("m0x3x1", new String[] {"Value", "", "",""});
	    detailMap.put("m1x0x0", new String[] {"Value", "", "",""});
	    detailMap.put("m1x0x1", new String[] {"Value", "", "",""});
	    detailMap.put("m1x1x0", new String[] {"2 Petal Spin Flower Vertical Orientation", "", "",""});
	    detailMap.put("m1x1x1", new String[] {"2 Petal Spin Flower Horizontal Orientation", "", "",""});
	    detailMap.put("m1x1x2", new String[] {"4 Petal Antispin Flower Diamond Orientation", "", "",""});
	    detailMap.put("m1x1x3", new String[] {"4 Petal Antispin Flower Box Orientation", "", "",""});
	    detailMap.put("m1x2x0", new String[] {"Value", "", "",""});
	    detailMap.put("m1x2x1", new String[] {"Value", "", "",""});
	    detailMap.put("m1x3x0", new String[] {"Value", "", "",""});
	    detailMap.put("m1x3x1", new String[] {"Value", "", "",""});
	    detailMap.put("m2x0x0", new String[] {"Value", "", "",""});
	    detailMap.put("m2x0x1", new String[] {"Value", "", "",""});
	    detailMap.put("m2x1x0", new String[] {"Value", "", "",""});
	    detailMap.put("m2x1x1", new String[] {"Value", "", "",""});
	    detailMap.put("m2x2x0", new String[] {"2 Petal Spin Flower Vertical Orientation", "", "",""});
	    detailMap.put("m2x2x1", new String[] {"2 Petal Spin Flower Horizontal Orientation", "", "",""});
	    detailMap.put("m2x2x2", new String[] {"4 Petal Antispin Flower Diamond Orientation", "", "",""});
	    detailMap.put("m2x2x3", new String[] {"4 Petal Antispin Flower Box Orientation", "", "",""});
	    detailMap.put("m2x3x0", new String[] {"Value", "", "",""});
	    detailMap.put("m2x3x1", new String[] {"Value", "", "",""});
	    detailMap.put("m3x0x0", new String[] {"Value", "", "",""});
	    detailMap.put("m3x0x1", new String[] {"Value", "", "",""});
	    detailMap.put("m3x1x0", new String[] {"Value", "", "",""});
	    detailMap.put("m3x1x1", new String[] {"Value", "", "",""});
	    detailMap.put("m3x2x0", new String[] {"Value", "", "",""});
	    detailMap.put("m3x2x1", new String[] {"Value", "", "",""});
	    detailMap.put("m3x3x0", new String[] {"2 Petal Spin Flower Vertical Orientation", "", "",""});
	    detailMap.put("m3x3x1", new String[] {"2 Petal Spin Flower Horizontal Orientation", "", "",""});
	    detailMap.put("m3x3x2", new String[] {"4 Petal Antispin Flower Diamond Orientation", "", "",""});
	    detailMap.put("m3x3x3", new String[] {"4 Petal Antispin Flower Box Orientation", "", "",""});
    }
}
