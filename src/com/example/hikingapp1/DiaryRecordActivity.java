package com.example.hikingapp1;

import com.example.hikingapp1.Hik_APP_Database.Description;



import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.widget.EditText;

import android.widget.TextView;


public class DiaryRecordActivity extends InitialActivity  {
private int mark_rate;
private int mark_diff;
private String findings_text;
private String attractions_text;
private String dangers_text;
private int key_id;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary_record);
		TextView find=(TextView) findViewById(R.id.textView5);
		TextView attract=(TextView) findViewById(R.id.textView7);
		TextView danger=(TextView) findViewById(R.id.textView9);
		TextView  rate=(TextView) findViewById(R.id.textView4);
		TextView  diff=(TextView) findViewById(R.id.textView11);
		Bundle extras = getIntent().getExtras();//takes the choice of the user from the main activity about the type of the activity
		  if (extras != null) {
	            key_id = extras.getInt("key_id");}
		 SQLiteDatabase db = mDatabase.getReadableDatabase();
			
				String Query = " SELECT " + Description.rate + "," + Description.findings + "," + Description.attractions + "," + Description.dangers + "," + Description.diff + " FROM " + Description.description + " WHERE " + Description.trek_id_d + " = " + key_id ;
				
				
				Cursor c = db.rawQuery(Query, null);
				if(c.moveToFirst())
				{
					
					find.setText(c.getString(c.getColumnIndex(Description.findings)));
					attract.setText(c.getString(c.getColumnIndex(Description.attractions)));
				    danger.setText(c.getString(c.getColumnIndex(Description.dangers)));
					rate.setText(Integer.toString(c.getInt(c.getColumnIndex(Description.rate))));
					diff.setText(Integer.toString(c.getInt(c.getColumnIndex(Description.diff))));
					// mark_rate=c.getInt(c.getColumnIndex(Description.rate));
					// mark_diff=c.getInt(c.getColumnIndex(Description.diff));
					// findings_text=c.getString(c.getColumnIndex(Description.findings));
					// attractions_text=c.getString(c.getColumnIndex(Description.attractions));
					// dangers_text=c.getString(c.getColumnIndex(Description.dangers));
					
					
				}
				c.close();
				db.close();
	
	
	
	
	
	
	
	
	
	
	
	}//close oncreate
	
	
}//CLOSE DIARYRECORDACTIVITY