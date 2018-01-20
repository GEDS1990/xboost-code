package com.mckinsey.ckc.sf.restful.data;

public class MoveRequest {
	public int carrierID;
	public int timeID;
	public double currentLong;
	public double currentLat;
	public int capacity;
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
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
}
