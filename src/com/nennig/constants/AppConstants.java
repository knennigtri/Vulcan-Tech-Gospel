/**
 * @author Kevin Nennig
 * This is all constants used in the app. Specifically for passing bundles and saving preferences
 */

package com.nennig.constants;

public class AppConstants {

	/*
	 * Preferences Constants
	 */
	public static final String VTG_PREFS = AppConfig.APP_TITLE_SHORT + ".prefs";
	public static final String CUR_MATRIX_ID = AppConfig.APP_TITLE_SHORT + ".cur.matrix.id";
	public static final String MOVE_PROP = AppConfig.APP_TITLE_SHORT + ".moveprop";
	
	public static final String CUR_SET = AppConfig.APP_TITLE_SHORT + ".cur.set";
    public static final String DV_FIRSTTIME = AppConfig.APP_TITLE_SHORT + ".detailViewfirstTime";
	
	/*
	 * App Constants
	 */
	public static final String ICON_VIEW_FOLDER = "iconView";
    public static final String LOGO_FOLDER = "logos";
	public static final String DEFAULT_ICON = "default_icon.png";
    public static final String MAIN_IMAGE = "mainLogo.png";
    public static final String PRO_ONLY_IMAGE = "proOnlyLogo.png";
    public static final String COMING_SOON_IMAGE = "comingSoonLogo.png";
    
	/*
	 * Constants for the legacy content
	 */
    public static String vtg2Index1Of3_youtube = "http://www.youtube.com/watch?v=gT6SKnBiZ1Q";
    public static String vtg2Index2Of3_youtube = "http://www.youtube.com/watch?v=evUnR4God6Q";
    public static String vtg2Index3Of3_youtube = "http://www.youtube.com/watch?v=fbdJOOkniF0";
    public static String vtg2Index1Of3_pdf = "http://noelyee.weebly.com/uploads/7/2/9/6/7296674/vtg2index.pdf";
    public static String vtg2Index2Of3_pdf = "http://noelyee.weebly.com/uploads/7/2/9/6/7296674/vtg2index2of3.pdf";
    public static String vtg2Index3Of3_pdf = "http://noelyee.weebly.com/uploads/7/2/9/6/7296674/vtg2index30f3.pdf";

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
    public static final String VTG_GENERATOR = "http://www.noelyee.com/VTGgenerator.html";

   public enum Set{
       ONEONE {
           @Override
            public String toString(){
                return super.toString();
           }
       }, ONETHREE {
           @Override
           public String toString(){
               return super.toString();
           }
       },ONEFIVE {
           @Override
           public String toString(){
               return super.toString();
           }
       };
       public String toSetID(){
           if(this.equals(ONEONE))
               return "11";
           if(this.equals(ONEFIVE))
               return "15";
           return "13";
       }
        public String toLabel(){
            if(this.equals(ONEONE))
                return "1:1::1:1";
            if(this.equals(ONEFIVE))
                return "1:5::1:5";
            return "1:3::1:3";
        }
       public static Set getSet(String str){
           if(str.equals(ONEONE.toString()) || str.equals(ONEONE.toSetID()) || str.equals(ONEONE.name()))
               return ONEONE;
           if(str.equals(ONEFIVE.toString()) || str.equals(ONEFIVE.toSetID()) || str.equals(ONEFIVE.name()))
               return ONEFIVE;
           else
               return ONETHREE;
       }
   }
    public enum PropType{
        POI  {
            @Override
            public String toString(){
                return "Poi";
            }
        },
        CLUBS  {
            @Override
            public String toString(){
                return "Clubs";
            }
        },
        DSTAFF  {
            @Override
            public String toString(){
                return "Double Staffs";
            }
        },
        HOOPS  {
            @Override
            public String toString(){
                return "Mini Hoops";
            }
        };

        public String toPropID(){
            if(this.equals(CLUBS))
                return "club";
            if(this.equals(DSTAFF))
                return "dstaff";
            if(this.equals(HOOPS))
                return "hoops";
            return "poi";
        }

        public static PropType getPropType(int i){
            switch(i){
                case 0:
                    return POI;
                case 1:
                    return CLUBS;
                case 2:
                    return DSTAFF;
                case 3:
                    return HOOPS;
                default:
                    return POI;
            }
        }
    }
}


