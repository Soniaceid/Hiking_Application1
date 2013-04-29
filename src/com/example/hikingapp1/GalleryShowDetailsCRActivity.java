package com.example.hikingapp1;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;



public class GalleryShowDetailsCRActivity extends Activity  {
	/** Called when the activity is first created. */
private int key_id;
private int key_trek_id;
private String img_path;
//variable to store the currently selected image
private int currentPic = 0;
private Bitmap[] imageBitmaps;
//adapter for gallery view
private PicAdapter imgAdapt;
private  ArrayList<String> key_paths;
private ActionBar actionbar;
public static String uploading_image;
private DownloadIMAGES download_image_task;
private HttpResponse response;
private HttpEntity entity;
private JSONArray Image_json;
private ProgressDialog progressDialog;
static JSONObject jObj ;
static JSONObject[] jsonArray;
private HttpPost httppost;
private ArrayList<NameValuePair> nameValuePairs;
StringBuilder sb = new StringBuilder();
private int responsecode=0;
private int result;
static String json = "";
private InputStream is;
private JSONArray images;
private Bitmap[] bit_array;
private ImageView picView;
private int php;
Gallery picGallery;
//placeholder bitmap for empty spaces in gallery
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery);
		picView = (ImageView) findViewById(R.id.picture);
		//get the gallery view
		picGallery = (Gallery) findViewById(R.id.gallery);
		   Bundle extras = getIntent().getExtras();//takes the choice of the user from the main activity about the type of the activity
	        if (extras != null) {
	            key_id = extras.getInt("key_id");
	            progressDialog = new ProgressDialog(GalleryShowDetailsCRActivity.this);
				progressDialog.setMessage("Loading Photos...");  
	        	download_image_task = new DownloadIMAGES();
	        	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
	        		 download_image_task .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);}
	        	else{
	        		 download_image_task .execute((Void[])null);}
	       }
		Log.d("gallery trek_id",Integer.toString(key_id));

	
		/*	//create a new adapter
			
			imgAdapt = new PicAdapter(this,bit_array);
			//set the gallery adapter
			picGallery.setAdapter(imgAdapt);
			picGallery.setOnItemClickListener(new OnItemClickListener() {
			        public void onItemClick(AdapterView<?> parent,
			          View v, int position, long id) {
			         
			        	Bitmap bitmap = bit_array[position];
			         //uploading_image=key_paths.get(position);
			         Log.d("imageee",key_paths.get(position));
			         Toast.makeText(GalleryShowDetailsCRActivity.this,
			           "lalal",
			              Toast.LENGTH_LONG).show();
			         
			        // Bitmap bitmap = resample(imageInSD);
			         picView.setImageBitmap(bitmap);
			         
			        }

				
			    });*/
	}//close oncreate
	
	
	
	public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
	public class PicAdapter extends BaseAdapter {
		//use the default gallery background image
		int defaultItemBackground;
		//gallery context
		private Bitmap[] ImageBitmaps;
		private Context galleryContext;
		//array to store bitmaps to display
		private Bitmap[] imageBitmaps;
		//placeholder bitmap for empty spaces in gallery
		Bitmap placeholder;
		private Bitmap[] Bitmaps;
		public PicAdapter(Context c, Bitmap[] B ) {
		    //instantiate context
		    galleryContext = c;
		    //create bitmap array
		    ImageBitmaps=B;
		    imageBitmaps  = new Bitmap[10];
		   
		    //decode the placeholder image
		    placeholder = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		    //more processing
		  //set placeholder as all thumbnail images in the gallery initially
		 //   for(int i=0; i<imageBitmaps.length; i++)
		  //      imageBitmaps[i]=placeholder;
		  //get the styling attributes - use default Andorid system resources
		    TypedArray styleAttrs = galleryContext.obtainStyledAttributes(R.styleable.PicGallery);
		    //get the background resource
		    defaultItemBackground = styleAttrs.getResourceId(
		        R.styleable.PicGallery_android_galleryItemBackground, 0);
		    //recycle attributes
		    styleAttrs.recycle();
		}
		//helper method to add a bitmap to the gallery when the user chooses one
		public void addPic(Bitmap newPic)
		{
		    //set at currently selected index
		    imageBitmaps[currentPic] = newPic;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			 return ImageBitmaps.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			 return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//create the view
		    ImageView imageView = new ImageView(galleryContext);
		    //specify the bitmap at this position in the array
		
	          
	        Bitmap bm=ImageBitmaps[position];
		    imageView.setImageBitmap(bm);
		    //set layout options
		    imageView.setLayoutParams(new Gallery.LayoutParams(300, 200));
		    //scale type within view area
		    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		    //set default gallery item background
		    imageView.setBackgroundResource(defaultItemBackground);
			return imageView;
		 
		    //return the view*/
		    //return imageView;
		}
	

	
	}//CLOSE BASEADAPTER
	
	
	
	
	public class DownloadIMAGES extends   AsyncTask<Void,Void,Void>{
		  String image_converted;
		   private final HttpClient Client = new DefaultHttpClient();  
	       //private String Content;  
	       private String Error = null; 
	       
		   protected void onPreExecute() {  
			 
			
				progressDialog.show();
		   
				 
			
		   
		 
		   } 
		@Override
		protected Void doInBackground(Void... params) {
		
			
					
			try{	
			
			httppost = new HttpPost("http://10.0.2.2/Hiking_App/download_image.php");  
			nameValuePairs = new ArrayList<NameValuePair>();
		   
		    nameValuePairs.add(new BasicNameValuePair("show_trek", 
		    		Integer.toString(key_id)));
			Log.d("namevaluepairs",Integer.toString(key_id));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			response = Client.execute(httppost);
			entity = response.getEntity();
			is = entity.getContent();
			responsecode=1;
	
			
			 try {
		            BufferedReader reader = new BufferedReader(new InputStreamReader(
		                    is, "iso-8859-1"), 8);
		            StringBuilder sb = new StringBuilder();
		            String line = null;
		            while ((line = reader.readLine()) != null) {
		                sb.append(line + "\n");
		                Log.v("err", line);
		                
		            }
		            is.close();
		            json = sb.toString();
		      
		            //this is the error
		            
		        } catch (Exception e) {
		            Log.e("Buffer Error", "Error converting result " + e.toString());
		        }
		        
		        // try parse the string to a JSON object
		        try {
		            jObj = new JSONObject(json);
		            php=Integer.parseInt(jObj.getString("response"));
		           
		        //    result=Integer.parseInt(jObj.getString("images"));
		            //Log.d("photosss json",jObj.getString("imagename"));
		            //Log.d("photosss json",jObj.getString("imagename1"));
		            Log.d("photosss json",jObj.getString("images"));
		        
		            if(php==1){
		            	
		            	 images=jObj.getJSONArray("images");	
		            	
		            }
		            
		            
		            
		            
		            //Log.d("error1",jObj.getString("error1"));
		        } catch (JSONException e) {
		            Log.e("JSON Parser", "Error parsing data " + e.toString());
		        }
		       
		  
		        Log.d("mikos",Integer.toString(images.length()));
		        if(php==1){
		  bit_array = new Bitmap[images.length()];
		        if (images!=null){
			        for ( int j=0;j<images.length();j++){
				    	
						JSONObject element= images.getJSONObject(j);
						String restoreimage=element.getString("image");
						byte[] restoredBytes = Base64.decode(restoreimage);
						Log.d("bytes",restoredBytes.toString());
						BitmapFactory.Options o = new BitmapFactory.Options();
						o.inJustDecodeBounds = true;
						BitmapFactory.decodeByteArray(restoredBytes, 0, restoredBytes.length,o);


						int scale = 1;
				        if (o.outHeight > 10 || o.outWidth > 10) {
				            scale = (int)Math.pow(2, (int) Math.round(Math.log(500 / 
				               (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
				        }
				        BitmapFactory.Options o2 = new BitmapFactory.Options();
				        o2.inSampleSize = scale;

						bit_array[j]=BitmapFactory.decodeByteArray(restoredBytes, 0, restoredBytes.length,o2);
						Log.d("bit_array",bit_array.toString());
			        
			        
			        
			        }  
			 }    
		        }
	
	} catch (ClientProtocolException e) {  
	    Error = e.getMessage();  
	    cancel(true);  
	} catch (IOException e1) {  
	    Error = e1.getMessage();  
	    cancel(true);  
	} catch (Exception e2) {
		e2.printStackTrace();
	}

	  
		return null;
		
		   
		} //close do background
		
		
		   protected void onPostExecute(Void unused) {  
	       	
			   progressDialog.dismiss();
 if (php==1){
			gallery();
}else{
	
	 Toast.makeText(getBaseContext(),"No captured photos for this trek,press back ",Toast.LENGTH_LONG).show();	
	
}
	     
	           
		   }  //close onPostExecute
	    
		   
	   }//CLOSE DOWNLOAD IMAGES
	public void gallery(){
	  imgAdapt = new PicAdapter(this,bit_array);
		//set the gallery adapter
		picGallery.setAdapter(imgAdapt);
		picGallery.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent,
		          View v, int position, long id) {
		         
		        	Bitmap bitmap = bit_array[position];
		         //uploading_image=key_paths.get(position);
		       //  Log.d("imageee",key_paths.get(position));
		         Toast.makeText(GalleryShowDetailsCRActivity.this,
		           "Click on a photo to be enlarged",
		              Toast.LENGTH_LONG).show();
		         
		        // Bitmap bitmap = resample(imageInSD);
		         picView.setImageBitmap(bitmap);
		         
		        }

			
		    });
	}
}//CLOSE GALLERYACTIVITY