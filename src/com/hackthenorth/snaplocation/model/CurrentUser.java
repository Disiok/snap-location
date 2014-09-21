package com.hackthenorth.snaplocation.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.hackthenorth.snaplocation.R;
import com.hackthenorth.snaplocation.view.LoadingFragment;
import com.hackthenorth.snaplocation.view.MainActivity;
import com.hackthenorth.snaplocation.view.FriendFragment.FriendComparator;

public class CurrentUser {
	
	private static CurrentUser current;
	
	public String unique_name;
	public String display_name;
	
	public static CurrentUser getInstance() {
		if (current == null) {
			current = new CurrentUser();
			current.unique_name = "htn";
		}
		
		return current;
	}
	
	public void getProfileData(Activity activity) {
		LoadingFragment loading = new LoadingFragment();
		((MainActivity) activity).setBackButtonLock(true);
		((MainActivity) activity).getSupportFragmentManager().beginTransaction()
			.add(R.id.fragment_friend, loading)
			.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
			.commit();
		
		new GetProfileTask(loading, activity).execute();
	}
	
	public class GetProfileTask extends AsyncTask<String, Void, Boolean> {
		private LoadingFragment loadingFragment;
		private Activity activity;
		
		public GetProfileTask(LoadingFragment loadingFragment, Activity activity) {
			super();
			this.loadingFragment = loadingFragment;
			this.activity = activity;
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
	        try {
	        	// Send requests
	            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	            nameValuePairs.add(new BasicNameValuePair("unique_name", unique_name));
	            
	            HttpClient httpClient = new DefaultHttpClient();
	            HttpPost postRequest = new HttpPost("http://test.tniechciol.ca:12345/snap_location/get_profile/");
	            postRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            
	            // Handle response
	            HttpResponse response = httpClient.execute(postRequest);
	            String jsonString = EntityUtils.toString(response.getEntity());
	            
	            Log.i("json_string", jsonString);
	            ProfileResponse profileResponse = new Gson().fromJson(jsonString, ProfileResponse.class);
	            display_name = profileResponse.getDisplayName();
	            
	            return true;
	        } catch (Exception e) {
	            // handle exception here
	            Log.e(e.getClass().getName(), e.getMessage());
	            return false;
	        }
		}

	     protected void onPostExecute(Boolean success) {
	    	 
	    	 ((MainActivity) activity).getSupportFragmentManager().beginTransaction().remove(this.loadingFragment).commit();
	    	 ((MainActivity) activity).setBackButtonLock(false);
	     }
	}
}
