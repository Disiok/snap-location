package com.hackthenorth.snaplocation.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;

public class Utils {
	public static final String TAG = Utils.class.getSimpleName();
	public static final String IMG_DIR = "SnapLocation";

	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
		} catch (Exception e) {
			Log.d(TAG, "Error getting camera instance: " + e.getMessage());
		}
		return c; // returns null if camera is unavailable
	}

	// Unused, for future use
	public static File getOutputMediaFile() {
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMG_DIR);
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(TAG, "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + timeStamp + ".jpg");

		return mediaFile;
	}
	
	// Unused, for future use
	public static boolean saveOutputMedia(byte[] data) {
		File pictureFile = Utils.getOutputMediaFile();
		if (pictureFile == null) {
			Log.d(TAG, "Error creating media file, check storage permissions");
			return false;
		}

		try {
			Log.d(TAG, "Saving captured image");
			FileOutputStream outputStream = new FileOutputStream(pictureFile);
			outputStream.write(data);
			outputStream.close();
			Log.d(TAG, "Finished saving image");
			return true;
		} catch (FileNotFoundException e) {
			Log.d(TAG, "File not found: " + e.getMessage());
			return false;
		} catch (IOException e) {
			Log.d(TAG, "Error accessing file: " + e.getMessage());
			return false;
		}
	}
}
