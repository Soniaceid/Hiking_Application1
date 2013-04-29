package com.example.hikingapp1;


import android.provider.BaseColumns;

public final class Hik_APP_Database {

    private Hik_APP_Database() {}
    
  /*  // Treks table
    public static final class Users implements BaseColumns {

        private Users() {}
        public static final String users = "USERS_TABLE_NAME";
        public static final String user_id = "USER_ID";
        public static final String email= "USER_EMAIL";
        public static final String nick = "USER_NICK";
        public static final String password = "USER_PASSWORD";
        public static final String birth = "USER_BIRTH";
        public static final String gender = "USER_GENDER";
        public static final String location_asc= "DEFAULT_SORT_ORDER";
    }
    
    
    
    */
    
    // Treks table
    public static final class Treks implements BaseColumns {

        private Treks() {}
        public static final String treks = "TREK_TABLE_NAME";
        public static final String trek_id = "TREK_ID";
        public static final String date= "TREK_DATE";
        public static final String location = "TREK_LOCATION";
        public static final String activity = "TREK_ACTIVITY";
        public static final String start_lati = "TREK_START_LATI";
        public static final String start_longi = "TREK_START_LONGI";
        public static final String uploaded = "IF_TREK_IS_UPLOADED";
        public static final String location_asc= "DEFAULT_SORT_ORDER";
    }
    
    
    
    // Description Type table
    public static final class Description implements BaseColumns {

        private Description() {}
      
        public static final String description = "DESCRIPTION_TABLE_NAME";
        public static final String desc_id = "DESC_ID";
        public static final String rate = "DESC_RATE";
        public static final String diff = "DESC_DIFF";
        public static final String dangers = "DESC_DANGERS";
        public static final String attractions= "DESC_ATTRACT";
        public static final String findings = "DESC_FINDINGS";
        public static final String trek_id_d = "TREK_ID_DESCR";
        
        public static final String rate_asc= "DEFAULT_SORT_ORDER";
    }
    
    //Workout Table
    public static final class Workout implements BaseColumns {

        private Workout() {}
      
        public static final String workout= "WORKOUT_TABLE_NAME";
        public static final String work_id = "WORK_ID";
        public static final String speed = "WORK_SPEED";
        public static final String calories = "WORK_CALORIES";
        public static final String distance = "WORK_DISTANCE";
        public static final String time= "WORK_TIME";
        public static final String trek_id_w = "TREK_ID_WORK";
        
        
        
        public static final String distance_ASC = "DEFAULT_SORT_ORDER";
    } 
    //Route Table
    public static final class Route implements BaseColumns {

        private Route() {}
      
        public static final String route= "ROUTE_TABLE_NAME";
        public static final String route_id = "ROUTE_ID";
        public static final String lat = "LATITUDE";
        public static final String lon = "LONGITUDE";
        public static final String trek_id_r = "TREK_ID_ROUTE";
        
     
      public static final String route_id_ASC = "DEFAULT_SORT_ORDER";
    }
    public static final class Photos implements BaseColumns {

        private Photos() {}
      
        public static final String photos= "PHOTOS_TABLE_NAME";
        public static final String photos_id = "PHOTOS_ID";
        public static final String path = "PATH";
        public static final String trek_id_p = "TREK_ID_PHOTOS";
        
     
      public static final String route_id_ASC = "DEFAULT_SORT_ORDER";
    }
    
	public void close() {
		// TODO Auto-generated method stub
		
	} 
}//close datavase