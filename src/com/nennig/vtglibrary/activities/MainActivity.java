/**
 * @author Kevin Nennig
 * This is the main activity for this application. It allows users to interact with the application and choose what type
 * of spinning they would like to view. Overall, this is an application to show different moves in poi spinning. It allows 
 * users to search through the different moves and choose the move they would like to work on. These moves and how they are 
 * organized is the work of Noel Yee and David Cantor. The actual development of the application is a complilation between
 * myself (Kevin), Noel Yee, and David Jonathan. 
 */
package com.nennig.vtglibrary.activities;

import java.io.IOException;
import java.io.InputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.nennig.constants.AppConfig;
import com.nennig.constants.AppConstants;
import com.nennig.constants.AppManager;
import com.nennig.constants.AppConstants.Set;
import com.nennig.vtglibrary.R;
import com.nennig.vtglibrary.custobjs.SingletonMatrixMap;
import com.nennig.vtglibrary.custobjs.VTGToast;

public class MainActivity extends DrawerActivity{
	private static final String TAG = AppConfig.APP_TITLE_SHORT + ".MainActivity";
    
	@Override
	public void onCreate(Bundle savedInstanceState) {      
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		createDrawer(MainActivity.this, R.id.main_left_drawer, R.id.main_drawer_wrapper);
		
        AppManager.app_launched(MainActivity.this);

        /*Sets up the Main Image*/
        ImageView mainImage = (ImageView) findViewById(R.id.main_image);
        InputStream iStream = null;
        try {
            iStream = getAssets().open(AppConstants.LOGO_FOLDER + "/" + AppConstants.MAIN_IMAGE);
        } catch (IOException e) {
            Log.d(TAG, "MainActivity IOException");
            Log.d(TAG, e.toString());
        }

        Bitmap image = getBitmapImage(iStream,
                Math.round((float) (displayWidth * .8)));
        mainImage.setImageBitmap(image);
        
		/*
		 * Parse the db file so that the objects are ready when needed.
		 */
		SingletonMatrixMap.getSingletonPoiMoveMap(this);
		
		Button tipOfTheWeek = (Button) findViewById(R.id.main_button_tip);
		tipOfTheWeek.setText("Tip of the Week");
		tipOfTheWeek.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, TipOfTheWkActivity.class));
			}
		});
		Button oneThreeButton = (Button) findViewById(R.id.main_button_13);
		oneThreeButton.setText(AppConstants.Set.ONETHREE.toLabel());
		oneThreeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveCurSet(Set.ONETHREE.toSetID());
	            startActivity(new Intent(MainActivity.this, SelectorActivity.class));
			}
		});
		Button oneOneButton = (Button) findViewById(R.id.main_button_11);
		oneOneButton.setText(AppConstants.Set.ONEONE.toLabel());
		oneOneButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isLiteVersion())
	             {
	                 AppManager.proVersionAlert(MainActivity.this);
	             }
	             else
	             {
					saveCurSet(Set.ONEONE.toSetID());
		            startActivity(new Intent(MainActivity.this, SelectorActivity.class));
	             }
			}
		});
		Button oneFiveButton = (Button) findViewById(R.id.main_button_15);
		oneFiveButton.setText(AppConstants.Set.ONEFIVE.toLabel());
		oneFiveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isLiteVersion())
	             {
	                 AppManager.proVersionAlert(MainActivity.this);
	             }
	             else
	             {
	            	 //TODO unlock 1:5
	            	new VTGToast(MainActivity.this).comingSoonProFeature();
//					saveCurSet(Set.ONEFIVE.toSetID());
//		            startActivity(new Intent(MainActivity.this, SelectorActivity.class));
	             }
			}
		});

	}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_share)
        {
            AppManager.share(this, "Vulcan Tech Gospel is now on Android! Check it out: " + AppConfig.appOnGPlayURL);
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }
}
