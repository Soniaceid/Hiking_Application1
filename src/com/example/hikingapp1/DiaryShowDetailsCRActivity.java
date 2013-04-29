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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;



public class DiaryShowDetailsCRActivity extends Activity  {
	/** Called when the activity is first created. */
private int key_id;
private int key_trek_id;
private String img_path;
//variable to store the currently selected image
private int currentPic = 0;
private Bitmap[] imageBitmaps;
//adapter for gallery view

private  ArrayList<String> key_paths;
private ActionBar actionbar;
public static String uploading_image;
private DiaryCRTask download_diary_task;
private HttpResponse response;
private HttpEntity entity;
private InputStream is;
private ProgressDialog progressDialog;
static JSONObject jObj ;
static JSONObject[] jsonArray;
private HttpPost httppost;
private ArrayList<NameValuePair> nameValuePairs;
StringBuilder sb = new StringBuilder();
private int responsecode=0;
private int result;
static String json = "";
private String find_mysql;
private String danger_mysql;
private String attract_mysql;
private int rate_mysql;
private int diff_mysql;
private TextView find;
private TextView attract;
private TextView danger;
private TextView  rate;
private TextView  diff;
private int php;

private JSONArray info; 

//placeholder bitmap for empty spaces in gallery
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary_record);
		
		 find=(TextView) findViewById(R.id.textView4);
		 attract=(TextView) findViewById(R.id.textView5);
		danger=(TextView) findViewById(R.id.textView7);
		  rate=(TextView) findViewById(R.id.textView9);
		  diff=(TextView) findViewById(R.id.textView11);
		
		   Bundle extras = getIntent().getExtras();//takes the choice of the user from the main activity about the type of the activity
	        if (extras != null) {
	            key_id = extras.getInt("key_id");
	            progressDialog = new ProgressDialog(DiaryShowDetailsCRActivity.this);
				progressDialog.setMessage("Loading Diary...");  
				download_diary_task = new DiaryCRTask();
	        	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
	        		download_diary_task .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);}
	        	else{
	        		download_diary_task .execute((Void[])null);}
	       }
		Log.d("diary trek_id",Integer.toString(key_id));
	
	
	}//CLOSE CREATE
	
	 public class DiaryCRTask extends   AsyncTask<Void,Void,Void>{
			
		   private final HttpClient Client = new DefaultHttpClient();  
	       //private String Content;  
	
	 	
		   
	 		
	 		protected void onPreExecute() {  
			 
			   
			  
	 			progressDialog.show(); 
		   
				
		   } 
		@Override
		protected Void doInBackground(Void... params) {
		
			
			String Error=null;
			
			try{	
			
			httppost = new HttpPost("http://10.0.2.2/Hiking_App/download_diary.php");  
			nameValuePairs = new ArrayList<NameValuePair>();
		
		
			
			nameValuePairs.add(new BasicNameValuePair("show_trek", 
				 Integer.toString(key_id)));
			Log.d("trek id", Integer.toString(key_id));
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
		        	info=new JSONArray();
		        	 
		            jObj = new JSONObject(json);
		            
		        //   info=jObj.getJSONArray("data");
		           php=Integer.parseInt(jObj.getString("response"));
		           info=jObj.getJSONArray("data");
		           //coordinates=jObj.getString("data");
		          //  IDS=jObj.getJSONArray("ids");
		            //IDS=jObj.getString("ids");
		           
		            Log.d("kleidiii",jObj.getString("info"));
		            Log.d("DATA_json",info.toString());
		           // Log.d("DATA_json",IDS.toString());
		         
		            
		        } catch (JSONException e) {
		            Log.e("JSON Parser", "Error parsing data " + e.toString());
		        }
		 if (php==1){
		        for ( int j=0;j<info.length();j++){
			    	
					JSONObject element= info.getJSONObject(j);
					find_mysql=element.getString("findings");
					attract_mysql=element.getString("attractions");
					danger_mysql=element.getString("dangers");
					rate_mysql=Integer.parseInt(element.getString("rate"));
					diff_mysql=Integer.parseInt(element.getString("difficulty"));
		        
		        } 
		        }
		
		 
			
//close arxiko try		        
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
		   
		      find.setText(find_mysql);
		      attract.setText(attract_mysql);
		      danger.setText(danger_mysql);
		      rate.setText(Integer.toString(rate_mysql));
		      diff.setText(Integer.toString(diff_mysql));
		      if (php==0){
		    	
		    	  Toast.makeText(getBaseContext(),"No recorded diary for this trek,press back ",Toast.LENGTH_LONG).show();	
		    }
		   }  //close onPostExecute
	    
		   
	   }//CLOSE SHOWDETAILSCR TASKS
}//CLOSE DiaryShowDetailsCRActivity