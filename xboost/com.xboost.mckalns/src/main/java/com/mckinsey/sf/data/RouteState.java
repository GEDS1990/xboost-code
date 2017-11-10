package com.mckinsey.sf.data;

import java.io.Serializable;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * Author：Alivia Chen 
 * Email : alivia_chen@mckinsey.com 
 * Date ：May 2, 2017
 * 
 * @version
 */
public class RouteState implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6168651104025817104L;

	@JsonProperty("total_cost")
	private double totalCost;

	@JsonProperty("total_stops")
	private int totalStops;

	@JsonProperty("total_distance")
	private double totalDist;

	@JsonProperty("total_time")
	private double totalTime;

	@JsonProperty("act_states")
	private HashMap<String, ActState> actStates;

	@JsonProperty("max_dim")
	private double[] maxDim;

	@JsonProperty("max_arrival")
	private double maxArr;

//	@JsonProperty("total_volume")
//	private double totalVolume;
	
	public RouteState() {

	}

	public RouteState(int length) {
		this.totalCost = 0;
		this.totalStops = 0;
		this.totalDist = 0;
		this.totalTime = 0;
		this.actStates = new HashMap<String, ActState>();
		this.maxDim = new double[length];
		this.maxArr = 0;
	}

	
//	public double getTotalVolume() {
//		return totalVolume;
//	}
//
//	public void setTotalVolume(double totalVolume) {
//		this.totalVolume = totalVolume;
//	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public int getTotalStops() {
		return totalStops;
	}

	public void setTotalStops(int totalStops) {
		this.totalStops = totalStops;
	}

	public double getTotalDist() {
		return totalDist;
	}

	public void setTotalDist(double totalDist) {
		this.totalDist = totalDist;
	}

	public double getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(double totalTime) {
		this.totalTime = totalTime;
	}

	public HashMap<String, ActState> getActStates() {
		return actStates;
	}

	public void setActStates(HashMap<String, ActState> actStates) {
		this.actStates = actStates;
	}

	public double[] getMaxDim() {
		return maxDim;
	}

	public void setMaxDim(double[] maxDim) {
		this.maxDim = maxDim;
	}

	public double getMaxArr() {
		return maxArr;
	}

	public void setMaxArr(double maxArr) {
		this.maxArr = maxArr;
	}

	public void calcStops(String currLoc, String prevLoc) {
		if (!currLoc.equals(prevLoc)) {
			totalStops += 1;
		}

	}

	public ActState getActStat(String id) {
		if (actStates.containsKey(id)) {
			return actStates.get(id);
		} else {
			ActState initAct = new ActState(maxDim.length);
			actStates.put(id, initAct);
			return initAct;
		}
	}

}
