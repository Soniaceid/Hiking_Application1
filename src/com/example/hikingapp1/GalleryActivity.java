package com.example.hikingapp1;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

import com.google.android.maps.GeoPoint;
import com.example.hikingapp1.Hik_APP_Database.Photos;
import com.example.hikingapp1.Hik_APP_Database.Route;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;





public class GalleryActivity extends Activity  {
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
private UploadIMAGES upload_image_task;
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
//placeholder bitmap for empty spaces in gallery
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery);
		// actionbar = getActionBar();
		//actionbar.hide();
		key_paths= new ArrayList<String>();
		   Bundle extras = getIntent().getExtras();//takes the choice of the user from the main activity about the type of the activity
	        if (extras != null) {
	            key_id = extras.getInt("id");
	            key_paths= extras.getStringArrayList("Image_P");
	            key_trek_id=extras.getInt("trek_id_photos");}
		Log.d("gallery trek_id",Integer.toString(key_trek_id));
		final ImageView picView = (ImageView) findViewById(R.id.picture);
		//get the gallery view
		
		Gallery picGallery = (Gallery) findViewById(R.id.gallery);

			//create a new adapter
			
			imgAdapt = new PicAdapter(this,key_paths);
			//set the gallery adapter
			picGallery.setAdapter(imgAdapt);
			picGallery.setOnItemClickListener(new OnItemClickListener() {
			        public void onItemClick(AdapterView<?> parent,
			          View v, int position, long id) {
			         
			         String imageInSD = key_paths.get(position);
			         uploading_image=key_paths.get(position);
			         Log.d("imageee",key_paths.get(position));
			         Toast.makeText(GalleryActivity.this,
			           imageInSD,
			              Toast.LENGTH_LONG).show();
			         
			         Bitmap bitmap = resample(imageInSD);
			         picView.setImageBitmap(bitmap);
			     //    actionbar.show();
			        }

				
			    });
	}//close oncreate
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		 super.onCreateOptionsMenu(menu);
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.action_bar_upload_photos, menu);
        return true;
    }
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    case R.id.upload_photo:
	    	progressDialog = new ProgressDialog(GalleryActivity.this);
			progressDialog.setMessage("Uploading Image...");  
	    	upload_image_task = new UploadIMAGES();
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
        		 upload_image_task .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);}
        	else{
     		 upload_image_task .execute((Void[])null);}
	    	break;
	    	
	   
	   
	   default:
		  break;}
	   return true;
	    
	  //respond to menu item selection
	}//close onOptionsItemSelected
	public Bitmap resample(String Uri){
		//if we have a new URI attempt to decode the image bitmap
		//declare the bitmap
		Bitmap pic = null;
			
			//set the width and height we want to use as maximum display
			int targetWidth = 600;
			int targetHeight = 400;
			//create bitmap options to calculate and use sample size
			BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
			//first decode image dimensions only - not the image bitmap itself
			bmpOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(Uri, bmpOptions);
			//image width and height before sampling
			int currHeight = bmpOptions.outHeight;
			int currWidth = bmpOptions.outWidth;
			//variable to store new sample size
			int sampleSize = 1;
			//calculate the sample size if the existing size is larger than target size
			if (currHeight>targetHeight || currWidth>targetWidth)
			{
			    //use either width or height
			    if (currWidth>currHeight)
			        sampleSize = Math.round((float)currHeight/(float)targetHeight);
			    else
			        sampleSize = Math.round((float)currWidth/(float)targetWidth);
			}
			//use the new sample size
			bmpOptions.inSampleSize = sampleSize;
			//now decode the bitmap using sample options
			bmpOptions.inJustDecodeBounds = false;
			//get the file as a bitmap
			pic = BitmapFactory.decodeFile(Uri, bmpOptions);
			return pic;
		}//close resample	
	
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
		private ArrayList<String> ImageList;
		private Context galleryContext;
		//array to store bitmaps to display
		private Bitmap[] imageBitmaps;
		//placeholder bitmap for empty spaces in gallery
		Bitmap placeholder;
		
		public PicAdapter(Context c, ArrayList<String> IList) {
		    //instantiate context
		    galleryContext = c;
		    //create bitmap array
		    ImageList=IList;
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
			 return ImageList.size();
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
		    
		    Bitmap bm=resample(ImageList.get(position));
		    imageView.setImageBitmap(bm);
		    //set layout options
		    imageView.setLayoutParams(new Gallery.LayoutParams(300, 200));
		    //scale type within view area
		    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		    //set default gallery item background
		    imageView.setBackgroundResource(defaultItemBackground);
		 
		    //return the view
		    return imageView;
		}
	

	
	}//CLOSE BASEADAPTER
	  private  String ResizeImage(String imagepath) {
		  
			
			
			//set the width and height we want to use as maximum display
			int targetWidth = 600;
			int targetHeight = 400;
			//create bitmap options to calculate and use sample size
			BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
			//first decode image dimensions only - not the image bitmap itself
			bmpOptions.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(imagepath, bmpOptions);
			//image width and height before sampling
			int currHeight = bmpOptions.outHeight;
			int currWidth = bmpOptions.outWidth;
			//variable to store new sample size
			int sampleSize = 1;
			//calculate the sample size if the existing size is larger than target size
			if (currHeight>targetHeight || currWidth>targetWidth)
			{
			    //use either width or height
			    if (currWidth>currHeight)
			        sampleSize = Math.round((float)currHeight/(float)targetHeight);
			    else
			        sampleSize = Math.round((float)currWidth/(float)targetWidth);
			}
			//use the new sample size
			bmpOptions.inSampleSize = sampleSize;
			//now decode the bitmap using sample options
			bmpOptions.inJustDecodeBounds = false;
			//get the file as a bitmap
			
        Bitmap bitmapOrg = BitmapFactory.decodeFile(imagepath,bmpOptions);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
         
        //Resize the image
        double width = bitmapOrg.getWidth();
        double height = bitmapOrg.getHeight();
        double ratio = 400/width;
        int newheight = (int)(ratio*height);
         
        System.out.println("———-width" + width);
        System.out.println("———-height" + height);
         
        bitmapOrg = Bitmap.createScaledBitmap(bitmapOrg, 400, newheight, true);
         
        //Here you can define .PNG as well
        bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 95, bao);
        byte[] ba = bao.toByteArray();
        String ba1 = Base64.encodeBytes(ba);
        return ba1;
	  }//close rezize image
	  public class UploadIMAGES extends   AsyncTask<Void,Void,Void>{
		  String image_converted;
		   private final HttpClient Client = new DefaultHttpClient();  
	       //private String Content;  
	       private String Error = null; 
	       
		   protected void onPreExecute() {  
			 
			   
				progressDialog.show();
		   
				image_converted=ResizeImage(uploading_image);
				Image_json=new JSONArray();
				Image_json.put(image_converted);
				Log.d("convert",image_converted);	 
			
		   
		 
		   } 
		@Override
		protected Void doInBackground(Void... params) {
		
			
					
			try{	
			
			httppost = new HttpPost("http://10.0.2.2/Hiking_App/upload_image.php");  
			nameValuePairs = new ArrayList<NameValuePair>();
		    nameValuePairs.add(new BasicNameValuePair("Image_converted", 
		    		Image_json.toString()));
		    nameValuePairs.add(new BasicNameValuePair("trek_id_photos", 
		    		Integer.toString(key_trek_id)));
			Log.d("namevaluepairs",Integer.toString(key_trek_id));
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
		            result=Integer.parseInt(jObj.getString("result"));
		            Log.d("photosss json",jObj.getString("imagename"));
		            Log.d("photosss json",jObj.getString("imagename1"));
		            Log.d("photosss json",jObj.getString("result"));
		        
		            //Log.d("error1",jObj.getString("error1"));
		        } catch (JSONException e) {
		            Log.e("JSON Parser", "Error parsing data " + e.toString());
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

			   
		          if (responsecode ==1 &&(result==1)) {
		        	   
		        	  Toast.makeText(getBaseContext(),"Your image was uploaded!Upload another,if you like! ",Toast.LENGTH_LONG).show();	
						
		          }
		           else if (responsecode ==0){
		        	   
		       	   
		       	 Toast.makeText(getBaseContext(),"No Upload",Toast.LENGTH_LONG).show();
		         }
		            
	     
	           
		   }  //close onPostExecute
	    
		   
	   }//CLOSE UPLOAD IMAGES
}//CLOSE GALLERYACTIVITY