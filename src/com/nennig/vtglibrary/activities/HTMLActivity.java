package com.nennig.vtglibrary.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.webkit.WebView;

import com.nennig.constants.AppConfig;
import com.nennig.constants.AppConstants;
import com.nennig.constants.AppManager;
import com.nennig.vtglibrary.R;

/**
 * Created by Kevin on 6/8/13.
 */
public class HTMLActivity extends BaseActivity {
//    private static String surveyURL = "https://docs.google.com/forms/d/1FgkWiiCRchB7MD398Fdr3vAP0jsGsHotYCt_Qjhl_f0/viewform";
    private static String surveyFrame = "<iframe src=\"";
    private static String last = "?embedded=true\" height=\"800\" frameborder=\"0\" marginheight=\"0\" marginwidth\"0\">" +
            "Loading...</iframe>";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        String html_url = getIntent().getExtras().getString(AppConstants.HTML_URL);
        
        WebView wv = (WebView) findViewById(R.id.survey_webView);
//        if(API_LEVEL >= Build.VERSION_CODES.HONEYCOMB)
//            Compatibility.setViewLayerTypeSoftware(wv);
        wv.setBackgroundColor(0); // transparent

//        wv.loadUrl(surveyURL);
        wv.loadData(surveyFrame+html_url+last,"text/html", "UTF-8");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_share)
        {
            AppManager.share(this, "Vulcan Tech Gospel is now on Android! Check it out: " + AppConfig.appOnGPlayURL);
            return true;
        }
        else if(item.getItemId() == android.R.id.home)
        {
            // app icon in Action Bar clicked; go home
        	NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }
}