package com.example.hikingapp1;

import java.sql.Date;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hikingapp1.Hik_APP_Database.Description;
import com.example.hikingapp1.Hik_APP_Database.Photos;
import com.example.hikingapp1.Hik_APP_Database.Route;
import com.example.hikingapp1.Hik_APP_Database.Treks;

import com.example.hikingapp1.Hik_APP_Database.Workout;

//import com.hikinga1pp.Hik_APP_Database.Description;
//import com.hikingapp.Hik_APP_Database.Route;
//import com.hikingapp.Hik_APP_Database.Treks;
//import com.hikingapp.Hik_APP_Database.Workout;

// This class handles the creation and versioning of the application database

public class Hik_APP_Databasehandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Hik_APP_Database.db";
    private static final int DATABASE_VERSION = 1;

        
    public Hik_APP_Databasehandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		
		
		if (!db.isReadOnly()) {
		    // Enable foreign key constraints
		    db.execSQL("PRAGMA foreign_keys=ON;");
		  }
		
		
		// Create the Treks table
		   db.execSQL("CREATE TABLE " + Treks.treks + " ("
					+ Treks.trek_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ Treks.location + " TEXT, " 
	                + Treks.date + " DATE,"
	                + Treks.start_lati + " DECIMAL,"
	                + Treks.start_longi + " DECIMAL,"
	                + Treks.uploaded + " INTEGER,"
	          + Treks.activity + " TEXT "+ ")");
		   
		// Create the Description table
		db.execSQL("CREATE TABLE " + Description.description + " ("
                + Description.desc_id + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Description.rate + " INTEGER,"
                + Description.diff + " INTEGER,"
                + Description.dangers + " TEXT,"
                + Description.findings + " TEXT,"
                + Description.trek_id_d + " INTEGER,"
                + Description.attractions + " TEXT, " 
                +"FOREIGN KEY(" + Description.trek_id_d + ") REFERENCES " + Treks.trek_id + "  ON DELETE CASCADE " + ")");
             //   +" FOREIGN KEY(" + Description.desc_id + ") REFERENCES Treks(" + Treks.trek_id + ")");
		// Create the Route table
		db.execSQL("CREATE TABLE " + Route.route + " ("
                + Route.route_id + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Route.lat + " DECIMAL, "
                + Route.trek_id_r + " INTEGER, "
                + Route.lon + " DECIMAL, "
                +"FOREIGN KEY(" + Route.trek_id_r + ") REFERENCES " + Treks.trek_id + " ON DELETE CASCADE " + ")");
                
             //*   +" FOREIGN KEY("+Route.route_id+") REFERENCES Treks(" + Treks.trek_id +")");
              //  +"FOREIGN KEY(id_work) REFERENCES workout(work_id)" 
               // +"FOREIGN KEY(id_trek) REFERENCES trek(trek_id));");
		// Create the Workout table
		db.execSQL("CREATE TABLE " + Workout.workout + " ("
                + Workout.work_id + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Workout.distance + " DECIMAL, "
                + Workout.time + " TEXT, "
                + Workout.speed + " DECIMAL, "
                + Workout.trek_id_w + " INTEGER, "
                + Workout.calories + " DECIMAL, "
                +"FOREIGN KEY(" + Workout.trek_id_w + ") REFERENCES " + Treks.trek_id + " ON DELETE CASCADE " + ")");
               // +" FOREIGN KEY(" + Workout.work_id +") REFERENCES Treks(" + Treks.trek_id + ")");   
	
		// Create the Photo Paths table
				db.execSQL("CREATE TABLE " + Photos.photos + " ("
		                + Photos.photos_id + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
		                + Photos.path + " TEXT, "
		                + Photos.trek_id_p + " INTEGER, "
		              
		                +"FOREIGN KEY(" + Photos.trek_id_p + ") REFERENCES " + Treks.trek_id + " ON DELETE CASCADE " + ")");
		

}//close oncreate
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Housekeeping here.
		// Implement how "move" your application data during an upgrade of schema versions		
		// There is no ALTER TABLE command in SQLite, so this generally involves
		// CREATING a new table, moving data if possible, or deleting the old data and starting fresh
		// Your call.
		  // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Treks.treks);
        db.execSQL("DROP TABLE IF EXISTS " + Description.description);
        db.execSQL("DROP TABLE IF EXISTS " + Workout.workout);
        db.execSQL("DROP TABLE IF EXISTS " + Route.route);
        db.execSQL("DROP TABLE IF EXISTS " + Photos.photos);
        // Create tables again
        onCreate(db);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}
   
 
}//CLASS HELPER
