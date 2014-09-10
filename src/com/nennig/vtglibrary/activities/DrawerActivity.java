/**
 * 
 */
package com.nennig.vtglibrary.activities;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.nennig.constants.AppConfig;
import com.nennig.constants.AppConstants;
import com.nennig.constants.AppManager;
import com.nennig.constants.AppConstants.Set;
import com.nennig.constants.Dlog;
import com.nennig.vtglibrary.R;
import com.nennig.vtglibrary.custobjs.VTGToast;
import com.nennig.vtglibrary.managers.VideoManager;

/**
 * @author Kevin Nennig (knennig213@gmail.com)
 *
 */
public class DrawerActivity extends BaseActivity {
	private static final String TAG = "DrawerActivity";
	private static final boolean ENABLE_DEBUG = false;
	
	protected boolean mDrawerEnabled = false;
	protected CharSequence mDrawerTitle, mTitle;
    protected DrawerLayout mDrawerLayout;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected ListView mDrawerList;
	private ArrayList<String> mDrawerItems;
	private OnItemClickListener mDrawerListener;
	
	private static final String mPdfs = "1:3 PDFs";
	private static final String mSuggestion = "Suggestion Box";
	private static final String mLearn = "Learn about VTG";
	private static final String mFacebook = "Join Facebook";
	private static final String mGenerator = "VTG Generator";
	private static final String mDownloadVideos = "Cache All Videos";
	
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
        drawerList.add(mGenerator);       //3
        drawerList.add(mFacebook);      //4
        //TODO implement Learn about VTG
//        drawerList.add(mLearn);           //5
        drawerList.add(mDownloadVideos); //6
//        drawerList.add(mPdfs);    //6
        drawerList.add(mSuggestion); 			 //7
        Dlog.d(TAG, "DrawerList: " + drawerList,ENABLE_DEBUG);
        
        setDrawerItems(drawerList, drawerID, drawerWrapperID, new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {
				selectItem(a, (TextView) view);
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
		private void selectItem(Activity a, TextView tv) {
			Intent intent;
			String url;
			CharSequence name = tv.getText();
			 if(name.equals(Set.ONETHREE.toLabel()))
			 {
	             saveCurSet(Set.ONETHREE.toSetID());
	             intent = new Intent(a,
	                     SelectorActivity.class);
	             startActivity(intent);
	         }
			 else if(name.equals(Set.ONEONE.toLabel()))
			 {
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
			 }
			 else if(name.equals(Set.ONEFIVE.toLabel()))
			 {
	             if(isLiteVersion())
	             {
	                 AppManager.proVersionAlert(a);
	             }
	             else
	             {
	            	 //TODO Unlock 1:5
	                 new VTGToast(a).comingSoonProFeature();
//	                 saveCurSet(Set.ONEFIVE.toSetID());
//	                 intent = new Intent(a,
//	                         SelectorActivity.class);
//	                 startActivity(intent);
	             }
			 }
			 else if(name.equals(mPdfs))
			 {
	             viewPDFList();
			 }
			 else if(name.equals(mDownloadVideos))
			 {
				 //TODO Pro - Finish implementing Download Videos
				 new VTGToast(this).comingSoonProFeature();
//				 viewDownloadsList();
			 }
			 else if(name.equals(mSuggestion))
			 {
	             intent = new Intent(a,HTMLActivity.class);
	             intent.putExtra(AppConstants.HTML_URL, AppConstants.VTG_SURVEY);
	             startActivity(intent);
			 }
			 else if(name.equals(mLearn))
			 {
	             //TODO Lite - coming soon
	             new VTGToast(a).comingSoonFeature();
			 }
			 else if(name.equals(mFacebook))
			 {
	             url = AppConstants.FACEBOOK;
	             intent = new Intent(Intent.ACTION_VIEW);
	             intent.setData(Uri.parse(url));
	             startActivity(intent);
			 }
			 else if(name.equals(mGenerator))
			 {
				 intent = new Intent(a,HTMLActivity.class);
	             intent.putExtra(AppConstants.HTML_URL, AppConstants.VTG_GENERATOR);
	             startActivity(intent);
//	        	 url = AppConstants.VTG_GENERATOR;
//	             startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url)));
			 }
	     }
			 
		 /**
		 * 
		 */
		//TODO cache videos
		private void viewDownloadsList() {	
			if(isLiteVersion()){
				new VTGToast(this).proOnlyFeature();
			}
			else
			{
		        final AlertDialog.Builder alert = new AlertDialog.Builder(this); 
		        alert.setTitle("Cache All Videos");
		        alert.setMessage("Downloading all videos will increase overall responsiveness of this app. " +
		        		"Would you like to download all the videos now?");
		        
		        alert.setPositiveButton("Download", new DialogInterface.OnClickListener() { 
		            public void onClick(DialogInterface dialog, int whichButton) { 
		            	ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		            	NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	
		            	if (mWifi.isConnected()) {
		            	    downloadVideos();
		            	}
		            	else{
		            		alert.setTitle("WARNING");
		            		alert.setMessage("It's suggested you are connected to Wifi when downloading all videos since " +
		            				"downloads can be upwards of 22MB.");
		            		alert.setPositiveButton("Just Download", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									downloadVideos();								
								}
							});
		            		alert.setNeutralButton("Wifi Settings", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									startActivityForResult(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS),100);
								}
							});
							alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {}
								});
		            	}
		            } 
		        });   
		        alert.setNegativeButton("No Thanks", new DialogInterface.OnClickListener() {				
					@Override
					public void onClick(DialogInterface dialog, int which) {}
				});
		        alert.show();
			}
		}
		//Callback once the wifi is turned on.
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			  if (requestCode == 100) {
			     if(resultCode == RESULT_OK){      
			         downloadVideos();          
			     }
			  }
		}
		//downloads all of the videos for the app
		private void downloadVideos() {
			VideoManager.downloadAllVideos(this);
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
