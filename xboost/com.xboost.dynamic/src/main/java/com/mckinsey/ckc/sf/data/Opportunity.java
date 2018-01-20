package com.mckinsey.ckc.sf.data;

public class Opportunity{
	private Carrier carrier;
	private Parcel parcel;
	private double urgency;
	private double currentDistance;
	private double destDistance;
	private double optParcelTimeRemaining;
	private double optParcelUrgency;
	private double parcelODDistance;
	private double angleBetween;
	private double incremenTime;
	
	public Carrier getCarrier() {
		return carrier;
	}
	public void setCarrier(Carrier carrier) {
		this.carrier = carrier;
	}
	public Parcel getParcel() {
		return parcel;
	}
	public void setParcel(Parcel parcel) {
		this.parcel = parcel;
	}
	public double getUrgency() {
		return urgency;
	}
	public void setUrgency(double urgency) {
		this.urgency = urgency;
	}
	public double getCurrentDistance() {
		return currentDistance;
	}
	public void setCurrentDistance(double currentDistance) {
		this.currentDistance = currentDistance;
	}
	public double getDestDistance() {
		return destDistance;
	}
	public void setDestDistance(double destDistance) {
		this.destDistance = destDistance;
	}
	public double getOptParcelTimeRemaining() {
		return optParcelTimeRemaining;
	}
	public void setOptParcelTimeRemaining(double optParcelTimeRemaining) {
		this.optParcelTimeRemaining = optParcelTimeRemaining;
	}
	public double getOptParcelUrgency() {
		return optParcelUrgency;
	}
	public void setOptParcelUrgency(double optParcelUrgency) {
		this.optParcelUrgency = optParcelUrgency;
	}
	public double getParcelODDistance() {
		return parcelODDistance;
	}
	public void setParcelODDistance(double parcelODDistance) {
		this.parcelODDistance = parcelODDistance;
	}
	public double getAngleBetween() {
		return angleBetween;
	}
	public void setAngleBetween(double angleBetween) {
		this.angleBetween = angleBetween;
	}
	public double getIncremenTime() {
		return incremenTime;
	}
	public void setIncremenTime(double incremenTime) {
		this.incremenTime = incremenTime;
	}
	
}
