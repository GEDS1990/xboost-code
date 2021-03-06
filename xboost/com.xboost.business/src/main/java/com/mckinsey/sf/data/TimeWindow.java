package com.mckinsey.sf.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 
 * Author：Alivia Chen 
 * Email : alivia_chen@mckinsey.com 
 * Date ：Apr 28, 2017
 * 
 * @version
 */
public class TimeWindow implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7020617863832427292L;

	@JsonProperty("start")
	private double start;

	@JsonProperty("end")
	private double end;

	private int carId;
	
	public TimeWindow(){
		
	}
	
	public TimeWindow(double start,double end){
		this.start = start;
		this.end = end;
	}

	public double getStart() {
		return start;
	}

	public void setStart(double start) {
		this.start = start;
	}

	public double getEnd() {
		return end;
	}

	public void setEnd(double end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return start + "-" + end;
	}

	public int getCarId() {
		return carId;
	}

	public void setCarId(int carId) {
		this.carId = carId;
	}
}
