package com.nennig.vulcan.tech.gospel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;

public class BaseActivity extends Activity {
	public static final String TEXT = "com.nennig.vulcan.tech.gospel.TEXT";

	
	public static final String ROOT_FOLDER = Environment.getExternalStorageDirectory().toString();
	private static final String TAG = "com.nennig.dma.truth.or.dare.Base";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        

        
    }

    
    public static void aboutAlert(final Activity c){
    	AlertDialog.Builder alert = new AlertDialog.Builder(c); 

        alert.setTitle("About"); 
        alert.setMessage("Noel Yee and blah blah. Copywrite @ 2012 Kevin Nennig");
        
        alert.setPositiveButton("View Site", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
            	String url = "http://noelyee.weebly.com/index.html";
            	Intent i = new Intent(Intent.ACTION_VIEW);
            	i.setData(Uri.parse(url));
            	c.startActivity(i);
            } 
        }); 
        
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() { 
            public void onClick(DialogInterface dialog, int whichButton) { 
              // Canceled. 
            } 
      }); 
      alert.show();
    }    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
//    	case R.id.menu_main_menu:
//    		startActivity(new Intent(this, MainActivity.class));
//    		finish();
//    		return true;
    	case R.id.menu_about: //TODO Settings page
    		aboutAlert(this);
    		return true;
//    	case R.id.menu_rate_this:
//    		String str ="https://play.google.com/store/apps/details?id=com.nennig.dma.truth.or.dare";
//    		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
//    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
}
