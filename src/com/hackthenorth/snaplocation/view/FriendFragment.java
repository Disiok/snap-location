package com.hackthenorth.snaplocation.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.hackthenorth.snaplocation.R;
import com.hackthenorth.snaplocation.R.id;
import com.hackthenorth.snaplocation.R.layout;
import com.hackthenorth.snaplocation.model.Friend;
import com.hackthenorth.snaplocation.model.FriendResponse;

public class FriendFragment extends Fragment{
	public static final String TAG = FriendFragment.class.getSimpleName();
	
	FriendListAdapter mAdapter;
	ListView mListView;
	ArrayList<Friend> mFriends;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// The last two arguments ensure LayoutParams are inflated
		// properly.
		View rootView = inflater.inflate(R.layout.fragment_friend, container, false);
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		resolveFriends();
		mAdapter = new FriendListAdapter(getActivity(), mFriends);
		mListView = (ListView) getView().findViewById(R.id.friend_list_view);
		mListView.setAdapter(mAdapter);
	}
	
	public void resolveFriends() {
		Log.d(TAG, "Resolving friends");
		mFriends = new ArrayList<Friend>();
		ResolveFriendsTask resolveFriendTask = new ResolveFriendsTask();
		resolveFriendTask.execute("htn");
	}
	public class FriendComparator implements Comparator<Friend> {
	    @Override
	    public int compare(Friend o1, Friend o2) {
	        if (o1.getNumberOfRoundsPending() > 0 && o2.getNumberOfRoundsPending() > 0) {
	        	return o1.getDisplayName().compareTo(o2.getDisplayName());
	        } else if (o1.getNumberOfRoundsPending() > 0) {
	        	return -1;
	        } else {
	        	return 1;
	        }
	    }
	}
	public class ResolveFriendsTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
	        try {
	        	Log.d(TAG, "Sending request for friend list");
	        	// Send requests
	        	String uniqueName = params[0];
	            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	            nameValuePairs.add(new BasicNameValuePair("unique_name", uniqueName));
	            
	            HttpClient httpClient = new DefaultHttpClient();
	            HttpPost postRequest = new HttpPost("http://test.tniechciol.ca:12345/snap_location/friends/");
	            postRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            
	            // Handle response
	            HttpResponse response = httpClient.execute(postRequest);
	            String jsonString = EntityUtils.toString(response.getEntity());
	            
	            Log.i("json_string", jsonString);
	            FriendResponse friendResponse = new Gson().fromJson(jsonString, FriendResponse.class);
	            mFriends.addAll(Arrays.asList(friendResponse.getFriends()));
	            Collections.sort(mFriends, new FriendComparator());
	            
	            return true;
	        } catch (Exception e) {
	            // handle exception here
	            Log.e(e.getClass().getName(), e.getMessage());
	            return false;
	        }
		}

	     protected void onPostExecute(Boolean success) {
	    	 if (success) {
	    		 Log.d(TAG, "Successfully obtained friend list");
	    	 } else {
	    		 Log.d(TAG, "Error obtaining friend list");
	    	 }
	    	 
	    	 mAdapter.notifyDataSetChanged();
	     }
		
	}
}
