package com.hackthenorth.snaplocation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends FragmentActivity {
	public static final String TAG = MainActivity.class.getSimpleName();
	
	ViewPager mViewPager;
	MainScreenPagerAdapter mPagerAdapter;
	
	// View elements
	Camera mCamera;
	CameraPreview mPreview;
	View mPolaroidBorder;
	ImageView mImageInbox;
//	ImageView mCaptureButton;

	PictureCallback mPictureCallback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mImageInbox = (ImageView) findViewById(R.id.image_inbox);
		mPolaroidBorder = findViewById(R.id.polaroid_border);
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		mPagerAdapter = new MainScreenPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		
		// Set fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
//		// Resolve view elements
//		mCaptureButton = (ImageView) findViewById(R.id.capture_button);
		
		// Create our Preview view and set it as the content of our activity.
		mPreview = new CameraPreview(this);
		FrameLayout cameraViewContainer = (FrameLayout) findViewById(R.id.camera_preview);
		cameraViewContainer.addView(mPreview);

		// Create picture callback
		mPictureCallback = new PictureCallback() {

			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				UploadMediaTask updateMediaTask = new UploadMediaTask(data);
				updateMediaTask.execute();
			}
		};
	}
	@Override
	public void onResume() {
		super.onResume();
		bindCameraAndPreview();
	}

	@Override
	public void onPause() {
		super.onPause();
		releaseCameraAndPreview(); // release the camera immediately on pause event
	}

	private void bindCameraAndPreview() {
		// Bind camera
		mCamera = Utils.getCameraInstance();
		
		// Use auto focus
		Camera.Parameters parameters = mCamera.getParameters();
		List<String> focusModes = parameters.getSupportedFocusModes();
		if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
		    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		    Log.d(TAG, "Camera auto focus enabled");
		}
		mCamera.setParameters(parameters);
		
		// Bind preview
		mPreview.setCamera(mCamera);
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				if (arg0 == 0) {
					mPolaroidBorder.setAlpha(arg1);
				} else {
					mPolaroidBorder.setAlpha(1 - arg1);
				}
				
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageSelected(int arg0) {
				if (arg0 == 1) {
					// get an image from the camera
					mCamera.autoFocus(new AutoFocusCallback() {
						@Override
						public void onAutoFocus(boolean success, Camera camera) {
							mCamera.takePicture(null, null, mPictureCallback);
						}
						
					});
				} else {
//					mPolaroidBorder.setVisibility(View.INVISIBLE);
				}
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
	public class UploadMediaTask extends AsyncTask<Void, Integer, Boolean> {
		private byte[] mData;
		
		public UploadMediaTask(byte[] data) {
			super();
			mData = data;
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
	        try {
	            // Add your data
	        	String encoded = Base64.encodeToString(mData, Base64.DEFAULT);
	        	
	            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	            nameValuePairs.add(new BasicNameValuePair("unique_name", "htn"));
	            nameValuePairs.add(new BasicNameValuePair("recipients", "rec1|rec2|rec3|rec4"));
	            nameValuePairs.add(new BasicNameValuePair("picture", encoded));
	            nameValuePairs.add(new BasicNameValuePair("latitude", "45.2038123"));
	            nameValuePairs.add(new BasicNameValuePair("longitude", "32.23829"));
	            
	            
	            HttpClient httpClient = new DefaultHttpClient();
	            HttpPost postRequest = new HttpPost("http://test.tniechciol.ca:12345/snap_location/upload_image/");
	            postRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));

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
