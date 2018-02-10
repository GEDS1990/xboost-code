package com.mckinsey.ckc.sf.restful.data;

import java.util.List;

import com.mckinsey.ckc.sf.data.CarrierRecords;

public class DataVModel {  
	  
//    private List<CarrierRecords> recordList;  
    private double currentDelivery;
    private double currentOnTimeDelivery;
    private int recentOneHourDemand;
    private int nCarrier;
    private int nSwotCarrier;
    
    private double undemand;
    private double unattended;
    private double pickup;
    
    private double presure;
    
    public double getPresure() {
		return presure;
	}

	public void setPresure(double presure) {
		this.presure = presure;
	}

	public double getUndemand() {
		return undemand;
	}

	public void setUndemand(double undemand) {
		this.undemand = undemand;
	}

	public double getUnattended() {
		return unattended;
	}

	public void setUnattended(double unattended) {
		this.unattended = unattended;
	}

	public double getPickup() {
		return pickup;
	}

	public void setPickup(double pickup) {
		this.pickup = pickup;
	}

	public int getRecentOneHourDemand() {
		return recentOneHourDemand;
	}

	public void setRecentOneHourDemand(int recentOneHourDemand) {
		this.recentOneHourDemand = recentOneHourDemand;
	}

	public DataVModel(){
    	
    }
    
    public int getnCarrier() {
		return nCarrier;
	}

	public void setnCarrier(int nCarrier) {
		this.nCarrier = nCarrier;
	}

	public int getnSwotCarrier() {
		return nSwotCarrier;
	}

	public void setnSwotCarrier(int nSwotCarrier) {
		this.nSwotCarrier = nSwotCarrier;
	}

	//	public List<CarrierRecords> getRecordList() {
//		return recordList;
//	}
//	public void setRecordList(List<CarrierRecords> recordList) {
//		this.recordList = recordList;
//	}
	public double getCurrentDelivery() {
		return currentDelivery;
	}
	public void setCurrentDelivery(double currentDelivery) {
		this.currentDelivery = currentDelivery;
	}
	public double getCurrentOnTimeDelivery() {
		return currentOnTimeDelivery;
	}
	public void setCurrentOnTimeDelivery(double currentOnTimeDelivery) {
		this.currentOnTimeDelivery = currentOnTimeDelivery;
	}
//
//	public void addRecord(CarrierRecords currentRecord) {
//		this.recordList.add(currentRecord);
//		
//	}
    
}
