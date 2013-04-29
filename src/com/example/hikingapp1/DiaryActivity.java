package com.example.hikingapp1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class DiaryActivity extends InitialActivity  {
private int mark_rate;
private int mark_diff;
private String findings_text;
private String attractions_text;
private String dangers_text;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary);
	
		//diary_information=getSharedPreferences(DIARY_DATA, Context.MODE_PRIVATE);
		
		final Spinner spinner1=(Spinner)findViewById(R.id.spinner1);
		final Spinner spinner2=(Spinner)findViewById(R.id.spinner2);
		final Button back=(Button)findViewById(R.id.button1);
		final EditText find=(EditText) findViewById(R.id.editText1);
		final EditText attract=(EditText) findViewById(R.id.editText2);
		final EditText danger=(EditText) findViewById(R.id.editText3);
		 ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	                this, R.array.order_array, android.R.layout.simple_spinner_item);
		    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spinner1.setAdapter(adapter);
	        spinner2.setAdapter(adapter);
	        spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

	            public void onItemSelected(AdapterView<?> arg0, View arg1,
	                    int arg2, long arg3) {
	                
	               // int index = arg0.getSelectedItemPosition();
	             mark_rate= arg0.getSelectedItemPosition();
	             
	               // storing string resources into Array
	              // activities = getResources().getStringArray(R.array.order_array);
	             //  Log.d(getClass().getName(), String.format("value = %d", choice));   
	             // type_act=activities[choice]; 
	                
	                
	            }

	            public void onNothingSelected(AdapterView<?> arg0) {
	                // do nothing
	                
	           }
	                      
	        });//close spinner1 setonitemselected 
	        spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

	            public void onItemSelected(AdapterView<?> arg0, View arg1,
	                    int arg2, long arg3) {
	                
	               // int index = arg0.getSelectedItemPosition();
	             mark_diff= arg0.getSelectedItemPosition();
	             
	               // storing string resources into Array
	              // activities = getResources().getStringArray(R.array.order_array);
	             //  Log.d(getClass().getName(), String.format("value = %d", choice));   
	             // type_act=activities[choice]; 
	                
	                
	            }

	            public void onNothingSelected(AdapterView<?> arg0) {
	                // do nothing
	                
	           }
	                      
	        });//close spinner2 setonitemselected   	
	     
	            
	    	        back.setOnClickListener(new View.OnClickListener() {
	    	            
	    	    	    public void onClick(View arg0) {
	    	    	              //Starting a new Intent
	    	    	    	findings_text = find.getText().toString();
	    	    	   // 	if (findings_text.length()==0){
	    	    	    //		findings_text="-";
	    	    	    //	}
	    	    	    	attractions_text = attract.getText().toString();
	    	    	    //	if (attractions_text.length()==0){
	    	    	    //		attractions_text="-";
	    	    	    //	}
	    	    	    	dangers_text = danger.getText().toString();
	    	    	    //	if (dangers_text.length()==0){
	    	    		//dangers_text="-";
	    	    	    //	}
	    	    	    	
	    	    	    	Bundle bundle = new Bundle();
	    	    	    	bundle.putString("findings_text",findings_text);
	    	    	    	bundle.putString("attractions_text",attractions_text);
	    	    	    	bundle.putString("dangers_text",dangers_text);
	    	    	    	bundle.putInt("mark_rate",mark_rate);
	    	    	    	bundle.putInt("mark_diff",mark_diff);
	    	    	    	Intent mIntent = new Intent();
	    	    	        mIntent.putExtras(bundle);
	    	    	        setResult(RESULT_OK, mIntent);
	    	    	       finish();
	    	    	    	}}); 	    	
	    	  
	}//close oncreate
	/*@Override
	public boolean onKeyDown(int keycode, KeyEvent e) {
	    switch(keycode) {
	        case KeyEvent.KEYCODE_MENU:
	        	Bundle bundle = new Bundle();
	        	bundle.putString("findings_text",findings_text);
	        	bundle.putString("attractions_text",attractions_text);
	        	bundle.putString("dangers_text",dangers_text);
	        	Intent mIntent = new Intent();
	            mIntent.putExtras(bundle);
	            setResult(RESULT_OK, mIntent);
	            finish();
	            return true;
	    }

	    return super.onKeyDown(keycode, e);
	}*/
	/*@Override
	public void finish(){
	   //super.onPause();
     // diary_information=new SharedPreferencesManager(getApplicationContext(),"DIARY_DATA");
     // diary_information.createDiarydata(mark_rate,findings_text,attractions_text,dangers_text,mark_diff);
	 // Log.d("sonia","sonia"); 
		Bundle bundle = new Bundle();
    	bundle.putString("findings_text",findings_text);
    	bundle.putString("attractions_text",attractions_text);
    	bundle.putString("dangers_text",dangers_text);
    	Intent mIntent = new Intent();
        mIntent.putExtras(bundle);
        setResult(RESULT_OK, mIntent);
        super.finish();
       
	}*/
}//CLOSE DIARYACTIVITY