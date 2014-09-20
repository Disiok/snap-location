package com.hackthenorth.snaplocation;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends FragmentActivity {
	public static final String TAG = MainActivity.class.getSimpleName();

	// View elements
//	Camera mCamera;
//	CameraPreview mPreview;
//	Button mCaptureButton;
//
//	PictureCallback mPictureCallback;
	
	ViewPager mViewPager;
	MainScreenPagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		mPagerAdapter = new MainScreenPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
//		
//		// Set fullscreen
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//		// Resolve view elements
//		mCaptureButton = (Button) findViewById(R.id.capture_button);
//
//		// Create our Preview view and set it as the content of our activity.
//		mPreview = new CameraPreview(this);
//		FrameLayout cameraViewContainer = (FrameLayout) findViewById(R.id.camera_preview);
//		cameraViewContainer.addView(mPreview);
//
//		// Create picture callback
//		mPictureCallback = new PictureCallback() {
//
//			@Override
//			public void onPictureTaken(byte[] data, Camera camera) {
//				UploadMediaTask updateMediaTask = new UploadMediaTask(data);
//				updateMediaTask.execute();
////				boolean saved = Utils.saveOutputMedia(data);
////				if (saved) {
////					Toast.makeText(mPreview.getContext(), "Image successfully saved", Toast.LENGTH_SHORT).show();
////				} else {
////					Toast.makeText(mPreview.getContext(), "Error saving image", Toast.LENGTH_SHORT).show();
////				}
//			}
//		};
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
//		// Bind camera
//		mCamera = Utils.getCameraInstance();
//		
//		// Bind preview
//		mPreview.setCamera(mCamera);
//		
//		mCaptureButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// get an image from the camera
//				mCamera.takePicture(null, null, mPictureCallback);
//			}
//		});
		
	}
	private void releaseCameraAndPreview() {
//		// Release camera
//		if (mCamera != null) {
//			mCamera.release(); // release the camera for other applications
//			mCamera = null;
//		}
//		// Release preview
//		mPreview.setCamera(null);
	}
	public class UploadMediaTask extends AsyncTask<Void, Integer, Boolean> {
		private byte[] mData;
		
		public UploadMediaTask(byte[] data) {
			super();
			mData = data;
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
	        try {
	            HttpClient httpClient = new DefaultHttpClient();
	            HttpPost postRequest = new HttpPost(
	                    "http://test.tniechciol.ca:12345/snap_location/test/");

	            postRequest.setEntity(new ByteArrayEntity(mData));
	            HttpResponse response = httpClient.execute(postRequest);
	            BufferedReader reader = new BufferedReader(new InputStreamReader(
	                    response.getEntity().getContent(), "UTF-8"));
	            String sResponse;
	            StringBuilder s = new StringBuilder();
	 
	            while ((sResponse = reader.readLine()) != null) {
	                s = s.append(sResponse);
	            }
	            Log.d(TAG, "Response: " + s);
	            return true;
	        } catch (Exception e) {
	            // handle exception here
	            Log.e(e.getClass().getName(), e.getMessage());
	            return false;
	        }
		}

	     protected void onPostExecute(Boolean success) {
	    	 if (success) {
	    		 Log.d(TAG, "Successfully uploaded an image to the server");
	    	 } else {
	    		 Log.d(TAG, "Uploading an image to the server failed");
	    	 }
	     }
	}


}
