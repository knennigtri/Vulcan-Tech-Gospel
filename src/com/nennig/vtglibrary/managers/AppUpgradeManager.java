/**
 * 
 */
package com.nennig.vtglibrary.managers;

import android.app.Activity;
import android.provider.Settings.Secure;

/**
 * @author Kevin Nennig (knennig213@gmail.com)
 *
 */
public class AppUpgradeManager {
	private String android_id;
	
	public AppUpgradeManager(Activity a){
		android_id = Secure.getString(a.getContentResolver(), Secure.ANDROID_ID); 
	}
	
}
