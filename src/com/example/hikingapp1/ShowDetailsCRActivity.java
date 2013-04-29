package com.example.hikingapp1;

import java.io.BufferedReader;
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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;



public class ShowDetailsCRActivity extends Activity{
private ProgressDialog progressDialog;
private TextView text_type;
private TextView text_date;
private TextView text_dist;
private TextView text_time;
private TextView text_user;
static JSONObject jObj ;
static JSONObject[] jsonArray;
private HttpPost httppost;
private ArrayList<NameValuePair> nameValuePairs;
StringBuilder sb = new StringBuilder();
private int responsecode=0;
private int result;
static String json = "";
private InputStream is;
private HttpResponse response;
private HttpEntity entity;
private JSONArray info;
private JSONArray info1;
private String username;
private ShowDetailsCRTask show_detailstask;
int value;
private String type_mysql;
private String date_mysql;
private String user_mysql;
private String time_mysql;
private Double distance_mysql;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        setContentView(R.layout.show_close_route_details);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
           value = extras.getInt("trek_id");}
        Log.d("pou pairno",Integer.toString(value));
        text_user=(TextView) findViewById(R.id.textView2);
        text_date=(TextView) findViewById(R.id.textView5);
        text_type=(TextView) findViewById(R.id.textView6);
        text_dist=(TextView) findViewById(R.id.textView8);
   		text_time=(TextView) findViewById(R.id.textView10);
   		
   	 
   		Button  gallery=(Button) findViewById(R.id.button1);
   		Button  diary=(Button) findViewById(R.id.button2);
   		progressDialog = new ProgressDialog(ShowDetailsCRActivity.this);
			progressDialog.setMessage("Loading data..."); 	
   	 show_detailstask = new ShowDetailsCRTask();
 	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
 		 show_detailstask .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);}
 	else{
 		 show_detailstask .execute((Void[])null);}
		
 	gallery.setOnClickListener(new View.OnClickListener() {
		
	    public void onClick(View arg0) {
	              //Starting a new Intent
        
	    	
	    	 Intent GalleryScreen = new Intent(getApplicationContext(),GalleryShowDetailsCRActivity.class);
	    	 GalleryScreen.putExtra("key_id",value);
	      
	         startActivity(GalleryScreen);}
	    	});
	diary.setOnClickListener(new View.OnClickListener() {
		
	    public void onClick(View arg0) {
	              //Starting a new Intent
        
	    	
	    	 Intent DiaryScreen = new Intent(getApplicationContext(),DiaryShowDetailsCRActivity.class);
	    	 DiaryScreen.putExtra("key_id",value);
	      
	         startActivity(DiaryScreen);}
	    	});
        
	
	}//close oncreate
	
	  public class ShowDetailsCRTask extends   AsyncTask<Void,Void,Void>{
			
		   private final HttpClient Client = new DefaultHttpClient();  
	       //private String Content;  
	
	 	
		   
	 		
	 		protected void onPreExecute() {  
			 
			   
			  
	 			progressDialog.show(); 
		   
				
		   } 
		@Override
		protected Void doInBackground(Void... params) {
		
			
			String Error=null;
			
			try{	
			
			httppost = new HttpPost("http://10.0.2.2/Hiking_App/show_close_details.php");  
			nameValuePairs = new ArrayList<NameValuePair>();
		
		
			
			nameValuePairs.add(new BasicNameValuePair("show_trek", 
				 Integer.toString(value)));
			Log.d("trek id", Integer.toString(value));
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
		            user_mysql=jObj.getString("username");
		           info=jObj.getJSONArray("info");
		           info1=jObj.getJSONArray("info1");
		            //coordinates=jObj.getString("data");
		          //  IDS=jObj.getJSONArray("ids");
		            //IDS=jObj.getString("ids");
		            Log.d("DATA",jObj.getString("username"));
		            Log.d("kleidiii",jObj.getString("info"));
		            Log.d("DATA_json",info.toString());
		           // Log.d("DATA_json",IDS.toString());
		            
		        } catch (JSONException e) {
		            Log.e("JSON Parser", "Error parsing data " + e.toString());
		        }
		 if (info!=null){
		        for ( int j=0;j<info.length();j++){
			    	
					JSONObject element= info.getJSONObject(j);
					type_mysql=element.getString("activity");
					date_mysql=element.getString("date");
					
		        }  
		 }
		 if (info1!=null){
		        for ( int j=0;j<info1.length();j++){
			    	
					JSONObject element= info1.getJSONObject(j);
					distance_mysql=Double.parseDouble(element.getString("distance"));
					time_mysql=element.getString("time");
					
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
		    text_user.setText(user_mysql);
		      text_type.setText(type_mysql);
		      text_date.setText(date_mysql);
		      text_time.setText(time_mysql);
		      text_dist.setText(Double.toString(distance_mysql));
		      if (info==null){
		    	
		    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShowDetailsCRActivity.this);
			       
		    	// set title
     			alertDialogBuilder.setTitle("There are no details");
     			// set dialog message
     			alertDialogBuilder
     				.setMessage("Click OK to return")
     				.setCancelable(false)
     				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
     					public void onClick(DialogInterface dialog,int id) {
     						dialog.cancel();
     					      
     		                  
     					}
     					
     				  });
     			// create alert dialog
 				AlertDialog alertDialog = alertDialogBuilder.create();
  
 				// show it
 				alertDialog.show();	
		    }
		   }  //close onPostExecute
	    
		   
	   }//CLOSE SHOWDETAILSCR TASKS
	
	
	
	
	
	
}//CLOSE ShowDetailsCR