package com.mckinsey.ckc.sf.data;

public class OD_Demand {

    int timeId;
    int inboundDepotId;
    double inboundLong;
    double inboundLat;
    int outboundDepotId;
    double outboundLong;
    double outboundLat;
    
	public OD_Demand(int timeId, int inboundDepotId, double inboundLong, double inboundLat, int outboundDepotId,
			double outboundLong, double outboundLat) {
		super();
		this.timeId = timeId;
		this.inboundDepotId = inboundDepotId;
		this.inboundLong = inboundLong;
		this.inboundLat = inboundLat;
		this.outboundDepotId = outboundDepotId;
		this.outboundLong = outboundLong;
		this.outboundLat = outboundLat;
	}
	public int getTimeId() {
		return timeId;
	}
	public void setTimeId(int timeId) {
		this.timeId = timeId;
	}
	public int getInboundDepotId() {
		return inboundDepotId;
	}
	public void setInboundDepotId(int inboundDepotId) {
		this.inboundDepotId = inboundDepotId;
	}
	public double getInboundLong() {
		return inboundLong;
	}
	public void setInboundLong(double inboundLong) {
		this.inboundLong = inboundLong;
	}
	public double getInboundLat() {
		return inboundLat;
	}
	public void setInboundLat(double inboundLat) {
		this.inboundLat = inboundLat;
	}
	public int getOutboundDepotId() {
		return outboundDepotId;
	}
	public void setOutboundDepotId(int outboundDepotId) {
		this.outboundDepotId = outboundDepotId;
	}
	public double getOutboundLong() {
		return outboundLong;
	}
	public void setOutboundLong(double outboundLong) {
		this.outboundLong = outboundLong;
	}
	public double getOutboundLat() {
		return outboundLat;
	}
	public void setOutboundLat(double outboundLat) {
		this.outboundLat = outboundLat;
	}
    
}