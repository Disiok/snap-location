package com.hackthenorth.snaplocation;

import android.os.AsyncTask;
import android.util.Log;

public class UploadImageTask extends AsyncTask<Void,Integer, Long> {
	private String imageName;
	private byte[] image;
	public UploadImageTask(byte[] image, String imageName){
		this.imageName=imageName;
		this.image=image;
	}
     protected Long doInBackground(Void... v) {
    	 Log.d("TASK","PREUPLOAD");
         UploadToServer.uploadFile(image, imageName);
         Log.d("TASK","UPLOADED");
         Long n=Long.valueOf(12345);
         return n;
     }

     protected void onProgressUpdate(Integer... progress) {
         Log.d("UploadImageTask", ""+progress[0]);
     }

     protected void onPostExecute(Long result) {
         Log.d("UploadImageTask", "Downloaded " + result + " bytes");
     }
 }
