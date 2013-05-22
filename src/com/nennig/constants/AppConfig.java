/**
 * @author Kevin Nennig
 * 
 * This is a config class for  app constants in the AppManager class.
 */

package com.nennig.constants;

import java.util.ArrayList;

public class AppConfig {

	public final static String APP_TITLE = "Vulcan Tech Gospel";
	public final static String APP_TITLE_SHORT = "VTG";
   // public final static String APP_PNAME = "com.nennig.vtglibrary";

    public static final String ABOUT_MESSAGE = "Vulcan Tech Gospel was created by David Cantor and Noel Yee." +
    		"Vulcan Tech Gospel Writers:  " +
    		"Brian Thompson, Lorq Nichols, David Cantor, and Noel Yee. " +
    		"App Developer: Kevin Nennig. Video Creation: David Everett.";
    public static final String PRO_TITLE = "VTG Pro Only";
    //TODO Update Pro Message with Pro release
    public static final String PRO_MESSAGE = "Get this feature and more in the VTG Pro! Available by the end of June 2012.";
    
	public static final ArrayList<String> CHANGE_LOG = new ArrayList<String>();
	public static final String appOnGPlayURL = "http://goo.gl/motWI";
	
	static{
		CHANGE_LOG.add("No New Updates");
	}
}


