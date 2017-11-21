package com.mckinsey.sf.data;

import java.io.Serializable;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * Author：Alivia Chen 
 * Email : alivia_chen@mckinsey.com 
 * Date ：Apr 27, 2017
 * 
 * @version
 */
public class Car implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3292204274815042073L;
	@JsonProperty("id")
	private String id;
	@JsonProperty("type")
	private String type;
	@JsonProperty("dimensions")
//	private double[] dimensions;
	private String dimensions;
	@JsonProperty("skills")
	private HashMap<String, Boolean> skills;
	@JsonProperty("time_window")
	private TimeWindow tw;
	@JsonProperty("start_location")
	private String startLocation;
	@JsonProperty("end_location")
	private String endLocation;
	@JsonProperty("max_distance")
	private double maxDistance;
	@JsonProperty("max_running_time")
	private double maxRunningTime;
	@JsonProperty("cost_per_distance")
	private double costPerDistance;
	@JsonProperty("cost_per_time")
	private double costPerTime;
	@JsonProperty("fixed_cost")
	private double fixedCost;
	private int maxStop;
	@JsonProperty("velocity")
	private double velocity;
	
	@JsonProperty("fixed_round")
//	private double[] fixedRound;
	private double fixedRound;
	
	@JsonProperty("fixed_round_fee")
//	private double[] fixedRoundFee;
	private double fixedRoundFee;

	public Car() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDimensions() {
		return dimensions;
	}

	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}

	public HashMap<String, Boolean> getSkills() {
		return skills;
	}

	public void setSkills(HashMap<String, Boolean> skills) {
		this.skills = skills;
	}

	public TimeWindow getTw() {
		return tw;
	}

	public void setTw(TimeWindow tw) {
		this.tw = tw;
	}

	public String getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(String startLocation) {
		this.startLocation = startLocation;
	}

	public String getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(String endLocation) {
		this.endLocation = endLocation;
	}

	public double getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(double maxDistance) {
		this.maxDistance = maxDistance;
	}

	public double getMaxRunningTime() {
		return maxRunningTime;
	}

	public void setMaxRunningTime(double maxRunningTime) {
		this.maxRunningTime = maxRunningTime;
	}

	public double getCostPerDistance() {
		return costPerDistance;
	}

	public void setCostPerDistance(double costPerDistance) {
		this.costPerDistance = costPerDistance;
	}

	public double getCostPerTime() {
		return costPerTime;
	}

	public void setCostPerTime(double costPerTime) {
		this.costPerTime = costPerTime;
	}

	public double getFixedCost() {
		return fixedCost;
	}

	public void setFixedCost(double fixedCost) {
		this.fixedCost = fixedCost;
	}

	public int getMaxStop() {
		return maxStop;
	}

	public void setMaxStop(int maxStop) {
		this.maxStop = maxStop;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public double getFixedRound() {
		return fixedRound;
	}

	public void setFixedRound(double fixedRound) {
		this.fixedRound = fixedRound;
	}

	public double getFixedRoundFee() {
		return fixedRoundFee;
	}

	public void setFixedRoundFee(double fixedRoundFee) {
		this.fixedRoundFee = fixedRoundFee;
	}
}
