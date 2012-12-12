package com.nennig.vulcan.tech.gospel;

import java.io.IOException;
import java.io.InputStream;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
//				String url = "http://noelyee.weebly.com/index.html";
//            	Intent i = new Intent(Intent.ACTION_VIEW);
//            	i.setData(Uri.parse(url));
//            	startActivity(i);
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
		Bitmap image = getBitmapImage(iStream, (int)(displayHeight / 2));
		
		mainImage.setImageBitmap(image);
    }
    
    
	public static Bitmap getBitmapImage(InputStream iStream, int iconSize) {
		Log.d(TAG, "ReqSize: " + iconSize);
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(iStream, null, options);
        
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap newBitmap = BitmapFactory.decodeStream(iStream, null, options);
        
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d(TAG, "H: " + height + " W: " + width);
        int newWidth = 0;
//        if(height>iconSize){//Requested size is smaller than Bitmap
        	newWidth = (int) Math.round(((float) iconSize / (float) height)*width);
//        }
//        else //Requested size is larger than Bitmap
//        {
//        	float val = ((float) height / (float) iconSize);
//        	newWidth = (int) Math.round((float) val * width);
//        	Log.d(TAG, "val: " + val);
//        }
        int newHeight= iconSize;
        Log.d(TAG, "nH: " + newHeight + " nW: " + newWidth);
    	newBitmap = Bitmap.createScaledBitmap(newBitmap, newWidth, newHeight, true);
        
        return newBitmap;
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.general, menu);
        return true;
    }
}
