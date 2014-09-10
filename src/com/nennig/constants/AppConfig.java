/**
 * @author Kevin Nennig
 * 
 * This is a config class for  app constants in the AppManager class.
 */

package com.nennig.constants;

import java.util.ArrayList;

import android.os.Environment;

public class AppConfig {

	public final static String APP_TITLE = "Vulcan Tech Gospel";
	public final static String APP_TITLE_SHORT = "VTG";
	public final static String APP_PRO = "Pro";
	public final static String APP_LITE = "Lite";
    public final static String APP_PNAME_VIRTUAL = "nennig.com.VTG";
    
	public static final String LITE_GOOGLEPLAYURL_SHORT = "http://goo.gl/motWI";
	public static final String PRO_GOOGLEPLAYURL_SHORT = "";
	protected static final String PRO_PACKAGE = "com.nennig.vtglibrary.Pro";
	protected static final String LITE_PACKAGE = "com.nennig.vulcan.tech.gospel";
	protected static final String MARKET_URI = "market://details?id=";
	
    
    public final static String APP_DIR = Environment.getExternalStorageDirectory() +
            "/Android/data/"+ AppConfig.APP_PNAME_VIRTUAL;
}


