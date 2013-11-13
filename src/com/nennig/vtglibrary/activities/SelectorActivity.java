package com.nennig.vtglibrary.activities;

import java.io.IOException;
import java.io.InputStream;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.nennig.constants.AppConstants.Set;
import com.nennig.constants.AppManager;
import com.nennig.constants.Dlog;
import com.nennig.vtglibrary.R;
import com.nennig.vtglibrary.custobjs.MatrixID;
import com.nennig.vtglibrary.custobjs.PropMove;
import com.nennig.vtglibrary.custobjs.SingletonMatrixMap;

public class SelectorActivity extends DrawerActivity {
	private static final String TAG = AppConfig.APP_TITLE_SHORT + ".SelectorActivity";
	private boolean ENABLE_DEBUG = false;
	Spinner _propSinner, _handSpinner;
	ImageView[] _posIvMatrix = new ImageView[4];
	String[] iconList;
	
	String _propType, _handType, _prevPoiType, _prevHandType;
	RelativeLayout mainLayout;

	private static SingletonMatrixMap sPoiMap;
	private Set _curSet;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);
        mainLayout = (RelativeLayout) findViewById(R.layout.activity_selector);
        
        createDrawer(SelectorActivity.this, R.id.selector_left_drawer, R.id.selector_drawer_wrapper);
                
        //Create the singleton and get the information for the detail view
		sPoiMap = SingletonMatrixMap.getSingletonPoiMoveMap(this);
		Dlog.d(TAG, "sPropMap: " + sPoiMap.toString(), ENABLE_DEBUG);
		
		SharedPreferences sP = getSharedPreferences(AppConstants.VTG_PREFS, MODE_PRIVATE);
		_curSet = Set.getSet(sP.getString(AppConstants.CUR_SET, Set.ONETHREE.toSetID()));
        
		setTitle(_curSet.toLabel());
		
        _propSinner = (Spinner) findViewById(R.id.prop_poi_spinner);
        _handSpinner = (Spinner) findViewById(R.id.prop_hand_spinner);
        _posIvMatrix[0] = (ImageView) findViewById(R.id.prop_pos_0);
        _posIvMatrix[1] = (ImageView) findViewById(R.id.prop_pos_1);
        _posIvMatrix[2] = (ImageView) findViewById(R.id.prop_pos_2);
        _posIvMatrix[3] = (ImageView) findViewById(R.id.prop_pos_3);
        _propType = MatrixID.MCategory.getStringLongFromIndex(0);
        _handType = MatrixID.MCategory.getStringLongFromIndex(0);
        _prevPoiType = "";
        _prevHandType = "";
        _propSinner.setSelection(0);
        _handSpinner.setSelection(0);
        
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
         public void onItemSelected(AdapterView<?> adapter, View v, int i, long lng) {
        	 _prevPoiType = _propType;
        	 _propType = adapter.getItemAtPosition(i).toString();
        	 Dlog.d(TAG,"Poi Changed to "+ _propType, ENABLE_DEBUG);
        	 
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
         public void onItemSelected(AdapterView<?> adapter, View v, int i, long lng) {
        	 _prevHandType = _handType;
        	 _handType = adapter.getItemAtPosition(i).toString();
        	 Dlog.d(TAG,"Hand Changed to "+ _handType, ENABLE_DEBUG);
        	 
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
    	Dlog.d(TAG, "Refreshing icons with Poi: '" + _propType + "' Hand: '" + _handType + "'", ENABLE_DEBUG);

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
			PropMove pm = sPoiMap.getPoiMove(matrixID); //Get the PropMove info from the PoiMap

			//If the position is real, then load the icon, else load the default icon
			if(positionExists){
				iconName = pm.getImageFileName(_curSet) + "." + pm.get_fileExt(_curSet);
                Dlog.d(TAG,"FName: " + iconName, ENABLE_DEBUG);
			}
			else
			{
				iconName = "";
			}
			try{
				//Get Bitmap for position icon
    			Dlog.d(TAG, "iconName: " + iconName, ENABLE_DEBUG);
	    		iStream = getAssets().open(AppConstants.ICON_VIEW_FOLDER + "/" + iconName);
	    		
	    		posMatrix[posIndex] = getBitmapImage(iStream, Math.round((float)(displayWidth / 2.5)));
			} catch (IOException e) {
				Dlog.d(TAG, "SelectorActivity IOException", ENABLE_DEBUG);
				Dlog.d(TAG, e.toString(), ENABLE_DEBUG);
			}
			
			//Set the position icon and listener
			final String curMatrixID = matrixID.toString();
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
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }
}
