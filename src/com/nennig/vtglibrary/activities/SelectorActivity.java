package com.nennig.vtglibrary.activities;

import java.io.IOException;
import java.io.InputStream;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
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
import android.widget.Spinner;

import com.nennig.constants.AppConstants;
import com.nennig.constants.AppManager;
import com.nennig.vtglibrary.R;
import com.nennig.vtglibrary.custobjs.PropMove;
import com.nennig.vtglibrary.custobjs.SingletonPoiMoveMap;

public class SelectorActivity extends BaseActivity {
	private static final String TAG = "SelectorActivity";
	Spinner _propSinner, _handSpinner;
	ImageView[] _posIvMatrix = new ImageView[4];
	String[] iconList;
	
	String _poiType, _handType, _prevPoiType, _prevHandType;
	RelativeLayout mainLayout;

	private static SingletonPoiMoveMap sPoiMap;
	private String _curSet = "";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        mainLayout = (RelativeLayout) findViewById(R.layout.activity_selector);
        
        //Create the singleton and get the information for the detail view
		sPoiMap = SingletonPoiMoveMap.getSingletonPoiMoveMap(this);
		Log.d(TAG,sPoiMap.toString());
		
		SharedPreferences sP = getSharedPreferences(AppConstants.VTG_PREFS, MODE_PRIVATE);
		_curSet = sP.getString(AppConstants.CUR_SET, AppConstants.SET_1313);
        
		setTitle(AppConstants.setTitleString(isLiteVersion(), _curSet));
		
        _propSinner = (Spinner) findViewById(R.id.prop_poi_spinner);
        _handSpinner = (Spinner) findViewById(R.id.prop_hand_spinner);
        _posIvMatrix[0] = (ImageView) findViewById(R.id.prop_pos_0);
        _posIvMatrix[1] = (ImageView) findViewById(R.id.prop_pos_1);
        _posIvMatrix[2] = (ImageView) findViewById(R.id.prop_pos_2);
        _posIvMatrix[3] = (ImageView) findViewById(R.id.prop_pos_3);
        _poiType = "";
        _handType = "";
        _prevPoiType = "";
        _prevHandType = "";
        _propSinner.setSelection(0);
        
        refreshIcons();
        
        //Selects the Spinner for Prop
        _propSinner.setOnItemSelectedListener(new OnItemSelectedListener() 
        {    
         @Override
         public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
        	 _prevPoiType = _poiType;
        	 _poiType = adapter.getItemAtPosition(i).toString();
        	 Log.d(TAG,"Poi Changed to "+ _poiType);
        	 
        	 if(!_prevPoiType.equals(_poiType)){
        		 refreshIcons();
        	 }	 
        } 
          @Override     
          public void onNothingSelected(AdapterView<?> parentView) 
        {         

         }
        });
        
        //Selects the Spinner for Hand
        _handSpinner.setOnItemSelectedListener(new OnItemSelectedListener() 
        {    
         @Override
         public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
        	 _prevHandType = _handType;
        	 _handType = adapter.getItemAtPosition(i).toString();
        	 Log.d(TAG,"Hand Changed to "+ _handType);
        	 
        	 if(!_prevHandType.equals(_handType)){
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
    	Log.d(TAG, "Refreshing icons with Poi: '" + _poiType + "' Hand: '" + _handType + "'");
    	
    	String ext = "png";
    	Bitmap[] posMatrix = new Bitmap[4];
    	String iconName = "";
    	InputStream iStream = null;
    	final int pTIndex = getTimeDirectionInt(_poiType);
    	final int hTIndex = getTimeDirectionInt(_handType);
		for(int i = 0; i<posMatrix.length;i++){
			_posIvMatrix[i].setOnTouchListener(null); //removes previous listener for icon
			
			boolean positionExists = false;
			
			//This is a check to see if the position exists.
			if(pTIndex == hTIndex)
			{
				positionExists = true;
			}
			else 
			{
				if((i == 0) || (i == 1))
					positionExists = true;
			}
			
			PropMove pm = sPoiMap.getPoiMove(pTIndex + "x" + hTIndex + "x" + i); //Get the PropMove info from the PoiMap
			
			//If the position is real, then load the icon, else load the default icon
			if(positionExists){
				Log.d(TAG,"Current Set: " + _curSet);
				iconName = pm.getImageFileName(_curSet);
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
			_posIvMatrix[i].setImageBitmap(posMatrix[i]);
			if(positionExists){
		    	_posIvMatrix[i].setOnTouchListener(new OnTouchListener() {
		 			@Override
		 			public boolean onTouch(View arg0, MotionEvent arg1) {
		 				String curMatixID = pTIndex + "x" + hTIndex + "x" + pos;
		 				
		 				SharedPreferences sP = getSharedPreferences(AppConstants.VTG_PREFS, MODE_PRIVATE);
		 				Editor e = sP.edit();
		 				e.putString(AppConstants.CUR_MATRIX_ID, curMatixID);
//		 				e.putInt(AppConstants.CUR_POI, pTIndex);
//		 				e.putInt(AppConstants.CUR_HAND, hTIndex);
//		 				e.putInt(AppConstants.CUR_POS, pos);
		 				e.commit();
		 				startActivity(new Intent(SelectorActivity.this, DetailViewActivity.class));		 
	 				return false;
		 			}
		         });
			}
	    }
		
    }
    
//    /**
//     * This gets the icon name depending on the current set that is selected
//     * @param pm poi move that will be displayed
//     * @return the correct file name based on the current poi set
//     */
//	private String getIconName(PropMove pm) {
//		SharedPreferences sP = getSharedPreferences(AppConstants.VTG_PREFS, MODE_PRIVATE);
//		String curSet = sP.getString(AppConstants.CUR_SET, AppConstants.SET_1313);
//		if(curSet.equals(AppConstants.SET_1313)){
//			return "i13_" + pm.m13_image + "." + pm.m13_imageExt;
//		}
//		else if(curSet.equals(AppConstants.SET_1111))
//		{
//			return "i11_" + pm.m11_image + "." + pm.m11_imageExt;
//		}
//		return "";
//	}

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
