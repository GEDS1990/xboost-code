package com.mckinsey.ckc.sf.data;

public class GroupInfo {
	private int preGroupId;
	private Coordinate originCenterPos;
	private Coordinate destCenterPos;
	private int originActivateTime;
	private int originDeactivateTime;
	private int destActivateTime;
	private int destDeactivateTime;
	private int shippingTime;
	private double volume;
	private int groupNCarrier;
	
	
	public GroupInfo(int preGroupId, Coordinate originCenterPos, Coordinate destCenterPos, int originActivateTime,
			int originDeactivateTime, int destActivateTime, int destDeactivateTime, int shippingTime, double volume,
			int groupNCarrier) {
		super();
		this.preGroupId = preGroupId;
		this.originCenterPos = originCenterPos;
		this.destCenterPos = destCenterPos;
		this.originActivateTime = originActivateTime;
		this.originDeactivateTime = originDeactivateTime;
		this.destActivateTime = destActivateTime;
		this.destDeactivateTime = destDeactivateTime;
		this.shippingTime = shippingTime;
		this.volume = volume;
		this.groupNCarrier = groupNCarrier;
	}
	
	public int getPreGroupId() {
		return preGroupId;
	}
	public void setPreGroupId(int preGroupId) {
		this.preGroupId = preGroupId;
	}
	public Coordinate getOriginCenterPos() {
		return originCenterPos;
	}
	public void setOriginCenterPos(Coordinate originCenterPos) {
		this.originCenterPos = originCenterPos;
	}
	public Coordinate getDestCenterPos() {
		return destCenterPos;
	}
	public void setDestCenterPos(Coordinate destCenterPos) {
		this.destCenterPos = destCenterPos;
	}
	public int getOriginActivateTime() {
		return originActivateTime;
	}
	public void setOriginActivateTime(int originActivateTime) {
		this.originActivateTime = originActivateTime;
	}
	public int getOriginDeactivateTime() {
		return originDeactivateTime;
	}
	public void setOriginDeactivateTime(int originDeactivateTime) {
		this.originDeactivateTime = originDeactivateTime;
	}
	public int getDestActivateTime() {
		return destActivateTime;
	}
	public void setDestActivateTime(int destActivateTime) {
		this.destActivateTime = destActivateTime;
	}
	public int getDestDeactivateTime() {
		return destDeactivateTime;
	}
	public void setDestDeactivateTime(int destDeactivateTime) {
		this.destDeactivateTime = destDeactivateTime;
	}
	public int getShippingTime() {
		return shippingTime;
	}
	public void setShippingTime(int shippingTime) {
		this.shippingTime = shippingTime;
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}
	public int getGroupNCarrier() {
		return groupNCarrier;
	}
	public void setGroupNCarrier(int groupNCarrier) {
		this.groupNCarrier = groupNCarrier;
	}
	
}
