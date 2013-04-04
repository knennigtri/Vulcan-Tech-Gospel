package com.nennig.vtglibrary;

import android.app.Application;
import android.util.Log;

public class VTGLibraryApplication extends Application{
	public boolean isLiteVersion() {
		Log.d("VTGLibrary", getPackageName());
		return getPackageName().toLowerCase().contains("lite"); 
	}
}
