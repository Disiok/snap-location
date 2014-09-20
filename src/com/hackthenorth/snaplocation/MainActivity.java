package com.hackthenorth.snaplocation;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static final String TAG = MainActivity.class.getSimpleName();

	// View elements
	Camera mCamera;
	CameraPreview mPreview;
	Button mCaptureButton;

	PictureCallback mPictureCallback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Set fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Resolve view elements
		mCaptureButton = (Button) findViewById(R.id.capture_button);

		// Create our Preview view and set it as the content of our activity.
		mPreview = new CameraPreview(this);
		FrameLayout cameraViewContainer = (FrameLayout) findViewById(R.id.camera_preview);
		cameraViewContainer.addView(mPreview);

		// Create picture callback
		mPictureCallback = new PictureCallback() {

			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				boolean saved = Utils.saveOutputMedia(data);
				if (saved) {
					Toast.makeText(mPreview.getContext(), "Image successfully saved", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(mPreview.getContext(), "Error saving image", Toast.LENGTH_SHORT).show();
				}
			}
		};
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		bindCameraAndPreview();
	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseCameraAndPreview(); // release the camera immediately on pause event
	}

	private void bindCameraAndPreview() {
		// Bind camera
		mCamera = Utils.getCameraInstance();
		
		// Bind preview
		mPreview.setCamera(mCamera);
		
		mCaptureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// get an image from the camera
				mCamera.takePicture(null, null, mPictureCallback);
			}
		});
		
	}
	private void releaseCameraAndPreview() {
		// Release camera
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
		// Release preview
		mPreview.setCamera(null);
	}
	

}
