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

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

public class UploadMediaTask extends AsyncTask<Void, Integer, Boolean> {
	private String mUser;
	private double mLatitude, mLongitude;
	private byte[] mData;
	
	public UploadMediaTask(byte[] data, String user, double latitude, double longitude){
		super();
		this.mUser = user;
		this.mData = data;
		this.mLatitude = latitude;
		this.mLongitude = longitude;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			// Add your data
			String encoded = Base64.encodeToString(mData, Base64.DEFAULT);

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost("http://test.tniechciol.ca:12345/snap_location/upload_image/");
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("unique_name", mUser));
			nameValuePairs.add(new BasicNameValuePair("image", encoded));
		    nameValuePairs.add(new BasicNameValuePair("recipients", "B"));
		    nameValuePairs.add(new BasicNameValuePair("recipients","T"));
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
			return true;
		} catch (Exception e) {
			// handle exception here
			Log.e(e.getClass().getName(), e.getMessage());
			return false;
		}
	}

	protected void onPostExecute(Boolean success) {
	}

}