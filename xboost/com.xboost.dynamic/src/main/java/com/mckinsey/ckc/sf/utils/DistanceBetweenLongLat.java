package com.mckinsey.ckc.sf.utils;


import java.util.HashMap;

import com.mckinsey.ckc.sf.algo.Point;

public class DistanceBetweenLongLat {
	public static HashMap<String,Double> tripleList = new HashMap<String,Double>();
	private static final double EARTH_RADIUS = 6378137;  
	  private static double rad(double d)  
	     {  
	        return d * Math.PI / 180.0;  
	     }  
	       
	     /** *//** 
	      * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米 
	      * @param lng1 
	      * @param lat1 
	      * @param lng2 
	      * @param lat2 
	      * @return 
	      */  
	     public static double GetDistance(double lng1, double lat1, double lng2, double lat2)  
	   {  
	        double radLat1 = rad(lat1);  
	        double radLat2 = rad(lat2);  
	        double a = radLat1 - radLat2;  
	        double b = rad(lng1) - rad(lng2);  
	        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +   
	         Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));  
	        s = s * EARTH_RADIUS;  
	        s = Math.round(s * 10000) / 10000;  
	        return s;  
	     }  
	       
	       
	     /**  
	      * @param args 
	      */  
//	     public static void loadDistance() 
//	     {  
//	    	 JDBCConnection.loadDeptTable();
//	    	 JDBCConnection.loadOutFieldTable();
//	    	 List<Point> pointsList = DataPreparation.deptOutfieldPoints;
//	    	 for(Point point1:pointsList){
//	    		 for(Point point2:pointsList){
//	    			 double distance = GetDistance(point1.getX(),point1.getY(),point2.getX(),point2.getY());  
//	    			 Triple t = new Triple(point1,point2,distance);
//	    			 tripleList.put(calculateHash(point1, point2),distance);
//		    	 }
//	    	 }
//	     }  
	     
	     public static String calculateHash(Point point1,Point point2){
	    	 StringBuffer sb = new StringBuffer();
	    	 sb.append(point1.getX());
	    	 sb.append(point1.getY());
	    	 sb.append(point2.getX());
	    	 sb.append(point2.getY());
	    	 return sb.toString();
	     }

}
