/**
 * This code was modified from:
 * http://www.androidsnippets.com/prompt-engaged-users-to-rate-your-app-in-the-android-market-appirater
 * Much thanks to android snippets!
 * 
 * This class was created to be used my all of my apps so that I can implement an easy changelog, rating, about page, and
 * anything else that might need to be standard in apps It uses the class AppConfig to get the unique 
 * variables about this class.
 */


package com.nennig.constants;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nennig.vtglibrary.R;

public class AppManager {
    
    
    //Preferences Holder
    private final static String PREV_VERSION_CODE = AppConfig.APP_TITLE_SHORT + ".current.version";
    private final static String DONT_SHOW_AGAIN = AppConfig.APP_TITLE_SHORT + ".dontshowagain";
    private final static String LAUNCH_COUNT = AppConfig.APP_TITLE_SHORT + ".launchcount";
    private final static String DATE_FIRST_LAUNCHED = AppConfig.APP_TITLE_SHORT + ".datefirstlaunched";
    
    private final static int DAYS_UNTIL_PROMPT = 3;
    private final static int LAUNCHES_UNTIL_PROMPT = 7;
	private static String TAG = AppConfig.APP_TITLE_SHORT + ".AppManager";

    /**
     * Method to initialize the AppManager. Checks for amount of times used and current version
     * @param c
     */
    public static void app_launched(Context c) {
        SharedPreferences prefs = c.getSharedPreferences(TAG, 0);
        if (prefs.getBoolean(DONT_SHOW_AGAIN, false)) { return ; }
        
        SharedPreferences.Editor editor = prefs.edit();
        
        /*
         * A counter to ask the user to rate the app every so often
         */
        // Increment launch counter
        long launch_count = prefs.getLong(LAUNCH_COUNT, 0) + 1;
        editor.putLong(LAUNCH_COUNT, launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong(DATE_FIRST_LAUNCHED, 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong(DATE_FIRST_LAUNCHED, date_firstLaunch);
        }
        
        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch + 
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(c, editor);
            }
        }

        /*
         * Checks if there was an update in the app. If there was the update message is displayed
         */
//    	PackageInfo pInfo;
//    	try {
//            pInfo = c.getPackageManager().getPackageInfo(c.getPackageName(), PackageManager.GET_META_DATA);
//            long vCode =  pInfo.versionCode;
//            if (prefs.getLong(PREV_VERSION_CODE, 0) < vCode) {
//                showVersionUpdateDialog(c,vCode);
//                editor = prefs.edit();
//                editor.putLong(PREV_VERSION_CODE, vCode);
//                editor.commit();
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            Log.e(TAG , "Error reading versionCode");
//            e.printStackTrace();
//        }
        
        ChangeLog cl = new ChangeLog(c);
        if (cl.firstRun())
            cl.getLogDialog().show();
//        cl.getFullLogDialog().show();
        
        editor.commit();
    }   
    
    /**
     * Inflater to show updates to the app.
     * @param c
     * @param version
     */
    public static void showVersionUpdateDialog(Context c,long version){
        String lastLogEntry = AppConfig.CHANGE_LOG.get(AppConfig.CHANGE_LOG.size()-1);
        AlertDialog.Builder alert = new AlertDialog.Builder(c); 

        alert.setTitle(AppConfig.APP_TITLE + " Version " + version);
        alert.setMessage(lastLogEntry);
        
        alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	
            } 
        });   
      alert.show();
    }
    
    /**
     * Inflater to ask the user if they would like to rate this app 
     * @param c
     * @param editor
     */
    public static void showRateDialog(final Context c, final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(c);
        dialog.setTitle("Rate " + AppConfig.APP_TITLE);

        LinearLayout ll = new LinearLayout(c);
        ll.setOrientation(LinearLayout.VERTICAL);
        
        TextView tv = new TextView(c);
        tv.setText("If you enjoy using " + AppConfig.APP_TITLE + ", please take a moment to rate it. Thanks for your support!");
        tv.setWidth(240);
        tv.setPadding(4, 0, 4, 10);
        ll.addView(tv);
        
        Button b1 = new Button(c);
        b1.setText("Rate " + AppConfig.APP_TITLE);
        b1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + c.getPackageName())));
                dialog.dismiss();
            }
        });        
        ll.addView(b1);

        Button b2 = new Button(c);
        b2.setText("Remind me later");
        b2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll.addView(b2);

        Button b3 = new Button(c);
        b3.setText("No, thanks");
        b3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean(DONT_SHOW_AGAIN, true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        ll.addView(b3);

        dialog.setContentView(ll);        
        dialog.show();        
    }
    
    /**
     * This is a simple about inflater to show information about me and my work.
     * @param c
     */
    public static void aboutAlert(final Context c){
    	AlertDialog.Builder alert = new AlertDialog.Builder(c); 

        alert.setTitle("About"); 
        alert.setMessage(AppConfig.ABOUT_MESSAGE);
        
        alert.setPositiveButton("Website", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	String url = DevConstants.MY_WEBSITE;
            	Intent i = new Intent(Intent.ACTION_VIEW);
            	i.setData(Uri.parse(url));
            	c.startActivity(i);
            } 
        }); 
        
        alert.setNeutralButton("Rate this app", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + c.getPackageName())));
            } 
        }); 
        
        alert.setNegativeButton("Donate!", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	Intent i = new Intent(Intent.ACTION_VIEW);
            	i.setData(Uri.parse(AppConstants.PAYPAL));
            	c.startActivity(i);
            } 
      }); 
      alert.show();
    }

    public static void getAboutDialog(final Context context) {
        WebView wv = new WebView(context);

//        if(Integer.parseInt(Build.VERSION.SDK) >= Build.VERSION_CODES.HONEYCOMB)
//            Compatibility.setViewLayerTypeSoftware(wv);
        wv.setBackgroundColor(0); // transparent
//        wv.loadDataWithBaseURL(null, context.getString(R.raw.about), "text/html", "UTF-8",
//                null);
        wv.loadUrl("file:///android_asset/about.html");

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(DevConstants.AM_TITLE)
                .setView(wv)
                .setNegativeButton("Donate!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(AppConstants.PAYPAL));
                        context.startActivity(i);
                    }
                })
                .setNeutralButton("Rate this app", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                    }
                })
                .setPositiveButton("Website", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String url = DevConstants.MY_WEBSITE;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        context.startActivity(i);
                    }
                });

//        if (!full) {
//            // "more ..." button
//            builder.setNegativeButton(DevConstants.CL_SHOW_FULL,
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            getFullLogDialog().show();
//                        }
//                    });
//        }

        builder.show();
    }
    
    public static void proVersionAlert(final Context c){
    	AlertDialog.Builder alert = new AlertDialog.Builder(c);
    	
    	alert.setTitle(AppConfig.PRO_TITLE);
    	alert.setMessage(AppConfig.PRO_MESSAGE);
    	
    	//TODO Update this Pro version inflator
    	
      alert.setNeutralButton("Okay", new DialogInterface.OnClickListener() { 
      public void onClick(DialogInterface dialog, int whichButton) { 
        // Canceled. 
      } 
      });
    	
//    	alert.setPositiveButton("Go Pro", new DialogInterface.OnClickListener() { 
//            public void onClick(DialogInterface dialog, int whichButton) { 
//            	String url = DevConstants.GOOGLE_PLAY + "." + AppConfig.APP_PNAME + ".pro";
//            	Intent i = new Intent(Intent.ACTION_VIEW);
//            	i.setData(Uri.parse(url));
//            	c.startActivity(i);
//            } 
//        }); 
//        
//        alert.setNegativeButton("No Thanks", new DialogInterface.OnClickListener() { 
//            public void onClick(DialogInterface dialog, int whichButton) { 
//              // Canceled. 
//            } 
//      }); 
      alert.show();
    }
    
    public static void share(final Context c, String str){
    	Intent intent=new Intent(android.content.Intent.ACTION_SEND);
    	intent.setType("text/plain");
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

    	// Add data to the intent, the receiving app will decide what to do with it.
    	intent.putExtra(Intent.EXTRA_SUBJECT, AppConfig.APP_TITLE);
    	intent.putExtra(Intent.EXTRA_TEXT, str);
    	c.startActivity(Intent.createChooser(intent, "Share with..."));
    }
}