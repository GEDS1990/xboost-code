package com.mckinsey.sf.data.solution;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mckinsey.sf.data.Car;
import com.mckinsey.sf.data.Job;

/**
 * 
 * Author：Alivia Chen 
 * Email : alivia_chen@mckinsey.com 
 * Date ：May 2, 2017
 * 
 * @version
 */
public class RouteJson {

	@JsonProperty("id")
	private String id;

	@JsonProperty("car")
	private Car c;

	@JsonProperty("jobs")
	private Job[] jobs;

	@JsonProperty("acts")
	private List<String> acts;
	
//	@JsonProperty("totalVolume")
//	private double totalVolume;

	public RouteJson(String id, Car c, Job[] jobs, List<String> acts) {
		super();
		this.id = id;
		this.c = c;
		this.jobs = jobs;
		this.acts = acts;
//		this.totalVolume = totalVolume;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Car getC() {
		return c;
	}

	public void setC(Car c) {
		this.c = c;
	}

	public Job[] getJobs() {
		return jobs;
	}

	public void setJobs(Job[] jobs) {
		this.jobs = jobs;
	}

	public List<String> getActs() {
		return acts;
	}

	public void setActs(List<String> acts) {
		this.acts = acts;
	}

//	public double getTotalVolume() {
//		return totalVolume;
//	}
//
//	public void setTotalVolume(double totalVolume) {
//		this.totalVolume = totalVolume;
//	}

}
