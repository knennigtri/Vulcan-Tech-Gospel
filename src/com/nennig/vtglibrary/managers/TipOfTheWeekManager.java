/**
 * 
 */
package com.nennig.vtglibrary.managers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.nennig.constants.Dlog;

import android.app.Activity;
import android.content.Context;

/**
 * @author Kevin Nennig (knennig213@gmail.com)
 *
 */
public class TipOfTheWeekManager{
	private static final String TAG = "TipOfTheWkManager";
	private static final boolean ENABLE_DEBUG = true;
	private ArrayList<Tip> list;
	private int curTipIndex;
	private InputStream iS;
	
	public TipOfTheWeekManager(InputStream iStream){
		iS = iStream;
	}
	
	/**
	 * Private class that creates a Tip object for storing and later retrieving information
	 * @author Kevin Nennig (knennig213@gmail.com)
	 *
	 */
	private class Tip{
		private String date, tip, writer;
		public Tip(String d, String t, String w){
			date = d;
			tip = t;
			writer = w;
		}
		public String getDate(){ return date;}
		public String getTip(){ return tip;}
		public String getWriter(){ return writer;}
		public String toString(){
			return date;
		}
	}
	/**
	 * initiates the parser
	 */
	public void parseTips() {
		list = new ArrayList<Tip>();		
		try {
			InputStreamReader iSR = new InputStreamReader(iS);
			BufferedReader bR = new BufferedReader(iSR);
			parseTips(bR);
		} catch (Exception e) {
			Dlog.d(TAG, "Pasring Error: " + e.getMessage() + "**Type: " + e.toString(), ENABLE_DEBUG);
		}
	}
	
	/**
	 * Parser that creates a list of Tips to be displayed
	 * @param bR - input for the parser
	 */
	public void parseTips(BufferedReader bR){
		String nextLineStr;
		Date today = new Date();
		Date tipRelease;
		String lineZero = "";
		try {
			while ((nextLineStr = bR.readLine()) != null) {
				String[] line = nextLineStr.split(",\\|,");
				if(line.length == 3)
				{
					lineZero = line[0];
					Dlog.d(TAG, "Adding Tip for Date: " + lineZero , ENABLE_DEBUG);
					tipRelease = new SimpleDateFormat("MM/dd/yyyy", Locale.US).parse(lineZero);
					if(tipRelease.before(today) || tipRelease.equals(today)){
						list.add(new Tip(lineZero,line[1],line[2]));
					}
				}
				else
				{
					Dlog.d(TAG, "Line does not have 3 identifiers", ENABLE_DEBUG);
				}
			
			}
		} catch (IOException e) {			
			Dlog.d(TAG, "IOException in Parsing: " + e.getMessage() + "**Type: " + e.toString(), ENABLE_DEBUG);
		} catch (ParseException e) {
			Dlog.d(TAG, "ParseException in Parsing: " + e.getMessage() + "**Type: " + e.toString(), ENABLE_DEBUG);
		}
		Dlog.d(TAG, "List: " + list.toString(), ENABLE_DEBUG);
	}

	/**
	 * Use this method with getOlderTip
	 * @return true if there is a Older tip to display
	 */
	public boolean isOlderTip() {
		if(curTipIndex != 0)return true;
		return false;
	}
	/**
	 * get a older tip
	 * @return a older tip than the current one
	 */
	public String getOlderTip() {
		Dlog.d(TAG, "Getting Older Tip", ENABLE_DEBUG);
		curTipIndex--;
		Tip t = list.get(curTipIndex);
		String str = htmlString(t.getDate(), t.getTip(), t.getWriter());
		return str;
	}

	/**
	 * Use this method with getNewerTip
	 * @return true if there is a newer tip to display
	 */
	public boolean isNewerTip() {
		if(curTipIndex != (list.size()-1))return true;
		return false;
	}
	/**
	 * get a newer tip
	 * @return a newer tip than the current one
	 */
	public String getNewerTip() {
		Dlog.d(TAG, "Getting newer Tip", ENABLE_DEBUG);
		curTipIndex++;
		Tip t = list.get(curTipIndex);
		String str = htmlString(t.getDate(), t.getTip(), t.getWriter());
		return str;
	}
	
	/**
	 * Creates the html for the webview that this manager produces
	 * @param date - date for html
	 * @param tip - tip for html
	 * @param writer - writer for html
	 * @return html for the webview
	 */
	public String htmlString(String date, String tip, String writer){
		String code = "<!DOCTYPE html><html>" +
        		"<h1>Tip of the Week:</h1>" +
        		"<h2>" + date + "</h2>" +
        		"<body>" +
        		"<p>\"" + tip + "\"</p>" +
        		"<div align='right'><i>-" + writer + "</i></div>"+
        		"</body>";
		Dlog.d(TAG, "New Code: \n" +code, ENABLE_DEBUG);
		return code;
	}

	/**
	 * Gets the initial tip
	 * @return The first tip to show. If there are no tips a default message is returned
	 */
	public String getInitialTip() {
		 if(!list.isEmpty())
        {
			curTipIndex = list.size()-1;
        	Tip t = list.get(curTipIndex);
    		return htmlString(t.getDate(), t.getTip(), t.getWriter());
        }
		return "<h1>No Tips<h1>";
	}
}
