/**
 * This is a class to create a custom class that helps the code base stay in the same branch of code.
 * 
 * @author Kevin Nennig
 */

package com.nennig.vtglibrary.managers;

import android.app.Application;
import android.util.Log;

public class VTGLibraryApplication extends Application{
	public boolean isLiteVersion() {
		Log.d("VTGLibrary", getPackageName());
		return getPackageName().toLowerCase().contains("lite"); 
	}
}
