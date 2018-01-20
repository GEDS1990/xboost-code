package com.mckinsey.ckc.sf.data;

public class Result {
	private int speed;
	private double optImportance;
	private int nCarrier;
	private double nParcel;
	private double nParcelDelivered;
	private double parcelPerCarrier;
	private double avgWorkingHour;
	private double avgActualWorkingHour;
	private double avgParcelOnCarrier;
	private double avgShippingTime;
	private double onTimeRate;
	private double distancePerCarrier;
	private double totalDistance;
	
	
	public Result(int speed, double optImportance, int nCarrier, double nParcel, double nParcelDelivered,
			double parcelPerCarrier, double avgWorkingHour, double avgActualWorkingHour, double avgParcelOnCarrier,
			double avgShippingTime, double onTimeRate, double distancePerCarrier, double totalDistance) {
		super();
		this.speed = speed;
		this.optImportance = optImportance;
		this.nCarrier = nCarrier;
		this.nParcel = nParcel;
		this.nParcelDelivered = nParcelDelivered;
		this.parcelPerCarrier = parcelPerCarrier;
		this.avgWorkingHour = avgWorkingHour;
		this.avgActualWorkingHour = avgActualWorkingHour;
		this.avgParcelOnCarrier = avgParcelOnCarrier;
		this.avgShippingTime = avgShippingTime;
		this.onTimeRate = onTimeRate;
		this.distancePerCarrier = distancePerCarrier;
		this.totalDistance = totalDistance;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public double getOptImportance() {
		return optImportance;
	}
	public void setOptImportance(double optImportance) {
		this.optImportance = optImportance;
	}
	public int getnCarrier() {
		return nCarrier;
	}
	public void setnCarrier(int nCarrier) {
		this.nCarrier = nCarrier;
	}
	public double getnParcel() {
		return nParcel;
	}
	public void setnParcel(double nParcel) {
		this.nParcel = nParcel;
	}
	public double getnParcelDelivered() {
		return nParcelDelivered;
	}
	public void setnParcelDelivered(double nParcelDelivered) {
		this.nParcelDelivered = nParcelDelivered;
	}
	public double getParcelPerCarrier() {
		return parcelPerCarrier;
	}
	public void setParcelPerCarrier(double parcelPerCarrier) {
		this.parcelPerCarrier = parcelPerCarrier;
	}
	public double getAvgWorkingHour() {
		return avgWorkingHour;
	}
	public void setAvgWorkingHour(double avgWorkingHour) {
		this.avgWorkingHour = avgWorkingHour;
	}
	public double getAvgActualWorkingHour() {
		return avgActualWorkingHour;
	}
	public void setAvgActualWorkingHour(double avgActualWorkingHour) {
		this.avgActualWorkingHour = avgActualWorkingHour;
	}
	public double getAvgParcelOnCarrier() {
		return avgParcelOnCarrier;
	}
	public void setAvgParcelOnCarrier(double avgParcelOnCarrier) {
		this.avgParcelOnCarrier = avgParcelOnCarrier;
	}
	public double getAvgShippingTime() {
		return avgShippingTime;
	}
	public void setAvgShippingTime(double avgShippingTime) {
		this.avgShippingTime = avgShippingTime;
	}
	public double getOnTimeRate() {
		return onTimeRate;
	}
	public void setOnTimeRate(double onTimeRate) {
		this.onTimeRate = onTimeRate;
	}
	public double getDistancePerCarrier() {
		return distancePerCarrier;
	}
	public void setDistancePerCarrier(double distancePerCarrier) {
		this.distancePerCarrier = distancePerCarrier;
	}
	public double getTotalDistance() {
		return totalDistance;
	}
	public void setTotalDistance(double totalDistance) {
		this.totalDistance = totalDistance;
	}

	

}
