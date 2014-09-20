package com.hackthenorth.snaplocation;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	public static final String TAG = CameraPreview.class.getSimpleName();
	SurfaceHolder mHolder;
	Camera mCamera;

	public CameraPreview(Context context) {
		super(context);

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (mCamera != null) {
			startPreview();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.

		if (mHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			// ignore: tried to stop a non-existent preview
		}

		// set preview size and make any resize, rotate or
		// reformatting changes here

		// start preview with new settings
		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();

		} catch (Exception e) {
			Log.d(TAG, "Error starting camera preview: " + e.getMessage());
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// empty. Take care of releasing the Camera preview in your activity.
	}
	
	public void setCamera(Camera camera) {
		if (mCamera != camera) {
			mCamera = camera;
			if (mCamera != null) {
				startPreview();	
			}
		}
	}
	
	private void startPreview() {
		// The Surface has been created, now tell the camera where to draw the
		// preview.
		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.setDisplayOrientation(90);
			mCamera.startPreview();
		} catch (IOException e) {
			Log.d(TAG, "Error setting camera preview: " + e.getMessage());
		}
	}
}
