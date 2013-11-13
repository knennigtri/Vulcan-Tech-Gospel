/**
 * This class is for parsing and holding the information for the different poi moves. This class allows for parsing of the csv file 
 * and then easily referencing for the detail view of each poi move.
 */

package com.nennig.vtglibrary.custobjs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.nennig.constants.AppConfig;
import com.nennig.constants.Dlog;

public class SingletonMatrixMap {
	private static final String DB_FILE = "detail_matrix_db.csv";
	private static final String TAG = AppConfig.APP_TITLE_SHORT
			+ ".SingletonMatrixMap";
	private static boolean ENABLE_DEBUG = false;

	private static SingletonMatrixMap ref;
	/**
	 * Holds all PropMove objects that are parsed from the db file
	 */
	public static Map<String, PropMove> poiMoveMap = new HashMap<String, PropMove>();
	/**
	 * Map of all the header indexes for the db file
	 */
	public static Map<String, Integer> headerIndexMap = new HashMap<String, Integer>();

	// For debugging dbFile ONLY-------
	public static void main(String[] args) {
		String location = "D:\\My Documents\\workspace\\Vulcan Tech Gospel\\assets\\"
				+ DB_FILE;
		final SingletonMatrixMap obj = SingletonMatrixMap
				.getSingletonPoiMoveMap(location);
	}

	public static SingletonMatrixMap getSingletonPoiMoveMap(final Object obj) {
		if (ref == null) {
			// parserThread = new Thread(new Runnable() {
			// public void run() {
			ref = new SingletonMatrixMap(obj);
			// }
			// });
			// parserThread.start();
		}
		return ref;
	}

	// public static boolean ThreadFinished(){
	// return parserThread.isAlive();
	// }

	/**
	 * Creating a factory object so that it can be parsed from Context or from
	 * String. *Strictly for debugging purposes.
	 * 
	 * @param obj
	 */
	private SingletonMatrixMap(Object obj) {
		if (obj != null) {
			if (obj instanceof Context)
				createSingleton((Context) obj);
			if (obj instanceof String)
				createSingleton((String) obj);
		}
	}

	private void createSingleton(String str) {
		try {
			BufferedReader bR = new BufferedReader(new FileReader(str));
			parseDBInfo(bR);
		} catch (Exception e) {
			Dlog.d(TAG, "createSingleton(str) Error: " + e.getMessage(), ENABLE_DEBUG);
		}
	}

	private void createSingleton(Context c) {
		try {
			InputStream iS = c.getAssets().open(DB_FILE);
			InputStreamReader iSR = new InputStreamReader(iS);
			BufferedReader bR = new BufferedReader(iSR);
			parseDBInfo(bR);
		} catch (Exception e) {
			Dlog.d(TAG, "createSingleton(c) Error: " + e.getMessage(), ENABLE_DEBUG);
		}
	}

	/**
	 * This method parses the csv file and puts it into a hashmap of PoiMoves
	 * Objects
	 * 
	 * @param bR
	 *            input reader
	 */
	private void parseDBInfo(BufferedReader bR) {
		boolean readHeaders = true;
		try {
			String nextLineStr;
			String[] nextLineParse;

			while ((nextLineStr = bR.readLine()) != null) {
				Dlog.d(TAG, "LINE: " + nextLineStr, ENABLE_DEBUG);
				nextLineParse = nextLineStr.split(",");

				if (readHeaders) {
					getheaderIndexes(nextLineParse);
					readHeaders = false;
				} else {
					PropMove pM = parsePoiMoveLine(nextLineParse);
					Dlog.d(TAG, "PM: " + pM.getMoveID().toString(), ENABLE_DEBUG);
					poiMoveMap.put(pM.getMoveID().toString(), pM);
				}
			}
		} catch (Exception e) {
			Dlog.d(TAG, "parseDBInfo Exception: " + e.toString(), ENABLE_DEBUG);
		} finally {
			Dlog.d(TAG, "Finished Parse", ENABLE_DEBUG);
		}
	}

	/**
	 * This parses each line of the db and then creates the PropMove
	 * 
	 * @param nextLineParse
	 *            - db Line to be parsed
	 * @return - new PropMove
	 */
	private PropMove parsePoiMoveLine(String[] nextLineParse) {
		PropMove pm = new PropMove();
		for (int i = 0; i < nextLineParse.length; i++) {
			for (String field : pm.getDBFields()) {
				String index = getDBColumnName(field);
				if (index != "") { // skips any PropMove fields that are not DB
									// fields
					if (headerIndexMap.get(index).equals(i)) {
						Dlog.d(TAG, "parsePoiMoveLine adding: " + field, ENABLE_DEBUG);
						pm.add(field, nextLineParse[i]);
						break;
					}
				}
			}
		}
		return pm;
	}

	/**
	 * This parses the fields in PropMove according to the db standard and then
	 * puts the header names into the indexMap
	 */
	private void createIndexMap() {
		PropMove pm = new PropMove();
		// iterates through all fields in the PropMove class so that the indexes
		// for each field can be found in the db file
		for (String field : pm.getDBFields()) {
			String columnName = getDBColumnName(field);
			if (!columnName.equals(""))
				headerIndexMap.put(columnName, -1);
		}
	}

	/**
	 * Simple String modifier to create the proper readable db headers from the
	 * PropMove class fields
	 * 
	 * @param fieldName
	 *            - field name from PropMove class that will used to create the
	 *            db header field
	 * @return - db header field
	 */
	private String getDBColumnName(String fieldName) {
		if (fieldName.toLowerCase().equals("moveID".toLowerCase())) // checks
																	// for UID
																	// in the db
																	// file
			return fieldName.toLowerCase();
		else {
			String[] split1 = fieldName.split("_");
			if (split1.length > 1) // Make sure it parsed correctly
			{
				String moveSet = split1[0];
				if (moveSet.length() > 2) // Checks for the identifier of "m"
											// and then the number to identify
											// the type of move "11","13","15"
				{
					String moveColumn = split1[1];
					String columnName = moveSet.charAt(1) + ":"
							+ moveSet.charAt(2) + "<"
							+ moveColumn.toLowerCase() + ">";
					return columnName;
				}
			} else
				Dlog.w(TAG, "Field: '" + fieldName
						+ "' could not parse because token: '_' was found.", ENABLE_DEBUG);
		}
		return "";
	}

	/**
	 * Takes in the header line and then assigns all PropMove fields to an
	 * associated index in the db file.
	 * 
	 * @param nextLineParse
	 */
	private void getheaderIndexes(String[] nextLineParse) {
		createIndexMap();
		for (int i = 0; i < nextLineParse.length; i++) {
			if (headerIndexMap.containsKey(nextLineParse[i].toLowerCase()))
				headerIndexMap.put(nextLineParse[i].toLowerCase(), i);
		}
		Dlog.d(TAG, "Headers: " + headerIndexMap.toString(), ENABLE_DEBUG);
	}

	public PropMove getPoiMove(MatrixID moveID) {
		return poiMoveMap.get(moveID.toString());
	}

	@Override
	public String toString() {
		String str = "{";
		for (PropMove pm : poiMoveMap.values()) {
			str = str + "; " + pm.getMoveID();
		}
		str = str + "}";
		return str;
	}
}
