/**
 * @author Kevin Nennig
 * This is all constants used in the app. Specifically for passing bundles and saving preferences
 */

package com.nennig.constants;

import java.util.HashMap;
import java.util.Map;

public class AppConstants {

	/*
	 * Preferences Constants
	 */
	public static final String VTG_PREFS = AppConfig.APP_PNAME + ".prefs";
	public static final String CUR_POI = AppConfig.APP_PNAME + ".cur.poi";
	public static final String CUR_HAND = AppConfig.APP_PNAME + ".cur.hand";
	public static final String CUR_POS = AppConfig.APP_PNAME + ".cur.pos";
	public static final String MOVE_NAME = AppConfig.APP_PNAME + ".move.name";
	public static final String MOVE_INDEX = AppConfig.APP_PNAME + ".move.index";
	public static final String MOVE_PROP = AppConfig.APP_PNAME + ".move.Prop";	
	
	
	/*
	 * App Constants
	 */
	public static final String DETAIL_VIEW_FOLDER = "detailView";
	public static final String ICON_VIEW_FOLDER = "iconView";
	public static final String DEFAULT_ICON = "default_icon";
	public static final String MAIN_IMAGE = "main_image.jpg";
	
	/*
	 * Constants for the youtube videos
	 */
    public static String vtg2Index1Of3 = "http://www.youtube.com/watch?v=gT6SKnBiZ1Q";
    public static String vtg2Index2Of3 = "http://www.youtube.com/watch?v=evUnR4God6Q";
    public static String vtg2Index3Of3 = "http://www.youtube.com/watch?v=fbdJOOkniF0";
    
    /*
     * Constants used for the inflator on every page
     */
    //http://misha.beshkin.lv/android-add-paypal-donation-page-to-app/
    public static String PAYPAL = "https://www.paypal.com/cgi-bin/webscr?" +
    		"cmd=_donations&" +
    		"business=kissena%40hotmail%2ecom&" +
    		"lc=EE&" +
    		"item_name=Mobile%20apps&currency_code=USD&" +
    		"bn=PP%2dDonationsBF%3abtn_donate_SM%2egif%3aNonHosted";
    public static String FACEBOOK = "https://www.facebook.com/groups/113059425470308/";
    public static String RATEAPP = DevConstants.GOOGLE_PLAY + AppConfig.APP_PNAME;
    
    //This static HashMap holds all the special details for all of the details of each move.
    //The key is a identifier of where it is in the matirx
    //[0] is the name
    //[1] is the url
    //[2] is the start time
    //[3] is the end time
    public static Map<String, String[]> detailMap = new HashMap<String, String[]>();
    static {
    	detailMap.put("m0x0x0", new String[] {"2 Petal Spin Flower Vertical Orientation", vtg2Index1Of3, "1:02","1:16"});
	    detailMap.put("m0x0x1", new String[] {"2 Petal Spin Flower Horizontal Orientation", "", "",""});
	    detailMap.put("m0x0x2", new String[] {"4 Petal Antispin Flower Diamond Orientation", "", "",""});
	    detailMap.put("m0x0x3", new String[] {"4 Petal Antispin Flower Box Orientation", "", "",""});
	    detailMap.put("m0x1x0", new String[] {"Value", "", "",""});
	    detailMap.put("m0x1x1", new String[] {"Value", "", "",""});
	    detailMap.put("m0x2x0", new String[] {"Value", "", "",""});
	    detailMap.put("m0x2x1", new String[] {"Value", "", "",""});
	    detailMap.put("m0x3x0", new String[] {"Value", "", "",""});
	    detailMap.put("m0x3x1", new String[] {"Value", "", "",""});
	    detailMap.put("m1x0x0", new String[] {"Value", "", "",""});
	    detailMap.put("m1x0x1", new String[] {"Value", "", "",""});
	    detailMap.put("m1x1x0", new String[] {"2 Petal Spin Flower Vertical Orientation", "", "",""});
	    detailMap.put("m1x1x1", new String[] {"2 Petal Spin Flower Horizontal Orientation", "", "",""});
	    detailMap.put("m1x1x2", new String[] {"4 Petal Antispin Flower Diamond Orientation", "", "",""});
	    detailMap.put("m1x1x3", new String[] {"4 Petal Antispin Flower Box Orientation", "", "",""});
	    detailMap.put("m1x2x0", new String[] {"Value", "", "",""});
	    detailMap.put("m1x2x1", new String[] {"Value", "", "",""});
	    detailMap.put("m1x3x0", new String[] {"Value", "", "",""});
	    detailMap.put("m1x3x1", new String[] {"Value", "", "",""});
	    detailMap.put("m2x0x0", new String[] {"Value", "", "",""});
	    detailMap.put("m2x0x1", new String[] {"Value", "", "",""});
	    detailMap.put("m2x1x0", new String[] {"Value", "", "",""});
	    detailMap.put("m2x1x1", new String[] {"Value", "", "",""});
	    detailMap.put("m2x2x0", new String[] {"2 Petal Spin Flower Vertical Orientation", "", "",""});
	    detailMap.put("m2x2x1", new String[] {"2 Petal Spin Flower Horizontal Orientation", "", "",""});
	    detailMap.put("m2x2x2", new String[] {"4 Petal Antispin Flower Diamond Orientation", "", "",""});
	    detailMap.put("m2x2x3", new String[] {"4 Petal Antispin Flower Box Orientation", "", "",""});
	    detailMap.put("m2x3x0", new String[] {"Value", "", "",""});
	    detailMap.put("m2x3x1", new String[] {"Value", "", "",""});
	    detailMap.put("m3x0x0", new String[] {"Value", "", "",""});
	    detailMap.put("m3x0x1", new String[] {"Value", "", "",""});
	    detailMap.put("m3x1x0", new String[] {"Value", "", "",""});
	    detailMap.put("m3x1x1", new String[] {"Value", "", "",""});
	    detailMap.put("m3x2x0", new String[] {"Value", "", "",""});
	    detailMap.put("m3x2x1", new String[] {"Value", "", "",""});
	    detailMap.put("m3x3x0", new String[] {"2 Petal Spin Flower Vertical Orientation", "", "",""});
	    detailMap.put("m3x3x1", new String[] {"2 Petal Spin Flower Horizontal Orientation", "", "",""});
	    detailMap.put("m3x3x2", new String[] {"4 Petal Antispin Flower Diamond Orientation", "", "",""});
	    detailMap.put("m3x3x3", new String[] {"4 Petal Antispin Flower Box Orientation", "", "",""});
    }
}


