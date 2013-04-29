package com.example.hikingapp1;





import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
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
public class MainActivity extends Activity  {
	/** Called when the activity is first created. */
	private String type_act;
	private String[] activities;
	private int choice;
	private int kilos;
	private double cm;
	private SharedPreferences_User_Settings user_settings;
	private static final String TAG = "MyApp";//for Log.
	//private static final String[] items={"Walking","Jogging","Cycling","Hiking","Running",
   //     "Mountain-Biking","Free-Climbing","Skating","Skiing","Boating","Canoeing"};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		user_settings=new SharedPreferences_User_Settings(getApplicationContext());
	    final Button Gps = (Button) findViewById(R.id.button1);
	    final Button Start = (Button) findViewById(R.id.button2);
	    final Spinner spinner=(Spinner)findViewById(R.id.spinner1);
	    final EditText Edittext = (EditText) findViewById(R.id.editText2);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.activities_array, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
      

         

        // set the app icon as an action to go home

        // we are home so we don't need it

         
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                    int arg2, long arg3) {
                
               // int index = arg0.getSelectedItemPosition();
              choice= arg0.getSelectedItemPosition();
             
               // storing string resources into Array
               activities = getResources().getStringArray(R.array.activities_array);
               Log.d(getClass().getName(), String.format("value = %d", choice));   
              type_act=activities[choice]; 
              // while(true){
               //for (int i=0;i<11;i++){
       	    	//if (type_act.equals(activities[i])){
       	    		//choice=i;
       	    		//break;
       	    		//}
       	   /* else if(type_act=="Jogging"){
       	    	choice=2;
           	//	break;
       	    }else if(type_act=="Cycling"){
       	    	choice=3;
           		//break;
           		}
       	    else if(type_act=="Hiking"){
       	    	choice=4;
           		//break;
           		}
       	    else if(type_act=="Running"){
       	    	choice=5;
           		//break;
           		}
       	    else if(type_act=="Mountain-Biking"){
           	    	choice=6;
               		//break;
               		}
       	    else if(type_act=="Free-Climbing"){
       	    	choice=7;
           		//break;
           		}
       	    else if(type_act=="Skating"){
       	    	choice=8;
           		//break;
           		}
       	    else if(type_act=="Skiing"){
       	    	choice=9;
           		//break;
           		}
       	    else if(type_act=="Boating"){
       	    	choice=10;
           		//break;
           		}
       	    else if(type_act=="Canoeing"){
       	    	choice=11;
           	//	break;
       	    	}*/
              // }
       	 //   }          
                
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // do nothing
                
           }
                      
        });
	   
	      //Listening to the button Gps and check if Gps is enabled
	    Gps.setOnClickListener(new View.OnClickListener() {
        
	    public void onClick(View arg0) {
	               
	    	  LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	          {
	          if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER )) 
	          { 
	          	
	          AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
	       
	      			// set title
	      			alertDialogBuilder.setTitle("You should enable gps to track the route");
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
	          else {
	        	  
                Gps.setText("Gps is enabled"); 
	        	  
	          }
	       /*   if(text.equals("Start")){
	        	  Intent CentralScreen = new Intent(getApplicationContext(),CentralActivity.class);
	 	          startActivity(CentralScreen);  
	          }*/
	        	  
	          
	          }//close location
	         //  Intent CentralScreen = new Intent(getApplicationContext(),CentralActivity.class);
	         
	     


	          //   startActivity(CentralScreen);
	            //  MainActivity.this.finish();
	          }//close onclick 
	 
	      });
	  //Listening to button event Start and start the next "central" screen of the app
	    Start.setOnClickListener(new View.OnClickListener() {
        
	    public void onClick(View arg0) {
	              //Starting a new Intent
	    	 kilos=Integer.parseInt(Edittext.getText().toString());
	    	 Intent CentralScreen = new Intent(getApplicationContext(),CentralActivity.class);
	    	 CentralScreen.putExtra("choice",choice);
	    	 //CentralScreen.putExtra("type_act",type_act);
	    	 CentralScreen.putExtra("kilos",kilos);
	    	 CentralScreen.putExtra("cm",cm);

	         startActivity(CentralScreen);
	    	}});
}//close oncrete
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    case R.id.treks:
	    	Intent MyTreksScreen = new Intent(getApplicationContext(), MyTreksActivity.class);

   	     


            startActivity(MyTreksScreen);	
	    	break;
	    	
	    case R.id.explore:
	    	Intent ExploreScreen = new Intent(getApplicationContext(), ExploreActivity.class);

   	     


            startActivity(ExploreScreen);	
	    	break;
	    	
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
	    
	    	
	   
	   
	   default:
		  break;}
	   return true;
	    
	  //respond to menu item selection
	}//close onOptionsItemSelected
	   


}//CLOSE MAIN ACTIVITY