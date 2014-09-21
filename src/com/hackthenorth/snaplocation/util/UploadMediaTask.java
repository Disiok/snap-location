package com.hackthenorth.snaplocation.util;

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

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

public class UploadMediaTask extends AsyncTask<Void, Integer, Boolean> {
	public static final String TAG = UploadMediaTask.class.getSimpleName();
	private String mUser;
	private double mLatitude, mLongitude;
	private byte[] mData;
	private Object[] mRecipients;
	private Activity mActivity;
	
	public UploadMediaTask(Activity activity, byte[] data, String user, Object[] recipients, double latitude, double longitude){
		super();
		this.mUser = user;
		this.mData = data;
		this.mLatitude = latitude;
		this.mLongitude = longitude;
		this.mRecipients = recipients;
		this.mActivity = activity;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			// Add your data
			String url = "http://test.tniechciol.ca:12345/snap_location/push_image_location/";
			String encoded = Base64.encodeToString(mData, Base64.DEFAULT);

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost(url);
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("unique_name", mUser));
			nameValuePairs.add(new BasicNameValuePair("image", encoded));
			for (int i = 0; i < mRecipients.length; i++) {
				nameValuePairs.add(new BasicNameValuePair("recipients", (String)mRecipients[i]));
			}
 		    nameValuePairs.add(new BasicNameValuePair("latitude", "" + mLatitude));
		    nameValuePairs.add(new BasicNameValuePair("longitude", "" + mLongitude));
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
			Log.d(TAG, "Successfully uploaded the image");
		} else {
			Log.d(TAG, "Error while uploading image");
		}
		mActivity.onBackPressed();
	}

}