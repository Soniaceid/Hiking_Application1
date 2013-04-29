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
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.example.hikingapp1.Hik_APP_Database.Description;
import com.example.hikingapp1.Hik_APP_Database.Photos;
import com.example.hikingapp1.Hik_APP_Database.Route;
import com.example.hikingapp1.Hik_APP_Database.Treks;
import com.example.hikingapp1.Hik_APP_Database.Workout;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;



import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;


public class DetailsOfTrekActivity extends MapActivity  {
	/** Called when the activity is first created. */
private int key_id;
	
public  ArrayList<GeoPoint> TrekPath;
public  ArrayList<String> Image_Paths;
private MyLocationOverlay m_locationOverlay;//shows the current position
private List<Overlay> mapoverlays;
private SharedPreferences_User_Settings user_settings;
private HttpPost httppost;
private ArrayList<NameValuePair> nameValuePairs;
private  ArrayList<String> Latitude_mysql;
private  ArrayList<String> Longitude_mysql;
private  ArrayList<String> Image_Paths_Pro;
private JSONArray Lati_Json;
private JSONArray Longi_Json;
private JSONArray Image_Paths_Json;
private JSONArray Json;
private HttpResponse response;
private HttpEntity entity;
private ProgressDialog progressDialog;
private int responcecode=0;
private UploadTask upload_task;
private int trek_id_photos;
private Hik_APP_Databasehandler mDatabase;
private Hik_APP_Databasehandler mDatabase1;
private TextView  text_type;
private TextView  text_date;
private TextView  text_loc;
private TextView  text_time;
private TextView  text_dist;
private TextView  text_cal;
private  int user_id_mysql;
private double start1;
private double start2;
static JSONObject jObj ;
static JSONObject[] jsonArray;
StringBuilder sb = new StringBuilder();
private int r1,r2,r3,r4;//Response from server about the success of the inserted trek
static String json = "";
private InputStream is;
private int mark_rate;
private int mark_diff;
private String findings_text;
private String attractions_text;
private String dangers_text;
private String response_php;
public static final double PI = 3.14159265;
public static final double deg2radians = PI/180.0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_trek);
	
		user_settings=new SharedPreferences_User_Settings(getApplicationContext());
		//updateMenuTitles(); 
		Latitude_mysql= new ArrayList<String>();
	      Longitude_mysql= new ArrayList<String>();
	      Lati_Json=new  JSONArray();
	      Longi_Json=new  JSONArray();
	      Image_Paths_Json=new  JSONArray();
	      Image_Paths_Pro= new ArrayList<String>();
	      TrekPath=new ArrayList<GeoPoint>();
	      Image_Paths= new ArrayList<String>();
	      
	   //   ActionBar actionBar = getActionBar();
	      
	      // More stuff here...
	    //  actionBar.show();
			
	      mDatabase = new Hik_APP_Databasehandler(this.getApplicationContext());
	      mDatabase1 = new Hik_APP_Databasehandler(this.getApplicationContext());
		MapView m_vwMap = (MapView) findViewById(R.id.map_view1);
	 text_type=(TextView) findViewById(R.id.textView8);
	 text_date=(TextView) findViewById(R.id.textView10);
		 text_loc=(TextView) findViewById(R.id.textView9);
		 text_time=(TextView) findViewById(R.id.textView11);
		 text_dist=(TextView) findViewById(R.id.textView12);
	 text_cal=(TextView) findViewById(R.id.textView13);
		Button  gallery=(Button) findViewById(R.id.button1);
		Button  diary=(Button) findViewById(R.id.button2);
		
		m_vwMap.setBuiltInZoomControls(true);
		final MapController mapControl = m_vwMap.getController();
		 mapoverlays= m_vwMap.getOverlays();
		mapControl.setZoom(15);
		 Bundle extras = getIntent().getExtras();//takes the choice of the user from the main activity about the type of the activity
	        if (extras != null) {
	            key_id = extras.getInt("id");}
		 SQLiteDatabase db = mDatabase.getReadableDatabase();
		
				String Query1 = " SELECT " + Route.lat + ","+ Route.lon + " FROM " + Route.route + " WHERE " + Route.trek_id_r + " = " + key_id + " ORDER BY " + Route.route_id + " ASC " ;
				
				
				Cursor c = db.rawQuery(Query1, null);
				if(c.moveToFirst())
				{
					for(int i = 0; i< c.getCount(); i++)
					{
					double latitude= (c.getDouble(c.getColumnIndex(Route.lat))* deg2radians);
					double longitude= (c.getDouble(c.getColumnIndex(Route.lon))* deg2radians);
					//Latitude_mysql.add(Double.toString(c.getDouble(c.getColumnIndex(Route.lat))));
					//int longitude=(int) (c.getDouble(c.getColumnIndex(Route.lon))* 1e6);
					//Longitude_mysql.add(Double.toString(c.getDouble(c.getColumnIndex(Route.lon))));
					int trekpath_lati=(int) (c.getDouble(c.getColumnIndex(Route.lat))* 1e6);
					int trekpath_longi=(int) (c.getDouble(c.getColumnIndex(Route.lon))* 1e6);
					Latitude_mysql.add(Double.toString(latitude));
					Longitude_mysql.add(Double.toString(longitude));
						GeoPoint point = new GeoPoint(trekpath_lati,trekpath_longi);
						TrekPath.add(point);
						 
					c.moveToNext();
					}
					
				}
				c.close();
				TrekPathOverlay m_pathOverlay = new TrekPathOverlay(TrekPath);
				 m_vwMap.postInvalidate();
				mapoverlays.add(m_pathOverlay);
				
	           String Query2=" SELECT " + Treks.activity + ","+ Treks.location + "," + Treks.date + "," + Treks.start_lati + "," + Treks.start_longi + " FROM " + Treks.treks + " WHERE " + Treks.trek_id + " = " + key_id ;
	            c = db.rawQuery(Query2, null);
	          if( c.moveToFirst()){
	           text_date.setText(c.getString(c.getColumnIndex(Treks.date)));
				text_type.setText(c.getString(c.getColumnIndex(Treks.activity)));
				text_loc.setText(c.getString(c.getColumnIndex(Treks.location)));
				start1=c.getDouble(c.getColumnIndex(Treks.start_lati));
				start2=c.getDouble(c.getColumnIndex(Treks.start_longi));
	          }c.close();
				String Query3=" SELECT " + Workout.distance + ","+ Workout.calories + ","+ Workout.time +" FROM " + Workout.workout + " WHERE " + Workout.trek_id_w + " = " + key_id ;
		           c = db.rawQuery(Query3, null);
		         if(  c.moveToFirst()){
		           text_time.setText(c.getString(c.getColumnIndex(Workout.time)));
					text_dist.setText(c.getString(c.getColumnIndex(Workout.distance)));
					text_cal.setText(c.getString(c.getColumnIndex(Workout.calories)));
				}
		         
		         c.close();
		        String Query4 = " SELECT " + Photos.path +  " FROM " + Photos.photos + " WHERE " + Photos.trek_id_p + " = " + key_id  ;
					
					
					 c = db.rawQuery(Query4, null);
					if(c.moveToFirst())
					{
						for(int i = 0; i< c.getCount(); i++)
						{
						String img_path = c.getString(c.getColumnIndex(Photos.path));
						Log.d("soniaaa",img_path);
						Image_Paths.add(c.getString(c.getColumnIndex(Photos.path)));	
						c.moveToNext();}
						}c.close();
						Log.d("soniaaa",Image_Paths.toString());
	db.close();
	 for(int i=0;i<Latitude_mysql.size();i++)
     {
		 Lati_Json.put(Latitude_mysql.get(i)); 
		 Longi_Json.put(Longitude_mysql.get(i));
		
     }
	 gallery.setOnClickListener(new View.OnClickListener() {
		
		    public void onClick(View arg0) {
		              //Starting a new Intent
             if (Image_Paths.size()==0){
            	 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailsOfTrekActivity.this);
      	       
	      			// set title
	      			alertDialogBuilder.setTitle("No captured photos");
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
		    		
		    	} else {
		    	
		    	 Intent GalleryScreen = new Intent(getApplicationContext(),GalleryActivity.class);
		    	 GalleryScreen.putExtra("key_id",key_id);
		         GalleryScreen.putExtra("Image_P",Image_Paths);
		         GalleryScreen.putExtra("trek_id_photos",trek_id_photos);
		         startActivity(GalleryScreen);}
		    	}});
	 
	 diary.setOnClickListener(new View.OnClickListener() {
	        
		    public void onClick(View arg0) {
		              //Starting a new Intent
		    	 
		    	 Intent DiaryRecordScreen = new Intent(getApplicationContext(),DiaryRecordActivity.class);
		    	 DiaryRecordScreen.putExtra("key_id",key_id);
		    	

		         startActivity(DiaryRecordScreen);
		    	}});
	
	
}//close on create	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
		 super.onCreateOptionsMenu(menu);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.action_bar_details_trek, menu);
	   // this.menu=menu;
	    return true;
	  } 
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
		
	    switch (item.getItemId()) {
	    case R.id.login:
	    	if (item.getTitle().equals("Log In")) {
	        
	    	Intent LoginScreen = new Intent(getApplicationContext(), LoginActivity.class);

	   	    startActivity(LoginScreen);
	    	}else if (item.getTitle().equals("Log Out")){
	    		user_settings.logoutUser();
	    		
	    		Intent MainScreen = new Intent(getApplicationContext(), MainActivity.class);

		   	    startActivity(MainScreen);
	    		
	    		
	    	}
	    	
	    	break;
	   	    
	     
	    case R.id.delete:
	    	Hik_APP_Databasehandler mDatabase = null; 
		    mDatabase = new Hik_APP_Databasehandler(this.getApplicationContext());
		    SQLiteDatabase db = mDatabase.getWritableDatabase();
			String astrArgs[] = { Integer.toString(key_id) };
	        int i=db.delete(Treks.treks, Treks.trek_id + "=?",astrArgs );
	        db.delete(Workout.workout, Workout.trek_id_w + "=?",astrArgs );
	        db.delete(Description.description, Description.trek_id_d + "=?",astrArgs );
	        db.delete(Route.route, Route.trek_id_r + "=?",astrArgs );
	        db.delete(Photos.photos, Photos.trek_id_p + "=?",astrArgs );
		    Log.d("delete",Integer.toString(i));
		    if (i==1){
		    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailsOfTrekActivity.this);
			       
      			// set title
      			alertDialogBuilder.setTitle("Your activity was deleted");
      			// set dialog message
      			alertDialogBuilder
      				.setMessage("Click OK to return in home!")
      				
      				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
      					public void onClick(DialogInterface dialog,int id) {
      						// if this button is clicked, close
      						// current activity
      					     Intent myIntent = new Intent( getApplicationContext(),MainActivity.class );
      					        startActivity(myIntent);
      					      
		    }
      			  });
      			// create alert dialog
  				AlertDialog alertDialog = alertDialogBuilder.create();
   
  				// show it
  				alertDialog.show();
  					
          }//close if
	        break;

		    case R.id.upload:
		    	SQLiteDatabase db1 = mDatabase1.getReadableDatabase();
				int up=-1;
	    		String Query = " SELECT " + Treks.uploaded + " FROM " + Treks.treks + " WHERE " + Treks.trek_id + " = " + key_id ;
	    				
	    			if (Query!=null){	
	    				Cursor c = db1.rawQuery(Query, null);
	    				if(c.moveToFirst())
	    				{
	    					if (c.getString(c.getColumnIndex(Treks.uploaded))!=null){
	    					up=(c.getInt(c.getColumnIndex(Treks.uploaded)));
	    					}
	    					
	    				}c.close();
	    			
	    			}
	    			db1.close();
	    			if (up==1){
	    				
	    				 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailsOfTrekActivity.this);
	    	      	       
	 	      			// set title
	 	      			alertDialogBuilder.setTitle("You have already uploaded this activity");
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
	    			else if (user_settings.isLoggedIn() == false) {
		        
		    		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailsOfTrekActivity.this);
				       
	      			// set title
	      			alertDialogBuilder.setTitle("You have to log in,first");
	      			// set dialog message
	      			alertDialogBuilder
	      				.setMessage("Click OK to login!")
	      				
	      				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
	      					public void onClick(DialogInterface dialog,int id) {
	      						// if this button is clicked, close
	      						// current activity
	      					     Intent myIntent = new Intent( getApplicationContext(),LoginActivity.class );
	      					        startActivity(myIntent);
	      					      
			    }
	      			  });
	      			// create alert dialog
	  				AlertDialog alertDialog = alertDialogBuilder.create();
	   
	  				// show it
	  				alertDialog.show();
		    		
		    		
		    		
		    		
		    		
		    		
		    		
		    	}//close if user is login
		    	else{
		    		SQLiteDatabase db2 = mDatabase1.getReadableDatabase();
					
		    		String Query2 = " SELECT " + Description.rate + "," + Description.findings + "," + Description.attractions + "," + Description.dangers + "," + Description.diff + " FROM " + Description.description + " WHERE " + Description.trek_id_d + " = " + key_id ;
		    				
		    			if (Query2!=null){	
		    				Cursor c = db2.rawQuery(Query2, null);
		    				if(c.moveToFirst())
		    				{
		    					
		    					findings_text=(c.getString(c.getColumnIndex(Description.findings)));
		    					
		    					attractions_text=(c.getString(c.getColumnIndex(Description.attractions)));
		    					
		    					dangers_text=(c.getString(c.getColumnIndex(Description.dangers)));
		    					
		    					mark_rate=(c.getInt(c.getColumnIndex(Description.rate)));
	
		    					mark_diff=(c.getInt(c.getColumnIndex(Description.diff)));
		    				}
		    				c.close();}
		    				db2.close();	
		    				
		    		progressDialog = new ProgressDialog(DetailsOfTrekActivity.this);
					progressDialog.setMessage("Uploading Data...");  
                   upload_task = new UploadTask();
                 
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                  		upload_task .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);}
                  	else{
                  		upload_task .execute((Void[])null);}
            	//if (Image_Paths.size()>0){
            	
		    		
		    		
		    		
		    		
		    	}
		    	
		    	
		    	
		    break;
	    default:
	      break;
	    }

	    return true;
	  } //close menu select
	
	/*  private void updateMenuTitles() {
	        MenuItem title_login = menu.findItem(R.id.login);
	        if (user_settings.isLoggedIn() == true) {
	    		// We have a user name
	        	title_login.setTitle("");
	        }
	    }*/
	  @Override
	  public boolean onPrepareOptionsMenu (Menu menu)
	  {

	

	      MenuItem title_login = menu.findItem(R.id.login);
	        if (user_settings.isLoggedIn() == true) {
	    		// We have a user name
	        	title_login.setTitle("Log Out");
	        }

	      return true;        
	  }//close onPrepareOptionsMenu
	  
	  
	  public class UploadTask extends   AsyncTask<Void,Void,Void>{
		  
		   private final HttpClient Client = new DefaultHttpClient();  
	       //private String Content;  
	       private String Error = null; 
	       
		   protected void onPreExecute() {  
			   
				progressDialog.show();
		   
				user_id_mysql=user_settings.getUserId(); 
	
		   } 
		@Override
		protected Void doInBackground(Void... params) {
			 String result;
			httppost = new HttpPost("http://10.0.2.2/Hiking_App/upload_trek.php");  
			nameValuePairs = new ArrayList<NameValuePair>();
					
			try{		
					
			
					// mark_rate=c.getInt(c.getColumnIndex(Description.rate));
					// mark_diff=c.getInt(c.getColumnIndex(Description.diff));
					// findings_text=c.getString(c.getColumnIndex(Description.findings));
					// attractions_text=c.getString(c.getColumnIndex(Description.attractions));
					// dangers_text=c.getString(c.getColumnIndex(Description.dangers));
					
					nameValuePairs.add(new BasicNameValuePair("findings_mysql",findings_text));
					Log.d("soniaa","sonia");
					
					
					nameValuePairs.add(new BasicNameValuePair("attractions_mysql",
							attractions_text));
				
					nameValuePairs
							.add(new BasicNameValuePair("dangers_mysql", 
							dangers_text));
					
					nameValuePairs.add(new BasicNameValuePair("rate_mysql", 
					Integer.toString(mark_rate)));
					
					nameValuePairs.add(new BasicNameValuePair("diff_mysql", 
							Integer.toString(mark_diff)));	
					
			
					
					
					
			
            
			nameValuePairs.add(new BasicNameValuePair("user_id_mysql",
					Integer.toString(user_id_mysql)));
		//	Log.d("sonia",Integer.toString(user_id_mysql));
			
			nameValuePairs.add(new BasicNameValuePair("location_mysql",
					text_loc.getText().toString()));
		//	Log.d("sonia",text_loc.getText().toString());
			nameValuePairs
					.add(new BasicNameValuePair("date_mysql", 
					text_date.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("activity_mysql", 
			text_type.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("start_lati_mysql",
					Double.toString(start1)));
			nameValuePairs.add(new BasicNameValuePair("start_longi_mysql",
					Double.toString(start2)));
		    nameValuePairs.add(new BasicNameValuePair("Lati_Json", 
				Lati_Json.toString()));
		    nameValuePairs.add(new BasicNameValuePair("Longi_Json", 
				Longi_Json.toString()));
		    nameValuePairs.add(new BasicNameValuePair("distance_mysql", 
					text_dist.getText().toString()));
		    nameValuePairs.add(new BasicNameValuePair("time_mysql", 
					text_time.getText().toString()));
		
		
		
		//	Log.d("vlakeia", Lati_Json.toString());
		//	Log.d("vlakeia", Longi_Json.toString());
			// for(int i=0;i<Latitude_mysql.size();i++)
            // {
                
				 
				 //nameValuePairs.add(new BasicNameValuePair("Latitude_mysql[]",Latitude_mysql.get(i)));
                 //Log.d("vlakeia", Latitude_mysql.get(i));
                // nameValuePairs.add(new BasicNameValuePair("Longitude_mysql[]",Longitude_mysql.get(i)));
            // }
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			response = Client.execute(httppost);
			entity = response.getEntity();
			 
			 is = entity.getContent();
			 
			
	
			responcecode=1;
	
		//	response_php=convertStreamToString(is).trim();
			
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
		           // jObj = new JSONObject(response_php);
		        	jObj = new JSONObject(json);
		          r1=Integer.parseInt(jObj.getString("result1"));
		        // r2=Integer.parseInt(jObj.getString("result2"));
		        //   r3=Integer.parseInt(jObj.getString("result3"));
		         //  r4=Integer.parseInt(jObj.getString("result4"));
		    trek_id_photos=Integer.parseInt(jObj.getString("trek_id"));
		      //     Log.d("result1",r1);
		      //   Log.d("result3",jObj.getString("result3"));
		       //  Log.d("result4",Integer.toString(trek_id_photos));
		  //  Log.d("result4",Integer.toString(r4));    
		  //  Log.d("result2",Integer.toString(r2));
		        } catch (JSONException e) {
		            Log.e("JSON Parser", "Error parsing data " + e.toString());
		        }
		        
		        
		        SQLiteDatabase db = mDatabase.getWritableDatabase();
				db.beginTransaction();
				try {
					
		
					  ContentValues trekvalues = new ContentValues();
				   
				        trekvalues.put(Treks.uploaded,1);
				    	
				    	db.update(Treks.treks, trekvalues, Treks.trek_id + " = ?",
				                new String[] { String.valueOf(key_id) });
			          
					
					db.setTransactionSuccessful();
				} finally {
					db.endTransaction();
				}
				db.close();
		 
		        

	
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

			
	          if (responcecode ==1 &&(r1==1)) {
	        	   
	        	  Toast.makeText(getBaseContext(),"Your Activity was uploaded!Now,you can upload the images of your trek! ",Toast.LENGTH_LONG).show();	
					
	          }
	           else if (responcecode ==0){
	        	   
	       	   
	       	 Toast.makeText(getBaseContext(),"No Upload",Toast.LENGTH_LONG).show();
	         }
		   }  //close onPostExecute
	    
		   
	   }//close logintask

	  private static String convertStreamToString(InputStream is) {
		    /*
		     * To convert the InputStream to String we use the BufferedReader.readLine()
		     * method. We iterate until the BufferedReader return null which means
		     * there's no more data to read. Each line will appended to a StringBuilder
		     * and returned as String.
		     */
		 BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		    StringBuilder sb = new StringBuilder();

		    String line = null;
		   
		    try {     while ((line = reader.readLine()) != null) {
		            sb.append(line + "\n");
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    } finally {
		        try {
		            is.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		    return sb.toString();
		}//END convertStreamToString()

}//CLOSE DetailsOfTrekActivity