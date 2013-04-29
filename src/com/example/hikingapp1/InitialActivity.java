package com.example.hikingapp1;


import com.example.hikingapp1.Hik_APP_Database;

import android.os.Bundle;
import android.app.Activity;

public class InitialActivity extends Activity {
	// Our application database
		protected Hik_APP_Databasehandler mDatabase = null; 
		
			
		
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mDatabase = new Hik_APP_Databasehandler(this.getApplicationContext());
	
    
    }

 

    @Override
	protected void onDestroy() {
		super.onDestroy();
		if(mDatabase != null)
		{
			mDatabase.close();
		}
	
    }
}