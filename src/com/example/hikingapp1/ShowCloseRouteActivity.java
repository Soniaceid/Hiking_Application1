package com.example.hikingapp1;




import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Toast;



public class ShowCloseRouteActivity extends MapActivity implements LocationListener {
	private MapView m_vwMap;	
	private MyLocationOverlay m_locationOverlay;
	private FlagOverlayDetails itemizedoverlay;
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
	private JSONArray trek_data;
	private Drawable marker;
	private ShowCloseRouteTask show_task;
	
	private  ArrayList<GeoPoint> ClosePath;
	public static final double PI = 3.14159265;
	public static final double deg2radians = PI/180.0;
	private ProgressDialog progressDialog;

	
int value;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        ClosePath=new ArrayList<GeoPoint>();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
           value = extras.getInt("trek_id");}
        Log.d("pou pairno",Integer.toString(value));
        
        initLayout();
        
        show_task = new ShowCloseRouteTask();
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
    		show_task .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);}
    	else{
    		show_task .execute((Void[])null);}
    
		m_vwMap.postInvalidate();
	
		
		
		TrekPathOverlay m_pathOverlay = new TrekPathOverlay(ClosePath);
		mapoverlays.add(m_pathOverlay);
	
	
	}//close create
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void initLayout() {
	
	
		setContentView(R.layout.show_close_route);
		progressDialog = new ProgressDialog(ShowCloseRouteActivity.this);
		progressDialog.setMessage("Loading the route..."); 
		 marker = getResources().getDrawable(R.drawable.flag);
		 m_locManager= (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
    	m_locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_CHANGE,MIN_DISTANCE_CHANGE,ShowCloseRouteActivity.this); 
    	
		m_vwMap = (MapView) findViewById(R.id.map_view);
		m_vwMap.setSatellite(false);
		m_vwMap.setBuiltInZoomControls(true);
		final MapController mapControl = m_vwMap.getController();
		mapControl.setZoom(15);
		m_locationOverlay=new MyLocationOverlay(this,m_vwMap);
		m_locationOverlay.enableCompass();//enable combass on the map
		m_locationOverlay.enableMyLocation();//enable current location on the map
		mapoverlays= m_vwMap.getOverlays();
         //add the new overlay to the list
		mapoverlays.add(m_locationOverlay);
		zoomToMyLocation();
	}
	
	  public class ShowCloseRouteTask extends   AsyncTask<Void,Void,Void>{
			
		   private final HttpClient Client = new DefaultHttpClient();  
	       //private String Content;  
	
	 	
		   
	 		
	 		protected void onPreExecute() {  
			 
			   
			  
			   progressDialog.show();
		   
				
		   } 
		@Override
		protected Void doInBackground(Void... params) {
		
			
			String Error=null;
			
			try{	
			
			httppost = new HttpPost("http://10.0.2.2/Hiking_App/show_trek.php");  
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
		        	trek_data=new JSONArray();
		        	 
		            jObj = new JSONObject(json);
		           trek_data=jObj.getJSONArray("trek_data");
		            //coordinates=jObj.getString("data");
		          //  IDS=jObj.getJSONArray("ids");
		            //IDS=jObj.getString("ids");
		            Log.d("DATA",jObj.getString("trek_data"));
		            Log.d("kleidiii",jObj.getString("kleidi"));
		            Log.d("DATA_json",trek_data.toString());
		           // Log.d("DATA_json",IDS.toString());
		            
		        } catch (JSONException e) {
		            Log.e("JSON Parser", "Error parsing data " + e.toString());
		        }
		 if (trek_data!=null){
		        for ( int j=0;j<trek_data.length();j++){
			    	
					JSONObject element= trek_data.getJSONObject(j);
					int l1=RadiansToGeo(Double.parseDouble( element.getString("latitude")));
					int l2=RadiansToGeo(Double.parseDouble( element.getString("longitude")));
					
					 
					GeoPoint point = new GeoPoint(l1,l2);
						// GeoPoint point2 = new GeoPoint(l3,l4);
					ClosePath.add(point);
                     if (j==0){
					OverlayItem overlayitem = new OverlayItem(point, "", "");
						 
					itemizedoverlay = new FlagOverlayDetails(marker,getApplicationContext(),value);
				        
				 
				    itemizedoverlay.addOverlay(overlayitem);
				        
				        
				    mapoverlays.add(itemizedoverlay);}
		        
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
		    if (trek_data==null){
		    	
		    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ShowCloseRouteActivity.this);
			       
		    	// set title
      			alertDialogBuilder.setTitle("The route is blank");
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
	    
		   
	   }//CLOSE EXPLORE CLOSE ROUTES TASKS
	public int RadiansToGeo(double coord){
			
			int conv=(int) ((coord/deg2radians)*1e6);
		return	conv;
		}
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
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
	public class FlagOverlayDetails extends ItemizedOverlay<OverlayItem> {
		  private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
		  Context mContext,c;
		int trek_id;
		  public FlagOverlayDetails(Drawable defaultMarker ) {
	        super(defaultMarker);
	        mContext = getApplicationContext();
	        // change the direction of the shadow so the bottom of the marker is the part "touching"
	        boundCenterBottom(defaultMarker);

	        //static data, so we call this right away
	        populate();
	    }
		  public FlagOverlayDetails(Drawable defaultMarker, Context context,int id) {

				this(defaultMarker);
				c = context;
				trek_id=id;
				// TODO Auto-generated constructor stub
			}

	    public void addOverlay(OverlayItem overlay) {
	        mOverlays.add(overlay);
	        populate();
	    }
	

	 
	    @Override
	    protected OverlayItem createItem(int i) {
	       
	    	return mOverlays.get(i);
	    
	    }
	 
	
		   
	   
	   
	    @Override
	    public int size() {
	        return mOverlays.size();
	    }
	    
	 
	    @Override
	    protected boolean onTap(int i) {
	        //when you tap on the marker this will show the informations provided by you when you create in the 
	        //main class the OverlayItem
	        OverlayItem item = mOverlays.get(i);
	        Intent ShowDETAILS = new Intent(getApplicationContext(),ShowDetailsCRActivity.class);
	        ShowDETAILS.putExtra("trek_id",value);
	  Log.d("id pou pernaei",Integer.toString(value));

	        startActivity( ShowDETAILS);  
	        return true;
	    }
	}//close FlagOverlayDetails
}//CLOSE ShowCloseRouteActivity