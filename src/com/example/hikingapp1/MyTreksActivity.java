package com.example.hikingapp1;




import com.example.hikingapp1.Hik_APP_Database.Treks;
import com.example.hikingapp1.Hik_APP_Database.Workout;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MyTreksActivity extends InitialActivity  {
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mytreks);
		
		TableLayout table = (TableLayout) findViewById(R.id.TableLayout);
		  SQLiteDatabase db = mDatabase.getReadableDatabase();
		
		
		
			String Query = " SELECT " + Treks.trek_id + ","+ Treks.activity + "," + Treks.date + "," + Workout.distance + "," +Workout.time + " FROM " + Treks.treks + " INNER JOIN " + Workout.workout + " ON " + Treks.trek_id + " = " + Workout.trek_id_w + " ORDER BY " + Treks.date + " DESC " ;
			 Log.d("sonia",Query);
			Cursor c = db.rawQuery(Query, null);
			  
		   
			if(c.moveToFirst())
			{
				for(int i = 0; i< c.getCount(); i++)
				{
				Log.d("plithos",Integer.toString(c.getCount()));
					TableRow newRow = new TableRow(this);
					TextView actiCol = new TextView(this);
					TextView dateCol = new TextView(this);
					TextView distCol = new TextView(this);
					TextView timeCol = new TextView(this);
					Button details=new Button(this);	
					
					newRow.setTag(c.getInt(c.getColumnIndex(Treks.trek_id)));		// set the tag field on the TableRow view so we know which row to delete
					dateCol.setText(c.getString(c.getColumnIndex(Treks.date)));
					actiCol.setText(c.getString(c.getColumnIndex(Treks.activity)));
					
					Log.d("sonia",c.getString(c.getColumnIndex(Treks.activity)));
					distCol.setText(Double.toString(c.getDouble(c.getColumnIndex(Workout.distance))));
					
					timeCol.setText(c.getString(c.getColumnIndex(Workout.time)));
					
					actiCol.setTextSize(10);
					dateCol.setTextSize(10);
					 distCol.setTextSize(10);
					 timeCol.setTextSize(10);
					 details.setWidth(10);
					 details.setHeight(10);
					details.setText("More");
					details.setTextSize(10);
					details.setTag(c.getInt(c.getColumnIndex(Treks.trek_id)));
		         		
					// set the tag field on the button so we know which ID to delete
					details.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {

							
							Integer id = (Integer) v.getTag();
							 Intent DetailsOfTrekScreen = new Intent(getApplicationContext(),DetailsOfTrekActivity.class);
							 DetailsOfTrekScreen.putExtra("id",id);
					    	

					         startActivity(DetailsOfTrekScreen);
							/*
							
							// Find and destroy the row tagged with the deleted pet id in the Table 
							final TableLayout petTable = (TableLayout) findViewById(R.id.TableLayout_PetList);
							// This should return the TableRow as the first tagged view in the layout but it would be nice if it returned an array of views with that tag...
							View viewToDelete = petTable.findViewWithTag(id);
							petTable.removeView(viewToDelete);
							;*/
							
						}//close onclicklistener
					});//close setonclicklistener
					newRow.addView(actiCol);
					
					newRow.addView(dateCol);
					newRow.addView(distCol);
					newRow.addView(timeCol);
					
					newRow.addView(details);
					
					table.addView(newRow);
					c.moveToNext();
				}
				
			}else
			{
				Log.d("xazoo","sonia");}
			
			
			c.close();
			
	
	 
	             
		
	 

	        
	             
	        



	db.close();
	   

		
		
		
	
	
	
	}//on create
	
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_explore, menu);
        return true;
    }
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    case R.id.close:
	    	Intent ExploreScreen = new Intent(getApplicationContext(), ExploreActivity.class);

   	     


            startActivity(ExploreScreen);	
	    	break;
	    	
	   
	   
	   default:
		  break;}
	   return true;
	    
	  //respond to menu item selection
	}//close onOptionsItemSelected
	
}//CLOSE MYTREKSACTIVITY