package com.mckinsey.sf.data;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mckinsey.sf.constants.IConstants;

/**
 * 
 * Author：Alivia Chen 
 * Email : alivia_chen@mckinsey.com 
 * Date ：May 2, 2017
 * 
 * @version
 */
public class ActState implements IConstants,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 480645552942756742L;

	@JsonProperty("arr_time")
	private double arrTime;

	@JsonProperty("end_time")
	private double endTime;

	@JsonProperty("wait_time")
	private double waitTime;

	@JsonProperty("capacity")
	private double[] capacity;
	

	public ActState() {
		super();
	}
	
	public ActState(int l) {
		capacity = new double[l];
	}

	public double getArrTime() {
		return arrTime;
	}

	public void setArrTime(double arrTime) {
		this.arrTime = arrTime;
	}

	public double getEndTime() {
		return endTime;
	}

	public void setEndTime(double endTime) {
		this.endTime = endTime;
	}

	public double getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(double waitTime) {
		this.waitTime = waitTime;
	}

	public double[] getCapacity() {
		return capacity;
	}

	public void setCapacity(double[] capacity) {
		this.capacity = capacity;
	}

	public void calcCapacity(ActState prevStat, Activity act, Job j, double[] maxDim) {
		for(int i=0; i< j.getDimensions().length; i++){
			switch(act.getType()){
			case "PICKUP":
				this.capacity[i] = prevStat.capacity[i]+j.getDimensions()[i];
				break;
			case "DELIVERY":
				this.capacity[i] = prevStat.capacity[i]-j.getDimensions()[i];
				break;
			}
			maxDim[i] = Math.max(maxDim[i], j.getDimensions()[i]);
		}
		
	}

	public void calcArrTime(ActState prevStat, double travelTime) {
		this.arrTime = prevStat.endTime + travelTime;
		
	}

	public void calcEndTime(double serviceTime, double ealiestStartTime) {
		if(ealiestStartTime > this.arrTime){
			this.waitTime = ealiestStartTime - this.arrTime;
		}
		
		this.endTime = this.arrTime + this.waitTime + serviceTime;
		
	}

	public boolean calcAndCheckCapacity(ActState prevStat, Activity act, Job j, Car c) {
//		System.err.println("prevState volume: "+prevStat.getCapacity()[0]);
////		if(prevStat.getCapacity()[0] <0 ){
////			prevStat.getCapacity()[0] = -prevStat.getCapacity()[0];
////		}
//		System.err.println("current job volume: "+j.getDimensions()[0]);
//		System.err.println("car dimension: "+c.getDimensions()[0]);
//		System.err.println("type: "+act.getType());
//		for(int i=0;i < j.getDimensions().length ; i++){
			switch(act.getType()){
			case "PICKUP":
				capacity[0] = prevStat.getCapacity()[0] + j.getDimensions()[0];
				break;
			case "DELIVERY":
				capacity[0] = prevStat.getCapacity()[0] - j.getDimensions()[0];
				break;
			}
//			if(capacity[0] < 0){
//				capacity[0] = 0;
//			}
			
			if(capacity[0] > c.getDimensions()[0]){
//				System.err.println("false");
				return false;
			}
//		}
//		System.err.println("true");
		return true;
	}

}
