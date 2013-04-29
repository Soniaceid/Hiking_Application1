package com.example.hikingapp1;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class PhotoActivity extends Activity {
	 private static final String TAG = PhotoActivity.class.getSimpleName(); 
	public static final int MEDIA_TYPE_IMAGE = 1;
	
	 private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	   
	    
	
	 
	    private Uri fileUri;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.photo);
        // give the image a name so we can store it in the phone's default location
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(MEDIA_TYPE_IMAGE));
         
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "IMG_" + timeStamp + ".jpg");
 
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
         
        //fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image (this doesn't work at all for images)
        fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values); // store content values
        intent.putExtra( MediaStore.EXTRA_OUTPUT,  fileUri);
        
        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }
    
         
      
         
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) 
        {
            super.onActivityResult(requestCode, resultCode, data);
             
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                     
                    // Originally I was going to iterate through the list of images and grab last added to the MediaStore.
                    // But this is not necessary if we store the Uri in the image
                    /*
                    String[] projection = {MediaStore.Images.ImageColumns._ID};
                    String sort = MediaStore.Images.ImageColumns._ID + " DESC";
     
                    Cursor cursor = this.managedQuery(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, sort);
     
                    try{
                        cursor.moveToFirst();
                        Long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID));
                        fileUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(id));
                    } finally{
                        cursor.close();
                    }
                    */
                     
                    if(fileUri != null) {
                        Log.d(TAG, "Image saved to:\n" + fileUri);
                        Log.d(TAG, "Image path:\n" + fileUri.getPath());
                        Log.d(TAG, "Image name:\n" + getName(fileUri)); // use uri.getLastPathSegment() if store in folder
                        Bundle bundle = new Bundle();
    	    	    	bundle.putString("path_photo",fileUri.getPath());
    	    	    
    	    	    	Intent mIntent = new Intent();
    	    	        mIntent.putExtras(bundle);
    	    	        setResult(RESULT_OK, mIntent);
    	    	       
                
                    }
                     
                } else if (resultCode == RESULT_CANCELED) {
                    // User cancelled the image capture
                } else {
                    // Image capture failed, advise user
                }
            }
     
           
        }
      
   
     
        // grab the name of the media from the Uri
        protected String getName(Uri uri) 
        {
            String filename = null;
     
            try {
                String[] projection = { MediaStore.Images.Media.DISPLAY_NAME };
                Cursor cursor = managedQuery(uri, projection, null, null, null);
     
                if(cursor != null && cursor.moveToFirst()){
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                    filename = cursor.getString(column_index);
                } else {
                    filename = null;
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting file name: " + e.getMessage());
            }
     
            return filename;
        }
    }//CLOSE PHOTOACTIVITY
