package com.hackthenorth.snaplocation.view;

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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hackthenorth.snaplocation.R;
import com.hackthenorth.snaplocation.R.layout;
import com.hackthenorth.snaplocation.util.GPSTracker;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class GameActivity extends Activity {
	public static final String TAG = GameActivity.class.getSimpleName();
	public static final String EXTRA_USER = "EXTRA_USER";
	public static final String EXTRA_OTHER = "EXTRA_OTHER";
	ImageView mControlButton;
	ImageView mGameImage;
	ImageView mSubmitButton;

	GoogleMap mGoogleMap;

	LatLng mCoordinate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		// Set fullscreen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		mControlButton = (ImageView) findViewById(R.id.control_button);
		mGameImage = (ImageView) findViewById(R.id.game_image);
		mSubmitButton = (ImageView) findViewById(R.id.submit_button);

		mControlButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
					mGameImage.setVisibility(View.VISIBLE);
					return true;
				} else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
					mGameImage.setVisibility(View.INVISIBLE);
				}
				return false;
			}
		});

		Bundle extras = getIntent().getExtras();
		final String userName = extras.getString(EXTRA_USER);
		final String otherName = extras.getString(EXTRA_OTHER);

		mSubmitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCoordinate != null) {

					Toast.makeText(getBaseContext(),
							"Location selected" + userName + otherName,
							Toast.LENGTH_SHORT).show();
					
					new GuessLocationTask(userName, otherName, mCoordinate).execute();
				} else {
					Toast.makeText(getBaseContext(),
							"Location hasn't been selected", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
		mGoogleMap.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng arg0) {
				mGoogleMap.clear();
				mGoogleMap.addMarker(new MarkerOptions().position(arg0).title(
						"Chosen location"));
				mCoordinate = arg0;
				Toast.makeText(getBaseContext(), arg0.toString(),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void getImage() {
		// TODO do something with server to get image and update the imageview
	}

	private void initilizeMap() {
		if (mGoogleMap == null) {
			mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			// check if map is created successfully or not
			if (mGoogleMap == null) {
				Toast.makeText(this, "Sorry! unable to create maps",
						Toast.LENGTH_SHORT).show();
			} else {
				GPSTracker gps = new GPSTracker(this);
				if(gps.canGetLocation()){
					LatLng ll = new LatLng(gps.getLatitude(), gps.getLongitude());
					mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 20));
				}
			}
		}
	}
	
	public class GuessLocationTask extends AsyncTask<Void, Void, Boolean>{
		String mUser;
		String mOther;
		LatLng mCoordinates;
		public GuessLocationTask(String user, String other, LatLng coordinates) {
			mUser = user;
			mOther = other;
			mCoordinates = coordinates;
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				// Add your data
				String url = "http://test.tniechciol.ca:12345/snap_location/guess_location/";

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost(url);
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("unique_name", mUser));
			    nameValuePairs.add(new BasicNameValuePair("friend_name", mOther));
	 		    nameValuePairs.add(new BasicNameValuePair("guess_lat", "" + mCoordinates.latitude));
			    nameValuePairs.add(new BasicNameValuePair("guess_lon", "" + mCoordinates.longitude));
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
				Log.d(TAG, "Successfully guessed the location");
			} else {
				Log.d(TAG, "Error while guessing location");
			}
		}
	}

}
