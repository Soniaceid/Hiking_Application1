package com.example.hikingapp1;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SharedPreferences_User_Settings{
	
	SharedPreferences pref;
	Editor editor;
	Context _context;
	int PRIVATE_MODE = 0;
	// Application preference values
	public static final String USER_SETTINGS = "User_Settings";
	public static final String KEY_EMAIL = "email";//login email
    public static final String KEY_PASSWORD = "password"; //login password  
    public static final String KEY_USER_ID = "user_id";
    public static final String IS_LOGIN = "IsLoggedIn";
	

    public SharedPreferences_User_Settings(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(USER_SETTINGS, PRIVATE_MODE);
		editor = pref.edit();
	}

    public void createLoginSession(String email, String password, Integer userid) {

		editor.putBoolean(IS_LOGIN, true);

		editor.putString(KEY_EMAIL, email);

		editor.putString(KEY_PASSWORD, password);
		
		editor.putInt(KEY_USER_ID, userid);
		editor.commit();
	}
    public int getUserId() {
		return pref.getInt(KEY_USER_ID,0);
	}
    public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGIN, false);
	}
    public void logoutUser() {

		editor.clear();
		editor.commit();


	
	}
	
}//CLOSE SharedPreferencesManager