package com.mckinsey.ckc.sf.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "carrier_record")
public class CarrierRecords {
	
	@Column(name = "timeID", unique = false, nullable = false)
	private int timeID;
	
	@Column(name = "carrierID", unique = false, nullable = false)
	private int carrierID;
	
	@Column(name = "groupID", unique = false, nullable = false)
	private int groupID;
	
	private Coordinate currentPos;
	private Coordinate destPos;
	
	@Column(name = "distanceTraveled", unique = false, nullable = false)
	private double distanceTraveled;
	
	@Column(name = "parcelType", unique = false, nullable = false)
	private int parcelType;
	
	@Column(name = "parcelVolume", unique = false, nullable = false)
	private double parcelVolume;
	
	@Column(name = "pickupCount", unique = false, nullable = false)
	private int pickupCount;
	
	@Column(name = "dropoffCount", unique = false, nullable = false)
	private int dropoffCount;
	
	@Column(name = "currentLong", unique = false, nullable = false)
	private double currentLong;
	
	@Column(name = "currentLat", unique = false, nullable = false)
	private double currentLat;
	
	@Column(name = "destLong", unique = false, nullable = false)
	private double destLong;
	
	@Column(name = "destLat", unique = false, nullable = false)
	private double destLat;
	
	public CarrierRecords(int timeID, int carrierID, int groupID, Coordinate currentPos, Coordinate destPos,
			double distanceTraveled, int parcelType, double parcelVolume, int pickupCount, int dropoffCount) {
		super();
		this.timeID = timeID;
		this.carrierID = carrierID;
		this.groupID = groupID;
		this.currentPos = currentPos;
		this.destPos = destPos;
		this.distanceTraveled = distanceTraveled;
		this.parcelType = parcelType;
		this.parcelVolume = parcelVolume;
		this.pickupCount = pickupCount;
		this.dropoffCount = dropoffCount;
	}
	
	public double getCurrentLong() {
		return currentPos.getX();
	}

	public void setCurrentLong(double currentLong) {
		this.currentLong = currentLong;
	}

	public double getCurrentLat() {
		return currentPos.getY();
	}

	public void setCurrentLat(double currentLat) {
		this.currentLat = currentLat;
	}

	public double getDestLong() {
		return destPos.getX();
	}

	public void setDestLong(double destLong) {
		this.destLong = destLong;
	}

	public double getDestLat() {
		return destPos.getY();
	}

	public void setDestLat(double destLat) {
		this.destLat = destLat;
	}

	public int getTimeID() {
		return timeID;
	}
	public void setTimeID(int timeID) {
		this.timeID = timeID;
	}
	public int getCarrierID() {
		return carrierID;
	}
	public void setCarrierID(int carrierID) {
		this.carrierID = carrierID;
	}
	public int getGroupID() {
		return groupID;
	}
	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}
	public Coordinate getCurrentPos() {
		return currentPos;
	}
	public void setCurrentPos(Coordinate currentPos) {
		this.currentPos = currentPos;
		this.currentLong = currentPos.getX();
		this.currentLat = currentPos.getY();
	}
	public Coordinate getDestPos() {
		return destPos;
	}
	public void setDestPos(Coordinate destPos) {
		this.destPos = destPos;
		this.destLong = destPos.getX();
		this.destLat = destPos.getY();
	}
	public double getDistanceTraveled() {
		return distanceTraveled;
	}
	public void setDistanceTraveled(double distanceTraveled) {
		this.distanceTraveled = distanceTraveled;
	}
	public int getParcelType() {
		return parcelType;
	}
	public void setParcelType(int parcelType) {
		this.parcelType = parcelType;
	}
	public double getParcelVolume() {
		return parcelVolume;
	}
	public void setParcelVolume(double parcelVolume) {
		this.parcelVolume = parcelVolume;
	}
	public int getPickupCount() {
		return pickupCount;
	}
	public void setPickupCount(int pickupCount) {
		this.pickupCount = pickupCount;
	}
	public int getDropoffCount() {
		return dropoffCount;
	}
	public void setDropoffCount(int dropoffCount) {
		this.dropoffCount = dropoffCount;
	}
	
	

}
