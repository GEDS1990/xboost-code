package com.mckinsey.ckc.sf.data;

public class Points {
	private int pointID;
	private Coordinate position;
	private double initialVolume;
	private double pointWeight;
	
	
	
	public Points() {
		super();
	}

	public Points(int pointID, double pointLong, double pointLat, double initialVolume, double pointWeight) {
		super();
		this.pointID = pointID;
		this.position = new Coordinate(pointLong,pointLat);
		this.initialVolume = initialVolume;
		this.pointWeight = pointWeight;
	}
	
	public int getPointID() {
		return pointID;
	}
	public void setPointID(int pointID) {
		this.pointID = pointID;
	}
	public double getInitialVolume() {
		return initialVolume;
	}
	public void setInitialVolume(double initialVolume) {
		this.initialVolume = initialVolume;
	}
	public double getPointWeight() {
		return pointWeight;
	}
	public void setPointWeight(double pointWeight) {
		this.pointWeight = pointWeight;
	}

	public Coordinate getPosition() {
		return position;
	}
	public void setPosition(Coordinate position) {
		this.position = position;
	}

}
