package com.example.hikingapp1;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import android.location.Address;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;











import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;




public class CentralActivity extends MapActivity implements LocationListener{
  
	/** Called when the activity is first created. */
	Context context;
	private long timeWhenStopped = 0;
	
	private String text;
	public static String findings_data;
	private String path;
	private String attractions_data;
	private String dangers_data;
	private int mark_rate;
	private int mark_diff;
	private String String_finaltime;

	public static double distance=0;
	public static double distance_in_klm=0;
	
	private int time=0;
	private int burned_calories;
	private double speed;
	private String klm="0,0";
	private double MET;
	private String TypeOfActivity;//this variable takes the choice of the user about the type of the activity from the previous screen
	private double weight;//this variable takes the user's weight from the previous screen(passing in an Intent)
	private String location;
	private double finaldistance;
	private double finaltime;
	ArrayList<GeoPoint> ArrPathPoints;//array o
	private MChronometer chrono;
	public static ArrayList<String> Photos_path;
	public static ArrayList<Double> Latitude;
	public static ArrayList<Double> Longitude;
	public Double start_latitude;
	public Double start_longitude;
	private boolean flag=true;
	private boolean flag_start=true;
	private GeoPoint p1=null;//auxiliary geopoints for the plot of the route
	private GeoPoint p2=null;//the same
	private MapView m_vwMap;
	private MapController m_mapController;
	private PathOverlay m_pathOverlay;//object of the Class PathOverlay-will draw the route
	private MyLocationOverlay m_locationOverlay;//shows the current position
	private List<Overlay> mapoverlays; //list of drawing overlays on the map 
	public static ArrayList<GeoPoint> m_arrPathPoints;//list of  recorded geopoints while tracking the route
	private List<Overlay> m_arrPicturePoints;
	private LocationManager m_locManager;
	private String mBestProvider;
	

	private static final double GEO_CONVERSION = 1E6;
	private static final int MIN_TIME_CHANGE = 1000;
	private static final int MIN_DISTANCE_CHANGE = 1;
	private static final double MLSEC_CONV_MINUT =0.0000166666667 ;
	private static final double MLSEC_CONV_SECOND =0.001 ;
	private static final int THRESHOLD_EXERSISE = 60000; //In milliseconds
	
	 
	public static final int MEDIA_TYPE_IMAGE = 1;
	
	 private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	   
	    
	
	 
	    private Uri fileUri;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     // initLocationData();
     
        
        initLayout();
        final ImageView Cam= (ImageView) findViewById(R.id.cam);
        final Button Start = (Button) findViewById(R.id.button1);
        final Button Pause = (Button) findViewById(R.id.button2);
        final Button Diary=(Button) findViewById(R.id.button3);
        context=getApplicationContext();
        
        Diary.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
            	Intent DiaryScreen = new Intent(getApplicationContext(), DiaryActivity.class);

        	     


                startActivityForResult(DiaryScreen,1);	
            }
            });
        Cam.setOnClickListener(new OnClickListener() {
        	   @Override
        	   public void onClick(View v) {
        		   if(v.equals(Cam)){
        	   
        			   String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(MEDIA_TYPE_IMAGE));
        		         
        		        ContentValues values = new ContentValues();
        		        values.put(MediaStore.Images.Media.TITLE, "IMG_" + timeStamp + ".jpg");
        		 
        		        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        		         
        		        //fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image (this doesn't work at all for images)
        		        fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values); // store content values
        		        intent.putExtra( MediaStore.EXTRA_OUTPUT,  fileUri);
        		        
        		        // start the image capture Intent
        		        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);	 }       
        	   }        
        	});
       chrono.setOnChronometerTickListener(new OnChronometerTickListener()
    	
        {

      public void onChronometerTick(Chronometer arg0) {


         int timeElapsed;
	if (chrono.isRunning) {
		//setCalories(timeElapsed);
        //}
    	 timeElapsed=chrono.get_time_work();
    
   if	(timeElapsed>THRESHOLD_EXERSISE)
   {
     setCalories(timeElapsed);//close timeElapsed if 
     if (distance_in_klm>=0.1){
    setSpeed(timeElapsed);
    }
   }
    }//close isrunning

   }//close onChronometerTick

  }//close setOnChronometerTickListener

 );//close new OnChronometerTickListener

      //  final Chronometer Mchronometer = (Chronometer) findViewById(R.id.chronometer);
        
     // extract MapView from layout
     		
        /*
        mapView = (MapView) findViewById(R.id.map_view);
    	mapView.setBuiltInZoomControls(true);
    	
		mapView.setSatellite(true);
		final MapController mapControl = mapView.getController();
		mapControl.setZoom(5);
      
         LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        {
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER )) 
        { 
        	final Context context=this;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
    				context);
     
    			// set title
    			alertDialogBuilder.setTitle("You should enable gps to track the roote");
    			// set dialog message
    			alertDialogBuilder
    				.setMessage("Click OK to launch settings!")
    				.setCancelable(false)
    				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog,int id) {
    						// if this button is clicked, close
    						// current activity
    					     Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
    					        startActivity(myIntent);
    					        
    					}
    				  });
    			// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
				
        }//close if
       
    		
    	
    	
        	mapView.setBuiltInZoomControls(true);
        	
    		//mapView.setSatellite(true);
    		final MapController mapControl = m_vwMap.getController();
    		mapControl.setZoom(15);

    		 // create an overlay that shows our current location
    		MyOverlay = new MyLocationOverlay(this, m_vwMap);
    		MyOverlay.enableCompass();
    		MyOverlay.enableMyLocation();
    		//retrieve list of overlay objects
            mapOverlays=mapView.getOverlays();
            //add the new overlay to the list
            mapOverlays.add(MyOverlay);
            //start record
          //  setRecordingState(true);
        	// call convenience method that zooms map on our location
    		zoomToMyLocation();*/
   
       
 
        
        Pause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
            	  text = Pause.getText().toString();
            if (text.equals("Pause")){
               // timeWhenStopped = chrono.getBase() - SystemClock.elapsedRealtime();
               //chrono.stop();
                chrono.pause();
            	Pause.setText("Resume");
                    
            }
            
        else if (text.equals("Resume")){
        	//chrono.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        	//chrono.start();
        	chrono.resume();
        	Pause.setText("Pause");
        }
            }
            });
        
        
        Start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
            	  text = Start.getText().toString();
            if (text.equals("Start")){
            	//Mchronometer.setBase(SystemClock.elapsedRealtime());
            	 //Mchronometer.start();
            	chrono.start(); 
            	Start.setText("Stop");
            		 m_locManager= (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                	
               	 
                	m_locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_CHANGE,MIN_DISTANCE_CHANGE,CentralActivity.this); 
                	m_arrPathPoints=new ArrayList<GeoPoint>();
                	Latitude=new ArrayList<Double>();
                	Longitude=new ArrayList<Double>();
                	Photos_path=new ArrayList<String>();
                //	long timeElapsed = SystemClock.elapsedRealtime() - Mchronometer.getBase();
                	//if(timeElapsed>THRESHOLD_EXERSISE){
                		//setCalories(timeElapsed);
                    //}
                	
            }
            
        else if (text.equals("Stop")){
        	 //  Mchronometer.setBase(SystemClock.elapsedRealtime());
             //  Mchronometer.stop();
              // timeWhenStopped = 0;
        	finaltime=chrono.get_time_work();
        	
        	Integer hours = (int) (finaltime / (1000*60*60));
            Integer minutes = (int) ((finaltime % (1000*60*60)) / (1000*60));
        	Integer seconds = (int) (((finaltime % (1000*60*60)) % (1000*60)) / 1000);
        	String_finaltime=hours + ":"+ minutes + ":"+ seconds;
        	chrono.stop();   
        	Start.setText("Start");
        	TextView text1=(TextView)findViewById(R.id.textView4 );
        	
        	location=text1.getText().toString();
        	//TextView text2=(TextView)findViewById(R.id.editText1 );
        	//finaldistance=Double.parseDouble(text2.getText().toString());
        	//ArrayList<ParcelableGeoPoint> gpoints = new ArrayList<ParcelableGeoPoint>();
        	
        	
			//for(GeoPoint point : m_arrPathPoints) {
			//	gpoints.add(new ParcelableGeoPoint(point));
      // 	}
        	
        	Intent SaveScreen = new Intent(getApplicationContext(),SaveActivity.class);
        	SaveScreen.putExtra("cal",burned_calories);
	    	 SaveScreen.putExtra("dist",distance_in_klm);
	    	 SaveScreen.putExtra("loc",location);
	    	 SaveScreen.putExtra("type",TypeOfActivity);
	    	 SaveScreen.putExtra("Latitude", Latitude);
	    	 SaveScreen.putExtra("Longitude", Longitude);
	    	 SaveScreen.putExtra("findings_data", findings_data);
	    	 SaveScreen.putExtra("mark_diff", mark_diff);
	    	 SaveScreen.putExtra("attractions_data", attractions_data);
	    	 SaveScreen.putExtra("dangers_data", dangers_data);
	    	 SaveScreen.putExtra("mark_rate", mark_rate);
	    	 SaveScreen.putExtra("Photos_Path",Photos_path);
	    	 SaveScreen.putExtra("Start1", start_latitude);
	    	 SaveScreen.putExtra("Start2", start_longitude);
	    	 SaveScreen.putExtra("speed", speed);
	    	// SaveScreen.putParcelableArrayListExtra("gpoints", gpoints);
	    	
	    	 SaveScreen.putExtra("time", String_finaltime);
             ClearValues();
	    	 startActivity(SaveScreen);
	    	// onDestroy();
        }
            }
            });
   
  
  
    
  /*      LocationManager m_locManager= (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		mapView.setBuiltInZoomControls(true);
	
		//mapView.setSatellite(true);
		final MapController mapControl = mapView.getController();
		mapControl.setZoom(15);

		 // create an overlay that shows our current location
		MyOverlay = new MyLocationOverlay(this, mapView);
		MyOverlay.enableCompass();
		MyOverlay.enableMyLocation();
		//retrieve list of overlay objects
        mapOverlays=mapView.getOverlays();
        //add the new overlay to the list
        mapOverlays.add(MyOverlay);
    	// call convenience method that zooms map on our location
		zoomToMyLocation();
		// }//close if
		// }//close location manager*/
		
    }//CLOSE ONCREATE

    /*  private void initLocationData() {
    	// TODO
    /*	LocationManager m_locManager= (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	
    	 
    	m_locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_CHANGE,MIN_DISTANCE_CHANGE,(LocationListener)this);*/ 
    	//m_arrPathPoints=new ArrayList<GeoPoint>();
    	
    	//}//close initlocationdata*/
    
	private void initLayout() {
		setContentView(R.layout.central);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.relative_l);
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
		chrono = new MChronometer(this);
		
		LayoutParams params = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.RIGHT_OF,findViewById(R.id.cam).getId());
		//params.addRule(RelativeLayout.BELOW,findViewById(R.id.textView3).getId());
		params.setMargins(10, 46, 0,46);
		layout.addView(chrono);
		chrono.setLayoutParams(params);
		chrono.setWidth(150);
		chrono.setHeight(80);
		chrono.setTextSize(25);
		 Bundle extras = getIntent().getExtras();//takes the choice of the user from the main activity about the type of the activity
	        if (extras != null) {
	            int value = extras.getInt("choice");//we have the choice at  the string
	           weight=extras.getInt("kilos");
	            
	           switch (value) {
	            case 1:  
	            	     MET=2.00;
	            	     TypeOfActivity = "Walking";
	                     break;        
	            case 2:  MET=7.00;
	            	TypeOfActivity = "Jogging";
	                     break;
	            case 3:  MET=5.8;
	            	TypeOfActivity = "Cycling";
	                     break;
	            case 4:  MET=5.3;
	            	TypeOfActivity = "Hiking";
	                     break;
	            case 5:  MET=10;
	            	TypeOfActivity = "Running";
	                     break;
	            case 6:  MET=8.5;
	            	TypeOfActivity = "Mountain-Biking";
	                     break;
	            case 7:  MET=6.3;
	            	TypeOfActivity = "Free-Climbing";
	                     break;
	            case 8:  MET=5.0;
	            	TypeOfActivity = "Skating";
	                     break;
	            case 9:  MET=7.0;
	            	TypeOfActivity = "Skiing";
	                     break;
	            case 10: MET=2.5;
	            	TypeOfActivity = "Boating";
	                     break;
	            case 11: MET=2.8;
	            	TypeOfActivity = "Canoeing";
	                     break;
	          
	            default: 
	            	TypeOfActivity = "Invalid choice";
	                     break;
	        }//close switch for the set of the MET
	        
	        
	          
	        
	        }//close if of the Intents passing
	 
	  
	       m_vwMap.postInvalidate();
	     
    	// TODO
    }//close initLayout
	
	
	private void setCalories(int time){
		time=(int)(time*MLSEC_CONV_MINUT);
		burned_calories=(int) (((MET*3.5*weight) / 200 )*time);
	   TextView text_cal=(TextView)findViewById(R.id.editText2);
	   String burned = Double.toString(burned_calories);
		text_cal.setText(burned);

	}
	private double setDistance(GeoPoint g1,GeoPoint g2) {
		
		double DlonA=g1.getLatitudeE6() / 1000000F ;
		double DlanA=g1.getLongitudeE6() / 1000000F ;
		Location locationA = new Location("point A");  

		locationA.setLatitude(DlanA);
		locationA.setLongitude(DlonA); 
		double DlonB=g2.getLatitudeE6() / 1000000F ;
		double DlanB=g2.getLongitudeE6() / 1000000F ;
		Location locationB = new Location("point B");  

		locationB.setLatitude(DlanB);  
		locationB.setLongitude(DlonB);
		
         distance=distance +RoundDecimal(locationA.distanceTo(locationB),2);
		
		
		distance_in_klm=RoundDecimal((distance*0.001),2);
         String klm = Double.toString(distance_in_klm);
		
		((TextView)findViewById(R.id.editText1)).setText(klm+"klm");
		  return distance;
		}//close setdistance
	public double RoundDecimal(double value, int decimalPlace)
    {
            BigDecimal bd = new BigDecimal(value);

            bd = bd.setScale(decimalPlace, 6);

            return bd.doubleValue();
    }
	protected boolean isRouteDisplayed() {
		return false;
	}//close isroutdisplayed
	
	/**
	 * This method zooms to the user's location with a zoom level of 10.
	 */
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
	
public void setLocation(String addr){
	TextView text_loc=(TextView)findViewById(R.id.textView4);
	   
		text_loc.setText(addr);
		
}//close setLocation
public void setSpeed(int time){
	time=(int)(time*MLSEC_CONV_MINUT);
	
	speed=(distance_in_klm*60)/time;
	speed = RoundDecimal(speed,2);
   TextView text_speed=(TextView)findViewById(R.id.textView6);
   String speed_text = Double.toString(speed);
   text_speed.setText(speed_text+"klm/h");
	
	
	
}//close  setSpeed
public void setLists(double lat1,double lon1){

	Longitude.add(lon1);
    Latitude.add(lat1);
}
	@Override
	public void onLocationChanged(Location location) {
   if (location != null) {
			
			double lon = (double) (location.getLongitude() * GEO_CONVERSION);
			double lat = (double) (location.getLatitude() * GEO_CONVERSION);
			double x = (double)location.getLatitude();
			double y = (double)location.getLongitude();
			int lontitue = (int)lon;
			int latitute = (int)lat;
			if (flag_start){
			start_latitude=x;
			start_longitude=y;
			flag_start=false;}
			if (x!=0 && y!=0){
			 setLists(x,y);} 
			if (flag){
		   p1=(new GeoPoint(latitute,lontitue));
		   m_arrPathPoints.add(p1);
		
		 setLocation(ConvertPointToLocation(p1));
		   flag=false;
		   }//close if flag
			else{
			
				p2=p1;
				p1=(new GeoPoint(latitute,lontitue));
				m_arrPathPoints.add(p1);
				mapoverlays.add(new PathOverlay(p2,p1));
				m_pathOverlay=new PathOverlay(p2,p1);
				 m_vwMap.postInvalidate();
				mapoverlays.add(m_pathOverlay);
				setDistance(p2,p1);
				
				//if (m_arrPathPoints.size()%2==0){
			}//close else
			//mapoverlays.add(new PathOverlay(m_arrPathPoints,m_arrPicturePoints));}
			//mapoverlays.add(m_pathOverlay);
			zoomToMyLocation();    
       }//close if null
}//close onLocationChanged




	@Override
	public void onProviderDisabled(String provider) {
		
		
	}




	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	@Override
	
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	
	        // TODO Auto-generated method stub
	
	        super.onActivityResult(requestCode, resultCode, data);
	        if (requestCode == 1 && resultCode == RESULT_OK && data != null)
	        {
	        Log.d("sonia","sonia");
	        data.getExtras();
	        findings_data=data.getStringExtra("findings_text");
	        attractions_data=data.getStringExtra("attractions_text");
	        dangers_data=data.getStringExtra("dangers_text");
	        mark_rate=data.getIntExtra("mark_rate",0);
	        mark_diff=data.getIntExtra("mark_diff",0);
	        Log.d("sonia",findings_data);
	        Log.d("sonia",Integer.toString(mark_rate));
	        }  
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
                        Log.d("Sonia", "Image saved to:\n" + fileUri);
                        Log.d("Sonia", "Image path:\n" + fileUri.getPath());
                        Log.d("Sonia", "Image path:\n" + getRealPathFromURI(fileUri));
                        Log.d("Sonia", "Image name:\n" + getName(fileUri)); // use uri.getLastPathSegment() if store in folder
                      path=getRealPathFromURI(fileUri);
    	    	       
                      Photos_path.add(path);
  	    	      
                    }
                     
                } else if (resultCode == RESULT_CANCELED) {
                    // User cancelled the image capture
                } else {
                    // Image capture failed, advise user
                }
            }
     
           
        
      
	       
	    }//close onactivityresult*/
	/* @Override
	    public void onDestroy()
	    {
	        super.onDestroy();
	    	m_locManager.removeUpdates(CentralActivity.this);
	    }*/
	
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
            Log.e("sonia", "Error getting file name: " + e.getMessage());
        }
 
        return filename;
    }//close getname()
	public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }//close getrealpathfromuri
	/*@Override
	public void onResume() 
	{ 
	    super.onResume(); 
	 //   diary_data= new SharedPreferencesManager(getApplicationContext(),"DIARY_DATA") ;
    	
	 //   findings_data=diary_data.TakeDiaryFind();
	//Log.d("sonia",findings_data);
	     Intent data = getIntent();
	        findings_data=data.getStringExtra("findings_text");
	} */
private void ClearValues(){
	distance=0;
	
	burned_calories=0;
	
	//TextView text_cal=(TextView)findViewById(R.id.editText2);
	//text_cal.setText("");
	//((TextView)findViewById(R.id.editText1)).setText("0"+"klm");
	//setLocation("");
	 m_locManager.removeUpdates(this); 
	m_arrPathPoints.clear();
}	
}//CLOSE CENTRAL ACTIVITY