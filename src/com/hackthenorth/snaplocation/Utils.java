package com.hackthenorth.snaplocation;

import android.hardware.Camera;
import android.util.Log;

public class Utils {
	public static final String TAG = Utils.class.getSimpleName();
	
	public static Camera getCameraInstance() {
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	    	Log.d(TAG, "Error getting camera instance: " + e.getMessage());
	    }
	    return c; // returns null if camera is unavailable
	}
}
