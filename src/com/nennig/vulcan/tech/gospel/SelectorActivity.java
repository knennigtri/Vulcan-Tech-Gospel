package com.nennig.vulcan.tech.gospel;

import java.io.IOException;
import java.io.InputStream;

import com.nennig.constants.AppConstants;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Region;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.support.v4.app.NavUtils;

public class SelectorActivity extends BaseActivity {
	private static final String TAG = "SelectorActivity";
	Spinner poiSp, handSp;
	ImageView[] posIvMatrix = new ImageView[4];
	String[] iconList;
	
	String poiType, handType, prevPoiType, prevHandType;
	RelativeLayout mainLayout;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prop);
        mainLayout = (RelativeLayout) findViewById(R.layout.activity_prop);
        
        poiSp = (Spinner) findViewById(R.id.prop_poi_spinner);
        handSp = (Spinner) findViewById(R.id.prop_hand_spinner);
        posIvMatrix[0] = (ImageView) findViewById(R.id.prop_pos_0);
        posIvMatrix[1] = (ImageView) findViewById(R.id.prop_pos_1);
        posIvMatrix[2] = (ImageView) findViewById(R.id.prop_pos_2);
        posIvMatrix[3] = (ImageView) findViewById(R.id.prop_pos_3);
        poiType = "TS";
        handType = "TS";
        prevPoiType = "TS";
        prevHandType = "TS";
        poiSp.setSelection(0);
        
        refreshIcons();
        
        //Selects the Spinner for Poi
        poiSp.setOnItemSelectedListener(new OnItemSelectedListener() 
        {    
         @Override
         public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
        	 prevPoiType = poiType;
        	 poiType = adapter.getItemAtPosition(i).toString();
        	 Log.d(TAG,"Poi Changed to "+ poiType);
        	 
        	 if(!prevPoiType.equals(poiType)){
        		 refreshIcons();
        	 }	 
        } 
          @Override     
          public void onNothingSelected(AdapterView<?> parentView) 
        {         

         }
        });
        
        //Selects the Spinner for Hand
        handSp.setOnItemSelectedListener(new OnItemSelectedListener() 
        {    
         @Override
         public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
        	 prevHandType = handType;
        	 handType = adapter.getItemAtPosition(i).toString();
        	 Log.d(TAG,"Hand Changed to "+ handType);
        	 
        	 if(!prevHandType.equals(handType)){
        		 refreshIcons();
        	 }	
        } 
          @Override     
          public void onNothingSelected(AdapterView<?> parentView) 
        {         

         }
        });

    }

    /**
     * This refreshes the icons on the screen in correlation with the identifiers selected.
     */
    public void refreshIcons(){
    	Log.d(TAG, "Refreshing icons with Poi: '" + poiType + "' Hand: '" + handType + "'");
    	
    	String ext = "png";
    	Bitmap[] posMatrix = new Bitmap[4];
    	String iconName = "";
    	InputStream iStream = null;
    	final int pTIndex = getTimeDirectionInt(poiType);
    	final int hTIndex = getTimeDirectionInt(handType);
		for(int i = 0; i<posMatrix.length;i++){
			posIvMatrix[i].setOnTouchListener(null); //removes previous listener for icon
			
			boolean positionExists = false;
			
			//This is a check to see if the position exists.
			if(pTIndex == hTIndex)
			{
				positionExists = true;
				//TODO Add four_icon_view
			}
			else 
			{
				if((i == 0) || (i == 1))
					positionExists = true;
				//TODO Add two_icon_view
			}
			
			//If the position is real, then load the icon, else load the default icon
			if(positionExists){
	    		iconName = "i" + pTIndex + "x" + hTIndex + "x" + i +  "." + ext;
			}
			else
			{
				iconName = "";//DEFAULT_ICON + "." + ext;
			}
			try{
				//Get Bitmap for position icon
    			Log.d(TAG, "iconName: " + iconName);
	    		iStream = getAssets().open(AppConstants.ICON_VIEW_FOLDER + "/" + iconName);
	    		Log.d(TAG, "Recieved iStream");
	    		
	    		posMatrix[i] = getBitmapImage(iStream, Math.round((float)(displayWidth / 2.5)));
			} catch (IOException e) {
				Log.d(TAG, "SelectorActivity IOException");
				Log.d(TAG, e.toString());
			}
			
			//Set the position icon and listener
			final int pos = i;
			posIvMatrix[i].setImageBitmap(posMatrix[i]);
			if(positionExists){
		    	posIvMatrix[i].setOnTouchListener(new OnTouchListener() {
		 			@Override
		 			public boolean onTouch(View arg0, MotionEvent arg1) {
		 				//TODO Create links to detailed view
		 				SharedPreferences sP = getSharedPreferences(AppConstants.VTG_PREFS, MODE_PRIVATE);
		 				Editor e = sP.edit();
		 				e.putInt(AppConstants.CUR_POI, pTIndex);
		 				e.putInt(AppConstants.CUR_HAND, hTIndex);
		 				e.putInt(AppConstants.CUR_POS, pos);
		 				e.commit();
		 				startActivity(new Intent(SelectorActivity.this, DetailViewActivity.class));		 
	 				return false;
		 			}
		         });
			}
	    }
		
    }
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.general, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
