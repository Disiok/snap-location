package com.hackthenorth.snaplocation.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.hackthenorth.snaplocation.R;
import com.hackthenorth.snaplocation.R.layout;
import com.hackthenorth.snaplocation.model.FriendResponse;
import com.hackthenorth.snaplocation.model.GuessResponse;
import com.hackthenorth.snaplocation.model.ImageResponse;
import com.hackthenorth.snaplocation.util.GPSTracker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
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
		
		new GetImageTask(userName, otherName).execute();
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
					mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 50));
				}
			}
		}
	}
	
	public class GuessLocationTask extends AsyncTask<Void, Void, LatLng>{
		String mUser;
		String mOther;
		LatLng mCoordinates;
		public GuessLocationTask(String user, String other, LatLng coordinates) {
			mUser = user;
			mOther = other;
			mCoordinates = coordinates;
		}
		
		@Override
		protected LatLng doInBackground(Void... params) {
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

				
				// Handle response
				HttpResponse response = httpClient.execute(postRequest);
				String jsonString = EntityUtils.toString(response.getEntity());

				GuessResponse guessResponse = new Gson().fromJson(jsonString, GuessResponse.class);
				return guessResponse.getCoordinates();
			} catch (Exception e) {
				// handle exception here
				Log.e(e.getClass().getName(), e.getMessage());
				return null;
			}
		}
		protected void onPostExecute(LatLng coordinate) {
			if (coordinate != null) {
				mGoogleMap.addMarker(new MarkerOptions().position(coordinate).title("Correct location"));
				mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 50));
				Log.d(TAG, "Successfully guessed the location");
			} else {
				Log.d(TAG, "Error while guessing location");
			}
		}
	}
	
	public class GetImageTask extends AsyncTask<Void, Void, String>{
		String mUser;
		String mOther;
		public GetImageTask(String user, String other) {
			mUser = user;
			mOther = other;
		}
		
		@Override
		protected String doInBackground(Void... params) {
			try {
				// Add your data
				String url = "http://test.tniechciol.ca:12345/snap_location/next_image/";

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost(url);
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("unique_name", mUser));
			    nameValuePairs.add(new BasicNameValuePair("friend_name", mOther));
				postRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = httpClient.execute(postRequest);
				String jsonString = EntityUtils.toString(response.getEntity());

				Log.i("json_string", jsonString);
				ImageResponse imageResponse = new Gson().fromJson(jsonString, ImageResponse.class);
				
				
				URL mediaUrl = new URL("http://test.tniechciol.ca:12345" + imageResponse.getUrl());
				Log.d(TAG, "Response: " + mediaUrl);
				
				// Read all the text returned by the server
			    BufferedReader in = new BufferedReader(new InputStreamReader(mediaUrl.openStream()));
			    StringBuilder total = new StringBuilder();
			    String str;
			    while ((str = in.readLine()) != null) {
			    	total.append(str + "\n");
			        // str is one line of text; readLine() strips the newline character(s)
			    }
			    in.close();
			    
				Log.d(TAG, "Encoded String: " + total);
			    
				
				return total.toString();
			} catch (Exception e) {
				// handle exception here
				Log.e(e.getClass().getName(), e.getMessage());
				return null;
			}
		}
		protected void onPostExecute(String string) {
			if (string != null) {
				Log.d(TAG, "Successfully getting image");
				// find the width and height of the screen:
				Display d = getWindowManager().getDefaultDisplay();
				int x = d.getWidth();
				int y = d.getHeight();
				
				byte[] data = Base64.decode(string, Base64.DEFAULT);
				Bitmap bMap = BitmapFactory.decodeByteArray(data, 0, data.length);
				
				// scale it to fit the screen, x and y swapped because my image is wider than it is tall
				Bitmap scaledBitmap = Bitmap.createScaledBitmap(bMap, y, x, true);
				 
				// create a matrix object
				Matrix matrix = new Matrix();
				matrix.postRotate(90); // anti-clockwise by 90 degrees
				 
				// create a new bitmap from the original using the matrix to transform the result
				Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);
				 
				// display the rotated bitmap
				mGameImage.setImageBitmap(rotatedBitmap);
				
			} else {
				Log.d(TAG, "Error while getting image");
			}
		}
	}
	

}
