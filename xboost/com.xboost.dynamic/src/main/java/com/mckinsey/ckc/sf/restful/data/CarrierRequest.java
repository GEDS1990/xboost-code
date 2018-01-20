package com.mckinsey.ckc.sf.restful.data;

public class CarrierRequest {
	
	    private int carrierID;  
	    private int timeID;  
	    private double currentLong;  
	    private double currentLat;
	    
		public CarrierRequest(int carrierID, int timeID, double currentLong, double currentLat) {
			super();
			this.carrierID = carrierID;
			this.timeID = timeID;
			this.currentLong = currentLong;
			this.currentLat = currentLat;
		}
		public int getCarrierID() {
			return carrierID;
		}
		public void setCarrierID(int carrierID) {
			this.carrierID = carrierID;
		}
		public int getTimeID() {
			return timeID;
		}
		public void setTimeID(int timeID) {
			this.timeID = timeID;
		}
		public double getCurrentLong() {
			return currentLong;
		}
		public void setCurrentLong(double currentLong) {
			this.currentLong = currentLong;
		}
		public double getCurrentLat() {
			return currentLat;
		}
		public void setCurrentLat(double currentLat) {
			this.currentLat = currentLat;
		}  
	  
	   
}
