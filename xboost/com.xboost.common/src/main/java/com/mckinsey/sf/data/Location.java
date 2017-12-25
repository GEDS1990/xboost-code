package com.mckinsey.sf.data;
/**   
*    
* Author：Alivia Chen   
* Email : alivia_chen@mckinsey.com
* Date  ：Sep 18, 2017
* @version        
*/
public class Location {
	private String loc;
	private double lon;
	private double lat;
	
	
	public Location(String loc, double lon, double lat) {
		super();
		this.loc = loc;
		this.lon = lon;
		this.lat = lat;
	}
	
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	
	
}
