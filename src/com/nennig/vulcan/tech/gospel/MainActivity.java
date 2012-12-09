package com.nennig.vulcan.tech.gospel;

import java.io.IOException;
import java.io.InputStream;

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
        
        ImageView mainImage = (ImageView) findViewById(R.id.main_image);
        
        InputStream iStream = null;
		try {
			iStream = getAssets().open(MAIN_IMAGE);
		} catch (IOException e) {
			Log.d(TAG, "MainActivity IOException");
			Log.d(TAG, e.toString());
		}
		Log.d(TAG, "Recieved iStream");
		Bitmap image = getBitmapImage(iStream, displayWidth-20, displayHeight-50);
		
		mainImage.setImageBitmap(image);
    }
    
    
	public static Bitmap getBitmapImage(InputStream iStream, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(iStream, null, options);
        
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap newBitmap = BitmapFactory.decodeStream(iStream, null, options);
        
        final int height = options.outHeight;
        final int width = options.outWidth;
        
        int newHeight = 0;
        if(width>reqWidth){
        	newHeight = (int) Math.round(((float) reqWidth / (float) width)*height);
        }
        else
        {
        	newHeight = (int) Math.round(((float) width / (float) reqWidth)*reqHeight);
        }
        int newWidth= reqWidth;
    	newBitmap = Bitmap.createScaledBitmap(newBitmap, newWidth, newHeight, true);
        
        return newBitmap;
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.general, menu);
        return true;
    }
}
