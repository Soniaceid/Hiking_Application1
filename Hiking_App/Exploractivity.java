package com.hikingapp;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

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
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.hikingapp.GalleryActivity.UploadIMAGES;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



//Choice of Activity
public class ExploreActivity extends MapActivity implements LocationListener {
	/** Called when the activity is first created. */
private MapView m_vwMap;	
private MyLocationOverlay m_locationOverlay;
private LocationManager m_locManager;
private static final double GEO_CONVERSION = 1E6;
private static final int MIN_TIME_CHANGE = 1000;
private static final int MIN_DISTANCE_CHANGE = 1;
private GeoPoint p=null;
private Location currentlocation;
private List<Overlay> mapoverlays; //list of drawing overlays on the map 
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
private double current_x;
private double current_y;
private double radius=6371.01;
private double distance=5;
private String currentlocation_;
private ExploreCloseRouteTask explore_task;
private double lat;
private double lon;
public  String Url="http://10.0.2.2/Hiking_App/";
public JSONArray coordinates;
public JSONArray IDS;
public static final double PI = 3.14159265;
public static final double deg2radians = PI/180.0;
private ProgressDialog progressDialog;
public  ArrayList<GeoPoint> TrekPath;
private PathOverlay m_pathOverlay;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.explore);
		m_vwMap = (MapView) findViewById(R.id.map_view);
		m_vwMap.setSatellite(false);
		m_vwMap.setBuiltInZoomControls(true);
		final MapController mapControl = m_vwMap.getController();
		mapControl.setZoom(15);
		m_locationOverlay=new MyLocationOverlay(this,m_vwMap);
		m_locationOverlay.enableCompass();//enable combass on the map
		m_locationOverlay.enableMyLocation();//enable current location on the map
		 m_locManager= (LocationManager)getSystemService(Context.LOCATION_SERVICE);
     	
		 TrekPath=new ArrayList<GeoPoint>();
     	m_locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_CHANGE,MIN_DISTANCE_CHANGE,ExploreActivity.this); 
    	mapoverlays= m_vwMap.getOverlays();
        //add the new overlay to the list
		mapoverlays.add(m_locationOverlay);
		zoomToMyLocation();
	
	}//close oncrete
	
	

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLocationChanged(Location location) {
if (location != null) {
			
			double lon = (double) (location.getLongitude() * GEO_CONVERSION);
			double lat = (double) (location.getLatitude() * GEO_CONVERSION);
			current_x = (double)location.getLatitude();
			current_y = (double)location.getLongitude();
			int lontitue = (int)lon;
			int latitute = (int)lat;
		
		
		
		   p=(new GeoPoint(latitute,lontitue));
		  
		currentlocation=location;
		 currentlocation_=(ConvertPointToLocation(p));
		// Log.d("current position",currentlocation);
		   }//close if flag
		zoomToMyLocation();
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	   
	private void zoomToMyLocation() {
		GeoPoint myLocationGeoPoint = m_locationOverlay.getMyLocation();
		if(myLocationGeoPoint != null) {
			m_vwMap.getController().animateTo(myLocationGeoPoint);
			m_vwMap.getController().setZoom(16);
		}
		else {
			Toast.makeText(this, "Cannot determine location", Toast.LENGTH_SHORT).show();
		}
	}//close zoom to my location
	public String ConvertPointToLocation(GeoPoint gp) {

        String address = "";
        String placeName = null;

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());

        try {

            //  List<Address> addresses = geoCoder.getFromLocation(

              //            (gp.getLatitudeE6()/1E6),(gp.getLongitudeE6()/1E6), 1);

              Iterator<Address> addresses = geoCoder.getFromLocation(

                      (gp.getLatitudeE6()/1E6),(gp.getLongitudeE6()/1E6), 1).iterator();
              if (addresses != null) {
                  while (addresses.hasNext()){
                	  Address namedLoc = addresses.next();
                   placeName = namedLoc.getLocality();

              }
              }
        } catch (IOException e) {

        }

        return placeName;

  }
	  public class ExploreCloseRouteTask extends   AsyncTask<Void,Void,Void>{
	
		   private final HttpClient Client = new DefaultHttpClient();  
	       //private String Content;  
	     private String Error=null;
	     GeoLocation geo=new GeoLocation();
	     GeoLocation location=GeoLocation.fromDegrees(current_x,current_y);
	     GeoLocation[] boundingCoordinates =
	    		 location.boundingCoordinates(distance, radius);
	 		boolean meridian180WithinDistance =
	 			boundingCoordinates[0].getLongitudeInRadians() >
	 			boundingCoordinates[1].getLongitudeInRadians();
	 	
		   
	 		
	 		protected void onPreExecute() {  
			 
			   
			   Log.d("ddfff",Boolean.toString(meridian180WithinDistance));
			   progressDialog.show();
		   
				if 	(meridian180WithinDistance==true){
		 			Url=Url+"closeOR.php";
		 		}else{
		 			Url=Url+"closeAND.php";
		 		}
		   } 
		@Override
		protected Void doInBackground(Void... params) {
		
			
					
			try{	
			
			httppost = new HttpPost(Url);  
			nameValuePairs = new ArrayList<NameValuePair>();
		
		  //  nameValuePairs.add(new BasicNameValuePair("current_x", 
		 //   		Double.toString(current_x)));
		//	Log.d("namevaluepairs",Double.toString(current_x));
		//	 nameValuePairs.add(new BasicNameValuePair("current_y", 
		//	    		Double.toString(current_y)));
		//	 Log.d("namevaluepairs",Double.toString(current_y));
		//	 nameValuePairs.add(new BasicNameValuePair("current_loc", 
		//	    		currentlocation));
		//	 Log.d("namevaluepairs",currentlocation);
			
			nameValuePairs.add(new BasicNameValuePair("var1", 
				  Double.toString(boundingCoordinates[0].getLatitudeInRadians())));
			nameValuePairs.add(new BasicNameValuePair("var2", 
				 Double.toString( boundingCoordinates[1].getLatitudeInRadians())));
			nameValuePairs.add(new BasicNameValuePair("var3", 
				Double.toString(boundingCoordinates[0].getLongitudeInRadians())));
		
			nameValuePairs.add(new BasicNameValuePair("var4", 
					Double.toString(boundingCoordinates[1].getLongitudeInRadians())));
			nameValuePairs.add(new BasicNameValuePair("var5", 
			Double.toString(location.getLatitudeInRadians())));	
			Log.d("namevaluepairs",Double.toString(location.getLatitudeInRadians()));
			nameValuePairs.add(new BasicNameValuePair("var6", 
			Double.toString(location.getLatitudeInRadians())));
		nameValuePairs.add(new BasicNameValuePair("var7", 
				Double.toString(location.getLongitudeInRadians())));
		nameValuePairs.add(new BasicNameValuePair("var8", 
		   		Double.toString(distance / radius)));
		nameValuePairs.add(new BasicNameValuePair("var9", 
				String.valueOf(meridian180WithinDistance)));	
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
		        	coordinates=new JSONArray();
		        	 IDS=new JSONArray();
		            jObj = new JSONObject(json);
		           coordinates=jObj.getJSONArray("data");
		            //coordinates=jObj.getString("data");
		            IDS=jObj.getJSONArray("ids");
		            //IDS=jObj.getString("ids");
		            Log.d("DATA",jObj.getString("data"));
		            Log.d("DATA_json",coordinates.toString());
		            Log.d("DATA_json",IDS.toString());
		            
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
		      ArrayList<Integer> resultList = new ArrayList<Integer>();
		      Log.d("lista as array",resultList.toString());
		      ArrayList<Integer> freq=new ArrayList<Integer>();
		      List<Integer> values = Arrays.asList();
		      int count=0;
		      int l6 = 0;
		      for ( int j=0;j<IDS.length();j++){
		    	  try {
					JSONObject element3= IDS.getJSONObject(j);
					l6=Integer.parseInt( element3.getString("trek_id_r"));
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    	  for ( int k=0;k<IDS.length();k++){
		    		  JSONObject element;
					try {
						element = IDS.getJSONObject(k);
						 int l5=Integer.parseInt( element.getString("trek_id_r"));	      
			    	     if (l6==l5){
						 count++;}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
					
		    	  }
		    	  freq.add(count); 
		      }
		  Log.d("frequency",freq.toString());
		  for ( int j=0;j<freq.size();j++){
			  
		 
			  for ( int k=0;k<freq.get(j);k++){
	        	  try {
					JSONObject element = coordinates.getJSONObject(k);
					//JSONObject element1 = coordinates.getJSONObject(k+1);
					 int l1=RadiansToGeo(Double.parseDouble( element.getString("latitude")));
					 int l2=RadiansToGeo(Double.parseDouble( element.getString("longitude")));
					// int l3=RadiansToGeo(Double.parseDouble( element1.getString("latitude")));
					// int l4=RadiansToGeo(Double.parseDouble( element1.getString("longitude")));
					 GeoPoint point = new GeoPoint(l1,l2);
					// GeoPoint point2 = new GeoPoint(l3,l4);
					TrekPath.add(point);
					 
					//	m_pathOverlay=new PathOverlay(point,point2);
					//	 m_vwMap.postInvalidate();
					//	mapoverlays.add(m_pathOverlay);
	        	  
	        	  
	        	  
	        	  } catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	  
	        	  
	        	  
	        	  
	        	  
	        	  }//for mesa
			  Log.d("trekpath",TrekPath.toString());
				TrekPathOverlay m_pathOverlay = new TrekPathOverlay(TrekPath);
				Log.d("heelloo","sonia");
				m_vwMap.postInvalidate();
				mapoverlays.add(m_pathOverlay);
				TrekPath.clear();
	          }//for ekso
		   }  //close onPostExecute
	    
		   
	   }//CLOSE EXPLORE CLOSE ROUTES TASKS
	  
	  @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.action_bar_explore, menu);
	        return true;
	    }
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
		    case R.id.close:
		    	progressDialog = new ProgressDialog(ExploreActivity.this);
				progressDialog.setMessage("Finding close routes...");  
				explore_task = new ExploreCloseRouteTask();
	        	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
	        		explore_task .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);}
	        	else{
	        		explore_task .execute((Void[])null);}
		    	break;
		    	
		   
		   
		   default:
			  break;}
		   return true;
		    
		  //respond to menu item selection
		}//close onOptionsItemSelected
		
		public int RadiansToGeo(double coord){
			
			int conv=(int) ((coord/deg2radians)*1e6);
		return	conv;
		}
}//CLOSE EXPLORE ACTIVITY