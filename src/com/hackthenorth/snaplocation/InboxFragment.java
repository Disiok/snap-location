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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class InboxFragment extends Fragment{
	public static final String TAG = InboxFragment.class.getSimpleName();
	
	Button mLogin;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated
		// properly.
		View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mLogin = (Button) getView().findViewById(R.id.button_add_user);
		
		mLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AddUserTask().execute();
			}
			
		});
	}
	public class AddUserTask extends AsyncTask<Void, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
	        try {
	            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	            nameValuePairs.add(new BasicNameValuePair("display_name", "Hack the North"));
	            nameValuePairs.add(new BasicNameValuePair("unique_name", "htn"));
	            
	            HttpClient httpClient = new DefaultHttpClient();
	            HttpPost postRequest = new HttpPost("http://test.tniechciol.ca:12345/snap_location/add_user/");
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
	    		 Log.d(TAG, "Successfully added user");
	    	 } else {
	    		 Log.d(TAG, "Add user failed");
	    	 }
	     }
	}
}