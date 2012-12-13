package com.nennig.vulcan.tech.gospel;

import java.io.IOException;
import java.io.InputStream;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
	private static final String TAG = "MainActivity";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final Button _3113Button = (Button) findViewById(R.id.main_3113_button);
        _3113Button.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, PropActivity.class);
				startActivity(intent);
			}	
        });
        
        //1:1::1:1 Button
        final Button _button2 = (Button) findViewById(R.id.main_button2);
        _button2.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(MainActivity.this, "1:1::1:1 will be implemented in V2 of this app.", Toast.LENGTH_LONG).show();
			}	
        });
        
        //View PDF Button
        final Button _button3 = (Button) findViewById(R.id.main_button3);
        _button3.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				viewPDFList();
			}	
        });      
        
        ImageView mainImage = (ImageView) findViewById(R.id.main_image);
        
        InputStream iStream = null;
		try {
			iStream = getAssets().open(MAIN_IMAGE);
		} catch (IOException e) {
			Log.d(TAG, "MainActivity IOException");
			Log.d(TAG, e.toString());
		}
		Log.d(TAG, "Recieved iStream");
		Bitmap image = getBitmapImage(iStream, Math.round((float)(displayWidth * .8)));
		
		mainImage.setImageBitmap(image);
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
			public void onItemClick(AdapterView<?> parent, View arg1, int pos, long arg3) { 
				int selItemPos = pos;
				Log.d(TAG, "selectedItem: " + selItemPos);
				String pdf = "";
				if(selItemPos == 0)
					pdf = "http://noelyee.weebly.com/uploads/7/2/9/6/7296674/vtg2index.pdf";
				if(selItemPos == 1)
					pdf = "http://noelyee.weebly.com/uploads/7/2/9/6/7296674/vtg2index2of3.pdf";
				if(selItemPos == 2)
					pdf = "http://noelyee.weebly.com/uploads/7/2/9/6/7296674/vtg2index30f3.pdf";
				Log.d(TAG, "PDFItem: " + pdf);
				Intent i = new Intent(Intent.ACTION_VIEW);
				Uri path = Uri.parse(pdf);
				i.setDataAndType(path, "application/pdf");
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				try{
					startActivity(i);
				}catch(ActivityNotFoundException e)
				{
					i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(pdf));
					startActivity(i);
				}
			} 
			public void onNothingSelected(AdapterView<?> arg0) { } 
		}); 

	}	

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.general, menu);
        return true;
    }
}
