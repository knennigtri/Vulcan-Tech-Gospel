/**
 * @author Kevin Nennig
 * This is the main activity for this application. It allows users to interact with the application and choose what type
 * of spinning they would like to view. Overall, this is an application to show different moves in poi spinning. It allows 
 * users to search through the different moves and choose the move they would like to work on. These moves and how they are 
 * organized is the work of Noel Yee and David Cantor. The actual development of the application is a complilation between
 * myself (Kevin), Noel Yee, and David Jonathan. 
 */
package com.nennig.vtglibrary.activities;

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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.nennig.constants.AppConfig;
import com.nennig.constants.AppConstants;
import com.nennig.constants.AppConstants.*;
import com.nennig.constants.AppManager;
import com.nennig.vtglibrary.R;
import com.nennig.vtglibrary.custobjs.SingletonMatrixMap;
import com.nennig.vtglibrary.custobjs.SingletonMovePinMap;
import com.nennig.vtglibrary.custobjs.VTGToast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends BaseActivity{
	private static final String TAG = AppConfig.APP_TITLE_SHORT + ".MainActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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

        /*Sets up the list of options*/
        ArrayList<String> listItems=new ArrayList<String>();
        listItems.add(Set.ONETHREE.toLabel());       //0
        listItems.add(Set.ONEONE.toLabel());       //1
        listItems.add(Set.ONEFIVE.toLabel());       //2
        listItems.add("Download The Vulcan");       //3
        listItems.add("Help Build this app!");      //4
        listItems.add("Learn about VTG");           //5
        listItems.add("Join the Poi Community");    //6

        final ArrayAdapter<String> aa = new ArrayAdapter<String>(this,R.layout.main_list_item,R.id.item_text,listItems);
        ListView lv = (ListView) findViewById(R.id.main_listview);
        lv.setAdapter(aa);

        lv.setFadingEdgeLength(10);
        lv.setVerticalFadingEdgeEnabled(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                    Intent intent;
                    switch (pos){
                        case 0: //13
                            saveCurSet(Set.ONETHREE.toSetID());
                            intent = new Intent(MainActivity.this,
                                    SelectorActivity.class);
                            startActivity(intent);
                            break;
                        case 1: //11
                            if(isLiteVersion())
                            {
                                AppManager.proVersionAlert(MainActivity.this);
                            }
                            else
                            {
                                saveCurSet(Set.ONEONE.toSetID());
                                intent = new Intent(MainActivity.this,
                                        SelectorActivity.class);
                                startActivity(intent);
                            }
                            break;
                        case 2: //15
                            if(isLiteVersion())
                            {
                                AppManager.proVersionAlert(MainActivity.this);
                            }
                            else
                            {
                                new VTGToast(MainActivity.this).comingSoonProFeature();
//                                saveCurSet(AppConstants.SET_1515);
//                                intent = new Intent(MainActivity.this,
//                                        SelectorActivity.class);
//                                startActivity(intent);
                            }
                            break;
                        case 3: //PDF
                            viewPDFList();
                            break;
                        case 4: //Survey
                            intent = new Intent(MainActivity.this,SurveyActivity.class);
                            startActivity(intent);
                            break;
                        case 5: //Learn about VTG
                            //TODO coming soon
                            new VTGToast(MainActivity.this).comingSoonFeature();
                            break;
                        case 6: //Facebook Group
                            String url = AppConstants.FACEBOOK;
                            intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);
                            break;
                        default:
                    }

            }
        });


//		//3:1::1:3 Button
//		final Button _1313Button = (Button) findViewById(R.id.main_3113_button);
//		_1313Button.setOnClickListener(new Button.OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				saveCurSet(AppConstants.SET_1313);
//				Intent intent = new Intent(MainActivity.this,
//						SelectorActivity.class);
//				startActivity(intent);
//			}
//		});
//
//		//1:1::1:1 Button
//		final Button _1111Button = (Button) findViewById(R.id.main_button2);
//		_1111Button.setOnClickListener(new Button.OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				if(isLiteVersion())
//				{
//					AppManager.proVersionAlert(MainActivity.this);
//				}
//				else
//				{
//					saveCurSet(AppConstants.SET_1111);
//					Intent intent = new Intent(MainActivity.this,
//							SelectorActivity.class);
//					startActivity(intent);
//				}
//			}
//		});
//
//		// View PDF Button
//		final Button _button3 = (Button) findViewById(R.id.main_button3);
//		_button3.setOnClickListener(new Button.OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				viewPDFList();
//			}
//		});
		
		/*
		 * Parse the db file so that the objects are ready when needed.
		 */
		SingletonMatrixMap.getSingletonPoiMoveMap(this);
		SingletonMovePinMap.getSingletonMovePinMap(this);

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
