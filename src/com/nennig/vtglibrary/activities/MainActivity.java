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

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.nennig.constants.AppConfig;
import com.nennig.constants.AppConstants;
import com.nennig.constants.AppManager;
import com.nennig.vtglibrary.R;
import com.nennig.vtglibrary.custobjs.SingletonMatrixMap;
import com.nennig.vtglibrary.custobjs.SingletonMovePinMap;

public class MainActivity extends BaseActivity {
	private static final String TAG = AppConfig.APP_TITLE_SHORT + ".MainActivity";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		AppManager.app_launched(MainActivity.this);
		
		//3:1::1:3 Button
		final Button _1313Button = (Button) findViewById(R.id.main_3113_button);
		_1313Button.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				saveCurSet(AppConstants.SET_1313);
				Intent intent = new Intent(MainActivity.this,
						SelectorActivity.class);
				startActivity(intent);
			}
		});
		
		//1:1::1:1 Button
		final Button _1111Button = (Button) findViewById(R.id.main_button2);
		_1111Button.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(isLiteVersion())
				{
					AppManager.proVersionAlert(MainActivity.this);
				}
				else
				{
					saveCurSet(AppConstants.SET_1111);
					Intent intent = new Intent(MainActivity.this,
							SelectorActivity.class);
					startActivity(intent);
				}
			}
		});

		// View PDF Button
		final Button _button3 = (Button) findViewById(R.id.main_button3);
		_button3.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				viewPDFList();
			}
		});

		//Sets up the Main Image
		ImageView mainImage = (ImageView) findViewById(R.id.main_image);
		InputStream iStream = null;
		try {
			iStream = getAssets().open(AppConstants.MAIN_IMAGE);
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
		SingletonMovePinMap.getSingletonMovePinMap(this, AppConstants.SET_1313);

	}

	private void viewPDFList() {
		Dialog viewDialog = new Dialog(this);
		viewDialog.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		viewDialog.setTitle("VTG #2Index");

		LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View dialogView = li.inflate(R.layout.view_pdfs, null);
		viewDialog.setContentView(dialogView);
		viewDialog.show();
		ListView lv = (ListView) dialogView.findViewById(R.id.main_pdf_list);

		dialogView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				dialogView.setVisibility(View.INVISIBLE);
				return false;
			}
		});

		lv.setOnItemClickListener(new ListView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View arg1, int pos,
					long arg3) {
				int selItemPos = pos;
				Log.d(TAG, "selectedItem: " + selItemPos);
				String pdf = "";
				if (selItemPos == 0)
					pdf = "http://noelyee.weebly.com/uploads/7/2/9/6/7296674/vtg2index.pdf";
				if (selItemPos == 1)
					pdf = "http://noelyee.weebly.com/uploads/7/2/9/6/7296674/vtg2index2of3.pdf";
				if (selItemPos == 2)
					pdf = "http://noelyee.weebly.com/uploads/7/2/9/6/7296674/vtg2index30f3.pdf";
				Log.d(TAG, "PDFItem: " + pdf);
				Intent i = new Intent(Intent.ACTION_VIEW);
				Uri path = Uri.parse(pdf);
				i.setDataAndType(path, "application/pdf");
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				try {
					startActivity(i);
				} catch (ActivityNotFoundException e) {
					i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(pdf));
					startActivity(i);
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

	}
	
	private void saveCurSet(String setNumber){
		SharedPreferences sP = getSharedPreferences(AppConstants.VTG_PREFS, MODE_PRIVATE);
			Editor e = sP.edit();
			e.putString(AppConstants.CUR_SET, setNumber);
			e.commit();
			Log.d(TAG, "Current Set: " + setNumber);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.general, menu);
		return true;
	}
}
