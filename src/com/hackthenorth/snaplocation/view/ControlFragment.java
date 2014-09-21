package com.hackthenorth.snaplocation.view;

import com.hackthenorth.snaplocation.R;
import com.hackthenorth.snaplocation.R.id;
import com.hackthenorth.snaplocation.R.layout;
import com.hackthenorth.snaplocation.util.GPSTracker;
import com.hackthenorth.snaplocation.util.Utils;
import com.hackthenorth.snaplocation.util.UploadMediaTask;

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

import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class ControlFragment extends Fragment {
	public static final String TAG = ControlFragment.class.getSimpleName();
	
	ImageView mCaptureButton;
	
	// View elements
	Camera mCamera;
	CameraPreview mPreview;
	View mPolaroidBorder;
	ImageView mImageInbox;

	PictureCallback mPictureCallback;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated
		// properly.
		View rootView = inflater.inflate(R.layout.fragment_control, container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle bundle) {
		super.onActivityCreated(bundle);
		// Resolve view elements
		mCaptureButton = (ImageView) getView().findViewById(R.id.capture_button);
		mCaptureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// get an image from the camera
				mCamera.autoFocus(new AutoFocusCallback() {
					@Override
					public void onAutoFocus(boolean success, Camera camera) {
						mCamera.takePicture(null, null, mPictureCallback);
					}

				});
			}
		});
		
		// Create our Preview view and set it as the content of our activity.
		mPreview = new CameraPreview(this.getActivity());
		FrameLayout cameraViewContainer = (FrameLayout)getView().findViewById(R.id.camera_preview);
		cameraViewContainer.addView(mPreview);

		// Create picture callback
				mPictureCallback = new PictureCallback() {

					@Override
					public void onPictureTaken(byte[] data, Camera camera) {
						GPSTracker gps = new GPSTracker(getActivity().getBaseContext());
						Log.d("Can",gps.canGetLocation()+"");
						if(gps.canGetLocation()){
							Log.d("Latitude", "" + gps.getLatitude());
							Log.d("Longitude", "" + gps.getLongitude());
							// show friend list with check marks
							// send button at the bottom
							//new UploadMediaTask(data, "htn", gps.getLatitude(), gps.getLongitude()).execute();
						}
						else{
		                    // can't get location
		                    // GPS or Network is not enabled
		                    // Ask user to enable GPS/network in settings
		                    gps.showSettingsAlert();
						}
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
		
		/*mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				if (arg0 == 0) {
					mPolaroidBorder.setAlpha(arg1);
				} else {
					mPolaroidBorder.setAlpha(1 - arg1);
				}				
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
					new Handler().postDelayed(new Runnable() {
						public void run() {
							mCamera.startPreview();
						}
					}, 500);
				}
			}
		});*/
		
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
