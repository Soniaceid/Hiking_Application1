package com.example.hikingapp1;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;


import com.google.android.maps.GeoPoint;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

 public class PathOverlay extends Overlay{

	
	    	List<OverlayItem> paths;
	    	private Point m_point;
	    	private Point m_point2;
	    	private Paint m_paint;
	    	private Paint m_paint2;
	    	private RectF m_rect;
			private GeoPoint gp1;
			private GeoPoint gp2;
	    	
	    	private static final int START_RADIUS = 10;
	    	private static final int PATH_WIDTH = 4;
	    	

	    	public PathOverlay(GeoPoint gp1,GeoPoint gp2) {
	    		super();
	        	// TODO
	    	
	    		this.gp1 = gp1;
                this.gp2 = gp2;
	    	     m_point= new Point();
	    		 m_rect=new RectF();
	    		 m_point2= new Point();
	    		m_paint= new Paint();
	    		m_paint2= new Paint();
	    	
	    	}
	    	 
			public void setGeoPoints (GeoPoint gp1, GeoPoint gp2)
	         {
	                 this.gp1 = gp1;
	                 this.gp2 = gp2;
	         }
	    	@Override
	    	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
	        	// TODO
	    		 
	    		m_paint2.setARGB(255,0,255,0);
	    	    
	    	
	    	       Projection projection = mapView.getProjection();
	    	    
	    	       m_paint.setARGB(255,255,100,0);
	    
	    		     projection.toPixels(gp1,m_point);
	    		       projection.toPixels(gp2,m_point2);
	    		      
	    		       canvas.drawLine((float)m_point.x,(float)m_point.y,(float) m_point2.x, (float)m_point2.y, m_paint);
	    		       
	    		       m_paint.setStrokeWidth(PATH_WIDTH);
	    		      
		    	       
		    
	    	       
	    	}//close draw
	    	

		

			
	    
	    	

}//CLOSE PATHOVERLAY