package com.mckinsey.ckc.sf.data;

public class Parcel {
	
	private int parcelID;
	private int currentCarrierID;
	private double parcelVolumn;
	private Coordinate origPos;
	private Coordinate destPos;
	private Coordinate currPos;
	private double minTimeToDest;
	private int timeId;
	private int parcelDeliverStatus;
	private int deadline;
	private int deliverTime;
	private int pickupTime;
	private int groupID;
	private double parcelTheta;
	
	//for hub mode
	private int parcel_group_id ;
	private int origin_center_id ;
	private int dest_center_id ;
	private int shipping_time;
	private Coordinate origCenterPos;
	private Coordinate destCenterPos;
	private int origin_deactivate_time ;
	private int origin_activate_time ;
	private int dest_activate_time;
	private int dest_deactivate_time ;
	
	private int preParcelGroupId;
	private Coordinate parcelRealDestPos;
	private Coordinate parcelRealOriginPos;
	private int parcelRealTimeId;

	public Parcel(int parcelID, int currentCarrierID, double parcelVolumn, Coordinate origPos, Coordinate destPos,
			Coordinate currPos, double minTimeToDest, int timeId, int parcelDeliverStatus) {
		super();
		this.parcelID = parcelID;
		this.currentCarrierID = currentCarrierID;
		this.parcelVolumn = parcelVolumn;
		this.origPos = origPos;
		this.destPos = destPos;
		this.currPos = currPos;
		this.minTimeToDest = minTimeToDest;
		this.timeId = timeId;
		this.parcelDeliverStatus = parcelDeliverStatus;
		this.preParcelGroupId = 0;
		this.origin_center_id = 0;
		this.dest_center_id = 0;
		this.origin_activate_time = 0;
		this.shipping_time = 0;
		this.origCenterPos = new Coordinate(0,0);
		this.destCenterPos = new Coordinate(0,0);
		this.origin_deactivate_time = 0;
		this.dest_activate_time = 0;
		this.dest_deactivate_time = 0;
	}
	
	
	public double getParcelTheta() {
		return parcelTheta;
	}
	public void setParcelTheta(double parcelTheta) {
		this.parcelTheta = parcelTheta;
	}
	public int getParcelID() {
		return parcelID;
	}
	public void setParcelID(int parcelID) {
		this.parcelID = parcelID;
	}
	public int getCurrentCarrierID() {
		return currentCarrierID;
	}
	public void setCurrentCarrierID(int currentCarrierID) {
		this.currentCarrierID = currentCarrierID;
	}
	public double getParcelVolumn() {
		return parcelVolumn;
	}
	public void setParcelVolumn(double parcelVolumn) {
		this.parcelVolumn = parcelVolumn;
	}
	public Coordinate getOrigPos() {
		return origPos;
	}
	public void setOrigPos(Coordinate origPos) {
		this.origPos.setX(origPos.getX());
		this.origPos.setY(origPos.getY());
	}
	public Coordinate getDestPos() {
		return destPos;
	}
	public void setDestPos(Coordinate destPos) {
		this.destPos.setX(destPos.getX());
		this.destPos.setY(destPos.getY());
	}
	public Coordinate getCurrPos() {
		return currPos;
	}
	public void setCurrPos(Coordinate currPos) {
		this.currPos.setX(currPos.getX());
		this.currPos.setY(currPos.getY());
	}
	public double getMinTimeToDest() {
		return minTimeToDest;
	}
	public void setMinTimeToDest(double minTimeToDest) {
		this.minTimeToDest = minTimeToDest;
	}
	public int getTimeId() {
		return timeId;
	}
	public void setTimeId(int timeId) {
		this.timeId = timeId;
	}
	public int getParcelDeliverStatus() {
		return parcelDeliverStatus;
	}
	public void setParcelDeliverStatus(int parcelDeliverStatus) {
		this.parcelDeliverStatus = parcelDeliverStatus;
	}
	public int getDeadline() {
		return deadline;
	}
	public void setDeadline(int deadline) {
		this.deadline = deadline;
	}
	public int getGroupID() {
		return groupID;
	}
	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}
	public int getDeliverTime() {
		return deliverTime;
	}
	public void setDeliverTime(int deliverTime) {
		this.deliverTime = deliverTime;
	}
	public int getPickupTime() {
		return pickupTime;
	}
	public void setPickupTime(int pickupTime) {
		this.pickupTime = pickupTime;
	}
	
	
	public String toString(){
		return "parcel id:"+parcelID+", current carrier id:"+currentCarrierID+", volume is :"+parcelVolumn+"minTimeToDest:"+minTimeToDest;
	}


	public int getParcel_group_id() {
		return parcel_group_id;
	}


	public void setParcel_group_id(int parcel_group_id) {
		this.parcel_group_id = parcel_group_id;
	}


	public int getOrigin_center_id() {
		return origin_center_id;
	}


	public void setOrigin_center_id(int origin_center_id) {
		this.origin_center_id = origin_center_id;
	}


	public int getDest_center_id() {
		return dest_center_id;
	}


	public void setDest_center_id(int dest_center_id) {
		this.dest_center_id = dest_center_id;
	}


	public int getShipping_time() {
		return shipping_time;
	}


	public void setShipping_time(int shipping_time) {
		this.shipping_time = shipping_time;
	}


	public Coordinate getOrigCenterPos() {
		return origCenterPos;
	}


	public void setOrigCenterPos(Coordinate origCenterPos) {
		if(this.origCenterPos == null)
			this.origCenterPos = new Coordinate();
		this.origCenterPos.setX(origCenterPos.getX());
		this.origCenterPos.setY(origCenterPos.getY());
	}


	public Coordinate getDestCenterPos() {
		return destCenterPos;
	}


	public void setDestCenterPos(Coordinate origDestPos) {
		if(this.destCenterPos == null)
			this.destCenterPos = new Coordinate();
		this.destCenterPos.setX(origDestPos.getX());
		this.destCenterPos.setY(origDestPos.getY());
	}


	public int getOrigin_deactivate_time() {
		return origin_deactivate_time;
	}


	public void setOrigin_deactivate_time(int origin_deactivate_time) {
		this.origin_deactivate_time = origin_deactivate_time;
	}


	public int getOrigin_activate_time() {
		return origin_activate_time;
	}


	public void setOrigin_activate_time(int origin_activate_time) {
		this.origin_activate_time = origin_activate_time;
	}


	public int getDest_activate_time() {
		return dest_activate_time;
	}


	public void setDest_activate_time(int dest_activate_time) {
		this.dest_activate_time = dest_activate_time;
	}


	public int getDest_deactivate_time() {
		return dest_deactivate_time;
	}


	public void setDest_deactivate_time(int dest_deactivate_time) {
		this.dest_deactivate_time = dest_deactivate_time;
	}


	public int getPreParcelGroupId() {
		return preParcelGroupId;
	}


	public void setPreParcelGroupId(int preParcelGroupId) {
		this.preParcelGroupId = preParcelGroupId;
	}


	public Coordinate getParcelRealDestPos() {
		return parcelRealDestPos;
	}


	public void setParcelRealDestPos(Coordinate parcelRealDestPos) {
		if(this.parcelRealDestPos == null)
			this.parcelRealDestPos = new Coordinate();
		this.parcelRealDestPos.setX(parcelRealDestPos.getX());
		this.parcelRealDestPos.setY(parcelRealDestPos.getY());
	}


	public Coordinate getParcelRealOriginPos() {
		return parcelRealOriginPos;
	}


	public void setParcelRealOriginPos(Coordinate parcelRealOriginPos) {
		if(this.parcelRealOriginPos == null){
			this.parcelRealOriginPos = new Coordinate();
		}
		this.parcelRealOriginPos.setX(parcelRealOriginPos.getX());
		this.parcelRealOriginPos.setY(parcelRealOriginPos.getY());
	}


	public int getParcelRealTimeId() {
		return parcelRealTimeId;
	}


	public void setParcelRealTimeId(int parcelRealTimeId) {
		this.parcelRealTimeId = parcelRealTimeId;
	}

}
