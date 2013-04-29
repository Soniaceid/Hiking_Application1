package com.example.hikingapp1;

import java.util.ArrayList;
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

 
/*
this class paints the path of a saved activity

*/
public class TrekPathOverlay extends Overlay{

	
	    	List<OverlayItem> paths;
	    	private Point m_point;
	    	private Point m_point2;
	    	private Paint m_paint;
	    	private RectF m_rect;
	    	private ArrayList<GeoPoint> points;
			private GeoPoint gp2;
	    	
	    	private static final int START_RADIUS = 10;
	    	private static final int PATH_WIDTH = 4;
	    	

	    	public TrekPathOverlay(ArrayList<GeoPoint> points) {
	    		super();
	        	// TODO
	    	
	    		this.points= points;
         
	    	     m_point= new Point();
	    		 m_rect=new RectF();
	    		 m_point2= new Point();
	    		m_paint= new Paint();
	    		
	    	
	    	}
	    	 
		
	    	@Override
	    	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
	        	// TODO
	    		Projection projection = mapView.getProjection();
	    	if (points.size()>=2){
	    	
	    		 GeoPoint start= points.get(0);
	    	       
	    	       GeoPoint next=points.get(1);
	    	       projection.toPixels(start,m_point);
		    	     projection.toPixels(next,m_point2);
		    	     m_paint.setARGB(255,0,255,0);
		    	       m_rect.set(m_point.x-START_RADIUS, m_point.y-START_RADIUS, m_point.x+START_RADIUS, m_point.y+START_RADIUS);
		    	       canvas.drawOval(m_rect,m_paint);
	    	for (int i=0;i<points.size()-1; i++){
	    	      // Projection projection = mapView.getProjection();
              
	    	       m_paint.setARGB(255,255,100,0);
	    
	    		     projection.toPixels(points.get(i),m_point);
	    		       projection.toPixels(points.get(i+1),m_point2);
	    		       canvas.drawLine((float)m_point.x,(float)m_point.y,(float) m_point2.x, (float)m_point2.y, m_paint);
	    		       
	    		       m_paint.setStrokeWidth(PATH_WIDTH);
	    	
	    	
		    	       
		    
	    	}//close for
	    	// GeoPoint end= points.get(points.size()-2);
  	       
  	      // GeoPoint end1=points.get(points.size()-1);
  	    // projection.toPixels(end,m_point);
	    // projection.toPixels(end1,m_point2);
  	     //m_paint.setARGB(255,255,0,0);
	     //  m_rect.set(m_point.x-START_RADIUS, m_point.y-START_RADIUS, m_point.x+START_RADIUS, m_point.y+START_RADIUS);
	     //  canvas.drawOval(m_rect,m_paint);
	    	}//close if
	    	mapView.postInvalidate();

	    	//mapView.getController().animateTo(points.get(0)); 
		
	    	}//close draw
	    	

		

			
	    
	    	

}//CLOSE PATHOVERLAY