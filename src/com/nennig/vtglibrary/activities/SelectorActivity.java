package com.nennig.vtglibrary.activities;

import java.io.IOException;
import java.io.InputStream;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.nennig.constants.AppConfig;
import com.nennig.constants.AppConstants;
import com.nennig.constants.AppManager;
import com.nennig.vtglibrary.R;
import com.nennig.vtglibrary.custobjs.MatrixID;
import com.nennig.vtglibrary.custobjs.PropMove;
import com.nennig.vtglibrary.custobjs.SingletonMatrixMap;

public class SelectorActivity extends BaseActivity {
	private static final String TAG = AppConfig.APP_TITLE_SHORT + ".SelectorActivity";
	Spinner _propSinner, _handSpinner;
	ImageView[] _posIvMatrix = new ImageView[4];
	String[] iconList;
	
	String _propType, _handType, _prevPoiType, _prevHandType;
	RelativeLayout mainLayout;

	private static SingletonMatrixMap sPoiMap;
	private String _curSet = "";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        mainLayout = (RelativeLayout) findViewById(R.layout.activity_selector);
        
        //Create the singleton and get the information for the detail view
		sPoiMap = SingletonMatrixMap.getSingletonPoiMoveMap(this);
		Log.d(TAG, "sPropMap: " + sPoiMap.toString());
		
		SharedPreferences sP = getSharedPreferences(AppConstants.VTG_PREFS, MODE_PRIVATE);
		_curSet = sP.getString(AppConstants.CUR_SET, AppConstants.SET_1313);
        
		setTitle(AppConstants.setTitleString(isLiteVersion(), _curSet));
		
        _propSinner = (Spinner) findViewById(R.id.prop_poi_spinner);
        _handSpinner = (Spinner) findViewById(R.id.prop_hand_spinner);
        _posIvMatrix[0] = (ImageView) findViewById(R.id.prop_pos_0);
        _posIvMatrix[1] = (ImageView) findViewById(R.id.prop_pos_1);
        _posIvMatrix[2] = (ImageView) findViewById(R.id.prop_pos_2);
        _posIvMatrix[3] = (ImageView) findViewById(R.id.prop_pos_3);
        _propType = "";
        _handType = "";
        _prevPoiType = "";
        _prevHandType = "";
        _propSinner.setSelection(0);
        
        refreshIcons();
        
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
        		R.layout.spinner_item,
        		MatrixID.MCategory.getSpinnerArrayList());
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        //Selects the Spinner for Prop
        _propSinner.setAdapter(adp);
        _propSinner.setOnItemSelectedListener(new OnItemSelectedListener() 
        {    
         @Override
         public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
        	 _prevPoiType = _propType;
        	 _propType = adapter.getItemAtPosition(i).toString();
        	 Log.d(TAG,"Poi Changed to "+ _propType);
        	 
        	 if(!_prevPoiType.equals(_propType)){
        		 refreshIcons();
        	 }	 
        } 
          @Override     
          public void onNothingSelected(AdapterView<?> parentView) 
        {         

         }
        });
        
        //Selects the Spinner for Hand
        _handSpinner.setAdapter(adp);
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
    	Log.d(TAG, "Refreshing icons with Poi: '" + _propType + "' Hand: '" + _handType + "'");

    	Bitmap[] posMatrix = new Bitmap[4];
    	String iconName = "";
    	InputStream iStream = null;
    	MatrixID matrixID;
    	final int pTIndex = MatrixID.MCategory.getIndex(_propType);
    	final int hTIndex = MatrixID.MCategory.getIndex(_handType);
		for(int posIndex = 0; posIndex<posMatrix.length;posIndex++){
			_posIvMatrix[posIndex].setOnTouchListener(null); //removes previous listener for icon
			
			boolean positionExists = false;
			
			//This is a check to see if the position exists.
			if(pTIndex == hTIndex)
			{
				positionExists = true;
			}
			else 
			{
				if((posIndex == 0) || (posIndex == 1))
					positionExists = true;
			}
			matrixID = new MatrixID(hTIndex, pTIndex, posIndex);
			PropMove pm = sPoiMap.getPoiMove(matrixID.getMatrixID()); //Get the PropMove info from the PoiMap
			
			//If the position is real, then load the icon, else load the default icon
			if(positionExists){
				Log.d(TAG,"Current Set: " + _curSet);
				iconName = pm.getImageFileName(_curSet);
			}
			else
			{
				iconName = "";
			}
			try{
				//Get Bitmap for position icon
    			Log.d(TAG, "iconName: " + iconName);
	    		iStream = getAssets().open(AppConstants.ICON_VIEW_FOLDER + "/" + iconName);
	    		Log.d(TAG, "Recieved iStream");
	    		
	    		posMatrix[posIndex] = getBitmapImage(iStream, Math.round((float)(displayWidth / 2.5)));
			} catch (IOException e) {
				Log.d(TAG, "SelectorActivity IOException");
				Log.d(TAG, e.toString());
			}
			
			//Set the position icon and listener
			final String curMatrixID = matrixID.getMatrixID();
			_posIvMatrix[posIndex].setImageBitmap(posMatrix[posIndex]);
			if(positionExists){
		    	_posIvMatrix[posIndex].setOnTouchListener(new OnTouchListener() {
		 			@Override
		 			public boolean onTouch(View arg0, MotionEvent arg1) {
		 				SharedPreferences sP = getSharedPreferences(AppConstants.VTG_PREFS, MODE_PRIVATE);
		 				Editor e = sP.edit();
		 				e.putString(AppConstants.CUR_MATRIX_ID, curMatrixID);
		 				e.commit();
		 				startActivity(new Intent(SelectorActivity.this, DetailViewActivity.class));		 
	 				return false;
		 			}
		         });
			}
	    }
		
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_share)
        {
            AppManager.share(this, "Checking out the " + _curSet + " set for Prop: " + MatrixID.MCategory.getStringAbbrFromLong(_propType) +
             " and Hand: " + MatrixID.MCategory.getStringAbbrFromLong(_handType) +
                    " in the new Vulcan Tech Gospel App. " + AppConfig.appOnGPlayURL);
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
