package com.example.hikingapp1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
/*this class 
 * 
 */
public class SignUpActivity extends Activity {
	public int pYear;
	public int pMonth;
	public int pDay;
	    /** This integer will uniquely define the dialog to be used for displaying date picker.*/
	    static final int DATE_DIALOG_ID = 0;
	    private TextView pDisplayDate;
	    private Button pPickDate;
	    public String gender_user;
	    private EditText nick;
	    private EditText email1;
	    private EditText pass;
	    public String nickname,email,password,date;
	    private ProgressDialog progressDialog;
	    public HttpPost httppost;
	    public HttpResponse response;
	    public HttpEntity entity;
	    public List<NameValuePair> nameValuePairs;
	    public InputStream is;
	    public int responsecode=-1;
	    public SignUpTask signtask;
	    public String response_php;
	    public boolean result;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);

		 nick=(EditText)findViewById(R.id.EditText_Nickname);
		 email1=(EditText)findViewById(R.id.EditText_Email);
		 pass=(EditText)findViewById(R.id.editText1);
		pPickDate=(Button)findViewById(R.id.Button_DOB);
		pDisplayDate=(TextView)findViewById(R.id.TextView_DOB_Info);
		Button signup=(Button)findViewById(R.id.btnNextScreen2);
		Spinner gender=(Spinner) findViewById(R.id.Spinner_Gender);
	    
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.genders, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);
        gender.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                    int arg2, long arg3) {
                
               // int index = arg0.getSelectedItemPosition();
              int choice= arg0.getSelectedItemPosition();
             
               // storing string resources into Array
              
              Log.d(getClass().getName(), String.format("value = %d", choice));   
              gender_user=getResources().getStringArray(R.array.genders)[choice]; 
              Log.d(getClass().getName(), gender_user);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // do nothing
                
           }
                      
        });//close onitemselected gender
		pPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
		        
		        /** Get the current date */
		        final Calendar cal = Calendar.getInstance();
		        pYear = cal.get(Calendar.YEAR);
		        pMonth = cal.get(Calendar.MONTH);
		        pDay = cal.get(Calendar.DAY_OF_MONTH);
		 
		        /** Display the current date in the TextView */
		        updateDisplay();

		        signup.setOnClickListener(new View.OnClickListener() {
		            
		    	    public void onClick(View arg0) {
		    	    	if (nick.getText().length() == 0) {
		    				nick.setText("Enter username");
		    			}

		    			else if (pass.getText().length() == 0) {
		    				pass.setText("Enter password");
		    			}else if (email1.getText().length() == 0) {
		    				email1.setText("Enter your email");
		    			} 
		    			
		    			else {
		    				nickname = nick.getText().toString().trim();
		    				password = pass.getText().toString().trim();
		    				email=email1.getText().toString().trim();
		    				date=pMonth+"/"+pDay+"/"+ pYear;
		    				progressDialog = new ProgressDialog(SignUpActivity.this);
							progressDialog.setMessage("Sending Data...");
							signtask=new SignUpTask();
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
								signtask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
			            	else
			            		signtask.execute((Void[])null);
		    				  }
		    	    	}});
	
	
	}//CLOSE ONCREATE
	
	  
	
	
	@Override
	    protected Dialog onCreateDialog(int id) {
	        switch (id) {
	        case DATE_DIALOG_ID:
	            return new DatePickerDialog(this, 
	                        pDateSetListener,
	                        pYear, pMonth, pDay);
	        }
	        return null;
	    }//close Dialog
	DatePickerDialog.OnDateSetListener pDateSetListener =
	        new DatePickerDialog.OnDateSetListener() {

	    

				@Override
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
				      pYear = year;
		                pMonth = monthOfYear;
		                pDay = dayOfMonth;
		              updateDisplay();
		               displayToast();
					
				}
	        };	//close datepickerdialog.listener
	        
	        /** Updates the date in the TextView */
	        private void updateDisplay() {
	            pDisplayDate.setText(
	                new StringBuilder()
	                        // Month is 0 based so add 1
	                        .append(pMonth + 1).append("/")
	                        .append(pDay).append("/")
	                        .append(pYear).append(" "));
	        }//close updatedipslay
	         
	       //  Displays a notification when the date is updated 
	        private void displayToast() {
	            Toast.makeText(this, new StringBuilder().append("Date choosen is ").append(pDisplayDate.getText()),  Toast.LENGTH_SHORT).show();
	                 
	        }//close display date TOAST
	        
public class SignUpTask extends   AsyncTask<Void,Void,Void>{
	      	  
	     	   private final HttpClient Client = new DefaultHttpClient();  
	            //private String Content;  
	            private String Error = null; 
	            
	     	   protected void onPreExecute() {  
	     		   
	     			progressDialog.show();
	     	   
	     	   } 
	     	@Override
	     	protected Void doInBackground(Void... params) {
	     		try{
	     			httppost = new HttpPost("http://10.0.2.2/Hiking_App/signup.php");

					nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("nickname", nickname));
					Log.d(getClass().getName(),nickname);
					nameValuePairs.add(new BasicNameValuePair("email",email));
					nameValuePairs.add(new BasicNameValuePair("password",password));
					nameValuePairs.add(new BasicNameValuePair("birth",date));
					nameValuePairs.add(new BasicNameValuePair("gender",gender_user));
					
					// nameValuePairs.add(new BasicNameValuePair("email", c));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            
					response = Client.execute(httppost);
					entity = response.getEntity();
					is = entity.getContent();
					responsecode=1;
					//response_php = EntityUtils.toString(entity);
					response_php=convertStreamToString(is).trim();
					Log.d("sonia",response_php);
					result=((response_php.trim()).equals("user_exists"));
					if (result){
					Log.d("sonia problem","vlakaaaaa");}
	     		}//close try

	     		catch (Exception e) {
					
					Log.e("log_tag", "Error in http connection " + e.toString());

				}

				return null;

	    

	       
	     	
	     	
	     	   
	     	} //close do background
	     	
	     	
	     	   protected void onPostExecute(Void unused) {  
	            	
	            	

                     progressDialog.dismiss();
	                
	                if (responsecode ==1 && response_php.equals("register") ) { 
	                	Toast.makeText(getApplicationContext(), "SUCCESS!",Toast.LENGTH_SHORT).show();
	             		Intent MainScreen = new Intent(getApplicationContext(), MainActivity.class);

	     		   	     


	     	            startActivity(MainScreen);	
	     				
	                }else if (response_php.equals("user_exists"))  {
	                	Toast.makeText(getBaseContext(),"User exists",Toast.LENGTH_SHORT).show();
	                }
	                else{Toast.makeText(getApplicationContext(), "problemm!",Toast.LENGTH_SHORT).show();
             		Intent MainScreen = new Intent(getApplicationContext(), MainActivity.class);

    		   	     


     	            startActivity(MainScreen);}
	            }  //close onPostExecute
	         
	     	   
	        }//close logintask


private static String convertStreamToString(InputStream is) {
    /*
     * To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String.
     */
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder sb = new StringBuilder();

    String line = null;
    try {
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    return sb.toString();
}//END convertStreamToString()







}//CLOSE SIGNUPACTIVITY



 