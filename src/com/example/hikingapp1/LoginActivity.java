package com.example.hikingapp1;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
 

public class LoginActivity extends Activity {
    Button btnLogin;
    Button btnLinkToRegister;
    EditText inputEmail;
    EditText inputPassword;
    TextView loginErrorMsg;
public String email,password,retemail,retpass,is1;
public Integer ret_user_id;
public HttpPost httppost;
public ArrayList<NameValuePair> nameValuePairs;
public HttpResponse response;
public HttpEntity entity;
public ProgressDialog progressDialog;
public int responcecode;
public LoginTask login_task;
public SharedPreferences_User_Settings login_settings;
public String response_php;
public InputStream is;
public Boolean result;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        inputEmail = (EditText) findViewById(R.id.editText1);
        inputPassword = (EditText) findViewById(R.id.editText2);
        btnLogin = (Button) findViewById(R.id.button1);
        btnLinkToRegister = (Button) findViewById(R.id.signup);
        login_settings = new SharedPreferences_User_Settings(getApplicationContext());
        btnLogin.setOnClickListener(new View.OnClickListener() {
        	 
            public void onClick(View view) { 
       
            	
             	email =  inputEmail.getText().toString();
				password = inputPassword.getText().toString();
				if (email.length()==0 || password.length()==0){
					Toast.makeText(getBaseContext(),"You have to sumbit your email and password!",Toast.LENGTH_SHORT).show();
					
				}
				else{  progressDialog = new ProgressDialog(LoginActivity.this);
					progressDialog.setMessage("Logging in...");  
                  login_task = new LoginTask();
            	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            		login_task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
            	else
            		login_task.execute((Void[])null);
            }	
        	}
		});
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {
       	 
            public void onClick(View view) { 
       
            	Intent SignupScreen = new Intent(getApplicationContext(), SignUpActivity.class);

		   	     


	            startActivity(SignupScreen);
        	}
		});
     				
    }//close oncreate
    
    public void showLoginError(int responseCode) {
    	int duration = Toast.LENGTH_LONG;
    	Context context = getApplicationContext();
		Toast toast = Toast.makeText(context, "Login Error", duration);
		toast.show();
    }
   public class LoginTask extends   AsyncTask<Void,Void,Void>{
	  
	   private final HttpClient Client = new DefaultHttpClient();  
       //private String Content;  
       private String Error = null; 
       
	   protected void onPreExecute() {  
		   
			progressDialog.show();
	   
	   } 
	@Override
	protected Void doInBackground(Void... params) {
		try{
		httppost = new HttpPost("http://10.0.2.2/Hiking_App/login.php");  
		nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("email",
				email));
		nameValuePairs
				.add(new BasicNameValuePair("password", password));

		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		response = Client.execute(httppost);
		if (response.getStatusLine().getStatusCode() == 200) {
			//t1.setText("ok2");
			entity = response.getEntity();
			 is1 = EntityUtils.toString(entity);
			// is = entity.getContent();
			
				
			//response_php=convertStreamToString(is).trim();
			//Log.d("sonia",response_php);
			//result=((response_php.trim()).equals("no_account"));
			//if (result){
			//Log.d("sonia problem","vlakaaaaa");}
			if (entity != null) {

				JSONObject jsonResponse = new JSONObject(is1);
				retemail = jsonResponse.getString("email");
				retpass = jsonResponse.getString("password");
				ret_user_id=jsonResponse.getInt("user_id");
				Log.d("soniara",ret_user_id.toString());
				//t1.setText(retUser);
				//checks if the returned values match the ones that the user has entered
				if (email.equals(retemail) && password.equals(retpass)) {

					login_settings.createLoginSession(retemail, retpass,ret_user_id);
						responcecode=1;
					//	finish();
				}
				else{ 
					responcecode=0;
				
				}
			}
			

			

		}



	//ResponseHandler<String> responseHandler = new BasicResponseHandler();  
    //Content = Client.execute(httpget, responseHandler);  
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


           
           if (responcecode ==1 ) {
        	   
        	  
        		Intent MainScreen = new Intent(getApplicationContext(), MainActivity.class);

		   	     


	            startActivity(MainScreen);	
				
           }
           else if (responcecode ==0){
        	   
       	   
        	 Toast.makeText(getBaseContext(),"Wrong email or password!Try Again",Toast.LENGTH_SHORT).show();
          }
           
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


    	
}//CLOSE LOGINACTIVITY

