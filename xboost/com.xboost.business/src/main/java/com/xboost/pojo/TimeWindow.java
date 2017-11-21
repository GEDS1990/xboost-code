package com.xboost.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class TimeWindow implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7020617863832427292L;

	private double start;
	private double end;
	
	public TimeWindow(){
		
	}
	
	public TimeWindow(double start, double end){
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

}
