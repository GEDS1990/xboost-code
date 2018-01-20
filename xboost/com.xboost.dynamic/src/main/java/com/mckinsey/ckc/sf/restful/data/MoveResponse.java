package com.mckinsey.ckc.sf.restful.data;

import java.util.List;

import com.mckinsey.ckc.sf.data.Parcel;

public class MoveResponse {
	
	public double nextLong;
	public double nextLat;
	public List<Parcel> pickupList;
	public double spendTime;
	public double getNextLong() {
		return nextLong;
	}
	public void setNextLong(double nextLong) {
		this.nextLong = nextLong;
	}
	public double getNextLat() {
		return nextLat;
	}
	public void setNextLat(double nextLat) {
		this.nextLat = nextLat;
	}
	public List<Parcel> getPickupList() {
		return pickupList;
	}
	public void setPickupList(List<Parcel> pickupList) {
		this.pickupList = pickupList;
	}
	public double getSpendTime() {
		return spendTime;
	}
	public void setSpendTime(double spendTime) {
		this.spendTime = spendTime;
	}
	
}
