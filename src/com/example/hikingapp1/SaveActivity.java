package com.example.hikingapp1;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.android.maps.GeoPoint;
import com.example.hikingapp1.Hik_APP_Database;
import com.example.hikingapp1.Hik_APP_Database.Description;
import com.example.hikingapp1.Hik_APP_Database.Photos;
import com.example.hikingapp1.Hik_APP_Database.Route;
import com.example.hikingapp1.Hik_APP_Database.Treks;
import com.example.hikingapp1.Hik_APP_Database.Workout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class SaveActivity extends InitialActivity  {
	String value_time;
	double value_dist;
	double value_speed;
	int value_cal;
	String value_loc;
	String value_type;
	String k1;
	String k2;
	String k3;
	String value_findings="-";
	String value_attractions="-";
	String value_dangers="-";
	Integer value_rate=0;
	Integer value_diff=0;
	Integer length;
	Integer length1;
	 Double value_start1;
	 Double value_start2;
	ArrayList<GeoPoint> points ;
	public  ArrayList<Double> listLan;
	public  ArrayList<Double> listLong;
	public  ArrayList<String> Photo_paths;
Long success;
	@SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save);
        
      	Log.d("save", "soniaaaaaaaaaaaa"); 
      	Log.d("save", value_findings);
    	Log.d("save", value_attractions);
    	Log.d("save", value_dangers);
    	Log.d("save", Integer.toString(value_rate));
        listLan= new ArrayList<Double>();
        listLong= new ArrayList<Double>();
        Photo_paths=new ArrayList<String>();
        final String cdate = new SimpleDateFormat("yy-MM-dd").format(new Date());
       
    	
    		//value_findings=diary_data.TakeDiaryFind();
    	
        Bundle extras = getIntent().getExtras();//takes the choice of the user from the main activity about the type of the activity
       if (extras != null) {
        //	ArrayList<ParcelableGeoPoint> pointsExtra = extras.getParcelableArrayList("gpoints");

        //	points = new ArrayList<GeoPoint>();

        	//for(ParcelableGeoPoint point: pointsExtra) {
        	//   points.add(point.getGeoPoint());
       //	}
        	listLan = (ArrayList<Double>) getIntent().getSerializableExtra("Latitude");
        	listLong = (ArrayList<Double>) getIntent().getSerializableExtra("Longitude");
        	 Photo_paths=extras.getStringArrayList("Photos_Path");
        	value_time=extras.getString("time");
        	k1=extras.getString("findings_data");
        	k2=extras.getString("attractions_data");
        	k3=extras.getString("dangers_data");
        	  if (k1!=null){
        		  value_findings=extras.getString("findings_data");
         	    }
         	
         	if (k2!=null){
         		 value_attractions=extras.getString("attractions_data");
     	    }
         	 
       	if (k3!=null){
       		value_dangers=extras.getString("dangers_data");
     	    }
        	
        	
        	Log.d("save1", value_findings);
        	Log.d("save1", value_attractions);
        	Log.d("save1", value_dangers);
        	Log.d("save1", Integer.toString(value_rate));
        	
       // 	value_findings=extras.getString("findings_data");
       	 
     	// value_attractions=extras.getString("attractions_data");
     	
     	// value_dangers=extras.getString("dangers_data");
   	
        	value_type=extras.getString("type");
        	value_loc=extras.getString("loc");
        	value_cal = extras.getInt("cal");//we have the choice at  the string
            value_dist=extras.getDouble("dist");
            value_speed=extras.getDouble("speed");
            value_start1=extras.getDouble("Start1");
            value_start2=extras.getDouble("Start2");
            }
       length=listLong.size();
       length1= Photo_paths.size();
     /*  final Button delete = (Button) findViewById(R.id.button2);
       delete.setOnClickListener(new View.OnClickListener() {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			onUpgrade(Hik_APP_Database,1,2);
		
       	
       	
       }//close onclick 
     	 
    });//close setonclick*/
       	
       	
       final Button discard = (Button) findViewById(R.id.button2);
       discard.setOnClickListener(new View.OnClickListener() {
           
   	    public void onClick(View arg0) {
   	              //Starting a new Intent
   	    	 
   	    	 Intent MainScreen = new Intent(getApplicationContext(),MainActivity.class);
   	    

   	         startActivity(MainScreen);
   	    	}});
        final Button save = (Button) findViewById(R.id.button1);
        save.setOnClickListener(new View.OnClickListener() {
        	
        	
        	
        	
        	
    	    public void onClick(View arg0) {
    	    	//if (points.size()>=1){
    	    	//for (int i=0;i<points.size();i++){
    	       // 	double x = points.get(i).getLatitudeE6() / 1000000F;
    	        //	double y = points.get(i).getLongitudeE6() / 1000000F;
    	        //	Latitude.add(x);
    	        //	Longitude.add(y);
    	    	
    	    //	}
    	    //	}
    	    	// Save new records
    	    
				SQLiteDatabase db = mDatabase.getWritableDatabase();
				db.beginTransaction();
				try {

		
					  ContentValues trekvalues = new ContentValues();
				        trekvalues.put(Treks.date, cdate); // date
				        trekvalues.put(Treks.location, value_loc); // location
				        trekvalues.put(Treks.activity, value_type); // type of activity
				        trekvalues.put(Treks.start_lati, value_start1);
				        trekvalues.put(Treks.start_longi, value_start2);
				        trekvalues.put(Treks.uploaded,0);
				 
				        success=db.insert(Treks.treks, null, trekvalues);
				        
				        String selectQuery = " SELECT  * FROM " + Treks.treks;
				        SQLiteDatabase db1 = mDatabase.getReadableDatabase();
						   Cursor cursor = db1.rawQuery(selectQuery, null);
						   cursor.moveToFirst();
						   cursor.moveToPosition(cursor.getCount() - 1);
						  // cursor.moveToLast();
						    Integer index = cursor.getInt( cursor.getColumnIndex(Treks.trek_id) );
						    cursor.close();
				        //db.close(); // Closing database connection
				
					ContentValues workvalues = new ContentValues();
					workvalues.put(Workout.trek_id_w, index);
					workvalues.put(Workout.speed, value_speed);
					workvalues.put(Workout.distance, value_dist);//distance
					 workvalues.put(Workout.calories, value_cal); // calories
				      workvalues.put(Workout.time, value_time); //time
				        
					db.insert(Workout.workout, null,
							workvalues);
					ContentValues photosvalues = new ContentValues();
					for (int i=0;i<length1;i++){
						 photosvalues.put(Photos.path ,Photo_paths.get(i));
						 photosvalues.put(Photos.trek_id_p ,index);
					
					db.insert(Photos.photos, null,
							 photosvalues);
					}
					ContentValues routevalues = new ContentValues();
					for (int i=0;i<length;i++){
					routevalues.put(Route.lon ,listLong.get(i));
					routevalues.put(Route.lat ,listLan.get(i));
					routevalues.put(Route.trek_id_r, index);
					db.insert(Route.route, null,
							routevalues);
					}
					//ContentValues routevalues_lati = new ContentValues();
					//for (int i=0;i<listLan.size();i++){
					//routevalues_lati.put(Route.lat ,listLan.get(i));
					//routevalues_lati.put(Route.trek_id_r, index);
					//db.insert(Route.route, null,
					//		routevalues_lati);
					ContentValues descriptionvalues=new ContentValues();
					descriptionvalues.put(Description.findings,value_findings);
					descriptionvalues.put(Description.trek_id_d,index);
					descriptionvalues.put(Description.attractions,value_attractions);
					descriptionvalues.put(Description.dangers,value_dangers);
					descriptionvalues.put(Description.rate,value_rate);
					descriptionvalues.put(Description.diff,value_diff);
					db.insert(Description.description, null,
							descriptionvalues);
					
					//db.close(); // Closing database connection
					db.setTransactionSuccessful();
				} finally {
					db.endTransaction();
				}

				// reset form
			//	petName.setText(null);
			//	petType.setText(null);
				db.close();
	         
				if (success!=-1){ AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SaveActivity.this);
			       
				// set title
				alertDialogBuilder.setTitle("Your activity was saved!");
				// set dialog message
				alertDialogBuilder
					.setMessage("Click OK to return home")
					.setCancelable(false)
					.setPositiveButton("OK",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, close
							// current activity
						   	Intent MainScreen = new Intent(getApplicationContext(), MainActivity.class);

					   	     


				            startActivity(MainScreen);
						      
			                  
						}
						
					  });
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
					
	  }//close if}
	        else{AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SaveActivity.this);
		       
				// set title
				alertDialogBuilder.setTitle("A problem occured, your activity wasn't saved");
				// set dialog message
				alertDialogBuilder
					.setMessage("Click OK to return home")
					.setCancelable(false)
					.setPositiveButton("OK",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, close
							// current activity
						   	Intent MainScreen = new Intent(getApplicationContext(), MainActivity.class);

					   	     


				            startActivity(MainScreen);
						      
			                  
						}
						
					  });
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();}
        }//close onclick 
   	
    });//close setonclick
        
	}//close oncreate
}//CLOSE SAVEACTIVITY