package com.example.hikingapp1;

import java.util.ArrayList;

import com.google.android.maps.GeoPoint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Chronometer;


public class MChronometer extends Chronometer {
        public int time_work=0;
        public int msElapsed;
        public boolean isRunning = false;
        private long timeWhenStopped = 0;
        public MChronometer(Context context) {
            super(context);
        }
 
        
       
                	  
        public int getMsElapsed() {
            return msElapsed;
        }
 
        public void setMsElapsed(int ms) {
            setBase(getBase() - ms);
            msElapsed  = ms;
        }
        public int get_time_work(){
        	return time_work = (int)(SystemClock.elapsedRealtime() - this.getBase());
        }
 
        @Override
        public void start() {
           // super.start();
           // setBase(SystemClock.elapsedRealtime() - msElapsed);
            //isRunning = true;
          setBase(SystemClock.elapsedRealtime());
       	  super.start();
       //	setBase(SystemClock.elapsedRealtime() - msElapsed);
       	  isRunning = true;
       	 
      	
      	
      	}
 
        @Override
        public void stop() {
        	super.stop();
        	setBase(SystemClock.elapsedRealtime());
             
              timeWhenStopped = 0;
        	
        	/*  if(isRunning) {
                 msElapsed = (int)(SystemClock.elapsedRealtime() - this.getBase());
                 // timeWhenStopped = 0;
        		//  setBase(SystemClock.elapsedRealtime());
        	  }
             */ isRunning = false;
        	//setBase(SystemClock.elapsedRealtime());
            
            // super.stop();
        	//    super.stop();
             // timeWhenStopped = 0;8*/
              
        }
   public void pause(){
    	//timeWhenStopped = getBase() - SystemClock.elapsedRealtime();
      //  stop();
    //	timeWhenStopped=getMsElapsed();
    /*	stop();
  	  if(isRunning) {
           msElapsed = (int)(SystemClock.elapsedRealtime() - this.getBase());
           // timeWhenStopped = 0;
  		//  setBase(SystemClock.elapsedRealtime());
  	  }*/
	
       super.stop();
       if(isRunning) {
           msElapsed = (int)(SystemClock.elapsedRealtime() - this.getBase());
       }
       isRunning = false; 
   }
    public void resume(){
    	//setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
    	//setMsElapsed(timeWhenStopped);
    	//start();
   //	start();
     //	setBase(SystemClock.elapsedRealtime() - msElapsed);
     	//  isRunning = true;
 	   super.start();
       setBase(SystemClock.elapsedRealtime() - msElapsed);
       isRunning = true;
    }
    
}//CLOSE CLASS MCHRONOMETER
