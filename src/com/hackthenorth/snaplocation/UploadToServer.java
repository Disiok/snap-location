package com.hackthenorth.snaplocation;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import android.app.Activity;
import android.app.ProgressDialog;

import android.util.Log;

  
public class UploadToServer extends Activity {
     
    
     
    /**********  File Path *************/
//    final String uploadFilePath = "/mnt/sdcard/";
//    final String uploadFileName = "service_lifecycle.png";
     

      
    public static int uploadFile(byte[] pictureBuffer,String pictureName) {
        
        int serverResponseCode = 0;
        ProgressDialog dialog = null;
            
        String upLoadServerUri = "http://test.tniechciol.ca:12345/snap_location/upload_image/"; 
  
          HttpURLConnection conn = null;
          DataOutputStream dos = null;  
          String lineEnd = "\r\n";
          String twoHyphens = "--";
          String boundary = "*****";
          int bytesRead, bytesAvailable, bufferSize;
          byte[] buffer;
          int maxBufferSize = 1 * 1024 * 1024; 
           try { 
                
                 // open a URL connection to the Servlet
               URL url = new URL(upLoadServerUri);
                
               // Open a HTTP  connection to  the URL
               conn = (HttpURLConnection) url.openConnection(); 
               conn.setDoInput(true); // Allow Inputs
               conn.setDoOutput(true); // Allow Outputs
               conn.setUseCaches(false); // Don't use a Cached Copy
               conn.setRequestMethod("POST");
               conn.setRequestProperty("Connection", "Keep-Alive");
               conn.setRequestProperty("ENCTYPE", "multipart/form-data");
               conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                
               dos = new DataOutputStream(conn.getOutputStream());
      
               dos.writeBytes(twoHyphens + boundary + lineEnd); 
               dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                       + pictureName + "\"" + lineEnd);
                
               dos.writeBytes(lineEnd);
               Log.d("UPLOAD","FILEMETA");
               // create a buffer of  maximum size
               bytesAvailable = pictureBuffer.length; 
      
               bufferSize = Math.min(bytesAvailable, maxBufferSize);
      
               // read file and write it into form...
               bytesRead = pictureBuffer.length; 
                  
               while (bytesRead > 0) {
                    
                 dos.write(pictureBuffer, 0, bufferSize);
                 bytesAvailable -=bufferSize;
                 bytesRead -=bufferSize; 
                 bufferSize = Math.min(bytesAvailable, maxBufferSize);
                }
      
               // send multipart form data necesssary after file data...
               dos.writeBytes(lineEnd);
               dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
      
               // Responses from the server (code and message)
               serverResponseCode = conn.getResponseCode();
               String serverResponseMessage = conn.getResponseMessage();
                 
               Log.i("uploadFile", "HTTP Response is : "
                       + serverResponseMessage + ": " + serverResponseCode);
               Log.d("Upload",""+serverResponseCode);
               if(serverResponseCode == 200){
                    
//                       runOnUiThread(new Runnable() {
//                            public void run() {
//                                 
////                                String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
////                                              +" http://www.androidexample.com/media/uploads/"
////                                              +uploadFileName;
////                                 
////                                messageText.setText(msg);
////                                Toast.makeText(UploadToServer.this, "File Upload Complete.", 
////                                             Toast.LENGTH_SHORT).show();
//                            }
//                        });  
            	   Log.d("Upload",conn.getResponseMessage());
            	   return 200;
               }    
                
               //close the streams //
               dos.flush();
               dos.close();
                 
          } catch (MalformedURLException ex) {
               
//                  dialog.dismiss();  
//                  ex.printStackTrace();
//                   
//                  runOnUiThread(new Runnable() {
//                      public void run() {
//                          messageText.setText("MalformedURLException Exception : check script url.");
//                          Toast.makeText(UploadToServer.this, "MalformedURLException", 
//                                                              Toast.LENGTH_SHORT).show();
//                      }
//                  });
               
              Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
          } catch (Exception e) {
               
//                  dialog.dismiss();  
//                  e.printStackTrace();
//                   
//                  runOnUiThread(new Runnable() {
//                      public void run() {
//                          messageText.setText("Got Exception : see logcat ");
//                          Toast.makeText(UploadToServer.this, "Got Exception : see logcat ", 
//                                  Toast.LENGTH_SHORT).show();
//                      }
//                  });
              Log.e("Upload file to server Exception", "Exception : "
                                               + e.getMessage(), e);  
          }
          dialog.dismiss();       
          return serverResponseCode; 
           
       
     } 
}