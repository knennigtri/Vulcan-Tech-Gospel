/**
 * 
 */
package com.nennig.vtglibrary.activities;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.nennig.constants.AppConfig;
import com.nennig.constants.AppConstants;
import com.nennig.constants.AppManager;
import com.nennig.constants.AppConstants.Set;
import com.nennig.constants.Dlog;
import com.nennig.vtglibrary.R;
import com.nennig.vtglibrary.custobjs.VTGToast;

/**
 * @author Kevin Nennig (knennig213@gmail.com)
 *
 */
public class DrawerActivity extends BaseActivity {
	private static final String TAG = "DrawerActivity";
	private static final boolean ENABLE_DEBUG = true;
	
	protected boolean mDrawerEnabled = false;
	protected CharSequence mDrawerTitle, mTitle;
    protected DrawerLayout mDrawerLayout;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected ListView mDrawerList;
	private ArrayList<String> mDrawerItems;
	private OnItemClickListener mDrawerListener;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {      
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void createDrawer(final Activity a, int drawerID, int drawerWrapperID){
        /*Sets up the list of options*/
        ArrayList<String> drawerList=new ArrayList<String>();
        drawerList.add(Set.ONETHREE.toLabel());      //0
        drawerList.add(Set.ONEONE.toLabel());        //1
        drawerList.add(Set.ONEFIVE.toLabel());       //2
        drawerList.add("Download The Vulcan");       //3
        drawerList.add("Help Build this app!");      //4
        drawerList.add("Learn about VTG");           //5
        drawerList.add("Join the Poi Community");    //6
        drawerList.add("VTG Generator"); 			 //7
        Dlog.d(TAG, "DrawerList: " + drawerList,ENABLE_DEBUG);
		
        setDrawerItems(drawerList, drawerID, drawerWrapperID, new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {
				selectItem(a, pos);
			}
        });
	}
	
	   @SuppressLint("NewApi")
		public void setDrawerItems(ArrayList<String> list, int drawerID, int drawerWrapperID, OnItemClickListener onItemClickListener){
	    	mDrawerEnabled = true;
	    	mDrawerItems = list;
	    	mDrawerListener = onItemClickListener;
	    	 mDrawerList = (ListView) findViewById(drawerID);
	         // Set the adapter for the list view
	         mDrawerList.setAdapter(new ArrayAdapter<String>(this,
	                 R.layout.drawer_text_item, mDrawerItems));
	         // Set the list's click listener
	         mDrawerList.setOnItemClickListener(mDrawerListener);
	
	 		mTitle = mDrawerTitle = isLiteVersion() ? AppConfig.APP_TITLE_SHORT +" "+ AppConfig.APP_LITE : AppConfig.APP_TITLE_SHORT +" "+ AppConfig.APP_PRO;
	         mDrawerLayout = (DrawerLayout) findViewById(drawerWrapperID);
	         mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
	         		R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
	
	             /** Called when a drawer has settled in a completely closed state. */
	             public void onDrawerClosed(View view) {
	                 getActionBar().setTitle(mTitle);
	                 invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
	             }
	
	             /** Called when a drawer has settled in a completely open state. */
	             public void onDrawerOpened(View drawerView) {
	                 getActionBar().setTitle(mDrawerTitle);
	                 invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
	             }
	         };
	         
	         // Set the drawer toggle as the DrawerListener
	         mDrawerLayout.setDrawerListener(mDrawerToggle);
	         getActionBar().setDisplayHomeAsUpEnabled(true);
	         getActionBar().setHomeButtonEnabled(true);
	    }
	   
		/** Starts a new activity according to what is clicked */
		private void selectItem(Activity a, int pos) {
			Intent intent;
			String url;
			 switch (pos){
	         case 0: //13
	             saveCurSet(Set.ONETHREE.toSetID());
	             intent = new Intent(a,
	                     SelectorActivity.class);
	             startActivity(intent);
	             break;
	         case 1: //11
	             if(isLiteVersion())
	             {
	                 AppManager.proVersionAlert(a);
	             }
	             else
	             {
	                 saveCurSet(Set.ONEONE.toSetID());
	                 intent = new Intent(a,
	                         SelectorActivity.class);
	                 startActivity(intent);
	             }
	             break;
	         case 2: //15
	             if(isLiteVersion())
	             {
	                 AppManager.proVersionAlert(a);
	             }
	             else
	             {
	            	 //TODO Unlock 15
	                 new VTGToast(a).comingSoonProFeature();
//	                 saveCurSet(Set.ONEFIVE.toSetID());
//	                 intent = new Intent(a,
//	                         SelectorActivity.class);
//	                 startActivity(intent);
	             }
	             break;
	         case 3: //PDF
	             viewPDFList();
	             break;
	         case 4: //Survey
	             intent = new Intent(a,SurveyActivity.class);
	             startActivity(intent);
	             break;
	         case 5: //Learn about VTG
	             //TODO coming soon
	             new VTGToast(a).comingSoonFeature();
	             break;
	         case 6: //Facebook Group
	             url = AppConstants.FACEBOOK;
	             intent = new Intent(Intent.ACTION_VIEW);
	             intent.setData(Uri.parse(url));
	             startActivity(intent);
	             break;
	         case 7: //VTG Generator
	        	 url = AppConstants.VTG_GENERATOR;
	             startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url)));
	        	 break;
			 }
	     }
			 
		 private void viewPDFList() {
			Dialog viewDialog = new Dialog(this);
			viewDialog.getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
					WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
			viewDialog.setTitle("VTG 1:3 Info");

			LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View dialogView = li.inflate(R.layout.view_pdfs, null);
			viewDialog.setContentView(dialogView);
			viewDialog.show();
			ListView lv = (ListView) dialogView.findViewById(R.id.pdf_list);

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
					Dlog.d(TAG, "selectedItem: " + selItemPos, ENABLE_DEBUG);
					String path = "";
					if (selItemPos == 0)
						path = AppConstants.vtg2Index1Of3_pdf;
					if (selItemPos == 1)
						path = AppConstants.vtg2Index1Of3_youtube;
					if (selItemPos == 2)
						path = AppConstants.vtg2Index2Of3_pdf;
					if (selItemPos == 3)
						path = AppConstants.vtg2Index1Of3_youtube;
					if (selItemPos == 4)
						path = AppConstants.vtg2Index3Of3_pdf;
					if (selItemPos == 5)
						path = AppConstants.vtg2Index1Of3_youtube;
					Intent i = new Intent(Intent.ACTION_VIEW);
					if(selItemPos == 0 || selItemPos == 2 || selItemPos == 4)
					{
						i.setDataAndType(Uri.parse(path), "application/pdf");
						i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					}
					else
					{
						i.setData(Uri.parse(path));
					}
					try {
						startActivity(i);
					} catch (ActivityNotFoundException e) {
						i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse(path));
						startActivity(i);
					}
				}
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});

		}
		
		public void saveCurSet(String setNumber){
			SharedPreferences sP = getSharedPreferences(AppConstants.VTG_PREFS, MODE_PRIVATE);
				Editor e = sP.edit();
				e.putString(AppConstants.CUR_SET, setNumber);
				e.commit();
				Dlog.d(TAG, "Current Set: " + setNumber, ENABLE_DEBUG);
		}
	   
	   /* Called whenever we call invalidateOptionsMenu() */
	   @Override
	   public boolean onPrepareOptionsMenu(Menu menu) {
	       if(mDrawerEnabled){
	     	  // If the nav drawer is open, hide action items related to the content view
	     	  boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//	       	menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
	       }
	       return super.onPrepareOptionsMenu(menu);
	   }

	 	@Override
	 	public void setTitle(CharSequence title) {
	 	    mTitle = title;
	 	    getActionBar().setTitle(mTitle);
	 	}
	     
	     @Override
	     protected void onPostCreate(Bundle savedInstanceState) {
	         super.onPostCreate(savedInstanceState);
	         // Sync the toggle state after onRestoreInstanceState has occurred.
	         if(mDrawerEnabled) mDrawerToggle.syncState();
	     }
	     
	     @Override
	     public void onConfigurationChanged(Configuration newConfig) {
	         super.onConfigurationChanged(newConfig);
	         if(mDrawerEnabled) mDrawerToggle.onConfigurationChanged(newConfig);
	     }
	     @Override
	     public boolean onOptionsItemSelected(MenuItem item) {
	     	if (mDrawerToggle.onOptionsItemSelected(item)) 
	     		return true;
	     	else
	     		return super.onOptionsItemSelected(item);
	     }
}
