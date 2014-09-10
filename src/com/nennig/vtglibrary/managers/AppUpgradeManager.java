/**
 * 
 */
package com.nennig.vtglibrary.managers;

import java.io.File;

import com.nennig.constants.AppConfig;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Environment;
import android.provider.Settings.Secure;

/**
 * @author Kevin Nennig (knennig213@gmail.com)
 *
 */
public class AppUpgradeManager {
	private String android_id;
	private static String upgradeFile = AppConfig.APP_DIR + "/infoFile.vtg/";
	
	public AppUpgradeManager(Activity a){
		android_id = Secure.getString(a.getContentResolver(), Secure.ANDROID_ID); 
	}

	private File getUpgradesFile(){
		String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File mDownloadRootDir;
            // http://developer.android.com/guide/topics/data/data-storage.html#filesExternal
            mDownloadRootDir = new File(upgradeFile);
            return mDownloadRootDir;
        }
        return null;
	}
	
	/**
	 * 
	 */
	public void CheckForUpgrades(SharedPreferences sp) {
		File f = getUpgradesFile();
		
		//Check to see if the Upgrade key is in the upgrades file.
		//If it is, then save the upgrade in the preferences.
		
	}
	
}
