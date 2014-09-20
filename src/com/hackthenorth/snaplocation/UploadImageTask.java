package com.hackthenorth.snaplocation;

//import java.io.ByteArrayInputStream;
import java.util.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import android.os.AsyncTask;
import android.util.Log;

public class UploadImageTask extends AsyncTask<Void,Integer, Long> {
	private String user;
	private byte[] image;
	public UploadImageTask(byte[] image, String user){
		this.user=user;
		this.image=image;
	}
	
     protected Long doInBackground(Void... v) {
    	 String url = "http://test.tniechciol.ca:12345/snap_location/upload_image/";
    	 String boundary="*****";
 		try {
 		    HttpClient httpclient = new DefaultHttpClient();

 		    HttpPost httppost = new HttpPost(url);

// 		    InputStreamEntity reqEntity = new InputStreamEntity(
// 		            new ByteArrayInputStream(image), -1);
// 		    reqEntity.setContentType("multipart/form-data");
// 		    reqEntity.setChunked(true); // Send in multiple parts if needed
// 		    httppost.setEntity(reqEntity);
 		    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
 		    nameValuePairs.add(new BasicNameValuePair("user", user));
 		    nameValuePairs.add(new BasicNameValuePair("image",new String(image,"UTF-8")));
 		    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
 		    Log.d("Request","PRESEND");
 		    HttpResponse response = httpclient.execute(httppost);
 		    //Do something with response...
 		    String responseString = EntityUtils.toString(response.getEntity());
 		    String[] lines = responseString.split("\n");
 		    for (int i = 0; i < lines.length; i++) {
 		    	Log.d("Response", lines[i]);
 		    }
 		} catch (Exception e) {
 		    Log.e("Exception","",e);
 		}
         Long n=Long.valueOf(12345);
         return n;
     }

     protected void onProgressUpdate(Integer... progress) {
         Log.d("UploadImageTask", ""+progress[0]);
     }

     protected void onPostExecute(Long result) {
         Log.d("UploadImageTask", "UPLOADED IMAGE");
     }
 }
